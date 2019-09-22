package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.Template.*;
import BP.WF.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 流转自定义组件
*/
public class FrmTransferCustom extends Entity
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
	public final FTCSta getFTCSta()
	{
		return FTCSta.forValue(this.GetValIntByKey(FTCAttr.FTCSta));
	}
	public final void setFTCSta(FTCSta value)
	{
		this.SetValByKey(FTCAttr.FTCSta, value.getValue());
	}
	/** 
	 工作模式
	*/
	public final int getFTCWorkModel()
	{
		return this.GetValIntByKey(FTCAttr.FTCWorkModel);
	}
	public final void setFTCWorkModel(int value)
	{
		this.SetValByKey(FTCAttr.FTCWorkModel, value);
	}
	/** 
	 Y
	*/
	public final float getFTC_Y()
	{
		return this.GetValFloatByKey(FTCAttr.FTC_Y);
	}
	public final void setFTC_Y(float value)
	{
		this.SetValByKey(FTCAttr.FTC_Y, value);
	}
	/** 
	 X
	*/
	public final float getFTC_X()
	{
		return this.GetValFloatByKey(FTCAttr.FTC_X);
	}
	public final void setFTC_X(float value)
	{
		this.SetValByKey(FTCAttr.FTC_X, value);
	}
	/** 
	 W
	*/
	public final float getFTC_W()
	{
		return this.GetValFloatByKey(FTCAttr.FTC_W);
	}
	public final void setFTC_W(float value)
	{
		this.SetValByKey(FTCAttr.FTC_W, value);
	}
	public final String getFTC_Wstr()
	{
		if (this.getFTC_W() == 0)
		{
			return "100%";
		}
		return this.getFTC_W() + "px";
	}
	/** 
	 H
	*/
	public final float getFTC_H()
	{
		return this.GetValFloatByKey(FTCAttr.FTC_H);
	}
	public final void setFTC_H(float value)
	{
		this.SetValByKey(FTCAttr.FTC_H, value);
	}
	public final String getFTC_Hstr()
	{
		if (this.getFTC_H() == 0)
		{
			return "100%";
		}
		return this.getFTC_H() + "px";
	}
	/** 
	 节点名称.
	*/
	public final String getName()
	{
		return this.GetValStringByKey("Name");
	}
	/** 
	 显示标签
	*/
	public final String getFTCLab()
	{
		return this.GetValStrByKey(FTCAttr.FTCLab);
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
	 流转自定义组件
	*/
	public FrmTransferCustom()
	{
	}
	/** 
	 流转自定义组件
	 
	 @param no
	*/
	public FrmTransferCustom(String mapData)
	{
		if (mapData.contains("ND") == false)
		{
			this.setFTCSta(FTCSta.Disable);
			return;
		}

		String mapdata = mapData.replace("ND", "");
		if (DataType.IsNumStr(mapdata) == false)
		{
			this.setFTCSta(FTCSta.Disable);
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
	 流转自定义组件
	 
	 @param no
	*/
	public FrmTransferCustom(int nodeID)
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

		Map map = new Map("WF_Node", "流转自定义组件");

		map.AddTBIntPK(NodeAttr.NodeID, 0, "节点ID", true, true);
		map.AddTBString(NodeAttr.Name, null, "节点名称", true, true, 0, 100, 10);
		map.AddTBString(FTCAttr.FTCLab, "流转自定义", "显示标签", true, false, 0, 50, 10, true);


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 此处变更了 NodeSheet类中的，map 描述该部分也要变更.

		map.AddDDLSysEnum(FTCAttr.FTCSta, getFTCSta().Disable.getValue(), "组件状态", true, true, FTCAttr.FTCSta, "@0=禁用@1=只读@2=可设置人员");

		map.AddDDLSysEnum(FTCAttr.FTCWorkModel,0, "工作模式", true, true, FTCAttr.FTCWorkModel, "@0=简洁模式@1=高级模式");

		map.AddTBFloat(FTCAttr.FTC_X, 5, "位置X", false, false);
		map.AddTBFloat(FTCAttr.FTC_Y, 5, "位置Y", false, false);

		map.AddTBFloat(FTCAttr.FTC_H, 300, "高度", true, false);
		map.AddTBFloat(FTCAttr.FTC_W, 400, "宽度", true, false);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 此处变更了 NodeSheet类中的，map 描述该部分也要变更.

		this._enMap = map;
		return this._enMap;
	}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}