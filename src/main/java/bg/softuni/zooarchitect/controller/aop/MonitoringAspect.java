package bg.softuni.zooarchitect.controller.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;

@Aspect
@Component
public class MonitoringAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringAspect.class);

    @Around("Pointcuts.onWarnIfExecutionTimeExceeds()")
    Object monitorExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
        WarnIfExecutionExceeds annotation = getAnnotation(pjp);
        long threshold = annotation.threshold();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // before
        var result = pjp.proceed();
        // after
        stopWatch.stop();
        long methodExecutionTime = stopWatch.lastTaskInfo().getTimeMillis();

        if (methodExecutionTime > threshold) {
            LOGGER.warn("The method {} executed in {} milliseconds which is more than " +
                            "the acceptable time of {} milliseconds. Threshold exceeded by {}.",
                    pjp.getSignature(),
                    methodExecutionTime,
                    threshold,
                    methodExecutionTime - threshold);
        }

        return result;
    }

    private static WarnIfExecutionExceeds getAnnotation(ProceedingJoinPoint pjp) {

        Method method = ((MethodSignature)pjp.getSignature()).getMethod();

        return method.getAnnotation(WarnIfExecutionExceeds.class);
    }
}
