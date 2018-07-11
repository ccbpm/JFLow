package BP.WF.HttpHandler;

import java.io.File;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.En.UIContralType;
import BP.Sys.FrmRB;
import BP.Sys.FrmRBAttr;
import BP.Sys.FrmRBs;
import BP.Sys.GroupFields;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapDtl;
import BP.Sys.MapDtlAttr;
import BP.Sys.MapDtls;
import BP.Sys.MapExt;
import BP.Sys.MapExtAttr;
import BP.Sys.MapExtXmlList;
import BP.Sys.PopValFormat;
import BP.Sys.PopValSelectModel;
import BP.Sys.PopValWorkModel;
import BP.Sys.SFDBSrcs;
import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import BP.Tools.StringHelper;
import BP.WF.HttpHandler.Base.WebContralBase;
import cn.jflow.common.util.ContextHolderUtils;

public class WF_Admin_FoolFormDesigner_MapExt extends WebContralBase {

	
	/**
	 * 构造函数
	 */
	public WF_Admin_FoolFormDesigner_MapExt()
	{
	
	}
	/// <summary>
    /// 保存
    /// </summary>
    /// <returns></returns>
	
	 public final String ActiveDDL_Save() throws Exception
		{
			MapExt me = new MapExt();
			me.Delete(MapExtAttr.ExtType, MapExtXmlList.ActiveDDL, MapExtAttr.FK_MapData, this.getFK_MapData(), MapExtAttr.AttrOfOper, this.getKeyOfEn());

			me.setFK_MapData(this.getFK_MapData());
			me.setAttrOfOper(this.getKeyOfEn());
			me.setAttrsOfActive(this.GetValFromFrmByKey("DDL_AttrsOfActive"));
			me.setFK_DBSrc(this.GetValFromFrmByKey("FK_DBSrc"));
			me.setDoc(this.GetValFromFrmByKey("TB_Doc")); //要执行的SQL.
			me.setExtType(MapExtXmlList.ActiveDDL);

			//执行保存.
			me.setMyPK(MapExtXmlList.ActiveDDL + "_" + me.getFK_MapData() + "_" + me.getAttrOfOper() + "_" + me.getAttrOfOper());
			me.Save();

			return "保存成功.";
		}
		public final String ActiveDDL_Delete()
		{
			MapExt me = new MapExt();
			me.Delete(MapExtAttr.ExtType, MapExtXmlList.ActiveDDL, MapExtAttr.FK_MapData, this.getFK_MapData(), MapExtAttr.AttrOfOper, this.getKeyOfEn());

			return "删除成功.";
		}
	
		
		
		
		
		
		/**
		 * ActiveDDL 初始化
		 * 
		 * @return
		 * @throws Exception 
		 */
	public String ActiveDDL_Init() throws Exception {
		DataSet ds = new DataSet();

		// 加载外键字段.
		String sql = "SELECT KeyOfEn AS No, Name FROM Sys_MapAttr WHERE UIContralType=1 AND FK_MapData='"
				+ this.getFK_MapData() + "' AND KeyOfEn!='" + this.getKeyOfEn() + "'";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapAttr";
		ds.Tables.add(dt);

		// 加载数据源.
		SFDBSrcs srcs = new SFDBSrcs();
		srcs.RetrieveAll();
		DataTable dtSrc = srcs.ToDataTableField("Main");
		dtSrc.TableName = "Sys_SFDBSrc";
		ds.Tables.add(dtSrc);

		// 加载mapext 数据.
		MapExt me = new MapExt();
		int i = me.Retrieve(MapExtAttr.ExtType, MapExtXmlList.ActiveDDL, MapExtAttr.FK_MapData, this.getFK_MapData(),
				MapExtAttr.AttrOfOper, this.getKeyOfEn());
		if (i == 0) {
			me.setFK_MapData(this.getFK_MapData());
			me.setAttrOfOper(this.getKeyOfEn());
			me.setFK_DBSrc("local");
		}

		if ("".equalsIgnoreCase(me.getDBSrc()))
			me.setFK_DBSrc("local");

		// 去掉 ' 号.
		me.SetValByKey("Doc", me.getDoc());

		dt = me.ToDataTableField("Main");
		dt.TableName = "Sys_MapExt";
		ds.Tables.add(dt);

		return BP.Tools.Json.ToJson(ds);
	}

	// region DDLFullCtrl 功能界面 .
	/**
	 * DDLFullCtrl 保存
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String DDLFullCtrl_Save() throws Exception {
		MapExt me = new MapExt();
		int i = me.Retrieve(MapExtAttr.ExtType, MapExtXmlList.DDLFullCtrl, MapExtAttr.FK_MapData, this.getFK_MapData(),
				MapExtAttr.AttrOfOper, this.getKeyOfEn());

		me.setFK_MapData(this.getFK_MapData());
		me.setAttrOfOper(this.getKeyOfEn());
		me.setFK_DBSrc(this.GetValFromFrmByKey("FK_DBSrc"));
		me.setDoc(this.GetValFromFrmByKey("TB_Doc")); // 要执行的SQL.

		me.setExtType(MapExtXmlList.DDLFullCtrl);

		// 执行保存.
		me.InitPK();
		if (me.Update() == 0)
			me.Insert();

		return "保存成功.";
	}

	/**
	 * DDLFullCtrl 删除
	 * 
	 * @return
	 */
	public String DDLFullCtrl_Delete() {
		MapExt me = new MapExt();
		me.Delete(MapExtAttr.ExtType, MapExtXmlList.DDLFullCtrl, MapExtAttr.FK_MapData, this.getFK_MapData(),
				MapExtAttr.AttrOfOper, this.getKeyOfEn());

		return "删除成功.";
	}

