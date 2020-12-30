package site.wanjiahao.live.socket;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.wanjiahao.live.entity.UserEntity;
import site.wanjiahao.live.service.UserService;
import site.wanjiahao.live.vo.SocketMessageVo;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * ServerEndpoint
 * <p>
 * 使用springboot的唯一区别是要@Component声明下，而使用独立容器是由容器自己管理websocket的，但在springboot中连容器都是spring管理的。
 * <p>
 * 虽然@Component默认是单例模式的，但springboot还是会为每个websocket连接初始化一个bean，所以可以用一个静态set保存起来。
 */
@ServerEndpoint("/live/socket/{id}")
@Component
@Slf4j
public class WebSocketServer {

    public static UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        WebSocketServer.userService = userService;
    }
    /**
     * 存活的session集合（使用线程安全的map保存） 需要注意的是浏览器刷新页面并不会触发OnClose方法
     */
    public final static  CopyOnWriteArraySet<WebSocketServer> webSocketServerSet =  new CopyOnWriteArraySet<>();
    /**
     * 与某个客户端建立的回话
     */
    private Session session;
    /**
     * 接收的用户id
     */
    private String id;
    /**
     * 建立连接的回调方法
     * @param session 与客户端的WebSocket连接会话
     * @param id  用户id，WebSocket支持路径参数
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        // 加入当前回话
        this.session = session;
        // 加入当前实例与socket集合中
        webSocketServerSet.add(this);
        // id
        this.id = id;
    }

    @OnMessage
    public void onMessage(String message, @PathParam("userId") String userId) {
        // 校验消息的合法性
        SocketMessageVo socketMessageVo = JSON.parseObject(message, SocketMessageVo.class);
        // 查询当前用户是否存在
        UserEntity userEntity = userService.findById(socketMessageVo.getId());
        if (userEntity != null) {
            this.sendMessageToAll(message);
        }
    }


    @OnError
    public void onError(Throwable error) {
        log.info("发生错误");
        log.error(error.getMessage() + "");
        log.error(error.getCause() + "");
    }


    @OnClose
    public void onClose(@PathParam("userId") String userId) {
        // 移除当前实例
        webSocketServerSet.remove(this);
        log.info("关闭连接");
    }

    /**
     * 单独发送消息
     * @param message
     */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 群发消息
     * @param message
     */
    public void sendMessageToAll(String message) {
        for (WebSocketServer webSocketServer : webSocketServerSet) {
            webSocketServer.sendMessage(message);
        }
    }
}