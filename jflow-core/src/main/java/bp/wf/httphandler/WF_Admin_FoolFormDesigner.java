package bp.wf.httphandler;

import bp.da.*;
import bp.difference.ContextHolderUtils;
import bp.difference.SystemConfig;
import bp.difference.handler.WebContralBase;
import bp.en.*;
import bp.wf.*;
import bp.sys.*;
import bp.sys.frmui.MapAttrDT;
import bp.sys.frmui.MapAttrString;
import bp.tools.StringHelper;
import bp.tools.StringUtils;
import bp.wf.template.*;
import java.util.*;

/** 
 处理页面的业务逻辑
*/
public class WF_Admin_FoolFormDesigner extends WebContralBase
{

		///表单设计器.
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
	 * @throws Exception 
	*/
	public final String Designer_Init() throws Exception
	{
		DataSet ds = new DataSet();
		//如果是第一次进入，就执行旧版本的升级检查.
		if (this.getIsFirst() == true)
		{

				/// 检查完整性.
			if (this.getFK_MapData().toUpperCase().contains("BP.") == true)
			{
				/*如果是类的实体.*/
				Entities ens = ClassFactory.GetEns(this.getFK_MapData());
				Entity en = ens.getGetNewEntity();

				MapData mymd = new MapData();
				mymd.setNo(this.getFK_MapData());
				mymd.ClearCash(); //清除缓存。

				int i = mymd.RetrieveFromDBSources();
				if (i == 0)
				{
					en.DTSMapToSys_MapData(this.getFK_MapData()); //调度数据到
				}

				mymd.RetrieveFromDBSources();
				mymd.setHisFrmType(FrmType.FoolForm);
				mymd.Update();
			}

				///

			MapFrmFool cols = new MapFrmFool(this.getFK_MapData());
			cols.DoCheckFixFrmForUpdateVer();
			return "url@Designer.htm?FK_MapData=" + this.getFK_MapData() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node();
		}
		//把表单属性放入里面去.
		MapData md = new MapData(this.getFK_MapData());
		//清缓存
		md.ClearCash();
		ds.Tables.add(md.ToDataTableField("Sys_MapData").copy());

		// 字段属性.
		MapAttrs attrs = new MapAttrs(this.getFK_MapData());
		for (MapAttr item : attrs.ToJavaList())
		{
			item.setDefVal(item.getDefValReal());
		}

		ds.Tables.add(attrs.ToDataTableField("Sys_MapAttr"));

		GroupFields gfs = new GroupFields(this.getFK_MapData());
		ds.Tables.add(gfs.ToDataTableField("Sys_GroupField"));

		MapDtls dtls = new MapDtls();
		dtls.Retrieve(MapDtlAttr.FK_MapData, this.getFK_MapData(), MapDtlAttr.FK_Node, 0);
		ds.Tables.add(dtls.ToDataTableField("Sys_MapDtl"));

		MapFrames frms = new MapFrames(this.getFK_MapData());
		ds.Tables.add(frms.ToDataTableField("Sys_MapFrame"));


		//附件表.
		FrmAttachments aths = new FrmAttachments(this.getFK_MapData());
		ds.Tables.add(aths.ToDataTableField("Sys_FrmAttachment"));


		//加入扩展属性.
		MapExts MapExts = new MapExts(this.getFK_MapData());
		ds.Tables.add(MapExts.ToDataTableField("Sys_MapExt"));

		// 检查组件的分组是否完整?
		for (GroupField item : gfs.ToJavaList())
		{
			boolean isHave = false;
			if (item.getCtrlType().equals("Dtl"))
			{
				for (MapDtl dtl : dtls.ToJavaList())
				{
					if (dtl.getNo().equals(item.getCtrlID()))
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
				ds.Tables.add(fnc.ToDataTableField("WF_Node").copy());
			}
		}


		//把dataet转化成json 对象.
		return bp.tools.Json.ToJson(ds);
	}

		///

	/** 
	 初始化
	 
	 @return 
	 * @throws Exception 
	*/
	public final String MapDefDtlFreeFrm_Init() throws Exception
	{
		String isFor = this.GetRequestVal("For");
		if (DataType.IsNullOrEmpty(isFor)==false)
		{
			return "sln@" + isFor;
		}

		if (this.getFK_MapDtl().contains("_Ath") == true)
		{
			return "info@附件扩展";
		}
		if (this.getFK_MapDtl().toUpperCase().contains("BP.WF.RETURNWORKS") == true)
		{
			return "info@退回字段扩展";
		}


		MapDtl dtl = new MapDtl();

		//如果传递来了节点信息, 就是说明了独立表单的节点方案处理, 现在就要做如下判断
		if (this.getFK_Node() != 0)
		{
			dtl.setNo(this.getFK_MapDtl() + "_" + this.getFK_Node());

			if (dtl.RetrieveFromDBSources() == 0)
			{
				// 开始复制它的属性.
				MapAttrs attrs = new MapAttrs(this.getFK_MapDtl());
				MapDtl odtl = new MapDtl();
				odtl.setNo(this.getFK_MapDtl());
				int i = odtl.RetrieveFromDBSources();
				if (i == 0)
				{
					return "info@字段列";
				}


				//存储表要与原明细表一致
				if (DataType.IsNullOrEmpty(odtl.getPTable()))
				{
					dtl.setPTable(odtl.getNo());
				}
				else
				{
					dtl.setPTable(odtl.getPTable());
				}

				//让其直接保存.
				dtl.setNo(this.getFK_MapDtl() + "_" + this.getFK_Node());
				dtl.setFK_MapData("Temp");
				dtl.DirectInsert(); //生成一个明细表属性的主表.

				//字段的分组也要一同复制
				HashMap<Integer, Integer> groupids = new HashMap<Integer, Integer>();

				//循环保存字段.
				int idx = 0;
				for (MapAttr item : attrs.ToJavaList())
				{
					if (item.getGroupID() != 0)
					{
						if (groupids.containsKey(item.getGroupID()))
						{
							item.setGroupID( groupids.get(item.getGroupID()));
						}
						else
						{
							GroupField gf = new GroupField();
							gf.setOID( item.getGroupID());

							if (gf.RetrieveFromDBSources() == 0)
							{
								gf.setLab("默认分组");
							}

							gf.setEnName(dtl.getNo());
							gf.InsertAsNew();

							if (groupids.containsKey(item.getGroupID()) == false)
							{
								groupids.put(item.getGroupID(), (int)gf.getOID());
							}

							item.setGroupID( gf.getOID());
						}
					}

					item.setFK_MapData(this.getFK_MapDtl() + "_" + this.getFK_Node());
					item.setMyPK(item.getFK_MapData() + "_" + item.getKeyOfEn());
					item.Save();
					idx++;
					item.setIdx( idx);
					item.DirectUpdate();
				}

				MapData md = new MapData();
				md.setNo("Temp");
				if (md.getIsExits() == false)
				{
					md.setName("为权限方案设置的临时的数据");
					md.Insert();
				}
			}

			return "sln@" + dtl.getNo();
		}

		dtl.setNo(this.getFK_MapDtl());
		if (dtl.RetrieveFromDBSources() == 0)
		{
			bp.sys.CCFormAPI.CreateOrSaveDtl(this.getFK_MapData(), this.getFK_MapDtl(), this.getFK_MapDtl(), 100, 200);
		}
		else
		{
			bp.sys.CCFormAPI.CreateOrSaveDtl(this.getFK_MapData(), this.getFK_MapDtl(), dtl.getName(), dtl.getX(), dtl.getY());
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
		return bp.sys.CCFormAPI.ParseStringToPinyinField(name,flag.equals("true")==true?true:false, true, 20);
	}

	public final String Designer_GFDoUp() throws Exception
	{
		String msg = "";
		GroupField gf = new GroupField(this.getRefOID());
		gf.DoUp();

		return msg;
	}
	public final String Designer_GFDoDown() throws Exception
	{
		String msg = "";
		GroupField mygf = new GroupField(this.getRefOID());
		mygf.DoDown();

		return msg;
	}
	/** 
	 增加一个枚举类型
	 
	 @return 
	 * @throws Exception 
	*/
	public final String SysEnumList_SaveEnumField() throws Exception
	{
		MapAttr attr = new MapAttr();
		attr.setMyPK(this.getFK_MapData() + "_" + this.getKeyOfEn());
		if (attr.RetrieveFromDBSources() != 0)
		{
			return "err@字段名[" + this.getKeyOfEn() + "]已经存在.";
		}

		attr.setFK_MapData(this.getFK_MapData());
		attr.setKeyOfEn( this.getKeyOfEn());
		attr.setUIBindKey( this.GetRequestVal("EnumKey"));

		attr.setGroupID(this.GetRequestValInt("GroupFeid"));
		int uiContralType = this.GetRequestValInt("UIContralType");

		if (uiContralType != 0)
		{
			attr.setUIContralType(UIContralType.forValue(uiContralType));
		}
		else
		{
			attr.setUIContralType( UIContralType.DDL);
		}
		if (attr.getUIContralType()== UIContralType.CheckBok)
		{
			attr.setMyDataType(DataType.AppString);
		}
		else
		{
			attr.setMyDataType(DataType.AppInt);
		}
		attr.setLGType( FieldTypeS.Enum);

		SysEnumMain sem = new SysEnumMain();
		sem.setNo(attr.getUIBindKey());
		if (sem.RetrieveFromDBSources() != 0)
		{
			attr.setName( sem.getName());
		}
		else
		{
			attr.setName("枚举" + attr.getUIBindKey());
		}

		//paras参数
		Paras ps = new Paras();
		ps.SQL="SELECT OID FROM Sys_GroupField A WHERE A.FrmID=" + SystemConfig.getAppCenterDBVarStr() + "FrmID AND ( CtrlType='' OR CtrlType IS NULL ) ORDER BY OID DESC ";
		ps.Add("FrmID", this.getFK_MapData());
		attr.setGroupID(DBAccess.RunSQLReturnValInt(ps, 0));
		attr.Insert();
		return attr.getMyPK();
	}

	public final String Designer_NewMapDtl() throws Exception
	{
		MapDtl en = new MapDtl();
		en.setFK_MapData(this.getFK_MapData());
		en.setNo(this.GetRequestVal("DtlNo"));

		if (en.RetrieveFromDBSources() == 1)
		{
			return "err@从表ID:" + en.getNo() + "已经存在.";
		}
		else
		{
			en.setFK_Node(this.getFK_Node());
			//en.setName("从表" + en.getNo();
			en.setName("从表");
			en.setPTable(en.getNo());
			en.setH(300);
			en.Insert();
			en.IntMapAttrs();
		}

		//返回字串.
		return en.getNo();
	}

	/** 
	 创建一个多附件
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Designer_AthNew() throws Exception
	{
		FrmAttachment ath = new FrmAttachment();
		ath.setFK_MapData(this.getFK_MapData());
		ath.setNoOfObj(this.GetRequestVal("AthNo"));
		ath.setMyPK(ath.getFK_MapData() + "_" + ath.getNoOfObj());
		if (ath.RetrieveFromDBSources() == 1)
		{
			return "err@附件ID:" + ath.getNoOfObj() + "已经存在.";
		}
		bp.sys.CCFormAPI.CreateOrSaveAthMulti(this.getFK_MapData(), this.GetRequestVal("AthNo"), "我的附件", 100, 200);
		return ath.getMyPK();
	}
	/** 
	 返回信息.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String GroupField_Init() throws Exception
	{
		GroupField gf = new GroupField();
		gf.setOID( this.GetRequestValInt("GroupField"));
		if (gf.getOID() != 0)
		{
			gf.Retrieve();
		}

		return gf.ToJson();
	}

	/** 
	 保存空白的分组.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String GroupField_SaveBlank() throws Exception
	{
		String no = this.GetValFromFrmByKey("TB_Blank_No");
		String name = this.GetValFromFrmByKey("TB_Blank_Name");

		GroupField gf = new GroupField();
		gf.setOID( this.GetRequestValInt("GroupField"));
		if (gf.getOID() != 0)
		{
			gf.Retrieve();
		}

		gf.setCtrlID(no);
		gf.setEnName(this.getFK_MapData());
		gf.setLab(name);
		gf.Save();
		return "保存成功.";
	}

	/** 
	 审核分组保存
	 
	 @return 
	 * @throws Exception 
	*/
	public final String GroupField_Save() throws Exception
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

		bp.sys.CCFormAPI.CreateCheckGroup(this.getFK_MapData(), lab, prx);

		return "保存成功";
	}
	/** 
	 创建审核分组
	 
	 @return 
	 * @throws Exception 
	*/
	public final String GroupField_Create() throws Exception
	{
		bp.sys.GroupField gf = new GroupField();
		gf.setFrmID(this.getFK_MapData());
		gf.setLab(this.GetRequestVal("Lab"));
		gf.setEnName(this.getFK_MapData());
		gf.Insert();
		return "创建成功..";
	}
	/** 
	 保存分组
	 
	 @return 
	 * @throws Exception 
	*/
	public final String GroupField_SaveCheck() throws Exception
	{
		String lab = this.GetRequestVal("TB_Check_Name");
		String prx = this.GetRequestVal("TB_Check_No");
		bp.sys.CCFormAPI.CreateCheckGroup(this.getFK_MapData(), lab, prx);
		return "创建成功...";
	}
	/** 
	 
	 删除分组
	 
	 @return 
	 * @throws Exception 
	*/
	public final String GroupField_DeleteCheck() throws Exception
	{
		GroupField gf = new GroupField();
		gf.setOID( this.GetRequestValInt("GroupField"));
		gf.Delete();

		bp.wf.template.MapFrmFool md = new bp.wf.template.MapFrmFool(this.getFK_MapData());
		md.DoCheckFixFrmForUpdateVer();

		return "删除成功...";
	}

	/** 
	 
	 删除并删除该分组下的字段
	 
	 @return 
	 * @throws Exception 
	*/
	public final String GroupField_DeleteAllCheck() throws Exception
	{
		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.GroupID, this.GetRequestValInt("GroupField"));
		for (MapAttr attr : attrs.ToJavaList())
		{
			if (attr.getHisEditType() != EditType.Edit)
			{
				continue;
			}
			if (attr.getKeyOfEn().equals("FID"))
			{
				continue;
			}

			attr.Delete();
		}

		GroupField gf = new GroupField();
		gf.setOID( this.GetRequestValInt("GroupField"));
		gf.Delete();

		return "删除并删除该分组下的字段成功...";
	}

