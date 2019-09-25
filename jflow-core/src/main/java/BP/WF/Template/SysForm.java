package BP.WF.Template;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.En.Map;
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
	public final String getPTable() throws Exception
	{
		return this.GetValStringByKey(SysFormAttr.PTable);
	}
	public final void setPTable(String value) throws Exception
	{
		this.SetValByKey(SysFormAttr.PTable, value);
	}
	public final String getURL() throws Exception
	{
		return this.GetValStringByKey(SysFormAttr.URL);
	}
	public final void setURL(String value) throws Exception
	{
		this.SetValByKey(SysFormAttr.URL, value);
	}
	public final FrmType getHisFrmType() throws Exception
	{
		return FrmType.forValue(this.GetValIntByKey(SysFormAttr.FrmType));
	}
	public final void setHisFrmType(FrmType value) throws Exception
	{
		this.SetValByKey(SysFormAttr.FrmType, value.getValue());
	}
	public final String getFK_FormTree() throws Exception
	{
		return this.GetValStringByKey(SysFormAttr.FK_FormTree);
	}
	public final void setFK_FormTree(String value) throws Exception
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
	 * @throws Exception 
	*/
	public SysForm(String no) throws Exception
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
	public final int getFrmW() throws Exception
	{
		return this.GetValIntByKey(MapDataAttr.FrmW);
	}
	public final int getFrmH() throws Exception
	{
		return this.GetValIntByKey(MapDataAttr.FrmH);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}