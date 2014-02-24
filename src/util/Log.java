package util;

/**
 * Logger class
 */
public class Log {
	
	class LogLevel {
		static final int NONE = 0;
		static final int ERROR = 1;
		static final int WARNING = 2;
		static final int INFO = 3;
		static final int ALL = 4;
	}
	
	private static int index = 0;
	
	private static boolean enabled;
	private static int level;
	
	static {
		// enable logging:
		enabled = true;
		
		// set log level:
		level = 2;		
	}
	
	public static void error(Object o) {
		if (level >= LogLevel.ERROR) log(o);	
	}
	
	public static void warning(Object o) {
		if (level >= LogLevel.WARNING) log(o);	
	}
	
	public static void info(Object o) {
		if (level >= LogLevel.INFO) log(o);	
	}
				
	private static void log(Object o) {
		if (enabled && level != LogLevel.NONE) {
			System.out.print((index++)+" >>> ");
			System.out.println(o);
		}
	}
	
}