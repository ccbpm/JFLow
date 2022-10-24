package bp.wf.dts;

import bp.da.*;
import bp.dts.DataIOEn;
import bp.dts.DoType;
import bp.dts.RunTimeType;
import bp.wf.*;
public class OutputSQLOfDeleteWork extends DataIOEn
{
	/**
	 流程时效考核
	 */
	public OutputSQLOfDeleteWork()throws Exception
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