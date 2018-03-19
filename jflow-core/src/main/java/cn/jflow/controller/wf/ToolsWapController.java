package cn.jflow.controller.wf;

import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.util.ArrayList;
import java.util.List;

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

import BP.Port.Emp;
import BP.Sys.Glo;
import BP.WF.Flow;
import BP.WF.Flows;
import BP.Web.WebUser;
import cn.jflow.common.util.ImageUtil;
import cn.jflow.controller.wf.workopt.BaseController;
@Controller
@RequestMapping("/WF")
public class ToolsWapController extends BaseController {
	
	@RequestMapping(value = "/ToolsWap", method = RequestMethod.POST)
	public ModelAndView btn_Profile_Click(String tel,String mail,int way,HttpServletRequest request,
			HttpServletResponse response){
		ModelAndView mv = new ModelAndView();
		BP.WF.Port.WFEmp emp = new BP.WF.Port.WFEmp(WebUser.getNo());
        emp.setTel(tel);
        emp.setEmail(mail);
        emp.setHisAlertWay(BP.WF.Port.AlertWay.forValue(way));
        try {
        	emp.Update();
		} catch (Exception e) {
			e.printStackTrace();
		}
        mv.setViewName("redirect:" + "/WF/ToolsWap.jsp?RefNo=Profile");
        return mv;
	}
	@ResponseBody
	@RequestMapping(value = "/btn_Click", method = RequestMethod.POST)
	public String btn_Click(String p1,String p2,String p3,HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		ModelAndView mv = new ModelAndView();
		Emp emp = new Emp(WebUser.getNo());
        if (emp.getPass().equals(p1))
        {
            emp.setPass(p2);
            emp.Update();
            return "密码修改成功，请牢记新密码。";
        }
        else
        {
        	return "老密码错误，不允许您修改它。";
        }
        //mv.setViewName("redirect:" + "/WF/ToolsWap.jsp?RefNo=Password");
        //return mv;
	}
	@ResponseBody
	@RequestMapping(value = "/btnSaveIt_Click", method = RequestMethod.POST)
	public String btnSaveIt_Click(String FK_Emp,String TB_DT,int DDL_AuthorWay,HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		ModelAndView mv = new ModelAndView();
		BP.WF.Port.WFEmp emp = new BP.WF.Port.WFEmp(WebUser.getNo());
        emp.setAuthorDate(BP.DA.DataType.getCurrentData());
        emp.setAuthor(FK_Emp);
        emp.setAuthorToDate(TB_DT);
        emp.setAuthorWay(DDL_AuthorWay);
        if (emp.getAuthorWay() == 2 && emp.getAuthorFlows().length() < 3)
        {
        	return "您指定授权方式是按指定的流程范围授权，但是您没有指定流程的授权范围.";
        }
        emp.Update();
        //BP.Sys.UserLog.AddLog("Auth", WebUser.getNo(), "全部授权");
        Glo.WriteUserLog("Auth", WebUser.getNo(), "全部授权");
		 mv.setViewName("redirect:" + "/WF/ToolsWap.jsp?RefNo=Per");
		 return "授权成功.";
		//return mv;
		
	}
	@RequestMapping(value = "/btnSaveAthFlows_Click", method = RequestMethod.POST)
	public ModelAndView btnSaveAthFlows_Click(HttpServletRequest request,
			HttpServletResponse response){
		ModelAndView mv = new ModelAndView();
		 Flows fls = new Flows();
         fls.RetrieveAll();
         String strs = "";
         for (Flow fl : fls.ToJavaList())
         {
             String check = request.getParameter("CB_" + fl.getNo());
             if (check == null)
                 continue;
             if (!check.equals("on"))
                 continue;
             strs += "," + fl.getNo();
         }
         BP.WF.Port.WFEmp emp = new BP.WF.Port.WFEmp(WebUser.getNo());
         emp.setAuthorFlows(strs);
         emp.Update();

         Glo.WriteUserLog("Auth", WebUser.getNo(), "授权:" + strs);
         mv.setViewName("redirect:" + "/WF/ToolsWap.jsp?RefNo=AthFlows");
		return mv;
		
	}
	@RequestMapping(value = "/btn_AdminSet_Click", method = RequestMethod.POST)
	public ModelAndView btn_AdminSet_Click(HttpServletRequest request,
			HttpServletResponse response){
		ModelAndView mv = new ModelAndView();
		
		
		mv.setViewName("redirect:" + "/WF/ToolsWap.jsp?RefNo=AdminSet");
		return mv;}
	@RequestMapping(value = "/btn_Siganture_Click", method = RequestMethod.POST)
	public ModelAndView btn_Siganture_Click(HttpServletRequest request,
			HttpServletResponse response, BindException errors)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest
				.getFile("file");

