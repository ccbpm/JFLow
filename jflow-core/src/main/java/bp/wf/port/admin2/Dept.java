package bp.wf.port.admin2;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.en.Map;
import bp.web.*;
import bp.port.*;
import bp.sys.*;
import bp.sys.CCFormAPI;
import bp.wf.template.*;
import bp.wf.*;
import bp.wf.httphandler.WF_Admin_Template;

/**
 * 部门
 */
public class Dept extends EntityTree {
	private static final long serialVersionUID = 1L;
	/// 属性

	/**
	 * 组织编号
	 * 
	 * @throws Exception
	 */
	public final String getOrgNo() throws Exception {
		return this.GetValStrByKey(DeptAttr.OrgNo);
	}

	public final void setOrgNo(String value) throws Exception {
		this.SetValByKey(DeptAttr.OrgNo, value);
	}

	/// 构造函数
	/**
	 * 部门
	 */
	public Dept() {
	}

	/**
	 * 部门
	 * 
	 * @param no 编号
	 */
	public Dept(String no) throws Exception {
		super(no);
	}

	///

	/// 重写方法
	/**
	 * UI界面上的访问控制
	 */
	@Override
	public UAC getHisUAC() throws Exception {
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}

	/**
	 * Map
	 */
	@Override
	public Map getEnMap() throws Exception {
		if (this.get_enMap() != null) {
			return this.get_enMap();
		}

		Map map = new Map("Port_Dept", "部门");

		map.setAdjunctType(AdjunctType.None);

		map.AddTBStringPK(DeptAttr.No, null, "编号", true, false, 1, 30, 40);
		map.AddTBString(DeptAttr.Name, null, "名称", true, false, 0, 60, 200);
		map.AddTBString(DeptAttr.ParentNo, null, "父节点编号", true, false, 0, 30, 40);
		map.AddTBString(DeptAttr.OrgNo, null, "隶属组织", true, false, 0, 50, 40);
		map.AddTBInt(DeptAttr.Idx, 0, "顺序号", true, false);

		RefMethod rm = new RefMethod();
		rm.Title = "设置为独立组织";
		rm.Warning = "如果当前部门已经是独立组织，系统就会提示错误。";
		rm.ClassMethodName = this.toString() + ".GenerOrg";
		rm.getHisAttrs().AddTBString("adminer", null, "组织管理员编号", true, false, 0, 100, 100);
		rm.getHisAttrs().AddBoolen("IsGener", false, "是否初始化岗位演示数据？");
		map.AddRefMethod(rm);

		// rm = new RefMethod();
		// rm.Title = "克隆独立组织";
		// rm.Warning = "如果当前部门已经是独立组织，系统就会提示错误。";
		// rm.ClassMethodName = this.toString() + ".DoCloneOrg";
		// rm.getHisAttrs().AddTBString("adminer", null, "组织管理员编号", true, false, 0, 100,
		// 100);
		// rm.getHisAttrs().AddTBString("coneOrgNo", null, "被克隆的组织编号(14903)", true,
		// false, 0, 100, 100);
		// map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}

	/**
	 * 设置组织
	 * 
	 * @param userNo 管理员编号
	 * @return
	 */
	public final String GenerOrg(String adminer, boolean isSetStationDemo) throws Exception {

		try {

			if (WebUser.getNo().equals("admin") == false) {
				return "err@非admin管理员，您无法执行该操作.";
			}

			// 检查是否有该用户.
			bp.port.Emp emp = new bp.port.Emp();
			emp.setNo(adminer);
			if (emp.RetrieveFromDBSources() == 0) {
				return "err@用户编号错误:" + adminer;
			}

			// 检查该部门是否是独立组织.
			Org org = new Org();
			org.setNo(this.getNo());
			if (org.RetrieveFromDBSources() == 1) {
				/* 已经是独立组织了. */
				String info1 = org.DoCheck();
				return "info@当前部门已经是独立组织了,检查信息如下:" + info1;
			}
			org.setName(this.getName()); // 把部门名字改为组织名字.

			// 设置父级信息.
			bp.port.Dept parentDept = new bp.port.Dept();

			if (this.getParentNo().equals("0") == true) {
				this.setParentNo(this.getNo());
			}

			parentDept.setNo(this.getParentNo());
			parentDept.Retrieve();

			org.setParentNo(this.getParentNo());
			org.setParentName(parentDept.getName());

			// 设置管理员信息.
			org.setAdminer(emp.getNo());
			org.setAdminerName(emp.getName());
			org.Insert();

			// 增加到管理员.
			OrgAdminer oa = new OrgAdminer();
			oa.setFK_Emp(emp.getNo());
			oa.setOrgNo(this.getNo());
			oa.Insert();

			// 如果不是视图.
			if (DBAccess.IsView("Port_StationType") == false && isSetStationDemo == true) {

				/// 高层岗位.
				StationType st = new StationType();
				st.setNo(DBAccess.GenerGUID());
				st.setName("高层岗");
				st.setOrgNo(this.getNo());
				st.DirectInsert();

				Station sta = new Station();
				sta.setNo(DBAccess.GenerGUID());
				sta.setName("总经理");
				sta.setOrgNo(this.getNo());
				sta.setFK_StationType(st.getNo());
				sta.DirectInsert();

				/// 高层岗位.

				/// 中层岗.
				st = new StationType();
				st.setNo(DBAccess.GenerGUID());
				st.setName("中层岗");
				st.setOrgNo(this.getNo());
				st.DirectInsert();

				sta = new Station();
				sta.setNo(DBAccess.GenerGUID());
				sta.setName("财务部经理");
				sta.setOrgNo(this.getNo());
				sta.setFK_StationType(st.getNo());
				sta.DirectInsert();

				sta = new Station();
				sta.setNo(DBAccess.GenerGUID());
				sta.setName("研发部经理");
				sta.setOrgNo(this.getNo());
				sta.setFK_StationType(st.getNo());
				sta.DirectInsert();

				sta = new Station();
				sta.setNo(DBAccess.GenerGUID());
				sta.setName("市场部经理");
				sta.setOrgNo(this.getNo());
				sta.setFK_StationType(st.getNo());
				sta.DirectInsert();

				/// 中层岗.

				/// 基层岗.
				st = new StationType();
				st.setNo(DBAccess.GenerGUID());
				st.setName("基层岗");
				st.setOrgNo(this.getNo());
				st.DirectInsert();

				sta = new Station();
				sta.setNo(DBAccess.GenerGUID());
				sta.setName("会计岗");
				sta.setOrgNo(this.getNo());
				sta.setFK_StationType(st.getNo());
				sta.DirectInsert();

				sta = new Station();
				sta.setNo(DBAccess.GenerGUID());
				sta.setName("销售岗");
				sta.setOrgNo(this.getNo());
				sta.setFK_StationType(st.getNo());
				sta.DirectInsert();

				sta = new Station();
				sta.setNo(DBAccess.GenerGUID());
				sta.setName("程序员岗");
				sta.setOrgNo(this.getNo());
				sta.setFK_StationType(st.getNo());
				sta.DirectInsert();

				/// 基层岗.
			}

			// 返回他的检查信息，这个方法里，已经包含了自动创建独立组织的，表单树，流程树。
			// 自动他创建，岗位类型，岗位信息.
			String info = org.DoCheck();

			if (info.indexOf("err@") == 0) {
				return info;
			}

			return "设置成功.";

		} catch (Exception ex) {

			// 删除数据.
			DBAccess.RunSQL("DELETE FROM Port_Org WHERE No='" + this.getNo() + "'");
			// DBAccess.RunSQL("DELETE FROM WF_FlowSort WHERE OrgNo='"+this.getNo()+"'");
			// DBAccess.RunSQL("DELETE FROM WF_FlowSort WHERE OrgNo='"+this.getNo()+"'");

			return "err@" + ex.getMessage();
		}

	}
}