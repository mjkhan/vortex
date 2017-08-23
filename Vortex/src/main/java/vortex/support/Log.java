package vortex.support;

import java.util.HashMap;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**org.slf4j.Logger를 wrapping하여 로깅 기능을 제공한다.<br />
 * 로깅을 활성화하려면 log4j2.xml나 애플리케이션 서버에서 요구하는 설정파일을 작성한다.
 * @author mjkhan
 */
public class Log {
	private static final HashMap<Class<?>, Log> loggers = new HashMap<>();
	/**klass와 연계된 Log를 반환한다.
	 * @param klass 클래스
	 * @return Log
	 */
	public static final Log get(Class<?> klass) {
		return loggers.computeIfAbsent(klass, key -> new Log(LoggerFactory.getLogger(key)));
	}
	
	private Logger logger;
	
	private Log(Logger logger) {
		this.logger = logger;
	}
	/**로깅 레벨이 INFO로 설정돼있으면 msg가 반환하는 메시지를 로깅한다.
	 * @param msg 메시지 supplier
	 */
	public void info(Supplier<String> msg) {
		if (logger.isInfoEnabled())
			logger.info(msg.get());
	}
	/**로깅 레벨이 WARN로 설정돼있으면 msg가 반환하는 메시지를 로깅한다.
	 * @param msg 메시지 supplier
	 */
	public void warn(Supplier<String> msg) {
		if (logger.isWarnEnabled())
			logger.warn(msg.get());
	}
	/**로깅 레벨이 DEBUG로 설정돼있으면 msg가 반환하는 메시지를 로깅한다.
	 * @param msg 메시지 supplier
	 */
	public void debug(Supplier<String> msg) {
		if (logger.isDebugEnabled())
			logger.debug(msg.get());
	}
	/**로깅 레벨이 TRACE로 설정돼있으면 msg가 반환하는 메시지를 로깅한다.
	 * @param msg 메시지 supplier
	 */
	public void trace(Supplier<String> msg) {
		if (logger.isTraceEnabled())
			logger.trace(msg.get());
	}
	/**로깅 레벨이 ERROR로 설정돼있으면 msg가 반환하는 메시지를 로깅한다.
	 * @param msg 메시지 supplier
	 */
	public void error(Supplier<String> msg) {
		if (logger.isErrorEnabled())
			logger.error(msg.get());
	}
}