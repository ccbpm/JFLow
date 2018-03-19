package cn.jflow.model.wf.rpt;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.Tools.StringHelper;
import BP.WF.Work;
import BP.WF.WorkNode;
import BP.WF.Template.FrmWorkCheckSta;
import BP.Web.WebUser;
import cn.jflow.common.model.BaseModel;
public class ReturnWorkModel extends BaseModel {

	public ReturnWorkModel(HttpServletRequest request,HttpServletResponse response) {
		super(request, response);
	}
	

	public final String getInfo()
	{
		String str = get_request().getParameter("Info");
		if (StringHelper.isNullOrEmpty(str))
		{
			return null;
		}
		else
		{
			return str;
		}
	}

	
	public String TB_doc;
	public List list;
	public String cb;
	public String errMsg;
	
	
	
	

	public void page_load(){
		
		list=new ArrayList();
		//#region 找到可以退回的节点.
		DataTable dt = null;
		dt = BP.WF.Dev2Interface.DB_GenerWillReturnNodes(this.getFK_Node(),
				this.getWorkID(), this.getFID());
		if (dt.Rows.size() == 0) {
			this.setErrMsg("退回错误,系统没有找到可以退回的节点.");
			return;
		}
		BP.WF.Node nd = new BP.WF.Node(this.getFK_Node());
		
		if (nd.getIsBackTracking()) {
			this.setCb("true");
		}
		WorkNode wn = new WorkNode(this.getWorkID(), this.getFK_Node());
		WorkNode pwn = wn.GetPreviousWorkNode();
		for (DataRow dr : dt.Rows) {
			/*   该代码不能退回上一个节点，与功能不符，暂时注释
			if(dr.get("no").equals(pwn.getHisNode().getNodeID())){
				continue;
			}
			*/
			list.add(dr);
		}
		
		Work wk = pwn.getHisWork();
		if (this.getInfo() != null) {
			this.setTB_doc(this.getInfo());
		} else {
			// 检查是否启用了审核组件，看看审核信息里填写了什么？
			BP.WF.Template.FrmWorkCheck fwc = new BP.WF.Template.FrmWorkCheck(this.getFK_Node());
			if (fwc.getHisFrmWorkCheckSta() == FrmWorkCheckSta.Enable) {
				/*this.getTB1().setText(
						BP.WF.Dev2Interface.GetCheckInfo(this.getFK_Flow(),
								this.getWorkID(), this.getFK_Node()));*/
				this.setTB_doc(BP.WF.Dev2Interface.GetCheckInfo(this.getFK_Flow(),
						this.getWorkID(), this.getFK_Node()));
				
				if ("同意".equals(this.getTB_doc())) {
					this.setTB_doc("");
				}
			} else {
				//String info = this.getDDL1().getSelectedItem().getText();
				//String recName = info.substring(0, info.indexOf('='));
				//String nodeName = info.substring(info.indexOf('>') + 1);
				if(list!=null && list.size()>0){
					DataRow dr =(DataRow) list.get(0);
					String recName=String.valueOf(dr.getValue("recname"));
					String nodeName=String.valueOf(dr.getValue("name"));
					
					this.setTB_doc(
							String.format(
									"%1$s同志: \n  您处理的“%2$s”工作有错误，需要您重新办理．\n\n此致!!!   \n\n  %3$s",
									recName,
									nodeName,
									WebUser.getName()
											+ "\n  "
											+ DataType.getCurrentDataTime()));
				}
				
			
			}
		}
		
		}

	public String getTB_doc() {
		return TB_doc;
	}

	public void setTB_doc(String tB_doc) {
		TB_doc = tB_doc;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public String getCb() {
		return cb;
	}

	public void setCb(String cb) {
		this.cb = cb;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

}
