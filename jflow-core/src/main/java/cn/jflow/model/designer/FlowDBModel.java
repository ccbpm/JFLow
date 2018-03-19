package cn.jflow.model.designer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.En.QueryObject;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.WF.Flow;
import BP.WF.WorkFlow;
import BP.WF.Entity.GenerWorkFlow;
import BP.WF.Entity.GenerWorkFlowAttr;
import BP.WF.Entity.GenerWorkFlows;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.core.LinkButton;
import cn.jflow.system.ui.core.NamesOfBtn;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;

public class FlowDBModel extends BaseModel {

	public String FK_Flow;
	public String Depts;
	public String Emps;
	public String DateFrom;
	public String DateTo;
	public String Keywords;
	public int WorkID;
	public String DeptsText;
	public String EmpsText;
	
	public boolean IsSearch;
	public String DoType;
	
	public String basePath;
	public StringBuilder Pub1 = null;
	public StringBuilder Pub2 = null;
	public StringBuilder Pub3 = null;
	public FlowDBModel(HttpServletRequest request,
			HttpServletResponse response, String basePath, String FK_Flow,
			String Depts, String Emps, String DateFrom, String DateTo,
			String Keywords, int WorkID, String DeptsText, String EmpsText,
			boolean IsSearch, String DoType) {
		super(request, response);
		Pub1 = new StringBuilder();
		Pub2 = new StringBuilder();
		Pub3 = new StringBuilder();
		this.basePath = basePath;
		this.FK_Flow = FK_Flow;
		this.Depts = Depts;
		this.Emps = Emps;
		this.DateFrom = DateFrom;
		this.DateTo = DateTo;
		this.Keywords = Keywords;
		this.WorkID = WorkID;
		this.DeptsText = DeptsText;
		this.EmpsText = EmpsText;
		
		this.IsSearch = IsSearch;
		this.DoType = DoType;
	}
	
