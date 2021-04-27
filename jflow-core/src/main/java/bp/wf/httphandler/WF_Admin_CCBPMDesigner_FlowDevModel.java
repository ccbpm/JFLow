package bp.wf.httphandler;

import bp.da.DBAccess;
import bp.difference.handler.WebContralBase;
import bp.web.WebUser;
import bp.wf.Flow;
import bp.wf.Node;
import bp.wf.NodeFormType;
import bp.wf.Nodes;
import bp.wf.template.DataStoreModel;
import bp.wf.template.FlowDevModel;
import bp.wf.template.FrmNode;
import bp.wf.template.FrmSln;
import bp.wf.template.FrmWorkCheckSta;
import bp.wf.template.NodeAttr;

/**
 页面功能实体
 */
public class WF_Admin_CCBPMDesigner_FlowDevModel extends WebContralBase
{
    /**
     类别
     */
    public final String getSortNo()
    {
        return this.GetRequestVal("SortNo");
    }
    /**
     流程名称
     */
    public final String getFlowName()
    {
        return this.GetRequestVal("FlowName");
    }
    public final FlowDevModel getFlowDevModel()
    {
        return FlowDevModel.forValue(this.GetRequestValInt("FlowDevModel"));
    }

    /**
     构造函数
     */
    public WF_Admin_CCBPMDesigner_FlowDevModel()
    {
    }
    /**
     获取默认的开发模式.

     @return
      * @throws Exception
     */
    public final String Default_Init() throws Exception
    {
        String sql = "SELECT val FROM Sys_GloVar WHERE No='FlowDevModel_" + WebUser.getNo() + "'";
        int val = DBAccess.RunSQLReturnValInt(sql, 1);
        return String.valueOf(val);
    }
    public final String FlowDevModel_Save() throws Exception
    {
        String SortNo = GetRequestVal("SortNo");
        String FlowName = GetRequestVal("FlowName");
        String url = GetRequestVal("Url");

        String FrmUrl = GetRequestVal("FrmUrl");
        String FrmID = GetRequestVal("FrmID");
        //执行创建流程模版.
        String flowNo = bp.wf.template.TemplateGlo.NewFlow(SortNo, FlowName, DataStoreModel.ByCCFlow, null, null);
        Flow fl = new Flow(flowNo);
        fl.setFlowDevModel(this.getFlowDevModel()); //流程开发模式.
        fl.setFrmUrl(url);
        if (FrmID.equals("") || FrmID.equals("undefined"))
        {
            fl.setFrmUrl(FrmUrl);
        }
        else
        {
            fl.setFrmUrl(FrmID);
        }
        fl.Update();

        //设置极简类型的表单信息.
        if (this.getFlowDevModel() == getFlowDevModel().JiJian)
        {
            Nodes nds = new Nodes();
            nds.Retrieve(NodeAttr.FK_Flow, fl.getNo());
            for (Node nd : nds.ToJavaList())
            {
                nd.setNodeFrmID("ND" + Integer.parseInt(fl.getNo()) + "01");
                if (nd.getIsStartNode() == false)
                {
                    nd.setFrmWorkCheckSta(FrmWorkCheckSta.Enable);

                    FrmNode fn = new FrmNode();
                    fn.setFK_Frm(nd.getNodeFrmID());
                    fn.setIsEnableFWC(FrmWorkCheckSta.Enable);
                    fn.setFK_Node(nd.getNodeID());
                    fn.setFK_Flow(flowNo);
                    fn.setFrmSln(FrmSln.Readonly);
                    fn.setMyPK(fn.getFK_Frm() + "_" + fn.getFK_Node() + "_" + fn.getFK_Flow());
                    //执行保存.
                    fn.Save();
                }
                nd.DirectUpdate();
            }
        }

        //设置累加类型的表单信息.
        if (this.getFlowDevModel() == getFlowDevModel().FoolTruck)
        {
            Nodes nds = new Nodes();
            nds.Retrieve(NodeAttr.FK_Flow, fl.getNo());
            for (Node nd : nds.ToJavaList())
            {
                //表单方案的保存
                FrmNode fn = new FrmNode();
                fn.setFK_Frm(nd.getNodeFrmID());
                //fn.IsEnableFWC = FrmWorkCheckSta.Enable;
                fn.setFK_Node(nd.getNodeID());
                fn.setFK_Flow(flowNo);
                fn.setFrmSln(FrmSln.Readonly);
                fn.setMyPK(fn.getFK_Frm() + "_" + fn.getFK_Node() + "_" + fn.getFK_Flow());
                //执行保存.
                fn.Save();
                nd.setHisFormType(NodeFormType.FoolTruck);
                nd.DirectUpdate();

            }
        }
        //设置绑定表单库的表单信息.
        if (this.getFlowDevModel() == getFlowDevModel().RefOneFrmTree)
        {
            Nodes nds = new Nodes();
            nds.Retrieve(NodeAttr.FK_Flow, fl.getNo());
            for (Node nd : nds.ToJavaList())
            {
                nd.setNodeFrmID(fl.getFrmUrl());
                if (nd.getIsStartNode() == true)
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
                fn.setFK_Frm(nd.getNodeFrmID());
                if (nd.getIsStartNode() == true)
                {
                    fn.setIsEnableFWC(FrmWorkCheckSta.Disable);
                }
                else
                {
                    fn.setIsEnableFWC(FrmWorkCheckSta.Enable);
                }
                fn.setFK_Node(nd.getNodeID());
                fn.setFK_Flow(flowNo);
                fn.setFrmSln(FrmSln.Readonly);
                fn.setMyPK(fn.getFK_Frm() + "_" + fn.getFK_Node() + "_" + fn.getFK_Flow());
                //执行保存.
                fn.Save();
            }
        }

        if (this.getFlowDevModel() == getFlowDevModel().SDKFrm)
        {
            Nodes nds = new Nodes();
            nds.Retrieve(NodeAttr.FK_Flow, fl.getNo());
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
            nds.Retrieve(NodeAttr.FK_Flow, fl.getNo());
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

     @param val
      * @throws Exception
     */
    public void SaveModel(FlowDevModel val) throws Exception
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
      * @throws Exception
     */
    public String Default_NewFlowMode_0() throws Exception
    {
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

            String flowNo = bp.wf.template.TemplateGlo.NewFlow(FlowSort, FlowName, bp.wf.template.DataStoreModel.SpecTable, PTable, FlowMark);

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
