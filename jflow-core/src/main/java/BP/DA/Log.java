package BP.DA;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import BP.Sys.SystemConfig;

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
		
	    return BP.Sys.SystemConfig.getIsDebug();
	
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

	//	public static void DefaultLogWriteLineError(String msg, boolean isOutDos)
	//	{
	//		// DefaultLogWriteLine(LogType.Error, msg);
	//		// if (isOutDos)
	//		// {
	//		// System.out.println(msg);
	//		// }
	//	}

	// 经常使用的静态方法
	// private static Log _enlog = new Log(Log.GetLogFileName("EnLog"));
	// public static void EnLogWriteLine(LogType type, string info)
	// {
	// _log.WriteLine(type, info);
	// }
	//
	//	private static Log _log = new Log(Log.GetLogFileName());

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

	//	public static void DefaultLogWriteLineWithOutUseInfo(String info)
	//	{
	//		// _log.openFile();
	//		// _log.writelog(info);
	//		// _log.closeFile();
	//	}

	//	private boolean isReady = false;
	//	private FileWriter swLog;
	//	private String strLogFile;
	//	private String userName = "System";

	// /**
	// 构造函数
	//
	// @param LogFileName
	// */
	//	public Log(String LogFileName)
	//	{
	// this.strLogFile = LogFileName;
	// try
	// {
	// openFile();
	// writelog("-----------------------------------------------------------------------------------------------------");
	// }
	// finally
	// {
	// closeFile();
	// }
	//	}

	//	/**
	//	 * 用户
	//	 */
	//	public final void setUserName(String value)
	//	{
	//		this.userName = value;
	//	}

	//	private void writelog(String msg)
	//	{
	// if(isReady)
	// {
	// try {
	// swLog.append(msg+"\n");
	// } catch (IOException e) {
	// 
	// e.printStackTrace();
	// }
	// }
	// else
	// {
	// System.out.println("Error Cannot write to log file.");
	// }
	//	}

	//	public static void ClearLog()
	//	{
	// try
	// {
	// File file = new File(Log.GetLogFileName());
	// file.delete();
	// }
	// catch (java.lang.Exception e)
	// {
	// }
	//	}

	//	private void openFile()
	//	{
	// try
	// {
	// swLog = new FileWriter(Log.GetLogFileName());
	// isReady = true;
	// }
	// catch (java.lang.Exception e)
	// {
	// isReady = false;
	// }
	//	}

	//	private void closeFile()
	//	{
	// if(isReady)
	// {
	// try
	// {
	// swLog.flush();
	// swLog.close();
	// }
	// catch (java.lang.Exception e)
	// {
	// }
	// }
	//	}

	//	/**
	//	 * 取得Log的文件路径和文件名
	//	 * 
	//	 * @param type
	//	 * @return
	//	 */
	//	public static String GetLogFileName()
	//	{
	//		// String filepath=SystemConfig.getPathOfLog();
	//		// File file = new File(filepath);
	//		//
	//		// if (!file.exists())
	//		// {
	//		// try {
	//		// file.createNewFile();
	//		// } catch (IOException e) {
	//		// 
	//		// e.printStackTrace();
	//		// }
	//		// }
	//		//
	//		// SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
	//		//
	//		// return filepath + "\\"+ df.format(new java.util.Date())+ ".log";
	//		//
	//		// //return filepath + "\\"+
	//		// DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss") + ".log";
	//		return "";
	//	}

	//	/**
	//	 * 写一行日志
	//	 * 
	//	 * @param message
	//	 */
	//	public final void WriteLine(String message)
	//	{
	//		// WriteLine(LogType.Info, message);
	//	}
	//	
	//	/**
	//	 * 写一行日志
	//	 * 
	//	 * @param logtype
	//	 * @param message
	//	 */
	//	public final void WriteLine(LogType logtype, String message)
	//	{
	//		// string stub = DateTime.Now.ToString("@HH:MM:ss") ;
	//		// SimpleDateFormat df = new
	//		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	//		// String stub = df.format(new Date());
	//		// switch(logtype)
	//		// {
	//		// case Info:
	//		// stub += "Info:";
	//		// break;
	//		// case Warning:
	//		// stub += "Warning:";
	//		// break;
	//		// case Error:
	//		// stub += "Fatal:";
	//		// break;
	//		// }
	//		// stub = stub + userName + "');\"" + message;
	//		// openFile();
	//		// writelog(stub);
	//		// closeFile();
	//		// //Console.WriteLine(stub);
	//	}
	//	/**
	//	 * 打开日志目录
	//	 */
	// public static void OpenLogDir()
	// {
	// String file = BP.Sys.SystemConfig.getPathOfLog();
	// try
	// {
	// System.Diagnostics.Process.Start(file);
	// }
	// catch (RuntimeException ex)
	// {
	// throw new RuntimeException("@打开日志目录出现错误。" + ex.getMessage());
	// }
	// }
	//	/**
	//	 * 打开今天的日志
	//	 */
	// public static void OpeLogToday()
	// {
	// String file = BP.Sys.SystemConfig.getPathOfLog() + new
	// java.util.Date().ToString("yyyy_MM_dd") + ".log";
	// try
	// {
	// System.Diagnostics.Process.Start(file);
	// }
	// catch(RuntimeException ex)
	// {
	// throw new RuntimeException("@打开日志文件出现错误。"+ex.getMessage());
	// }
	// }

}
