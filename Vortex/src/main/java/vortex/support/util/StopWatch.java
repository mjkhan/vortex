package vortex.support.util;

import java.util.concurrent.TimeUnit;

import vortex.support.AbstractComponent;

/**소요 시간 측정을 위한 유틸리티
 * @author mjkhan
 */
public class StopWatch extends AbstractComponent {
	private boolean started;
	private long start;
	/**StopWatch를 시작한다.
	 * @return StopWatch
	 */
	public StopWatch start() {
		if (started)
			throw new RuntimeException(this + " already started.");
		
		start = System.nanoTime();
		started = true;
		return this;
	}
	/**StopWatch를 정지하고 소요시간을 millisecond 단위로 반환한다.
	 * @return 소요시간
	 */
	public long stop() {
		if (!started)
			throw new RuntimeException(this + " not started.");
		
		long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
		start = 0;
		started = false;
		return elapsed;
	}
	
	/**runnable을 실행하고 소요시간을 millisecond 단위로 반환한다. 
	 * @param runnable Runnable
	 * @return runnable의 실행 소요시간
	 */
	public long elapsedTime(Runnable runnable) {
		if (runnable == null)
			return 0;
		
		try {
			start();
			runnable.run();
		} catch (Exception e) {
			throw runtimeException(e);
		} finally {
			return stop();
		}
	}
}