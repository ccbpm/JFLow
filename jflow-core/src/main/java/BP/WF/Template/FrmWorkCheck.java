package BP.WF.Template;

import BP.DA.DataType;
import BP.En.EnType;
import BP.En.Entity;
import BP.En.Map;
import BP.En.UAC;
import BP.Sys.AttachmentUploadType;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachmentAttr;
import BP.Tools.StringHelper;

/** 
 审核组件
 
*/
public class FrmWorkCheck extends Entity
{

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
	 状态
	 
	*/
	public final FrmWorkCheckSta getHisFrmWorkCheckSta()
	{
		return FrmWorkCheckSta.forValue(this.GetValIntByKey(FrmWorkCheckAttr.FWCSta));
	}
	public final void setHisFrmWorkCheckSta(FrmWorkCheckSta value)
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCSta, value.getValue());
	}
	/** 
	 显示格式(0=表格,1=自由.)
	 
	*/
	public final FrmWorkShowModel getHisFrmWorkShowModel()
	{
		return FrmWorkShowModel.forValue(this.GetValIntByKey(FrmWorkCheckAttr.FWCShowModel));
	}
	public final void setHisFrmWorkShowModel(FrmWorkShowModel value)
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCShowModel, value.getValue());
	}
	/** 
	 附件类型
	 
	*/
	public final FWCAth getFWCAth()
	{
		return FWCAth.forValue(this.GetValIntByKey(FrmWorkCheckAttr.FWCAth));
	}
	public final void setFWCAth(FWCAth value)
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCAth, value.getValue());
	}
	/** 
	 组件类型
	 
	*/
	public final FWCType getHisFrmWorkCheckType()
	{
		return FWCType.forValue(this.GetValIntByKey(FrmWorkCheckAttr.FWCType));
	}
	public final void setHisFrmWorkCheckType(FWCType value)
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCType, value.getValue());
	}
	/** 
	 标签
	 
	*/
	public final String getFWCLab()
	{
		return this.GetValStrByKey(FrmWorkCheckAttr.FWCLab);
	}
	/** 
	 组件类型名称
	 
	*/
	public final String getFWCTypeT()
	{
		return this.GetValRefTextByKey(FrmWorkCheckAttr.FWCType);
	}
	/** 
	 Y
	 
	*/
	public final float getFWC_Y()
	{
		return this.GetValFloatByKey(FrmWorkCheckAttr.FWC_Y);
	}
	public final void setFWC_Y(float value)
	{
		this.SetValByKey(FrmWorkCheckAttr.FWC_Y, value);
	}
	/** 
	 X
	 
	*/
	public final float getFWC_X()
	{
		return this.GetValFloatByKey(FrmWorkCheckAttr.FWC_X);
	}
	public final void setFWC_X(float value)
	{
		this.SetValByKey(FrmWorkCheckAttr.FWC_X, value);
	}
	/** 
	 W
	 
	*/
	public final float getFWC_W()
	{
		return this.GetValFloatByKey(FrmWorkCheckAttr.FWC_W);
	}
	public final void setFWC_W(float value)
	{
		this.SetValByKey(FrmWorkCheckAttr.FWC_W, value);
	}
	public final String getFWC_Wstr()
	{
		if (this.getFWC_W() == 0)
		{
			return "100%";
		}
		return this.getFWC_W() + "px";
	}
	/** 
	 H
	 
	*/
	public final float getFWC_H()
	{
		return this.GetValFloatByKey(FrmWorkCheckAttr.FWC_H);
	}
	public final void setFWC_H(float value)
	{
		this.SetValByKey(FrmWorkCheckAttr.FWC_H, value);
	}
	public final String getFWC_Hstr()
	{
		if (this.getFWC_H() == 0)
		{
			return "100%";
		}
		return this.getFWC_H() + "px";
	}
	/** 
	 轨迹图是否显示?
	 
	*/
	public final boolean getFWCTrackEnable()
	{
		return this.GetValBooleanByKey(FrmWorkCheckAttr.FWCTrackEnable);
	}
	public final void setFWCTrackEnable(boolean value)
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCTrackEnable, value);
	}
	/** 
	 历史审核信息是否显示?
	 
	*/
	public final boolean getFWCListEnable()
	{
		return this.GetValBooleanByKey(FrmWorkCheckAttr.FWCListEnable);
	}
	public final void setFWCListEnable(boolean value)
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCListEnable, value);
	}
	/** 
	 在轨迹表里是否显示所有的步骤？
	 
	*/
	public final boolean getFWCIsShowAllStep()
	{
		return this.GetValBooleanByKey(FrmWorkCheckAttr.FWCIsShowAllStep);
	}
	public final void setFWCIsShowAllStep(boolean value)
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCIsShowAllStep, value);
	}
	/** 
	 是否显示轨迹在没有走到的节点
	 
	*/
	public final boolean getFWCIsShowTruck()
	{
		return this.GetValBooleanByKey(FrmWorkCheckAttr.FWCIsShowTruck);
	}
	public final void setFWCIsShowTruck(boolean value)
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCIsShowTruck, value);
	}
	/** 
	 如果用户未审核是否按照默认意见填充?
	 
	*/
	public final boolean getFWCIsFullInfo()
	{
		return this.GetValBooleanByKey(FrmWorkCheckAttr.FWCIsFullInfo);
	}
	public final void setFWCIsFullInfo(boolean value)
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCIsFullInfo, value);
	}
	/** 
	 默认审核信息
	 
	*/
	public final String getFWCDefInfo()
	{
		return this.GetValStringByKey(FrmWorkCheckAttr.FWCDefInfo);
	}
	public final void setFWCDefInfo(String value)
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCDefInfo, value);
	}
	/** 
	 节点名称.
	 
	*/
	public final String getName()
	{
		return this.GetValStringByKey("Name");
	}
	/** 
	 节点意见名称，如果为空则取节点名称.
	 
	*/
	public final String getFWCNodeName()
	{
		String str = this.GetValStringByKey(FrmWorkCheckAttr.FWCNodeName);
		if (StringHelper.isNullOrEmpty(str))
		{
			return this.getName();
		}
		return str;
	}
	/** 
	 操作名词(审核，审定，审阅，批示)
	 
	*/
	public final String getFWCOpLabel()
	{
		return this.GetValStringByKey(FrmWorkCheckAttr.FWCOpLabel);
	}
	public final void setFWCOpLabel(String value)
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCOpLabel, value);
	}
	/** 
	 操作字段
	 
	*/
	public final String getFWCFields()
	{
		return this.GetValStringByKey(FrmWorkCheckAttr.FWCFields);
	}
	public final void setFWCFields(String value)
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCFields, value);
	}
	/** 
	 是否显示数字签名？
	 
	*/
	public final boolean getSigantureEnabel()
	{
		return this.GetValBooleanByKey(FrmWorkCheckAttr.SigantureEnabel);
	}
	public final void setSigantureEnabel(boolean value)
	{
		this.SetValByKey(FrmWorkCheckAttr.SigantureEnabel, value);
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
	 审核组件
	 
	*/
	public FrmWorkCheck()
	{
	}
	/** 
	 审核组件
	 
	 @param no
	 * @throws Exception 
	*/
	public FrmWorkCheck(String mapData) throws Exception
	{
		if (mapData.contains("ND") == false)
		{
			this.setHisFrmWorkCheckSta(FrmWorkCheckSta.Disable);
			return;
		}

		String mapdata = mapData.replace("ND", "");
		if (DataType.IsNumStr(mapdata) == false)
		{
			this.setHisFrmWorkCheckSta(FrmWorkCheckSta.Disable);
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
	 审核组件
	 
	 @param no
	 * @throws Exception 
	*/
	public FrmWorkCheck(int nodeID) throws Exception
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

		Map map = new Map("WF_Node", "审核组件");
		map.Java_SetEnType(EnType.Sys);

		map.AddTBIntPK(NodeAttr.NodeID, 0, "节点ID", true, true);
		map.AddTBString(NodeAttr.Name, null, "节点名称", true, true, 0, 100, 10);
		map.AddTBString(FrmWorkCheckAttr.FWCLab, "审核信息", "显示标签", true, false, 0, 200, 10, true);



			///#region 此处变更了 NodeSheet类中的，map 描述该部分也要变更.
		map.AddDDLSysEnum(FrmWorkCheckAttr.FWCSta, FrmWorkCheckSta.Disable.getValue(), "审核组件状态", true, true, FrmWorkCheckAttr.FWCSta, "@0=禁用@1=启用@2=只读");
		map.AddDDLSysEnum(FrmWorkCheckAttr.FWCShowModel, FrmWorkShowModel.Free.getValue(), "显示方式", true, true, FrmWorkCheckAttr.FWCShowModel, "@0=表格方式@1=自由模式"); //此属性暂时没有用.

		map.AddDDLSysEnum(FrmWorkCheckAttr.FWCType, FWCType.Check.getValue(), "审核组件", true, true, FrmWorkCheckAttr.FWCType, "@0=审核组件@1=日志组件@2=周报组件@3=月报组件");

		map.AddTBString(FrmWorkCheckAttr.FWCNodeName, null, "节点意见名称", true, false, 0, 100, 10);

		map.AddDDLSysEnum(FrmWorkCheckAttr.FWCAth, getFWCAth().None.getValue(), "附件上传", true, true, FrmWorkCheckAttr.FWCAth, "@0=不启用@1=多附件@2=单附件(暂不支持)@3=图片附件(暂不支持)");
		map.SetHelperAlert(FrmWorkCheckAttr.FWCAth, "在审核期间，是否启用上传附件？启用什么样的附件？注意：附件的属性在节点表单里配置。"); //使用alert的方式显示帮助信息.

		map.AddBoolean(FrmWorkCheckAttr.FWCTrackEnable, true, "轨迹图是否显示？", true, true, false);
		

		map.AddBoolean(FrmWorkCheckAttr.FWCListEnable, true, "历史审核信息是否显示？(否,仅出现意见框)", true, true, true);
		map.AddBoolean(FrmWorkCheckAttr.FWCIsShowAllStep, false, "在轨迹表里是否显示所有的步骤？", true, true);

		map.AddTBString(FrmWorkCheckAttr.FWCOpLabel, "审核", "操作名词(审核/审阅/批示)", true, false, 0, 50, 10);
		map.AddTBString(FrmWorkCheckAttr.FWCDefInfo, "同意", "默认审核信息", true, false, 0, 50, 10);
		map.AddBoolean(FrmWorkCheckAttr.SigantureEnabel, false, "操作人是否显示为图片签名？", true, true);
		map.AddBoolean(FrmWorkCheckAttr.FWCIsFullInfo, true, "如果用户未审核是否按照默认意见填充？", true, true, true);
		

		map.AddTBFloat(FrmWorkCheckAttr.FWC_X, 5, "位置X", false, false);
		map.AddTBFloat(FrmWorkCheckAttr.FWC_Y, 5, "位置Y", false, false);

		map.AddTBFloat(FrmWorkCheckAttr.FWC_H, 300, "高度(0=100%)", true, false);
		map.AddTBFloat(FrmWorkCheckAttr.FWC_W, 400, "宽度(0=100%)", true, false);

		map.AddTBString(FrmWorkCheckAttr.FWCFields, null, "审批格式字段", true, false, 0, 50, 10, true);
		map.AddBoolean(FrmWorkCheckAttr.FWCIsShowReturnMsg, false, "是否显示退回信息？", true, true, true);
		map.AddBoolean(FrmWorkCheckAttr.FWCIsShowTruck, false, "是否显示未审核的轨迹？", true, true, true);

			//增加如下字段是为了查询与排序的需要.
		map.AddTBString(NodeAttr.FK_Flow, null, "流程编号", false, false, 0, 3, 10);
		map.AddTBInt(NodeAttr.Step, 0, "步骤", false, false);

		//协作模式下审核人显示顺序. add for yantai zongheng.
        map.AddDDLSysEnum(FrmWorkCheckAttr.FWCOrderModel, 0, "协作模式下操作员显示顺序", true, true,
          FrmWorkCheckAttr.FWCOrderModel, "@0=按审批时间先后排序@1=按照接受人员列表先后顺序(官职大小)");
			///#endregion 此处变更了 NodeSheet类中的，map 描述该部分也要变更.

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		FrmAttachment workCheckAth = new FrmAttachment();
		boolean isHave = workCheckAth.RetrieveByAttr(FrmAttachmentAttr.MyPK, this.getNodeID() + "_FrmWorkCheck");
		//不包含审核组件
		if (isHave == false)
		{
			workCheckAth = new FrmAttachment();
			//如果没有查询到它,就有可能是没有创建.
			workCheckAth.setMyPK(this.getNodeID() + "_FrmWorkCheck");
			workCheckAth.setFK_MapData((new Integer(this.getNodeID())).toString());
			workCheckAth.setNoOfObj(this.getNodeID() + "_FrmWorkCheck");
			workCheckAth.setExts("*.*");

			//存储路径.
			//workCheckAth.setSaveTo("/DataUser/UploadFile/");
			
			workCheckAth.setIsNote(false); //不显示note字段.
			workCheckAth.setIsVisable(false); // 让其在form 上不可见.

			//位置.
			workCheckAth.setX((float)94.09);
			workCheckAth.setY((float)333.18);
			workCheckAth.setW((float)626.36);
			workCheckAth.setH((float)150);

			//多附件.
			workCheckAth.setUploadType(AttachmentUploadType.Multi);
			workCheckAth.setName("审核组件");
			workCheckAth.SetValByKey("AtPara", "@IsWoEnablePageset=1@IsWoEnablePrint=1@IsWoEnableViewModel=1@IsWoEnableReadonly=0@IsWoEnableSave=1@IsWoEnableWF=1@IsWoEnableProperty=1@IsWoEnableRevise=1@IsWoEnableIntoKeepMarkModel=1@FastKeyIsEnable=0@IsWoEnableViewKeepMark=1@FastKeyGenerRole=@IsWoEnableTemplete=1");
			workCheckAth.Insert();
		}
		return super.beforeUpdateInsertAction();
	}
	
	/** 协作模式下操作员显示顺序
	 
	*/
	public final FWCOrderModel getFWCOrderModel()
	{
		return FWCOrderModel.forValue(this.GetValIntByKey(FrmWorkCheckAttr.FWCOrderModel, 0));
	}
	public final void setFWCOrderModel(FWCOrderModel value)
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCOrderModel, value.getValue());
	}
}