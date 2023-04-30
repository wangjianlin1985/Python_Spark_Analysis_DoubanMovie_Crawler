package com.movie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class UserInfo {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String loginName;
    private String name;
    private String password;
    private int role;//0普通用户，1管理员
}