	/**
	 * DDLFullCtrl 初始化
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String DDLFullCtrl_Init() throws Exception {
		DataSet ds = new DataSet();

		// 加载数据源.
		SFDBSrcs srcs = new SFDBSrcs();
		srcs.RetrieveAll();
		DataTable dtSrc = srcs.ToDataTableField("Main");
		dtSrc.TableName = "Sys_SFDBSrc";
		ds.Tables.add(dtSrc);

		// 加载 mapext 数据.
		MapExt me = new MapExt();
		int i = me.Retrieve(MapExtAttr.ExtType, MapExtXmlList.DDLFullCtrl, MapExtAttr.FK_MapData, this.getFK_MapData(),
				MapExtAttr.AttrOfOper, this.getKeyOfEn());

		if (i == 0) {
			me.setFK_MapData(this.getFK_MapData());
			me.setAttrOfOper(this.getKeyOfEn());
			me.setFK_DBSrc("local");
		}

		if ("".equals(me.getFK_DBSrc()))
			me.setFK_DBSrc("local");

		// 去掉 ' 号.
		me.SetValByKey("Doc", me.getDoc());

		DataTable dt = me.ToDataTableField("Main");
		dt.TableName = "Sys_MapExt";
		ds.Tables.add(dt);

		return BP.Tools.Json.ToJson(ds);
	}
	// endregion DDLFullCtrl 功能界面.

	// region AutoFull 自动计算 a*b 功能界面 .
	/**
	 * 自动计算 保存(自动计算: @单价*@数量 模式.)
	 * 
	 * @return
	 * @throws Exception 
	 */	
	public String AutoFull_Save() throws Exception {
		MapExt me = new MapExt();
		int i = me.Retrieve(MapExtAttr.ExtType, MapExtXmlList.AutoFull, MapExtAttr.FK_MapData, this.getFK_MapData(),
				MapExtAttr.AttrOfOper, this.getKeyOfEn());

		me.setFK_MapData(this.getFK_MapData());
		me.setAttrOfOper(this.getKeyOfEn());
		me.setDoc(this.GetValFromFrmByKey("TB_Doc")); // 要执行的表达式.

		me.setExtType(MapExtXmlList.AutoFull);

		// 执行保存.
		me.setMyPK(MapExtXmlList.AutoFull + "_" + me.getFK_MapData() + "_" + me.getAttrOfOper());
		if (me.Update() == 0)
			me.Insert();

		return "保存成功.";
	}

	/**
	 * 自动计算 删除
	 * 
	 * @return
	 */
	public String AutoFull_Delete() {
		MapExt me = new MapExt();
		me.Delete(MapExtAttr.ExtType, MapExtXmlList.AutoFull, MapExtAttr.FK_MapData, this.getFK_MapData(),
				MapExtAttr.AttrOfOper, this.getKeyOfEn());

		return "删除成功.";
	}

