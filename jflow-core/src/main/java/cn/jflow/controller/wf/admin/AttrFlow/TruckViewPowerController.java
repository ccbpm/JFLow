package cn.jflow.controller.wf.admin.AttrFlow;

import java.io.IOException;
import java.io.PrintWriter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import BP.DA.Paras;
import BP.Tools.StringHelper;
import BP.WF.Template.TruckViewPower;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/TruckViewPower")
@Scope("request")
public class TruckViewPowerController extends BaseController {
	@ResponseBody
	@RequestMapping(value = "/BtnSaveClick", method = RequestMethod.POST)
	protected void Btn_Save_Click(String BBM,String ZSSJ,String SJ,String PJ,
			String ZDBM,String ZDGW,String ZDQXZ,String ZDRY,
			String PSpecDeptExt,String PSpecStaExt,String PSpecGroupExt,String PSpecEmpExt)
			throws Exception {
		TruckViewPower en = new TruckViewPower(getFK_Flow());
		if (StringHelper.isNullOrEmpty(getFK_Flow())) {
			throw new Exception("@流程编号为空");
		} else {
				if (StringHelper.isNullOrEmpty(BBM)==false) {
					en.setPMyDept(true);
				} else {
					en.setPMyDept(false);
				}

				if (StringHelper.isNullOrEmpty(ZSSJ)==false) {
					en.setPPMyDept(true);
				} else {
					en.setPPMyDept(false);
				}
				if (StringHelper.isNullOrEmpty(SJ)==false) {
					en.setPPDept(true);
				} else {
					en.setPPDept(false);
				}
				if (StringHelper.isNullOrEmpty(PJ)==false) {
					en.setPSameDept(true);
				} else {
					en.setPSameDept(false);
				}
				if (StringHelper.isNullOrEmpty(ZDBM)==false) {
					en.setPSpecDept(true);
				} else {
					en.setPSpecDept(false);
				}
				if (StringHelper.isNullOrEmpty(PSpecDeptExt)==false) {
					en.setPSpecDeptExt(PSpecDeptExt);
				} else {
					en.setPSpecDeptExt("");
				}
				if (StringHelper.isNullOrEmpty(ZDGW)==false) {
					en.setPSpecSta(true);
				} else {
					en.setPSpecSta(false);
				}
				if (StringHelper.isNullOrEmpty(PSpecStaExt)==false) {
					en.setPSpecStaExt(PSpecStaExt);
				} else {
					en.setPSpecStaExt("");
				}
				if (StringHelper.isNullOrEmpty(ZDQXZ)==false) {
					en.setPSpecGroup(true);

				} else {
					en.setPSpecGroup(false);
				}
				if (StringHelper.isNullOrEmpty(PSpecGroupExt)==false) {
					en.setPSpecGroupExt(PSpecGroupExt);
				} else {
					en.setPSpecGroupExt("");
				}
				if (StringHelper.isNullOrEmpty(ZDRY)==false) {
					en.setPSpecEmp(true);
				} else {
					en.setPSpecEmp(false);
				}

				if (StringHelper.isNullOrEmpty(PSpecEmpExt)==false) {
					en.setPSpecEmpExt(PSpecEmpExt);
				} else {
					en.setPSpecEmpExt("");
				}
				en.Update();
			}
		}
	@RequestMapping(value = "/Init", method = RequestMethod.POST)
	@ResponseBody
    public ModelAndView Init() throws IOException
    {
		ModelAndView mv=new ModelAndView();
		Paras ps = new Paras();
		ps.SQL = "SELECT PMyDept,PPMyDept,PPDept,PSameDept,PSpecDept,PSpecSta,PSpecGroup,PSpecEmp,PSpecDeptExt,PSpecStaExt,PSpecGroupExt,PSpecEmpExt"
				+ " FROM  WF_Flow WHERE NO="+ Integer.parseInt(getFK_Flow());
		   PrintWriter out = getResponse().getWriter();
			String message = BP.Tools.Json.ToJson(BP.DA.DBAccess.RunSQLReturnTable(ps));
			out.write(message);
            mv.setViewName("Admin/AttrFlow/TruckViewPower"); 
            out.flush();
            out.close(); 
            return mv;              
    }		
}