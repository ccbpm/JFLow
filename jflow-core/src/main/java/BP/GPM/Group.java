package BP.GPM;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 权限组
*/
public class Group extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 按钮权限控制
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.OpenForAppAdmin();
		return uac;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 权限组
	*/
	public Group()
	{
	}
	/** 
	 权限组
	 
	 @param mypk
	*/
	public Group(String no)
	{
		this.No = no;
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

		Map map = new Map("GPM_Group");
		map.DepositaryOfEntity = Depositary.None;
		map.EnDesc = "权限组";
		map.EnType = EnType.Sys;
		map.IsAutoGenerNo = true;

		map.AddTBStringPK(GroupAttr.No, null, "编号", true, true, 3, 3, 3);
		map.AddTBString(GroupAttr.Name, null, "名称", true, false, 0, 300, 20);
		 //   map.AddTBString(GroupAttr.ParentNo, null, "父亲节编号", true, true, 0, 100, 20);
		map.AddTBInt(GroupAttr.Idx, 0, "显示顺序", true, false);


		map.AttrsOfOneVSM.Add(new GroupEmps(), new Emps(), GroupEmpAttr.FK_Group, GroupEmpAttr.FK_Emp, EmpAttr.Name, EmpAttr.No, "人员(简单)");


			//节点绑定人员. 使用树杆与叶子的模式绑定.
		map.AttrsOfOneVSM.AddBranchesAndLeaf(new GroupEmps(), new BP.Port.Emps(), GroupEmpAttr.FK_Group, GroupEmpAttr.FK_Emp, "人员(树)", EmpAttr.FK_Dept, EmpAttr.Name, EmpAttr.No, "@WebUser.getFK_Dept()");

			//map.AttrsOfOneVSM.Add(new GroupEmps(), new Emps(),
			// GroupEmpAttr.FK_Group, GroupEmpAttr.FK_Emp, EmpAttr.Name, EmpAttr.No, "人员(简单)");

		map.AttrsOfOneVSM.Add(new GroupStations(), new Stations(), GroupEmpAttr.FK_Group, GroupStationAttr.FK_Station, EmpAttr.Name, EmpAttr.No, "岗位(简单)");


		map.AttrsOfOneVSM.AddGroupListModel(new GroupStations(), new BP.GPM.Stations(), GroupStationAttr.FK_Group, GroupStationAttr.FK_Station, "岗位(平铺)", StationAttr.FK_StationType);


			//节点绑定部门. 节点绑定部门.
		map.AttrsOfOneVSM.AddBranches(new GroupMenus(), new BP.GPM.Menus(), BP.GPM.GroupMenuAttr.FK_Group, BP.GPM.GroupMenuAttr.FK_Menu, "绑定菜单", EmpAttr.Name, EmpAttr.No, "0");


		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}