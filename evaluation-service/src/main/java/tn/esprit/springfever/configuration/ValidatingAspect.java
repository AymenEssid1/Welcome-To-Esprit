package tn.esprit.springfever.configuration;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@Aspect
public class ValidatingAspect {

    @Pointcut("execution(* tn.esprit.springfever.Controllers.*.add*(..))")
    public void addPostPointcut() {}

    @Around("addPostPointcut()")
    //handleValidationException that takes in a ProceedingJoinPoint (which represents the method being called), the ID, post, and files.
    public Object handleValidationException(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object result = joinPoint.proceed();
            return ResponseEntity.ok(result);
        } catch (ConstraintViolationException ex) {
            Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
            List<String> validationErrors = new ArrayList<>();
            for (ConstraintViolation<?> violation : violations) {
                validationErrors.add(violation.getMessage());
            }
            return ResponseEntity.badRequest().body(validationErrors.toString());
        }
    }

}