package com;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 统计影评时间统计分析数
 */
public class CommontNum {
    public static void main(String[] args) {
        SparkSession sparkSession = new SparkSession.Builder().master("local").appName("count year numbers").getOrCreate();
        JavaRDD<String> javaRDD = sparkSession.read().textFile("movie.csv").javaRDD();
        JavaRDD<String> yearRDD = javaRDD.map(line -> line.split(",")[4].substring(0,10));
        JavaPairRDD<String, Integer> javaPairRDD = yearRDD.mapToPair(word -> new Tuple2<>(word, 1));
        JavaPairRDD<String, Integer> reduceByKey = javaPairRDD.reduceByKey((x, y) -> x + y);
        reduceByKey.collect().forEach(System.out::println);
        JavaRDD<Row> rowJavaRDD = reduceByKey.map(x -> RowFactory.create(x._1, x._2));
        //数据库内容
        String url = "jdbc:mysql://localhost:3306/spark_movie?useUnicode=true&characterEncoding=utf8";
        Properties connectionProperties = new Properties();
        connectionProperties.put("user", "root");
        connectionProperties.put("password", "123456");
        connectionProperties.put("driver", "com.mysql.jdbc.Driver");
        /**
         * 第二步：动态构造DataFrame的元数据。
         */
        List structFields = new ArrayList();
        structFields.add(DataTypes.createStructField("year", DataTypes.StringType, true));
        structFields.add(DataTypes.createStructField("num", DataTypes.IntegerType, true));

        //构建StructType，用于最后DataFrame元数据的描述
        StructType structType = DataTypes.createStructType(structFields);

        /**
         * 第三步：基于已有的元数据以及RDD<Row>来构造DataFrame
         */
        Dataset<Row> dataFrame = sparkSession.createDataFrame(rowJavaRDD, structType);

        /**
         * 第四步：将数据写入到user表中
         */
        dataFrame.write().mode("append").jdbc(url, "commentnum", connectionProperties);

        //停止SparkContext
        sparkSession.stop();
    }
}
