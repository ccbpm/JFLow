package cn.jflow.model.wf.mapdef;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;
import BP.En.QueryObject;
import BP.Sys.SFDBSrcAttr;
import BP.Sys.SFDBSrcs;
import BP.Sys.SFTable;
import BP.Sys.SFTableAttr;
import BP.Tools.StringHelper;
import BP.WF.Glo;

public class SFWSModel extends BaseModel{
	
	private StringBuilder pubBuilder;
	
	private String refNo;
	
	private String title;
	
	public String getTitle() {
		return title;
	}
	
	public String getPubBuilder() {
		return pubBuilder.toString();
	}

	public SFWSModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	
	public String getIDX(){
		return getParameter("IDX");
	}
	public final String getFromApp() {
		return getParameter("FromApp");
	}
	public final String getDoType() {
		return getParameter("DoType");
	}


	private void appendPubBuilder(String str){
		pubBuilder.append(str);
	}
	
	public void pageLoad()
	{
		pubBuilder = new StringBuilder();
		refNo = getRefNo();
		SFTable tempVar = new SFTable();
		tempVar.setFK_SFDBSrc(""); 
		SFTable main = tempVar; //此处FK_SFDBSrc的默认值为local，需要将其设为空，否则下方报错


		if (!StringHelper.isNullOrEmpty(refNo))
		{
			main.setNo(refNo);
			main.Retrieve();
		}
		this.BindSFTable(main);
	}
	
