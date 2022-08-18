package bp.sys.frmui;

import bp.da.*;
import bp.en.*;
import bp.sys.*;
/** 
 实体属性
*/
public class MapAttrCheck extends EntityMyPK
{

		///#region 文本字段参数属性.


	/** 
	 表单ID
	*/
	public final String getFKMapData()
	{
		return this.GetValStringByKey(MapAttrAttr.FK_MapData);
	}
	public final void setFKMapData(String value)
	 {
		this.SetValByKey(MapAttrAttr.FK_MapData, value);
	}
	public final String getFrmID()
	{
		return this.GetValStringByKey(MapAttrAttr.FK_MapData);
	}

	/** 
	 字段
	*/
	public final String getKeyOfEn()
	{
		return this.GetValStringByKey(MapAttrAttr.KeyOfEn);
	}
	public final void setKeyOfEn(String value)
	 {
		this.SetValByKey(MapAttrAttr.KeyOfEn, value);
	}

		///#endregion


		///#region 构造方法
	/** 
	 控制权限
	*/
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();
		uac.IsInsert = false;
		uac.IsUpdate = true;
		uac.IsDelete = true;
		return uac;
	}
	/** 
	 实体属性
	*/
	public MapAttrCheck() {
	}
	/** 
	 实体属性
	*/
	public MapAttrCheck(String mypk)throws Exception
	{
		this.setMyPK(mypk);
		this.Retrieve();

	}
	/** 
	 EnMap
	*/
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_MapAttr", "签批字段");


			///#region 基本字段信息.
		map.AddTBStringPK(MapAttrAttr.MyPK, null, "主键", false, false, 0, 200, 20);
		map.AddTBString(MapAttrAttr.FK_MapData, null, "表单ID", false, false, 1, 100, 20);
		map.AddTBString(MapAttrAttr.Name, null, "字段中文名", true, false, 0, 200, 20, true);
		map.AddTBString(MapAttrAttr.KeyOfEn, null, "字段名", true, true, 1, 200, 20, true);

		map.AddTBInt(MapAttrAttr.MinLen, 0, "最小长度", true, false);
		map.AddTBInt(MapAttrAttr.MaxLen, 50, "最大长度", true, false);
		map.SetHelperAlert(MapAttrAttr.MaxLen, "定义该字段的字节长度.");


		map.AddTBFloat(MapAttrAttr.UIWidth, 100, "宽度", true, false);
		map.SetHelperAlert(MapAttrAttr.UIWidth, "对自由表单,从表有效,显示文本框的宽度.");

			//map.AddTBInt(MapAttrAttr.UIContralType, 0, "控件", false, false);

			/**map.AddBoolean(MapAttrAttr.UIVisible, true, "是否可见？", true, true);
			map.SetHelperAlert(MapAttrAttr.UIVisible, "对于不可见的字段可以在隐藏功能的栏目里找到这些字段进行编辑或者删除.");

			map.AddBoolean(MapAttrAttr.UIIsEnable, true, "是否可编辑？", true, true);
			map.SetHelperAlert(MapAttrAttr.UIIsEnable, "不可编辑,让该字段设置为只读.");

			map.AddBoolean(MapAttrAttr.UIIsInput, false, "是否必填项？", true, true);
			map.AddBoolean(MapAttrAttr.IsRichText, false, "是否富文本？", true, true);
			map.SetHelperAlert(MapAttrAttr.IsRichText, "以html编辑器呈现或者编写字段.");
			map.AddBoolean(MapAttrAttr.IsSecret, false, "是否保密？", true, true);

			map.AddBoolean(MapAttrAttr.IsSupperText, false, "是否大块文本？(是否该字段存放的超长字节字段)", true, true, true);
			map.SetHelperAlert(MapAttrAttr.IsSupperText, "大块文本存储字节比较长，超过4000个字符.");

			map.AddTBString(MapAttrAttr.Tip, null, "激活提示", true, false, 0, 400, 20, true);
			map.SetHelperAlert(MapAttrAttr.Tip, "在文本框输入的时候显示在文本框背景的提示文字,也就是文本框的 placeholder 的值.");
			//CCS样式
			*/
		map.AddDDLSQL(MapAttrAttr.CSSCtrl, "0", "自定义样式", MapAttrString.getSQLOfCSSAttr(), true);

			///#endregion 基本字段信息.


			///#region 傻瓜表单
			//单元格数量 2013-07-24 增加
		map.AddDDLSysEnum(MapAttrAttr.ColSpan, 1, "TextBox单元格数量", true, true, "ColSpanAttrDT", "@1=跨1个单元格@2=跨2个单元格@3=跨3个单元格@4=跨4个单元格@5=跨5个单元格@6=跨6个单元格");
		map.SetHelperAlert(MapAttrAttr.ColSpan, "对于傻瓜表单有效: 标识该字段TextBox横跨的宽度,占的单元格数量.");

			//文本占单元格数量
		map.AddDDLSysEnum(MapAttrAttr.LabelColSpan, 1, "Label单元格数量", true, true, "ColSpanAttrString", "@1=跨1个单元格@2=跨2个单元格@3=跨3个单元格@4=跨4个单元格@5=跨6个单元格@6=跨6个单元格");
		map.SetHelperAlert(MapAttrAttr.LabelColSpan, "对于傻瓜表单有效: 标识该字段Lable，标签横跨的宽度,占的单元格数量.");


			//文本跨行
		map.AddTBInt(MapAttrAttr.RowSpan, 1, "行数", true, false);

			//显示的分组.
		map.AddDDLSQL(MapAttrAttr.GroupID, 0, "显示的分组", MapAttrString.getSQLOfGroupAttr(), true);


		map.AddTBInt(MapAttrAttr.Idx, 0, "顺序号", true, false);
		map.SetHelperAlert(MapAttrAttr.Idx, "对傻瓜表单有效:用于调整字段在同一个分组中的顺序.");


			///#endregion 傻瓜表单

		RefMethod rm = new RefMethod();
		rm = new RefMethod();
		rm.Title = "字段重命名";
		rm.ClassMethodName = this.toString() + ".DoRenameField()";
		rm.getHisAttrs().AddTBString("key1", "@KeyOfEn", "字段重命名为?", true, false, 0, 100, 100);
		rm.refMethodType = RefMethodType.Func;
		rm.Warning = "如果是节点表单，系统就会把该流程上的所有同名的字段都会重命名，包括NDxxxRpt表单。";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "转化为文本框组件";
		rm.ClassMethodName = this.toString() + ".DoSetTextBox()";
		rm.Warning = "您确定要转化为文本框组件吗？";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "帮助弹窗显示";
		rm.ClassMethodName = this.toString() + ".DoFieldBigHelper()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.Icon = "icon-settings";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "视频教程";
		rm.ClassMethodName = this.toString() + ".DoVideo()";
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}
	public final String DoFieldBigHelper() {
		return "../../Admin/FoolFormDesigner/MapExt/FieldBigHelper.htm?FK_MapData=" + this.getFrmID() + "&KeyOfEn=" + this.getKeyOfEn();
	}

	public final String DoVideo() {
		return "https://www.bilibili.com/video/BV1EK411T7U4";
	}
	/** 
	 设置签批组件
	 
	 @return 执行结果
	*/
	public final String DoSetTextBox() throws Exception {
		MapAttrString en = new MapAttrString(this.getMyPK());
		en.setUIContralType(UIContralType.TB);
		en.setUIIsEnable(true);
		en.setUIVisible(true);
		en.Update();

		return "设置成功,当前签批组件已经是文本框了,请关闭掉当前的窗口.";
	}

	/** 
	 字段分组查询语句
	*/
	public static String getSQLOfGroupAttr() {
		return "SELECT OID as No, Lab as Name FROM Sys_GroupField WHERE FrmID='@FK_MapData'  AND (CtrlType IS NULL OR CtrlType='')  ";
	}

	/** 
	 删除
	*/
	@Override
	protected void afterDelete() throws Exception {

		//删除相对应的rpt表中的字段
		if (this.getFrmID().contains("ND") == true)
		{
			String fk_mapData = this.getFrmID().substring(0, this.getFrmID().length() - 2) + "Rpt";
			String sql = "DELETE FROM Sys_MapAttr WHERE FK_MapData='" + fk_mapData + "' AND( KeyOfEn='" + this.getKeyOfEn() + "T' OR KeyOfEn='" + this.getKeyOfEn() + "')";
			DBAccess.RunSQL(sql);
		}

		//调用frmEditAction, 完成其他的操作.
		CCFormAPI.AfterFrmEditAction(this.getFrmID());

		super.afterDelete();
	}


	@Override
	protected void afterInsertUpdateAction() throws Exception {
		MapAttr mapAttr = new MapAttr();
		mapAttr.setMyPK(this.getMyPK());
		mapAttr.RetrieveFromDBSources();
		mapAttr.Update();

		//调用frmEditAction, 完成其他的操作.
		CCFormAPI.AfterFrmEditAction(this.getFrmID());

		super.afterInsertUpdateAction();
	}


		///#endregion

	public final String DoRenameField(String newField) throws Exception {
		String sql = "";
		if (this.getFrmID().indexOf("ND") == 0)
		{
			String strs = this.getFrmID().replace("ND", "");
			strs = strs.substring(0, strs.length() - 2);

			String rptTable = "ND" + strs + "Rpt";
			MapDatas mds = new MapDatas();
			mds.Retrieve(MapDataAttr.PTable, rptTable);

			for (MapData item : mds.ToJavaList())
			{
				sql = "UPDATE Sys_MapAttr SET KeyOfEn='" + newField + "',  MyPK='" + item.getNo() + "_" + newField + "' WHERE KeyOfEn='" + this.getKeyOfEn() + "' AND FK_MapData='" + item.getNo() + "'";
				DBAccess.RunSQL(sql);
			}
		}
		else
		{
			sql = "UPDATE Sys_MapAttr SET KeyOfEn='" + newField + "', MyPK='" + this.getFrmID() + "_" + newField + "'  WHERE KeyOfEn='" + this.getKeyOfEn() + "' AND FK_MapData='" + this.getFrmID() + "'";
			DBAccess.RunSQL(sql);
		}

		return "重名称成功,如果是自由表单，请关闭表单设计器重新打开.";
	}


		///#region 重载.
	@Override
	protected boolean beforeUpdateInsertAction() throws Exception {
		MapAttr attr = new MapAttr();
		attr.setMyPK(this.getMyPK());
		attr.RetrieveFromDBSources();

		//强制设置为签批组件.
		this.SetValByKey(MapAttrAttr.UIContralType, UIContralType.SignCheck.getValue());

		//默认值.
		String defval = this.GetValStrByKey("ExtDefVal");
		if (defval.equals("") == true || defval.equals("0") == true)
		{
			String defVal = this.GetValStrByKey("DefVal");
			if (defval.contains("@") == true)
			{
				this.SetValByKey("DefVal", "");
			}
		}
		else
		{
			this.SetValByKey("DefVal", this.GetValByKey("ExtDefVal"));
		}

		//执行保存.
		attr.Save();

		if (this.GetValStrByKey("GroupID").equals("无") == true)
		{
			this.SetValByKey("GroupID", "0");
		}

		return super.beforeUpdateInsertAction();
	}

		///#endregion
}