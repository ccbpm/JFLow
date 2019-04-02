package BP.Sys.FrmUI;

import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.EntityMyPK;
import BP.En.Map;
import BP.En.RefMethod;
import BP.En.RefMethodType;
import BP.En.UAC;
import BP.Sys.MapAttrAttr;


/** 
 外键字段
*/
public class MapAttrSFTable extends EntityMyPK
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
	 外键字段
	*/
	public MapAttrSFTable()
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

        // #region 基本信息.
         
         map.AddTBStringPK(MapAttrAttr.MyPK, null, "主键", false, false, 0, 200, 20);
         map.AddTBString(MapAttrAttr.FK_MapData, null, "表单ID", false, false, 1, 100, 20);

         map.AddTBString(MapAttrAttr.Name, null, "字段中文名", true, false, 0, 200, 20,true);
         map.AddTBString(MapAttrAttr.KeyOfEn, null, "字段名", true, true, 1, 200, 20,true);

         //默认值.
         map.AddDDLSysEnum(MapAttrAttr.LGType, 4, "类型", true, false);
         map.AddTBString(MapAttrAttr.UIBindKey, null, "外键SFTable", true, true, 0, 100, 20,true);

         map.AddTBString(MapAttrAttr.DefVal, null, "默认值", true, false, 0, 300, 20);

         //map.AddTBFloat(MapAttrAttr.UIWidth, 100, "宽度", true, false);
         //map.AddTBFloat(MapAttrAttr.UIHeight, 23, "高度", true, true);
 
         map.AddBoolean(MapAttrAttr.UIVisible, true, "是否可见", true, false);
         map.AddBoolean(MapAttrAttr.UIIsEnable, true, "是否可编辑？", true, true);
         map.AddBoolean(MapAttrAttr.UIIsInput, false, "是否必填项？", true, true);
        // map.AddBoolean("IsEnableJS", false, "是否启用JS高级设置？", true, true); //参数字段.

         //#endregion 基本信息.

        // #region 傻瓜表单。
         map.AddDDLSysEnum(MapAttrAttr.ColSpan, 1, "单元格数量", true, true, "ColSpanAttrDT", "@1=跨1个单元格@3=跨3个单元格");

         //文本占单元格数量
         map.AddDDLSysEnum(MapAttrAttr.TextColSpan, 1, "文本单元格数量", true, true, "ColSpanAttrString",
             "@1=跨1个单元格@2=跨2个单元格@3=跨3个单元格@4=跨4个单元格");

         //文本跨行
         map.AddDDLSysEnum(MapAttrAttr.RowSpan, 1, "行数", true, true, "RowSpanAttrString",
            "@1=跨1行@2=跨2行@3=跨3行");
         
         //显示的分组.
         map.AddDDLSQL(MapAttrAttr.GroupID, "0", "显示的分组",MapAttrString.SQLOfGroupAttr(), true);
         //#endregion 傻瓜表单。

        // #region 执行的方法.
         RefMethod rm = new RefMethod();
         rm = new RefMethod();
         rm.Title = "设置联动";
         rm.ClassMethodName = this.toString() + ".DoActiveDDL()";
         rm.refMethodType =RefMethodType.RightFrameOpen;
         map.AddRefMethod(rm);

         rm = new RefMethod();
         rm.Title = "设置显示过滤";
         rm.ClassMethodName = this.toString() + ".DoAutoFullDLL()";
         rm.refMethodType = RefMethodType.RightFrameOpen;
         map.AddRefMethod(rm);

         rm = new RefMethod();
         rm.Title = "填充其他控件";
         rm.ClassMethodName = this.toString() + ".DoDDLFullCtrl2019()";
         rm.refMethodType= RefMethodType.RightFrameOpen;
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
      
       //  #endregion 执行的方法.
         

		this.set_enMap(map);
		return this.get_enMap();
	}

	/** 
	 旧版本设置
	 
	 @return 
	*/
	public final String DoOldVer()
	{
		return "../../Admin/FoolFormDesigner/EditTable.htm?KeyOfEn=" + this.getKeyOfEn() + "&EnumKey=" + this.getUIBindKey() + "&MyPK=" + this.getMyPK() + "&UIBindKey=" + this.getUIBindKey();
	}
	public final String DoOldVerAspx()
	{
		return "../../Admin/FoolFormDesigner/EditTable.aspx?KeyOfEn=" + this.getKeyOfEn() + "&EnumKey=" + this.getUIBindKey() + "&MyPK=" + this.getMyPK() + "&UIBindKey=" + this.getUIBindKey();
	}
	/** 
	 外键表属性
	 
	 @return 
	*/
	public final String DoSFTable()
	{
		return "../../Admin/FoolFormDesigner/GuideSFTableAttr.htm?FK_SFTable=" + this.getUIBindKey();
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
	 设置填充其他下拉框
	 
	 @return 
	*/
	
	 public final String DoDDLFullCtrl2019()
     {
         return "../../Admin/FoolFormDesigner/MapExt/DDLFullCtrl2019.htm?FK_MapData=" + this.getFK_MapData() + "&ExtType=AutoFull&KeyOfEn=" + this.getKeyOfEn() + "&RefNo=" + this.getMyPK();
     }
	 
	 
	/** 
	 设置下拉框显示过滤
	 
	 @return 
	*/
	public final String DoAutoFullDLL()
	{
		return "../../Admin/FoolFormDesigner/MapExt/AutoFullDLL.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
	}
	/** 
	 设置级联
	 
	 @return 
	*/
	public final String DoActiveDDL()
	{
		return "../../Admin/FoolFormDesigner/MapExt/ActiveDDL.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
	}
}
