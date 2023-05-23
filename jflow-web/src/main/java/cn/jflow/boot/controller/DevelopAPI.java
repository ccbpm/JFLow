package cn.jflow.boot.controller;

import bp.da.*;
import bp.difference.ContextHolderUtils;
import bp.difference.handler.HttpHandlerBase;
import bp.en.QueryObject;
import bp.web.WebUser;
import bp.wf.*;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.Hashtable;

@RestController
@Api(tags="工具包接口")
@RequestMapping(value = "/API")
public class DevelopAPI extends HttpHandlerBase {
    /**
     * 根据密钥和用户名登录
     * @param PrivateKey 密钥
     * @param UserNo 用户名
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/Portal_Login_Submit")
    public final String Portal_Login_Submit(@RequestParam(required = true)String PrivateKey, @ApiParam("用户编号") @RequestParam(required = true)String UserNo) throws Exception {
        if(DataType.IsNullOrEmpty(PrivateKey) == true){
            return "err@参数privateKey不能为空";
        }
        if(DataType.IsNullOrEmpty(UserNo) == true){
            return "err@参数userNo不能为空";
        }
        String localKey = bp.difference.SystemConfig.GetValByKey("PrivateKey", "DiGuaDiGua,IamCCBPM");
        if (localKey.equals(PrivateKey) == false)
            return "err@私约错误，请检查全局文件中配置 PrivateKey ";
        try{
            bp.wf.Dev2Interface.Port_Login(UserNo);
            String Token = bp.wf.Dev2Interface.Port_GenerToken(UserNo);

            Hashtable ht = new Hashtable();
            ht.put("No", WebUser.getNo());
            ht.put("Name", WebUser.getName());
            ht.put("FK_Dept", WebUser.getFK_Dept());
            ht.put("FK_DeptName", WebUser.getFK_DeptName());
            ht.put("OrgNo", WebUser.getOrgNo());
            ht.put("OrgName", WebUser.getOrgName());
            ht.put("Token", Token);
            return bp.tools.Json.ToJson(ht);
        }catch(Exception e){
            return "err@登录失败:"+e.getMessage();
        }
    }
    @PostMapping(value = "/Port_Emp_Save")
    @ApiOperation("人员信息保存")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="Token",paramType = "query",required = true),
            @ApiImplicitParam(name="userNo",value="用户编号",required = true),
            @ApiImplicitParam(name="userName",value="用户名称",required = true),
            @ApiImplicitParam(name="deptNo",value="部门编号",required = true),
            @ApiImplicitParam(name="kvs",value="参数集合,例如:@Tel=18660153303@Addr=山东.济南",required = false),
            @ApiImplicitParam(name="stats",value="岗位(角色)集合,比如:001,002,003,",required = false),
    })
    public String Port_Emp_Save(String token, String orgNo, String userNo, String userName, String deptNo, String kvs, String stats) throws Exception {
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        if (bp.web.WebUser.getIsAdmin() == false)
            return "0";
        return bp.port.OrganizationAPI.Port_Emp_Save(orgNo, userNo, userName, deptNo, kvs, stats);

    }
    /// <summary>
    /// 保存岗位
    /// </summary>
    /// <param name="userNo"></param>
    /// <param name="stas">岗位用逗号分开</param>
    /// <returns>reutrn 1=成功,  其他的标识异常.</returns>
    @PostMapping(value = "/Port_Emp_Delete")
    @ApiOperation("人员删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="Token",paramType = "query",required = true),
            @ApiImplicitParam(name="orgNo",value="组织编号",required = false),
            @ApiImplicitParam(name="userNo",value="用户编号",required = true),
    })
    public String Port_Emp_Delete(String token,String orgNo, String userNo) throws Exception {
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        if (bp.web.WebUser.getIsAdmin() == false)
            return "0";
        return bp.port.OrganizationAPI.Port_Emp_Delete(orgNo, userNo);
    }
    /// <returns>return 1 增加成功，其他的增加失败.</returns>
    @PostMapping(value = "/Port_Org_Save")
    @ApiOperation("集团模式下同步组织以及管理员信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "orgNo", value = "组织编号", required = true),
            @ApiImplicitParam(name = "name", value = "组织名称", required = true),
            @ApiImplicitParam(name = "adminer", value = "管理员账号", required = true),
            @ApiImplicitParam(name = "adminerName", value = "管理员名字", required = true),
            @ApiImplicitParam(name = "keyVals", value = "其他的值:@Leaer=zhangsan@Tel=12233333@Idx=1", required = false)
    })
    public String Port_Org_Save(String token,String orgNo,  String name, String adminer, String adminerName, String keyVals) throws Exception {
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        if (bp.web.WebUser.getIsAdmin() == false)
            return "0";
        return bp.port.OrganizationAPI.Port_Org_Save(orgNo, name, adminer, adminerName,keyVals);
    }

    @PostMapping(value = "/Port_Dept_Save")
    @ApiOperation("保存部门, 如果有此数据则修改，无此数据则增加.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "orgNo", value = "组织编号", required = true),
            @ApiImplicitParam(name = "no", value = "部门编号", required = true),
            @ApiImplicitParam(name = "name", value = "名称", required = true),
            @ApiImplicitParam(name = "parentNo", value = "父节点编号", required = true),
            @ApiImplicitParam(name = "keyVals", value = "其他的值:@Leaer=zhangsan@Tel=12233333@Idx=1", required = false)
    })
    public String Port_Dept_Save(String token,String orgNo, String no, String name, String parentNo, String keyVals) throws Exception {
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        if (bp.web.WebUser.getIsAdmin() == false)
            return "0";
        return bp.port.OrganizationAPI.Port_Dept_Save(orgNo, no, name, parentNo, keyVals);

    }

    @PostMapping(value = "/Port_Dept_Delete")
    @ApiOperation("删除部门.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "no", value = "部门编号", required = true)
    })
    public String Port_Dept_Delete(String token,String no) throws Exception {
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        if (bp.web.WebUser.getIsAdmin() == false)
            return "0";
        return bp.port.OrganizationAPI.Port_Dept_Delete(no);
    }
    /// <summary>
    /// 保存岗位, 如果有此数据则修改，无此数据则增加.
    /// </summary>
    /// <param name="orgNo">组织编号</param>
    /// <param name="no">编号</param>
    /// <param name="name">名称</param>
    /// <returns>return 1 增加成功，其他的增加失败.</returns>
    @PostMapping(value = "/Port_Station_Save")
    @ApiOperation("保存岗位, 如果有此数据则修改，无此数据则增加..")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "orgNo", value = "组织编号", required = true),
            @ApiImplicitParam(name = "no", value = "岗位编号", required = true),
            @ApiImplicitParam(name = "name", value = "岗位编号", required = true),
            @ApiImplicitParam(name = "keyVals", value = "其他值", required = false)
    })
    public String Port_Station_Save(String token,String orgNo, String no, String name, String keyVals) throws Exception {
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        if (bp.web.WebUser.getIsAdmin() == false)
            return "0";
        return bp.port.OrganizationAPI.Port_Station_Save(orgNo, no, name, keyVals);
    }
    /// <summary>
    /// 删除岗位.
    /// </summary>
    /// <param name="no">删除指定的岗位编号</param>
    /// <returns></returns>
    @PostMapping(value = "/Port_Station_Delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "no", value = "岗位编号", required = true)
    })
    public String Port_Station_Delete(String token,String no) throws Exception {
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        if (bp.web.WebUser.getIsAdmin() == false)
            return "0";
        return bp.port.OrganizationAPI.Port_Station_Delete(no);
    }

    /**
     * 创建根据流程编号WorkID
     * @param Token
     * @param FK_Flow
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/Node_CreateBlankWorkID")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "FK_Flow", value = "流程编号", required = true)
    })
    public final String Node_CreateBlankWorkID(String Token,String FK_Flow) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        if(DataType.IsNullOrEmpty(FK_Flow) == true)
            return "err@流程编号FK_Flow值的不能为空";
        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        try{
            long WorkID = bp.wf.Dev2Interface.Node_CreateBlankWork(FK_Flow, bp.web.WebUser.getNo());
            return String.valueOf(WorkID);
        }catch(Exception e){
            return "err@流程创建WorkID失败:"+e.getMessage();
        }
    }

    /**
     * 设置流程实例为草稿状态
     * @param Token
     * @param FK_Flow
     * @param WorkID
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/Node_SetDraft")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "FK_Flow", value = "流程编号", required = true),
            @ApiImplicitParam(name = "WorkID", value = "流程实例WorkID", required = true)
    })
    public final String Node_SetDraft(String Token,String FK_Flow,long WorkID) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        if(DataType.IsNullOrEmpty(FK_Flow) == true)
            return "err@流程编号值的不能为空";
        if(DataType.IsNullOrEmpty(WorkID) == true)
            return "err@流程实例WorkID值不能为空";

        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        try{
            bp.wf.Dev2Interface.Node_SetDraft(FK_Flow, WorkID);
            return "流程实例设置草稿成功";
        }catch(Exception e){
            return "err@流程设置草稿失败:"+e.getMessage();
        }
    }

    /**
     * 指定给特定的移交人
     * @param Token
     * @param WorkID
     * @param ToEmpNo
     * @param Msg
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/Node_Shift")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "WorkID", value = "流程实例WorkID", required = true),
            @ApiImplicitParam(name = "ToEmpNo", value = "人员编号", required = true),
            @ApiImplicitParam(name = "Msg", value = "说明", required = false)
    })
    public final String Node_Shift(String Token,long WorkID,String ToEmpNo,String Msg) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        if(DataType.IsNullOrEmpty(WorkID) == true)
            return "err@流程实例WorkID值不能为空";
        if(DataType.IsNullOrEmpty(ToEmpNo) == true)
            return "err@指定的移交人不能为空";

        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        try{
            return bp.wf.Dev2Interface.Node_Shift(WorkID, ToEmpNo, Msg);
        }catch(Exception e){
            return "err@设置工作移交失败:"+e.getMessage();
        }
    }
    /**
     * 给指定的流程实例增加指定的待办人员
     * @param Token
     * @param WorkID
     * @param EmpNo
     * @return 执行结果,
     * @throws Exception
     */
    @PostMapping(value = "/Node_AddTodolist")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "WorkID", value = "流程实例WorkID", required = true),
            @ApiImplicitParam(name = "EmpNo", value = "人员编号", required = true)
    })
    public final String Node_AddTodolist(String Token,long WorkID,String EmpNo) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        if(DataType.IsNullOrEmpty(WorkID) == true)
            return "err@流程实例WorkID值不能为空";
        if(DataType.IsNullOrEmpty(EmpNo) == true)
            return "err@给指定的人增加待办，人员编号不能为空";

        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        try{
            bp.wf.Dev2Interface.Node_AddTodolist(WorkID, EmpNo);
            return "给指定的人增加待办成功";

        }catch(Exception e){
            return "err@给指定的人增加待办失败:"+e.getMessage();
        }
    }

    /**
     * 获取指定的流程实例信息
     * @param Token
     * @param WorkID
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/Flow_GenerWorkFlow")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "WorkID", value = "流程实例WorkID", required = true)
    })
    public final String Flow_GenerWorkFlow(String Token,long WorkID) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        if(DataType.IsNullOrEmpty(WorkID) == true)
            return "err@流程实例WorkID值不能为空";

        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        try{
            GenerWorkFlow gwf = new GenerWorkFlow(WorkID);
            return gwf.ToJson();
        }catch(Exception e){
            return "err@获取流程实例信息失败:"+e.getMessage();

        }
    }

    @PostMapping(value = "/Node_SaveParas")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "WorkID", value = "流程实例WorkID", required = true),
            @ApiImplicitParam(name = "Paras", value = "参数", required = true)
    })
    public final String Node_SaveParas(String Token,long WorkID,String Paras) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        if(DataType.IsNullOrEmpty(WorkID) == true)
            return "err@流程实例WorkID值不能为空";
        if(DataType.IsNullOrEmpty(Paras) == true)
            return "err@参数Paras值不能为空";

        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        try{
            bp.wf.Dev2Interface.Flow_SaveParas(WorkID, Paras);
            return "参数值保存成功";
        }catch(Exception e){
            return "err@参数值保存失败:"+e.getMessage();
        }
    }

    @PostMapping(value = "/Flow_SetTitle")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "WorkID", value = "流程实例WorkID", required = true),
            @ApiImplicitParam(name = "Title", value = "标题", required = true),
            @ApiImplicitParam(name = "FK_Flow", value = "流程编号", required = false)
    })
    public final String Flow_SetTitle(String Token,long WorkID,String Title,String FK_Flow) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        if(DataType.IsNullOrEmpty(WorkID) == true)
            return "err@流程实例WorkID值不能为空";
        if(DataType.IsNullOrEmpty(Title) == true)
            return "err@标题Title值不能为空";

        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        try{
            bp.wf.Dev2Interface.Flow_SetFlowTitle(FK_Flow, WorkID, Title);
            return "标题设置成功";
        }catch(Exception e){
            return "err@标题设置失败:"+e.getMessage();
        }
    }

    @PostMapping(value = "/Node_SendWork")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "WorkID", value = "流程实例WorkID", required = true),
            @ApiImplicitParam(name = "FK_Flow", value = "流程编号", required = false),
            @ApiImplicitParam(name = "ToNodeID", value = "到达节点ID,空时传0", required = false),
            @ApiImplicitParam(name = "ToEmps", value = "到达人员编号，多人以,隔开", required = false)
    })
    public final String Node_SendWork(String Token,long WorkID,String FK_Flow,int ToNodeID,String ToEmps) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        if(DataType.IsNullOrEmpty(WorkID) == true)
            return "err@流程实例WorkID值不能为空";

        bp.wf.Dev2Interface.Port_LoginByToken(Token);

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
        try{
            String fk_flow = FK_Flow;
            if (DataType.IsNullOrEmpty(fk_flow) == true)
                fk_flow = DBAccess.RunSQLReturnString("SELECT FK_Flow FROM WF_GenerWorkFlow WHERE WorkID=" +WorkID);
            //执行发送.
            SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, WorkID, ht, null, ToNodeID, ToEmps);
            return objs.ToMsgOfText();

        }catch(Exception e){
            return "err@发送失败:"+e.getMessage();

        }
    }

    @PostMapping(value = "/DB_GenerWillReturnNodes")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "WorkID", value = "流程实例WorkID", required = true),
            @ApiImplicitParam(name = "FID", value = "流程ID", required = false),
            @ApiImplicitParam(name = "FK_Node", value = "节点编号", required = true)
    })
    public final String DB_GenerWillReturnNodes(String Token,long WorkID,Long FID,Integer FK_Node) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        if(DataType.IsNullOrEmpty(WorkID) == true || WorkID==0)
            return "err@流程实例WorkID值不能为空";
        if(DataType.IsNullOrEmpty(FK_Node) == true || FK_Node.intValue() == 0)
            return "err@节点编号FK_Node值不能为空";

        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        try{
            //获得可以退回的节点.
            DataTable dt = bp.wf.Dev2Interface.DB_GenerWillReturnNodes(FK_Node!=null?FK_Node.intValue():0, WorkID, FID==null?0:FID.longValue());
            return bp.tools.Json.ToJson(dt);
        }catch(Exception e){
            return "err@获取可以退回的节点失败:"+e.getMessage();

        }
    }

    /**
     * 执行退回
     * @param Token
     * @param WorkID
     * @param ReturnToNodeID 退回到节点
     * @param ReturnToEmp 退回给指定的人员
     * @param Msg 退回原因
     * @param IsBackToThisNode 是否原路返回
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/Node_ReturnWork")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "WorkID", value = "流程实例WorkID", required = true),
            @ApiImplicitParam(name = "FID", value = "流程ID", required = false),
            @ApiImplicitParam(name = "FK_Node", value = "当前节点编号", required = true),
            @ApiImplicitParam(name = "ReturnToNodeID", value = "退回到达节点编号，空时传0", required = false),
            @ApiImplicitParam(name = "ReturnToEmp", value = "退回到达人员编号", required = false),
            @ApiImplicitParam(name = "Msg", value = "退回原因", required = true),
            @ApiImplicitParam(name = "IsBackToThisNode", value = "是否按原路返回", required = false)
    })
    public final String Node_ReturnWork(String Token,long WorkID,Integer FID,Integer FK_Node,Integer ReturnToNodeID,String ReturnToEmp,String Msg,boolean IsBackToThisNode) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        if(DataType.IsNullOrEmpty(WorkID) == true || WorkID ==0)
            return "err@流程实例WorkID值不能为空或者0";
        if(FK_Node==null || FK_Node.intValue()==0)
            return "err@当前节点编号FK_Node值不能为空或者0";
        if(DataType.IsNullOrEmpty(Msg) == true)
            return "err@退回原因Msg值不能为空";

        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        try{
            int returnToNodeID = ReturnToNodeID==null?0:ReturnToNodeID.intValue();
            if(returnToNodeID == 0)
            {
                int nodeID = 0;
                if(FK_Node!=null)
                    nodeID = FK_Node.intValue();
                DataTable dt = bp.wf.Dev2Interface.DB_GenerWillReturnNodes(nodeID, WorkID, FID!=null?FID.longValue():0);
                if (dt.Rows.size() == 1)
                {
                    returnToNodeID = Integer.parseInt(dt.Rows.get(0).getValue("No").toString());
                    ReturnToEmp = dt.Rows.get(0).getValue("Rec").toString();

                }
            }
            return  bp.wf.Dev2Interface.Node_ReturnWork(WorkID,  returnToNodeID, ReturnToEmp, Msg, IsBackToThisNode);
        }catch(Exception e){
            return "err@执行退回失败:"+e.getMessage();

        }
    }

    @PostMapping(value = "/Search_Init")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "Scop", value = "流程实例WorkID", required = false),
            @ApiImplicitParam(name = "Key", value = "0:我参与的，1:我发起的，2:我部门发起的", required = false),
            @ApiImplicitParam(name = "DTFrom", value = "日期从", required = false),
            @ApiImplicitParam(name = "DTTo", value = "日期到", required = false),
            @ApiImplicitParam(name = "PageIdx", value = "页码", required = false)
    })
    public final String Search_Init(String Token,String Scop,String Key,String DTFrom,String DTTo,int PageIdx) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";

        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        try{
            return Search_Init(Scop,Key,DTFrom,DTTo,PageIdx);
        }catch(Exception e){
            return "err@获取可以退回的节点失败:"+e.getMessage();

        }
    }

    @PostMapping(value = "/GenerFrmUrl")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "WorkID", value = "流程实例WorkID", required = true),
            @ApiImplicitParam(name = "FK_Flow", value = "流程编号", required = true),
            @ApiImplicitParam(name = "FK_Node", value = "当前节点编号", required = false)
    })
    public final String GenerFrmUrl(String Token,long WorkID,String FK_Flow,Integer FK_Node) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        if(DataType.IsNullOrEmpty(WorkID) == true)
            return "err@流程实例WorkID值不能为空";
        if(DataType.IsNullOrEmpty(FK_Flow) == true)
            return "err@流程编号FK_Flow值不能为空";
        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        try{
            return GenerFrmUrl(WorkID,FK_Flow,FK_Node==null?0:FK_Node.intValue(),Token);
        }catch(Exception e){
            return "err@获取可以发起的流程列表失败:"+e.getMessage();

        }
    }

    /**
     * 获取可以发起的流程
     * @param Token
     * @param Domain
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/DB_Start")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "Domain", value = "域，可传空或空字符串", required = false)
    })
    public final String DB_Start(String Token,String Domain) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        try{
            DataTable dtStrat = bp.wf.Dev2Interface.DB_StarFlows(bp.web.WebUser.getNo(),Domain);
            return bp.tools.Json.ToJson(dtStrat);
        }catch(Exception e){
            return "err@获取可以发起的流程列表失败:"+e.getMessage();

        }
    }

    /**
     * 获取草稿列表
     * @param Token
     * @param FK_Flow
     * @param Domain
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/DB_Draft")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "FK_Flow", value = "流程编号", required = false),
            @ApiImplicitParam(name = "Domain", value = "域，可传空或空字符串", required = false)
    })
    public final String DB_Draft(String Token,String FK_Flow,String Domain) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        try{
            DataTable dtDraft = bp.wf.Dev2Interface.DB_GenerDraftDataTable(FK_Flow,Domain);
            return bp.tools.Json.ToJson(dtDraft);
        }catch(Exception e){
            return "err@获取草稿列表失败:"+e.getMessage();

        }
    }

    /**
     * 获取待办列表
     * @param Token
     * @param flowNo
     * @param nodeID
     * @param Domain
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/DB_Todolist")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "flowNo", value = "流程编号", required = false),
            @ApiImplicitParam(name = "nodeID", value = "当前节点编号", required = false),
            @ApiImplicitParam(name = "Domain", value = "域，可传空或空字符串", required = false)
    })
    public final String DB_Todolist(String Token,String flowNo,Integer nodeID,String Domain) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        try{
            DataTable dtTodolist = bp.wf.Dev2Interface.DB_Todolist(bp.web.WebUser.getNo(), nodeID==null?0:nodeID.intValue(), flowNo, Domain);
            return bp.tools.Json.ToJson(dtTodolist);
        }catch(Exception e){
            return "err@获取待办列表失败:"+e.getMessage();

        }
    }

    @PostMapping(value = "/DB_Runing")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "Domain", value = "域，可传空或空字符串", required = false)
    })
    public final String DB_Runing(String Token,String Domain) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        try{
            DataTable dtRuing = bp.wf.Dev2Interface.DB_GenerRuning(bp.web.WebUser.getNo(), false, Domain);
            return bp.tools.Json.ToJson(dtRuing);
        }catch(Exception e){
            return "err@获取在途列表失败:"+e.getMessage();

        }
    }
    @PostMapping(value = "/DB_CCList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "Domain", value = "域，可传空或空字符串", required = false)
    })
    public final String DB_CCList(String Token,String Domain) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        try{
            DataTable dtCC = bp.wf.Dev2Interface.DB_CCList(Domain);
            return bp.tools.Json.ToJson(dtCC);
        }catch(Exception e){
            return "err@获取在抄送列表失败:"+e.getMessage();
        }
    }

    /**
     * 根据抄送状态获取抄送列表
     * @param Token
     * @param Sta
     * @param Domain
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/DB_CCListBySta")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "Sta", value = "状态,枚举类型:0 未读;1 已读;2 已回复;3 删除;", required = false),
            @ApiImplicitParam(name = "Domain", value = "域，可传空或空字符串", required = false)
    })
    public final String DB_CCListBySta(String Token,Integer Sta, String Domain) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        try{
            DataTable dtCC = bp.wf.Dev2Interface.DB_CCList(CCSta.forValue(Sta==null?0:Sta.intValue()),Domain);
            return bp.tools.Json.ToJson(dtCC);
        }catch(Exception e){
            return "err@根据抄送状态获取抄送列表失败:"+e.getMessage();
        }
    }

    /**
     * 给指定的节点增加抄送列表
     * @param Token
     * @param FK_Node
     * @param Title
     * @param Doc
     * @param Emps
     * @param Depts
     * @param Stations
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/Node_CC_WriteTo_CClist")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "WorkID", value = "流程实例WorkID", required = false),
            @ApiImplicitParam(name = "FK_Node", value = "当前节点编号", required = true),
            @ApiImplicitParam(name = "Title", value = "标题", required = false),
            @ApiImplicitParam(name = "Doc", value = "内容", required = false),
            @ApiImplicitParam(name = "Emps", value = "人员编号,多人以,隔开", required = false),
            @ApiImplicitParam(name = "Depts", value = "部门编号,多个以,隔开", required = false),
            @ApiImplicitParam(name = "Stations", value = "岗位编号,多个以,隔开", required = false)
    })
    public final String Node_CC_WriteTo_CClist(String Token,long WorkID,Integer FK_Node,String Title, String Doc,String Emps,String Depts,String Stations) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        if(FK_Node==null || FK_Node.intValue() == 0)
            return "err@当前节点编号FK_Node值不能为空或者0";
        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        try{
            //抄送给指定的人 格式：zhangsan,张三;lisi,李四；，部门 格式：1001;10002。岗位  格式 1001;1002
            if(DataType.IsNullOrEmpty(Title)==true)
                Title="来自"+bp.web.WebUser.getNo()+"的抄送信息.";
            if(DataType.IsNullOrEmpty(Doc)==true)
                Doc="来自"+bp.web.WebUser.getNo()+"的抄送信息.";

            String ccRec = bp.wf.Dev2Interface.Node_CC_WriteTo_CClist(FK_Node==null?0:FK_Node.intValue(), WorkID, Title, Doc,  Emps,
                    Depts ,Stations, null);
            if(DataType.IsNullOrEmpty(ccRec)==true)
                return "err@没有设置可以抄送的人员";
            return ccRec;
        }catch(Exception e){
            return "err@获取在抄送列表失败:"+e.getMessage();
        }
    }

    /**
     * 批量催办
     * @param Token
     * @param WorkIDs
     * @param Msg
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/Flow_DoPress")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "WorkIDs", value = "流程实例WorkID集合,多个以,隔开", required = false),
            @ApiImplicitParam(name = "Msg", value = "催办信息", required = false)
    })
    public final String Flow_DoPress(String Token,String WorkIDs,String Msg) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        try{
            return this.Flow_DoPress(WorkIDs,Msg);
        }catch(Exception e){
            return "err@催办失败:"+e.getMessage();
        }
    }

    /**
     * 批量抄送审核.
     * @param Token
     * @param WorkIDs
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/CC_BatchCheckOver")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "WorkIDs", value = "流程实例WorkID集合,多个以,隔开", required = true)
    })
    public final String CC_BatchCheckOver(String Token,String WorkIDs) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        if(DataType.IsNullOrEmpty(WorkIDs) == true)
            return "err@批量阅读完毕不能为空";
        try{
            return this.CC_BatchCheckOver(WorkIDs);
        }catch(Exception e){
            return "err@催办失败:"+e.getMessage();
        }
    }

    /**
     * 批量逻辑删除流程实例
     * @param Token
     * @param WorkIDs
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/Flow_BatchDeleteByFlag")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "WorkIDs", value = "流程实例WorkID集合,多个以,隔开", required = true)
    })
    public final String Flow_BatchDeleteByFlag(String Token,String WorkIDs) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        if(DataType.IsNullOrEmpty(WorkIDs) == true)
            return "err@批删除WorkIDs值不能为空";
        try{
            return this.Flow_BatchDeleteByFlag(WorkIDs);
        }catch(Exception e){
            return "err@催办失败:"+e.getMessage();
        }
    }

    /**
     * 批量物流删除流程实例
     * @param Token
     * @param WorkIDs
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/Flow_BatchDeleteByReal")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "WorkIDs", value = "流程实例WorkID集合,多个以,隔开", required = true)
    })
    public final String Flow_BatchDeleteByReal(String Token,String WorkIDs) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        if(DataType.IsNullOrEmpty(WorkIDs) == true)
            return "err@批删除WorkIDs值不能为空";
        try{
            return this.Flow_BatchDeleteByReal(WorkIDs);
        }catch(Exception e){
            return "err@催办失败:"+e.getMessage();
        }
    }

    /**
     * 批量撤销删除的流程
     * @param Token
     * @param WorkIDs
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/Flow_BatchDeleteByFlagAndUnDone")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "WorkIDs", value = "流程实例WorkID集合,多个以,隔开", required = true)
    })
    public final String Flow_BatchDeleteByFlagAndUnDone(String Token,String  WorkIDs) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        if(DataType.IsNullOrEmpty(WorkIDs) == true)
            return "err@批量撤销删除WorkIDs值不能为空";
        try{
            return this.Flow_BatchDeleteByFlagAndUnDone(WorkIDs);
        }catch(Exception e){
            return "err@批量删除流程实例失败:"+e.getMessage();
        }
    }

    /**
     * 批量撤销流程实例
     * @param Token
     * @param WorkIDs
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/Flow_DoUnSend")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "WorkIDs", value = "流程实例WorkID集合,多个以,隔开", required = true)
    })
    public final String Flow_DoUnSend(String Token,String WorkIDs) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        if(DataType.IsNullOrEmpty(WorkIDs) == true)
            return "err@批量撤销WorkIDs值不能为空";
        try{
            return this.Flow_DoUnSend(WorkIDs);
        }catch(Exception e){
            return "err@批量撤销流程失败:"+e.getMessage();
        }
    }

    /**
     * 批量删除草稿
     * @param Token
     * @param WorkIDs
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/Flow_DeleteDraft")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "WorkIDs", value = "流程实例WorkID集合,多个以,隔开", required = true)
    })
    public final String Flow_DeleteDraft(String Token,String  WorkIDs) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        if(DataType.IsNullOrEmpty(WorkIDs) == true)
            return "err@批量删除草稿WorkIDs值不能为空";
        try{
            return this.Flow_DeleteDraft();
        }catch(Exception e){
            return "err@批量删除草稿失败:"+e.getMessage();
        }
    }

    /**
     * 批量结束流程实例
     * @param Token
     * @param WorkIDs
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/Flow_DoFlowOver")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "WorkIDs", value = "流程实例WorkID集合,多个以,隔开", required = true)
    })
    public final String Flow_DoFlowOver(String Token,String WorkIDs) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        try{
            return this.Flow_DoFlowOver(WorkIDs);
        }catch(Exception e){
            return "err@批量结束流程实例失败:"+e.getMessage();
        }
    }

    /**
     * 退出登录
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/Port_LoginOut")
    public final String Portal_LoginOut() throws Exception {
        try{
            bp.wf.Dev2Interface.Port_SigOut();
            return "退出成功";
        }catch(Exception e){
            return "err@退出成功失败:"+e.getMessage();
        }
    }

    /**
     * 获取消息类型
     * @param Token
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/InfoSorts")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true)
    })
    public final String InfoSorts(String Token) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        try{
            String sql = "SELECT * FROM OA_InfoType ";
            DataTable dt = DBAccess.RunSQLReturnTable(sql);
            return bp.tools.Json.ToJson(dt);
        }catch(Exception e){
            return "err@获取消息类型失败:"+e.getMessage();
        }
    }

    /**
     * 获取消息列表
     * @param Token
     * @param SortNo
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/InfoDtls")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "SortNo", value = "类型,外键:对应物理表:OA_InfoType,表描述:信息类型", required = true)
    })
    public final String InfoDtls(String Token,String SortNo) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        try{
            String sql="";
            if (DataType.IsNullOrEmpty(SortNo) == true)
                sql = "SELECT * FROM OA_Info WHERE InfoPRI=1 ";
            else
                sql = "SELECT * FROM OA_Info WHERE InfoType='" + SortNo + "' ";

            DataTable dt = DBAccess.RunSQLReturnTable(sql);
            return bp.tools.Json.ToJson(dt);
        }catch(Exception e){
            return "err@获取消息列表失败:"+e.getMessage();
        }
    }

    /**
     * 根据编号获取消息内容
     * @param Token
     * @param No
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/Dtl")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "No", value = "消息编号", required = true)
    })
    public final String Dtl(String Token,String No) throws Exception {
        if(DataType.IsNullOrEmpty(Token) == true)
            return "err@用户的Token值不能为空";
        if(DataType.IsNullOrEmpty(No) == true)
            return "err@消息编号No值不能为空";
        bp.wf.Dev2Interface.Port_LoginByToken(Token);
        try{
            String sql = "SELECT * FROM OA_Info WHERE No='" + No + "' ";
            DataTable dt = DBAccess.RunSQLReturnTable(sql);
            dt.TableName = "OA_InfoDtl";
            return bp.tools.Json.ToJson(dt);
        }catch(Exception e){
            return "err@根据编号["+No+"]获取消息信息失败:"+e.getMessage();
        }
    }

    public String Search_Init(String scop,String key,String dtFrom,String dtTo,int pageIdx) throws Exception {
        GenerWorkFlows gwfs = new GenerWorkFlows();

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


    public final String Flow_DoFlowOver(String WorkIDs)throws Exception
    {
        String[] strs = WorkIDs.split("[,]", -1);

        String info = "";
        for (String WorkIDStr : strs)
        {
            if (bp.da.DataType.IsNullOrEmpty(WorkIDStr) == true)
                continue;
            bp.wf.Dev2Interface.Flow_DoFlowOver(Long.parseLong(WorkIDStr), "批量结束", 1);
        }
        return "执行成功.";
    }
    /**
     删除草稿

     */
    public final String Flow_DeleteDraft()throws Exception
    {
        String WorkIDs = this.GetValByKey("WorkIDs");
        String[] strs = WorkIDs.split("[,]", -1);

        String info = "";
        for (String WorkIDStr : strs)
        {
            if (bp.da.DataType.IsNullOrEmpty(WorkIDStr) == true)
            {
                continue;
            }

            bp.wf.Dev2Interface.Node_DeleteDraft(Long.parseLong(WorkIDStr));
        }
        return "删除成功.";
    }

    /**
     撤销发送

     */
    public final String Flow_DoUnSend(String WorkIDs)throws Exception
    {
        String[] strs = WorkIDs.split("[,]", -1);

        String info = "";
        for (String WorkIDStr : strs)
        {
            if (bp.da.DataType.IsNullOrEmpty(WorkIDStr) == true)
            {
                continue;
            }

            info += bp.wf.Dev2Interface.Flow_DoUnSend(null, Long.parseLong(WorkIDStr), 0, 0);
        }
        return info;
    }
    public final String Flow_BatchDeleteByReal(String WorkIDs) throws Exception
    {
        String[] strs = WorkIDs.split("[,]", -1);
        for (String WorkIDStr : strs)
        {
            if (bp.da.DataType.IsNullOrEmpty(WorkIDStr) == true)
                continue;
            bp.wf.Dev2Interface.Flow_DoDeleteFlowByReal(Long.parseLong(WorkIDStr), true);
        }
        return  "删除成功.";
    }
    /**
     删除功能
     */
    public final String Flow_BatchDeleteByFlag(String WorkIDs)throws Exception
    {
        String[] strs = WorkIDs.split("[,]", -1);
        for (String WorkIDStr : strs)
        {
            if (bp.da.DataType.IsNullOrEmpty(WorkIDStr) == true)
                continue;
            bp.wf.Dev2Interface.Flow_DoDeleteFlowByFlag(Long.parseLong(WorkIDStr), "删除", true);
        }
        return "删除成功.";
    }
    /**
     恢复删除
     */
    public final String Flow_BatchDeleteByFlagAndUnDone(String WorkIDs) throws Exception
    {
        String[] strs = WorkIDs.split("[,]", -1);

        for (String WorkIDStr : strs)
        {
            if (bp.da.DataType.IsNullOrEmpty(WorkIDStr) == true)
                continue;
            bp.wf.Dev2Interface.Flow_DoUnDeleteFlowByFlag(null, Integer.parseInt(WorkIDStr), "删除");
        }

        return "恢复成功.";
    }

    /**
     * 批量催办
     * @param WorkIDs
     * @param msg
     * @return
     * @throws Exception
     */
    public final String Flow_DoPress(String WorkIDs,String msg)throws Exception
    {
        String[] strs = WorkIDs.split("[,]", -1);
        if (DataType.IsNullOrEmpty(msg))
            msg = "需要您处理待办工作.";
        String info = "";
        for (String WorkIDStr : strs)
        {
            if (bp.da.DataType.IsNullOrEmpty(WorkIDStr) == true)
                continue;
            info += "@" + bp.wf.Dev2Interface.Flow_DoPress(Integer.parseInt(WorkIDStr), msg, true);
        }
        return info;
    }
    /**
     批量设置抄送查看完毕
     @return
     */
    public final String CC_BatchCheckOver(String WorkIDs) throws Exception
    {
        return bp.wf.Dev2Interface.Node_CC_SetCheckOverBatch(WorkIDs);
    }


    /**
     获得发起的url.

     */
    public final String GenerFrmUrl(long WorkID,String fk_flow,Integer fk_node,String Token) throws Exception
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
            int nodeID = fk_node!=null?fk_node.intValue():0;
            if (nodeID == 0)
                nodeID = Integer.parseInt(fk_flow + "01");
            if (WorkID == 0)
                WorkID = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow, bp.web.WebUser.getNo());

            String url = "";
            bp.wf.Node nd = new bp.wf.Node(nodeID);
            if (nd.getFormType() == NodeFormType.SDKForm || nd.getFormType() == NodeFormType.SelfForm)
            {
                url = nd.getFormUrl();
                if (url.contains("?") == true)
                {
                    url += "&FK_Flow=" + fk_flow + "&FK_Node=" + nodeID + "&WorkID=" + WorkID + "&Token=" +Token + "&UserNo=" + bp.web.WebUser.getNo();
                }
                else
                {
                    url += "?FK_Flow=" + fk_flow + "&FK_Node=" + nodeID + "&WorkID=" + WorkID + "&Token=" + Token + "&UserNo=" + bp.web.WebUser.getNo();
                }
            }
            else
            {
                url = "/WF/MyFlow.htm?FK_Flow=" + fk_flow + "&FK_Node=" + nodeID + "&WorkID=" + WorkID + "&Token=" + Token;
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
