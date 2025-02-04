package bg.softuni.zooarchitect.controller.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {
    @Pointcut("@annotation(WarnIfExecutionExceeds)")
    void onWarnIfExecutionTimeExceeds(){}
}
