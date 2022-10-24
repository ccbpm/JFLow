package bp.wf.difference;

import bp.*;
import bp.wf.*;

public class WF_DynamicWebService
{
	/** 
	 动态调用web服务
	 
	 param url
	 param classname
	 param methodname
	 param args
	 @return 
	*/

//	暂时未使用，使用的时候再翻译
//	public static Object InvokeWebService(String url, String className, String methodName, Object[] args)
//	{
//		 CookieContainer container = new CookieContainer();
//		String namespace = "EnterpriseServerBase.WebService.DynamicWebCalling";
//		if ((className == null) || (className.equals("")))
//		{
//			className = GetWsClassName(url);
//		}
//		try
//		{
//			//获取WSDL
//			WebClient wc = new WebClient();
////C# TO JAVA CONVERTER TODO TASK: C# to Java Converter cannot determine whether this System.IO.Stream is input or output:
//			Stream stream = wc.OpenRead(url + "?WSDL");
//			ServiceDescription sd = ServiceDescription.Read(stream);
//			ServiceDescriptionImporter sdi = new ServiceDescriptionImporter();
//			sdi.AddServiceDescription(sd, "", "");
//			CodeNamespace cn = new CodeNamespace(namespace);
//
//			//生成客户端代理类代码
//			CodeCompileUnit ccu = new CodeCompileUnit();
//			ccu.Namespaces.Add(cn);
//			sdi.Import(cn, ccu);
//			CodeDomProvider icc = CodeDomProvider.CreateProvider("CSharp");
//			//CSharpCodeProvider csc = new CSharpCodeProvider();
//			//ICodeCompiler icc = csc.CreateCompiler();
//
//			//设定编译参数
//			CompilerParameters cplist = new CompilerParameters();
//			cplist.GenerateExecutable = false;
//			cplist.GenerateInMemory = true;
//			cplist.ReferencedAssemblies.Add("System.dll");
//			cplist.ReferencedAssemblies.Add("System.XML.dll");
//			cplist.ReferencedAssemblies.Add("System.Web.Services.dll");
//			cplist.ReferencedAssemblies.Add("System.Data.dll");
//			//编译代理类
//			CompilerResults cr = icc.CompileAssemblyFromDom(cplist, ccu);
//			if (true == cr.Errors.HasErrors)
//			{
//				StringBuilder sb = new StringBuilder();
//				for (System.CodeDom.Compiler.CompilerError ce : cr.Errors)
//				{
//					sb.append(ce.toString());
//					sb.append(System.lineSeparator());
//				}
//				throw new RuntimeException(sb.toString());
//			}
//
//			//生成代理实例，并调用方法
//			System.Reflection.Assembly assembly = cr.CompiledAssembly;
//			java.lang.Class t = assembly.GetType(namespace + "." + className, true, true);
//			Object obj = t.newInstance();
//
//			//设置CookieContainer 1987raymond添加
//			PropertyInfo property = t.GetProperty("CookieContainer");
//			property.SetValue(obj, container, null);
//
//			java.lang.reflect.Method mi = t.getMethod(methodName);
//			return mi.Invoke(obj, args);
//		}
//		catch (RuntimeException ex)
//		{
//			throw ex;
//		}
//	}
//	private static String GetWsClassName(String wsUrl)
//	{
//		String[] parts = wsUrl.split("[/]", -1);
//		String[] pps = parts[parts.length - 1].split("[.]", -1);
//		return pps[0];
//	}
}