package BP.WF.DTS;

import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import BP.Port.*;
import BP.Sys.*;
import BP.WF.Data.*;
import BP.WF.Template.*;
import BP.DTS.*;
import BP.WF.*;
import java.io.*;
import java.time.*;

public class OutputSQLOfDeleteWork extends DataIOEn
{
	/** 
	 流程时效考核
	*/
	public OutputSQLOfDeleteWork()
	{
		this.HisDoType = DoType.UnName;
		this.Title = "生成删除节点数据的sql.";
		this.HisRunTimeType = RunTimeType.UnName;
		this.FromDBUrl = DBUrlType.AppCenterDSN;
		this.ToDBUrl = DBUrlType.AppCenterDSN;
	}
	@Override
	public void Do() throws Exception
	{
		String sql = this.GenerSqls();
	}
	public final String GenerSqls() throws Exception
	{
		Nodes nds = new Nodes();
		nds.RetrieveAll();
		String delSQL = "";
		for (Node nd : nds.ToJavaList())
		{
			delSQL += "\n DELETE FROM " + nd.getPTable() + "  ; ";
		}
		return delSQL;
	}
}