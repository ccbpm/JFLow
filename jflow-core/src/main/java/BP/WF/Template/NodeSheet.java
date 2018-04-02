package BP.WF.Template;

import BP.DA.DataType;
import BP.DA.Log;
import BP.En.Entity;
import BP.En.Map;
import BP.En.RefMethod;
import BP.En.RefMethodType;
import BP.En.UAC;
import BP.Port.DeptAttr;
import BP.Port.Depts;
import BP.Port.EmpAttr;
import BP.Sys.AttachmentUploadType;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachmentAttr;
import BP.Sys.OSModel;
import BP.Sys.PubClass;
import BP.Sys.ToolbarExcel;
import BP.Tools.StringHelper;
import BP.WF.BatchRole;
import BP.WF.BlockModel;
import BP.WF.CancelRole;
import BP.WF.DeliveryWay;
import BP.WF.Flow;
import BP.WF.Glo;
import BP.WF.Node;
import BP.WF.NodeFormType;
import BP.WF.ReturnRole;
import BP.WF.RunModel;
import BP.WF.SubFlowStartWay;
import BP.WF.ThreadKillRole;
import BP.WF.TodolistModel;
import BP.WF.Port.StationAttr;
import BP.WF.Port.StationTypes;
import cn.jflow.common.util.ContextHolderUtils;
import BP.En.Dot2DotModel;

/** 
 节点属性.
 
*/
public class NodeSheet extends Entity
{

		///#region 索引
	/** 
	 获取节点的帮助信息url
	 <p></p>
	 <p>added by liuxc,2014-8-19</p> 
	 
	 @param sysNo 帮助网站中所属系统No
	 @param searchTitle 帮助主题标题
	 @return 
	*/
	/*
	private String getItem(String sysNo, String searchTitle)
	{
		if (StringHelper.isNullOrWhiteSpace(sysNo) || StringHelper.isNullOrWhiteSpace(searchTitle))
		{
			return "javascript:alert('此处还没有帮助信息！')";
		}

		return String.format("http://online.ccflow.org/KM/Tree.jsp?no=%1$s&st=%2$s", sysNo, Uri.EscapeDataString(searchTitle));
	}
	*/

		///#endregion


		///#region 常量
	/** 
	 CCFlow流程引擎
	 
	*/
	private static final String SYS_CCFLOW = "001";
	/** 
	 CCForm表单引擎
	 
	*/
	private static final String SYS_CCFORM = "002";

