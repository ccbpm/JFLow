package bp.wf.dts;

import bp.da.*;
import bp.en.*; import bp.en.Map;
import bp.*;
import bp.wf.*;

/** 
 Method 的摘要说明
*/
public class DTSDominInfo extends Method
{
	/** 
	 不带有参数的方法
	*/
	public DTSDominInfo()
	{
		this.Title = "生成域数据";
		this.Help = "生成域数据(未完成)";

		this.GroupName = "AD数据管理";

		// this.HisAttrs.AddTBString("Path", "C:/ccflow.Template", "生成的路径", true, false, 1, 1900, 200);
	}
	/** 
	 设置执行变量
	 
	 @return 
	*/
	@Override
	public void Init()
	{
	}
	/** 
	 当前的操纵员是否可以执行这个方法
	*/
	@Override
	public boolean getIsCanDo()
	{
		return true;
	}
	/** 
	 执行
	 
	 @return 返回执行结果
	*/
	@Override
	public Object Do()
	{
		return "功能未实现。";

//		String domainHost = "127.0.0.1";
//
//		String sqls = "";
//		sqls += "@DELETE FROM Port_Emp";
//		sqls += "@DELETE FROM Port_Dept";
//		sqls += "@DELETE FROM Port_Station";
//		sqls += "@DELETE FROM Port_DeptEmpStation";
//		DBAccess.RunSQLs(sqls);


		// 把部门导入里面去。

		//DirectoryEntry de = new DirectoryEntry("LDAP://" + domain, name, pass);
		//DirectorySearcher srch = new DirectorySearcher();
		//srch.Filter = ("(objectclass=User)");

		//srch.SearchRoot = de;
		//srch.SearchScope = SearchScope.Subtree;
		//srch.PropertiesToLoad.Add("sn");
		//srch.PropertiesToLoad.Add("givenName");
		//srch.PropertiesToLoad.Add("uid");
		//srch.PropertiesToLoad.Add("telephoneNumber");
		//srch.PropertiesToLoad.Add("employeeNumber");
		//foreach (SearchResult res in srch.FindAll())
		//{
		//    string[] strArray;
		//    String str;
		//    str = "";
		//    strArray = res.Path.Split(',');
		//    for (int j = strArray.length(); j > 0; j--)
		//    {
		//        if (strArray[j - 1].Substring(0, 3) == "OU=")
		//        {
		//            str = "└" + strArray[j - 1].replace("OU=", "");
		//        }
		//    }
		//}

		//return "生成成功，请打开 。<br>如果您想共享出来请压缩后发送到template＠ccbpm.cn";
	}
}
