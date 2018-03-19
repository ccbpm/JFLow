package cn.jflow.common.model;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.Port.Emp;
import BP.Sys.OSModel;
import BP.Tools.StringHelper;
import BP.WF.Flow;
import BP.WF.Glo;
import BP.WF.Node;
import BP.Web.WebUser;
import cn.jflow.common.util.ContextHolderUtils;
import cn.jflow.system.ui.core.Label;

public class TestFlowModel extends BaseModel{
	
	public StringBuilder pub;
	
	public TestFlowModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
		pub = new StringBuilder();
	}
	
	private void appendPub(String str){
		pub.append(str);
	}

	public final String getLang()
	{
		return getParameter("Lang");
	}
	public final String getGloSID()
	{
		return getParameter("GloSID");
	}
	
	public final void loadPage() throws IOException
	{
		//if (this.GloSID != BP.WF.Glo.GloSID)
		//{
		//    this.Response.Write("全局的安全验证码错误,或者您没有设置,请在Web.config中的appsetting节点里设置GloSID 的值.");
		//    return;
		//}

		BP.Sys.SystemConfig.DoClearCash();
		BP.WF.Dev2Interface.Port_Login("admin");
		//BP.WF.Dev2Interface.Port_Login("admin", this.SID);

		if (this.getFK_Flow() == null)
		{
			appendPub(AddFieldSet("关于流程测试"));
			appendPub(AddUL());
			appendPub(AddLi("现在是流程测试状态，此功能紧紧提供给流程设计人员使用。"));
			appendPub(AddLi("提供此功能的目的是，快速的让各个角色人员登录，以便减少登录的繁琐麻烦。"));
			appendPub(AddLi("点左边的流程列表后，系统自动显示能够发起此流程的工作人员，点一个工作人员就直接登录了。"));
			appendPub(AddULEnd());
			appendPub(AddFieldSetEnd());
			return;
		}

		if (this.getRefNo() != null)
		{
			Emp emp = new Emp(this.getRefNo());
			WebUser.SignInOfGenerLang(emp, this.getLang());
			get_request().getSession().setAttribute("FK_Flow", this.getFK_Flow());
			if (getParameter("Type") != null)
			{
				if (getParameter("IsWap").equals("1"))
				{
					ContextHolderUtils.getResponse().sendRedirect("../WAP/MyFlow.htm?FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + Integer.parseInt(this.getFK_Flow()) + "01");
				}
				else
				{
					ContextHolderUtils.getResponse().sendRedirect("../MyFlow.htm?FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + Integer.parseInt(this.getFK_Flow()) + "01");
				}
			}
			else
			{
				ContextHolderUtils.getResponse().sendRedirect("../Port/Home.htm?FK_Flow=" + this.getFK_Flow());
			}
			return;
		}

		WebUser.setSysLang(this.getLang());
		Flow fl = new Flow(this.getFK_Flow());
		//fl.DoCheck();

		int nodeid = Integer.parseInt(this.getFK_Flow() + "01");
		DataTable dt=null;
		StringBuilder sql = new StringBuilder();
		Node nd = new Node(nodeid);
		try
		{
			switch (nd.getHisDeliveryWay())
			{
				case ByStation:
					if (BP.WF.Glo.getOSModel() == OSModel.OneOne)
					{
						sql.append("SELECT Port_Emp.No  FROM Port_Emp LEFT JOIN Port_Dept   Port_Dept_FK_Dept ON  Port_Emp.FK_Dept=Port_Dept_FK_Dept.No   join Port_Empstation on (fk_emp=Port_Emp.No)   join WF_NodeStation on (WF_NodeStation.fk_station=Port_Empstation.fk_station) WHERE (1=1) AND  FK_Node="+nd.getNodeID());	
					}
					else
					{
						sql.append("SELECT Port_Emp.No  FROM Port_Emp LEFT JOIN Port_Dept   Port_Dept_FK_Dept ON  Port_Emp.FK_Dept=Port_Dept_FK_Dept.No   join Port_DeptEmpstation on (fk_emp=Port_Emp.No)   join WF_NodeStation on (WF_NodeStation.fk_station=Port_DeptEmpstation.fk_station) WHERE (1=1) AND  FK_Node="+nd.getNodeID());
					}
					break;
				case ByDept:
					sql.append("select No,Name from Port_Emp where FK_Dept in (select FK_Dept from WF_NodeDept where FK_Node='" + nodeid + "') ");
					break;
				case ByBindEmp:
					sql.append("select No,Name from Port_Emp where No in (select FK_Emp from WF_NodeEmp where FK_Node='" + nodeid + "') ");
					break;
				case ByDeptAndStation:
					sql.append("SELECT No FROM Port_Emp WHERE No IN ");
					if (BP.WF.Glo.getOSModel() == OSModel.OneOne){
						sql.append("(SELECT No FROM Port_Emp WHERE FK_Dept IN ");
						
					}else{
						sql.append("(SELECT FK_Emp FROM Port_DeptEmp WHERE FK_Dept IN ");
					}
					sql.append("( SELECT FK_Dept FROM WF_NodeDept WHERE FK_Node=" + nodeid + ")");
					sql.append(")");
					sql.append("AND No IN ");
					sql.append("(");
					if (BP.WF.Glo.getOSModel() == OSModel.OneOne){
						sql.append("SELECT FK_Emp FROM Port_EmpStation WHERE FK_Station IN ");
					}else{
						sql.append("SELECT FK_Emp FROM Port_DeptEmpStation WHERE FK_Station IN ");
					}
					sql.append("( SELECT FK_Station FROM WF_NodeStation WHERE FK_Node=" + nodeid + ")");
					sql.append(") ORDER BY No ");
					break;
				case BySQL:
					if (StringHelper.isNullOrEmpty(nd.getDeliveryParas()))
					{
//						throw new RuntimeException("@您设置的按SQL访问开始节点，但是您没有设置sql.");
						ToErrorPage("@您设置的按SQL访问开始节点，但是您没有设置sql.");
						return;
					}
				   // emps.RetrieveInSQL(nd.DeliveryParas);
					break;
				default:
					break;
			}

			 dt = BP.DA.DBAccess.RunSQLReturnTable(sql.toString());
			if (dt.Rows.size() == 0)
			{
				ToErrorPage("@您按照:" + nd.getHisDeliveryWay() + "的方式设置的开始节点的访问规则，但是开始节点没有人员。");
				return;
//				throw new RuntimeException("@您按照:" + nd.getHisDeliveryWay() + "的方式设置的开始节点的访问规则，但是开始节点没有人员。");
			}
		}
		catch (RuntimeException ex)
		{
			String error = AddMsgOfWarning("Error", "错误原因 <h2>您没有正确的设置开始节点的访问规则，请查看流程设计操作手册。</h2>" + ex.getStackTrace() + " - " + ex.getMessage());
			ToErrorPage(error);
			return;
		}
		//appendPub(AddFieldSet("可发起(<font color=red>" + fl.getName() + "</font>)流程的人员,选择一个人员进入并测试"));
		appendPub("<table style='margin-top:0px;background:white; text-align:center;' border='1' cellpadding='0' cellspacing='0' width='90%'>");
		//appendPub(AddTable("align=center"));
		appendPub(AddCaptionLeft("流程编号:" + fl.getNo() + " 名称:" + fl.getName()));
		appendPub(AddTR());
		appendPub(AddTH("table_id","序号"));

//		Label cball = new Label();
//		cball.addAttr("onclick", "SelectAll(this);");
//		cball.setText("选择全部");
//		cball.setText("人员");
		appendPub(AddTH("table-author", "人员"));
		appendPub(AddTH("table_set", "经典模式"));
//		appendPub(AddTDTitle("LigerUI模式"));
	  //appendPub(AddTDTitle("手机模式");
//		appendPub(AddTDTitle("SDK"));
//		appendPub(AddTDTitle("简洁模式"));
		appendPub(AddTDTitle("极速模式"));
		appendPub(AddTH("table_type", "部门"));
		appendPub(AddTREnd());
		boolean is1 = false;
		int idx = 0;

		StringBuilder emps = new StringBuilder();
		for (DataRow dr : dt.Rows)
		{
			String no = (String)((dr.getValue(0) instanceof String) ? dr.getValue(0) : null);
			if (StringHelper.isNullOrEmpty(no))
			{
//				throw new RuntimeException();
				ToErrorPage("人员基础数据不完整，人员编号为空，请执行如下SQL检查："+sql);
				return;
			}

			String myemp = dr.getValue(0).toString();
			String temp = "," + myemp + ",";
			if (emps.toString().contains(temp) == true)
			{
				continue;
			}

			emps.append(temp);


			BP.Port.Emp emp = new Emp(myemp);
			idx++;
			//is1 = appendPub(AddTR(is1, ""));
			appendPub(AddTDIdx(idx));

			Label cb = new Label();
			cb.setId("CB_" + emp.getNo());
			cb.setName("CB_" + emp.getNo());
			cb.setText(emp.getNo() + "," + emp.getName());
			appendPub(AddTD(cb));

			appendPub(AddTD("<a href='./../Port.jsp?DoWhat=Start5&UserNo=" + emp.getNo() + "&FK_Flow=" + this.getFK_Flow() + "&Lang=" +  WebUser.getSysLang() + "&Type=" + getParameter("Type") + "'  ><img src='"+Glo.getCCFlowAppPath()+"WF/Img/ie.gif' border=0 />经典模式</a>"));
//			appendPub(AddTD("<a href='./../Port.jsp?DoWhat=StartLigerUI&UserNo=" + emp.getNo() + "&FK_Flow=" + this.getFK_Flow() + "&Lang=" +  WebUser.getSysLang() + "&Type=" + getParameter("Type") + "'  ><img src='"+Glo.getCCFlowAppPath()+"WF/Img/ie.gif' border=0 />LigerUI模式</a>"));
			//appendPub(AddTD("<a href='./../Port.aspx?DoWhat=Start&UserNo=" + emp.No + "&FK_Flow=" + this.FK_Flow + "&Lang=" + BP.Web.WebUser.getSysLang() + "&Type=" + getParameter(["Type"] + "'  ><img src='"+Glo.getCCFlowAppPath()+"WF/Img/ie.gif' border=0 />Blog模式</a>");                
			//appendPub(AddTD("<a href='./../Port.aspx?DoWhat=StartSmallSingle&UserNo=" + emp.No + "&FK_Flow=" + this.FK_Flow + "&Lang=" + BP.Web.WebUser.getSysLang() + "&Type=" + getParameter(["Type"] + "'  ><img src='"+Glo.getCCFlowAppPath()+"WF/Img/ie.gif' border=0 />Internet Explorer</a>");
			//appendPub(AddTD("<a href=\"javascript:WinOpen('TestFlow.aspx?RefNo=" + emp.No + "&FK_Flow=" + this.FK_Flow + "&Lang=" + BP.Web.WebUser.getSysLang() + "&Type=" + getParameter(["Type"] + "&IsWap=1','470px','600px','" + emp.No + "');\"  ><img src='./../Img/Mobile.gif' border=0 width=25px height=18px />Mobile</a> ");
//			appendPub(AddTD("<a href='TestSDK.jsp?RefNo=" + emp.getNo() + "&FK_Flow=" + this.getFK_Flow() + "&Lang=" + WebUser.getSysLang() + "&Type=" + getParameter("Type") + "&IsWap=1'  >SDK</a> "));
//			appendPub(AddTD("<a href='./../Port.jsp?DoWhat=Amaze&UserNo=" + emp.getNo() + "&FK_Flow=" + this.getFK_Flow() + "&Lang=" + WebUser.getSysLang() + "&Type=" + getParameter("Type") + "'  ><img src='"+Glo.getCCFlowAppPath()+"WF/Img/ie.gif' border=0 />简洁模式</a>"));
			appendPub(AddTD("<a href='./../Port.jsp?DoWhat=JiSu&UserNo=" + emp.getNo() + "&FK_Flow=" + this.getFK_Flow() + "&Lang=" +  WebUser.getSysLang() + "&Type=" + getParameter("Type") + "'  ><img src='"+Glo.getCCFlowAppPath()+"WF/Img/ie.gif' border=0 />极速模式</a>"));
			appendPub(AddTD(emp.getFK_DeptText()));
			appendPub(AddTREnd());
		}

//		Button btn = new Button();
//		btn.setText("把选择的人员执行自动模拟运行");
//		btn.attributes.put("onclick", "btn_Click();");
//		appendPub(AddTR());
//		appendPub(AddTD("colspan=7", btn));
//		appendPub(AddTREnd());
//
		//appendPub(AddFieldSetEnd());
		String buttom = "<tr><td height='100%;margin-top:5px;' valign='top' colspan='6'>"
				+ "<br/><a href=\"javascript:ShowIt('BP.WF.DTS.ClearDB');\"><img src='../Img/Btn/Delete.gif' border=0 />清除所有流程运行的数据(此功能要在测试环境里运行)</a> <br/><br/> <font size=2 color=\"Green\">清除所有流程运行的数据，包括待办工作。</font> <br/><br/>"
				+ "</td></tr><tr><td colspan='6'> <br/> <a href=\"javascript:Open('"+getFK_Flow()+"');\"> <img src='../Img/Btn/Delete.gif' border=0 />删除本流程数据(此功能要在测试环境里运行)</a> <br/><br/> <font size=\"2\" color=\"Green\">清除本流程运行的数据，包括待办工作。</font> <br/><br/></td></tr>";
		appendPub(buttom);
		appendPub(AddTableEnd());
	}
}
