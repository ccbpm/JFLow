package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.WF.*;
import BP.Sys.*;
import java.util.*;

/** 
 父子流程
*/
public class FrmSubFlow extends Entity
{

		///#region 属性
	/** 
	 标签
	 * @throws Exception 
	*/
	public final String getSFLab() throws Exception
	{
		return this.GetValStringByKey(FrmSubFlowAttr.SFLab);
	}
	/** 
	 编号
	 * @throws Exception 
	*/
	public final String getNo() throws Exception
	{
		return "ND" + this.getNodeID();
	}
	public final void setNo(String value) throws Exception
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
	 可触发的子流程
	 * @throws Exception 
	*/
	public final String getSFActiveFlows() throws Exception
	{
		return this.GetValStringByKey(NodeAttr.SFActiveFlows);
	}
	public final void setSFActiveFlows(String value) throws Exception
	{
		this.SetValByKey(NodeAttr.SFActiveFlows, value);
	}
	/** 
	 字段列
	 * @throws Exception 
	*/
	public final String getSFFields() throws Exception
	{
		return this.GetValStringByKey(FrmSubFlowAttr.SFFields);
	}
	public final void setSFFields(String value) throws Exception
	{
		this.SetValByKey(FrmSubFlowAttr.SFFields, value);
	}
	/** 
	 状态
	 * @throws Exception 
	*/
	public final FrmSubFlowSta getHisFrmSubFlowSta() throws Exception
	{
		return FrmSubFlowSta.forValue(this.GetValIntByKey(FrmSubFlowAttr.SFSta));
	}
	public final void setHisFrmSubFlowSta(FrmSubFlowSta value) throws Exception
	{
		this.SetValByKey(FrmSubFlowAttr.SFSta, value.getValue());
	}
	/** 
	 显示控制方式
	 * @throws Exception 
	*/
	public final SFShowCtrl getSFShowCtrl() throws Exception
	{
		return SFShowCtrl.forValue(this.GetValIntByKey(FrmSubFlowAttr.SFShowCtrl));
	}
	public final void setSFShowCtrl(SFShowCtrl value) throws Exception
	{
		this.SetValByKey(FrmSubFlowAttr.SFShowCtrl, value.getValue());
	}
	/** 
	 显示格式(0=表格,1=自由.)
	 * @throws Exception 
	*/
	public final FrmWorkShowModel getHisFrmWorkShowModel() throws Exception
	{
		return FrmWorkShowModel.forValue(this.GetValIntByKey(FrmSubFlowAttr.SFShowModel));
	}
	public final void setHisFrmWorkShowModel(FrmWorkShowModel value) throws Exception
	{
		this.SetValByKey(FrmSubFlowAttr.SFShowModel, value.getValue());
	}
	/** 
	 控件状态
	 * @throws Exception 
	*/
	public final FrmSubFlowSta getSFSta() throws Exception
	{
		return FrmSubFlowSta.forValue(this.GetValIntByKey(FrmSubFlowAttr.SFSta));
	}
	public final void setSFSta(FrmSubFlowSta value) throws Exception
	{
		this.SetValByKey(FrmSubFlowAttr.SFSta, value.getValue());
	}
	/** 
	 显示方式
	 * @throws Exception 
	*/
	public final FrmWorkShowModel getSFShowModel() throws Exception
	{
		return FrmWorkShowModel.forValue(this.GetValIntByKey(FrmSubFlowAttr.SFShowModel));
	}
	public final void setSFShowModel(FrmWorkShowModel value) throws Exception
	{
		this.SetValByKey(FrmSubFlowAttr.SFShowModel, value.getValue());
	}
	/** 
	 Y
	 * @throws Exception 
	*/
	public final float getSF_Y() throws Exception
	{
		return this.GetValFloatByKey(FrmSubFlowAttr.SF_Y);
	}
	public final void setSF_Y(float value) throws Exception
	{
		this.SetValByKey(FrmSubFlowAttr.SF_Y, value);
	}
	/** 
	 X
	 * @throws Exception 
	*/
	public final float getSF_X() throws Exception
	{
		return this.GetValFloatByKey(FrmSubFlowAttr.SF_X);
	}
	public final void setSF_X(float value) throws Exception
	{
		this.SetValByKey(FrmSubFlowAttr.SF_X, value);
	}
	/** 
	 打开类型
	 * @throws Exception 
	*/
	public final int getSFOpenType() throws Exception
	{
		return this.GetValIntByKey(FrmSubFlowAttr.SFOpenType);
	}
	public final void setSFOpenType(int value) throws Exception
	{
		this.SetValByKey(FrmSubFlowAttr.SFOpenType, value);
	}
	/** 
	 W
	 * @throws Exception 
	*/
	public final float getSF_W() throws Exception
	{
		return this.GetValFloatByKey(FrmSubFlowAttr.SF_W);
	}
	public final void setSF_W(float value) throws Exception
	{
		this.SetValByKey(FrmSubFlowAttr.SF_W, value);
	}
	public final String getSF_Wstr() throws Exception
	{
		if (this.getSF_W() == 0)
		{
			return "100%";
		}
		return this.getSF_W() + "px";
	}
	/** 
	 H
	 * @throws Exception 
	*/
	public final float getSF_H() throws Exception
	{
		return this.GetValFloatByKey(FrmSubFlowAttr.SF_H);
	}
	public final void setSF_H(float value) throws Exception
	{
		this.SetValByKey(FrmSubFlowAttr.SF_H, value);
	}
	public final String getSF_Hstr() throws Exception
	{
		if (this.getSF_H() == 0)
		{
			return "100%";
		}
		return this.getSF_H() + "px";
	}
	/** 
	 轨迹图是否显示?
	 * @throws Exception 
	*/
	public final boolean getSFTrackEnable() throws Exception
	{
		return this.GetValBooleanByKey(FrmSubFlowAttr.SFTrackEnable);
	}
	public final void setSFTrackEnable(boolean value) throws Exception
	{
		this.SetValByKey(FrmSubFlowAttr.SFTrackEnable, value);
	}
	/** 
	 历史审核信息是否显示?
	 * @throws Exception 
	*/
	public final boolean getSFListEnable() throws Exception
	{
		return this.GetValBooleanByKey(FrmSubFlowAttr.SFListEnable);
	}
	public final void setSFListEnable(boolean value) throws Exception
	{
		this.SetValByKey(FrmSubFlowAttr.SFListEnable, value);
	}
	/** 
	 在轨迹表里是否显示所有的步骤？
	 * @throws Exception 
	*/
	public final boolean getSFIsShowAllStep() throws Exception
	{
		return this.GetValBooleanByKey(FrmSubFlowAttr.SFIsShowAllStep);
	}
	public final void setSFIsShowAllStep(boolean value) throws Exception
	{
		this.SetValByKey(FrmSubFlowAttr.SFIsShowAllStep, value);
	}
	/** 
	 如果用户未审核是否按照默认意见填充?
	 * @throws Exception 
	*/
	public final boolean getSFIsFullInfo() throws Exception
	{
		return this.GetValBooleanByKey(FrmSubFlowAttr.SFIsFullInfo);
	}
	public final void setSFIsFullInfo(boolean value) throws Exception
	{
		this.SetValByKey(FrmSubFlowAttr.SFIsFullInfo, value);
	}
	/** 
	 默认审核信息
	 * @throws Exception 
	*/
	public final String getSFDefInfo() throws Exception
	{
		return this.GetValStringByKey(FrmSubFlowAttr.SFDefInfo);
	}
	public final void setSFDefInfo(String value) throws Exception
	{
		this.SetValByKey(FrmSubFlowAttr.SFDefInfo, value);
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
	 标题，如果为空则取节点名称.
	 * @throws Exception 
	*/
	public final String getSFCaption() throws Exception
	{
		String str = this.GetValStringByKey(FrmSubFlowAttr.SFCaption);
		if (str.equals(""))
		{
			str = "启动子流程";
		}
		return str;
	}
	public final void setSFCaption(String value) throws Exception
	{
		this.SetValByKey(FrmSubFlowAttr.SFCaption, value);
	}
	/** 
	 操作名词(审核，审定，审阅，批示)
	 * @throws Exception 
	*/
	public final String getSFOpLabel() throws Exception
	{
		return this.GetValStringByKey(FrmSubFlowAttr.SFOpLabel);
	}
	public final void setSFOpLabel(String value) throws Exception
	{
		this.SetValByKey(FrmSubFlowAttr.SFOpLabel, value);
	}
	/** 
	 是否显示数字签名？
	 * @throws Exception 
	*/
	public final boolean getSigantureEnabel() throws Exception
	{
		return this.GetValBooleanByKey(FrmSubFlowAttr.SigantureEnabel);
	}
	public final void setSigantureEnabel(boolean value) throws Exception
	{
		this.SetValByKey(FrmSubFlowAttr.SigantureEnabel, value);
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
	 父子流程
	*/
	public FrmSubFlow()
	{
	}
	/** 
	 父子流程
	 
	 @param mapData
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
	 
	 @param nodeID
	 * @throws Exception 
	*/
	public FrmSubFlow(int nodeID) throws Exception
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

		Map map = new Map("WF_Node", "父子流程");

		map.AddTBIntPK(NodeAttr.NodeID, 0, "节点ID", true, true);
		map.AddTBString(NodeAttr.Name, null, "节点名称", true, true, 0, 100, 10);
		map.AddTBString(FrmSubFlowAttr.SFLab, "子流程", "显示标签", true, false, 0, 200, 10, true);


			///#region 此处变更了 NodeSheet类中的，map 描述该部分也要变更.

		map.AddDDLSysEnum(FrmSubFlowAttr.SFSta, FrmSubFlowSta.Disable.getValue(), "组件状态", true, true, FrmSubFlowAttr.SFSta, "@0=禁用@1=启用@2=只读");

		map.AddDDLSysEnum(FrmSubFlowAttr.SFShowModel, FrmWorkShowModel.Free.getValue(), "显示方式", true, true, FrmSubFlowAttr.SFShowModel, "@0=表格方式@1=自由模式"); //此属性暂时没有用.

		map.AddTBString(FrmSubFlowAttr.SFCaption, "启动子流程", "连接标题", true, false, 0, 100, 10, true);
		map.AddTBString(FrmSubFlowAttr.SFDefInfo, null, "可启动的子流程编号(多个用逗号分开)", false, false, 0, 50, 10, true);
		map.AddTBString(FrmSubFlowAttr.SFActiveFlows, null, "可触发的子流程编号(多个用逗号分开)", false, false, 0, 50, 10, true);

		map.AddTBFloat(FrmSubFlowAttr.SF_X, 5, "位置X", true, false);
		map.AddTBFloat(FrmSubFlowAttr.SF_Y, 5, "位置Y", true, false);

		map.AddTBFloat(FrmSubFlowAttr.SF_H, 300, "高度", true, false);
		map.AddTBFloat(FrmSubFlowAttr.SF_W, 400, "宽度", true, false);

		map.AddTBString(FrmSubFlowAttr.SFFields, null, "审批格式字段", true, false, 0, 50, 10, true);

		map.AddDDLSysEnum(FrmSubFlowAttr.SFShowCtrl, SFShowCtrl.All.getValue(), "显示控制方式", true, true, FrmSubFlowAttr.SFShowCtrl, "@0=可以看所有的子流程@1=仅仅可以看自己发起的子流程"); //此属性暂时没有用.

		map.AddDDLSysEnum(FrmSubFlowAttr.SFOpenType, 0, "打开子流程显示", true, true, FrmSubFlowAttr.SFOpenType, "@0=工作查看器@1=傻瓜表单轨迹查看器"); //此属性暂时没有用.



			///#endregion 此处变更了 NodeSheet类中的，map 描述该部分也要变更.

		RefMethod rm = new RefMethod();
		rm.Title = "手动启动子流程";
		rm.ClassMethodName = this.toString() + ".DoSubFlowHand";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "自动触发子流程";
		rm.ClassMethodName = this.toString() + ".DoSubFlowAuto";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "延续子流程";
		rm.ClassMethodName = this.toString() + ".DoSubFlowYanXu";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion


		///#region 子流程。
	/** 
	 自动触发
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoSubFlowAuto() throws Exception
	{
		return "../../Admin/AttrNode/SubFlow/SubFlowAuto.htm?FK_Node=" + this.getNodeID() + "&tk=" + (new Random()).nextDouble();
	}
	/** 
	 手动启动子流程
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoSubFlowHand() throws Exception
	{
		return "../../Admin/AttrNode/SubFlow/SubFlowHand.htm?FK_Node=" + this.getNodeID() + "&tk=" + (new Random()).nextDouble();
	}
	/** 
	 延续子流程
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoSubFlowYanXu() throws Exception
	{
		return "../../Admin/AttrNode/SubFlow/SubFlowYanXu.htm?FK_Node=" + this.getNodeID() + "&tk=" + (new Random()).nextDouble();
	}

		///#endregion 子流程。


		///#region 重写方法.
	 
	@Override
	protected void afterUpdate() throws Exception
	{
		//清空缓存，重新查数据
		Node nd = new Node(this.getNodeID());
		nd.RetrieveFromDBSources();
		Cash2019.UpdateRow(nd.toString(),String.valueOf(this.getNodeID()),nd.getRow());

		GroupField gf = new GroupField();
		if (this.getSFSta() == FrmSubFlowSta.Disable)
		{
			gf.Delete(GroupFieldAttr.CtrlID, "SubFlow" + this.getNo());
		}
		else
		{
			if (gf.IsExit(GroupFieldAttr.CtrlID, "SubFlow" + this.getNo()) == false)
			{
				gf = new GroupField();
				gf.setFrmID("ND" + this.getNodeID());
				gf.setCtrlID("SubFlow" + this.getNo());
				gf.setCtrlType(GroupCtrlType.SubFlow);
				gf.setLab("父子流程组件");
				gf.setIdx(0);
				gf.Insert(); //插入.
			}
		}

		super.afterUpdate();
	}

		///#endregion 重写方法.
}