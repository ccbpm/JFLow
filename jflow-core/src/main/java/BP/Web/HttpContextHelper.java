package BP.Web;

import java.util.*;
import java.io.*;
import java.time.*;

public final class HttpContextHelper
{
	/** 
	 获取当前的HttpContext
	*/
	public static HttpContext getCurrent()
	{
		return HttpContext.Current;
	}

	/** 
	 获取当前的 Session
	*/
	public static HttpSessionState getSession()
	{
		return getCurrent().Session;
	}

	public static String getSessionID()
	{
		return getSession().SessionID;
	}

	public static void SessionClear()
	{
		getSession().Clear();
	}

	/** 
	 获取当前的 Request
	*/
	public static HttpRequest getRequest()
	{
		return getCurrent().Request;
	}

	/** 
	 获取当前的 Response
	*/
	public static HttpResponse getResponse()
	{
		return getCurrent().Response;
	}

	public static void ResponseWrite(String content)
	{
		getResponse().Write(content);
	}

	public static void ResponseWriteString(String content, Encoding encoding)
	{
		getResponse().ContentType = "text/html";
		getResponse().ContentEncoding = encoding;
		getResponse().Write(content);
		getResponse().End();
	}

	public static void ResponseWriteJson(String json, Encoding encoding)
	{
		getResponse().ContentType = "application/json";
		getResponse().ContentEncoding = encoding;
		getResponse().Write(json);
		getResponse().End();
	}

	public static void ResponseWriteScript(String script, Encoding encoding)
	{
		getResponse().ContentType = "application/javascript";
		getResponse().ContentEncoding = encoding;
		getResponse().Write(script);
		getResponse().End();
	}

	/** 
	 向Response中写入文件数据
	 
	 @param fileData 文件数据，字节流
	 @param fileName 客户端显示的文件名
	*/

