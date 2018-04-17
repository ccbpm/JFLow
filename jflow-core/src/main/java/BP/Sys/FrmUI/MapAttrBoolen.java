package BP.Sys.FrmUI;

import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.EntityMyPK;
import BP.En.Map;
import BP.En.RefMethod;
import BP.En.RefMethodType;
import BP.En.UAC;
import BP.Sys.MapAttrAttr;
import BP.WF.Glo;

/** 
Boolen字段
*/
public class MapAttrBoolen extends EntityMyPK
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
	 数据类型
	*/
	public final int getMyDataType()
	{
		return this.GetValIntByKey(MapAttrAttr.MyDataType);
	}
	public final void setMyDataType(int value)
	{
		this.SetValByKey(MapAttrAttr.MyDataType, value);
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
	 Boolen字段
	*/
	public MapAttrBoolen()
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

		Map map = new Map("Sys_MapAttr", "Boolen字段");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);


		map.AddTBStringPK(MapAttrAttr.MyPK, null, "主键", false, false, 0, 200, 20);
		map.AddTBString(MapAttrAttr.FK_MapData, null, "实体标识", false, false, 1, 100, 20);

		map.AddTBString(MapAttrAttr.Name, null, "字段中文名", true, false, 0, 200, 20);
		map.AddTBString(MapAttrAttr.KeyOfEn, null, "字段名", true, true, 1, 200, 20);

			//数据类型.
		map.AddDDLSysEnum(MapAttrAttr.MyDataType, 4, "数据类型", true, false);

		map.AddBoolean(MapAttrAttr.UIVisible, true, "是否可见？", true, true);
		map.AddBoolean(MapAttrAttr.UIIsEnable, true, "是否可编辑？", true, true);
		
		if (Glo.Plant.equals("CCFlow"))
			map.AddBoolean(MapAttrAttr.DefVal, false, "默认值(是否选中？)", true, true);
		else
			map.AddTBString(MapAttrAttr.DefVal, null, "默认值(是否选中？0=否,1=是)", true, false, 0, 200, 20);

		map.AddTBString(MapAttrAttr.Tip, null, "激活提示", true, false, 0, 800, 20, true);
		//单元格数量 2013-07-24 增加。
		map.AddDDLSysEnum(MapAttrAttr.ColSpan, 1, "单元格数量", true, true, "ColSpanAttrString", "@1=跨1个单元格@3=跨3个单元格@4=跨4个单元格");

		//显示的分组.
		map.AddDDLSQL(MapAttrAttr.GroupID, "0", "显示的分组", "SELECT OID as No, Lab as Name FROM Sys_GroupField WHERE FrmID='@FK_MapData'", true);
		RefMethod rm = new RefMethod();

		rm = new RefMethod();
		rm.Title = "旧版本设置";
		rm.ClassMethodName = this.toString() + ".DoOldVer()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		//map.AddRefMethod(rm);
		
	    rm = new RefMethod();
        rm.Title = "事件绑函数";
        rm.ClassMethodName = this.toString() + ".BindFunction()";
        rm.refMethodType = RefMethodType.RightFrameOpen;
        map.AddRefMethod(rm);


		this.set_enMap(map);
		return this.get_enMap();
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
	 旧版本设置
	 
	 @return 
	*/
	public final String DoOldVer()
	{
		return Glo.getCCFlowAppPath() + "/WF/Admin/FoolFormDesigner/EditF.htm?KeyOfEn=" + this.getKeyOfEn() + "&FType=" + this.getMyDataType() + "&MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData();
	}
		///#endregion 方法执行.
}
