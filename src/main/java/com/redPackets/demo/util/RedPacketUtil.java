package com.redPackets.demo.util;

import lombok.Data;

import java.util.Random;

@Data
public class RedPacketUtil {
    /**
     * 紅包總個數
     */
    private int count;
    /**
     * 紅包總金額
     */
    private int sumMoney;
    /**
     * 紅包剩餘個數
     */
    private int surplusCount;
    /**
     * 紅包剩餘金額
     */
    private int surplusMoney;

    public RedPacketUtil(int count, int sumMoney){
        this.count = count;
        this.sumMoney = sumMoney;
        this.surplusCount = count;  //初始化都一直，所以一併初始化；
        this.surplusMoney = sumMoney;
    }

    private final Random random = new Random();
    /**
     * 採用預先按照規則分配金額到固定份數的方法
     * 分配紅包金額算法
     */
    public int nextRedPacket(){
        //預設搶到的紅包
        int money = 0;

        if(surplusCount ==1){
            money = surplusMoney;
        }else {
            money = random.nextInt(sumMoney/count);
            if(money == 0){
                money = random.nextInt(sumMoney/count);
            }
            surplusMoney = surplusMoney - money;
            surplusCount = surplusCount-1;
        }

        return money;
    }


}
