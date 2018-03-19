package cn.jflow.controller.wf.admin.xap.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import BP.DA.DBAccess;
import BP.Sys.PubClass;
import BP.Sys.SystemConfig;
import BP.Tools.FileAccess;
import BP.Tools.StringHelper;
import BP.WF.Glo;
import BP.WF.Node;
import BP.WF.NodeFormType;
import BP.WF.Template.BillFileType;
import BP.WF.Template.BillTemplate;
import BP.WF.Template.BillTemplates;
import BP.WF.Template.BillType;
import BP.WF.Template.BillTypes;
import cn.jflow.common.model.BaseModel;
import cn.jflow.common.util.ContextHolderUtils;
import cn.jflow.common.util.ConvertTools;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.HtmlUtils;

@Controller
@RequestMapping("/Wf/Admin")
@Scope("request")
public class BillControlle {

//		@Autowired  
//	    public HttpServletRequest request;  
	  
		
	//private void btn_Gener_Click() {
	@RequestMapping(value = "/btn_Gener_Click", method = RequestMethod.GET)
	public void btn_Gener_Click(HttpServletRequest request,HttpServletResponse response) throws IOException {	
		String FK_Flow=request.getParameter("FK_Flow");
		String FK_Node=request.getParameter("FK_Node");
		String RefNo=request.getParameter("RefNo");
		String url = Glo.getCCFlowAppPath()+"WF/Admin/Bill.jsp?FK_Flow=" + FK_Flow+ "&NodeID=" + FK_Node + "&DoType=Edit&RefNo=" + RefNo;
		//this.Response.Redirect(url, true);
		response.sendRedirect(url);
	}
	
