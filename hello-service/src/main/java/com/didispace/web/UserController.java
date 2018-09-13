package com.didispace.web;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liwei.tan@baozun.com
 * @Date 2018/9/10 10:23
 */
@RestController
public class UserController {

    private static List<User> USERS = new ArrayList<>();
    static {
        USERS.add(new User("张三", 19));
        USERS.add(new User("李四", 12));
        USERS.add(new User("tom", 18));
        USERS.add(new User("jack", 28));
    }
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public User get(@PathVariable(value = "id") Long id){
        if (id < USERS.size()) {
            return USERS.get(id.intValue());
        }
        return null;
    }

    @RequestMapping(value="/users", method = RequestMethod.GET)
    public List<User> getList(@ModelAttribute(value = "ids") List<Integer> ids){
        List ret = new ArrayList();
        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i) < USERS.size()) {
                ret.add(USERS.get(ids.get(i)));
            }
        }
        return ret;
    }
}
