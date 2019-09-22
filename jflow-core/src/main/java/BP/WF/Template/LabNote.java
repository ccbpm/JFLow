package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.Template.*;
import BP.Port.*;
import BP.WF.*;
import java.util.*;

/** 
 标签.	 
*/
public class LabNote extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 基本属性
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.IsUpdate = true;
		return uac;
	}

	/** 
	 x
	*/
	public final int getX()
	{
		return this.GetValIntByKey(NodeAttr.X);
	}
	public final void setX(int value)
	{
		this.SetValByKey(NodeAttr.X, value);
	}

	/** 
	 y
	*/
	public final int getY()
	{
		return this.GetValIntByKey(NodeAttr.Y);
	}
	public final void setY(int value)
	{
		this.SetValByKey(NodeAttr.Y, value);
	}
	/** 
	 标签的事务编号
	*/
	public final String getFK_Flow()
	{
		return this.GetValStringByKey(NodeAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)
	{
		SetValByKey(NodeAttr.FK_Flow, value);
	}
	public final String getName()
	{
		return this.GetValStringByKey(NodeAttr.Name);
	}
	public final void setName(String value)
	{
		SetValByKey(NodeAttr.Name, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
	/** 
	 标签
	*/
	public LabNote()
	{
	}
	/** 
	 标签
	 
	 @param _oid 标签ID	
	*/
	public LabNote(String mypk)
	{
		this.setMyPK(mypk);
		this.Retrieve();
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

		Map map = new Map("WF_LabNote", "标签");
		map.IndexField = NodeAttr.FK_Flow;

		map.AddMyPK();

		map.AddTBString(NodeAttr.Name, null, null, true, false, 0, 3000, 10, true);
		map.AddTBString(NodeAttr.FK_Flow, null, "流程", false, true, 0, 100, 10);

		map.AddTBInt(NodeAttr.X, 0, "X坐标", false, false);
		map.AddTBInt(NodeAttr.Y, 0, "Y坐标", false, false);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	@Override
	protected boolean beforeInsert()
	{
		this.setMyPK( BP.DA.DBAccess.GenerOID().toString();
		return super.beforeInsert();
	}
}