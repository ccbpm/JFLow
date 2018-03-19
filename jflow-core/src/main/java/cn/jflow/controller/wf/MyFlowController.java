package cn.jflow.controller.wf;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import BP.DA.DBAccess;
import BP.DA.DataType;
import BP.DA.Paras;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.Entity;
import BP.Sys.FrmEventList;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Sys.PubClass;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.WF.Dev2Interface;
import BP.WF.Flow;
import BP.WF.FlowAppType;
import BP.WF.GenerWorkFlow;
import BP.WF.Glo;
import BP.WF.Node;
import BP.WF.SMSMsgType;
import BP.WF.SaveModel;
import BP.WF.StartWorkAttr;
import BP.WF.WFState;
import BP.WF.Work;
import BP.WF.WorkFlowBuessRole;
import BP.WF.WorkNode;
import BP.WF.Data.GERpt;
import BP.WF.Data.GERptAttr;
import BP.WF.Template.CondModel;
import BP.WF.Template.DraftRole;
import BP.WF.Template.TurnTo;
import BP.WF.Template.TurnTos;
import BP.Web.WebUser;
import cn.jflow.common.model.BaseModel;
import cn.jflow.common.util.ConvertTools;

@Controller
@RequestMapping("/WF/MyFlow")
public class MyFlowController{
	
	private static final long WorkId = 0;
	private static HttpServletRequest _request = null;
	private static HttpServletResponse _response = null;
	
	// 流程编号
	private static String fk_flow;
	// 工作id
	private static String workID;
	// 节点id
	private static String fk_node;
	private static String fid;
	
	/**
	 * 初始化参数
	 * @param request
	 */
	private static void initParameter(){
		// 获取参数
		workID = getParameter("WorkID");
		fk_node = getParameter("FK_Node");
		fk_flow = getParameter("FK_Flow");
		fid = getParameter("FID");
		
	}
	public MyFlowController(){
		
	}
	