	public final String ImpTableField_Step1() throws Exception
	{
		bp.sys.SFDBSrcs ens = new bp.sys.SFDBSrcs();
		ens.RetrieveAll();
		DataSet ds = new DataSet();
		ds.Tables.add(ens.ToDataTableField("SFDBSrcs"));
		return bp.tools.Json.ToJson(ds);
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
	}


	private String _STable = null;
	public final String getSTable() throws Exception
	{
		if (_STable == null)
		{
				//return this.GetRequestVal("FK_SFDBSrc");

			_STable = this.GetRequestVal("STable"); // context.Request.QueryString["STable"];
			if (_STable == null || "".equals(_STable))
			{
				Entity en = ClassFactory.GetEn(this.getFK_MapData());
				if (en != null)
				{
					_STable = en.getEnMap().getPhysicsTable();
				}
				else
				{
					MapData md = new MapData(this.getFK_MapData());
					_STable = md.getPTable();
				}
			}
		}

		if (_STable == null)
		{
			_STable = "";
		}
		return _STable;
	}

	public final String ImpTableField_Step2() throws Exception
	{

		DataSet ds = new DataSet();

		SFDBSrc src = new SFDBSrc(this.getFK_SFDBSrc());
		ds.Tables.add( src.ToDataTableField("SFDBSrc"));

		DataTable tables = src.GetTables();
		tables.TableName="tables";
		ds.Tables.add(tables);

		DataTable tableColumns = src.GetColumns(this.getSTable());
		tableColumns.TableName="columns";
		ds.Tables.add(tableColumns);

		MapAttrs attrs = new MapAttrs(this.getFK_MapData());
		ds.Tables.add( attrs.ToDataTableField("attrs"));

		DataTable dt = new DataTable();
		dt.TableName="STable";
		dt.Columns.Add("STable",String.class);
		DataRow dr = dt.NewRow();
		dr.setValue("STable",this.getSTable());
		dt.Rows.AddRow(dr);
		ds.Tables.add(dt);

		return bp.tools.Json.ToJson(ds);
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
			if (DataType.IsNullOrEmpty(s))
			{
				continue;
			}

			sCols.add(s);
		}

