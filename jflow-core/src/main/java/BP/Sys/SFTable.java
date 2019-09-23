package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.Web.*;
import java.util.*;

/** 
 用户自定义表
*/
public class SFTable extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 数据源属性.
	/** 
	 获得外部数据表
	*/
	public final DataTable GenerHisDataTable()
	{
		//创建数据源.
		SFDBSrc src = new SFDBSrc(this.getFK_SFDBSrc());

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region BP类
		if (this.getSrcType() == SrcType.BPClass)
		{
			Entities ens = ClassFactory.GetEns(this.getNo());
			return ens.RetrieveAllToTable();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region  WebServices
		// this.SrcType == Sys.SrcType.WebServices，by liuxc 
		//暂只考虑No,Name结构的数据源，2015.10.04，added by liuxc
		if (this.getSrcType() == SrcType.WebServices)
		{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
			var td = this.getTableDesc().split("[,]", -1); //接口名称,返回类型
			String tempVar = this.getSelectStatement();
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
			var ps = (tempVar != null ? tempVar : "").split("[&]", -1);
			ArrayList args = new ArrayList();
			String[] pa = null;

//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
			for (var p : ps)
			{
				if (DataType.IsNullOrEmpty(p))
				{
					continue;
				}

				pa = p.Split("[=]", -1);
				if (pa.length != 2)
				{
					continue;
				}

				//此处要SL中显示表单时，会有问题
				try
				{
					if (pa[1].contains("@WebUser.getNo()"))
					{
						pa[1] = pa[1].replace("@WebUser.getNo()", WebUser.getNo());
					}
					if (pa[1].contains("@WebUser.getName()"))
					{
						pa[1] = pa[1].replace("@WebUser.getName()", WebUser.getName());
					}
					if (pa[1].contains("@WebUser.getFK_Dept()"))
					{
						pa[1] = pa[1].replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
					}
					if (pa[1].contains("@WebUser.getFK_DeptName"))
					{
						pa[1] = pa[1].replace("@WebUser.getFK_DeptName", WebUser.getFK_DeptName());
					}
				}
				catch (java.lang.Exception e)
				{
				}

				if (pa[1].contains("@WorkID"))
				{
					String tempVar2 = HttpContextHelper.RequestParams("WorkID");
					pa[1] = pa[1].replace("@WorkID", tempVar2 != null ? tempVar2 : "");
				}
				if (pa[1].contains("@NodeID"))
				{
					String tempVar3 = HttpContextHelper.RequestParams("NodeID");
					pa[1] = pa[1].replace("@NodeID", tempVar3 != null ? tempVar3 : "");
				}
				if (pa[1].contains("@FK_Node"))
				{
					String tempVar4 = HttpContextHelper.RequestParams("FK_Node");
					pa[1] = pa[1].replace("@FK_Node", tempVar4 != null ? tempVar4 : "");
				}
				if (pa[1].contains("@FK_Flow"))
				{
					String tempVar5 = HttpContextHelper.RequestParams("FK_Flow");
					pa[1] = pa[1].replace("@FK_Flow", tempVar5 != null ? tempVar5 : "");
				}
				if (pa[1].contains("@FID"))
				{
					String tempVar6 = HttpContextHelper.RequestParams("FID");
					pa[1] = pa[1].replace("@FID", tempVar6 != null ? tempVar6 : "");
				}

				args.add(pa[1]);
			}

			Object result = InvokeWebService(src.getIP(), td[0], args.toArray(new Object[0]));

			switch (td[1])
			{
				case "DataSet":
					return result == null ? new DataTable() : (result instanceof DataSet ? (DataSet)result : null).Tables[0];
				case "DataTable":
					return result instanceof DataTable ? (DataTable)result : null;
				case "Json":
					LitJson.JsonData jdata = LitJson.JsonMapper.ToObject(result instanceof String ? (String)result : null);

					if (!jdata.getIsArray())
					{
						throw new RuntimeException("@返回的JSON格式字符串“" + (result != null ? result : "") + "”不正确");
					}

					DataTable dt = new DataTable();
					dt.Columns.Add("No", String.class);
					dt.Columns.Add("Name", String.class);

					for (int i = 0; i < jdata.size(); i++)
					{
						dt.Rows.add(jdata.get(i)["No"].toString(), jdata.get(i)["Name"].toString());
					}

					return dt;
				case "Xml":
					if (result == null || DataType.IsNullOrEmpty(result.toString()))
					{
						throw new RuntimeException("@返回的XML格式字符串不正确。");
					}

					XmlDocument xml = new XmlDocument();
					xml.LoadXml(result instanceof String ? (String)result : null);

					XmlNode root = null;

					if (xml.ChildNodes.size() < 2)
					{
						root = xml.ChildNodes[0];
					}
					else
					{
						root = xml.ChildNodes[1];
					}

					dt = new DataTable();
					dt.Columns.Add("No", String.class);
					dt.Columns.Add("Name", String.class);

					for (XmlNode node : root.ChildNodes)
					{
						dt.Rows.add(node.SelectSingleNode("No").InnerText, node.SelectSingleNode("Name").InnerText);
					}

					return dt;
				default:
					throw new RuntimeException("@不支持的返回类型" + td[1]);
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region SQL查询.外键表/视图，edited by liuxc,2016-12-29
		if (this.getSrcType() == Sys.SrcType.TableOrView)
		{
			String sql = "SELECT " + this.getColumnValue() + " No, " + this.getColumnText() + " Name";
			if (this.getCodeStruct() == Sys.CodeStruct.Tree)
			{
				sql += ", " + this.getParentValue() + " ParentNo";
			}

			sql += " FROM " + this.getSrcTable();
			return src.RunSQLReturnTable(sql);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion SQL查询.外键表/视图，edited by liuxc,2016-12-29

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 动态SQL，edited by liuxc,2016-12-29
		if (this.getSrcType() == Sys.SrcType.SQL)
		{
			String runObj = this.getSelectStatement();

			if (DataType.IsNullOrEmpty(runObj))
			{
				throw new RuntimeException("@外键类型SQL配置错误," + this.getNo() + " " + this.getName() + " 是一个(SQL)类型(" + this.GetValStrByKey("SrcType") + ")，但是没有配置sql.");
			}

			if (runObj == null)
			{
				runObj = "";
			}

			runObj = runObj.replace("~", "'");
			if (runObj.contains("@WebUser.getNo()"))
			{
				runObj = runObj.replace("@WebUser.getNo()", WebUser.getNo());
			}

			if (runObj.contains("@WebUser.getName()"))
			{
				runObj = runObj.replace("@WebUser.getName()", WebUser.getName());
			}

			if (runObj.contains("@WebUser.getFK_Dept()"))
			{
				runObj = runObj.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
			}

			DataTable dt = src.RunSQLReturnTable(runObj);
			return dt;
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 自定义表.
		if (this.getSrcType() == Sys.SrcType.CreateTable)
		{
			String sql = "SELECT No, Name FROM " + this.getNo();
			return src.RunSQLReturnTable(sql);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

		return null;

	}
	public final String GenerHisJson()
	{
		return BP.Tools.Json.ToJson(this.GenerHisDataTable());
	}
	/** 
	 自动生成编号
	 
	 @return 
	*/
	public final String GenerSFTableNewNo()
	{
		String table = this.getSrcTable();
		try
		{
			String sql = null;
			String field = "No";
			switch (this.getEnMap().getEnDBUrl().getDBType())
			{
				case MSSQL:
					sql = "SELECT CONVERT(INT, MAX(CAST(" + field + " as int)) )+1 AS No FROM " + table;
					break;
				case PostgreSQL:
					sql = "SELECT to_number( MAX(" + field + ") ,'99999999')+1   FROM " + table;
					break;
				case Oracle:
					sql = "SELECT MAX(" + field + ") +1 AS No FROM " + table;
					break;
				case MySQL:
					sql = "SELECT CONVERT(MAX(CAST(" + field + " AS SIGNED INTEGER)),SIGNED) +1 AS No FROM " + table;
					break;
				case Informix:
					sql = "SELECT MAX(" + field + ") +1 AS No FROM " + table;
					break;
				case Access:
					sql = "SELECT MAX( [" + field + "]) +1 AS  No FROM " + table;
					break;
				default:
					throw new RuntimeException("error");
			}
			String str = String.valueOf(DBAccess.RunSQLReturnValInt(sql, 1));
			if (str.equals("0") || str.equals(""))
			{
				str = "1";
			}
			return tangible.StringHelper.padLeft(str, 3, '0');
		}
		catch (RuntimeException e)
		{
			return "";
		}
	}
	/** 
	 实例化 WebServices
	 
	 @param url WebServices地址
	 @param methodname 调用的方法
	 @param args 把webservices里需要的参数按顺序放到这个object[]里
	*/
	public final Object InvokeWebService(String url, String methodname, Object[] args)
	{
		return null;
/* TODO 2019-07-25 为了合并core，注释掉
            //这里的namespace是需引用的webservices的命名空间，在这里是写死的，大家可以加一个参数从外面传进来。
            string @namespace = "BP.RefServices";
            try
            {
                if (url.EndsWith(".asmx"))
                    url += "?wsdl";
                else if (url.EndsWith(".svc"))
                    url += "?singleWsdl";

                //获取WSDL
                WebClient wc = new WebClient();
                Stream stream = wc.OpenRead(url);
                ServiceDescription sd = ServiceDescription.Read(stream);
                string classname = sd.Services[0].Name;
                ServiceDescriptionImporter sdi = new ServiceDescriptionImporter();
                sdi.AddServiceDescription(sd, "", "");
                CodeNamespace cn = new CodeNamespace(@namespace);

                //生成客户端代理类代码
                CodeCompileUnit ccu = new CodeCompileUnit();
                ccu.Namespaces.Add(cn);
                sdi.Import(cn, ccu);
                CSharpCodeProvider csc = new CSharpCodeProvider();
                ICodeCompiler icc = csc.CreateCompiler();

                //设定编译参数
                CompilerParameters cplist = new CompilerParameters();
                cplist.GenerateExecutable = false;
                cplist.GenerateInMemory = true;
                cplist.ReferencedAssemblies.Add("System.dll");
                cplist.ReferencedAssemblies.Add("System.XML.dll");
                cplist.ReferencedAssemblies.Add("System.Web.Services.dll");
                cplist.ReferencedAssemblies.Add("System.Data.dll");

                //编译代理类
                CompilerResults cr = icc.CompileAssemblyFromDom(cplist, ccu);
                if (true == cr.Errors.HasErrors)
                {
                    System.Text.StringBuilder sb = new System.Text.StringBuilder();
                    foreach (System.CodeDom.Compiler.CompilerError ce in cr.Errors)
                    {
                        sb.Append(ce.ToString());
                        sb.Append(System.Environment.NewLine);
                    }
                    throw new Exception(sb.ToString());
                }

                //生成代理实例，并调用方法
                System.Reflection.Assembly assembly = cr.CompiledAssembly;
                Type t = assembly.GetType(@namespace + "." + classname, true, true);
                object obj = Activator.CreateInstance(t);
                System.Reflection.MethodInfo mi = t.GetMethod(methodname);

                return mi.Invoke(obj, args);
            }
            catch
            {
                return null;
            }
*/
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 链接到其他系统获取数据的属性
	/** 
	 数据源
	*/
	public final String getFK_SFDBSrc()
	{
		return this.GetValStringByKey(SFTableAttr.FK_SFDBSrc);
	}
	public final void setFK_SFDBSrc(String value)
	{
		this.SetValByKey(SFTableAttr.FK_SFDBSrc, value);
	}
	public final String getFK_SFDBSrcT()
	{
		return this.GetValRefTextByKey(SFTableAttr.FK_SFDBSrc);
	}
	/** 
	 数据缓存时间
	*/
	public final String getRootVal()
	{
		return this.GetValStringByKey(SFTableAttr.RootVal);
	}
	public final void setRootVal(String value)
	{
		this.SetValByKey(SFTableAttr.RootVal, value);
	}
	/** 
	 同步间隔
	*/
	public final int getCashMinute()
	{
		return this.GetValIntByKey(SFTableAttr.CashMinute);
	}
	public final void setCashMinute(int value)
	{
		this.SetValByKey(SFTableAttr.CashMinute, value);
	}

	/** 
	 物理表名称
	*/
	public final String getSrcTable()
	{
		String str = this.GetValStringByKey(SFTableAttr.SrcTable);
		if (str.equals("") || str == null)
		{
			return this.getNo();
		}
		return str;
	}
	public final void setSrcTable(String value)
	{
		this.SetValByKey(SFTableAttr.SrcTable, value);
	}
	/** 
	 值/主键字段名
	*/
	public final String getColumnValue()
	{
		return this.GetValStringByKey(SFTableAttr.ColumnValue);
	}
	public final void setColumnValue(String value)
	{
		this.SetValByKey(SFTableAttr.ColumnValue, value);
	}
	/** 
	 显示字段/显示字段名
	*/
	public final String getColumnText()
	{
		return this.GetValStringByKey(SFTableAttr.ColumnText);
	}
	public final void setColumnText(String value)
	{
		this.SetValByKey(SFTableAttr.ColumnText, value);
	}
	/** 
	 父结点字段名
	*/
	public final String getParentValue()
	{
		return this.GetValStringByKey(SFTableAttr.ParentValue);
	}
	public final void setParentValue(String value)
	{
		this.SetValByKey(SFTableAttr.ParentValue, value);
	}
	/** 
	 查询语句
	*/
	public final String getSelectStatement()
	{
		return this.GetValStringByKey(SFTableAttr.SelectStatement);
	}
	public final void setSelectStatement(String value)
	{
		this.SetValByKey(SFTableAttr.SelectStatement, value);
	}
	/** 
	 加入日期
	*/
	public final String getRDT()
	{
		return this.GetValStringByKey(SFTableAttr.RDT);
	}
	public final void setRDT(String value)
	{
		this.SetValByKey(SFTableAttr.RDT, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 是否是类
	*/
	public final boolean getIsClass()
	{
		if (this.getNo().contains("."))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/** 
	 是否是树形实体?
	*/
	public final boolean getIsTree()
	{
		if (this.getCodeStruct() == Sys.CodeStruct.NoName)
		{
			return false;
		}
		return true;
	}
	/** 
	 数据源类型
	*/
	public final SrcType getSrcType()
	{
		if (this.getNo().contains("BP.") == true)
		{
			return SrcType.BPClass;
		}
		else
		{
			SrcType src = SrcType.forValue(this.GetValIntByKey(SFTableAttr.SrcType));
			if (src == Sys.SrcType.BPClass)
			{
				return Sys.SrcType.CreateTable;
			}
			return src;
		}
	}
	public final void setSrcType(SrcType value)
	{
		this.SetValByKey(SFTableAttr.SrcType, value.getValue());
	}
	/** 
	 数据源类型名称
	*/
	public final String getSrcTypeText()
	{
		switch (this.getSrcType())
		{
			case TableOrView:
				if (this.getIsClass())
				{
					return "<img src='/WF/Img/Class.png' width='16px' broder='0' />实体类";
				}
				else
				{
					return "<img src='/WF/Img/Table.gif' width='16px' broder='0' />表/视图";
				}
			case SQL:
				return "<img src='/WF/Img/SQL.png' width='16px' broder='0' />SQL表达式";
			case WebServices:
				return "<img src='/WF/Img/WebServices.gif' width='16px' broder='0' />WebServices";
			default:
				return "";
		}
	}
	/** 
	 字典表类型
	 <p>0：NoName类型</p>
	 <p>1：NoNameTree类型</p>
	 <p>2：NoName行政区划类型</p>
	*/
	public final CodeStruct getCodeStruct()
	{
		return CodeStruct.forValue(this.GetValIntByKey(SFTableAttr.CodeStruct));
	}
	public final void setCodeStruct(CodeStruct value)
	{
		this.SetValByKey(SFTableAttr.CodeStruct, value.getValue());
	}
	/** 
	 编码类型
	*/
	public final String getCodeStructT()
	{
		return this.GetValRefTextByKey(SFTableAttr.CodeStruct);
	}
	/** 
	 值
	*/
	public final String getFK_Val()
	{
		return this.GetValStringByKey(SFTableAttr.FK_Val);
	}
	public final void setFK_Val(String value)
	{
		this.SetValByKey(SFTableAttr.FK_Val, value);
	}
	/** 
	 描述
	*/
	public final String getTableDesc()
	{
		return this.GetValStringByKey(SFTableAttr.TableDesc);
	}
	public final void setTableDesc(String value)
	{
		this.SetValByKey(SFTableAttr.TableDesc, value);
	}
	/** 
	 默认值
	*/
	public final String getDefVal()
	{
		return this.GetValStringByKey(SFTableAttr.DefVal);
	}
	public final void setDefVal(String value)
	{
		this.SetValByKey(SFTableAttr.DefVal, value);
	}
	public final EntitiesNoName getHisEns()
	{
		if (this.getIsClass())
		{
			EntitiesNoName ens = (EntitiesNoName)BP.En.ClassFactory.GetEns(this.getNo());
			ens.RetrieveAll();
			return ens;
		}

		BP.En.GENoNames ges = new GENoNames(this.getNo(), this.getName());
		ges.RetrieveAll();
		return ges;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		uac.IsInsert = false;
		return uac;
	}
	/** 
	 用户自定义表
	*/
	public SFTable()
	{
	}
	public SFTable(String mypk)
	{
		this.setNo(mypk);
		try
		{
			this.Retrieve();
		}
		catch (RuntimeException ex)
		{
			switch (this.getNo())
			{
				case "BP.Pub.NYs":
					this.setName("年月");
					this.setFK_Val("FK_NY");
					this.Insert();
					break;
				case "BP.Pub.YFs":
					this.setName("月");
					this.setFK_Val("FK_YF");
					this.Insert();
					break;
				case "BP.Pub.Days":
					this.setName("天");
					this.setFK_Val("FK_Day");
					this.Insert();
					break;
				case "BP.Pub.NDs":
					this.setName("年");
					this.setFK_Val("FK_ND");
					this.Insert();
					break;
				default:
					throw new RuntimeException(ex.getMessage());
			}
		}
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Sys_SFTable", "字典表");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);

		map.AddTBStringPK(SFTableAttr.No, null, "表英文名称", true, false, 1, 200, 20);
		map.AddTBString(SFTableAttr.Name, null, "表中文名称", true, false, 0, 200, 20);

		map.AddDDLSysEnum(SFTableAttr.SrcType, 0, "数据表类型", true, false, SFTableAttr.SrcType, "@0=本地的类@1=创建表@2=表或视图@3=SQL查询表@4=WebServices@5=微服务Handler外部数据源@6=JavaScript外部数据源@7=动态Json");

		map.AddDDLSysEnum(SFTableAttr.CodeStruct, 0, "字典表类型", true, false, SFTableAttr.CodeStruct);
		map.AddTBString(SFTableAttr.RootVal, null, "根节点值", false, false, 0, 200, 20);


		map.AddTBString(SFTableAttr.FK_Val, null, "默认创建的字段名", true, false, 0, 200, 20);
		map.AddTBString(SFTableAttr.TableDesc, null, "表描述", true, false, 0, 200, 20);
		map.AddTBString(SFTableAttr.DefVal, null, "默认值", true, false, 0, 200, 20);


			//数据源.
		map.AddDDLEntities(SFTableAttr.FK_SFDBSrc, "local", "数据源", new BP.Sys.SFDBSrcs(), true);

		map.AddTBString(SFTableAttr.SrcTable, null, "数据源表", false, false, 0, 200, 20);
		map.AddTBString(SFTableAttr.ColumnValue, null, "显示的值(编号列)", false, false, 0, 200, 20);
		map.AddTBString(SFTableAttr.ColumnText, null, "显示的文字(名称列)", false, false, 0, 200, 20);
		map.AddTBString(SFTableAttr.ParentValue, null, "父级值(父级列)", false, false, 0, 200, 20);
		map.AddTBString(SFTableAttr.SelectStatement, null, "查询语句", true, false, 0, 1000, 600, true);

		map.AddTBDateTime(SFTableAttr.RDT, null, "加入日期", false, false);

			//查找.
		map.AddSearchAttr(SFTableAttr.FK_SFDBSrc);

		RefMethod rm = new RefMethod();
		rm.Title = "查看数据";
		rm.ClassMethodName = this.toString() + ".DoEdit";
		rm.RefMethodType = RefMethodType.RightFrameOpen;
		rm.IsForEns = false;
		map.AddRefMethod(rm);

			//rm = new RefMethod();
			//rm.Title = "创建Table向导";
			//rm.ClassMethodName = this.ToString() + ".DoGuide";
			//rm.RefMethodType = RefMethodType.RightFrameOpen;
			//rm.IsForEns = false;
			//map.AddRefMethod(rm);

			//rm = new RefMethod();
			//rm.Title = "数据源管理";
			//rm.ClassMethodName = this.ToString() + ".DoMangDBSrc";
			//rm.RefMethodType = RefMethodType.RightFrameOpen;
			//rm.IsForEns = false;
			//map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 数据源管理
	 
	 @return 
	*/
	public final String DoMangDBSrc()
	{
		return SystemConfig.getCCFlowWebPath() + "WF/Comm/Sys/SFDBSrcNewGuide.htm";
	}
	/** 
	 创建表向导
	 
	 @return 
	*/
	public final String DoGuide()
	{
		return SystemConfig.getCCFlowWebPath() + "WF/Admin/FoolFormDesigner/CreateSFGuide.htm";
	}
	/** 
	 编辑数据
	 
	 @return 
	*/
	public final String DoEdit()
	{
		if (this.getIsClass())
		{
			return SystemConfig.getCCFlowWebPath() + "WF/Comm/Ens.htm?EnsName=" + this.getNo();
		}
		else
		{
			return SystemConfig.getCCFlowWebPath() + "WF/Admin/FoolFormDesigner/SFTableEditData.htm?FK_SFTable=" + this.getNo();
		}
	}
	public final String IsCanDelete()
	{
		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.UIBindKey, this.getNo());
		if (attrs.size() != 0)
		{
			String err = "";
			for (MapAttr item : attrs)
			{
				err += " @ " + item.getMyPK() + " " + item.getName();
			}
			return "err@如下实体字段在引用:" + err + "。您不能删除该表。";
		}
		return null;
	}
	@Override
	protected boolean beforeDelete()
	{
		String delMsg = this.IsCanDelete();
		if (delMsg != null)
		{
			throw new RuntimeException(delMsg);
		}

		return super.beforeDelete();
	}
	@Override
	protected boolean beforeInsert()
	{
		//利用这个时间串进行排序.
		this.setRDT(DataType.getCurrentDataTime());


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 如果是本地类. @于庆海.
		if (this.getSrcType() == Sys.SrcType.BPClass)
		{
			Entities ens = ClassFactory.GetEns(this.getNo());
			Entity en = ens.getNewEntity();
			this.setName(en.getEnDesc());

			//检查是否是树结构.
			if (en.getIsTreeEntity() == true)
			{
				this.setCodeStruct(Sys.CodeStruct.Tree);
			}
			else
			{
				this.setCodeStruct(Sys.CodeStruct.NoName);
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 如果是本地类.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 本地类，物理表..
		if (this.getSrcType() == Sys.SrcType.CreateTable)
		{
			if (DBAccess.IsExitsObject(this.getNo()) == true)
			{
				return super.beforeInsert();
				//throw new Exception("err@表名[ " + this.getNo()+ " ]已经存在，请使用其他的表名.");
			}

			String sql = "";
			if (this.getCodeStruct() == Sys.CodeStruct.NoName || this.getCodeStruct() == Sys.CodeStruct.GradeNoName)
			{
				sql = "CREATE TABLE " + this.getNo() + " (";
				sql += "No varchar(30) NOT NULL,";
				sql += "Name varchar(3900) NULL";
				sql += ")";
			}

			if (this.getCodeStruct() == Sys.CodeStruct.Tree)
			{
				sql = "CREATE TABLE " + this.getNo() + " (";
				sql += "No varchar(30) NOT NULL,";
				sql += "Name varchar(3900)  NULL,";
				sql += "ParentNo varchar(3900)  NULL";
				sql += ")";
			}
			this.RunSQL(sql);

			//初始化数据.
			this.InitDataTable();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 如果是本地类.

		return super.beforeInsert();
	}

	@Override
	protected void afterInsert()
	{
		try
		{
			if (this.getSrcType() == Sys.SrcType.TableOrView)
			{
				//暂时这样处理
				String sql = "CREATE VIEW " + this.getNo() + " (";
				sql += "[No],";
				sql += "[Name]";
				sql += (this.getCodeStruct() == Sys.CodeStruct.Tree ? ",[ParentNo])" : ")");
				sql += " AS ";
				sql += "SELECT " + this.getColumnValue() + " No," + this.getColumnText() + " Name" + (this.getCodeStruct() == Sys.CodeStruct.Tree ? ("," + this.getParentValue() + " ParentNo") : "") + " FROM " + this.getSrcTable() + (DataType.IsNullOrEmpty(this.getSelectStatement()) ? "" : (" WHERE " + this.getSelectStatement()));

				if (Sys.SystemConfig.getAppCenterDBType() == DBType.MySQL)
				{
					sql = sql.replace("[", "`").replace("]", "`");
				}
				else
				{
					sql = sql.replace("[", "").replace("]", "");
				}
				this.RunSQL(sql);
			}

			//if (this.SrcType == Sys.SrcType.SQL)
			//{
			//    //暂时这样处理
			//    string sql = "CREATE VIEW  " + this.getNo()+ "  (";
			//    sql += "[No],";
			//    sql += "[Name]";
			//    sql += (this.CodeStruct == Sys.CodeStruct.Tree ? ",[ParentNo])" : ")");
			//    sql += " AS ";
			//    sql += this.SelectStatement;

			//    if (Sys.SystemConfig.getAppCenterDBType() == DBType.MySQL)
			//    {
			//        sql = sql.Replace("[", "`").replace("]", "`");
			//    }
			//    else
			//    {
			//        sql = sql.Replace("[", "").replace("]", "");
			//    }
			//    this.RunSQL(sql);
			//}
		}
		catch (RuntimeException ex)
		{
			//创建视图失败时，删除此记录，并提示错误
			this.DirectDelete();
			throw ex;
		}

		super.afterInsert();
	}

	/** 
	 获得该数据源的数据
	 
	 @return 
	*/
	public final DataTable GenerData_bak()
	{
		String sql = "";
		DataTable dt = null;
		if (this.getSrcType() == Sys.SrcType.CreateTable)
		{
			sql = "SELECT No,Name FROM " + this.getSrcTable();
			dt = this.RunSQLReturnTable(sql);
		}

		if (this.getSrcType() == Sys.SrcType.TableOrView)
		{
			sql = "SELECT No,Name FROM " + this.getSrcTable();
			dt = this.RunSQLReturnTable(sql);
		}

		if (dt == null)
		{
			throw new RuntimeException("@没有判断的数据.");
		}

		dt.Columns[0].ColumnName = "No";
		dt.Columns[1].ColumnName = "Name";

		return dt;
	}
	/** 
	 返回json.
	 
	 @return 
	*/
	public final String GenerDataOfJson()
	{
		return BP.Tools.Json.ToJson(this.GenerHisDataTable());
	}
	/** 
	 初始化数据.
	*/
	public final void InitDataTable()
	{
		DataTable dt = this.GenerHisDataTable();

		String sql = "";
		if (dt.Rows.size() == 0)
		{
			/*初始化数据.*/
			if (this.getCodeStruct() == Sys.CodeStruct.Tree)
			{
				sql = "INSERT INTO " + this.getSrcTable() + " (No,Name,ParentNo) VALUES('1','" + this.getName() + "','0') ";
				this.RunSQL(sql);

				for (int i = 1; i < 4; i++)
				{
					String no = String.valueOf(i);
					no = tangible.StringHelper.padLeft(no, 3, '0');

					sql = "INSERT INTO " + this.getSrcTable() + " (No,Name,ParentNo) VALUES('" + no + "','Item" + no + "','1') ";
					this.RunSQL(sql);
				}
			}

			if (this.getCodeStruct() == Sys.CodeStruct.NoName)
			{
				for (int i = 1; i < 4; i++)
				{
					String no = String.valueOf(i);
					no = tangible.StringHelper.padLeft(no, 3, '0');
					sql = "INSERT INTO " + this.getSrcTable() + " (No,Name) VALUES('" + no + "','Item" + no + "') ";
					this.RunSQL(sql);
				}
			}
		}
	}

}