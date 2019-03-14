//
// Created by hadoop on 18-5-10.
//
#include <android/log.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <jni.h>
#include <string.h>
#include <android/log.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <assert.h>


#include "com_njust_SerialPortJNI.h"
#include "Sercomm.h"
#include "Log.h"

#define NELEM(x) ((int) (sizeof(x) / sizeof((x)[0])))

#define CMD_SET_LENGTH  9
#define SERIAL_READ_DATA_LENGTH  128

typedef unsigned short u16;
typedef unsigned char u8;
typedef unsigned int u32;


static jclass clz;

u8 CMD[CMD_SET_LENGTH] = {0xaa, 0x33, 0x55, 0xf0, 0xff, 0x90, 0x0f, 0xa5, 0x5a};

JNIEXPORT jint JNICALL Java_com_major_SerialPortJNI_Open
        (JNIEnv *, jobject, jint Port, jint Rate, jint nBits, jchar nEvent, jint nStop) {
    InitSerPort((int) Port);
    SerOpen((int) Port, (int) Rate, (int) nBits, (int) nStop, (char) nEvent);
    LOGI("serOpen:%d\n", (int) Port);
    return 1;
}

JNIEXPORT jint JNICALL Java_com_major_SerialPortJNI_Close
        (JNIEnv *, jobject, jint Port) {
    CloseSerPort((int) Port);
    return 1;
}

JNIEXPORT jbyteArray JNICALL Java_com_major_SerialPortJNI_Read
        (JNIEnv *env, jobject obj, jint com) {
    int count = 512;
    u8 *data = NULL;
    int err = 0;
    jbyteArray jbarray = NULL;
    data = (u8 *) malloc(count);
    if (data != NULL) {
        err = SerRead((int) com, data, count, 2000, 0xaa);
        if (err > 0) {
            jbarray = env->NewByteArray(err);//建立jbarray数组
            env->SetByteArrayRegion(jbarray, 0, err, (jbyte *) data);
            free(data);
            return jbarray;
        }
    }

    env->DeleteLocalRef(jbarray);
    free(data);
    return (jbyteArray) NULL;
}

JNIEXPORT jint JNICALL Java_com_major_SerialPortJNI_Write
        (JNIEnv *env, jobject obj, jint com, jbyteArray byarray, jint datalen) {
    u8 *data = NULL;
    u8 *buff = NULL;
    int err = -1;
    int speed;

    // LOGI("Write\n");

    data = (u8 *) env->GetByteArrayElements(byarray, NULL);
    buff = (u8 *) malloc(datalen + 1);
    if (buff != NULL) {
        memcpy(buff, data, datalen);
        err = SerWrite((int) com, buff, datalen, 1500);
        free(buff);
    } else {
        free(buff);
    }

    env->ReleaseByteArrayElements(byarray, (jbyte *) data, JNI_ABORT);
    return (jint) err;

}















