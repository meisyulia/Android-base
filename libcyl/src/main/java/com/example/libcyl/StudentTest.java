package com.example.libcyl;


import org.junit.Test;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class StudentTest {

  @Test
  public void getClazz() throws ClassNotFoundException {
    //练习获取字节码对象的3种方式
    Class<?> clazz1 = Class.forName("com.example.libcyl.Student");
    Class<?> clazz2 = Student.class;
    Class<?> clazz3 = new Student().getClass();

    //打印的是Student类对应的字节码对象
    System.out.println(clazz1);//class com.example.libcyl.Student
    //获取当前字节码对象clazz1的名字
    System.out.println(clazz1.getName());//com.example.libcyl.Student
    //通过字节码对象，获取Student类的类名
    System.out.println(clazz2.getSimpleName()); //Student
    //通过字节码对象，获取Student类对应的包对象
    System.out.println(clazz3.getPackage());//package com.example.libcyl
    //通过字节码对象，先获取Student类对应的包对象，再获取这个包对象的名字
    System.out.println(clazz3.getPackage().getName());//com.example.libcyl
  }

  //3.通过单元测试方法练习引用类型数组的定义与遍历
  @Test
  public void getStu(){
    //1.创建Student类的3个对象
    Student s1 = new Student("张三",3);
    Student s2 = new Student("李四", 4);
    Student s3 = new Student("王五", 5);
    //2.将创建的3个对象放进数组
    Student[] s= {s1,s2,s3};
    //3.直接打印数组，查看里面的元素
    System.out.println(Arrays.toString(s));
    //4.遍历数组，拿到里面每一个对象
    for(Student stu:s)
    {
      System.out.println(stu);
      //遍历到的对象执行play
      stu.play();
      System.out.println(stu.age);
    }

  }
//  4.通过单元测试方法，获取Student类中的成员变量
  @Test
  public void getFie() throws ClassNotFoundException {
//    1.获取字节码对象
    Class<?> clazz = Class.forName("com.example.libcyl.Student");
    //2.通过字节码对象获取成员变量们,遍历数组获取变量的具体信息
    //注意：目前成员变量的修饰符必须是public
    for (Field f : clazz.getFields()) {
      System.out.println(f.getName()); //获取字段名
      System.out.println(f.getType());//获取字段的类型
    }


  }
  //5.通过单元测试方法，获取Student类中的成员方法
  @Test
  public void getFunction(){
    //1.获取字节码对象
    Class<Student> clazz = Student.class;
    //2.通过字节码对象获取目标类中成员方法们
    for (Method m : clazz.getMethods()) {
      System.out.println("方法对象"+m); //直接打印遍历到的方法对象
      System.out.println("方法名"+m.getName()); //通过方法对象获取方法名
      Class<?>[] pt = m.getParameterTypes();//通过方法对象获取方法所有参数的类型
      System.out.println(Arrays.toString(pt));//打印方法参数的数组
    }

  }

  //6.通过测试单元方法，获取Student类中的构造方法
  @Test
  public void getCons(){
    //1.获取字节码对象
    Class<? extends Student> clazz = new Student().getClass();
    //2.通过字节码对象获取Student类的构造方法们，并进行遍历
    for (Constructor<?> con : clazz.getConstructors()) {
      System.out.println("构造函数名："+con.getName());
      Class<?>[] pt = con.getParameterTypes();//获取构造函数的参数类型
      System.out.println(Arrays.toString(pt));
    }

  }

  //7.通过单元测试方法，创建Student目标类的对象
  @Test
  public void getObject() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    //1.获取字节码对象
    Class<Student> clazz = Student.class;
    //2.通过反射技术创建目标类的对象，注意抛出异常
    /*反射创建对象方案1：通过触发目标类的无参构造创建对象*/
    Student o = clazz.newInstance();
    System.out.println(o);
    /*反射创建对象方案2：通过触发目标类的全参构造创建对象
    * 思路：
    * 1.先获取指定的构造函数对象，注意需要指定构造函数的参数，传入的是.class字节码对象
    * 2.通过刚刚获取到的构造函数对象创建Student目标类的对象，并且给对象的属性赋值*/
    //3.获取目标类中指定的全参构造函数
    Constructor<Student> c = clazz.getConstructor(String.class, int.class);
    //4.通过获取到的构造函数创建对象+给对象的属性赋值
    Student o2 = c.newInstance("赵六", 6);
    System.out.println(o2);
    o2.play();
  }
}