	public void BindSFTable(SFTable en){
		boolean isItem = false;
		String star = "<font color=red><b>(*)</b></font>";
		appendPubBuilder(AddTable());
		if ("SL".equals(this.getFromApp())) {
			if (StringHelper.isNullOrEmpty(refNo)) {
				appendPubBuilder(AddCaption("新建WebService数据源接口"));
			} else {
				appendPubBuilder(AddCaption("编辑WebService数据源接口"));
			}
		} else {
			appendPubBuilder(AddCaption("<a href='Do.jsp?DoType=AddF&MyPK=" + this.getMyPK() + "&IDX=" + this.getIDX() + "'><img src='"+Glo.getCCFlowAppPath()+"WF/Img/Btn/Back.gif'>返回</a> - <a href='Do.jsp?DoType=AddSFWS&MyPK=" + this.getMyPK() + "&IDX=" + this.getIDX() + "'>WebService数据源接口</a> - 新建WebService数据源接口"));
		}
		if (StringHelper.isNullOrEmpty(refNo)) {
			this.title = "新建WebService数据源接口";
		} else {
			this.title = "编辑WebService数据源接口";
		}
		int idx = 0;
		appendPubBuilder(AddTR());
		appendPubBuilder(AddTDTitle("Idx"));
		appendPubBuilder(AddTDTitle("项目"));
		appendPubBuilder(AddTDTitle("采集"));
		appendPubBuilder(AddTDTitle("备注"));
		appendPubBuilder(AddTREnd());
		
		
		appendPubBuilder(AddTR(isItem));
		appendPubBuilder(AddTDIdx(idx++));
		appendPubBuilder(AddTD("接口英文名称" + star));
		TextBox tb = new TextBox();
		tb.setId("TB_"+SFTableAttr.No);
		tb.setText(en.getNo());
		if (StringHelper.isNullOrEmpty(refNo)) {
			tb.setEnabled(true);
		} else {
			tb.setEnabled(false);
		}

		if (tb.getText().equals("")) {
			tb.setText("SF_");
		}
		appendPubBuilder(AddTD(tb));
		appendPubBuilder(AddTDBigDoc("必须以字母或者下划线开头，不能包含特殊字符。"));
		appendPubBuilder(AddTREnd());

		
		appendPubBuilder(AddTR(isItem));
		appendPubBuilder(AddTDIdx(idx++));
		appendPubBuilder(AddTD("接口中文名称" + star));
		tb = new TextBox();
		tb.setId("TB_"+SFTableAttr.Name);
		tb.setText(en.getName());
		appendPubBuilder(AddTD(tb));
		appendPubBuilder(AddTD("WebService中的接口方法的中文名称。"));
		appendPubBuilder(AddTREnd());
		
		
		appendPubBuilder(AddTR(isItem));
		appendPubBuilder(AddTDIdx(idx++));
		appendPubBuilder(AddTD("数据源" + star));
		DDL ddl = new DDL();
		ddl.setId("DDL_"+SFTableAttr.FK_SFDBSrc);
		SFDBSrcs srcs = new SFDBSrcs();
		QueryObject qo = new QueryObject(srcs);
		qo.AddWhere(SFDBSrcAttr.DBSrcType, " = ", "100");
		qo.DoQuery();
		ddl.Bind(srcs, en.getFK_SFDBSrc());
		appendPubBuilder(AddTD(ddl));
		appendPubBuilder(AddTD("选择数据源,点击这里<a href=\"javascript:WinOpen('"+Glo.getCCFlowAppPath()+"WF/Comm/Search.jsp?EnsName=BP.Sys.SFDBSrcs')\">创建</a>，<a href='SFSQL.jsp?DoType=New&MyPK=" + this.getMyPK() + "&Idx='>刷新</a>。"));
		appendPubBuilder(AddTREnd());
		
		
		appendPubBuilder(AddTR(isItem));
		appendPubBuilder(AddTDIdx(idx++));
		appendPubBuilder(AddTD("选择接口" + star));
		ddl = new DDL();
		ddl.setId ("DDL_" + SFTableAttr.TableDesc);
		
		/*	
		if (srcs.size() > 0) {
			Map<String, String> ms = GetWebServiceMethods(!StringHelper
					.isNullOrEmpty(en.getFK_SFDBSrc()) ? (SFDBSrc) srcs
					.GetEntityByKey(SFDBSrcAttr.No, en.getFK_SFDBSrc())
					: (SFDBSrc) srcs[0]);
			for (var m : ms) {
				ddl.Items.add(new ListItem(m.getValue(), m.getKey()));
			}
			ddl.SetSelectItem(rt.getLength() == 2 ? rt[0] : ms.size() > 0 ? ms.First().getKey() : "");
		}*/

		appendPubBuilder(AddTD(ddl));
		appendPubBuilder(AddTD("选择WebService中的接口方法名."));
		appendPubBuilder(AddTREnd());

		appendPubBuilder(AddTR(isItem));
		appendPubBuilder(AddTDIdx(idx++));
		appendPubBuilder(AddTD("colspan=3", "接口参数定义" + star + "支持jform表达式，允许有WebUser.No,@WebUser.Name,@WebUser.FK_Dept变量。"));
		appendPubBuilder(AddTREnd());
		
		
		appendPubBuilder(AddTR(isItem));
		appendPubBuilder(AddTDIdx(idx++));
		tb = new TextBox();
		tb.setId("TB_"+ SFTableAttr.SelectStatement);//查询
		tb.setText(en.getSelectStatement()); //查询语句.
		tb.setTextMode(TextBoxMode.MultiLine);
		//tb.setRows(4);
		//tb.setColumns(70);
		tb.addAttr("style", "width:95%;height:50px;");
		appendPubBuilder(AddTD("colspan=3", tb));
		appendPubBuilder(AddTREnd());
		

		appendPubBuilder(AddTR(isItem));
		appendPubBuilder(AddTDIdx(idx++));
		appendPubBuilder(AddTD("colspan=3", "如：WorkId=@WorkID&FK_Flow=@FK_Flow&FK_Node=@FK_Node&SearchType=1，"
				+ "带@的参数值在运行时自动使用发起流程的相关参数值替换，而不带@的参数值使用后面的赋值；参数个数与WebServices接口方法的参数个数一致，"
				+ "且顺序一致，且值均为字符类型。"));
		appendPubBuilder(AddTREnd());
		
		appendPubBuilder(AddTR(isItem));
		appendPubBuilder(AddTDIdx(idx++));
		appendPubBuilder(AddTD("返回值类型" + star));
		ddl = new DDL();
		ddl.setId ("DDL_ResultType");
		ddl.Items.add(new ListItem("DataTable数据表", "DataTable"));
		ddl.Items.add(new ListItem("DataSet数据集", "DataSet"));
		ddl.Items.add(new ListItem("Json字符串", "Json"));
		ddl.Items.add(new ListItem("Xml字符串", "Xml"));

		/*if (rt.getLength() == 2) {
			ddl.SetSelectItem(rt[1]);
		}*/

		appendPubBuilder(AddTDBegin());
		appendPubBuilder(Add(ddl));
		appendPubBuilder(AddBR());
		appendPubBuilder(Add("注意：所有返回值类型都需有No,Name这两列。" + "<script type='text/javascript'>" + "   var info = '1. DataTable数据表，必须为DataTable命名。\\n" + "2. DataSet数据集，只取数据集里面的第1个DataTable。\\n" + "3. Json字符串，格式如：\\n" + "[\\n" + "  {\"No\":\"001\",\"Name\":\"生产部\"},\\n" + "  {\"No\":\"002\",\"Name\":\"研发部\"},\\n" + "  ...\\n" + "]\\n" + "4. Xml字符串，格式如：\\n" + "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\\n" + "<Array>\\n" + "  <Item>\\n" + "    <No>001</No>\\n" + "    <Name>生产部</Name>\\n" + "  </Item>\\n" + "  <Item>\\n" + "    <No>002</No>\\n" + "    <Name>研发部</Name>\\n" + "  </Item>\\n" + "  ...\\n" + "</Array>';" + "</script>" + "<a href='javascript:void(0)' onclick='alert(info)'>格式说明</a>"));
		appendPubBuilder(AddTDEnd());
		appendPubBuilder(AddTDBigDoc("选择WebService中的接口方法返回值的类型。"));
		appendPubBuilder(AddTREnd());
		
		appendPubBuilder(AddTR(isItem));
		appendPubBuilder(AddTDIdx(idx++));
		appendPubBuilder(AddTD("返回数据结构"));
		ddl = new DDL();
		ddl.setId ("DDL_" + SFTableAttr.CodeStruct);
		ddl.BindSysEnum(SFTableAttr.CodeStruct, en.getCodeStruct().getValue());
		appendPubBuilder(AddTD(ddl));
		appendPubBuilder(AddTD("WebService接口返回的数据结构，用于在下拉框中不同格式的展现。"));
		appendPubBuilder(AddTREnd());
		
		appendPubBuilder(AddTR());
		appendPubBuilder(AddTDIdx(idx++));
		appendPubBuilder("<TD colspan=3 align=center>");
		Button btn = new Button();
		btn.setId("Btn_Save");
		btn.setCssClass("Btn");
		if (StringHelper.isNullOrEmpty(refNo))
		{
			btn.setText("创建");
		}
		else
		{
			btn.setText("保存");
		}
		btn.addAttr("onclick", " btn_Save_Click();");
		appendPubBuilder(btn.toString());

	

		if (!"SL".equals(this.getFromApp())) {
			btn = new Button();
			btn.setId("Btn_Add");
			btn.setCssClass("Btn");
	
			btn.setText("添加到表单"); // "添加到表单";
			btn.addAttr("onclick", " return confirm('您确认吗？');");
			btn.addAttr("onclick", " btn_Add_Click();");
			if (StringHelper.isNullOrEmpty(refNo))
			{
				btn.setEnabled(false);
			}
	
			appendPubBuilder(btn.toString());
		}
		
		
		
		btn = new Button();
		btn.setId("Btn_Del");
		btn.setCssClass("Btn");

		btn.setText("删除");
		btn.addAttr("onclick", " return confirm('您确认吗？');");
		btn.addAttr("onclick", " btn_Del_Click();");
		if (StringHelper.isNullOrEmpty(refNo))
		{
			btn.setEnabled(false);
		}

		appendPubBuilder(btn.toString());
		appendPubBuilder("</TD>");
		appendPubBuilder(AddTREnd());
		appendPubBuilder(AddTableEnd());
	}
	/** 
	 获取webservice方法列表
	 
	 @param dbsrc WebService数据源
	 @return 
	*/
	/*public final Map<String, String> GetWebServiceMethods(SFDBSrc dbsrc) {
		if (dbsrc == null || BP.WF.StringHelper.isNullOrEmpty(dbsrc.getIP())) {
			return new java.util.HashMap<String, String>();
		}

		String wsurl = dbsrc.getIP().toLowerCase();
		if (!wsurl.endsWith(".asmx") && !wsurl.endsWith(".svc")) {
			throw new RuntimeException("@失败:" + dbsrc.getNo() + " 中WebService地址不正确。");
		}

		wsurl += wsurl.endsWith(".asmx") ? "?wsdl" : "?singleWsdl";

		//解析WebService所有方法列表
		Map<String, String> methods = new java.util.HashMap<String, String>(); //名称Name，全称Text
		WebClient wc = new WebClient();
		var stream = wc.OpenRead(wsurl);
		var sd = ServiceDescription.Read(stream);
		var eles = sd.Types.Schemas[0].Elements.Values.<XmlSchemaElement>Cast();
		StringBuilder s = new StringBuilder();
		XmlSchemaComplexType ctype = null;
		XmlSchemaSequence seq = null;
		XmlSchemaElement res = null;

		for (var ele : eles) {
			if (ele == null) {
				continue;
			}

			String resType = "";
			String mparams = "";

			//获取接口返回元素
			res = eles.FirstOrDefault(o => o.getName() == (ele.getName() + "Response"));

			if (res != null) {
				//1.接口名称 ele.Name
				//2.接口返回值类型
				ctype = (XmlSchemaComplexType)((res.getSchemaType() instanceof XmlSchemaComplexType) ? res.getSchemaType() : null);
				seq = (XmlSchemaSequence)((ctype.getParticle() instanceof XmlSchemaSequence) ? ctype.getParticle() : null);

				if (seq != null && seq.getItems().size() > 0) {
					resType = ((XmlSchemaElement)((seq.getItems().get(0) instanceof XmlSchemaElement) ? seq.getItems().get(0) : null)).SchemaTypeName.getName();
				}
				else {
					continue; // resType = "void"; //去除不返回结果的接口
				}

				//3.接口参数
				ctype = (XmlSchemaComplexType)((ele.SchemaType instanceof XmlSchemaComplexType) ? ele.SchemaType : null);
				seq = (XmlSchemaSequence)((ctype.getParticle() instanceof XmlSchemaSequence) ? ctype.getParticle() : null);

				if (seq != null && seq.getItems().size() > 0) {
					for (XmlSchemaElement pe : seq.Items) {
						mparams += pe.SchemaTypeName.getName() + " " + pe.getName() + ", ";
					}

					mparams = mparams.TrimEnd((new String(", ")).toCharArray());
				}

				methods.put(ele.getName(), String.format("%1$s %2$s(%3$s)", resType, ele.getName(), mparams));
			}
		}

		stream.Close();
		stream.dispose();
		wc.dispose();

		return methods;
	}
*/

}
