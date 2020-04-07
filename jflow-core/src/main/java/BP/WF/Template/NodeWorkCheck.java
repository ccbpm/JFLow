package BP.WF.Template;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.En.Map;
import BP.WF.*;

/** 
 审核组件
*/
public class NodeWorkCheck extends Entity
{

		///#region 属性
	/** 
	 节点编号
	 * @throws Exception 
	*/
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
	 状态
	 * @throws Exception 
	*/
	public final FrmWorkCheckSta getHisFrmWorkCheckSta() throws Exception
	{
		return FrmWorkCheckSta.forValue(this.GetValIntByKey(NodeWorkCheckAttr.FWCSta));
	}
	public final void setHisFrmWorkCheckSta(FrmWorkCheckSta value) throws Exception
	{
		this.SetValByKey(NodeWorkCheckAttr.FWCSta, value.getValue());
	}
	/** 
	 显示格式(0=表格,1=自由.)
	 * @throws Exception 
	*/
	public final FrmWorkShowModel getHisFrmWorkShowModel() throws Exception
	{
		return FrmWorkShowModel.forValue(this.GetValIntByKey(NodeWorkCheckAttr.FWCShowModel));
	}
	public final void setHisFrmWorkShowModel(FrmWorkShowModel value) throws Exception
	{
		this.SetValByKey(NodeWorkCheckAttr.FWCShowModel, value.getValue());
	}
	/** 
	 附件类型
	 * @throws Exception 
	*/
	public final FWCAth getFWCAth() throws Exception
	{
		return FWCAth.forValue(this.GetValIntByKey(NodeWorkCheckAttr.FWCAth));
	}
	public final void setFWCAth(FWCAth value) throws Exception
	{
		this.SetValByKey(NodeWorkCheckAttr.FWCAth, value.getValue());
	}
	/** 
	 组件类型
	 * @throws Exception 
	*/
	public final FWCType getHisFrmWorkCheckType() throws Exception
	{
		return FWCType.forValue(this.GetValIntByKey(NodeWorkCheckAttr.FWCType));
	}
	public final void setHisFrmWorkCheckType(FWCType value) throws Exception
	{
		this.SetValByKey(NodeWorkCheckAttr.FWCType, value.getValue());
	}
	/** 
	 标签
	 * @throws Exception 
	*/
	public final String getFWCLab() throws Exception
	{
		return this.GetValStrByKey(NodeWorkCheckAttr.FWCLab);
	}
	/** 
	 组件类型名称
	 * @throws Exception 
	*/
	public final String getFWCTypeT() throws Exception
	{
		return this.GetValRefTextByKey(NodeWorkCheckAttr.FWCType);
	}
	/** 
	 Y
	 * @throws Exception 
	*/
	public final float getFWC_Y() throws Exception
	{
		return this.GetValFloatByKey(NodeWorkCheckAttr.FWC_Y);
	}
	public final void setFWC_Y(float value) throws Exception
	{
		this.SetValByKey(NodeWorkCheckAttr.FWC_Y, value);
	}
	/** 
	 X
	 * @throws Exception 
	*/
	public final float getFWC_X() throws Exception
	{
		return this.GetValFloatByKey(NodeWorkCheckAttr.FWC_X);
	}
	public final void setFWC_X(float value) throws Exception
	{
		this.SetValByKey(NodeWorkCheckAttr.FWC_X, value);
	}
	/** 
	 W
	 * @throws Exception 
	*/
	public final float getFWC_W() throws Exception
	{
		return this.GetValFloatByKey(NodeWorkCheckAttr.FWC_W);
	}
	public final void setFWC_W(float value) throws Exception
	{
		this.SetValByKey(NodeWorkCheckAttr.FWC_W, value);
	}
	public final String getFWC_Wstr() throws Exception
	{
		if (this.getFWC_W() == 0)
		{
			return "100%";
		}
		return this.getFWC_W() + "px";
	}
	/** 
	 H
	 * @throws Exception 
	*/
	public final float getFWC_H() throws Exception
	{
		return this.GetValFloatByKey(NodeWorkCheckAttr.FWC_H);
	}
	public final void setFWC_H(float value) throws Exception
	{
		this.SetValByKey(NodeWorkCheckAttr.FWC_H, value);
	}
	public final String getFWC_Hstr() throws Exception
	{
		if (this.getFWC_H() == 0)
		{
			return "100%";
		}
		return this.getFWC_H() + "px";
	}
	/** 
	 轨迹图是否显示?
	 * @throws Exception 
	*/
	public final boolean getFWCTrackEnable() throws Exception
	{
		return this.GetValBooleanByKey(NodeWorkCheckAttr.FWCTrackEnable);
	}
	public final void setFWCTrackEnable(boolean value) throws Exception
	{
		this.SetValByKey(NodeWorkCheckAttr.FWCTrackEnable, value);
	}
	/** 
	 历史审核信息是否显示?
	 * @throws Exception 
	*/
	public final boolean getFWCListEnable() throws Exception
	{
		return this.GetValBooleanByKey(NodeWorkCheckAttr.FWCListEnable);
	}
	public final void setFWCListEnable(boolean value) throws Exception
	{
		this.SetValByKey(NodeWorkCheckAttr.FWCListEnable, value);
	}
	/** 
	 在轨迹表里是否显示所有的步骤？
	 * @throws Exception 
	*/
	public final boolean getFWCIsShowAllStep() throws Exception
	{
		return this.GetValBooleanByKey(NodeWorkCheckAttr.FWCIsShowAllStep);
	}
	public final void setFWCIsShowAllStep(boolean value) throws Exception
	{
		this.SetValByKey(NodeWorkCheckAttr.FWCIsShowAllStep, value);
	}
	/** 
	 是否显示轨迹在没有走到的节点
	 * @throws Exception 
	*/
	public final boolean getFWCIsShowTruck() throws Exception
	{
		return this.GetValBooleanByKey(NodeWorkCheckAttr.FWCIsShowTruck);
	}
	public final void setFWCIsShowTruck(boolean value) throws Exception
	{
		this.SetValByKey(NodeWorkCheckAttr.FWCIsShowTruck, value);
	}
	/** 
	 是否显示退回信息？
	 * @throws Exception 
	*/
	public final boolean getFWCIsShowReturnMsg() throws Exception
	{
		return this.GetValBooleanByKey(NodeWorkCheckAttr.FWCIsShowReturnMsg);
	}
	public final void setFWCIsShowReturnMsg(boolean value) throws Exception
	{
		this.SetValByKey(NodeWorkCheckAttr.FWCIsShowReturnMsg, value);
	}
	/** 
	 如果用户未审核是否按照默认意见填充?
	 * @throws Exception 
	*/
	public final boolean getFWCIsFullInfo() throws Exception
	{
		return this.GetValBooleanByKey(NodeWorkCheckAttr.FWCIsFullInfo);
	}
	public final void setFWCIsFullInfo(boolean value) throws Exception
	{
		this.SetValByKey(NodeWorkCheckAttr.FWCIsFullInfo, value);
	}
	/** 
	 默认审核信息
	 * @throws Exception 
	*/
	public final String getFWCDefInfo() throws Exception
	{
		return this.GetValStringByKey(NodeWorkCheckAttr.FWCDefInfo);
	}
	public final void setFWCDefInfo(String value) throws Exception
	{
		this.SetValByKey(NodeWorkCheckAttr.FWCDefInfo, value);
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
	 节点意见名称，如果为空则取节点名称.
	 * @throws Exception 
	*/
	public final String getFWCNodeName() throws Exception
	{
		String str = this.GetValStringByKey(NodeWorkCheckAttr.FWCNodeName);
		if (DataType.IsNullOrEmpty(str))
		{
			return this.getName();
		}
		return str;
	}
	/** 
	 操作名词(审核，审定，审阅，批示)
	 * @throws Exception 
	*/
	public final String getFWCOpLabel() throws Exception
	{
		return this.GetValStringByKey(NodeWorkCheckAttr.FWCOpLabel);
	}
	public final void setFWCOpLabel(String value) throws Exception
	{
		this.SetValByKey(NodeWorkCheckAttr.FWCOpLabel, value);
	}
	/** 
	 操作字段
	 * @throws Exception 
	*/
	public final String getFWCFields() throws Exception
	{
		return this.GetValStringByKey(NodeWorkCheckAttr.FWCFields);
	}
	public final void setFWCFields(String value) throws Exception
	{
		this.SetValByKey(NodeWorkCheckAttr.FWCFields, value);
	}
	/**
	 自定义常用短语
	 * @throws Exception
	 */
	public final String getFWCNewDuanYu() throws Exception
	{
		return this.GetValStringByKey(NodeWorkCheckAttr.FWCNewDuanYu);
	}
	public final void setFWCNewDuanYu(String value) throws Exception
	{
		this.SetValByKey(NodeWorkCheckAttr.FWCNewDuanYu, value);
	}
	/** 
	 是否显示数字签名？
	 * @throws Exception 
	*/
	public final boolean getSigantureEnabel() throws Exception
	{
		return this.GetValBooleanByKey(NodeWorkCheckAttr.SigantureEnabel);
	}
	public final void setSigantureEnabel(boolean value) throws Exception
	{
		this.SetValByKey(NodeWorkCheckAttr.SigantureEnabel, value);
	}

	/** 
	 协作模式下操作员显示顺序
	 * @throws Exception 
	*/
	public final FWCOrderModel getFWCOrderModel() throws Exception
	{
		return FWCOrderModel.forValue(this.GetValIntByKey(NodeWorkCheckAttr.FWCOrderModel, 0));
	}
	public final void setFWCOrderModel(FWCOrderModel value) throws Exception
	{
		this.SetValByKey(NodeWorkCheckAttr.FWCOrderModel, value.getValue());
	}
	/** 
	 审核组件状态
	 * @throws Exception 
	*/
	public final FrmWorkCheckSta getFWCSta() throws Exception
	{
		return FrmWorkCheckSta.forValue(this.GetValIntByKey(NodeWorkCheckAttr.FWCSta, 0));
	}
	public final void setFWCSta(FrmWorkCheckSta value) throws Exception
	{
		this.SetValByKey(NodeWorkCheckAttr.FWCSta, value.getValue());
	}

	public final int getFWCVer() throws Exception
	{
		return this.GetValIntByKey(NodeWorkCheckAttr.FWCVer, 0);
	}
	public final void setFWCVer(int value) throws Exception
	{
		this.SetValByKey(NodeWorkCheckAttr.FWCVer,value);
	}
	public final String getFWCView() throws Exception
	{
		return this.GetValStringByKey(NodeWorkCheckAttr.FWCView);
	}
	public final void setFWCView(String value) throws Exception
	{
		this.SetValByKey(NodeWorkCheckAttr.FWCView, value);
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
	 审核组件
	*/
	public NodeWorkCheck()
	{
	}
	/** 
	 审核组件
	 
	 @param mapData
	 * @throws Exception 
	*/
	public NodeWorkCheck(String mapData) throws Exception
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
	 
	 @param nodeID
	 * @throws Exception 
	*/
	public NodeWorkCheck(int nodeID) throws Exception
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

		Map map = new Map("WF_Node", "审核组件");
		map.Java_SetEnType(EnType.Sys);



		map.AddTBIntPK(NodeAttr.NodeID, 0, "节点ID", true, true);
		map.AddTBString(NodeAttr.Name, null, "节点名称", true, true, 0, 100, 10);
		map.AddTBString(NodeWorkCheckAttr.FWCLab, "审核信息", "显示标签", true, false, 0, 100, 10, true);


			///#region 此处变更了 NodeSheet类中的，map 描述该部分也要变更.
		map.AddDDLSysEnum(NodeWorkCheckAttr.FWCSta, FrmWorkCheckSta.Disable.getValue(), "审核组件状态", true, true, NodeWorkCheckAttr.FWCSta, "@0=禁用@1=启用@2=只读");
		map.AddDDLSysEnum(NodeWorkCheckAttr.FWCShowModel, FrmWorkShowModel.Free.getValue(), "显示方式", true, true, NodeWorkCheckAttr.FWCShowModel, "@0=表格方式@1=自由模式"); //此属性暂时没有用.

		map.AddDDLSysEnum(NodeWorkCheckAttr.FWCType, FWCType.Check.getValue(), "审核组件", true, true, NodeWorkCheckAttr.FWCType, "@0=审核组件@1=日志组件@2=周报组件@3=月报组件");

		map.AddTBString(NodeWorkCheckAttr.FWCNodeName, null, "节点意见名称", true, false, 0, 100, 10);

		map.AddDDLSysEnum(NodeWorkCheckAttr.FWCAth, FWCAth.None.getValue(), "附件上传", true, true, NodeWorkCheckAttr.FWCAth, "@0=不启用@1=多附件@2=单附件(暂不支持)@3=图片附件(暂不支持)");
		map.SetHelperAlert(NodeWorkCheckAttr.FWCAth, "在审核期间，是否启用上传附件？启用什么样的附件？注意：附件的属性在节点表单里配置。"); //使用alert的方式显示帮助信息.

		map.AddBoolean(NodeWorkCheckAttr.FWCTrackEnable, true, "轨迹图是否显示？", true, true, false);

		map.AddBoolean(NodeWorkCheckAttr.FWCListEnable, true, "历史审核信息是否显示？(否,仅出现意见框)", true, true, true);
		map.AddBoolean(NodeWorkCheckAttr.FWCIsShowAllStep, false, "在轨迹表里是否显示所有的步骤？", true, true);

		map.AddTBString(NodeWorkCheckAttr.FWCOpLabel, "审核", "操作名词(审核/审阅/批示)", true, false, 0, 50, 10);
		map.AddTBString(NodeWorkCheckAttr.FWCDefInfo, "同意", "默认审核信息", true, false, 0, 50, 10);
		map.AddBoolean(NodeWorkCheckAttr.SigantureEnabel, false, "操作人是否显示为图片签名？", true, true);
		map.AddBoolean(NodeWorkCheckAttr.FWCIsFullInfo, true, "如果用户未审核是否按照默认意见填充？", true, true, true);


		map.AddTBFloat(NodeWorkCheckAttr.FWC_X, 300, "位置X", true, false);
		map.AddTBFloat(NodeWorkCheckAttr.FWC_Y, 500, "位置Y", true, false);

		map.AddTBFloat(NodeWorkCheckAttr.FWC_H, 300, "高度(0=100%)", true, false);
		map.AddTBFloat(NodeWorkCheckAttr.FWC_W, 400, "宽度(0=100%)", true, false);

		map.AddTBString(NodeWorkCheckAttr.FWCFields, null, "审批格式字段", true, false, 0, 50, 10, true);
		map.AddTBString(NodeWorkCheckAttr.FWCNewDuanYu, null, "自定义常用短语(使用@分隔)", true, false, 0, 100, 10, true);
		map.AddBoolean(NodeWorkCheckAttr.FWCIsShowTruck, false, "是否显示未审核的轨迹？", true, true, true);
		map.AddBoolean(NodeWorkCheckAttr.FWCIsShowReturnMsg, false, "是否显示退回信息？", true, true, true);

			//增加如下字段是为了查询与排序的需要.
		map.AddTBString(NodeAttr.FK_Flow, null, "流程编号", false, false, 0, 3, 10);
		map.AddTBInt(NodeAttr.Step, 0, "步骤", false, false);


			//协作模式下审核人显示顺序. add for yantai zongheng.
		map.AddDDLSysEnum(NodeWorkCheckAttr.FWCOrderModel, 0, "协作模式下操作员显示顺序", true, true, NodeWorkCheckAttr.FWCOrderModel, "@0=按审批时间先后排序@1=按照接受人员列表先后顺序(官职大小)");

			//for tianye , 多人审核的时候，不让其看到其他人的意见.
		map.AddDDLSysEnum(NodeWorkCheckAttr.FWCMsgShow, 0, "审核意见显示方式", true, true, NodeWorkCheckAttr.FWCMsgShow, "@0=都显示@1=仅显示自己的意见");

		map.AddDDLSysEnum(NodeWorkCheckAttr.FWCVer, 0, "审核意见版本号", true, true, NodeWorkCheckAttr.FWCVer, "@0=2018@1=2019");
		String sql = "SELECT KeyOfEn AS No,Name From Sys_MapAttr Where UIContralType=14 AND FK_MapData='ND@NodeID'";
		map.AddDDLSQL(NodeWorkCheckAttr.CheckField, null, "签批字段", sql, true);
		map.AddTBString(NodeWorkCheckAttr.FWCView, null, "审核意见立场", true, false, 20, 200, 200,true);


			///#endregion 此处变更了 NodeSheet类中的，map 描述该部分也要变更.

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		if (this.getFWCAth() == FWCAth.MinAth)
		{
			FrmAttachment workCheckAth = new FrmAttachment();
			boolean isHave = workCheckAth.RetrieveByAttr(FrmAttachmentAttr.MyPK, this.getNodeID() + "_FrmWorkCheck");
			//不包含审核组件
			if (isHave == false)
			{
				workCheckAth = new FrmAttachment();
				/*如果没有查询到它,就有可能是没有创建.*/
				workCheckAth.setMyPK("ND" + this.getNodeID() + "_FrmWorkCheck");
				workCheckAth.setFK_MapData("ND" + String.valueOf(this.getNodeID()));
				workCheckAth.setNoOfObj("FrmWorkCheck");
				workCheckAth.setExts("*.*");

				//存储路径.
				workCheckAth.setSaveTo("/DataUser/UploadFile/");
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
		}
		return super.beforeUpdateInsertAction();
	}

	@Override
	protected void afterInsertUpdateAction() throws Exception
	{
		Node fl = new Node();
		fl.setNodeID(this.getNodeID());
		fl.RetrieveFromDBSources();
		fl.Update();

		super.afterInsertUpdateAction();
	}
}