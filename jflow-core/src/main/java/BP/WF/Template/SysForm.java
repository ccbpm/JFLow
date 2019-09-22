package BP.WF.Template;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.Port.*;
import BP.WF.Template.*;
import BP.WF.*;
import BP.WF.*;
import java.util.*;

/** 
 系统表单
*/
public class SysForm extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
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
		return (FrmType)this.GetValIntByKey(SysFormAttr.FrmType);
	}
	public final void setHisFrmType(FrmType value)
	{
		this.SetValByKey(SysFormAttr.FrmType, value.getValue());
	}
	public final String getFK_FormTree()
	{
		return this.GetValStringByKey(SysFormAttr.FK_FormTree);
	}
	public final void setFK_FormTree(String value)
	{
		this.SetValByKey(SysFormAttr.FK_FormTree, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
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
		map.AddTBInt(Sys.MapDataAttr.FrmType, 1, "表单类型", true, false);


			//该表单对应的物理表
		map.AddTBString(SysFormAttr.PTable, null, "物理表", true, false, 0, 50, 10);

			// FrmType=嵌入式表单时, 该字段有效. 
		map.AddTBString(SysFormAttr.URL, null, "Url", true, false, 0, 50, 10);

			//系统表单类别.
		map.AddTBString(SysFormAttr.FK_FormTree, null, "表单树", true, false, 0, 10, 20);

		map.AddTBInt(Sys.MapDataAttr.FrmW, 900, "系统表单宽度", true, false);
		map.AddTBInt(Sys.MapDataAttr.FrmH, 1200, "系统表单高度", true, false);

		this.set_enMap(map);
		return this.get_enMap();
	}
	public final int getFrmW()
	{
		return this.GetValIntByKey(Sys.MapDataAttr.FrmW);
	}
	public final int getFrmH()
	{
		return this.GetValIntByKey(Sys.MapDataAttr.FrmH);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}