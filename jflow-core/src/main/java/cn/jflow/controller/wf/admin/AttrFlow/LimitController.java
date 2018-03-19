package cn.jflow.controller.wf.admin.AttrFlow;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.WF.Flow;
import BP.WF.StartLimitRole;
import cn.jflow.common.model.AjaxJson;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/Limit")
@Scope("request")
public class LimitController extends BaseController {
	/*
	 * public String getFK_Flow() { return
	 * ContextHolderUtils.getRequest().getParameter("FK_Flow"); }
	 */
	@ResponseBody
	@RequestMapping(value = "/PageLoad", method = RequestMethod.POST)
	public String Page_Load(HttpServletRequest request,
			HttpServletResponse response,String FK_Flow) {
			String flowNo = FK_Flow;
			Flow fl = new Flow();
			fl.setNo(flowNo);
			fl.RetrieveFromDBSources();
			
			AjaxJson j = new AjaxJson();
			
			String TB_Alert = fl.getStartLimitAlert();// 限制规则提示.
			String TB_ByTimePara = "",DDL_ByTime = "",TB_ColNotExit_Fields = "",TB_SQL_Para = "";
			Boolean RB_None = false,RB_OnlyOneSubFlow = false,RB_ColNotExit = false,RB_ByTime = false,RB_SQL = false;
			int DDL_SQL = 9;
			switch (fl.getStartLimitRole()){
			case None: // 不限制.
				RB_None = true;
				break;
			case Day: // 天.
				RB_ByTime = true;
				DDL_ByTime = "0";
				TB_Alert= fl.getStartLimitAlert();
				TB_ByTimePara = fl.getStartLimitPara();
				break;
			case Week: // 周.
				RB_ByTime = true;
				DDL_ByTime = "1";
				TB_Alert=fl.getStartLimitAlert();
				TB_ByTimePara=fl.getStartLimitPara();
				break;
			case Month: // 月份.
				RB_ByTime = true;
				DDL_ByTime = "2";
				TB_Alert=fl.getStartLimitAlert();
				TB_ByTimePara=fl.getStartLimitPara();
				break;
			case JD: // 月份.
				RB_ByTime = true;
				DDL_ByTime = "3";
				TB_Alert=fl.getStartLimitAlert();
				TB_ByTimePara=fl.getStartLimitPara();
				break;
			case Year: // 年度.
				RB_ByTime = true;
				DDL_ByTime = "4";
				TB_Alert=fl.getStartLimitAlert();
				TB_ByTimePara=fl.getStartLimitAlert();
				break;
				
			case OnlyOneSubFlow: // 为子流程时仅仅只能被调用1此.
                RB_OnlyOneSubFlow = true;
                break;
                
			case ColNotExit: // 发起的列不能重复,(多个列可以用逗号分开).
				RB_ColNotExit= true;
				TB_ColNotExit_Fields=fl.getStartLimitPara();
				break;
			case ResultIsZero: // 小于等于0.
				RB_SQL = true;
				DDL_SQL = 0;
				TB_SQL_Para=fl.getStartLimitPara();
				break;
			case ResultIsNotZero: // 大于 0.
				RB_SQL = true;
				DDL_SQL = 1;
				TB_SQL_Para=fl.getStartLimitPara();
				break;
			default:
				break;
			}
			return "{\"TB_Alert\":\""+TB_Alert+"\",\"TB_ByTimePara\":\""+TB_ByTimePara+"\",\"DDL_ByTime\":\""+DDL_ByTime+
					"\",\"TB_ColNotExit_Fields\":\""+TB_ColNotExit_Fields+"\",\"TB_SQL_Para\":\""+TB_SQL_Para+
					"\",\"RB_None\":\""+RB_None+"\",\"RB_ColNotExit\":\""+RB_ColNotExit+"\",\"RB_ByTime\":\""+RB_ByTime+
					"\",\"RB_SQL\":\""+RB_SQL+"\",\"DDL_SQL\":\""+DDL_SQL+"\"}";
	}

	/*
	 * protected final void Btn_Save_Click(Object sender, EventArgs e) { Save();
	 * 
	 * }
	 */
	@RequestMapping(value = "/BtnSaveClick", method = RequestMethod.POST)
	public String Btn_Save_Click(HttpServletRequest request,
			HttpServletResponse response,String FK_Flow,String TB_Alert,
			String xz,String DDL_ByTime,String TB_ByTimePara,String TB_ColNotExit_Fields,
			String TB_SQL_Para,String DDL_SQL) {
		String flowNo = FK_Flow;
		Flow fl = new Flow(flowNo);

		fl.setStartLimitAlert(TB_Alert); // 限制提示信息

		if ("RB_None".equals(xz)) {
			fl.setStartLimitRole(StartLimitRole.None);
		}

		if ("RB_ByTime".equals(xz)) {
			if (DDL_ByTime.endsWith("0")) // 一人一天一次
			{
				fl.setStartLimitRole(StartLimitRole.Day);
				fl.setStartLimitPara(TB_ByTimePara);
			}

			if (DDL_ByTime.endsWith("1")) // 一人一周一次
			{
				fl.setStartLimitRole(StartLimitRole.Week);
				fl.setStartLimitPara(TB_ByTimePara);
			}

			if (DDL_ByTime.endsWith("2")) // 一人一月一次
			{
				fl.setStartLimitRole(StartLimitRole.Month);
				fl.setStartLimitPara(TB_ByTimePara);
			}

			if (DDL_ByTime.endsWith("3")) // 一人一季一次
			{
				fl.setStartLimitRole(StartLimitRole.JD);
				fl.setStartLimitPara(TB_ByTimePara);
				fl.DirectUpdate();
			}

			if (DDL_ByTime.endsWith("4")) // 一人一年一次
			{
				fl.setStartLimitRole(StartLimitRole.Year);
				fl.setStartLimitPara(TB_ByTimePara);
			}
		}

		if ("RB_ColNotExit".equals(xz)) // 按照发起字段不能重复规则
		{
			fl.setStartLimitRole(StartLimitRole.ColNotExit);
			fl.setStartLimitPara(TB_ColNotExit_Fields);
		}

		if ("RB_SQL".equals(xz)) {
			// 字段参数.
			fl.setStartLimitPara(TB_SQL_Para);

			// 选择的模式.
			if ("0".equals(DDL_SQL)) {
				fl.setStartLimitRole(StartLimitRole.ResultIsZero);
			}

			if ("1".equals(DDL_SQL)) {
				fl.setStartLimitRole(StartLimitRole.ResultIsNotZero);
			}
		}

		int res = fl.Update();
		if(res<0){
			return "{\"msg\":\"保存失败\"}";
		}
		return "{\"msg\":\"保存成功\"}";
	}
}