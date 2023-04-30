package com.movie.etl;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;

/**
 * 数据清洗，去掉字段数不对的脏数据
 * 去掉评分不对的数据
 * 去掉表头
 */
public class MapReduceETL {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        String[] otherArgs = new GenericOptionsParser(args).getRemainingArgs();
        if (otherArgs.length != 2) {
            System.out.println("参数不对，输入路径 and 输出路径.");
            System.exit(2);
        }

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        job.setJarByClass(MapReduceETL.class);
        job.setMapperClass(map.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);
        job.setReducerClass(reduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        for (int i = 0; i < otherArgs.length - 1; i++) {
            // 设置需要统计的文件的输入路径
            FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
        }
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length - 1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    public static class map extends Mapper<LongWritable, Text, Text, NullWritable> {

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            try {
                String[] arrs = value.toString().split(",");
                if (arrs.length != 6) {
                    System.out.println("字段数不对，脏数据跳过！");
                    return;
                }
                double score = Double.parseDouble(arrs[3]);
                context.write(value, NullWritable.get());
            } catch (Exception e) {
                System.out.println("评分格式不对，脏数据跳过！");
                return;
            }
        }
    }

    public static class reduce extends Reducer<Text, NullWritable, Text, NullWritable> {
        @Override
        protected void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            context.write(key, NullWritable.get());
        }
    }
}
