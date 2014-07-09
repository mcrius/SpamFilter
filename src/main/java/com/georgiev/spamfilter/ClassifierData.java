/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.georgiev.spamfilter;

import java.util.HashMap;


public class ClassifierData {
    
    private HashMap<String, Long> hamMap;
    private HashMap<String, Long> spamMap;
    private Long spamCount;
    private Long hamCount;
    
    

    public ClassifierData() {
    }

    public ClassifierData(HashMap<String, Long> hamMap, HashMap<String, Long> spamMap, Long spamCount, Long hamCount) {
        this.hamMap = hamMap;
        this.spamMap = spamMap;
        this.spamCount = spamCount;
        this.hamCount = hamCount;
    }

    public HashMap<String, Long> getHamMap() {
        return hamMap;
    }

    public void setHamMap(HashMap<String, Long> hamMap) {
        this.hamMap = hamMap;
    }

    public HashMap<String, Long> getSpamMap() {
        return spamMap;
    }

    public void setSpamMap(HashMap<String, Long> spamMap) {
        this.spamMap = spamMap;
    }

    public Long getSpamCount() {
        return spamCount;
    }

    public void setSpamCount(Long spamCount) {
        this.spamCount = spamCount;
    }

    public Long getHamCount() {
        return hamCount;
    }

    public void setHamCount(Long hamCount) {
        this.hamCount = hamCount;
    }
    

    
    
}
