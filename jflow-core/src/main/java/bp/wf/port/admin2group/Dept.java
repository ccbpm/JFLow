package bp.wf.port.admin2group;

import bp.da.*;
import bp.en.*;
import bp.web.*;
import bp.port.*;
import bp.sys.*;
import bp.wf.template.*;
import bp.wf.*;
import bp.sys.CCFormAPI;
import bp.wf.httphandler.WF_Admin_Template;
/** 
 部门
*/
public class Dept extends EntityTree
{

		///#region 属性
	/** 
	 父节点编号
	*/
	public final String getParentNo()
	{
		return this.GetValStrByKey(DeptAttr.ParentNo);
	}
	public final void setParentNo(String value)
	 {
		this.SetValByKey(DeptAttr.ParentNo, value);
	}
	/** 
	 组织编号
	*/
	public final String getOrgNo()
	{
		return this.GetValStrByKey(DeptAttr.OrgNo);
	}
	public final void setOrgNo(String value)
	 {
		this.SetValByKey(DeptAttr.OrgNo, value);
	}

		///#endregion


		///#region 构造函数
	/** 
	 部门
	*/
	public Dept()  {
	}
	/** 
	 部门
	 
	 param no 编号
	*/
	public Dept(String no) throws Exception {
		super(no);
	}

		///#endregion


		///#region 重写方法
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	/** 
	 Map
	*/
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
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
		rm.ClassMethodName = this.toString() + ".SetOrg";
		rm.getHisAttrs().AddTBString("adminer", null, "组织管理员编号", true, false, 0, 100, 100);
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "克隆独立组织";
		rm.Warning = "如果当前部门已经是独立组织，系统就会提示错误。";
		rm.ClassMethodName = this.toString() + ".DoCloneOrg";
		rm.getHisAttrs().AddTBString("adminer", null, "组织管理员编号", true, false, 0, 100, 100);
		rm.getHisAttrs().AddTBString("coneOrgNo", null, "被克隆的组织编号(14903)", true, false, 0, 100, 100);
		map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
	/** 
	 克隆组织
	 
	 param adminer
	 param orgNo
	 @return 
	*/
	public final String DoCloneOrg(String adminer, String cloneOrgNo) throws Exception {
		Org orgClone = new Org(cloneOrgNo);

		if (WebUser.getNo().equals("admin") == false)
		{
			return "err@非admin管理员，您无法执行该操作.";
		}

		//检查是否有该用户.
		Emp emp = new Emp();
		emp.setUserID(adminer);
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
			return "info@当前部门已经是独立组织了,您不能在克隆了.";
		}
		org.setName(this.getName()); //把部门名字改为组织名字.

