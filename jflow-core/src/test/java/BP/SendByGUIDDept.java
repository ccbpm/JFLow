package BP;

import BP.DA.DBAccess;
import BP.Port.Depts;
import BP.WF.Flow;
import BP.WF.SendReturnObjs;
import BP.WF.Entity.GenerWorkFlow;
import BP.WF.Entity.GenerWorkerList;

/** 
 部门编号为GUID模式下的发送
 
*/
public class SendByGUIDDept
{
	/** 
	 部门编号为GUID模式下的发送
	 
	*/
	public SendByGUIDDept()
	{
//		this.Title = "部门编号为GUID模式下的发送";
//		this.DescIt = "以send024,send023,send005为基础测试，部门编号是GUID模式下的数据存储问题.";
//		this.EditState = CT.EditState.Passed;
	}

		///#region 全局变量
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
		///#endregion 变量

	/** 
	 部门编号为GUID模式下的发送
	 
	*/
	//@Override
	public void Do()
	{
		//重新装载演示环境.
		ReLoadDept();

		try
		{
			///#region 把数据换成guid模式.
			BP.Port.Depts depts = new Depts();
			depts.RetrieveAll();

			String guid1 = "";
			for (BP.Port.Dept item : Depts.convertDepts(depts))
			{
				String deptNo = item.getNo();
				String guid = DBAccess.GenerGUID();
				if (item.getNo().equals("1"))
				{
					guid1 = guid;
				}

				String sql = "UPDATE Port_Dept SET No='" + guid + "' WHERE No='" + deptNo + "'";
				DBAccess.RunSQL(sql);

				sql = "UPDATE Port_Emp SET FK_Dept='" + guid + "' WHERE FK_Dept='" + deptNo + "'";
				DBAccess.RunSQL(sql);

			 
			}

			String sql = "UPDATE Port_Dept SET ParentNo='" + guid1 + "' WHERE ParentNo='1'";
			DBAccess.RunSQL(sql);
			///#endregion

			String err = "";
			Flow fl = new Flow("023");
			try {
				fl.CheckRpt();
			} catch (Exception e) {
				e.printStackTrace();
			}
			fl.DoDelData();

			BP.Sys.SystemConfig.DoClearCash_del();

			err = "@第Send023 错误.";
			Send023 se = new Send023();
			se.Do();

			fl = new Flow("024");
			try {
				fl.CheckRpt();
			} catch (Exception e) {
				e.printStackTrace();
			}
			err = "@第Send024 错误.";
			Send024 s2e = new Send024();
			s2e.Do();


			fl = new Flow("005");
			try {
				fl.CheckRpt();
			} catch (Exception e) {
				e.printStackTrace();
			}
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
		String sqls = "@DROP VIEW Port_EmpDept";
		sqls += "@DROP VIEW Port_EmpStation";
		BP.DA.DBAccess.RunSQLs(sqls);

		String sqlscript = "";
		sqlscript = BP.Sys.SystemConfig.getPathOfData() + "Install/SQLScript/Port_Inc_CH.sql";
		BP.DA.DBAccess.RunSQLScript(sqlscript);
		BP.Sys.SystemConfig.DoClearCash_del();
	}
}