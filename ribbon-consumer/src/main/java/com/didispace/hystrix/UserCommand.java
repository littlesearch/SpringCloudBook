package com.didispace.hystrix;

import com.didispace.web.User;
import com.netflix.hystrix.HystrixCommand;
import org.springframework.web.client.RestTemplate;

/**
 * Spring Cloud自定义Hystrix请求命令
 * https://blog.csdn.net/u012702547/article/details/78032191
 *
 * @author liwei.tan@baozun.com
 * @Date 2018/9/10 10:21
 */
public class UserCommand extends HystrixCommand<User> {

    private RestTemplate restTemplate;

    private Long id;
    public UserCommand(Setter setter, RestTemplate restTemplate, Long id) {
        super(setter);
        this.restTemplate = restTemplate;
        this.id = id;
    }
    @Override
    protected User run() throws Exception {
        //url路径 区分大小写的
        User forObject = restTemplate.getForObject("http://hello-service/users/{1}", User.class, id);
        return forObject;
    }

    @Override
    protected User getFallback(){
        return new User("Wo Ca",404);
    }
}
