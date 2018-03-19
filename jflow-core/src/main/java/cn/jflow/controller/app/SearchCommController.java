package cn.jflow.controller.app;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.jflow.common.app.CommModel;
import cn.jflow.common.model.AjaxJson;
import cn.jflow.common.model.TempObject;
import BP.DA.DataType;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.ClassFactory;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.FieldType;
import BP.En.QueryObject;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;

@Controller
@RequestMapping("/App")
public class SearchCommController {
	
	
	private Entity HisEn;
	private Entities HisEns;
	private StringBuilder UCSys1;
	private StringBuilder UCSys2;
	private final int PageSize = SystemConfig.getPageSize();

	@RequestMapping(value = "/SearchComm", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson search(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {

		AjaxJson j = new AjaxJson();
		HashMap<String, Object> UCSys = new HashMap<String, Object>();
		
		this.UCSys1 = new StringBuilder();
        this.UCSys2 = new StringBuilder();
		try{
			String url = request.getQueryString();
			HashMap<String, String> map = this.getParamsMap(url, "utf-8");
			
			//HashMap<String,BaseWebControl> controls = HtmlUtils.httpParser(object.getFormHtml(), request);
			
			this.HisEns = ClassFactory.GetEns(this.getEnsName(map));
			this.HisEn = this.HisEns.getGetNewEntity();
			QueryObject qo = CommModel.GetnQueryObject(this.HisEns, this.HisEn, request);
			
			//#region 生成翻页
	        this.UCSys2.setLength(0);
	        try{
	        	 //BaseModel.BindPageIdx_ver1(this.UCSys2, qo.GetCount(), this.PageSize, this.getPageIdx(), this.basePath+"WF/Comm/RefFunc/Dtl.jsp?EnName=" + this.getEnName() + "&PK=" + this.getRefVal() + "&EnsName=" + this.getEnsName() + "&RefVal=" + this.getRefVal() + "&RefKey=" + this.getRefKey() + "&MainEnsName=" + this.getMainEnsName());
	        	CommModel.BindPageIdxEasyUi(this.UCSys2, qo.GetCount(), this.getPageIdx(map), this.PageSize,  "'first','prev','sep','manual','sep','next','last'", false);
	        	 
	            qo.DoQuery(this.HisEn.getPK(), this.PageSize, this.getPageIdx(map), false);
	        }catch(Exception e){
	        	e.printStackTrace();
	        	this.HisEn.CheckPhysicsTable();
	             //this.Response.Redirect("Ens.aspx?EnsName=" + this.EnsName + "&RefPKVal=" + this.RefPKVal, true);
	        }
	        // #endregion 生成翻页
			
	        //查询值标红
	        if (this.HisEn.getEnMap().IsShowSearchKey){
	            String keyVal = StringHelper.isEmpty(request.getParameter("TB_Key"), "").trim();
	            if (keyVal.length() >= 1){
	                Attrs attrs = this.HisEn.getEnMap().getAttrs();
	                for (Entity myen : this.HisEns.ToJavaListEn()){
	                    for(Attr attr : attrs){
	                        if (attr.getIsFKorEnum())
	                            continue;

	                        /*if (attr.getIsPK())
	                            continue;*/
	                        
	                        if (attr.getMyFieldType() == FieldType.RefText)
	                            continue;

	                        if ("FK_Dept".equals(attr.getKey()))
	                            continue;
	                        
	                        switch (attr.getMyDataType()){
	                            case DataType.AppRate:
	                            case DataType.AppMoney:
	                            case DataType.AppInt:
	                            case DataType.AppFloat:
	                            case DataType.AppDouble:
	                            case DataType.AppBoolean:
	                            case DataType.AppDate:
	                            case DataType.AppDateTime:
	                                continue;
	                            default:
	                                break;
	                        }
	                        myen.SetValByKey(attr.getKey(), myen.GetValStrByKey(attr.getKey()).replace(keyVal, "<font color=red>" + keyVal + "</font>"));
	                    }
	                }
	            }
	        }
			
	        CommModel.DataPanelDtl(UCSys1, this.HisEns, this.getPageIdx(map));
			
	        //保存查询状态
	        //DemoModel.SaveSearchState(this.getEnName(map), null, request);
            
	        UCSys.put("UCSys1", UCSys1.toString());
	        UCSys.put("UCSys2", UCSys2.toString());
	        j.setAttributes(UCSys);
	        return j;
		}catch(Exception e){
			j.setSuccess(false);
            j.setMsg("查询错误："+e.getMessage());
            return j;
		}
	}
	
	private int getPageIdx(HashMap<String, String> map){
		String PageIdx = map.get("PageIdx");
		if(null == PageIdx || "".equals(PageIdx)){
			return 1;
		}
		return Integer.parseInt(PageIdx);
	}
	
	private String getEnName(HashMap<String, String> map) throws Exception{
		String enName = map.get("EnName");
		String ensName = map.get("EnsName");
		if (StringHelper.isNullOrEmpty(enName) && StringHelper.isNullOrEmpty(ensName))
             throw new Exception("@缺少参数");
		
	    if (StringHelper.isNullOrEmpty(enName)){
	    	Entities ens = ClassFactory.GetEns(this.getEnsName(map));
	    	enName = ens.getGetNewEntity().getClass().getName();
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
			 ensName = en.getGetNewEntities().getClass().getName();
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
