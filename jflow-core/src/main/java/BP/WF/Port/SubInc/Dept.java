package BP.WF.Port.SubInc;

import BP.DA.*;
import BP.En.*;
import BP.Web.*;
import BP.WF.*;
import BP.WF.Port.*;
import java.util.*;

/** 
 部门
*/
public class Dept extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
	public Dept(String no)
	{
		super(no);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 重写方法
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()
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
		if (this._enMap != null)
		{
			return this._enMap;
		}

		Map map = new Map("Port_Dept", "部门");

		map.Java_SetDepositaryOfEntity(Depositary.Application); //实体map的存放位置.
		map.Java_SetDepositaryOfMap(Depositary.Application); // Map 的存放位置.

		map.AdjunctType = AdjunctType.None;

		map.AddTBStringPK(DeptAttr.No, null, "编号", true, false, 1, 30, 40);
		map.AddTBString(DeptAttr.Name, null,"名称", true, false, 0, 60, 200);
		map.AddTBString(DeptAttr.ParentNo, null, "父节点编号", true, false, 0, 30, 40);
		  //  map.AddTBString(DeptAttr.FK_Unit, "1", "隶属单位", false, false, 0, 50, 10);

		RefMethod rm = new RefMethod();
		rm.Title = "初始化子公司二级管理员";
		rm.Warning = "设置为子公司后，系统就会在流程树上分配一个目录节点.";
		rm.ClassMethodName = this.toString() + ".SetSubInc";
		rm.HisAttrs.AddTBString("No", null, "子公司管理员编号", true, false, 0, 100, 100);
		map.AddRefMethod(rm);

		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	private void InitFlowSortTree()
	{
		//设置流程树权限.
		BP.WF.Template.FlowSort fs = new WF.Template.FlowSort();
		fs.No = "Inc" + this.No;
		if (fs.RetrieveFromDBSources() != 0)
		{
			fs.setOrgNo(this.No);
			fs.Update();
			return;
		}

		//获得根目录节点.
		BP.WF.Template.FlowSort root = new Template.FlowSort();
		int i = root.Retrieve(BP.WF.Template.FlowSortAttr.ParentNo, "0");

		//设置流程树权限.
		fs.Name = this.Name;
		fs.ParentNo = root.No;
		fs.setOrgNo(this.No);
		fs.Idx = 999;
		fs.Save();


		//创建下一级目录.
		EntityTree en = fs.DoCreateSubNode();
		en.Name = "流程目录1";
		en.Update();

		en = fs.DoCreateSubNode();
		en.Name = "流程目录2";
		en.Update();

		//表单根目录.
		BP.Sys.FrmTree ftRoot = new Sys.FrmTree();
		ftRoot.Retrieve(BP.WF.Template.FlowSortAttr.ParentNo, "0");


		//设置表单树权限.
		BP.Sys.FrmTree ft = new Sys.FrmTree();
		ft.No = "Inc" + this.No;
		if (ft.RetrieveFromDBSources() == 0)
		{
			ft.Name = this.Name;
			ft.ParentNo = ftRoot.No;
			ft.OrgNo = this.No;
			ft.Idx = 999;
			ft.Insert();

			//创建两个目录.
			ft.DoCreateSubNode();
			ft.DoCreateSubNode();
		}
		else
		{
			ft.Name = this.Name;
			ft.ParentNo = ftRoot.No;
			ft.OrgNo = this.No;
			ft.Idx = 999;
			ft.Update();
		}
	}

	public final String SetSubInc(String userNo)
	{
		//检查是否有该用户.
		BP.Port.Emp emp = new BP.Port.Emp();
		emp.No = userNo;
		if (emp.RetrieveFromDBSources() == 0)
		{
			return "err@用户编号错误:" + userNo;
		}

		AdminEmp ad = new AdminEmp();
		ad.No = userNo + "@" + this.No;
		if (ad.RetrieveFromDBSources() == 1)
		{
			return "err@该用户已经是该公司的管理员了.";
		}

		ad.Copy(emp);
		ad.No = userNo + "@" + this.No; //增加一个影子版本.
		ad.setRootOfDept(this.No);
		ad.setRootOfFlow("Inc" + this.No);
		ad.setRootOfForm("Inc" + this.No);
		ad.setUserType(1);
		ad.setUseSta(1);
		ad.Insert();

		//设置二级管理员.
		ad.No = userNo;
		if (ad.RetrieveFromDBSources() == 0)
		{
			ad.Copy(emp);
			ad.setRootOfDept(this.No);
			ad.setRootOfFlow("Inc" + this.No);
			ad.setRootOfForm("Inc" + this.No);
			ad.setUserType(1);
			ad.setUseSta(1);
			ad.Insert();
		}
		else
		{
			ad.setRootOfDept(this.No);
			ad.setRootOfFlow("Inc" + this.No);
			ad.setRootOfForm("Inc" + this.No);
			ad.setUserType(1);
			ad.setUseSta(1);
			ad.Update();
		}

		//初始化表单树，流程树.
		InitFlowSortTree();

		return "设置成功,[" + ad.No + "," + ad.Name + "]重新登录就可以看到.";
	}
}