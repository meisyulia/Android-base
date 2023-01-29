package com.example.libcyl;

import static org.junit.Assert.*;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PersonTest {
    @Test
    /*1.通过暴力反射获取与操作属性*/
    public void getFie2() throws Exception {
        //1.获取字节码对象
        Class<Person> clazz = Person.class;
        //2.获取指定的私有属性，传入的是属性名，注意抛出异常
        Field field = clazz.getDeclaredField("name");
        //3.根据刚刚获取到的属性对象，查看属性的信息
        System.out.println(field); //private java.lang.String com.example.libcyl.Person.name
        System.out.println(field.getType().getName()); //private java.lang.String com.example.libcyl.Person.name
        System.out.println(field.getType()); //class java.lang.String

        //4.设置属性的值
        //4.1 需要指定到底是给哪个对象的name属性设置值，没有对象就创建对象
        Person person = clazz.newInstance(); //触发无参构造利用反射创建对象
        //4.2 暴力反射，需要设置私有可见权限
        field.setAccessible(true);
        //4.3通过字段对象给创建号的对象person设置属性值为海绵宝宝
        field.set(person,"海绵宝宝"); //field就是刚刚获取的name属性，set(m,n),m是要设置属性的对象，n是设置的值
        //4.4 打印查看刚刚设置的属性值
        System.out.println(field.get(person));
    }

    //2.定义单元测试方法，利用暴力反射Person类中的私有属性age
    @Test
    public void getFie3() throws Exception{
        Class<Person> clazz = Person.class;
        Field f = clazz.getDeclaredField("age");
        System.out.println(f.getType().getName());
        Person person = clazz.newInstance();
        f.setAccessible(true);
        f.set(person,17);
        System.out.println(f.get(person));
    }
    //3.单元测试2：暴力反射获取和摄者私有方法
    @Test
    public void getFunction() throws Exception {
        //1.获取字节码对象
        Class<Person> clazz = Person.class;
        //2.通过暴力反射获取私有方法
        /*
        * getDeclaredMethod(m,x,y,z,...)
        * m:要获取的方法名
        * x,y,z...可变参数，是整个方法的参数类型，但注意要加“.class”
        * */
        Method method = clazz.getDeclaredMethod("save", int.class, String.class);
        //3.1 没有对象就通过反射的方法创建对象
        Person person = clazz.newInstance();
        //3.2 想要执行私有方法，也需要先设置私有可见
        method.setAccessible(true);
        /*
        * invoke(o,x,y,z...),表示通过反射急速执行方法
        * o:要执行的是哪个对象的方法
        * x,y,z,...:执行这个方法【method对象代表的之前获取到的save()]时需要传入的参数*/
        //3.3 通过反射技术invoke()，执行目标对象obj的目标方法method【save（）】
        method.invoke(person,100,"海绵宝宝");
    }
}