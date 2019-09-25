package BP.WF;

import BP.En.*;
import BP.Web.*;
import BP.Port.*;
import BP.WF.Template.*;

/**
 * 取回任务
 */
public class GetTask extends BP.En.Entity {
	/**
	 * 我可以处理当前的工作吗？
	 * 
	 * @return
	 * @throws Exception
	 */
	public final boolean Can_I_Do_It() throws Exception {
		/* 判断我是否可以处理当前点数据？ */
		switch (this.getHisDeliveryWay()) {
		case ByPreviousNodeFormEmpsField:
			NodeEmps ndemps = new NodeEmps(this.getNodeID());
			if (ndemps.Contains(NodeEmpAttr.FK_Emp, WebUser.getNo()) == false) {
				return false;
			} else {
				return true;
			}
		case ByStation:
			Stations sts = WebUser.getHisStations();
			String myStaStrs = "@";
			for (Station st : sts.ToJavaList()) {
				myStaStrs += "@" + st.getNo();
			}
			myStaStrs = myStaStrs + "@";

			NodeStations ndeStas = new NodeStations(this.getNodeID());
			boolean isHave = false;
			for (NodeStation ndS : ndeStas.ToJavaList()) {
				if (myStaStrs.contains("@" + ndS.getFK_Station() + "@") == true) {
					isHave = true;
					break;
				}
			}
			if (isHave == false) {
				return false;
			}
			return true;
		default: // 其它的情况则不与判断。
			return false;

		}
	}

	/// #region attrs
	/**
	 * 投递方式
	 * 
	 * @throws Exception
	 */
	public final DeliveryWay getHisDeliveryWay() throws Exception {
		return DeliveryWay.forValue(this.GetValIntByKey(NodeAttr.DeliveryWay));
	}

	/**
	 * 节点ID
	 */
	public final int getNodeID() throws Exception {
		return this.GetValIntByKey(NodeAttr.NodeID);
	}

	public final void setNodeID(int value) throws Exception {
		this.SetValByKey(NodeAttr.NodeID, value);
	}

	/**
	 * 名称
	 * 
	 * @throws Exception
	 */
	public final String getName() throws Exception {
		return this.GetValStringByKey(NodeAttr.Name);
	}

	public final void setName(String value) throws Exception {
		this.SetValByKey(NodeAttr.Name, value);
	}

	/**
	 * 流程编号
	 * 
	 * @throws Exception
	 */
	public final String getFK_Flow() throws Exception {
		return this.GetValStringByKey(GetTaskAttr.FK_Flow);
	}

	public final void setFK_Flow(String value) throws Exception {
		this.SetValByKey(GetTaskAttr.FK_Flow, value);
	}

	/**
	 * 步骤
	 * 
	 * @throws Exception
	 */
	public final int getStep() throws Exception {
		return this.GetValIntByKey(NodeAttr.Step);
	}

	public final String getCheckNodes() throws Exception {
		String s = this.GetValStringByKey(GetTaskAttr.CheckNodes);
		s = s.replace("~", "'");
		return s;
	}

	public final void setCheckNodes(String value) throws Exception {
		this.SetValByKey(GetTaskAttr.CheckNodes, value);
	}

	/// #endregion attrs

	/// #region 属性
	@Override
	public Map getEnMap() {
		if (this.get_enMap() != null) {
			return this.get_enMap();
		}

		Map map = new Map("WF_Node", "取回任务");

		/// #region 字段
		map.AddTBIntPK(NodeAttr.NodeID, 0, "NodeID", true, true);
		map.AddTBString(NodeAttr.Name, null, "节点名称", true, false, 0, 100, 10);
		map.AddTBInt(NodeAttr.Step, 0, "步骤", true, false);
		map.AddTBString(NodeAttr.FK_Flow, null, "流程编号", true, false, 0, 10, 10);
		map.AddTBString(GetTaskAttr.CheckNodes, null, "工作节点s", true, false, 0, 50, 100);

		map.AddTBInt(NodeAttr.DeliveryWay, 0, "访问规则", true, true);

		/// #endregion 字段

		this.set_enMap(map);
		return this.get_enMap();
	}

	/**
	 * 取回任务
	 */
	public GetTask() {
	}

	public GetTask(int nodeId) throws Exception {
		this.setNodeID(nodeId);
		this.Retrieve();
	}

}