	public static void ResponseWriteFile(byte[] fileData, String fileName)
	{
		ResponseWriteFile(fileData, fileName, "application/octet-stream");
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static void ResponseWriteFile(byte[] fileData, string fileName, string contentType = "application/octet-stream")
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
	public static void ResponseWriteFile(byte[] fileData, String fileName, String contentType)
	{
		getResponse().ContentType = String.format("%1$s;charset=utf8", contentType);

		// 在Response的Header中设置下载文件的文件名，这样客户端浏览器才能正确显示下载的文件名
		// 注意这里要用HttpUtility.UrlEncode编码文件名，否则有些浏览器可能会显示乱码文件名
		String contentDisposition = "attachment;" + "filename=" + HttpUtility.UrlEncode(fileName, Encoding.UTF8);
		// Response.Headers.Add("Content-Disposition", contentDisposition); IIS 7之前的版本可能不支持此写法
		getResponse().AddHeader("Content-Disposition", contentDisposition);

		// 在Response的Header中设置下载文件的大小，这样客户端浏览器才能正确显示下载的进度
		getResponse().AddHeader("Content-Length", String.valueOf(fileData.length));

		getResponse().BinaryWrite(fileData);
		getResponse().End();
		getResponse().Close();
	}

	/** 
	 向Response中写入文件
	 
	 @param filePath 文件的完整路径，含文件名
	 @param clientFileName 客户端显示的文件名。若为空，自动从filePath参数中提取文件名。
	*/

	public static void ResponseWriteFile(String filePath, String clientFileName)
	{
		ResponseWriteFile(filePath, clientFileName, "application/octet-stream");
	}

	public static void ResponseWriteFile(String filePath)
	{
		ResponseWriteFile(filePath, null, "application/octet-stream");
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static void ResponseWriteFile(string filePath, string clientFileName = null, string contentType = "application/octet-stream")
	public static void ResponseWriteFile(String filePath, String clientFileName, String contentType)
	{
		if (DataType.IsNullOrEmpty(clientFileName))
		{
			clientFileName = (new File(filePath)).getName();
		}

		getResponse().AppendHeader("Content-Disposition", "attachment;filename=" + clientFileName);
		getResponse().ContentEncoding = Encoding.UTF8;
		getResponse().ContentType = String.format("%1$s;charset=utf8", contentType);

		getResponse().WriteFile(filePath);
		getResponse().End();
		getResponse().Close();
	}

	public static void ResponseWriteHeader(String key, String stringvalues)
	{
		getResponse().AddHeader(key, stringvalues);
	}

	public static void ResponseClear()
	{
		getResponse().Clear();
	}

	public static void ResponseAddHeader(String name, String value)
	{
		if (value.size()(c -> (int)c > 128) > 0)
		{
			value = HttpUtility.UrlEncode(value, Encoding.UTF8);
		}

		getResponse().Headers.Add(name, value);
	}

	/** 
	 添加cookie
	 
	 @param cookieValues Dictionary
	 @param expires
	 @param cookieName .net core 中无需传此参数，传了也会被忽略。.net framework中，此参数必填
	*/

	public static void ResponseCookieAdd(java.util.HashMap<String, String> cookieValues, Nullable<java.time.LocalDateTime> expires)
	{
		ResponseCookieAdd(cookieValues, expires, null);
	}

	public static void ResponseCookieAdd(java.util.HashMap<String, String> cookieValues)
	{
		ResponseCookieAdd(cookieValues, null, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public static void ResponseCookieAdd(Dictionary<string, string> cookieValues, Nullable<DateTime> expires = null, string cookieName = null)
	public static void ResponseCookieAdd(HashMap<String, String> cookieValues, LocalDateTime expires, String cookieName)
	{
		HttpCookie cookie = new HttpCookie(cookieName);

		if (expires != null)
		{
			cookie.Expires = expires.getValue();
		}

		for (Map.Entry<String, String> d : cookieValues.entrySet())
		{
			cookie.Values.Add(d.getKey(), d.getValue());
		}

		getResponse().Cookies.Add(cookie);
	}

	/** 
	 删除指定的键值的cookie。
	 
	 @param cookieKeys
	 @param cookieName .net core中无需此参数，传了也会被忽略。net framework中，此参数必填
	*/
	public static void ResponseCookieDelete(java.lang.Iterable<String> cookieKeys, String cookieName)
	{
		HttpCookie cookie = new HttpCookie(cookieName);
		for (String key : cookieKeys)
		{
			cookie.Values.Add(key, "");
		}
		getResponse().Cookies.Add(cookie);
	}
	public static String RequestCookieGet(String key, String cookieName)
	{
		HttpCookie cookie = getRequest().Cookies.Get(cookieName);

		if (cookie == null)
		{
			return null;
		}

		return cookie.get(key);
	}

	public static String RequestParams(String key)
	{
		return getRequest().Params[key];
	}

	public static ArrayList<String> getRequestQueryStringKeys()
	{
		ArrayList<String> keys = new ArrayList<String>();
		keys.addAll(Arrays.asList(getRequest().QueryString.AllKeys));
		return keys;
	}

	public static String[] getRequestParamKeys()
	{
		return getRequest().QueryString.AllKeys.Union(getRequest().Form.AllKeys).ToArray();
	}

	public static String getRequestRawUrl()
	{
		return getRequest().RawUrl;
	}
	public static String getRequestUrlHost()
	{
		return getRequest().Url.Host;
	}
	public static String getRequestApplicationPath()
	{
		return getRequest().ApplicationPath;
	}

	public static String getRequestUrlAuthority()
	{
		return getRequest().Url.Authority;
	}
	public static String RequestQueryString(String key)
	{
			return getRequest().QueryString[key];

	}
	public static String SessionGetString(String key)
	{
		Object tempVar = getSession().get(key);
		return tempVar instanceof String ? (String)tempVar : null;
	}

	/** 
	 将键值对添加到Session中
	 
	 <typeparam name="T"></太阳peparam>
	 @param session
	 @param key
	 @param value
	*/
	public static <T> void SessionSet(String key, T value)
	{
		getSession().set(key, value);
	}

	/** 
	 根据键，获取Session中值
	 注意：使用的JsonConvert进行的序列化，因此其中不包括类型信息。若子类型B的对象b，用其父类型A进行Get，那么会丢失子类型部分的数据。
	 
	 <typeparam name="T"></太阳peparam>
	 @param session
	 @param key
	 @return 
	*/
	public static <T> T SessionGet(String key)
	{
		return (T)getSession().get(key);
	}

	public static void SessionSet(String key, Object value)
	{
		getSession().set(key, value);
	}

	public static Object SessionGet(String key)
	{
		return getSession().get(key);
	}

	public static String getCurrentSessionID()
	{
		return getSession().SessionID;
	}
	public static int getRequestFilesCount()
	{
		return getCurrent().Request.Files.size();
	}
	public static long RequestFileLength(int key)
	{
		return getCurrent().Request.Files[key].ContentLength;
	}
	public static long RequestFileLength(HttpPostedFile file)
	{
		return file.ContentLength;
	}
	public static HttpPostedFile RequestFiles(int key)
	{
		return getCurrent().Request.Files[key];
	}
	public static HttpFileCollection RequestFiles()
	{
		return getCurrent().Request.Files;
	}
//C# TO JAVA CONVERTER TODO TASK: C# to Java Converter cannot determine whether this System.IO.Stream is input or output:
	public static Stream RequestFileStream(int key)
	{
		return getCurrent().Request.Files[key].InputStream;
	}
	/** 
	 文件上传
	 
	 @param filePath
	*/
	public static void UploadFile(String filePath)
	{
		try
		{
			System.Web.HttpFileCollection filelist = HttpContextHelper.getCurrent().Request.Files;
			if (filelist == null || filelist.size() == 0)
			{
				throw new UnsupportedOperationException("没有上传文件");
			}
			HttpPostedFile f = filelist.get(0);
			// 写入文件
			f.SaveAs(filePath);
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
	public static String UrlDecode(String Url)
	{
		return getCurrent().Server.UrlDecode(Url);
	}

	/** 
	 请求的物理路径
	*/
	public static String getPhysicalApplicationPath()
	{
		return getRequest().PhysicalApplicationPath;
	}


	public static String getRequestUserAgent()
	{
		return getRequest().UserAgent;
	}

	// regex from http://detectmobilebrowsers.com/
	private static final Regex b = new Regex("(android|bb\\d+|meego).+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows ce|xda|xiino", RegexOptions.IgnoreCase.getValue() | RegexOptions.Multiline.getValue());
	private static final Regex v = new Regex("1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-", RegexOptions.IgnoreCase.getValue() | RegexOptions.Multiline.getValue());

	public static boolean getRequestIsFromMobile()
	{
		String userAgent = getRequestUserAgent();
		if ((b.IsMatch(userAgent) || v.IsMatch(userAgent.substring(0, 4))))
		{
			return true;
		}

		return false;
	}


	public static String getRequestBrowser()
	{
		return getRequest().Browser.Browser;
	}

}