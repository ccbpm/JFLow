package BP.WF.HttpHandler;

import BP.Sys.*;
import BP.Web.*;
import BP.WF.*;
import java.util.*;

public class HttpHandlerGlo
{

		///#region 转化格式  chen

	public static void DownloadFile(String filepath, String tempName)
	{
		if ("firefox".compareToIgnoreCase(HttpContextHelper.RequestBrowser) != 0)
		{
			tempName = HttpUtility.UrlEncode(tempName);
		}

		HttpContextHelper.ResponseWriteFile(filepath, tempName);
	}

	/** 
	 从别的网站服务器上下载文件
	 
	 @param filepath
	 @param tempName
	*/
	public static void DownloadHttpFile(String filepath, String tempName)
	{
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: List<byte> byteList = new List<byte>();
		ArrayList<Byte> byteList = new ArrayList<Byte>();

		//打开网络连接
		String filePth = filepath.replace("\\", "/").trim();
		if (filepath.indexOf("/") == 0)
		{
			filepath = tangible.StringHelper.remove(filepath, 1, filepath.length() - 1);
		}
		if (!SystemConfig.AttachWebSite.trim().endsWith("/"))
		{
			filepath = SystemConfig.AttachWebSite.trim() + "/" + filepath;
		}
		else
		{
			filepath = SystemConfig.AttachWebSite.trim() + filepath;
		}

		HttpWebRequest myRequest = (HttpWebRequest)HttpWebRequest.Create(filepath);
		//向服务器请求,获得服务器的回应数据流
		try (Stream myStream = myRequest.GetResponse().GetResponseStream())
		{
			//定义一个字节数据
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] btContent = new byte[512];
			byte[] btContent = new byte[512];
			int intSize = 0;
			intSize = myStream.Read(btContent, 0, 512);
			while (intSize > 0)
			{
				if (intSize == 512)
				{
					tangible.ByteLists.addPrimitiveArrayToList(btContent, byteList);
				}
				else
				{
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] btContent2 = new byte[intSize];
					byte[] btContent2 = new byte[intSize];
					for (int i = 0; i < btContent2.length; i++)
					{
						btContent2[i] = btContent[i];
					}
					tangible.ByteLists.addPrimitiveArrayToList(btContent2, byteList);
				}
				intSize = myStream.Read(btContent, 0, 512);
			}

			myStream.Close();
			HttpContextHelper.ResponseWriteFile(tangible.ByteLists.toArray(byteList), tempName);
		}
	}
	public static void OpenWordDoc(String filepath, String tempName)
	{
		HttpContextHelper.ResponseWriteFile(filepath, tempName, "application/ms-msword");
	}
	public static void OpenWordDocV2(String filepath, String tempName)
	{
		HttpContextHelper.ResponseWriteFile(filepath, tempName);
	}

		///#endregion
}