package com.sec.assignment;

import java.util.*;

public class SecurityAppResponse {

    List<KVPair> fileNameCountMap;
    List<KVPair> userNameCountMap;

    public List<KVPair> getUserNameCountMap() {
        return userNameCountMap;
    }

    public void setUserNameCountMap(List<KVPair> userNameCountMap) {
        this.userNameCountMap = userNameCountMap;
    }

    public List<KVPair> getDayActivityMap() {
        return dayActivityMap;
    }

    public void setDayActivityMap(List<KVPair> dayActivityMap) {
        this.dayActivityMap = dayActivityMap;
    }

    List<KVPair> dayActivityMap;

    public List<KVPair> getFileNameCountMap() {
        return fileNameCountMap;
    }

    public void setFileNameCountMap(List<KVPair> fileNameCountMap) {
        this.fileNameCountMap = fileNameCountMap;
    }
}