	/**
	 * 发送工作
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/SendWork", method = RequestMethod.POST)
	public ModelAndView SendWork(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		_request = request;
		_response = response;
		
		// 初始参数
		initParameter();
		
		try {
			Send(false);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			printResult(toJson("flowMsg", BaseModel.AddMsgOfWarning("错误", e.getMessage())));
		}
		
		return null;
	}

	/**
	 * 保存工作
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/SaveWork", method = RequestMethod.POST)
	public ModelAndView SaveWork(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		_request = request;
		_response = response;
		// 初始参数
		initParameter();
		
		request.getParameterValues("CB_dianlema");
		
		try {
			Send(true);
		} catch (Exception e) {
			e.printStackTrace();
			printResult(toJson("flowMsg", BaseModel.AddMsgOfWarning("错误", e.getMessage())));
		}
		return null;
	}
	
	/**
	 * 发送，保存公共方法
	 * @param isSave
	 * @throws Exception 
	 */
	public static void Send(boolean isSave) throws Exception
	{
		if(StringHelper.isNullOrEmpty(fk_node)){
			fk_node = getParameter("FK_Node");
		}
		
	   	Node currND = new Node(fk_node);
		Work currWK = currND.getHisWork();
		currWK.SetValByKey("OID", workID);
		currWK.Retrieve();
		Flow currFlow=new Flow(fk_flow);
		
		// 判断当前人员是否有执行该人员的权限。
        if (!currND.getIsStartNode() && Dev2Interface.Flow_IsCanDoCurrentWork(fk_flow, Integer.parseInt(fk_node), Long.parseLong(workID), WebUser.getNo()) == false)
        	throw new Exception("您好：" + WebUser.getNo() + "," + WebUser.getName() + "：<br> 当前工作已经被其它人处理，您不能在执行保存或者发送!!!");

        Paras ps = new Paras();
//        String dtStr = SystemConfig.getAppCenterDBVarStr();
        try
        {
            switch (currND.getHisFormType())
            {
                case SelfForm:
                    break;
                case FoolForm:
                case FreeForm:
                case WebOffice:
            		// 绑定数据
            		PubClass.copyFromRequest(currWK, _request);
                    // 设置默认值....
                    MapAttrs mattrs = currND.getMapData().getMapAttrs();
                    for (MapAttr attr : mattrs.ToJavaList())
                    {
                        if (attr.getTBModel() == 2){
                            /* 如果是富文本 */
                            currWK.SetValByKey(attr.getKeyOfEn(), getParameter("TB_" + attr.getKeyOfEn()));
                        }

                        if (attr.getUIIsEnable())
                            continue;
                        if (attr.getDefValReal().contains("@") == false)
                            continue;
                        currWK.SetValByKey(attr.getKeyOfEn(), attr.getDefVal());
                    }
                  //如果有单据编号，让其在每次保存后生成.
                    if (currND.getIsStartNode() && currFlow.getBillNoFormat().length() >2)
                    {
                        String newBillNo= BP.WF.Glo.GenerBillNo(currFlow.getBillNoFormat(), Long.parseLong(workID), currWK, currFlow.getPTable());
                        currWK.SetValByKey("BillNo", newBillNo );
                        //UCEn1.GetTBByID("TB_BillNo").Text = newBillNo;
                    }
                    break;
                case DisableIt:
                    currWK.Retrieve();
                    break;
                default:
                    throw new Exception("@未涉及到的情况。");
            }
        }
        catch (Exception ex)
        {
            //this.Btn_Send.Enabled = true;
        	throw new Exception("@在保存前执行逻辑检查错误。" + ex.getMessage() + " @StackTrace:" + ConvertTools.getStackTraceString(ex.getStackTrace()));
        }
        
        // region 判断特殊的业务逻辑
        String dbStr = SystemConfig.getAppCenterDBVarStr();
        if (currND.getIsStartNode())
        {
            if (currND.getHisFlow().getHisFlowAppType().equals(FlowAppType.PRJ))
            {
                /*对特殊的流程进行检查，检查是否有权限。*/
                String prjNo = currWK.GetValStringByKey("PrjNo");
                ps = new Paras();
                ps.SQL = "SELECT * FROM WF_NodeStation WHERE FK_Station IN ( SELECT FK_Station FROM Prj_EmpPrjStation WHERE FK_Prj=" + dbStr + "FK_Prj AND FK_Emp=" + dbStr + "FK_Emp )  AND  FK_Node=" + dbStr + "FK_Node ";
                ps.Add("FK_Prj", prjNo);
                ps.AddFK_Emp();
                ps.Add("FK_Node", fk_node);

                if (DBAccess.RunSQLReturnTable(ps).Rows.size() == 0)
                {
                    String prjName = currWK.GetValStringByKey("PrjName");
                    ps = new Paras();
                    ps.SQL = "SELECT * FROM Prj_EmpPrj WHERE FK_Prj=" + dbStr + "FK_Prj AND FK_Emp=" + dbStr + "FK_Emp ";
                    ps.Add("FK_Prj", prjNo);
                    ps.AddFK_Emp();
                    //   ps.AddFK_Emp();

                    if (DBAccess.RunSQLReturnTable(ps).Rows.size() == 0){
                    	throw new Exception("您不是(" + prjNo + "," + prjName + ")成员，您不能发起改流程。");
                    }else{
                    	throw new Exception("您属于这个项目(" + prjNo + "," + prjName + ")，但是在此项目下您没有发起改流程的岗位");
                    }
                }
            }
        }
        // #endregion 判断特殊的业务逻辑。
        currWK.SetValByKey("Rec", WebUser.getNo());
        currWK.SetValByKey("FK_Dept", WebUser.getFK_Dept());
        currWK.SetValByKey("FK_NY", BP.DA.DataType.getCurrentYearMonth());

        // 处理节点表单保存事件.
        currND.getMapData().getFrmEvents().DoEventNode(FrmEventList.SaveBefore, currWK);
        try
        {
            if (currND.getIsStartNode()){
            	currWK.setFID(0);
            }

            if (currND.getHisFlow().getIsMD5())
            {
                /*重新更新md5值.*/
                currWK.SetValByKey("MD5", BP.WF.Glo.GenerMD5(currWK));
            }

            if (currND.getIsStartNode() && isSave)
                currWK.SetValByKey(StartWorkAttr.Title, WorkFlowBuessRole.GenerTitle(currND.getHisFlow(), currWK));
            currWK.Update();
            /*如果是保存*/
            //为草稿设置标题.
			if (currND.getIsStartNode() == true) {
				//if (currFlow.getDraftRole() != DraftRole.None) {
				if (isSave) {
					String title = WorkFlowBuessRole.GenerTitle(currFlow, currWK);
					String wfState=String.valueOf(WFState.Draft.getValue());//设置草稿的状态，重载接口
					BP.WF.Dev2Interface.Flow_SetFlowTitle(fk_flow,Long.parseLong(workID), title,wfState);
					if (currFlow.getDraftRole() == DraftRole.SaveToTodolist && isSave == true) //如果是开始节点并且是保存事件
                        BP.WF.Dev2Interface.Node_SaveEmpWorks(fk_flow, title, Long.parseLong(workID), WebUser.getNo());
				}
			}

        }
        catch (Exception ex)
        {
            try
            {
                currWK.CheckPhysicsTable();
            }
            catch (Exception ex1)
            {
            	throw new Exception("@保存错误:" + ex.getMessage() + "@检查物理表错误：" + ex1.getMessage());
            }
            throw new Exception(ex.getMessage() + "@有可能此错误被系统自动修复,请您从新保存一次.");
            //this.Btn_Send.Enabled = true;
            // this.Pub1.AlertMsg_Warning("错误", ex.getMessage() + "@有可能此错误被系统自动修复,请您从新保存一次.");
        }

        // region 处理保存后事件
        boolean isHaveSaveAfter = false;
        try
        {
            //处理表单保存后。
            String s = currND.getMapData().getFrmEvents().DoEventNode(FrmEventList.SaveAfter, currWK);
            if (s != null)
            {
                /*如果不等于null,说明已经执行过数据保存，就让其从数据库里查询一次。*/
                currWK.RetrieveFromDBSources();
                isHaveSaveAfter = true;
            }
        }
        catch (Exception ex)
        {
            //this.Response.Write(ex.Message);
        	printResult(toJson("alert", ex.getMessage().replace("'", "‘")));
            return;
        }
        //endregion

        //region 2012-10-15  数据也要保存到Rpt表里.
        if (currND.getSaveModel().equals(SaveModel.NDAndRpt))
        {
            /* 如果保存模式是节点表与Node与Rpt表. */
            WorkNode wn = new WorkNode(currWK, currND);
            GERpt rptGe = currND.getHisFlow().getHisGERpt();
            rptGe.SetValByKey("OID", workID);
            wn.rptGe = rptGe;
            if (rptGe.RetrieveFromDBSources() == 0)
            {
                rptGe.SetValByKey("OID", workID);
                wn.DoCopyRptWork(currWK);

                rptGe.SetValByKey(GERptAttr.FlowEmps, "@" + WebUser.getNo() + "," + WebUser.getName());
                rptGe.SetValByKey(GERptAttr.FlowStarter, WebUser.getNo());
                rptGe.SetValByKey(GERptAttr.FlowStartRDT, DataType.getCurrentDataTime());
                rptGe.SetValByKey(GERptAttr.WFState, 0);

                rptGe.setWFState(WFState.Draft);

                rptGe.SetValByKey(GERptAttr.FK_NY, DataType.getCurrentYearMonth());
                rptGe.SetValByKey(GERptAttr.FK_Dept, WebUser.getFK_Dept());
                rptGe.Insert();
            }
            else
            {
                wn.DoCopyRptWork(currWK);
                rptGe.Update();
            }
        }
        if (Glo.getIsEnableDraft() && currND.getIsStartNode())
        {
            /*如果启用草稿, 并且是开始节点. */
            BP.WF.Dev2Interface.Node_SaveWork(fk_flow, Integer.parseInt(fk_node), Long.parseLong(workID));
        }

        String msg = "";
        // 调用工作流程，处理节点信息采集后保存后的工作。
        if (isSave)
        {
        	 //return;
            if (isHaveSaveAfter)
            {
                /*如果有保存后事件，就让其重新绑定. */
                currWK.RetrieveFromDBSources();
                //this.UCEn1.ResetEnVal(currWK);
                printResult(toJson(currWK));
                return;
            }
          //处理草稿.
			if (currND.getIsStartNode() == true && !currFlow.getDraftRole().equals(DraftRole.None)) {
				BP.WF.Dev2Interface.Node_SetDraft(fk_flow, currWK.getOID());
			}

            if (StringHelper.isNullOrEmpty(workID))
                return;

            currWK.RetrieveFromDBSources();
            //this.UCEn1.ResetEnVal(currWK);
            printResult(toJson(currWK));
            
            //printResult("<script>window.location.reload();</script>");
            return;
        }
        GenerWorkFlow gwf = new GenerWorkFlow(Long.parseLong(workID));
        //检查是否是退回?
        if (gwf.getWFState() == WFState.ReturnSta && gwf.getParas_IsTrackBack() == false)
        {
            /* 如果是退回 */
        }
        else
        {
        	if (currND.getCondModel().equals(CondModel.ByUserSelected) && currND.getHisToNDNum() > 1)
            {
                //如果是用户选择的方向条件.
                printResult(toJson("url", Glo.getCCFlowAppPath()+"WF/WorkOpt/ToNodes.jsp?FK_Flow=" + fk_flow + "&FK_Node=" + fk_node + "&WorkID=" + workID + "&FID=" + fid));
                return;
            }
        }

        //执行发送.
        WorkNode firstwn = new WorkNode(currWK, currND);
        try
        {
            msg = firstwn.NodeSend().ToMsgOfHtml();
        }
        catch (Exception exSend)
        {
        	String msge = StringHelper.isEmpty(exSend.getMessage(),"");
        	if (msge.contains("请选择下一步骤工作") || msge.contains("用户没有选择发送到的节点"))
            {
                /*如果抛出异常，我们就让其转入选择到达的节点里, 在节点里处理选择人员. */
        		String url = Glo.getCCFlowAppPath()+"WF/WorkOpt/ToNodes.jsp?FK_Flow=" + fk_flow + "&FK_Node=" + fk_node + "&WorkID=" + workID + "&FID=" + fid;
        		printResult(toJson("url", url));
        		return;
            }

        	//绑定独立表单，表单自定义方案验证错误弹出窗口进行提示
			if (currND.getHisFrms() != null && currND.getHisFrms().size() > 0 && msge.contains("在提交前检查到如下必输字段填写不完整") == true) {
				printResult(toJson("alert", msge.replace("@@", "@").replace("@", "<BR>@")));
				return;
			}
			
        	exSend.printStackTrace();
			
			BP.WF.Dev2Interface.Port_SendMsg("admin", currFlow.getName() + "在" + currND.getName() + "节点处，出现错误", msg, 
					"Err" + currND.getNo() + "_" + workID, SMSMsgType.Err, fk_flow, Long.valueOf(fk_node), Long.valueOf(workID), Long.valueOf(fid));
            
        	StringBuffer errorMsg = new StringBuffer();
            errorMsg.append(BaseModel.AddFieldSetGreen("错误"));
            errorMsg.append(msge.replace("@@", "@").replace("@", "<BR>@"));
            if (currND.getCondModel().equals(CondModel.ByUserSelected)){
            	errorMsg.append("<BR>友情提示：发送前请在该节点属性 设置 '接受人按钮标签' 方式为 '发送前打开'");
            }
            errorMsg.append(BaseModel.AddFieldSetEnd());
            
            printResult(toJson("flowMsg", errorMsg.toString()));
            return;
        }

        //#region 处理通用的发送成功后的业务逻辑方法，此方法可能会抛出异常.
        try
        {
            //处理通用的发送成功后的业务逻辑方法，此方法可能会抛出异常.
            BP.WF.Glo.DealBuinessAfterSendWork(fk_flow, Long.parseLong(workID),  getParameter("DoFunc"),  getParameter("WorkIDs"), getParameter("CFlowNo"), 0, null);
        }
        catch (Exception ex)
        {
        	throw new Exception(msg+ex.getMessage());
        }
        //#endregion 处理通用的发送成功后的业务逻辑方法，此方法可能会抛出异常.

        //this.Btn_Send.Enabled = false;
        /*处理转向问题.*/
        switch (firstwn.getHisNode().getHisTurnToDeal())
        {
            case SpecUrl:
                String myurl = firstwn.getHisNode().getTurnToDealDoc();
                if (myurl.contains("&") == false)
                    myurl += "?1=1";
                Attrs myattrs = firstwn.getHisWork().getEnMap().getAttrs();
                Work hisWK = firstwn.getHisWork();
                for (Attr attr : myattrs.ToJavaList())
                {
                    if (myurl.contains("@") == false)
                        break;
                    myurl = myurl.replace("@" + attr.getKey(), hisWK.GetValStrByKey(attr.getKey()));
                }
                if (myurl.contains("@")){
                	throw new Exception("流程设计错误，在节点转向url中参数没有被替换下来。Url:" + myurl);
                }
                
                if (myurl.contains("PWorkID") == false) {
					myurl += "&PWorkID=" + WorkId;
				}


                myurl += "&FromFlow=" + fk_flow + "&FromNode=" + fk_node + "&PWorkID=" + workID + "&UserNo=" + WebUser.getNo() + "&SID=" + WebUser.getSID();
                printResult(toJson("url", myurl));
				return;
                
            case TurnToByCond:
                TurnTos tts = new TurnTos(fk_flow);
                if (tts.size() == 0){
                	BP.WF.Dev2Interface.Port_SendMsg("admin", currFlow.getName() + "在" + currND.getName() + "节点处，出现错误", "您没有设置节点完成后的转向条件。", "Err" + currND.getNo() + "_" + WorkId, SMSMsgType.Err,fk_flow,Long.valueOf(fk_node), Long.valueOf(WorkId), Long.valueOf(fid));
    				
                	throw new Exception("@您没有设置节点完成后的转向条件。");
                }
                for (TurnTo tt : tts.ToJavaList())
                {
                    tt.HisWork = firstwn.getHisWork();
                    if (tt.getIsPassed() == true)
                    {
                        String url = tt.getTurnToURL();
                        if (url.contains("&") == false)
                            url += "?1=1";
                        Attrs attrs = firstwn.getHisWork().getEnMap().getAttrs();
                        Work hisWK1 = firstwn.getHisWork();
                        for (Attr attr : attrs.ToJavaList())
                        {
                            if (url.contains("@") == false)
                                break;
                            url = url.replace("@" + attr.getKey(), hisWK1.GetValStrByKey(attr.getKey()));
                        }
                        if (url.contains("@")){
                        	throw new Exception("流程设计错误，在节点转向url中参数没有被替换下来。Url:" + url);
                        }

                        url += "&PFlowNo=" + fk_flow + "&FromNode=" + fk_node + "&PWorkID=" + workID + "&UserNo=" + WebUser.getNo() + "&SID=" + WebUser.getSID();
                        printResult(toJson("url", url));
                    	return;
                    }
                }

                //#warning 为上海修改了如果找不到路径就让它按系统的信息提示。
                printResult(toJson("url", getToMsg(msg)));
                //throw new Exception("您定义的转向条件不成立，没有出口。");
                break;
            default:
            	 printResult(toJson("url", getToMsg(msg)));
                break;
        }
        return;
	}
	
