package com.redPackets.demo.controller;

import com.redPackets.demo.util.Task;
import com.redPackets.demo.util.WebSocketClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
@RequestMapping("gen_client")
public class GenClientController {

    @Autowired
    WebSocketClientUtil webSocketClientUtil;

    //初始化客戶連線數量
    @GetMapping("init/{count}")
    public boolean genClient(@PathVariable int count) throws InterruptedException {
        List<Future> futureList = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(count);
        Task task = new Task();
        for(int i = 0;i < count;i++){
            futureList.add(executor.submit(webSocketClientUtil));
        }
        futureList.forEach(x-> {
            try {
                x.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        executor.shutdown();

        return true;
    }
}
