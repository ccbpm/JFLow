package BP.Sys.FrmUI;

import BP.DA.DBAccess;
import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.EntityMyPK;
import BP.En.Map;
import BP.En.RefMethod;
import BP.En.RefMethodType;
import BP.En.UAC;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.WF.Glo;

/** 
日期字段
*/
public class MapAttrDT extends EntityMyPK
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
	 日期字段
	*/
	public MapAttrDT()
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

		Map map = new Map("Sys_MapAttr", "日期字段");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);

		map.AddTBStringPK(MapAttrAttr.MyPK, null, "主键", false, false, 0, 200, 20);
		map.AddTBString(MapAttrAttr.FK_MapData, null, "实体标识", false, false, 1, 100, 20);

		map.AddTBString(MapAttrAttr.Name, null, "字段中文名", true, false, 0, 200, 20);
		map.AddTBString(MapAttrAttr.KeyOfEn, null, "字段名", true, true, 1, 200, 20);

		map.AddDDLSysEnum(MapAttrAttr.MyDataType, 6, "数据类型", true, false);

		map.AddTBString(MapAttrAttr.DefVal, null, "默认值(@RDT为当前日期)", true, false, 0, 100, 20);

		map.AddBoolean(MapAttrAttr.UIVisible, true, "是否可见？", true, true);
		map.AddBoolean(MapAttrAttr.UIIsEnable, true, "是否可编辑？", true, true);
		map.AddBoolean(MapAttrAttr.UIIsInput, false, "是否必填项？", true, true);

		map.AddTBString(MapAttrAttr.Tip, null, "激活提示", true, false, 0, 4000, 20, true);

		map.AddDDLSysEnum(MapAttrAttr.ColSpan, 1, "单元格数量", true, true, "ColSpanAttrDT", "@0=跨0个单元格@1=跨1个单元格@2=跨2个单元格@3=跨3个单元格@4=跨4个单元格");

		 //文本占单元格数量
        map.AddDDLSysEnum(MapAttrAttr.TextColSpan, 1, "文本单元格数量", true, true, "ColSpanAttrString",
            "@1=跨1个单元格@2=跨2个单元格@3=跨3个单元格@4=跨4个单元格");

        //文本跨行
        map.AddDDLSysEnum(MapAttrAttr.RowSpan, 1, "行数", true, true, "RowSpanAttrString",
           "@1=跨1行@2=跨2行@3=跨3行");
        
		//显示的分组.
		map.AddDDLSQL(MapAttrAttr.GroupID, "0", "显示的分组",
				MapAttrString.SQLOfGroupAttr(), true);
		
		RefMethod rm = new RefMethod();

		rm = new RefMethod();
		rm.Title = "自动计算";
		rm.ClassMethodName = this.toString() + ".DoAutoFull()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		//map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "正则表达式";
		rm.ClassMethodName = this.toString() + ".DoRegularExpression()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		//map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "脚本验证";
		rm.ClassMethodName = this.toString() + ".DoInputCheck()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
	//	map.AddRefMethod(rm);

		 rm = new RefMethod();
	        rm.Title = "事件绑函数";
	        rm.ClassMethodName = this.toString() + ".BindFunction()";
	        rm.refMethodType = RefMethodType.RightFrameOpen;
	        map.AddRefMethod(rm);
	        
		 
	        rm = new RefMethod();
            rm.Title = "日期输入限制";
            rm.ClassMethodName = this.toString() + ".DataFieldInputRole()";
            rm.refMethodType = RefMethodType.RightFrameOpen;
            map.AddRefMethod(rm);
            

		this.set_enMap(map);
		return this.get_enMap();
	}

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		return super.beforeUpdateInsertAction();
	}
	/** 
	 旧版本设置
	 
	 @return 
	*/
	public final String DoOldVer()
	{
		return  "../../Admin/FoolFormDesigner/EditF.htm?KeyOfEn=" + this.getKeyOfEn() + "&FType=" + this.getMyDataType() + "&MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData();
	}

	/** 
	 自动计算
	 
	 @return 
	*/
	public final String DoAutoFull()
	{
		return "../../Admin/FoolFormDesigner/MapExt/AutoFull.htm?FK_MapData=" + this.getFK_MapData() + "&ExtType=AutoFull&KeyOfEn=" + this.getKeyOfEn();
	}
	 /// <summary>
    /// 绑定函数
    /// </summary>
    /// <returns></returns>
    public final String BindFunction()
    {
        return "../../Admin/FoolFormDesigner/MapExt/BindFunction.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
    }
	 
    public final String DataFieldInputRole()
    {
        return "../../Admin/FoolFormDesigner/MapExt/DataFieldInputRole.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
    }
	 
	/** 
	 文本框自动完成
	 
	 @return 
	*/
	public final String DoTBFullCtrl()
	{
		return "../../Admin/FoolFormDesigner/MapExt/TBFullCtrl.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=" + this.getMyPK();
	}

	/** 
	 设置级联
	 
	 @return 
	*/
	public final String DoInputCheck()
	{
		return Glo.getCCFlowAppPath() + "/WF/Admin/FoolFormDesigner/MapExt/InputCheck.jsp?FK_MapData=" + this.getFK_MapData() + "&OperAttrKey=" + this.getKeyOfEn() + "&RefNo=" + this.getMyPK() + "&DoType=New&ExtType=InputCheck";
	}
	/** 
	 扩展控件
	 
	 @return 
	*/
	public final String DoEditFExtContral()
	{
		return Glo.getCCFlowAppPath() + "/WF/Admin/FoolFormDesigner/EditFExtContral.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=" + this.getMyPK();
	}
	
	@Override
	protected  void afterInsertUpdateAction() throws Exception
    {
        MapAttr mapAttr = new MapAttr();
        mapAttr.setMyPK(this.getMyPK());
        mapAttr.RetrieveFromDBSources();
        mapAttr.Update();
        //删除相对应的rpt表中的字段
        if (this.getFK_MapData().contains("ND") == true)
        {
            String fk_mapData = this.getFK_MapData().substring(0, this.getFK_MapData().length() - 2) + "Rpt";
            String sql = "DELETE FROM Sys_MapAttr WHERE FK_MapData='" + fk_mapData + "' AND KeyOfEn='" + this.getKeyOfEn() + "'";
            DBAccess.RunSQL(sql);
        }
        super.afterInsertUpdateAction();
    }
}

