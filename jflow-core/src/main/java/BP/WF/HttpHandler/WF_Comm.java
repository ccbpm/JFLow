package BP.WF.HttpHandler;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.lexer.PageIndex;

import BP.DA.AtPara;
import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.En.Attr;
import BP.En.AttrOfSearch;
import BP.En.AttrSearch;
import BP.En.AttrSearchs;
import BP.En.Attrs;
import BP.En.ClassFactory;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.FieldType;
import BP.En.FieldTypeS;
import BP.En.Map;
import BP.En.Method;
import BP.En.QueryObject;
import BP.En.RefMethodType;
import BP.En.UAC;
import BP.Sys.DTSearchWay;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.Sys.UserRegedit;
import BP.Tools.StringHelper;
import BP.WF.DotNetToJavaStringHelper;
import BP.WF.Glo;
import BP.WF.HttpHandler.Base.WebContralBase;
import BP.Web.WebUser;

public class WF_Comm extends WebContralBase {

	// <summary>
	// 页面功能实体
	// </summary>
	// <param name="mycontext"></param>

	// #region 统计分析组件.
	// <summary>
	// 初始化数据
	// </summary>
	// <returns></returns>
	public String ContrastDtl_Init() {
		return "";
	}

	// #endregion 统计分析组件.

	// #region 公共类库.
	public String getEnName() {
		return this.GetRequestVal("EnName");
	}

	public String getPKVal() {
		
		  String str = this.GetRequestVal("PKVal");
          if (DataType.IsNullOrEmpty(str) == true)
              str = this.GetRequestVal("OID");

          if (DataType.IsNullOrEmpty(str) == true)
              str = this.GetRequestVal("No");

          if (DataType.IsNullOrEmpty(str) == true)
              str = this.GetRequestVal("MyPK");
          if (DataType.IsNullOrEmpty(str) == true)
              str = this.GetRequestVal("NodeID");

          if (DataType.IsNullOrEmpty(str) == true)
              str = this.GetRequestVal("WorkID");
          
          return str;
          
		//return this.GetRequestVal("PKVal");
	}

	// <summary>
	// 获得实体
	// </summary>
	// <returns></returns>
	public String GEEntity_Init() {
		try {
			Entity en = ClassFactory.GetEn(this.getEnName());
			en.setPKVal(this.getPKVal());
			en.Retrieve();

			return en.ToJson();
		} catch (Exception ex) {
			return "err@" + ex.getMessage();
		}
	}

	// #endregion

	public String Method_Done() {
		String ensName = this.GetRequestVal("M");
		Method rm = BP.En.ClassFactory.GetMethod(ensName);
		int mynum = 0;
		for (Attr attr : rm.getHisAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText)
				continue;
			mynum++;
		}
		int idx = 0;
		for (Attr attr : rm.getHisAttrs()) {
			if (attr.getMyFieldType() == FieldType.RefText)
				continue;
			if (attr.getUIVisible() == false)
				continue;
			try {
				switch (attr.getUIContralType()) {
				case TB:
					switch (attr.getMyDataType()) {
					case BP.DA.DataType.AppString:
					case BP.DA.DataType.AppDate:
					case BP.DA.DataType.AppDateTime:
						String str1 = this.GetValFromFrmByKey(attr.getKey());
						rm.SetValByKey(attr.getKey(), str1);
						break;
					case BP.DA.DataType.AppInt:
						int myInt = this.GetValIntFromFrmByKey(attr.getKey()); // int.Parse(this.UCEn1.GetTBByID("TB_" + attr.Key).Text);
						// rm.Row[idx] = myInt;
						// rm.getRow().put(String.valueOf(idx), Integer.valueOf(myInt));
						rm.getRow().SetValByKey(String.valueOf(idx), Integer.valueOf(myInt));
						rm.SetValByKey(attr.getKey(), myInt);
						break;
					case BP.DA.DataType.AppFloat:
						float myFloat = this.GetValFloatFromFrmByKey(attr.getKey()); // float.Parse(this.UCEn1.GetTBByID("TB_" + attr.Key).Text);
						rm.SetValByKey(attr.getKey(), myFloat);
						break;
					case BP.DA.DataType.AppDouble:
					case BP.DA.DataType.AppMoney:
						BigDecimal myDoub = this.GetValDecimalFromFrmByKey(attr.getKey()); // decimal.Parse(this.UCEn1.GetTBByID("TB_" + attr.Key).Text);
						rm.SetValByKey(attr.getKey(), myDoub);
						break;
					case BP.DA.DataType.AppBoolean:
						boolean myBool = this.GetValBoolenFromFrmByKey(attr.getKey()); // decimal.Parse(this.UCEn1.GetTBByID("TB_" + attr.Key).Text);
						rm.SetValByKey(attr.getKey(), myBool);
						break;
					default:
						return "err@没有判断的数据类型．";
					}
					break;
				case DDL:
					try {
						String str = this.GetValFromFrmByKey(attr.getKey());
						rm.SetValByKey(attr.getKey(), str);
					} catch (Exception e) {
						rm.SetValByKey(attr.getKey(), "");
					}
					break;
				case CheckBok:
					boolean myBoolval = this.GetValBoolenFromFrmByKey(attr
							.getKey());
					rm.SetValByKey(attr.getKey(), myBoolval);
					break;
				default:
					break;
				}
				idx++;
			} catch (Exception ex) {
				return "err@获得参数错误" + "attr=" + attr.getKey() + " attr = "
						+ attr.getKey() + ex.getMessage();
			}
		}

