package com.example.libcyl;

public class Student {
    //1.定义成员变量
    private String name;
    public int age;

    //2.给被封装属性提供get与set方法
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    //3.生成本类的无参构造与全参构造
    public Student(){}
    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }
    //4.提供本类的普通方法
    public void play(){
        System.out.println("今天大结局，放学后"+this.name+"要写1W行代码玩玩~");
    }
    public void sunDay(int n){
        System.out.println("国庆一共放"+n+"天");
    }
    //5.为了查看学生对象的具体属性与属性值，重写toString()
    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
