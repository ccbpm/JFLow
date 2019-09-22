package BP.DA;

import BP.Pub.*;
import BP.Sys.*;
import java.io.*;
import java.time.*;

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region class Log
/** 
 日志
*/
public class Log
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 在测试状态下
	public static void DebugWriteInfo(String msg)
	{
	//	if (SystemConfig.IsDebug)
			DefaultLogWriteLine(LogType.Info, msg);
	}
	public static void DebugWriteWarning(String msg)
	{
		//if (SystemConfig.IsDebug)
			DefaultLogWriteLine(LogType.Warning, msg);
	}
	public static void DebugWriteError(String msg)
	{
		//  if (SystemConfig.IsDebug)
		DefaultLogWriteLine(LogType.Error, msg);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	public static void DefaultLogWriteLineError(RuntimeException ex)
	{

		DefaultLogWriteLine(LogType.Error, ex.getMessage());

	//	DefaultLogWriteLine(LogType.Error, ex.StackTrace);
	}

	public static void DefaultLogWriteLineError(String msg)
	{
		DefaultLogWriteLine(LogType.Error, msg);
	}

	public static void DefaultLogWriteLineError(String msg, boolean isOutDos)
	{
		DefaultLogWriteLine(LogType.Error, msg);
		if (isOutDos)
		{
			System.out.println(msg);
		}
	}

	public static void DefaultLogWriteLineInfo(String msg)
	{
		DefaultLogWriteLine(LogType.Info, msg);
	}

	public static void DefaultLogWriteLineInfo(String msg, boolean isoutDos)
	{
		DefaultLogWriteLine(LogType.Info, msg);
		if (isoutDos)
		{
			System.out.println(msg);
		}
	}

	public static void DefaultLogWriteLineWarning(String msg)
	{
		DefaultLogWriteLine(LogType.Warning, msg);
	}
	public static void DefaultLogWriteLineWarning(String msg, boolean isOutdoc)
	{
		DefaultLogWriteLine(LogType.Warning, msg);
		if (isOutdoc)
		{
			System.out.println(msg);
		}
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 经常使用的静态方法
	private static Log _log = new Log(Log.GetLogFileName());
	public static void DefaultLogWriteLine(LogType type, String info)
	{
		_log.WriteLine(type, info);
	}
	public static void DefaultLogWriteLineWithOutUseInfo(String info)
	{
		_log.openFile();
		_log.writelog(info);
		_log.closeFile();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	private boolean isReady = false;
	private OutputStreamWriter swLog;
	private String strLogFile;
	private String userName = "System";
	/** 
	 构造函数
	 
	 @param LogFileName
	*/
	public Log(String LogFileName)
	{
		this.strLogFile = LogFileName;
		try
		{
			openFile();
			writelog("-----------------------------------------------------------------------------------------------------");
		}
		finally
		{
			closeFile();
		}
	}
	/** 
	 用户
	*/
	public final void setUserName(String value)
	{
		this.userName = value;
	}
	private void writelog(String msg)
	{
		if (isReady)
		{
			swLog.write(msg + System.lineSeparator());
		}
		else
		{
			System.out.println("Error Cannot write to log file.");
		}
	}
	public static void ClearLog()
	{
		try
		{
			(new File(BP.Sys.SystemConfig.getPathOfLog() + LocalDateTime.now().toString("\\yyyy_MM_dd.log"))).delete();
		}
		catch (java.lang.Exception e)
		{
		}
	}
	private void openFile()
	{
		try
		{
			swLog = File.AppendText(strLogFile);
			isReady = true;
		}
		catch (java.lang.Exception e)
		{
			isReady = false;
		}
	}
	private void closeFile()
	{
		if (isReady)
		{
			try
			{
				swLog.flush();
				swLog.close();
			}
			catch (java.lang.Exception e)
			{
			}
		}
	}
	/** 
	 取得Log的文件路径和文件名
	 
	 @param type
	 @return 
	*/
	public static String GetLogFileName()
	{
		String filepath = SystemConfig.getPathOfLog();

		//如果目录没有建立，则建立.
		if ((new File(filepath)).isDirectory() == false)
		{
			(new File(filepath)).mkdirs();
		}

		return filepath + "\\" + LocalDateTime.now().toString("yyyy_MM_dd") + ".log";

		//return filepath + "\\"+ DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss") + ".log";
	}
	/** 
	 写一行日志
	 
	 @param message
	*/
	public final void WriteLine(String message)
	{
		WriteLine(LogType.Info, message);
	}
	/** 
	 写一行日志
	 
	 @param logtype
	 @param message
	*/
	public final void WriteLine(LogType logtype, String message)
	{
//			string stub = DateTime.Now.ToString("@HH:MM:ss") ;
		String stub = String.format("%d", LocalDateTime.now());
		switch (logtype)
		{
			case Info:
				stub += "Info:";
				break;
			case Warning:
				stub += "Warning:";
				break;
			case Error:
				stub += "Fatal:";
				break;
		}
		stub = stub + userName + "');\"" + message;
		openFile();
		writelog(stub);
		closeFile();
		//Console.WriteLine(stub);
	}
	/** 
	 打开日志目录
	*/
	public static void OpenLogDir()
	{
		String file = BP.Sys.SystemConfig.getPathOfLog();
		try
		{
			System.Diagnostics.Process.Start(file);
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@打开日志目录出现错误。" + ex.getMessage());
		}
	}
	/** 
	 打开今天的日志
	*/
	public static void OpeLogToday()
	{
		String file = BP.Sys.SystemConfig.getPathOfLog() + LocalDateTime.now().toString("yyyy_MM_dd") + ".log";
		try
		{
			System.Diagnostics.Process.Start(file);
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@打开日志文件出现错误。" + ex.getMessage());
		}
	}

}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion
