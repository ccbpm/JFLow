package cn.jflow.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.Button;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.NamesOfBtn;
import BP.DA.AtPara;
import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.Paras;
import BP.En.Attr;
import BP.En.ClassFactory;
import BP.En.Entities;
import BP.En.Entity;
import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import BP.Sys.UserRegedit;
import BP.Tools.StringHelper;
import BP.Web.WebUser;

public class SelectMValModel {

	//private String basePath;
	private HttpServletRequest _request = null;
	//private HttpServletResponse _response = null;
	
	public UiFatory Pub1 = null;
	public SelectMValModel(HttpServletRequest request, HttpServletResponse response, String basePath) {
		//this.basePath = basePath; 
		this._request = request;
		//this._response = response;
		
		this.Pub1 = new UiFatory();
	}
	
	public final String getMyPK(){
		return WebUser.getNo() + this.getEnsName() + "_SearchAttrs";
	}
	public final String getAttrKey(){
		if(_request.getParameter("AttrKey")==null)
			return "";
		return _request.getParameter("AttrKey");
	}
	
	public final String getEnName(){
		if(_request.getParameter("EnName")==null)
			return "";
		return _request.getParameter("EnName");
	}
	public final String getEnsName(){
		if(_request.getParameter("EnsName")==null)
			return "";
		return _request.getParameter("EnsName");
    }

	public Entity getHisEn(){
		return this.getHisEns().getGetNewEntity();
	}
	public Entities getHisEns(){
		 Entities ens = ClassFactory.GetEns(this.getEnsName());
		 return ens;
	}
	
	
	public void init(){
		try{
			UserRegedit ur = new UserRegedit();
			ur.setMyPK(this.getMyPK());
			if (ur.RetrieveFromDBSources() == 0){
				ur.setMyPK(this.getMyPK());
				ur.setFK_Emp(WebUser.getNo());
				ur.setCfgKey(this.getEnsName() + "_SearchAttrs");
                ur.Insert();
            }
			
			Entity en = this.getHisEn();
			Attr attr = en.getEnMap().GetAttrByKey(this.getAttrKey());
		    String cfgVal = ur.getMVals();
		    cfgVal= StringHelper.isEmpty(cfgVal, "");
		    
		    AtPara ap = new AtPara(cfgVal);
            cfgVal = ap.GetValStrByKey(this.getAttrKey());
            cfgVal= StringHelper.isEmpty(cfgVal, "");

            if (attr.getIsEnum()){
                this.BindEnum(ur, attr, cfgVal);
            }else if(attr.getIsBindTable()){
            	this.BindTable(ur, attr, cfgVal);
            }else{
            	this.BindEns(ur, attr, cfgVal);
            }
                
			
		}catch(Exception e){
			this.Pub1.append(BaseModel.AddMsgOfWarning("错误", e.getMessage()));
		}
	}
	
	public void BindEnum(UserRegedit ur, Attr attr, String cfgVal){
		 this.Pub1.append(BaseModel.AddTable("width=95%"));
         this.Pub1.append(BaseModel.AddCaptionLeft("请选择其中的一项或者多项"));
         this.Pub1.append(BaseModel.AddTR());
         this.Pub1.append(BaseModel.AddTDTitle("IDX"));

         this.Pub1.append("<TD class=Title>");
         this.Pub1.append("<input type=checkbox text='选择全部' name=checkedAll onclick='SelectAll(this)' >选择全部");
         Button btn = this.Pub1.creatButton("Btn_Save1");
         btn.setText(" OK ");
         btn.setCssClass("Btn");
         btn.addAttr("onclick", "onSave();");
         this.Pub1.append(btn);
         this.Pub1.append("</TD>");

         this.Pub1.append(BaseModel.AddTREnd());
         
         SysEnums ses = new SysEnums(attr.getUIBindKey());
         int idx = 0;
         boolean is1 = false;
         for (SysEnum item : ses.ToJavaList()){
             idx++;
             this.Pub1.append(BaseModel.AddTR(is1));
             is1 = !is1;
             this.Pub1.append(BaseModel.AddTDIdx(idx));

             CheckBox cb = this.Pub1.creatCheckBox("CB_" + item.getIntKey());
             cb.setText(item.getLab());
           
             cb.setChecked(cfgVal.contains(String.valueOf(item.getIntKey())));

             this.Pub1.append("\n<TD nowrap = 'nowrap'>");
       	  	 this.Pub1.append(cb);
       	  	 this.Pub1.append("</TD>");
             this.Pub1.append(BaseModel.AddTREnd());
         }
         this.Pub1.append(BaseModel.AddTableEndWithHR());
		
         btn = this.Pub1.creatButton("Btn_Save");
         btn.setText(" OK ");
         btn.setCssClass("Btn");
         btn.addAttr("onclick", "onSave();");
         this.Pub1.append("&nbsp;&nbsp;&nbsp;");
         this.Pub1.append(btn);
	}
	
