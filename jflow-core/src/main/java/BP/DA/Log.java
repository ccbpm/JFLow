package BP.DA;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import BP.Difference.SystemConfig;

public class Log {

	private static Logger logger = LoggerFactory.getLogger(Log.class);

	public static void DebugWriteInfo(String msg) {
//		if (SystemConfig.getIsDebug()) { // 信息日志不判断Debug，直接打印
			DefaultLogWriteLine(LogType.Info, msg);
//		}
	}

	public static void DebugWriteWarning(String msg) {
//		if (SystemConfig.getIsDebug()) { // 警告日志不判断Debug，直接打印
			DefaultLogWriteLine(LogType.Warning, msg);
//		}
	}

	public static void DebugWriteError(String msg) {
//		if (SystemConfig.getIsDebug()) { // 错误日志不判断Debug，直接打印
			DefaultLogWriteLine(LogType.Error, msg);
//		}
	}

	public static boolean isLoggerDebugEnabled() {
		
	    return SystemConfig.getIsDebug();
	    
		//return logger.isDebugEnabled();
	}

	public static void DefaultLogWriteLineDebug(String msg) {
		DefaultLogWriteLine(LogType.Debug, msg);
	}

	//
	//	public static void DefaultLogWriteLineInfo(String msg, boolean isoutDos)
	//	{
	//		// DefaultLogWriteLine(LogType.Info, msg);
	//		// if (isoutDos)
	//		// {
	//		// System.out.println(msg);
	//		// }
	//	}

	public static void DefaultLogWriteLineInfo(String msg) {
		DefaultLogWriteLine(LogType.Info, msg);
	}

	//
	//	public static void DefaultLogWriteLineInfo(String msg, boolean isoutDos)
	//	{
	//		// DefaultLogWriteLine(LogType.Info, msg);
	//		// if (isoutDos)
	//		// {
	//		// System.out.println(msg);
	//		// }
	//	}

	public static void DefaultLogWriteLineWarning(String msg) {
		DefaultLogWriteLine(LogType.Warning, msg);
	}

	//	public static void DefaultLogWriteLineWarning(String msg, boolean isOutdoc) {
	// DefaultLogWriteLine(LogType.Warning, msg);
	// if (isOutdoc)
	// {
	// System.out.println(msg);
	// }
	//	}

	public static void DefaultLogWriteLineError(String msg) {
		DefaultLogWriteLine(LogType.Error, msg);
	}
	
	public static void DefaultLogWriteLineError(String msg, Throwable ex) {
		logger.error(msg, ex);
	}


	public static void DefaultLogWriteLine(LogType type, String info) {
		switch (type) {
		case Debug:
			logger.debug(info);
			break;
		case Info:
			logger.info(info);
			break;
		case Warning:
			logger.warn(info);
			break;
		case Error:
			logger.error(info);
			break;
		}
	}


}
