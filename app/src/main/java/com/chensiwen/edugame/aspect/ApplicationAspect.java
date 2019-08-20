package com.chensiwen.edugame.aspect;

import com.chensiwen.edugame.EduApplication;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author chencheng
 * @since 2018/11/12
 */
@Aspect
public class ApplicationAspect {
    @Pointcut("call(* *.*(..))")
    void execsInApplicationMethods(JoinPoint jp) {
    }

    @Before("call(* com.chensiwen.edugame.EduApplication*(..)) && this(application)")
    void beforeExec(JoinPoint jp, EduApplication application) {
        Log.e("cccc", "Pointcut:" + jp);
    }
}
