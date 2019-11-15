package com.redPackets.demo.controller;

import com.redPackets.demo.pojo.ChatClientModel;
import com.redPackets.demo.pojo.ServerResponseModel;
import com.redPackets.demo.util.RedPacketUtil;
import com.redPackets.demo.websocket.SocketManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("red_packet")
public class RushRedPacketsController {

    private static final Logger logger = LogManager.getLogger(RushRedPacketsController.class);

    //同一時間的取得紅包id
    private ConcurrentMap<Long, Set<String>> userSessionIds;
    //放紅包list
    private ConcurrentLinkedDeque<Integer> redPacketList;
    //最後同時搶得取得者與未取得者
    private ConcurrentMap<String, Set<String>> lastredPacket;

    //初始化紅包數量金額
    @GetMapping("init/{count}/{sum}")
    public ConcurrentLinkedDeque init(@PathVariable int count, @PathVariable int sum) {
        RedPacketUtil redPacketUtil = new RedPacketUtil(count,sum);
        redPacketList = new ConcurrentLinkedDeque<>();
        userSessionIds = new ConcurrentHashMap();
        lastredPacket = new ConcurrentHashMap();
        //產生紅包
        for (int i = 0; i < redPacketUtil.getCount(); i++){
            int red = redPacketUtil.nextRedPacket();
            logger.info("第{}個紅包的金額:{}", i + 1, red);
            //存入回顯頁面map
            redPacketList.offer(red);
        }
        return  redPacketList;
    }

    /**
     * 實現搶紅包
     * 把sessionId做爲userId
     *
     */
    @GetMapping("get")
    public String get(HttpServletRequest request){
        userSessionIds.forEach((k,v) -> logger.info("系統時間:{}, 同時搶紅包有:{}",k,v));
        if(!lastredPacket.isEmpty()){
            logger.info("最後同時進入獲得紅包者 :",lastredPacket.get("get"));
            logger.info("最後同時進入未獲得紅包者 :",lastredPacket.get("notGet"));
        }
        return "userSessionIds :" + userSessionIds + ",redPacketList :" + redPacketList;
    }


    /**
     * 客戶端連進websocket搶紅包
     *
     */
    @MessageMapping("/messageControl")
    @SendTo("/topic/getResponse")
    public ServerResponseModel getRedPacket(@RequestParam ChatClientModel responseMessage) {
        long now=System.currentTimeMillis();
        String name = responseMessage.getName();
        String id = "";
        int amount = 0;

        WebSocketSession webSocketSession = SocketManager.get(name);
        if (webSocketSession != null) {
             id = webSocketSession.getId();
        }

        Set<String> idSet = new TreeSet<String>();
        idSet.add(id);

        if(redPacketList.isEmpty()){
//            logger.info("沒取得紅包 :{}, id :{}", now, id);
            return new ServerResponseModel("紅包搶完了");
        }else if(0 == redPacketList.size() && null != userSessionIds.putIfAbsent(now, idSet)){
            Set<String> value = userSessionIds.putIfAbsent(now, idSet);
            lastredPacket.putIfAbsent("get", value);
            Set<String> notGet = lastredPacket.putIfAbsent("notGet",idSet);
            if(null != notGet){
                notGet.add(id);
            }
        } else {
            amount = redPacketList.poll();
            Set<String> value = userSessionIds.putIfAbsent(now, idSet);
            if(null != value){
                value.add(id);
            }
        }
        logger.info("userId :{} , 取得紅包金額 :{}" , id, amount);

        return new ServerResponseModel("取得紅包金額：" + amount);
    }
}