	public void init(){
		
		if ("DelIt".equals(DoType)){
			 try{
                 WorkFlow wf = new WorkFlow(this.FK_Flow, this.WorkID);
                 wf.DoDeleteWorkFlowByReal(true);
             }
             catch (Exception ex){
            	this.winCloseWithMsg(ex.getMessage());
             }
			 this.winCloseWithMsg("删除成功！");
		}else{
            Pub3.append("<div style='width:100%; padding: 2px; height: auto;background-color:#E0ECFF; line-height:30px'>");
            Pub3.append("部门：");
            TextBox tb = new TextBox();
            tb.setId("TB_Dept");
            tb.setText(DeptsText);
            tb.addAttr("Style", "width:100px;");;
            Pub3.append(tb);
            
            tb =  new TextBox();
            tb.setTextMode(TextBoxMode.Hidden);
            tb.setId("Hid_Dept");
            tb.setText(Depts);
            Pub3.append(tb);
            
            Pub3.append("<a class='easyui-linkbutton' href=\"javascript:openSelectDept('Hid_Dept','TB_Dept')\" data-options=\"iconCls:'icon-department',plain:true\" title='选择部门'> </a>&nbsp;&nbsp;");
		
            Pub3.append("发起人：");
            tb = new TextBox();
            tb.setId("TB_FQR");
            tb.setText(EmpsText);
            tb.addAttr("Style", "width:100px;");
            Pub3.append(tb);
            
            tb =  new TextBox();
            tb.setTextMode(TextBoxMode.Hidden);
            tb.setId("Hid_FQR");
            tb.setText(Emps);
            Pub3.append(tb);
            
            Pub3.append("<a class='easyui-linkbutton' href=\"javascript:openSelectEmp('Hid_FQR','TB_FQR')\" data-options=\"iconCls:'icon-user',plain:true\" title='选择发起人'> </a>&nbsp;&nbsp;");
		
            Pub3.append("发起日期：");
            tb = new TextBox();
            tb.setId("TB_DateFrom");
            tb.setText(DateFrom);
            tb.addAttr("Style", "width:80px;");
            tb.addAttr("onfocus", "WdatePicker();");
            Pub3.append(tb);

            Pub3.append(BaseModel.AddSpace(1));
            Pub3.append("到");
            Pub3.append(BaseModel.AddSpace(1));
            
            tb = new TextBox();
            tb.setId("TB_DateTo");
            tb.setText(DateTo);
            tb.addAttr("Style", "width:80px;");
            tb.addAttr("onfocus", "WdatePicker();");
            Pub3.append(tb);
            Pub3.append(BaseModel.AddSpace(2));
         
            Pub3.append("关键字：");
            tb = new TextBox();
            tb.setId("TB_KeyWords");
            tb.setText(Keywords);
            tb.addAttr("Style", "width:100px;");
            Pub3.append(tb);
            
            LinkButton lbtn = new LinkButton(false, NamesOfBtn.Search.getCode(), "查询");
            lbtn.setHref("doPostBack()");
            
            Pub3.append(BaseModel.AddSpace(3));
            Pub3.append(lbtn);

            Pub3.append(BaseModel.AddDivEnd());
            
            if (IsSearch)
                BindSearch();
		}
	}
	
	
	public void BindSearch() {
		Flow fl = new Flow(FK_Flow);
		GenerWorkFlows gwfs = new GenerWorkFlows();
		QueryObject qo = new QueryObject(gwfs);
		qo.AddWhere(GenerWorkFlowAttr.FK_Flow, FK_Flow);

		if (!StringHelper.isNullOrEmpty(Depts)) {
			qo.addAnd();
			qo.AddWhereIn(GenerWorkFlowAttr.FK_Dept, "(" + Depts + ")");
		}

		if (!StringHelper.isNullOrEmpty(Emps)) {
			qo.addAnd();
			String array[] = Emps.split(",");
			
			String ary = "";
			for(String str : array){
				if(ary.length()>0){
					ary += ",'"+str+"'";
				}else{
					ary = "'"+str+"'";
				}
			}
			qo.AddWhereIn(GenerWorkFlowAttr.Starter,"(" + ary + ")");
			// qo.AddWhereIn(GenerWorkFlowAttr.Starter, "(" +
			// Emps.split(",").Aggregate(String.Empty, (curr, next) => curr +
			// "'" + next + "',").TrimEnd(',') + ")");
		}

		if (!StringHelper.isNullOrEmpty(DateFrom)) {
			qo.addAnd();
			qo.AddWhere(GenerWorkFlowAttr.RDT, ">", DateFrom);
		}

		if (!StringHelper.isNullOrEmpty(DateTo)) {
			qo.addAnd();
			qo.AddWhere(GenerWorkFlowAttr.RDT, "<=", DateTo);
		}

		if (!StringHelper.isNullOrEmpty(Keywords)) {
			qo.addAnd();
			qo.AddWhere(GenerWorkFlowAttr.Title, "LIKE", "%" + Keywords + "%");
		}

		qo.addOrderBy(GenerWorkFlowAttr.RDT);

		String url = String
				.format("FlowDB.jsp?FK_Flow="+FK_Flow+"&WorkID="+WorkID+"&IsSearch=1&Depts="+Depts+"&DeptsText="+DeptsText+"&Emps="+Emps+"&EmpsText="+EmpsText+"&DateFrom="+DateFrom+"&DateTo="+DateTo+"&Keywords="+Keywords
);

		BindPageIdxEasyUi(Pub2, qo.GetCount(), url, BaseModel.getPageIdx(),
				SystemConfig.getPageSize(),
				"'first','prev','sep','manual','sep','next','last'", false);
		
		qo.DoQuery(gwfs.getGetNewEntity().getPK(), SystemConfig.getPageSize(),
				BaseModel.getPageIdx());

		Pub1.append(AddTable("class='Table' cellspacing='0' cellpadding='0' border='0' style='width:100%'"));

		Pub1.append(AddTR());
		Pub1.append(AddTDGroupTitle("colspan='8'", fl.getName()));
		Pub1.append(AddTREnd());

		Pub1.append(AddTR());
		Pub1.append(AddTDGroupTitle("style='text-align:center'", "序号"));
		Pub1.append(AddTDGroupTitle("部门"));
		Pub1.append(AddTDGroupTitle("发起人"));
		Pub1.append(AddTDGroupTitle("发起时间"));
		Pub1.append(AddTDGroupTitle("当前停留节点"));
		Pub1.append(AddTDGroupTitle("标题"));
		Pub1.append(AddTDGroupTitle("处理人"));
		Pub1.append(AddTDGroupTitle("操作"));
		Pub1.append(AddTREnd());

		int idx = 0;
		for (GenerWorkFlow item : gwfs.ToJavaList()) {
			idx++;
			Pub1.append(AddTR());
			this.Pub1.append(AddTDIdx(SystemConfig.getPageSize()
					* (BaseModel.getPageIdx() - 1) + idx));
			this.Pub1.append(AddTD(item.getDeptName()));
			this.Pub1.append(AddTD(item.getStarterName()));
			this.Pub1.append(AddTD(item.getRDT()));
			this.Pub1.append(AddTD(item.getNodeName()));
			this.Pub1.append(AddTD(item.getTitle()));
			this.Pub1.append(AddTD(item.getTodoEmps()));
			//this.Pub1.append("\n<TD>"+item.getTodoEmps()+"</TD>");

			this.Pub1.append(AddTDBegin());
			this.Pub1
					.append("<a href=\"javascript:WinOpen('"+this.basePath+"WF/WFRpt.jsp?WorkID="
							+ item.getWorkID()
							+ "&FK_Flow="
							+ this.FK_Flow
							+ "&FID="
							+ item.getFID()
							+ "','ds'); \" class='easyui-linkbutton'>轨迹</a>&nbsp;");
			// this.Pub1.Add("<a href=\"javascript:WinOpen('../../../WFRpt.jsp?WorkID="
			// + item.WorkID + "&FK_Flow=" + this.FK_Flow + "&FID=" + item.FID +
			// "&FK_Node=" + item.FK_Node + "','ds'); \" >报告</a>-");
			this.Pub1
					.append("<a href=\"javascript:DelIt('"
							+ item.getFK_Flow()
							+ "','"
							+ item.getWorkID()
							+ "');\" class='easyui-linkbutton' data-options=\"iconCls:'icon-delete'\" onclick=\"return confirm('您确定要删除吗？');\">删除</a>&nbsp;");
			/*this.Pub1.append("<a href=\"javascript:FlowShift('"
					+ item.getFK_Flow() + "','" + item.getWorkID()
					+ "');\" class='easyui-linkbutton'>移交</a>&nbsp;");
			this.Pub1.append("<a href=\"javascript:FlowSkip('"
					+ item.getFK_Flow() + "','" + item.getWorkID()
					+ "');\" class='easyui-linkbutton'>跳转</a>");*/
			this.Pub1.append(AddTDEnd());
			this.Pub1.append(AddTREnd());
		}
		this.Pub1.append(AddTableEnd());
	}

}
