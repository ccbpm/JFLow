package BP.WF;

import BP.DA.*;
import BP.Sys.*;
import BP.En.*;
import BP.WF.Template.*;
import java.util.*;


/** 
 流程集合
*/
public class Flows extends EntitiesNoName
{
		///#region 查询
	public static void GenerHtmlRpts() throws Exception
	{
		Flows fls = new Flows();
		fls.RetrieveAll();

		for (Flow fl : fls.ToJavaList())
		{
			fl.DoCheck();
			fl.GenerFlowXmlTemplete();
		}

		// 生成索引界面
		String path = SystemConfig.getPathOfWorkDir() + "\\VisualFlow\\DataUser\\FlowDesc\\";
		String msg = "";
		msg += "<html>";
		msg += "\r\n<title>.net工作流程引擎设计，流程模板</title>";

		msg += "\r\n<body>";

		msg += "\r\n<h1>驰骋流程模板网</h1> <br><a href=index.htm >返回首页</a> - <a href='http://ccFlow.org' >访问驰骋工作流程管理系统，工作流引擎官方网站</a> 流程系统建设请联系:QQ:793719823,Tel:18660153393<hr>";

		for (Flow fl : fls.ToJavaList())
		{
			msg += "\r\n <h3><b><a href='./" + fl.getNo() + "/index.htm' target=_blank>" + fl.getName() + "</a></b> - <a href='" + fl.getNo() + ".gif' target=_blank  >" + fl.getName() + "流程图</a></h3>";

			msg += "\r\n<UL>";
			Nodes nds = fl.getHisNodes();
			for (Node nd : nds.ToJavaList())
			{
				msg += "\r\n<li><a href='./" + fl.getNo() + "/" + nd.getNodeID() + "_" + nd.getFlowName() + "_" + nd.getName() + "表单.doc' target=_blank>步骤" + nd.getStep() + ", - " + nd.getName() + "模板</a> -<a href='./" + fl.getNo() + "/" + nd.getNodeID() + "_" + nd.getName() + "_表单模板.htm' target=_blank>Html版</a></li>";
			}
			msg += "\r\n</UL>";
		}
		msg += "\r\n</body>";
		msg += "\r\n</html>";

		try
		{
			String pathDef = SystemConfig.getPathOfWorkDir() + "\\VisualFlow\\DataUser\\FlowDesc\\" + SystemConfig.getCustomerNo() + "_index.htm";
			DataType.WriteFile(pathDef, msg);

			pathDef = SystemConfig.getPathOfWorkDir() + "\\VisualFlow\\DataUser\\FlowDesc\\index.htm";
			DataType.WriteFile(pathDef, msg);
		}
		catch (java.lang.Exception e)
		{
		}
	}

		///#endregion 查询


		///#region 查询
	/** 
	 查出来全部的自动流程
	 * @throws Exception 
	*/
	public final void RetrieveIsAutoWorkFlow() throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FlowAttr.FlowType, 1);
		qo.addOrderBy(FlowAttr.No);
		qo.DoQuery();
	}
	/** 
	 查询出来全部的在生存期间内的流程
	 
	 @param flowSort 流程类别
	 @param IsCountInLifeCycle 是不是计算在生存期间内 true 查询出来全部的 
	 * @return 
	 * @throws Exception 
	*/
	public final int Retrieve(String flowSort) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FlowAttr.FK_FlowSort, flowSort);
		qo.addOrderBy(FlowAttr.No);
		return qo.DoQuery();
	}

		///#endregion


		///#region 构造方法
	/** 
	 工作流程
	*/
	public Flows()
	{
	}
	/** 
	 工作流程
	 
	 @param fk_sort
	 * @throws Exception 
	*/
	public Flows(String fk_sort) throws Exception
	{
		this.Retrieve(FlowAttr.FK_FlowSort, fk_sort);
	}

		///#endregion


		///#region 得到实体
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getNewEntity()
	{
		return new Flow();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Flow> ToJavaList()
	{
		return (List<Flow>)(Object)this;
	}
	/** 
	 转化成 list
	 
	 @return List
	*/
	public final ArrayList<Flow> Tolist()
	{
		ArrayList<Flow> list = new ArrayList<Flow>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Flow)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}