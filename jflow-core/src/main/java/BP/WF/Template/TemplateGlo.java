package BP.WF.Template;

import BP.Tools.DateUtils;
import BP.WF.*;
import BP.Web.WebUser;

import java.time.*;
import java.util.Date;

/** 
 流程模版的操作
*/
public class TemplateGlo
{
	/** 
	 创建一个流程模版
	 
	 @param flowSort 流程类别
	 @param flowName 名称
	 @param dsm 存储方式
	 @param ptable 物理量
	 @param flowMark 标记
	 @param flowVer 版本
	 @return 创建的流程编号
	 * @throws Exception 
	*/
	public static String NewFlow(String flowSort, String flowName, BP.WF.Template.DataStoreModel dsm, String ptable, String flowMark, String flowVer) throws Exception
	{
		//执行保存.
		BP.WF.Flow fl = new BP.WF.Flow();

		String flowNo = fl.DoNewFlow(flowSort, flowName, dsm, ptable, flowMark);
		fl.setNo(flowNo);
		fl.Retrieve();

	   FlowExt flowExt = new FlowExt(flowNo);
	   flowExt.setDesignerNo(WebUser.getNo());
	   flowExt.setDesignerName(WebUser.getName());
	   flowExt.setDesignTime(DateUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
	   flowExt.DirectSave();


		//如果为CCFlow模式则不进行写入Json串
		if (flowVer.equals("0"))
		{
			return flowNo;
		}

		//创建连线
		Direction drToNode = new Direction();
		drToNode.setFK_Flow(flowNo);
		drToNode.setNode(Integer.parseInt(Integer.parseInt(flowNo) + "01"));
		drToNode.setToNode(Integer.parseInt(Integer.parseInt(flowNo) + "02"));
		drToNode.Insert();

		//执行一次流程检查, 为了节省效率，把检查去掉了.
		fl.DoCheck();

		return flowNo;
	}
	/** 
	 创建一个节点
	 
	 @param flowNo 流程编号
	 @param x 位置x
	 @param y 位置y
	 @return 新的节点ID
	 * @throws Exception 
	*/

	public static int NewNode(String flowNo, int x, int y) throws Exception
	{
		return NewNode(flowNo, x, y, null);
	}

	public static int NewNode(String flowNo, int x, int y, String icon) throws Exception
	{
		BP.WF.Flow fl = new BP.WF.Flow(flowNo);
		BP.WF.Node nd = fl.DoNewNode(x, y, icon);
		return nd.getNodeID();
	}
	/** 
	 删除节点.
	 
	 @param nodeid
	 * @throws Exception 
	*/
	public static void DeleteNode(int nodeid) throws Exception
	{
		BP.WF.Node nd = new BP.WF.Node(nodeid);
		nd.Delete();
	}
}