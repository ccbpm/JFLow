package BP.Sys.FrmUI;

import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.EntityMyPK;
import BP.En.Map;
import BP.En.RefMethod;
import BP.En.RefMethodType;
import BP.En.UAC;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.SystemConfig;
import BP.WF.Glo;


/** 
 枚举字段
*/
public class MapAttrEnum extends EntityMyPK
{
	/** 
	 表单ID
	*/
	public final String getFK_MapData()
	{
		return this.GetValStringByKey(MapAttrAttr.FK_MapData);
	}
	public final void setFK_MapData(String value)
	{
		this.SetValByKey(MapAttrAttr.FK_MapData, value);
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
	/** 
	 绑定的枚举ID
	*/
	public final String getUIBindKey()
	{
		return this.GetValStringByKey(MapAttrAttr.UIBindKey);
	}
	public final void setUIBindKey(String value)
	{
		this.SetValByKey(MapAttrAttr.UIBindKey, value);
	}
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
	 枚举字段
	*/
	public MapAttrEnum()
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

		Map map = new Map("Sys_MapAttr", "枚举字段");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);

		map.AddTBStringPK(MapAttrAttr.MyPK, null, "主键", false, false, 0, 200, 20);
		map.AddTBString(MapAttrAttr.FK_MapData, null, "实体标识", false, false, 1, 100, 20);

		map.AddTBString(MapAttrAttr.Name, null, "字段中文名", true, false, 0, 200, 20);
		map.AddTBString(MapAttrAttr.KeyOfEn, null, "字段名", true, true, 1, 200, 20);

		//默认值.
		//sunxd
		//问题:ORACLE数据库会自动将字段名称转换为大写，导致页面接收不到
		//解决:“SELECT  IntKey as No, Lab as Name FROM Sys_Enum where EnumKey='@UIBindKey'” 修改为 “SELECT  IntKey \"No\", Lab \"Name\" FROM Sys_Enum where EnumKey='@UIBindKey'”
		map.AddDDLSQL(MapAttrAttr.DefVal, "0", "默认值（选中）", "SELECT  IntKey \"No\", Lab \"Name\" FROM Sys_Enum where EnumKey='@UIBindKey'", true);

		map.AddTBString(MapAttrAttr.DefVal, null, "默认值", true, true, 0, 2000, 20);
		map.AddDDLSysEnum(MapAttrAttr.UIContralType, 0, "控件类型", true, true, "EnumUIContralType", "@1=下拉框@3=单选按钮");

		map.AddDDLSysEnum("RBShowModel", 0, "单选按钮的展现方式", true, true, "RBShowModel", "@0=竖向@3=横向");

		//map.AddDDLSysEnum(MapAttrAttr.LGType, 0, "逻辑类型", true, false, MapAttrAttr.LGType, 
		// "@0=普通@1=枚举@2=外键@3=打开系统页面");

		map.AddTBFloat(MapAttrAttr.UIWidth, 100, "宽度", true, false);
		map.AddTBFloat(MapAttrAttr.UIHeight, 23, "高度", true, true);

		map.AddTBString(MapAttrAttr.UIBindKey, null, "枚举ID", true, true, 0, 100, 20);

		map.AddBoolean(MapAttrAttr.UIVisible, true, "是否可见", true, true);
		map.AddBoolean(MapAttrAttr.UIIsEnable, true, "是否可编辑", true, true);

		map.AddBoolean(MapAttrAttr.UIIsInput, false, "是否必填项？", true, true);
		map.AddBoolean("IsEnableJS", false, "是否启用JS高级设置？", true, true); //参数字段.

		map.AddDDLSysEnum(MapAttrAttr.ColSpan, 1, "单元格数量", true, true, "ColSpanAttrDT", "@1=跨1个单元格@3=跨3个单元格");


		//显示的分组.
		//sunxd
		//问题:ORACLE数据库会自动将字段名称转换为大写，导致页面接收不到
		//
		map.AddDDLSQL(MapAttrAttr.GroupID, "0", "显示的分组", "SELECT OID No, Lab Name FROM Sys_GroupField WHERE FrmID='@FK_MapData' ", true);

		RefMethod rm = new RefMethod();

		rm = new RefMethod();
		rm.Title = "设置联动";
		rm.ClassMethodName = this.toString() + ".DoActiveDDL()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "填充其他控件";
		rm.ClassMethodName = this.toString() + ".DoDDLFullCtrl()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "编辑枚举值";
		rm.ClassMethodName = this.toString() + ".DoSysEnum()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		 rm = new RefMethod();
	        rm.Title = "事件绑函数";
	        rm.ClassMethodName = this.toString() + ".BindFunction()";
	        rm.refMethodType = RefMethodType.RightFrameOpen;
	        map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "高级JS设置";
		rm.ClassMethodName = this.toString() + ".DoRadioBtns()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "高级设置";
		map.AddRefMethod(rm);


		this.set_enMap(map);
		return this.get_enMap();
	}

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		MapAttr attr = new MapAttr();
		attr.setMyPK(this.getMyPK());
		attr.RetrieveFromDBSources();

		//是否启用高级js设置.
		attr.setIsEnableJS(this.GetValBooleanByKey("IsEnableJS"));

		//单选按钮的展现方式.
		attr.setRBShowModel(this.GetValIntByKey("RBShowModel"));

		//执行保存.
		attr.Save();

		return super.beforeUpdateInsertAction();
	}
	/** 
	 编辑枚举值
	 
	 @return 
	*/
	public final String DoSysEnum()
	{
		return Glo.getCCFlowAppPath() + "WF/Admin/FoolFormDesigner/SysEnum.html?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&RefNo=" + this.getMyPK() + "&EnumKey=" + this.getUIBindKey();
	}
	/** 
	 设置自动填充
	 
	 @return 
	*/
	public final String DoDDLFullCtrl()
	{
		return Glo.getCCFlowAppPath() + "WF/Admin/FoolFormDesigner/MapExt/DDLFullCtrl.htm?FK_MapData=" + this.getFK_MapData() + "&ExtType=AutoFull&KeyOfEn=" + this.getKeyOfEn() + "&RefNo=" + this.getMyPK();
	}
	/** 
	 设置自动填充
	 
	 @return 
	*/
	public final String DoAutoFull()
	{
		return Glo.getCCFlowAppPath() + "WF/MapDef/MapExt/AutoFullDLL.jsp?FK_MapData=" + this.getFK_MapData() + "&ExtType=AutoFull&KeyOfEn=" + this.getKeyOfEn() + "&RefNo=" + this.getMyPK();
	}
	 /// <summary>
    /// 绑定函数
    /// </summary>
    /// <returns></returns>
    public final String BindFunction()
    {
        return "../../Admin/FoolFormDesigner/MapExt/BindFunction.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
    }
	/** 
	 高级设置
	 
	 @return 
	*/
	public final String DoRadioBtns()
	{
		return Glo.getCCFlowAppPath() + "WF/Admin/FoolFormDesigner/MapExt/RadioBtns.htm?FK_MapData=" + this.getFK_MapData() + "&ExtType=AutoFull&KeyOfEn=" + this.getKeyOfEn() + "&RefNo=" + this.getMyPK();
	}
	/** 
	 设置级联
	 
	 @return 
	*/
	public final String DoActiveDDL()
	{
		return Glo.getCCFlowAppPath() + "/WF/Admin/FoolFormDesigner/MapExt/ActiveDDL.htm?FK_MapData=" + this.getFK_MapData() + "&ExtType=AutoFull&KeyOfEn=" + this.getKeyOfEn() + "&RefNo=" + this.getMyPK();
	}
}

