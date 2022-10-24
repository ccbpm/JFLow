package bp.wf.httphandler;

import bp.da.*;
import bp.difference.handler.WebContralBase;
import bp.web.*;
import bp.wf.template.*;
import bp.*;
import bp.wf.*;

import java.io.UnsupportedEncodingException;

/** 
 页面功能实体
*/
public class WF_Admin_CCBPMDesigner_FlowDevModel extends WebContralBase
{

		///#region 变量.
	/** 
	 类别
	*/
	public final String getSortNo() throws UnsupportedEncodingException {
		return this.GetRequestVal("SortNo");
	}
	/** 
	 流程名称
	*/
	public final String getFlowName() throws UnsupportedEncodingException {
		return this.GetRequestVal("FlowName");
	}
	public final FlowDevModel getFlowDevModel() throws UnsupportedEncodingException {
		return FlowDevModel.forValue(this.GetRequestValInt("FlowDevModel"));
	}

		///#endregion 变量.

	/** 
	 构造函数
	*/
	public WF_Admin_CCBPMDesigner_FlowDevModel()  {
	}
	/** 
	 获取默认的开发模式.
	 
	 @return 
	*/
	public final String Default_Init()  {
		String sql = "SELECT val FROM Sys_GloVar WHERE No='FlowDevModel_" + WebUser.getNo() + "'";
		int val = DBAccess.RunSQLReturnValInt(sql, 1);
		return String.valueOf(val);
	}
	public final String FlowDevModel_Save() throws Exception {
		String SortNo = GetRequestVal("SortNo");
		String FlowName = GetRequestVal("FlowName");
		String url = GetRequestVal("Url");

		String FrmUrl = GetRequestVal("FrmUrl");
		String FrmID = GetRequestVal("FrmID");
		//执行创建流程模版.
		String flowNo = TemplateGlo.NewFlow(SortNo, FlowName, DataStoreModel.ByCCFlow, null, null);
		Flow fl = new Flow(flowNo);
		fl.setFlowDevModel(this.getFlowDevModel()); //流程开发模式.
		fl.setHisDataStoreModel(DataStoreModel.SpecTable);
		fl.setFrmUrl(FrmUrl);
		if (DataType.IsNullOrEmpty(FrmID) == true || FrmID.equals("undefined"))
		{
			fl.setFrmUrl(FrmUrl);
		}
		else
		{
			fl.setFrmUrl(FrmID);
		}
		fl.Update();

		//设置极简类型的表单信息.
		if (this.getFlowDevModel() == FlowDevModel.JiJian)
		{
			Nodes nds = new Nodes();
			nds.Retrieve(NodeAttr.FK_Flow, fl.getNo(), null);
			for (Node nd : nds.ToJavaList())
			{
				nd.setNodeFrmID("ND" + Integer.parseInt(fl.getNo()) + "01");
				if (nd.isStartNode() == false)
				{
					nd.setFrmWorkCheckSta(FrmWorkCheckSta.Enable);

					FrmNode fn = new FrmNode();
					fn.setFKFrm(nd.getNodeFrmID());
					fn.setEnableFWC(FrmWorkCheckSta.Enable);
					fn.setFK_Node(nd.getNodeID());
					fn.setFK_Flow(flowNo);
					fn.setFrmSln(FrmSln.Readonly);
					fn.setMyPK(fn.getFKFrm() + "_" + fn.getFK_Node() + "_" + fn.getFK_Flow());
					//执行保存.
					fn.Save();
				}
				nd.DirectUpdate();
			}
		}

		//设置累加类型的表单信息.
		if (this.getFlowDevModel() == FlowDevModel.FoolTruck)
		{
			Nodes nds = new Nodes();
			nds.Retrieve(NodeAttr.FK_Flow, fl.getNo(), null);
			for (Node nd : nds.ToJavaList())
			{
			   //表单方案的保存
				FrmNode fn = new FrmNode();
				fn.setFKFrm(nd.getNodeFrmID());
				//fn.IsEnableFWC = FrmWorkCheckSta.Enable;
				fn.setFK_Node(nd.getNodeID());
				fn.setFK_Flow(flowNo);
				fn.setFrmSln(FrmSln.Readonly);
				fn.setMyPK(fn.getFKFrm() + "_" + fn.getFK_Node() + "_" + fn.getFK_Flow());
				//执行保存.
				fn.Save();
				nd.setHisFormType(NodeFormType.FoolTruck);
				nd.DirectUpdate();

			}
		}
		//设置绑定表单库的表单信息.
		if (this.getFlowDevModel() == FlowDevModel.RefOneFrmTree)
		{
			Nodes nds = new Nodes();
			nds.Retrieve(NodeAttr.FK_Flow, fl.getNo(), null);
			for (Node nd : nds.ToJavaList())
			{
				nd.setNodeFrmID(fl.getFrmUrl());
				if (nd.isStartNode() == true)
				{
					nd.setFrmWorkCheckSta(FrmWorkCheckSta.Disable);
				}
				else
				{
					nd.setFrmWorkCheckSta(FrmWorkCheckSta.Enable);
				}
				nd.setHisFormType(NodeFormType.RefOneFrmTree);
				nd.DirectUpdate();

				FrmNode fn = new FrmNode();
				fn.setFKFrm(nd.getNodeFrmID());
				if (nd.isStartNode() == true)
				{
					fn.setEnableFWC(FrmWorkCheckSta.Disable);
					fn.setFrmSln(FrmSln.Default);
				}

				else
				{
					fn.setEnableFWC(FrmWorkCheckSta.Enable);
					fn.setFrmSln(FrmSln.Readonly);
				}

				fn.setFK_Node(nd.getNodeID());
				fn.setFK_Flow(flowNo);
				fn.setFrmSln(FrmSln.Readonly);
				fn.setMyPK(fn.getFKFrm() + "_" + fn.getFK_Node() + "_" + fn.getFK_Flow());
				//执行保存.
				fn.Save();
			}
		}

		//绑定表单库的表单,现在绑定了一个表单
		if (this.getFlowDevModel() == FlowDevModel.FrmTree)
		{
			Nodes nds = new Nodes();
			nds.Retrieve(NodeAttr.FK_Flow, fl.getNo(), null);
			for (Node nd : nds.ToJavaList())
			{
				//nd.setNodeFrmID(fl.getFrmUrl());
				if (nd.isStartNode() == true)
					nd.setFrmWorkCheckSta(FrmWorkCheckSta.Disable);
				else
					nd.setFrmWorkCheckSta(FrmWorkCheckSta.Enable);
				nd.setHisFormType(NodeFormType.SheetTree);
				nd.DirectUpdate();
				FrmNode fn = new FrmNode();
				fn.setFKFrm(fl.getFrmUrl());
				if (nd.isStartNode() == true)
				{
					fn.setEnableFWC(FrmWorkCheckSta.Disable);
					fn.setFrmSln(FrmSln.Default);
				}

				else
				{
					fn.setEnableFWC(FrmWorkCheckSta.Enable);
					fn.setFrmSln(FrmSln.Readonly);
				}

				fn.setFK_Node(nd.getNodeID());
				fn.setFK_Flow(flowNo);
				fn.setFrmSln(FrmSln.Readonly);
				fn.setMyPK(fn.getFKFrm() + "_" + fn.getFK_Node() + "_" + fn.getFK_Flow());
				//执行保存.
				fn.Save();
			}
		}
		if (this.getFlowDevModel() == FlowDevModel.SDKFrm)
		{
			Nodes nds = new Nodes();
			nds.Retrieve(NodeAttr.FK_Flow, fl.getNo(), null);
			for (Node nd : nds.ToJavaList())
			{
				nd.setHisFormType(NodeFormType.SDKForm);
				nd.setFormUrl(fl.getFrmUrl());
				nd.DirectUpdate();
			}
		}
		if (this.getFlowDevModel() == FlowDevModel.SelfFrm)
		{
			Nodes nds = new Nodes();
			nds.Retrieve(NodeAttr.FK_Flow, fl.getNo(), null);
			for (Node nd : nds.ToJavaList())
			{
				nd.setHisFormType(NodeFormType.SDKForm);
				nd.setFormUrl(fl.getFrmUrl());
				nd.DirectUpdate();
			}

		}

		/**保存模式.
		*/
		SaveModel(this.getFlowDevModel());

		//返回流程编号
		return flowNo;
	}
	/** 
	 保存模式
	 
	 param val
	*/
	public final void SaveModel(FlowDevModel val)
	{
		String pk = "FlowDevModel_" + WebUser.getNo();

		String sql = "SELECT Val FROM Sys_GloVar WHERE No='" + pk + "'";
		int valInt = DBAccess.RunSQLReturnValInt(sql, 1);
		if (valInt == val.getValue())
		{
			return;
		}

		sql = "UPDATE Sys_GloVar SET Val=" + val.getValue() + " WHERE No='" + pk + "'";
		int myval = DBAccess.RunSQL(sql);
		if (myval == 1)
		{
			return;
		}

		sql = "INSERT INTO Sys_GloVar (No,Name,Val) VALUES('" + pk + "','FlowDevModel','" + val.getValue() + "')";
		DBAccess.RunSQL(sql);
	}
	/** 
	 创建流程-早期版本模式
	 
	 @return 
	*/
	public final String Default_NewFlowMode_0() throws Exception {
		try
		{
			int runModel = this.GetRequestValInt("RunModel");
			String FlowName = this.GetRequestVal("FlowName");
			String FlowSort = this.GetRequestVal("FlowSort").trim();
			FlowSort = FlowSort.trim();

			int DataStoreModel = this.GetRequestValInt("DataStoreModel");
			String PTable = this.GetRequestVal("PTable");
			String FlowMark = this.GetRequestVal("FlowMark");
			int FlowFrmModel = this.GetRequestValInt("FlowFrmModel");
			String FrmUrl = this.GetRequestVal("FrmUrl");
			String FlowVersion = this.GetRequestVal("FlowVersion");

			String flowNo = TemplateGlo.NewFlow(FlowSort, FlowName, bp.wf.template.DataStoreModel.SpecTable, PTable, FlowMark);

			Flow fl = new Flow(flowNo);


			//清空WF_Emp 的StartFlows ,让其重新计算.
			// DBAccess.RunSQL("UPDATE  WF_Emp Set StartFlows =''");
			return flowNo;
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}

}