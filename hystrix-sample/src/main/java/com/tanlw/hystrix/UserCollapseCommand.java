package com.tanlw.hystrix;

import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCommand;
import com.tanlw.service.UserService;
import com.tanlw.web.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author liwei.tan@baozun.com
 * @Date 2018/9/13 11:03
 */
//HystrixCollapser<BatchReturnType, ResponseType, RequestArgumentType>
public class UserCollapseCommand extends HystrixCollapser<List<User>, User, Long> {
    UserService userService;
    Long userId;

    public UserCollapseCommand(UserService userService, Long userId){
        this.userService = userService;
        this.userId = userId;
    }

    @Override
    public Long getRequestArgument() {
        return userId;
    }
//    protected abstract HystrixCommand<BatchReturnType>
// createCommand(Collection<CollapsedRequest<ResponseType, RequestArgumentType>> requests);
//    @Override
    protected HystrixCommand<List<User>> createCommand(Collection<CollapsedRequest<User, Long>> collapsedRequests){
        List<Long> userIds = new ArrayList<>(collapsedRequests.size());
        userIds.addAll(collapsedRequests.stream().map(CollapsedRequest::getArgument).collect(Collectors.toList()));
        return new UserBatchCommand(userService, userIds);
    }

    @Override
    protected void mapResponseToRequests(List<User> batchResponse, Collection<CollapsedRequest<User, Long>> collapsedRequests) {
        int count = 0;
        for (CollapsedRequest<User, Long > collapsedRequest : collapsedRequests) {
            User user = batchResponse.get(count++);
            collapsedRequest.setResponse(user);
        }
    }

}
