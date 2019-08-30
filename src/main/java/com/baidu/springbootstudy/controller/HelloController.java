package com.baidu.springbootstudy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RequestMapping(value = "/say", method = RequestMethod.GET)
    public String sayHello () {
        return "Hello World! 你好";
    }
}
