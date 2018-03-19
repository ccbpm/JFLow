package BP.WF;

  public class HttpWebResponseUtility{}
//	{
//		private static final String DefaultUserAgent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)";
//		/**   
//		 创建GET方式的HTTP请求  
//		   
//		 @param url 请求的URL  
//		 @param timeout 请求的超时时间  
//		 @param userAgent 请求的客户端浏览器信息，可以为空  
//		 @param cookies 随同HTTP请求发送的Cookie信息，如果不需要身份验证可以为空  
//		 @return   
//		*/
//		public final HttpWebResponse CreateGetHttpResponse(String url, Integer timeout, String userAgent, CookieCollection cookies)
//		{
//			if (StringHelper.isNullOrEmpty(url))
//			{
//				throw new ArgumentNullException("url");
//			}
//			Object tempVar = WebRequest.Create(url);
//			HttpWebRequest request = (HttpWebRequest)((tempVar instanceof HttpWebRequest) ? tempVar : null);
//			request.Method = "GET";
//			request.UserAgent = DefaultUserAgent;
//			if (!StringHelper.isNullOrEmpty(userAgent))
//			{
//				request.UserAgent = userAgent;
//			}
//			if (timeout != null)
//			{
//				request.Timeout = timeout;
//			}
//			if (cookies != null)
//			{
//				request.CookieContainer = new CookieContainer();
//				request.CookieContainer.Add(cookies);
//			}
//			Object tempVar2 = request.GetResponse();
//			return (HttpWebResponse)((tempVar2 instanceof HttpWebResponse) ? tempVar2 : null);
//		}
//		/**   
//		 创建POST方式的HTTP请求  
//		   
//		 @param url 请求的URL  
//		 @param parameters 随同请求POST的参数名称及参数值字典  
//		 @param timeout 请求的超时时间  
//		 @param userAgent 请求的客户端浏览器信息，可以为空  
//		 @param requestEncoding 发送HTTP请求时所用的编码  
//		 @param cookies 随同HTTP请求发送的Cookie信息，如果不需要身份验证可以为空  
//		 @return   
//		*/
//		public static HttpWebResponse CreatePostHttpResponse(String url, java.util.Map<String, String> parameters, Integer timeout, String userAgent, Encoding requestEncoding, CookieCollection cookies)
//		{
//			if (StringHelper.isNullOrEmpty(url))
//			{
//				throw new ArgumentNullException("url");
//			}
//			if (requestEncoding == null)
//			{
//				throw new ArgumentNullException("requestEncoding");
//			}
//			HttpWebRequest request = null;
//			//如果是发送HTTPS请求  
//			if (url.startsWith("https", StringComparison.OrdinalIgnoreCase))
//			{
//				ServicePointManager.ServerCertificateValidationCallback = new RemoteCertificateValidationCallback(CheckValidationResult);
//				Object tempVar = WebRequest.Create(url);
//				request = (HttpWebRequest)((tempVar instanceof HttpWebRequest) ? tempVar : null);
//				request.ProtocolVersion = HttpVersion.Version10;
//			}
//			else
//			{
//				Object tempVar2 = WebRequest.Create(url);
//				request = (HttpWebRequest)((tempVar2 instanceof HttpWebRequest) ? tempVar2 : null);
//			}
//			request.Method = "POST";
//			request.ContentType = "application/x-www-form-urlencoded";
//
//			if (!StringHelper.isNullOrEmpty(userAgent))
//			{
//				request.UserAgent = userAgent;
//			}
//			else
//			{
//				request.UserAgent = DefaultUserAgent;
//			}
//
//			if (timeout != null)
//			{
//				request.Timeout = timeout;
//			}
//			if (cookies != null)
//			{
//				request.CookieContainer = new CookieContainer();
//				request.CookieContainer.Add(cookies);
//			}
//			//如果需要POST数据  
//			if (!(parameters == null || parameters.isEmpty()))
//			{
//				StringBuilder buffer = new StringBuilder();
//				int i = 0;
//				for (String key : parameters.keySet())
//				{
//					if (i > 0)
//					{
//						buffer.append(String.format("&%1$s=%2$s", key, parameters.get(key)));
//					}
//					else
//					{
//						buffer.append(String.format("%1$s=%2$s", key, parameters.get(key)));
//					}
//					i++;
//				}
//				byte[] data = requestEncoding.GetBytes(buffer.toString());
//
////				using (Stream stream = request.GetRequestStream())
//				Stream stream = request.GetRequestStream();
//				try
//				{
//					stream.Write(data, 0, data.length);
//				}
//				finally
//				{
//					stream.dispose();
//				}
//			}
//			Object tempVar3 = request.GetResponse();
//			return (HttpWebResponse)((tempVar3 instanceof HttpWebResponse) ? tempVar3 : null);
//		}
//
//	  /**   
//	   创建POST方式的HTTP请求  (用于微信自动推送)
//		 
//	   @param url 请求的URL  
//	   @param parameters 随同请求POST的参数名称及参数值字典  
//	   @param timeout 请求的超时时间  
//	   @param userAgent 请求的客户端浏览器信息，可以为空  
//	   @param requestEncoding 发送HTTP请求时所用的编码  
//	   @param cookies 随同HTTP请求发送的Cookie信息，如果不需要身份验证可以为空  
//	   @return   
//	  */
//		public final HttpWebResponse WXCreateGetHttpResponse(String url, StringBuilder parameters, Integer timeout, String userAgent, Encoding requestEncoding, CookieCollection cookies)
//	  {
//		  if (StringHelper.isNullOrEmpty(url))
//		  {
//			  throw new ArgumentNullException("url");
//		  }
//		  if (requestEncoding == null)
//		  {
//			  throw new ArgumentNullException("requestEncoding");
//		  }
//		  HttpWebRequest request = null;
//		  //如果是发送HTTPS请求  
//		  if (url.startsWith("https", StringComparison.OrdinalIgnoreCase))
//		  {
//			  ServicePointManager.ServerCertificateValidationCallback = new RemoteCertificateValidationCallback(CheckValidationResult);
//			  Object tempVar = WebRequest.Create(url);
//			  request = (HttpWebRequest)((tempVar instanceof HttpWebRequest) ? tempVar : null);
//			  request.ProtocolVersion = HttpVersion.Version10;
//		  }
//		  else
//		  {
//			  Object tempVar2 = WebRequest.Create(url);
//			  request = (HttpWebRequest)((tempVar2 instanceof HttpWebRequest) ? tempVar2 : null);
//		  }
//		  request.Method = "POST";
//		  request.ContentType = "application/x-www-form-urlencoded";
//
//		  if (!StringHelper.isNullOrEmpty(userAgent))
//		  {
//			  request.UserAgent = userAgent;
//		  }
//		  else
//		  {
//			  request.UserAgent = DefaultUserAgent;
//		  }
//
//		  if (timeout != null)
//		  {
//			  request.Timeout = timeout;
//		  }
//		  if (cookies != null)
//		  {
//			  request.CookieContainer = new CookieContainer();
//			  request.CookieContainer.Add(cookies);
//		  }
//		  if (parameters != null)
//		  {
//			  StringBuilder sb = parameters;
//			  byte[] requestBytes = Encoding.UTF8.GetBytes(sb.toString());
//			  request.ContentLength = requestBytes.length;
//			  Stream requeStream = request.GetRequestStream();
//			  requeStream.Write(requestBytes, 0, requestBytes.length);
//			  requeStream.Close();
//		  }
//		  Object tempVar3 = request.GetResponse();
//		  return (HttpWebResponse)((tempVar3 instanceof HttpWebResponse) ? tempVar3 : null);
//
//	  }
//
//	  private static boolean CheckValidationResult(Object sender, X509Certificate certificate, X509Chain chain, SslPolicyErrors errors)
//		{
//			return true; //总是接受
//		}
//	}