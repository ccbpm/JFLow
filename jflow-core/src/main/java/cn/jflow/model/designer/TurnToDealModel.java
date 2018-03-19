package cn.jflow.model.designer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.WF.Node;
import cn.jflow.system.ui.UiFatory;


public class TurnToDealModel {
	public HttpServletRequest request;
	public HttpServletResponse response;
	public UiFatory ui = null;
	public String basePath = "";

	
	public TurnToDealModel (HttpServletRequest request,HttpServletResponse response){
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
	public String flowMsgCheck;
	/**
	 * 提示指定信息
	 */
	public String specMsgCheck;
	/**
	 * 提示指定信息提示信息
	 */
	public String specMsgText;
	/**
	 * 转向指定的URL单选框
	 */
	public String specUrlCheck;
	/**
	 * 转向指定的URL地址
	 */
	public String specUrlText;
	
	/**
     * @Description: 节点属性发送后转向处理规则页面初始化
	 * @Title: Page_Load
     * @author peixiaofeng
     * @date 2016年5月10日
	 */
	public void Page_Load() {
		Node nd = new Node();
		nd.setNodeID(this.getFK_Node());
		nd.RetrieveFromDBSources();
		switch (nd.getHisTurnToDeal()) {
			case CCFlowMsg:
				this.setFlowMsgCheck("checked='checked'");
				this.setSpecMsgCheck("");
				this.setSpecMsgText("");
				this.setSpecUrlText("");
				this.setSpecUrlCheck("");
				break;
			case SpecMsg:
				this.setSpecMsgCheck("checked='checked'");
				this.setSpecMsgText(nd.getTurnToDealDoc());
				this.setSpecUrlText("");
				this.setSpecUrlCheck("");
				break;
			case SpecUrl:
				this.setSpecUrlCheck("checked='checked'");
				this.setSpecUrlText(nd.getTurnToDealDoc());
				this.setSpecMsgCheck("");
				this.setSpecMsgText("");
				break;
			}
	}
	
	
	public enum TurnToDeal{
        /// <summary>
        /// 按系统默认的提示
        /// </summary>
        CCFlowMsg,
        /// <summary>
        /// 指定消息
        /// </summary>
        SpecMsg,
        /// <summary>
        /// 指定Url
        /// </summary>
        SpecUrl,
        /// <summary>
        /// 按条件转向
        /// </summary>
        TurnToByCond
    }
	//*****************************************get()/set()********************************************************//
	
	public String getFlowMsgCheck() {
		return flowMsgCheck;
	}

	public void setFlowMsgCheck(String flowMsgCheck) {
		this.flowMsgCheck = flowMsgCheck;
	}

	public String getSpecMsgCheck() {
		return specMsgCheck;
	}

	public void setSpecMsgCheck(String specMsgCheck) {
		this.specMsgCheck = specMsgCheck;
	}

	public String getSpecMsgText() {
		return specMsgText;
	}

	public void setSpecMsgText(String specMsgText) {
		this.specMsgText = specMsgText;
	}

	public String getSpecUrlCheck() {
		return specUrlCheck;
	}

	public void setSpecUrlCheck(String specUrlCheck) {
		this.specUrlCheck = specUrlCheck;
	}

	public String getSpecUrlText() {
		return specUrlText;
	}

	public void setSpecUrlText(String specUrlText) {
		this.specUrlText = specUrlText;
	}

}
