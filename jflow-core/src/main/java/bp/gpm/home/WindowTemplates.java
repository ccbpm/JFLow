package bp.gpm.home;

import java.util.ArrayList;
import java.util.List;

import bp.da.DBAccess;
import bp.da.DataTable;
import bp.difference.SystemConfig;
import bp.en.EntitiesNoName;
import bp.en.Entity;
import bp.sys.CCBPMRunModel;

/** 
信息块s
*/
public class WindowTemplates extends EntitiesNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 构造
	/** 
	 信息块s
	*/
	public WindowTemplates()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new WindowTemplate();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<WindowTemplate> ToJavaList()
	{
		return (List<WindowTemplate>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<WindowTemplate> Tolist()
	{
		ArrayList<WindowTemplate> list = new ArrayList<WindowTemplate>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((WindowTemplate)this.get(i));
		}
		return list;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

	public final void InitDocs() throws Exception
	{
		//处理内容.
		for (WindowTemplate item : this.ToJavaList())
		{
			//文本的, 不用转化.
			if (item.getWinDocType().equals("0"))
			{
				continue;
			}

			//内置的.
			if (item.getWinDocType().equals(WinDocType.System))
			{
				String tempVar = item.getDocs();
				String exp = tempVar instanceof String ? (String)tempVar : null;
				exp = bp.wf.Glo.DealExp(exp, null);
				item.setDocs(exp);
			}

			//SQL列表. 
			if (item.getWinDocType().equals(WinDocType.SQLList)
					|| item.getWinDocType().equals(WinDocType.ChatZhuZhuang)
					|| item.getWinDocType().equals(WinDocType.ChatZheXian )
					|| item.getWinDocType().equals(WinDocType.ChatPie))
			{
				try
				{
					String tempVar2 = item.getDocs();
					String sql = tempVar2 instanceof String ? (String)tempVar2 : null;
				   // sql = sql.Replace("~", "'");
					sql = bp.wf.Glo.DealExp(sql, null);
					DataTable dt = DBAccess.RunSQLReturnTable(sql);
					item.setDocs(bp.tools.Json.ToJson(dt));
				}
				catch (RuntimeException ex)
				{
					item.setWinDocType(WinDocType.Html);
					item.setDocs("err@" + ex.getMessage() + " SQL=" + item.getDocs());
				}

			}
		}
	}

	@Override
	public int RetrieveAll() throws Exception
	{
		int i = this.RetrieveAllFromDBSource("Idx");
		if (i >= 1)
		{
			InitDocs();
			return i;
		}

		//初始化模板数据.
		InitData();

		//查询模数据.
		i = this.RetrieveAllFromDBSource("Idx");
		InitDocs();
		return i;
	}
	/** 
	 初始化数据
	 * @throws Exception 
	*/
	public final void InitData() throws Exception
	{
		WindowTemplate en = new WindowTemplate();


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 关于我们.
		en.setNo("001");
		en.setWinDocType(WinDocType.Html);
		en.setName("关于我们");
		String html = "";
		html += "<ul>";
		html += "<li>ccbpm是一个100%的开源软件,包含工作流程引擎、表单引擎、组织结构管理、菜单管理等敏捷开发的基础模块。</li>";
		html += "<li>该开源软件由驰骋公司从2003年开始研发到至今，经过多个版本迭代，并历经数千个项目于用户需求场景完成。</li>";
		html += "<li>设计严谨、考究抽象程度高、覆盖大部分客户应用需求，属于一款不可多得的应用国产的敏捷开发工具。</li>";
		html += "<li>源代码都发布在giee上，采用GPL开源协议进行开源，遵守GPL开源协议使用ccbpm合法有效。</li>";
		html += "<li>驰骋公司对外提供现场培训、技术支持、协助集成、协助项目落地服务，对小微企业，小企业，中等企业，大企业收费8,12,18,23三个等级的付费。</li>";
		html += "</ul>";
		en.setDocs(html);
		en.setMoreLinkModel(1);
		en.setColSpan(2);
		en.setIsDel(true);
		en.Insert();
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 关于我们.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 登录信息.
		en = new WindowTemplate();
		en.setNo("002");
		en.setName("登录信息");
		en.setWinDocType(WinDocType.System); //系统内置的.

		html = "<table>";
		html += "<tr>";
		html += " <td>帐号</td>";
		html += " <td>@WebUser.No</td>";
		html += "</tr>";

		html += "<tr>";
		html += " <td>姓名</td>";
		html += " <td>@WebUser.Name</td>";
		html += "</tr>";

		html += "<tr>";
		html += " <td>部门</td>";
		html += " <td>@WebUser.FK_DeptName</td>";
		html += "</tr>";
		en.setDocs(html);
		en.setColSpan(1);
		en.Insert();
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 登录信息.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 我的待办.
		en = new WindowTemplate();
		en.setNo("003");
		en.setName("我的待办");
		en.setWinDocType(WinDocType.ChatZhuZhuang); //柱状图.

		html = "SELECT FK_NodeText AS FlowName, COUNT(WorkID) as Num ";
		html += " FROM WF_GenerWorkerlist WHERE FK_Emp = '@WebUser.No' AND IsPass=0 GROUP BY FK_NodeText ";
		en.setDocs(html);
		en.setMoreLinkModel(1);
		en.setColSpan(4);
		en.Insert();
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 我的待办分布.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 全部流程.
		en = new WindowTemplate();
		en.setNo("004");
		en.setName("全部流程");
		en.setWinDocType(WinDocType.ChatZhuZhuang); //柱状图.

		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			en.setDocs("SELECT FlowName, COUNT(WorkID) AS Num  FROM WF_GenerWorkFlow WHERE WFState !=0 GROUP BY FlowName");
		}
		else
		{
			en.setDocs("SELECT FlowName, COUNT(WorkID) AS Num  FROM WF_GenerWorkFlow WHERE WFState !=0 AND OrgNo='@WebUser.OrgNo' GROUP BY FlowName");
		}

		en.setMoreLinkModel(1);
		en.setColSpan(2);
		en.Insert();
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 我的待办分布.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 我的未完成.
		en = new WindowTemplate();

	}
}
