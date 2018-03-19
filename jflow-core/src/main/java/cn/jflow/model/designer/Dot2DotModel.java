package cn.jflow.model.designer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.En.Attr;
import BP.En.AttrOfOneVSM;
import BP.En.ClassFactory;
import BP.En.EnType;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.FieldType;
import BP.En.Map;
import BP.En.QueryObject;
import BP.En.UAC;
import BP.Sys.SysEnum;
import BP.Sys.SysEnums;
import BP.Tools.StringHelper;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.LinkButton;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.ToolBar;

public class Dot2DotModel {

	//private String basePath;
	private HttpServletRequest _request = null;
	private HttpServletResponse _response = null;
	
	private boolean IsLine = false;
	private String MainEnName;
	private String MainEnPKVal;
	private String MainEnPK;
	private Entity MainEn;
	
	public UiFatory ui = null;
	public ToolBar toolBar = null;
	public UiFatory UCSys = null;
	public Dot2DotModel(HttpServletRequest request, HttpServletResponse response, String basePath) {
		//this.basePath = basePath; 
		this._request = request;
		this._response = response;
		
		this.ui = new UiFatory();
		this.UCSys = new UiFatory();
		this.toolBar = new ToolBar(this._request, this._response, this.ui);
	}
	
	public AttrOfOneVSM getAttrOfOneVSM() throws Exception{
		 Entity en = ClassFactory.GetEn(this.getEnName());
		 for(AttrOfOneVSM attr : en.getEnMap().getAttrsOfOneVSM()){
			 //System.out.println(attr.getEnsOfMM().getClass().getName());
			if ((attr.getEnsOfMM().getClass().getName()).equals(this.getAttrKey())){
                return attr;
            }
		 }
		 throw new Exception("@没有找到对应属性");
	}
	
	public String getParameter(String key){
		String value = _request.getParameter(key);
		if(StringHelper.isNullOrEmpty(value))
			return "";
		return value;
	}
	public final String getEnName(){
		return getParameter("EnName");
	}
	public final String getEnsName(){
		return getParameter("EnsName");
    }
	public final String getAttrKey(){
		return getParameter("AttrKey");
	}
	public final String getPK() throws Exception{
		String pk = this._request.getParameter("PK");
		if(StringHelper.isNullOrEmpty(pk)){
			pk = this._request.getParameter("No");
		}
		if(StringHelper.isNullOrEmpty(pk)){
			pk = this._request.getParameter("RefNo");
		}
		if(StringHelper.isNullOrEmpty(pk)){
			pk = this._request.getParameter("OID");
		}
		if(StringHelper.isNullOrEmpty(pk)){
			pk = this._request.getParameter("MyPK");
		}
		if(StringHelper.isNullOrEmpty(pk)){
			Entity en = ClassFactory.GetEn(this.getEnName());
			pk = this._request.getParameter(en.getPK());
		}
		return pk;
	}
	public final DDL getDDL_Group(){
		return (DDL) this.toolBar.GetDDLByKey("DDL_Group");
	}
	public final boolean getIsTreeShowWay(){
		
		if(!getParameter("IsTreeShowWay").equals(""))
			return true;
		return false;
	}
	public final String getShowWay(){
		String showWay = getParameter("ShowWay");
		if(showWay.equals(""))
			return this.getDDL_Group().getSelectedItemStringVal();
		return showWay;
	}
	
	public int ErrMyNum = 0;
	public final LinkButton getBtn_Save(){
		return (LinkButton) this.toolBar.GetLinkBtnByID("Btn_Save");
	}
	public final LinkButton getBtn_SaveAndClose(){
		return (LinkButton) this.toolBar.GetLinkBtnByID("Btn_SaveAndClose");
	}
	
	public void init(){
		try{
			 //#region 处理可能来自于 父实体 的业务逻辑。
             Entity enP = ClassFactory.GetEn(this.getEnName());
             this.setMainEnName(enP.getEnDesc());
             this.setMainEnPKVal(this.getPK());
			 this.setMainEnPK(enP.getPK());
			 if (enP.getEnMap().getEnType() != EnType.View){
				 try {
                     enP.SetValByKey(enP.getPK(), this.getPK());// =this.PK;
                     enP.Retrieve(); //查询。
                     enP.Update(); // 执行更新，处理写在 父实体 的业务逻辑。
                 }catch(Exception ee){}
			 }
			 this.setMainEn(enP);
			 //#endregion
			 
			 AttrOfOneVSM ensattr = this.getAttrOfOneVSM();
			 this.toolBar.AddLab("lab_desc", "分组:");	
			 DDL ddl = this.toolBar.AddDDL("DDL_Group");
			 ddl.Items.clear();
			 ddl.addAttr("onchange", "onChange()");
			 //ddl.SelectedIndexChanged += new EventHandler(DDL_Group_SelectedIndexChanged);
			 Entity open = ensattr.getEnsOfM().getGetNewEntity();
			 Map map = open.getEnMap();
	         int len = 19;
			  
	         // 如果最长的 标题  〉 15 长度。就用一行显示。
             if (len > 20)
                this.setIsLine(true);
             else
                this.setIsLine(false);           
             
             if("BP.WF.Template.NodeEmps".equals(getAttrKey())){
            	// 默认是“部门”
                 this.getDDL_Group().Items.add(new ListItem("部门", "FK_Dept"));
             }else{
            	// 默认是“无”
                 this.getDDL_Group().Items.add(new ListItem("无", "None"));
             }
             // 先加入enum 类型。
             for (Attr attr : map.getAttrs()){
            	 /* map */
                 if (!attr.getIsFKorEnum())
                     continue;
                 //this.getDDL_Group().Items.add(new ListItem(attr.getDesc(), attr.getKey()));
                 this.getDDL_Group().Items.add(new ListItem("无", "None"));
             }
             for (ListItem li : ddl.Items){
                 if (li.getValue().equals(this.getShowWay()))
                     li.setSelected(true);
             }
             this.toolBar.AddSpt("spt");
             CheckBox cb = this.toolBar.AddCB("checkedAll");
             cb.addAttr("onclick", "SelectAll(this);");
             cb.setText("选择全部");
             //this.DDL_Group.SelectedIndexChanged += new EventHandler(DDL_Group_SelectedIndexChanged);
             
             //#region 处理保存权限.
             UAC uac = ensattr.getEnsOfMM().getGetNewEntity().getHisUAC();//权限控制
             if (uac.IsInsert || uac.IsUpdate){
            	 this.toolBar.AddSpace(4);
                 this.toolBar.AddLinkBtn("Btn_Save", "保存");
                 try{
                     this.toolBar.GetLinkBtnByID("Btn_Save").setHref("onSave()");
                     //this.ToolBar1.GetLinkBtnByID("Btn_SaveAndClose").Click += new EventHandler(BPToolBar1_ButtonClick);
                 }catch (Exception ee){}
             }
             //#endregion 处理保存权限.
             
             this.SetDataV2();
		}catch(Exception e){
			this.UCSys.append(BaseModel.AddMsgOfWarning("错误", e.getMessage()));
		}
	}
	
	public void SetDataV2() throws Exception{
		
		 if(this.UCSys.getTmpList().size()>0)this.UCSys.getTmpList().clear();
		 AttrOfOneVSM attrOM = this.getAttrOfOneVSM();
         Entities ensOfM = attrOM.getEnsOfM();
         //if (ensOfM.Count == 0)
         ensOfM.RetrieveAll();
         
         try{
        	 Entities ensOfMM = attrOM.getEnsOfMM();
             QueryObject qo = new QueryObject(ensOfMM);
             qo.AddWhere(attrOM.getAttrOfOneInMM(), this.getPK());
             qo.DoQuery();
        	 
             if ("None".equals(this.getDDL_Group().getSelectedItemStringVal())){
            	 if (this.IsLine){
            		 this.UIEn1ToM_OneLine(ensOfM, attrOM.getAttrOfMValue(), attrOM.getAttrOfMText(), ensOfMM, attrOM.getAttrOfMInMM());
            	 }else{
            		 this.UIEn1ToM(ensOfM, attrOM.getAttrOfMValue(), attrOM.getAttrOfMText(), ensOfMM, attrOM.getAttrOfMInMM());
            	 }
             }else{
            	  if (this.IsLine){
            		  this.UIEn1ToMGroupKey_Line(ensOfM, attrOM.getAttrOfMValue(), attrOM.getAttrOfMText(), ensOfMM, attrOM.getAttrOfMInMM(), this.getDDL_Group().getSelectedItemStringVal());
            	  }else{
            		  this.UIEn1ToMGroupKey(ensOfM, attrOM.getAttrOfMValue(), attrOM.getAttrOfMText(), ensOfMM, attrOM.getAttrOfMInMM(), this.getDDL_Group().getSelectedItemStringVal());
            	  }
             }
         }catch (Exception e){
        	  if(this.UCSys.getTmpList().size()>0)this.UCSys.getTmpList().clear();
        	  try{
                  ensOfM.getGetNewEntity().CheckPhysicsTable();
              }catch (Exception ee){
            	  this.UCSys.append(BaseModel.AddMsgOfWarning("错误", ee.getMessage()));
              }
        	  
        	  ErrMyNum++;
              if (ErrMyNum > 3){
            	  this.UCSys.append(BaseModel.AddMsgOfWarning("错误", e.getMessage()));
            	  return;
              }
              this.SetDataV2();
         }
	}
	
	public void UIEn1ToM_OneLine(Entities ens, String showVal, String showText, Entities selectedEns, String selecteVal){
		 this.UCSys.append("<table border=0 width='500px'>");
		 boolean is1 = false;
		 for (Entity en : ens.ToJavaListEn()){
			 this.UCSys.append(BaseModel.AddTR(is1));
             is1 = !is1;
			 
             CheckBox cb = this.ui.creatCheckBox("CB_" + en.GetValStrByKey(showVal));
             cb.setText(en.GetValStringByKey(showText));
             
             this.UCSys.append("\n<TD nowrap = 'nowrap'>");
             this.UCSys.append(cb);
             this.UCSys.append("</TD>");
             //this.UCSys.append(BaseModel.AddTD(cb));
             this.UCSys.append(BaseModel.AddTREnd());
		 }
		 this.UCSys.append(BaseModel.AddTableEnd());
		 
		  // 设置选择的 ens .
         for (Entity en : selectedEns.ToJavaListEn()){
        	 String key = en.GetValStrByKey(selecteVal);
        	 CheckBox bp = (CheckBox)this.ui.GetUIByID("CB_" + key);
        	 bp.setChecked(true);
         }
	}
	
	public void UIEn1ToM(Entities ens, String showVal, String showText, Entities selectedEns, String selecteVal){
		 this.UCSys.append(BaseModel.AddTable("class='Table' cellSpacing='1' cellPadding='1'  border='1' style='width:100%'"));
		  int i = 0;
          boolean is1 = false;
          for (Entity en : ens.ToJavaListEn()){
        	  i++;
              if (i == 4)
                  i = 1;

              if (i == 1){
            	  this.UCSys.append(BaseModel.AddTR(is1));
                  is1 = !is1;
              }
              
              CheckBox cb = this.ui.creatCheckBox("CB_" + en.GetValStringByKey(showVal));
              cb.setText(en.GetValStringByKey(showText));
              
              this.UCSys.append("\n<TD>");
              this.UCSys.append(cb);
              this.UCSys.append("</TD>");
              if (i == 3)this.UCSys.append(BaseModel.AddTREnd());
          }
          
          switch (i){
	          case 1:
	        	  this.UCSys.append(BaseModel.AddTD());
	        	  this.UCSys.append(BaseModel.AddTD());//"<TD>&nbsp;</TD>");
	        	  this.UCSys.append(BaseModel.AddTREnd());//("</TR>");
	              break;
	          case 2:
	        	  this.UCSys.append(BaseModel.AddTD());
	        	  this.UCSys.append(BaseModel.AddTREnd());
	              break;
	          default:
	              break;
          }
          this.UCSys.append(BaseModel.AddTableEnd());
          
          // 设置选择的 ens .
          for (Entity en : selectedEns.ToJavaListEn()){
              String key = en.GetValStringByKey(selecteVal);
              try{
            	  CheckBox bp = (CheckBox)this.ui.GetUIByID("CB_" + key);
             	  bp.setChecked(true);
              }catch(Exception e){}
          }
	}
	
	 public void UIEn1ToMGroupKey_Line(Entities ens, String showVal, String showText, Entities selectedEns, String selecteVal, String groupKey){
		 this.UCSys.append(BaseModel.AddTable("<TABLE class='Table' cellSpacing='1' cellPadding='1'  border='1' width='100%' >"));
		 
		 Attr attr = ens.getGetNewEntity().getEnMap().GetAttrByKey(groupKey);
		 String val = null;
         Entity seEn = null;
         if (attr.getMyFieldType() == FieldType.Enum || attr.getMyFieldType() == FieldType.PKEnum){ // 检查是否是 enum 类型。
        	 SysEnums eens = new SysEnums(attr.getKey());
        	 for (SysEnum se : eens.ToJavaList()){
        		 this.UCSys.append(BaseModel.AddTR());
        		 this.UCSys.append("<TD class='GroupTitle' >" + se.getLab() + "</TD>");
        		 this.UCSys.append(BaseModel.AddTREnd());
        		 for (Entity en : ens.ToJavaListEn()){
        			 if (en.GetValIntByKey(attr.getKey()) != se.getIntKey())
                         continue;

        			 this.UCSys.append(BaseModel.AddTR());
        			 val = en.GetValStrByKey(showVal);
        			 CheckBox cb = this.ui.creatCheckBox("CB_" + val + "_" + se.getIntKey());//edited by liuxc,2015.1.6
        			 
        			 seEn = selectedEns.GetEntityByKey(selecteVal, val);
        			  if (seEn != null)
                          cb.setChecked(true);
        			 
        			  cb.setText(en.GetValStrByKey(showText));
        			  this.UCSys.append("\n<TD nowrap = 'nowrap'>");
                      this.UCSys.append(cb);
                      this.UCSys.append("</TD>");
        			  //this.UCSys.append(BaseModel.AddTD(cb));
        			  this.UCSys.append(BaseModel.AddTREnd());
        		 }
        	 }
         }else{
        	 Entities groupEns = ClassFactory.GetEns(attr.getUIBindKey());
        	 groupEns.RetrieveAll();
             String gVal = null;
             for (Entity group : groupEns.ToJavaListEn()){
            	 gVal = group.GetValStringByKey(attr.getUIRefKeyValue());
            	 this.UCSys.append("<TR>");
            	 this.UCSys.append("<TD class='GroupTitle' >" + group.GetValStringByKey(attr.getUIRefKeyText()) + "</TD>");
            	 this.UCSys.append(BaseModel.AddTREnd());
            	 
            	 for (Entity en : ens.ToJavaListEn()){
            		    if (!(en.GetValStringByKey(attr.getKey())).equals(gVal))
                            continue;
            		    
            		    this.UCSys.append("<TR>");
            		    val = en.GetValStringByKey(showVal);
            		    CheckBox cb = this.ui.creatCheckBox("CB_" + val + "_" + gVal); //edited by liuxc,2015.1.6
            		    cb.setText(en.GetValStringByKey(showText));
            		    
            		    seEn = selectedEns.GetEntityByKey(selecteVal, val);
            		    if (seEn != null)
                            cb.setChecked(true);
            		    
            		    this.UCSys.append("<TD nowrap = 'nowrap'>");
            		    this.UCSys.append(cb);
            		    this.UCSys.append("</TD>");
            		    this.UCSys.append(BaseModel.AddTREnd());
            	 }
             }
         }
         this.UCSys.append(BaseModel.AddTableEnd());
	 }
	
	 public void UIEn1ToMGroupKey(Entities ens, String showVal, String showText, Entities selectedEns, String selecteVal, String groupKey){
		 this.UCSys.append("<TABLE class='Table' cellSpacing='1' cellPadding='1'  border='1' style='width:100%'>");
		 
		  String val = null;
          Entity seEn = null;
          Attr attr = ens.getGetNewEntity().getEnMap().GetAttrByKey(groupKey);
          if (attr.getMyFieldType() == FieldType.Enum || attr.getMyFieldType() == FieldType.PKEnum){ // 检查是否是 enum 类型
        	  
        	  SysEnums eens = new SysEnums(attr.getKey());
        	  for (SysEnum se : eens.ToJavaList()){
        		  this.UCSys.append("<TR>");
        		  this.UCSys.append("<TD class='GroupTitle' colspan=3 >");
        		  
        		  CheckBox cb1 = this.ui.creatCheckBox("CB_SE_" + se.getIntKey());
        		  cb1.setText(se.getLab());
        		  this.UCSys.append(cb1);
      		      this.UCSys.append("</TD>");
      		      this.UCSys.append(BaseModel.AddTREnd());
      		      
      		      int i = 0;
                  boolean is1 = false;
                  String ctlIDs = "";
                  for (Entity en : ens.ToJavaListEn()){
                	  if (en.GetValIntByKey(attr.getKey()) != se.getIntKey())
                          continue;
                	
                      i++;
                      if (i == 4)
                          i = 1;
                      if (i == 1){
                    	 this.UCSys.append(BaseModel.AddTR(is1));
                         is1 = !is1;
                      }
                    
                      val = en.GetValStringByKey(showVal);
                      CheckBox cb = this.ui.creatCheckBox("CB_" + val + "_" + se.getIntKey());
                      ctlIDs += cb.getId() + ",";
                    
                      seEn = selectedEns.GetEntityByKey(selecteVal, val);
                      if (seEn != null)
                         cb.setChecked(true);
                    
                      cb.setText(en.GetValStringByKey(showText));
                      //cb.AccessKey = se.IntKey.ToString();
                      this.UCSys.append("<TD nowrap = 'nowrap'>");
        		      this.UCSys.append(cb);
        		      this.UCSys.append("</TD>");
        		      if (i == 3)
        		    	 this.UCSys.append(BaseModel.AddTREnd());
                  }
                  
                  if(ctlIDs.length()>0)ctlIDs = ctlIDs.substring(0,ctlIDs.length()-1);
                  cb1.addAttr("onclick", "SetSelected(this,'" + ctlIDs + "')");
                
                  switch (i){
	    	          case 1:
	    	        	  this.UCSys.append(BaseModel.AddTD());
	    	        	  this.UCSys.append(BaseModel.AddTD());//"<TD>&nbsp;</TD>");
	    	        	  this.UCSys.append(BaseModel.AddTREnd());//("</TR>");
	    	              break;
	    	          case 2:
	    	        	  this.UCSys.append(BaseModel.AddTD());
	    	        	  this.UCSys.append(BaseModel.AddTREnd());
	    	              break;
	    	          default:
	    	              break;
                  }
        	  }
          }else{
        	  Entities groupEns = ClassFactory.GetEns(attr.getUIBindKey());
        	  groupEns.RetrieveAll();
              for (Entity group : groupEns.ToJavaListEn()){
            	  this.UCSys.append("<TR>");
            	  this.UCSys.append("<TD class='GroupTitle' colspan=3>");
            	  
            	  CheckBox cb1 = this.ui.creatCheckBox("CB_EN_" + group.GetValStrByKey(attr.getUIRefKeyValue()));
            	  cb1.setText(group.GetValStrByKey(attr.getUIRefKeyText()));
            	  //cb1.Attributes["onclick"] = "SetSelected(this,'" + group.GetValStringByKey(attr.UIRefKeyValue) + "')";
            	  this.UCSys.append(cb1);
    		      this.UCSys.append("</TD>");
    		      this.UCSys.append(BaseModel.AddTREnd());
    		      
    		      String ctlIDs = "";
                  int i = 0;
                  String gVal = group.GetValStrByKey(attr.getUIRefKeyValue());
                  for (Entity en : ens.ToJavaListEn()){
                	  if (!(en.GetValStrByKey(attr.getKey())).equals(gVal))
                          continue;
                	  i++;
                      if (i == 4)
                          i = 1;
                      if (i == 1)
                    	  this.UCSys.append("<TR>");
                      
                      val = en.GetValStringByKey(showVal);
                      CheckBox cb = this.ui.creatCheckBox("CB_" + val + "_" + gVal);//edited by liuxc,2015.1.6
                      
                      seEn = selectedEns.GetEntityByKey(selecteVal, val);
                      if (seEn != null)
                          cb.setChecked(true);
                      
                      cb.setText(en.GetValStrByKey(showText));
                      this.UCSys.append("<TD nowrap = 'nowrap'>");
        		      this.UCSys.append(cb);
        		      this.UCSys.append("</TD>");
        		      
        		      ctlIDs += cb.getId() + ",";
                      if (i == 3)
                    	  this.UCSys.append(BaseModel.AddTREnd());
                  }
               
                  if(ctlIDs.length()>0)ctlIDs = ctlIDs.substring(0,ctlIDs.length()-1);
                  cb1.addAttr("onclick", "SetSelected(this,'" + ctlIDs + "')");
                
                  switch (i){
	    	          case 1:
	    	        	  this.UCSys.append(BaseModel.AddTD());
	    	        	  this.UCSys.append(BaseModel.AddTD());//"<TD>&nbsp;</TD>");
	    	        	  this.UCSys.append(BaseModel.AddTREnd());//("</TR>");
	    	              break;
	    	          case 2:
	    	        	  this.UCSys.append(BaseModel.AddTD());
	    	        	  this.UCSys.append(BaseModel.AddTREnd());
	    	              break;
	    	          default:
	    	              break;
                  }
                  
              }
          }
          
          this.UCSys.append(BaseModel.AddTableEnd());
	 }
	
	
	public boolean isIsLine() {
		return IsLine;
	}
	public void setIsLine(boolean isLine) {
		IsLine = isLine;
	}
	public String getMainEnName() {
		return MainEnName;
	}
	public void setMainEnName(String mainEnName) {
		MainEnName = mainEnName;
	}
	public String getMainEnPKVal() {
		return MainEnPKVal;
	}
	public void setMainEnPKVal(String mainEnPKVal) {
		MainEnPKVal = mainEnPKVal;
	}
	public String getMainEnPK() {
		return MainEnPK;
	}
	public void setMainEnPK(String mainEnPK) {
		MainEnPK = mainEnPK;
	}
	public Entity getMainEn() {
		if(null == MainEn)
			return ClassFactory.GetEn(this.getEnsName());
		return MainEn;
	}
	public void setMainEn(Entity mainEn) {
		MainEn = mainEn;
	}
	
	public static void main(String[] args) {
		Dot2DotModel dot = new Dot2DotModel(null,null,"");
		CheckBox cb = dot.toolBar.AddCB("checkedAll");
        cb.addAttr("onclick", "SelectAll(this);");
        cb.setText("选择全部");
        System.out.println(((CheckBox)dot.ui.getTmpList().get(0)).getText());
	}
}
