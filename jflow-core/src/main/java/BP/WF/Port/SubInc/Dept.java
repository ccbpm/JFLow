package BP.WF.Port.SubInc;

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
public class Dept extends EntityNoName
{

		///#region 属性
	/** 
	 父节点编号
	 * @throws Exception 
	*/
	public final String getParentNo() throws Exception
	{
		return this.GetValStrByKey(DeptAttr.ParentNo);
	}
	public final void setParentNo(String value) throws Exception
	{
		this.SetValByKey(DeptAttr.ParentNo, value);
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

		map.Java_SetDepositaryOfEntity(Depositary.Application); //实体map的存放位置.
		map.Java_SetDepositaryOfMap(Depositary.Application); // Map 的存放位置.

		map.setAdjunctType(AdjunctType.None);

		map.AddTBStringPK(DeptAttr.No, null, "编号", true, false, 1, 30, 40);
		map.AddTBString(DeptAttr.Name, null,"名称", true, false, 0, 60, 200);
		map.AddTBString(DeptAttr.ParentNo, null, "父节点编号", true, false, 0, 30, 40);
		  //  map.AddTBString(DeptAttr.FK_Unit, "1", "隶属单位", false, false, 0, 50, 10);

		RefMethod rm = new RefMethod();
		rm.Title = "初始化子公司二级管理员";
		rm.Warning = "设置为子公司后，系统就会在流程树上分配一个目录节点.";
		rm.ClassMethodName = this.toString() + ".SetSubInc";
		rm.getHisAttrs().AddTBString("No", null, "子公司管理员编号", true, false, 0, 100, 100);
		map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	private void InitFlowSortTree() throws Exception
	{
		//设置流程树权限.
		BP.WF.Template.FlowSort fs = new BP.WF.Template.FlowSort();
		fs.setNo("Inc" + this.getNo());
		if (fs.RetrieveFromDBSources() != 0)
		{
			fs.setOrgNo(this.getNo());
			fs.Update();
			return;
		}

		//获得根目录节点.
		BP.WF.Template.FlowSort root = new BP.WF.Template.FlowSort();
		int i = root.Retrieve(BP.WF.Template.FlowSortAttr.ParentNo, "0");

		//设置流程树权限.
		fs.setName(this.getName());
		fs.setParentNo(root.getNo());
		fs.setOrgNo(this.getNo());
		fs.setIdx(999);
		fs.Save();


		//创建下一级目录.
		EntityTree en = fs.DoCreateSubNode();
		en.setName("流程目录1");
		en.Update();

		en = fs.DoCreateSubNode();
		en.setName("流程目录2");
		en.Update();

		//表单根目录.
		BP.Sys.FrmTree ftRoot = new BP.Sys.FrmTree();
		ftRoot.Retrieve(BP.WF.Template.FlowSortAttr.ParentNo, "0");


		//设置表单树权限.
		BP.Sys.FrmTree ft = new BP.Sys.FrmTree();
		ft.setNo("Inc" + this.getNo());
		if (ft.RetrieveFromDBSources() == 0)
		{
			ft.setName(this.getName());
			ft.setParentNo(ftRoot.getNo());
			ft.setOrgNo(this.getNo());
			ft.setIdx(999);
			ft.Insert();

			//创建两个目录.
			ft.DoCreateSubNode();
			ft.DoCreateSubNode();
		}
		else
		{
			ft.setName(this.getName());
			ft.setParentNo(ftRoot.getNo());
			ft.setOrgNo(this.getNo());
			ft.setIdx(999);
			ft.Update();
		}
	}

	public final String SetSubInc(String userNo) throws Exception
	{
		//检查是否有该用户.
		BP.Port.Emp emp = new BP.Port.Emp();
		emp.setNo(userNo);
		if (emp.RetrieveFromDBSources() == 0)
		{
			return "err@用户编号错误:" + userNo;
		}

		AdminEmp ad = new AdminEmp();
		ad.setNo(userNo + "@" + this.getNo());
		if (ad.RetrieveFromDBSources() == 1)
		{
			return "err@该用户已经是该公司的管理员了.";
		}

		ad.Copy(emp);
		ad.setNo(userNo + "@" + this.getNo()); //增加一个影子版本.
		ad.setRootOfDept(this.getNo());
		ad.setRootOfFlow("Inc" + this.getNo());
		ad.setRootOfForm("Inc" + this.getNo());
		ad.setUserType(1);
		ad.setUseSta(1);
		ad.Insert();

		//设置二级管理员.
		ad.setNo(userNo);
		if (ad.RetrieveFromDBSources() == 0)
		{
			ad.Copy(emp);
			ad.setRootOfDept(this.getNo());
			ad.setRootOfFlow("Inc" + this.getNo());
			ad.setRootOfForm("Inc" + this.getNo());
			ad.setUserType(1);
			ad.setUseSta(1);
			ad.Insert();
		}
		else
		{
			ad.setRootOfDept(this.getNo());
			ad.setRootOfFlow("Inc" + this.getNo());
			ad.setRootOfForm("Inc" + this.getNo());
			ad.setUserType(1);
			ad.setUseSta(1);
			ad.Update();
		}

		//初始化表单树，流程树.
		InitFlowSortTree();

		return "设置成功,[" + ad.getNo() + "," + ad.getName() + "]重新登录就可以看到.";
	}
}