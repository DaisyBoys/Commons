package com.iooicc.GlobalNews.async.handler;

import com.iooicc.GlobalNews.async.EventHandler;
import com.iooicc.GlobalNews.async.EventModel;
import com.iooicc.GlobalNews.async.EventType;
import com.iooicc.GlobalNews.model.Message;
import com.iooicc.GlobalNews.model.User;
import com.iooicc.GlobalNews.service.MessageService;
import com.iooicc.GlobalNews.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by nowcoder on 2016/7/16.
 */
@Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(3);
        //message.setToId(model.getEntityOwnerId());
        message.setToId(model.getActorId());
        User user = userService.getUser(model.getActorId());
        message.setContent("用户" + user.getName()
                + "赞了你的资讯,http://127.0.0.1:8080/news/" + model.getEntityId());
        message.setCreatedDate(new Date());
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
