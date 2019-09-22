package BP.WF.Template;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 表单
*/
public class Frm extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	public FrmNode HisFrmNode = null;
	public final String getPTable()
	{
		return this.GetValStringByKey(FrmAttr.PTable);
	}
	public final void setPTable(String value)
	{
		this.SetValByKey(FrmAttr.PTable, value);
	}
	public final String getFK_Flow11()
	{
		return this.GetValStringByKey(FrmAttr.FK_Flow);
	}
	public final void setFK_Flow11(String value)
	{
		this.SetValByKey(FrmAttr.FK_Flow, value);
	}
	public final String getURL()
	{
		return this.GetValStringByKey(FrmAttr.URL);
	}
	public final void setURL(String value)
	{
		this.SetValByKey(FrmAttr.URL, value);
	}
	public final FrmType getHisFrmType()
	{
		return (FrmType)this.GetValIntByKey(FrmAttr.FrmType);
	}
	public final void setHisFrmType(FrmType value)
	{
		this.SetValByKey(FrmAttr.FrmType, (int)value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 Frm
	*/
	public Frm()
	{
	}
	/** 
	 Frm
	 
	 @param no
	*/
	public Frm(String no)
	{
		super(no);

	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this._enMap != null)
		{
			return this._enMap;
		}

		Map map = new Map("Sys_MapData", "表单库");
		map.Java_SetCodeStruct("4");

		map.AddTBStringPK(FrmAttr.No, null, null, true, true, 1, 200, 4);
		map.AddTBString(FrmAttr.Name, null, null, true, false, 0, 50, 10);
		map.AddTBString(FrmAttr.FK_Flow, null, "独立表单属性:FK_Flow", true, false, 0, 50, 10);
		 //   map.AddDDLSysEnum(FrmAttr.FrmType, 0, "独立表单属性:运行类型", true, false, FrmAttr.FrmType);

			//表单的运行类型.
		map.AddDDLSysEnum(FrmAttr.FrmType, (int)BP.Sys.FrmType.FreeFrm, "表单类型",true, false, FrmAttr.FrmType);

		map.AddTBString(FrmAttr.PTable, null, "物理表", true, false, 0, 50, 10);
		map.AddTBInt(FrmAttr.DBURL, 0, "DBURL", true, false);

			// 如果是个嵌入式表单.
		map.AddTBString(FrmAttr.URL, null, "Url", true, false, 0, 50, 10);

			//表单类别.
		map.AddTBString(MapDataAttr.FK_FrmSort, "01", "表单类别", true, false, 0, 500, 20);

		map.AddTBInt(BP.Sys.MapDataAttr.FrmW, 900, "表单宽度", true, false);
		map.AddTBInt(BP.Sys.MapDataAttr.FrmH, 1200, "表单高度", true, false);

		this._enMap = map;
		return this._enMap;
	}
	public final int getFrmW()
	{
		return this.GetValIntByKey(Sys.MapDataAttr.FrmW);
	}
	public final int getFrmH()
	{
		return this.GetValIntByKey(BP.Sys.MapDataAttr.FrmH);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}