package cn.jflow.common.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.Label;
import cn.jflow.system.ui.core.ToolBar;
import BP.En.ClassFactory;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.Map;
import BP.Tools.StringHelper;

public class SearchCommModel {

	private String basePath;
	private HttpServletRequest _request = null;
	private HttpServletResponse _response = null;
	
	public UiFatory ui = null;
	public Label label = null;
	public ToolBar toolBar = null;
	public SearchCommModel(HttpServletRequest request, HttpServletResponse response, String basePath) {
		this.basePath = basePath; 
		this._request = request;
		this._response = response;
		
		this.ui = new UiFatory();
		this.label = new Label();
		this.toolBar = new ToolBar(this._request, this._response, this.ui);
	}
	
	public String getParameter(String key){
		String value = _request.getParameter(key);
		if(StringHelper.isNullOrEmpty(value))
			return "";
		return value;
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
	
	private Entity HisEn;
	private Entities HisEns;
	public void init(){
		try{
			this.HisEns = ClassFactory.GetEns(this.getEnsName());
			this.HisEn = this.HisEns.getGetNewEntity();
			
			//构建标题
			label.setId("Label");
		    label.setText(BaseModel.GenerCaption(this.HisEn.getEnMap().getEnDesc() + "" + StringHelper.isEmpty(this.HisEn.getEnMap().TitleExt, "")));
			
			Map map = this.HisEn.getEnMap();//获取当前实体类的所有属性map
			//开始构造toolbar
			CommModel.InitByMap(map, 1, this.getEnsName(), this.ui, this.toolBar);
			
			/*boolean isEdit = this.HisEn.getHisUAC().IsInsert;
			if(isEdit){
				this.toolBar.AddLab("new",
						"<input type=button id='Btn_New' class='Btn' name='Btn_New' onclick=\"javascript:ShowEn('"+this.basePath+"WF/App/Comm/Creat.jsp?EnsName=" + this.getEnsName() + "','cd','" + EnsAppCfgs.GetValInt(getEnsName(), "WinCardH") + "' , '" + EnsAppCfgs.GetValInt(getEnsName(), "WinCardW") + "');\"  value='新建(N)'  />");
				this.toolBar.AddLab("set", 
						"<input type=button class='Btn'  id='Btn_Option' name='Btn_Option' onclick=\"javascript:OpenAttrs('" + this.getEnsName() + "');\" value='设置(P)'/>");
			}*/
			
		}catch(Exception e){
			this.ui.append(BaseModel.AddMsgOfWarning("错误", e.getMessage()));
		}
	}
}
