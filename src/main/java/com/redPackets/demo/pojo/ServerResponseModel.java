package com.redPackets.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServerResponseModel {
    private String responseMessage;

    public ServerResponseModel(){
    }

    @Override
    public String toString() {
        return responseMessage;
    }
}