	@RequestMapping(value = "/btn_Click", method = RequestMethod.POST)
	public void btn_Click(@RequestParam("f") MultipartFile file ,HttpServletRequest request,HttpServletResponse response ) throws IllegalStateException, IOException {
	//private void btn_Click(HttpServletRequest request,HttpServletResponse response)  {
		String content = ContextHolderUtils.getRequest().getParameter("DDL_BillType");
		
		// 判断文件是否为空   start
/*	      if (!file.isEmpty()) {  
	            try {  
	                // 文件保存路径  
	                String filePath = request.getSession().getServletContext().getRealPath("/") + "/upload/"  
	                        + file.getOriginalFilename();  
	                // 转存文件  
	                System.out.println(filePath.toString());
	                file.transferTo(new File(filePath));  
	            } catch (Exception e) {  
	                e.printStackTrace();  
	            }  
	        } */
	   // 判断文件是否为空   end
	      
				String tempVar=request.getParameter("tempVar");
				String FK_Flow=request.getParameter("FK_Flow");
				String FK_Node=request.getParameter("FK_Node");
				String RefNo = request.getParameter("RefNo");
				//String content = ContextHolderUtils.getRequest().getParameter("FK_Node");
				content = request.getParameter("BodyHtml");
				//(FileUpload)((tempVar instanceof FileUpload) ? tempVar : null) ;		
				//FileUpload file = null;
				BP.WF.Template.BillTemplate bt = new BP.WF.Template.BillTemplate();
			//	DDL_BillType.
				//bt.setNodeID(this.getNodeID());
				String NodeID=request.getParameter("NodeID");
				bt.setNodeID(Integer.parseInt(NodeID));
				Node nd = new Node(NodeID);
				
				if(!StringHelper.isNullOrEmpty(RefNo)){
					bt.setNo(RefNo);
					bt.Retrieve();
					// 原有代码 Object tempVar2 = this.Ucsys1.Copy(bt);
					request.getParameter("DDL_BillFileType");
					ContextHolderUtils.getRequest().getParameter("BodyHtml");
					HashMap<String, BaseWebControl> controls = HtmlUtils.httpParser(content, request);
					Object tempVar2 = BaseModel.Copy(request, bt, null, bt.getEnMap(), controls);
 
					bt = (BillTemplate)((tempVar2 instanceof BillTemplate) ? tempVar2 : null);
					bt.setNodeID(Integer.parseInt(NodeID));
					
					// 0 bt.FK_BillType = this.Ucsys1.GetDDLByID("DDL_BillType").SelectedItemStringVal;
					// 1 ((DDL)(controls.get("DDL_BillType"))).getSelectedItemStringVal();
					//2 获取select标签的值                                                     DDL_BillType
					bt.setFK_BillType(request.getParameter("DDL_BillType"));
					if (file.getOriginalFilename() == null ||file.getOriginalFilename().trim().equals("")) {
						bt.Update();
						PubClass.Alert("保存成功", response);
						String url=Glo.getCCFlowAppPath()+"WF/Admin/Bill.jsp?FK_Flow=" + FK_Flow + "&NodeID=" + NodeID;
						String  script="window.location.href='"+url+"'";
						PubClass.ResponseWriteScript(script);
						return;
					}

					if(bt.getHisBillFileType()==BillFileType.RuiLang){
						if (file.getOriginalFilename().toLowerCase().contains(".grf") == false) {
							PubClass.Alert("@错误，非法的 grf 格式文件", response);
							String  script="javascript: window.history.go(-1);";
							PubClass.ResponseWriteScript(script);
					
							return;
						}
					}
					else {
						if (file.getOriginalFilename().toLowerCase().contains(".rtf") == false) {
							PubClass.Alert("@错误，非法的 rtf 格式文件", response);
							String  script="javascript: window.history.go(-1);";
							PubClass.ResponseWriteScript(script);
							return;
						}
					}
					
					
					String temp = "";
					String tempName = "";
					if (bt.getHisBillFileType() == BillFileType.RuiLang) {
						tempName = "Temp.grf";
						temp = BP.Sys.SystemConfig.getPathOfCyclostyleFile() + "Temp.grf";
						try {
							file.transferTo(new File(temp));
						} catch (IllegalStateException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
//						file.PostedFile.SaveAs(temp);
						
					}
					else {
						tempName = "Temp.rtf";
						temp = BP.Sys.SystemConfig.getPathOfCyclostyleFile() + "Temp.rtf";
						try {
							//file.transferTo(new File(temp));
							FileAccess.copyFolder(new File(temp), new File(temp));
						} catch (IllegalStateException e) {
							e.printStackTrace();
						}
					}

 
					//检查文件是否正确。
					try {
						String[] paras = BP.DA.Cash.GetBillParas_Gener(tempName, nd.getHisFlow().getHisGERpt().getEnMap().getAttrs());
					}
					catch (RuntimeException ex) {
						BaseModel.ToErrorPage("错误信息"+ex.getMessage());
//						this.UCSys2.append(this.AddMsgOfWarning("错误信息", ex.getMessage()));
						return;
					}
					String fullFile;
					try {
						fullFile = FileFullPath(file.getOriginalFilename(), bt,request,response);
						file.transferTo(new File(fullFile));
					} catch (IOException e1) {
						e1.printStackTrace();
					} //BP.Sys.SystemConfig.PathOfCyclostyleFile + "\\" + bt.No + ".rtf";
//					System.IO.File.Copy(temp, fullFile, true);
					bt.Update();
					String url=Glo.getCCFlowAppPath()+"WF/Admin/Bill.jsp?FK_Flow=" + FK_Flow + "&NodeID=" + NodeID;
					String  script="window.location.href='"+url+"'";
					PubClass.ResponseWriteScript(script);
					return;
				}
//				Object tempVar3 = this.Ucsys1.Copy(bt);
				HashMap<String, BaseWebControl> controls = HtmlUtils.httpParser(content, request);
				Object tempVar3 = BaseModel.Copy(request, bt, null, bt.getEnMap(), controls);
				bt = (BillTemplate)((tempVar3 instanceof BillTemplate) ? tempVar3 : null);

				if (file.getOriginalFilename() != null) {
					if (bt.getHisBillFileType() == BillFileType.RuiLang) {
						if (file.getOriginalFilename().toLowerCase().contains(".grf") == false) {
							PubClass.Alert("@错误，非法的 grf 格式文件。", response);
							String  script="javascript: window.history.go(-1);";
							PubClass.ResponseWriteScript(script);
							return;
						}
					}
					else {
						if (file.getOriginalFilename().toLowerCase().contains(".rtf") == false) {
							//this.Alert("@错误，非法的 rtf 格式文件。");
							PubClass.Alert("@错误，非法的 rtf 格式文件", response);
							String  script="javascript: window.history.go(-1);";
							PubClass.ResponseWriteScript(script);
							return;
						}
					}
				} else {
					//this.Alert("请上传文件。");
					PubClass.Alert("请上传文件", response);
					String  script="javascript:window.history.go(-1);";
					PubClass.ResponseWriteScript(script);
					return;
				}
				

				// 如果包含这二个字段。
				//String fileName = file.PostedFile.FileName;
				String fileName=file.getOriginalFilename();
				fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
				if (bt.getName().equals("")) {

					bt.setName(fileName.replace(".rtf", ""));
					bt.setName(fileName.replace(".grf", ""));
				}

				try {
					bt.setNo(BP.Tools.chs2py.convert(bt.getName())) ;
					bt.setNo(Integer.valueOf(DBAccess.GenerOID()).toString());
					if(bt.getIsExits()){
						bt.setNo(bt.getNo()+"."+Integer.valueOf(DBAccess.GenerOID()).toString());
					}
				}
				catch (java.lang.Exception e1) {
					//bt.No = BP.DA.DBAccess.GenerOID().toString();
					bt.setNo(Integer.valueOf(DBAccess.GenerOID()).toString());
				}
				String tmp = "";
				String tmpName = "";
				if (bt.getHisBillFileType() == BillFileType.RuiLang) {
					tmpName = "Temp.grf";
					//tmp = BP.Sys.SystemConfig.PathOfCyclostyleFile + "\\Temp.grf";
					tmp=SystemConfig.getPathOfCyclostyleFile()+ "Temp.grf";
					
					//file.PostedFile.SaveAs(tmp);
					try {
						FileAccess.copyFolder(new File(tmp), new File(tmp));
						//file.transferTo(new File(tmp));
					} catch (IllegalStateException e) {
						e.printStackTrace();
					}
				}
				else {
					tmpName = "Temp.rtf";
					tmp =SystemConfig.getPathOfCyclostyleFile() + "Temp.rtf";
					try {
						//file.transferTo(new File(tmp));
						FileAccess.copyFolder(new File(tmp), new File(tmp));
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} 
				}


				//检查文件是否正确。
				try {
					String[] paras1 = BP.DA.Cash.GetBillParas_Gener(tmpName, nd.getHisFlow().getHisGERpt().getEnMap().getAttrs());
				}
				catch (RuntimeException ex) {
					BaseModel.ToErrorPage("Error:" + ex.getMessage());
					//增加错误信息在页面显示
//					this.UCSys2.append(this.AddMsgOfWarning("Error:", ex.getMessage()));
					return;
				}

				try {
					String fullFile1 = FileFullPath(fileName, bt,request,response); //BP.Sys.SystemConfig.PathOfCyclostyleFile + "\\" + bt.No + ".rtf";
					//System.IO.File.Copy(tmp, fullFile1, true);
					file.transferTo(new File(fullFile1));
					//FileAccess.copyFolder(new File(tmp), new File(fullFile1));
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				//Ucsys1.GetDDLByID getddlbyid 弄不错俩
				 
				 // bt.FK_BillType = this.Ucsys1.GetDDLByID("DDL_BillType").SelectedItemStringVal;
				bt.setFK_BillType(request.getParameter("DDL_BillType"));
				bt.Insert();

		
				///#region 更新节点信息。
				String Billids = "";
				BillTemplates tmps = new BillTemplates(nd);
				for (BillTemplate Btmp : tmps.ToJavaList()) {
					Billids += "@" + Btmp.getNo();
				}
				nd.setHisBillIDs(Billids);
				nd.Update();
		
				///#endregion 更新节点信息。

				try {
					response.sendRedirect(Glo.getCCFlowAppPath()+"WF/Admin/Bill.jsp?FK_Flow=" + FK_Flow + "&NodeID=" +NodeID);
				} catch (IOException e) {
					e.printStackTrace();
				}
	
			}
			
	@RequestMapping(value = "/btn_Del_Click", method = RequestMethod.POST)
	public void btn_Del_Click(HttpServletRequest request,HttpServletResponse response) throws IOException {
//	private void btn_Del_Click(Object sender, EventArgs e) {
		String RefNo=request.getParameter("RefNo");
		String NodeID=request.getParameter("NodeID");
		String FK_Flow=request.getParameter("FK_Flow");
		BP.WF.Template.BillTemplate t = new BP.WF.Template.BillTemplate();
		t.setNo(RefNo);
		t.Delete();

		///#region 更新节点信息。
		Node nd =new Node(NodeID);
		String Billids = "";
		BillTemplates tmps = new BillTemplates(nd);
		for (BillTemplate tmp : tmps.ToJavaList()) {
			Billids += "@" + tmp.getNo();
		}
		nd.setHisBillIDs(Billids);
		nd.Update();
		///#endregion 更新节点信息。
		//this.Response.Redirect("Bill.aspx?FK_Flow=" + this.getFK_Flow() + "&NodeID=" + this.getNodeID(), true);
		response.sendRedirect(Glo.getCCFlowAppPath()+"WF/Admin/Bill.jsp?FK_Flow=" + FK_Flow + "&NodeID=" + NodeID);
	}
	
	
	@RequestMapping(value = "/btn_SaveTypes_Click", method = RequestMethod.POST)
	public void btn_SaveTypes_Click(HttpServletRequest request,HttpServletResponse response)  {
	//protected final void btn_SaveTypes_Click(Object sender, EventArgs e) {
		String FK_Flow=request.getParameter("FK_Flow");
		String NodeID=request.getParameter("NodeID");
		BillTypes ens = new BillTypes();
		ens.RetrieveAll();
		try {
			ens.Delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//ens.Delete();
		for (int i = 1; i < 18; i++) {
			// 看看这个地方获取的对吗
			//String name = this.Ucsys1.GetTextBoxByID("TB_" + i).getText();
			String name = request.getParameter("TB_"+i);
			if (StringHelper.isNullOrEmpty(name)) {
				continue;
			}

			BillType en = new BillType();
			//en.setNo(new Integer(i)).toString().PadLeft(2, '0'); 下面是修改的
			en.setNo(ConvertTools.padLeft(String.valueOf(i), 2, "0"));
			
			en.setName(name);
			//en.FK_Flow = this.getFK_Flow();
			en.setFK_Flow(FK_Flow);
			en.Insert();
		}
		PubClass.Alert("保存成功.", response);
		String url=Glo.getCCFlowAppPath()+"WF/Admin/Bill.jsp?FK_Flow=" + FK_Flow + "&NodeID=" + NodeID+"&DoType=EditType";
		String  script="window.location.href='"+url+"'";
		PubClass.ResponseWriteScript(script);
		//this.Alert("保存成功.");

	}
	/** 
	 获取文件路径 
	 * @throws IOException 
	 
	*/
	public final String FileFullPath(String fileName, BillTemplate bt,HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		String fileType = "";
		//4 if (bt.HisBillFileType == BillFileType.RuiLang) {
		if (bt.getHisBillFileType() == BillFileType.RuiLang) {
			fileType = ".grf";
		}
		else {
			fileType = ".rtf";
		}

		String filePath = BP.Sys.SystemConfig.getPathOfCyclostyleFile() + "/" + bt.getNo() + fileType;
		//String NodeID=ContextHolderUtils.getRequest().getParameter("NodeID");
		
		//String FK_Flow=ContextHolderUtils.getRequest().getParameter("FK_Flow");
		String NodeID = request. getParameter("NodeID");
		String FK_Flow=request.getParameter("FK_Flow");
		Node curNode=new Node(NodeID);
		//表单树时对存放路径进行修改
		if (curNode.getFormType() == NodeFormType.SheetTree) {
			bt.setUrl("FlowFrm/" + FK_Flow + "/" + NodeID+ "/" + fileName.replace(fileType, ""));
			filePath = BP.Sys.SystemConfig.getPathOfCyclostyleFile() + "/FlowFrm/" + FK_Flow + "/" + NodeID;
			
		/*10	if (!System.IO.Directory.Exists(filePath)) {
				System.IO.Directory.CreateDirectory(filePath);
			}*/
			File f = new File(filePath);
			if (!f.exists()) {
				
				f.createNewFile();
			}
			filePath = BP.Sys.SystemConfig.getPathOfCyclostyleFile() + "/FlowFrm/" +FK_Flow+ "/" + NodeID + "/" + fileName;
		}
		return filePath;
	}

 
	}
	
	
