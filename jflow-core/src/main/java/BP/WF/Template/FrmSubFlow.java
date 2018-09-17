package BP.WF.Template;

import BP.DA.DataType;
import BP.En.Entity;
import BP.En.Map;
import BP.En.UAC;

/** 
 父子流程
 
*/
public class FrmSubFlow extends Entity
{

		
	/** 
	 标签
	 
	*/
	public final String getSFLab()
	{
		return this.GetValStringByKey(FrmSubFlowAttr.SFLab);
	}
	/** 
	 编号
	 
	*/
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
	 可触发的子流程
	 
	*/
	public final String getSFActiveFlows()
	{
		return this.GetValStringByKey(NodeAttr.SFActiveFlows);
	}
	public final void setSFActiveFlows(String value)
	{
		this.SetValByKey(NodeAttr.SFActiveFlows, value);
	}
	/** 
	 字段列
	 
	*/
	public final String getSFFields()
	{
		return this.GetValStringByKey(FrmSubFlowAttr.SFFields);
	}
	public final void setSFFields(String value)
	{
		this.SetValByKey(FrmSubFlowAttr.SFFields, value);
	}
	/** 
	 状态
	 
	*/
	public final FrmSubFlowSta getHisFrmSubFlowSta()
	{
		return FrmSubFlowSta.forValue(this.GetValIntByKey(FrmSubFlowAttr.SFSta));
	}
	public final void setHisFrmSubFlowSta(FrmSubFlowSta value)
	{
		this.SetValByKey(FrmSubFlowAttr.SFSta, value.getValue());
	}
	/** 
	 显示控制方式
	 
	*/
	public final SFShowCtrl getSFShowCtrl()
	{
		return SFShowCtrl.forValue(this.GetValIntByKey(FrmSubFlowAttr.SFShowCtrl));
	}
	public final void setSFShowCtrl(SFShowCtrl value)
	{
		this.SetValByKey(FrmSubFlowAttr.SFShowCtrl, value.getValue());
	}
	/** 
	 显示格式(0=表格,1=自由.)
	 
	*/
	public final FrmWorkShowModel getHisFrmWorkShowModel()
	{
		return FrmWorkShowModel.forValue(this.GetValIntByKey(FrmSubFlowAttr.SFShowModel));
	}
	public final void setHisFrmWorkShowModel(FrmWorkShowModel value)
	{
		this.SetValByKey(FrmSubFlowAttr.SFShowModel, value.getValue());
	}
	/** 
	 控件状态
	 
	*/
	public final FrmSubFlowSta getSFSta()
	{
		return FrmSubFlowSta.forValue(this.GetValIntByKey(FrmSubFlowAttr.SFSta));
	}
	public final void setSFSta(FrmSubFlowSta value)
	{
		this.SetValByKey(FrmSubFlowAttr.SFSta, value.getValue());
	}
	/** 
	 显示方式
	 
	*/
	public final FrmWorkShowModel getSFShowModel()
	{
		return FrmWorkShowModel.forValue(this.GetValIntByKey(FrmSubFlowAttr.SFShowModel));
	}
	public final void setSFShowModel(FrmWorkShowModel value)
	{
		this.SetValByKey(FrmSubFlowAttr.SFShowModel, value.getValue());
	}
	/** 
	 Y
	 
	*/
	public final float getSF_Y()
	{
		return this.GetValFloatByKey(FrmSubFlowAttr.SF_Y);
	}
	public final void setSF_Y(float value)
	{
		this.SetValByKey(FrmSubFlowAttr.SF_Y, value);
	}
	/** 
	 X
	 
	*/
	public final float getSF_X()
	{
		return this.GetValFloatByKey(FrmSubFlowAttr.SF_X);
	}
	public final void setSF_X(float value)
	{
		this.SetValByKey(FrmSubFlowAttr.SF_X, value);
	}
	/** 
	 打开类型
	 
	*/
	public final int getSFOpenType()
	{
		return this.GetValIntByKey(FrmSubFlowAttr.SFOpenType);
	}
	public final void setSFOpenType(int value)
	{
		this.SetValByKey(FrmSubFlowAttr.SFOpenType, value);
	}
	/** 
	 W
	 
	*/
	public final float getSF_W()
	{
		return this.GetValFloatByKey(FrmSubFlowAttr.SF_W);
	}
	public final void setSF_W(float value)
	{
		this.SetValByKey(FrmSubFlowAttr.SF_W, value);
	}
	public final String getSF_Wstr()
	{
		if (this.getSF_W() == 0)
		{
			return "100%";
		}
		return this.getSF_W() + "px";
	}
	/** 
	 H
	 
	*/
	public final float getSF_H()
	{
		return this.GetValFloatByKey(FrmSubFlowAttr.SF_H);
	}
	public final void setSF_H(float value)
	{
		this.SetValByKey(FrmSubFlowAttr.SF_H, value);
	}
	public final String getSF_Hstr()
	{
		if (this.getSF_H() == 0)
		{
			return "100%";
		}
		return this.getSF_H() + "px";
	}
	/** 
	 轨迹图是否显示?
	 
	*/
	public final boolean getSFTrackEnable()
	{
		return this.GetValBooleanByKey(FrmSubFlowAttr.SFTrackEnable);
	}
	public final void setSFTrackEnable(boolean value)
	{
		this.SetValByKey(FrmSubFlowAttr.SFTrackEnable, value);
	}
	/** 
	 历史审核信息是否显示?
	 
	*/
	public final boolean getSFListEnable()
	{
		return this.GetValBooleanByKey(FrmSubFlowAttr.SFListEnable);
	}
	public final void setSFListEnable(boolean value)
	{
		this.SetValByKey(FrmSubFlowAttr.SFListEnable, value);
	}
	/** 
	 在轨迹表里是否显示所有的步骤？
	 
	*/
	public final boolean getSFIsShowAllStep()
	{
		return this.GetValBooleanByKey(FrmSubFlowAttr.SFIsShowAllStep);
	}
	public final void setSFIsShowAllStep(boolean value)
	{
		this.SetValByKey(FrmSubFlowAttr.SFIsShowAllStep, value);
	}
	/** 
	 如果用户未审核是否按照默认意见填充?
	 
	*/
	public final boolean getSFIsFullInfo()
	{
		return this.GetValBooleanByKey(FrmSubFlowAttr.SFIsFullInfo);
	}
	public final void setSFIsFullInfo(boolean value)
	{
		this.SetValByKey(FrmSubFlowAttr.SFIsFullInfo, value);
	}
	/** 
	 默认审核信息
	 
	*/
	public final String getSFDefInfo()
	{
		return this.GetValStringByKey(FrmSubFlowAttr.SFDefInfo);
	}
	public final void setSFDefInfo(String value)
	{
		this.SetValByKey(FrmSubFlowAttr.SFDefInfo, value);
	}
	/** 
	 节点名称.
	 
	*/
	public final String getName()
	{
		return this.GetValStringByKey("Name");
	}
	/** 
	 标题，如果为空则取节点名称.
	 
	*/
	public final String getSFCaption()
	{
		String str= this.GetValStringByKey(FrmSubFlowAttr.SFCaption);
		if (str.equals(""))
		{
			str = "启动子流程";
		}
		return str;
	}
	public final void setSFCaption(String value)
	{
		this.SetValByKey(FrmSubFlowAttr.SFCaption, value);
	}
	/** 
	 操作名词(审核，审定，审阅，批示)
	 
	*/
	public final String getSFOpLabel()
	{
		return this.GetValStringByKey(FrmSubFlowAttr.SFOpLabel);
	}
	public final void setSFOpLabel(String value)
	{
		this.SetValByKey(FrmSubFlowAttr.SFOpLabel, value);
	}
	/** 
	 是否显示数字签名？
	 
	*/
	public final boolean getSigantureEnabel()
	{
		return this.GetValBooleanByKey(FrmSubFlowAttr.SigantureEnabel);
	}
	public final void setSigantureEnabel(boolean value)
	{
		this.SetValByKey(FrmSubFlowAttr.SigantureEnabel, value);
	}

