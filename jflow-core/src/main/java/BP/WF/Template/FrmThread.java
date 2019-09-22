package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.Template.*;
import BP.WF.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 子线程组件
*/
public class FrmThread extends Entity
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	public final String getNo()
	{
		return "ND" + this.getNodeID();
	}
	public final void setNo(String value)
	{
		String nodeID = value.replace("ND", "");
		this.setNodeID(Integer.parseInt(nodeID));
	}
	/** 
	 节点ID
	*/
	public final int getNodeID()
	{
		return this.GetValIntByKey(NodeAttr.NodeID);
	}
	public final void setNodeID(int value)
	{
		this.SetValByKey(NodeAttr.NodeID, value);
	}
	/** 
	 控件状态
	*/
	public final FrmThreadSta getFrmThreadSta()
	{
		return FrmThreadSta.forValue(this.GetValIntByKey(FrmThreadAttr.FrmThreadSta));
	}
	public final void setFrmThreadSta(FrmThreadSta value)
	{
		this.SetValByKey(FrmThreadAttr.FrmThreadSta, value.getValue());
	}
	/** 
	 Y
	*/
	public final float getFrmThread_Y()
	{
		return this.GetValFloatByKey(FrmThreadAttr.FrmThread_Y);
	}
	public final void setFrmThread_Y(float value)
	{
		this.SetValByKey(FrmThreadAttr.FrmThread_Y, value);
	}
	/** 
	 X
	*/
	public final float getFrmThread_X()
	{
		return this.GetValFloatByKey(FrmThreadAttr.FrmThread_X);
	}
	public final void setFrmThread_X(float value)
	{
		this.SetValByKey(FrmThreadAttr.FrmThread_X, value);
	}
	/** 
	 W
	*/
	public final float getFrmThread_W()
	{
		return this.GetValFloatByKey(FrmThreadAttr.FrmThread_W);
	}
	public final void setFrmThread_W(float value)
	{
		this.SetValByKey(FrmThreadAttr.FrmThread_W, value);
	}
	public final String getFrmThread_Wstr()
	{
		if (this.getFrmThread_W() == 0)
		{
			return "100%";
		}
		return this.getFrmThread_W() + "px";
	}
	/** 
	 H
	*/
	public final float getFrmThread_H()
	{
		return this.GetValFloatByKey(FrmThreadAttr.FrmThread_H);
	}
	public final void setFrmThread_H(float value)
	{
		this.SetValByKey(FrmThreadAttr.FrmThread_H, value);
	}
	public final String getFrmThread_Hstr()
	{
		if (this.getFrmThread_H() == 0)
		{
			return "100%";
		}
		return this.getFrmThread_H() + "px";
	}
	/** 
	 节点名称.
	*/
	public final String getName()
	{
		return this.GetValStringByKey("Name");
	}
	/** 
	 标签
	*/
	public final String getFrmThreadLab()
	{
		return this.GetValStringByKey(FrmThreadAttr.FrmThreadLab);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 控制
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		uac.IsDelete = false;
		uac.IsInsert = false;
		return uac;
	}
	/** 
	 重写主键
	*/
	@Override
	public String getPK()
	{
		return "NodeID";
	}
	/** 
	 子线程组件
	*/
	public FrmThread()
	{
	}
	/** 
	 子线程组件
	 
	 @param no
	*/
	public FrmThread(String mapData)
	{
		if (mapData.contains("ND") == false)
		{
			this.setFrmThreadSta(FrmThreadSta.Disable);
			return;
		}

		String mapdata = mapData.replace("ND", "");
		if (DataType.IsNumStr(mapdata) == false)
		{
			this.setFrmThreadSta(FrmThreadSta.Disable);
			return;
		}

		try
		{
			this.setNodeID(Integer.parseInt(mapdata));
		}
		catch (java.lang.Exception e)
		{
			return;
		}
		this.Retrieve();
	}
	/** 
	 子线程组件
	 
	 @param no
	*/
	public FrmThread(int nodeID)
	{
		this.setNodeID(nodeID);
		this.Retrieve();
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap()
	{
		if (this._enMap != null)
		{
			return this._enMap;
		}

		Map map = new Map("WF_Node", "子线程组件");

		map.AddTBIntPK(NodeAttr.NodeID, 0, "节点ID", true, true);
		map.AddTBString(NodeAttr.Name, null, "节点名称", true, true, 0, 100, 10);

		map.AddTBString(FrmThreadAttr.FrmThreadLab, "子线程", "显示标签", true, false, 0, 200, 10, true);


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 此处变更了 NodeSheet类中的，map 描述该部分也要变更.

		map.AddDDLSysEnum(FrmThreadAttr.FrmThreadSta, getFrmThreadSta().Disable.getValue(), "组件状态", true, true, FrmThreadAttr.FrmThreadSta, "@0=禁用@1=启用");

		map.AddTBFloat(FrmThreadAttr.FrmThread_X, 5, "位置X", true, false);
		map.AddTBFloat(FrmThreadAttr.FrmThread_Y, 5, "位置Y", true, false);

		map.AddTBFloat(FrmThreadAttr.FrmThread_H, 300, "高度", true, false);
		map.AddTBFloat(FrmThreadAttr.FrmThread_W, 400, "宽度", true, false);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 此处变更了 NodeSheet类中的，map 描述该部分也要变更.

		this._enMap = map;
		return this._enMap;
	}

	@Override
	protected boolean beforeUpdateInsertAction()
	{
		return super.beforeUpdateInsertAction();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}