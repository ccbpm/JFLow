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
import BP.En.RefMethod;
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
	// 椤甸潰鍔熻兘瀹炰綋
	// </summary>
	// <param name="mycontext"></param>

	// #region 缁熻鍒嗘瀽缁勪欢.
	// <summary>
	// 鍒濆鍖栨暟鎹�
	// </summary>
	// <returns></returns>
	public String ContrastDtl_Init() {
		return "";
	}

	// #endregion 缁熻鍒嗘瀽缁勪欢.

	// #region 鍏叡绫诲簱.
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
	// 鑾峰緱瀹炰綋
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
						return "err@娌℃湁鍒ゆ柇鐨勬暟鎹被鍨嬶紟";
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
				return "err@鑾峰緱鍙傛暟閿欒" + "attr=" + attr.getKey() + " attr = "
						+ attr.getKey() + ex.getMessage();
			}
		}

		try {
			Object obj = rm.Do();
			if (obj != null)
				return obj.toString();
			else
				return "err@鎵ц瀹屾垚娌℃湁杩斿洖淇℃伅.";
		} catch (Exception ex) {
			return "err@鎵ц閿欒:" + ex.getMessage();
		}
	}

	// #region 鐣岄潰 .
	// <summary>
	// 瀹炰綋鍒濆鍖�
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
              
				//璁剧疆榛樿鐨勬暟鎹�.
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
				//閬嶅巻灞炴�э紝寰幆璧嬪��.
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

	/** 鏇存柊
	 
	 @return 
*/
	public final String Entity_Update()
	{
		try
		{
			Entity en = ClassFactory.GetEn(this.getEnName());
			en.setPKVal(this.getPKVal());
			en.RetrieveFromDBSources();

			//閬嶅巻灞炴�э紝寰幆璧嬪��.
			for (Attr attr : en.getEnMap().getAttrs())
			{
				en.SetValByKey(attr.getKey(), this.GetRequestVal(attr.getKey()));
			}

		 
			
			return en.Update() + ""; //杩斿洖褰卞搷琛屾暟.
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	
	/** 浠庢暟鎹簮鏌ヨ.
	 
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
	
	/** 浠庢暟鎹簮鏌ヨ.
	 
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
	
	/** 鏄惁瀛樺湪
	 
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
	 鎵ц淇濆瓨
	 
	 @return 
	*/
	public final String Entity_Save()
	{
		try
		{
			Entity en = ClassFactory.GetEn(this.getEnName());
			en.setPKVal(this.getPKVal());
			en.RetrieveFromDBSources();

			//閬嶅巻灞炴�э紝寰幆璧嬪��.
			for (Attr attr : en.getEnMap().getAttrs())
			{
				en.SetValByKey(attr.getKey(), this.GetValFromFrmByKey(attr.getKey()));
			}

			//淇濆瓨鍙傛暟灞炴��.
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
	 鎵ц鎻掑叆.
	 
	 @return 
	*/
	public final String Entity_Insert()
	{
		try
		{
			Entity en = ClassFactory.GetEn(this.getEnName());
			//en.setPKVal(this.getPKVal());
			//en.RetrieveFromDBSources();

			//閬嶅巻灞炴�э紝寰幆璧嬪��.
			for (Attr attr : en.getEnMap().getAttrs())
			{
				en.SetValByKey(attr.getKey(), this.GetValFromFrmByKey(attr.getKey()));
			}

			//鎻掑叆鏁版嵁搴�.
			en.Insert();

			//鎵ц鏌ヨ.
			en.Retrieve();

			//杩斿洖鏁版嵁.
			return en.ToJson(false);
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}

	public final String Entity_DoMethodReturnString()
	{
		//鍒涘缓绫诲疄浣�.
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
			return "err@娌℃湁鎵惧埌绫籟" + this.getEnName() + "]鏂规硶[" + methodName + "].";
		} catch (SecurityException e1) {
			e1.printStackTrace();
			return "err@娌℃湁鎵惧埌绫籟" + this.getEnName() + "]鏂规硶[" + methodName + "].";
		}
		if (mp == null)
		{
			return "err@娌℃湁鎵惧埌绫籟" + this.getEnName() + "]鏂规硶[" + methodName + "].";
		}

		String paras = this.GetRequestVal("paras");

		//鎵ц璇ユ柟娉�.
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
		String result = (String)((tempVar instanceof String) ? tempVar : null); //璋冪敤鐢辨 MethodInfo 瀹炰緥鍙嶅皠鐨勬柟娉曟垨鏋勯�犲嚱鏁般��
		return result;
	}
	
	
	
	///#region Entities 鍏叡绫诲簱.
			/** 
			 璋冪敤鍙傛暟.
			 
			*/
			public final String getParas()
			{
				return this.GetRequestVal("Paras");
			}
			/** 
			 鏌ヨ鍏ㄩ儴
			 
			 @return 
			*/
			public final String Entities_RetrieveAll()
			{
				Entities ens = ClassFactory.GetEns(this.getEnsName());
				ens.RetrieveAll();
				return ens.ToJson();
			}
			/** 
			 鑾峰緱瀹炰綋闆嗗悎s
			 
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

	
			///#region 鍔熻兘鎵ц.
			/** 
			 鍒濆鍖�.
			 
			 @return 
			*/
			public final String Method_Init()
			{
				String ensName = this.GetRequestVal("M");
				Method rm = BP.En.ClassFactory.GetMethod(ensName);
				if (rm == null)
				{
					return "err@鏂规硶鍚嶉敊璇垨鑰呰鏂规硶宸茬粡涓嶅瓨鍦�" + ensName;
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

				//杞寲涓洪泦鍚�.
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
	
			
			///#region 鏌ヨ.
					/** 
					 鑾峰緱鏌ヨ鐨勫熀鏈俊鎭�.
					 
					 @return 
					*/
					public String Search_MapBaseInfo()
					{
						//鑾峰緱
						Entities ens = ClassFactory.GetEns(this.getEnsName());
						Entity en = ens.getGetNewEntity();
						Map map = ens.getGetNewEntity().getEnMapInTime();

						java.util.Hashtable ht = new java.util.Hashtable();

						//鎶婃潈闄愪俊鎭斁鍏�.
						UAC uac = en.getHisUAC();
						ht.put("IsUpdata", uac.IsUpdate);
						ht.put("IsInsert", uac.IsInsert);
						ht.put("IsDelete", uac.IsDelete);
						ht.put("IsView", uac.IsView);
						ht.put("IsExp", uac.IsExp); //鏄惁鍙互瀵煎嚭?
						ht.put("IsImp", uac.IsImp); //鏄惁鍙互瀵煎叆?
						
			            ht.put("EnDesc", en.getEnDesc()); //鎻忚堪?
			            
			            ht.put("EnName", en.toString() ); //绫诲悕?
 

						//鎶妋ap淇℃伅鏀惧叆
						ht.put("PhysicsTable", map.getPhysicsTable());
						ht.put("CodeStruct", map.getCodeStruct());
						ht.put("CodeLength", map.getCodeLength());

						//鏌ヨ鏉′欢.
						if (map.IsShowSearchKey==true)
						{
						   ht.put("IsShowSearchKey", 1);
						}
						else
						{
							ht.put("IsShowSearchKey", 0);
						}

						//鎸夋棩鏈熸煡璇�.
						ht.put("DTSearchWay", map.DTSearchWay.getValue());
						ht.put("DTSearchLable", map.DTSearchLable);
						ht.put("DTSearchKey", map.DTSearchKey);

						return BP.Tools.Json.ToJson(ht);
					}
					/** 
					 澶栭敭鎴栬�呮灇涓剧殑鏌ヨ.
					 
					 @return 
					*/
					public final String Search_SearchAttrs()
					{
						//鑾峰緱
						Entities ens = ClassFactory.GetEns(this.getEnsName());
						Entity en = ens.getGetNewEntity();
						Map map = ens.getGetNewEntity().getEnMapInTime();

						DataSet ds = new DataSet();

						//鏋勯�犳煡璇㈡潯浠堕泦鍚�.
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

						//鎶婂閿灇涓惧鍔犲埌閲岄潰.
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
					 /** 鎵ц鏌ヨ.
					 
					 @return 
					  */
					public final String Search_SearchIt()
					{
						//鑾峰緱.
						Entities ens = ClassFactory.GetEns(this.getEnsName());
						Entity en = ens.getGetNewEntity();
						Map map = en.getEnMapInTime();

						MapAttrs attrs = map.getAttrs().ToMapAttrs();

						//灞炴�ч泦鍚�.
						DataTable dtAttrs = attrs.ToDataTableField();
						dtAttrs.TableName = "Attrs";

						DataSet ds = new DataSet();
						ds.Tables.add(dtAttrs); //鎶婃弿杩板姞鍏�.

						//鍙栧嚭鏉ユ煡璇㈡潯浠�.
						BP.Sys.UserRegedit ur = new UserRegedit();
						ur.setMyPK(WebUser.getNo() + "_" + this.getEnsName() + "_SearchAttrs");
						ur.RetrieveFromDBSources();

						//鑾峰緱鍏抽敭瀛�.
						AtPara ap = new AtPara(ur.getVals());

						//鍏抽敭瀛�.
						String keyWord = ur.getSearchKey();
						QueryObject qo = new QueryObject(ens);

						///#region 鍏抽敭瀛楀瓧娈�.
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
									// 绗竴娆¤繘鏉ャ�� 
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
								//鍙栧墠涓�澶╃殑24锛�00
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
								dBefore = calendar.getTime(); //寰楀埌鍓嶄竴澶╃殑鏃堕棿
								
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


						///#region 鏅�氬睘鎬�
						String opkey = ""; // 鎿嶄綔绗﹀彿銆�
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
									// 灏卞彲鑳芥槸骞存湀鏃ャ�� 
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
										dBefore = calendar.getTime(); //寰楀埌鍚庝竴澶╃殑鏃堕棿
										
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

						///#region 鑾峰緱鏌ヨ鏁版嵁.
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

						//鑾峰緱琛屾暟.
						if (this.getPageIdx() == 1)
						{
							ur.SetPara("RecCount", qo.GetCount());
							ur.Update();
						}

						qo.DoQuery(en.getPK(),12,this.getPageIdx());
						///#endregion 鑾峰緱鏌ヨ鏁版嵁.

						DataTable mydt = ens.ToDataTableField();
						mydt.TableName = "DT";

						ds.Tables.add(mydt); //鎶婃暟鎹姞鍏ラ噷闈�.

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
							//this.Add("銆�- 銆�-");
							html += "<img style='vertical-align:middle' src='"+Glo.getCCFlowAppPath()+"WF/Img/Arr/LeftEnd.png' border=0/><img style='vertical-align:middle' src='"+Glo.getCCFlowAppPath()+"WF/Img/Arr/Left.png' border=0/>";
						}
						else
						{
							myidx = getPageIdx() - 1;
							//this.Add("<a href='" + url + "&PageIdx=1' >銆�-</a> <a href='" + url + "&PageIdx=" + myidx + "'>銆�-</a>");
							html += "<a href='" + url + "&PageIdx=1' ><img style='vertical-align:middle' src='"+Glo.getCCFlowAppPath()+"WF/Img/Arr/LeftEnd.png' border=0/></a><a href='" + url + "&PageIdx=" + myidx + "'><img style='vertical-align:middle' src='"+Glo.getCCFlowAppPath()+"WF/Img/Arr/Left.png' border=0/></a>";
						}

						//鍒嗛〉閲囩敤java榛樿鏂瑰紡鍒嗛〉锛岄噰鐢╞igdecimal鍒嗛〉鎶ラ敊
						int pageNum = (recNum + pageSize - 1) / pageSize;// 椤甸潰涓暟銆�

						int from = getPageIdx()<1?0:(getPageIdx()-1)*pageSize+1;//浠�
						
						int to = getPageIdx()<1?pageSize:getPageIdx()*pageSize;//鍒�

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
							//this.Add("&nbsp;<a href='" + url + "&PageIdx=" + myidx + "'>-銆�</a>&nbsp;<a href='" + url + "&PageIdx=" + pageNum + "'>-銆�</a>&nbsp;&nbsp;Page:" + PageIdx + "/" + pageNum + " Total:" + recNum + ".");
							html += "&nbsp;<a href='" + url + "&PageIdx=" + myidx + "'><img style='vertical-align:middle' src='"+Glo.getCCFlowAppPath()+"WF/Img/Arr/Right.png' border=0/></a>&nbsp;<a href='" + url + "&PageIdx=" + pageNum + "'><img style='vertical-align:middle' src='"+Glo.getCCFlowAppPath()+"WF/Img/Arr/RightEnd.png' border=0/></a>&nbsp;&nbsp;椤垫暟:" + getPageIdx() + "/" + pageNum + "&nbsp;&nbsp;鎬绘暟:" + recNum;
						}
						else
						{
							//this.Add("&nbsp;<a href='" + url + "&PageIdx=" + pageNum + "'> -銆嬨��</a>&nbsp;&nbsp;Page:" + PageIdx + "/" + pageNum + " Totlal:" + recNum + ".");
							html += "&nbsp;<img style='vertical-align:middle' src='"+Glo.getCCFlowAppPath()+"WF/Img/Arr/Right.png' border=0/>&nbsp;&nbsp;";
							html += "&nbsp;<img style='vertical-align:middle' src='"+Glo.getCCFlowAppPath()+"WF/Img/Arr/RightEnd.png' border=0/>&nbsp;&nbsp;椤垫暟:" + getPageIdx() + "/" + pageNum + "&nbsp;&nbsp;鎬绘暟:" + recNum;
							//this.Add("<img src='/WF/Img/Page_Down.gif' border=1 />");
						}
						html += "</div>";
						return html;
					}
			
					///#region Refmethod.htm 鐩稿叧鍔熻兘.
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
							
							if (pk == null)
			                    return "err@错误pkval 没有值。";
							
							String infos="";
							String[] pks = pk.split(",");
							for (int i=0;i<pks.length; i++)
				            {
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
										 infos += "err@应该返回的url.";
				                         break;
									}
	
									 infos += "url@" + url;
				                     break;
								}

								Object obj = null;
								try {
									obj = rm.Do(null);
								} catch (Exception e) {
									e.printStackTrace();
								}
								if (obj == null)
								{
									 infos += "close@info";
				                     break;
								}
								String result = obj.toString();
								if (result.indexOf("url@") != -1)
			                    {
			                        infos += result;
			                        break;
			                    }

			                    result = result.replace("@", "\t\n");
			                    infos += "close@" + result;
							}
							
							return infos;
						}
						///#endregion 处理无参数的方法.

						//转化为json 返回到前台解析. 处理有参数的方法.
						DataSet ds = new DataSet();
						MapAttrs attrs = rm.getHisAttrs().ToMapAttrs();

						//属性
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
			
						///#endregion  该方法的默认值.

			
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
								 /*如果是空的*/
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

			
						///#region  增加该方法的信息
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
					
					 ///#region 获取批处理的方法.
				    public String Refmethod_BatchInt(){
			            String ensName = this.getEnsName();
			            Entity en = null;
			    		if (DataType.IsNullOrEmpty(ensName) == true) {
			    			if (DataType.IsNullOrEmpty(this.getEnName()) == true) {
			    				return "err@类名没有传递过来";
			    			}

			    			Entities ens = ClassFactory.GetEns(this.getEnsName());
			    			en = ens.getGetNewEntity();
			    		} else {
			    			en = ClassFactory.GetEn(this.getEnName());
			    		}
			            BP.En.RefMethods rms = en.getEnMap().getHisRefMethods();
			            DataTable dt = new DataTable();
			             dt.TableName = "RM";
			             dt.Columns.Add("No");
			             dt.Columns.Add("Title");
			             dt.Columns.Add("Tip");
			             dt.Columns.Add("Visable");

			             dt.Columns.Add("Url");
			             dt.Columns.Add("Target");
			             dt.Columns.Add("Warning");
			             dt.Columns.Add("RefMethodType");
			             dt.Columns.Add("GroupName");
			             dt.Columns.Add("W");
			             dt.Columns.Add("H");
			             dt.Columns.Add("Icon");
			             dt.Columns.Add("IsCanBatch");
			             dt.Columns.Add("RefAttrKey");
			             for (RefMethod item : rms){
			                if (item.IsCanBatch == false)
			                    continue;
			                DataRow mydr = dt.NewRow();
			                String myurl = "";
			                if (item.refMethodType != RefMethodType.Func){
			                	Object tempVar = null;
			    				try {
			    					tempVar = item.Do(null);
			    				} catch (Exception e) {
			    					e.printStackTrace();
			    				}
			    				myurl = (String) ((tempVar instanceof String) ? tempVar : null);
			    				if (myurl == null) {
			    					continue;
			    				}
			                 }else{
			                    myurl = "../Comm/RefMethod.htm?Index=" + item.Index + "&EnName=" + en.toString() + "&EnsName=" + en.getGetNewEntities().toString() + "&PKVal=" + this.getPKVal();
			                 }

			                DataRow dr = dt.NewRow();
			                dr.setValue("No", item.Index);
			    			dr.setValue("Title", item.Title);
			    			dr.setValue("Tip", item.ToolTip);
			    			dr.setValue("Visable", item.Visable);
			    			dr.setValue("Warning", item.Warning);
			    			dr.setValue("RefMethodType", item.refMethodType.getValue());
			    			dr.setValue("RefAttrKey", item.RefAttrKey);
			    			dr.setValue("URL", myurl);
			    			dr.setValue("W", item.Width);
			    			dr.setValue("H", item.Height);
			    			dr.setValue("Icon", item.Icon);
			    			dr.setValue("IsCanBatch", item.IsCanBatch);
			    			dr.setValue("GroupName", item.GroupName);
			                dt.Rows.add(dr);
			             }
			            
			            return BP.Tools.Json.ToJson(dt);
			        }
			        ///#endregion
			        
					public final String Refmethod_Done()
					{
						Entities ens = BP.En.ClassFactory.GetEns(this.getEnsName());
						Entity en = ens.getGetNewEntity();
						String msg = "";

						String pk = this.getRefEnKey();
						if (pk.contains(",") == false)
						{
							 /*批处理的方式.*/
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
											throw new RuntimeException("娌℃湁鍒ゆ柇鐨勬暟鎹被鍨嬶紟");

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
							String err = "@鎵ц[" + this.getEnsName() + "]["+rm.ClassMethodName+"]鏈熼棿鍑虹幇閿欒锛�" + ex.getMessage() + " InnerException= " + ex.getCause() + "[鍙傛暟涓猴細]" + msg;
							return "<font color=red>" + err + "</font>";
						} catch (Exception ex) {
							String msg = "";
							for (Object obj : objs)
							{
								msg += "@" + obj.toString();
							}
							String err = "@鎵ц[" + this.getEnsName() + "]["+rm.ClassMethodName+"]鏈熼棿鍑虹幇閿欒锛�" + ex.getMessage() + " InnerException= " + ex.getCause() + "[鍙傛暟涓猴細]" + msg;
							return "<font color=red>" + err + "</font>";
						}
					}
			
					///#endregion 鐩稿叧鍔熻兘.

			
					///#region  鍏叡鏂规硶銆�
					public final String SFTable()
					{
						BP.Sys.SFTable sftable = new BP.Sys.SFTable(this.GetRequestVal("SFTable"));
						DataTable dt = sftable.GenerData();
						return BP.Tools.Json.ToJson(dt);
					}
			
					///#endregion  鍏叡鏂规硶銆�

			
					///#region 鎵ц鏂规硶.
					/** 
					 鎵ц鏂规硶
					 
					 @param clsName 绫诲悕绉�
					 @param monthodName 鏂规硶鍚嶇О
					 @param paras 鍙傛暟锛屽彲浠ヤ负绌�.
					 @return 鎵ц缁撴灉
					*/
			//C# TO JAVA CONVERTER TODO TASK: C# optional parameters are not converted to Java:
			//ORIGINAL LINE: public string Exec(string clsName, string methodName, string paras=null)
					public final String Exec(String clsName, String methodName, String paras)
					{
			
						///#region 澶勭悊 HttpHandler 绫�.
						if (clsName.contains(".HttpHandler.") == true)
						{
							//鍒涘缓绫诲疄浣�.
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
								return "err@" + e.getMessage() + " 鍙傛暟:" + parasStr;
							} catch (IllegalAccessException e) {
								String parasStr = "";
								while (getRequest().getParameterNames().hasMoreElements())
								{
									String key = (String) BP.Sys.Glo.getRequest().getParameterNames().nextElement();
									String val = BP.Sys.Glo.getRequest().getParameter(key);
									parasStr += "@" + key + "=" + val;
								}
								e.printStackTrace();
								return "err@" + e.getMessage() + " 鍙傛暟:" + parasStr;
							} catch (ClassNotFoundException e) {
								String parasStr = "";
								while (getRequest().getParameterNames().hasMoreElements())
								{
									String key = (String) BP.Sys.Glo.getRequest().getParameterNames().nextElement();
									String val = BP.Sys.Glo.getRequest().getParameter(key);
									parasStr += "@" + key + "=" + val;
								}
								e.printStackTrace();
								return "err@" + e.getMessage() + " 鍙傛暟:" + parasStr;
							}
							WebContralBase ctrl = (WebContralBase)((tempVar instanceof WebContralBase) ? tempVar : null);
							ctrl.context = this.context;

							try
							{
								//鎵ц鏂规硶杩斿洖json.
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
								return "err@" + ex.getMessage() + " 鍙傛暟:" + parasStr;
							}
						}
			
						///#endregion 澶勭悊 page 绫�.


			
						///#region 鎵цentity绫荤殑鏂规硶.
						try
						{
							//鍒涘缓绫诲疄浣�.
							Object tempVar2 = java.lang.Class.forName("BP.En.Entity").newInstance();
							BP.En.Entity en = (BP.En.Entity)((tempVar2 instanceof BP.En.Entity) ? tempVar2 : null);
							en.setPKVal(this.getPKVal());
							en.RetrieveFromDBSources();

							java.lang.Class tp = en.getClass();
							java.lang.reflect.Method mp = tp.getMethod(methodName);
							if (mp == null)
							{
								return "err@娌℃湁鎵惧埌绫籟" + clsName + "]鏂规硶[" + methodName + "].";
							}

							//鎵ц璇ユ柟娉�.
							Object[] myparas = null;
							Object tempVar3 = mp.invoke(this, myparas);
							String result= (String)((tempVar3 instanceof String) ? tempVar3 : null); //璋冪敤鐢辨 MethodInfo 瀹炰緥鍙嶅皠鐨勬柟娉曟垨鏋勯�犲嚱鏁般��
							return result;
						}
						catch (RuntimeException ex)
						{
							return "err@鎵ц瀹炰綋绫荤殑鏂规硶閿欒:" + ex.getMessage();
						} catch (InstantiationException e) {
							String parasStr = "";
							while (getRequest().getParameterNames().hasMoreElements())
							{
								String key = (String) BP.Sys.Glo.getRequest().getParameterNames().nextElement();
								String val = BP.Sys.Glo.getRequest().getParameter(key);
								parasStr += "@" + key + "=" + val;
							}
							return "err@" + e.getMessage() + " 鍙傛暟:" + parasStr;
						} catch (IllegalAccessException e) {
							String parasStr = "";
							while (getRequest().getParameterNames().hasMoreElements())
							{
								String key = (String) BP.Sys.Glo.getRequest().getParameterNames().nextElement();
								String val = BP.Sys.Glo.getRequest().getParameter(key);
								parasStr += "@" + key + "=" + val;
							}
							return "err@" + e.getMessage() + " 鍙傛暟:" + parasStr;
						} catch (ClassNotFoundException e) {
							String parasStr = "";
							while (getRequest().getParameterNames().hasMoreElements())
							{
								String key = (String) BP.Sys.Glo.getRequest().getParameterNames().nextElement();
								String val = BP.Sys.Glo.getRequest().getParameter(key);
								parasStr += "@" + key + "=" + val;
							}
							return "err@" + e.getMessage() + " 鍙傛暟:" + parasStr;
						} catch (NoSuchMethodException e) {
							String parasStr = "";
							while (getRequest().getParameterNames().hasMoreElements())
							{
								String key = (String) BP.Sys.Glo.getRequest().getParameterNames().nextElement();
								String val = BP.Sys.Glo.getRequest().getParameter(key);
								parasStr += "@" + key + "=" + val;
							}
							return "err@" + e.getMessage() + " 鍙傛暟:" + parasStr;
						} catch (InvocationTargetException e) {
							String parasStr = "";
							while (getRequest().getParameterNames().hasMoreElements())
							{
								String key = (String) BP.Sys.Glo.getRequest().getParameterNames().nextElement();
								String val = BP.Sys.Glo.getRequest().getParameter(key);
								parasStr += "@" + key + "=" + val;
							}
							return "err@" + e.getMessage() + " 鍙傛暟:" + parasStr;
						}
			
						///#endregion 鎵цentity绫荤殑鏂规硶.
					}
			
					///#endregion


			
					///#region 鏁版嵁搴撶浉鍏�.
					/** 
					 杩愯SQL
					 
					 @return 杩斿洖褰卞搷琛屾暟
					*/
					public final String DBAccess_RunSQL()
					{
						String sql = this.GetRequestVal("SQL");
						return DBAccess.RunSQL(sql) + "";
					}
					/** 
					 杩愯SQL杩斿洖DataTable
					 
					 @return DataTable杞崲鐨刯son
					*/
					public final String DBAccess_RunSQLReturnTable()
					{
						String sql = this.GetRequestVal("SQL");
						sql = sql.replaceAll("~","'");
						sql = sql.replaceAll("-","%");
						if(null == sql || StringUtils.isEmpty(sql)){
							return "err@鏌ヨsql涓虹┖";
						}
						DataTable dt = DBAccess.RunSQLReturnTable(sql);
						return BP.Tools.Json.ToJson(dt);
					}
			
					///#endregion

					//鎵ц鏂规硶.
					public final String HttpHandler()
					{
						
						//鑾峰緱涓や釜鍙傛暟.
						String httpHandlerName = this.GetRequestVal("HttpHandlerName");
						String methodName = this.GetRequestVal("DoMethod");
						try {
							Object tempVar = null;
						try {
							tempVar = java.lang.Class.forName(httpHandlerName).newInstance();
						} catch (InstantiationException e) {
							e.printStackTrace();
							return "err@鎵ц鏂规硶"+methodName+"閿欒:"+e.getMessage();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
							return "err@鎵ц鏂规硶"+methodName+"閿欒:"+e.getMessage();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
							return "err@鎵ц鏂规硶"+methodName+"閿欒:"+e.getMessage();
						}
						WebContralBase en = (WebContralBase)((tempVar instanceof WebContralBase) ? tempVar : null);

						en.context = this.context;
						return en.DoMethod(en, methodName);

						} catch (Exception e) {							 
							
							return "err@鎵ц鏂规硶"+methodName+"閿欒:"+e.getMessage();
						}
					}
			
	// #endregion 鐣岄潰鏂规硶.

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
	// 鎵ц涓�涓猄QL锛岀劧鍚庤繑鍥炰竴涓垪琛�.
	// 鐢ㄤ簬gener.js 鐨勫叕鍏辨柟娉�.
	// </summary>
	// <returns></returns>
	public String SQLList() {
		String sqlKey = this.getRequest().getParameter("SQLKey"); // SQL鐨刱ey.
		String paras = this.getRequest().getParameter("Paras"); // 鍙傛暟. 鏍煎紡涓�
		BP.Sys.XML.SQLList sqlXml = new BP.Sys.XML.SQLList(sqlKey);
		// 鑾峰緱SQL
		String sql = sqlXml.getSQL();
		String[] strs = paras.split("@");
		for (String str : strs) {
			if (str == null || str == "")
				continue;
			// 鍙傛暟.
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
	// #endregion 鍏叡鏂规硶銆�

	/** 褰撳墠鐧诲綍浜哄憳淇℃伅
	 
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
	
	/// 杩愯Url杩斿洖string.
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
