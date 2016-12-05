//
// Created by zczhang on 16/6/4.
//

#include "com_zzc_androidtrain_jnitest_HelloJni.h"
#include <stdio.h>

jstring Java_com_zzc_androidtrain_jnitest_HelloJni_stringFromJni(JNIEnv *env, jobject thiz) {
    printf("invoke get in c++\n");
    return env->NewStringUTF("Hello from JNI !");
}
