package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.Tools.Json;
import BP.Tools.StringHelper;

/** 
 用户自定义表
 * @param <CodeCompileUnit>
*/
public class SFTable<CodeCompileUnit> extends EntityNoName
{

	/** 
	 获得外部数据表
	 * @throws Exception 
	*/
	public final DataTable getGenerHisDataTable() throws Exception
	{
			//创建数据源.
		SFDBSrc src = new SFDBSrc(this.getFK_SFDBSrc());
		if (this.getSrcType() == BP.Sys.SrcType.BPClass)
		{
			Entities ens = ClassFactory.GetEns(this.getNo());
			return ens.RetrieveAllToTable();
		}
			// this.SrcType == Sys.SrcType.WebServices，by liuxc 
			//暂只考虑No,Name结构的数据源，2015.10.04，added by liuxc
		if (this.getSrcType() == BP.Sys.SrcType.WebServices)
		{
			String[] td = this.getTableDesc().split("[,]", -1); //接口名称,返回类型
			String tempVar = this.getSelectStatement();
			String[] ps = ((tempVar != null) ? tempVar : "").split("[&]", -1);
			java.util.ArrayList args = new java.util.ArrayList();
			String[] pa = null;
			for (String p : ps)
			{
				if (StringHelper.isNullOrWhiteSpace(p))
				{
					continue;
				}

				pa = p.split("[=]", -1);
				if (pa.length != 2)
				{
					continue;
				}

					//此处要SL中显示表单时，会有问题
				try
				{
					if (pa[1].contains("WebUser.No"))
					{
						pa[1] = pa[1].replace("WebUser.No", BP.Web.WebUser.getNo());
					}
					if (pa[1].contains("@WebUser.Name"))
					{
						pa[1] = pa[1].replace("@WebUser.Name", BP.Web.WebUser.getName());
					}
					if (pa[1].contains("@WebUser.FK_Dept"))
					{
						pa[1] = pa[1].replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
					}
					if (pa[1].contains("@WebUser.FK_DeptName"))
					{
						pa[1] = pa[1].replace("@WebUser.FK_DeptName", BP.Web.WebUser.getFK_DeptName());
					}
				}
				catch (java.lang.Exception e)
				{
				}

				if (pa[1].contains("@WorkID"))
				{
					pa[1] = pa[1].replace("@WorkID", (BP.Sys.Glo.getRequest().getParameter("WorkID") != null) ? BP.Sys.Glo.getRequest().getParameter("WorkID") : "");
				}
				if (pa[1].contains("@NodeID"))
				{
					pa[1] = pa[1].replace("@NodeID", (BP.Sys.Glo.getRequest().getParameter("NodeID") != null) ? BP.Sys.Glo.getRequest().getParameter("NodeID") : "");
				}
				if (pa[1].contains("@FK_Node"))
				{
					pa[1] = pa[1].replace("@FK_Node", (BP.Sys.Glo.getRequest().getParameter("FK_Node") != null) ? BP.Sys.Glo.getRequest().getParameter("FK_Node") : "");
				}
				if (pa[1].contains("@FK_Flow"))
				{
					pa[1] = pa[1].replace("@FK_Flow", (BP.Sys.Glo.getRequest().getParameter("FK_Flow") != null) ? BP.Sys.Glo.getRequest().getParameter("FK_Flow") : "");
				}
				if (pa[1].contains("@FID"))
				{
					pa[1] = pa[1].replace("@FID", (BP.Sys.Glo.getRequest().getParameter("FID") != null) ? BP.Sys.Glo.getRequest().getParameter("FID") : "");
				}

				args.add(pa[1]);
			}

			Object result = InvokeWebService(src.getIP(), td[0], args.toArray());

			if("DataSet".equals(td[1]))
			{
					return result == null ? new DataTable() : ((DataSet)((result instanceof DataSet) ? result : null)).Tables.get(0);
			}
			else if("DataTable".equals(td[1]))
			{
					return (DataTable)((result instanceof DataTable) ? result : null);
			}
			else if("Json".equals(td[1]))
			{
				String json = Json.ToJson(((result instanceof String) ? result.toString() : null));

					/*if (!jdata.getIsArray())
					{
						throw new RuntimeException("@返回的JSON格式字符串“" + ((result != null) ? result : "") + "”不正确");
					}*/

					DataTable dt = new DataTable();
					dt.Columns.Add("No", String.class);
					dt.Columns.Add("Name", String.class);

					/*for (int i = 0; i < jdata.getCount(); i++)
					{
						dt.Rows.Add(jdata.getItem(i)["No"].toString(), jdata.getItem(i)["Name"].toString());
					}
*/
					return dt;
			}
			else if (td[1].equals("Xml"))
			{
					if (result == null || StringHelper.isNullOrWhiteSpace(result.toString()))
					{
						throw new RuntimeException("@返回的XML格式字符串不正确。");
					}

					/*XmlDocument xml = new XmlDocument();
					xml.LoadXml((String)((result instanceof String) ? result : null));

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
						dt.Rows.Add(node.SelectSingleNode("No").InnerText, node.SelectSingleNode("Name").InnerText);
					}

					return dt;*/
			}
			else
			{
					throw new RuntimeException("@不支持的返回类型" + td[1]);
			}
		}

			///#endregion


			///#region SQL查询.外键表/视图，edited by liuxc,2016-12-29
		if (this.getSrcType() == BP.Sys.SrcType.SQL || this.getSrcType() == BP.Sys.SrcType.TableOrView)
		{
			String runObj = this.getSelectStatement();

			if (runObj == null)
			{
				runObj = "";
			}

			runObj = runObj.replace("~", "'");
			if (runObj.contains("WebUser.No"))
			{
				runObj = runObj.replace("WebUser.No", BP.Web.WebUser.getNo());
			}

			if (runObj.contains("@WebUser.Name"))
			{
				runObj = runObj.replace("@WebUser.Name", BP.Web.WebUser.getName());
			}

			if (runObj.contains("@WebUser.FK_Dept"))
			{
				runObj = runObj.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
			}


				///#warning  这是写的什么？ 不是说了吗 不采用这样的表达式,不好转java.  写的这样长，谁能看懂了？ 读完这行代码，还能传过来气吗？
			if (this.getSrcType() == BP.Sys.SrcType.TableOrView)
			{
				runObj = "SELECT " + this.getColumnValue() + " No, " + this.getColumnText() + " Name" + (this.getCodeStruct() == BP.Sys.CodeStruct.Tree ? (", " + this.getParentValue() + " ParentNo") : "") + " FROM " + this.getSrcTable() + (StringHelper.isNullOrWhiteSpace(runObj) ? "" : (" WHERE " + runObj));
			}
			return src.RunSQLReturnTable(runObj);
		}
			///#region 自定义表.
		if (this.getSrcType() == BP.Sys.SrcType.CreateTable)
		{
			String sql = "SELECT No, Name FROM " + this.getNo();
			return src.RunSQLReturnTable(sql);
		}
		
		//@浙商银行
		//throw new RuntimeException("@没有判断的数据类型." + this.getSrcType() + " - " + this.getSrcTypeText());
		return null;
	}
	/** 
	 实例化 WebServices
	 @param url WebServices地址
	 @param methodname 调用的方法
	 @param args 把webservices里需要的参数按顺序放到这个object[]里
	*/
	public final Object InvokeWebService(String url, String methodname, Object[] args)
	{
		return args;

		//这里的namespace是需引用的webservices的命名空间，在这里是写死的，大家可以加一个参数从外面传进来。
		/*String namespace = "BP.RefServices";
		try
		{
			if (url.endsWith(".asmx"))
			{
				url += "?wsdl";
			}
			else if (url.endsWith(".svc"))
			{
				url += "?singleWsdl";
			}

			//获取WSDL
			WebClient wc = new WebClient();
			Stream stream = wc.OpenRead(url);
			ServiceDescription sd = ServiceDescription.Read(stream);
			String classname = sd.Services[0].getName();
			ServiceDescriptionImporter sdi = new ServiceDescriptionImporter();
			sdi.AddServiceDescription(sd, "", "");
			CodeNamespace cn = new CodeNamespace(namespace);
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
				StringBuilder sb = new StringBuilder();
				for (System.CodeDom.Compiler.CompilerError ce : cr.Errors)
				{
					sb.append(ce.toString());
					sb.append(System.Environment.NewLine);
				}
				throw new RuntimeException(sb.toString());
			}

			//生成代理实例，并调用方法
			System.Reflection.Assembly assembly = cr.CompiledAssembly;
			java.lang.Class t = assembly.GetType(namespace + "." + classname, true, true);
			Object obj = Activator.CreateInstance(t);
			java.lang.reflect.Method mi = t.getMethod(methodname);

			return mi.invoke(obj, args);
		}
		catch (java.lang.Exception e)
		{
			return null;
		}*/
	}
		//链接到其他系统获取数据的属性
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
		if (this.getCodeStruct() == BP.Sys.CodeStruct.NoName)
		{
			return false;
		}
		return true;
	}
	public final void setIsTree(boolean value)
	{
		this.SetValByKey(SFTableAttr.IsTree, value);
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
			if (src == BP.Sys.SrcType.BPClass)
			{
				return BP.Sys.SrcType.CreateTable;
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
		if(BP.Sys.SrcType.TableOrView.equals(getSrcType()))
		{
			if (this.getIsClass())
			{
				return "<img src='/WF/Img/Class.png' width='16px' broder='0' />实体类";
			}
			else
			{
				return "<img src='/WF/Img/Table.gif' width='16px' broder='0' />表/视图";
			}
		}else if(BP.Sys.SrcType.TableOrView.equals(getSrcType()))
		{
			
		}else if(BP.Sys.SrcType.SQL.equals(getSrcType()))
		{
			return "<img src='/WF/Img/SQL.png' width='16px' broder='0' />SQL表达式";
		}else if(BP.Sys.SrcType.WebServices.equals(getSrcType()))
		{
			return "<img src='/WF/Img/WebServices.gif' width='16px' broder='0' />WebServices";
		}else
		{
			return "";
		}
		return null ;
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
	public final EntitiesNoName getHisEns() throws Exception
	{
		if (this.getIsClass())
		{
			EntitiesNoName ens = (EntitiesNoName)BP.En.ClassFactory.GetEns(this.getNo());
			ens.RetrieveAll();
			return ens;
		}

		BP.Sys.GENoNames ges = new GENoNames(this.getNo(), this.getName());
		ges.RetrieveAll();
		return ges;
	}

	@Override
	public UAC getHisUAC() throws Exception
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
	public SFTable(String mypk) throws Exception
	{
		this.setNo(mypk);
		try
		{
			this.Retrieve();
		}
		catch (RuntimeException ex)
		{
			if (this.getNo().equals("BP.Pub.NYs"))
			{
					this.setName("年月");
					this.setFK_Val("FK_NY");
					this.Insert();
			}
			else if (this.getNo().equals("BP.Pub.YFs"))
			{
					this.setName("月");
					this.setFK_Val("FK_YF");
					this.Insert();
			}
			else if (this.getNo().equals("BP.Pub.Days"))
			{
					this.setName("天");
					this.setFK_Val("FK_Day");
					this.Insert();
			}
			else if (this.getNo().equals("BP.Pub.NDs"))
			{
					this.setName("年");
					this.setFK_Val("FK_ND");
					this.Insert();
			}
			else
			{
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

		map.AddDDLSysEnum(SFTableAttr.SrcType, 0, "数据表类型", true, false, SFTableAttr.SrcType, "@0=本地的类@1=创建表@2=表或视图@3=SQL查询表@4=WebServices@5=微服务Handler外部数据源@6=JavaScript外部数据源");

		map.AddDDLSysEnum(SFTableAttr.CodeStruct, 0, "字典表类型", true, false, SFTableAttr.CodeStruct);

		//根目录值
	map.AddTBString(SFTableAttr.RootVal, null, "根目录值", false, false, 0, 200, 20);


		map.AddTBString(SFTableAttr.FK_Val, null, "默认创建的字段名", true, false, 0, 200, 20);
		map.AddTBString(SFTableAttr.TableDesc, null, "表描述", true, false, 0, 200, 20);
		map.AddTBString(SFTableAttr.DefVal, null, "默认值", true, false, 0, 200, 20);

			//数据源.
		map.AddDDLEntities(SFTableAttr.FK_SFDBSrc, "local", "数据源", new BP.Sys.SFDBSrcs(), true);

		map.AddTBString(SFTableAttr.SrcTable, null, "数据源表", false, false, 0, 200, 20);
		map.AddTBString(SFTableAttr.ColumnValue, null, "显示的值(编号列)", false, false, 0, 200, 20);
		map.AddTBString(SFTableAttr.ColumnText, null, "显示的文字(名称列)", false, false, 0, 200, 20);
		map.AddTBString(SFTableAttr.ParentValue, null, "父级值(父级列)", false, false, 0, 200, 20);
		map.AddTBString(SFTableAttr.SelectStatement, null, "查询语句", false, false, 0, 1000, 600, true);

		map.AddTBDateTime(SFTableAttr.RDT, null, "加入日期", false, false);

			//查找.
		map.AddSearchAttr(SFTableAttr.FK_SFDBSrc);

		RefMethod rm = new RefMethod();
		rm.Title = "查看数据";
		rm.ClassMethodName = this.toString() + ".DoEdit";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.IsForEns = false;
		map.AddRefMethod(rm);

//		rm = new RefMethod();
//		rm.Title = "创建Table向导";
//		rm.ClassMethodName = this.toString() + ".DoGuide";
//		rm.refMethodType = RefMethodType.RightFrameOpen;
//		rm.IsForEns = false;
//		map.AddRefMethod(rm);

			//rm = new RefMethod();
			//rm.Title = "数据源管理";
			//rm.ClassMethodName = this.ToString() + ".DoMangDBSrc";
			//rm.RefMethodType = RefMethodType.RightFrameOpen;
			//rm.IsForEns = false;
			//map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}

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
		return SystemConfig.getCCFlowWebPath() + "WF/Comm/Sys/SFGuide.htm";
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
	@Override
	protected boolean beforeDelete() throws Exception
	{
		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.UIBindKey, this.getNo());
		if (attrs.size() != 0)
		{
			String err = "";
			for (MapAttr item : attrs.ToJavaList())
			{
				err += " @ " + item.getMyPK() + " " + item.getName();
			}
			throw new RuntimeException("@如下实体字段在引用:" + err + "。您不能删除该表。");
		}
		return super.beforeDelete();
	}
	@Override
	protected boolean beforeInsert() throws Exception
	{
		//利用这个时间串进行排序.
		this.setRDT(DataType.getCurrentDataTime());

		if (this.getSrcType() == BP.Sys.SrcType.CreateTable)
		{

			String sql = "";
			if (this.getCodeStruct() == BP.Sys.CodeStruct.NoName || this.getCodeStruct() == BP.Sys.CodeStruct.GradeNoName)
			{
				sql = "CREATE TABLE " + this.getNo() + " (";
				sql += "No varchar(30) NOT NULL,";
				sql += "Name varchar(3900) NULL,";
				sql += "GUID varchar(36)  NULL";

				sql += ")";
			}

			if (this.getCodeStruct() == BP.Sys.CodeStruct.Tree)
			{
				sql = "CREATE TABLE " + this.getNo() + " (";
				sql += "No  varchar(30) NOT NULL,";
				sql += "Name varchar(3900)  NULL,";
				sql += "ParentNo varchar(3900)  NULL,";
				sql += "GUID varchar(36)  NULL";
				sql += ")";
			}
			this.RunSQL(sql);

			//初始化数据.
			this.InitDataTable();
		}

		return super.beforeInsert();
	}

	@Override
	protected void afterInsert() throws Exception
	{
		if (this.getSrcType() == BP.Sys.SrcType.TableOrView)
		{
			//暂时这样处理
			String sql = "CREATE VIEW " + this.getNo() + " (";
			if(SystemConfig.getAppCenterDBType() == BP.DA.DBType.Oracle || SystemConfig.getAppCenterDBType() == BP.DA.DBType.MySQL){
				sql += "No,";
				sql += "Name";
			}
			else{
				sql += "[No],";
				sql += "[Name]";
			}
			sql += (this.getCodeStruct() == BP.Sys.CodeStruct.Tree ? ",[ParentNo])" : ")");
			sql += " AS ";
			sql += "SELECT " + this.getColumnValue() + " No," + this.getColumnText() + " Name" + (this.getCodeStruct() == BP.Sys.CodeStruct.Tree ? ("," + this.getParentValue() + " ParentNo") : "") + " FROM " + this.getSrcTable() + (StringHelper.isNullOrWhiteSpace(this.getSelectStatement()) ? "" : (" WHERE " + this.getSelectStatement()));

			this.RunSQL(sql);
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
		if (this.getSrcType() == BP.Sys.SrcType.CreateTable)
		{
			sql = "SELECT * FROM " + this.getSrcTable();
			return this.RunSQLReturnTable(sql);
		}

		if (this.getSrcType() == BP.Sys.SrcType.TableOrView)
		{
			sql = "SELECT * FROM " + this.getSrcTable();
			return this.RunSQLReturnTable(sql);
		}

		throw new RuntimeException("@没有判断的数据.");
	}

	/** 
	 初始化数据.
	 * @throws Exception 
	 
	*/
	public final void InitDataTable() throws Exception
	{
		DataTable dt = this.getGenerHisDataTable();

		String sql = "";
		if (dt.Rows.size() == 0)
		{
			//初始化数据.
			if (this.getCodeStruct() == BP.Sys.CodeStruct.Tree)
			{
				sql = "INSERT INTO " + this.getSrcTable() + " (No,Name,ParentNo,GUID) VALUES('" + this.getDefVal() + "','根目录','" + this.getDefVal() + "','" + DBAccess.GenerGUID() + "') ";
				this.RunSQL(sql);

				for (int i = 1; i < 4; i++)
				{
					String no = (new Integer(i)).toString();
					no = StringHelper.padLeft("",3, '0');

					sql = "INSERT INTO " + this.getSrcTable() + " (No,Name,ParentNo,GUID) VALUES('" + no + "','Item" + no + "','" + this.getDefVal() + "', '" + DBAccess.GenerGUID() + "') ";
					this.RunSQL(sql);
				}
			}

			if (this.getCodeStruct() == BP.Sys.CodeStruct.NoName)
			{
				for (int i = 1; i < 4; i++)
				{
					String no = (new Integer(i)).toString();
					no = StringHelper.padLeft("",3, '0');
					sql = "INSERT INTO " + this.getSrcTable() + " (No,Name,GUID) VALUES('" + no + "','Item" + no + "','" + DBAccess.GenerGUID() + "') ";
					this.RunSQL(sql);
				}
			}
		}
	}

	 /// 返回json.
    /// </summary>
    /// <returns></returns>
    public String GenerDataOfJson() throws Exception
    {
        return BP.Tools.Json.ToJson(this.getGenerHisDataTable());
    }
}