package BP.WF.Template;

import BP.En.EntityNoName;
import BP.En.Map;
import BP.Sys.FrmType;
import BP.Sys.MapDataAttr;

/** 
 系统表单
 
*/
public class SysForm extends EntityNoName
{

		
	public final String getPTable()
	{
		return this.GetValStringByKey(SysFormAttr.PTable);
	}
	public final void setPTable(String value)
	{
		this.SetValByKey(SysFormAttr.PTable, value);
	}
	public final String getURL()
	{
		return this.GetValStringByKey(SysFormAttr.URL);
	}
	public final void setURL(String value)
	{
		this.SetValByKey(SysFormAttr.URL, value);
	}
	public final FrmType getHisFrmType()
	{
		return FrmType.forValue(this.GetValIntByKey(SysFormAttr.FrmType));
	}
	public final void setHisFrmType(FrmType value)
	{
		this.SetValByKey(SysFormAttr.FrmType, value);
	}
	public final String getFK_FormTree()
	{
		return this.GetValStringByKey(SysFormAttr.FK_FormTree);
	}
	public final void setFK_FormTree(String value)
	{
		this.SetValByKey(SysFormAttr.FK_FormTree, value);
	}

		///#endregion


		
	/** 
	 Frm
	 
	*/
	public SysForm()
	{
	}
	/** 
	 Frm
	 
	 @param no
	*/
	public SysForm(String no)
	{
		super(no);

	}
	/** 
	 重写基类方法
	 
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_MapData", "系统表单");
		map.Java_SetCodeStruct("4");

		map.AddTBStringPK(SysFormAttr.No, null, null, true, true, 1, 200, 4);
		map.AddTBString(SysFormAttr.Name, null, null, true, false, 0, 500, 10);

			//表单类型.
		map.AddTBInt(MapDataAttr.FrmType, 1, "表单类型", true, false);


			//该表单对应的物理表
		map.AddTBString(SysFormAttr.PTable, null, "物理表", true, false, 0, 50, 10);

			// FrmType=嵌入式表单时, 该字段有效. 
		map.AddTBString(SysFormAttr.URL, null, "Url", true, false, 0, 50, 10);

			//系统表单类别.
		map.AddTBString(SysFormAttr.FK_FormTree, null, "表单树", true, false, 0, 10, 20);

		map.AddTBInt(MapDataAttr.FrmW, 900, "系统表单宽度", true, false);
		map.AddTBInt(MapDataAttr.FrmH, 1200, "系统表单高度", true, false);

		this.set_enMap(map);
		return this.get_enMap();
	}
	public final int getFrmW()
	{
		return this.GetValIntByKey(MapDataAttr.FrmW);
	}
	public final int getFrmH()
	{
		return this.GetValIntByKey(MapDataAttr.FrmH);
	}


		///#endregion
}