package cn.jflow.controller.app;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import cn.jflow.common.model.AjaxJson;
import cn.jflow.common.model.TempObject;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.ClassFactory;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.FieldType;
import BP.Sys.SystemConfig;
import BP.Tools.FileAccess;
import BP.Tools.StringHelper;

@Controller
@RequestMapping("/App")
public class SaveCommController {
	
	@RequestMapping(value = "/SaveComm", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson saveComm(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		
		try{
			String url = request.getQueryString();
			HashMap<String, String> map = this.getParamsMap(url, "utf-8");
			
			//HashMap<String,BaseWebControl> controls = HtmlUtils.httpParser(object.getFormHtml(), request);
			
			Entity en = ClassFactory.GetEn(this.getEnName(map));
			if (!StringHelper.isNullOrEmpty(this.getPK(map))) {
				en.setPKVal(this.getPK(map));
				en.RetrieveFromDBSources();
			}
			
			en = this.getEnData(en, request);
			en.Save();
			
	    	Attrs attrs = en.getEnMap().getAttrs();
			for (Attr attr : attrs){
				 //#region 查看是否包含 MyFile字段如果有就认为是附件。
	        	 if("MyFileName".equals(attr.getKey())){
	        		 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	        		 CommonsMultipartFile item = (CommonsMultipartFile) multipartRequest.getFile("file");
	        		 //int maxSize = 10 * 1024 * 1024; // 单个上传文件大小的上限
	        		 if (null != item.getOriginalFilename()
	        					&& !"".equals(item.getOriginalFilename())) {// 判断是否选择了文件
	        			 
	        			 long upFileSize = item.getSize(); // 上传文件的大小
	        			 String fileName = item.getOriginalFilename(); // 获取文件名
	        			 
	        			 String path =  request.getSession().getServletContext().getRealPath(File.separator + SystemConfig.getAppSavePath());
	        			 path = path +  File.separator + fileName;
	        			 
	        			 FileUtils.copyInputStreamToFile(item.getInputStream(), new File(path));
	        			 en.SetValByKey("MyFilePath", path);

	 					 en.SetValByKey("MyFileExt",  FileAccess.getExtensionName(fileName).toLowerCase());
	 					 en.SetValByKey("MyFileName", fileName);
	 					 en.SetValByKey("WebPath", SystemConfig.getAppSavePath());
	 					 en.SetValByKey("MyFileSize", upFileSize);
	 					 en.Update();
	        		 }
	        	 }
			}
			
			j.setMsg("保存成功！");
			return j;
		}catch(Exception e){
			j.setSuccess(false);
			j.setMsg("保存失败，"+e.getMessage());
			return j;
		}
		
	}
	
	
	private Entity getEnData(Entity en, HttpServletRequest request) throws Exception{
		try {
			for (Attr attr : en.getEnMap().getAttrs()) {
				if (attr.getMyFieldType() == FieldType.RefText)
					continue;

				if ("MyNum".equals(attr.getKey()))
					continue;

				if (!attr.getUIVisible())
					continue;
				
				switch (attr.getUIContralType()){
               		case TB:
	               		 if (attr.getUIHeight() != 0){//大文本字段
	               			en.SetValByKey(attr.getKey(),
									request.getParameter("TB_" + attr.getKey()));
	               		 }else{
	               			en.SetValByKey(attr.getKey(),
									request.getParameter("TB_" + attr.getKey()));
	               		 }
               			break;
               		case DDL:
               			en.SetValByKey(attr.getKey(),
								request.getParameter("DDL_" + attr.getKey()));
               			break;
               		case CheckBok:
               			String cb = request.getParameter("CB_" + attr.getKey());// 选种了为on 不选中为null
               		    if (null == cb){
               		    	en.SetValByKey(attr.getKey(), 1);
               		    }else{
               		    	en.SetValByKey(attr.getKey(), 0);
               		    }
               			break;
               		default:
                     break;	
				}
			}			
		}catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
		return en;
	}
	
	private String getPK(HashMap<String, String> map){
		String pk = map.get("PK");
		if(null == pk){
			pk = map.get("No");
		}
		if(null == pk){
			pk = map.get("RefNo");
		}
		if(null == pk){
			pk = map.get("OID");
		}
		if(null == pk){
			pk = map.get("MyPK");
		}
		if(null == pk){
			Entity en = ClassFactory.GetEn(map.get("EnName"));
			pk = map.get(en.getPK());
		}
		return pk;
	}
	
	
	private String getEnName(HashMap<String, String> map) throws Exception{
		String enName = map.get("EnName");
		String ensName = map.get("EnsName");
		if (StringHelper.isNullOrEmpty(enName) && StringHelper.isNullOrEmpty(ensName))
             throw new Exception("@缺少参数");
		
	    if (StringHelper.isNullOrEmpty(enName)){
	    	Entities ens = ClassFactory.GetEns(this.getEnsName(map));
	    	enName = ens.getGetNewEntity().toString();
	    }
		return enName;
	}
	
	private String getEnsName(HashMap<String, String> map) throws Exception{
		String enName = map.get("EnName");
		String ensName = map.get("EnsName");
		if (StringHelper.isNullOrEmpty(enName) && StringHelper.isNullOrEmpty(ensName))
			throw new Exception("@缺少参数");
		
		if (StringHelper.isNullOrEmpty(ensName)){
			 Entity en = ClassFactory.GetEn(this.getEnName(map));
			 ensName = en.getGetNewEntities().toString();
		}
		return ensName;
	}
	
	private HashMap<String, String> getParamsMap(String queryString, String enc) {
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		if (queryString != null && queryString.length() > 0) {
			int ampersandIndex, lastAmpersandIndex = 0;
			String subStr, param, value;
			String[] paramPair;
			do {
				ampersandIndex = queryString.indexOf('&', lastAmpersandIndex) + 1;
				if (ampersandIndex > 0) {
					subStr = queryString.substring(lastAmpersandIndex,
							ampersandIndex - 1);
					lastAmpersandIndex = ampersandIndex;
				} else {
					subStr = queryString.substring(lastAmpersandIndex);
				}
				paramPair = subStr.split("=");
				param = paramPair[0];
				value = paramPair.length == 1 ? "" : paramPair[1];
				try {
					value = URLDecoder.decode(value, enc);
				} catch (UnsupportedEncodingException ignored) {
				}
				paramsMap.put(param, value);
			} while (ampersandIndex > 0);
		}
		return paramsMap;
	}
}
