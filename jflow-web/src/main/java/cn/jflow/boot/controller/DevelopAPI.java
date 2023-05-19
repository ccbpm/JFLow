package cn.jflow.boot.controller;

import bp.da.*;
import bp.difference.ContextHolderUtils;
import bp.difference.handler.HttpHandlerBase;
import bp.en.QueryObject;
import bp.web.WebUser;
import bp.wf.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Hashtable;

@RestController
@RequestMapping(value = "/DataUser/DevelopAPI")
public class DevelopAPI extends HttpHandlerBase {
    public String SID = "";
    @PostMapping(value = "/ProcessRequest")
    public final String ProcessRequest(HttpServletRequest request) throws Exception
    {
        String doType = null;
        String domain = "";
        try
        {
            domain =  request.getParameter("DoMain");
            //设置通用的变量.
            doType = request.getParameter("DoType");
            if (bp.da.DataType.IsNullOrEmpty(doType) == true)
            {
                doType = request.getParameter("DoWhat");
            }

            //如果是请求登录. @hongyan.
            if (doType.equals("Portal_Login_Submit") == true)
            {
                String key = request.getParameter("PrivateKey");
                String userNo = request.getParameter("UserNo");

                String localKey = bp.difference.SystemConfig.GetValByKey("PrivateKey", "DiGuaDiGua,IamCCBPM");
                if (localKey.equals(key) == false)
                    return "err@私约错误，请检查全局文件中配置 PrivateKey ";

                bp.wf.Dev2Interface.Port_Login(userNo);
                String toke = bp.wf.Dev2Interface.Port_GenerToken(userNo);

                Hashtable ht = new Hashtable();
                ht.put("No", WebUser.getNo());
                ht.put("Name", WebUser.getName());
                ht.put("FK_Dept", WebUser.getFK_Dept());
                ht.put("FK_DeptName", WebUser.getFK_DeptName());
                ht.put("OrgNo", WebUser.getOrgNo());
                ht.put("OrgName", WebUser.getOrgName());
                ht.put("Token", toke);
                return bp.tools.Json.ToJson(ht);
            }
            String  token =  request.getParameter("Token");
            if (bp.da.DataType.IsNullOrEmpty(doType) == true || bp.da.DataType.IsNullOrEmpty(token) == true)
               return "err@参数Token,DoWhat不能为空.";
            //执行登录.
            if(DataType.IsNullOrEmpty(token))
                bp.wf.Dev2Interface.Port_LoginByToken(token);
            this.SID = token; //记录下来他的sid.
        }
        catch (RuntimeException ex)
        {
            return "err@" + ex.getMessage();
        }
        ///#endregion 效验问题.

        ///#region  与流程处理相关的接口API.
        if (doType.equals("Node_CreateBlankWorkID") == true)
        {
            //创建workid.
            long workid = bp.wf.Dev2Interface.Node_CreateBlankWork(this.getFK_Flow(), bp.web.WebUser.getNo());
            return String.valueOf(workid);
        }

        if (doType.equals("Node_SetDraft") == true)
        {
            bp.wf.Dev2Interface.Node_SetDraft(this.getFK_Flow(), this.getWorkID());
            return "设置成功";
        }

        if (doType.equals("Node_Shift") == true)
        {
            String toEmpNo = request.getParameter("ToEmpNo");
            String msg = request.getParameter("Msg");
            return bp.wf.Dev2Interface.Node_Shift(this.getWorkID(), toEmpNo, msg);
        }

        // 给人员增加待办.
        if (doType.equals("Node_AddTodolist") == true)
        {
            String EmpNo = request.getParameter("EmpNo");
            bp.wf.Dev2Interface.Node_AddTodolist(this.getWorkID(), EmpNo);
            return "增加人员成功.";
        }

        //获得流程信息.
        if (doType.equals("Flow_GenerWorkFlow") == true)
        {
            GenerWorkFlow gwf = new GenerWorkFlow(this.getWorkID());
            return gwf.ToJson();
        }


        //保存参数字段.
        if (doType.equals("Node_SaveParas") == true)
        {
            bp.wf.Dev2Interface.Flow_SaveParas(this.getWorkID(), request.getParameter("Paras"));
            return "参数保存成功";
        }
        //保存参数字段.
        if (doType.equals("Flow_SetTitle") == true)
        {
            bp.wf.Dev2Interface.Flow_SetFlowTitle(this.getFK_Flow(), this.getWorkID(), request.getParameter("Title"));
            return "标题设置成功.";
        }

        if (doType.equals("Node_SendWork") == true)
        {
            //执行发送.
            java.util.Hashtable ht = new java.util.Hashtable();
            Enumeration enu = ContextHolderUtils.getRequest().getParameterNames();
            while (enu.hasMoreElements()) {
                String str = (String) enu.nextElement();
                if (DataType.IsNullOrEmpty(str) == true) {
                    continue;
                }
                String val = this.GetValByKey(str);
                if (val != null)
                    ht.put(str, val);
            }

            int toNodeID = this.GetValIntByKey("ToNodeID");
            String toEmps = this.GetValByKey("ToEmps");
            try
            {
                String flowNo = this.getFK_Flow();
                if (DataType.IsNullOrEmpty(flowNo) == true)
                    flowNo = DBAccess.RunSQLReturnString("SELECT FK_Flow FROM WF_GenerWorkFlow WHERE WorkID=" + this.getWorkID());

                //执行发送.
                SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(flowNo, this.getWorkID(), ht, null, toNodeID, toEmps);
                return objs.ToMsgOfText();
            }
            catch (RuntimeException ex)
            {
                return "err@" + ex.getMessage();
            }
        }

        if (doType.equals("DB_GenerWillReturnNodes") == true)
        {
            //获得可以退回的节点.
            DataTable dt = bp.wf.Dev2Interface.DB_GenerWillReturnNodes(this.getFK_Node(), this.getWorkID(), this.getFID());
            return bp.tools.Json.ToJson(dt);
        }
        if (doType.equals("Node_ReturnWork") == true)
        {
            //执行退回.
            int toNodeID = this.GetValIntByKey("ReturnToNodeID");
            String returnToEmp = this.GetValByKey("ReturnToEmp");
            if (toNodeID == 0)
            {
                DataTable dt = bp.wf.Dev2Interface.DB_GenerWillReturnNodes(this.getFK_Node(), this.getWorkID(), this.getFID());
                if (dt.Rows.size() == 1)
                {
                    toNodeID = Integer.parseInt(dt.Rows.get(0).getValue("No").toString());
                    returnToEmp = dt.Rows.get(0).getValue("Rec").toString();

                }
            }
            return  bp.wf.Dev2Interface.Node_ReturnWork(this.getWorkID(),  toNodeID, returnToEmp, this.GetValByKey("Msg"), this.GetValBoolenByKey("IsBackToThisNode"));
        }
        ///#endregion 与流程处理相关的接口API.


        ///#region 处理相关功能.
        try
        {
            if (doType.equals("Search_Init")){
                return Search_Init();
            }
            if (doType.equals("DB_Start")) //获得发起列表.
            {
                DataTable dtStrat = bp.wf.Dev2Interface.DB_StarFlows(bp.web.WebUser.getNo(),domain);
                return bp.tools.Json.ToJson(dtStrat);
            }
            else if (doType.equals("DB_Draft")) //草稿.
            {
                DataTable dtDraft = bp.wf.Dev2Interface.DB_GenerDraftDataTable(null,domain);
                return bp.tools.Json.ToJson(dtDraft);
            }
            else if (doType.equals("GenerFrmUrl")) //获得发起的URL.
            {
                return GenerFrmUrl();
            }
            else if (doType.equals("DB_Todolist")) //获得待办.
            {
                DataTable dtTodolist = bp.wf.Dev2Interface.DB_Todolist(bp.web.WebUser.getNo(), 0, null, domain);
                return bp.tools.Json.ToJson(dtTodolist);
            }
            else if (doType.equals("DB_Runing")) //获得未完成(在途).
            {
                DataTable dtRuing = bp.wf.Dev2Interface.DB_GenerRuning(bp.web.WebUser.getNo(), false, domain);
                return bp.tools.Json.ToJson(dtRuing);
            }
            else if(doType.equals("DB_CCList"))//获取抄送数据
            {
                DataTable dtCC = bp.wf.Dev2Interface.DB_CCList("");
                return bp.tools.Json.ToJson(dtCC);
            }
            else if(doType.equals("DB_CCListBySta"))//获取抄送数据已读或者未读的抄送数据  0 未读 1已读
            {
                DataTable dtCC = bp.wf.Dev2Interface.DB_CCList(this.getSta());
                return bp.tools.Json.ToJson(dtCC);
            }
            else if(doType.equals("Node_CC_WriteTo_CClist"))//手动抄送的接口
            {
                //抄送给指定的人 格式：zhangsan,张三;lisi,李四；，部门 格式：1001;10002。岗位  格式 1001;1002
                String title = this.GetValByKey("Title");
                if(DataType.IsNullOrEmpty(title)==true)
                    title="来自"+bp.web.WebUser.getNo()+"的抄送信息.";
                String doc = this.GetValByKey("Doc");
                if(DataType.IsNullOrEmpty(doc)==true)
                    doc="来自"+bp.web.WebUser.getNo()+"的抄送信息.";

                String ccRec = bp.wf.Dev2Interface.Node_CC_WriteTo_CClist(this.getFK_Node(), this.getWorkID(), title, doc,  this.GetValByKey("Emps"),
                        this.GetValByKey("Depts") ,this.GetValByKey("Stations") , null);
                return ccRec;
            }
            else if (doType.equals("Flow_DoPress")) //批量催办.
            {
                return this.Flow_DoPress();
            }
            else if (doType.equals("CC_BatchCheckOver")) //批量抄送审核.
            {
                return this.CC_BatchCheckOver();
            }
            else if (doType.equals("Flow_BatchDeleteByFlag")) //批量删除.
            {
                return this.Flow_BatchDeleteByFlag();
            }
            else if (doType.equals("Flow_BatchDeleteByReal")) //批量删除.
            {
                return this.Flow_BatchDeleteByReal();
            }
            else if (doType.equals("Flow_BatchDeleteByFlagAndUnDone")) //恢复批量删除.
            {
                return this.Flow_BatchDeleteByFlagAndUnDone();
            }
            else if (doType.equals("Flow_DoUnSend")) //撤销发送..
            {
                return this.Flow_DoUnSend();
            }
            else if (doType.equals("Flow_DeleteDraft")) //删除草稿箱..
            {
                return this.Flow_DeleteDraft();
            }
            else if (doType.equals("Flow_DoFlowOver")) //批量结束.
            {
                return this.Flow_DoFlowOver();
            }
            else if(doType.equals("Portal_LoginOut"))
            {
                bp.wf.Dev2Interface.Port_SigOut();
            }else if(doType.equals("InfoSorts")){
                String sql = "SELECT * FROM OA_InfoType ";
                DataTable dt = DBAccess.RunSQLReturnTable(sql);
                return bp.tools.Json.ToJson(dt);

            }else if(doType.equals("InfoDtls")){
                String sortNo = this.GetValByKey("SortNo");
                String sql="";
                if (DataType.IsNullOrEmpty(sortNo) == true)
                    sql = "SELECT * FROM OA_Info WHERE InfoPRI=1 ";
                else
                    sql = "SELECT * FROM OA_Info WHERE InfoType='" + sortNo + "' ";

                DataTable dt = DBAccess.RunSQLReturnTable(sql);
                return bp.tools.Json.ToJson(dt);

            }else if(doType.equals("Dtl")){
                String dtlNo = this.GetValByKey("No");
                String sql = "SELECT * FROM OA_Info WHERE No='" + dtlNo + "' ";
                DataTable dt = DBAccess.RunSQLReturnTable(sql);
                dt.TableName = "OA_InfoDtl";
                return bp.tools.Json.ToJson(dt);
            }
            return "err@没有判断的执行类型:" + doType;
        }
        catch (RuntimeException ex)
        {
            return "err@" + ex.getMessage();
        }
        ///#endregion 处理相关功能.
    }
    public String Search_Init() throws Exception {
        GenerWorkFlows gwfs = new GenerWorkFlows();

        String scop = this.GetValByKey("Scop");//范围.
        String key = this.GetValByKey("Key");//关键字.
        String dtFrom = this.GetValByKey("DTFrom");//日期从.
        String dtTo = this.GetValByKey("DTTo");//日期到.
        int pageIdx = Integer.parseInt(this.GetValByKey("PageIdx"));//分页.

        //创建查询对象.
        QueryObject qo = new QueryObject(gwfs);
        if (DataType.IsNullOrEmpty(key) == false)
        {
            qo.AddWhere(GenerWorkFlowAttr.Title, " LIKE ", "%" + key + "%");
            qo.addAnd();
        }

        //我参与的.
        if (scop.equals("0") == true)
            qo.AddWhere(GenerWorkFlowAttr.Emps, "LIKE", "%@" + WebUser.getNo() + ",%");

        //我发起的.
        if (scop.equals("1") == true)
            qo.AddWhere(GenerWorkFlowAttr.Starter, "=", WebUser.getNo());

        //我部门发起的.
        if (scop.equals("2") == true)
            qo.AddWhere(GenerWorkFlowAttr.FK_Dept, "=", WebUser.getFK_Dept());


        //任何一个为空.
        if (DataType.IsNullOrEmpty(dtFrom) == true || DataType.IsNullOrEmpty(dtTo) == true)
        {

        }
        else
        {
            qo.addAnd();
            qo.AddWhere(GenerWorkFlowAttr.RDT, ">=", dtFrom);
            qo.addAnd();
            qo.AddWhere(GenerWorkFlowAttr.RDT, "<=", dtTo);
        }

        int count = qo.GetCount(); //获得总数.

        qo.DoQuery("WorkID", 20, pageIdx);
        //   qo.DoQuery(); // "WorkID", 20, pageIdx);


        DataTable dt = gwfs.ToDataTableField("gwls");

        //创建容器.
        DataSet ds = new DataSet();
        ds.Tables.add(dt); //增加查询对象.

        //增加数量.
        DataTable mydt = new DataTable();
        mydt.TableName = "count";
        mydt.Columns.Add("CC");
        DataRow dr = mydt.NewRow();
        dr.setValue(0,String.valueOf(count)); //把数量加进去.
        mydt.Rows.add(dr);
        ds.Tables.add(mydt);
        return bp.tools.Json.ToJson(ds);
    }
    ///#region 通用方法.
    public final String GetValByKey(String key)
    {
        String str = ContextHolderUtils.getRequest().getParameter(key);
        if (bp.da.DataType.IsNullOrEmpty(str))
        {
            return null;
        }
        return str;
    }
    public final int GetValIntByKey(String key)
    {
        String val = GetValByKey(key);
        if (val == null)
        {
            return 0;
        }
        return Integer.parseInt(val);
    }
    public final boolean GetValBoolenByKey(String key)
    {
        String val = GetValByKey(key);
        if (val == null)
        {
            return false;
        }
        if (val.equals("0") == true)
        {
            return false;
        }
        return true;
    }


