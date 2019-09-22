package BP.WF.NetPlatformImpl;

import BP.Web.*;
import BP.WF.*;

public class WF_File
{
	/** 
	 文件上传
	 
	 @param filePath
	*/
	public static void UploadFile(String filePath)
	{
		try
		{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
			 var filelist = HttpContextHelper.Current.Request.Files;
			if (filelist == null || filelist.size() == 0)
			{
				throw new UnsupportedOperationException("没有上传文件");
			}
			HttpPostedFile f = filelist[0];
			// 写入文件
			f.SaveAs(filePath);
		}
		catch (RuntimeException ex)
		{
			throw new UnsupportedOperationException(ex.getMessage());
		}
	}
	/** 
	 获取文件
	 
	 @return 
	*/

	public static HttpPostedFile GetUploadFile()
	{
		return GetUploadFile(0);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static HttpPostedFile GetUploadFile(int index=0)
	public static HttpPostedFile GetUploadFile(int index)
	{
		try
		{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
			var filelist = HttpContextHelper.Current.Request.Files;
			if (filelist == null || filelist.size() == 0)
			{
				throw new UnsupportedOperationException("没有上传文件");
			}
			return filelist[index];
		}
		catch (RuntimeException ex)
		{
			throw new UnsupportedOperationException(ex.getMessage());
		}
	}
	public static HttpFileCollection GetUploadFiles()
	{
		try
		{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
			var filelist = HttpContextHelper.Current.Request.Files;
			if (filelist == null || filelist.size() == 0)
			{
				throw new UnsupportedOperationException("没有上传文件");
			}
			return filelist;
		}
		catch (RuntimeException ex)
		{
			throw new UnsupportedOperationException(ex.getMessage());
		}
	}
	public static void UploadFile(HttpPostedFile file, String filePath)
	{
		try
		{
			// 写入文件
			file.SaveAs(filePath);
		}
		catch (RuntimeException ex)
		{
			throw new UnsupportedOperationException(ex.getMessage());
		}
	}
	public static long GetFileLength(HttpPostedFile file)
	{
		try
		{
			return file.ContentLength;
		}
		catch (RuntimeException ex)
		{
			throw new UnsupportedOperationException(ex.getMessage());
		}
	}
}