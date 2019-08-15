package com.poscoict.posledger.assets.model;

public class Sig {

    private  int sigNum;
    private String signID;
    private String signPath;

    public int getSignNum() {
        return sigNum;
    }

    public void setSignNum(int sigNum) {
        this.sigNum = sigNum;
    }

    public String getSignID() {
        return signID;
    }

    public void setSignID(String signID) {
        this.signID = signID;
    }

    public String getSignPath() {
        return signPath;
    }

    public void setSignPath(String signPath) {
        this.signPath = signPath;
    }

    @Override
    public String toString() {
        return "Sig{" +
                "signID=" + signID +
                ", signPath'" + signPath + '\'' +
                '}';
    }
}