		String name = multipartRequest.getParameter("name");
//		System.out.println("name: " + name);
		// 获得文件名：
		String realFileName = file.getOriginalFilename();
//		System.out.println("获得文件名：" + realFileName);
		// 获取路径
		String ctxPath = request.getSession().getServletContext().getRealPath(
				"/") + "DataUser/Siganture/";
		// 创建文件
		File dirPath = new File(ctxPath);
		if (!dirPath.exists()) {
			dirPath.mkdir();
		}
//		File uploadFile = new File(ctxPath + WebUser.getNo()+".jpg");
		realFileName.replace("\\\\", "\\");
		File uploadFile = new File(ctxPath + WebUser.getNo()+".jpg");
		File uploadFile2 = new File(ctxPath + WebUser.getName()+".jpg");
		System.out.println("上传路径"+ctxPath + realFileName);
		FileCopyUtils.copy(file.getBytes(), uploadFile);
		FileCopyUtils.copy(file.getBytes(), uploadFile2);
		request.setAttribute("files", loadFiles(request));
		mv.setViewName("redirect:" + "/WF/ToolsWap.jsp?RefNo=Per");
		return mv;
		}
	public List<String> loadFiles(HttpServletRequest request) {
		List<String> files = new ArrayList<String>();
		String ctxPath = request.getSession().getServletContext().getRealPath(
				"/")
				+ "DataUser/Siganture/";
		File file = new File(ctxPath);
		if (file.exists()) {
			File[] fs = file.listFiles();
			String fname = null;
			for (File f : fs) {
				fname = f.getName();
				if (f.isFile()) {
					files.add(fname);
				}
			}
		}
		return files;
	}
	
	/**
	 * 保存图片
	 * @param request
	 * @param response
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveImgx", method = RequestMethod.POST)
	public ModelAndView saveImgx(HttpServletRequest request,
			HttpServletResponse response, BindException errors)
			throws Exception {
		int x = Integer.parseInt(request.getParameter("x"));
		int y = Integer.parseInt(request.getParameter("y"));
		int w = Integer.parseInt(request.getParameter("w"));
		int h = Integer.parseInt(request.getParameter("h"));
		int height = Integer.parseInt(request.getParameter("height"));
	    int width = Integer.parseInt(request.getParameter("width"));
	    int sf = height>width?height/510:width/510;
	    if(sf<1)
	    	sf=1;
		ModelAndView mv = new ModelAndView();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest
				.getFile("UpFile");

		String name = multipartRequest.getParameter("name");
//		System.out.println("name: " + name);
		// 获得文件名：
		String realFileName = file.getOriginalFilename();
//		System.out.println("获得文件名：" + realFileName);
		// 获取路径
		String ctxPath = request.getSession().getServletContext().getRealPath(
				"/") + "DataUser/UserIcon/";
		// 创建文件
		File dirPath = new File(ctxPath);
		if (!dirPath.exists()) {
			dirPath.mkdir();
		}
//		File uploadFile = new File(ctxPath + WebUser.getNo()+".jpg");
		File uploadFile = null;
		File uploadFile2 = null;
		String userName = WebUser.getNo();
		uploadFile = new File(ctxPath + WebUser.getNo()+"BigerCon.png");
		uploadFile2 = new File(ctxPath + WebUser.getNo()+"BigerCon.jpg");
		if(realFileName!=null&&!"".equals(realFileName)){
			realFileName.replace("\\\\", "\\");
			System.out.println("上传路径"+ctxPath + realFileName);
			FileCopyUtils.copy(file.getBytes(), uploadFile);
			FileCopyUtils.copy(file.getBytes(), uploadFile2);
//		对原始的图片进行压缩（如果原始图片大于510px）
			if(height>510||width>510){
				new ImageUtil().thumbnailImage(ctxPath + WebUser.getNo()+"SmallerCon.png", width/sf, height/sf,userName);
			}
			
		}
		if(uploadFile!=null&&uploadFile.exists()){
//			剪切图
	        new ImageUtil().cutImage(ctxPath + WebUser.getNo()+"BigerCon.png",ctxPath, userName,x, y, w, h);
//	                     三张缩略
	        new ImageUtil().thumbnailImage(ctxPath + WebUser.getNo()+"SmallerCon.png", 40, 40,userName);
	        new ImageUtil().thumbnailImage(ctxPath + WebUser.getNo()+"SmallerCon.png", 60, 60,userName);
	        new ImageUtil().thumbnailImage(ctxPath + WebUser.getNo()+"SmallerCon.png", 100, 100,userName);
		}
		request.setAttribute("files", loadFiles(request));
		mv.setViewName("redirect:" + "/WF/ToolsWap.jsp?RefNo=PerPng");
		return mv;
		}
}
