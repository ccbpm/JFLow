package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.En.ClassFactory;
import BP.En.Entities;
import BP.En.Entity;

public class SysMapEnModel extends BaseModel {

	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	private String EnsName;
	
	private String PK;
	
	public SysMapEnModel(HttpServletRequest request,
			HttpServletResponse response,String EnsName,String PK) {
		super(request, response);
		this.request=request;
		this.response=response;
		this.EnsName=EnsName;
		this.PK=PK;
	}

	public void init(){
		Entities ens = ClassFactory.GetEns(this.EnsName);
        Entity en = null;//ens.GetNewEntity;
        en.setPKVal(PK);
        en.RetrieveFromDBSources();

//      this.SysMapEnUC1.BindColumn4(en, this.EnsName);
//        this.Title = en.EnDesc;
	}
	
}
