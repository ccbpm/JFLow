package cn.jflow.boot.controller;

import bp.da.*;
import bp.difference.ContextHolderUtils;
import bp.difference.SystemConfig;
import bp.difference.handler.HttpHandlerBase;
import bp.en.QueryObject;
import bp.sys.CCBPMRunModel;
import bp.tools.Json;
import bp.web.WebUser;
import bp.wf.*;
import bp.wf.template.Directions;
import io.lettuce.core.dynamic.annotation.Param;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.Hashtable;

@RestController
@Api(tags="工具包接口")
@RequestMapping(value = "/WF/API")
public class DevelopAPI extends HttpHandlerBase {
    public static Object Return_Info(int code, String msg, String data)
    {
        Hashtable ht = new Hashtable();
        ht.put("code", code);
        ht.put("message", msg);
        ht.put("data", data);
        return ht;
    }
    @PostMapping(value = "/Port_Login")
    @ApiOperation("根据密钥和用户名登录,返回用户登陆信息其中有Token")
     @ApiImplicitParams({
            @ApiImplicitParam(name="privateKey",value="密钥",paramType = "query",required = true),
            @ApiImplicitParam(name="userNo",value="用户编号",required = true),
            @ApiImplicitParam(name="orgNo",value="租户/组织编号（集团版和SaaS版需要填写）",required = true),
    })
    public final Object Port_Login_Submit(String privateKey, String userNo, String orgNo) throws Exception {
        if(DataType.IsNullOrEmpty(privateKey) == true){
            return Return_Info(500,"登陆失败","参数privateKey不能为空");
        }
        if(DataType.IsNullOrEmpty(userNo) == true){
            return Return_Info(500,"登陆失败","参数userNo不能为空");
        }
        String localKey = bp.difference.SystemConfig.GetValByKey("PrivateKey", "DiGuaDiGua,IamCCBPM");
        if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
        {
            bp.wf.port.admin2group.Org org = new bp.wf.port.admin2group.Org(orgNo);
            String key = org.GetValStrByKey("PrivateKey");
            if (DataType.IsNullOrEmpty(key) == false)
                localKey = key;
        }
        if (DataType.IsNullOrEmpty(localKey) == true)
            localKey = "DiGuaDiGua,IamCCBPM";
        if (localKey.equals(privateKey) == false)
            return Return_Info(500,"登陆失败","私钥错误，请检查全局文件中配置 PrivateKey");

        try{
            bp.wf.Dev2Interface.Port_Login(userNo,orgNo);
            String Token = bp.wf.Dev2Interface.Port_GenerToken();
            Hashtable ht = new Hashtable();
            ht.put("No", WebUser.getNo());
            ht.put("Name", WebUser.getName());
            ht.put("FK_Dept", WebUser.getFK_Dept());
            ht.put("FK_DeptName", WebUser.getFK_DeptName());
            ht.put("OrgNo", WebUser.getOrgNo());
            ht.put("OrgName", WebUser.getOrgName());
            ht.put("Token", Token);
            return Return_Info(200,"登陆成功",bp.tools.Json.ToJson(ht));
        }catch(Exception e){
            return Return_Info(500,"登陆失败","登录失败:"+e.getMessage());
        }
    }
    @PostMapping(value = "/Port_Emp_Save")
    @ApiOperation("人员信息保存")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="Token",paramType = "query",required = true),
            @ApiImplicitParam(name="orgNo",value="组织编号",required = true),
            @ApiImplicitParam(name="userNo",value="用户编号",required = true),
            @ApiImplicitParam(name="userName",value="用户名称",required = true),
            @ApiImplicitParam(name="deptNo",value="部门编号",required = true),
            @ApiImplicitParam(name="kvs",value="参数集合,例如:@Tel=18660153303@Addr=山东.济南",required = false),
            @ApiImplicitParam(name="stats",value="岗位(角色)集合,比如:001,002,003,",required = false),
    })
    public Object Port_Emp_Save(String token, String orgNo, String userNo, String userName, String deptNo, String kvs, String stats) throws Exception {
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        if (bp.web.WebUser.getIsAdmin() == false)
            return Return_Info(500,"[" + bp.web.WebUser.getName() + "]不是管理员不能操作人员信息","0");
        return Return_Info(200,"人员信息保存成功",bp.port.OrganizationAPI.Port_Emp_Save(orgNo, userNo, userName, deptNo, kvs, stats));
    }
    /// <summary>
    /// 人员删除
    /// </summary>
    /// <param name="token">Token</param>
    /// <param name="orgNo">组织编号</param>
    /// <param name="userNo">人员编号</param>
    /// <returns>reutrn 1=成功,  其他的标识异常.</returns>
    @PostMapping(value = "/Port_Emp_Delete")
    @ApiOperation("人员删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="Token",paramType = "query",required = true),
            @ApiImplicitParam(name="orgNo",value="组织编号",required = false),
            @ApiImplicitParam(name="userNo",value="用户编号",required = true),
    })
    public Object Port_Emp_Delete(String token,String orgNo, String userNo) throws Exception {
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        if (bp.web.WebUser.getIsAdmin() == false)
            return Return_Info(500,"[" + bp.web.WebUser.getName() + "]不是管理员不能操作人员信息","0");
        return Return_Info(200,"人员删除成功",bp.port.OrganizationAPI.Port_Emp_Delete(orgNo, userNo));
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
    public Object Port_Org_Save(String token,String orgNo,  String name, String adminer, String adminerName, String keyVals) throws Exception {
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        if (bp.web.WebUser.getIsAdmin() == false)
            return Return_Info(500,"[" + bp.web.WebUser.getName() + "]不是管理员不能操作组织信息","0");
        return Return_Info(200,"集团模式下同步组织以及管理员信息成功",bp.port.OrganizationAPI.Port_Org_Save(orgNo, name, adminer, adminerName,keyVals));
    }

    @PostMapping(value = "/Port_Dept_Save")
    @ApiOperation("保存部门,如果有此数据则修改,无此数据则增加.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "orgNo", value = "组织编号", required = false),
            @ApiImplicitParam(name = "no", value = "部门编号", required = true),
            @ApiImplicitParam(name = "name", value = "名称", required = true),
            @ApiImplicitParam(name = "parentNo", value = "父节点编号", required = true),
            @ApiImplicitParam(name = "keyVals", value = "其他的值:@Leaer=zhangsan@Tel=18660153393@Idx=1", required = false)
    })
    public Object Port_Dept_Save(String token,String orgNo, String no, String name, String parentNo, String keyVals) throws Exception {
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        if (bp.web.WebUser.getIsAdmin() == false)
            return Return_Info(500,"[" + bp.web.WebUser.getName() + "]不是管理员不能删保存部门信息","0");
        return Return_Info(200,"保存部门信息成功",bp.port.OrganizationAPI.Port_Dept_Save(orgNo, no, name, parentNo, keyVals));

    }

    @PostMapping(value = "/Port_Dept_Delete")
    @ApiOperation("删除部门.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "no", value = "要删除的部门编号", required = true)
    })
    public Object Port_Dept_Delete(String token,String no) throws Exception {
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        if (bp.web.WebUser.getIsAdmin() == false)
            return Return_Info(500,"[" + bp.web.WebUser.getName() + "]不是管理员不能删除部门信息","0");
        return Return_Info(200,"删除部门成功",bp.port.OrganizationAPI.Port_Dept_Delete(no));
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
    public Object Port_Station_Save(String token,String orgNo, String no, String name, String keyVals) throws Exception {
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        if (bp.web.WebUser.getIsAdmin() == false)
            return Return_Info(500,"[" + bp.web.WebUser.getName() + "]不是管理员不能保存岗位信息","0");
        return Return_Info(200,"保存岗位成功",bp.port.OrganizationAPI.Port_Station_Save(orgNo, no, name, keyVals));
    }
    /// <summary>
    /// 删除岗位.
    /// </summary>
    /// <param name="no">删除指定的岗位编号</param>
    /// <returns></returns>
    @PostMapping(value = "/Port_Station_Delete")
    @ApiOperation("删除岗位")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "no", value = "要删除的岗位编号", required = true)
    })
    public Object Port_Station_Delete(String token,String no) throws Exception {
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        if (bp.web.WebUser.getIsAdmin() == false)
            return Return_Info(500,"[" + bp.web.WebUser.getName() + "]不是管理员不能删除岗位信息","0");
        return Return_Info(200,"删除岗位成功",bp.port.OrganizationAPI.Port_Station_Delete(no));
    }
    /**
     保存用户组, 如果有此数据则修改，无此数据则增加.
     @param token Token
     @param orgNo 组织编号
     @param no 用户组编号
     @param name 用户组名称
     @param keyVals 其他值
     @return
     */
    @PostMapping(value = "/Port_Team_Save")
    @ApiOperation("保存用户组, 如果有此数据则修改，无此数据则增加")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "orgNo", value = "组织编号", required = true),
            @ApiImplicitParam(name = "no", value = "用户组编号", required = true),
            @ApiImplicitParam(name = "name", value = "用户组名称", required = true),
            @ApiImplicitParam(name = "keyVals", value = "其他值", required = false)
    })
   public final Object Port_Team_Save(String token, String orgNo, String no, String name, String keyVals) throws Exception {
        bp.wf.Dev2Interface.Port_LoginByToken(token);

        if (WebUser.getIsAdmin() == false)
            return Return_Info(500,"[" + bp.web.WebUser.getName() + "]不是管理员不能维护用户组信息","0");

        return Return_Info(200,"保存用户组成功",bp.port.OrganizationAPI.Port_Team_Save(orgNo, no, name, keyVals));
    }
    /**
     删除用户组.
     @param no 删除指定的用户组编号
     @return
     */
    @PostMapping(value = "/Port_Team_Delete")
    @ApiOperation("删除用户组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "no", value = "删除指定的用户组编号", required = true)
    })
    public final Object Port_Team_Delete(String token, String no) throws Exception {
        //根据token登录
        bp.wf.Dev2Interface.Port_LoginByToken(token);

        if (WebUser.getIsAdmin() == false)
            return Return_Info(500,"[" + bp.web.WebUser.getName() + "]不是管理员不能删除用户组信息","0");

        return  Return_Info(200,"删除用户组成功",bp.port.OrganizationAPI.Port_Team_Delete(no));
    }
    /**
     保存用户组, 如果有此数据则修改，无此数据则增加.

     @param token Token
     @param orgNo 组织编号
     @param no 用户组编号
     @param name 用户组名称
     @param keyVals 其他值
     @return
     */
    @PostMapping(value = "/Port_TeamType_Save")
    @ApiOperation("保存用户组类型, 如果有此数据则修改，无此数据则增加")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "orgNo", value = "组织编号", required = true),
            @ApiImplicitParam(name = "no", value = "用户组编号", required = true),
            @ApiImplicitParam(name = "name", value = "用户组名称", required = true),
            @ApiImplicitParam(name = "keyVals", value = "其他值", required = false)
    })
   public final Object Port_TeamType_Save(String token, String orgNo, String no, String name, String keyVals) throws Exception {
        bp.wf.Dev2Interface.Port_LoginByToken(token);
       
        if (WebUser.getIsAdmin() == false)
            return Return_Info(500,"[" + bp.web.WebUser.getName() + "]不是管理员不能维护用户组信息","0");

        return Return_Info(200,"保存用户组类型成功",bp.port.OrganizationAPI.Port_TeamType_Save(orgNo, no, name, keyVals));
    }
    /**
     * 删除用户组.
     *
     * @param no 删除指定的用户组编号
     * @return
     */
    @PostMapping(value = "/Port_TeamType_Delete")
    @ApiOperation("删除用户组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "no", value = "删除指定的用户组编号", required = true)
    })
    public final Object Port_TeamType_Delete(String token, String no) throws Exception {
        //根据token登录
        bp.wf.Dev2Interface.Port_LoginByToken(token);

        if (WebUser.getIsAdmin() == false)
            return Return_Info(500,"[" + bp.web.WebUser.getName() + "]不是管理员不能删除用户组信息","0");

        return Return_Info(200,"删除用户组成功",bp.port.OrganizationAPI.Port_TeamType_Delete(no));
    }

    /**
     * 保存岗位类型, 如果有此数据则修改，无此数据则增加.
     *
     * @param token   Token
     * @param orgNo   组织编号
     * @param no      岗位类型编号
     * @param name    岗位类型名称
     * @param keyVals 其他值
     * @return
     */
    @PostMapping(value = "/Port_StationType_Save")
    @ApiOperation("保存岗位类型, 如果有此数据则修改，无此数据则增加")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "orgNo", value = "组织编号", required = true),
            @ApiImplicitParam(name = "no", value = "岗位类型编号", required = true),
            @ApiImplicitParam(name = "name", value = "岗位类型名称", required = true),
            @ApiImplicitParam(name = "keyVals", value = "其他值", required = false)
    })
   public final Object Port_StationType_Save(String token, String orgNo, String no, String name, String keyVals) throws Exception {
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        
        if (WebUser.getIsAdmin() == false)
            return Return_Info(500,"[" + bp.web.WebUser.getName() + "]不是管理员不能维护岗位类型信息","0");

        return Return_Info(200,"保存岗位类型成功",bp.port.OrganizationAPI.Port_StationType_Save(orgNo, no, name, keyVals));
    }
    /**
     * 删除岗位类型.
     *
     * @param no 删除指定的岗位类型编号
     * @return
     */
    @PostMapping(value = "/Port_StationType_Delete")
    @ApiOperation("删除岗位类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "no", value = "删除指定的岗位类型编号", required = true)
    })
    public final Object Port_StationType_Delete(String token, String no) throws Exception {
        //根据token登录
        bp.wf.Dev2Interface.Port_LoginByToken(token);

        if (WebUser.getIsAdmin() == false)
            return Return_Info(500,"[" + bp.web.WebUser.getName() + "]不是管理员不能删除岗位类型信息","0");

        return Return_Info(200,"删除岗位类型成功",bp.port.OrganizationAPI.Port_StationType_Delete(no));
    }

    /**
     * 创建根据流程编号WorkID
     * @param token
     * @param flowNo
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/Node_CreateBlankWorkID")
    @ApiOperation("创建根据流程编号WorkID,开发者根据这个WorkID作为单据的主键保存单据表里.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "flowNo", value = "流程模板编号", required = true)
    })
    public final Object Node_CreateBlankWorkID(String token,String flowNo) throws Exception {
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        try{
            long WorkID = bp.wf.Dev2Interface.Node_CreateBlankWork(flowNo, bp.web.WebUser.getUserID());
            return Return_Info(200,"创建根据流程编号WorkID成功",String.valueOf(WorkID));
        }catch(Exception e){
            return Return_Info(500,"流程创建WorkID失败","流程创建WorkID失败:"+e.getMessage());
        }
    }

    @PostMapping(value = "/Node_SetDraft")
    @ApiOperation("设置草稿.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "flowNo", value = "流程编号", required = true),
            @ApiImplicitParam(name = "workID", value = "流程实例WorkID", dataType = "Long", required = true)
    })
    public final Object Node_SetDraft(String token,String flowNo,Long workID) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"设置草稿失败","用户的Token值不能为空");
        if(DataType.IsNullOrEmpty(flowNo) == true)
            return Return_Info(500,"设置草稿失败","流程编号值的不能为空");
        if(DataType.IsNullOrEmpty(workID) == true)
            return Return_Info(500,"设置草稿失败","流程实例WorkID值不能为空");

        bp.wf.Dev2Interface.Port_LoginByToken(token);
        try{
            bp.wf.Dev2Interface.Node_SetDraft( workID);
            return Return_Info(200,"流程实例设置草稿成功","");
        }catch(Exception e){
            return Return_Info(500,"流程设置草稿失败","流程设置草稿失败:"+e.getMessage());
        }
    }
    /**
     * 指定给特定的移交人
     * @param token
     * @param workID
     * @param toEmpNo
     * @param msg
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/Node_Shift")
    @ApiOperation("移交")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "workID", value = "流程实例WorkID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "toEmpNo", value = "人员编号", required = true),
            @ApiImplicitParam(name = "Msg", value = "移交原因", required = true)
    })
    public final Object Node_Shift(String token,Long workID,String toEmpNo,String msg) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"移交失败","用户的Token值不能为空");
        if(DataType.IsNullOrEmpty(workID) == true)
            return Return_Info(500,"移交失败","流程实例WorkID值不能为空");
        if(DataType.IsNullOrEmpty(toEmpNo) == true)
            return Return_Info(500,"移交失败","指定的移交人不能为空");
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        try{

            return  Return_Info(200,"移交成功",bp.wf.Dev2Interface.Node_Shift(workID, toEmpNo, msg));
        }catch(Exception e){
            return Return_Info(500,"设置工作移交失败","设置工作移交失败:"+e.getMessage());
        }
    }
    /**
     * 给指定的流程实例增加指定的待办人员
     * @param token
     * @param workID
     * @param empNo
     * @return 执行结果,
     * @throws Exception
     */
    @PostMapping(value = "/Node_AddTodolist")
    @ApiOperation("给指定的流程实例增加指定的待办人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "workID", value = "流程实例WorkID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "empNo", value = "要增加的人员编号", required = true)
    })
    public final Object Node_AddTodolist(String token,Long workID,String empNo) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"给指定的流程实例增加指定的待办人员失败","用户的Token值不能为空");
        if(DataType.IsNullOrEmpty(workID) == true)
            return Return_Info(500,"给指定的流程实例增加指定的待办人员失败","流程实例WorkID值不能为空");
        if(DataType.IsNullOrEmpty(empNo) == true)
            return Return_Info(500,"给指定的流程实例增加指定的待办人员失败","给指定的人增加待办，人员编号不能为空");
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        try{

            bp.wf.Dev2Interface.Node_AddTodolist(workID,empNo );
            return  Return_Info(200,"给指定的人增加待办成功","");

        }catch(Exception e){
            return Return_Info(500,"给指定的人增加待办失败","给指定的人增加待办失败:"+e.getMessage());
        }
    }

    /**
     * 
     * @param token
     * @param workID
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/Flow_GenerWorkFlow")
    @ApiOperation("获取流程实例信息,节点状态,发起人,停留节点,当前人待办等.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "workID", value = "流程实例workID", dataType = "Long", required = true)
    })
    public final Object Flow_GenerWorkFlow(String token,Long workID) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"获取流程实例信息失败","用户的Token值不能为空");
        if(DataType.IsNullOrEmpty(workID) == true)
            return Return_Info(500,"获取流程实例信息失败","流程实例WorkID值不能为空");

        bp.wf.Dev2Interface.Port_LoginByToken(token);
        try{
            GenerWorkFlow gwf = new GenerWorkFlow(workID);

            return  Return_Info(200,"获取流程实例信息成功",gwf.ToJson());
        }catch(Exception e){
            return Return_Info(500,"获取流程实例信息失败","获取流程实例信息失败:"+e.getMessage());
        }
    }

    @PostMapping(value = "/Node_SaveParas")
    @ApiOperation("保存参数:可以作为方向条件,接受人规则等参数.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "workID", value = "流程实例workID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "paras", value = "参数,格式@Key1=Val1@Key2=Val2比如,@Tel=18660153393@Addr=山东.济南@Age=35", required = true)
    })
    public final Object Node_SaveParas(String token,Long workID,String paras) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"保存参数失败","用户的Token值不能为空");
        if(DataType.IsNullOrEmpty(workID) == true)
            return Return_Info(500,"保存参数失败","流程实例WorkID值不能为空");
        if(DataType.IsNullOrEmpty(paras) == true)
            return Return_Info(500,"保存参数失败","参数Paras值不能为空");
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        try{
            bp.wf.Dev2Interface.Flow_SaveParas(workID, paras);
            return  Return_Info(200,"参数值保存成功","");
        }catch(Exception e){
            return Return_Info(500,"保存参数失败","参数值保存失败:"+e.getMessage());
        }
    }

    @PostMapping(value = "/Flow_SetTitle")
    @ApiOperation("设置标题:流程实例的标题,也可以使用流程属性的标题生成规则生成.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "workID", value = "流程实例workID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "title", value = "标题", required = true),
            @ApiImplicitParam(name = "flowNo", value = "流程编号", required = false)
    })
    public final Object Flow_SetTitle(String token,Long workID,String title,String flowNo) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"设置标题失败","用户的Token值不能为空");
        if(DataType.IsNullOrEmpty(workID) == true)
            return Return_Info(500,"设置标题失败","流程实例WorkID值不能为空");
        if(DataType.IsNullOrEmpty(title) == true)
            return Return_Info(500,"设置标题失败","标题Title值不能为空");

        bp.wf.Dev2Interface.Port_LoginByToken(token);
        try{
            bp.wf.Dev2Interface.Flow_SetFlowTitle(flowNo, workID, title);
            return  Return_Info(200,"标题设置成功","");
        }catch(Exception e){
            return Return_Info(500,"设置标题失败","标题设置失败:"+e.getMessage());
        }
    }

    @PostMapping(value = "/Node_SendWork")
    @ApiOperation("执行发送:从一个节点运动到下一个节点.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "workID", value = "流程实例workID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "ht", value = "主表单数据,没有可为null", required = false),
            @ApiImplicitParam(name = "paras", value = "参数，保存到WF_GenerWorkFlow,用与参数条件", required = false),
            @ApiImplicitParam(name = "toEmps", value = "接受人:设置空表示,根据到达的节点的接受人规则计算接收人,多个接受人用逗号分开,比如:zhangsan,lisi", required = false),
            @ApiImplicitParam(name = "toNodeIDStr", value = "到达的下一个节点,默认为0设置0表示让ccbpm根据方向条件判断方向,可以是节点Mark", required = false),
            @ApiImplicitParam(name = "checkNote", value = "审核意见:启用了审核组件，就需要填写审核意见", required = false)
    })
    public final Object Node_SendWork(String token,Long workID,String ht,String toEmps,String paras,String toNodeIDStr, String checkNote) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"执行发送失败","用户的Token值不能为空");
        if(DataType.IsNullOrEmpty(workID) == true)
            return Return_Info(500,"执行发送失败","流程实例WorkID值不能为空");
        if(!DataType.IsNullOrEmpty(ht) && !DataType.IsJson(ht))
            return Return_Info(500,"执行发送失败","输入的ht参数不是JSON格式");

        bp.wf.Dev2Interface.Port_LoginByToken(token);
        //保存参数.
        if (DataType.IsNullOrEmpty(paras) == false)
            bp.wf.Dev2Interface.Flow_SaveParas(workID, paras);

        //写入审核意见.
        if (DataType.IsNullOrEmpty(checkNote) == false)
            bp.wf.Dev2Interface.WriteTrackWorkCheck(workID, checkNote,null,null,null);
        //执行发送.
        try{
            String  flowNo = DBAccess.RunSQLReturnString("SELECT FK_Flow FROM WF_GenerWorkFlow WHERE workID=" +workID);
            int toNodeID = DealNodeIDStr(toNodeIDStr, flowNo);
            //执行发送.
            SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(flowNo, workID, DataType.IsNullOrEmpty(ht) == true ? null : DataType.ToHashtable(ht), null, toNodeID, toEmps);

            return  Return_Info(200,"执行发送成功",objs.ToMsgOfText());
        }catch(Exception e){
            return Return_Info(500,"执行发送失败",e.getMessage());
        }
    }
    @PostMapping(value = "/Node_SendWork_ReJson")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "workID", value = "流程实例workID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "flowNo", value = "流程编号", required = false),
            @ApiImplicitParam(name = "toNodeID", value = "到达节点ID,空时传0", dataType = "Int", required = false),
            @ApiImplicitParam(name = "toEmps", value = "到达人员编号，多人以,隔开", required = false)
    })
    public final Object Node_SendWork_ReJson(String token,Long workID,String flowNo,Integer toNodeID,String toEmps) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"执行发送失败","用户的Token值不能为空");
        if(DataType.IsNullOrEmpty(workID) == true)
            return Return_Info(500,"执行发送失败","流程实例WorkID值不能为空");
        bp.wf.Dev2Interface.Port_LoginByToken(token);
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
            String fk_flow = flowNo;
            if (DataType.IsNullOrEmpty(fk_flow) == true)
                fk_flow = DBAccess.RunSQLReturnString("SELECT FK_Flow FROM WF_GenerWorkFlow WHERE workID=" +workID);
            //执行发送.
            SendReturnObjs objs = bp.wf.Dev2Interface.Node_SendWork(fk_flow, workID, ht, null, toNodeID, toEmps);

            return  Return_Info(200,"执行发送成功",objs.ToJson());
        }catch(Exception e){
            return Return_Info(500,"执行发送失败",e.getMessage());
        }
    }
    @PostMapping(value = "/DB_GenerWillReturnNodes")
    @ApiOperation("退回到的节点:执行退回的时候,首先要选择可以退回的节点,放入到下拉框让操作员选择.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "workID", value = "流程实例workID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "fid", value = "流程ID", dataType = "Long", required = false),
            @ApiImplicitParam(name = "nodeID", value = "节点ID", dataType = "Int", required = true)
    })
    public final Object DB_GenerWillReturnNodes(String token,Long workID,Long fid,Integer nodeID) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"获取可以退回的节点失败","用户的Token值不能为空");
        if(DataType.IsNullOrEmpty(workID) == true)
            return Return_Info(500,"获取可以退回的节点失败","流程实例WorkID值不能为空");
        if(DataType.IsNullOrEmpty(nodeID) == true || nodeID.intValue() == 0)
            return Return_Info(500,"获取可以退回的节点失败","节点编号FK_Node值不能为空");
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        try{
            //获得可以退回的节点.
            DataTable dt = bp.wf.Dev2Interface.DB_GenerWillReturnNodes(workID);
            return  Return_Info(200,"获取可以退回的节点成功",bp.tools.Json.ToJson(dt));
        }catch(Exception e){
            return Return_Info(500,"获取可以退回的节点失败","获取可以退回的节点失败:"+e.getMessage());
        }
    }
   
    @PostMapping(value = "/Node_ReturnWork")
    @ApiOperation("执行退回:退回与发送是相反的操作,在ccbpm没有同意不同意概念,只有前进后退.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "workID", value = "流程实例workID", required = true),
            @ApiImplicitParam(name = "nodeIDStr", value = "当前节点编号，支持标识", required = true),
            @ApiImplicitParam(name = "returnToNodeIDStr", value = "退回到达节点编号，空时传0", required = false),
            @ApiImplicitParam(name = "returnToEmp", value = "退回到达人员编号", required = false),
            @ApiImplicitParam(name = "msg", value = "退回原因", required = true),
            @ApiImplicitParam(name = "isBackToThisNode", value = "是否按原路返回", required = false)
    })
    public final Object Node_ReturnWork(String token,long workID, String nodeIDStr,String returnToNodeIDStr,String returnToEmp,String msg,boolean isBackToThisNode) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"执行退回失败","用户的Token值不能为空");
        if(DataType.IsNullOrEmpty(workID) == true)
            return Return_Info(500,"执行退回失败","流程实例WorkID值不能为空");
        if(nodeIDStr==null)
            return Return_Info(500,"执行退回失败","当前节点编号FK_Node值不能为空或者0");
        if(DataType.IsNullOrEmpty(msg) == true)
            return Return_Info(500,"执行退回失败","退回原因Msg值不能为空");

        bp.wf.Dev2Interface.Port_LoginByToken(token);
        try{

            String  flowNo = DBAccess.RunSQLReturnString("SELECT FK_Flow FROM WF_GenerWorkFlow WHERE workID=" +workID);
            int nodeID = DealNodeIDStr(nodeIDStr, flowNo);
            int returnToNodeID = DealNodeIDStr(returnToNodeIDStr, flowNo);

            if(returnToNodeID == 0)
            {
                DataTable dt = bp.wf.Dev2Interface.DB_GenerWillReturnNodes(workID);
                if (dt.Rows.size() == 1)
                {
                    returnToNodeID = Integer.parseInt(dt.Rows.get(0).getValue("No").toString());
                    returnToEmp = dt.Rows.get(0).getValue("Rec").toString();

                }
            }
            return  Return_Info(200,"执行退回成功",
                    bp.wf.Dev2Interface.Node_ReturnWork(workID,  returnToNodeID, returnToEmp, msg, isBackToThisNode));
        }catch(Exception e){
            return Return_Info(500,"执行退回失败",e.getMessage());
        }
    }

    @PostMapping(value = "/Search_Init")
    @ApiOperation("流程实例查询:对流程注册表的查询,WF_GenerWorkFlow请参考表结构.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "scop", value = "流程实例workID", required = false),
            @ApiImplicitParam(name = "key", value = "0:我参与的，1:我发起的，2:我部门发起的", required = false),
            @ApiImplicitParam(name = "dtFrom", value = "日期从", required = false),
            @ApiImplicitParam(name = "dtTo", value = "日期到", required = false),
            @ApiImplicitParam(name = "pageIdx", value = "页码", dataType = "Int", required = false)
    })
    public final Object Search_Init(String token,String scop,String key,String dtFrom,String dtTo,Integer pageIdx) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"流程实例查询失败","用户的Token值不能为空");
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        try{
            return  Return_Info(200,"流程实例查询成功",Search_Init(scop,key,dtFrom,dtTo,pageIdx));
        }catch(Exception e){
            return Return_Info(500,"流程实例查询失败",e.getMessage());
        }
    }

     @PostMapping(value = "/GenerFrmUrl")
     @ApiOperation("获得表单URL:对流程注册表的查询,WF_GenerWorkFlow请参考表结构.")
     @ApiImplicitParams({
             @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
             @ApiImplicitParam(name = "workID", value = "流程实例workID",dataType = "Long",required = true),
             @ApiImplicitParam(name = "FK_Flow", value = "流程编号", required = true),
             @ApiImplicitParam(name = "FK_Node", value = "当前节点编号", dataType = "Int",required = false)
     })
     public final Object GenerFrmUrl(@RequestParam("token") String token,@RequestParam("workID") long workID, @RequestParam("flowNo")String FK_Flow, @RequestParam("nodeID")Integer FK_Node) throws Exception {
         if(DataType.IsNullOrEmpty(token) == true)
             return "err@用户的Token值不能为空";
         if(DataType.IsNullOrEmpty(workID) == true)
             return "err@流程实例workID值不能为空";
         if(DataType.IsNullOrEmpty(FK_Flow) == true)
             return "err@流程编号FK_Flow值不能为空";
         bp.wf.Dev2Interface.Port_LoginByToken(token);
         try{
             return GenerFrmUrl(workID,FK_Flow,FK_Node==null?0:FK_Node.intValue(),token);
         }catch(Exception e){
             return "err@获取可以发起的流程列表失败:"+e.getMessage();
         }
     }
  
    @PostMapping(value = "/DB_Start")
    @ApiOperation("获得可发起的流程:一个可以发起的流程由他的身份决定的,在开始节点的右键属性里设置.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "domain", value = "域:可传空,比如:CRM,OA,ERP等,配置在流程目录属性上.", required = false)
    })
    public final Object DB_Start(String token,String domain) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"获得可发起的流程失败","用户的Token值不能为空");

        bp.wf.Dev2Interface.Port_LoginByToken(token);
        try{
            String userNo = bp.web.WebUser.getNo();
            if(SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS){
                userNo = bp.web.WebUser.getOrgNo() + "_" +bp.web.WebUser.getNo();;
            }
            DataTable dtStrat = bp.wf.Dev2Interface.DB_StarFlows(userNo,domain);
            return  Return_Info(200,"流程实例查询成功", bp.tools.Json.ToJson(dtStrat));
        }catch(Exception e){
            return Return_Info(500,"获取可以发起的流程列表失败",e.getMessage());
        }
    }

    @PostMapping(value = "/DB_Draft")
    @ApiOperation("草稿:暂存的表单,请参考流程属性草稿规则.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "flowNo", value = "流程模板编号", required = false),
            @ApiImplicitParam(name = "domain", value = "域:可传空,比如:CRM,OA,ERP等,配置在流程目录属性上.", required = false)
    })
    public final Object DB_Draft(String token,String flowNo,String domain) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"获取草稿列表失败","用户的Token值不能为空");
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        try{
            DataTable dtDraft = bp.wf.Dev2Interface.DB_GenerDraftDataTable(flowNo,domain);
            return  Return_Info(200,"获取草稿列表成功", bp.tools.Json.ToJson(dtDraft));
        }catch(Exception e){
            return Return_Info(500,"获取草稿列表失败",e.getMessage());
        }
    }

    @PostMapping(value = "/DB_Todolist")
    @ApiOperation("待办:等待我解决的问题,包括发送、转发、移交、撤销的工作.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "flowNo", value = "流程模板编号:要取得指定流程的待办就传入.", required = false),
            @ApiImplicitParam(name = "nodeID", value = "节点ID:要取得指定节点的待办就传入,默认为0.", dataType = "Int", required = false),
            @ApiImplicitParam(name = "domain", value = "域:可传空,比如:CRM,OA,ERP等,配置在流程目录属性上.", required = false)
    })
    public final Object DB_Todolist(String token,String flowNo,Integer nodeID,String domain) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"获取待办列表失败","用户的Token值不能为空");
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        try{
            DataTable dtTodolist = bp.wf.Dev2Interface.DB_GenerEmpWorksOfDataTable(bp.web.WebUser.getNo(), nodeID==null?0:nodeID.intValue(), flowNo, domain);
            return  Return_Info(200,"获取待办列表成功",  bp.tools.Json.ToJson(dtTodolist));
        }catch(Exception e){
            return Return_Info(500,"获取待办列表失败",e.getMessage());

        }
    }

    @PostMapping(value = "/DB_Runing")
    @ApiOperation("在途:我参与的流程但是该流程没有走完,可以执行催办与撤销操作.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "domain", value = "域:可传空,比如:CRM,OA,ERP等,配置在流程目录属性上.", required = false)
    })
    public final Object DB_Runing(String token,String domain) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"获取在途列表失败","用户的Token值不能为空");
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        try{
            DataTable dtRuing = bp.wf.Dev2Interface.DB_GenerRuning(bp.web.WebUser.getNo(), false, domain);
            return  Return_Info(200,"获取在途列表成功", bp.tools.Json.ToJson(dtRuing));
        }catch(Exception e){
            return Return_Info(500,"获取在途列表失败",e.getMessage());
        }
    }
    
    @PostMapping(value = "/DB_CCList")
    @ApiOperation("抄送:发送给别人,知会给我的工作.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "domain", value = "域:可传空,比如:CRM,OA,ERP等,配置在流程目录属性上.", required = false)
    })
    public final Object DB_CCList(String token,String domain) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"获取在途列表失败","用户的Token值不能为空");
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        try{
            DataTable dtCC = bp.wf.Dev2Interface.DB_CCList(domain);
            return  Return_Info(200,"获取抄送列表成功", bp.tools.Json.ToJson(dtCC));
        }catch(Exception e){
            return Return_Info(500,"获取抄送列表失败",e.getMessage());
        }
    }

    @PostMapping(value = "/DB_CCListBySta")
    @ApiOperation("抄送:发送给别人,知会给我的工作.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "sta", value = "状态,枚举类型:0 未读;1 已读;2 已回复;3 删除;", dataType = "Int", required = false),
            @ApiImplicitParam(name = "domain", value = "域:可传空,比如:CRM,OA,ERP等,配置在流程目录属性上.", required = false)
    })
    public final Object DB_CCListBySta(String token,Integer sta, String domain) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"根据抄送状态获取抄送列表失败","用户的Token值不能为空");
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        try{
            DataTable dtCC = bp.wf.Dev2Interface.DB_CCList(CCSta.forValue(sta==null?0:sta.intValue()),domain);

            return  Return_Info(200,"根据抄送状态获取抄送列表成功",bp.tools.Json.ToJson(dtCC));
        }catch(Exception e){
            return Return_Info(500,"根据抄送状态获取抄送列表失败",e.getMessage());
        }
    }

    @PostMapping(value = "/Node_CC_WriteTo_CClist")
    @ApiOperation("执行抄送:写入抄送的方法")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "workID", value = "流程实例workID", dataType = "Long", required = false),
            @ApiImplicitParam(name = "nodeID", value = "当前节点ID", dataType = "Int", required = true),
            @ApiImplicitParam(name = "title", value = "标题", required = true),
            @ApiImplicitParam(name = "doc", value = "内容", required = true),
            @ApiImplicitParam(name = "emps", value = "人员编号,多人以,隔开", required = false),
            @ApiImplicitParam(name = "depts", value = "部门编号,多个以,隔开", required = false),
            @ApiImplicitParam(name = "stations", value = "岗位编号,多个以,隔开", required = false)
    })
    public final Object Node_CC_WriteTo_CClist(String token,Long workID,Integer nodeID,String title, String doc,String emps,String depts,String stations) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"执行抄送失败","用户的Token值不能为空");
        if(nodeID==null || nodeID.intValue() == 0)
            return Return_Info(500,"执行抄送失败","当前节点编号FK_Node值不能为空或者0");
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        try{
            //抄送给指定的人 格式：zhangsan,张三;lisi,李四；，部门 格式：1001;10002。岗位  格式 1001;1002
            if(DataType.IsNullOrEmpty(title)==true)
                title="来自"+bp.web.WebUser.getNo()+"的抄送信息.";
            if(DataType.IsNullOrEmpty(doc)==true)
                doc="来自"+bp.web.WebUser.getNo()+"的抄送信息.";

            String ccRec = bp.wf.Dev2Interface.Node_CC_WriteTo_CClist(nodeID==null?0:nodeID.intValue(), workID, title, doc,  emps,
                    depts ,stations, null);
            if(DataType.IsNullOrEmpty(ccRec)==true)
                return Return_Info(500,"没有设置可以抄送的人员","");

            return Return_Info(200,"执行抄送成功",ccRec);
        }catch(Exception e){
            return Return_Info(500,"执行抄送失败",e.getMessage());
        }
    }

    @PostMapping(value = "/Flow_DoPress")
    @ApiOperation("催办:提醒当前流程的节点处理人,及时处理工作.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "workIDs", value = "流程实例workID集合:多个以,隔开", required = false),
            @ApiImplicitParam(name = "msg", value = "催办信息", required = false)
    })
    public final Object Flow_DoPress(String token,String workIDs,String msg) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"催办失败","用户的Token值不能为空");
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        try{
            String[] strs = workIDs.split("[,]", -1);
        if (DataType.IsNullOrEmpty(msg))
            msg = "需要您处理待办工作.";
        String info = "";
        for (String workIDStr : strs)
        {
            if (bp.da.DataType.IsNullOrEmpty(workIDStr) == true)
                continue;
            info += "@" + bp.wf.Dev2Interface.Flow_DoPress(Integer.parseInt(workIDStr), msg, true);
        }
            return Return_Info(200,"催办成功",info);
        }catch(Exception e){
            return Return_Info(500,"催办失败",e.getMessage());
        }
    }

    /**
     * 批量抄送审核.
     * @param token
     * @param workIDs
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/CC_BatchCheckOver")
    @ApiOperation("抄送批量阅知:批量阅知已读.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "workIDs", value = "流程实例workID集合:多个以,隔开", required = true)
    })
    public final Object CC_BatchCheckOver(String token,String workIDs) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"抄送批量阅知失败","用户的Token值不能为空");
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        if(DataType.IsNullOrEmpty(workIDs) == true)
            return Return_Info(500,"抄送批量阅知失败","批量阅读完毕的workIDs不能为空");
        try{
            return Return_Info(200,"抄送批量阅知成功",this.CC_BatchCheckOver(workIDs));
        }catch(Exception e){
            return Return_Info(500,"抄送批量阅知失败",e.getMessage());
        }
    }

    @PostMapping(value = "/Flow_BatchDeleteByFlag")
    @ApiOperation("批量逻辑删除流程实例:对流程实例在WF_GenerWorkFlow的WFState设置删除标记=8.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "workIDs", value = "流程实例workID集合:多个以,隔开", required = true)
    })
    public final Object Flow_BatchDeleteByFlag(String token,String workIDs) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"批量逻辑删除流程实例失败","用户的Token值不能为空");
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        if(DataType.IsNullOrEmpty(workIDs) == true)
            return Return_Info(500,"批量逻辑删除流程实例失败","批删除workIDs值不能为空");
        try{
              String[] strs = workIDs.split("[,]", -1);
        for (String workIDStr : strs)
        {
            if (bp.da.DataType.IsNullOrEmpty(workIDStr) == true)
                continue;
            bp.wf.Dev2Interface.Flow_DoDeleteFlowByFlag(Long.parseLong(workIDStr), "删除", true);
        }
        return Return_Info(200,"删除成功","");
        }catch(Exception e){
            return Return_Info(500,"批量逻辑删除流程实例失败",e.getMessage());
        }
    }

    @PostMapping(value = "/Flow_BatchDeleteByReal")
    @ApiOperation("批量物理删除流程实例:对流程实例在WF_GenerWorkFlow里直接删除,并删除相关的轨迹,工作人员,业务表数据.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "workIDs", value = "流程实例workID集合,多个以,隔开", required = true)
    })
    public final Object Flow_BatchDeleteByReal(String token,String workIDs) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"批量物理删除流程实例失败","用户的Token值不能为空");
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        if(DataType.IsNullOrEmpty(workIDs) == true)
            return Return_Info(500,"批量物理删除流程实例失败","批删除workIDs值不能为空");
        try{
            
              String[] strs = workIDs.split("[,]", -1);
        for (String workIDStr : strs)
        {
            if (bp.da.DataType.IsNullOrEmpty(workIDStr) == true)
                continue;
            bp.wf.Dev2Interface.Flow_DoDeleteFlowByReal(Long.parseLong(workIDStr), true);
        }
        return Return_Info(200,"删除成功","");
        }catch(Exception e){
            return Return_Info(500,"批量物理删除流程实例失败",e.getMessage());
        }
    }

    @PostMapping(value = "/Flow_BatchDeleteByFlagAndUnDone")
    @ApiOperation("批量撤销删除的流程:对已经逻辑删除流程实例在恢复正常状态.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "workIDs", value = "流程实例workID集合:多个以,隔开", required = true)
    })
    public final Object Flow_BatchDeleteByFlagAndUnDone(String token, String  workIDs) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"批量撤销删除的流程失败","用户的Token值不能为空");
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        if(DataType.IsNullOrEmpty(workIDs) == true)
            return Return_Info(500,"批量撤销删除的流程失败","批删除workIDs值不能为空");
        try{
            String[] strs = workIDs.split("[,]", -1);
            for (String workIDStr : strs)
            {
                if (bp.da.DataType.IsNullOrEmpty(workIDStr) == true)
                    continue;
                bp.wf.Dev2Interface.Flow_DoUnDeleteFlowByFlag(null, Integer.parseInt(workIDStr), "删除");
            }
            return Return_Info(200,"恢复成功","");
        }catch(Exception e){
            return Return_Info(500,"批量撤销删除的流程失败",e.getMessage());
        }
    }
    @PostMapping(value = "/Flow_DoUnSend")
    @ApiOperation("撤销流程实例:撤销后当前工作返回自己的待办列表里.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "workIDs", value = "流程实例workID集合:多个以,隔开", required = true)
    })
    public final Object Flow_DoUnSend(String token, String workIDs) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"撤销流程实例失败","用户的Token值不能为空");
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        if(DataType.IsNullOrEmpty(workIDs) == true)
            return Return_Info(500,"撤销流程实例失败","批撤销workIDs值不能为空");
        try{
           
            String[] strs = workIDs.split("[,]", -1);

            String info = "";
            for (String workIDStr : strs)
            {
                if (bp.da.DataType.IsNullOrEmpty(workIDStr) == true)
                {
                    continue;
                }

                info += bp.wf.Dev2Interface.Flow_DoUnSend(null, Long.parseLong(workIDStr), 0, 0);
            }
            return Return_Info(200,"撤销流程实例成功",info);
        }catch(Exception e){
            return Return_Info(500,"批量撤销流程失败",e.getMessage());
        }
    }

    
    @PostMapping(value = "/Flow_DeleteDraft")
    @ApiOperation("删除草稿:从草稿箱里执行批量删除操作,请参考流程属性,草稿规则.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "workIDs", value = "流程实例workID集合:多个以,隔开", required = true)
    })
    public final Object Flow_DeleteDraft(String token, String  workIDs) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"批量删除草稿失败","用户的Token值不能为空");
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        if(DataType.IsNullOrEmpty(workIDs) == true)
            return Return_Info(500,"批量删除草稿失败","批量删除草稿workIDs值不能为空");
        try{
            return Return_Info(200,"批量删除草稿成功", this.Flow_DeleteDraft(workIDs));
        }catch(Exception e){
            return Return_Info(500,"批量删除草稿失败",e.getMessage());
        }
    }
    @PostMapping(value = "/Flow_DoFlowOver")
    @ApiOperation("执行流程结束:对流程注册表WF_GenerWorkFlow的WFState设置为结束状态.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "workIDs", value = "流程实例workID集合:多个以,隔开", required = true)
    })
    public final Object Flow_DoFlowOver(String token, String workIDs) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"批量删除草稿失败","用户的Token值不能为空");
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        try{
            
            String[] strs = workIDs.split("[,]", -1);

            String info = "";
            for (String workIDStr : strs)
            {
                if (bp.da.DataType.IsNullOrEmpty(workIDStr) == true)
                    continue;
                bp.wf.Dev2Interface.Flow_DoFlowOver(Long.parseLong(workIDStr), "批量结束", 1);
            }
            return Return_Info(200,"执行成功","执行成功");
        }catch(Exception e){
            return Return_Info(500,"批量结束流程实例失败",e.getMessage());
        }
    }

    @PostMapping(value = "/Port_LoginOut")
    @ApiOperation("退出登录:清除当前的会话,注销cookies.")
    public final Object Portal_LoginOut() throws Exception {
        try{
            bp.wf.Dev2Interface.Port_SigOut();
            return Return_Info(200,"退出成功","退出成功");
        }catch(Exception e){
            return Return_Info(500,"退出成功失败",e.getMessage());
        }
    }


    @PostMapping(value = "/Node_GetNextStepNodesByNodeID")
    @ApiOperation("获得到达节点的集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "nodeID", value = "节点ID", dataType = "Int", required = true)
    })
    public final Object Node_GetNextStepNodesByNodeID(String token,Integer nodeID) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"获得到达节点的集合失败","用户的Token值不能为空");
        if(DataType.IsNullOrEmpty(nodeID) == true || nodeID == 0)
            return Return_Info(500,"获得到达节点的集合失败","节点编号nodeID不能为空");

        bp.wf.Dev2Interface.Port_LoginByToken(token);
        try{
            //获得可以退回的节点.
            Directions dirs = bp.wf.Dev2Interface.Node_GetNextStepNodesByNodeID(nodeID);
            return Return_Info(200,"获得到达节点的执行成功",dirs.ToJson());
        }catch(Exception e){
            return Return_Info(500,"获得到达节点的集合失败",e.getMessage());
        }
    }
    @PostMapping(value = "/Node_GetNextStepEmpsByNodeID")
    @ApiOperation("获得到指定节点上的工作人员集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "nodeID", value = "节点ID", dataType = "Int", required = true),
            @ApiImplicitParam(name = "workID", value = "流程实例ID,可为0，系统自动查询", dataType = "Int", required = true)
    })
    public final Object Node_GetNextStepEmpsByNodeID(String token,Integer nodeID, Integer workID) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"获得到指定节点上的工作人员集合失败","用户的Token值不能为空");
        if(DataType.IsNullOrEmpty(nodeID) == true || nodeID == 0)
            return Return_Info(500,"获得到指定节点上的工作人员集合失败","节点编号nodeID不能为空");
        if(DataType.IsNullOrEmpty(workID) == true)
            return Return_Info(500,"获得到指定节点上的工作人员集合失败","流程实例workID不能为空，可为0");
        bp.wf.Dev2Interface.Port_LoginByToken(token);
        try{
            //获得可以退回的节点.
            DataTable dt = bp.wf.Dev2Interface.Node_GetNextStepEmpsByNodeID(nodeID,workID);
            return Return_Info(200,"获得到指定节点上的工作人员集合成功", Json.ToJson(dt));
        }catch(Exception e){
            return Return_Info(500,"获得到指定节点上的工作人员集合失败",e.getMessage());
        }
    }
    @PostMapping(value = "/Node_GetNextStepNodesByWorkID")
    @ApiOperation("获得到达节点的集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "workID", value = "流程实例ID", dataType = "Int", required = true)
    })
    public final Object  Node_GetNextStepNodesByWorkID(String token,Integer workID) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"获取可以退回的节点失败","用户的Token值不能为空");
        if(DataType.IsNullOrEmpty(workID) == true)
            return Return_Info(500,"获取可以退回的节点失败","流程实例IDworkID不能为空");

        bp.wf.Dev2Interface.Port_LoginByToken(token);
        try{
            //获得可以退回的节点.
            Directions dirs = bp.wf.Dev2Interface.Node_GetNextStepNodesByWorkID(workID);
            return Return_Info(200,"获取可以退回的节点成功",  dirs.ToJson());
        }catch(Exception e){
            return Return_Info(500,"获取可以退回的节点失败",e.getMessage());
        }
    }

    @PostMapping(value = "/HuiQian_Init")
    @ApiOperation("会签")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workID", value = "流程实例ID", dataType = "Long", required = true)
    })
    public final Object  HuiQian_Init(Long workID) throws Exception {
        if(DataType.IsNullOrEmpty(workID) == true)
            return Return_Info(500,"获取会签初始化数据失败","流程实例workID不能为空");
        try{
            DataSet ds = Dev2Interface.Node_HuiQian_Init(workID);

            return Return_Info(200,"获取会签初始化数据成功",  bp.tools.Json.ToJson(ds));
        }catch(Exception e){
            return Return_Info(500,"获取会签初始化数据失败",e.getMessage());
        }
    }
    @PostMapping(value = "/HuiQian_AddEmps")
    @ApiOperation("增加会签人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workID", value = "流程实例ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "huiQianType", value = "会签类型，huiQianType=AddLeader增加组长,可为空", required = false),
            @ApiImplicitParam(name = "empStrs", value = "选择人员,多人使用,隔开", required = true)
    })
    public final Object  HuiQian_AddEmps(Long workID, String huiQianType, String empStrs) throws Exception {
        if(DataType.IsNullOrEmpty(workID) == true)
            return Return_Info(500,"增加会签人失败","流程实例workID不能为空");
        if (DataType.IsNullOrEmpty(empStrs) == true)
            return Return_Info(500,"增加会签人失败","您没有选择人员");
        try{
            return Return_Info(200,"增加会签人成功",  Dev2Interface.Node_HuiQian_AddEmps(workID,huiQianType, empStrs));
        }catch(Exception e){
            return Return_Info(500,"增加会签人失败",e.getMessage());
        }
    }
    @PostMapping(value = "/HuiQian_DeleteEmps")
    @ApiOperation("删除会签人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workID", value = "流程实例ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "empStrs", value = "选择人员,多人使用,隔开", required = true)
    })
    public final Object  HuiQian_DeleteEmps(Long workID, String empStrs) throws Exception {
        if(DataType.IsNullOrEmpty(workID) == true)
            return Return_Info(500,"删除会签人员失败","流程实例workID不能为空");
        if (DataType.IsNullOrEmpty(empStrs) == true)
            return Return_Info(500,"删除会签人员失败","您没有选择人员");
        try{
            Dev2Interface.Node_HuiQian_Delete(workID, empStrs);
            return Return_Info(200,"删除成功",  "");
        }catch(Exception e){
            return Return_Info(500,"删除会签人员失败",e.getMessage());
        }
    }

    @PostMapping(value = "/WorkFlow_State")
    @ApiOperation("根据WorkID获取流程实例状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "Token", paramType = "query", required = true),
            @ApiImplicitParam(name = "workID", value = "流程实例ID", dataType = "Long", required = true)
    })
    public final Object  WorkFlow_State(String token,Long workID) throws Exception {
        if(DataType.IsNullOrEmpty(token) == true)
            return Return_Info(500,"根据WorkID获取流程实例状态失败","用户的Token值不能为空");
        if(DataType.IsNullOrEmpty(workID) == true)
            return Return_Info(500,"根据WorkID获取流程实例状态失败","流程实例workID不能为空");

        bp.wf.Dev2Interface.Port_LoginByToken(token);
        //判断是否有查看权限
        if (Dev2Interface.Flow_IsCanDoCurrentWork(workID, WebUser.getNo()) == false){
            return Return_Info(500,"根据WorkID获取流程实例状态失败","当前用户没有查看权限");
        }

        GenerWorkFlow gwf = new GenerWorkFlow(workID);
        return Return_Info(200,"获取成功",  gwf.ToJson());

    }
    public String Search_Init(String scop,String key,String dtFrom,String dtTo,Integer pageIdx) throws Exception {
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

        qo.DoQuery("workID", 20, pageIdx);
        //   qo.DoQuery(); // "workID", 20, pageIdx);


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

    /**
     批量设置抄送查看完毕
     @return
     */
    public final String CC_BatchCheckOver(String workIDs) throws Exception
    {
        return bp.wf.Dev2Interface.Node_CC_SetCheckOverBatch(workIDs);
    }


    /**
     获得发起的url.

     */
    public final Object GenerFrmUrl(long workID,String fk_flow,Integer fk_node,String Token) throws Exception
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
            if (workID == 0)
                workID = bp.wf.Dev2Interface.Node_CreateBlankWork(fk_flow, bp.web.WebUser.getNo());

            String url = "";
            bp.wf.Node nd = new bp.wf.Node(nodeID);
            if (nd.getFormType() == NodeFormType.SDKForm || nd.getFormType() == NodeFormType.SelfForm)
            {
                url = nd.getFormUrl();
                if (url.contains("?") == true)
                {
                    url += "&FK_Flow=" + fk_flow + "&FK_Node=" + nodeID + "&workID=" + workID + "&Token=" +Token + "&UserNo=" + bp.web.WebUser.getNo();
                }
                else
                {
                    url += "?FK_Flow=" + fk_flow + "&FK_Node=" + nodeID + "&workID=" + workID + "&Token=" + Token + "&UserNo=" + bp.web.WebUser.getNo();
                }
            }
            else
            {
                url = "/WF/MyFlow.htm?FK_Flow=" + fk_flow + "&FK_Node=" + nodeID + "&workID=" + workID + "&Token=" + Token;
            }
            return Return_Info(200, "获取打开的表单成功", url);
        }
        catch (RuntimeException ex)
        {
            //输出url.
            return "err@" + ex.getMessage();
        }
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
    /*
    删除草稿

     */
    public final String Flow_DeleteDraft(String workIDs)throws Exception
    {
        String[] strs = workIDs.split("[,]", -1);

        String info = "";
        for (String workIDStr : strs)
        {
            if (bp.da.DataType.IsNullOrEmpty(workIDStr) == true)
            {
                continue;
            }

            bp.wf.Dev2Interface.Node_DeleteDraft(Long.parseLong(workIDStr));
        }
        return "删除成功.";
    }
    @Override
    public Class getCtrlType() {
        return null;
    }

    private int DealNodeIDStr(String nodeIDStr, String flowNo)
    {
        int returnToNodeID = 0;
        if(DataType.IsNullOrEmpty(nodeIDStr)==true)
            return 0;
        if (DataType.IsNumStr(nodeIDStr) == true)
            returnToNodeID = Integer.parseInt(nodeIDStr);
        else
            returnToNodeID = DBAccess.RunSQLReturnValInt("SELECT NodeID FROM WF_Node WHERE FK_Flow='" + flowNo + "' AND Mark='" + nodeIDStr + "'");
        return returnToNodeID;
    }
}
