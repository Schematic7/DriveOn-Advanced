package bg.softuni.autoservice.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class PerformanceAspect {

    @Pointcut("execution(* bg.softuni.autoservice.service..*(..))")
    public void serviceMethodsPointcut() {
    }

    @Around("serviceMethodsPointcut()")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();

        Object result = joinPoint.proceed();

        stopWatch.stop();

        log.info("AOP Performance: Method [{}] from class [{}] executed in {} ms.",
                joinPoint.getSignature().getName(),
                joinPoint.getTarget().getClass().getSimpleName(),
                stopWatch.getTotalTimeMillis());

        return result;
    }
}