LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := jniTest
LOCAL_SRC_FILES :=  test.cpp

LOCAL_STATIC_LIBRARIES := \lbz2

LOCAL_C_INCLUDES += \ $(JNI_H_INCLUDE) bzip2

include $(BUILD_SHARED_LIBRARY)
