package com.redPackets.demo.util;

import java.util.concurrent.Callable;

public class Task implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println("產生客戶端");
        int sum = 0;
        for(int i=0;i<100;i++)
            sum += i;
        return sum;
    }
}
