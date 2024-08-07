package animelistapi.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LogAspect {
	
	private static final String LOG_TARGET_SERVICE_RE = "execution(* animelistapi.service.*Service.*())";
	
	@Before(LOG_TARGET_SERVICE_RE)
	public void loggginServiceStart(JoinPoint jp) {
		log.info("処理開始：" + jp.getSignature());
	}
	
	@After(LOG_TARGET_SERVICE_RE)
	public void loggingServiceEnd(JoinPoint jp) {
		log.info("処理終了：" + jp.getSignature());
	}

	@Around("bean(*Controller)")
	public Object startControllerLogging(ProceedingJoinPoint jp) throws Throwable {
		log.info("処理開始：" + jp.getSignature());
		
		try {
			Object result = jp.proceed();
			
			log.info("処理終了：" + jp.getSignature());
			log.info("返り値：" + result.toString());
			
			return result;
		} catch(Throwable e) {
			log.error("処理異常終了：" + jp.getSignature(), e);
			
			throw e;
		}
	}
	
}
