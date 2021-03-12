package bp.gpm;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.port.StationAttr;
import bp.port.Stations;

/** 
 权限组
*/
public class Group extends EntityNoName
{
	private static final long serialVersionUID = 1L;
	///按钮权限控制
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForAppAdmin();
		return uac;
	}

		///


		///构造方法
	/** 
	 权限组
	*/
	public Group()
	{
	}
	/** 
	 权限组
	 
	 @param mypk
	 * @throws Exception 
	*/
	public Group(String no) throws Exception
	{
		this.setNo(no);
		this.Retrieve();
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("GPM_Group");
		map.setDepositaryOfEntity( Depositary.None);
		map.setEnDesc("权限组");
		map.setEnType( EnType.Sys);
		map.setIsAutoGenerNo( true);

		map.AddTBStringPK(GroupAttr.No, null, "编号", true, true, 3, 3, 3);
		map.AddTBString(GroupAttr.Name, null, "名称", true, false, 0, 300, 20);
		 //   map.AddTBString(GroupAttr.ParentNo, null, "父亲节编号", true, true, 0, 100, 20);
		map.AddTBInt(GroupAttr.Idx, 0, "显示顺序", true, false);


		map.getAttrsOfOneVSM().Add(new bp.gpm.GroupEmps(), new Emps(), GroupEmpAttr.FK_Group, GroupEmpAttr.FK_Emp, EmpAttr.Name, EmpAttr.No, "人员(简单)");


			//节点绑定人员. 使用树杆与叶子的模式绑定.
		map.getAttrsOfOneVSM().AddBranchesAndLeaf(new bp.gpm.GroupEmps(), new Emps(), GroupEmpAttr.FK_Group, GroupEmpAttr.FK_Emp, "人员(树)", EmpAttr.FK_Dept, EmpAttr.Name, EmpAttr.No, "@WebUser.FK_Dept");

			//map.getAttrsOfOneVSM().Add(new GroupEmps(), new Emps(),
			// GroupEmpAttr.FK_Group, GroupEmpAttr.FK_Emp, EmpAttr.Name, EmpAttr.No, "人员(简单)");

		map.getAttrsOfOneVSM().Add(new GroupStations(), new Stations(), GroupEmpAttr.FK_Group, GroupStationAttr.FK_Station, EmpAttr.Name, EmpAttr.No, "岗位(简单)");


		map.getAttrsOfOneVSM().AddGroupListModel(new GroupStations(), new Stations(), GroupStationAttr.FK_Group, GroupStationAttr.FK_Station, "岗位(平铺)", StationAttr.FK_StationType);

		map.getAttrsOfOneVSM().AddBranches(new GroupDepts(), new Depts(), GroupEmpAttr.FK_Group, GroupDeptAttr.FK_Dept, "部门(树)", EmpAttr.Name, EmpAttr.No);


			//节点绑定部门. 节点绑定部门.
		map.getAttrsOfOneVSM().AddBranches(new GroupMenus(), new bp.gpm.Menus(), bp.gpm.GroupMenuAttr.FK_Group, bp.gpm.GroupMenuAttr.FK_Menu, "绑定菜单", EmpAttr.Name, EmpAttr.No, "0");


		this.set_enMap(map);
		return this.get_enMap();
	}

		///
}