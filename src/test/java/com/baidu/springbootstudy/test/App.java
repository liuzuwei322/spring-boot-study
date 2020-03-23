package com.baidu.springbootstudy.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class App {
    public static void write () {
        Person person = new Person();
        person.setAge(26);
        person.setName("wge");

        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("D:\\obj.txt"));
            outputStream.writeObject(person);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void read() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("D:\\obj.txt"));
            try {
                Object object = inputStream.readObject();
                Person person = (Person) object;
                System.out.println(person.getName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        read();
    }
}
