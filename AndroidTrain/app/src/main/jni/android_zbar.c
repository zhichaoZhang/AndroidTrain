#include <string.h>
#include <stdlib.h>
#include <android/log.h>

#include "zbar.h"

//#include "com_hae_zapp_Zbar.h"
//JNIEXPORT jstring JNICALL Java_com_hae_zapp_Zbar_decode
//  (JNIEnv *env, jobject obj, jint w, jint h, jbyteArray img) {

/*
* Class:     com_zzc_androidtrain_qrcode_zbar_Zbar
* Method:    decode
* Signature: (II[B)Ljava/lang/String;
*/
#include "com_zzc_androidtrain_qrcode_zbar_Zbar.h"

JNIEXPORT jstring JNICALL Java_com_zzc_androidtrain_qrcode_zbar_Zbar_decode
        (JNIEnv * env, jobject obj, jint w, jint h, jbyteArray img) {
    __android_log_print(ANDROID_LOG_DEBUG, "zbar", "w = %d", w);
    __android_log_print(ANDROID_LOG_DEBUG, "zbar", "h = %d", h);
    __android_log_print(ANDROID_LOG_DEBUG, "zbar", "img = %s", img);

    zbar_image_scanner_t *scanner;
    zbar_image_t *zimage;
    zbar_image_t *zgrayimage;

    jbyte *pixbuf;
    jstring s = NULL;

    zbar_set_verbosity(10); // XXX

    pixbuf = (*env)->GetByteArrayElements(env, img, 0);

    zimage = zbar_image_create();
    if (zimage == NULL) {
        (*env)->ReleaseByteArrayElements(env, img, pixbuf, 0);
        return NULL;
    }
    zbar_image_set_format(zimage, *(unsigned long *) "Y800");
    zbar_image_set_size(zimage, w, h);
    zbar_image_set_data(zimage, pixbuf, (*env)->GetArrayLength(env, img),
                        zbar_image_free_data);

    zgrayimage = zbar_image_convert(zimage, *(unsigned long *) "Y800");
    if (zgrayimage == NULL) {
        (*env)->ReleaseByteArrayElements(env, img, pixbuf, 0);
        return NULL;
    }

    zbar_image_destroy(zimage);

    scanner = zbar_image_scanner_create();
    zbar_image_scanner_set_config(scanner, 0, ZBAR_CFG_ENABLE, 1);
    zbar_scan_image(scanner, zgrayimage);

    const zbar_symbol_t *sym;

    sym = zbar_image_first_symbol(zgrayimage);
    if (sym != NULL) {
        const char *sym_data;
        sym_data = zbar_symbol_get_data(sym);
        __android_log_print(ANDROID_LOG_DEBUG, "zbar", "url: %s", sym_data);
        s = (*env)->NewStringUTF(env, sym_data);
    }

    (*env)->ReleaseByteArrayElements(env, img, pixbuf, 0);
    return s;
}

