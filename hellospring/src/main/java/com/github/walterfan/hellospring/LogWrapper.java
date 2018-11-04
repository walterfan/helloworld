package com.github.walterfan.hellospring;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LogWrapper {
    @Pointcut("execution(* com.github.walterfan.hellospring.*.*(..))")
    private void anyMethod() {

    }

    @Before("anyMethod() && args(userName)")
    public void doAccessCheck(String userName) {
    }

    @AfterReturning(pointcut = "anyMethod()", returning = "revalue")
    public void doReturnCheck(String revalue) {

    }

    @AfterThrowing(pointcut = "anyMethod()", throwing = "ex")
    public void doExceptionAction(Exception ex) {
    }

    @After("anyMethod()")
    public void doReleaseAction() {
    }

    @Around("anyMethod()")
    public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable {
        return pjp.proceed();
    }
}