	/**
	 * 自动计算 初始化
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String AutoFull_Init() throws Exception {
		DataSet ds = new DataSet();

		// 加载mapext 数据.
		MapExt me = new MapExt();
		int i = me.Retrieve(MapExtAttr.ExtType, MapExtXmlList.AutoFull, MapExtAttr.FK_MapData, this.getFK_MapData(),
				MapExtAttr.AttrOfOper, this.getKeyOfEn());
		if (i == 0) {
			me.setFK_MapData(this.getFK_MapData());
			me.setAttrOfOper(this.getKeyOfEn());
			me.setFK_DBSrc("local");
		}

		if ("".equals(me.getFK_DBSrc()))
			me.setFK_DBSrc("local");

		// 去掉 ' 号.
		me.SetValByKey("Doc", me.getDoc());

		DataTable dt = me.ToDataTableField("Main");
		dt.TableName = "Sys_MapExt";
		ds.Tables.add(dt);

		return BP.Tools.Json.ToJson(ds);
	}

	// endregion ActiveDDL 功能界面.
	/**
	 * 保存设置
	 * 
	 * @return
	 */
	public String PopVal_Save() {
		try {
			MapExt me = new MapExt();
			me.setMyPK(this.getFK_MapExt());
			me.setFK_MapData(this.getFK_MapData());
			me.setExtType("PopVal");
			me.setAttrOfOper(getRequest().getParameter("KeyOfEn"));
			me.RetrieveFromDBSources();

			String valWorkModel = this.GetValFromFrmByKey("Model");
			if ("None".equals(valWorkModel)) // 禁用模式.
			{
				me.setPopValWorkModelNew(PopValWorkModel.None);
			} else if ("SelfUrl".equals(valWorkModel))// URL模式.
			{
				me.setPopValWorkModelNew(PopValWorkModel.SelfUrl);
				me.setPopValUrl(this.GetValFromFrmByKey("TB_Url"));
			} else if ("TableOnly".equals(valWorkModel))// 表格模式.
			{
				me.setPopValWorkModelNew(PopValWorkModel.TableOnly);
				me.setPopValEntitySQL(this.GetValFromFrmByKey("TB_Table_SQL"));
			} else if ("TablePage".equals(valWorkModel)) // 分页模式.
			{
				me.setPopValWorkModelNew(PopValWorkModel.TablePage);
				me.setPopValTablePageSQL(this.GetValFromFrmByKey("TB_TablePage_SQL"));
				me.setPopValTablePageSQLCount(this.GetValFromFrmByKey("TB_TablePage_SQLCount"));
			} else if ("Group".equals(valWorkModel))// 分组模式.
			{
				me.setPopValWorkModelNew(PopValWorkModel.Group);
				me.setPopValGroupSQL(this.GetValFromFrmByKey("TB_GroupModel_Group"));
				me.setPopValEntitySQL(this.GetValFromFrmByKey("TB_GroupModel_Entity"));
			} else if ("Tree".equals(valWorkModel))// 单实体树.
			{
				me.setPopValWorkModelNew(PopValWorkModel.Tree);
				me.setPopValTreeSQL(this.GetValFromFrmByKey("TB_TreeSQL"));
				me.setPopValTreeParentNo(this.GetValFromFrmByKey("TB_TreeParentNo"));
			} else if ("TreeDouble".equals(valWorkModel))// 双实体树.
			{
				me.setPopValWorkModelNew(PopValWorkModel.TreeDouble);
				me.setPopValTreeSQL(this.GetValFromFrmByKey("TB_DoubleTreeSQL"));// 树SQL
				me.setPopValTreeParentNo(this.GetValFromFrmByKey("TB_DoubleTreeParentNo"));
				me.setPopValDoubleTreeEntitySQL(this.GetValFromFrmByKey("TB_DoubleTreeEntitySQL")); // 实体SQL
			}
			// 高级属性.
			me.setW(Integer.parseInt(this.GetValFromFrmByKey("TB_Width")));
			me.setH(Integer.parseInt(this.GetValFromFrmByKey("TB_Height")));
			me.setPopValColNames(this.GetValFromFrmByKey("TB_ColNames")); // 中文列名的对应.
			me.setPopValTitle(this.GetValFromFrmByKey("TB_Title")); // 标题.
			me.setPopValSearchTip(this.GetValFromFrmByKey("TB_PopValSearchTip")); // 关键字提示.
			me.setPopValSearchCond(this.GetValFromFrmByKey("TB_PopValSearchCond")); // 查询条件.
			// 数据返回格式.
			String popValFormat = this.GetValFromFrmByKey("PopValFormat");
			if ("OnlyNo".equals(popValFormat)) {
				me.setPopValFormatNew(PopValFormat.OnlyNo);
			} else if ("OnlyName".equals(popValFormat)) {
				me.setPopValFormatNew(PopValFormat.OnlyName);
			} else if ("NoName".equals(popValFormat)) {
				me.setPopValFormatNew(PopValFormat.NoName);
			}
			// 选择模式.
			String seleModel = this.GetValFromFrmByKey("PopValSelectModel");
			if ("One".equals(seleModel))
				me.setPopValSelectModelNew(PopValSelectModel.One);
			else
				me.setPopValSelectModelNew(PopValSelectModel.More);

			me.Save();
			return "保存成功.";
		} catch (Exception ex) {
			return "@保存失败:" + ex.getMessage();
		}
	}
	
	
	/** 
	 保存
	 
	 @return 
	 * @throws Exception 
	*/
	public final String PopFullCtrl_Save() throws Exception
	{
		try
		{
			MapExt me = new MapExt();
			int i = me.Retrieve(MapExtAttr.ExtType, MapExtXmlList.PopFullCtrl, MapExtAttr.FK_MapData, this.getFK_MapData(), MapExtAttr.AttrOfOper, this.getKeyOfEn());

			me.setFK_MapData(this.getFK_MapData());
			me.setAttrOfOper(this.getKeyOfEn());
			me.setFK_DBSrc(this.GetValFromFrmByKey("FK_DBSrc"));
			me.setDoc(this.GetValFromFrmByKey("TB_Doc")); //要执行的SQL.

			me.setExtType(MapExtXmlList.PopFullCtrl);

			//执行保存.
			me.InitPK();

			if (me.Update() == 0)
			{
				me.Insert();
			}

			return "保存成功.";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	public final String PopFullCtrl_Delete()
	{
		MapExt me = new MapExt();
		me.Delete(MapExtAttr.ExtType, MapExtXmlList.PopFullCtrl, MapExtAttr.FK_MapData, this.getFK_MapData(), MapExtAttr.AttrOfOper, this.getKeyOfEn());

		return "删除成功.";
	}
	public final String PopFullCtrl_Init() throws Exception
	{
		DataSet ds = new DataSet();

		//加载数据源.
		SFDBSrcs srcs = new SFDBSrcs();
		srcs.RetrieveAll();
		DataTable dtSrc = srcs.ToDataTableField();
		dtSrc.TableName = "Sys_SFDBSrc";
		ds.Tables.add(dtSrc);

		// 加载 mapext 数据.
		MapExt me = new MapExt();
		int i = me.Retrieve(MapExtAttr.ExtType, MapExtXmlList.PopFullCtrl, MapExtAttr.FK_MapData, this.getFK_MapData(), MapExtAttr.AttrOfOper, this.getKeyOfEn());

		if (i == 0)
		{
			me.setFK_MapData(this.getFK_MapData());
			me.setAttrOfOper(this.getKeyOfEn());
			me.setFK_DBSrc("local");
		}

		//这个属性没有用.
		me.setW(i); //用于标记该数据是否保存? 从而不现实填充从表，填充下拉框.按钮是否可以用.
		if (me.getFK_DBSrc().equals(""))
		{
			me.setFK_DBSrc("local");
		}

		//去掉 ' 号.
		me.SetValByKey("Doc", me.getDoc());

		DataTable dt = me.ToDataTableField("");
		dt.TableName = "Sys_MapExt";
		ds.Tables.add(dt);

		return BP.Tools.Json.ToJson(ds);
	}
	/** 
	 填充从表
	 
	 @return 
	 * @throws Exception 
	*/
	public final String PopFullCtrlDtl_Init() throws Exception
	{
		MapExt me = new MapExt(this.getMyPK());

		String[] strs = me.getTag1().split("[$]", -1);
		// 格式为: $ND101Dtl2:SQL.

		MapDtls dtls = new MapDtls();
		dtls.Retrieve(MapDtlAttr.FK_MapData, me.getFK_MapData());
		for (String str : strs)
		{
			if (StringHelper.isNullOrEmpty(str) || str.contains(":") == false)
			{
				continue;
			}

			String[] kvs = str.split("[:]", -1);
			String fk_mapdtl = kvs[0];
			String sql = kvs[1];

			for (MapDtl dtl : dtls.ToJavaList())
			{
				if (!fk_mapdtl.equals(dtl.getNo()))
				{
					continue;
				}
				dtl.setMTR(sql.trim());
			}
		}

		for (MapDtl dtl : dtls.ToJavaList()
				)
		{
			String cols = "";
			MapAttrs attrs = new MapAttrs(dtl.getNo());
			for (MapAttr item : attrs.ToJavaList())
			{
				if (item.getKeyOfEn().equals("OID") || item.getKeyOfEn().equals("RefPKVal") || item.getKeyOfEn().equals("RefPK"))
				{
					continue;
				}

				cols += item.getKeyOfEn() + ",";
			}
			dtl.setAlias(cols); //把ptable作为一个数据参数.
		}
		return dtls.ToJson();
	}

	public final String PopFullCtrlDtl_Save() throws Exception
	{
		MapDtls dtls = new MapDtls(this.getFK_MapData());
		MapExt me = new MapExt(this.getMyPK());

		String str = "";
		for (MapDtl dtl : dtls.ToJavaList())
		{
			String sql = this.GetRequestVal("TB_" + dtl.getNo());
			sql = sql.trim();
			if (sql.equals("") || sql == null)
			{
				continue;
			}

			if (sql.contains("@Key") == false)
			{
				return "err@在配置从表:" + dtl.getNo() + " sql填写错误, 必须包含@Key列, @Key就是当前文本框输入的值. ";
			}

			str += "$" + dtl.getNo() + ":" + sql;
		}
		me.setTag1(str);
		me.Update();

		return "保存成功.";
	}

	public final String PopFullCtrlDDL_Init() throws Exception
	{
		MapExt myme = new MapExt(this.getMyPK());
		MapAttrs attrs = new MapAttrs(myme.getFK_MapData());
		attrs.Retrieve(MapAttrAttr.FK_MapData, myme.getFK_MapData(), MapAttrAttr.UIIsEnable, 1, MapAttrAttr.UIContralType, UIContralType.DDL);

		String[] strs = myme.getTag().split("[$]", -1);
		for (MapAttr attr : attrs.ToJavaList())
		{
			for (String s : strs)
			{
				if (s == null)
				{
					continue;
				}
				if (s.contains(attr.getKeyOfEn() + ":") == false)
				{
					continue;
				}

				String[] ss = s.split("[:]", -1);
				attr.setDefVal(ss[1]); //使用这个字段作为对应设置的sql.
			}
		}

		return attrs.ToJson();
	}
	public final String PopFullCtrlDDL_Save() throws Exception
	{
		MapExt myme = new MapExt(this.getMyPK());

		MapAttrs attrs = new MapAttrs(myme.getFK_MapData());
		attrs.Retrieve(MapAttrAttr.FK_MapData, myme.getFK_MapData(), MapAttrAttr.UIIsEnable, 1, MapAttrAttr.UIContralType, UIContralType.DDL);

		MapExt me = new MapExt(this.getMyPK());

		String str = "";
		for (MapAttr attr : attrs.ToJavaList())
		{

			String sql = this.GetRequestVal("TB_" + attr.getKeyOfEn());
			sql = sql.trim();
			if (sql.equals("") || sql == null)
			{
				continue;
			}

			if (sql.contains("@Key") == false)
			{
				return "err@在配置从表:" + attr.getKeyOfEn() + " sql填写错误, 必须包含@Key列, @Key就是当前文本框输入的值. ";
			}

			str += "$" + attr.getKeyOfEn() + ":" + sql;
		}
		me.setTag(str);
		me.Update();

		return "保存成功.";
	}
	

	/**
	 * 初始化正则表达式界面
	 * 
	 * @return
	 */
	public String RegularExpression_Init() {
		DataSet ds = new DataSet();
		String sql = "SELECT * FROM Sys_MapExt WHERE AttrOfOper='" + this.getKeyOfEn() + "' AND FK_MapData='"
				+ this.getFK_MapData() + "'";
		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Sys_MapExt";
		ds.Tables.add(dt);

		BP.Sys.XML.RegularExpressions res = new BP.Sys.XML.RegularExpressions();
		res.RetrieveAll();

		DataTable myDT = res.ToDataTable();
		myDT.TableName = "RE";
		ds.Tables.add(myDT);

		BP.Sys.XML.RegularExpressionDtls dtls = new BP.Sys.XML.RegularExpressionDtls();
		dtls.RetrieveAll();
		DataTable myDTDtls = dtls.ToDataTable();
		myDTDtls.TableName = "REDtl";
		ds.Tables.add(myDTDtls);

		return BP.Tools.Json.ToJson(ds);
	}

	/**
	 * 执行 保存.
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String RegularExpression_Save() throws Exception {
		// 删除该字段的全部扩展设置.
		MapExt me = new MapExt();
		me.Delete(MapExtAttr.FK_MapData, this.getFK_MapData(), MapExtAttr.ExtType, MapExtXmlList.RegularExpression,
				MapExtAttr.AttrOfOper, this.getKeyOfEn());

		// 执行存盘.
		RegularExpression_Save_Tag("onblur");
		RegularExpression_Save_Tag("onchange");
		RegularExpression_Save_Tag("onclick");
		RegularExpression_Save_Tag("ondblclick");
		RegularExpression_Save_Tag("onkeypress");
		RegularExpression_Save_Tag("onkeyup");
		RegularExpression_Save_Tag("onsubmit");
		return "保存成功...";
	}

	private void RegularExpression_Save_Tag(String tagID) throws Exception {
		String val = this.GetValFromFrmByKey("TB_Doc_" + tagID);
		if (StringHelper.isNullOrEmpty(val))
			return;

		MapExt me = new MapExt();
		me.setMyPK(MapExtXmlList.TBFullCtrl + "_" + this.getFK_MapData() + "_" + this.getKeyOfEn() + "_" + tagID);
		me.setFK_MapData(this.getFK_MapData());
		me.setAttrOfOper(this.getKeyOfEn());
		me.setExtType("RegularExpression");
		me.setTag(tagID);
		me.setDoc(val);
		me.setTag1(this.GetValFromFrmByKey("TB_Tag1_" + tagID));
		me.Save();
	}

	/**
	 * 返回
	 * @throws Exception 
	 */
	public final String PopVal_Init() throws Exception {
		MapExt ext = new MapExt();
		ext.setMyPK(this.getFK_MapExt());
		if (ext.RetrieveFromDBSources() == 0) {
			ext.setFK_DBSrc("local");
			ext.setPopValSelectModelNew(PopValSelectModel.One);
			ext.setPopValWorkModelNew(PopValWorkModel.TableOnly);
		}
		return ext.PopValToJson();
	}

	public final String AutoFullDLL_Init() throws Exception {
		DataSet ds = new DataSet();

		// 加载数据源.
		SFDBSrcs srcs = new SFDBSrcs();
		srcs.RetrieveAll();
		DataTable dtSrc = srcs.ToDataTableField();
		dtSrc.TableName = "Sys_SFDBSrc";
		ds.Tables.add(dtSrc);

		// 加载 mapext 数据.
		MapExt me = new MapExt();
		int i = me.Retrieve(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.FK_MapData, this.getFK_MapData(),
				MapExtAttr.AttrOfOper, this.getKeyOfEn());

		if (i == 0) {
			me.setFK_MapData(this.getFK_MapData());
			me.setAttrOfOper(this.getKeyOfEn());
			me.setFK_DBSrc("local");
		}

		if (me.getFK_DBSrc().equals("")) {
			me.setFK_DBSrc("local");
		}

		// 去掉 ' 号.
		me.SetValByKey("Doc", me.getDoc());

		DataTable dt = me.ToDataTableField("Sys_MapExt");
		dt.TableName = "Sys_MapExt";
		ds.Tables.add(dt);

		return BP.Tools.Json.ToJson(ds);
	}

	// 2017-10-24添加

	// 执行删除。
	public final String TBFullCtrl_Delete() {
		MapExt me = new MapExt();
		me.Delete(MapExtAttr.ExtType, MapExtXmlList.TBFullCtrl, MapExtAttr.FK_MapData, this.getFK_MapData(),
				MapExtAttr.AttrOfOper, this.getKeyOfEn());

		return "删除成功.";
	}

	// 初始化。
	public final String TBFullCtrl_Init() throws Exception {
		DataSet ds = new DataSet();

		// 加载数据源.
		SFDBSrcs srcs = new SFDBSrcs();
		srcs.RetrieveAll();
		DataTable dtSrc = srcs.ToDataTableField("Sys_SFDBSrc");
		ds.Tables.add(dtSrc);

		// 加载 mapext 数据.
		MapExt me = new MapExt();
		int i = me.Retrieve(MapExtAttr.ExtType, MapExtXmlList.TBFullCtrl, MapExtAttr.FK_MapData, this.getFK_MapData(),
				MapExtAttr.AttrOfOper, this.getKeyOfEn());

		if (i == 0) {
			me.setFK_MapData(this.getFK_MapData());
			me.setAttrOfOper(this.getKeyOfEn());
			me.setFK_DBSrc("local");
		}

		// 这个属性没有用.
		me.setW(i); // 用于标记该数据是否保存? 从而不现实填充从表，填充下拉框.按钮是否可以用.
		if (me.getFK_DBSrc().equals("")) {
			me.setFK_DBSrc("local");
		}

		// 去掉 ' 号.
		me.SetValByKey("Doc", me.getDoc());

		DataTable dt = me.ToDataTableField("Sys_MapExt");
		ds.Tables.add(dt);

		return BP.Tools.Json.ToJson(ds);
	}

	// 保存
	public String TBFullCtrl_Save() {
		try {
			MapExt me = new MapExt();
			int i = me.Retrieve(MapExtAttr.ExtType, MapExtXmlList.TBFullCtrl, MapExtAttr.FK_MapData,
					this.getFK_MapData(), MapExtAttr.AttrOfOper, this.getKeyOfEn());

			me.setFK_MapData(this.getFK_MapData());
			me.setAttrOfOper(this.getKeyOfEn());
			me.setFK_DBSrc(this.GetValFromFrmByKey("FK_DBSrc"));
			me.setDoc(this.GetValFromFrmByKey("TB_Doc")); // 要执行的SQL.

			me.setExtType(MapExtXmlList.TBFullCtrl);

			// 执行保存.
			me.InitPK();

			if (me.Update() == 0)
				me.Insert();

			return "保存成功.";
		} catch (Exception ex) {
			return "err@" + ex.getMessage();
		}
	}

	// 开始
	public final String TBFullCtrlDDL_Init() throws Exception {
		MapExt myme = new MapExt(this.getMyPK());
		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.FK_MapData, myme.getFK_MapData(), MapAttrAttr.UIIsEnable, 1,
				MapAttrAttr.UIContralType, 1);

		String[] strs = myme.getTag().split("[$]", -1);
		for (MapAttr attr : attrs.ToJavaList()) {
			for (String s : strs) {
				if (s == null)

					continue;

				if (s.contains(attr.getKeyOfEn() + ":") == false)
					continue;

				String[] ss = s.split("[:]", -1);
				attr.setDefVal(ss[1]); // 使用这个字段作为对应设置的sql.
			}
		}

		return attrs.ToJson();
	}

	public String TBFullCtrlDDL_Save() throws Exception {
		MapExt myme = new MapExt(this.getMyPK());

		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.FK_MapData, myme.getFK_MapData(), MapAttrAttr.UIIsEnable, 1,
				MapAttrAttr.UIContralType, 1);

		MapExt me = new MapExt(this.getMyPK());

		String str = "";
		for (MapAttr attr : attrs.ToJavaList()) {

			String sql = this.GetRequestVal("TB_" + attr.getKeyOfEn());
			sql = sql.trim();
			if (sql == null || "".equals(sql))
				continue;

			if (sql.contains("@Key") == false)
				return "err@在配置从表:" + attr.getKeyOfEn() + " sql填写错误, 必须包含@Key列, @Key就是当前文本框输入的值. ";

			str += "$" + attr.getKeyOfEn() + ":" + sql;
		}
		me.setTag(str);
		me.Update();

		return "保存成功.";
	}

