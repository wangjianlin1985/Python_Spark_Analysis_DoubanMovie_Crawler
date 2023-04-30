package com.movie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.movie.entity.*;
import com.movie.mapper.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class MovieController {
    @Resource
    private LvNumMapper lvNumMapper;
    @Resource
    private TypeMapper typeMapper;
    @Resource
    private YearNumMapper yearNumMapper;
    @Resource
    private WordsMapper wordsMapper;
    @Resource
    private CommentNumMapper commentNumMapper;

    @GetMapping
    @RequestMapping("/year")
    public Object year() {
        // 查询数据库数据
        QueryWrapper<YearNum> queryWrapper = new QueryWrapper<YearNum>();
        queryWrapper.orderByAsc("year");
        return yearNumMapper.selectList(queryWrapper);
    }

    @GetMapping
    @RequestMapping("/yeartop")
    public Object yeartop() {
        // 查询数据库数据
        QueryWrapper<YearNum> queryWrapper = new QueryWrapper<YearNum>();
        queryWrapper.orderByDesc("num");
        queryWrapper.last("limit 20");
        return yearNumMapper.selectList(queryWrapper);
    }


    @GetMapping
    @RequestMapping("/type")
    public Object type() {
        // 查询数据库数据
        return typeMapper.selectList(null);
    }

    @GetMapping
    @RequestMapping("/lvnum")
    public Object lvnum() {
        // 查询数据库数据
        return lvNumMapper.selectList(null);
    }

    @GetMapping
    @RequestMapping("/times")
    public Object times() {
        // 查询数据库数据
        QueryWrapper<CommentNum> c = new QueryWrapper<>();
        c.orderByDesc("num").last("limit 20");
        return commentNumMapper.selectList(c);
    }

    @GetMapping
    @RequestMapping("/words")
    public Object words() {
        QueryWrapper<Words> c = new QueryWrapper<>();
        c.orderByDesc("value").last("limit 20");
        // 查询数据库数据
        return wordsMapper.selectList(c);
    }
}
