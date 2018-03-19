package BP;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import BP.DA.DataTable;
import BP.DA.Paras;
import BP.Sys.SystemConfig;

public class TestDb{
	
//	private SystemConfig sysConfig = null;
//	@Before
//	public void setUp() throws Exception {
//		SystemConfig.ReadConfigFile(this.getClass().getResourceAsStream("/conf/web.config"));
//	}
//	//@Test
//	public void test() throws Exception {
//		System.out.println(SystemConfig.getPathOfData());
//		
//	}
//	//@Test
//	public final void DataBaseAccess()
//	{
////		int result = BP.DA.DBAccess.RunSQL("DELETE FROM Port_Emp WHERE ID=1");
////		// 执行多个sql
////		String sqls = "DELETE FROM Port_Emp WHERE 1=2";
////		sqls += "@DELETE FROM Port_Emp WHERE 1=2";
////		sqls += "@DELETE FROM Port_Emp WHERE 1=2";
////		BP.DA.DBAccess.RunSQLs(sqls);
//////
////		//执行查询返回datatable.
////		String sql = "SELECT * FROM Port_Emp";
////		DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
////		System.out.println(dt.getValue(0, 0));
////		System.out.println(dt.getValue(0, 1));
////		//执行查询返回 string 值.
////		sql = "SELECT FK_Dept FROM Port_Emp WHERE No='AA'";
////		String fk_dept = BP.DA.DBAccess.RunSQLReturnString(sql);
////		System.out.println("fk_dept: "+fk_dept);
//////		//执行查询返回 int 值. 也可以返回float, string
////		sql = "SELECT count(*) as Num FROM Port_Emp ";
////		int empNum = BP.DA.DBAccess.RunSQLReturnValInt(sql);
////        System.out.println("empNum: "+empNum);
////		//运行存储过程.
////		String spName = "MySp";
////		BP.DA.DBAccess.RunSP(spName);
////
////		///#endregion 执行不带有参数.
////
////
//		///#region 执行带有参数.
//		// 执行Insert ,delete, update 语句.
//		// 已经明确数据库类型.
//		Paras ps = new Paras();
//		ps.SQL = "DELETE FROM Port_Emp WHERE No="+BP.Sys.SystemConfig.getAppCenterDBVarStr()+"UserNo";
//		ps.Add("UserNo", "egdsd");
//		BP.DA.DBAccess.RunSQL(ps);
////
//		// 不知道数据库类型.
////		ps = new Paras();
////		ps.SQL = "DELETE FROM Port_Emp WHERE No=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + "UserNo";
////		ps.Add("UserNo", "abc");
////		BP.DA.DBAccess.RunSQL(ps);
////
////
////		//执行查询返回datatable.
////		ps = new Paras();
////		ps.SQL = "SELECT * FROM Port_Emp WHERE FK_Dept=@DeptNoVar";
////		ps.Add("DeptNoVar", "0102");
////		DataTable dtDept = BP.DA.DBAccess.RunSQLReturnTable(ps);
////
////		//运行存储过程.
////		ps = new Paras();
////		ps.Add("DeptNoVar", "0102");
////		spName = "MySp";
////		BP.DA.DBAccess.RunSP(spName, ps);
////
////		///#endregion 执行带有参数.
//	}
//
//	//@Test
//	public final void EntityBaseApp()
//	{
//		BP.Port.Emp emp = new BP.Port.Emp();
//		try {
//			emp.CheckPhysicsTable();
//		} catch (Exception e) {
//			
//			e.printStackTrace();
//		}
////          检查物理表是否与Map一致 
////         *  1，如果没有这个物理表则创建。
////         *  2，如果缺少字段则创建。
////         *  3，如果字段类型不一直则删除创建，比如原来是int类型现在map修改成string类型。
////         *  4，map字段减少则不处理。
////         *  5，手工的向物理表中增加的字段则不处理。
////         *  6，数据源是视图字段不匹配则创建失败。
////         * 
//		emp.setNo("zhangsan");
//		emp.setName("张三");
//		emp.setFK_Dept("01");
//		emp.setPass("pub");
//		emp.Insert(); // 如果主键重复要抛异常。
//
//		///#region  保存的方式插入一条数据.
//		emp.setNo("zhangsan");
//		emp.setName("张三");
//		emp.setFK_Dept("01");
//		emp.setPass("pub");
//		emp.Save(); // 如果主键重复直接更新，不会抛出异常。
//
//		///#region  数据复制.
////        
////         * 如果一个实体与另外的一个实体两者的属性大致相同，就可以执行copy.
////         *  比如：在创建人员时，张三与李四两者只是编号与名称不同，只是改变不同的属性就可以执行相关的业务操作。
////         
//		BP.Port.Emp emp1 = new BP.Port.Emp("zhangsan");
//		emp = new BP.Port.Emp();
//		emp.Copy(emp1); // 同实体copy, 不同的实体也可以实现copy.
//		emp.setNo("lisi");
//		emp.setName("李四");
//		emp.Insert();
//		///#region 查询.
//		String msg = ""; // 查询这条数据.
//		BP.Port.Emp myEmp = new BP.Port.Emp();
//		myEmp.setNo("zhangsan");
//		if (myEmp.RetrieveFromDBSources() == 0) // RetrieveFromDBSources() 返回来的是查询数量.
//		{
//			System.out.println("没有查询到编号等于zhangsan的人员记录.");
//			return;
//		}
//		else
//		{
//			msg = "";
//			msg += "\n编号:" + myEmp.getNo();
//			msg += "\n名称:" + myEmp.getName();
//			msg += "\n密码:" + myEmp.getPass();
//			msg += "\n部门编号:" + myEmp.getFK_Dept();
//			msg += "\n部门名称:" + myEmp.getFK_DeptText();
//			System.out.println(msg);
//		}
////
//		myEmp = new BP.Port.Emp();
//		myEmp.setNo("zhangsan");
//		myEmp.Retrieve(); // 执行查询，如果查询不到则要抛出异常。
////
//		msg = "";
//		msg += "\n编号:" + myEmp.getNo();
//		msg += "\n名称:" + myEmp.getName();
//		msg += "\n密码:" + myEmp.getPass();
//		msg += "\n部门编号:" + myEmp.getFK_Dept();
//		msg += "\n部门名称:" + myEmp.getFK_DeptText();
//		System.out.println(msg);
//
//		// 删除操作。
//		emp = new BP.Port.Emp();
//		emp.setNo("zhangsan");
//		int delNum = emp.Delete(); // 执行删除。
//		if (delNum == 0)
//		{
//			System.out.println("删除 zhangsan 失败.");
//		}
//
//		if (delNum == 1)
//		{
//			System.out.println("删除 zhangsan 成功..");
//		}
//		if (delNum > 1)
//		{
//			System.out.println("不应该出现的异常。");
//		}
////		// 初试化实例后，执行删除，这种方式要执行两个sql.
//		emp = new BP.Port.Emp("zhangyifan"); // 事例化它.
//		emp.setName("张一帆123"); //改变属性.
//		emp.Update(); // 更新它，这个时间BP将会把所有的属性都要执行更新，UPDATA 语句涉及到各个列。
//	}
//	@Test
//	public final void GloBaseApp()
//	{
//		// 执行登录。
//		BP.Port.Emp emp = new BP.Port.Emp("guobaogeng");
//		BP.Port.WebUser.SignInOfGener(emp);
//
//		// 当前登录人员编号.
//		String currLoginUserNo = BP.Port.WebUser.getNo();
//		// 登录人员名称
//		String currLoginUserName = BP.Port.WebUser.getName();
//		// 登录人员部门编号.
//		String currLoginUserDeptNo = BP.Port.WebUser.getFK_Dept();
//		// 登录人员部门名称
//		String currLoginUserDeptName = BP.Port.WebUser.getFK_DeptName();
//
//		BP.Port.WebUser.Exit(); //执行退出.
//	}
}
