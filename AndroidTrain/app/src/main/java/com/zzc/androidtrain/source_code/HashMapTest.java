package com.zzc.androidtrain.source_code;

import java.util.HashMap;
import java.util.Map;

/**
 * HashMap测试
 *
 * Created by zczhang on 17/1/4.
 */

public class HashMapTest {
    private Map<String, Long> strLongMap = new HashMap<>();

    public void testGetValueByUnexistKey() {

    }


    public static void main(String[] args) {
        String key = "key";
        Map<String, Long> strLongMap = new HashMap<>();
        strLongMap.put(key, 1000L);

        Long value = strLongMap.remove(key);
        System.out.println("第一次 strLongMap.remove(key) = " + value);

        Long value2 = strLongMap.remove(key);
        System.out.println("第二次 strLongMap.remove(key) = " + value2);

    }

}
