package com.example.libcyl;

public class Person {
//    提供私有属性
    private String name;
    private int age;
//    提供私有方法
    private void save(int n,String s){
        System.out.println("sava()..."+n+s);
    }
    private void update(){
        System.out.println("update()...");
    }
}