		try {
			Object obj = rm.Do();
			if (obj != null)
				return obj.toString();
			else
				return "err@执行完成没有返回信息.";
		} catch (Exception ex) {
			return "err@执行错误:" + ex.getMessage();
		}
	}

	// #region 界面 .
	// <summary>
	// 实体初始化
	// </summary>
	// <returns></returns>
	public String Entity_Init() {
		try
		{
			String pkval = this.getPKVal();
			Entity en = ClassFactory.GetEn(this.getEnName());
			if (pkval == null || pkval.equals("0") || pkval.equals("") ||  pkval.equals("undefined"))
			{
				Map map = en.getEnMap();
				
                  for(BP.En.Attr  attr : en.getEnMap().getAttrs())
                      en.SetValByKey(attr.getKey(), attr.getDefaultVal());
              
				//设置默认的数据.
				en.ResetDefaultVal();
			}
			else
			{
				en.setPKVal(pkval);
				en.Retrieve();
			}

			return en.ToJson();
		}
		catch(RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}

	public String Entity_Delete() {
		try
		{
		 
			Entity en = ClassFactory.GetEn(this.getEnName());
			
			if (en.getPKCount()!=1)
			{
				//遍历属性，循环赋值.
				for (Attr attr : en.getEnMap().getAttrs())
				{
					en.SetValByKey(attr.getKey(), this.GetRequestVal(attr.getKey()));
				}
				
			}else				
			{
               en.setPKVal( this.getPKVal());
			}
			
            en.RetrieveFromDBSources();
            int num= en.Delete(); 

			return num+""; 
		}
		catch(RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}

	/** 更新
	 
	 @return 
*/
	public final String Entity_Update()
	{
		try
		{
			Entity en = ClassFactory.GetEn(this.getEnName());
			en.setPKVal(this.getPKVal());
			en.RetrieveFromDBSources();

			//遍历属性，循环赋值.
			for (Attr attr : en.getEnMap().getAttrs())
			{
				en.SetValByKey(attr.getKey(), this.GetRequestVal(attr.getKey()));
			}

		 
			
			return en.Update() + ""; //返回影响行数.
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	
	/** 从数据源查询.
	 
	 @return 
*/
	public final String Entity_RetrieveFromDBSources()
	{
		try
		{
			Entity en = ClassFactory.GetEn(this.getEnName());
			en.setPKVal(this.getPKVal());
			int i = en.RetrieveFromDBSources();

			if (i == 0)
			{
				en.ResetDefaultVal();
				en.setPKVal(this.getPKVal());
			}

			if (en.getRow().containsKey("RetrieveFromDBSources") == true)
			{
				en.getRow().SetValByKey("RetrieveFromDBSources", i);
			}
			else
			{
				en.getRow().put("RetrieveFromDBSources", i);
			}

			return en.ToJson(false);
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	
	/** 从数据源查询.
	 
	 @return 
*/
	public final String Entity_Retrieve()
	{
		try
		{
			Entity en = ClassFactory.GetEn(this.getEnName());
			en=en.CreateInstance();
		//	Entity_Retrieve
			en.setPKVal(this.getPKVal());
			int i = en.Retrieve();


			if (en.getRow().containsKey("Retrieve") == true)
			{
				en.getRow().SetValByKey("Retrieve", i);
			}
			else
			{
				en.getRow().put("Retrieve", i);
			}


			return en.ToJson(false);
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	
	/** 是否存在
	 
	 @return 
*/
	public final String Entity_IsExits()
	{
		try
		{
			Entity en = ClassFactory.GetEn(this.getEnName());
			en.setPKVal(this.getPKVal());
			boolean isExit = en.getIsExits();
			
			if (isExit==true)
				return "1";
			return "0"; 
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 执行保存
	 
	 @return 
	*/
	public final String Entity_Save()
	{
		try
		{
			Entity en = ClassFactory.GetEn(this.getEnName());
			en.setPKVal(this.getPKVal());
			en.RetrieveFromDBSources();

			//遍历属性，循环赋值.
			for (Attr attr : en.getEnMap().getAttrs())
			{
				en.SetValByKey(attr.getKey(), this.GetValFromFrmByKey(attr.getKey()));
			}

			//保存参数属性.
			String frmParas = this.GetValFromFrmByKey("frmParas","");
			if (DataType.IsNullOrEmpty(frmParas) == false)
			{
				AtPara ap = new AtPara(frmParas);
				for (String key : ap.getHisHT().keySet())
				{
					en.SetPara(key, ap.GetValStrByKey(key));
				}
			}
			
			return en.Save() + "";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}

	/** 
	 执行插入.
	 
	 @return 
	*/
	public final String Entity_Insert()
	{
		try
		{
			Entity en = ClassFactory.GetEn(this.getEnName());
			//en.setPKVal(this.getPKVal());
			//en.RetrieveFromDBSources();

			//遍历属性，循环赋值.
			for (Attr attr : en.getEnMap().getAttrs())
			{
				en.SetValByKey(attr.getKey(), this.GetValFromFrmByKey(attr.getKey()));
			}

			//插入数据库.
			en.Insert();

			//执行查询.
			en.Retrieve();

			//返回数据.
			return en.ToJson(false);
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}

	public final String Entity_DoMethodReturnString()
	{
		//创建类实体.
		BP.En.Entity en = ClassFactory.GetEn(this.getEnName()); // Activator.CreateInstance(System.Type.GetType("BP.En.Entity")) as BP.En.Entity;
		en.setPKVal(this.getPKVal());
		en.RetrieveFromDBSources();

		String methodName = this.GetRequestVal("MethodName");

		java.lang.Class tp = en.getClass();
		java.lang.reflect.Method mp = null;
		try {
			mp = tp.getMethod(methodName);
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
			return "err@没有找到类[" + this.getEnName() + "]方法[" + methodName + "].";
		} catch (SecurityException e1) {
			e1.printStackTrace();
			return "err@没有找到类[" + this.getEnName() + "]方法[" + methodName + "].";
		}
		if (mp == null)
		{
			return "err@没有找到类[" + this.getEnName() + "]方法[" + methodName + "].";
		}

		String paras = this.GetRequestVal("paras");

		//执行该方法.
		Object[] myparas = new Object[0];

		if (DataType.IsNullOrEmpty(paras) == false)
		{
			myparas = paras.split("[,]", -1);
		}

		Object tempVar = null;
		try {
			tempVar = mp.invoke(en, myparas);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		String result = (String)((tempVar instanceof String) ? tempVar : null); //调用由此 MethodInfo 实例反射的方法或构造函数。
		return result;
	}
	
	
	
	///#region Entities 公共类库.
			/** 
			 调用参数.
			 
			*/
			public final String getParas()
			{
				return this.GetRequestVal("Paras");
			}
			/** 
			 查询全部
			 
			 @return 
			*/
			public final String Entities_RetrieveAll()
			{
				Entities ens = ClassFactory.GetEns(this.getEnsName());
				ens.RetrieveAll();
				return ens.ToJson();
			}
			/** 
			 获得实体集合s
			 
			 @return 
			*/
			public final String Entities_Init()
			{
				try
				{
					Entities ens = ClassFactory.GetEns(this.getEnsName());
					if (this.getParas() == null)
					{
						return "0";
					}

					QueryObject qo = new QueryObject(ens);
					String[] myparas = this.getParas().split("[@]", -1);

					int idx = 0;
					for (int i = 0; i < myparas.length; i++)
					{
						String para = myparas[i];
						if (DataType.IsNullOrEmpty(para) || para.contains("=")==false)
						{
							continue;
						}

						String[] strs = para.split("[=]", -1);
						String key = strs[0];
						String val = strs[1];

						if (key.toLowerCase().equals("orderby") == true)
						{
							qo.addOrderBy(val);
							continue;
						}

						if (idx == 0)
						{
							qo.AddWhere(key, val);
						}
						else
						{
							qo.addAnd();
							qo.AddWhere(key, val);
						}
						idx++;
					}

					qo.DoQuery();
					return ens.ToJson();
				}
				catch (RuntimeException ex)
				{
					return "err@" + ex.getMessage();
				}
			}
	
			///#endregion

	
			///#region 功能执行.
			/** 
			 初始化.
			 
			 @return 
			*/
			public final String Method_Init()
			{
				String ensName = this.GetRequestVal("M");
				Method rm = BP.En.ClassFactory.GetMethod(ensName);
				if (rm == null)
				{
					return "err@方法名错误或者该方法已经不存在" + ensName;
				}

				if (rm.getHisAttrs().size() == 0)
				{
					java.util.Hashtable ht = new java.util.Hashtable();
					ht.put("No", ensName);
					ht.put("Title", rm.Title);
					ht.put("Help", rm.Help);
					ht.put("Warning", rm.Warning);
					return BP.Tools.Json.ToJson(ht);
				}

				DataTable dt = new DataTable();

				//转化为集合.
				MapAttrs attrs = rm.getHisAttrs().ToMapAttrs();

				return "";
			}
			public final String MethodLink_Init()
			{
				java.util.ArrayList al = BP.En.ClassFactory.GetObjects("BP.En.Method");
				int i = 1;
				String html = "";

				//DataTable dt = new DataTable();
				//dt.Columns.Add("No", typeof(string));
				//dt.Columns.Add("Name", typeof(string));
				//dt.Columns.Add("Icon", typeof(string));
				//dt.Columns.Add("Note", typeof(string));
				Iterator it1 = al.iterator();
				while(it1.hasNext()){
					
					BP.En.Method en = (Method) it1.next();
					if (en.getIsCanDo() == false || en.IsVisable == false)
					{
						continue;
					}

				  //DataRow dr = dt.NewRow();
				   String str=	en.toString();
				   
				    str=str.substring(0,str.indexOf('@') );

					html += "<li><a href=\"javascript:ShowIt('" + str+ "');\"  >" + en.GetIcon("/") + en.Title + "</a><br><font size=2 color=Green>" + en.Help + "</font><br><br></li>";
				}
				return html;
			}
	
			///#endregion
	
			
			///#region 查询.
					/** 
					 获得查询的基本信息.
					 
					 @return 
					*/
					public String Search_MapBaseInfo()
					{
						//获得
						Entities ens = ClassFactory.GetEns(this.getEnsName());
						Entity en = ens.getGetNewEntity();
						Map map = ens.getGetNewEntity().getEnMapInTime();

						java.util.Hashtable ht = new java.util.Hashtable();

						//把权限信息放入.
						UAC uac = en.getHisUAC();
						ht.put("IsUpdata", uac.IsUpdate);
						ht.put("IsInsert", uac.IsInsert);
						ht.put("IsDelete", uac.IsDelete);
						ht.put("IsView", uac.IsView);
						ht.put("IsExp", uac.IsExp); //是否可以导出?
						ht.put("IsImp", uac.IsImp); //是否可以导入?
						
			            ht.put("EnDesc", en.getEnDesc()); //描述?
			            
			            ht.put("EnName", en.toString() ); //类名?
 

						//把map信息放入
						ht.put("PhysicsTable", map.getPhysicsTable());
						ht.put("CodeStruct", map.getCodeStruct());
						ht.put("CodeLength", map.getCodeLength());

						//查询条件.
						if (map.IsShowSearchKey==true)
						{
						   ht.put("IsShowSearchKey", 1);
						}
						else
						{
							ht.put("IsShowSearchKey", 0);
						}

						//按日期查询.
						ht.put("DTSearchWay", map.DTSearchWay.getValue());
						ht.put("DTSearchLable", map.DTSearchLable);
						ht.put("DTSearchKey", map.DTSearchKey);

						return BP.Tools.Json.ToJson(ht);
					}
					/** 
					 外键或者枚举的查询.
					 
					 @return 
					*/
					public final String Search_SearchAttrs()
					{
						//获得
						Entities ens = ClassFactory.GetEns(this.getEnsName());
						Entity en = ens.getGetNewEntity();
						Map map = ens.getGetNewEntity().getEnMapInTime();

						DataSet ds = new DataSet();

						//构造查询条件集合.
						DataTable dt = new DataTable();
						dt.Columns.Add("Field");
						dt.Columns.Add("Name");
						dt.TableName = "Attrs";

						AttrSearchs attrs = map.getSearchAttrs();
						for (AttrSearch item : attrs)
						{
							DataRow dr = dt.NewRow();
							dr.setValue("Field",item.Key);
							dr.setValue("Name",item.HisAttr.getDesc());
							dt.Rows.add(dr);
						}
						ds.Tables.add(dt);

						//把外键枚举增加到里面.
						for (AttrSearch item : attrs)
						{
							if (item.HisAttr.getIsEnum() == true)
							{
								SysEnums ses = new SysEnums(item.HisAttr.getUIBindKey());
								DataTable dtEnum = ses.ToDataTableField();
								dtEnum.TableName = item.Key;
								ds.Tables.add(dtEnum);
								continue;
							}

							if (item.HisAttr.getIsFK() == true)
							{
								Entities ensFK = item.HisAttr.getHisFKEns();
								ensFK.RetrieveAll();

								DataTable dtEn = ensFK.ToDataTableField();
								dtEn.TableName = item.Key;

								ds.Tables.add(dtEn);
							}
						}

						return BP.Tools.Json.ToJson(ds);
					}
					 /** 执行查询.
					 
					 @return 
					  */
					public final String Search_SearchIt()
					{
						//获得.
						Entities ens = ClassFactory.GetEns(this.getEnsName());
						Entity en = ens.getGetNewEntity();
						Map map = en.getEnMapInTime();

						MapAttrs attrs = map.getAttrs().ToMapAttrs();

						//属性集合.
						DataTable dtAttrs = attrs.ToDataTableField();
						dtAttrs.TableName = "Attrs";

						DataSet ds = new DataSet();
						ds.Tables.add(dtAttrs); //把描述加入.

						//取出来查询条件.
						BP.Sys.UserRegedit ur = new UserRegedit();
						ur.setMyPK(WebUser.getNo() + "_" + this.getEnsName() + "_SearchAttrs");
						ur.RetrieveFromDBSources();

						//获得关键字.
						AtPara ap = new AtPara(ur.getVals());

						//关键字.
						String keyWord = ur.getSearchKey();
						QueryObject qo = new QueryObject(ens);

						///#region 关键字字段.
						if (en.getEnMap().IsShowSearchKey && DataType.IsNullOrEmpty(keyWord) == false && keyWord.length() > 1)
						{
							Attr attrPK = new Attr();
							for (Attr attr : map.getAttrs())
							{
								if (attr.getIsPK())
								{
									attrPK = attr;
									break;
								}
							}
							int i = 0;
							for (Attr attr : map.getAttrs())
							{
								switch (attr.getMyFieldType())
								{
									case Enum:
									case FK:
									case PKFK:
										continue;
									default:
										break;
								}

								if (attr.getMyDataType() != DataType.AppString)
								{
									continue;
								}

								if (attr.getMyFieldType() == FieldType.RefText)
								{
									continue;
								}

								if (attr.getKey().equals("FK_Dept"))
								{
									continue;
								}

								i++;
								if (i == 1)
								{
									// 第一次进来。 
									qo.addLeftBracket();
									if (SystemConfig.getAppCenterDBVarStr().equals("@"))
									{
										qo.AddWhere(attr.getKey(), " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? (" CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + "SKey,'%')") : (" '%'+" + SystemConfig.getAppCenterDBVarStr() + "SKey+'%'"));
									}
									else
									{
										qo.AddWhere(attr.getKey(), " LIKE ", " '%'||" + SystemConfig.getAppCenterDBVarStr() + "SKey||'%'");
									}
									continue;
								}
								qo.addOr();

								if (SystemConfig.getAppCenterDBVarStr().equals("@"))
								{
									qo.AddWhere(attr.getKey(), " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? ("CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + "SKey,'%')") : ("'%'+" + SystemConfig.getAppCenterDBVarStr() + "SKey+'%'"));
								}
								else
								{
									qo.AddWhere(attr.getKey(), " LIKE ", "'%'||" + SystemConfig.getAppCenterDBVarStr() + "SKey||'%'");
								}

							}
							qo.getMyParas().Add("SKey", keyWord);
							qo.addRightBracket();

						}
						else
						{
							qo.AddHD();
						}
			//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
						///#endregion


						if (map.DTSearchWay != DTSearchWay.None && DataType.IsNullOrEmpty(ur.getDTFrom()) ==false)
						{
							String dtFrom = ur.getDTFrom(); // this.GetTBByID("TB_S_From").Text.Trim().Replace("/", "-");
							String dtTo = ur.getDTTo(); // this.GetTBByID("TB_S_To").Text.Trim().Replace("/", "-");

							if (map.DTSearchWay == DTSearchWay.ByDate)
							{
								qo.addAnd();
								qo.addLeftBracket();
								qo.setSQL(map.DTSearchKey + " >= '" + dtFrom + "'");
								qo.addAnd();
								qo.setSQL(map.DTSearchKey + " <= '" + dtTo + "'");
								qo.addRightBracket();
							}

							if (map.DTSearchWay == DTSearchWay.ByDateTime)
							{
								//取前一天的24：00
								if (dtFrom.trim().length() == 10) //2017-09-30
								{
									dtFrom += " 00:00:00";
								}
								if (dtFrom.trim().length() == 16) //2017-09-30 00:00
								{
									dtFrom += ":00";
								}

								Date dBefore = new Date();
								Calendar calendar = Calendar.getInstance();
								calendar.setTime(new Date());
								calendar.add(Calendar.DAY_OF_MONTH, -1);
								dBefore = calendar.getTime(); //得到前一天的时间
								
								SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
								String defaultStartDate = sdf.format(dBefore);
								dtFrom = dBefore + " 24:00";
								

								if (dtTo.trim().length() < 11 || dtTo.trim().indexOf(' ') == -1)
								{
									dtTo += " 24:00";
								}

								qo.addAnd();
								qo.addLeftBracket();
								qo.setSQL(map.DTSearchKey + " >= '" + dtFrom + "'");
								qo.addAnd();
								qo.setSQL(map.DTSearchKey + " <= '" + dtTo + "'");
								qo.addRightBracket();
							}
						}


						///#region 普通属性
						String opkey = ""; // 操作符号。
						for (AttrOfSearch attr : en.getEnMap().getAttrsOfSearch())
						{
							if (attr.getIsHidden())
							{
								qo.addAnd();
								qo.addLeftBracket();
								qo.AddWhere(attr.getRefAttrKey(), attr.getDefaultSymbol(), attr.getDefaultValRun());
								qo.addRightBracket();
								continue;
							}

							if (attr.getSymbolEnable() == true)
							{
								opkey = ap.GetValStrByKey("DDL_" + attr.getKey());
								if (opkey.equals("all"))
								{
									continue;
								}
							}
							else
							{
								opkey = attr.getDefaultSymbol();
							}

							qo.addAnd();
							qo.addLeftBracket();

							if (attr.getDefaultVal().length() >= 8)
							{
								String date = "2005-09-01";
								try
								{
									// 就可能是年月日。 
									String y =ap.GetValStrByKey("DDL_" + attr.getKey() + "_Year");
									String m = ap.GetValStrByKey("DDL_" + attr.getKey() + "_Month");
									String d = ap.GetValStrByKey("DDL_" + attr.getKey() + "_Day");
									date = y + "-" + m + "-" + d;

									if (opkey.equals("<="))
									{
										Date dBefore = new Date();
										Calendar calendar = Calendar.getInstance();
										calendar.setTime(new Date());
										calendar.add(Calendar.DAY_OF_MONTH, 1);
										dBefore = calendar.getTime(); //得到后一天的时间
										
										SimpleDateFormat sdf=new SimpleDateFormat(DataType.getSysDataFormat());
										date = sdf.format(dBefore);
									}
								}
								catch (java.lang.Exception e)
								{
								}

								qo.AddWhere(attr.getRefAttrKey(), opkey, date);
							}
							else
							{
								qo.AddWhere(attr.getRefAttrKey(), opkey, ap.GetValStrByKey("TB_" + attr.getKey()));
							}
							qo.addRightBracket();
						}
						///#endregion

						///#region 获得查询数据.
						for (String str : ap.getHisHT().keySet())
						{
							Object val = ap.GetValStrByKey(str);
							if (val.equals("all"))
							{
								continue;
							}
							qo.addAnd();
							qo.addLeftBracket();
							qo.AddWhere(str, ap.GetValStrByKey(str));
							qo.addRightBracket();
						}

						//获得行数.
						if (this.getPageIdx() == 1)
						{
							ur.SetPara("RecCount", qo.GetCount());
							ur.Update();
						}

						qo.DoQuery(en.getPK(),12,this.getPageIdx());
						///#endregion 获得查询数据.

						DataTable mydt = ens.ToDataTableField();
						mydt.TableName = "DT";

						ds.Tables.add(mydt); //把数据加入里面.

						return BP.Tools.Json.ToJson(ds);
					}
					
					public final String Search_GenerPageIdx()
					{
						BP.Sys.UserRegedit ur = new UserRegedit();
						ur.setMyPK(WebUser.getNo() + "_" + this.getEnsName() + "_SearchAttrs");
						ur.RetrieveFromDBSources();
						
						String url = "?EnsName="+this.getEnsName();
						int pageSpan = 20;
						int recNum = ur.GetParaInt("RecCount");
						int pageSize = 12;
						if (recNum <= pageSize)
						{
							return "1";
						}

						String html = "";
						html += "<div style='text-align:center;'>";

						String appPath = ""; // this.Request.ApplicationPath;
						int myidx = 0;
						if (getPageIdx() <= 1)
						{
							//this.Add("《- 《-");
							html += "<img style='vertical-align:middle' src='"+Glo.getCCFlowAppPath()+"WF/Img/Arr/LeftEnd.png' border=0/><img style='vertical-align:middle' src='"+Glo.getCCFlowAppPath()+"WF/Img/Arr/Left.png' border=0/>";
						}
						else
						{
							myidx = getPageIdx() - 1;
							//this.Add("<a href='" + url + "&PageIdx=1' >《-</a> <a href='" + url + "&PageIdx=" + myidx + "'>《-</a>");
							html += "<a href='" + url + "&PageIdx=1' ><img style='vertical-align:middle' src='"+Glo.getCCFlowAppPath()+"WF/Img/Arr/LeftEnd.png' border=0/></a><a href='" + url + "&PageIdx=" + myidx + "'><img style='vertical-align:middle' src='"+Glo.getCCFlowAppPath()+"WF/Img/Arr/Left.png' border=0/></a>";
						}

						//分页采用java默认方式分页，采用bigdecimal分页报错
						int pageNum = (recNum + pageSize - 1) / pageSize;// 页面个数。

						int from = getPageIdx()<1?0:(getPageIdx()-1)*pageSize+1;//从
						
						int to = getPageIdx()<1?pageSize:getPageIdx()*pageSize;//到

						for (int i = 1; i <= pageNum; i++)
						{
							if (i >= from && i < to)
							{
							}
							else
							{
								continue;
							}

							if (getPageIdx() == i)
							{
								html += "&nbsp;<font style='font-weight:bloder;color:#f00'>" + i + "</font>&nbsp;";
							}
							else
							{
								html += "&nbsp;<a href='" + url + "&PageIdx=" + i + "'>" + i + "</a>";
							}
						}

						if (getPageIdx() != pageNum)
						{
							myidx = getPageIdx() + 1;
							//this.Add("&nbsp;<a href='" + url + "&PageIdx=" + myidx + "'>-》</a>&nbsp;<a href='" + url + "&PageIdx=" + pageNum + "'>-》</a>&nbsp;&nbsp;Page:" + PageIdx + "/" + pageNum + " Total:" + recNum + ".");
							html += "&nbsp;<a href='" + url + "&PageIdx=" + myidx + "'><img style='vertical-align:middle' src='"+Glo.getCCFlowAppPath()+"WF/Img/Arr/Right.png' border=0/></a>&nbsp;<a href='" + url + "&PageIdx=" + pageNum + "'><img style='vertical-align:middle' src='"+Glo.getCCFlowAppPath()+"WF/Img/Arr/RightEnd.png' border=0/></a>&nbsp;&nbsp;页数:" + getPageIdx() + "/" + pageNum + "&nbsp;&nbsp;总数:" + recNum;
						}
						else
						{
							//this.Add("&nbsp;<a href='" + url + "&PageIdx=" + pageNum + "'> -》》</a>&nbsp;&nbsp;Page:" + PageIdx + "/" + pageNum + " Totlal:" + recNum + ".");
							html += "&nbsp;<img style='vertical-align:middle' src='"+Glo.getCCFlowAppPath()+"WF/Img/Arr/Right.png' border=0/>&nbsp;&nbsp;";
							html += "&nbsp;<img style='vertical-align:middle' src='"+Glo.getCCFlowAppPath()+"WF/Img/Arr/RightEnd.png' border=0/>&nbsp;&nbsp;页数:" + getPageIdx() + "/" + pageNum + "&nbsp;&nbsp;总数:" + recNum;
							//this.Add("<img src='/WF/Img/Page_Down.gif' border=1 />");
						}
						html += "</div>";
						return html;
					}
			
					///#region Refmethod.htm 相关功能.
					public final String getRefEnKey()
					{
						String str = this.GetRequestVal("No");
						if (str == null)
						{
							str = this.GetRequestVal("OID");
						}

						if (str == null)
						{
							str = this.GetRequestVal("MyPK");
						}

						if (str == null)
						{
							str = this.GetRequestVal("PK");
						}

						return str;
					}
					public final String Refmethod_Init()
					{
						String ensName = this.getEnsName();
						int index = this.getIndex();
						Entities ens = BP.En.ClassFactory.GetEns(ensName);
						Entity en = ens.getGetNewEntity();
						BP.En.RefMethod rm = en.getEnMap().getHisRefMethods().get(index);

			
						///#region 处理无参数的方法.
						if (rm.getHisAttrs() == null || rm.getHisAttrs().size() == 0)
						{
							String pk = this.getRefEnKey();
							if (pk == null)
							{
								pk = this.GetRequestVal(en.getPK());
							}

							en.setPKVal(pk);
							en.Retrieve();
							rm.HisEn = en;

							// 如果是link.
							if (rm.refMethodType == RefMethodType.LinkModel)
							{
								Object tempVar = null;
								try {
									tempVar = rm.Do(null);
								} catch (Exception e) {
									e.printStackTrace();
								}
								String url = (String)((tempVar instanceof String) ? tempVar : null);
								if (DotNetToJavaStringHelper.isNullOrEmpty(url))
								{
									return "err@应该返回的url.";
								}

								return "url@" + url;
							}

							Object obj = null;
							try {
								obj = rm.Do(null);
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (obj == null)
							{
								return "close@info";
							}

							String info = obj.toString();
							info = info.replace("@", "\t\n");
							return "close@" + info;
						}
			
						///#endregion 处理无参数的方法.

						//转化为json 返回到前台解析. 处理有参数的方法.
						DataSet ds = new DataSet();
						MapAttrs attrs = rm.getHisAttrs().ToMapAttrs();

						//属性.
						DataTable mapAttrs = attrs.ToDataTableField("Sys_MapAttrs");
						ds.Tables.add(mapAttrs);

			
						///#region 该方法的默认值.
						DataTable dtMain = new DataTable();
						dtMain.TableName = "MainTable";
						for (MapAttr attr : attrs.ToJavaList())
						{
							dtMain.Columns.Add(attr.getKeyOfEn(), String.class);
						}

						DataRow mydrMain = dtMain.NewRow();
						for (MapAttr item : attrs.ToJavaList())
						{
							mydrMain.setValue(item.getKeyOfEn(), item.getDefValReal());
						}
						dtMain.Rows.add(mydrMain);
						ds.Tables.add(dtMain);
			
						///#endregion 该方法的默认值.

			
						///#region 加入该方法的外键.
						for (DataRow dr : mapAttrs.Rows)
						{
							String lgType = dr.getValue("LGType").toString();
							if (!lgType.equals("2"))
							{
								continue;
							}

							String UIIsEnable = dr.getValue("UIVisible").toString();
							if (UIIsEnable.equals("0"))
							{
								continue;
							}

							String uiBindKey = dr.getValue("UIBindKey").toString();
							if (DotNetToJavaStringHelper.isNullOrEmpty(uiBindKey) == true)
							{
								String myPK = dr.getValue("MyPK").toString();
								//如果是空的
								//   throw new Exception("@属性字段数据不完整，流程:" + fl.No + fl.Name + ",节点:" + nd.NodeID + nd.Name + ",属性:" + myPK + ",的UIBindKey IsNull ");
							}

							// 检查是否有下拉框自动填充。
							String keyOfEn = dr.getValue("KeyOfEn").toString();
							String fk_mapData = dr.getValue("FK_MapData").toString();

							ds.Tables.add(BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey));
						}
			
						///#endregion 加入该方法的外键.

			
						///#region 加入该方法的枚举.
						DataTable dtEnum = new DataTable();
						dtEnum.Columns.Add("Lab", String.class);
						dtEnum.Columns.Add("EnumKey", String.class);
						dtEnum.Columns.Add("IntKey", String.class);
						dtEnum.TableName = "Sys_Enum";

						for (MapAttr item : attrs.ToJavaList())
						{
							if (item.getLGType() != FieldTypeS.Enum)
							{
								continue;
							}

							SysEnums ses = new SysEnums(item.getUIBindKey());
							for (SysEnum se : ses.ToJavaList())
							{
								DataRow drEnum = dtEnum.NewRow();
								drEnum.setValue("Lab", se.getLab());
								drEnum.setValue("EnumKey",se.getEnumKey());
								drEnum.setValue("IntKey",se.getIntKey());
								dtEnum.Rows.add(drEnum);
							}

						}

						ds.Tables.add(dtEnum);
			
						///#endregion 加入该方法的枚举.

			
						///#region 增加该方法的信息
						DataTable dt = new DataTable();
						dt.TableName = "RM";
						dt.Columns.Add("Title",String.class);
						dt.Columns.Add("Warning", String.class);

						DataRow mydr = dt.NewRow();
						mydr.setValue("Title",rm.Title);
						mydr.setValue("Warning",rm.Warning);
						dt.Rows.add(mydr);
			
						///#endregion 增加该方法的信息

						//增加到里面.
						ds.Tables.add(dt);

						return BP.Tools.Json.ToJson(ds);
					}
					public final String Refmethod_Done()
					{
						Entities ens = BP.En.ClassFactory.GetEns(this.getEnsName());
						Entity en = ens.getGetNewEntity();
						String msg = "";

						String pk = this.getRefEnKey();
						if (pk.contains(",") == false)
						{
							//批处理的方式.
							en.setPKVal(pk);
							en.Retrieve();
							msg = DoOneEntity(en, this.getIndex());
							if (msg == null)
							{
								return "close@info";
							}
							else
							{
								return "info@" + msg;
							}
						}

						//如果是批处理.
						String[] pks = pk.split("[,]", -1);
						for (String mypk : pks)
						{
							if (DotNetToJavaStringHelper.isNullOrEmpty(mypk) == true)
							{
								continue;
							}
							en.setPKVal(mypk);
							en.Retrieve();

							String s = DoOneEntity(en, this.getIndex());
							if (s != null)
							{
								msg += "@" + s;
							}
						}
						if (msg.equals(""))
						{
							return "close@info";
						}
						else
						{
							return "info@" + msg;
						}
					}
					public final String DoOneEntity(Entity en, int rmIdx)
					{
						BP.En.RefMethod rm = en.getEnMap().getHisRefMethods().get(rmIdx);
						rm.HisEn = en;
						int mynum = 0;
						for (Attr attr : rm.getHisAttrs())
						{
							if (attr.getMyFieldType() == FieldType.RefText)
							{
								continue;
							}
							mynum++;
						}

						Object[] objs = new Object[mynum];

						int idx = 0;
						for (Attr attr : rm.getHisAttrs())
						{
							if (attr.getMyFieldType() == FieldType.RefText)
							{
								continue;
							}

							switch (attr.getUIContralType())
							{
								case TB:
									switch (attr.getMyDataType())
									{
										case BP.DA.DataType.AppString:
										case BP.DA.DataType.AppDate:
										case BP.DA.DataType.AppDateTime:
											String str1 = this.GetValFromFrmByKey(attr.getKey());
											objs[idx] = str1;
											//attr.DefaultVal=str1;
											break;
										case BP.DA.DataType.AppInt:
											int myInt = this.GetValIntFromFrmByKey(attr.getKey());
											objs[idx] = myInt;
											//attr.DefaultVal=myInt;
											break;
										case BP.DA.DataType.AppFloat:
											float myFloat = this.GetValFloatFromFrmByKey(attr.getKey());
											objs[idx] = myFloat;
											//attr.DefaultVal=myFloat;
											break;
										case BP.DA.DataType.AppDouble:
										case BP.DA.DataType.AppMoney:
											java.math.BigDecimal myDoub = this.GetValDecimalFromFrmByKey(attr.getKey());
											objs[idx] = myDoub;
											//attr.DefaultVal=myDoub;
											break;
										case BP.DA.DataType.AppBoolean:
											objs[idx] = this.GetValBoolenFromFrmByKey(attr.getKey());
											attr.setDefaultVal(false);
											break;
										default:
											throw new RuntimeException("没有判断的数据类型．");

									}
									break;
								case DDL:
									try
									{
										if (attr.getMyFieldType() == FieldType.FK)
										{
											String str = this.GetValFromFrmByKey(attr.getKey());
											objs[idx] = str;
											attr.setDefaultVal(str);
										}
										else
										{
											int enumVal = this.GetValIntFromFrmByKey(attr.getKey());
											objs[idx] = enumVal;
											attr.setDefaultVal(enumVal);
										}

									}
									catch (java.lang.Exception e)
									{
										objs[idx] = null;
									}
									break;
								case CheckBok:
									objs[idx] = this.GetValBoolenFromFrmByKey(attr.getKey());

									attr.setDefaultVal(objs[idx].toString());

									break;
								default:
									break;
							}
							idx++;
						}

						try
						{
							Object obj = rm.Do(objs);
							if (obj != null)
							{
								return obj.toString();
							}

							return null;
						}
						catch (RuntimeException ex)
						{
							String msg = "";
							for (Object obj : objs)
							{
								msg += "@" + obj.toString();
							}
							String err = "@执行[" + this.getEnsName() + "]["+rm.ClassMethodName+"]期间出现错误：" + ex.getMessage() + " InnerException= " + ex.getCause() + "[参数为：]" + msg;
							return "<font color=red>" + err + "</font>";
						} catch (Exception ex) {
							String msg = "";
							for (Object obj : objs)
							{
								msg += "@" + obj.toString();
							}
							String err = "@执行[" + this.getEnsName() + "]["+rm.ClassMethodName+"]期间出现错误：" + ex.getMessage() + " InnerException= " + ex.getCause() + "[参数为：]" + msg;
							return "<font color=red>" + err + "</font>";
						}
					}
			
					///#endregion 相关功能.

			
					///#region  公共方法。
					public final String SFTable()
					{
						BP.Sys.SFTable sftable = new BP.Sys.SFTable(this.GetRequestVal("SFTable"));
						DataTable dt = sftable.GenerData();
						return BP.Tools.Json.ToJson(dt);
					}
			
					///#endregion  公共方法。

			
					///#region 执行方法.
					/** 
					 执行方法
					 
					 @param clsName 类名称
					 @param monthodName 方法名称
					 @param paras 参数，可以为空.
					 @return 执行结果
					*/
			//C# TO JAVA CONVERTER TODO TASK: C# optional parameters are not converted to Java:
			//ORIGINAL LINE: public string Exec(string clsName, string methodName, string paras=null)
					public final String Exec(String clsName, String methodName, String paras)
					{
			
						///#region 处理 HttpHandler 类.
						if (clsName.contains(".HttpHandler.") == true)
						{
							//创建类实体.
							Object tempVar = null;
							try {
								tempVar = java.lang.Class.forName("BP.WF.HttpHandler.DirectoryPageBase").newInstance();
							} catch (InstantiationException e) {
								String parasStr = "";
								while (getRequest().getParameterNames().hasMoreElements())
								{
									String key = (String) BP.Sys.Glo.getRequest().getParameterNames().nextElement();
									String val = BP.Sys.Glo.getRequest().getParameter(key);
									parasStr += "@" + key + "=" + val;
								}
								e.printStackTrace();
								return "err@" + e.getMessage() + " 参数:" + parasStr;
							} catch (IllegalAccessException e) {
								String parasStr = "";
								while (getRequest().getParameterNames().hasMoreElements())
								{
									String key = (String) BP.Sys.Glo.getRequest().getParameterNames().nextElement();
									String val = BP.Sys.Glo.getRequest().getParameter(key);
									parasStr += "@" + key + "=" + val;
								}
								e.printStackTrace();
								return "err@" + e.getMessage() + " 参数:" + parasStr;
							} catch (ClassNotFoundException e) {
								String parasStr = "";
								while (getRequest().getParameterNames().hasMoreElements())
								{
									String key = (String) BP.Sys.Glo.getRequest().getParameterNames().nextElement();
									String val = BP.Sys.Glo.getRequest().getParameter(key);
									parasStr += "@" + key + "=" + val;
								}
								e.printStackTrace();
								return "err@" + e.getMessage() + " 参数:" + parasStr;
							}
							WebContralBase ctrl = (WebContralBase)((tempVar instanceof WebContralBase) ? tempVar : null);
							ctrl.context = this.context;

							try
							{
								//执行方法返回json.
								String data = ctrl.DoMethod(ctrl, methodName);
								return data;
							}
							catch (RuntimeException ex)
							{
								String parasStr = "";
								while (getRequest().getParameterNames().hasMoreElements())
								{
									String key = (String) BP.Sys.Glo.getRequest().getParameterNames().nextElement();
									String val = BP.Sys.Glo.getRequest().getParameter(key);
									parasStr += "@" + key + "=" + val;
								}
								return "err@" + ex.getMessage() + " 参数:" + parasStr;
							}
						}
			
						///#endregion 处理 page 类.


			
						///#region 执行entity类的方法.
						try
						{
							//创建类实体.
							Object tempVar2 = java.lang.Class.forName("BP.En.Entity").newInstance();
							BP.En.Entity en = (BP.En.Entity)((tempVar2 instanceof BP.En.Entity) ? tempVar2 : null);
							en.setPKVal(this.getPKVal());
							en.RetrieveFromDBSources();

							java.lang.Class tp = en.getClass();
							java.lang.reflect.Method mp = tp.getMethod(methodName);
							if (mp == null)
							{
								return "err@没有找到类[" + clsName + "]方法[" + methodName + "].";
							}

							//执行该方法.
							Object[] myparas = null;
							Object tempVar3 = mp.invoke(this, myparas);
							String result= (String)((tempVar3 instanceof String) ? tempVar3 : null); //调用由此 MethodInfo 实例反射的方法或构造函数。
							return result;
						}
						catch (RuntimeException ex)
						{
							return "err@执行实体类的方法错误:" + ex.getMessage();
						} catch (InstantiationException e) {
							String parasStr = "";
							while (getRequest().getParameterNames().hasMoreElements())
							{
								String key = (String) BP.Sys.Glo.getRequest().getParameterNames().nextElement();
								String val = BP.Sys.Glo.getRequest().getParameter(key);
								parasStr += "@" + key + "=" + val;
							}
							return "err@" + e.getMessage() + " 参数:" + parasStr;
						} catch (IllegalAccessException e) {
							String parasStr = "";
							while (getRequest().getParameterNames().hasMoreElements())
							{
								String key = (String) BP.Sys.Glo.getRequest().getParameterNames().nextElement();
								String val = BP.Sys.Glo.getRequest().getParameter(key);
								parasStr += "@" + key + "=" + val;
							}
							return "err@" + e.getMessage() + " 参数:" + parasStr;
						} catch (ClassNotFoundException e) {
							String parasStr = "";
							while (getRequest().getParameterNames().hasMoreElements())
							{
								String key = (String) BP.Sys.Glo.getRequest().getParameterNames().nextElement();
								String val = BP.Sys.Glo.getRequest().getParameter(key);
								parasStr += "@" + key + "=" + val;
							}
							return "err@" + e.getMessage() + " 参数:" + parasStr;
						} catch (NoSuchMethodException e) {
							String parasStr = "";
							while (getRequest().getParameterNames().hasMoreElements())
							{
								String key = (String) BP.Sys.Glo.getRequest().getParameterNames().nextElement();
								String val = BP.Sys.Glo.getRequest().getParameter(key);
								parasStr += "@" + key + "=" + val;
							}
							return "err@" + e.getMessage() + " 参数:" + parasStr;
						} catch (InvocationTargetException e) {
							String parasStr = "";
							while (getRequest().getParameterNames().hasMoreElements())
							{
								String key = (String) BP.Sys.Glo.getRequest().getParameterNames().nextElement();
								String val = BP.Sys.Glo.getRequest().getParameter(key);
								parasStr += "@" + key + "=" + val;
							}
							return "err@" + e.getMessage() + " 参数:" + parasStr;
						}
			
						///#endregion 执行entity类的方法.
					}
			
					///#endregion


			
					///#region 数据库相关.
					/** 
					 运行SQL
					 
					 @return 返回影响行数
					*/
					public final String DBAccess_RunSQL()
					{
						String sql = this.GetRequestVal("SQL");
						return DBAccess.RunSQL(sql) + "";
					}
					/** 
					 运行SQL返回DataTable
					 
					 @return DataTable转换的json
					*/
					public final String DBAccess_RunSQLReturnTable()
					{
						String sql = this.GetRequestVal("SQL");
						sql = sql.replaceAll("~","'");
						sql = sql.replaceAll("-","%");
						if(null == sql || StringUtils.isEmpty(sql)){
							return "err@查询sql为空";
						}
						DataTable dt = DBAccess.RunSQLReturnTable(sql);
						return BP.Tools.Json.ToJson(dt);
					}
			
					///#endregion

					//执行方法.
					public final String HttpHandler()
					{
						
						//获得两个参数.
						String httpHandlerName = this.GetRequestVal("HttpHandlerName");
						String methodName = this.GetRequestVal("DoMethod");
						try {
							Object tempVar = null;
						try {
							tempVar = java.lang.Class.forName(httpHandlerName).newInstance();
						} catch (InstantiationException e) {
							e.printStackTrace();
							return "err@执行方法"+methodName+"错误:"+e.getMessage();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
							return "err@执行方法"+methodName+"错误:"+e.getMessage();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
							return "err@执行方法"+methodName+"错误:"+e.getMessage();
						}
						WebContralBase en = (WebContralBase)((tempVar instanceof WebContralBase) ? tempVar : null);

						en.context = this.context;
						return en.DoMethod(en, methodName);

						} catch (Exception e) {							 
							
							return "err@执行方法"+methodName+"错误:"+e.getMessage();
						}
					}
			
	// #endregion 界面方法.

	public String EnsData() {
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		String filter = this.GetRequestVal("Filter");
		if (filter == null || filter == "" || filter.contains("=") == false) {
			ens.RetrieveAll();
		} else {
			QueryObject qo = new QueryObject(ens);
			String[] strs = filter.split("=");
			qo.AddWhere(strs[0], strs[1]);
			qo.DoQuery();
		}
		return ens.ToJson();
	}

	// <summary>
	// 执行一个SQL，然后返回一个列表.
	// 用于gener.js 的公共方法.
	// </summary>
	// <returns></returns>
	public String SQLList() {
		String sqlKey = this.getRequest().getParameter("SQLKey"); // SQL的key.
		String paras = this.getRequest().getParameter("Paras"); // 参数. 格式为
		BP.Sys.XML.SQLList sqlXml = new BP.Sys.XML.SQLList(sqlKey);
		// 获得SQL
		String sql = sqlXml.getSQL();
		String[] strs = paras.split("@");
		for (String str : strs) {
			if (str == null || str == "")
				continue;
			// 参数.
			String[] p = str.split("=");
			sql = sql.replaceAll("@" + p[0], p[1]);
		}
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		return BP.Tools.Json.ToJson(dt);
	}

	public String EnumList() {
		SysEnums ses = new SysEnums(this.getEnumKey());
		return ses.ToJson();
	}
	// #endregion 公共方法。

	/** 当前登录人员信息
	 
	 @return 
	 */
	public String WebUser_Init()
	{
		Hashtable ht = new Hashtable();
		ht.put("No", WebUser.getNo()==null?"":WebUser.getNo());
		ht.put("Name", WebUser.getName()==null?"":WebUser.getName());
		ht.put("FK_Dept", WebUser.getFK_Dept()==null?"":WebUser.getFK_Dept());
		ht.put("FK_DeptName", WebUser.getFK_DeptName()==null?"":WebUser.getFK_DeptName());
		ht.put("FK_DeptNameOfFull", WebUser.getFK_DeptNameOfFull()==null?"":WebUser.getFK_DeptNameOfFull());		
		ht.put("GroupNo", "0");
		ht.put("orgNo",   WebUser.getFK_Dept()==null?"":WebUser.getFK_Dept());
		return BP.Tools.Json.ToJson(ht);
	}
	
	/// 运行Url返回string.
    /// </summary>
    /// <param name="url"></param>
    /// <returns></returns>
    public String RunUrlCrossReturnString()
    {
    	String url = this.GetRequestVal("url");
        String strs = DataType.ReadURLContext(url, 9999);
        return strs;
    }
}
