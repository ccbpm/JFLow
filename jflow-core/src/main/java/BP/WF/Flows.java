package BP.WF;

import BP.DA.*;
import BP.Sys.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.Template.*;
import BP.WF.Data.*;
import BP.Web.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.math.*;

/** 
 流程集合
*/
public class Flows extends EntitiesNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 查询
	public static void GenerHtmlRpts()
	{
		Flows fls = new Flows();
		fls.RetrieveAll();

		for (Flow fl : fls)
		{
			fl.DoCheck();
			fl.GenerFlowXmlTemplete();
		}

		// 生成索引界面
		String path = SystemConfig.PathOfWorkDir + "\\VisualFlow\\DataUser\\FlowDesc\\";
		String msg = "";
		msg += "<html>";
		msg += "\r\n<title>.net工作流程引擎设计，流程模板</title>";

		msg += "\r\n<body>";

		msg += "\r\n<h1>驰骋流程模板网</h1> <br><a href=index.htm >返回首页</a> - <a href='http://ccFlow.org' >访问驰骋工作流程管理系统，工作流引擎官方网站</a> 流程系统建设请联系:QQ:793719823,Tel:18660153393<hr>";

		for (Flow fl : fls)
		{
			msg += "\r\n <h3><b><a href='./" + fl.No + "/index.htm' target=_blank>" + fl.Name + "</a></b> - <a href='" + fl.No + ".gif' target=_blank  >" + fl.Name + "流程图</a></h3>";

			msg += "\r\n<UL>";
			Nodes nds = fl.getHisNodes();
			for (Node nd : nds)
			{
				msg += "\r\n<li><a href='./" + fl.No + "/" + nd.getNodeID() + "_" + nd.getFlowName() + "_" + nd.getName() + "表单.doc' target=_blank>步骤" + nd.getStep() + ", - " + nd.getName() + "模板</a> -<a href='./" + fl.No + "/" + nd.getNodeID() + "_" + nd.getName() + "_表单模板.htm' target=_blank>Html版</a></li>";
			}
			msg += "\r\n</UL>";
		}
		msg += "\r\n</body>";
		msg += "\r\n</html>";

		try
		{
			String pathDef = SystemConfig.PathOfWorkDir + "\\VisualFlow\\DataUser\\FlowDesc\\" + SystemConfig.CustomerNo + "_index.htm";
			DataType.WriteFile(pathDef, msg);

			pathDef = SystemConfig.PathOfWorkDir + "\\VisualFlow\\DataUser\\FlowDesc\\index.htm";
			DataType.WriteFile(pathDef, msg);
			System.Diagnostics.Process.Start(SystemConfig.PathOfWorkDir + "\\VisualFlow\\DataUser\\FlowDesc\\");
		}
		catch (java.lang.Exception e)
		{
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 查询

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 查询
	/** 
	 查出来全部的自动流程
	*/
	public final void RetrieveIsAutoWorkFlow()
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
	*/
	public final void Retrieve(String flowSort)
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FlowAttr.FK_FlowSort, flowSort);
		qo.addOrderBy(FlowAttr.No);
		qo.DoQuery();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
	*/
	public Flows(String fk_sort)
	{
		this.Retrieve(FlowAttr.FK_FlowSort, fk_sort);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 得到实体
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new Flow();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Flow> ToJavaList()
	{
		return (List<Flow>)this;
	}
	/** 
	 转化成 list
	 
	 @return List
	*/
	public final ArrayList<Flow> Tolist()
	{
		ArrayList<Flow> list = new ArrayList<Flow>();
		for (int i = 0; i < this.Count; i++)
		{
			list.add((Flow)this[i]);
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}