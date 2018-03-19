package cn.jflow.controller.wf.workopt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.DA.DBAccess;
import BP.DA.DataTable;
import BP.En.Attr;
import BP.En.Attrs;
import BP.Tools.StringHelper;
import BP.WF.DeliveryWay;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Work;
import BP.WF.WorkNode;
import BP.WF.Data.GERpt;
import BP.WF.Template.Cond;
import BP.WF.Template.CondAttr;
import BP.WF.Template.CondModel;
import BP.WF.Template.NodeAttr;
import BP.WF.Template.Selector;
import BP.WF.Template.TurnTo;
import BP.WF.Template.TurnTos;
import BP.Web.WebUser;

@Controller
@RequestMapping("/WF/WorkOpt")
public class AccepterController {
	private HttpServletRequest _request = null;
	private HttpServletResponse _response = null;

	/** 
	 打开
	 
	*/
	public final int getIsWinOpen()
	{
		String str = _request.getParameter("IsWinOpen");
		if (str.equals("1") || str == null||str.equals(""))
		{
			return 1;
		}
		return 0;
	}
	/** 
	 到达的节点
	 
	*/
	public final int getToNode()
	{

		if (StringHelper.isNullOrEmpty(_request.getParameter("ToNode")))
		{
			return 0;
		}
		return Integer.parseInt(_request.getParameter("ToNode").toString());
	}
	public final int getFK_Node()
	{
		return Integer.parseInt(_request.getParameter("FK_Node").toString());
	}
	public final long getWorkID()
	{
		return Long.parseLong(_request.getParameter("WorkID").toString());
	}
	public final long getFID()
	{
		if (_request.getParameter("FID") != null)
		{
			return Long.parseLong(_request.getParameter("FID").toString());
		}

		return 0;
	}
	public final String getFK_Dept()
	{
		String s = _request.getParameter("FK_Dept");
		if (StringHelper.isNullOrEmpty(s))
		{
			s = WebUser.getFK_Dept();
		}
		return s;
	}
	public final String getFK_Station()
	{
		return _request.getParameter("FK_Station");
	}
	public final String getWorkIDs()
	{
		return _request.getParameter("WorkIDs");
	}
	public final String getDoFunc()
	{
		return _request.getParameter("DoFunc");
	}
	public final String getCFlowNo()
	{
		return _request.getParameter("CFlowNo");
	}
	public final String getFK_Flow()
	{
		return _request.getParameter("FK_Flow");
	}