		///#endregion
	/** 
	 超时处理方式
	 
	*/
	public final OutTimeDeal getHisOutTimeDeal()
	{
		return OutTimeDeal.forValue(this.GetValIntByKey(NodeAttr.OutTimeDeal));
	}
	public final void setHisOutTimeDeal(OutTimeDeal value)
	{
		this.SetValByKey(NodeAttr.OutTimeDeal, value.getValue());
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
	 节点名称
	 
	*/
	public final String getName()
	{
		return this.GetValStringByKey(NodeAttr.Name);
	}
	public final void setName(String value)
	{
		this.SetValByKey(NodeAttr.Name, value);
	}
	/** 
	 流程编号
	 
	*/
	public final String getFK_Flow()
	{
		return this.GetValStringByKey(NodeAttr.FK_Flow);
	}
	public final void setFK_Flow(String value)
	{
		this.SetValByKey(NodeAttr.FK_Flow, value);
	}
	/** 
	 流程名称
	 
	*/
	public final String getFlowName()
	{
		return this.GetValStringByKey(NodeAttr.FlowName);
	}
	public final void setFlowName(String value)
	{
		this.SetValByKey(NodeAttr.FlowName, value);
	}
	  
	/** 
	 主键
	 
	*/
	@Override
	public String getPK()
	{
		return "NodeID";
	}
 
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();		 
		if (BP.Web.WebUser.getNo().equals("admin"))
		{
			uac.IsUpdate = true;
		}
		return uac;
	}

	 


		
	/** 
	 节点
	 
	*/
	public NodeSheet()
	{
	}
	public NodeSheet(int nodeid)
	{
		this.setNodeID(nodeid);
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

			//map 的基 础信息.
		Map map = new Map("WF_Node", "节点");
 
		 
	
         map.AddTBIntPK(NodeAttr.NodeID, 0, "节点ID", true, true);
         map.SetHelperUrl(NodeAttr.NodeID, "http://ccbpm.mydoc.io/?v=5404&t=17901");
         map.AddTBInt(NodeAttr.Step, 0, "步骤(无计算意义)", true, false);
         map.SetHelperUrl(NodeAttr.Step, "http://ccbpm.mydoc.io/?v=5404&t=17902");
         //map.SetHelperAlert(NodeAttr.Step, "它用于节点的排序，正确的设置步骤可以让流程容易读写."); //使用alert的方式显示帮助信息.
         map.AddTBString(NodeAttr.FK_Flow, null, "流程编号", false, false, 3, 3, 10, false, "http://ccbpm.mydoc.io/?v=5404&t=17023");
         map.AddTBString(NodeAttr.Name, null, "名称", true, true, 0, 100, 10, false, "http://ccbpm.mydoc.io/?v=5404&t=17903");
         
         

			//节点工具栏, 主从表映射.
		map.AddDtl(new NodeToolbars(), NodeToolbarAttr.FK_Node);

 
		map.getAttrsOfOneVSM().Add(new BP.WF.Template.NodeStations(), new BP.Port.Stations(), BP.WF.Template.NodeStationAttr.FK_Node, BP.WF.Template.NodeStationAttr.FK_Station, DeptAttr.Name, DeptAttr.No, 
				"节点绑定岗位", Dot2DotModel.Default,null,null);
		
	   map.getAttrsOfOneVSM().AddGroupListModel(new BP.WF.Template.NodeStations(), new BP.WF.Port.Stations(),BP.WF.Template.NodeStationAttr.FK_Node, BP.WF.Template.NodeStationAttr.FK_Station,
			   "节点绑定岗位AddGroupListModel", StationAttr.FK_StationType, DeptAttr.Name,DeptAttr.No);
	   
		
	   map.getAttrsOfOneVSM().Add(new BP.WF.Template.NodeDepts(), new BP.Port.Depts(), NodeDeptAttr.FK_Node, NodeDeptAttr.FK_Dept, DeptAttr.Name, DeptAttr.No, 
			   "节点绑定部门", Dot2DotModel.TreeDept,null,null); 
	    

       //节点绑定人员. 使用树杆与叶子的模式绑定.
       map.getAttrsOfOneVSM().AddBranchesAndLeaf(new BP.WF.Template.NodeEmps(), new BP.Port.Emps(),
          BP.WF.Template.NodeEmpAttr.FK_Node,
          BP.WF.Template.NodeEmpAttr.FK_Emp, "节点绑定接受人", EmpAttr.FK_Dept, EmpAttr.Name, EmpAttr.No, "@WebUser.FK_Dept");

       
	   
	  // map.getAttrsOfOneVSM().Add(new BP.WF.Template.NodeEmps(),new BP.Port.Emps(), NodeEmpAttr.FK_Node, NodeEmpAttr.FK_Emp,DeptAttr.Name, DeptAttr.No, 
		//	   "节点绑定接受人",Dot2DotModel.TreeDeptEmp, new Depts(), EmpAttr.FK_Dept); 

		this.set_enMap(map);
		return this.get_enMap();
	} 
	@Override
	protected boolean beforeUpdate()
	{
		//更新流程版本
		Flow.UpdateVer(this.getFK_Flow());

		//把工具栏的配置放入 sys_mapdata里.
		ToolbarExcel te = new ToolbarExcel("ND" + this.getNodeID());
		te.Copy(this);
		try
		{
			te.Update();
		}
		catch (java.lang.Exception e)
		{
		}


			///#region  检查考核逾期处理的设置的完整性.
		String doOutTime = this.GetValStrByKey(NodeAttr.DoOutTime);
		switch (this.getHisOutTimeDeal())
		{
			case AutoJumpToSpecNode:
				String[] jumps = doOutTime.split("[,]", -1);
				if (jumps.length > 2)
				{
					String msg = "自动跳转到相应节点,配置的内容不正确,格式应该为: Node,EmpNo , 比如: 101,zhoupeng  现在设置的格式为:" + doOutTime;
					throw new RuntimeException(msg);
				}
				break;
			case AutoShiftToSpecUser:
			case RunSQL:
			case SendMsgToSpecUser:
				if (StringHelper.isNullOrEmpty(doOutTime) == false)
				{
					throw new RuntimeException("@在考核逾期处理方式上，您选择的是:" + this.getHisOutTimeDeal() + " ,但是您没有为该规则设置内容。");
				}
				break;
			default:
				break;
		}

			///#endregion 检查考核逾期处理的设置的完整性


			///#region 处理节点数据.
		Node nd = new Node(this.getNodeID());
		if (nd.getIsStartNode() == true)
		{
			//处理按钮的问题
			//不能退回, 加签，移交，退回, 子线程.
			this.SetValByKey(BtnAttr.ReturnRole,ReturnRole.CanNotReturn.getValue());
			this.SetValByKey(BtnAttr.HungEnable, false);
			this.SetValByKey(BtnAttr.ThreadEnable, false); //子线程.
		}

		if (nd.getHisRunModel() == RunModel.HL || nd.getHisRunModel() == RunModel.FHL)
		{
			//如果是合流点
		}
		else
		{
			this.SetValByKey(BtnAttr.ThreadEnable, false); //子线程.
		}

			///#endregion 处理节点数据.


			///#region 处理消息参数字段.
		//this.SetPara(NodeAttr.MsgCtrl, this.GetValIntByKey(NodeAttr.MsgCtrl));
		//this.SetPara(NodeAttr.MsgIsSend, this.GetValIntByKey(NodeAttr.MsgIsSend));
		//this.SetPara(NodeAttr.MsgIsReturn, this.GetValIntByKey(NodeAttr.MsgIsReturn));
		//this.SetPara(NodeAttr.MsgIsShift, this.GetValIntByKey(NodeAttr.MsgIsShift));
		//this.SetPara(NodeAttr.MsgIsCC, this.GetValIntByKey(NodeAttr.MsgIsCC));

		//this.SetPara(NodeAttr.MailEnable, this.GetValIntByKey(NodeAttr.MailEnable));
		//this.SetPara(NodeAttr.MsgMailTitle, this.GetValStrByKey(NodeAttr.MsgMailTitle));
		//this.SetPara(NodeAttr.MsgMailDoc, this.GetValStrByKey(NodeAttr.MsgMailDoc));

		//this.SetPara(NodeAttr.MsgSMSEnable, this.GetValIntByKey(NodeAttr.MsgSMSEnable));
		//this.SetPara(NodeAttr.MsgSMSDoc, this.GetValStrByKey(NodeAttr.MsgSMSDoc));

			///#endregion

		//创建审核组件附件
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

		//清除所有的缓存.
		BP.DA.CashEntity.getDCash().clear();

		return super.beforeUpdate();
	}

		///#endregion
}