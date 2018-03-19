package BP.WF.DTS;

import BP.DA.DBUrlType;
import BP.DTS.DataIOEn2;
import BP.DTS.DoType;
import BP.DTS.RunTimeType;

public class UserPort extends DataIOEn2
{
	/** 
	 调度人员,岗位,部门
	*/
	public UserPort()
	{
		this.HisDoType = DoType.UnName;
		this.Title = "生成流程部门(运行在系统第一次安装时或者部门变化时)";
		this.HisRunTimeType = RunTimeType.UnName;
		this.FromDBUrl = DBUrlType.AppCenterDSN;
		this.ToDBUrl = DBUrlType.AppCenterDSN;
	}
	@Override
	public void Do()
	{
		//执行调度部门。
		//BP.Port.DTS.GenerDept gd = new BP.Port.DTS.GenerDept();
		//gd.Do();
		// 调度人员信息。
		// Emp emp = new Emp(Web.WebUser.getNo());
		// emp.DoDTSEmpDeptStation();
	}
}