	private boolean IsMultiple = false;
	/** 
	 获取传入参数
	 
	 @param param 参数名
	 @return 
	*/
	public final String getUTF8ToString(String param)
	{
		try {
			return java.net.URLDecoder.decode(_request.getParameter(param),
					"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}	
	}


	private Node _HisNode = null;
	/** 
	 它的节点
	 
	*/
	public final Node getHisNode()
	{
		if (_HisNode == null)
		{
			_HisNode = new Node(this.getFK_Node());
		}
		return _HisNode;
	}
	/** 
	 是否多分支
	 
	*/
	public final boolean getIsMFZ()
	{
		Nodes nds = this.getHisNode().getHisToNodes();
		int num = 0;
		for (Node mynd :nds.ToJavaList())
		{
			Cond cond = new Cond();
			int i = cond.Retrieve(CondAttr.FK_Node, this.getHisNode().getNodeID(), CondAttr.ToNodeID, mynd.getNodeID());
			if (i == 0)
			{
				continue; // 没有设置方向条件，就让它跳过去。
			}
			cond.setWorkID(this.getWorkID());
			cond.en = getwk();

			if (!cond.getIsPassed())
			{
				continue;
			}
			///#endregion 过滤不能到达的节点.

			if (mynd.getHisDeliveryWay() == DeliveryWay.BySelected)
			{
				num++;
			}
		}
		if (num == 0)
		{
			return false;
		}
		if (num == 1)
		{
			return false;
		}
		return true;
	}
	public Selector MySelector = null;
	public GERpt _wk = null;
	public final GERpt getwk()
	{
		if (_wk == null)
		{
			_wk = this.getHisNode().getHisFlow().getHisGERpt();
			_wk.setOID(this.getWorkID());
			_wk.Retrieve();
			_wk.ResetDefaultVal();
		}
		return _wk;
	}

	@RequestMapping(value = "/AccepterS", method = RequestMethod.POST)
	protected final void execute(HttpServletRequest request,
			HttpServletResponse response)
	{
		_request = request;
		_response = response;
		String dirSend = getUTF8ToString("dirSend");
		String DoWhat = _request.getParameter("DoWhat");

		if (!StringHelper.isNullOrEmpty(DoWhat))
		{
			_request.getSession().setAttribute("DoWhat", DoWhat); //默认到期时间20min
			_request.getSession().setAttribute("dirSend",dirSend);
		}
		DoWhat = _request.getSession().getAttribute("DoWhat") == null ? "" : _request.getSession().getAttribute("DoWhat").toString(); //读取


		//判断是否需要转向。
		if (this.getToNode() == 0)
		{
			int num = 0;
			int tempToNodeID = 0;
			//如果到达的点为空 
//               首先判断当前节点的ID，是否配置到了其他节点里面，
//                * * 如果有则需要转向高级的选择框中去，当前界面不能满足公文类的选择人需求。
			String sql = "SELECT COUNT(*) FROM WF_Node WHERE FK_Flow='" + this.getHisNode().getFK_Flow() + "' AND " + NodeAttr.DeliveryWay + "=" +  DeliveryWay.BySelected.ordinal() + " AND " + NodeAttr.DeliveryParas + " LIKE '%" + this.getHisNode().getNodeID() + "%' ";

			if (DBAccess.RunSQLReturnValInt(sql, 0) > 0)
			{
				//说明以后的几个节点人员处理的选择 
				String url = "AccepterAdv.jsp?1=3&" + _request.getParameterMap();
				try {
					_response.sendRedirect(url );
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}

			Nodes nds = this.getHisNode().getHisToNodes();
			if (nds.size() == 0)
			{
				try {
					_response.getOutputStream().write("<script type=\"text/javascript\">alert('当前点是最后的一个节点,不能使用此功能!')</script> ".getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
				//this.Pub1.AddFieldSetRed("提示", "当前点是最后的一个节点，不能使用此功能。");
				return;
			}
			else if (nds.size() == 1)
			{
				Node toND = (Node)((nds.get(0) instanceof Node) ? nds.get(0) : null);
				tempToNodeID = toND.getNodeID();
			}
			else
			{
				Node nd = new Node(this.getFK_Node());
				for (Node mynd : nds.ToJavaList())
				{
					if (mynd.getHisDeliveryWay() != DeliveryWay.BySelected)
					{
						continue;
					}

					if (nd.getCondModel() == CondModel.ByLineCond)
					{
						Cond cond = new Cond();
						int i = cond.Retrieve(CondAttr.FK_Node, this.getHisNode().getNodeID(), CondAttr.ToNodeID, mynd.getNodeID());
						if (i == 0)
						{
							continue; // 没有设置方向条件，就让它跳过去。
						}
						cond.setWorkID(this.getWorkID());
						cond.en = getwk();
						if (!cond.getIsPassed())
						{
							continue;
						}
					}
					tempToNodeID = mynd.getNodeID();
					num++;
				}
			}

			if (tempToNodeID == 0)
			{
				try {
					_response.getOutputStream().write("<script type=\"text/javascript\">alert('@流程设计错误：\n\n 当前节点的所有分支节点没有一个接受人员规则为按照选择接受!')</script> ".getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
				//this.WinCloseWithMsg("@流程设计错误：\n\n 当前节点的所有分支节点没有一个接受人员规则为按照选择接受。");
				return;
			}


			if (dirSend.equals("yes"))
			{
				try {
					_response.sendRedirect("Accepter.jsp?FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node() + "&ToNode=" + tempToNodeID + "&FID=" + this.getFID() + "&type=1&WorkID=" + this.getWorkID() + "&IsWinOpen=" + this.getIsWinOpen() + "&dirSend=" + dirSend + "&DoWhat=" + DoWhat);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else
			{
				try {
					_response.sendRedirect("Accepter.jsp?FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node() + "&ToNode=" + tempToNodeID + "&FID=" + this.getFID() + "&type=1&WorkID=" + this.getWorkID() + "&IsWinOpen=" + this.getIsWinOpen() + "&dirSend=no" + "&DoWhat=" + DoWhat);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return;
		}
		String s_responsetext = "";
		try
		{
			// 首先判断是否有多个分支的情况。
			if (this.getIsMFZ() && getToNode() == 0)
			{
				IsMultiple = true;
				//this.BindMStations();
				return;
			}
			returnValue();
		}
		catch (RuntimeException ex)
		{
			try {
				_response.getOutputStream().write("<script type=\"text/javascript\">alert('错误!')</script> ".getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			//this.Pub1.Clear();
			//this.Pub1.AddMsgOfWarning("错误", ex.Message);
		}
	}

	/** 
	 返回值
	 
	*/
	private void returnValue()
	{
		String method = "";
		//返回值
		String s_responsetext = "";

		if (StringHelper.isNullOrEmpty(WebUser.getNo()) || StringHelper.isNullOrEmpty(_request.getParameter("method")))
		{
			addEmps();
			return;
		}


		method = _request.getParameter("method").toString();
		if (method.equals("saveMet"))
		{
				saveMet();
		}
		else if (method.equals("clearDataMet"))
		{
				clearDataMet();
				return;
		}

		if (StringHelper.isNullOrEmpty(s_responsetext))
		{
			s_responsetext = "";
		}
		//组装ajax字符串格式,返回调用客户端 树型
		_response.setCharacterEncoding("UTF-8");
		_response.setContentType("text/html");
		try {
			_response.getOutputStream().write(s_responsetext.replace("][", "],[").getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/** 
	 这是一个清空全部数据的操作
	 由于此前可能更改配置规则--因此删除时不必再判断，直接清空相关数据
	 
	 @return 
	*/
	public final String clearDataMet()
	{
		String delHadEmps = String.format("delete from WF_SelectAccper where FK_Node=%1$s and WorkID=%2$s)", this.getToNode(), this.getWorkID());
		DBAccess.RunSQL(delHadEmps);
		return "";
	}
	//保存
	public final void saveMet()
	{
		String getSaveNo = getUTF8ToString("getSaveNo");

		//此处做判断,删除checked的部门数据
		String[] getSaveNoArray = getSaveNo.split("[,]", -1);
		java.util.ArrayList<String> getSaveNoList = new java.util.ArrayList<String>();

		for (int i = 0; i < getSaveNoArray.length; i++)
		{
			getSaveNoList.add(getSaveNoArray[i]);
		}

		getSaveNo = null;
		String ziFu = ",";
		for (int i = 0; i < getSaveNoList.size(); i++)
		{
			if (i == getSaveNoList.size() - 1)
			{
				ziFu = null;
			}
			getSaveNo += (getSaveNoList.get(i) + ziFu);
		}

		//设置人员.
		BP.WF.Dev2Interface.WorkOpt_SetAccepter(this.getToNode(), this.getWorkID(), this.getFID(), getSaveNo, false);
		//ScriptManager.RegisterStartupScript(this.Page, typeof(Page), "", "alert('111');", true);

		if (this.getIsWinOpen() == 0)
		{
			//如果是 MyFlow.htm 调用的, 就要调用发送逻辑. 
			//this.DoSend();
		}
		if (_request.getParameter("IsEUI") == null)
		{
			try {
				_response.getOutputStream().write("<script type=\"text/javascript\">window.close();</script> ".getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			try {
				_response.getOutputStream().write("window.parent.$('windowIfrem').window('close');".getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public final void DoSend()
	{
		// 以下代码是从 MyFlow.htm Send 方法copy 过来的，需要保持业务逻辑的一致性，所以代码需要保持一致.

		Node nd = new Node(this.getFK_Node());
		Work wk = nd.getHisWork();
		wk.setOID(this.getWorkID());
		wk.Retrieve();

		WorkNode firstwn = new WorkNode(wk, nd);
		String msg = "";
		try
		{
			msg = firstwn.NodeSend().ToMsgOfHtml();
		}
		catch (Exception exSend)
		{
			return;
		}

		try
		{
			//处理通用的发送成功后的业务逻辑方法，此方法可能会抛出异常.
			BP.WF.Glo.DealBuinessAfterSendWork(this.getFK_Flow(), this.getWorkID(), this.getDoFunc(), getWorkIDs(), this.getCFlowNo(), 0, null);
		}
		catch (Exception ex)
		{
			this.ToMsg(msg, ex.getMessage());
			return;
		}

		//处理转向问题.
		switch (firstwn.getHisNode().getHisTurnToDeal())
		{
			case SpecUrl:
				String myurl = firstwn.getHisNode().getTurnToDealDoc().toString();
				if (myurl.contains("&") == false)
				{
					myurl += "?1=1";
				}
				Attrs myattrs = firstwn.getHisWork().getEnMap().getAttrs();
				Work hisWK = firstwn.getHisWork();
				for (Attr attr : myattrs)
				{
					if (myurl.contains("@") == false)
					{
						break;
					}
					myurl = myurl.replace("@" + attr.getKey(), hisWK.GetValStrByKey(attr.getKey()));
				}
				if (myurl.contains("@"))
				{
					throw new RuntimeException("流程设计错误，在节点转向url中参数没有被替换下来。Url:" + myurl);
				}

				myurl += "&FromFlow=" + this.getFK_Flow() + "&FromNode=" + this.getFK_Node() + "&PWorkID=" + this.getWorkID() + "&UserNo=" + WebUser.getNo() + "&SID=" + WebUser.getSID();
			try {
				_response.sendRedirect(myurl);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
				return;
			case TurnToByCond:
				TurnTos tts = new TurnTos(this.getFK_Flow());
				if (tts.size() == 0)
				{
					throw new RuntimeException("@您没有设置节点完成后的转向条件。");
				}
				for (TurnTo tt : tts.ToJavaList())
				{
					tt.HisWork = firstwn.getHisWork();
					if (tt.getIsPassed() )
					{
						String url = tt.getTurnToURL().toString();
						if (url.contains("&") == false)
						{
							url += "?1=1";
						}
						Attrs attrs = firstwn.getHisWork().getEnMap().getAttrs();
						Work hisWK1 = firstwn.getHisWork();
						for (Attr attr : attrs)
						{
							if (!url.contains("@"))
							{
								break;
							}
							url = url.replace("@" + attr.getKey(), hisWK1.GetValStrByKey(attr.getKey()));
						}
						if (url.contains("@"))
						{
							throw new RuntimeException("流程设计错误，在节点转向url中参数没有被替换下来。Url:" + url);
						}

						url += "&PFlowNo=" + this.getFK_Flow() + "&FromNode=" + this.getFK_Node() + "&PWorkID=" + this.getWorkID() + "&UserNo=" + WebUser.getNo() + "&SID=" + WebUser.getSID();
						try {
							_response.sendRedirect(url);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					}
				}
				this.ToMsg(msg, "info");
				//throw new Exception("您定义的转向条件不成立，没有出口。");
				break;
			default:
				this.ToMsg(msg, "info");
				break;
		}
		return;
	}

	public final String addEmps()
	{
		String alreadyHadEmps = String.format("select No, Name from Port_Emp where No in( select FK_Emp from WF_SelectAccper " + "where FK_Node=%1$s and WorkID=%2$s)", this.getToNode(), this.getWorkID());
		DataTable dt = DBAccess.RunSQLReturnTable(alreadyHadEmps);
		String addLi = "";
		//this.creatLi.InnerHtml = "";
		for (int i = 0; i < dt.Rows.size(); i++)
		{
			addLi += String.format("<li style='text-align:center;vertical-align:middle;width:100px;' id='%1$s' class='liNode' onmouseout='javascript:removeCss(this);'" + "onmouseover='javascript:addCss(this);'onclick='javascript:RemoveLiList(this);' title='%1$s'>" + "<img src='../Style/themes/icons/emp.png' />%2$s<img src='../Style/themes/icons/close.png' /></li>", dt.getValue(i, "No").toString(), dt.getValue(i,"Name").toString());
		}

		//this.creatLi.InnerHtml = addLi;
		return "";
	}

	public final void ToMsg(String msg, String type)
	{
		_request.getSession().setAttribute("info", msg);
		//this.Application["info" + WebUser.getNo()] = msg;

		try {
			BP.WF.Glo.setSessionMsg(msg);
			_response.sendRedirect("./../MyFlowInfo" + BP.WF.Glo.getFromPageType() + ".jsp?FK_Flow=" + this.getFK_Flow() + "&FK_Type=" + type + "&FK_Node=" + this.getFK_Node() + "&WorkID=" + this.getWorkID());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

