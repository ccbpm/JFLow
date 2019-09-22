package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Port.*;
import BP.En.*;
import BP.Tools.*;
import BP.WF.*;
import BP.Web.*;
import BP.Sys.*;
import BP.WF.Template.*;
import BP.WF.*;
import java.util.*;

/** 
 处理页面的业务逻辑
*/
public class WF_Admin_FoolFormDesigner extends DirectoryPageBase
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 表单设计器.
	/** 
	 是不是第一次进来.
	*/
	public final boolean getIsFirst()
	{
		if (this.GetRequestVal("IsFirst") == null || this.GetRequestVal("IsFirst").equals("") || this.GetRequestVal("IsFirst").equals("null"))
		{
			return false;
		}
		return true;
	}
	/** 
	  设计器初始化.
	 
	 @return 
	*/
	public final String Designer_Init()
	{
		DataSet ds = new DataSet();
		//如果是第一次进入，就执行旧版本的升级检查.
		if (this.getIsFirst() == true)
		{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region
			if (this.getFK_MapData().contains("BP.") == true)
			{
				/*如果是类的实体.*/
				Entities ens = ClassFactory.GetEns(this.getFK_MapData());
				Entity en = ens.GetNewEntity;

				MapData mymd = new MapData();
				mymd.No = this.getFK_MapData();
				int i = mymd.RetrieveFromDBSources();
				if (i == 0)
				{
					en.DTSMapToSys_MapData(this.getFK_MapData()); //调度数据到
				}

				mymd.RetrieveFromDBSources();
				mymd.HisFrmType = FrmType.FoolForm;
				mymd.Update();

			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion

			MapFrmFool cols = new MapFrmFool(this.getFK_MapData());
			cols.DoCheckFixFrmForUpdateVer();
			return "url@Designer.htm?FK_MapData=" + this.getFK_MapData() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node();
		}
		//把表单属性放入里面去.
		MapData md = new MapData(this.getFK_MapData());
		//清缓存
		md.ClearCash();
		ds.Tables.Add(md.ToDataTableField("Sys_MapData").Copy());


		// 字段属性.
		MapAttrs attrs = new MapAttrs(this.getFK_MapData());
		for (MapAttr item : attrs)
		{
			item.DefVal = item.DefValReal;
		}

		ds.Tables.Add(attrs.ToDataTableField("Sys_MapAttr"));

		GroupFields gfs = new GroupFields(this.getFK_MapData());
		ds.Tables.Add(gfs.ToDataTableField("Sys_GroupField"));

		MapDtls dtls = new MapDtls();
		dtls.Retrieve(MapDtlAttr.FK_MapData,this.getFK_MapData(),MapDtlAttr.FK_Node,0);
		ds.Tables.Add(dtls.ToDataTableField("Sys_MapDtl"));

		MapFrames frms = new MapFrames(this.getFK_MapData());
		ds.Tables.Add(frms.ToDataTableField("Sys_MapFrame"));



		//附件表.
		FrmAttachments aths = new FrmAttachments(this.getFK_MapData());
		ds.Tables.Add(aths.ToDataTableField("Sys_FrmAttachment"));


		//加入扩展属性.
		MapExts MapExts = new MapExts(this.getFK_MapData());
		ds.Tables.Add(MapExts.ToDataTableField("Sys_MapExt"));

		// 检查组件的分组是否完整?
		for (GroupField item : gfs)
		{
				boolean isHave = false;
			if (item.CtrlType.equals("Dtl"))
			{
				for (MapDtl dtl : dtls)
				{
					if (dtl.No == item.CtrlID)
					{
						isHave = true;
						break;
					}
				}
				//分组不存在了，就删除掉他.
				if (isHave == false)
				{
					item.Delete();
				}
			}
		}

		if (this.getFK_MapData().indexOf("ND") == 0)
		{
			String nodeStr = this.getFK_MapData().replace("ND", "");
			if (DataType.IsNumStr(nodeStr) == true)
			{
				FrmNodeComponent fnc = new FrmNodeComponent(Integer.parseInt(nodeStr));
				//   var f = fnc.GetValFloatByKey("FWC_H");
				ds.Tables.Add(fnc.ToDataTableField("WF_Node").Copy());
			}
		}


		//把dataet转化成json 对象.
		return BP.Tools.Json.ToJson(ds);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 初始化
	 
	 @return 
	*/
	public final String MapDefDtlFreeFrm_Init()
	{
		String isFor = this.GetRequestVal("For");
		if (!isFor.equals(""))
		{
			return "sln@" + isFor;
		}

		if (this.getFK_MapDtl().contains("_Ath") == true)
		{
			return "info@附件扩展";
		}


		MapDtl dtl = new MapDtl();

		//如果传递来了节点信息, 就是说明了独立表单的节点方案处理, 现在就要做如下判断
		if (this.getFK_Node() != 0)
		{
			dtl.No = this.getFK_MapDtl() + "_" + this.getFK_Node();

			if (dtl.RetrieveFromDBSources() == 0)
			{
				// 开始复制它的属性.
				MapAttrs attrs = new MapAttrs(this.getFK_MapDtl());
				MapDtl odtl = new Sys.MapDtl();
				odtl.No = this.getFK_MapDtl();
				int i = odtl.RetrieveFromDBSources();
				if (i == 0)
				{
					return "info@字段列";
				}


				//存储表要与原明细表一致
				if (tangible.StringHelper.isNullOrWhiteSpace(odtl.PTable))
				{
					dtl.PTable = odtl.No;
				}
				else
				{
					dtl.PTable = odtl.PTable;
				}

				//让其直接保存.
				dtl.No = this.getFK_MapDtl() + "_" + this.getFK_Node();
				dtl.FK_MapData = "Temp";
				dtl.DirectInsert(); //生成一个明细表属性的主表.

				//字段的分组也要一同复制
				HashMap<Integer, Integer> groupids = new HashMap<Integer, Integer>();

				//循环保存字段.
				int idx = 0;
				for (MapAttr item : attrs)
				{
					if (item.GroupID != 0)
					{
						if (groupids.containsKey(item.GroupID))
						{
							item.GroupID = groupids.get(item.GroupID);
						}
						else
						{
							GroupField gf = new Sys.GroupField();
							gf.OID = item.GroupID;

							if (gf.RetrieveFromDBSources() == 0)
							{
								gf.Lab = "默认分组";
							}

							gf.EnName = dtl.No;
							gf.InsertAsNew();

							if (groupids.containsKey(item.GroupID) == false)
							{
								groupids.put(item.GroupID, gf.OID);
							}

							item.GroupID = gf.OID;
						}
					}

					item.FK_MapData = this.getFK_MapDtl() + "_" + this.getFK_Node();
					item.MyPK = item.FK_MapData + "_" + item.KeyOfEn;
					item.Save();
					idx++;
					item.Idx = idx;
					item.DirectUpdate();
				}

				MapData md = new MapData();
				md.No = "Temp";
				if (md.IsExits == false)
				{
					md.Name = "为权限方案设置的临时的数据";
					md.Insert();
				}
			}

			return "sln@" + dtl.No;
		}

		dtl.No = this.getFK_MapDtl();
		if (dtl.RetrieveFromDBSources() == 0)
		{
			BP.Sys.CCFormAPI.CreateOrSaveDtl(this.getFK_MapData(), this.getFK_MapDtl(), this.getFK_MapDtl(), 100, 200);
		}
		else
		{
			BP.Sys.CCFormAPI.CreateOrSaveDtl(this.getFK_MapData(), this.getFK_MapDtl(), dtl.Name, dtl.X, dtl.Y);
		}

		return "创建成功.";
	}


	/** 
	 构造函数
	*/
	public WF_Admin_FoolFormDesigner()
	{
	}
	/** 
	 转拼音
	 
	 @return 
	*/
	public final String ParseStringToPinyin()
	{
		String name = GetRequestVal("name");
		String flag = GetRequestVal("flag");
		//此处为字段中文转拼音，设置为最大20个字符，edited by liuxc,2017-9-25
		return BP.Sys.CCFormAPI.ParseStringToPinyinField(name, Equals(flag, "true"), true, 20);
	}

	public final String Designer_GFDoUp()
	{
		String msg = "";
		GroupField gf = new GroupField(this.getRefOID());
		gf.DoUp();

		return msg;
	}
	public final String Designer_GFDoDown()
	{
		String msg = "";
		GroupField mygf = new GroupField(this.getRefOID());
		mygf.DoDown();

		return msg;
	}
	/** 
	 增加一个枚举类型
	 
	 @return 
	*/
	public final String SysEnumList_SaveEnumField()
	{
		MapAttr attr = new Sys.MapAttr();
		attr.MyPK = this.getFK_MapData() + "_" + this.getKeyOfEn();
		if (attr.RetrieveFromDBSources() != 0)
		{
			return "err@字段名[" + this.getKeyOfEn() + "]已经存在.";
		}

		attr.FK_MapData = this.getFK_MapData();
		attr.KeyOfEn = this.getKeyOfEn();
		attr.UIBindKey = this.GetRequestVal("EnumKey");

		attr.GroupID = this.GetRequestValInt("GroupFeid");

		attr.UIContralType = En.UIContralType.DDL;
		attr.MyDataType = DataType.AppInt;
		attr.LGType = En.FieldTypeS.Enum;

		SysEnumMain sem = new Sys.SysEnumMain();
		sem.No = attr.UIBindKey;
		if (sem.RetrieveFromDBSources() != 0)
		{
			attr.Name = sem.Name;
		}
		else
		{
			attr.Name = "枚举" + attr.UIBindKey;
		}

		//paras参数
		Paras ps = new Paras();
		ps.SQL = "SELECT OID FROM Sys_GroupField A WHERE A.FrmID=" + SystemConfig.AppCenterDBVarStr + "FrmID AND ( CtrlType='' OR CtrlType IS NULL ) ORDER BY OID DESC ";
		ps.Add("FrmID", this.getFK_MapData());
		//string sql = "SELECT OID FROM Sys_GroupField A WHERE A.FrmID='" + this.FK_MapData + "' AND ( CtrlType='' OR CtrlType IS NULL ) ORDER BY OID DESC ";
		attr.GroupID = DBAccess.RunSQLReturnValInt(ps, 0);
		attr.Insert();
		return attr.MyPK;
	}

	public final String Designer_NewMapDtl()
	{
		MapDtl en = new MapDtl();
		en.FK_MapData = this.getFK_MapData();
		en.No = this.GetRequestVal("DtlNo");

		if (en.RetrieveFromDBSources() == 1)
		{
			return "err@从表ID:" + en.No + "已经存在.";
		}
		else
		{
			en.FK_Node = this.getFK_Node();
			//en.Name = "从表" + en.No;
			en.Name = "从表";
			en.PTable = en.No;
			en.H = 300;
			en.Insert();
			en.IntMapAttrs();
		}

		//返回字串.
		return en.No;
	}

	/** 
	 创建一个多附件
	 
	 @return 
	*/
	public final String Designer_AthNew()
	{
		FrmAttachment ath = new FrmAttachment();
		ath.FK_MapData = this.getFK_MapData();
		ath.NoOfObj = this.GetRequestVal("AthNo");
		ath.MyPK = ath.FK_MapData + "_" + ath.NoOfObj;
		if (ath.RetrieveFromDBSources() == 1)
		{
			return "err@附件ID:" + ath.NoOfObj + "已经存在.";
		}
		BP.Sys.CCFormAPI.CreateOrSaveAthMulti(this.getFK_MapData(), this.GetRequestVal("AthNo"), "我的附件", 100, 200);
		return ath.MyPK;
	}
	/** 
	 返回信息.
	 
	 @return 
	*/
	public final String GroupField_Init()
	{
		GroupField gf = new GroupField();
		gf.OID = this.GetRequestValInt("GroupField");
		if (gf.OID != 0)
		{
			gf.Retrieve();
		}

		return gf.ToJson();
	}

	/** 
	 保存空白的分组.
	 
	 @return 
	*/
	public final String GroupField_SaveBlank()
	{
		String no = this.GetValFromFrmByKey("TB_Blank_No");
		String name = this.GetValFromFrmByKey("TB_Blank_Name");

		GroupField gf = new GroupField();
		gf.OID = this.GetRequestValInt("GroupField");
		if (gf.OID != 0)
		{
			gf.Retrieve();
		}

		gf.CtrlID = no;
		gf.EnName = this.getFK_MapData();
		gf.Lab = name;
		gf.Save();
		return "保存成功.";
	}

	/** 
	 审核分组保存
	 
	 @return 
	*/
	public final String GroupField_Save()
	{
		String lab = this.GetValFromFrmByKey("TB_Check_Name");
		if (lab.length() == 0)
		{
			return "err@审核岗位不能为空";
		}

		String prx = this.GetValFromFrmByKey("TB_Check_No");
		if (prx.length() == 0)
		{
			prx = DataType.ParseStringToPinyin(lab);
		}

		MapAttr attr = new MapAttr();
		int i = attr.Retrieve(MapAttrAttr.FK_MapData, this.getFK_MapData(), MapAttrAttr.KeyOfEn, prx + "_Note");
		i += attr.Retrieve(MapAttrAttr.FK_MapData, this.getFK_MapData(), MapAttrAttr.KeyOfEn, prx + "_Checker");
		i += attr.Retrieve(MapAttrAttr.FK_MapData, this.getFK_MapData(), MapAttrAttr.KeyOfEn, prx + "_RDT");

		if (i > 0)
		{
			return "err@前缀已经使用：" + prx + " ， 请确认您是否增加了这个审核分组或者，请您更换其他的前缀。";
		}

		BP.Sys.CCFormAPI.CreateCheckGroup(this.getFK_MapData(), lab, prx);

		return "保存成功";
	}
	/** 
	 创建审核分组
	 
	 @return 
	*/
	public final String GroupField_Create()
	{
		BP.Sys.GroupField gf = new GroupField();
		gf.FrmID = this.getFK_MapData();
		gf.Lab = this.GetRequestVal("Lab");
		gf.EnName = this.getFK_MapData();
		gf.Insert();
		return "创建成功..";
	}
	/** 
	 保存分组
	 
	 @return 
	*/
	public final String GroupField_SaveCheck()
	{
		String lab = this.GetRequestVal("TB_Check_Name");
		String prx = this.GetRequestVal("TB_Check_No");
		BP.Sys.CCFormAPI.CreateCheckGroup(this.getFK_MapData(), lab, prx);
		return "创建成功...";
	}
	/** 
	 
	 删除分组
	 
	 @return 
	*/
	public final String GroupField_DeleteCheck()
	{
		GroupField gf = new GroupField();
		gf.OID = this.GetRequestValInt("GroupField");
		gf.Delete();

		BP.WF.Template.MapFrmFool md = new BP.WF.Template.MapFrmFool(this.getFK_MapData());
		md.DoCheckFixFrmForUpdateVer();

		return "删除成功...";
	}

	/** 
	 
	 删除并删除该分组下的字段
	 
	 @return 
	*/
	public final String GroupField_DeleteAllCheck()
	{
		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.GroupID, this.GetRequestValInt("GroupField"));
		for (MapAttr attr : attrs)
		{
			if (attr.HisEditType != EditType.Edit)
			{
				continue;
			}
			if (attr.KeyOfEn.equals("FID"))
			{
				continue;
			}

			attr.Delete();
		}

		GroupField gf = new GroupField();
		gf.OID = this.GetRequestValInt("GroupField");
		gf.Delete();

		return "删除并删除该分组下的字段成功...";
	}

	public final String ImpTableField_Step1()
	{
		BP.Sys.SFDBSrcs ens = new BP.Sys.SFDBSrcs();
		ens.RetrieveAll();
		DataSet ds = new DataSet();
		ds.Tables.Add(ens.ToDataTableField("SFDBSrcs"));
		return BP.Tools.Json.ToJson(ds);
	}

	public final String getFK_MapData()
	{
		String str = this.GetRequestVal("FK_MapData"); //context.Request.QueryString["FK_MapData"];
		if (DataType.IsNullOrEmpty(str))
		{
			return "abc";
		}
		return str;
	}
	public final String getFK_SFDBSrc()
	{
		return this.GetRequestVal("FK_SFDBSrc");
			//return context.Request.QueryString["FK_SFDBSrc"];
	}


	private String _STable = null;
	public final String getSTable()
	{
		if (_STable == null)
		{
				//return this.GetRequestVal("FK_SFDBSrc");

			_STable = this.GetRequestVal("STable"); // context.Request.QueryString["STable"];
			if (_STable == null || "".equals(_STable))
			{
				BP.En.Entity en = BP.En.ClassFactory.GetEn(this.getFK_MapData());
				if (en != null)
				{
					_STable = en.EnMap.PhysicsTable;
				}
				else
				{
					MapData md = new MapData(this.getFK_MapData());
					_STable = md.PTable;
				}
			}
		}

		if (_STable == null)
		{
			_STable = "";
		}
		return _STable;
	}

	public final String ImpTableField_Step2()
	{

		HashMap<String, Object> dictionary = new HashMap<String, Object>();

		SFDBSrc src = new SFDBSrc(this.getFK_SFDBSrc());
		dictionary.put("SFDBSrc", src.ToDataTableField());

		DataTable tables = src.GetTables();
		dictionary.put("tables", tables);

		DataTable tableColumns = src.GetColumns(this.getSTable());
		dictionary.put("columns", tableColumns);

		MapAttrs attrs = new MapAttrs(this.getFK_MapData());
		dictionary.put("attrs", attrs.ToDataTableField("attrs"));
		dictionary.put("STable", this.getSTable());

		return BP.Tools.Json.ToJson(dictionary);
	}

	private ArrayList<String> sCols = null;

	public final ArrayList<String> getSColumns()
	{
		if (sCols != null)
		{
			return sCols;
		}

		String tempVar = this.GetRequestVal("SColumns");
		String cols = tempVar != null ? tempVar : "";
		String[] arr = cols.split("[,]", -1);
		sCols = new ArrayList<String>();

		for (String s : arr)
		{
			if (tangible.StringHelper.isNullOrWhiteSpace(s))
			{
				continue;
			}

			sCols.add(s);
		}

		return sCols;
	}

	public final String ImpTableField_Step3()
	{
		DataSet ds = new DataSet();
		SFDBSrc src = new SFDBSrc(this.getFK_SFDBSrc());
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		var tableColumns = src.GetColumns(this.getSTable());
		DataTable dt = tableColumns.Clone();
		dt.TableName = "selectedColumns";
		for (DataRow dr : tableColumns.Rows)
		{
			if (this.getSColumns().contains(dr.get("No")))
			{
				dt.Rows.Add(dr.ItemArray);
			}
		}
		ds.Tables.Add(dt);
		SysEnums ens = new SysEnums(MapAttrAttr.MyDataType);
		ds.Tables.Add(ens.ToDataTableField("MyDataType"));
		SysEnums ens1 = new SysEnums(MapAttrAttr.LGType);
		ds.Tables.Add(ens1.ToDataTableField("LGType"));
		return BP.Tools.Json.ToJson(ds);
	}
	/** 
	 保存字段
	 
	 @return 
	*/
	public final String ImpTableField_Save()
	{
		MapData md = new MapData();
		md.setNo(this.getFK_MapData());
		md.RetrieveFromDBSources();

		String msg = md.getName() + "导入字段信息:" + this.getFK_MapData();
		boolean isLeft = true;
		float maxEnd = md.getMaxEnd();

		for (String name : HttpContextHelper.RequestParamKeys)
		{
			if (name.startsWith("HID_Idx_") == false)
			{
				continue;
			}

			String columnName = name.substring("HID_Idx_".length());

			MapAttr ma = new MapAttr();
			ma.KeyOfEn = columnName;
			ma.FK_MapData = this.getFK_MapData();
			ma.MyPK = this.getFK_MapData() + "_" + ma.KeyOfEn;
			if (ma.IsExits)
			{
				msg += "\t\n字段:" + ma.KeyOfEn + " - " + ma.Name + "已存在.";
				continue;
			}

			ma.Name = this.GetValFromFrmByKey("TB_Desc_" + columnName);
			if (DataType.IsNullOrEmpty(ma.Name))
			{
				ma.Name = ma.KeyOfEn;
			}

			ma.MyDataType = this.GetValIntFromFrmByKey("DDL_DBType_" + columnName);
			ma.MaxLen = this.GetValIntFromFrmByKey("TB_Len_" + columnName);
			ma.UIBindKey = this.GetValFromFrmByKey("TB_BindKey_" + columnName);
			ma.LGType = BP.En.FieldTypeS.Normal;

			//绑定了外键或者枚举.
			if (DataType.IsNullOrEmpty(ma.UIBindKey) == false)
			{
				SysEnums se = new SysEnums();
				se.Retrieve(SysEnumAttr.EnumKey, ma.UIBindKey);
				if (se.size() > 0)
				{
					ma.MyDataType = BP.DA.DataType.AppInt;
					ma.LGType = BP.En.FieldTypeS.Enum;
					ma.UIContralType = BP.En.UIContralType.DDL;
				}
				SFTable tb = new SFTable();
				tb.No = ma.UIBindKey;
				if (tb.IsExits == true)
				{
					ma.MyDataType = BP.DA.DataType.AppString;
					ma.LGType = BP.En.FieldTypeS.FK;
					ma.UIContralType = BP.En.UIContralType.DDL;
				}
			}

			if (ma.MyDataType == BP.DA.DataType.AppBoolean)
			{
				ma.UIContralType = BP.En.UIContralType.CheckBok;
			}

			ma.Insert();

			msg += "\t\n字段:" + ma.KeyOfEn + " - " + ma.Name + "加入成功.";

			//生成lab.
			FrmLab lab = null;
			if (isLeft == true)
			{
				maxEnd = maxEnd + 40;
				/* 是否是左边 */
				lab = new FrmLab();
				lab.MyPK = BP.DA.DBAccess.GenerGUID();
				lab.FK_MapData = this.getFK_MapData();
				lab.Text = ma.Name;
				lab.X = 40;
				lab.Y = maxEnd;
				lab.Insert();

				ma.X = lab.X + 80;
				ma.Y = maxEnd;
				ma.Update();
			}
			else
			{
				lab = new FrmLab();
				lab.MyPK = BP.DA.DBAccess.GenerGUID();
				lab.FK_MapData = this.getFK_MapData();
				lab.Text = ma.Name;
				lab.X = 350;
				lab.Y = maxEnd;
				lab.Insert();

				ma.X = lab.X + 80;
				ma.Y = maxEnd;
				ma.Update();
			}
			isLeft = !isLeft;
		}

		//更新名称.
		DBAccess.RunSQL("UPDATE Sys_MapAttr SET Name=KeyOfEn WHERE Name=NULL OR Name='' ");

		md.ResetMaxMinXY();
		return msg;
	}
	/** 
	 框架信息.
	 
	 @return 
	*/
	public final String MapFrame_Init()
	{
		MapFrame mf = new MapFrame();
		mf.FK_MapData = this.getFK_MapData();

		if (this.getMyPK() == null)
		{
			mf.URL = "http://ccflow.org";
			mf.W = 400;
			mf.H = 300;
			mf.Name = "我的框架.";
			mf.FK_MapData = this.getFK_MapData();
			mf.MyPK = BP.DA.DBAccess.GenerGUID();
		}
		else
		{
			mf.MyPK = this.getMyPK();
			mf.RetrieveFromDBSources();
		}
		return mf.ToJson();
	}
	/** 
	 框架信息保存.
	 
	 @return 
	*/
	public final String MapFrame_Save()
	{
		MapFrame mf = new MapFrame();
		Object tempVar = BP.Sys.PubClass.CopyFromRequestByPost(mf);
		mf = tempVar instanceof MapFrame ? (MapFrame)tempVar : null;
		mf.FK_MapData = this.getFK_MapData();

		mf.Save(); //执行保存.
		return "保存成功..";
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region SFList 外键表列表.
	/** 
	 删除
	 
	 @return 
	*/
	public final String SFList_Delete()
	{
		try
		{
			SFTable sf = new SFTable(this.getFK_SFTable());
			sf.Delete();
			return "删除成功...";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 字典表列表.
	 
	 @return 
	*/
	public final String SFList_Init()
	{
		DataSet ds = new DataSet();

		SFTables ens = new SFTables();
		ens.RetrieveAll();

		DataTable dt = ens.ToDataTableField("SFTables");
		ds.Tables.Add(dt);

		int pTableModel = 0;
		if (this.GetRequestVal("PTableModel").equals("2"))
		{
			pTableModel = 2;
		}

		//获得ptableModel.
		if (pTableModel == 0)
		{
			MapDtl dtl = new MapDtl();
			dtl.No = this.getFK_MapData();
			if (dtl.RetrieveFromDBSources() == 1)
			{
				pTableModel = dtl.PTableModel;
			}
			else
			{
				MapData md = new MapData();
				md.No = this.getFK_MapData();
				if (md.RetrieveFromDBSources() == 1)
				{
					pTableModel = md.PTableModel;
				}
			}
		}

		if (pTableModel == 2)
		{
			DataTable mydt = MapData.GetFieldsOfPTableMode2(this.getFK_MapData());
			mydt.TableName = "Fields";
			ds.Tables.Add(mydt);
		}

		return BP.Tools.Json.ToJson(ds);
	}
	public final String SFList_SaveSFField()
	{
		MapAttr attr = new Sys.MapAttr();
		attr.MyPK = this.getFK_MapData() + "_" + this.getKeyOfEn();
		if (attr.RetrieveFromDBSources() != 0)
		{
			return "err@字段名[" + this.getKeyOfEn() + "]已经存在.";
		}

		BP.Sys.CCFormAPI.SaveFieldSFTable(this.getFK_MapData(), this.getKeyOfEn(), null, this.GetRequestVal("SFTable"), 100, 100, 1);

		attr.Retrieve();
		Paras ps = new Paras();
		ps.SQL = "SELECT OID FROM Sys_GroupField A WHERE A.FrmID=" + SystemConfig.AppCenterDBVarStr + "FrmID AND (CtrlType='' OR CtrlType IS NULL) ORDER BY OID DESC ";
		ps.Add("FrmID",this.getFK_MapData());
		//string sql = "SELECT OID FROM Sys_GroupField A WHERE A.FrmID='" + this.FK_MapData + "' AND (CtrlType='' OR CtrlType IS NULL) ORDER BY OID DESC ";
		attr.GroupID = DBAccess.RunSQLReturnValInt(ps, 0);
		attr.Update();

		SFTable sf = new SFTable(attr.UIBindKey);

		if (sf.SrcType == SrcType.TableOrView || sf.SrcType == SrcType.BPClass || sf.SrcType == SrcType.CreateTable)
		{
			return "../../Comm/En.htm?EnName=BP.Sys.FrmUI.MapAttrSFTable&PKVal=" + attr.MyPK;
		}
		else
		{
			return "../../Comm/En.htm?EnName=BP.Sys.FrmUI.MapAttrSFSQL&PKVal=" + attr.MyPK;
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 外键表列表.




	/** 
	 初始化表.
	 
	 @return 
	*/
	public final String EditTableField_Init()
	{
		MapAttr attr = new MapAttr();
		attr.KeyOfEn = this.getKeyOfEn();
		attr.FK_MapData = this.getFK_MapData();

		if (DataType.IsNullOrEmpty(this.getMyPK()) == false)
		{
			attr.MyPK = this.getMyPK();
			attr.RetrieveFromDBSources();
		}
		else
		{
			SFTable sf = new SFTable(this.getFK_SFTable());
			attr.Name = sf.Name;
			attr.KeyOfEn = sf.No;
		}

		//第1次加载.
		attr.UIContralType = UIContralType.DDL;

		attr.FK_MapData = this.getFK_MapData();

		//字体大小.
		int size = attr.Para_FontSize;
		if (size == 0)
		{
			attr.Para_FontSize = 12;
		}

		//横跨的列数.
		if (attr.ColSpan == 0)
		{
			attr.ColSpan = 1;
		}

		return attr.ToJson();
	}
	/** 
	 从表里选择字段.
	 
	 @return 
	*/
	public final String FieldTypeListChoseOneField_Init()
	{
		String ptable = "";

		MapDtl dtl = new MapDtl();
		dtl.No = this.getFK_MapData();
		if (dtl.RetrieveFromDBSources() == 1)
		{
			ptable = dtl.PTable;
		}
		else
		{
			MapData md = new MapData(this.getFK_MapData());
			ptable = md.PTable;
		}

		//获得原始数据.
		DataTable dt = BP.DA.DBAccess.GetTableSchema(ptable, false);

		//创建样本.
		DataTable mydt = BP.DA.DBAccess.GetTableSchema(ptable, false);
		mydt.Rows.Clear();

		//获得现有的列..
		MapAttrs attrs = new MapAttrs(this.getFK_MapData());

		String flowFiels = ",GUID,PRI,PrjNo,PrjName,PEmp,AtPara,FlowNote,WFSta,PNodeID,FK_FlowSort,FK_Flow,OID,FID,Title,WFState,CDT,FlowStarter,FlowStartRDT,FK_Dept,FK_NY,FlowDaySpan,FlowEmps,FlowEnder,FlowEnderRDT,FlowEndNode,MyNum,PWorkID,PFlowNo,BillNo,ProjNo,";

		//排除已经存在的列.
		for (DataRow dr : dt.Rows)
		{
			String key = dr.get("FName").toString();
			if (attrs.Contains(MapAttrAttr.KeyOfEn, key) == true)
			{
				continue;
			}

			if (flowFiels.contains("," + key + ",") == true)
			{
				continue;
			}

			DataRow mydr = mydt.NewRow();
			mydr.set("FName", dr.get("FName"));
			mydr.set("FType", dr.get("FType"));
			mydr.set("FLen", dr.get("FLen"));
			mydr.set("FDesc", dr.get("FDesc"));
			mydt.Rows.Add(mydr);
		}

		mydt.TableName = "dt";
		return BP.Tools.Json.ToJson(mydt);
	}
	public final String FieldTypeListChoseOneField_Save()
	{
		int dataType = this.GetRequestValInt("DataType");
		String keyOfEn = this.GetRequestVal("KeyOfEn");
		String name = this.GetRequestVal("FDesc");
		String frmID = this.GetRequestVal("FK_MapData");

		MapAttr attr = new MapAttr();
		attr.FK_MapData = frmID;
		attr.KeyOfEn = keyOfEn;
		attr.MyPK = attr.FK_MapData + "_" + keyOfEn;
		if (attr.IsExits)
		{
			return "err@该字段[" + keyOfEn + "]已经加入里面了.";
		}

		attr.Name = name;
		attr.MyDataType = dataType;

		if (BP.DA.DataType.AppBoolean == dataType)
		{
			attr.UIContralType = UIContralType.CheckBok;
		}
		else
		{
			attr.UIContralType = UIContralType.TB;
		}

		Paras ps = new Paras();
		ps.SQL = "SELECT OID FROM Sys_GroupField A WHERE A.FrmID=" + SystemConfig.AppCenterDBVarStr + "FrmID AND CtrlType='' OR CtrlType= NULL";
		ps.Add("FrmID", this.getFK_MapData());
		//string sql = "SELECT OID FROM Sys_GroupField A WHERE A.FrmID='" + this.FK_MapData + "' AND CtrlType='' OR CtrlType= NULL";
		attr.GroupID = DBAccess.RunSQLReturnValInt(ps, 0);

		attr.Insert();

		return "保存成功.";
	}
	/** 
	 字段选择.
	 
	 @return 
	*/
	public final String FieldTypeSelect_Create()
	{
		String no = this.GetRequestVal("KeyOfEn");
		if (no.equals("No"))
		{
			no = "No1";
		}

		String name = this.GetRequestVal("name");
		String newNo = DataType.ParseStringForNo(no, 20);
		String newName = DataType.ParseStringForName(name, 20);
		int fType = Integer.parseInt(this.GetRequestVal("FType"));

		MapAttrs attrs = new MapAttrs();
		int i = attrs.Retrieve(MapAttrAttr.FK_MapData, this.getFK_MapData(), MapAttrAttr.KeyOfEn, newNo);
		if (i != 0)
		{
			return "err@字段名：" + newNo + "已经存在.";
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 计算GroupID  需要翻译
		int iGroupID = this.getGroupField();
		try
		{
			Paras ps = new Paras();
			ps.SQL = "SELECT OID FROM Sys_GroupField WHERE FrmID=" + SystemConfig.AppCenterDBVarStr + "FrmID and (CtrlID is null or ctrlid ='') ORDER BY OID DESC ";
			ps.Add("FrmID", this.getFK_MapData());
			DataTable dt = DBAccess.RunSQLReturnTable(ps);
			if (dt != null && dt.Rows.size() > 0)
			{
				iGroupID = Integer.parseInt(dt.Rows[0][0].toString());
			}
		}
		catch (RuntimeException ex)
		{

		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

		try
		{
			MapData md = new MapData();
			md.No = this.getFK_MapData();
			if (md.RetrieveFromDBSources() != 0)
			{
				md.CheckPTableSaveModel(newNo);
			}
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}

		//求出选择的字段类型.
		MapAttr attr = new MapAttr();
		attr.Name = newName;
		attr.KeyOfEn = newNo;
		attr.FK_MapData = this.getFK_MapData();
		attr.LGType = FieldTypeS.Normal;
		attr.MyPK = this.getFK_MapData() + "_" + newNo;
		attr.GroupID = iGroupID;
		attr.MyDataType = fType;

		int colspan = attr.ColSpan;
		attr.Para_FontSize = 12;
		int rows = attr.UIRows;

		if (attr.MyDataType == DataType.AppString)
		{
			attr.UIWidth = 100;
			attr.UIHeight = 23;
			attr.UIVisible = true;
			attr.UIIsEnable = true;
			attr.ColSpan = 1;
			attr.MinLen = 0;
			attr.MaxLen = 50;
			attr.MyDataType = DataType.AppString;
			attr.UIContralType = UIContralType.TB;
			attr.Insert();
			return "url@../../Comm/En.htm?EnName=BP.Sys.FrmUI.MapAttrString&MyPK=" + attr.MyPK;
		}

		if (attr.MyDataType == DataType.AppInt)
		{
			attr.UIWidth = 100;
			attr.UIHeight = 23;
			attr.UIVisible = true;
			attr.UIIsEnable = true;
			attr.ColSpan = 1;
			attr.MinLen = 0;
			attr.MaxLen = 50;
			attr.MyDataType = DataType.AppInt;
			attr.UIContralType = UIContralType.TB;
			attr.DefVal = "0";
			attr.Insert();

			return "url@../../Comm/En.htm?EnName=BP.Sys.FrmUI.MapAttrNum&MyPK=" + attr.MyPK + "&FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + newNo + "&FType=" + attr.MyDataType + "&DoType=Edit&GroupField=" + this.getGroupField();
		}

		if (attr.MyDataType == DataType.AppMoney)
		{
			attr.UIWidth = 100;
			attr.UIHeight = 23;
			attr.UIVisible = true;
			attr.UIIsEnable = true;
			attr.ColSpan = 1;
			attr.MinLen = 0;
			attr.MaxLen = 50;
			attr.MyDataType = DataType.AppMoney;
			attr.UIContralType = UIContralType.TB;
			attr.DefVal = "0.00";
			attr.Insert();
			return "url@../../Comm/En.htm?EnName=BP.Sys.FrmUI.MapAttrNum&MyPK=" + attr.MyPK + "&FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + newNo + "&FType=" + attr.MyDataType + "&DoType=Edit&GroupField=" + this.getGroupField();
		}

		if (attr.MyDataType == DataType.AppFloat)
		{
			attr.UIWidth = 100;
			attr.UIHeight = 23;
			attr.UIVisible = true;
			attr.UIIsEnable = true;
			attr.ColSpan = 1;
			attr.MinLen = 0;
			attr.MaxLen = 50;
			attr.MyDataType = DataType.AppFloat;
			attr.UIContralType = UIContralType.TB;

			attr.DefVal = "0";
			attr.Insert();

			return "url@../../Comm/En.htm?EnName=BP.Sys.FrmUI.MapAttrNum&MyPK=" + attr.MyPK + "&FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + newNo + "&FType=" + attr.MyDataType + "&DoType=Edit&GroupField=" + this.getGroupField();
		}

		if (attr.MyDataType == DataType.AppDouble)
		{
			attr.UIWidth = 100;
			attr.UIHeight = 23;
			attr.UIVisible = true;
			attr.UIIsEnable = true;
			attr.ColSpan = 1;
			attr.MinLen = 0;
			attr.MaxLen = 50;
			attr.MyDataType = DataType.AppDouble;
			attr.UIContralType = UIContralType.TB;
			attr.DefVal = "0";
			attr.Insert();



			return "url@../../Comm/En.htm?EnName=BP.Sys.FrmUI.MapAttrNum&MyPK=" + attr.MyPK + "&FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + newNo + "&FType=" + attr.MyDataType + "&DoType=Edit&GroupField=" + this.getGroupField();
		}

		if (attr.MyDataType == DataType.AppDate)
		{

			attr.UIWidth = 100;
			attr.UIHeight = 23;
			attr.UIVisible = true;
			attr.UIIsEnable = true;
			attr.ColSpan = 1;
			attr.MinLen = 0;
			attr.MaxLen = 50;
			attr.UIContralType = UIContralType.TB;
			attr.MyDataType = DataType.AppDate;
			attr.Insert();

			BP.Sys.FrmUI.MapAttrDT dt = new Sys.FrmUI.MapAttrDT();
			dt.MyPK = attr.MyPK;
			dt.RetrieveFromDBSources();
			dt.Format = 0;
			dt.Update();


			return "url@../../Comm/En.htm?EnName=BP.Sys.FrmUI.MapAttrDT&MyPK=" + attr.MyPK + "&FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + newNo + "&FType=" + attr.MyDataType + "&DoType=Edit&GroupField=" + this.getGroupField();
		}

		if (attr.MyDataType == DataType.AppDateTime)
		{
			attr.UIWidth = 100;
			attr.UIHeight = 23;
			attr.UIVisible = true;
			attr.UIIsEnable = true;
			attr.ColSpan = 1;
			attr.MinLen = 0;
			attr.MaxLen = 50;
			attr.UIContralType = UIContralType.TB;
			attr.MyDataType = DataType.AppDateTime;
			attr.Insert();

			BP.Sys.FrmUI.MapAttrDT dt = new Sys.FrmUI.MapAttrDT();
			dt.MyPK = attr.MyPK;
			dt.RetrieveFromDBSources();
			dt.Format = 1;
			dt.Update();

			return "url@../../Comm/En.htm?EnName=BP.Sys.FrmUI.MapAttrDT&MyPK=" + attr.MyPK + "&FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + newNo + "&FType=" + attr.MyDataType + "&DoType=Edit&GroupField=" + this.getGroupField();
		}

		if (attr.MyDataType == DataType.AppBoolean)
		{
			attr.UIWidth = 100;
			attr.UIHeight = 23;
			attr.UIVisible = true;
			attr.UIIsEnable = true;
			attr.ColSpan = 1;
			attr.MinLen = 0;
			attr.MaxLen = 50;
			attr.UIContralType = UIContralType.CheckBok;
			attr.MyDataType = DataType.AppBoolean;
			attr.DefVal = "0";
			attr.Insert();

			return "url@../../Comm/En.htm?EnName=BP.Sys.FrmUI.MapAttrBoolen&MyPK=" + attr.MyPK + "&FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + newNo + "&FType=" + attr.MyDataType + "&DoType=Edit&GroupField=" + this.getGroupField();
		}

		return "err@没有判断的数据类型." + attr.MyDataTypeStr;
	}
	/** 
	 字段初始化数据.
	 
	 @return 
	*/
	public final String EditF_FieldInit()
	{
		MapAttr attr = new MapAttr();
		attr.KeyOfEn = this.getKeyOfEn();
		attr.FK_MapData = this.getFK_MapData();

		if (DataType.IsNullOrEmpty(this.getMyPK()) == false)
		{
			attr.MyPK = this.getMyPK();
			attr.RetrieveFromDBSources();
		}
		else
		{
			attr.GroupID = this.getGroupField();
		}

		attr.FK_MapData = this.getFK_MapData();

		//字体大小.
		int size = attr.Para_FontSize;
		if (size == 0)
		{
			attr.Para_FontSize = 12;
		}

		String field = attr.Para_SiganField;
		boolean IsEnableJS = attr.IsEnableJS;
		boolean IsSupperText = attr.IsSupperText; //是否是超大文本？
		boolean isBigDoc = attr.IsBigDoc;

		//横跨的列数.
		if (attr.ColSpan == 0)
		{
			attr.ColSpan = 1;
		}

		return attr.ToJson();
	}
	public final String FieldInitEnum()
	{
		MapAttr attr = new MapAttr();
		attr.KeyOfEn = this.getKeyOfEn();
		attr.FK_MapData = this.getFK_MapData();

		if (DataType.IsNullOrEmpty(this.getMyPK()) == false)
		{
			attr.MyPK = this.getMyPK();
			attr.RetrieveFromDBSources();
		}
		else
		{
			SysEnumMain sem = new SysEnumMain(this.getEnumKey());
			attr.Name = sem.Name;
			attr.KeyOfEn = sem.No;
			attr.DefVal = "0";
		}

		//第1次加载.
		if (attr.UIContralType == UIContralType.TB)
		{
			attr.UIContralType = UIContralType.DDL;
		}

		attr.FK_MapData = this.getFK_MapData();

		//字体大小.
		int size = attr.Para_FontSize;
		if (size == 0)
		{
			attr.Para_FontSize = 12;
		}

		//横跨的列数.
		if (attr.ColSpan == 0)
		{
			attr.ColSpan = 1;
		}

//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		var model = attr.RBShowModel;
		attr.RBShowModel = model;

		return attr.ToJson();
	}
	/** 
	 转化成json
	 
	 @return 
	*/
	public final String FieldInitGroupID()
	{
		GroupFields gfs = new GroupFields(this.getFK_MapData());

		//转化成json输出.
		return gfs.ToJson();
	}
	/** 
	 分组&枚举： 两个数据源.
	 
	 @return 
	*/
	public final String FieldInitGroupAndSysEnum()
	{
		GroupFields gfs = new GroupFields(this.getFK_MapData());

		//分组值.
		DataSet ds = new DataSet();
		ds.Tables.Add(gfs.ToDataTableField("Sys_GroupField"));

		//枚举值.
		String enumKey = this.getEnumKey();
		if (enumKey.equals("") || enumKey == null || enumKey.equals("null"))
		{
			MapAttr ma = new MapAttr(this.getMyPK());
			enumKey = ma.UIBindKey;
		}

		SysEnums enums = new SysEnums(enumKey);
		ds.Tables.Add(enums.ToDataTableField("Sys_Enum"));

		//转化成json输出.
		String json = BP.Tools.Json.ToJson(ds);
		// BP.DA.DataType.WriteFile("c:\\FieldInitGroupAndSysEnum.json", json);
		return json;
	}

	/** 
	 执行删除.
	 
	 @return 
	*/
	public final String FieldDelete()
	{
		try
		{
			MapAttr attr = new MapAttr();
			attr.MyPK = this.getMyPK();
			attr.RetrieveFromDBSources();
			attr.Delete();
			return "删除成功...";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 保存枚举值.
	 
	 @return 
	*/
	public final String FieldSaveEnum()
	{
		try
		{
			//定义变量.
			if (this.getEnumKey() == null)
			{
				return "err@没有接收到EnumKey的值，无法进行保存操作。";
			}


			//@周朋 , 判断数据模式，创建的字段是否符合要求.
			MapData md = new MapData(this.getFK_MapData());
			md.CheckPTableSaveModel(this.getKeyOfEn());



			//赋值.
			MapAttr attr = new MapAttr();
			attr.KeyOfEn = this.getKeyOfEn();
			attr.FK_MapData = this.getFK_MapData();
			if (DataType.IsNullOrEmpty(this.getMyPK()) == false)
			{
				attr.MyPK = this.getMyPK();
				attr.RetrieveFromDBSources();
			}
			else
			{
				/*判断字段是否存在？*/
				if (attr.IsExit(MapAttrAttr.KeyOfEn, this.getKeyOfEn(), MapAttrAttr.FK_MapData, this.getFK_MapData()) == true)
				{
					return "err@字段名:" + this.getKeyOfEn() + "已经存在.";
				}
			}

			attr.KeyOfEn = this.getKeyOfEn();
			attr.FK_MapData = this.getFK_MapData();
			attr.LGType = FieldTypeS.Enum;
			attr.UIBindKey = this.getEnumKey();
			attr.MyDataType = DataType.AppInt;

			//控件类型.
			attr.UIContralType = UIContralType.DDL;

			attr.Name = this.GetValFromFrmByKey("TB_Name");
			attr.KeyOfEn = this.GetValFromFrmByKey("TB_KeyOfEn");
			attr.ColSpan = this.GetValIntFromFrmByKey("DDL_ColSpan");
			if (attr.ColSpan == 0)
			{
				attr.ColSpan = 1;
			}

			//显示方式.
			attr.RBShowModel = this.GetValIntFromFrmByKey("DDL_RBShowModel");

			//控件类型.
			attr.UIContralType = (UIContralType)this.GetValIntFromFrmByKey("RB_CtrlType");

			attr.UIIsInput = this.GetValBoolenFromFrmByKey("CB_IsInput"); //是否是必填项.

			attr.IsEnableJS = this.GetValBoolenFromFrmByKey("CB_IsEnableJS"); //是否启用js设置？

			attr.Para_FontSize = this.GetValIntFromFrmByKey("TB_FontSize"); //字体大小.

			//默认值.
			attr.DefVal = this.GetValFromFrmByKey("TB_DefVal");

			try
			{
				//分组.
				if (this.GetValIntFromFrmByKey("DDL_GroupID") != 0)
				{
					attr.GroupID = this.GetValIntFromFrmByKey("DDL_GroupID"); //在那个分组里？
				}
			}
			catch (java.lang.Exception e)
			{

			}

			//是否可用？所有类型的属性，都需要。
			int isEnable = this.GetValIntFromFrmByKey("RB_UIIsEnable");
			if (isEnable == 0)
			{
				attr.UIIsEnable = false;
			}
			else
			{
				attr.UIIsEnable = true;
			}

			//是否可见?
			int visable = this.GetValIntFromFrmByKey("RB_UIVisible");
			if (visable == 0)
			{
				attr.UIVisible = false;
			}
			else
			{
				attr.UIVisible = true;
			}

			attr.MyPK = this.getFK_MapData() + "_" + this.getKeyOfEn();

			attr.Save();

			return "保存成功.";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 保存外键表字段.
	 
	 @return 
	*/
	public final String EditTableField_Save()
	{
		try
		{
			//定义变量.
			if (this.getFK_SFTable() == null)
			{
				return "err@没有接收到FK_SFTable的值，无法进行保存操作。";
			}

			//赋值.
			MapAttr attr = new MapAttr();
			attr.KeyOfEn = this.getKeyOfEn();
			attr.FK_MapData = this.getFK_MapData();
			if (DataType.IsNullOrEmpty(this.getMyPK()) == false)
			{
				attr.MyPK = this.getMyPK();
				attr.RetrieveFromDBSources();
			}
			else
			{
				/*判断字段是否存在？*/
				if (attr.IsExit(MapAttrAttr.KeyOfEn, this.getKeyOfEn(), MapAttrAttr.FK_MapData, this.getFK_MapData()) == true)
				{
					return "err@字段名:" + this.getKeyOfEn() + "已经存在.";
				}
			}

			attr.KeyOfEn = this.getKeyOfEn();
			attr.FK_MapData = this.getFK_MapData();
			attr.LGType = FieldTypeS.FK;
			attr.UIBindKey = this.getFK_SFTable();
			attr.MyDataType = DataType.AppString;

			//控件类型.
			attr.UIContralType = UIContralType.DDL;

			attr.Name = this.GetValFromFrmByKey("TB_Name");
			attr.KeyOfEn = this.GetValFromFrmByKey("TB_KeyOfEn");
			attr.ColSpan = this.GetValIntFromFrmByKey("DDL_ColSpan");
			if (attr.ColSpan == 0)
			{
				attr.ColSpan = 1;
			}

			attr.UIIsInput = this.GetValBoolenFromFrmByKey("CB_IsInput"); //是否是必填项.

			attr.Para_FontSize = this.GetValIntFromFrmByKey("TB_FontSize"); //字体大小.

			//默认值.
			attr.DefVal = this.GetValFromFrmByKey("TB_DefVal");

			try
			{
				//分组.
				if (this.GetValIntFromFrmByKey("DDL_GroupID") != 0)
				{
					attr.GroupID = this.GetValIntFromFrmByKey("DDL_GroupID"); //在那个分组里？
				}
			}
			catch (java.lang.Exception e)
			{

			}

			//是否可用？所有类型的属性，都需要。
			int isEnable = this.GetValIntFromFrmByKey("RB_UIIsEnable");
			if (isEnable == 0)
			{
				attr.UIIsEnable = false;
			}
			else
			{
				attr.UIIsEnable = true;
			}

			//是否可见?
			int visable = this.GetValIntFromFrmByKey("RB_UIVisible");
			if (visable == 0)
			{
				attr.UIVisible = false;
			}
			else
			{
				attr.UIVisible = true;
			}

			attr.MyPK = this.getFK_MapData() + "_" + this.getKeyOfEn();
			attr.Save();

			return "保存成功.";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 执行保存.
	 
	 @return 
	*/
	public final String EditF_Save()
	{
		try
		{
			//定义变量.
			int fType = Integer.parseInt(this.GetRequestVal("FType")); //字段数据物理类型
			FieldTypeS lgType = (FieldTypeS)Integer.parseInt(this.GetRequestVal("LGType")); //逻辑类型.
			String uiBindKey = this.GetRequestVal("UIBindKey"); // context.Request.QueryString["UIBindKey"];

			//赋值.
			MapAttr attr = new MapAttr();
			attr.KeyOfEn = this.getKeyOfEn();
			attr.FK_MapData = this.getFK_MapData();
			attr.LGType = lgType; //逻辑类型.
			attr.UIBindKey = uiBindKey; //绑定的枚举或者外键.
			attr.MyDataType = fType; //物理类型.

			if (DataType.IsNullOrEmpty(this.getMyPK()) == false)
			{
				attr.MyPK = this.getMyPK();
				attr.RetrieveFromDBSources();
			}

			attr.FK_MapData = this.getFK_MapData();
			attr.MyDataType = fType; //数据类型.
			attr.Name = this.GetValFromFrmByKey("TB_Name");

			attr.KeyOfEn = this.GetValFromFrmByKey("TB_KeyOfEn");
			attr.ColSpan = this.GetValIntFromFrmByKey("DDL_ColSpan");

			if (attr.ColSpan == 0)
			{
				attr.ColSpan = 1;
			}

			attr.Para_FontSize = this.GetValIntFromFrmByKey("TB_FontSize"); //字体大小.
			attr.Para_Tip = this.GetValFromFrmByKey("TB_Tip"); //操作提示.

			//默认值.
			attr.DefVal = this.GetValFromFrmByKey("TB_DefVal");


			//对于明细表就可能没有值.
			try
			{
				//分组.
				if (this.GetValIntFromFrmByKey("DDL_GroupID") != 0)
				{
					attr.GroupID = this.GetValIntFromFrmByKey("DDL_GroupID"); //在那个分组里？
				}
			}
			catch (java.lang.Exception e)
			{

			}


			//把必填项拿出来，所有字段都可以设置成必填项 杨玉慧
			attr.UIIsInput = this.GetValBoolenFromFrmByKey("CB_IsInput"); //是否是必填项.

			if (attr.MyDataType == BP.DA.DataType.AppString && lgType == FieldTypeS.Normal)
			{
				attr.IsRichText = this.GetValBoolenFromFrmByKey("CB_IsRichText"); //是否是富文本？
				attr.IsSupperText = this.GetValBoolenFromFrmByKey("CB_IsSupperText"); //是否是超大文本？

				//高度.
				attr.UIHeightInt = this.GetValIntFromFrmByKey("DDL_Rows") * 23;

				//最大最小长度.
				attr.MaxLen = this.GetValIntFromFrmByKey("TB_MaxLen");
				attr.MinLen = this.GetValIntFromFrmByKey("TB_MinLen");

				attr.UIWidth = this.GetValIntFromFrmByKey("TB_UIWidth"); //宽度.
			}

			switch (attr.MyDataType)
			{
				case DataType.AppInt:
				case DataType.AppFloat:
				case DataType.AppDouble:
				case DataType.AppMoney:
					attr.IsSum = this.GetValBoolenFromFrmByKey("CB_IsSum");
					break;
			}

			//获取宽度.
			try
			{
				attr.UIWidth = this.GetValIntFromFrmByKey("TB_UIWidth"); //宽度.
			}
			catch (java.lang.Exception e2)
			{
			}


			//是否可用？所有类型的属性，都需要。
			int isEnable = this.GetValIntFromFrmByKey("RB_UIIsEnable");
			if (isEnable == 0)
			{
				attr.UIIsEnable = false;
			}
			else
			{
				attr.UIIsEnable = true;
			}

			//仅仅对普通类型的字段需要.
			if (lgType == FieldTypeS.Normal)
			{
				//是否可见?
				int visable = this.GetValIntFromFrmByKey("RB_UIVisible");
				if (visable == 0)
				{
					attr.UIVisible = false;
				}
				else
				{
					attr.UIVisible = true;
				}
			}

			attr.MyPK = this.getFK_MapData() + "_" + this.getKeyOfEn();
			attr.Save();

			return "保存成功.";
		}
		catch (RuntimeException ex)
		{
			return ex.getMessage();
		}
	}
	/** 
	 该方法有2处调用。
	 1，修改字段。
	 2，编辑属性。
	 
	 @return 
	*/
	public final String DtlInit()
	{
		MapDtl dtl = new MapDtl();
		dtl.No = this.getFK_MapDtl();
		if (dtl.RetrieveFromDBSources() == 0)
		{
			dtl.FK_MapData = this.getFK_MapData();
			dtl.Name = this.getFK_MapData();
			dtl.Insert();
			dtl.IntMapAttrs();
		}

		if (this.getFK_Node() != 0)
		{
			/* 如果传递来了节点信息, 就是说明了独立表单的节点方案处理, 现在就要做如下判断.
			 * 1, 如果已经有了.
			 */
			dtl.No = this.getFK_MapDtl() + "_" + this.getFK_Node();
			if (dtl.RetrieveFromDBSources() == 0)
			{

				// 开始复制它的属性.
				MapAttrs attrs = new MapAttrs(this.getFK_MapDtl());

				//让其直接保存.
				dtl.No = this.getFK_MapDtl() + "_" + this.getFK_Node();
				dtl.FK_MapData = "Temp";
				dtl.DirectInsert(); //生成一个明细表属性的主表.

				//循环保存字段.
				int idx = 0;
				for (MapAttr item : attrs)
				{
					item.FK_MapData = this.getFK_MapDtl() + "_" + this.getFK_Node();
					item.MyPK = item.FK_MapData + "_" + item.KeyOfEn;
					item.Save();
					idx++;
					item.Idx = idx;
					item.DirectUpdate();
				}

				MapData md = new MapData();
				md.No = "Temp";
				if (md.IsExits == false)
				{
					md.Name = "为权限方案设置的临时的数据";
					md.Insert();
				}
			}
		}

		DataSet ds = new DataSet();
		DataTable dt = dtl.ToDataTableField("Main");
		ds.Tables.Add(dt);

		//获得字段列表.
		MapAttrs attrsDtl = new MapAttrs(this.getFK_MapDtl());
		DataTable dtAttrs = attrsDtl.ToDataTableField("Ens");
		ds.Tables.Add(dtAttrs);

		//返回json配置信息.
		return BP.Tools.Json.ToJson(ds);
	}
	/** 
	 执行保存.
	 
	 @return 
	*/
	public final String DtlSave()
	{
		try
		{
			//复制.
			MapDtl dtl = new MapDtl(this.getFK_MapDtl());

			//从request对象里复制数据,到entity.
			BP.Sys.PubClass.CopyFromRequest(dtl);

			dtl.Update();

			return "保存成功...";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 下载表单.
	*/
	public final void DownTempFrm()
	{
		String fileFullName = SystemConfig.PathOfWebApp + "\\Temp\\" + this.getFK_MapData() + ".xml";

		HttpContextHelper.ResponseWriteFile(fileFullName, this.getFK_MapData() + ".xml");
	}


	public final boolean getIsReusable()
	{
		return false;
	}


	public final boolean getIsNodeSheet()
	{
		if (this.getFK_MapData().startsWith("ND") == true)
		{
			return true;
		}
		return false;
	}
	/** 
	 字段属性编辑 初始化
	 
	 @return 
	*/
	public final String Attachment_Init()
	{
		FrmAttachment ath = new FrmAttachment();
		ath.FK_MapData = this.getFK_MapData();
		ath.NoOfObj = this.getAth();
		ath.FK_Node = this.getFK_Node();
		if (this.getMyPK() == null)
		{
			if (this.getFK_Node() == 0)
			{
				ath.MyPK = this.getFK_MapData() + "_" + this.getAth();
			}
			else
			{
				ath.MyPK = this.getFK_MapData() + "_" + this.getAth() + "_" + this.getFK_Node();
			}
		}
		else
		{
			ath.MyPK = this.getMyPK();

		}
		int i = ath.RetrieveFromDBSources();
		if (i == 0)
		{
			/*初始化默认值.*/
			ath.NoOfObj = "Ath1";
			ath.Name = "我的附件";
			ath.SaveTo = SystemConfig.PathOfDataUser + "\\UploadFile\\" + this.getFK_MapData() + "\\";
			ath.W = 150;
			ath.H = 40;
			ath.Exts = "*.*";
		}

		if (i == 0 && this.getFK_Node() != 0)
		{
			/*这里处理 独立表单解决方案, 如果有FK_Node 就说明该节点需要单独控制该附件的属性. */
			MapData mapData = new MapData();
			mapData.RetrieveByAttr(MapDataAttr.No, this.getFK_MapData());
			if (mapData.AppType.equals("0"))
			{
				FrmAttachment souceAthMent = new FrmAttachment();
				// 查询出来原来的数据.
				int rowCount = souceAthMent.Retrieve(FrmAttachmentAttr.FK_MapData, this.getFK_MapData(), FrmAttachmentAttr.NoOfObj, this.getAth(), FrmAttachmentAttr.FK_Node, "0");
				if (rowCount > 0)
				{
					ath.Copy(souceAthMent);
				}
			}
			if (this.getFK_Node() == 0)
			{
				ath.MyPK = this.getFK_MapData() + "_" + this.getAth();
			}
			else
			{
				ath.MyPK = this.getFK_MapData() + "_" + this.getAth() + "_" + this.getFK_Node();
			}

			//插入一个新的.
			ath.FK_Node = this.getFK_Node();
			ath.FK_MapData = this.getFK_MapData();
			ath.NoOfObj = this.getAth();
			//  ath.DirectInsert();
		}

		return ath.ToJson();
	}
	/** 
	 保存.
	 
	 @return 
	*/
	public final String Attachment_Save()
	{
		FrmAttachment ath = new FrmAttachment();
		ath.FK_MapData = this.getFK_MapData();
		ath.NoOfObj = this.getAth();
		ath.FK_Node = this.getFK_Node();
		ath.MyPK = this.getFK_MapData() + "_" + this.getAth();

		int i = ath.RetrieveFromDBSources();
		Object tempVar = BP.Sys.PubClass.CopyFromRequestByPost(ath);
		ath = tempVar instanceof FrmAttachment ? (FrmAttachment)tempVar : null;
		if (i == 0)
		{
			ath.Save(); //执行保存.
		}
		else
		{
			ath.Update();
		}
		return "保存成功..";
	}
	public final String Attachment_Delete()
	{
		FrmAttachment ath = new FrmAttachment();
		ath.MyPK = this.getMyPK();
		ath.Delete();
		return "删除成功.." + ath.MyPK;
	}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region sfGuide
	/** 
	 获取数据源字典表信息
	 
	 @return 
	*/
	public final String SFGuide_GetInfo()
	{
		String sfno = this.GetRequestVal("sfno"); //context.Request.QueryString["sfno"];

		if (tangible.StringHelper.isNullOrWhiteSpace(sfno))
		{
			return "err@参数不正确";
		}

		SFTable sftable = new SFTable(sfno);
		DataTable dt = sftable.ToDataTableField("info");

		for (DataColumn col : dt.Columns)
		{
			col.ColumnName = col.ColumnName.toUpperCase();
		}

		return BP.Tools.Json.ToJson(dt);
	}
	public final String SFGuide_SaveInfo()
	{
		boolean IsNew = this.GetRequestValBoolen("IsNew");
		String sfno = this.GetRequestVal("No");
		String myname = this.GetRequestVal("Name");

		int srctype = this.GetRequestValInt("SrcType");
		int codestruct = this.GetRequestValInt("CodeStruct");

		String defval = this.GetRequestVal("DefVal");
		String sfdbsrc = this.GetRequestVal("FK_SFDBSrc");
		String srctable = this.GetRequestVal("SrcTable");
		String columnvalue = this.GetRequestVal("ColumnValue");
		String columntext = this.GetRequestVal("ColumnText");

		String parentvalue = this.GetRequestVal("ParentValue");
		String tabledesc = this.GetRequestVal("TableDesc");
		String selectstatement = this.GetRequestVal("Selectstatement");

		//判断是否已经存在
		SFTable sftable = new SFTable();
		sftable.No = sfno;

		if (IsNew && sftable.RetrieveFromDBSources() > 0)
		{
			return "err@字典编号" + sfno + "已经存在，不允许重复。";
		}

		sftable.Name = myname;
		sftable.SrcType = (SrcType)srctype;
		sftable.CodeStruct = (CodeStruct)codestruct;
		sftable.DefVal = defval;
		sftable.FK_SFDBSrc = sfdbsrc;
		sftable.SrcTable = srctable;
		sftable.ColumnValue = columnvalue;
		sftable.ColumnText = columntext;
		sftable.ParentValue = parentvalue;
		sftable.TableDesc = tabledesc;
		sftable.SelectStatement = selectstatement;

		switch (sftable.SrcType)
		{
			case SrcType.BPClass:
				String[] nos = sftable.No.split("[.]", -1);
				sftable.FK_Val = "FK_" + tangible.StringHelper.trimEnd(nos[nos.length - 1], 's');
				sftable.FK_SFDBSrc = "local";
				break;
			default:
				sftable.FK_Val = "FK_" + sftable.No;
				break;
		}

		sftable.Save();
		return "保存成功！";
	}
	public final String SFGuide_Getmtds()
	{
		String src = this.GetRequestVal("src"); //context.Request.QueryString["src"];
		if (tangible.StringHelper.isNullOrWhiteSpace(src))
		{
			return "err@系统中没有webservices类型的数据源，该类型的外键表不能创建，请维护数据源.";
		}

		SFDBSrc sr = new SFDBSrc(src);

		if (sr.DBSrcType != DBSrcType.WebServices)
		{
			return "err@数据源“" + sr.Name + "”不是WebService数据源.";
		}

		ArrayList<WSMethod> mtds = GetWebServiceMethods(sr);

		return LitJson.JsonMapper.ToJson(mtds);
	}
	public final String SFGuide_GetCols()
	{
		String src = this.GetRequestVal("src"); //context.Request.QueryString["src"];
		String table = this.GetRequestVal("table"); //context.Request.QueryString["table"];

		if (tangible.StringHelper.isNullOrWhiteSpace(src))
		{
			throw new RuntimeException("err@参数不正确");
		}


		if (tangible.StringHelper.isNullOrWhiteSpace(table))
		{
			return "[]";
		}

		SFDBSrc sr = new SFDBSrc(src);
		DataTable dt = sr.GetColumns(table);

		for (DataColumn col : dt.Columns)
		{
			col.ColumnName = col.ColumnName.toUpperCase();
		}

		for (DataRow r : dt.Rows)
		{
			r.set("Name", r.get("No") + (r.get("Name") == null || r.get("Name") == DBNull.Value || tangible.StringHelper.isNullOrWhiteSpace(r.get("Name").toString()) ? "" : String.format("[%1$s]", r.get("Name"))));
		}

		return BP.Tools.Json.ToJson(dt);
	}
	/** 
	 //获取表/视图列表
	 
	 @return 
	*/
	public final String SFGuide_GetTVs()
	{
		String src = this.GetRequestVal("src"); // context.Request.QueryString["src"];

		SFDBSrc sr = new SFDBSrc(src);
		DataTable dt = sr.GetTables();

		for (DataColumn col : dt.Columns)
		{
			col.ColumnName = col.ColumnName.toUpperCase();
		}

		return BP.Tools.Json.ToJson(dt);
	}
	/** 
	 获得clsss列表.
	 
	 @return 
	*/
	public final String SFGuide_GetClass()
	{
		String sfno = this.GetRequestVal("sfno"); // context.Request.QueryString["sfno"];
		String stru = this.GetRequestVal("struct"); //context.Request.QueryString["struct"];
		int st = 0;

		tangible.OutObject<Integer> tempOut_st = new tangible.OutObject<Integer>();
		if (tangible.StringHelper.isNullOrWhiteSpace(stru) || !tangible.TryParseHelper.tryParseInt(stru, tempOut_st))
		{
		st = tempOut_st.argValue;
			throw new RuntimeException("err@参数不正确.");
		}
	else
	{
		st = tempOut_st.argValue;
	}

		String error = "";
		ArrayList arr = null;
		SFTables sfs = new SFTables();
		Entities ens = null;
		SFTable sf = null;
		sfs.Retrieve(SFTableAttr.SrcType, (int)SrcType.BPClass);

		switch (st)
		{
			case 0:
				arr = ClassFactory.GetObjects("BP.En.EntityNoName");
				break;
			case 1:
				arr = ClassFactory.GetObjects("BP.En.EntitySimpleTree");
				break;
			default:
				arr = new ArrayList();
				break;
		}

		StringBuilder s = new StringBuilder("[");
		for (BP.En.Entity en : arr)
		{
			try
			{
				if (en == null)
				{
					continue;
				}

				ens = en.GetNewEntities;
				if (ens == null)
				{
					continue;
				}

				Object tempVar = sfs.GetEntityByKey(ens.toString());
				sf = tempVar instanceof SFTable ? (SFTable)tempVar : null;

				if ((sf != null && !sfno.equals(sf.No)) || tangible.StringHelper.isNullOrWhiteSpace(ens.toString()))
				{
					continue;
				}

				s.append(String.format("{\"NO\":\"%1$s\",\"NAME\":\"%1$s[%2$s]\",\"DESC\":\"%2$s\"},", ens, en.EnDesc));
			}
			catch (java.lang.Exception e)
			{
				continue;
			}
		}
		return tangible.StringHelper.trimEnd(s.toString(), ',') + "]";
	}
	/** 
	 获取数据源列表
	 
	 @return 
	*/
	public final String SFGuide_GetSrcs()
	{

		String type = this.GetRequestVal("type");
		int itype;
		boolean onlyWS = false;

		SFDBSrcs srcs = new SFDBSrcs();
		tangible.OutObject<Integer> tempOut_itype = new tangible.OutObject<Integer>();
		if (!tangible.StringHelper.isNullOrWhiteSpace(type) && tangible.TryParseHelper.tryParseInt(type, tempOut_itype))
		{
		itype = tempOut_itype.argValue;
			onlyWS = true;
			srcs.Retrieve(SFDBSrcAttr.DBSrcType, itype);
		}
		else
		{
		itype = tempOut_itype.argValue;
			srcs.RetrieveAll();
		}

		DataTable dt = srcs.ToDataTableField();

		for (DataColumn col : dt.Columns)
		{
			col.ColumnName = col.ColumnName.toUpperCase();
		}

		if (onlyWS == false)
		{
			ArrayList<DataRow> wsRows = new ArrayList<DataRow>();
			for (DataRow r : dt.Rows)
			{
				if (Equals(r.get("DBSrcType"), (int)DBSrcType.WebServices))
				{
					wsRows.add(r);
				}
			}

			for (DataRow r : wsRows)
			{
				dt.Rows.Remove(r);
			}
		}
		return BP.Tools.Json.ToJson(dt);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Methods
	/** 
	 获取webservice方法列表
	 
	 @param dbsrc WebService数据源
	 @return 
	*/
	public final ArrayList<WSMethod> GetWebServiceMethods(SFDBSrc dbsrc)
	{
		return BP.WF.NetPlatformImpl.WF_Admin_FoolFormDesigner.GetWebServiceMethods(dbsrc);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region  ImpTableFieldSelectBindKey 外键枚举
	/** 
	 初始化数据
	 
	 @return 
	*/
	public final String ImpTableFieldSelectBindKey_Init()
	{
		DataSet ds = new DataSet();

		BP.Sys.SysEnumMains ens = new BP.Sys.SysEnumMains();
		ens.RetrieveAll();
		ds.Tables.Add(ens.ToDataTableField("EnumMain"));

		BP.Sys.SFTables tabs = new BP.Sys.SFTables();
		tabs.RetrieveAll();
		ds.Tables.Add(tabs.ToDataTableField("SFTables"));

		return BP.Tools.Json.ToJson(ds);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion  ImpTableFieldSelectBindKey 外键枚举


}