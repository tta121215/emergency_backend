package com.emergency.rollcall.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

//    @Before("execution(* com.yourpackage.controller..*(..))")
//    public void logBefore(JoinPoint joinPoint) {
//        logger.info("Entering method: " + joinPoint.getSignature().getName());
//        logger.info("Arguments: " + Arrays.toString(joinPoint.getArgs()));
//    }
//
//    @AfterReturning(pointcut = "execution(* com.yourpackage.controller..*(..))", returning = "result")
//    public void logAfterReturning(JoinPoint joinPoint, Object result) {
//        logger.info("Exiting method: " + joinPoint.getSignature().getName());
//        logger.info("Return value: " + result);
//    }
    
    @Pointcut("execution(public * com.yourpackage.controller..*(..))")
    public void controllerMethods() {}

    @Before("controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Entering method: {} with arguments: {}", joinPoint.getSignature(), joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        logger.info("Exiting method: {} with result: {}", joinPoint.getSignature(), result);
    }
}