	public final String TBFullCtrlDtl_Init() throws Exception {
		MapExt me = new MapExt(this.getMyPK());

		String[] strs = me.getTag1().split("[$]", -1);
		// 格式为: $ND101Dtl2:SQL.

		MapDtls dtls = new MapDtls();
		dtls.Retrieve(MapDtlAttr.FK_MapData, me.getFK_MapData());

		for (String str : strs) {
			if (str == null || str.contains(":") == false) {
				continue;
			}

			String[] kvs = str.split("[:]", -1);
			String fk_mapdtl = kvs[0];
			String sql = kvs[1];

			for (MapDtl dtl : dtls.ToJavaList()) {
				if (!fk_mapdtl.equals(dtl.getNo())) {
					continue;
				}
				dtl.setMTR(sql.trim());
			}
		}

		for (MapDtl dtl : dtls.ToJavaList())
		{	String cols = "";
				MapAttrs attrs = new MapAttrs(dtl.getNo());
				for (MapAttr item : attrs.ToJavaList()) {
					if (item.getKeyOfEn().equals("OID") || item.getKeyOfEn().equals("RefPKVal")) {
						continue;
					}

					cols += item.getKeyOfEn() + ",";
				}

				dtl.setAlias(cols); // 把ptable作为一个数据参数.
			}
		
		return dtls.ToJson();
	}

