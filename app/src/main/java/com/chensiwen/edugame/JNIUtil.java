package com.chensiwen.edugame;

/**
 * @author chencheng
 * @since 2018/10/12
 */
public class JNIUtil {
    public native String getNativeString();
    public native String getNativeStringWithParam(String a, int b, long d, boolean e);
}
