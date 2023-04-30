package com.movie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.movie.entity.UserInfo;
import com.movie.mapper.UserMapper;
import com.movie.utils.AccountValidatorUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Resource
    private UserMapper userMapper;

    @RequestMapping("/user/login")
    public String login(@RequestParam("loginName") String loginName, @RequestParam("password") String password, Model model, RedirectAttributes attributes, HttpSession session) {
        if (ObjectUtils.isEmpty(loginName) || ObjectUtils.isEmpty(password)) {
            model.addAttribute("login_msg", "关键信息不能为空。");
            return "login";
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("login_name", loginName);
        UserInfo user = userMapper.selectOne(queryWrapper);
        if (user == null || !user.getPassword().equals(password)) {
            model.addAttribute("login_msg", "用户名或密码错误。");
            return "login";
        } else {
            session.setAttribute("loginUser", user);
            return "redirect:/main.html";
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/main.html";
    }

    @PostMapping("/user/register")
    public String register(UserInfo user, @Param("confirmPassword") String confirmPassword, Model model) {
        if (user == null || ObjectUtils.isEmpty(user.getLoginName()) || ObjectUtils.isEmpty(user.getPassword()) || ObjectUtils.isEmpty(confirmPassword)) {
            model.addAttribute("register_msg", "关键信息不能为空，请输入！");
            return "register";
        }
        if (!AccountValidatorUtil.isMobile(user.getLoginName())) {
            model.addAttribute("register_msg", "账号只能是手机号！");
            return "register";
        }
        if (!AccountValidatorUtil.isPassword(user.getPassword())) {
            model.addAttribute("register_msg", "密码只能是6~20位的字母和数字。");
            return "register";
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("login_name", user.getLoginName());
        UserInfo oldUser = userMapper.selectOne(queryWrapper);
        if (oldUser != null) {
            model.addAttribute("register_msg", "账号已存在，换个手机试试！");
            return "register";
        }
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("register_msg", "两次密码不一致！");
        } else {
            userMapper.insert(user);
            model.addAttribute("register_msg", "注册成功，请登录。");
        }
        return "register";
    }


}