	public final class DotNetToJavaStringHelper {

		public strictfp boolean isNullOrEmpty(String string) {
			return string == null || string.equals("");
		}

		public String join(String separator, String[] stringarray) {
			if (stringarray == null)
				return null;
			else
				return join(separator, stringarray, 0, stringarray.length);
		}

		public String join(String separator, String[] stringarray, int startindex, int count) {
			String result = "";

			if (stringarray == null)
				return null;

			for (int index = startindex; index < stringarray.length && index - startindex < count; index++) {
				if (separator != null && index > startindex)
					result += separator;

				if (stringarray[index] != null)
					result += stringarray[index];
			}

			return result;
		}

		public String trimEnd(String string, Character... charsToTrim) {
			if (string == null || charsToTrim == null)
				return string;

			int lengthToKeep = string.length();
			for (int index = string.length() - 1; index >= 0; index--) {
				boolean removeChar = false;
				if (charsToTrim.length == 0) {
					if (Character.isWhitespace(string.charAt(index))) {
						lengthToKeep = index;
						removeChar = true;
					}
				} else {
					for (int trimCharIndex = 0; trimCharIndex < charsToTrim.length; trimCharIndex++) {
						if (string.charAt(index) == charsToTrim[trimCharIndex]) {
							lengthToKeep = index;
							removeChar = true;
							break;
						}
					}
				}
				if (!removeChar)
					break;
			}
			return string.substring(0, lengthToKeep);
		}

