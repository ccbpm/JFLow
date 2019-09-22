package BP.En;

import BP.DA.*;
import BP.En.*;
import BP.GPM.*;
import java.util.*;

/**
 * 权限组
 */
public class Group extends EntityNoName {
	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	/// #region 构造方法
	/**
	 * 权限组
	 */
	public Group() {
	}

	/**
	 * 权限组
	 * 
	 * @param mypk
	 * @throws Exception
	 */
	public Group(String no) throws Exception {
		this.setNo(no);
		this.Retrieve();
	}

	/**
	 * EnMap
	 */
	@Override
	public Map getEnMap() {
		if (this.get_enMap() != null) {
			return this.get_enMap();
		}

		Map map = new Map("GPM_Group", "权限组");

		map.setEnType(EnType.Sys);
		map.setIsAutoGenerNo(true);

		map.AddTBStringPK(GroupAttr.No, null, "编号", true, true, 3, 3, 3);
		map.AddTBString(GroupAttr.Name, null, "名称", true, false, 0, 300, 20);
		// map.AddTBString(GroupAttr.ParentNo, null, "父亲节编号", true, true, 0,
		// 100, 20);
		map.AddTBInt(GroupAttr.Idx, 0, "显示顺序", true, false);

		map.AttrsOfOneVSM.Add(new GroupEmps(), new Emps(), GroupEmpAttr.FK_Group, GroupEmpAttr.FK_Emp, EmpAttr.Name,
				EmpAttr.No, "人员(简单)");

		// 节点绑定人员. 使用树杆与叶子的模式绑定.
		map.AttrsOfOneVSM.AddBranchesAndLeaf(new GroupEmps(), new BP.Port.Emps(), GroupEmpAttr.FK_Group,
				GroupEmpAttr.FK_Emp, "人员(树)", EmpAttr.FK_Dept, EmpAttr.Name, EmpAttr.No, "@WebUser.FK_Dept");

		// map.AttrsOfOneVSM.Add(new GroupEmps(), new Emps(),
		// GroupEmpAttr.FK_Group, GroupEmpAttr.FK_Emp, EmpAttr.Name, EmpAttr.No,
		// "人员(简单)");

		map.AttrsOfOneVSM.Add(new GroupStations(), new Stations(), GroupEmpAttr.FK_Group, GroupStationAttr.FK_Station,
				EmpAttr.Name, EmpAttr.No, "岗位(简单)");

		map.AttrsOfOneVSM.AddGroupListModel(new GroupStations(), new BP.GPM.Stations(), GroupStationAttr.FK_Group,
				GroupStationAttr.FK_Station, "岗位(平铺)", StationAttr.FK_StationType);

		this.set_enMap(map);
		return map;
	}
	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	/// #endregion
}