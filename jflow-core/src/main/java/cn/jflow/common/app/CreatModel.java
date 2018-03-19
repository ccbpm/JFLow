package cn.jflow.common.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.NamesOfBtn;
import cn.jflow.system.ui.core.ToolBar;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.ClassFactory;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.UAC;
import BP.Tools.StringHelper;

public class CreatModel {

	//private String basePath;
	private HttpServletRequest _request = null;
	private HttpServletResponse _response = null;
	
	public UiFatory ui = null;
	public UiFatory UCSys = null;
	public ToolBar toolBar = null;
	public CreatModel(HttpServletRequest request, HttpServletResponse response, String basePath) {
		//this.basePath = basePath; 
		this._request = request;
		this._response = response;
		
		this.ui = new UiFatory();
		this.UCSys = new UiFatory();
		this.toolBar = new ToolBar(this._request, this._response, this.ui);
	}
	
	public String getParameter(String key){
		String value = _request.getParameter(key);
		if(StringHelper.isNullOrEmpty(value))
			return "";
		return value;
	}
	
	public final String getPK() throws Exception{
		String pk = this.getParameter("PK");
		if(StringHelper.isNullOrEmpty(pk)){
			pk = this.getParameter("No");
		}
		if(StringHelper.isNullOrEmpty(pk)){
			pk = this.getParameter("RefNo");
		}
		if(StringHelper.isNullOrEmpty(pk)){
			pk = this.getParameter("OID");
		}
		if(StringHelper.isNullOrEmpty(pk)){
			pk = this.getParameter("MyPK");
		}
		if(StringHelper.isNullOrEmpty(pk)){
			Entity en = ClassFactory.GetEn(this.getEnName());
			pk = this.getParameter(en.getPK());
		}
		return pk;
	}
	
	public final String getEnName() throws Exception{
		String enName = getParameter("EnName");
		String ensName = getParameter("EnsName");
		if (StringHelper.isNullOrEmpty(enName) && StringHelper.isNullOrEmpty(ensName))
             throw new Exception("@缺少参数");
		
	    if (StringHelper.isNullOrEmpty(enName)){
	    	Entities ens = ClassFactory.GetEns(this.getEnsName());
	    	enName = ens.getGetNewEntity().getClass().getName();
	    }
		return enName;
	}
	
	public final String getEnsName() throws Exception{
		String enName = getParameter("EnName");
		String ensName = getParameter("EnsName");
		if (StringHelper.isNullOrEmpty(enName) && StringHelper.isNullOrEmpty(ensName))
			throw new Exception("@缺少参数");
		
		if (StringHelper.isNullOrEmpty(ensName)){
			 Entity en = ClassFactory.GetEn(this.getEnName());
			 ensName = en.getGetNewEntities().getClass().getName();
		}
		return ensName;
	}
	
	private Entity getCurrEn(Entity en) throws Exception{
		String pk = this.getPK();
		if (en.getPKCount() == 1){//只有一个主键
			  if (!StringHelper.isNullOrEmpty(pk)){
	                en.setPKVal(pk);
	          }else{
	        	  return en;
	          }
			  if (!en.getIsExits()){//根据主键查询不到记录
				  return null;
			  }else{
				  en.RetrieveFromDBSources();
				  return en;
			  }
		}else if(en.getIsMIDEntity()){//MID主键
			 String val = this.getParameter("MID");
			 if (StringHelper.isNullOrEmpty(val)){
				 val = pk;
			 }
			 if (StringHelper.isNullOrEmpty(pk)){
				 return en;
			 }else{
				 en.SetValByKey("MID", val);
	             en.RetrieveFromDBSources();
	             return en;
			 }
		}else{
			Attrs myattrs = en.getEnMap().getAttrs();
			for(Attr attr:myattrs){//填充 HisEn
				if (attr.getIsPK()){
					 String str = getParameter(attr.getKey());
					 if (StringHelper.isNullOrEmpty(str)){
						 if (en.getIsMIDEntity()){
	                        en.SetValByKey("MID", pk);
	                        continue;
		                 }else {
		                	 en.SetValByKey(attr.getKey(), pk);
		                	 continue;
		                 }
		             }
	            }
	            if (StringHelper.isNullOrEmpty(this.getParameter(attr.getKey())))
	                continue;
	            en.SetValByKey(attr.getKey(), this.getParameter(attr.getKey()));
		    }
			if (!en.getIsExits()){
	        	return null;
	        }else{
	            en.RetrieveFromDBSources();
	           return en;
	        }
		}
	}
	
	private Entity HisEn;
	private Entities HisEns;
	private boolean isReadOnly;
	public void init(){
		try{
			this.HisEns = ClassFactory.GetEns(this.getEnsName());
			this.HisEn = this.getCurrEn(this.HisEns.getGetNewEntity());
			if(null == this.HisEn){
				this.UCSys.append(BaseModel.AddMsgOfWarning("错误", "记录不存在，或者没有保存！"));
				return;
			}
			
			UAC uac = this.HisEn.getHisUAC();
			if (!uac.IsView){
				this.UCSys.append(BaseModel.AddMsgOfWarning("错误", "对不起，您没有查看的权限！"));
				return;
			}

			this.isReadOnly = !uac.IsUpdate;  //是否有修改的权限．
			
//			if (this.getEnsName().contains("BP.WF.Template") == true)
//                BindV2(this.HisEn, this.HisEn.toString(), this.isReadOnly, true);
//          else
            NewModel.Bind(this.HisEn, this.HisEn.toString(), this.getEnsName(), this.isReadOnly, true, this.UCSys);
			
            NewModel.InitFuncEn(uac, this.HisEn, toolBar);
            if (this.toolBar.IsExit(NamesOfBtn.New))
    			this.toolBar.GetLinkBtnByID(NamesOfBtn.New).setHref("onNew();");	
    		
    		if (this.toolBar.IsExit(NamesOfBtn.Save))
    		    this.toolBar.GetLinkBtnByID(NamesOfBtn.Save).setHref("onSave();");	
    		
    		if (this.toolBar.IsExit(NamesOfBtn.SaveAndClose))
    			this.toolBar.GetLinkBtnByID(NamesOfBtn.SaveAndClose).setHref("onSaveOrClose();");
    		
    		if (this.toolBar.IsExit(NamesOfBtn.SaveAndNew))
    			this.toolBar.GetLinkBtnByID(NamesOfBtn.SaveAndNew).setHref("onSaveAndNew();");	
    		
    		if (this.toolBar.IsExit(NamesOfBtn.Delete))
    		    this.toolBar.GetLinkBtnByID(NamesOfBtn.Delete).setHref("onDelete();");	
			
		}catch(Exception e){
			this.UCSys.append(BaseModel.AddMsgOfWarning("错误", e.getMessage()));
		}
	}
	
	

	
	
}
