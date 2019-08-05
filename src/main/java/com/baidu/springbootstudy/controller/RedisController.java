package com.baidu.springbootstudy.controller;

import com.baidu.springbootstudy.entity.Person;
import com.baidu.springbootstudy.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@RestController
public class RedisController {
    @Autowired
    RedisTemplate redisTemplate;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @RequestMapping(method = RequestMethod.GET, value = "/redis/set/{val}")
    public String setByPath (@PathVariable("val")  String val) {
        redisTemplate.opsForValue().set("name:setByPath", val);
        return "success";
    }

    @RequestMapping(method = RequestMethod.GET, value = "redis/setByParam")
    public String setByParam (@RequestParam(value = "val", required = false, defaultValue = "default") String val) {
        redisTemplate.opsForValue().set("name:setByParam", val);
        return "success";
    }

    @RequestMapping(method = RequestMethod.GET, value = "redis/setUser")
    public String setObject (@RequestParam(value = "userName", required = false, defaultValue = "liuzuwei") String userName) {
        Person person = new Person();
        person.setUserName(userName);
        person.setPassword(MD5Utils.encrypt("123456"));
        person.setCreateTime(simpleDateFormat.format(new Date()));
        person.setId(new Random().nextInt(10000) + 1);
        person.setStatus("正常");
        redisTemplate.opsForValue().set("person", person);
        return person.toString();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/redis/get")
    public String get (String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }
}
