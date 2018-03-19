package cn.jflow.controller.wf.admin.AttrFlow;

import java.io.File;
import java.io.IOException;
import java.net.BindException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import BP.DA.Log;
import cn.jflow.common.model.BaseModel;
import cn.jflow.controller.wf.workopt.BaseController;
@Controller
@RequestMapping("/WF/AttrFlow/imp")
public class ImpController extends BaseController {

	@ResponseBody
	@RequestMapping(value = "/import_file", method = RequestMethod.POST)
	public ModelAndView import_file(HttpServletRequest request,HttpServletResponse response, BindException errors) throws IOException {
		ModelAndView mv=new ModelAndView();
		String import_code=request.getParameter("type");
		String specifiedNumberStr=request.getParameter("SpecifiedNumber");
		String FK_Flow=request.getParameter("FK_Flow");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");
		String realFileName = file.getOriginalFilename();
		// 获取路径
		String ctxPath = request.getSession().getServletContext().getRealPath("/")+ "DataUser/FlowFile/";
		// 判断文件夹是否存在，不存在则创建
		File dirPath = new File(ctxPath);
		if (!dirPath.exists()) {
			dirPath.mkdir();
		}
		File uploadFile = new File(ctxPath + realFileName);
		FileCopyUtils.copy(file.getBytes(), uploadFile);
		
		BP.WF.Flow flow = new BP.WF.Flow();
		if (FK_Flow!=null && !"".equals(FK_Flow) && !"null".equals(FK_Flow) ) {
			flow = new BP.WF.Flow(FK_Flow);
		}

		int SpecifiedNumber = 0;
		BP.WF.ImpFlowTempleteModel model = BP.WF.ImpFlowTempleteModel.AsNewFlow;
		//作为新流程导入(由ccbpm自动生成新的流程编号)
		if("Import_1".equals(import_code)){
			model = BP.WF.ImpFlowTempleteModel.AsNewFlow;
		}
		//作为新流程导入(使用流程模版里面的流程编号，如果该编号已经存在系统则会提示错误)
		else if("Import_2".equals(import_code)){
			model = BP.WF.ImpFlowTempleteModel.AsTempleteFlowNo;
		}
		//作为新流程导入(使用流程模版里面的流程编号，如果该编号已经存在系统则会覆盖此流程)
		else if("Import_3".equals(import_code)){
			model = BP.WF.ImpFlowTempleteModel.OvrewaiteCurrFlowNo;
		}
		
		else if("Import_4".equals(import_code)){
			model = BP.WF.ImpFlowTempleteModel.OvrewaiteCurrFlowNo;
			if("".equals(specifiedNumberStr)){
				BaseModel.Alert("@请输入指定流程编号。");
			}else{
				SpecifiedNumber = Integer.parseInt(specifiedNumberStr);
				model = BP.WF.ImpFlowTempleteModel.AsSpecFlowNo;
			}
		}
		
		//执行导入
		try {
			flow = BP.WF.Flow.DoLoadFlowTemplate(flow.getFK_FlowSort(), ctxPath + realFileName, model);
		} catch (Exception e) {
			Log.DebugWriteError("ImpController import_file() "+e);
		}
		if (!"".equals(flow.getNo())) {
			 mv.addObject("success", "导入成功!");
	         mv.setViewName("redirect:" + "/WF/Admin/AttrFlow/imp.jsp?FK_FlowSort="+FK_Flow+"&Lang=CH&y=t");
		}
		else {
			 mv.addObject("success", "导入失败!");
			 mv.setViewName("redirect:" + "/WF/Admin/AttrFlow/imp.jsp?FK_FlowSort="+FK_Flow+"&Lang=CH&y=f");
		}
		return  mv;
	}

	
}
