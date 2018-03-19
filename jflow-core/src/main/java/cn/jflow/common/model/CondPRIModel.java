package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DBAccess;
import BP.WF.Node;
import BP.WF.Template.Cond;
import BP.WF.Template.CondAttr;
import BP.WF.Template.Conds;
import cn.jflow.system.ui.UiFatory;

public class CondPRIModel extends BaseModel{

	public CondPRIModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	public UiFatory Pub1=new UiFatory();
	/** 
	 主键
	 
	*/
	public final String getMyPK()
	{
		return this.get_request().getParameter("MyPK");
	}
	/** 
	 流程编号
	 
	*/
	public final String getFK_Flow()
	{
		return this.get_request().getParameter("FK_Flow");
	}
	public final int getFK_MainNode()
	{
		return Integer.parseInt(this.get_request().getParameter("FK_MainNode"));
	}
	public final int getToNodeID()
	{
		try
		{
			return Integer.parseInt(this.get_request().getParameter("ToNodeID"));
		}
		catch (java.lang.Exception e)
		{
			return 0;
		}
	}
		///#endregion 属性

	public  void init()
	{

//		switch (this.DoType)
//ORIGINAL LINE: case "Up":
		if (this.getDoType().equals("Up"))
		{
				Cond up = new Cond(this.getMyPK());
				up.DoUp(this.getFK_MainNode());
				up.RetrieveFromDBSources();
				DBAccess.RunSQL("UPDATE WF_Cond SET PRI=" + up.getPRI() + " WHERE ToNodeID=" + up.getToNodeID());
		}
//ORIGINAL LINE: case "Down":
		else if (this.getDoType().equals("Down"))
		{
				Cond down = new Cond(this.getMyPK());
				down.DoDown(this.getFK_MainNode());
				down.RetrieveFromDBSources();
				DBAccess.RunSQL("UPDATE WF_Cond SET PRI=" + down.getPRI() + " WHERE ToNodeID=" + down.getToNodeID());
		}
		else
		{
		}

		Node nd = new Node(this.getFK_MainNode());

		this.Pub1.append(AddTable("class='Table' cellSpacing='1' cellPadding='1'  border='1' style='width:100%'"));
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD("class='GroupTitle' colspan='7'", nd.getName()));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD("class='GroupTitle'", "从节点ID"));
		this.Pub1.append(AddTD("class='GroupTitle'", "从节点名称"));
		this.Pub1.append(AddTD("class='GroupTitle'", "到节点ID"));
		this.Pub1.append(AddTD("class='GroupTitle'", "到节点名称"));
		this.Pub1.append(AddTD("class='GroupTitle'", "优先级"));
		this.Pub1.append(AddTD("class='GroupTitle' colspan=2", "操作"));
		this.Pub1.append(AddTREnd());

		Conds cds = new Conds();
		//BP.En.QueryObject qo = new QueryObject(cds);
		//qo.AddWhere(CondAttr.FK_Node, this.FK_MainNode);
		//qo.addAnd();
		//qo.AddWhere(CondAttr.FK_Node, this.FK_MainNode);

		cds.Retrieve(CondAttr.FK_Node, this.getFK_MainNode(), CondAttr.CondType, 2, CondAttr.PRI);
		String strs = "";

		for (Cond cd : cds.ToJavaList())
		{
			if (strs.contains("," + String.valueOf(cd.getToNodeID())))
			{
				continue;
			}

			strs += "," + String.valueOf(cd.getToNodeID());

			Node mynd = new Node(cd.getToNodeID());

			this.Pub1.append(AddTR());
			this.Pub1.append(AddTD(nd.getNodeID()));
			this.Pub1.append(AddTD(nd.getName()));
			this.Pub1.append(AddTD(mynd.getNodeID()));
			this.Pub1.append(AddTD(mynd.getName()));
			this.Pub1.append(AddTD(cd.getPRI()));
			this.Pub1.append(AddTD("<a href='CondPRI.jsp?CondType=2&DoType=Up&FK_Flow=" + this.getFK_Flow() + "&FK_MainNode=" + this.getFK_MainNode() + "&ToNodeID=" + this.getToNodeID() + "&MyPK=" + cd.getMyPK() + "' class='easyui-linkbutton' data-options=\"iconCls:'icon-up'\">上移</a>"));
			this.Pub1.append(AddTD("<a href='CondPRI.jsp?CondType=2&DoType=Down&FK_Flow=" + this.getFK_Flow() + "&FK_MainNode=" + this.getFK_MainNode() + "&ToNodeID=" + this.getToNodeID() + "&MyPK=" + cd.getMyPK() + "' class='easyui-linkbutton' data-options=\"iconCls:'icon-down'\">下移</a>"));
			this.Pub1.append(AddTREnd());
		}

		this.Pub1.append(AddTableEnd());
		this.Pub1.append(AddBR());

		String help = "";
		help += "<ul>";
		help += "<li>在转向中，如果出现一个以上的路线都成立时时，系统就会按照第一个路线来计算，那一个排列最前面就按照那一个计算。</li>";
		help += "<li>例如：在demo中002.请假流程，如果一个人员既有基层岗，也有中层岗那么到达基层与中层的路线都会成立，如果设置了方向条件的优先级，系统就会按照优先满足的条件的路线计算。</li>";
		help += "</ul>";

		this.Pub1.append(AddEasyUiPanelInfo("帮助", "<span style='font-weight:bold'>什么是方向条件的优先级？</span><br />" + "\r\n" + help, "icon-help"));
	}


}