	public void BindEns(UserRegedit ur, Attr attr, String cfgVal){
		
		 Entities ens = BP.En.ClassFactory.GetEns(attr.getUIBindKey());
         ens.RetrieveAll();

         this.Pub1.append(BaseModel.AddTable("width='95%'"));
         this.Pub1.append(BaseModel.AddCaptionLeft("请选择其中的一项或者多项"));
         this.Pub1.append(BaseModel.AddTR());
         this.Pub1.append(BaseModel.AddTDTitle("IDX"));

         this.Pub1.append("<TD class=Title>");
         this.Pub1.append("<input type=checkbox text='选择全部' name=checkedAll onclick='SelectAll(this)' >选择全部");
         Button btn = this.Pub1.creatButton("Btn_Save1");
         btn.setText(" OK ");
         btn.setCssClass("Btn");
         btn.addAttr("onclick", "onSave();");
         this.Pub1.append(btn);
         
         this.Pub1.append("</TD>");
         //this.Pub1.AddTDTitle("<input type=checkbox   text='选择全部' name=checkedAll onclick='SelectAll()' >选择全部");
         this.Pub1.append(BaseModel.AddTREnd());
         
         int idx = 0;
         boolean is1 = false;
         for (Entity item : ens.ToJavaListEn()){
             idx++;
             this.Pub1.append(BaseModel.AddTR(is1));
             is1 = !is1;
             this.Pub1.append(BaseModel.AddTDIdx(idx));
             
             CheckBox cb = this.Pub1.creatCheckBox("CB_" + item.GetValByKey(attr.getUIRefKeyValue()));
             cb.setText(item.GetValStrByKey(attr.getUIRefKeyText()));
             cb.setChecked(cfgVal.contains(item.GetValStrByKey(attr.getUIRefKeyValue())));

             this.Pub1.append("\n<TD nowrap = 'nowrap'>");
       	  	 this.Pub1.append(cb);
       	  	 this.Pub1.append("</TD>");
             this.Pub1.append(BaseModel.AddTREnd());
         }
         this.Pub1.append(BaseModel.AddTableEndWithHR());
         
         btn = this.Pub1.creatButton("Btn_Save");
         btn.setText(" OK ");
         btn.setCssClass("Btn");
         btn.addAttr("onclick", "onSave();");
         this.Pub1.append("&nbsp;&nbsp;&nbsp;");
         this.Pub1.append(btn);
	}
	
	public void BindTable(UserRegedit ur, Attr attr, String cfgVal){
		
	
        this.Pub1.append(BaseModel.AddTable("width='95%'"));
        this.Pub1.append(BaseModel.AddCaptionLeft("请选择其中的一项或者多项"));
        this.Pub1.append(BaseModel.AddTR());
        this.Pub1.append(BaseModel.AddTDTitle("IDX"));

        this.Pub1.append("<TD class=Title>");
        this.Pub1.append("<input type=checkbox text='选择全部' name=checkedAll onclick='SelectAll(this)' >选择全部");
        Button btn = this.Pub1.creatButton("Btn_Save1");
        btn.setText(" OK ");
        btn.setCssClass("Btn");
        btn.addAttr("onclick", "onSave();");
        this.Pub1.append(btn);
        
        this.Pub1.append("</TD>");
        //this.Pub1.AddTDTitle("<input type=checkbox   text='选择全部' name=checkedAll onclick='SelectAll()' >选择全部");
        this.Pub1.append(BaseModel.AddTREnd());
        
        Paras ps = new Paras();
		ps.SQL = "select * from " + attr.getUIBindKey();
		DataTable dt = DBAccess.RunSQLReturnTable(ps);
		
		int idx = 0;
	    boolean is1 = false;
    	for(DataRow dr : dt.Rows){
            idx++;
            this.Pub1.append(BaseModel.AddTR(is1));
            is1 = !is1;
            this.Pub1.append(BaseModel.AddTDIdx(idx));
            
            CheckBox cb = this.Pub1.creatCheckBox("CB_" + dr.getValue(attr.getUIRefKeyValue()));
            cb.setText(dr.getValue(attr.getUIRefKeyText()).toString());
            cb.setChecked(cfgVal.contains(dr.getValue(attr.getUIRefKeyValue()).toString()));

            this.Pub1.append("\n<TD nowrap = 'nowrap'>");
      	  	 this.Pub1.append(cb);
      	  	 this.Pub1.append("</TD>");
            this.Pub1.append(BaseModel.AddTREnd());
        }
        this.Pub1.append(BaseModel.AddTableEndWithHR());
        
        btn = this.Pub1.creatButton("Btn_Save");
        btn.setText(" OK ");
        btn.setCssClass("Btn");
        btn.addAttr("onclick", "onSave();");
        this.Pub1.append("&nbsp;&nbsp;&nbsp;");
        this.Pub1.append(btn);
	}
	

	public static void main(String[] args) {
//		DtlModel dot = new DtlModel(null,null,"");
//		CheckBox cb = dot.toolBar.AddCB("checkedAll");
//        cb.addAttr("onclick", "SelectAll(this);");
//        cb.setText("选择全部");
//        System.out.println(((CheckBox)dot.ui.getTmpList().get(0)).getText());
		
		System.out.println(NamesOfBtn.Save.getDesc());
	}
}
