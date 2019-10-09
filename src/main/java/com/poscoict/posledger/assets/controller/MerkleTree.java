package com.poscoict.posledger.assets.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class MerkleTree {

    public static String createHash(String str) {
        String hashString = "";
        try {
            // MD2, MD4, MD5, SHA-1, SHA-256, SHA-512
            MessageDigest sh = MessageDigest.getInstance("SHA-512");
            sh.update(str.getBytes());
            byte byteData[] = sh.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            hashString = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            hashString = null;
        }
        return hashString;
    }

    public static String merkleRoot(String leaf[], int start, int end) {

        if(start >= end) {
            System.out.println(leaf[start] + " " + createHash(leaf[start]));
            //return leaf[0];
            return createHash(leaf[start]);
        }

        int middle = (start+end)/2;
        String left = merkleRoot(leaf, start, middle);
        String right = merkleRoot(leaf, middle+1, end);
        String result = createHash(left + right);

        System.out.println("left + right : " + result);
        return result;
    }

    public static void main (String[] args) throws java.lang.Exception {

    }

}