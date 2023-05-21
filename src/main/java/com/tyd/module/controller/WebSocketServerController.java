package com.tyd.module.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tyd.common.constant.FwConstants;
import com.tyd.common.util.ApplicationContextUtil;
import com.tyd.common.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@ServerEndpoint(value = "/task/{userId}")
public class WebSocketServerController {

    private Session session;
    private static CopyOnWriteArraySet<WebSocketServerController> webSockets =new CopyOnWriteArraySet<>();
    private static Map<String,Session> sessionPool = new HashMap<String,Session>();



    @OnOpen
    public void onOpen(Session session, @PathParam(value="userId")String userId) {
        RedisUtils redisUtils = ApplicationContextUtil.getBean("redisUtils", RedisUtils.class);
        List<String> scan = redisUtils.scan("taskID*" + userId + "*");
        Map<String,Object> data =new HashMap<>();
        for (String s : scan) {
           redisUtils.expire(s,FwConstants.CacheTTL_Long*3);
            data.put(s,redisUtils.lGet(s,0,-1));
        }
        this.session = session;
        webSockets.add(this);
        sessionPool.put(userId, session);
        this.sendOneMessage(userId,JSON.toJSONString(data));
        log.info(userId+"【websocket消息】有新的连接，总数为:"+webSockets.size());
    }

    @OnClose
    public void onClose() {
        webSockets.remove(this);
        log.info("【websocket消息】连接断开，总数为:"+webSockets.size());
    }

    @OnMessage
    public void onMessage(String message) {
        RedisUtils redisUtils = ApplicationContextUtil.getBean("redisUtils", RedisUtils.class);
        JSONObject jsonObject = JSON.parseObject(message);
        System.out.println(jsonObject.get("user1"));
        System.out.println(jsonObject.get("user2"));
        Object data = jsonObject.get("data");
        String key = "taskID"+ jsonObject.get("user1")  + jsonObject.get("user2");
        List<String> messages = new ArrayList<>();
        System.out.println(key);
        messages.add(data.toString());
        redisUtils.lSet(key,messages);
        this.sendOneMessage(jsonObject.get("user2").toString(),JSON.toJSONString(data));
        log.info("【websocket消息】收到客户端消息:"+message);
    }

    @OnError
    public String onError(Throwable error){
        log.error(error.getMessage());
        return error.getMessage();
    }

    // 此为广播消息
    public void sendAllMessage(String message) {
        for(WebSocketServerController webSocket : webSockets) {
            System.out.println("【websocket消息】广播消息:"+message);
            try {
                webSocket.session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 此为单点消息
    public void sendOneMessage(String userId, String message) {
        System.out.println("【websocket消息】单点消息:"+message);
        Session session = sessionPool.get(userId);
        if (session != null) {
            try {
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}