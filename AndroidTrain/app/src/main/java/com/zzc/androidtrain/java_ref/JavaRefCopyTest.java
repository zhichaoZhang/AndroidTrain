package com.zzc.androidtrain.java_ref;

import android.util.Log;

/**
 * Java值传递和引用地址测试
 * Created by zczhang on 16/7/29.
 */
public class JavaRefCopyTest {
    private static final String TAG = "JavaRefCopyTest";

    public static void main(String[] args) {
        JavaRefCopyTest javaRefCopyTest = new JavaRefCopyTest();

        Person person = new Person("joye", 10);
        JavaRefCopyTest.log(TAG, "main: old:" + person.toString());


        Person newPerson = javaRefCopyTest.changePerson(person);



        JavaRefCopyTest.log(TAG, "main: new : " + newPerson.toString());

        JavaRefCopyTest.log(TAG, "main: old:" + person.toString());

    }

    public Person changePerson(Person person) {
//        person.setPersonAge(11);
//        person.setPersonName("zbc");


        person = new Person("", 12);

//        JavaRefCopyTest.log(TAG, "changePerson: "+ person.toString());

        return person;
    }

    public static void log(String tag, String str) {
        System.out.println(tag + ": " + str);
    }
}
