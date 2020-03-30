package BP.WF.Port.Admin2;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Web.*;
import BP.WF.*;
import BP.WF.Port.*;
import java.util.*;

/** 
 部门
*/
public class Dept extends EntityTree
{
		///#region 属性
	/** 
	 父节点编号
	*/
	public final String getParentNo() throws Exception
	{
		return this.GetValStrByKey(DeptAttr.ParentNo);
	}
	public final void setParentNo(String value) throws Exception
	{
		this.SetValByKey(DeptAttr.ParentNo, value);
	}
	/** 
	 组织编号
	*/
	public final String getOrgNo() throws Exception
	{
		return this.GetValStrByKey(DeptAttr.OrgNo);
	}
	public final void setOrgNo(String value) throws Exception
	{
		this.SetValByKey(DeptAttr.OrgNo, value);
	}
		///#endregion

		///#region 构造函数
	/** 
	 部门
	*/
	public Dept()
	{
	}
	/** 
	 部门
	 
	 @param no 编号
	*/
	public Dept(String no) throws Exception
	{
		super(no);
	}
		///#endregion

		///#region 重写方法
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	/** 
	 Map
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Port_Dept", "部门");

		map.setAdjunctType(AdjunctType.None);

		map.AddTBStringPK(DeptAttr.No, null, "编号", true, false, 1, 30, 40);
		map.AddTBString(DeptAttr.Name, null, "名称", true, false, 0, 60, 200);
		map.AddTBString(DeptAttr.ParentNo, null, "父节点编号", true, false, 0, 30, 40);
		map.AddTBString(DeptAttr.OrgNo, null, "隶属组织", true, false, 0, 100, 40);

		map.AddTBInt(DeptAttr.Idx, 0, "顺序号", true, false);


			//map.AddTBString(DeptAttr.FK_Unit, "1", "隶属单位", false, false, 0, 50, 10);

		RefMethod rm = new RefMethod();
		rm.Title = "设置为独立组织";
		rm.Warning = "如果当前部门已经是独立组织，系统就会提示错误。";
		rm.ClassMethodName = this.toString() + ".SetOrg";
		rm.getHisAttrs().AddTBString("adminer", null, "组织管理员编号", true, false, 0, 100, 100);
		map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 设置组织
	 
	 @param userNo 管理员编号
	 @return 
	*/
	public final String SetOrg(String adminer) throws Exception
	{
		if (WebUser.getNo().equals("admin") == false)
		{
			return "err@非admin管理员，您无法执行该操作.";
		}

		//检查是否有该用户.
		BP.Port.Emp emp = new BP.Port.Emp();
		emp.setNo(adminer);
		if (emp.RetrieveFromDBSources() == 0)
		{
			return "err@用户编号错误:" + adminer;
		}

		//检查该部门是否是独立组织.
		Org org = new Org();
		org.setNo(this.getNo());
		if (org.RetrieveFromDBSources() == 1)
		{
			/* 已经是独立组织了. */
			String info1 = org.DoCheck();
			return "info@当前部门已经是独立组织了,检查信息如下:" + info1;
		}
		org.setName(this.getName()); //把部门名字改为组织名字.

		//设置父级信息.
		BP.Port.Dept parentDept = new BP.Port.Dept();

		if (this.getParentNo().equals("0") == true)
		{
			this.setParentNo(this.getNo());
		}

		parentDept.setNo(this.getParentNo());
		parentDept.Retrieve();

		org.setParentNo(this.getParentNo());
		org.setParentName(parentDept.getName());

		//设置管理员信息.
		org.setAdminer(emp.getNo());
		org.setAdminerName(emp.getName());
		org.Insert();

		//如果不是视图.
		if (DBAccess.IsView("Port_StationType") == false)
		{
				///#region 高层岗位.
			BP.WF.Port.Admin2.StationType st = new StationType();
			st.setNo(DBAccess.GenerGUID());
			st.setName("高层岗");
			st.setOrgNo(this.getNo());
			st.DirectInsert();

			BP.WF.Port.Admin2.Station sta = new Station();
			sta.setNo(DBAccess.GenerGUID());
			sta.setName("总经理");
			sta.setOrgNo(this.getNo());
			sta.setFK_StationType(st.getNo());
			sta.DirectInsert();
				///#endregion 高层岗位.

				///#region 中层岗.
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
				///#endregion 中层岗.

				///#region 基层岗.
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

				///#endregion 基层岗.
		}

		// 返回他的检查信息，这个方法里，已经包含了自动创建独立组织的，表单树，流程树。
		// 自动他创建，岗位类型，岗位信息.
		String info = org.DoCheck();

		if (info.indexOf("err@") == 0)
		{
			return info;
		}

		return "设置成功.";


		//初始化表单树，流程树.
		//InitFlowSortTree();
		//return "设置成功,[" + ad.No + "," + ad.Name + "]重新登录就可以看到.";
	}
}