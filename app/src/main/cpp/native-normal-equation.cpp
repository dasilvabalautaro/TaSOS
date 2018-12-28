#include <jni.h>
#include <string>
#include <vector>
#include <sstream>
#include <iostream>
#include <fstream>
#include "Eigen/Dense"
#include "Eigen/Eigen"
#include <android/log.h>

#define  LOG_TAG    "normalEquationFromJNI"
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)

using Eigen::MatrixXf;
using Eigen::VectorXf;
using namespace std;


extern "C" JNIEXPORT jfloatArray JNICALL
Java_com_globalhiddenodds_tasos_models_machineLearning_MLMessageSequence_normalEquationFromJNI(
        JNIEnv *env,
        jobject, jintArray day, jlongArray hour, jintArray state) {

    Eigen::MatrixXf X;
    Eigen::VectorXf y;
    Eigen::VectorXf theta;
    vector<float> arrayDays;
    vector<float> arrayHours;
    vector<float> probability;
    jfloatArray result;

    jboolean isCopy;
    jint *bodyDays = (env)->GetIntArrayElements(day, &isCopy);
    jsize len = (env)->GetArrayLength(day);
    jlong *bodyHours = (env)->GetLongArrayElements(hour, &isCopy);
    jint *bodySate = (env)->GetIntArrayElements(state, &isCopy);

    for (int i = 0; i < len; ++i) {
        float x = static_cast< float >(bodyDays[i]);
        arrayDays.push_back(x);
        float w = static_cast< float >(bodyHours[i]);
        arrayHours.push_back(w);
        float z = static_cast< float >(bodySate[i]);
        probability.push_back(z);

    }
    if (arrayDays.size() > 0) {
        MatrixXf z(arrayDays.size(), 3);
        VectorXf w(arrayDays.size());
        X = z;
        y = w;
        for (int i = 0; i < arrayDays.size(); i++) {
            X(i, 0) = 1;
            X(i, 1) = arrayDays[i];
            X(i, 2) = arrayHours[i];
            y(i) = probability[i];
        }
    }

    VectorXf t = (X.transpose() * X).ldlt().solve(X.transpose() * y);
    theta = t;

    long size = theta.size();
    int sizeResult = static_cast< int >(size);
    result = (env)->NewFloatArray(sizeResult);
    jfloat fill[sizeResult];
    for (int i = 0; i < theta.size(); ++i) {
        fill[i] = theta[i];
    }

    LOGI("Result Param %.1f",fill[0]);
    (env)->SetFloatArrayRegion(result, 0, sizeResult, fill);
    return result;

 }

