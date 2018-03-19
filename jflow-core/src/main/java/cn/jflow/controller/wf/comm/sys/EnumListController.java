package cn.jflow.controller.wf.comm.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import BP.Sys.SysEnum;
import BP.Sys.SysEnumAttr;
import BP.Sys.SysEnumMain;
import BP.Sys.SysEnums;
import BP.Tools.StringHelper;
import cn.jflow.controller.wf.workopt.BaseController;
@Controller
@RequestMapping("/WF/comm/enumList")
public class EnumListController extends BaseController {
	
	
	@RequestMapping(value = "/btn_Click", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView btn_Click(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView mv=new ModelAndView();
		String TB_Name=request.getParameter("TB_Name");
		//原有个数
		SysEnums souceSes = new SysEnums();
		souceSes.Retrieve(SysEnumAttr.EnumKey, this.getRefNo(), SysEnumAttr.IntKey);

		SysEnums ses = new SysEnums();
		for (int i = 0; i < souceSes.size() + 10; i++) {
			String tb = request.getParameter("TB_" + i);
			if (tb == null) {
				continue;
			}
			if (StringHelper.isNullOrEmpty(tb)) {
				continue;
			}

			SysEnum se = new SysEnum();
			se.setIntKey(i);
			se.setLab(tb);
			se.setLang (BP.Web.WebUser.getSysLang());
			se.setEnumKey(this.getRefNo());
			se.setMyPK (se.getEnumKey() + "_" + se.getLang() + "_" + se.getIntKey());
			ses.AddEntity(se);
		}

		if (ses.size() == 0) {
			//this.Alert("枚举项目不能为空.");
			mv.addObject("info", "枚举项目不能为空.");
			mv.setViewName("redirect:" + "/WF/Comm/Sys/EnumList.jsp?RefNo=" + this.getRefNo() + "&T=" + new java.util.Date());
			return mv;
		}

		ses.Delete(SysEnumAttr.EnumKey, this.getRefNo());

		String lab = "";
		for (int n=0;n<ses.size();n++) {
			SysEnum se = (SysEnum) ses.get(n);
			se.Save();
			lab += "@" + se.getIntKey() + "=" + se.getLab();
		}
		SysEnumMain main = new SysEnumMain(this.getRefNo());
		main.setName(TB_Name);
		main.setCfgVal(lab);
		main.Update();
		mv.addObject("info", "保存成功.");
		mv.setViewName("redirect:" + "/WF/Comm/Sys/EnumList.jsp?T=" + new java.util.Date());
		return mv;
		//this.Alert("保存成功.");
	}


	@RequestMapping(value = "/btn_New_Click", method = RequestMethod.POST)
	@ResponseBody
	private ModelAndView btn_New_Click(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView mv=new ModelAndView();
		String no = request.getParameter("TB_No");
		String name = request.getParameter("TB_Name");
		SysEnumMain m = new SysEnumMain();
		m.setNo(no);
		if (m.RetrieveFromDBSources() == 1) {
			//this.Alert("枚举编号:" + m.getNo() + " 已经被:" + m.getName() + "占用");
			mv.addObject("info", "枚举编号:" + m.getNo() + " 已经被:" + m.getName() + "占用");
			mv.setViewName("redirect:" + "/WF/Comm/Sys/EnumList.jsp?DoType=New&T=" + new java.util.Date());
			return mv;
		}
		m.setName(name);
		if (StringHelper.isNullOrEmpty(name)) {
			//this.Alert("枚举名称不能为空");
			mv.addObject("info", "枚举名称不能为空.");
			mv.setViewName("redirect:" + "/WF/Comm/Sys/EnumList.jsp?DoType=New&T=" + new java.util.Date());
			return mv;
		}

		SysEnums ses = new SysEnums();
		for (int i = 0; i < 20; i++) {
			String tb = request.getParameter("TB_" + i);
			if (tb == null) {
				continue;
			}
			if (StringHelper.isNullOrEmpty(tb)) {
				continue;
			}

			SysEnum se = new SysEnum();
			se.setIntKey(i);
			se.setLab(tb);
			se.setLang(BP.Web.WebUser.getSysLang());
			se.setEnumKey(m.getNo());
			se.setMyPK(se.getEnumKey() + "_" + se.getLang() + "_" + se.getIntKey());
			ses.AddEntity(se);
		}

		if (ses.size() == 0) {
			//this.Alert("枚举项目不能为空.");
			mv.addObject("info", "枚举项目不能为空.");
			mv.setViewName("redirect:" + "/WF/Comm/Sys/EnumList.jsp?DoType=New&T=" + new java.util.Date());
			return mv;
		}

		String lab = "";
		//for (SysEnum se : ses) {
		for (int i=0;i<ses.size();i++) {
			SysEnum se = (SysEnum) ses.get(i);
			se.Save();
			lab += "@" + se.getIntKey() + "=" + se.getLab();
		}

		m.setLang (BP.Web.WebUser.getSysLang());
		m.setCfgVal(lab);
		m.Insert();
		mv.addObject("info", "保存成功.");
		mv.setViewName("redirect:" + "/WF/Comm/Sys/EnumList.jsp?T=" + new java.util.Date());
		return mv;
		//this.Response.Redirect("EnumList.aspx?RefNo=" + m.No + "&T=" + new java.util.Date().ToString("yyyyMMddHHmmssfff"), true);
	}

}