		///#endregion


		
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
	 父子流程
	 
	*/
	public FrmSubFlow()
	{
	}
	/** 
	 父子流程
	 
	 @param no
	 * @throws Exception 
	*/
	public FrmSubFlow(String mapData) throws Exception
	{
		if (mapData.contains("ND") == false)
		{
			this.setHisFrmSubFlowSta(FrmSubFlowSta.Disable);
			return;
		}

		String mapdata = mapData.replace("ND", "");
		if (DataType.IsNumStr(mapdata) == false)
		{
			this.setHisFrmSubFlowSta(FrmSubFlowSta.Disable);
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
	 父子流程
	 
	 @param no
	 * @throws Exception 
	*/
	public FrmSubFlow(int nodeID) throws Exception
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
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_Node", "父子流程");

		map.AddTBIntPK(NodeAttr.NodeID, 0, "节点ID", true, true);
		map.AddTBString(NodeAttr.Name, null, "节点名称", true, true, 0, 100, 10);
		map.AddTBString(FrmSubFlowAttr.SFLab, "子流程", "显示标签", true, false, 0, 200, 10, true);


			///#region 此处变更了 NodeSheet类中的，map 描述该部分也要变更.

		map.AddDDLSysEnum(FrmSubFlowAttr.SFSta, FrmSubFlowSta.Disable.getValue(), "父子流程状态", true, true, FrmSubFlowAttr.SFSta, "@0=禁用@1=启用@2=只读");

		map.AddDDLSysEnum(FrmSubFlowAttr.SFShowModel, FrmWorkShowModel.Free.getValue(), "显示方式", true, true, FrmSubFlowAttr.SFShowModel, "@0=表格方式@1=自由模式"); //此属性暂时没有用.

		map.AddTBString(FrmSubFlowAttr.SFCaption, "启动子流程", "连接标题", true, false, 0, 100, 10, true);
		map.AddTBString(FrmSubFlowAttr.SFDefInfo, null, "可启动的子流程", true, false, 0, 50, 10,true);
		map.AddTBString(FrmSubFlowAttr.SFActiveFlows, null, "可触发的子流程", true, false, 0, 50, 10, true);

		map.AddTBFloat(FrmSubFlowAttr.SF_X, 5, "位置X", true, false);
		map.AddTBFloat(FrmSubFlowAttr.SF_Y, 5, "位置Y", true, false);

		map.AddTBFloat(FrmSubFlowAttr.SF_H, 300, "高度", true, false);
		map.AddTBFloat(FrmSubFlowAttr.SF_W, 400, "宽度", true, false);

		map.AddTBString(FrmSubFlowAttr.SFFields, null, "审批格式字段", true, false, 0, 50, 10,true);

		map.AddDDLSysEnum(FrmSubFlowAttr.SFShowCtrl, 0, "显示控制方式", true, true, FrmSubFlowAttr.SFShowCtrl, "@0=可以看所有的子流程@1=仅仅可以看自己发起的子流程"); //此属性暂时没有用.

		map.AddDDLSysEnum(FrmSubFlowAttr.SFOpenType,0, "打开子流程显示", true, true, FrmSubFlowAttr.SFOpenType, "@0=工作查看器@1=傻瓜表单轨迹查看器"); //此属性暂时没有用.




			///#endregion 此处变更了 NodeSheet类中的，map 描述该部分也要变更.

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

 
}