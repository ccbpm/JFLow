package BP.WF.Template;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.En.Map;
import BP.WF.*;
import BP.WF.*;
import java.util.*;

/** 
 审核组件
*/
public class FrmWorkCheck extends Entity
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
		return FrmWorkCheckSta.forValue(this.GetValIntByKey(FrmWorkCheckAttr.FWCSta));
	}
	public final void setHisFrmWorkCheckSta(FrmWorkCheckSta value) throws Exception
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCSta, value.getValue());
	}
	/** 
	 显示格式(0=表格,1=自由.)
	 * @throws Exception 
	*/
	public final FrmWorkShowModel getHisFrmWorkShowModel() throws Exception
	{
		return FrmWorkShowModel.forValue(this.GetValIntByKey(FrmWorkCheckAttr.FWCShowModel));
	}
	public final void setHisFrmWorkShowModel(FrmWorkShowModel value) throws Exception
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCShowModel, value.getValue());
	}
	/** 
	 附件类型
	 * @throws Exception 
	*/
	public final FWCAth getFWCAth() throws Exception
	{
		return FWCAth.forValue(this.GetValIntByKey(FrmWorkCheckAttr.FWCAth));
	}
	public final void setFWCAth(FWCAth value) throws Exception
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCAth, value.getValue());
	}
	/** 
	 组件类型
	 * @throws Exception 
	*/
	public final FWCType getHisFrmWorkCheckType() throws Exception
	{
		return FWCType.forValue(this.GetValIntByKey(FrmWorkCheckAttr.FWCType));
	}
	public final void setHisFrmWorkCheckType(FWCType value) throws Exception
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCType, value.getValue());
	}
	/** 
	 标签
	 * @throws Exception 
	*/
	public final String getFWCLab() throws Exception
	{
		return this.GetValStrByKey(FrmWorkCheckAttr.FWCLab);
	}
	/** 
	 组件类型名称
	 * @throws Exception 
	*/
	public final String getFWCTypeT() throws Exception
	{
		return this.GetValRefTextByKey(FrmWorkCheckAttr.FWCType);
	}
	/** 
	 Y
	 * @throws Exception 
	*/
	public final float getFWC_Y() throws Exception
	{
		return this.GetValFloatByKey(FrmWorkCheckAttr.FWC_Y);
	}
	public final void setFWC_Y(float value) throws Exception
	{
		this.SetValByKey(FrmWorkCheckAttr.FWC_Y, value);
	}
	/** 
	 X
	 * @throws Exception 
	*/
	public final float getFWC_X() throws Exception
	{
		return this.GetValFloatByKey(FrmWorkCheckAttr.FWC_X);
	}
	public final void setFWC_X(float value) throws Exception
	{
		this.SetValByKey(FrmWorkCheckAttr.FWC_X, value);
	}
	/** 
	 W
	 * @throws Exception 
	*/
	public final float getFWC_W() throws Exception
	{
		return this.GetValFloatByKey(FrmWorkCheckAttr.FWC_W);
	}
	public final void setFWC_W(float value) throws Exception
	{
		this.SetValByKey(FrmWorkCheckAttr.FWC_W, value);
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
		return this.GetValFloatByKey(FrmWorkCheckAttr.FWC_H);
	}
	public final void setFWC_H(float value) throws Exception
	{
		this.SetValByKey(FrmWorkCheckAttr.FWC_H, value);
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
		return this.GetValBooleanByKey(FrmWorkCheckAttr.FWCTrackEnable);
	}
	public final void setFWCTrackEnable(boolean value) throws Exception
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCTrackEnable, value);
	}
	/** 
	 历史审核信息是否显示?
	 * @throws Exception 
	*/
	public final boolean getFWCListEnable() throws Exception
	{
		return this.GetValBooleanByKey(FrmWorkCheckAttr.FWCListEnable);
	}
	public final void setFWCListEnable(boolean value) throws Exception
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCListEnable, value);
	}
	/** 
	 在轨迹表里是否显示所有的步骤？
	 * @throws Exception 
	*/
	public final boolean getFWCIsShowAllStep() throws Exception
	{
		return this.GetValBooleanByKey(FrmWorkCheckAttr.FWCIsShowAllStep);
	}
	public final void setFWCIsShowAllStep(boolean value) throws Exception
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCIsShowAllStep, value);
	}
	/** 
	 是否显示轨迹在没有走到的节点
	 * @throws Exception 
	*/
	public final boolean getFWCIsShowTruck() throws Exception
	{
		return this.GetValBooleanByKey(FrmWorkCheckAttr.FWCIsShowTruck);
	}
	public final void setFWCIsShowTruck(boolean value) throws Exception
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCIsShowTruck, value);
	}
	/** 
	 是否显示退回信息？
	 * @throws Exception 
	*/
	public final boolean getFWCIsShowReturnMsg() throws Exception
	{
		return this.GetValBooleanByKey(FrmWorkCheckAttr.FWCIsShowReturnMsg);
	}
	public final void setFWCIsShowReturnMsg(boolean value) throws Exception
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCIsShowReturnMsg, value);
	}
	/** 
	 如果用户未审核是否按照默认意见填充?
	 * @throws Exception 
	*/
	public final boolean getFWCIsFullInfo() throws Exception
	{
		return this.GetValBooleanByKey(FrmWorkCheckAttr.FWCIsFullInfo);
	}
	public final void setFWCIsFullInfo(boolean value) throws Exception
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCIsFullInfo, value);
	}
	/** 
	 默认审核信息
	 * @throws Exception 
	*/
	public final String getFWCDefInfo() throws Exception
	{
		return this.GetValStringByKey(FrmWorkCheckAttr.FWCDefInfo);
	}
	public final void setFWCDefInfo(String value) throws Exception
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCDefInfo, value);
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
		String str = this.GetValStringByKey(FrmWorkCheckAttr.FWCNodeName);
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
		return this.GetValStringByKey(FrmWorkCheckAttr.FWCOpLabel);
	}
	public final void setFWCOpLabel(String value) throws Exception
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCOpLabel, value);
	}
	/** 
	 操作字段
	 * @throws Exception 
	*/
	public final String getFWCFields() throws Exception
	{
		return this.GetValStringByKey(FrmWorkCheckAttr.FWCFields);
	}
	public final void setFWCFields(String value) throws Exception
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCFields, value);
	}
	/** 
	 是否显示数字签名？
	 * @throws Exception 
	*/
	public final boolean getSigantureEnabel() throws Exception
	{
		return this.GetValBooleanByKey(FrmWorkCheckAttr.SigantureEnabel);
	}
	public final void setSigantureEnabel(boolean value) throws Exception
	{
		this.SetValByKey(FrmWorkCheckAttr.SigantureEnabel, value);
	}

	/** 
	 协作模式下操作员显示顺序
	 * @throws Exception 
	*/
	public final FWCOrderModel getFWCOrderModel() throws Exception
	{
		return FWCOrderModel.forValue(this.GetValIntByKey(FrmWorkCheckAttr.FWCOrderModel, 0));
	}
	public final void setFWCOrderModel(FWCOrderModel value) throws Exception
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCOrderModel, value.getValue());
	}
	/** 
	 审核组件状态
	 * @throws Exception 
	*/
	public final FrmWorkCheckSta getFWCSta() throws Exception
	{
		return FrmWorkCheckSta.forValue(this.GetValIntByKey(FrmWorkCheckAttr.FWCSta, 0));
	}
	public final void setFWCSta(FrmWorkCheckSta value) throws Exception
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCSta, value.getValue());
	}

	public final int getFWCVer() throws Exception
	{
		return this.GetValIntByKey(FrmWorkCheckAttr.FWCVer, 0);
	}
	public final void setFWCVer(int value) throws Exception
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCVer,value);
	}
	public final String getFWCView() throws Exception
	{
		return this.GetValStringByKey(FrmWorkCheckAttr.FWCView);
	}
	public final void setFWCView(String value) throws Exception
	{
		this.SetValByKey(FrmWorkCheckAttr.FWCView, value);
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
		map.AddTBString(FrmWorkCheckAttr.FWCLab, "审核信息", "显示标签", true, false, 0, 100, 10, true);


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


		map.AddTBFloat(FrmWorkCheckAttr.FWC_X, 300, "位置X", true, false);
		map.AddTBFloat(FrmWorkCheckAttr.FWC_Y, 500, "位置Y", true, false);

		map.AddTBFloat(FrmWorkCheckAttr.FWC_H, 300, "高度(0=100%)", true, false);
		map.AddTBFloat(FrmWorkCheckAttr.FWC_W, 400, "宽度(0=100%)", true, false);

		map.AddTBString(FrmWorkCheckAttr.FWCFields, null, "审批格式字段", true, false, 0, 50, 10, true);

		map.AddBoolean(FrmWorkCheckAttr.FWCIsShowTruck, false, "是否显示未审核的轨迹？", true, true, true);
		map.AddBoolean(FrmWorkCheckAttr.FWCIsShowReturnMsg, false, "是否显示退回信息？", true, true, true);

			//增加如下字段是为了查询与排序的需要.
		map.AddTBString(NodeAttr.FK_Flow, null, "流程编号", false, false, 0, 3, 10);
		map.AddTBInt(NodeAttr.Step, 0, "步骤", false, false);


			//协作模式下审核人显示顺序. add for yantai zongheng.
		map.AddDDLSysEnum(FrmWorkCheckAttr.FWCOrderModel, 0, "协作模式下操作员显示顺序", true, true, FrmWorkCheckAttr.FWCOrderModel, "@0=按审批时间先后排序@1=按照接受人员列表先后顺序(官职大小)");

			//for tianye , 多人审核的时候，不让其看到其他人的意见.
		map.AddDDLSysEnum(FrmWorkCheckAttr.FWCMsgShow, 0, "审核意见显示方式", true, true, FrmWorkCheckAttr.FWCMsgShow, "@0=都显示@1=仅显示自己的意见");

		map.AddDDLSysEnum(FrmWorkCheckAttr.FWCVer, 0, "审核意见版本号", true, true, FrmWorkCheckAttr.FWCVer, "@0=2018@1=2019");
		map.AddTBString(FrmWorkCheckAttr.FWCView, null, "审核意见立场", true, false, 20, 200, 200,true);


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