		try
		{
			//设置父级信息.
			Dept parentDept = new Dept();
			if (this.getParentNo().equals("0") == true)
			{
				this.setParentNo(this.getNo());
			}

			parentDept.setNo(this.getParentNo());
			parentDept.Retrieve();

			org.setParentNo(this.getParentNo());
			org.setParentName(parentDept.getName());

			//设置管理员信息.
			org.setAdminer(emp.getUserID());
			org.setAdminerName(emp.getName());
			org.DirectInsert();

			//增加到管理员.
			OrgAdminer oa = new OrgAdminer();
			oa.setFK_Emp(emp.getUserID());
			oa.setOrgNo(this.getNo());
			oa.DirectDelete();
			oa.DirectInsert();

			//初始化流程树的根节点.
			FlowSort fsRoot = new FlowSort();
			fsRoot.setNo(this.getNo());
			fsRoot.setParentNo("1");
			fsRoot.setName(this.getName());
			fsRoot.setOrgNo(this.getNo());
			fsRoot.DirectUpdate();

			String info = "";

			//执行 clone...

			//查询出来被克隆的流程树.
			FlowSorts sorts = new FlowSorts();
			sorts.Retrieve(FlowSortAttr.OrgNo, cloneOrgNo, null);
			for (FlowSort sort : sorts.ToJavaList())
			{
				if (sort.getParentNo().equals("1") == true)
				{
					continue;
				}

				FlowSort fs = new FlowSort();
				fs.Copy(sort);
				fs.setName(sort.getName());
				fs.setParentNo(this.getNo());
				fs.setNo(DBAccess.GenerGUID(0, null, null));
				fs.setOrgNo(this.getNo());
				fs.DirectInsert();

				//查询出来模版，开始执行clone.
				Flows fls = new Flows();
				fls.Retrieve(FlowAttr.FK_FlowSort, sort.getNo(), null);
				for (Flow fl : fls.ToJavaList())
				{
					try
					{
						String fileName = bp.difference.SystemConfig.getPathOfTemp() + "" + DBAccess.GenerGUID(0, null, null) + ".xml";
						DataSet ds = fl.GetFlow(fileName);
						ds.WriteXml(fileName,XmlWriteMode.IgnoreSchema,ds);

						Flow flowNew = TemplateGlo.LoadFlowTemplate(fs.getNo(), fileName, ImpFlowTempleteModel.AsNewFlow);
						flowNew.setOrgNo(this.getNo());
						flowNew.DirectUpdate();
					}
					catch (RuntimeException ex)
					{
						info += "err@" + ex.getMessage();
					}
				}
			}

			//初始化frmTree的根节点.
			SysFormTree frmRoot = new SysFormTree();
			frmRoot.setNo(this.getNo());
			frmRoot.setParentNo("1");
			frmRoot.setName(this.getName());
			frmRoot.setOrgNo(this.getNo());
			frmRoot.DirectInsert();

			//查询出来被克隆的表单树
			SysFormTrees frmTrees = new SysFormTrees();
			sorts.Retrieve(FlowSortAttr.OrgNo, cloneOrgNo, null);
			for (SysFormTree sort : frmTrees.ToJavaList())
			{
				if (sort.getParentNo().equals("1") == true)
				{
					continue;
				}

				SysFormTree fs = new SysFormTree();
				fs.Copy(sort);
				fs.setName(sort.getName());
				fs.setParentNo(this.getNo());
				fs.setNo(DBAccess.GenerGUID(0, null, null));
				fs.setOrgNo(this.getNo());
				fs.DirectInsert();

				//查询出来模版，开始执行clone.
				MapDatas mds = new MapDatas();
				mds.Retrieve(MapDataAttr.FK_FormTree, sort.getNo(), null);
				for (MapData frm : mds.ToJavaList())
				{
					try
					{
						DataSet myds = CCFormAPI.GenerHisDataSet(frm.getNo(), frm.getName(), null);

						bp.wf.httphandler.WF_Admin_Template en = new WF_Admin_Template();
						en.ImpFrm("2", frm.getNo(), frm, myds, sort.getNo());
					}
					catch (RuntimeException ex)
					{
						info += "err@" + ex.getMessage();
					}
				}
			}

			return info;
		}
		catch (RuntimeException ex)
		{
			String sql = " delete from port_org where adminer = '" + adminer + "' ";
			DBAccess.RunSQL(sql);

			sql = "delete from port_orgAdminer where FK_Emp = '" + adminer + "'";
			DBAccess.RunSQL(sql);

			//删除流程&流程类别.
			FlowSorts fss = new FlowSorts();
			fss.Retrieve("OrgNo", this.getNo(), null);
			for (FlowSort fs : fss.ToJavaList())
			{
				Flows fls = new Flows();
				fls.Retrieve(FlowAttr.FK_FlowSort, fs.getNo(), null);

				//删除流程.
				for (Flow item : fls.ToJavaList())
				{
					item.DoDelete();
				}
				fs.Delete(); //删除.
			}

			//删除表单与表单类别.
			SysFormTrees fts = new SysFormTrees();
			fts.Retrieve("OrgNo", this.getNo(), null);
			for (SysFormTree ft : fts.ToJavaList())
			{
				MapDatas mds = new MapDatas();
				mds.Retrieve(MapDataAttr.FK_FormTree, ft.getNo(), null);

				//删除流程.
				for (MapData item : mds.ToJavaList())
				{
					item.Delete();
				}
				ft.Delete(); //删除.
			}

			//sql = "delete from wf_flowsort where orgNo = '" + this.No + "'";
			//DBAccess.RunSQL(sql);

			////SELECT* FROM sys_formtree WHERE OrgNo = ''
			//sql = "delete from sys_formtree where orgNo = '" + this.No + "'";
			//DBAccess.RunSQL(sql);

			return "err@" + ex.getMessage();
		}
	}
	/** 
	 设置组织
	 
	 param userNo 管理员编号
	 @return 
	*/
	public final String SetOrg(String adminer) throws Exception {

		if (WebUser.getNo().equals("admin") == false)
		{
			return "err@非admin管理员，您无法执行该操作.";
		}

		//检查是否有该用户.
		Emp emp = new Emp();
		emp.setUserID(adminer);
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
		Dept parentDept = new Dept();

		if (this.getParentNo().equals("0") == true)
		{
			this.setParentNo(this.getNo());
		}

		parentDept.setNo(this.getParentNo());
		parentDept.Retrieve();

		org.setParentNo(this.getParentNo());
		org.setParentName(parentDept.getName());

		//设置管理员信息.
		org.setAdminer(emp.getUserID());
		org.setAdminerName(emp.getName());
		org.Insert();

		//增加到管理员.
		OrgAdminer oa = new OrgAdminer();
		oa.setFK_Emp(emp.getUserID());
		oa.setOrgNo(this.getNo());
		oa.Insert();

		//如果不是视图.
		if (DBAccess.IsView("Port_StationType") == false)
		{

				///#region 高层岗位.
			StationType st = new StationType();
			st.setNo(DBAccess.GenerGUID(0, null, null));
			st.setName("高层岗");
			st.setOrgNo(this.getNo());
			st.DirectInsert();

			Station sta = new Station();
			sta.setNo(DBAccess.GenerGUID(0, null, null));
			sta.setName("总经理");
			sta.setOrgNo(this.getNo());
			sta.setFK_StationType(st.getNo());
			sta.DirectInsert();

				///#endregion 高层岗位.


				///#region 中层岗.
			st = new StationType();
			st.setNo(DBAccess.GenerGUID(0, null, null));
			st.setName("中层岗");
			st.setOrgNo(this.getNo());
			st.DirectInsert();

			sta = new Station();
			sta.setNo(DBAccess.GenerGUID(0, null, null));
			sta.setName("财务部经理");
			sta.setOrgNo(this.getNo());
			sta.setFK_StationType(st.getNo());
			sta.DirectInsert();


			sta = new Station();
			sta.setNo(DBAccess.GenerGUID(0, null, null));
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