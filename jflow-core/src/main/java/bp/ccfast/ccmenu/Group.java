package bp.ccfast.ccmenu;

import bp.en.*;
import bp.port.*;

/** 
 权限组
*/
public class Group extends EntityNoName
{

		///#region 按钮权限控制
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();
		uac.OpenForAppAdmin();
		return uac;
	}

		///#endregion


		///#region 构造方法
	/** 
	 权限组
	*/
	public Group()  {
	}
	/** 
	 权限组
	 
	 param no
	*/
	public Group(String no)
	{
		this.setNo(no);
		try {
			this.Retrieve();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/** 
	 EnMap
	*/
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("GPM_Group", "权限组");
		map.setIsAutoGenerNo(true);

		map.AddTBStringPK(GroupAttr.No, null, "编号", true, true, 3, 3, 3);
		map.AddTBString(GroupAttr.Name, null, "名称", true, false, 0, 300, 20);
		 //   map.AddTBString(GroupAttr.ParentNo, null, "父亲节编号", true, true, 0, 100, 20);
		map.AddTBInt(GroupAttr.Idx, 0, "显示顺序", true, false);

		map.getAttrsOfOneVSM().AddBranchesAndLeaf(new GroupEmps(), new Emps(), GroupEmpAttr.FK_Group, GroupEmpAttr.FK_Emp, "人员(树)", EmpAttr.FK_Dept, EmpAttr.Name, EmpAttr.No, "@WebUser.FK_Dept", null);

			//节点绑定人员. 使用树杆与叶子的模式绑定.
		map.getAttrsOfOneVSM().AddBranchesAndLeaf(new GroupEmps(), new Emps(), GroupEmpAttr.FK_Group, GroupEmpAttr.FK_Emp, "人员(树)", EmpAttr.FK_Dept, EmpAttr.Name, EmpAttr.No, "@WebUser.FK_Dept", null);

			//map.getAttrsOfOneVSM().Add(new GroupEmps(), new Emps(),
			// GroupEmpAttr.FK_Group, GroupEmpAttr.FK_Emp, EmpAttr.Name, EmpAttr.No, "人员(简单)");

		map.getAttrsOfOneVSM().Add(new GroupStations(), new bp.port.Stations(), GroupEmpAttr.FK_Group, GroupStationAttr.FK_Station, EmpAttr.Name, EmpAttr.No, "岗位(简单)");

		map.getAttrsOfOneVSM().AddGroupListModel(new GroupStations(), new Stations(), GroupStationAttr.FK_Group, GroupStationAttr.FK_Station, "岗位(平铺)", StationAttr.FK_StationType, "Name", "No");

		map.getAttrsOfOneVSM().AddBranches(new GroupDepts(), new Depts(), GroupEmpAttr.FK_Group, GroupDeptAttr.FK_Dept, "部门(树)", EmpAttr.Name, EmpAttr.No, "0", null);


			//节点绑定部门. 节点绑定部门.
		map.getAttrsOfOneVSM().AddBranches(new GroupMenus(), new Menus(), GroupMenuAttr.FK_Group, GroupMenuAttr.FK_Menu, "绑定菜单", EmpAttr.Name, EmpAttr.No, "0", null);


		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}