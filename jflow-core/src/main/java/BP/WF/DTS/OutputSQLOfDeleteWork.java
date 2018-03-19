package BP.WF.DTS;

import BP.DA.DBUrlType;
import BP.DTS.DataIOEn;
import BP.DTS.DoType;
import BP.DTS.RunTimeType;
import BP.Sys.PubClass;
import BP.WF.Node;
import BP.WF.Nodes;

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
		PubClass.ResponseWriteBlueMsg(sql.replace("\n", "<BR>"));
	}
	public final String GenerSqls()
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
///// <summary>
///// 流程中应用到的静态方法。
///// </summary>
//public class WFDTS
//{
//    /// <summary>
//    /// 流程统计分析
//    /// </summary>
//    /// <param name="fromDateTime"></param>
//    /// <returns></returns>
//    public static string InitFlows(string fromDateTime)
//    {
//        return null; /* 好像这个不再应用它了。*/
//        //Log.DefaultLogWriteLine(LogType.Info, Web.WebUser.Name + " ################# Start 执行统计 #####################");
//        ////删除部门错误的流程
//        ////DBAccess.RunSQL("DELETE FROM WF_BadWF WHERE BadFlag='FlowDeptBad'");
//        //fromDateTime = "2004-01-01 00:00";
//        //Flows fls = new Flows();
//        //fls.RetrieveAll();
//        //CHOfFlow fs = new CHOfFlow();
//        //foreach (Flow fl in fls)
//        //{
//        //    Node nd = fl.HisStartNode;
//        //    try
//        //    {
//        //        string sql = "INSERT INTO WF_CHOfFlow SELECT OID WorkID, " + fl.No + " as FK_Flow, WFState, ltrim(rtrim(Title)) as Title, Rec as FK_Emp,"
//        //            + " RDT, CDT, 0 as SpanDays,'' FK_Dept,"
//        //            + "'' as FK_Dept,'' AS FK_NY,'' as FK_AP,'' AS FK_ND, '' AS FK_YF, Rec ,'' as FK_XJ, '' as FK_Station   "
//        //            + " FROM " + nd.HisWork.getEnMap().getPhysicsTable() + " WHERE RDT>='" + fromDateTime + "' AND OID NOT IN ( SELECT WorkID FROM WF_CHOfFlow  )";
//        //        DBAccess.RunSQL(sql);
//        //    }
//        //    catch (Exception ex)
//        //    {
//        //        throw new Exception(fl.Name + "   " + nd.Name + "" + ex.Message);
//        //    }
//        //}
//        //DBAccess.RunSP("WF_UpdateCHOfFlow");
//        //Log.DefaultLogWriteLine(LogType.Info, Web.WebUser.Name + " End 执行统计调度");
//        //return "";
//    }
//}



