package com.poscoict.posledger.assets.util;

public class Manager {
    private static String chaincodeId;

    private Manager() {}

    public static void setChaincodeId(String chaincodeId) {
        Manager.chaincodeId = chaincodeId;
    }

    public static String getChaincodeId() {
        return chaincodeId;
    }
}