		public String trimStart(String string, Character... charsToTrim) {
			if (string == null || charsToTrim == null)
				return string;

			int startingIndex = 0;
			for (int index = 0; index < string.length(); index++) {
				boolean removeChar = false;
				if (charsToTrim.length == 0) {
					if (Character.isWhitespace(string.charAt(index))) {
						startingIndex = index + 1;
						removeChar = true;
					}
				} else {
					for (int trimCharIndex = 0; trimCharIndex < charsToTrim.length; trimCharIndex++) {
						if (string.charAt(index) == charsToTrim[trimCharIndex]) {
							startingIndex = index + 1;
							removeChar = true;
							break;
						}
					}
				}
				if (!removeChar)
					break;
			}
			return string.substring(startingIndex);
		}

		public String trim(String string, Character... charsToTrim) {
			return trimEnd(trimStart(string, charsToTrim), charsToTrim);
		}

		public boolean stringsEqual(String s1, String s2) {
			if (s1 == null && s2 == null)
				return true;
			else
				return s1 != null && s1.equals(s2);
		}

	}

	public final String TBFullCtrlDtl_Save() throws Exception {
		MapDtls dtls = new MapDtls(this.getFK_MapData());
		MapExt me = new MapExt(this.getMyPK());

		String str = "";
		for (MapDtl dtl : dtls.ToJavaList()) {
			String sql = this.GetRequestVal("TB_" + dtl.getNo());
			sql = sql.trim();
			if (sql.equals("") || sql == null) {
				continue;
			}

			if (sql.contains("@Key") == false) {
				return "err@在配置从表:" + dtl.getNo() + " sql填写错误, 必须包含@Key列, @Key就是当前文本框输入的值. ";
			}

			str += "$" + dtl.getNo() + ":" + sql;
		}
		me.setTag1(str);
		me.Update();

		return "保存成功.";
	}
	
