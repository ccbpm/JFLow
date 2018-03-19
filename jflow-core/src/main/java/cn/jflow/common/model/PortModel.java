package cn.jflow.common.model;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import BP.Port.Emp;
import BP.Sys.SystemConfig;
import BP.WF.Dev2Interface;
import BP.WF.DoWhatList;
import BP.Web.UserWorkDev;
import BP.Web.WebUser;

public class PortModel extends BaseModel {

	public PortModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public final String getDoWhat() {
		return getParameter("DoWhat");
	}

	public final String getUserNo() {
		return getParameter("UserNo");
	}

	public final String getSID() {
		return getParameter("SID");
	}

	public final void loadPage() {
		
		if (StringUtils.isNotBlank(this.getUserNo()) && StringUtils.isNotBlank(this.getSID())) {
			
			try {
				String uNo = "";
				if (this.getUserNo().equals("admin")) {
					uNo = "zhoupeng";
				} else {
					uNo = this.getUserNo();
				}

 
					Emp emL = new Emp(this.getUserNo());
					WebUser.setToken(get_request().getSession().getId());
					WebUser.SignInOfGenerLang(emL, SystemConfig.getSysLanguage());
 
			} catch (RuntimeException ex) {
				throw new RuntimeException("@有可能您没有配置好ccflow的安全验证机制:" + ex.getMessage());
			}
 
		}

		Emp em = new Emp(this.getUserNo());
		WebUser.setToken(get_request().getSession().getId());
		WebUser.SignInOfGenerLang(em, SystemConfig.getSysLanguage());

		String paras = "";
		Enumeration en = this.getRequest().getParameterNames();
		while(en.hasMoreElements()){
			
			String str = (String)en.nextElement();
			String val = this.getRequest().getParameter(str);
			if (val.indexOf('@') != -1) {
				throw new RuntimeException("您没有能参数: [ " + str + " ," + val + " ] 给值 ，URL 将不能被执行。");
			}
			
			if (!(str.equals(DoWhatList.DoNode) || str.equals(DoWhatList.Emps)
					|| str.equals(DoWhatList.EmpWorks)
					|| str.equals(DoWhatList.FlowSearch)
					|| str.equals(DoWhatList.Login)
					|| str.equals(DoWhatList.MyFlow)
					|| str.equals(DoWhatList.MyWork)
					|| str.equals(DoWhatList.Start)
					|| str.equals(DoWhatList.Start5)
					|| str.equals(DoWhatList.JiSu)
					|| str.equals(DoWhatList.StartSmall)
					|| str.equals(DoWhatList.FlowFX)
					|| str.equals(DoWhatList.DealWork)
					|| str.equals(DoWhatList.DealWorkInSmall)
					// || str.equals(DoWhatList.CallMyFlow)
					|| str.equals("FK_Flow") || str.equals("WorkID")
					|| str.equals("FK_Node") || str.equals("SID"))) {
				paras += "&" + str + "=" + val;
			}
		}

//		if (this.IsPostBack == false) {
		if (this.IsCanLogin() == false) {
			ToMsgPage("<fieldset><legend>安全验证错误</legend> 系统无法执行您的请求，可能是您的登录时间太长，请重新登录。<br>如果您要取消安全验证请修改web.config 中IsDebug 中的值设置成1。</fieldset>");
			return;
		}

		BP.Port.Emp emp = new BP.Port.Emp(this.getUserNo());
		WebUser.SignInOfGener(emp); // 开始执行登录。

		if (this.getParameter("IsMobile") != null) {
			WebUser.setUserWorkDev(UserWorkDev.Mobile);
		}

		String nodeID = String.valueOf(Integer.parseInt(this.getFK_Flow()+ "01"));
		String doWhat = this.getDoWhat();
		if (doWhat.equals(DoWhatList.OneWork)) // 工作处理器调用.
		{
			if (this.getFK_Flow() == null || this.getWorkID() == 0) {
				throw new RuntimeException("@参数 FK_Flow 或者 WorkID 为 Null 。");
			}
//			if (BP.WF.Dev2Interface.Flow_IsCanDoCurrentWork(ap.GetValStrByKey("FK_Flow"),
//                    ap.GetValIntByKey("FK_Node"), ap.GetValInt64ByKey("WorkID"), WebUser.No) == true)
//                    this.Response.Redirect("MyFlow.htm?FK_Flow=" + ap.GetValStrByKey("FK_Flow") + "&WorkID=" + ap.GetValStrByKey("WorkID") + "&o2=1" + paras, true);
//                else
//                    this.Response.Redirect("WFRpt.htm?FK_Flow=" + ap.GetValStrByKey("FK_Flow") + "&WorkID=" + ap.GetValStrByKey("WorkID") + "&o2=1" + paras, true);
			sendRedirect("WFRpt.jsp?FK_Flow=" + this.getFK_Flow() + "&WorkID="
					+ this.getWorkID() + "&o2=1" + paras);
			
			return;
		} 

		if (doWhat.equals(DoWhatList.JiSu)) // 极速模式的方式发起工作
		{
			if (this.getFK_Flow() == null) {
				sendRedirect("App/Simple/Default.htm");
			} else {
				sendRedirect("App/Simple/Default.htm?FK_Flow="
						+ this.getFK_Flow() + paras + "&FK_Node=" + nodeID);
			}
			
			return;
		} 
		
        if (doWhat.equals(DoWhatList.Start5) || doWhat.equals("StartClassic") ) // 发起工作
		{
			if (this.getFK_Flow() == null) {
				sendRedirect("AppClassic/Home.htm");
			} else {
				sendRedirect("AppClassic/Home.htm?FK_Flow="
						+ this.getFK_Flow() + paras + "&FK_Node=" + nodeID);
			}
			
			return;
		} 

         if (doWhat.equals(DoWhatList.Start)) // 发起工作
		{
			if (StringUtils.isEmpty(this.getFK_Flow())) {
				sendRedirect("Start.htm");
			} else {
				sendRedirect("MyFlow.htm?FK_Flow=" + this.getFK_Flow() + paras
						+ "&FK_Node=" + nodeID);
			}
			
			return;
		}

         if (doWhat.equals(DoWhatList.StartSmall)) // 发起工作　小窗口
		{
			if (this.getFK_Flow() == null) {
				sendRedirect("Start.htm?FK_Flow=" + this.getFK_Flow() + paras);
			} else {
				sendRedirect("MyFlow.htm?FK_Flow=" + this.getFK_Flow() + paras);
			}
			
			return;
			
		} 

         if (doWhat.equals(DoWhatList.StartSmallSingle)) // 发起工作单独小窗口
		{
			if (this.getFK_Flow() == null) {
				sendRedirect("Start.htm?FK_Flow=" + this.getFK_Flow() + paras
						+ "&IsSingle=1&FK_Node=" + nodeID);
			} else {
				sendRedirect("MyFlowSmallSingle.htm?FK_Flow="
						+ this.getFK_Flow() + paras + "&FK_Node=" + nodeID);
			}
			return;
		} 

         if (doWhat.equals(DoWhatList.Runing)) // 在途中工作
		{
			sendRedirect("Runing.jsp?FK_Flow=" + this.getFK_Flow());
			return;
		} 

         if (doWhat.equals(DoWhatList.Tools)) // 工具栏目。
		{
			sendRedirect("Tools.jsp");
			return;
		} 

         if (doWhat.equals(DoWhatList.ToolsSmall)) // 小工具栏目.
		{
			sendRedirect("Tools.jsp?RefNo=" + this.getParameter("RefNo"));
			return;
		} 

         if (doWhat.equals(DoWhatList.EmpWorks)) // 我的工作小窗口.
		{
			if (this.getFK_Flow() == null || this.getFK_Flow().equals("")) {
				sendRedirect("Todolist.htm");
			} else {
				sendRedirect("Todolist.htm?FK_Flow=" + this.getFK_Flow());
			}
			return;
		} 

         if (doWhat.equals(DoWhatList.Login)) {
			if (this.getFK_Flow() == null) {
				sendRedirect("Todolist.htm");
			} else {
				sendRedirect("Todolist.htm?FK_Flow=" + this.getFK_Flow());
			}
			return;
		} 

         if (doWhat.equals(DoWhatList.Emps)) // 通讯录。
		{
			sendRedirect("Emps.jsp");
			return;
		} 

         if (doWhat.equals(DoWhatList.FlowSearch)) // 流程查询。
		{
			if (this.getFK_Flow() == null) {
				sendRedirect("FlowSearch.htm");
			} else {
				sendRedirect("Rpt/Search.jsp?Endse=s&FK_Flow=001&EnsName=ND"
						+ Integer.parseInt(this.getFK_Flow()) + "Rpt" + paras);
			}
			return;
		} 

         if (doWhat.equals(DoWhatList.FlowSearchSmall)) // 流程查询。
		{
			if (this.getFK_Flow() == null) {
				sendRedirect("FlowSearch.htm");
			} else {
				sendRedirect("Comm/Search.htm?EnsName=ND"
						+ Integer.parseInt(this.getFK_Flow()) + "Rpt" + paras);
			}
			return;
		} 

         if (doWhat.equals(DoWhatList.FlowSearchSmallSingle)) // 流程查询。
		{
			if (this.getFK_Flow() == null) {
				sendRedirect("FlowSearchSmallSingle.htm");
			} else {
				sendRedirect("Comm/Search.htm?EnsName=ND"
						+ Integer.parseInt(this.getFK_Flow()) + "Rpt" + paras);
			}
			
			return;
		} 

         if (doWhat.equals(DoWhatList.FlowFX)) // 流程查询。
		{
			if (this.getFK_Flow() == null) {
				throw new RuntimeException("@没有参数流程编号。");
			}

			sendRedirect("Comm/Group.htm?EnsName=ND"
					+ Integer.parseInt(this.getFK_Flow()) + "Rpt" + paras);
			
			return;
		} 

         if (doWhat.equals(DoWhatList.DealWork)) {
			if (this.getFK_Flow() == null || this.getWorkID() == 0) {
				throw new RuntimeException("@参数 FK_Flow 或者 WorkID 为Null 。");
			}
			if(Dev2Interface.Flow_IsCanDoCurrentWork(this.getFK_Flow(), this.getNodeID(), this.getWorkID(),this.getUserNo())){
				sendRedirect("MyFlow.htm?FK_Flow=" + this.getFK_Flow() + "&WorkID="
						+ this.getWorkID() + "&o2=1" + paras);
			}else{
				sendRedirect("WFRpt.htm?FK_Flow=" + this.getFK_Flow() + "&WorkID="
						+ this.getWorkID() + "&o2=1" + paras);
			}
			return;
		} 

         if (doWhat.equals(DoWhatList.DealWorkInSmall)) {
			if (this.getFK_Flow() == null || this.getWorkID() == 0) {
				throw new RuntimeException("@参数 FK_Flow 或者 WorkID 为Null 。");
			}
			if(Dev2Interface.Flow_IsCanDoCurrentWork(this.getFK_Flow(), this.getNodeID(), this.getWorkID(),this.getUserNo())){
				sendRedirect("MyFlow.htm?FK_Flow=" + this.getFK_Flow() + "&WorkID="
						+ this.getWorkID() + "&o2=1" + paras);
			}else{
				sendRedirect("WFRpt.htm?FK_Flow=" + this.getFK_Flow() + "&WorkID="
						+ this.getWorkID() + "&o2=1" + paras);
			}
			return;
		} 

       if (doWhat.equals("ACE")) {
	       if (this.getFK_Flow() == null)
	    	   sendRedirect("../AppACE/Login.htm");
	       else
	    	   sendRedirect("../AppACE/Home.htm?FK_Flow=" + this.getFK_Flow() + paras + "&FK_Node=" + nodeID);
       }
         
         return;
	}

	/** 
	 验证登录用户是否合法
	 
	 @return 
	*/
	public final boolean IsCanLogin() {
		Object isAuth = SystemConfig.getAppSettings().get("IsAuth");
		if (isAuth == null) {
			return true;
		}
		if (isAuth.equals("1")) {
			if (!this.getSID().equals(this.GetKey())) {
				if (SystemConfig.getIsDebug()) {
					return true;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public final String GetKey() {
		return BP.DA.DBAccess.RunSQLReturnString("SELECT SID From Port_Emp WHERE no='" + this.getUserNo() + "'");
	}

	//	/**
	//	 * 发起工作
	//	 */
	//	private void sendWork(){
	//		if(!getFK_Flow().equals("")){
	//			String nodeID = String.valueOf(Integer.parseInt(this.getFK_Flow() + "01"));
	//			String url = "./MyFlow.htm?FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + nodeID + "&DoWhat=" + getDoWhat();
	//            try {
	//				PubClass.WinOpen(get_response(), url, 800, 600);
	//			} catch (IOException e) {
	//				e.printStackTrace();
	//			}
	//		}
	//	}

}
