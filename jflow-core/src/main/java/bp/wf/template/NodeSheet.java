package bp.wf.template;

import bp.difference.SystemConfig;
import bp.en.*;
import bp.en.Map;
import bp.sys.*;
import bp.port.*;

/** 
 节点属性.
*/
public class NodeSheet extends Entity
{

		///属性.
	public final int getNodeID()  throws Exception
	{
		return this.GetValIntByKey(NodeAttr.NodeID);
	}
	public final void setNodeID(int value) throws Exception
	{
		this.SetValByKey(NodeAttr.NodeID, value);
	}
	/** 
	 节点名称
	 * @throws Exception 
	*/
	public final String getName() throws Exception
	{
		return this.GetValStringByKey(NodeAttr.Name);
	}
	public final void setName(String value) throws Exception
	{
		this.SetValByKey(NodeAttr.Name, value);
	}

		/// 属性.


		///构造函数
	/** 
	 节点
	*/
	public NodeSheet()
	{
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

			//map 的基 础信息.
		Map map = new Map("WF_Node", "节点");


			/// 基础属性
		map.AddTBIntPK(NodeAttr.NodeID, 0, "节点ID", true, true);
		map.SetHelperUrl(NodeAttr.NodeID, "http://ccbpm.mydoc.io/?v=5404&t=17901");
		map.AddTBInt(NodeAttr.Step, 0, "步骤(无计算意义)", true, false);
		map.SetHelperUrl(NodeAttr.Step, "http://ccbpm.mydoc.io/?v=5404&t=17902");
			//map.SetHelperAlert(NodeAttr.Step, "它用于节点的排序，正确的设置步骤可以让流程容易读写."); //使用alert的方式显示帮助信息.
		map.AddTBString(NodeAttr.FK_Flow, null, "流程编号", false, false, 0, 5, 10, false, "http://ccbpm.mydoc.io/?v=5404&t=17023");
		map.AddTBString(NodeAttr.Name, null, "名称", true, true, 0, 100, 10, false, "http://ccbpm.mydoc.io/?v=5404&t=17903");

			///  基础属性


			///对应关系用户组。
			//平铺模式.
		map.getAttrsOfOneVSM().AddGroupPanelModel(new bp.wf.template.NodeTeams(), new bp.port.Teams(), bp.wf.template.NodeTeamAttr.FK_Node, bp.wf.template.NodeTeamAttr.FK_Team, "节点绑定用户组");

			//deptAndEmp列表模式. @sly 
		map.getAttrsOfOneVSM().AddGroupListModel(new bp.wf.template.NodeTeams(), new bp.port.Teams(), bp.wf.template.NodeTeamAttr.FK_Node, bp.wf.template.NodeTeamAttr.FK_Team, "节点绑定用户组AddTeamListModel", TeamAttr.FK_TeamType);

			///  对应关系用户组


			///对岗位.
			//平铺模式.
		map.getAttrsOfOneVSM().AddGroupPanelModel(new bp.wf.template.NodeStations(), new bp.port.Stations(), bp.wf.template.NodeStationAttr.FK_Node, bp.wf.template.NodeStationAttr.FK_Station, "节点绑定岗位", StationAttr.FK_StationType);

			//列表模式.
		map.getAttrsOfOneVSM().AddGroupListModel(new bp.wf.template.NodeStations(), new bp.port.Stations(), bp.wf.template.NodeStationAttr.FK_Node, bp.wf.template.NodeStationAttr.FK_Station, "节点绑定岗位AddTeamListModel", StationAttr.FK_StationType);

			/// 对岗位.


			///节点绑定人员.
			//平铺模式.
		map.getAttrsOfOneVSM().AddGroupPanelModel(new bp.port.TeamEmps(), new bp.port.Teams(), bp.port.TeamEmpAttr.FK_Team, bp.port.TeamEmpAttr.FK_Emp, "节点绑定人员", TeamAttr.FK_TeamType);

			//列表模式.
		map.getAttrsOfOneVSM().AddGroupListModel(new bp.port.TeamEmps(), new bp.port.Teams(), bp.port.TeamEmpAttr.FK_Team, bp.port.TeamEmpAttr.FK_Emp, "节点绑定人员", TeamAttr.FK_TeamType);

			//节点绑定部门. 节点绑定部门.
		String defDeptVal = "@WebUser.FK_Dept";
		if (SystemConfig.getCCBPMRunModel() !=CCBPMRunModel.Single)
		{
			defDeptVal = "@WebUser.OrgNo";
		}

			//绑定部门的.
		map.getAttrsOfOneVSM().AddBranches(new bp.wf.template.NodeDepts(), new bp.wf.port.Depts(), bp.wf.template.NodeDeptAttr.FK_Node, bp.wf.template.NodeDeptAttr.FK_Dept, "节点绑定部门AddBranches", EmpAttr.Name, EmpAttr.No, defDeptVal, "@No=编号@Name=名称");


			//节点绑定人员. 使用树杆与叶子的模式绑定.
		map.getAttrsOfOneVSM().AddBranchesAndLeaf(new bp.wf.template.NodeEmps(), new bp.port.Emps(), bp.wf.template.NodeEmpAttr.FK_Node, bp.wf.template.NodeEmpAttr.FK_Emp, "节点绑定接受人", EmpAttr.FK_Dept, EmpAttr.Name, EmpAttr.No, defDeptVal, "@No=编号@Name=名称@FK_Dept=部门");



		map.AddDtl(new NodeToolbars(), NodeToolbarAttr.FK_Node);

			// 傻瓜表单可以调用的子流程. 2014.10.19 去掉.
			//map.getAttrsOfOneVSM().Add(new BP.WF.NodeFlows(), new Flows(), NodeFlowAttr.FK_Node, NodeFlowAttr.FK_Flow, DeptAttr.Name, DeptAttr.No,
			//    "傻瓜表单可调用的子流程");

			///

		this.set_enMap(map);
		return this.get_enMap();
	}

		///
}