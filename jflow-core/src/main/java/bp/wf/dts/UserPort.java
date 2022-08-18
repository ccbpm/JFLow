package bp.wf.dts;

import bp.da.*;
import bp.dts.DataIOEn;
import bp.dts.DoType;
import bp.dts.RunTimeType;

public class UserPort extends DataIOEn
{
	/**
	 调度人员,岗位,部门
	 */
	public UserPort()throws Exception
	{
		this.HisDoType = DoType.UnName;
		this.Title = "生成流程部门(运行在系统第一次安装时或者部门变化时)";
		this.HisRunTimeType = RunTimeType.UnName;
		this.FromDBUrl = DBUrlType.AppCenterDSN;
		this.ToDBUrl = DBUrlType.AppCenterDSN;
	}
	@Override
	public void Do() throws Exception {
		//执行调度部门。
		//bp.port.DTS.GenerDept gd = new bp.port.DTS.GenerDept();
		//gd.Do();
		// 调度人员信息。
		// Emp emp = new Emp(WebUser.getNo());
		// emp.DoDTSEmpDeptStation();
	}
}