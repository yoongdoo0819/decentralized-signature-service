package com.poscoict.posledger.assets.model;

public class Sign {

    private String signID;
    private String signPath;

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
        return "Sign{" +
                "signID=" + signID +
                ", signPath'" + signPath + '\'' +
                '}';
    }
}
