package tn.esprit.springfever.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j

public class LoggingAspect {
    private static final Logger logger =
            LogManager.getLogger(LoggingAspect.class);
    @After("execution(* tn.esprit.springfever.Services.Implementation.*.get*(..))")
    public void apres(JoinPoint thisJoinPoint) {
        log.info("\n******************************************************************************\n" +"Out of the method (After) " + thisJoinPoint.getSignature().getName() + "\n *****************************************");
    }
}
