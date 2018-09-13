package com.didispace.web;

import com.didispace.hystrix.UserCommand;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;

/**
 * @author liwei.tan@baozun.com
 * @Date 2018/9/10 10:31
 */
@RestController
public class ConsumerHytrix {
    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "hystrix/{id}", method = RequestMethod.GET)
    public User get(@PathVariable Long id) throws ExecutionException, InterruptedException {
       UserCommand userCommand = new UserCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("hystrix")),restTemplate,id);
       //同步调用
       return userCommand.execute();
       //异步调用
//        Future<User> queue = userCommand.queue();
//        return queue.get();
    }
}
