package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.Template.*;
import BP.WF.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 轨迹图标组件
*/
public class FrmTrack extends Entity
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
	public final FrmTrackSta getFrmTrackSta()
	{
		return FrmTrackSta.forValue(this.GetValIntByKey(FrmTrackAttr.FrmTrackSta));
	}
	public final void setFrmTrackSta(FrmTrackSta value)
	{
		this.SetValByKey(FrmTrackAttr.FrmTrackSta, value.getValue());
	}
	/** 
	 Y
	*/
	public final float getFrmTrack_Y()
	{
		return this.GetValFloatByKey(FrmTrackAttr.FrmTrack_Y);
	}
	public final void setFrmTrack_Y(float value)
	{
		this.SetValByKey(FrmTrackAttr.FrmTrack_Y, value);
	}
	/** 
	 X
	*/
	public final float getFrmTrack_X()
	{
		return this.GetValFloatByKey(FrmTrackAttr.FrmTrack_X);
	}
	public final void setFrmTrack_X(float value)
	{
		this.SetValByKey(FrmTrackAttr.FrmTrack_X, value);
	}
	/** 
	 W
	*/
	public final float getFrmTrack_W()
	{
		return this.GetValFloatByKey(FrmTrackAttr.FrmTrack_W);
	}
	public final void setFrmTrack_W(float value)
	{
		this.SetValByKey(FrmTrackAttr.FrmTrack_W, value);
	}
	public final String getFrmTrack_Wstr()
	{
		if (this.getFrmTrack_W() == 0)
		{
			return "100%";
		}
		return this.getFrmTrack_W() + "px";
	}
	/** 
	 H
	*/
	public final float getFrmTrack_H()
	{
		return this.GetValFloatByKey(FrmTrackAttr.FrmTrack_H);
	}
	public final void setFrmTrack_H(float value)
	{
		this.SetValByKey(FrmTrackAttr.FrmTrack_H, value);
	}
	public final String getFrmTrack_Hstr()
	{
		if (this.getFrmTrack_H() == 0)
		{
			return "100%";
		}
		return this.getFrmTrack_H() + "px";
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
	public final String getFrmTrackLab()
	{
		return this.GetValStrByKey(FrmTrackAttr.FrmTrackLab);
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
	 轨迹图标组件
	*/
	public FrmTrack()
	{
	}
	/** 
	 轨迹图标组件
	 
	 @param no
	*/
	public FrmTrack(String mapData)
	{
		if (mapData.contains("ND") == false)
		{
			this.setFrmTrackSta(FrmTrackSta.Disable);
			return;
		}

		String mapdata = mapData.replace("ND", "");
		if (DataType.IsNumStr(mapdata) == false)
		{
			this.setFrmTrackSta(FrmTrackSta.Disable);
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
	 轨迹图标组件
	 
	 @param no
	*/
	public FrmTrack(int nodeID)
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

		Map map = new Map("WF_Node", "轨迹图标组件");

		map.AddTBIntPK(NodeAttr.NodeID, 0, "节点ID", true, true);
		map.AddTBString(NodeAttr.Name, null, "节点名称", true, true, 0, 100, 10);
		map.AddTBString(FrmTrackAttr.FrmTrackLab, "轨迹", "显示标签", true, false, 0, 200, 10, false);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 此处变更了 NodeSheet类中的，map 描述该部分也要变更.

		map.AddDDLSysEnum(FrmTrackAttr.FrmTrackSta, getFrmTrackSta().Disable.getValue(), "组件状态", true, true, FrmTrackAttr.FrmTrackSta, "@0=禁用@1=显示轨迹图@2=显示轨迹表");

		map.AddTBFloat(FrmTrackAttr.FrmTrack_X, 5, "位置X", false, false);
		map.AddTBFloat(FrmTrackAttr.FrmTrack_Y, 5, "位置Y", false, false);

		map.AddTBFloat(FrmTrackAttr.FrmTrack_H, 300, "高度", true, false);
		map.AddTBFloat(FrmTrackAttr.FrmTrack_W, 400, "宽度", true, false);

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