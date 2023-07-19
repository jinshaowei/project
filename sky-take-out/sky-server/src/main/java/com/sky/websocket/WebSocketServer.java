package com.sky.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket 入门
 */
@Slf4j
@Component
@ServerEndpoint("/ws/{sid}")  //标识当前类就是处理webSocket请求  //处理WebSocket请求
public class WebSocketServer {

    //将每一个连接的对象存储起来
    private static Map<String,Session> sessionMap = new HashMap<>();

    //该方法在连接建立时触发
    @OnOpen           // 获取一个webSocket会话对象,      可以获取客户端id（类似spring中的路径参数）
    public void onOpen(Session session, @PathParam("sid") String sid){
        log.info("连接建立");
        //键为客户端id，值为每一个会话对象
        sessionMap.put(sid,session);

    }

    //接收到客户端消息触发，可以获取客户端消息
    @OnMessage
    public void onMessage(Session session, String message , @PathParam("sid") String sid){
        log.info("接收消息：{}", message);

    }

    //连接关闭时触发
    @OnClose
    public void onClose(Session session, @PathParam("sid") String sid){
        log.info("连接关闭：{}");
        sessionMap.remove(sid);
    }

    //通信异常时触发
    @OnError
    public void onError(Session session, @PathParam("sid") String sid, Throwable throwable ){
        //可以拿到异常消息
        log.info("通信异常：{}", throwable);
    }


    //创建一个方法进行群发消息
    public void sendMessageToAllClient(String message) throws Exception {
        //获取集合所有的会话对象
        Collection<Session> sessions = sessionMap.values();
        if (!CollectionUtils.isEmpty(sessionMap)){
            //获取每一个会话对象
            for (Session session : sessions) {
                //发送消息
                session.getBasicRemote().sendText(message);
            }
        }
    }


}