			/** 
			 保存
			 *region 超链接保存. @于庆海需要翻译.
			 @return 
			 * @throws Exception 
			*/
			public final String Link_Save() throws Exception
			{
				try
				{
					MapExt me = new MapExt();
					int i = me.Retrieve(MapExtAttr.ExtType, MapExtXmlList.Link, MapExtAttr.FK_MapData, this.getFK_MapData(), MapExtAttr.AttrOfOper, this.getKeyOfEn());

					me.setFK_MapData(this.getFK_MapData());
					me.setAttrOfOper(this.getKeyOfEn());

					//url.
					me.setDoc(this.GetValFromFrmByKey("TB_Url")); //要执行的SQL.

					//宽度，高度.
					me.setW(this.GetValIntFromFrmByKey("TB_Width")); //宽度.
					me.setH(this.GetValIntFromFrmByKey("TB_Height")); //高度.

					me.setExtType(MapExtXmlList.Link);

					//执行保存.
					me.InitPK();

					if (me.Update() == 0)
					{
						me.Insert();
					}

					return "保存成功.";
				}
				catch (RuntimeException ex)
				{
					return "err@" + ex.getMessage();
				}
			}
			
			
			 public final String AutoFullDtlField_Init() throws Exception
				{
					DataSet ds = new DataSet();
					// 加载mapext 数据.
					MapExt me = new MapExt();
					int i = me.Retrieve(MapExtAttr.ExtType, MapExtXmlList.AutoFullDtlField, MapExtAttr.FK_MapData, this.getFK_MapData(), MapExtAttr.AttrOfOper, this.getKeyOfEn());
					if (i == 0)
					{
						me.setFK_MapData(this.getFK_MapData());
						me.setAttrOfOper(this.getKeyOfEn());
						me.setFK_DBSrc("local");
					}

					if (me.getFK_DBSrc().equals(""))
					{
						me.setFK_DBSrc("local");
					}

					//去掉 ' 号.
					me.SetValByKey("Doc", me.getDoc());

					DataTable dt = me.ToDataTableField(null);
					dt.TableName = "Sys_MapExt";
					ds.Tables.add(dt);

					//把从表放入里面.
					MapDtls dtls = new MapDtls(this.getFK_MapData());
					ds.Tables.add(dtls.ToDataTableField("Dtls"));

					//把从表的字段放入.
					for (MapDtl dtl : dtls.ToJavaList())
					{
						String sql = "SELECT KeyOfEn as No, Name FROM Sys_MapAttr WHERE FK_MapData='"+dtl.getNo()+"' AND (MyDataType=2 OR MyDataType=3 OR MyDataType=5 OR MyDataType=8)  ";
						sql+=" AND KeyOfEn !='OID' AND KeyOfEn!='FID' AND KeyOfEn!='RefPK' ";

						//把从表增加里面去.
						DataTable mydt = DBAccess.RunSQLReturnTable(sql);
						mydt.TableName = dtl.getNo();
						ds.Tables.add(mydt);
					}

					return BP.Tools.Json.ToJson(ds);
				}
			  /** 
			 保存(自动计算: @单价*@数量 模式.)
			 
			 @return 
			 * @throws Exception 
	  */
			public final String AutoFullDtlField_Save() throws Exception
			{
				MapExt me = new MapExt();
				int i = me.Retrieve(MapExtAttr.ExtType, MapExtXmlList.AutoFullDtlField, MapExtAttr.FK_MapData, this.getFK_MapData(), MapExtAttr.AttrOfOper, this.getKeyOfEn());

				me.setFK_MapData(this.getFK_MapData());
				me.setAttrOfOper(this.getKeyOfEn());
				me.setDoc(this.GetValFromFrmByKey("DDL_Dtl") + "." + this.GetValFromFrmByKey("DDL_Field") + "." + this.GetValFromFrmByKey("DDL_JSFS")); //要执行的表达式.

				me.setExtType(MapExtXmlList.AutoFullDtlField);

				//执行保存.
				me.setMyPK(MapExtXmlList.AutoFullDtlField + "_" + me.getFK_MapData() + "_" + me.getAttrOfOper());
				if (me.Update() == 0)
				{
					me.Insert();
				}

				return "保存成功.";
			}
			  public final String AutoFullDtlField_Delete()
				{
					MapExt me = new MapExt();
					me.Delete(MapExtAttr.ExtType, MapExtXmlList.AutoFullDtlField, MapExtAttr.FK_MapData, this.getFK_MapData(), MapExtAttr.AttrOfOper, this.getKeyOfEn());

					return "删除成功.";
				}

