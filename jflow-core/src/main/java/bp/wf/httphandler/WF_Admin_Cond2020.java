package bp.wf.httphandler;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.difference.handler.WebContralBase;
import bp.wf.template.*;

/** 
 页面功能实体
*/
public class WF_Admin_Cond2020 extends WebContralBase
{

	/** 
	 构造函数
	*/
	public WF_Admin_Cond2020()
	{
	}
	/** 
	 初始化列表
	 
	 @return 
	*/
	public final String List_Init()
	{
		// BP.WF.Template.CondAttr.ToNodeID
		// Conds condes = new Conds();
		// condes.RetrieveAll();
		return "";
	}
	/** 
	 校验是否正确
	 
	 @return 
	 * @throws Exception 
	*/
	public final String List_DoCheck() throws Exception
	{
		String str = "";

		String mystr = this.GetRequestVal("ToNodeID");
		int toNodeID = this.getFK_Node();
		if (DataType.IsNullOrEmpty(mystr) == false)
		{
			toNodeID = Integer.parseInt(mystr);
		}

		//集合.
		Conds conds = new Conds();
		conds.Retrieve(CondAttr.FK_Node, this.getFK_Node(), CondAttr.ToNodeID, toNodeID, CondAttr.CondType, this.GetRequestValInt("CondType"), CondAttr.Idx);

		if (conds.size() == 0)
		{
			return "";
		}
		if (conds.size() == 1)
		{
			for (Cond item : conds.ToJavaList())
			{
				if (item.getHisDataFrom() == ConnDataFrom.CondOperator)
				{
					return "info@请继续增加条件";
				}
				else
				{
					return "条件成立.";
				}
			}
		}

		//遍历方向条件.
		for (Cond item : conds.ToJavaList())
		{
			if (item.getHisDataFrom() == ConnDataFrom.CondOperator)
			{
				str += " " + item.getOperatorValue();
			}
			else
			{
				str += " 1=1 "; // + item.AttrKey + item.FK_Operator + item.OperatorValue;
			}
		}

		String sql = "";
		switch (SystemConfig.getAppCenterDBType())
		{
			case MSSQL:
				sql = " SELECT TOP 1 No FROM Port_Emp WHERE " + str;
				break;
			case MySQL:
				sql = " SELECT No FROM Port_Emp WHERE " + str + "  limit 1 ";
				break;
			case Oracle:
			case KingBaseR3:
			case KingBaseR6:
			case DM:
				sql = " SELECT No FROM Port_Emp WHERE " + str + "  rownum <=1 ";
				break;
			default:
				return "err@没有做的数据库类型判断.";
		}

		try
		{
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			return "格式正确:<font color=blue>" + str + "</blue>";
		}
		catch (RuntimeException ex)
		{
			return "err@不符合规范. <font color=blue>" + str + "</font>";
		}
	}

}