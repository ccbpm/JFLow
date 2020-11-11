package bp.unittesting.sendcase;

import bp.wf.*;
import bp.en.*;
import bp.port.Depts;
import bp.da.*;
import bp.difference.SystemConfig;
import bp.web.*;
import bp.unittesting.*;
import bp.unittesting.*;

/** 
 部门编号为GUID模式下的发送
*/
public class SendByGUIDDept extends TestBase
{
	/** 
	 部门编号为GUID模式下的发送
	*/
	public SendByGUIDDept()
	{
		this.Title = "部门编号为GUID模式下的发送";
		this.DescIt = "以send024,send023,send005为基础测试，部门编号是GUID模式下的数据存储问题.";
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
	 部门编号为GUID模式下的发送
	 * @throws Exception 
	*/
	@Override
	public void Do() throws Exception
	{
		//重新装载演示环境.
		ReLoadDept();

		try
		{

				///把数据换成guid模式.
			Depts depts = new Depts();
			depts.RetrieveAll();

			String guid1 = "";
			for (bp.port.Dept item : depts.ToJavaList())
			{
				String deptNo = item.getNo();
				String guid = DBAccess.GenerGUID();
				if (item.getNo().equals("1"))
				{
					guid1 = guid;
				}

				sql = "UPDATE Port_Dept SET No='" + guid + "' WHERE No='" + deptNo + "'";
				DBAccess.RunSQL(sql);

				sql = "UPDATE Port_Emp SET FK_Dept='" + guid + "' WHERE FK_Dept='" + deptNo + "'";
				DBAccess.RunSQL(sql);

			}

			sql = "UPDATE Port_Dept SET ParentNo='" + guid1 + "' WHERE ParentNo='1'";
			DBAccess.RunSQL(sql);

				///

			String err = "";
			Flow fl = new Flow("023");
			fl.CheckRpt();
			fl.DoDelData();

			SystemConfig.DoClearCash();

			err = "@第Send023 错误.";
			Send023 se = new Send023();
			se.Do();

			fl = new Flow("024");
			fl.CheckRpt();
			err = "@第Send024 错误.";
			Send024 s2e = new Send024();
			s2e.Do();


			fl = new Flow("005");
			fl.CheckRpt();
			err = "@第Send005 错误.";
			Send005 s5 = new Send005();
			s5.Do();

			//重新装载演示环境.
			ReLoadDept();
		}
		catch (RuntimeException ex)
		{
			//重新装载演示环境.
			ReLoadDept();

			throw ex;
		}
	}
	/** 
	 重新装载环境.
	*/
	public final void ReLoadDept()
	{
		String sqls = "";
		sqls += "@DROP VIEW Port_DeptEmpStation";
		DBAccess.RunSQLs(sqls);

		String sqlscript = "";
		sqlscript = SystemConfig.getPathOfData() + "/Install/SQLScript/Port_Inc_CH_BMP.sql";
		DBAccess.RunSQLScript(sqlscript);
		SystemConfig.DoClearCash();
	}
}