	/**
	 * 输出Alert
	 * @param response
	 * @param msg
	 * @throws IOException
	 */
	public static void printResult(String result){
		_response.setContentType("text/html; charset=utf-8");
		PrintWriter out = null;
		try {
			out = _response.getWriter();
			out.write(result);
			
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(out != null){
				out.close();
			}
		}
	}
	
	/**
	 * 重定向到myflowInfo.jsp
	 * @param msg
	 */
	public static final String getToMsg(String msg)
	{
		_request.getSession().setAttribute("info", msg.trim());
		try {
			BP.WF.Glo.setSessionMsg(msg);
			return BP.WF.Glo.getCCFlowAppPath()+"WF/MyFlowInfo.jsp?FK_Flow=" + fk_flow + "&FK_Node=" + fk_node + "&WorkID=" + workID + "&FID=" + fid+ "&FK_Emp=" +WebUser.getNo() + "&SID=" + WebUser.getSID();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 获取vlaue
	 * @param key
	 * @return
	 */
	private static String getParameter(String key){
		String value = _request.getParameter(key);
		if (StringHelper.isNullOrEmpty(value)) {
			value = "";
		}
		return value;
	}
	
	/**
	 * 处理返回值
	 * @param type
	 * @param action
	 * @return
	 */
	private static String toJson (String type, String action){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", type);
		jsonObject.put("action", action);
		
		return jsonObject.toString();
	}
	
	/**
	 * 保存返回值
	 * @param en
	 * @return
	 */
	public static String toJson(Entity en){
		// 控件id:控件值
		HashMap<String, String> map = new HashMap<String, String>();
		// 获取数据
		Attrs attrs = en.getEnMap().getAttrs();
		String ctlid = "";
		String value = "";
		for (Attr attr : attrs) {
			switch (attr.getUIContralType()) {
			case TB:
				ctlid = "TB_" + attr.getKeyLowerCase();
				break;
			case DDL:
				ctlid = "DDL_" + attr.getKeyLowerCase();
				break;
			case CheckBok:
				ctlid = "CB_" + attr.getKeyLowerCase();
				break;
			default:
				break;
			}
			value = en.GetValStrByKey(attr.getKey());
			
			map.put(ctlid, value);
		}
		
		// 转换json
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", "data");
		jsonObject.put("rows", JSONObject.fromObject(map));
		
		return jsonObject.toString();
	}
	
}