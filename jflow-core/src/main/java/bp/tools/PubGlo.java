package bp.tools;

import bp.da.*;
import bp.en.*;
import bp.sys.*;
import bp.web.*;
import bp.difference.*;
import bp.*;
import java.io.*;

public class PubGlo
{

		///#region 表达式替换
	/** 
	 表达式替换
	 
	 param exp
	 param en
	 @return 
	*/
	public static String DealExp(String exp, Entity en)
	{
		//替换字符
		exp = exp.replace("~", "'");

		if (exp.contains("@") == false)
		{
			return exp;
		}

		//首先替换加; 的。
		exp = exp.replace("@WebUser.No;", WebUser.getNo());
		exp = exp.replace("@WebUser.Name;", WebUser.getName());
		exp = exp.replace("@WebUser.FK_DeptName;", WebUser.getFK_DeptName());
		exp = exp.replace("@WebUser.FK_Dept;", WebUser.getFK_Dept());


		// 替换没有 ; 的 .
		exp = exp.replace("@WebUser.No", WebUser.getNo());
		exp = exp.replace("@WebUser.Name", WebUser.getName());
		exp = exp.replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());
		exp = exp.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

		if (exp.contains("@") == false)
		{
			return exp;
		}

		//增加对新规则的支持. @MyField; 格式.
		if (en != null)
		{
			Attrs attrs = en.getEnMap().getAttrs();
			Row row = en.getRow();
			//特殊判断.
			if (row.containsKey("OID") == true)
			{
				exp = exp.replace("@WorkID", row.get("OID").toString());
			}

			if (exp.contains("@") == false)
			{
				return exp;
			}

			for (Object key : row.keySet())
			{
				//值为空或者null不替换
				if (row.get(key) == null || row.get(key).equals("") == true)
				{
					continue;
				}

				if (exp.contains("@" + key))
				{
					Attr attr = attrs.GetAttrByKeyOfEn(String.valueOf(key));
					//是枚举或者外键替换成文本
					if (attr.getMyFieldType() == FieldType.Enum || attr.getMyFieldType() == FieldType.PKEnum || attr.getMyFieldType() == FieldType.FK || attr.getMyFieldType() == FieldType.PKFK)
					{
						exp = exp.replace("@" + key, row.get(key + "Text").toString());
					}
					else
					{
						if (attr.getMyDataType() == DataType.AppString && attr.getUIContralType() == UIContralType.DDL && attr.getMyFieldType() == FieldType.Normal)
						{
							exp = exp.replace("@" + key, row.get(key + "T").toString());
						}
						else
						{
							exp = exp.replace("@" + key, row.get(key).toString());
						}
						;
					}


				}

				//不包含@则返回SQL语句
				if (exp.contains("@") == false)
				{
					return exp;
				}
			}

		}

		if (exp.contains("@") && SystemConfig.getIsBSsystem() == true)
		{
			/*如果是bs*/
			for (String key : ContextHolderUtils.getRequest().getParameterMap().keySet())
			{
				if (DataType.IsNullOrEmpty(key))
				{
					continue;
				}
				exp = exp.replace("@" + key, ContextHolderUtils.getRequest().getParameter(key.toString()));
			}
		}

		exp = exp.replace("~", "'");
		return exp;
	}

		///#endregion 表达式替换


		///#region http请求
	/** 
	 Http Get请求
	 
	 param url
	 @return 
	*/
//	public static String HttpGet(String url)
//	{
//		try
//		{
//			HttpWebRequest request;
//			// 创建一个HTTP请求
//			request = (HttpWebRequest)WebRequest.Create(url);
//			// request.Method="get";
//			HttpWebResponse response;
//			response = (HttpWebResponse)request.GetResponse();
//			InputStreamReader myreader = new InputStreamReader(response.GetResponseStream(), java.nio.charset.StandardCharsets.UTF_8);
//			String responseText = myreader.ReadToEnd();
//			myreader.close();
//			response.Close();
//			return responseText;
//		}
//		catch (RuntimeException ex)
//		{
//			//url请求失败
//			return ex.getMessage();
//		}
//	}
	/** 
	 httppost方式发送数据
	 
	 param url 要提交的url
	 param postDataStr
	 param timeOut 超时时间
	 param encode text code.
	 @return 成功：返回读取内容；失败：0
	*/

//暂时没时间改
//	public static String HttpPostConnect(String serverUrl, String postData)
//	{
//		var dataArray = postData.getBytes(java.nio.charset.StandardCharsets.UTF_8);
//		//创建请求
//		var request = (HttpWebRequest)HttpWebRequest.Create(serverUrl);
//		request.Method = "POST";
//		request.ContentLength = dataArray.Length;
//		//设置上传服务的数据格式  设置之后不好使
//		//request.ContentType = "application/x-www-form-urlencoded";
//		//请求的身份验证信息为默认
//		request.Credentials = CredentialCache.DefaultCredentials;
//		request.ContentType = "application/x-www-form-urlencoded";
//		//请求超时时间
//		request.Timeout = 10000;
//		//创建输入流
////C# TO JAVA CONVERTER TODO TASK: C# to Java Converter cannot determine whether this System.IO.Stream is input or output:
//		Stream dataStream;
//		try
//		{
//			dataStream = request.GetRequestStream();
//		}
//		catch (RuntimeException e)
//		{
//			return "0"; //连接服务器失败
//		}
//		//发送请求
//		dataStream.Write(dataArray, 0, dataArray.Length);
//		dataStream.Close();
//
//		HttpWebResponse res;
//		try
//		{
//			res = (HttpWebResponse)request.GetResponse();
//		}
//		catch (WebException ex)
//		{
//			res = (HttpWebResponse)ex.Response;
//		}
//		InputStreamReader sr = new InputStreamReader(res.GetResponseStream(), java.nio.charset.StandardCharsets.UTF_8);
//		//读取返回消息
//		String data = sr.ReadToEnd();
//		sr.close();
//		return data;
//	}
}

	///#endregion http请求
