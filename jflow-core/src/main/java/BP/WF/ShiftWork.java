package BP.WF;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.Port.*;
import java.util.*;

/** 
 移交记录
*/
public class ShiftWork extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	/** 
	 工作ID
	*/
	public final long getWorkID()
	{
		return this.GetValInt64ByKey(ShiftWorkAttr.WorkID);
	}
	public final void setWorkID(long value)
	{
		SetValByKey(ShiftWorkAttr.WorkID,value);
	}
	/** 
	 工作节点
	*/
	public final int getFK_Node()
	{
		return this.GetValIntByKey(ShiftWorkAttr.FK_Node);
	}
	public final void setFK_Node(int value)
	{
		SetValByKey(ShiftWorkAttr.FK_Node,value);
	}
	/** 
	 是否读取？
	*/
	public final boolean getIsRead()
	{
		return this.GetValBooleanByKey(ShiftWorkAttr.IsRead);
	}
	public final void setIsRead(boolean value)
	{
		SetValByKey(ShiftWorkAttr.IsRead, value);
	}
	/** 
	 ToEmpName
	*/
	public final String getToEmpName()
	{
		return this.GetValStringByKey(ShiftWorkAttr.ToEmpName);
	}
	public final void setToEmpName(String value)
	{
		SetValByKey(ShiftWorkAttr.ToEmpName, value);
	}
	/** 
	 移交人名称.
	*/
	public final String getFK_EmpName()
	{
		return this.GetValStringByKey(ShiftWorkAttr.FK_EmpName);
	}
	public final void setFK_EmpName(String value)
	{
		SetValByKey(ShiftWorkAttr.FK_EmpName, value);
	}
	/** 
	 移交时间
	*/
	public final String getRDT()
	{
		return this.GetValStringByKey(ShiftWorkAttr.RDT);
	}
	public final void setRDT(String value)
	{
		SetValByKey(ShiftWorkAttr.RDT, value);
	}
	/** 
	 移交意见
	*/
	public final String getNote()
	{
		return this.GetValStringByKey(ShiftWorkAttr.Note);
	}
	public final void setNote(String value)
	{
		SetValByKey(ShiftWorkAttr.Note,value);
	}
	/** 
	 移交意见html格式
	*/
	public final String getNoteHtml()
	{
		return this.GetValHtmlStringByKey(ShiftWorkAttr.Note);
	}
	/** 
	 移交人
	*/
	public final String getFK_Emp()
	{
		return this.GetValStringByKey(ShiftWorkAttr.FK_Emp);
	}
	public final void setFK_Emp(String value)
	{
		SetValByKey(ShiftWorkAttr.FK_Emp, value);
	}
	/** 
	 移交给
	*/
	public final String getToEmp()
	{
		return this.GetValStringByKey(ShiftWorkAttr.ToEmp);
	}
	public final void setToEmp(String value)
	{
		SetValByKey(ShiftWorkAttr.ToEmp, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 移交记录
	*/
	public ShiftWork()
	{
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

		Map map = new Map("WF_ShiftWork", "移交记录");

		map.AddMyPK();

		map.AddTBInt(ShiftWorkAttr.WorkID, 0, "工作ID", true, true);
		map.AddTBInt(ShiftWorkAttr.FK_Node, 0, "FK_Node", true, true);
		map.AddTBString(ShiftWorkAttr.FK_Emp, null, "移交人", true, true, 0, 40, 10);
		map.AddTBString(ShiftWorkAttr.FK_EmpName, null, "移交人名称", true, true, 0, 40, 10);

		map.AddTBString(ShiftWorkAttr.ToEmp, null, "移交给", true, true, 0, 40, 10);
		map.AddTBString(ShiftWorkAttr.ToEmpName, null, "移交给名称", true, true, 0, 40, 10);

		map.AddTBDateTime(ShiftWorkAttr.RDT, null, "移交时间", true, true);
		map.AddTBString(ShiftWorkAttr.Note, null, "移交原因", true, true, 0, 2000, 10);

		map.AddTBInt(ShiftWorkAttr.IsRead, 0, "是否读取？", true, true);
		this._enMap = map;
		return this._enMap;
	}
	@Override
	protected boolean beforeInsert()
	{
		this.MyPK = BP.DA.DBAccess.GenerOIDByGUID().toString();
		this.setRDT(DataType.CurrentDataTime);
		return super.beforeInsert();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}