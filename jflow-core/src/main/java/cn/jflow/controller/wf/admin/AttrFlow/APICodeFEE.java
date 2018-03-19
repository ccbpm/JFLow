package cn.jflow.controller.wf.admin.AttrFlow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Tools.StringHelper;
import BP.WF.Flow;
import cn.jflow.common.model.BaseModel;

public class APICodeFEE 
{
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	public StringBuffer Pub1=null;
	public final String getFK_Flow()
	{
		return request.getParameter("FK_Flow");
	}

	public final boolean getDownload()
	{
		if(null == request.getParameter("Download"))
		{
			return false;
		}else
		{
			return request.getParameter("Download").equals("1");
		}
	}

	private String Title;
	public final String getTitle()
	{
		return Title;
	}
	public final void setTitle(String value)
	{
		Title = value;
	}

	public APICodeFEE(HttpServletRequest request, HttpServletResponse response) {
		this.request= request;
		this.response = response;
		Pub1 = new StringBuffer();
	}
	public final void Page_Load()
	{
		if (StringHelper.isNullOrEmpty(getFK_Flow()))
		{
			Pub1.append(BaseModel.AddEasyUiPanelInfo("错误", "FK_Flow参数不能为空！"));
			return;
		}

		Flow flow = new Flow(getFK_Flow());

		if (StringHelper.isNullOrEmpty(flow.getNo()))
		{
			Pub1.append(BaseModel.AddEasyUiPanelInfo("错误", String.format("FK_Flow参数不正确，未找到编号为%1$s的流程！", getFK_Flow())));
			return;
		}
        String path =this.getClass().getResource("").getPath();
        path = path.substring(0,path.indexOf("target"));
		String tmpPath =  path+ "/src/main/java/cn/jflow/controller/wf/admin/AttrFlow/APICodeFEE.java";
		if (!new File(tmpPath).exists())
		{
			Pub1.append(BaseModel.AddEasyUiPanelInfo("错误", String.format("未找到事件编写模板文件“%1$s”，请联系管理员！", tmpPath)));
			return;
		}

		Title = flow.getName() + "[" + flow.getNo() + "]";
		String line = "";
		String code = "";
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(tmpPath),"utf-8"));
			while((line= br.readLine()) !=null)
			{
				code += "\r"+line;
			}
			code = code.replace("F001Templepte", String.format("FEE%1$s", flow.getNo())).replace("@FlowName", flow.getName()).replace("@FlowNo", flow.getNo());
	
			if (getDownload())
			{
					//response.ClearHeaders();
					//response.clear();
					//response.Expires = 0;
					//response.Buffer(true);
					response.setHeader("Content-Type", "text/html; charset=utf-8");
					response.setHeader("content-disposition", String.format("attachment; filename=FEE%1$s.cs", flow.getNo()));
					response.setContentType("application/octet-stream");
					response.getWriter().print(code);
				//response.end();
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		//此处将重要行标示出来，根据下面的数组中的项来检索重要行号
		String[] lineStrings = new String[] { "namespace BP.FlowEvent", ": BP.WF.FlowEventBase", "public override string FlowMark", "public override string SendWhen()", "public override string SendSuccess()", "public override string SendError()", "public override string FlowOverBefore()", "public override string FlowOverAfter()", "public override string BeforeFlowDel()", "public override string AfterFlowDel()", "public override string SaveAfter()", "public override string SaveBefore()", "public override string UndoneBefore()", "public override string UndoneAfter()", "public override string ReturnBefore()", "public override string ReturnAfter()", "public override string AskerAfter()", "public override string AskerReAfter()" };

		Pub1.append(BaseModel.AddLi("<a href=\"APICodeFEE.jsp?FK_Flow="+getFK_Flow()+"&Download=1\" target=\"_blank\" class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-save',plain:true\">下载代码</a><br />"));
		Pub1.append("<pre type=\"syntaxhighlighter\" class=\"brush: csharp; html-script: false; highlight: ["+GetImportantLinesNumbers(lineStrings, code)+"]\" title=\""+flow.getName()+"[编号："+flow.getNo()+"] 流程自定义事件代码生成\">");
		Pub1.append(BaseModel.Add(code.replace("<", "&lt;"))); //SyntaxHighlighter中，使用<Pre>包含的代码要将左尖括号改成其转义形式
		Pub1.append(BaseModel.Add("</pre>"));
		Pub1.append(BaseModel.Add("<script type=\"text/javascript\">SyntaxHighlighter.highlight();</script>"));
		Pub1.append(BaseModel.Add("<a href=\"APICodeFEE.jsp?FK_Flow="+getFK_Flow()+"&Download=1\" target=\"_blank\" class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-save',plain:true\">下载代码</a>  您需要把该代码整合到您的类库里，并且该类库必须以BP 开头命名。<br />"));
	}
	/** 
	 获取重要行的标号连接字符串，如3,6,8
	 
	 @param lineInStrings 重要行中包含的字符串数组，只要行中包含其中的一项字符串，则这行就是重要行
	 @param str 要检索的字符串，使用Environment.NewLine分行
	 @return 
	*/
	private String GetImportantLinesNumbers(String[] lineInStrings, String str)
	{
		String[] lines = str.replace("\r\n", "`").split("[`]", -1);
		String nums = "";

		for (int i = 0; i < lines.length; i++)
		{
			for (String instr : lineInStrings)
			{
				if (lines[i].indexOf(instr) != -1)
				{
					nums += (i + 1) + ",";
					break;
				}
			}
		}

		return StringHelper.trimEnd(nums, ',');
	}
}

