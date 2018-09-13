package com.tanlw.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.tanlw.service.UserService;
import com.tanlw.web.User;

import java.util.List;

import static com.netflix.hystrix.HystrixCommandGroupKey.Factory.asKey;

/**
 * @author liwei.tan@baozun.com
 * @Date 2018/9/13 10:59
 */
public class UserBatchCommand extends HystrixCommand<List<User>> {
    UserService userService;
    List<Long> userIds;
    public UserBatchCommand(UserService userService, List<Long> userIds){
        super(Setter.withGroupKey(asKey("userServiceCommand")));
        this.userService = userService;
        this.userIds = userIds;
    }
    @Override
    protected List<User> run() throws Exception {
        return userService.findAll(userIds);
    }
}
