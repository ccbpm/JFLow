package cn.jflow.model.designer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.WF.Node;
import cn.jflow.system.ui.UiFatory;


public class BlockModel {
	public HttpServletRequest request;
	public HttpServletResponse response;
	public UiFatory ui = null;
	public String basePath = "";

	
	public BlockModel (HttpServletRequest request,HttpServletResponse response){
		this.request=request;
		this.response=response;
	}
	
	/**
	 * 节点ID
	 * @return
	 */
	public final int getFK_Node() {
		try {
			return Integer.parseInt(request.getParameter("FK_Node"));
		} catch (Exception e) {
			return 0;
		}
	}
    
	/**
	 * 提示jflow默认信息单选框
	 */
	public String radioCheck1;
	public String radioCheck2;
	public String radioCheck3;
	public String radioCheck4;
	public String radioCheck5;
	/**
	 * 按约定格式阻塞未完成子流程
	 */
	public String specSubFlow;
	/**
	 * 按照SQL阻塞
	 */
	public String SQL;
	/**
	 * 按照表达式阻塞
	 */
	public String exp;
	/**
	 * 其他选项设置
	 */
	public String other;
	
	/**
     * @Description: 节点属性发送阻塞规则页面初始化
	 * @Title: Page_Load
     * @author peixiaofeng
     * @date 2016年5月11日
	 */
	public void Page_Load() {
		Node nd = new Node();
		nd.setNodeID(this.getFK_Node());
		nd.RetrieveFromDBSources();
		this.setOther(nd.getBlockAlert());
		switch (nd.getBlockModel()) {
			case None:
				this.setRadioCheck1("checked='checked'");
				this.setRadioCheck2("");
				this.setRadioCheck3("");
				this.setRadioCheck4("");
				this.setRadioCheck5("");
				this.setSpecSubFlow("");
				this.setSQL("");
				this.setExp("");
				break;
			case CurrNodeAll:
				this.setRadioCheck2("checked='checked'");
				this.setRadioCheck1("");
				this.setRadioCheck3("");
				this.setRadioCheck4("");
				this.setRadioCheck5("");
				this.setSpecSubFlow("");
				this.setSQL("");
				this.setExp("");
				break;
			case SpecSubFlow:
				this.setRadioCheck3("checked='checked'");
				this.setRadioCheck1("");
				this.setRadioCheck2("");
				this.setRadioCheck4("");
				this.setRadioCheck5("");
				this.setSpecSubFlow(nd.getBlockExp());
				this.setSQL("");
				this.setExp("");
				break;
			case BySQL:
				this.setRadioCheck4("checked='checked'");
				this.setRadioCheck1("");
				this.setRadioCheck2("");
				this.setRadioCheck3("");
				this.setRadioCheck5("");
				this.setSpecSubFlow("");
				this.setSQL(nd.getBlockExp());
				this.setExp("");
				break;
			case ByExp:
				this.setRadioCheck5("checked='checked'");
				this.setRadioCheck1("");
				this.setRadioCheck2("");
				this.setRadioCheck3("");
				this.setRadioCheck4("");
				this.setSpecSubFlow("");
				this.setSQL("");
				this.setExp(nd.getBlockExp());
				break;
			 default:
				 break;
			}
	}
	
//*****************************************get()/set()********************************************************//

    
	

	public String getRadioCheck1() {
		return radioCheck1;
	}

	public void setRadioCheck1(String radioCheck1) {
		this.radioCheck1 = radioCheck1;
	}

	public String getRadioCheck2() {
		return radioCheck2;
	}

	public void setRadioCheck2(String radioCheck2) {
		this.radioCheck2 = radioCheck2;
	}

	public String getRadioCheck3() {
		return radioCheck3;
	}

	public void setRadioCheck3(String radioCheck3) {
		this.radioCheck3 = radioCheck3;
	}

	public String getRadioCheck4() {
		return radioCheck4;
	}

	public void setRadioCheck4(String radioCheck4) {
		this.radioCheck4 = radioCheck4;
	}

	public String getRadioCheck5() {
		return radioCheck5;
	}

	public void setRadioCheck5(String radioCheck5) {
		this.radioCheck5 = radioCheck5;
	}
	public String getSpecSubFlow() {
		return specSubFlow;
	}
	public void setSpecSubFlow(String specSubFlow) {
		this.specSubFlow = specSubFlow;
	}

	public String getSQL() {
		return SQL;
	}

	public void setSQL(String sQL) {
		SQL = sQL;
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}
	
	
	
	
}