    public final String Flow_DoFlowOver()throws Exception
    {
        String workids = this.GetValByKey("WorkIDs");
        String[] strs = workids.split("[,]", -1);

        String info = "";
        for (String workidStr : strs)
        {
            if (bp.da.DataType.IsNullOrEmpty(workidStr) == true)
            {
                continue;
            }

            bp.wf.Dev2Interface.Flow_DoFlowOver(Long.parseLong(workidStr), "批量结束", 1);
        }
        return "执行成功.";
    }
    /**
     删除草稿

     */
    public final String Flow_DeleteDraft()throws Exception
    {
        String workids = this.GetValByKey("WorkIDs");
        String[] strs = workids.split("[,]", -1);

        String info = "";
        for (String workidStr : strs)
        {
            if (bp.da.DataType.IsNullOrEmpty(workidStr) == true)
            {
                continue;
            }

            bp.wf.Dev2Interface.Node_DeleteDraft(Long.parseLong(workidStr));
        }
        return "删除成功.";
    }

    /**
     撤销发送

     */
    public final String Flow_DoUnSend()throws Exception
    {
        String workids = this.GetValByKey("WorkIDs");
        String[] strs = workids.split("[,]", -1);

        String info = "";
        for (String workidStr : strs)
        {
            if (bp.da.DataType.IsNullOrEmpty(workidStr) == true)
            {
                continue;
            }

            info += bp.wf.Dev2Interface.Flow_DoUnSend(null, Long.parseLong(workidStr), 0, 0);
        }
        return info;
    }
    public final String Flow_BatchDeleteByReal() throws Exception
    {
        String workids = this.GetValByKey("WorkIDs");
        String[] strs = workids.split("[,]", -1);

        for (String workidStr : strs)
        {
            if (bp.da.DataType.IsNullOrEmpty(workidStr) == true)
            {
                continue;
            }

            String st1r = bp.wf.Dev2Interface.Flow_DoDeleteFlowByReal(Long.parseLong(workidStr), true);
        }
        return  "删除成功.";
    }
    /**
     删除功能

     */
    public final String Flow_BatchDeleteByFlag()throws Exception
    {
        String workids = this.GetValByKey("WorkIDs");
        String[] strs = workids.split("[,]", -1);

        for (String workidStr : strs)
        {
            if (bp.da.DataType.IsNullOrEmpty(workidStr) == true)
            {
                continue;
            }

            String st1r = bp.wf.Dev2Interface.Flow_DoDeleteFlowByFlag(Long.parseLong(workidStr), "删除", true);
        }

        return "删除成功.";
    }
    /**
     恢复删除

     */
    public final String Flow_BatchDeleteByFlagAndUnDone() throws Exception
    {
        String workids = this.GetValByKey("WorkIDs");
        String[] strs = workids.split("[,]", -1);

        for (String workidStr : strs)
        {
            if (bp.da.DataType.IsNullOrEmpty(workidStr) == true)
            {
                continue;
            }

            String st1r = bp.wf.Dev2Interface.Flow_DoUnDeleteFlowByFlag(null, Integer.parseInt(workidStr), "删除");
        }

        return "恢复成功.";
    }
    public final String Flow_DoPress()throws Exception
    {
        String workids = this.GetValByKey("WorkIDs");
        String[] strs = workids.split("[,]", -1);

        String msg = this.GetValByKey("Msg");
        if (msg == null)
        {
            msg = "需要您处理待办工作.";
        }

        String info = "";
        for (String workidStr : strs)
        {
            if (bp.da.DataType.IsNullOrEmpty(workidStr) == true)
            {
                continue;
            }

            info += "@" + bp.wf.Dev2Interface.Flow_DoPress(Integer.parseInt(workidStr), msg, true);
        }
       return info;
    }
    /**
     批量设置抄送查看完毕

     @return
     */
    public final String CC_BatchCheckOver() throws Exception
    {
        String workids = this.GetValByKey("WorkIDs");
        String str = bp.wf.Dev2Interface.Node_CC_SetCheckOverBatch(workids);
        return str;
    }


