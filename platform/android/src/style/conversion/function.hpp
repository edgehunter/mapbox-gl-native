#pragma once

#include <mbgl/style/property_value.hpp>
#include "../../conversion/conversion.hpp"
#include "../../conversion/constant.hpp"
#include "types.hpp"
#include "../../java/lang.hpp"

#include <jni/jni.hpp>
#include "json.hpp"
#include <tuple>
#include <map>

namespace mbgl {
namespace android {
namespace conversion {

template <class T>
struct Converter<jni::jobject*, mbgl::style::CameraFunction<T>> {

    Result<jni::jobject*> operator()(jni::JNIEnv& env, const mbgl::style::CameraFunction<T>& value) const {
        static jni::jclass* clazz = jni::NewGlobalRef(env, &jni::FindClass(env, "com/mapbox/mapboxsdk/style/functions/CameraFunction")).release();
        static jni::jmethodID* constructor = &jni::GetMethodID(env, *clazz, "<init>", "(Ljava/lang/Object;)V");

        // Convert expressions
        mbgl::Value expressionValue = value.getExpression().serialize();
        JsonEvaluator jsonEvaluator{env};
        jni::jobject* converted = apply_visitor(jsonEvaluator, expressionValue);

        return &jni::NewObject(env, *clazz, *constructor, converted);
    }
};

template <class T>
struct Converter<jni::jobject*, mbgl::style::SourceFunction<T>> {

    Result<jni::jobject*> operator()(jni::JNIEnv& env, const mbgl::style::SourceFunction<T>& value) const {
        static jni::jclass* clazz = jni::NewGlobalRef(env, &jni::FindClass(env, "com/mapbox/mapboxsdk/style/functions/SourceFunction")).release();
        static jni::jmethodID* constructor = &jni::GetMethodID(env, *clazz, "<init>",
                                                               "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)V");

        // Convert expressions
        mbgl::Value expressionValue = value.getExpression().serialize();
        JsonEvaluator jsonEvaluator{env};
        jni::jobject* converted = apply_visitor(jsonEvaluator, expressionValue);

        // Convert default value
        jni::jobject* defaultValue = nullptr;
        if (value.defaultValue) {
            defaultValue = *convert<jni::jobject*>(env, *value.defaultValue);
        }

        return { &jni::NewObject(env, *clazz, *constructor, defaultValue, jni::Make<jni::String>(env, value.property).Get(), converted) };
    }
};

template <class T>
struct Converter<jni::jobject*, mbgl::style::CompositeFunction<T>> {

    Result<jni::jobject*> operator()(jni::JNIEnv& env, const mbgl::style::CompositeFunction<T>& value) const {
        static jni::jclass* clazz = jni::NewGlobalRef(env, &jni::FindClass(env, "com/mapbox/mapboxsdk/style/functions/CompositeFunction")).release();
        static jni::jmethodID* constructor = &jni::GetMethodID(env, *clazz, "<init>",
                                                                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)V");

        // Convert expressions
        mbgl::Value expressionValue = value.getExpression().serialize();
        JsonEvaluator jsonEvaluator{env};
        jni::jobject* converted = apply_visitor(jsonEvaluator, expressionValue);

        // Convert default value
        jni::jobject* defaultValue = nullptr;
        if (value.defaultValue) {
            defaultValue = *convert<jni::jobject*>(env, *value.defaultValue);
        }

        return { &jni::NewObject(env, *clazz, *constructor, defaultValue, jni::Make<jni::String>(env, value.property).Get(), converted) };
    }
};

} // namespace conversion
} // namespace android
} // namespace mbgl
