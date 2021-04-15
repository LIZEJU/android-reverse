#include <jni.h>
#include <string>

//#include "<android.log.h>"

extern "C"{
#include "testc.h"
}
#include "android/log.h"
extern "C" JNIEXPORT jstring JNICALL
Java_com_example_ndk1_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C" JNIEXPORT void  JNICALL Java_com_example_ndk1_MainActivity_myfirstJNI(JNIEnv* env,
                                                                            jobject /* this */){

}

extern "C" JNIEXPORT void JNICALL Java_com_example_ndk1_MainActivity_staticfunc(JNIEnv* env,
                                                                                jclass /* this */){
    int result = add_c(3,4);
    __android_log_print(ANDROID_LOG_INFO,"JNI","JNI->%d",result);
}