package com.example.mrwen.bean;

/**
 * Created by mrwen on 2017/2/11.
 */

public class UniversalResult {
    private int resultCode;
    private int id;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
