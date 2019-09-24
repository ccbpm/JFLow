package BP.Sys.FrmUI;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Sys.*;
import BP.Sys.*;
import java.util.*;

/** 
 外部数据源
*/
public class MapAttrSFSQL extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 文本字段参数属性.
	/** 
	 表单ID
	 * @throws Exception 
	*/
	public final String getFK_MapData() throws Exception
	{
		return this.GetValStringByKey(MapAttrAttr.FK_MapData);
	}
	public final void setFK_MapData(String value) throws Exception
	{
		this.SetValByKey(MapAttrAttr.FK_MapData, value);
	}
	/** 
	 字段
	 * @throws Exception 
	*/
	public final String getKeyOfEn() throws Exception
	{
		return this.GetValStringByKey(MapAttrAttr.KeyOfEn);
	}
	public final void setKeyOfEn(String value) throws Exception
	{
		this.SetValByKey(MapAttrAttr.KeyOfEn, value);
	}
	/** 
	 绑定的枚举ID
	 * @throws Exception 
	*/
	public final String getUIBindKey() throws Exception
	{
		return this.GetValStringByKey(MapAttrAttr.UIBindKey);
	}
	public final void setUIBindKey(String value) throws Exception
	{
		this.SetValByKey(MapAttrAttr.UIBindKey, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 控制权限
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.IsInsert = false;
		uac.IsUpdate = true;
		uac.IsDelete = true;
		return uac;
	}
	/** 
	 外部数据源
	*/
	public MapAttrSFSQL()
	{
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

		Map map = new Map("Sys_MapAttr", "外部数据源");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);
		map.IndexField = MapAttrAttr.FK_MapData;


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 基本信息.
		map.AddTBStringPK(MapAttrAttr.MyPK, null, "主键", false, false, 0, 200, 20);
		map.AddDDLSysEnum(MapAttrAttr.LGType, 4, "类型", false, false);
		map.AddTBString(MapAttrAttr.FK_MapData, null, "表单ID", false, false, 1, 100, 20);

		map.AddTBString(MapAttrAttr.Name, null, "字段中文名", true, false, 0, 200, 20, true);
		map.AddTBString(MapAttrAttr.KeyOfEn, null, "字段名", true, true, 1, 200, 20, true);

			//默认值.
		map.AddTBString(MapAttrAttr.UIBindKey, null, "外键SFTable", true, true, 0, 100, 20);

		map.AddTBString(MapAttrAttr.DefVal, null, "默认值", true, false, 0, 300, 20);

			//map.AddTBFloat(MapAttrAttr.UIWidth, 100, "宽度", true, false);
			//map.AddTBFloat(MapAttrAttr.UIHeight, 23, "高度", true, true);

		map.AddBoolean(MapAttrAttr.UIVisible, true, "可见", true, false);
		map.AddBoolean(MapAttrAttr.UIIsEnable, true, "是否可编辑？", true, true);

		   // map.AddBoolean(MapAttrAttr.UIIsInput, false, "是否必填项？", true, true);
		   // map.AddBoolean("IsEnableJS", false, "是否启用JS高级设置？", true, true); //参数字段.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 基本信息.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 傻瓜表单。
		map.AddDDLSysEnum(MapAttrAttr.ColSpan, 1, "单元格数量", true, true, "ColSpanAttrDT", "@0=跨0个单元格@1=跨1个单元格@2=跨2个单元格@3=跨3个单元格@4=跨4个单元格");

			//文本占单元格数量
		map.AddDDLSysEnum(MapAttrAttr.TextColSpan, 1, "文本单元格数量", true, true, "ColSpanAttrString", "@1=跨1个单元格@2=跨2个单元格@3=跨3个单元格@4=跨4个单元格");

			//文本跨行
		map.AddTBInt(MapAttrAttr.RowSpan, 1, "行数", true, false);
			//显示的分组.
		map.AddDDLSQL(MapAttrAttr.GroupID, 0, "显示的分组", MapAttrString.getSQLOfGroupAttr(), true);
		map.AddTBInt(MapAttrAttr.Idx, 0, "顺序号", true, false); //@李国文
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 傻瓜表单。

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 执行的方法.
		RefMethod rm = new RefMethod();
		rm = new RefMethod();
		rm.Title = "设置联动";
		rm.ClassMethodName = this.toString() + ".DoActiveDDL()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置显示过滤";
		rm.ClassMethodName = this.toString() + ".DoAutoFullDLL()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "填充其他控件";
		rm.ClassMethodName = this.toString() + ".DoDDLFullCtrl2019()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "外键表属性";
		rm.ClassMethodName = this.toString() + ".DoSFTable()";
		rm.refMethodType = RefMethodType.LinkeWinOpen;
		rm.GroupName = "高级";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "事件绑函数";
		rm.ClassMethodName = this.toString() + ".BindFunction()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 执行的方法.

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 方法执行.
	/** 
	 绑定函数
	 
	 @return 
	 * @throws Exception 
	*/
	public final String BindFunction() throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/BindFunction.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
	}
	/** 
	 外键表属性
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoSFTable() throws Exception
	{
		return "../../Admin/FoolFormDesigner/GuideSFTableAttr.htm?FK_SFTable=" + this.getUIBindKey();
	}
	/** 
	 设置填充其他下拉框
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoDDLFullCtrl() throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/DDLFullCtrl.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=DDLFullCtrl_" + this.getFK_MapData() + "_" + this.getKeyOfEn();
	}
	public final String DoDDLFullCtrl2019() throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/DDLFullCtrl2019.htm?FK_MapData=" + this.getFK_MapData() + "&ExtType=AutoFull&KeyOfEn=" + this.getKeyOfEn() + "&RefNo=" + this.getMyPK();
	}
	/** 
	 设置下拉框显示过滤
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoAutoFullDLL() throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/AutoFullDLL.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
	}
	/** 
	 设置级联
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoActiveDDL() throws Exception
	{
		return "../../Admin/FoolFormDesigner/MapExt/ActiveDDL.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 方法执行.

	/** 
	 删除，把影子字段也要删除.
	 * @throws Exception 
	*/
	@Override
	protected void afterDelete() throws Exception
	{
		MapAttr attr = new MapAttr();
		attr.setMyPK(attr.getFK_MapData() + "_" + this.getKeyOfEn() + "T");
		attr.Delete();

		//删除相对应的rpt表中的字段
		if (this.getFK_MapData().contains("ND") == true)
		{
			String fk_mapData = this.getFK_MapData().substring(0, this.getFK_MapData().length() - 2) + "Rpt";
			String sql = "DELETE FROM Sys_MapAttr WHERE FK_MapData='" + fk_mapData + "' AND( KeyOfEn='" + this.getKeyOfEn() + "T' OR KeyOfEn='" + this.getKeyOfEn() + "')";
			DBAccess.RunSQL(sql);
		}

		//调用frmEditAction, 完成其他的操作.
		BP.Sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());
		super.afterDelete();
	}

	@Override
	protected void afterInsertUpdateAction() throws Exception
	{
		MapAttr mapAttr = new MapAttr();
		mapAttr.setMyPK(this.getMyPK());
		mapAttr.RetrieveFromDBSources();
		mapAttr.Update();

		//调用frmEditAction, 完成其他的操作.
		BP.Sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());

		super.afterInsertUpdateAction();
	}
}