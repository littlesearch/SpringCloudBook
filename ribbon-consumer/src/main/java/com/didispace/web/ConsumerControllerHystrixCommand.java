package com.didispace.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerControllerHystrixCommand {

    @Autowired
    HelloService helloService;

    @RequestMapping(value = "/ribbon-hystrix", method = RequestMethod.GET)
    public String helloConsumer() {
        return helloService.helloHytrix();
    }

}