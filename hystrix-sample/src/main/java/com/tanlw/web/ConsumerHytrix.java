package com.tanlw.web;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.tanlw.hystrix.UserCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author liwei.tan@baozun.com
 * @Date 2018/9/10 10:31
 */
@RestController
public class ConsumerHytrix {
    @Autowired
    private RestTemplate restTemplate;
    private boolean inited = false;

    @RequestMapping(value = "hystrix/{id}", method = RequestMethod.GET)
    public User get(@PathVariable Long id) throws ExecutionException, InterruptedException {

        HystrixRequestContext.initializeContext();//初始化请求上下文
        UserCommand userCommand = new UserCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("hystrix")), restTemplate, id);
        //同步调用
        return userCommand.execute();
        //异步调用
//        Future<User> queue = userCommand.queue();
//        return queue.get();
    }


    @RequestMapping(value = "cache/{id}", method = RequestMethod.GET)
    public List<User> cache(@PathVariable Long id) throws ExecutionException, InterruptedException {

        //注释掉下行 报错：
        //java.lang.IllegalStateException: Request caching is not available. Maybe you need to initialize the HystrixRequestContext?
        //上下文又会初始化一次，前面缓存的就失效了 https://blog.csdn.net/lvyuan1234/article/details/76691112#commentBox
        HystrixRequestContext.initializeContext();//初始化请求上下文
        UserCommand userCommand = new UserCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("hystrix")), restTemplate, id);

        List<User> users = new ArrayList<>();
        User user = userCommand.execute();
        //重复调用走缓存
        UserCommand userCommand2 = new UserCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("hystrix")), restTemplate, id);
        User user2 = userCommand2.execute();

        users.add(user);
        users.add(user2);
        return users;
        //异步调用
//        Future<User> queue = userCommand.queue();
//        return queue.get();
    }

    @RequestMapping(value = "cache2/{id}", method = RequestMethod.GET)
    public List<User> cache2(@PathVariable Long id) throws ExecutionException, InterruptedException {

        //注释掉下面if分支 第二次请求是报错，
        //java.lang.IllegalStateException: Request caching is not available. Maybe you need to initialize the HystrixRequestContext?
        //也就是每次请求都要 初始化Hystrix上下文后 缓存生效

        if (!inited) {
            HystrixRequestContext.initializeContext();//初始化请求上下文
            inited = true;
        }

        List<User> users = new ArrayList<>();
        try {
            UserCommand userCommand = new UserCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("hystrix")), restTemplate, id);

            User user = userCommand.execute();
            //重复调用
            UserCommand userCommand2 = new UserCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("hystrix")), restTemplate, id);
            User user2 = userCommand2.execute();

            users.add(user);
            users.add(user2);
        } catch (IllegalStateException s) {
            return null;
        }
        return users;
        //异步调用
//        Future<User> queue = userCommand.queue();
//        return queue.get();
    }

}
