package BP.WF.NetPlatformImpl;

import BP.Sys.*;
import BP.WF.HttpHandler.*;
import BP.WF.*;
import java.util.*;

   public class WF_Admin_FoolFormDesigner
   {
		/** 
		 获取webservice方法列表
		 
		 @param dbsrc WebService数据源
		 @return 
		*/
		public static ArrayList<WSMethod> GetWebServiceMethods(SFDBSrc dbsrc)
		{
				if (dbsrc == null || DataType.IsNullOrEmpty(dbsrc.IP))
				{
					return new ArrayList<WSMethod>();
				}

				String wsurl = dbsrc.IP.toLowerCase();
				if (!wsurl.endsWith(".asmx") && !wsurl.endsWith(".svc"))
				{
					throw new RuntimeException("@失败:" + dbsrc.No + " 中WebService地址不正确。");
				}

				wsurl += wsurl.endsWith(".asmx") ? "?wsdl" : "?singleWsdl";


				///#region //解析WebService所有方法列表
				//var methods = new Dictionary<string, string>(); //名称Name，全称Text
				ArrayList<WSMethod> mtds = new ArrayList<WSMethod>();
				WSMethod mtd = null;
				WebClient wc = new WebClient();
//C# TO JAVA CONVERTER TODO TASK: C# to Java Converter cannot determine whether this System.IO.Stream is input or output:
				System.IO.Stream stream = wc.OpenRead(wsurl);
				System.Web.Services.Description.ServiceDescription sd = ServiceDescription.Read(stream);
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
				var eles = sd.Types.Schemas[0].Elements.Values.<XmlSchemaElement>Cast();
				StringBuilder s = new StringBuilder();
				XmlSchemaComplexType ctype = null;
				XmlSchemaSequence seq = null;
				XmlSchemaElement res = null;

//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
				for (var ele : eles)
				{
					if (ele == null)
					{
						continue;
					}

					String resType = "";
					String mparams = "";

					//获取接口返回元素
					res = eles.FirstOrDefault(o -> o.Name == (ele.Name + "Response"));

					if (res != null)
					{
						mtd = new WSMethod();
						//1.接口名称 ele.Name
						mtd.setNo(ele.Name);
						mtd.setParaMS(new HashMap<String, String>());
						//2.接口返回值类型
						System.Xml.Schema.XmlSchemaType tempVar = res.SchemaType;
						ctype = tempVar instanceof XmlSchemaComplexType ? (XmlSchemaComplexType)tempVar : null;
						System.Xml.Schema.XmlSchemaParticle tempVar2 = ctype.Particle;
						seq = tempVar2 instanceof XmlSchemaSequence ? (XmlSchemaSequence)tempVar2 : null;

						if (seq != null && seq.Items.size() > 0)
						{
							mtd.setReturn(resType = (seq.Items[0] instanceof XmlSchemaElement ? (XmlSchemaElement)seq.Items[0] : null).SchemaTypeName.Name);
						}
						else
						{
							continue; // resType = "void";   //去除不返回结果的接口
						}

						//3.接口参数
						ctype = ele.SchemaType instanceof XmlSchemaComplexType ? (XmlSchemaComplexType)ele.SchemaType : null;
						System.Xml.Schema.XmlSchemaParticle tempVar3 = ctype.Particle;
						seq = tempVar3 instanceof XmlSchemaSequence ? (XmlSchemaSequence)tempVar3 : null;

						if (seq != null && seq.Items.size() > 0)
						{
							for (XmlSchemaElement pe : seq.Items)
							{
								mparams += pe.SchemaTypeName.Name + " " + pe.Name + ", ";
								mtd.getParaMS().put(pe.Name, pe.SchemaTypeName.Name);
							}

							mparams = tangible.StringHelper.trimEnd(mparams, ", ".toCharArray());
						}

						mtd.setName(String.format("%1$s %2$s(%3$s)", resType, ele.Name, mparams));
						mtds.add(mtd);
						//methods.Add(ele.Name, string.Format("{0} {1}({2})", resType, ele.Name, mparams));
					}
				}

				stream.Close();
				stream.Dispose();
				wc.Dispose();

				///#endregion

				return mtds;
		}
   }