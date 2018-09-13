package com.tanlw.service;

import com.tanlw.web.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author liwei.tan@baozun.com
 * @Date 2018/9/13 10:56
 */
public class UserServiceImpl implements UserService {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public User find(Long id) {
        restTemplate.getForObject("http://hello-service/users/{1}", User.class, id);
        return null;
    }

    @Override
    public List<User> findAll(List<Long> ids) {
        return restTemplate.getForObject("http://hello-service/users?ids={1}", List.class, StringUtils.join(ids));
    }
}
