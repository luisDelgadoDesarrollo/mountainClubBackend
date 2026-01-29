package luis.delgado.clubmontana.backend.core.aspects.logging;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

  @Around(
"""
    execution(* luis.delgado.clubmontana.backend.api..*(..)) ||
    execution(* luis.delgado.clubmontana.backend.application..*(..)) ||
    execution(* luis.delgado.clubmontana.backend.infrastructure..*(..))
""")
  public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {

    String className = joinPoint.getTarget().getClass().getSimpleName();
    String methodName = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();

    log.info("➡️  {}.{}() START | args={}", className, methodName, Arrays.toString(args));

    long startTime = System.currentTimeMillis();

    try {
      Object result = joinPoint.proceed();

      long duration = System.currentTimeMillis() - startTime;
      log.info("✅ {}.{}() END | time={}ms", className, methodName, duration);

      return result;

    } catch (Throwable ex) {
      long duration = System.currentTimeMillis() - startTime;
      log.error("❌ {}.{}() ERROR after {}ms", className, methodName, duration, ex);
      throw ex;
    }
  }
}
