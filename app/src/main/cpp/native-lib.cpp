#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring

JNICALL
Java_com_ahmdalii_ecommerce_BaseApplication_getBaseURL(
        JNIEnv *env,
        jobject /* this */) {
    std::string BASE_URL = "https://fakestoreapi.com/";
    return env->NewStringUTF(BASE_URL.c_str());
}