package com.movie.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("yearnum")
public class YearNum {
    private String year;
    private int num;
}