	// #region AutoFullDLL 功能界面 .
	// <summary>
	// 保存
	// </summary>
	// <returns></returns>
	public String AutoFullDLL_Save() throws Exception {
		MapExt me = new MapExt();
		me.Retrieve(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.FK_MapData, this.getFK_MapData(), MapExtAttr.AttrOfOper, this.getKeyOfEn());
		me.setFK_MapData(this.getFK_MapData());
		me.setAttrOfOper(this.getKeyOfEn());
		me.setFK_DBSrc(this.GetValFromFrmByKey("FK_DBSrc"));
		me.setDoc(this.GetValFromFrmByKey("TB_Doc")); // 要执行的SQL.
		me.setExtType(MapExtXmlList.AutoFullDLL);
		// 执行保存.
		me.InitPK();
		if (me.Update() == 0)
			me.Insert();
		return "保存成功.";
	}

	public String AutoFullDLL_Delete() {
		MapExt me = new MapExt();
		me.Delete(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLL, MapExtAttr.FK_MapData, this.getFK_MapData(), MapExtAttr.AttrOfOper, this.getKeyOfEn());
		return "删除成功.";
	}
	
	   //#region 单选按钮事件
       /// <summary>
       /// 返回信息。
       /// </summary>
       /// <returns></returns>
       public String RadioBtns_Init() throws Exception
       {
           DataSet ds = new DataSet();

           //放入表单字段.
           MapAttrs attrs = new MapAttrs(this.getFK_MapData());
           ds.Tables.add(attrs.ToDataTableField("Sys_MapAttr"));

           //属性.
           MapAttr attr = new MapAttr();
           attr.setMyPK(this.getFK_MapData() + "_" + this.getKeyOfEn());
           attr.Retrieve();

           //把分组加入里面.
           GroupFields gfs = new GroupFields(this.getFK_MapData());
           ds.Tables.add(gfs.ToDataTableField("Sys_GroupFields"));

           //字段值.
           FrmRBs rbs = new FrmRBs();
           rbs.Retrieve(FrmRBAttr.FK_MapData, this.getFK_MapData(), FrmRBAttr.KeyOfEn, this.getKeyOfEn());
           if (rbs.size() == 0)
           {
               /*初始枚举值变化.
                */
               SysEnums ses = new SysEnums(attr.getUIBindKey());
               for (SysEnum se: ses.ToJavaList())
               {
                   FrmRB rb = new FrmRB();
                   rb.setFK_MapData(this.getFK_MapData());
                   rb.setKeyOfEn(this.getKeyOfEn());
                   rb.setIntKey(se.getIntKey());
                   rb.setLab(se.getLab());
                   rb.setEnumKey(attr.getUIBindKey());
                   rb.Insert(); //插入数据.
               }

               rbs.Retrieve(FrmRBAttr.FK_MapData, this.getFK_MapData(), FrmRBAttr.KeyOfEn, this.getKeyOfEn());
           }

           //加入单选按钮.
           ds.Tables.add(rbs.ToDataTableField("Sys_FrmRB"));
           return BP.Tools.Json.ToJson(ds);
       }
       /// <summary>
       /// 执行保存
       /// </summary>
       /// <returns></returns>
       public String RadioBtns_Save() throws Exception
       {
           String json = this.getRequest().getParameter("data"); 
           DataTable dt = BP.Tools.Json.ToDataTable(json);

           for (DataRow dr : dt.Rows)
           {
               FrmRB rb = new FrmRB();
               rb.setMyPK( dr.getValue("MyPK").toString());
               rb.Retrieve();

               rb.setScript(dr.getValue("Script").toString());
               rb.setFieldsCfg(dr.getValue("FieldsCfg").toString()); //格式为 @字段名1=1@字段名2=0
               rb.setTip(dr.getValue("Tip").toString()); //提示信息

               rb.setSetVal(dr.getValue("SetVal").toString()); //设置值.

               rb.Update();
           }

           return "保存成功.";
       }
       ///#endregion
	

       //杨玉慧  表单设计--表单属性   JS编程 
       /**
        * JS编程 初始化
        * @return content
        */
       public String InitScript_Init()
       {
           try {
	        	   String webPath = ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath("/");
	        	   webPath = webPath.replace("\\", "/");
	               String filePath = webPath + "/DataUser/JSLibData/" + this.getFK_MapData() + "_Self.js";
	               String content = "";	
	               File file = new File(filePath);
	               if(!file.exists())
	               {
	            	   content = "";
	               }else{
	            	   content =DataType.ReadTextFile(filePath);
	               }
	               
	               return content;
	           }
	           catch (Exception ex)
	           {
	               return "err@" + ex.getMessage();
	           }
       }

       /**
        * 保存
        * @return
        */
       public String InitScript_Save()
       {
           try
           {
        	   String webPath = ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath("/");
        	   webPath = webPath.replace("\\", "/");
               String filePath = webPath + "/DataUser/JSLibData/" + this.getFK_MapData() + "_Self.js";
               String content = ContextHolderUtils.getRequest().getParameter("JSDoc");

               //在应用程序当前目录下的File1.txt文件中追加文件内容，如果文件不存在就创建，默认编码
               //File.WriteAllText(filePath, content);
               DataType.WriteFile(filePath, content);

               return "保存成功";
           }
           catch (Exception ex)
           {
               return "err@" + ex.getMessage();
           }

       }

       /**
        * 删除
        * @return
        */
       public String InitScript_Delete()
       {
           try {
        	   String webPath = ContextHolderUtils.getRequest().getSession().getServletContext().getRealPath("/");
        	   webPath = webPath.replace("\\", "/");
               String filePath = webPath + "/DataUser/JSLibData/" + this.getFK_MapData() + "_Self.js";
               
               File file = new File(filePath);
               if (file.exists())
               {
                   file.delete();
               }

               return "删除成功";
           }
           catch (Exception ex)
           {
               return "err@" + ex.getMessage();
           }
       }

}
