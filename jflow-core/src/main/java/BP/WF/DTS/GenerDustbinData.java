package BP.WF.DTS;

import BP.DA.DBAccess;
import BP.DA.DataTable;
import BP.En.Method;
import BP.Sys.MapData;
import BP.Tools.StringHelper;
import BP.WF.Flow;
import BP.WF.Flows;
import BP.WF.WFState;

/** 
 Method 的摘要说明
 
*/
public class GenerDustbinData extends Method
{
	/** 
	 不带有参数的方法
	 
	*/
	public GenerDustbinData()
	{
		this.Title = "找出因为ccbpm内部的错误而产生的垃圾数据";
		this.Help = "系统不去自动修复它，需要手工的确定原因。";
		this.Icon = "<img src='/WF/Img/Btn/Card.gif'  border=0 />";
	}
	/** 
	 设置执行变量
	 
	 @return 
	*/
	@Override
	public void Init()
	{
		//this.Warning = "您确定要执行吗？";
		//HisAttrs.AddTBString("P1", null, "原密码", true, false, 0, 10, 10);
		//HisAttrs.AddTBString("P2", null, "新密码", true, false, 0, 10, 10);
		//HisAttrs.AddTBString("P3", null, "确认", true, false, 0, 10, 10);
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

		String msg = "";
		Flows fls = new Flows();
		fls.RetrieveAll();
		for (Flow fl : fls.ToJavaList())
		{
			String rptTable = "ND" + Integer.parseInt(fl.getNo()) + "Rpt";
			String fk_mapdata = "ND" + Integer.parseInt(fl.getNo()) + "01";
			MapData md = new MapData(fk_mapdata);

			String sql = "SELECT OID,Title,Rec,WFState,WFState FROM " + md.getPTable() + " WHERE WFState=" + WFState.Runing.getValue() + " AND OID IN (SELECT OID FROM " + rptTable + " WHERE WFState!=0 )";
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			if (dt.Rows.size() == 0)
			{
				continue;
			}

			msg += "@" + sql;
			//msg += "修复sql: UPDATE " + ndTable"  " ;
		}
		if (StringHelper.isNullOrEmpty(msg))
		{
			return "@能检测到的数据正常.";
		}

		BP.DA.Log.DefaultLogWriteLineInfo(msg);
		return "如下数据产生异常，开始节点的标示的草稿状态在实际工作中标示出来已经完成了:"+msg+" 以上的数据写入了Log文件中，请打开查看。";
	}
}