    /**
     获得发起的url.

     */
    public final String GenerFrmUrl() throws Exception
    {
//
//             * 发起的url需要在该流程的开始节点的表单方案中，使用SDK表单，并把表单的url设置到里面去.
//             * 设置步骤:
//             * 1. 打开流程设计器.
//             * 2. 在开始节点上右键，选择表单方案.
//             * 3. 选择SDK表单，把url配置到文本框里去.
//             * 比如: /App/F027QingJia.htm
//

        try
        {
            int nodeID = this.getFK_Node();
            if (nodeID == 0)
            {
                nodeID = Integer.parseInt(this.getFK_Flow() + "01");
            }

            long workid = this.getWorkID();
            if (workid == 0)
            {
                workid = bp.wf.Dev2Interface.Node_CreateBlankWork(this.getFK_Flow(), bp.web.WebUser.getNo());
            }

            String url = "";
            bp.wf.Node nd = new bp.wf.Node(nodeID);
            if (nd.getFormType() == NodeFormType.SDKForm || nd.getFormType() == NodeFormType.SelfForm)
            {
                url = nd.getFormUrl();
                if (url.contains("?") == true)
                {
                    url += "&FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + nodeID + "&WorkID=" + workid + "&Token=" + this.SID + "&UserNo=" + bp.web.WebUser.getNo();
                }
                else
                {
                    url += "?FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + nodeID + "&WorkID=" + workid + "&Token=" + this.SID + "&UserNo=" + bp.web.WebUser.getNo();
                }
            }
            else
            {
                url = "/WF/MyFlow.htm?FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + nodeID + "&WorkID=" + this.getWorkID() + "&Token=" + this.SID;
            }
            return url;
        }
        catch (RuntimeException ex)
        {
            //输出url.
            return "err@" + ex.getMessage();
        }
    }

    @Override
    public Class getCtrlType() {
        return null;
    }
}
