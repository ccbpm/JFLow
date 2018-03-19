package cn.jflow.controller.wf.workopt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataRowCollection;
import BP.DA.DataTable;
import BP.En.QueryObject;
import BP.Tools.StringHelper;
import BP.WF.DeliveryWay;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Data.GERpt;
import BP.WF.Template.Cond;
import BP.WF.Template.CondAttr;
import BP.WF.Template.CondModel;
import BP.WF.Template.NodeAttr;
import BP.WF.Template.NodeDept;
import BP.WF.Template.NodeDeptAttr;
import BP.WF.Template.NodeDepts;
import BP.WF.Template.Selector;


@Controller
@RequestMapping("/WF/WorkOpt")
public class AccepterEUIController
{
	public Selector MySelector = null;
	private HttpServletRequest _request = null;
	private HttpServletResponse _response = null;
	public final String getFK_Node()
	{
		try
		{
			String nodeid = _request.getParameter("NodeID");
			if (nodeid == null)
			{
				nodeid = _request.getParameter("FK_Node");
			}
			return nodeid;
		}
		catch (java.lang.Exception e)
		{
			return "101"; // 0; 有可能是流程调用流程表单。
		}
	}
	/** 
	 到达的节点
	 
	*/
	public final int getToNode()
	{

		if (_request.getParameter("ToNode") == null)
		{
			return 0;
		}
		return Integer.parseInt(_request.getParameter("ToNode").toString());
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
	public final long getWorkID()
	{
		return Long.parseLong(_request.getParameter("WorkID").toString());
	}
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
	public final String getFK_Flow()
	{
		return _request.getParameter("FK_Flow");
	}
	private boolean IsMultiple = false;
	/** 
	 是否多分支
	 
	*/
	public final boolean getIsMFZ()
	{
		Nodes nds = this.getHisNode().getHisToNodes();
		int num = 0;
		for (Node mynd : nds.ToJavaList())
		{
			Cond cond = new Cond();
			int i = cond.Retrieve(CondAttr.FK_Node, this.getHisNode().getNodeID(), CondAttr.ToNodeID, mynd.getNodeID());
			if (i == 0)
			{
				continue; // 没有设置方向条件，就让它跳过去。
			}
			cond.setWorkID( this.getWorkID());
			cond.en = getwk();

			if (!cond.getIsPassed())
			{
				continue;
			}

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
	public final long getFID()
	{
		if (_request.getParameter("FID") != null)
		{
			return Long.parseLong(_request.getParameter("FID").toString());
		}

		return 0;
	}
	/** 
	 打开
	 
	*/
	public final int getIsWinOpen()
	{
		String str = _request.getParameter("IsWinOpen");
		if (str.equals("1") || str == null)
		{
			return 1;
		}
		return 0;
	}
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

	@RequestMapping(value = "/AccepterEUI", method = RequestMethod.POST)
	protected final void execute(HttpServletRequest request,
			HttpServletResponse response)
	{
		_request = request;
		_response = response;
		treeResult.setLength(0);
		treesb.setLength(0);
		//返回值
		String s_responsetext = "";
		s_responsetext = BindByWhichMet();

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
	public final String BindByWhichMet()
	{
		///#region
		//判断是否需要转向。
		if (this.getToNode() == 0)
		{
			int num = 0;
			int tempToNodeID = 0;
			//如果到达的点为空 
//                首先判断当前节点的ID，是否配置到了其他节点里面，
//                 * * 如果有则需要转向高级的选择框中去，当前界面不能满足公文类的选择人需求。
			String sql = "SELECT COUNT(*) FROM WF_Node WHERE FK_Flow='" + this.getHisNode().getFK_Flow() + "' AND " + NodeAttr.DeliveryWay + "=" + DeliveryWay.BySelected.ordinal() + " AND " + NodeAttr.DeliveryParas + " LIKE '%" + this.getHisNode().getNodeID() + "%' ";

			if (DBAccess.RunSQLReturnValInt(sql, 0) > 0)
			{
				//说明以后的几个节点人员处理的选择 
				String url = "AccepterAdv.jsp?1=3" + _request.getParameterMap();
				try {
					_response.sendRedirect(url);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return "";
			}

			Nodes nds = this.getHisNode().getHisToNodes();
			if (nds.size() == 0)
			{
				//this.Pub1.AddFieldSetRed("提示", "当前点是最后的一个节点，不能使用此功能。");
				return "";
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
						cond.setWorkID( this.getWorkID());
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
				//this.WinCloseWithMsg("@流程设计错误：\n\n 当前节点的所有分支节点没有一个接受人员规则为按照选择接受。");
				return "";
			}
			try {
				_response.sendRedirect("Accepter.jsp?FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node() + "&ToNode=" + tempToNodeID + "&FID=" + this.getFID() + "&type=1&WorkID=" + this.getWorkID() + "&IsWinOpen=" + this.getIsWinOpen());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "";
		}


		try
		{
			// 首先判断是否有多个分支的情况。
			if (this.getIsMFZ() && getToNode() == 0)
			{
				IsMultiple = true;
				//this.BindMStations();
				return "";
			}

			MySelector = new Selector(this.getToNode());
			switch (MySelector.getSelectorModel())
			{
				//case SelectorModel.Station:
				//    this.BindByStation();
				//    break;
				//case SelectorModel.SQL:
				//    this.BindBySQL();
				//    break;
				//case SelectorModel.Dept:
				//    this.BindByDept();
				//    break;
				case Emp:
					this.BindByEmp();
					break;
				case Url:
					if (MySelector.getSelectorP1().contains("?"))
					{
						try {
							_response.sendRedirect(MySelector.getSelectorP1() + "&WorkID=" + this.getWorkID() + "&FK_Node=" + this.getFK_Node());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					else
					{
						try {
							_response.sendRedirect(MySelector.getSelectorP1() + "?WorkID=" + this.getWorkID() + "&FK_Node=" + this.getFK_Node());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					return "";
				default:
					break;
			}
		}
		catch (RuntimeException ex)
		{
			//this.Pub1.Clear();
			//this.Pub1.AddMsgOfWarning("错误", ex.Message);
		}

		///#endregion
		return "";
	}
	/** 
	 按BindByEmp 方式
	 
	*/
	public final String BindByEmp()
	{
		String s_responsetext = "";
		String s_checkded = "";
		//节点部门集合
		NodeDepts nodeDepts = new NodeDepts();
		QueryObject obj = new QueryObject(nodeDepts);
		obj.AddWhere(NodeDeptAttr.FK_Node, this.getFK_Node());
		obj.DoQuery();
		//将已有部门，拼接字符串
		if (nodeDepts != null && nodeDepts.size() > 0)
		{
			for (NodeDept item : nodeDepts.ToJavaList())
			{
				s_checkded += "," + item.getFK_Dept() + ",";
			}
		}
		String BindByEmpSql = String.format("select A.* from (select No,Name,ParentNo  from Port_Dept   WHERE No IN (SELECT FK_Dept " + "FROM Port_Emp WHERE No in(SELECT FK_EMP FROM WF_NodeEmp WHERE FK_Node=%s)) union " + "select No,Name,FK_Dept as ParentNo  from Port_Emp  WHERE No in (SELECT FK_EMP FROM WF_NodeEmp WHERE FK_Node=%s)) A where 1=1 ", MySelector.getNodeID(), MySelector.getNodeID());
		DataTable BindByEmpDt = DBAccess.RunSQLReturnTable(BindByEmpSql);
		s_responsetext = GetTreeJsonByTable(BindByEmpSql, BindByEmpDt, "NO", "NAME", "ParentNo", "0", s_checkded);
		return s_responsetext;
	}
	/** 
	 按table方式.
	 
	//public void BindBySQL_Table(DataTable dtGroup, DataTable dtObj)
	//{
	//    int col = 4;
	//    this.Pub1.AddTable("style='border:0px;width:100%'");
	//    foreach (DataRow drGroup in dtGroup.Rows)
	//    {
	//        string ctlIDs = "";
	//        string groupNo = drGroup[0).ToString();
	*/

	//        //增加全部选择.
	//        this.Pub1.AddTR();
	//        CheckBox cbx = new CheckBox();
	//        cbx.ID = "CBs_" + drGroup[0).ToString();
	//        cbx.Text = drGroup[1).ToString();
	//        this.Pub1.AddTDTitle("align=left", cbx);
	//        this.Pub1.AddTREnd();

	//        this.Pub1.AddTR();
	//        this.Pub1.AddTDBegin("nowarp=false");

	//        this.Pub1.AddTable("style='border:0px;width:100%'");
	//        int colIdx = -1;
	//        foreach (DataRow drObj in dtObj.Rows)
	//        {
	//            string no = drObj[0).ToString();
	//            string name = drObj[1).ToString();
	//            string group = drObj[2).ToString();
	//            if (group.Trim() != groupNo.Trim())
	//                continue;

	//            colIdx++;
	//            if (colIdx == 0)
	//                this.Pub1.AddTR();

	//            CheckBox cb = new CheckBox();
	//            cb.ID = "CB_" + no;
	//            ctlIDs += cb.ID + ",";
	//            cb.Attributes["onclick"] = "isChange=true;";
	//            cb.Text = name;
	//            cb.Checked = false;
	//            if (cb.Checked)
	//                cb.Text = "<font color=green>" + cb.Text + "</font>";
	//            this.Pub1.AddTD(cb);
	//            if (col - 1 == colIdx)
	//            {
	//                this.Pub1.AddTREnd();
	//                colIdx = -1;
	//            }
	//        }
	//        cbx.Attributes["onclick"] = "SetSelected(this,'" + ctlIDs + "')";

	//        if (colIdx != -1)
	//        {
	//            while (colIdx != col - 1)
	//            {
	//                colIdx++;
	//                this.Pub1.AddTD();
	//            }
	//            this.Pub1.AddTREnd();
	//        }
	//        this.Pub1.AddTableEnd();
	//        this.Pub1.AddTDEnd();
	//        this.Pub1.AddTREnd();
	//    }
	//    this.Pub1.AddTableEnd();

	//    this.BindEnd();
	//}
	
	private String boolToValue(boolean bool)
	{
		if(bool)
			return "1";
		else
			return "0";
	}

	public final String BindBySQL_Tree(DataTable dtGroup, DataTable dtDB)
	{
		return "";
	}
	/** 
	 根据DataTable生成Json树结构
	 
	 @param tabel 数据源
	 @param idCol ID列
	 @param txtCol Text列
	 @param rela 关系字段
	 @param pId 父ID
	@return easyui tree json格式
	*/
	private StringBuilder treeResult = new StringBuilder();
	private StringBuilder treesb = new StringBuilder();
	public final String GetTreeJsonByTable(String sql,DataTable tabel, String idCol, String txtCol, String rela, Object pId, String CheckedString)
	{
		String treeJson = "";
		treeResult.append(treesb.toString());
		DataTable tmpTable = null;
		// rows = null;
		treesb.setLength(0);
		if (tabel.Rows.size() > 0)
		{
			treesb.append("[");
			String filer = "";
			if (pId.toString().equals(""))
			{
				filer = String.format("A.%s is null", rela);
			}
			else
			{
				filer = String.format("A.%s='%s'", rela, pId);
			}
			//DataRow[] rows = tabel.Select(filer);
			tmpTable = DBAccess.RunSQLReturnTable(sql+" and "+filer);
			DataRowCollection rows = tmpTable.Rows;
			if (rows.size() > 0)
			{
				for (DataRow row : rows)
				{
					String deptNo = row.getValue(idCol).toString();

					if (treeResult.length() == 0)
					{
						treesb.append("{\"id\":\"" + row.getValue(idCol) + 
								"\",\"text\":\"" + row.getValue(txtCol) + 
								"\",\"checked\":" + boolToValue(CheckedString.contains("," + row.getValue(idCol) + ","))  + ",\"state\":\"open\"");

					}
					else {
						String fi = String.format("A.%s='%s'", rela, row.getValue(idCol));
						DataTable tmpTable2 = DBAccess.RunSQLReturnTable(sql+" and "+fi);
						DataRowCollection rows2 = tmpTable2.Rows;
						if (rows2.size() > 0)
						{
								//+ "\",\"IsParent\":\"" + row.getValue(IsParent]
							treesb.append("{\"id\":\"" + row.getValue(idCol)  + "\",\"text\":\"" + row.getValue(txtCol) + "\",\"checked\":" + boolToValue(CheckedString.contains("," + row.getValue(idCol) + ",")) + ",\"state\":\"closed\"");
							//+ "\",\"checked\":" + CheckedString.Contains("," + row.getValue(idCol] + ",").ToString().ToLower() + ",\"state\":\"open\"");
						}
						else
						{
								//+ "\",\"IsParent\":\"" +row.getValue(IsParent]
							treesb.append("{\"id\":\"" + row.getValue(idCol) + "\",\"text\":\"" + row.getValue(txtCol) + "\",\"checked\":" + boolToValue(CheckedString.contains("," + row.getValue(idCol))));
							//+ "\",\"checked\":" + CheckedString.Contains("," + row.getValue(idCol] + ",").ToString().ToLower());
						}
					}

					String fi = String.format("A.%s='%s'", rela, row.getValue(idCol));
					DataTable tmpTable3 = DBAccess.RunSQLReturnTable(sql+" and "+fi);
					DataRowCollection rows3 = tmpTable3.Rows;
					if (rows3.size() > 0)
					{
						treesb.append(",\"children\":");
						GetTreeJsonByTable(sql,tabel, idCol, txtCol, rela, row.getValue(idCol), CheckedString);
						treeResult.append(treesb.toString());
						treesb.setLength(0);
					}
					treeResult.append(treesb.toString());
					treesb.setLength(0);
					treesb.append("},");
				}
				treesb = treesb.deleteCharAt(treesb.length() - 1);
			}
			treesb.append("]");
			treeResult.append(treesb.toString());
			treeJson = treeResult.toString();
			treesb.setLength(0);
		}
		return treeJson;
	}


}