		return sCols;
	}

	public final String ImpTableField_Step3() throws Exception
	{
		DataSet ds = new DataSet();
		SFDBSrc src = new SFDBSrc(this.getFK_SFDBSrc());
		DataTable tableColumns = src.GetColumns(this.getSTable());
		DataTable dt = tableColumns.clone();
		dt.TableName = "selectedColumns";
		for (DataRow dr : tableColumns.Rows)
		{
			if (this.getSColumns().contains(dr.getValue("No")))
			{
				dt.Rows.add(dr);
			}
		}
		ds.Tables.add(dt);
		SysEnums ens = new SysEnums(MapAttrAttr.MyDataType);
		ds.Tables.add(ens.ToDataTableField("MyDataType"));
		SysEnums ens1 = new SysEnums(MapAttrAttr.LGType);
		ds.Tables.add(ens1.ToDataTableField("LGType"));
		return bp.tools.Json.ToJson(ds);
	}
	/** 
	 保存字段
	 
	 @return 
	 * @throws Exception 
	*/
	public final String ImpTableField_Save() throws Exception
	{
		MapData md = new MapData();
		md.setNo(this.getFK_MapData());
		md.RetrieveFromDBSources();

		String msg = md.getName() + "导入字段信息:" + this.getFK_MapData();
		boolean isLeft = true;
		float maxEnd = md.getMaxEnd();

		int iGroupID = 0;

		Paras ps = new Paras();
		ps.SQL="SELECT OID FROM Sys_GroupField WHERE FrmID=" + SystemConfig.getAppCenterDBVarStr() + "FrmID and (CtrlID is null or ctrlid ='') ORDER BY OID DESC ";
		ps.Add("FrmID", this.getFK_MapData());
		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		if (dt != null && dt.Rows.size() > 0)
		{
			iGroupID = Integer.parseInt(dt.Rows.get(0).getValue(0).toString());
		}


		Enumeration<String> e = ContextHolderUtils.getRequest().getParameterNames();
		while (e.hasMoreElements()) {
			String name = e.nextElement();
		
			if (name.startsWith("HID_Idx_") == false)
			{
				continue;
			}

			String columnName = name.substring("HID_Idx_".length());

			MapAttr ma = new MapAttr();
			ma.setKeyOfEn(columnName);
			ma.setFK_MapData(this.getFK_MapData());
			ma.setMyPK(this.getFK_MapData() + "_" + ma.getKeyOfEn());
			if (ma.getIsExits())
			{
				msg += "\t\n字段:" + ma.getKeyOfEn() + " - " + ma.getName() + "已存在.";
				continue;
			}

			ma.setName( this.GetValFromFrmByKey("TB_Desc_" + columnName));
			if (DataType.IsNullOrEmpty(ma.getName()))
			{
				ma.setName( ma.getKeyOfEn());
			}

			ma.setMyDataType(this.GetValIntFromFrmByKey("DDL_DBType_" + columnName));

			//翻译过去.
			String len = this.GetValFromFrmByKey("TB_Len_" + columnName);
			if (len.equals("null") || DataType.IsNullOrEmpty(len) == true)
			{
				len = "20";
			}

			try
			{
				int mylen = Integer.parseInt(len);
				if (mylen > 4000)
				{
					mylen = 0;
					//ma.isu = true;
				}


				ma.setMaxLen( mylen);
			}
			catch (RuntimeException ex)
			{
				ma.setMaxLen( 0);
				//throw new Exception("err@转化为最大长度的时候错误:" + ma.KeyOfEn + " len:" + len);
			}

			ma.setUIBindKey( this.GetValFromFrmByKey("TB_BindKey_" + columnName));
			ma.setLGType( FieldTypeS.Normal);
			ma.setGroupID(iGroupID);

			//绑定了外键或者枚举.
			if (DataType.IsNullOrEmpty(ma.getUIBindKey()) == false)
			{
				SysEnums se = new SysEnums();
				se.Retrieve(SysEnumAttr.EnumKey, ma.getUIBindKey());
				if (se.size() > 0)
				{
					ma.setMyDataType(bp.da.DataType.AppInt);
					ma.setLGType( FieldTypeS.Enum);
					ma.setUIContralType(UIContralType.DDL);
				}
				SFTable tb = new SFTable();
				tb.setNo(ma.getUIBindKey());
				if (tb.getIsExits() == true)
				{
					ma.setMyDataType(DataType.AppString);
					ma.setLGType(FieldTypeS.FK);
					ma.setUIContralType(UIContralType.DDL);
				}
			}

			if (ma.getMyDataType() == DataType.AppBoolean)
			{
				ma.setUIContralType(UIContralType.CheckBok);
			}

			ma.Insert();

			msg += "\t\n字段:" + ma.getKeyOfEn() + " - " + ma.getName() + "加入成功.";

			//生成lab.
			FrmLab lab = null;
			if (isLeft == true)
			{
				maxEnd = maxEnd + 40;
				/* 是否是左边 */
				lab = new FrmLab();
				lab.setMyPK(DBAccess.GenerGUID());
				lab.setFK_MapData(this.getFK_MapData());
				lab.setLab(ma.getName());
				lab.setX(40);
				lab.setY(maxEnd);
				lab.Insert();

				ma.setX(lab.getX() + 80);
				ma.setY(maxEnd);
				ma.Update();
			}
			else
			{
				lab = new FrmLab();
				lab.setMyPK(DBAccess.GenerGUID());
				lab.setFK_MapData(this.getFK_MapData());
				lab.setLab(ma.getName());
				lab.setX(350);
				lab.setY( maxEnd);
				lab.Insert();

				ma.setX(lab.getX() + 80);
				ma.setY(maxEnd);
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
	 * @throws Exception 
	*/
	public final String MapFrame_Init() throws Exception
	{
		MapFrame mf = new MapFrame();
		mf.setFK_MapData(this.getFK_MapData());

		if (this.getMyPK() == null)
		{
			mf.setURL("http://ccflow.org");
			mf.setW(400);
			mf.setH(300);
			mf.setName("我的框架.");
			mf.setFK_MapData(this.getFK_MapData());
			mf.setMyPK(DBAccess.GenerGUID());
		}
		else
		{
			mf.setMyPK(this.getMyPK());
			mf.RetrieveFromDBSources();
		}
		return mf.ToJson();
	}
	/** 
	 框架信息保存.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String MapFrame_Save() throws Exception
	{
		MapFrame mf = new MapFrame();
		Object tempVar = PubClass.CopyFromRequestByPost(mf);
		mf = tempVar instanceof MapFrame ? (MapFrame)tempVar : null;
		mf.setFK_MapData(this.getFK_MapData());

		mf.Save(); //执行保存.
		return "保存成功..";
	}


		///SFList 外键表列表.
	/** 
	 删除
	 
	 @return 
	 * @throws Exception 
	*/
	public final String SFList_Delete() throws Exception
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
	 * @throws Exception 
	*/
	public final String SFList_Init() throws Exception
	{
		DataSet ds = new DataSet();

		SFTables ens = new SFTables();
		ens.RetrieveAll();

		DataTable dt = ens.ToDataTableField("SFTables");
		ds.Tables.add(dt);

		int pTableModel = 0;
		if (this.GetRequestVal("PTableModel").equals("2"))
		{
			pTableModel = 2;
		}

		//获得ptableModel.
		if (pTableModel == 0)
		{
			MapDtl dtl = new MapDtl();
			dtl.setNo(this.getFK_MapData());
			if (dtl.RetrieveFromDBSources() == 1)
			{
				pTableModel = dtl.getPTableModel();
			}
			else
			{
				MapData md = new MapData();
				md.setNo(this.getFK_MapData());
				if (md.RetrieveFromDBSources() == 1)
				{
					pTableModel = md.getPTableModel();
				}
			}
		}

		if (pTableModel == 2)
		{
			DataTable mydt = MapData.GetFieldsOfPTableMode2(this.getFK_MapData());
			mydt.TableName = "Fields";
			ds.Tables.add(mydt);
		}

		return bp.tools.Json.ToJson(ds);
	}
	public final String SFList_SaveSFField() throws Exception
	{
		MapAttr attr = new MapAttr();
		attr.setMyPK(this.getFK_MapData() + "_" + this.getKeyOfEn());
		if (attr.RetrieveFromDBSources() != 0)
		{
			return "err@字段名[" + this.getKeyOfEn() + "]已经存在.";
		}

		bp.sys.CCFormAPI.SaveFieldSFTable(this.getFK_MapData(), this.getKeyOfEn(), null, this.GetRequestVal("SFTable"), 100, 100, 1);

		attr.Retrieve();
		Paras ps = new Paras();
		ps.SQL="SELECT OID FROM Sys_GroupField A WHERE A.FrmID=" + SystemConfig.getAppCenterDBVarStr() + "FrmID AND (CtrlType='' OR CtrlType IS NULL) ORDER BY OID DESC ";
		ps.Add("FrmID", this.getFK_MapData());
		attr.setGroupID(DBAccess.RunSQLReturnValInt(ps, 0));
		attr.Update();

		SFTable sf = new SFTable(attr.getUIBindKey());

		if (sf.getSrcType() == SrcType.TableOrView || sf.getSrcType() == SrcType.BPClass || sf.getSrcType() == SrcType.CreateTable)
		{
			return "../../Comm/En.htm?EnName=bp.sys.FrmUI.MapAttrSFTable&PKVal=" + attr.getMyPK();
		}
		else
		{
			return "../../Comm/En.htm?EnName=bp.sys.FrmUI.MapAttrSFSQL&PKVal=" + attr.getMyPK();
		}
	}

		/// 外键表列表.




	/** 
	 初始化表.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String EditTableField_Init() throws Exception
	{
		MapAttr attr = new MapAttr();
		attr.setKeyOfEn( this.getKeyOfEn());
		attr.setFK_MapData(this.getFK_MapData());

		if (DataType.IsNullOrEmpty(this.getMyPK()) == false)
		{
			attr.setMyPK(this.getMyPK());
			attr.RetrieveFromDBSources();
		}
		else
		{
			SFTable sf = new SFTable(this.getFK_SFTable());
			attr.setName( sf.getName());
			attr.setKeyOfEn( sf.getNo());
		}

		//第1次加载.
		attr.setUIContralType( UIContralType.DDL);

		attr.setFK_MapData(this.getFK_MapData());

		//字体大小.
		int size = attr.getPara_FontSize();
		if (size == 0)
		{
			attr.setPara_FontSize(12);
		}

		//横跨的列数.
		if (attr.getColSpan() == 0)
		{
			attr.setColSpan(1);
		}

		return attr.ToJson();
	}
	/** 
	 从表里选择字段.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String FieldTypeListChoseOneField_Init() throws Exception
	{
		String ptable = "";

		MapDtl dtl = new MapDtl();
		dtl.setNo(this.getFK_MapData());
		if (dtl.RetrieveFromDBSources() == 1)
		{
			ptable = dtl.getPTable();
		}
		else
		{
			MapData md = new MapData(this.getFK_MapData());
			ptable = md.getPTable();
		}

		//获得原始数据.
		DataTable dt = DBAccess.GetTableSchema(ptable);

		//创建样本.
		DataTable mydt = DBAccess.GetTableSchema(ptable);
		mydt.Rows.clear();

		//获得现有的列..
		MapAttrs attrs = new MapAttrs(this.getFK_MapData());

		String flowFiels = ",GUID,PRI,PrjNo,PrjName,PEmp,AtPara,FlowNote,WFSta,PNodeID,FK_FlowSort,FK_Flow,OID,FID,Title,WFState,CDT,FlowStarter,FlowStartRDT,FK_Dept,FK_NY,FlowDaySpan,FlowEmps,FlowEnder,FlowEnderRDT,FlowEndNode,MyNum,PWorkID,PFlowNo,BillNo,ProjNo,";

		//排除已经存在的列.
		for (DataRow dr : dt.Rows)
		{
			String key = dr.getValue("FName").toString();
			if (attrs.Contains(MapAttrAttr.KeyOfEn, key) == true)
			{
				continue;
			}

			if (flowFiels.contains("," + key + ",") == true)
			{
				continue;
			}

			DataRow mydr = mydt.NewRow();
			mydr.setValue("FName", dr.getValue("FName"));
			mydr.setValue("FType", dr.getValue("FType"));
			mydr.setValue("FLen", dr.getValue("FLen"));
			mydr.setValue("FDesc", dr.getValue("FDesc"));
			mydt.Rows.add(mydr);
		}

		mydt.TableName = "dt";
		return bp.tools.Json.ToJson(mydt);
	}
	public final String FieldTypeListChoseOneField_Save() throws Exception
	{
		int dataType = this.GetRequestValInt("DataType");
		String keyOfEn = this.GetRequestVal("KeyOfEn");
		String name = this.GetRequestVal("FDesc");
		String frmID = this.GetRequestVal("FK_MapData");

		MapAttr attr = new MapAttr();
		attr.setFK_MapData(frmID);
		attr.setKeyOfEn( keyOfEn);
		attr.setMyPK(attr.getFK_MapData() + "_" + keyOfEn);
		if (attr.getIsExits())
		{
			return "err@该字段[" + keyOfEn + "]已经加入里面了.";
		}

		attr.setName( name);
		attr.setMyDataType(dataType);

		if (bp.da.DataType.AppBoolean == dataType)
		{
			attr.setUIContralType( UIContralType.CheckBok);
		}
		else
		{
			attr.setUIContralType( UIContralType.TB);
		}

		Paras ps = new Paras();
		ps.SQL="SELECT OID FROM Sys_GroupField A WHERE A.FrmID=" + SystemConfig.getAppCenterDBVarStr() + "FrmID AND CtrlType='' OR CtrlType= NULL";
		ps.Add("FrmID", this.getFK_MapData());
		attr.setGroupID(DBAccess.RunSQLReturnValInt(ps, 0));

		attr.Insert();

		return "保存成功.";
	}
	/** 
	 字段选择.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String FieldTypeSelect_Create() throws Exception
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
		boolean isSupperText = this.GetRequestValBoolen("IsSupperText");

		MapAttrs attrs = new MapAttrs();
		int i = attrs.Retrieve(MapAttrAttr.FK_MapData, this.getFK_MapData(), MapAttrAttr.KeyOfEn, newNo);
		if (i != 0)
		{
			return "err@字段名：" + newNo + "已经存在.";
		}


			///计算GroupID  需要翻译
		int iGroupID = this.getGroupField();
		try
		{
			Paras ps = new Paras();
			ps.SQL="SELECT OID FROM Sys_GroupField WHERE FrmID=" + SystemConfig.getAppCenterDBVarStr() + "FrmID and (CtrlID is null or ctrlid ='') ORDER BY OID DESC ";
			ps.Add("FrmID", this.getFK_MapData());
			DataTable dt = DBAccess.RunSQLReturnTable(ps);
			if (dt != null && dt.Rows.size() > 0)
			{
				iGroupID = Integer.parseInt(dt.Rows.get(0).getValue(0).toString());
			}
		}
		catch (RuntimeException ex)
		{

		}

			///

		try
		{
			MapData md = new MapData();
			md.setNo(this.getFK_MapData());
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
		attr.setName( newName);
		attr.setKeyOfEn( newNo);
		attr.setFK_MapData(this.getFK_MapData());
		attr.setLGType( FieldTypeS.Normal);
		attr.setMyPK(this.getFK_MapData() + "_" + newNo);
		attr.setGroupID(iGroupID);
		attr.setMyDataType(fType);

		if (DataType.IsNullOrEmpty(this.GetRequestVal("FK_Flow")) == false)
		{
			attr.SetPara("FK_Flow", this.GetRequestVal("FK_Flow"));
		}

		int colspan = attr.getColSpan();
		attr.setPara_FontSize(12);
		int rows = attr.getUIRows();

		if (attr.getMyDataType() == DataType.AppString)
		{

			UIContralType uiContralType = UIContralType.forValue(this.GetRequestValInt("UIContralType"));
			attr.setUIWidth(100);
			attr.setUIHeight(23);
			if (uiContralType == UIContralType.SignCheck || uiContralType == UIContralType.FlowBBS)
			{
				attr.setUIIsEnable(false);
				attr.setUIVisible( true);
			}
			else
			{
				attr.setUIVisible( true);
				attr.setUIIsEnable(true);
			}

			attr.setColSpan(1);
			attr.setMinLen(0);
			attr.setMaxLen(50);
			attr.setMyDataType(DataType.AppString);
			attr.setUIContralType(uiContralType);
			attr.Insert();

			if (isSupperText == true)
			{
				MapAttrString attrString = new MapAttrString(attr.getMyPK());
				attrString.setIsSupperText(true);
				attrString.Update();
			}
			return "url@../../Comm/En.htm?EnName=bp.sys.FrmUI.MapAttrString&MyPK=" + attr.getMyPK();
		}

		if (attr.getMyDataType() == DataType.AppInt)
		{
			attr.setUIWidth(100);
			attr.setUIHeight(23);
			attr.setUIVisible(true);
			attr.setUIIsEnable(true);
			attr.setColSpan(1);
			attr.setMinLen(0);
			attr.setMaxLen(50);
			attr.setMyDataType(DataType.AppInt);
			attr.setUIContralType(UIContralType.TB);
			attr.setDefVal("0");
			attr.Insert();
			return "url@../../Comm/En.htm?EnName=bp.sys.FrmUI.MapAttrNum&MyPK=" + attr.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + newNo + "&FType=" + attr.getMyDataType() + "&DoType=Edit&GroupField=" + this.getGroupField();
		}

		if (attr.getMyDataType() == DataType.AppMoney)
		{
			attr.setUIWidth(100);
			attr.setUIHeight(23);
			attr.setUIVisible(true);
			attr.setUIIsEnable(true);
			attr.setColSpan(1);
			attr.setMinLen(0);
			attr.setMaxLen(50);
			attr.setMyDataType(DataType.AppMoney);
			attr.setUIContralType(UIContralType.TB);
			attr.setDefVal("0.00");
			attr.Insert();
			return "url@../../Comm/En.htm?EnName=bp.sys.FrmUI.MapAttrNum&MyPK=" + attr.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + newNo + "&FType=" + attr.getMyDataType() + "&DoType=Edit&GroupField=" + this.getGroupField();
		}

		if (attr.getMyDataType() == DataType.AppFloat)
		{
			attr.setUIWidth(100);
			attr.setUIHeight(23);
			attr.setUIVisible(true);
			attr.setUIIsEnable(true);
			attr.setColSpan(1);
			attr.setMinLen(0);
			attr.setMaxLen(50);
			attr.setMyDataType(DataType.AppFloat);
			attr.setUIContralType(UIContralType.TB);

			attr.setDefVal("0");
			attr.Insert();

			return "url@../../Comm/En.htm?EnName=bp.sys.FrmUI.MapAttrNum&MyPK=" + attr.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + newNo + "&FType=" + attr.getMyDataType() + "&DoType=Edit&GroupField=" + this.getGroupField();
		}

		if (attr.getMyDataType() == DataType.AppDouble)
		{
			attr.setUIWidth(100);
			attr.setUIHeight(23);
			attr.setUIVisible(true);
			attr.setUIIsEnable(true);
			attr.setColSpan(1);
			attr.setMinLen(0);
			attr.setMaxLen(50);
			attr.setMyDataType(DataType.AppDouble);
			attr.setUIContralType(UIContralType.TB);
			attr.setDefVal("0");
			attr.Insert();



			return "url@../../Comm/En.htm?EnName=bp.sys.FrmUI.MapAttrNum&MyPK=" + attr.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + newNo + "&FType=" + attr.getMyDataType() + "&DoType=Edit&GroupField=" + this.getGroupField();
		}

		if (attr.getMyDataType() == DataType.AppDate)
		{

			attr.setUIWidth(100);
			attr.setUIHeight(23);
			attr.setUIVisible(true);
			attr.setUIIsEnable(true);
			attr.setColSpan(1);
			attr.setMinLen(0);
			attr.setMaxLen(50);
			attr.setUIContralType (UIContralType.TB);
			attr.setMyDataType (DataType.AppDate);
			attr.Insert();

			MapAttrDT dt = new MapAttrDT();
			dt.setMyPK( attr.getMyPK());
			dt.RetrieveFromDBSources();
			dt.setFormat(0);
			dt.Update();


			return "url@../../Comm/En.htm?EnName=bp.sys.FrmUI.MapAttrDT&MyPK=" + attr.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + newNo + "&FType=" + attr.getMyDataType() + "&DoType=Edit&GroupField=" + this.getGroupField();
		}

		if (attr.getMyDataType() == DataType.AppDateTime)
		{
			attr.setUIWidth(100);
			attr.setUIHeight(23);
			attr.setUIVisible(true);
			attr.setUIIsEnable(true);
			attr.setColSpan(1);
			attr.setMinLen(0);
			attr.setMaxLen(50);
			attr.setUIContralType (UIContralType.TB);
			attr.setMyDataType (DataType.AppDateTime);
			attr.Insert();

			MapAttrDT dt = new MapAttrDT();
			dt.setMyPK(attr.getMyPK());
			dt.RetrieveFromDBSources();
			dt.setFormat(1);
			dt.Update();

			return "url@../../Comm/En.htm?EnName=bp.sys.FrmUI.MapAttrDT&MyPK=" + attr.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + newNo + "&FType=" + attr.getMyDataType() + "&DoType=Edit&GroupField=" + this.getGroupField();
		}

		if (attr.getMyDataType() == DataType.AppBoolean)
		{
			attr.setUIWidth(100);
			attr.setUIHeight(23);
			attr.setUIVisible(true);
			attr.setUIIsEnable(true);
			attr.setColSpan(1);
			attr.setMinLen(0);
			attr.setMaxLen(50);
			attr.setUIContralType(UIContralType.CheckBok);
			attr.setMyDataType(DataType.AppBoolean);
			attr.setDefVal("0");
			attr.Insert();

			return "url@../../Comm/En.htm?EnName=bp.sys.FrmUI.MapAttrBoolen&MyPK=" + attr.getMyPK() + "&FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + newNo + "&FType=" + attr.getMyDataType() + "&DoType=Edit&GroupField=" + this.getGroupField();
		}

		return "err@没有判断的数据类型." + attr.getMyDataTypeStr();
	}
	/** 
	 字段初始化数据.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String EditF_FieldInit() throws Exception
	{
		MapAttr attr = new MapAttr();
		attr.setKeyOfEn( this.getKeyOfEn());
		attr.setFK_MapData(this.getFK_MapData());

		if (DataType.IsNullOrEmpty(this.getMyPK()) == false)
		{
			attr.setMyPK(this.getMyPK());
			attr.RetrieveFromDBSources();
		}
		else
		{
			attr.setGroupID(this.getGroupField());
		}

		attr.setFK_MapData(this.getFK_MapData());

		//字体大小.
		int size=attr.getPara_FontSize();
		if (size == 0)
		{
			attr.setPara_FontSize(12);
		}


		//横跨的列数.
		if (attr.getColSpan() == 0)
		{
			attr.setColSpan(1);
		}

		return attr.ToJson();
	}
	public final String FieldInitEnum() throws Exception
	{
		MapAttr attr = new MapAttr();
		attr.setKeyOfEn( this.getKeyOfEn());
		attr.setFK_MapData(this.getFK_MapData());

		if (DataType.IsNullOrEmpty(this.getMyPK()) == false)
		{
			attr.setMyPK(this.getMyPK());
			attr.RetrieveFromDBSources();
		}
		else
		{
			SysEnumMain sem = new SysEnumMain(this.getEnumKey());
			attr.setName( sem.getName());
			attr.setKeyOfEn( sem.getNo());
			attr.setDefVal("0");
		}

		//第1次加载.
		if (attr.getUIContralType()== UIContralType.TB)
		{
			attr.setUIContralType( UIContralType.DDL);
		}

		attr.setFK_MapData(this.getFK_MapData());

		//字体大小.
		int size = attr.getPara_FontSize();
		if (size == 0)
		{
			attr.setPara_FontSize(12);
		}

		//横跨的列数.
		if (attr.getColSpan() == 0)
		{
			attr.setColSpan(1);
		}

		int model = attr.getRBShowModel();
		attr.setRBShowModel(model);

		return attr.ToJson();
	}
	/** 
	 转化成json
	 
	 @return 
	 * @throws Exception 
	*/
	public final String FieldInitGroupID() throws Exception
	{
		GroupFields gfs = new GroupFields(this.getFK_MapData());

		//转化成json输出.
		return gfs.ToJson();
	}
	/** 
	 分组&枚举： 两个数据源.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String FieldInitGroupAndSysEnum() throws Exception
	{
		GroupFields gfs = new GroupFields(this.getFK_MapData());

		//分组值.
		DataSet ds = new DataSet();
		ds.Tables.add(gfs.ToDataTableField("Sys_GroupField"));

		//枚举值.
		String enumKey = this.getEnumKey();
		if (DataType.IsNullOrEmpty(enumKey)==true || enumKey.equals("null"))
		{
			MapAttr ma = new MapAttr(this.getMyPK());
			enumKey = ma.getUIBindKey();
		}

		SysEnums enums = new SysEnums(enumKey);
		ds.Tables.add(enums.ToDataTableField("Sys_Enum"));

		//转化成json输出.
		String json = bp.tools.Json.ToJson(ds);
		// bp.da.DataType.WriteFile("c:\\FieldInitGroupAndSysEnum.json", json);
		return json;
	}

	/** 
	 执行删除.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String FieldDelete() throws Exception
	{
		try
		{
			MapAttr attr = new MapAttr();
			attr.setMyPK(this.getMyPK());
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
	 * @throws Exception 
	*/
	public final String FieldSaveEnum() throws Exception
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
			attr.setKeyOfEn( this.getKeyOfEn());
			attr.setFK_MapData(this.getFK_MapData());
			if (DataType.IsNullOrEmpty(this.getMyPK()) == false)
			{
				attr.setMyPK(this.getMyPK());
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

			attr.setKeyOfEn( this.getKeyOfEn());
			attr.setFK_MapData(this.getFK_MapData());
			attr.setLGType( FieldTypeS.Enum);
			attr.setUIBindKey( this.getEnumKey());
			attr.setMyDataType(DataType.AppInt);

			//控件类型.
			attr.setUIContralType( UIContralType.DDL);

			attr.setName( this.GetValFromFrmByKey("TB_Name"));
			attr.setKeyOfEn( this.GetValFromFrmByKey("TB_KeyOfEn"));
			attr.setColSpan(this.GetValIntFromFrmByKey("DDL_ColSpan"));
			if (attr.getColSpan() == 0)
			{
				attr.setColSpan(1);
			}

			//显示方式.
			attr.setRBShowModel(this.GetValIntFromFrmByKey("DDL_RBShowModel"));

			//控件类型.
			attr.setUIContralType( UIContralType.forValue(this.GetValIntFromFrmByKey("RB_CtrlType")));

			attr.setUIIsInput(this.GetValBoolenFromFrmByKey("CB_IsInput")); //是否是必填项.

			attr.setIsEnableJS(this.GetValBoolenFromFrmByKey("CB_IsEnableJS")); //是否启用js设置？

			attr.setPara_FontSize(this.GetValIntFromFrmByKey("TB_FontSize")); //字体大小.

			//默认值.
			attr.setDefVal(this.GetValFromFrmByKey("TB_DefVal"));

			try
			{
				//分组.
				if (this.GetValIntFromFrmByKey("DDL_GroupID") != 0)
				{
					attr.setGroupID(this.GetValIntFromFrmByKey("DDL_GroupID")); //在那个分组里？
				}
			}
			catch (java.lang.Exception e)
			{

			}

			//是否可用？所有类型的属性，都需要。
			int isEnable = this.GetValIntFromFrmByKey("RB_UIIsEnable");
			if (isEnable == 0)
			{
				attr.setUIIsEnable(false);
			}
			else
			{
				attr.setUIIsEnable(true);
			}

			//是否可见?
			int visable = this.GetValIntFromFrmByKey("RB_UIVisible");
			if (visable == 0)
			{
				attr.setUIVisible( false);
			}
			else
			{
				attr.setUIVisible( true);
			}

			attr.setMyPK(this.getFK_MapData() + "_" + this.getKeyOfEn());

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
	 * @throws Exception 
	*/
	public final String EditTableField_Save() throws Exception
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
			attr.setKeyOfEn( this.getKeyOfEn());
			attr.setFK_MapData(this.getFK_MapData());
			if (DataType.IsNullOrEmpty(this.getMyPK()) == false)
			{
				attr.setMyPK(this.getMyPK());
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

			attr.setKeyOfEn( this.getKeyOfEn());
			attr.setFK_MapData(this.getFK_MapData());
			attr.setLGType( FieldTypeS.FK);
			attr.setUIBindKey( this.getFK_SFTable());
			attr.setMyDataType(DataType.AppString);

			//控件类型.
			attr.setUIContralType( UIContralType.DDL);

			attr.setName( this.GetValFromFrmByKey("TB_Name"));
			attr.setKeyOfEn( this.GetValFromFrmByKey("TB_KeyOfEn"));
			attr.setColSpan(this.GetValIntFromFrmByKey("DDL_ColSpan"));
			if (attr.getColSpan() == 0)
			{
				attr.setColSpan(1);
			}

			attr.setUIIsInput(this.GetValBoolenFromFrmByKey("CB_IsInput")); //是否是必填项.

			attr.setPara_FontSize(this.GetValIntFromFrmByKey("TB_FontSize")); //字体大小.

			//默认值.
			attr.setDefVal(this.GetValFromFrmByKey("TB_DefVal"));

			try
			{
				//分组.
				if (this.GetValIntFromFrmByKey("DDL_GroupID") != 0)
				{
					attr.setGroupID(this.GetValIntFromFrmByKey("DDL_GroupID")); //在那个分组里？
				}
			}
			catch (java.lang.Exception e)
			{

			}

			//是否可用？所有类型的属性，都需要。
			int isEnable = this.GetValIntFromFrmByKey("RB_UIIsEnable");
			if (isEnable == 0)
			{
				attr.setUIIsEnable(false);
			}
			else
			{
				attr.setUIIsEnable(true);
			}

			//是否可见?
			int visable = this.GetValIntFromFrmByKey("RB_UIVisible");
			if (visable == 0)
			{
				attr.setUIVisible( false);
			}
			else
			{
				attr.setUIVisible( true);
			}

			attr.setMyPK(this.getFK_MapData() + "_" + this.getKeyOfEn());
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
	 * @throws Exception 
	*/
	public final String EditF_Save() throws Exception
	{
		try
		{
			//定义变量.
			int fType = Integer.parseInt(this.GetRequestVal("FType")); //字段数据物理类型
			FieldTypeS lgType = FieldTypeS.forValue(Integer.parseInt(this.GetRequestVal("LGType"))); //逻辑类型.
			String uiBindKey = this.GetRequestVal("UIBindKey"); // context.Request.QueryString["UIBindKey"];

			//赋值.
			MapAttr attr = new MapAttr();
			attr.setKeyOfEn( this.getKeyOfEn());
			attr.setFK_MapData(this.getFK_MapData());
			attr.setLGType( lgType); //逻辑类型.
			attr.setUIBindKey( uiBindKey); //绑定的枚举或者外键.
			attr.setMyDataType(fType); //物理类型.

			if (DataType.IsNullOrEmpty(this.getMyPK()) == false)
			{
				attr.setMyPK(this.getMyPK());
				attr.RetrieveFromDBSources();
			}

			attr.setFK_MapData(this.getFK_MapData());
			attr.setMyDataType(fType); //数据类型.
			attr.setName( this.GetValFromFrmByKey("TB_Name"));

			attr.setKeyOfEn( this.GetValFromFrmByKey("TB_KeyOfEn"));
			attr.setColSpan(this.GetValIntFromFrmByKey("DDL_ColSpan"));

			if (attr.getColSpan() == 0)
			{
				attr.setColSpan(1);
			}

			attr.setPara_FontSize(this.GetValIntFromFrmByKey("TB_FontSize")); //字体大小.
			attr.setPara_Tip(this.GetValFromFrmByKey("TB_Tip")); //操作提示.

			//默认值.
			attr.setDefVal(this.GetValFromFrmByKey("TB_DefVal"));


			//对于明细表就可能没有值.
			try
			{
				//分组.
				if (this.GetValIntFromFrmByKey("DDL_GroupID") != 0)
				{
					attr.setGroupID(this.GetValIntFromFrmByKey("DDL_GroupID")); //在那个分组里？
				}
			}
			catch (java.lang.Exception e)
			{

			}


			//把必填项拿出来，所有字段都可以设置成必填项 杨玉慧
			attr.setUIIsInput(this.GetValBoolenFromFrmByKey("CB_IsInput")); //是否是必填项.

			if (attr.getMyDataType() == bp.da.DataType.AppString && lgType == FieldTypeS.Normal)
			{
				attr.setIsRichText(this.GetValBoolenFromFrmByKey("CB_IsRichText")); //是否是富文本？
				attr.setIsSupperText(this.GetValIntFromFrmByKey("CB_IsSupperText")); //是否是超大文本？


				//高度.
				attr.setUIHeightInt(this.GetValIntFromFrmByKey("DDL_Rows") * 23);

				//最大最小长度.
				attr.setMaxLen( this.GetValIntFromFrmByKey("TB_MaxLen"));
				attr.setMinLen(this.GetValIntFromFrmByKey("TB_MinLen"));

				attr.setUIWidth( this.GetValIntFromFrmByKey("TB_UIWidth")); //宽度.
			}

			switch (attr.getMyDataType())
			{
				case DataType.AppInt:
				case DataType.AppFloat:
				case DataType.AppDouble:
				case DataType.AppMoney:
					attr.setIsSum(this.GetValBoolenFromFrmByKey("CB_IsSum"));
					break;
			}

			//获取宽度.
			try
			{
				attr.setUIWidth( this.GetValIntFromFrmByKey("TB_UIWidth")); //宽度.
			}
			catch (java.lang.Exception e2)
			{
			}


			//是否可用？所有类型的属性，都需要。
			int isEnable = this.GetValIntFromFrmByKey("RB_UIIsEnable");
			if (isEnable == 0)
			{
				attr.setUIIsEnable(false);
			}
			else
			{
				attr.setUIIsEnable(true);
			}

			//仅仅对普通类型的字段需要.
			if (lgType == FieldTypeS.Normal)
			{
				//是否可见?
				int visable = this.GetValIntFromFrmByKey("RB_UIVisible");
				if (visable == 0)
				{
					attr.setUIVisible( false);
				}
				else
				{
					attr.setUIVisible( true);
				}
			}

			attr.setMyPK(this.getFK_MapData() + "_" + this.getKeyOfEn());
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
	 * @throws Exception 
	*/
	public final String DtlInit() throws Exception
	{
		MapDtl dtl = new MapDtl();
		dtl.setNo(this.getFK_MapDtl());
		if (dtl.RetrieveFromDBSources() == 0)
		{
			dtl.setFK_MapData(this.getFK_MapData());
			dtl.setName( this.getFK_MapData());
			dtl.Insert();
			dtl.IntMapAttrs();
		}

		if (this.getFK_Node() != 0)
		{
			/* 如果传递来了节点信息, 就是说明了独立表单的节点方案处理, 现在就要做如下判断.
			 * 1, 如果已经有了.
			 */
			dtl.setNo(this.getFK_MapDtl() + "_" + this.getFK_Node());
			if (dtl.RetrieveFromDBSources() == 0)
			{

				// 开始复制它的属性.
				MapAttrs attrs = new MapAttrs(this.getFK_MapDtl());

				//让其直接保存.
				dtl.setNo(this.getFK_MapDtl() + "_" + this.getFK_Node());
				dtl.setFK_MapData("Temp");
				dtl.DirectInsert(); //生成一个明细表属性的主表.

				//循环保存字段.
				int idx = 0;
				for (MapAttr item : attrs.ToJavaList())
				{
					item.setFK_MapData(this.getFK_MapDtl() + "_" + this.getFK_Node());
					item.setMyPK(item.getFK_MapData() + "_" + item.getKeyOfEn());
					item.Save();
					idx++;
					item.setIdx( idx);
					item.DirectUpdate();
				}

				MapData md = new MapData();
				md.setNo("Temp");
				if (md.getIsExits() == false)
				{
					md.setName("为权限方案设置的临时的数据");
					md.Insert();
				}
			}
		}

		DataSet ds = new DataSet();
		DataTable dt = dtl.ToDataTableField("Main");
		ds.Tables.add(dt);

		//获得字段列表.
		MapAttrs attrsDtl = new MapAttrs(this.getFK_MapDtl());
		DataTable dtAttrs = attrsDtl.ToDataTableField("Ens");
		ds.Tables.add(dtAttrs);

		//返回json配置信息.
		return bp.tools.Json.ToJson(ds);
	}
	/** 
	 执行保存.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DtlSave() throws Exception
	{
		try
		{
			//复制.
			MapDtl dtl = new MapDtl(this.getFK_MapDtl());

			//从request对象里复制数据,到entity.
			PubClass.CopyFromRequest(dtl);

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
	 * @throws Exception 
	*/
	public final String Attachment_Init() throws Exception
	{
		FrmAttachment ath = new FrmAttachment();
		ath.setFK_MapData(this.getFK_MapData());
		ath.setNoOfObj( this.getAth());
		ath.setFK_Node(this.getFK_Node());
		if (this.getMyPK() == null)
		{
			if (this.getFK_Node() == 0)
			{
				ath.setMyPK(this.getFK_MapData() + "_" + this.getAth());
			}
			else
			{
				ath.setMyPK(this.getFK_MapData() + "_" + this.getAth() + "_" + this.getFK_Node());
			}
		}
		else
		{
			ath.setMyPK(this.getMyPK());

		}
		int i = ath.RetrieveFromDBSources();
		if (i == 0)
		{
			/*初始化默认值.*/
			ath.setNoOfObj("Ath1");
			ath.setName("我的附件");
		   // ath.SaveTo = SystemConfig.getPathOfDataUser() + "\\UploadFile\\" + this.FK_MapData + "\\";
			ath.setW(150);
			ath.setH(40);
			ath.setExts("*.*");
		}

		if (i == 0 && this.getFK_Node() != 0)
		{
			/*这里处理 独立表单解决方案, 如果有FK_Node 就说明该节点需要单独控制该附件的属性. */
			MapData mapData = new MapData();
			mapData.RetrieveByAttr(MapDataAttr.No, this.getFK_MapData());
			if (mapData.getAppType().equals("0"))
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
				ath.setMyPK(this.getFK_MapData() + "_" + this.getAth());
			}
			else
			{
				ath.setMyPK(this.getFK_MapData() + "_" + this.getAth() + "_" + this.getFK_Node());
			}

			//插入一个新的.
			ath.setFK_Node(this.getFK_Node());
			ath.setFK_MapData(this.getFK_MapData());
			ath.setNoOfObj(this.getAth());
			//  ath.DirectInsert();
		}

		return ath.ToJson();
	}
	/** 
	 保存.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Attachment_Save() throws Exception
	{
		FrmAttachment ath = new FrmAttachment();
		ath.setFK_MapData(this.getFK_MapData());
		ath.setNoOfObj(this.getAth());
		ath.setFK_Node(this.getFK_Node());
		ath.setMyPK(this.getFK_MapData() + "_" + this.getAth());

		int i = ath.RetrieveFromDBSources();
		Object tempVar = PubClass.CopyFromRequestByPost(ath);
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
	public final String Attachment_Delete() throws Exception
	{
		FrmAttachment ath = new FrmAttachment();
		ath.setMyPK(this.getMyPK());
		ath.Delete();
		return "删除成功.." + ath.getMyPK();
	}



		///sfGuide
	/** 
	 获取数据源字典表信息
	 
	 @return 
	 * @throws Exception 
	*/
	public final String SFGuide_GetInfo() throws Exception
	{
		String sfno = this.GetRequestVal("sfno"); //context.Request.QueryString["sfno"];

		if (DataType.IsNullOrEmpty(sfno))
		{
			return "err@参数不正确";
		}

		SFTable sftable = new SFTable(sfno);
		DataTable dt = sftable.ToDataTableField("info");

		for (DataColumn col : dt.Columns)
		{
			col.ColumnName = col.ColumnName.toUpperCase();
		}

		return bp.tools.Json.ToJson(dt);
	}
	public final String SFGuide_SaveInfo() throws Exception
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
		sftable.setNo(sfno);

		if (IsNew && sftable.RetrieveFromDBSources() > 0)
		{
			return "err@字典编号" + sfno + "已经存在，不允许重复。";
		}

		sftable.setName(myname);
		sftable.setSrcType(SrcType.forValue(srctype));
		sftable.setCodeStruct(CodeStruct.forValue(codestruct));
		sftable.setDefVal(defval);
		sftable.setFK_SFDBSrc(sfdbsrc);
		sftable.setSrcTable(srctable);
		sftable.setColumnValue(columnvalue);
		sftable.setColumnText(columntext);
		sftable.setParentValue(parentvalue);
		sftable.setTableDesc(tabledesc);
		sftable.setSelectStatement(selectstatement);

		switch (sftable.getSrcType())
		{
			case BPClass:
				String[] nos = sftable.getNo().split("[.]", -1);
				sftable.setFK_Val("FK_" + StringHelper.trimEnd(nos[nos.length - 1], 's'));
				sftable.setFK_SFDBSrc("local");
				break;
			default:
				sftable.setFK_Val("FK_" + sftable.getNo());
				break;
		}

		sftable.Save();
		return "保存成功！";
	}

	public final String SFGuide_GetCols() throws Exception
	{
		String src = this.GetRequestVal("src"); //context.Request.QueryString["src"];
		String table = this.GetRequestVal("table"); //context.Request.QueryString["table"];

		if (DataType.IsNullOrEmpty(src))
		{
			throw new RuntimeException("err@参数不正确");
		}


		if (DataType.IsNullOrEmpty(table))
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
			if(SystemConfig.AppCenterDBFieldCaseModel() == FieldCaseModel.UpperCase){
				r.setValue("NAME", r.getValue("NO") + (r.getValue("NAME") == null || "".equals(r.getValue("NAME")) || StringUtils.isEmpty(r.getValue("NAME").toString()) ? "" : String.format("[%1$s]", r.getValue("NAME"))));
			}else{
				r.setValue("Name", r.getValue("No") + (r.getValue("Name") == null || "".equals(r.getValue("Name")) || StringUtils.isEmpty(r.getValue("Name").toString()) ? "" : String.format("[%1$s]", r.getValue("Name"))));
			}
		}

		return bp.tools.Json.ToJson(dt);
	}
	/** 
	 //获取表/视图列表
	 
	 @return 
	 * @throws Exception 
	*/
	public final String SFGuide_GetTVs() throws Exception
	{
		String src = this.GetRequestVal("src"); // context.Request.QueryString["src"];

		SFDBSrc sr = new SFDBSrc(src);
		DataTable dt = sr.GetTables();

		for (DataColumn col : dt.Columns)
		{
			col.ColumnName = col.ColumnName.toUpperCase();
		}

		return bp.tools.Json.ToJson(dt);
	}
	
	/**
	 * 转换数值是否成功
	 * @param st
	 * @return
	 */
	public final boolean TryParse(String st){
		int a = 0;
		try {
			a = Integer.parseInt(st);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/** 
	 获得clsss列表.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String SFGuide_GetClass() throws Exception
	{
		String sfno = this.GetRequestVal("sfno"); // context.Request.QueryString["sfno"];
		String stru = this.GetRequestVal("struct"); //context.Request.QueryString["struct"];
		int st = 0;
		RefObject<Integer> tempRef_st = new RefObject<Integer>(st);
		boolean ntempVar = StringUtils.isEmpty(stru) || !TryParse(stru);
			st = tempRef_st.argvalue;
		if (ntempVar)
		{
			throw new RuntimeException("err@参数不正确.");
		}
	

		String error = "";
		ArrayList arr = null;
		SFTables sfs = new SFTables();
		Entities ens = null;
		SFTable sf = null;
		sfs.Retrieve(SFTableAttr.SrcType, SrcType.BPClass.getValue());

		switch (st)
		{
			case 0:
				arr = ClassFactory.GetObjects("bp.en.EntityNoName");
				break;
			case 1:
				arr = ClassFactory.GetObjects("bp.en.EntitySimpleTree");
				break;
			default:
				arr = new ArrayList();
				break;
		}

		StringBuilder s = new StringBuilder("[");
		for (Object en : arr)
		{
			try
			{
				if (en == null)
				{
					continue;
				}

				ens = ((Entity) en).getGetNewEntities();
				if (ens == null)
				{
					continue;
				}

				Object tempVar = sfs.GetEntityByKey(ens.toString());
				sf = tempVar instanceof SFTable ? (SFTable)tempVar : null;

				if ((sf != null && !sfno.equals(sf.getNo())) || DataType.IsNullOrEmpty(ens.toString()))
				{
					continue;
				}

				s.append(String.format("{\"NO\":\"%1$s\",\"NAME\":\"%1$s[%2$s]\",\"DESC\":\"%2$s\"},", ens, ((Entity) en).getEnDesc()));
			}
			catch (java.lang.Exception e)
			{
				continue;
			}
		}
		return StringHelper.trimEnd(s.toString(), ',') + "]";
	}
	/** 
	 获取数据源列表
	 
	 @return 
	 * @throws Exception 
	*/
	public final String SFGuide_GetSrcs() throws Exception
	{

		String type = ContextHolderUtils.getRequest().getParameter("type");
		int itype = 0;
		boolean onlyWS = false;

		SFDBSrcs srcs = new SFDBSrcs();
		RefObject<Integer> tempRef_itype = new RefObject<Integer>(itype);
		boolean tempVar = !StringUtils.isEmpty(type) && TryParse(type);
			itype = tempRef_itype.argvalue;
		if (tempVar)
		{
			onlyWS = true;
			srcs.Retrieve(SFDBSrcAttr.DBSrcType, itype);
		}
		else
		{
			srcs.RetrieveAll();
		}

		DataTable dt = srcs.ToDataTableField();

	
		if (onlyWS == false)
		{
			java.util.ArrayList<DataRow> wsRows = new java.util.ArrayList<DataRow>();
			for (DataRow r : dt.Rows)
			{
				if (r.getValue("DBSRCTYPE").equals(DBSrcType.WebServices.getValue()))
				{
					wsRows.add(r);
				}
			}

			for (DataRow r : wsRows)
			{
				dt.Rows.remove(r);
			}
		}
		return bp.tools.Json.ToJsonUpper(dt);
	}


	/** 
	
	 
	 @return 
	 * @throws Exception 
	*/
	public final String FrmView_Init() throws Exception
	{
		String frmID = this.GetRequestVal("FrmID");

		MapData md = new MapData(frmID);

		//获得表单模版.
		DataSet myds = bp.sys.CCFormAPI.GenerHisDataSet(md.getNo());



			///把主从表数据放入里面.
		//.工作数据放里面去, 放进去前执行一次装载前填充事件.
		GEEntity wk = new GEEntity(getFrmID());

		DataTable mainTable = wk.ToDataTableField("MainTable");
		mainTable.TableName = "MainTable";
		myds.Tables.add(mainTable);

			///



			///增加附件信息.
		bp.sys.FrmAttachments athDescs = new FrmAttachments();

		athDescs.Retrieve(FrmAttachmentAttr.FK_MapData, this.getFrmID());
		if (athDescs.size() != 0)
		{
			FrmAttachment athDesc = athDescs.get(0) instanceof FrmAttachment ? (FrmAttachment)athDescs.get(0) : null;

			//查询出来数据实体.
			bp.sys.FrmAttachmentDBs dbs = new bp.sys.FrmAttachmentDBs();
			if (athDesc.getHisCtrlWay() == AthCtrlWay.PWorkID)
			{
				Paras ps = new Paras();
				ps.SQL="SELECT PWorkID FROM WF_GenerWorkFlow WHERE WorkID=" + SystemConfig.getAppCenterDBVarStr() + "WorkID";
				ps.Add("WorkID", this.getWorkID());
				String pWorkID = String.valueOf(DBAccess.RunSQLReturnValInt(ps, 0));
				if (pWorkID == null || pWorkID.equals("0"))
				{
					pWorkID = String.valueOf(this.getWorkID());
				}

				if (athDesc.getAthUploadWay() == AthUploadWay.Inherit)
				{
					/* 继承模式 */
					QueryObject qo = new QueryObject(dbs);
					qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, pWorkID);
					qo.addOr();
					qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, String.valueOf(this.getWorkID()));
					qo.addOrderBy("RDT");
					qo.DoQuery();
				}

				if (athDesc.getAthUploadWay() == AthUploadWay.Interwork)
				{
					/*共享模式*/
					dbs.Retrieve(FrmAttachmentDBAttr.RefPKVal, pWorkID);
				}
			}
			else if (athDesc.getHisCtrlWay() == AthCtrlWay.WorkID)
			{
				/* 继承模式 */
				QueryObject qo = new QueryObject(dbs);
				qo.AddWhere(FrmAttachmentDBAttr.NoOfObj, athDesc.getNoOfObj());
				qo.addAnd();
				qo.AddWhere(FrmAttachmentDBAttr.RefPKVal, String.valueOf(this.getWorkID()));
				qo.addOrderBy("RDT");
				qo.DoQuery();
			}

			//增加一个数据源.
			myds.Tables.add(dbs.ToDataTableField("Sys_FrmAttachmentDB").copy());
		}

			///


			///把外键表加入DataSet
		DataTable dtMapAttr = myds.GetTableByName("Sys_MapAttr");
		DataTable dt = new DataTable();
		MapExts mes = md.getMapExts();
		MapExt me = new MapExt();
		DataTable ddlTable = new DataTable();
		ddlTable.Columns.Add("No");
		for (DataRow dr : dtMapAttr.Rows)
		{
			String lgType = dr.getValue("LGType").toString();
			String uiBindKey = dr.getValue("UIBindKey").toString();

			if (DataType.IsNullOrEmpty(uiBindKey) == true)
			{
				continue; //为空就continue.
			}

			if (lgType.equals("1") == true)
			{
				continue; //枚举值就continue;
			}

			String uiIsEnable = dr.getValue("UIIsEnable").toString();
			if (uiIsEnable.equals("0") == true && lgType.equals("2") == true)
			{
				continue; //如果是外键，并且是不可以编辑的状态.
			}

			if (uiIsEnable.equals("1") == true && lgType.equals("0") == true)
			{
				continue; //如果是外部数据源，并且是不可以编辑的状态.
			}



			// 检查是否有下拉框自动填充。
			String keyOfEn = dr.getValue("KeyOfEn").toString();
			String fk_mapData = dr.getValue("FK_MapData").toString();


				///处理下拉框数据范围. for 小杨.
			Object tempVar = mes.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.AttrOfOper, keyOfEn);
			me = tempVar instanceof MapExt ? (MapExt)tempVar : null;
			if (me != null)
			{
				Object tempVar2 = me.getDoc();
				String fullSQL = tempVar2 instanceof String ? (String)tempVar2 : null;
				fullSQL = fullSQL.replace("~", ",");
				fullSQL = bp.wf.Glo.DealExp(fullSQL, wk, null);
				dt = DBAccess.RunSQLReturnTable(fullSQL);
				//重构新表
				DataTable dt_FK_Dll = new DataTable();
				dt_FK_Dll.TableName = keyOfEn; //可能存在隐患，如果多个字段，绑定同一个表，就存在这样的问题.
				dt_FK_Dll.Columns.Add("No", String.class);
				dt_FK_Dll.Columns.Add("Name", String.class);
				for (DataRow dllRow : dt.Rows)
				{
					DataRow drDll = dt_FK_Dll.NewRow();
					drDll.setValue("No", dllRow.getValue("No"));
					drDll.setValue("Name", dllRow.getValue("Name"));
					dt_FK_Dll.Rows.add(drDll);
				}
				myds.Tables.add(dt_FK_Dll);
				continue;
			}

				/// 处理下拉框数据范围.

			// 判断是否存在.
			if (myds.GetTableByName(uiBindKey) !=null)
			{
				continue;
			}

			DataTable mydt = PubClass.GetDataTableByUIBineKey(uiBindKey);
			if (mydt == null)
			{
				DataRow ddldr = ddlTable.NewRow();
				ddldr.setValue("No", uiBindKey);
				ddlTable.Rows.add(ddldr);
			}
			else
			{
				myds.Tables.add(mydt);
			}
		}
		ddlTable.TableName = "UIBindKey";
		myds.Tables.add(ddlTable);

			/// End把外键表加入DataSet


			///图片附件
		FrmImgAthDBs imgAthDBs = new FrmImgAthDBs(this.getFrmID(), String.valueOf(this.getWorkID()));
		if (imgAthDBs != null && imgAthDBs.size() > 0)
		{
			DataTable dt_ImgAth = imgAthDBs.ToDataTableField("Sys_FrmImgAthDB");
			myds.Tables.add(dt_ImgAth);
		}

			///

		return bp.tools.Json.ToJson(myds);
	}

		///



		/// ImpTableFieldSelectBindKey 外键枚举
	/** 
	 初始化数据
	 
	 @return 
	 * @throws Exception 
	*/
	public final String ImpTableFieldSelectBindKey_Init() throws Exception
	{
		DataSet ds = new DataSet();

		bp.sys.SysEnumMains ens = new bp.sys.SysEnumMains();
		ens.RetrieveAll();
		ds.Tables.add(ens.ToDataTableField("EnumMain"));

		bp.sys.SFTables tabs = new bp.sys.SFTables();
		tabs.RetrieveAll();
		ds.Tables.add(tabs.ToDataTableField("SFTables"));

		return bp.tools.Json.ToJson(ds);
	}

	
}