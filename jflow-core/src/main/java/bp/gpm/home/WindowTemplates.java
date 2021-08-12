package bp.gpm.home;

import java.util.ArrayList;
import java.util.List;

import bp.da.DBAccess;
import bp.da.DataTable;
import bp.difference.SystemConfig;
import bp.en.EntitiesNoName;
import bp.en.Entity;
import bp.gpm.home.windowext.DtlAttr;
import bp.gpm.home.windowext.HtmlVarDtl;
import bp.gpm.home.windowext.HtmlVarDtls;
import bp.gpm.home.windowext.TabDtl;
import bp.gpm.home.windowext.TabDtls;
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
			if (item.getWinDocModel().equals(WinDocModel.Html))
			{
				continue;
			}

			//内置的.
			if (item.getWinDocModel().equals(WinDocModel.System))
			{
				String tempVar = item.getDocs();
				String exp = tempVar instanceof String ? (String)tempVar : null;
				exp = bp.wf.Glo.DealExp(exp, null);
				item.setDocs(exp);
			}
			//HtmlVar 变量字段.
			if (item.getWinDocModel().equals(WinDocModel.HtmlVar))
			{
				HtmlVarDtls dtls = new HtmlVarDtls();
				dtls.Retrieve(DtlAttr.RefWindowTemplate, item.getNo());

				for (HtmlVarDtl dtl : dtls.ToJavaList())
				{
					Object tempVar = dtl.getExp0();
					String sql = tempVar instanceof String ? (String)tempVar : null;
					sql = sql.replace("~", "'");
					sql = bp.wf.Glo.DealExp(sql, null);
					try
					{
						dtl.setExp0(DBAccess.RunSQLReturnStringIsNull(sql, "0"));
					}
					catch (RuntimeException ex)
					{
						dtl.setExp0("err@" + ex.getMessage());
					}
				}
				item.setDocs(dtls.ToJson());
				continue;
			}
			//tab 标签页.
			if (item.getWinDocModel().equals(WinDocModel.Tab))
			{
				TabDtls dtls = new TabDtls();
				dtls.Retrieve(DtlAttr.RefWindowTemplate, item.getNo());

				for (TabDtl dtl : dtls.ToJavaList())
				{
					Object tempVar = dtl.getExp0();
					String sql = tempVar instanceof String ? (String)tempVar : null;
					sql = sql.replace("~", "'");
					sql = bp.wf.Glo.DealExp(sql, null);
					try
					{
						dtl.setExp0( DBAccess.RunSQLReturnStringIsNull(sql, "0"));
					}
					catch (RuntimeException ex)
					{
						dtl.setExp0("err@" + ex.getMessage());
					}
				}
				item.setDocs(dtls.ToJson());
				continue;
			}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 扇形百分比.
			if (item.getWinDocModel().equals(WinDocModel.ChartRate)) //sql列表.
			{
				try
				{
					//分子
					Object tempVar2 = item.GetValStringByKey(WindowTemplateAttr.SQLOfFZ);
					String sql = tempVar2 instanceof String ? (String)tempVar2 : null;
					sql = sql.replace("~", "'");
					sql = bp.wf.Glo.DealExp(sql, null);
					String val = DBAccess.RunSQLReturnString(sql);
					item.SetValByKey(WindowTemplateAttr.SQLOfFZ, val);

					//分母
					Object tempVar3 = item.GetValStringByKey(WindowTemplateAttr.SQLOfFM);
					sql = tempVar3 instanceof String ? (String)tempVar3 : null;
					sql = sql.replace("~", "'");
					sql = bp.wf.Glo.DealExp(sql, null);
					val = DBAccess.RunSQLReturnString(sql);
					item.SetValByKey(WindowTemplateAttr.SQLOfFM, val);
				}
				catch (RuntimeException ex)
				{
					item.setWinDocModel(WinDocModel.Html);
					item.setDocs("err@" + ex.getMessage() + " SQL=" + item.getDocs());
				}
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 扇形百分比.

			//SQL列表. 
			if (item.getWinDocModel().equals(WinDocModel.Table) //sql列表.
                    || item.getWinDocModel().equals(WinDocModel.ChartLine) //sql柱状图
                    || item.getWinDocModel().equals(WinDocModel.ChartZZT) //折线图.
                    || item.getWinDocModel().equals(WinDocModel.ChartRing) //环形图.
                    || item.getWinDocModel().equals(WinDocModel.ChartPie)) //饼图.
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
			InitHomePageData();
			return i;
		}

		//初始化模板数据.
		InitHomePageData();

		//查询模数据.
		i = this.RetrieveAllFromDBSource("Idx");
		InitDocs();
		return i;
	}
	/** 
	 初始化数据
	 * @throws Exception 
	*/
	public final void InitHomePageData() throws Exception
	{
		WindowTemplate en = new WindowTemplate();


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 关于我们.
		en.setNo("001");
		en.setWinDocModel(WinDocModel.Html);
		en.setPageID("Home");
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
		en.setPageID("Home");
		en.setWinDocModel(WinDocModel.System); //系统内置的.

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
		en.setPageID("Home");
		en.setWinDocModel(WinDocModel.ChartLine); //柱状图.

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
		en.setPageID("Home");
		en.setWinDocModel(WinDocModel.ChartLine); //柱状图.

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
		///#region 我的未完成.
		en = new WindowTemplate();
		en.setPageID("Home");
		en.setWinDocModel(WinDocModel.ChartLine); //.
		en.setNo("005");
		en.setName("未完成");
		html = "SELECT FlowName, COUNT(WorkID) AS Num FROM WF_GenerWorkFlow  WHERE WFState = 2 ";
		html += "and Emps like '%@WebUser.No%' GROUP BY FlowName";
		en.setDocs(html);
		en.setMoreLinkModel(1);
		en.setColSpan(4);

		en.Insert();
		//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion 我的未完成.

		//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 我的发起.
		en = new WindowTemplate();
		en.setPageID("Home");
		en.setNo("006");
		en.setName("我的发起");
		en.setWinDocModel(WinDocModel.ChartPie); //柱状图.

		if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
		{
			en.setDocs("SELECT FlowName, COUNT(WorkID) AS Num  FROM WF_GenerWorkFlow WHERE WFState !=0 AND Starter='@WebUser.No'  GROUP BY FlowName");
		}
		else
		{
			en.setDocs("SELECT FlowName, COUNT(WorkID) AS Num  FROM WF_GenerWorkFlow WHERE WFState !=0 AND Starter='@WebUser.No' AND OrgNo='@WebUser.OrgNo' GROUP BY FlowName");
		}

		en.setMoreLinkModel(1);
		en.setColSpan(1);
		en.Insert();
		//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion 我的发起.

		//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#region 我参与的.
		en = new WindowTemplate();
		en.setPageID("Home");
		en.setNo("007");
		en.setName("我参与的");
		en.setWinDocModel(WinDocModel.ChartPie); //柱状图.

		if (SystemConfig.getCCBPMRunModel() ==CCBPMRunModel.Single)
		{
			en.setDocs("SELECT FlowName, COUNT(WorkID) AS Num  FROM WF_GenerWorkFlow WHERE WFState !=0 AND Emps LIKE  '%@WebUser.No,%'  GROUP BY FlowName");
		}
		else
		{
			en.setDocs("SELECT FlowName, COUNT(WorkID) AS Num  FROM WF_GenerWorkFlow WHERE WFState !=0 AND Emps LIKE '%@WebUser.No,%' AND OrgNo='@WebUser.OrgNo' GROUP BY FlowName");
		}

		en.setMoreLinkModel(1);
		en.setColSpan(2);

		en.Insert();
		//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion 我的发起.

		//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 流程实例月份柱状图.
		en = new WindowTemplate();
		en.setPageID("Home");
		en.setNo("008");
		en.setName("月统计发起");
		en.setWinDocModel(WinDocModel.ChartLine);
		html = "SELECT FK_NY AS FlowName, COUNT(WorkID) AS Num  FROM WF_GenerWorkFlow WHERE WFState !=0 GROUP BY FK_NY";
		en.setDocs(html);
		en.setMoreLinkModel(1);
		en.setColSpan(4);
		en.Insert();
		//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
					///#endregion 流程实例月份柱状图.
		en = new WindowTemplate();

	}
}
