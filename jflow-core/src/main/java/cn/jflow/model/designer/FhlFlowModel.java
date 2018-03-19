package cn.jflow.model.designer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.WF.GenerWorkFlow;
import BP.WF.NodeFormType;
import BP.WF.WorkFlow;
import BP.WF.WorkNode;
import cn.jflow.common.model.EnModel;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.uc.ToolBar1;

public class FhlFlowModel extends EnModel{

	public ToolBar1 toolBar1 = null;
	
	public StringBuffer pub1 = new StringBuffer();
	
	public EnModel enModel = null ;
	
	public FhlFlowModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		toolBar1 = new ToolBar1(request, response);
		enModel = new EnModel(request, response);
	}
	
	
	
	
	public void pageLoad()
	{
		// 退回流程.
		String ReturnWorkUrl = BP.WF.Glo.getCCFlowAppPath() + "WF/WorkOpt/ReturnWork.jsp?WorkID=" + this.getWorkID() + "&FID=" + this.getFID() + "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + this.getFK_Node()+"&isThread=isThread";
		toolBar1.Add("<input id =\"Btn_Return\" name =\"Btn_Return\" class=Btn type=button onclick=\"threadReturnWork('"+ReturnWorkUrl +"')\""+" value='退回' >");
		
		// 关闭.
		toolBar1.Add("<input type=button name=\"关闭\" id=\"Btn_Close\" value =\"关闭\" onclick=\"onCloseWindow()\"");

		GenerWorkFlow gwf = new GenerWorkFlow(this.getFID());
		WorkFlow wf = new WorkFlow(this.getFK_Flow(), this.getFID());
		WorkNode wn = new WorkNode(this.getFID(), gwf.getFK_Node());

		WorkNode wnPri = wn.GetPreviousWorkNode_FHL(this.getWorkID()); // 他的上一个节点.
		BP.WF.Node ndPri = wnPri.getHisNode();
		try
		{
			//根据不同的表单类型展示不同的表单.
			if (ndPri.getHisFormType() == NodeFormType.FoolForm)
			{
				enModel.BindColumn4(wnPri.getHisWork(), "ND" + wnPri.getHisNode().getNodeID());
				enModel.Pub.append(Add(wnPri.getHisWork().getWorkEndInfo()));
			}
			else if (ndPri.getHisFormType() == NodeFormType.FreeForm)
			{
				enModel.BindCCForm(wnPri.getHisWork(), "ND" + wnPri.getHisNode().getNodeID(), true, 0,false);
			}
			else if (ndPri.getHisFormType() == NodeFormType.SDKForm)
			{
				String url = "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + ndPri.getNodeID() + "&WorkID=" + this.getWorkID() + "&FID=" + this.getFID();

				String src = ndPri.getFormUrl();
				if (src.contains("?"))
				{
					src = src + "&IsReadonly=1&FK" + url;
				}
				else
				{
					src = src + "?1=2&IsReadonly=1" + url;
				}
				enModel.Pub.append(Add("<iframe ID='Ff' src='" + src + "' frameborder=0  style='width:100%; height:900px;text-align: left;'  leftMargin='0'  topMargin='0' scrolling=auto />"));
				enModel.Pub.append(Add("</iframe>"));
			}
		}
		catch (java.lang.Exception e)
		{
			//enModel.Pub.WinCloseWithMsg("此工作已经终止或者被删除。");
			enModel.Alert("此工作已经终止或者被删除。");
		}
	}
	
	public final Button getBtn_Return()
	{
		return new Button("Btn_Return");
	}
	public final Button getBtn_Del()
	{
		return new Button("Btn_Del");
	}
	public final Button getBtn_Close()
	{
		return new Button("Btn_Close");
	}

}
