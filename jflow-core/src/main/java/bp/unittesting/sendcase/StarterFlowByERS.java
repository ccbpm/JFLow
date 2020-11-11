package bp.unittesting.sendcase;

import bp.wf.*;
import bp.en.*;
import bp.da.*;
import bp.web.*;
import bp.unittesting.*;
import bp.unittesting.*;

/** 
 为多人创建开始工作节点
*/
public class StarterFlowByERS extends TestBase
{
	/** 
	 部门编号为GUID模式下的发送
	*/
	public StarterFlowByERS()
	{
		this.Title = "为多人创建开始工作节点";
		this.DescIt = "以send024,send023,send005为基础测试，为多人创建开始工作节点.";
		this.editState = EditState.Passed;
	}


		///全局变量
	/** 
	 流程编号
	*/
	public String fk_flow = "";
	/** 
	 用户编号
	*/
	public String userNo = "";
	/** 
	 所有的流程
	*/
	public Flow fl = null;
	/** 
	 主线程ID
	*/
	public long workid = 0;
	/** 
	 发送后返回对象
	*/
	public SendReturnObjs objs = null;
	/** 
	 工作人员列表
	*/
	public GenerWorkerList gwl = null;
	/** 
	 流程注册表
	*/
	public GenerWorkFlow gwf = null;

		/// 变量

	/** 
	 
	*/
	@Override
	public void Do()
	{
		// string 
		this.fk_flow = "023";
		this.userNo = "zhoupeng";
		String toEmps = "zhanghaicheng,zhangyifan";
		String belontToDept = "2";


			///检查接受人的待办工作.
		sql = "SELECT * FROM WF_EmpWorks WHERE WorkID=" + this.workid;
		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		if (dt.Rows.size() != 2)
		{
			throw new RuntimeException("@应当有两个人的待办工作,现在是:" + dt.Rows.size() + "个.");
		}

		for (DataRow dr : dt.Rows)
		{
			String fk_emp = dr.getValue("FK_Emp").toString();

			if (fk_emp.equals("zhanghaicheng"))
			{
			}

			if (fk_emp.equals("zhangyifan"))
			{
			}

		}

			/// 检查接受人的待办工作.
	}
}