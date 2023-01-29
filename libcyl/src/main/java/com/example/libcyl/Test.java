package com.example.libcyl;

public class Test {
    public static void main(String[] args) {
        for(int j = 0; j< 100; j++){
            int i=(int)(Math.random()*1000000);
            String messageCode = String.valueOf(i);
            System.out.println(messageCode);

        }
    }
}
