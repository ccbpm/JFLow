package cn.jflow.controller.wf;

import java.io.File;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.jflow.common.model.AjaxJson;
import cn.jflow.common.model.TempObject;
import BP.En.FieldTypeS;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrs;
import BP.Tools.FileAccess;
import BP.Tools.StringHelper;
import BP.WF.Dev2Interface;
import BP.WF.Flow;
import BP.Web.WebUser;

@Controller
@RequestMapping("/WF")
public class BatchStartController {


	@RequestMapping(value = "/BatchStartSend", method = RequestMethod.POST)
	private ModelAndView batchStartSend(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();

		int RowNum = object.getRowNum(); 
		Flow fl = new Flow(object.getFK_Flow());
		
		MapAttrs attrs = new MapAttrs("ND" + Integer.parseInt(object.getFK_Flow() + "01"));
		String[] strs = fl.getBatchStartFields().split(",");
		//String workIDs = "";
		String infos = "";
		try{
			for (int i = 1; i <= RowNum; i++){
				Object cb = request.getParameter("CB_IDX_" + i);// 选种了为on 不选中为null
				if (null == cb)
					continue;
				
				long workid = Dev2Interface.Node_CreateBlankWork(object.getFK_Flow(), null, null, WebUser.getNo(), null);
				Hashtable<String, Object> ht = new Hashtable<String, Object>();
				// #region 给属性赋值.
				boolean isChange = false;
				for (String str : strs) {
					if (StringHelper.isNullOrEmpty(str))
						continue;
					for (MapAttr attr : attrs.ToJavaList()) {
						if (!str.equals(attr.getKeyOfEn()))
							continue;
	
						if (attr.getLGType() == FieldTypeS.Normal) {
							Object obj = request.getParameter("TB_"
									+ attr.getKeyOfEn() + "_" + i);
							if (null != obj) {
								if (!attr.getDefVal().equals(obj.toString()))
									isChange = true;
								ht.put(str, obj.toString());
								continue;
							}
							
							obj = request.getParameter("CB_" + attr.getKeyOfEn()
									+ "_" + i);// 选种了为on 不选中为null
							if (null != obj) {// 选种
								if (!attr.getDefValOfBool())
									isChange = true;
								ht.put(str, 1);
							} else {// 不选中
								if (attr.getDefValOfBool())
									isChange = true;
								ht.put(str, 0);
							}
						}else{
							Object obj = request.getParameter("DDL_"
									+ attr.getKeyOfEn() + "_" + i);
							if (null != obj && !"".equals(obj.toString())) {
								if (!attr.getDefVal().equals(obj.toString()))
									isChange = true;
								if (attr.getLGType() == FieldTypeS.Enum)
									ht.put(str, Integer.parseInt(obj.toString()));
								else
									ht.put(str, obj.toString());
							}
						}
					}
				}
				// #endregion 给属性赋值.
				
				//#region 开始发起流程.
	            if (!isChange)
	            	continue;
	            
	            String info = Dev2Interface.Node_SendWork(object.getFK_Flow(), workid, ht).ToMsgOfHtml();
	            
	            infos += "@第 (" + i + ") 条工作启动成功。<br>";
	            infos += info;
	            infos += "<hr>";
//	            infos += "<br><fieldset width='100%' ><legend>&nbsp;&nbsp;第 (" + i + ") 条工作启动成功&nbsp;</legend>";
//	            infos += info;
//	            infos += "</fieldset>";
				
	            //#endregion 开始发起流程.
			}
		}catch(Exception e){
			infos = "批量发起失败："+e.getMessage();
		}
		
		mv.addObject("normMsg",infos);
		mv.addObject("DoType","Send");
		//mv.setViewName("redirect:" + "/WF/BatchStart.jsp");
		//mv.setViewName("forward:" + "/WF/Batch.jsp");
		mv.setViewName("BatchStart");
		return mv;
	}
	
	@RequestMapping(value = "/BatchStartUpload", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson batchStartUpload(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {

		AjaxJson j = new AjaxJson();
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile item = (CommonsMultipartFile) multipartRequest.getFile("fileupload");
		
		int maxSize = 10 * 1024 * 1024; // 单个上传文件大小的上限
		if ( null != item.getOriginalFilename()
				&& !"".equals(item.getOriginalFilename())) {// 判断是否选择了文件
			
			long upFileSize = item.getSize(); // 上传文件的大小
			String fileName = item.getOriginalFilename(); // 获取文件名
			
			String ext = FileAccess.getExtensionName(fileName).toLowerCase();
			if(!ext.equals("xlsx") && !ext.equals("xls")){
				j.setSuccess(false);
				j.setMsg("上传文件失败，只能上传Excel文件！");
				return j;
			}
			
			if(upFileSize > maxSize){
				j.setSuccess(false);
				j.setMsg("上传文件失败，文件大于10M！");
				return j;
			}
			
			String path =  request.getSession().getServletContext().getRealPath("/DataUser/Temp/");
			path = path + File.separator + object.getFK_Flow() + request.getSession().getId() + ".xls";
			
			try{
				FileUtils.copyInputStreamToFile(item.getInputStream(), new File(path));
				j.setMsg("上传文件成功！");
				return j;
			}catch(Exception e){
				j.setSuccess(false);
				j.setMsg("上传文件失败，"+e.getMessage());
				return j;
			}
		}else{
			j.setSuccess(false);
			j.setMsg("上传文件失败，请选择xls或者xlsx文件！");
			return j;
		}
	}
}