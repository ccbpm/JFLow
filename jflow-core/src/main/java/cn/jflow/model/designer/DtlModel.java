package cn.jflow.model.designer;

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DataType;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.ClassFactory;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.Map;
import BP.En.QueryObject;
import BP.En.TBType;
import BP.En.UAC;
import BP.Sys.SystemConfig;
import cn.jflow.common.model.BaseModel;
import cn.jflow.system.ui.UiFatory;
import cn.jflow.system.ui.core.CheckBox;
import cn.jflow.system.ui.core.DDL;
import cn.jflow.system.ui.core.ListItem;
import cn.jflow.system.ui.core.NamesOfBtn;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.ToolBar;

public class DtlModel {

	private String basePath;
	private HttpServletRequest _request = null;
	private HttpServletResponse _response = null;
	
	private final int PageSize = SystemConfig.getPageSize();
	
	public UiFatory ui = null;
	public ToolBar toolBar = null;
	public UiFatory UCSys1 = null;
	public StringBuilder UCSys2 = null;
	public DtlModel(HttpServletRequest request, HttpServletResponse response, String basePath) {
		this.basePath = basePath; 
		this._request = request;
		this._response = response;
		
		this.ui = new UiFatory();
		this.UCSys1 = new UiFatory();
		this.UCSys2 = new StringBuilder();
		this.toolBar = new ToolBar(this._request, this._response, this.ui);
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
	public final String getMainEnsName(){
		if(_request.getParameter("MainEnsName")==null)
			return "";
		return _request.getParameter("MainEnsName");
    }
	public final String getRefKey(){
		if(_request.getParameter("RefKey")==null)
			return "";
		return _request.getParameter("RefKey");
	}
	public final String getRefVal(){
		String refVal = _request.getParameter("RefVal");
		if(null == refVal){
			refVal = this._request.getParameter("PK");
		}
		if(null == refVal){
			refVal = this._request.getParameter("No");
		}
		if(null == refVal){
			refVal = this._request.getParameter("OID");
		}
		if(null == refVal){
			refVal = this._request.getParameter("MyPK");
		}
		return refVal;
	}
	public Entity getHisEn(){
		return this.getHisEns().getGetNewEntity();
	}
	public Entities getHisEns(){
		 Entities ens = ClassFactory.GetEns(this.getEnsName());
		 return ens;
	}
	public int getPageIdx(){
		String PageIdx = this._request.getParameter("PageIdx");
		if(null == PageIdx || "".equals(PageIdx)){
			return 1;
		}
		return Integer.parseInt(PageIdx);
	}
	
	public void init(){
		try{
			 this.toolBar.AddLinkBtn(NamesOfBtn.Save);
	         //    this.ToolBar1.AddLinkBtn(NamesOfBtn.SaveAndClose);
			 this.toolBar.AddLinkBtn(NamesOfBtn.Delete);
			 //	   this.ToolBar1.AddLinkBtn(NamesOfBtn.Excel, "导出Excel");
			 
			 this.toolBar.GetLinkBtnByID(NamesOfBtn.Save).setHref("onSave()");
			 this.toolBar.GetLinkBtnByID(NamesOfBtn.Delete).setHref("onDel()");
			 
			 this.Bind();
		}catch(Exception e){
			this.UCSys2.append(BaseModel.AddMsgOfWarning("错误", e.getMessage()));
		}
	}
	
	public void Bind(){
		
		 //#region 生成标题
         Entity en = this.getHisEn();
         Map map = this.getHisEn().getEnMap();
         Attrs attrs = map.getAttrs();
         boolean isFJ = false;
         if (attrs.Contains("MyFileName"))
             isFJ = true;
         
         this.UCSys1.append(BaseModel.AddTable("class='Table' cellSpacing='1' cellPadding='1' border='1' style='width:100%'"));
         this.UCSys1.append(BaseModel.AddTR());
         this.UCSys1.append("\n<TH style='width:20px'>序</TH>");

         String str1 = "<INPUT id='checkedAll' onclick='SelectAll(this);' type='checkbox' name='checkedAll'>";
         this.UCSys1.append(BaseModel.AddTDTitle(str1));
         for(Attr attr : attrs){
        	 if (!attr.getUIVisible())
                 continue;
        	 
        	 this.UCSys1.append(BaseModel.AddTDTitle(attr.getDesc()));
         }
         
         if (isFJ)
             this.UCSys1.append(BaseModel.AddTDTitle());
         

         this.UCSys1.append(BaseModel.AddTREnd());
         //#endregion 生成标题
         
         Entities dtls = this.getHisEns();
         QueryObject qo = new QueryObject(dtls);
         qo.AddWhere(this.getRefKey(), this.getRefVal());
         
         //#region 生成翻页
         this.UCSys2.setLength(0);
         try{
        	 //BaseModel.BindPageIdx_ver1(this.UCSys2, qo.GetCount(), this.PageSize, this.getPageIdx(), this.basePath+"WF/Comm/RefFunc/Dtl.jsp?EnName=" + this.getEnName() + "&PK=" + this.getRefVal() + "&EnsName=" + this.getEnsName() + "&RefVal=" + this.getRefVal() + "&RefKey=" + this.getRefKey() + "&MainEnsName=" + this.getMainEnsName());
        	 BaseModel.BindPageIdxEasyUi(this.UCSys2, qo.GetCount(), this.basePath+"WF/Comm/RefFunc/Dtl.jsp?EnName=" + this.getEnName() + "&PK=" + this.getRefVal() + "&EnsName=" + this.getEnsName() + "&RefVal=" + this.getRefVal() + "&RefKey=" + this.getRefKey() + "&MainEnsName=" + this.getMainEnsName(), this.getPageIdx(), this.PageSize,  "'first','prev','sep','manual','sep','next','last'", false);
        	 
             qo.DoQuery(en.getPK(), this.PageSize, this.getPageIdx(), false);
         }catch(Exception e){
             dtls.getGetNewEntity().CheckPhysicsTable();
             //this.Response.Redirect("Ens.aspx?EnsName=" + this.EnsName + "&RefPKVal=" + this.RefPKVal, true);
             return;
         }
         // #endregion 生成翻页
         
         UAC uac = en.getHisUAC();
         if (!uac.IsDelete)
             this.toolBar.GetLinkBtnByID(NamesOfBtn.Delete).setEnabled(false);;

         if (uac.IsInsert){
             en.setPKVal( "0");
             dtls.AddEntity(en);
         }
         
         DDL ddl = null;
         CheckBox cb = null;
         boolean is1 = false;
         
         //#region 生成数据
         int i = 0;
         for (Entity dtl : dtls.ToJavaListEn()){
        	 i++;
             if ("".equals(dtl.getPKVal().toString()) || "0".equals(dtl.getPKVal().toString())){
                 this.UCSys1.append(BaseModel.AddTRSum());
                 this.UCSys1.append(BaseModel.AddTD("colspan=2", "<b>*</b>"));
             }else{
            	 //is1 = this.ucsys1.AddTR(is1, "ondblclick=\"WinOpen( 'UIEn.aspx?EnsName=" + this.EnsName + "&PK=" + dtl.PKVal + "', 'cd' )\"");
                 this.UCSys1.append(BaseModel.AddTR(is1));
                 is1 = !is1;
                 
                 this.UCSys1.append(BaseModel.AddTDIdx(i));
                 cb =  this.UCSys1.creatCheckBox("CB_" + dtl.getPKVal());
                 this.UCSys1.append("\n<TD nowrap = 'nowrap'>");
                 this.UCSys1.append(cb);
                 this.UCSys1.append("</TD>");
             }
             
             for(Attr attr : attrs){
            	 if (!attr.getUIVisible())
                     continue;

                 if ("OID".equals(attr.getKey()))
                     continue;
                 
                 String val = dtl.GetValByKey(attr.getKey()).toString();
                 switch (attr.getUIContralType()){
                 	case TB:
		         		  TextBox tb = this.UCSys1.creatTextBox("TB_" + attr.getKey() + "_" + dtl.getPKVal());
		                  tb.LoadMapAttr(attr);
		                  tb.addAttr("style", "width:" + attr.getUIWidth() + "px;border-width:0px;");
		                  this.UCSys1.append("\n<TD nowrap = 'nowrap'>");
		                  this.UCSys1.append(tb);
		                  this.UCSys1.append("</TD>");
		                  switch (attr.getMyDataType()){
		                      case DataType.AppMoney:
		                      case DataType.AppRate:
		                          tb.setText(decimalFormat(val));
		                          break;
		                      case DataType.AppDate:
		                    	  tb.setText(val);
		                    	  tb.setShowType(TBType.Date);
		                    	  if (!attr.getUIIsReadonly()){
		                    		  tb.addAttr("onfocus", "WdatePicker();");
		                    	  }
		                    	  break;
							 case DataType.AppDateTime:
								 tb.setText(val);
								 tb.setShowType(TBType.DateTime);
								 if (!attr.getUIIsReadonly()){
									tb.addAttr("onfocus", "WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});");
								 }
								 break;
							 default:
								tb.setText(val);
								break;
		                  }
		                  if (attr.getIsNum() && !attr.getIsFKorEnum()){
		                      if (tb.getEnabled()){
		                          // OnKeyPress="javascript:return VirtyNum(this);"
		                          //  tb.Attributes["OnKeyDown"] = "javascript:return VirtyNum(this);";
		                          // tb.Attributes["onkeyup"] += "javascript:C" + dtl.PKVal + "();C" + attr.Key + "();";
		                    	  tb.addAttr("class", "TBNum");
		                      }else{
		                          //   tb.Attributes["onpropertychange"] += "C" + attr.Key + "();";
		                    	  tb.addAttr("class", "TBNumReadonly");
		                      }
		                  }
                 		break;
                 	 case DDL:
                 		 ddl = this.UCSys1.creatDDL("DDL_" + attr.getKey() + "_" + dtl.getPKVal());
                 		 if (!attr.getUIIsReadonly()){
                 			 ddl.Items.add(new ListItem(dtl.GetValRefTextByKey(attr.getKey()), val));
                 			 ddl.setEnabled(false);
                 		 }else{
							if (attr.getIsEnum()) {
								ddl.BindSysEnum(attr.getUIBindKey());
							} else {
								ddl.BindEntities(attr.getHisFKEns(), attr.getUIRefKeyValue(),
										attr.getUIRefKeyText());
							}
                 		 }
                 		 ddl.SetSelectItem(val);
                 		 this.UCSys1.append("\n<TD nowrap = 'nowrap'>");
                 		 this.UCSys1.append(ddl);
                 		 this.UCSys1.append("</TD>");
                 		break;
                 	case CheckBok:
						 cb = this.UCSys1.creatCheckBox("CB_" + attr.getKey() + "_" + dtl.getPKVal());
						 cb.setText(attr.getDesc());
						 if (val.equals("1")) {
							cb.setChecked(true);
						 } else {
							cb.setChecked(false);
						 }
						 this.UCSys1.append("\n<TD nowrap = 'nowrap'>");
                 		 this.UCSys1.append(cb);
                 		 this.UCSys1.append("</TD>");
						break;
                 	 default:
                 		break;
                 }
             }
             
             if (isFJ){
 				String ext = dtl.GetValStrByKey("MyFileExt");
 				if (!ext.equals("")){
 					this.UCSys1.append(BaseModel.AddTD("<img src='../Images/FileType/" + ext + ".gif' border=0/>" + dtl.GetValStrByKey("MyFileName")));
 				}
 			}
             this.UCSys1.append(BaseModel.AddTREnd());
         }
         
         //#region 生成合计
         //this.ucsys1.AddTRSum();
         //this.ucsys1.AddTD("colspan=2", "合计");
         //foreach (Attr attr in attrs)
         //{
         //    if (attr.UIVisible == false)
         //        continue;
         //    if (attr.IsNum && attr.IsFKorEnum == false)
         //    {
         //        TB tb = new TB();
         //        tb.ID = "TB_" + attr.Key;
         //        tb.Text = attr.DefaultVal.ToString();
         //        tb.ShowType = attr.HisTBType;
         //        tb.ReadOnly = true;
         //        tb.Font.Bold = true;
         //        tb.BackColor = System.Drawing.Color.FromName("infobackground");

         //        switch (attr.MyDataType)
         //        {
         //            case DataType.AppRate:
         //            case DataType.AppMoney:
         //                tb.TextExtMoney = dtls.GetSumDecimalByKey(attr.Key);
         //                break;
         //            case DataType.AppInt:
         //                tb.TextExtInt = dtls.GetSumIntByKey(attr.Key);
         //                break;
         //            case DataType.AppFloat:
         //                tb.TextExtFloat = dtls.GetSumFloatByKey(attr.Key);
         //                break;
         //            default:
         //                break;
         //        }
         //        this.ucsys1.AddTD(tb);
         //    }
         //    else
         //    {
         //        this.ucsys1.AddTD();
         //    }
         //}
         //this.ucsys1.AddTD();
         //this.ucsys1.AddTREnd();
         //#endregion 生成合计

         //#endregion 生成数据
         this.UCSys1.append(BaseModel.AddTableEnd());
	}
	
	private String decimalFormat(Object object){
		if(null == object || "".equals(object.toString())){
			object = 0.00;
		}
		String str = new DecimalFormat("#0.00").format(Double.parseDouble(object.toString()));
		return str;
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
