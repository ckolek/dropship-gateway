package me.kolek.ecommerce.dsgw.auth.aspect;

import me.kolek.ecommerce.dsgw.auth.AuthContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RequireScopeAspect {

  @Around("@annotation(RequireScope)")
  public Object verifyScope(ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

    RequireScope requirement = methodSignature.getMethod().getAnnotation(RequireScope.class);

    AuthContext.requireScope(requirement.action(), requirement.path());

    return joinPoint.proceed();
  }
}
