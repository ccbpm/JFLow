package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
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

		///#region 属性
	public final String getNo() throws Exception
	{
		return "ND" + this.getNodeID();
	}
	public final void setNo(String value) throws NumberFormatException, Exception
	{
		String nodeID = value.replace("ND", "");
		this.setNodeID(Integer.parseInt(nodeID));
	}
	/** 
	 节点ID
	 * @throws Exception 
	*/
	public final int getNodeID() throws Exception
	{
		return this.GetValIntByKey(NodeAttr.NodeID);
	}
	public final void setNodeID(int value) throws Exception
	{
		this.SetValByKey(NodeAttr.NodeID, value);
	}
	/** 
	 控件状态
	 * @throws Exception 
	*/
	public final FTCSta getFTCSta() throws Exception
	{
		return FTCSta.forValue(this.GetValIntByKey(FTCAttr.FTCSta));
	}
	public final void setFTCSta(FTCSta value) throws Exception
	{
		this.SetValByKey(FTCAttr.FTCSta, value.getValue());
	}
	/** 
	 工作模式
	 * @throws Exception 
	*/
	public final int getFTCWorkModel() throws Exception
	{
		return this.GetValIntByKey(FTCAttr.FTCWorkModel);
	}
	public final void setFTCWorkModel(int value) throws Exception
	{
		this.SetValByKey(FTCAttr.FTCWorkModel, value);
	}
	/** 
	 Y
	 * @throws Exception 
	*/
	public final float getFTC_Y() throws Exception
	{
		return this.GetValFloatByKey(FTCAttr.FTC_Y);
	}
	public final void setFTC_Y(float value) throws Exception
	{
		this.SetValByKey(FTCAttr.FTC_Y, value);
	}
	/** 
	 X
	 * @throws Exception 
	*/
	public final float getFTC_X() throws Exception
	{
		return this.GetValFloatByKey(FTCAttr.FTC_X);
	}
	public final void setFTC_X(float value) throws Exception
	{
		this.SetValByKey(FTCAttr.FTC_X, value);
	}
	/** 
	 W
	 * @throws Exception 
	*/
	public final float getFTC_W() throws Exception
	{
		return this.GetValFloatByKey(FTCAttr.FTC_W);
	}
	public final void setFTC_W(float value) throws Exception
	{
		this.SetValByKey(FTCAttr.FTC_W, value);
	}
	public final String getFTC_Wstr() throws Exception
	{
		if (this.getFTC_W() == 0)
		{
			return "100%";
		}
		return this.getFTC_W() + "px";
	}
	/** 
	 H
	 * @throws Exception 
	*/
	public final float getFTC_H() throws Exception
	{
		return this.GetValFloatByKey(FTCAttr.FTC_H);
	}
	public final void setFTC_H(float value) throws Exception
	{
		this.SetValByKey(FTCAttr.FTC_H, value);
	}
	public final String getFTC_Hstr() throws Exception
	{
		if (this.getFTC_H() == 0)
		{
			return "100%";
		}
		return this.getFTC_H() + "px";
	}
	/** 
	 节点名称.
	 * @throws Exception 
	*/
	public final String getName() throws Exception
	{
		return this.GetValStringByKey("Name");
	}
	/** 
	 显示标签
	 * @throws Exception 
	*/
	public final String getFTCLab() throws Exception
	{
		return this.GetValStrByKey(FTCAttr.FTCLab);
	}

		///#endregion


		///#region 构造方法
	/** 
	 控制
	 * @throws Exception 
	*/
	@Override
	public UAC getHisUAC() throws Exception
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
	 * @throws Exception 
	*/
	public FrmTransferCustom(String mapData) throws Exception
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
	 * @throws Exception 
	*/
	public FrmTransferCustom(int nodeID) throws Exception
	{
		this.setNodeID(nodeID);
		this.Retrieve();
	}
	/** 
	 EnMap
	 * @throws Exception 
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_Node", "流转自定义组件");

		map.AddTBIntPK(NodeAttr.NodeID, 0, "节点ID", true, true);
		map.AddTBString(NodeAttr.Name, null, "节点名称", true, true, 0, 100, 10);
		map.AddTBString(FTCAttr.FTCLab, "流转自定义", "显示标签", true, false, 0, 50, 10, true);



			///#region 此处变更了 NodeSheet类中的，map 描述该部分也要变更.

		map.AddDDLSysEnum(FTCAttr.FTCSta, FTCSta.Disable.getValue(), "组件状态", true, true, FTCAttr.FTCSta, "@0=禁用@1=只读@2=可设置人员");

		map.AddDDLSysEnum(FTCAttr.FTCWorkModel,0, "工作模式", true, true, FTCAttr.FTCWorkModel, "@0=简洁模式@1=高级模式");

		map.AddTBFloat(FTCAttr.FTC_X, 5, "位置X", false, false);
		map.AddTBFloat(FTCAttr.FTC_Y, 5, "位置Y", false, false);

		map.AddTBFloat(FTCAttr.FTC_H, 300, "高度", true, false);
		map.AddTBFloat(FTCAttr.FTC_W, 400, "宽度", true, false);


			///#endregion 此处变更了 NodeSheet类中的，map 描述该部分也要变更.

		this.set_enMap(map);
		return this.get_enMap();
	}



		///#endregion
}