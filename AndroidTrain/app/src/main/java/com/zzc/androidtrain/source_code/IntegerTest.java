package com.zzc.androidtrain.source_code;

/**
 *
 * Created by zczhang on 16/9/27.
 */
public class IntegerTest {

    public void autoBoxTest() {
        Integer i1 = 120, i2 = 120, i3 = 150, i4 = 150;
        System.out.println("i1 == i2 is " + (i1 == i2));
        System.out.println("i3 == i4 is " + (i3 == i4));

        Integer i5 = new Integer(3);
        Integer i6 = 3;
        int i7 = 3;
        System.out.println("i4 == i6 is " + (i5 == i6));
        System.out.println("i6 == i7 is " + (i6 == i7));


    }

    public static void main(String[] args) {
        IntegerTest integerTest = new IntegerTest();
        integerTest.autoBoxTest();
    }
}
