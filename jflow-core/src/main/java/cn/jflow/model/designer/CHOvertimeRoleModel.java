package cn.jflow.model.designer;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.WF.Node;
import BP.WF.Nodes;
import cn.jflow.system.ui.UiFatory;

public class CHOvertimeRoleModel {
	
		public HttpServletRequest request;
		public HttpServletResponse response;
		public UiFatory ui = null;
		public String basePath = "";
		
		public CHOvertimeRoleModel (HttpServletRequest request,HttpServletResponse response,String basePath){
			this.request=request;
			this.response=response;
			this.basePath=basePath;
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
		public String radioCheck6;
		public String radioCheck7;
		public List listId  = new ArrayList();;
		public List listName= new ArrayList();
		/**
	     * @Description: 节点属性超市处理规则页面初始化
		 * @Title: Page_Load
	     * @author peixiaofeng
	     * @date 2016年5月11日
		 */
		public void Page_Load() {
			Node nd = new Node();
			nd.setNodeID(this.getFK_Node());
			nd.RetrieveFromDBSources();
			Nodes nds = new Nodes(nd.getFK_Flow());
			CHOvertimeRoleModel cho = new CHOvertimeRoleModel(request, response, AppointNode);		
			for(int i = 0;i<nds.Tolist().size();i++){
				
				listId.add(nds.get(i).getRow().get("nodeid").toString());
				listName.add("\""+nds.get(i).getRow().get("name").toString()+"\"");		
			
			}
		
			//this.setIsEval();
			switch (nd.getHisOutTimeDeal()) {
				case None:
					this.setRadioCheck1("checked='checked'");
					this.setRadioCheck2("");
					this.setRadioCheck3("");
					this.setRadioCheck4("");
					this.setRadioCheck5("");
					this.setRadioCheck6("");
					this.setRadioCheck7("");
					this.setAppointNode("");
					this.setTransfer("");
					this.setSQL("");
					this.setEmp("");
					this.setIsEval("");			
					break;
				case AutoTurntoNextStep:
					this.setRadioCheck1("");
					this.setRadioCheck2("checked='checked'");
					this.setRadioCheck3("");
					this.setRadioCheck4("");
					this.setRadioCheck5("");
					this.setRadioCheck6("");
					this.setRadioCheck7("");
					this.setAppointNode("");
					this.setTransfer("");
					this.setSQL("");
					this.setEmp("");
					this.setIsEval("");	
					break;
				case AutoJumpToSpecNode:
					this.setRadioCheck1("");
					this.setRadioCheck2("");
					this.setRadioCheck3("checked='checked'");
					this.setRadioCheck4("");
					this.setRadioCheck5("");
					this.setRadioCheck6("");
					this.setRadioCheck7("");
					this.setAppointNode(nd.getDoOutTime());
					this.setTransfer("");
					this.setSQL("");
					this.setEmp("");
					this.setIsEval("");	
					break;
				case AutoShiftToSpecUser:
					this.setRadioCheck1("");
					this.setRadioCheck2("");
					this.setRadioCheck3("");
					this.setRadioCheck4("checked='checked'");
					this.setRadioCheck5("");
					this.setRadioCheck6("");
					this.setRadioCheck7("");
					this.setAppointNode("");
					this.setTransfer(nd.getDoOutTime());
					this.setSQL("");
					this.setEmp("");
					this.setIsEval("");	
					break;
				case SendMsgToSpecUser:
					this.setRadioCheck1("");
					this.setRadioCheck2("");
					this.setRadioCheck3("");
					this.setRadioCheck4("");
					this.setRadioCheck5("checked='checked'");
					this.setRadioCheck6("");
					this.setRadioCheck7("");
					this.setAppointNode("");
					this.setTransfer("");
					this.setSQL("");
					this.setEmp(nd.getDoOutTime());
					this.setIsEval("");	
					break;
				case DeleteFlow:
					this.setRadioCheck1("");
					this.setRadioCheck2("");
					this.setRadioCheck3("");
					this.setRadioCheck4("");
					this.setRadioCheck5("");
					this.setRadioCheck6("checked='checked'");
					this.setRadioCheck7("");
					this.setAppointNode("");
					this.setTransfer("");
					this.setSQL("");
					this.setEmp("");
					this.setIsEval("");	
					break;
				case RunSQL:
					this.setRadioCheck1("");
					this.setRadioCheck2("");
					this.setRadioCheck3("");
					this.setRadioCheck4("");
					this.setRadioCheck5("");
					this.setRadioCheck6("");
					this.setRadioCheck7("checked='checked'");
					this.setAppointNode("");
					this.setTransfer("");
					this.setSQL(nd.getDoOutTime());
					this.setEmp("");
					this.setIsEval("");	
					break;
				 default:
					 break;
				}
		}
		private void setRadioCheck1(String string) {
			// TODO Auto-generated method stub
			
		}
		/**
		 * 不处理
		 */
		public String None;
		/**
		 *跳转到指定节点
		 */
		public String AppointNode;
		/**
		 *  移交给指定的人
		 */
		public String Transfer;
		/**
		 * 按照SQL阻塞
		 */
		public String SQL;
		/**
		 * 删除流程
		 */
		public String delete;
		/**
		 * 向指定的人员发信息,如果设置为空就向当前人发信息
		 */
		public String Emp;
		/**
		 * 其他选项
		 */
		public String IsEval;
		//*****************************************get()/set()********************************************************//

		public String getNone() {
			return None;
		}

		public void setNone(String none) {
			
			
			None = none;
		}
		
		public String getAppointNode() {
			return AppointNode;
		}


		public void setAppointNode(String appointNode) {
			AppointNode = appointNode;
		}

		public String getTransfer() {
			return Transfer;
		}

		public void setTransfer(String transfer) {
			Transfer = transfer;
		}

		public String getSQL() {
			return SQL;
		}

		public void setSQL(String sQL) {
			SQL = sQL;
		}

		public String getDelete() {
			return delete;
		}

		public void setDelete(String delete) {
			this.delete = delete;
		}

		public String getEmp() {
			return Emp;
		}

		public void setEmp(String emp) {
			Emp = emp;
		}

		public String getIsEval() {
			return IsEval;
		}

		public void setIsEval(String isEval) {
			IsEval = isEval;
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

		public String getRadioCheck6() {
			return radioCheck6;
		}

		public void setRadioCheck6(String radioCheck6) {
			this.radioCheck6 = radioCheck6;
		}

		public String getRadioCheck7() {
			return radioCheck7;
		}

		public void setRadioCheck7(String radioCheck7) {
			this.radioCheck7 = radioCheck7;
		}

		public String getRadioCheck1() {
			return radioCheck1;
		}

			
}
