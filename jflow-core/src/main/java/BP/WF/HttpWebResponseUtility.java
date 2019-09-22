package BP.WF;

import BP.DA.*;
import java.util.*;
import java.io.*;

public class HttpWebResponseUtility
{
	private static final String DefaultUserAgent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)";
	/**   
	 创建GET方式的HTTP请求  
	   
	 @param url 请求的URL  
	 @param timeout 请求的超时时间  
	 @param userAgent 请求的客户端浏览器信息，可以为空  
	 @param cookies 随同HTTP请求发送的Cookie信息，如果不需要身份验证可以为空  
	 @return   
	*/
	public final HttpWebResponse CreateGetHttpResponse(String url, Integer timeout, String userAgent, CookieCollection cookies)
	{
		if (DataType.IsNullOrEmpty(url))
		{
			throw new NullPointerException("url");
		}

		Object tempVar = WebRequest.Create(url);
		HttpWebRequest request = tempVar instanceof HttpWebRequest ? (HttpWebRequest)tempVar : null;
		request.Method = "GET";
		request.UserAgent = DefaultUserAgent;
		if (!DataType.IsNullOrEmpty(userAgent))
		{
			request.UserAgent = userAgent;
		}
		if (timeout != null)
		{
			request.Timeout = timeout.intValue();
		}
		if (cookies != null)
		{
			request.CookieContainer = new CookieContainer();
			request.CookieContainer.Add(cookies);
		}
		Object tempVar2 = request.GetResponse();
		return tempVar2 instanceof HttpWebResponse ? (HttpWebResponse)tempVar2 : null;
	}
	/** 
	 创建POST方式的HTTP请求  
	   
	 @param url 请求的URL  
	 @param parameters 随同请求POST的参数名称及参数值字典  
	 @param timeout 请求的超时时间  
	 @param userAgent 请求的客户端浏览器信息，可以为空  
	 @param requestEncoding 发送HTTP请求时所用的编码  
	 @param cookies 随同HTTP请求发送的Cookie信息，如果不需要身份验证可以为空  
	 @return   
	*/
	public final HttpWebResponse CreatePostHttpResponse(String url, Map<String, String> parameters, Integer timeout, String userAgent, Encoding requestEncoding, CookieCollection cookies)
	{
		if (DataType.IsNullOrEmpty(url))
		{
			throw new NullPointerException("url");
		}
		if (requestEncoding == null)
		{
			throw new NullPointerException("requestEncoding");
		}
		HttpWebRequest request = null;
		//如果是发送HTTPS请求  
		if (url.startsWith("https", StringComparison.OrdinalIgnoreCase))
		{
			ServicePointManager.ServerCertificateValidationCallback = (Object sender, System.Security.Cryptography.X509Certificates.X509Certificate certificate, System.Security.Cryptography.X509Certificates.X509Chain chain, System.Net.Security.SslPolicyErrors sslPolicyErrors) -> CheckValidationResult(sender, certificate, chain, sslPolicyErrors);
			ServicePointManager.ServerCertificateValidationCallback = (Object sender, System.Security.Cryptography.X509Certificates.X509Certificate certificate, System.Security.Cryptography.X509Certificates.X509Chain chain, System.Net.Security.SslPolicyErrors sslPolicyErrors) -> CheckValidationResult(sender, certificate, chain, sslPolicyErrors);
			Object tempVar = WebRequest.Create(url);
			request = tempVar instanceof HttpWebRequest ? (HttpWebRequest)tempVar : null;
			request.ProtocolVersion = HttpVersion.Version10;
		}
		else
		{
			Object tempVar2 = WebRequest.Create(url);
			request = tempVar2 instanceof HttpWebRequest ? (HttpWebRequest)tempVar2 : null;
		}
		request.Method = "POST";
		request.ContentType = "application/x-www-form-urlencoded";

		if (!DataType.IsNullOrEmpty(userAgent))
		{
			request.UserAgent = userAgent;
		}
		else
		{
			request.UserAgent = DefaultUserAgent;
		}

		if (timeout != null)
		{
			request.Timeout = timeout.intValue();
		}
		if (cookies != null)
		{
			request.CookieContainer = new CookieContainer();
			request.CookieContainer.Add(cookies);
		}
		//如果需要POST数据  
		if (!(parameters == null || parameters.isEmpty()))
		{
			StringBuilder buffer = new StringBuilder();
			int i = 0;
			for (String key : parameters.keySet())
			{
				if (i > 0)
				{
					buffer.append(String.format("&%1$s=%2$s", key, parameters.get(key)));
				}
				else
				{
					buffer.append(String.format("%1$s=%2$s", key, parameters.get(key)));
				}
				i++;
			}
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] data = requestEncoding.GetBytes(buffer.ToString());
			byte[] data = requestEncoding.GetBytes(buffer.toString());
			try (Stream stream = request.GetRequestStream())
			{
				stream.Write(data, 0, data.length);
			}
		}
		Object tempVar3 = request.GetResponse();
		return tempVar3 instanceof HttpWebResponse ? (HttpWebResponse)tempVar3 : null;
	}

	/**   
	 创建POST方式的HTTP请求 ContentType json 
	   
	 @param url 请求的URL  
	 @param JsonParameters 随同请求POST的参数名称及参数值字典json  
	 @param timeout 请求的超时时间  
	 @param userAgent 请求的客户端浏览器信息，可以为空  
	 @param requestEncoding 发送HTTP请求时所用的编码  
	 @param cookies 随同HTTP请求发送的Cookie信息，如果不需要身份验证可以为空  
	 @return   
	*/
	public final HttpWebResponse CreatePostHttpResponse(String url, String JsonParameters, Integer timeout, String userAgent, Encoding requestEncoding, CookieCollection cookies)
	{
		if (DataType.IsNullOrEmpty(url))
		{
			throw new NullPointerException("url");
		}
		if (requestEncoding == null)
		{
			throw new NullPointerException("requestEncoding");
		}
		HttpWebRequest request = null;
		//如果是发送HTTPS请求  
		if (url.startsWith("https", StringComparison.OrdinalIgnoreCase))
		{
			ServicePointManager.ServerCertificateValidationCallback = (Object sender, System.Security.Cryptography.X509Certificates.X509Certificate certificate, System.Security.Cryptography.X509Certificates.X509Chain chain, System.Net.Security.SslPolicyErrors sslPolicyErrors) -> CheckValidationResult(sender, certificate, chain, sslPolicyErrors);
			ServicePointManager.ServerCertificateValidationCallback = (Object sender, System.Security.Cryptography.X509Certificates.X509Certificate certificate, System.Security.Cryptography.X509Certificates.X509Chain chain, System.Net.Security.SslPolicyErrors sslPolicyErrors) -> CheckValidationResult(sender, certificate, chain, sslPolicyErrors);
			Object tempVar = WebRequest.Create(url);
			request = tempVar instanceof HttpWebRequest ? (HttpWebRequest)tempVar : null;
			request.ProtocolVersion = HttpVersion.Version10;
		}
		else
		{
			Object tempVar2 = WebRequest.Create(url);
			request = tempVar2 instanceof HttpWebRequest ? (HttpWebRequest)tempVar2 : null;
		}
		request.Method = "POST";
		request.ContentType = "application/json";

		if (!DataType.IsNullOrEmpty(userAgent))
		{
			request.UserAgent = userAgent;
		}
		else
		{
			request.UserAgent = DefaultUserAgent;
		}

		if (timeout != null)
		{
			request.Timeout = timeout.intValue();
		}
		if (cookies != null)
		{
			request.CookieContainer = new CookieContainer();
			request.CookieContainer.Add(cookies);
		}
		//如果需要POST数据  
		if (!DataType.IsNullOrEmpty(JsonParameters))
		{
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] data = requestEncoding.GetBytes(JsonParameters);
			byte[] data = requestEncoding.GetBytes(JsonParameters);
			try (Stream stream = request.GetRequestStream())
			{
				stream.Write(data, 0, data.length);
			}
		}
		Object tempVar3 = request.GetResponse();
		return tempVar3 instanceof HttpWebResponse ? (HttpWebResponse)tempVar3 : null;
	}
	/**   
	 创建POST方式的HTTP请求  (用于微信自动推送)
	   
	 @param url 请求的URL  
	 @param parameters 随同请求POST的参数名称及参数值字典  
	 @param timeout 请求的超时时间  
	 @param userAgent 请求的客户端浏览器信息，可以为空  
	 @param requestEncoding 发送HTTP请求时所用的编码  
	 @param cookies 随同HTTP请求发送的Cookie信息，如果不需要身份验证可以为空  
	 @return   
	*/
	public final HttpWebResponse WXCreateGetHttpResponse(String url, StringBuilder parameters, Integer timeout, String userAgent, Encoding requestEncoding, CookieCollection cookies)
	{
		if (DataType.IsNullOrEmpty(url))
		{
			throw new NullPointerException("url");
		}
		if (requestEncoding == null)
		{
			throw new NullPointerException("requestEncoding");
		}
		HttpWebRequest request = null;
		//如果是发送HTTPS请求  
		if (url.startsWith("https", StringComparison.OrdinalIgnoreCase))
		{
			ServicePointManager.ServerCertificateValidationCallback = (Object sender, System.Security.Cryptography.X509Certificates.X509Certificate certificate, System.Security.Cryptography.X509Certificates.X509Chain chain, System.Net.Security.SslPolicyErrors sslPolicyErrors) -> CheckValidationResult(sender, certificate, chain, sslPolicyErrors);
			ServicePointManager.ServerCertificateValidationCallback = (Object sender, System.Security.Cryptography.X509Certificates.X509Certificate certificate, System.Security.Cryptography.X509Certificates.X509Chain chain, System.Net.Security.SslPolicyErrors sslPolicyErrors) -> CheckValidationResult(sender, certificate, chain, sslPolicyErrors);
			Object tempVar = WebRequest.Create(url);
			request = tempVar instanceof HttpWebRequest ? (HttpWebRequest)tempVar : null;
			request.ProtocolVersion = HttpVersion.Version10;
		}
		else
		{
			Object tempVar2 = WebRequest.Create(url);
			request = tempVar2 instanceof HttpWebRequest ? (HttpWebRequest)tempVar2 : null;
		}
		request.Method = "POST";
		request.ContentType = "application/x-www-form-urlencoded";

		if (!DataType.IsNullOrEmpty(userAgent))
		{
			request.UserAgent = userAgent;
		}
		else
		{
			request.UserAgent = DefaultUserAgent;
		}

		if (timeout != null)
		{
			request.Timeout = timeout.intValue();
		}
		if (cookies != null)
		{
			request.CookieContainer = new CookieContainer();
			request.CookieContainer.Add(cookies);
		}
		if (parameters != null)
		{
			StringBuilder sb = parameters;
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] requestBytes = Encoding.UTF8.GetBytes(sb.ToString());
			byte[] requestBytes = sb.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
			request.ContentLength = requestBytes.length;
			OutputStream requeStream = request.GetRequestStream();
			requeStream.write(requestBytes, 0, requestBytes.length);
			requeStream.close();
		}
		Object tempVar3 = request.GetResponse();
		return tempVar3 instanceof HttpWebResponse ? (HttpWebResponse)tempVar3 : null;

	}

	private boolean CheckValidationResult(Object sender, X509Certificate certificate, X509Chain chain, SslPolicyErrors errors)
	{
		return true; //总是接受
	}

	/** 
	 根据Url获取返回内容
	 
	 @param url
	 @return 
	*/
	public final String HttpResponseGet(String url)
	{
		HttpWebResponse response = this.CreateGetHttpResponse(url, 10000, null, null);
		InputStreamReader reader = new InputStreamReader(response.GetResponseStream(), java.nio.charset.StandardCharsets.UTF_8);
		String str = reader.ReadToEnd();
		reader.Dispose();
		reader.close();
		if (response != null)
		{
			response.Close();
		}

		return str;
	}
	/** 
	 随同请求POST的参数名称及参数值字典
	 
	 @param url 请求地址
	 @param parameters 参数集合
	 @return 响应返回结果
	*/
	public final String HttpResponsePost(String url, Map<String, String> parameters)
	{
		Encoding encoding = Encoding.GetEncoding("utf-8");
		HttpWebResponse response = this.CreatePostHttpResponse(url, parameters, 10000, null, encoding, null);
		InputStreamReader reader = new InputStreamReader(response.GetResponseStream(), java.nio.charset.StandardCharsets.UTF_8);
		String str = reader.ReadToEnd();
		reader.Dispose();
		reader.close();
		if (response != null)
		{
			response.Close();
		}

		return str;
	}
	/** 
	 随同请求POST的参数名称及参数值字典
	 
	 @param url 请求地址
	 @param parameters 参数集合
	 @return 响应返回结果
	*/
	public final String HttpResponsePost_Json(String url, String parameters)
	{
		Encoding encoding = Encoding.GetEncoding("utf-8");
		HttpWebResponse response = this.CreatePostHttpResponse(url, parameters, 10000, null, encoding, null);
		InputStreamReader reader = new InputStreamReader(response.GetResponseStream(), java.nio.charset.StandardCharsets.UTF_8);
		String str = reader.ReadToEnd();
		reader.Dispose();
		reader.close();
		if (response != null)
		{
			response.Close();
		}

		return str;
	}
}