package cn.jflow.common.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.AtPara;
import BP.DA.DBAccess;
import BP.DA.DataType;
import BP.En.Attrs;
import BP.En.QueryObject;
import BP.Sys.FrmAttachment;
import BP.Sys.FrmAttachmentDB;
import BP.Sys.FrmAttachmentDBAttr;
import BP.Sys.FrmAttachmentDBs;
import BP.Tools.StringHelper;
import BP.WF.ActionType;
import BP.WF.Dev2Interface;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Pub;
import BP.WF.TodolistModel;
import BP.WF.Entity.FrmWorkCheck;
import BP.WF.Entity.FrmWorkCheckSta;
import BP.WF.Entity.FrmWorkShowModel;
import BP.WF.Entity.GenerWorkerList;
import BP.WF.Entity.GenerWorkerLists;
import BP.WF.Entity.Track;
import BP.WF.Entity.Tracks;
import BP.WF.Entity.WorkCheck;
import BP.WF.Template.CCList;
import BP.WF.Template.CCListAttr;
import BP.WF.Template.CCLists;
import BP.WF.Template.CCSta;
import BP.WF.Template.FWCAth;
import BP.WF.Template.FWCType;
import BP.WF.Template.SelectAccper;
import BP.WF.Template.SelectAccperAttr;
import BP.WF.Template.SelectAccpers;
import BP.Web.WebUser;
import cn.jflow.system.ui.core.TextBox;
import cn.jflow.system.ui.core.TextBoxMode;

public class WorkCheckM extends BaseModel {

	public StringBuffer Pub1 = null;

	//#region 属性
	public boolean getIsHidden() {
		try {
			if (getDoType().equals("View"))
				return true;
			return Boolean.parseBoolean(this.get_request().getParameter("IsHidden"));
		} catch (Exception e) {
			return false;
		}
	}
	public int getNodeID() {
		try {
			return Integer.parseInt(this.get_request().getParameter("FK_Node"));
		} catch (Exception e) {
			return Integer.valueOf((this.get_request().getParameter("FK_Flow") + "01"));
		}
	}

	/// <summary>
	/// 工作ID
	/// </summary>
	//    public int getWorkID()
	//    {
	//    	String workid = this.get_request().getParameter("OID");
	//    	if (workid == null)
	//    		workid = this.get_request().getParameter("WorkID");
	//    	return Integer.parseInt(workid);
	//    }
	//    
	//    public int getFID()
	//    {
	//    	String workid = this.get_request().getParameter("FID");
	//    	if (StringHelper.isNullOrEmpty(workid) == true)
	//    		return 0;
	//    	return Integer.parseInt(workid);
	//    }

	/// <summary>
	/// 流程编号
	/// </summary>
	public String getFK_Flow() {
		return this.get_request().getParameter("FK_Flow");
	}

	/// <summary>
	/// 操作View
	/// </summary>
	public String getDoType() {
		return this.get_request().getParameter("DoType");
	}

	/// <summary>
	/// 是否是抄送.
	/// </summary>
	//    public boolean getIsCC()
	//    {
	//    	String s = this.get_request().getParameter("Paras");
	//    	if (s == null)
	//    		return false;
	//
	//    	if (s.contains("IsCC") == true)
	//    		return true;
	//    	return false;
	//    }

	public WorkCheckM(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		Pub1 = new StringBuffer();
	}

	public void Page_Load() throws ParseException {
		//工作流编号不存在绑定空框架.
		if (StringHelper.isNullOrEmpty(this.getFK_Flow())) {
			ViewEmptyForm();
			return;
		}

		//审批节点.
		FrmWorkCheck wcDesc = new FrmWorkCheck(getNodeID());
		//         if (wcDesc.getHisFrmWorkShowModel() == FrmWorkShowModel.Free)
		//             this.BindFreeModel(wcDesc);
		//         else
		//             this.BindFreeModel(wcDesc);
		//         
		if (wcDesc.getHisFrmWorkShowModel() == FrmWorkShowModel.Free)
			this.BindFreeModel(wcDesc);
		else if (wcDesc.getHisFrmWorkShowModel() == FrmWorkShowModel.Table)
			this.BindFreeModel(wcDesc);
		else if (wcDesc.getHisFrmWorkShowModel() == FrmWorkShowModel.Sign)
			this.BindSignModel(wcDesc);
	}

	/// <summary>
	/// 实现的功能：
	/// 1，显示轨迹表。
	/// 2，如果启用了审核，就把审核信息显示出来。
	/// 3，如果启用了抄送，就把抄送的人显示出来。
	/// 4，可以把子流程的信息与处理的结果显示出来。
	/// 5，可以把子线程的信息列出来。
	/// 6，可以把未来到达节点处理人显示出来。
	/// </summary>
	/// <param name="wcDesc"></param>
	public void BindFreeModel(FrmWorkCheck wcDesc) throws ParseException {
		WorkCheck wc = null;
		if (getFID() != 0)
			wc = new WorkCheck(this.getFK_Flow(), this.getNodeID(), this.getFID(), 0);
		else
			wc = new WorkCheck(this.getFK_Flow(), this.getNodeID(), this.getWorkID(), this.getFID());

		boolean isCanDo = BP.WF.Dev2Interface.Flow_IsCanDoCurrentWork(this.getFK_Flow(), this.getNodeID(), this.getWorkID(), WebUser.getNo());

		//yqh  2016-7-5新增翻译content
		boolean isExitTb_doc = true;
		//批量上传附件实现
		StringBuilder uploadJS = new StringBuilder();
		if (wcDesc.getFWCAth().getValue() == FWCAth.MinAth.getValue()) {
			uploadJS.append("<script language='javascript' type='text/javascript'> ");
			uploadJS.append("\t\n function btn_Save_Click(){$('#form1').submit();} ");
			uploadJS.append("\t\n  $(function() {");
			uploadJS.append("\t\n $('#file_upload').uploadify({");
			//对应多附件模板MyPK
			String AttachPK = this.getNodeID() + "_FrmWorkCheck";

			uploadJS.append("\t\n 'swf':'" + getBasePath() + "WF/Scripts/Jquery-plug/fileupload/uploadify.swf',");
			uploadJS.append("\t\n 'uploader':'" + getBasePath() + "WF/CCFormHeader/AttachmentUpload.do?AttachPK=" + AttachPK + "&WorkID=" + this.getWorkID()
					+ "&DoType=MoreAttach&FK_Node=" + this.getFK_Node() + "&EnsName=" + this.getEnName() + "&FK_Flow=" + this.getFK_Flow()
					+ "&PKVal=" + this.getWorkID() + "',");
			/*System.out.println(getBasePath() + "WF/CCForm/AttachmentUpload.jsp?AttachPK=" + AttachPK + "&WorkID=" + this.getWorkID()
					+ "&DoType=MoreAttach&FK_Node=" + this.getFK_Node() + "&EnsName=" + this.getEnName() + "&FK_Flow=" + this.getFK_Flow()
					+ "&PKVal=" + this.getWorkID());*/
            uploadJS.append("\t\n  'auto':true,");
            uploadJS.append("\t\n  'fileTypeDesc':'请选择上传文件',");
            uploadJS.append("\t\n  'buttonText':'上传附件',");
            //uploadJS.append("\t\n hideButton:true,");
            uploadJS.append("\t\n  'width':60,");
            uploadJS.append("\t\n  'fileTypeExts':'*.*',");
            uploadJS.append("\t\n  'height':18,");
            uploadJS.append("\t\n  'multi':true,");
            uploadJS.append("\t\n  'queueSizeLimit':999,");
            uploadJS.append("\t\n  'onDialogOpen': function (a,b) {");
            uploadJS.append("\t\n   },");
            uploadJS.append("\t\n  'onQueueComplete': function (queueData) {");
            uploadJS.append("\t\n       isChange = true;");
            uploadJS.append("\t\n       SaveDtlData(queueData);");
            uploadJS.append("\t\n  },");
            uploadJS.append("\t\n  'removeCompleted':true");
            uploadJS.append("\t\n });");
            uploadJS.append("\t\n });");
            uploadJS.append("\t\n </script>");
			uploadJS.append("<div id='file_upload-queue' class='uploadify-queue'></div>");
			uploadJS.append("<div id='s' style='float:right;margin-right:10px;' ><input type='file' name='file_upload' id='file_upload' width='60' height='30' /></div>");
		}

		//#region 输出历史审核信息.
		if (wcDesc.getFWCListEnable() == true) {
			//求轨迹表.
			Tracks tks = wc.getHisWorkChecks();

			//求抄送列表,把抄送的信息与抄送的读取状态显示出来.
			CCLists ccls = new CCLists(this.getFK_Flow(), this.getWorkID(), this.getFID());

			//查询出来未来节点处理人信息,以方便显示未来没有运动到节点轨迹.
			long wfid = this.getWorkID();
			if (this.getFID() != 0)
				wfid = this.getFID();

			//获得 节点处理人数据。
			SelectAccpers accepts = new SelectAccpers(wfid);

			//取出来该流程的所有的节点。
			Nodes nds = new Nodes(this.getFK_Flow());
			Nodes ndsOrder = new Nodes();
			//求出已经出现的步骤.
			String nodes = ""; //已经出现的步骤.
			for (Track tk : tks.ToJavaList()) {
				switch (tk.getHisActionType()) {
				//case ActionType.Forward:
				case WorkCheck:
					if (nodes.contains(tk.getNDFrom() + ",") == false) {
						nodes += tk.getNDFrom() + ",";
					}
					break;
				case StartChildenFlow:
					if (nodes.contains(tk.getNDFrom() + ",") == false) {
						nodes += tk.getNDFrom() + ",";
					}
					break;
				default:
					continue;
				}
			}

			int biaoji = 0;
			int count = 0;
			int ndfrom = 0;
			for (Node nd : nds.ToJavaList()) {

				if (nodes.contains(nd.getNodeID() + ",") == true) {
					//输出发送审核信息与抄送信息.
					String emps = "";
					String empsorder = ""; //保存队列显示中的人员，做判断，避免重复显示
					String empcheck = ""; //记录当前节点已经输出的
					for (Track tk : tks.ToJavaList()) {
						if (tk.getNDFrom() != nd.getNodeID())
							continue;

						//#region 如果是前进，并且当前节点没有启用审核组件
						if (tk.getHisActionType().getValue() == ActionType.Forward.getValue()) {
							continue;
							//                             FrmWorkCheck fwc = new FrmWorkCheck(nd.getNodeID());
							//                             if (fwc.getHisFrmWorkCheckSta() == FrmWorkCheckSta.Disable)
							//                             {
							//                                 this.Pub1.append(AddBR());
							//                                 this.Pub1.append(tk.getMsgHtml());
							//                                 this.Pub1.append(AddBR());
							//
							//                                 this.Pub1.append("<div style='float:right' >");
							//                               //  this.Pub1.Add("<img src='../Img/Mail_Read.png' border=0 />" + tk.ActionTypeText);
							//                                 if (wcDesc.getSigantureEnabel() == true)
							//                                     this.Pub1.append(BP.WF.Glo.GenerUserSigantureHtml(ContextHolderUtils.getRequest().getRealPath("/"), tk.getEmpFrom(), tk.getEmpFromT()));
							//                                 else
							//                                     this.Pub1.append(BP.WF.Glo.GenerUserImgSmallerHtml(tk.getEmpFrom(), tk.getEmpFromT()));
							//                                 this.Pub1.append("</div>");
							//                                 this.Pub1.append(AddHR());
							//                                 continue;
							//                             }
						}

						if (tk.getHisActionType() != ActionType.WorkCheck && tk.getHisActionType() != ActionType.StartChildenFlow)
							continue;

						emps += tk.getEmpFrom() + ",";

						/*if (tk.getHisActionType() == ActionType.WorkCheck)
						{
						//用户签名信息，显示签名or图片
						    String sigantrueHtml = "";
						    //#region 显示出来队列流程中未审核的那些人.
						    if (nd.getTodolistModel() == TodolistModel.Order)
						    {
						         如果是队列流程就要显示出来未审核的那些人.
						        String empsNodeOrder = "";  //记录当前节点队列访问未执行的人员

						        GenerWorkerLists gwls = new GenerWorkerLists(this.getWorkID());
						        for (GenerWorkerList item: gwls.ToJavaList())
						        {
						            if (item.getFK_Node() == nd.getNodeID())
						            {
						                empsNodeOrder += item.getFK_Emp();
						            }
						        }

						        for (SelectAccper accper: accepts.ToJavaList())
						        {
						            if (empsorder.contains(accper.getFK_Emp()) == true)
						                continue;
						            if (empsNodeOrder.contains(accper.getFK_Emp()) == false)
						                continue;
						            if (tk.getEmpFrom().equals(accper.getFK_Emp()))
						            {
						                 审核信息,首先输出它.

						                this.Pub1.append(tk.getMsgHtml());


						                this.Pub1.append("<img src='../Img/Mail_Read.png' border=0/>" + tk.getActionTypeText());
						                this.Pub1.append(tk.getRDT());
						                this.Pub1.append(BP.WF.Glo.GenerUserImgSmallerHtml(tk.getEmpFrom(), tk.getEmpFromT()));

						                this.Pub1.append(AddHR());
						                empcheck += tk.getEmpFrom();
						            }
						            else
						            {
						                this.Pub1.append(AddTR());
						                if (accper.getAccType() == 0)
						                    this.Pub1.append(" <font style='color:Red;' >执行</font>");
						                else
						                    this.Pub1.append(" <font style='color:Red;' >抄送</font>");
						                this.Pub1.append("无");
						                this.Pub1.append(" <font style='color:Red;' >"+ BP.WF.Glo.GenerUserImgSmallerHtml(accper.getFK_Emp(), accper.getEmpName())+"</font>");
						                this.Pub1.append(" <font style='color:Red;' >" + accper.getInfo() + "</font>");
						                this.Pub1.append(AddHR());
						                empsorder += accper.getFK_Emp();
						            }
						        }
						    }
						    //#endregion 显示出来队列流程中未审核的那些人.
						    else
						    {
						        审核信息,首先输出它.

						        this.Pub1.append("<b>" + nd.getFWCNodeName() + "</b> <br><br>");

						        this.Pub1.append(tk.getMsgHtml());


						       // this.Pub1.Add("<img src='../Img/Mail_Read.png' border=0/>" + tk.ActionTypeText);

						        this.Pub1.append(AddBR());
						        this.Pub1.append(AddBR());

						        //if (wcDesc.SigantureEnabel == true)
						        //    this.Pub1.Add(BP.WF.Glo.GenerUserSigantureHtml(Server.MapPath("../../"), tk.EmpFrom, tk.EmpFromT));
						        //else
						        //    this.Pub1.Add(BP.WF.Glo.GenerUserImgSmallerHtml(tk.EmpFrom, tk.EmpFromT));

						        //this.Pub1.Add( "<div style='float:right'>审核人:"+tk.EmpFrom+ ","+ tk.EmpFromT+" 日期:"+tk.RDT+"</div>");

						        this.Pub1.append("<div style='float:right'>审核人:" + tk.getEmpFrom() + "," + tk.getEmpFromT() + " 日期:" + tk.getRDT() + "</div>");

						        //this.Pub1.Add(tk.RDT);
						         this.Pub1.append(AddHR());
						        empcheck += tk.getEmpFrom();
						    }
						}*/
						//重新翻译tk.getHisActionType() == ActionType.WorkCheck
						if (tk.getHisActionType() == ActionType.WorkCheck) {
							//用户签名信息，显示签名or图片
							String sigantrueHtml = "";

							if (wcDesc.getSigantureEnabel()) {
								sigantrueHtml = BP.WF.Glo.GenerUserSigantureHtml(tk.getEmpFrom(), tk.getEmpFromT());
							} else {
								sigantrueHtml = BP.WF.Glo.GenerUserImgSmallerHtml(tk.getEmpFrom(), tk.getEmpFromT());
							}

							//审核组件附件数据
							FrmAttachmentDBs athDBs = new FrmAttachmentDBs();
							QueryObject obj_Ath = new QueryObject(athDBs);
							obj_Ath.AddWhere(FrmAttachmentDBAttr.FK_FrmAttachment, tk.getNDFrom() + "_FrmWorkCheck");
							obj_Ath.addAnd();
							obj_Ath.AddWhere(FrmAttachmentDBAttr.RefPKVal, this.getWorkID());
							obj_Ath.addOrderBy(FrmAttachmentDBAttr.RDT);
							obj_Ath.DoQuery();

							//审核信息,首先输出它.

							///#region 根据类型加载标题  表格  自由
							//意见输入框
							TextBox tb = new TextBox();//PostBackTextBox
							tb.setId("TB_Doc");
							tb.setTextMode(TextBoxMode.MultiLine);
							tb.addAttr("style", "width:98%;border-style:solid");
							tb.addAttr("onblur", "btn_Save_Click()");
							tb.setRows(3);
							if (this.getDoType() != null && this.getDoType().equals("View")) {
								tb.setReadOnly(true);
							}
							tb.setText(BP.WF.Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getNodeID()));
							if ("同意".equals(tb.getText())) {
								tb.setText("");
							}

							switch (wcDesc.getHisFrmWorkShowModel()) //可编辑框全局唯一
							{
							///#region 表格模式
							case Table:
								if (ndfrom != tk.getNDFrom()) {
									this.Pub1.append(AddTable1("style='padding:0px;width:100%;table-layout: fixed;' leftMargin=0 topMargin=0"));

									this.Pub1.append(AddTR(" style='background-color: #E2F6FB' "));
									this.Pub1.append(AddTD(nd.getFWCNodeName()));
									this.Pub1.append(AddTREnd());

									ndfrom = tk.getNDFrom();
								}

								//审核组件配置字段
								FrmWorkCheck frmWorkCheck = new FrmWorkCheck(tk.getNDFrom());
								//存在审核组件配置字段，则不显示审核意见框
								if (!isNullOrEmpty(frmWorkCheck.getFWCFields())) {
									AtPara ap = new AtPara(tk.getMsg().replace(";", "@"));
									//字段生成表单
									Attrs fwcAttrs = new Attrs(frmWorkCheck.getFWCFields());//
									this.Pub1.append(AddTR());
									this.Pub1.append(AddTDBegin());
									Pub pubx = new Pub(getRequest(), getResponse());
									this.Pub1.append(pubx.BindAttrsForHtml(fwcAttrs, ap));//pub修改
									this.Pub1.append(AddTDEnd());
									this.Pub1.append(AddTREnd());
								} else {
									//审核意见
									this.Pub1.append(AddTR());
									///#region

									if (tk.getEmpFrom() == WebUser.getNo()
											&& this.getFK_Node() == tk.getNDFrom()
											&& isExitTb_doc
											&& (wcDesc.getHisFrmWorkCheckType().getValue() == FWCType.Check.getValue()
													|| ((wcDesc.getHisFrmWorkCheckType().getValue() == FWCType.DailyLog.getValue() || wcDesc.getHisFrmWorkCheckType().getValue() == FWCType.WeekLog.getValue()) && strDate(tk.getRDT()) || (wcDesc.getHisFrmWorkCheckType().getValue() == FWCType.MonthLog.getValue() && 
															strDate(tk.getRDT()))))) {
										isExitTb_doc = false;

										this.Pub1.append(AddTDBegin());

										this.Pub1.append(Add("<div style='float:left'>" + wcDesc.getFWCOpLabel()
												+ "</div><div style='float:left'><a href=javascript:TBHelp('WorkCheck_Doc','ND" + getNodeID() + "')"
												+ "><img src='" + getBasePath()
												+ "WF/Img/Emps.gif' width='23px' align='middle' border=0 />选择词汇</a></div>"
												+ "<div style='float:right' onmouseover='UploadFileChange()'>" + uploadJS.toString() + "</div>"));

										this.Pub1.append(Add("<div style='float:left;width:100%;'>"));
										this.Pub1.append(Add(tb));
										this.Pub1.append(Add("</div>"));

										this.Pub1.append(AddTDEnd());
									} else {
										this.Pub1.append(Add("<td style='WORD-WRAP: break-word;min-height:80px;'>" + tk.getMsgHtml() + "</td>"));
									}
									///#endregion

									this.Pub1.append(AddTREnd());
								}
								//附件
								AddTDOfFrmAttachMent(athDBs, tk);//
								//签名与日期
								this.Pub1.append(AddTR());
								this.Pub1.append(Add("<td style='text-align:right;height:35px;line-height:35px;'>签名:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
										+ sigantrueHtml + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日期:&nbsp;&nbsp;&nbsp;" + tk.getRDT() + "</td>"));
								this.Pub1.append(AddTREnd());
								break;
							///#endregion

							///#region 自由模式
							case Free:
								if (ndfrom != tk.getNDFrom()) {
									this.Pub1.append(AddTable1(" style='padding:0px;width:100%;table-layout: fixed;' leftMargin=0 topMargin=0"));
									//处理节点名称分组列，合并多少行
									//不严格的计算，利用浏览器的容错，渲染时自动匹配
									int rowspan = 13;//3 * tks.size();

									this.Pub1.append(AddTR());
									this.Pub1.append(Add("<td  rowspan='" + rowspan + "' style='width:20px;border:1px solid #D6DDE6;'>" + nd.getFWCNodeName()
											+ "</td>"));
									this.Pub1.append(AddTREnd());

									ndfrom = tk.getNDFrom();
								}

								//审核组件配置字段
								frmWorkCheck = new FrmWorkCheck(tk.getNDFrom());
								//存在审核组件配置字段，则不显示审核意见框
								if (!isNullOrEmpty(frmWorkCheck.getFWCFields())) {
									AtPara ap = new AtPara(tk.getMsg().replace(";", "@"));
									//字段生成表单.
									Attrs fwcAttrs = new Attrs(frmWorkCheck.getFWCFields());//) //xxx
									this.Pub1.append(AddTDBegin());
									Pub pubx = new Pub(getRequest(), getResponse());
									this.Pub1.append(pubx.BindAttrsForHtml(fwcAttrs, ap));//xxx
									this.Pub1.append(AddTDEnd());
									this.Pub1.append(AddTREnd());
								} else {
									this.Pub1.append(AddTR());
									if (tk.getEmpFrom().equals(WebUser.getNo())
											&& this.getFK_Node() == tk.getNDFrom()
											&& isExitTb_doc
											&& (wcDesc.getHisFrmWorkCheckType().getValue() == FWCType.Check.getValue()
													|| ((wcDesc.getHisFrmWorkCheckType().getValue() == FWCType.DailyLog.getValue() || wcDesc.getHisFrmWorkCheckType().getValue() == FWCType.WeekLog.getValue()) && 
															strDate(tk.getRDT())) || (wcDesc.getHisFrmWorkCheckType().getValue() == FWCType.MonthLog.getValue() && 
													strDate(tk.getRDT())))) {
										//if (this.FK_Node == tk.NDFrom && isExitTb_doc)
										//{
										isExitTb_doc = false;
										this.Pub1.append(AddTDBegin());

										this.Pub1.append(Add("<div style='float:left'>" + wcDesc.getFWCOpLabel() + "</div><div style='float:left'><a href=javascript:TBHelp('WorkCheck_Doc','ND" + getNodeID() + "')" + "><img src='" + getBasePath() + "WF/Img/Emps.gif' width='23px' align='middle' border=0 />选择词汇</a></div>"
							                    + "<div style='float:right' onmouseover='UploadFileChange()'>" + uploadJS.toString() + "</div>"));

                                        this.Pub1.append(Add("<div style='float:left;width:100%;'>"));
                                        this.Pub1.append(Add(tb));
                                        this.Pub1.append(Add("</div>"));

                                        this.Pub1.append(AddTDEnd());
									} else {
										this.Pub1.append(Add("<td style='WORD-WRAP: break-word;min-height:80px;'>" + tk.getMsgHtml() + "</td>"));
									}
									this.Pub1.append(AddTREnd());
								}
								//附件
								AddTDOfFrmAttachMent(athDBs, tk);//xxx
								this.Pub1.append(AddTR());
								this.Pub1.append(Add("<td style=' text-align:right;height:35px;line-height:35px;'>签名:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
										+ sigantrueHtml + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日期:&nbsp;&nbsp;&nbsp;" + tk.getRDT() + "</td>"));
								this.Pub1.append(AddTREnd());

								//this.Pub1.AddTableEnd();
								break;
							///#endregion
							default:
								break;
							}
							///#endregion
							count += 1;
							empcheck += tk.getEmpFrom();
						}
						//end

						//#region 检查是否有调用子流程的情况。如果有就输出调用子流程信息. (手机部分的翻译暂时不考虑).
						// int atTmp = (int)ActionType.StartChildenFlow;
						WorkCheck wc2 = new WorkCheck(getFK_Flow(), tk.getNDFrom(), tk.getWorkID(), tk.getFID());
						if (wc2.FID != 0) {
							//Tracks ztks = wc2.HisWorkChecks;    //重复循环！
							//foreach (BP.WF.Track subTK in ztks)
							//{
							if (tk.getHisActionType().equals(ActionType.StartChildenFlow)) {
								/*说明有子流程*/
								/*如果是调用子流程,就要从参数里获取到都是调用了那个子流程，并把他们显示出来.*/
								String[] paras = tk.getTag().split("@");
								String[] p1 = paras[1].split("=");
								String fk_flow = p1[1]; //子流程编号

								String[] p2 = paras[2].split("=");
								String workId = p2[1]; //子流程ID.

								WorkCheck subwc = new WorkCheck(fk_flow, Integer.parseInt(fk_flow + "01"), Long.parseLong(workId), 0);

								Tracks subtks = subwc.getHisWorkChecks();

								//取出来子流程的所有的节点。
								Nodes subNds = new Nodes(fk_flow);
								for (Node item : subNds.ToJavaList()) //主要按顺序显示
								{
									for (Track mysubtk : subtks.ToJavaList()) {
										if (item.getNodeID() != mysubtk.getNDFrom())
											continue;
										/*输出该子流程的审核信息，应该考虑子流程的子流程信息, 就不考虑那样复杂了.*/
										if (mysubtk.getHisActionType() == ActionType.WorkCheck) {
											//biaojie  发起多个子流程时，发起人只显示一次
											if (mysubtk.getNDFrom() == Integer.parseInt(fk_flow + "01") && biaoji == 1)
												continue;

											/*如果是审核.*/
											this.Pub1.append(mysubtk.getActionTypeText() + "<img src='../Img/Mail_Read.png' border=0/>");
											this.Pub1.append(mysubtk.getRDT());
											this.Pub1.append(BP.WF.Glo.GenerUserImgSmallerHtml(mysubtk.getEmpFrom(), mysubtk.getEmpFromT()));

											this.Pub1.append(AddBR());
											this.Pub1.append(mysubtk.getMsgHtml());
											this.Pub1.append(AddBR());
											if (mysubtk.getNDFrom() == Integer.parseInt(fk_flow + "01")) {
												biaoji = 1;
											}
										}
									}
								}

							}
							//}
						}
						//#endregion 检查是否有调用子流程的情况。如果有就输出调用子流程信息.
					}

					for (SelectAccper item : accepts.ToJavaList()) {
						if (item.getFK_Node() != nd.getNodeID())
							continue;
						if (empcheck.contains(item.getFK_Emp()) == true)
							continue;
						if (item.getAccType() == 0)
							continue;
						if (ccls.IsExits(CCListAttr.FK_Node, nd.getNodeID()) == true)
							continue;

						this.Pub1.append("<font style='color:Red;'>执行</font>");

						this.Pub1.append("<font style='color:Red;'>无</font>");

						this.Pub1.append("<font style='color:Red;'>" + BP.WF.Glo.GenerUserImgSmallerHtml(item.getFK_Emp(), item.getEmpName())
								+ "</font>");
						this.Pub1.append(AddBR(item.getInfo()));
						this.Pub1.append(AddHR());
					}

					//#region 输出抄送
					for (SelectAccper it : accepts.ToJavaList()) {
						SelectAccper item = (SelectAccper) it;
						if (item.getFK_Node() != nd.getNodeID())
							continue;
						if (item.getAccType() != 1)
							continue;
						if (ccls.IsExits(CCListAttr.FK_Node, nd.getNodeID()) == false) {
							this.Pub1.append("<font style='color:Red;'> 抄送</font>");
							this.Pub1.append("<font style='color:Red;'> 无</font>");
							// 显示要执行的人员。
							this.Pub1.append("<font style='color:Red;'> " + BP.WF.Glo.GenerUserImgSmallerHtml(item.getFK_Emp(), item.getEmpName())
									+ "</font>");

							//info.
							this.Pub1.append("<font style='color:Red;'>" + item.getInfo() + "</font>");
							this.Pub1.append(AddHR());

						} else {
							for (CCList cc : ccls.ToJavaList()) {
								if (cc.getFK_Node() != nd.getNodeID())
									continue;

								if (cc.getHisSta() == CCSta.CheckOver)
									continue;
								if (!cc.getCCTo().equals(item.getFK_Emp()))
									continue;

								this.Pub1.append(AddTR());
								if (cc.getHisSta() == CCSta.Read) {
									if (nd.getIsEndNode() == true) {
										this.Pub1.append("<img src='../Img/Mail_Read.png' border=0/>抄送已阅");

										this.Pub1.append(cc.getCDT()); //读取时间.
										this.Pub1.append(BP.WF.Glo.GenerUserImgSmallerHtml(cc.getCCTo(), cc.getCCToName()));
										this.Pub1.append(AddBR());
										this.Pub1.append(cc.getCheckNoteHtml());
									} else {
										continue;
									}
								} else {
									if (WebUser.getNo().equals(cc.getCCTo())) {
										continue;

										/*如果打开的是我,*/
										//                                         if (cc.getHisSta() == CCSta.UnRead)
										//                                             BP.WF.Dev2Interface.Node_CC_SetRead(cc.getMyPK());
										//                                         this.Pub1.append("<img src='../Img/Mail_Read.png' border=0/>正在查阅");

									} else {
										this.Pub1.append("<img src='../Img/Mail_UnRead.png' border=0/>抄送未阅");
									}

									this.Pub1.append("无");
									this.Pub1.append(BP.WF.Glo.GenerUserImgSmallerHtml(cc.getCCTo(), cc.getCCToName()));
									this.Pub1.append("无");
								}
								this.Pub1.append(AddHR());
							}
						}
					}

				} else {
					if (wcDesc.getFWCIsShowAllStep() == false)
						continue;

					/*判断该节点下是否有人访问，或者已经设置了抄送与接收人对象, 如果没有就不输出*/
					if (accepts.IsExits(SelectAccperAttr.FK_Node, nd.getNodeID()) == false)
						continue;

					/*未出现的节点.*/
					this.Pub1.append(nd.getName());

					//是否输出了.
					boolean isHaveIt = false;
					for (SelectAccper item : accepts.ToJavaList()) {
						if (item.getFK_Node() != nd.getNodeID())
							continue;
						if (item.getAccType() != 0)
							continue;

						this.Pub1.append("<font style='color:Red;'>执行</font>");
						this.Pub1.append("<font style='color:Red;'>无</font>");

						// 显示要执行的人员。
						this.Pub1.append("<font style='color:Red;'>" + BP.WF.Glo.GenerUserImgSmallerHtml(item.getFK_Emp(), item.getEmpName())
								+ "</font>");

						//info.
						this.Pub1.append("<font style='color:Red;'>" + item.getInfo() + "</font>");
						this.Pub1.append(AddHR());
						isHaveIt = true;
					}

					//#region 输出抄送
					for (SelectAccper item : accepts.ToJavaList()) {
						if (item.getFK_Node() != nd.getNodeID())
							continue;
						if (item.getAccType() != 1)
							continue;
						if (ccls.IsExits(CCListAttr.FK_Node, nd.getNodeID()) == false) {
							this.Pub1.append("<font style='color:Red;'>抄送</font>");
							this.Pub1.append("<font style='color:Red;'>无</font>");
							// 显示要执行的人员。
							this.Pub1.append("<font style='color:Red;'>" + BP.WF.Glo.GenerUserImgSmallerHtml(item.getFK_Emp(), item.getEmpName())
									+ "</font>");

							//info.
							this.Pub1.append("<font style='color:Red;'>" + item.getInfo() + "</font>");
							this.Pub1.append(AddHR());
							isHaveIt = true;
						} else {
							for (CCList cc : ccls.ToJavaList()) {
								if (cc.getFK_Node() != nd.getNodeID())
									continue;

								if (cc.getHisSta() == CCSta.CheckOver)
									continue;
								if (!cc.getCCTo().equals(item.getFK_Emp()))
									continue;

								this.Pub1.append(AddTR());
								if (cc.getHisSta() == CCSta.Read) {
									if (nd.getIsEndNode() == true) {
										this.Pub1.append("<img src='../Img/Mail_Read.png' border=0/>抄送已阅");
										this.Pub1.append(cc.getCDT()); //读取时间.
										this.Pub1.append(BP.WF.Glo.GenerUserImgSmallerHtml(cc.getCCTo(), cc.getCCToName()));
										this.Pub1.append(cc.getCheckNoteHtml());
									} else {
										continue;
									}
								} else {
									if (WebUser.getNo().equals(cc.getCCTo())) {
										continue;

										/*如果打开的是我,*/
										//                                         if (cc.getHisSta() == CCSta.UnRead)
										//                                             BP.WF.Dev2Interface.Node_CC_SetRead(cc.getMyPK());
										//                                         this.Pub1.append("<img src='../Img/Mail_Read.png' border=0/>正在查阅");
									} else {
										this.Pub1.append("<img src='../Img/Mail_UnRead.png' border=0/>抄送未阅");
									}

									this.Pub1.append("无");
									this.Pub1.append(BP.WF.Glo.GenerUserImgSmallerHtml(cc.getCCTo(), cc.getCCToName()));
									this.Pub1.append("无");
								}
								this.Pub1.append(AddHR());
							}
						}
					}

				}
			}
		} // 输出轨迹.

		if (getIsHidden() == false && wcDesc.getHisFrmWorkCheckSta().equals(FrmWorkCheckSta.Enable) && isCanDo) {
			//this.Pub1.append(AddTable("border=0 style='padding:0px;width:100%;' leftMargin=0 topMargin=0"));
			//this.Pub1.append(AddTR());

			String lab = wcDesc.getFWCOpLabel();
			lab = lab.replace("WebUser.No", WebUser.getNo());
			lab = lab.replace("@WebUser.Name", WebUser.getName());
			lab = lab.replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());

			//this.Pub1.append(AddTD("<div style='float:left'>" + wcDesc.getFWCOpLabel() + "</div><div style='float:right'><a href=javascript:TBHelp('TB_Doc','ND" + getNodeID() + "')" + "><img src='" + BP.WF.Glo.getCCFlowAppPath() + "WF/Img/Emps.gif' align='middle' border=0 />选择词汇</a>&nbsp;&nbsp;</div>"));

			//this.Pub1.append(AddTREnd());

			TextBox tb = new TextBox();
			tb.setId("TB_Doc");
			tb.setTextMode(TextBoxMode.MultiLine);
			tb.addAttr("onblur", "btn_Click()");
			//             tb.OnBlur += new EventHandler(btn_Click);

			tb.addAttr("style", "width:100%;border-color:#E2F6FB;border-style:solid;");
			tb.setRows(3);
			if (!StringHelper.isNullOrEmpty(getDoType()) && getDoType().equals("View")) {
				tb.setReadOnly(true);
			}

			tb.setText(BP.WF.Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getNodeID()));

			if ("同意".equals(tb.getText()))
				tb.setText("");

			if (StringHelper.isNullOrEmpty(tb.getText())) {
				tb.setText(wcDesc.getFWCDefInfo());

				// 以下手机端都不要去处理
				if ("1".equals(this.getIsCC())) {
					/*如果当前工作是抄送. */
					BP.WF.Dev2Interface.WriteTrackWorkCheck(this.getFK_Flow(), this.getNodeID(), this.getWorkID(), this.getFID(), tb.getText(), "抄送");

					//设置当前已经审核完成.
					BP.WF.Dev2Interface.Node_CC_SetSta(this.getNodeID(), this.getWorkID(), WebUser.getNo(), CCSta.CheckOver);

				} else {
					if (wcDesc.getFWCIsFullInfo() == true)
						BP.WF.Dev2Interface.WriteTrackWorkCheck(this.getFK_Flow(), this.getNodeID(), this.getWorkID(), this.getFID(), tb.getText(),
								wcDesc.getFWCOpLabel());
				}
				// 以上手机端都不要去处理.

			}
			if (isExitTb_doc)
			{

				this.Pub1.append(AddTable1("  border=1 style='padding:0px;width:100%;' leftMargin=0 topMargin=0"));
				//配置字段解析
				if (!isNullOrEmpty(wcDesc.getFWCFields()))
				{
					this.Pub1.append(AddTR());
					//不需要常用词汇
					this.Pub1.append(AddTD("<div style='float:left'>" + wcDesc.getFWCOpLabel() + "</div><div style='float:right' onmouseover='UploadFileChange()'>" + uploadJS.toString() + "</div>"));
					this.Pub1.append(AddTREnd());
					//添加字段
					Attrs fwcAttrs = new Attrs(wcDesc.getFWCFields()); //
					String msg = DBAccess.RunSQLReturnStringIsNull("SELECT MSG FROM ND" + Integer.parseInt(this.getFK_Flow()) + "Track WHERE  WorkID='" + this.getWorkID() + "'  AND NDFrom='" + this.getNodeID() + "' AND ActionType=" + ActionType.WorkCheck.getValue(), null);
					this.Pub1.append(AddTR("style='border-color:Red;border-style:solid;'"));
					this.Pub1.append(AddTDBegin());
					Pub pubx = new Pub(getRequest(), getResponse());
					if (isNullOrEmpty(msg) || msg.equals(wcDesc.getFWCDefInfo()))
					{
						this.Pub1.append(pubx.BindAttrs(fwcAttrs));
					}
					else
					{
						AtPara ap = new AtPara(msg.replace(";", "@"));
						this.Pub1.append(pubx.BindAttrs(fwcAttrs, ap));
					}
					this.Pub1.append(AddTDEnd());
					this.Pub1.append(AddTREnd());
				}
				else
				{
					String title = "";
					BP.WF.Node n = new BP.WF.Node(this.getNodeID());
					title = n.getName();

					if (wcDesc.getHisFrmWorkShowModel().equals(FrmWorkShowModel.Table)) //表格模式
					{
						this.Pub1.append(AddTR());
						this.Pub1.append(AddTD(title));
						this.Pub1.append(AddTREnd());
					}
					else //自由模式
					{
						///#region
						this.Pub1.append(AddTR());
						this.Pub1.append(Add("<td rowspan='3' style='width:20px;border:1px solid #D6DDE6;'>" + title + "</td>"));
						this.Pub1.append(AddTREnd());
						///#endregion
					}

					this.Pub1.append(AddTR());
					//需要常用词汇
					//this.Pub1.AddTD("<div style='float:left'>" + wcDesc.FWCOpLabel + "</div><div style='float:left'><a href=javascript:TBHelp('WorkCheck_Doc','ND" + NodeID + "')" + "><img src='" + BP.WF.Glo.CCFlowAppPath + "WF/Img/Emps.gif' width='23px' align='middle' border=0 />选择词汇</a></div>"
					//    + "<div style='float:right' onmouseover='UploadFileChange()'>" + uploadJS.ToString() + "</div>");

					//需要常用词汇.
					this.Pub1.append(AddTD("<div style='float:left'>" + wcDesc.getFWCOpLabel() + "</div><div style='float:right' onmouseover='UploadFileChange()'>" + uploadJS.toString() + "</div>"));

					this.Pub1.append(AddTREnd());

					//意见输入框
					this.Pub1.append(AddTR());
					this.Pub1.append(AddTD(tb));
					this.Pub1.append(AddTREnd());
				}
				this.Pub1.append(AddTableEnd());
			}
		}
			//this.Pub1.append(AddTR());
			//this.Pub1.append(AddTD(tb));
			//this.Pub1.append(AddTREnd());
			this.Pub1.append(AddTableEnd());
		//}

	}

	public void BindFreeModelV1_del(FrmWorkCheck wcDesc) {
		SimpleDateFormat sdf = new SimpleDateFormat("yy年MM月dd日HH时mm分");
		WorkCheck wc = new WorkCheck(this.getFK_Flow(), this.getNodeID(), this.getWorkID(), this.getFID());
		this.Pub1.append(AddTable("border=0 style='padding:0px;width:100%;' leftMargin=0 topMargin=0"));
		if (getIsHidden() == false && wcDesc.getHisFrmWorkCheckSta() == FrmWorkCheckSta.Enable) {
			this.Pub1.append(AddTR());
			this.Pub1.append(AddTD("<div style='float:right'><img src='../Img/Btn/Save.gif' border=0 />保存</div>"));
			this.Pub1.append(AddTREnd());

			TextBox tb = new TextBox();
			tb.setId("TB_Doc");
			tb.setTextMode(TextBoxMode.MultiLine);
			//tb.OnBlur += new EventHandler(btn_Click);
			tb.addAttr("onblur", "btn_Click");
			tb.addAttr("width", "100%");
			tb.setRows(3);
			if (!StringHelper.isNullOrEmpty(getDoType()) && getDoType().equals("View"))
				tb.setReadOnly(true);
			tb.setText(BP.WF.Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getNodeID()));
			if (StringHelper.isNullOrEmpty(tb.getText())) {
				tb.setText(wcDesc.getFWCDefInfo());
				BP.WF.Dev2Interface.WriteTrackWorkCheck(this.getFK_Flow(), this.getNodeID(), this.getWorkID(), this.getFID(), tb.getText(),
						wcDesc.getFWCOpLabel());
			}

			this.Pub1.append(AddTR());
			this.Pub1.append(AddTD(tb));
			this.Pub1.append(AddTREnd());
		}

		if (wcDesc.getFWCListEnable() == false) {
			this.Pub1.append(AddTableEnd());
			return;
		}

		int i = 0;
		Tracks tks = wc.getHisWorkChecks();
		for (Track tk : tks.ToJavaList()) {
			//#region 输出审核.
			if (tk.getHisActionType() == ActionType.WorkCheck) {
				/*如果是审核.*/
				i++;
				ActionType at = tk.getHisActionType();
				Date dtt = BP.DA.DataType.ParseSysDateTime2DateTime(tk.getRDT());

				this.Pub1.append(AddTR());
				this.Pub1.append(AddTDBegin());
				this.Pub1.append(AddB(tk.getNDFromT()));
				this.Pub1.append(AddBR(tk.getMsgHtml()));
				this.Pub1.append(AddBR("<div style='float:right'>" + BP.WF.Glo.GenerUserImgSmallerHtml(tk.getEmpFrom(), tk.getEmpFromT())
						+ " &nbsp;&nbsp;&nbsp; " + sdf.format(dtt) + "</div>"));
				this.Pub1.append(AddTDEnd());
				this.Pub1.append(AddTREnd());
			}
			//#endregion 输出审核.

			//#region 检查是否有子流程.
			if (tk.getHisActionType() == ActionType.StartChildenFlow) {
				/*如果是调用子流程,就要从参数里获取到都是调用了那个子流程，并把他们显示出来.*/
				String[] paras = tk.getTag().split("@");
				String[] p1 = paras[1].split("=");
				String fk_flow = p1[1];

				String[] p2 = paras[2].split("=");
				String workId = p2[1];

				WorkCheck subwc = new WorkCheck(fk_flow, Integer.parseInt(fk_flow + "01"), Long.parseLong(workId), 0);
				Tracks subtks = subwc.getHisWorkChecks();
				for (Track subtk : subtks.ToJavaList()) {
					if (subtk.getHisActionType() == ActionType.WorkCheck) {
						/*如果是审核.*/
						i++;
						ActionType at = subtk.getHisActionType();
						Date dtt = BP.DA.DataType.ParseSysDateTime2DateTime(subtk.getRDT());

						this.Pub1.append(AddTR());
						this.Pub1.append(AddTDBegin());

						this.Pub1.append(AddB(subtk.getNDFromT()));
						this.Pub1.append(AddBR(subtk.getMsgHtml()));
						this.Pub1.append(AddBR("<div style='float:right'>"
								+ BP.WF.Glo.GenerUserImgSmallerHtml(subtk.getEmpFrom(), subtk.getEmpFromT()) + " &nbsp;&nbsp;&nbsp; "
								+ sdf.format(dtt) + "</div>"));

						this.Pub1.append(AddTDEnd());
						this.Pub1.append(AddTREnd());

					}
				}
			}
			//#endregion 检查是否有子流程.

		}
		this.Pub1.append(AddTableEnd());
	}

	public void BindTableModel(FrmWorkCheck wcDesc) {
		SimpleDateFormat sdf = new SimpleDateFormat("yy年MM月dd日HH时mm分");
		WorkCheck wc = new WorkCheck(this.getFK_Flow(), this.getNodeID(), this.getWorkID(), this.getFID());

		this.Pub1.append(AddTable1("border=1 style='padding:0px;width:100%;'"));
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD("colspan=8",
				"<div style='float:left'>审批意见</div> <div style='float:right'><img src='../Img/Btn/Save.gif' border=0 /></div>"));
		this.Pub1.append(AddTREnd());

		if (!getIsHidden()) {
			TextBox tb = new TextBox();
			tb.setId("TB_Doc");
			tb.setTextMode(TextBoxMode.MultiLine);
			//tb.OnBlur += new EventHandler(btn_Click);
			tb.addAttr("onblur", "btn_Click()");
			tb.addAttr("style", "width:100%");
			tb.setRows(3);
			if (!StringHelper.isNullOrEmpty(getDoType()) && "View".equals(getDoType()))
				tb.setReadOnly(true);

			tb.setText(BP.WF.Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getNodeID()));

			this.Pub1.append(AddTD("colspan=8", tb));
			this.Pub1.append(AddTREnd());
		}

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD("IDX"));
		this.Pub1.append(AddTD("发生时间"));
		this.Pub1.append(AddTD("发生节点"));
		//   this.Pub1.AddTD("人员");
		this.Pub1.append(AddTD("活动"));
		this.Pub1.append(AddTD("信息/审批意见"));
		this.Pub1.append(AddTD("执行人"));
		this.Pub1.append(AddTREnd());

		int i = 0;
		Tracks tks = wc.getHisWorkChecks();
		for (Track tk : tks.ToJavaList()) {
			if (tk.getHisActionType() == ActionType.Forward || tk.getHisActionType() == ActionType.ForwardFL
					|| tk.getHisActionType() == ActionType.ForwardHL) {
				String nd = "" + tk.getNDFrom();
				if (nd.substring(nd.length() - 2) != "01")
					continue;
				//string len=tk.NDFrom.ToString();
				//if (
				//if (tk.NDFrom.ToString().Contains
			}

			if (tk.getHisActionType() != ActionType.WorkCheck)
				continue;

			i++;
			this.Pub1.append(AddTR());
			this.Pub1.append(AddTD(i));
			Date dtt = BP.DA.DataType.ParseSysDateTime2DateTime(tk.getRDT());
			this.Pub1.append(AddTD(sdf.format(dtt)));
			this.Pub1.append(AddTD(tk.getNDFromT()));
			//  this.Pub1.AddTD(tk.EmpFromT);
			ActionType at = tk.getHisActionType();
			//this.Pub1.AddTD("<img src='./../Img/Action/" + at.ToString() + ".png' class='ActionType' width='16px' border=0/>" + BP.WF.Track.GetActionTypeT(at));
			this.Pub1.append(AddTD("<img src='./../Img/Action/" + at.toString() + ".png' class='ActionType' width='16px' border=0/>"
					+ tk.getActionTypeText()));
			this.Pub1.append(AddTD(tk.getMsgHtml()));
			this.Pub1.append(AddTD(tk.getExer())); //如果是委托，增加一个  人员(委托))
			this.Pub1.append(AddTREnd());
		}
		this.Pub1.append(AddTableEnd());
	}

	//展示空表单
	private void ViewEmptyForm() {
		this.Pub1.append(AddTable1(" border=1 style='padding:0px;width:100%;'"));
		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD("colspan=6 style='text-align:left' ", "审批意见"));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTR());
		this.Pub1.append(AddTD("IDX"));
		this.Pub1.append(AddTD("发生时间"));
		this.Pub1.append(AddTD("发生节点"));
		this.Pub1.append(AddTD("活动"));
		this.Pub1.append(AddTD("信息/审批意见"));
		this.Pub1.append(AddTD("执行人"));
		this.Pub1.append(AddTREnd());

		this.Pub1.append(AddTableEnd());
	}

	public final void BindSignModel(FrmWorkCheck wcDesc) {

		Node currentNode = new Node(this.getNodeID());

		this.Pub1.append(AddTable1(" style='border:0px solie white'  cellspacing='0' cellpadding='0' "));
		if (!getIsHidden() && wcDesc.getHisFrmWorkCheckSta() == FrmWorkCheckSta.Enable) {

			this.Pub1.append(AddTR());
			this.Pub1.append(AddTD("colspan=2 style='font-weight:bold;'", currentNode.getName() + ":"));

			this.Pub1.append(AddTREnd());
			this.Pub1.append(AddTR());
			TextBox tb = new TextBox();
			tb.setId("TB_Doc");
			tb.setTextMode(TextBoxMode.MultiLine);
			tb.attributes.put("onblur", "width:100%");
			tb.setRows(3);
			if (getDoType() != null && getDoType().equals("View")) {
				tb.setReadOnly(true);
			}

			String text = null;
			if (currentNode.getTodolistModel() == TodolistModel.Order) {
				text = Dev2Interface.GetOrderCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getNodeID());
			} else {
				text = BP.WF.Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getNodeID());
			}
			tb.setText(StringHelper.isNullOrEmpty(text) ? "同意" : text);

			if (currentNode.getTodolistModel() == TodolistModel.Order) {
				InsertDefault(tb.getText());
			} else {
				if (StringHelper.isNullOrEmpty(text)) {
					InsertDefault("同意");
				}
			}

			this.Pub1.append(AddTD("colspan=2", tb));
			this.Pub1.append(AddTREnd());

			this.Pub1.append(AddTR());
			String v = WebUser.getNo();

			this.Pub1.append(AddTD("<img src='" + BP.WF.Glo.getCCFlowAppPath() + "DataUser/Siganture/" + v + ".jpg' border=0 onerror=\"this.src='"
					+ BP.WF.Glo.getCCFlowAppPath() + "DataUser/Siganture/UnName.jpg'\"/>"));
			this.Pub1.append(AddTD(DataType.getCurrentDateByFormart("yyyy-MM-dd")));

			this.Pub1.append(AddTREnd());
		}

		int i = 0;
		WorkCheck wc = new WorkCheck(getFK_Flow(), getNodeID(), getWorkID(), getFID());
		Tracks tks = wc.getHisWorkChecks();
		java.util.ArrayList<Track> list = new java.util.ArrayList<Track>();

		boolean isFirst = false;
		for (Track tk : tks.ToJavaList()) {
			if (tk.getHisActionType() == ActionType.Forward || tk.getHisActionType() == ActionType.ForwardFL
					|| tk.getHisActionType() == ActionType.ForwardHL) {
				String nd = String.valueOf(tk.getNDFrom());
				if (!nd.substring(nd.length() - 2).equals("01")) {
					continue;
				}
				//string len=tk.NDFrom.ToString();
				//if (
				//if (tk.NDFrom.ToString().Contains
			}

			if (tk.getHisActionType() != ActionType.WorkCheck) {
				continue;
			}

			Node node = new Node(tk.getNDFrom());
			if (!getIsHidden() && wcDesc.getHisFrmWorkCheckSta() == FrmWorkCheckSta.Enable && node.getNodeID() == this.getNodeID()) {
				if (i == 0) {

					if (node.getTodolistModel() == TodolistModel.Order) {
						for (Track singleTk : tks.ToJavaList()) {
							if (singleTk.getHisActionType() != ActionType.WorkCheck) {
								continue;
							}

							if (singleTk.getNDFrom() == tk.getNDFrom() && singleTk.getExer() != tk.getExer()) {
								this.Pub1.append(AddTR());

								java.util.Date singleDtt = BP.DA.DataType.ParseSysDateTime2DateTime(tk.getRDT());

								this.Pub1
										.append(AddTD("<img src='" + BP.WF.Glo.getCCFlowAppPath() + "DataUser/Siganture/" + singleTk.getEmpFrom()
												+ ".jpg' border=0 onerror=\"this.src='" + BP.WF.Glo.getCCFlowAppPath()
												+ "DataUser/Siganture/UnName.jpg'\"/>")); //如果是委托，增加一个 人员(委托)

								this.Pub1.append(AddTD(DataType.getDateByFormart(singleDtt, "yyyy-MM-dd")));

								this.Pub1.append(AddTREnd());
								if (!list.contains(singleTk)) {
									list.add(singleTk);
								}
							}

						}
						i++;
						isFirst = true;
						continue;
					}
					i++;
					isFirst = false;
					continue;
				}
			}

			if (list.contains(tk)) {
				continue;
			}
			this.Pub1.append(AddTR());
			this.Pub1.append(AddTD("colspan=2 style='font-weight:bold;'", node.getName() + ":"));

			this.Pub1.append(AddTREnd());

			if (node.getTodolistModel() == TodolistModel.Order) {

				if (!isFirst) {
					this.Pub1.append(AddTR());

					this.Pub1.append(AddTD("colspan=2 style='height:50px'", tk.getMsgHtml()));
					this.Pub1.append(AddTREnd());

					this.Pub1.append(AddTR());

					java.util.Date singleDtt = BP.DA.DataType.ParseSysDateTime2DateTime(tk.getRDT());

					this.Pub1.append(AddTD("<img src='" + BP.WF.Glo.getCCFlowAppPath() + "DataUser/Siganture/" + tk.getEmpFrom()
							+ ".jpg' border=0 onerror=\"this.src='" + BP.WF.Glo.getCCFlowAppPath() + "DataUser/Siganture/UnName.jpg'\"/>")); //如果是委托，增加一个 人员(委托)

					this.Pub1.append(AddTD(DataType.getDateByFormart(singleDtt, "yyyy-MM-dd")));

					this.Pub1.append(AddTREnd());
					if (!list.contains(tk)) {
						list.add(tk);
					}
				}

				for (Track singleTk : tks.ToJavaList()) {
					if (singleTk.getHisActionType() != ActionType.WorkCheck) {
						continue;
					}

					if (singleTk.getNDFrom() == tk.getNDFrom() && !singleTk.getExer().equals(tk.getExer())) {
						this.Pub1.append(AddTR());

						java.util.Date singleDtt = BP.DA.DataType.ParseSysDateTime2DateTime(tk.getRDT());

						this.Pub1.append(AddTD("<img src='" + BP.WF.Glo.getCCFlowAppPath() + "DataUser/Siganture/" + singleTk.getEmpFrom()
								+ ".jpg' border=0 onerror=\"this.src='" + BP.WF.Glo.getCCFlowAppPath() + "DataUser/Siganture/UnName.jpg'\"/>")); //如果是委托，增加一个 人员(委托)

						this.Pub1.append(AddTD(DataType.getDateByFormart(singleDtt, "yyyy-MM-dd")));

						this.Pub1.append(AddTREnd());
						if (!list.contains(singleTk)) {
							list.add(singleTk);
						}
					}

				}
				i++;
				continue;
			} else {
				this.Pub1.append(AddTR());

				this.Pub1.append(AddTD("colspan=2 style='height:50px'", tk.getMsgHtml()));
				this.Pub1.append(AddTREnd());
			}

			this.Pub1.append(AddTR());

			java.util.Date dtt = BP.DA.DataType.ParseSysDateTime2DateTime(tk.getRDT());

			this.Pub1.append(AddTD("<img src='" + BP.WF.Glo.getCCFlowAppPath() + "DataUser/Siganture/" + tk.getEmpFrom()
					+ ".jpg' border=0 onerror=\"this.src='" + BP.WF.Glo.getCCFlowAppPath() + "DataUser/Siganture/UnName.jpg'\"/>")); //如果是委托，增加一个 人员(委托)

			this.Pub1.append(AddTD(DataType.getDateByFormart(dtt, "yyyy-MM-dd")));

			this.Pub1.append(AddTREnd());
			i++;
		}
		this.Pub1.append(AddTableEnd());
	}

	private void InsertDefault(String msg) {
		//查看时取消保存
		if (getDoType() != null && getDoType().equals("View")) {
			return;
		}

		//内容为空，取消保存
		if (StringHelper.isNullOrEmpty(msg)) {
			return;
		}
		// 加入审核信息.

		FrmWorkCheck wcDesc = new FrmWorkCheck(this.getNodeID());

		// 处理人大的需求，需要把审核意见写入到FlowNote里面去.
		String sql = "UPDATE WF_GenerWorkFlow SET FlowNote='" + msg + "' WHERE WorkID=" + this.getWorkID();
		BP.DA.DBAccess.RunSQL(sql);

		// 判断是否是抄送?
		if (this.getIsCC().equals("1")) {
			// 写入审核信息，有可能是update数据。
			BP.WF.Dev2Interface
					.WriteTrackWorkCheck(this.getFK_Flow(), this.getNodeID(), this.getWorkID(), this.getFID(), msg, wcDesc.getFWCOpLabel());

			//设置抄送状态 - 已经审核完毕.
			BP.WF.Dev2Interface.Node_CC_SetSta(this.getNodeID(), this.getWorkID(), WebUser.getNo(), CCSta.CheckOver);
		} else {
			BP.WF.Dev2Interface
					.WriteTrackWorkCheck(this.getFK_Flow(), this.getNodeID(), this.getWorkID(), this.getFID(), msg, wcDesc.getFWCOpLabel());
		}

	}
	//------------------------------------------------------------------------------------
		//	This method replaces the .NET static string method 'IsNullOrEmpty'.
		//------------------------------------------------------------------------------------
		public static boolean isNullOrEmpty(String string)
		{
			return string == null || string.equals("");
		}

		//------------------------------------------------------------------------------------
		//	This method replaces the .NET static string method 'Join' (2 parameter version).
		//------------------------------------------------------------------------------------
		public static String join(String separator, String[] stringarray)
		{
			if (stringarray == null)
				return null;
			else
				return join(separator, stringarray, 0, stringarray.length);
		}

		//------------------------------------------------------------------------------------
		//	This method replaces the .NET static string method 'Join' (4 parameter version).
		//------------------------------------------------------------------------------------
		public static String join(String separator, String[] stringarray, int startindex, int count)
		{
			String result = "";

			if (stringarray == null)
				return null;

			for (int index = startindex; index < stringarray.length && index - startindex < count; index++)
			{
				if (separator != null && index > startindex)
					result += separator;

				if (stringarray[index] != null)
					result += stringarray[index];
			}

			return result;
		}

		//------------------------------------------------------------------------------------
		//	This method replaces the .NET static string method 'TrimEnd'.
		//------------------------------------------------------------------------------------
		public static String trimEnd(String string, Character... charsToTrim)
		{
			if (string == null || charsToTrim == null)
				return string;

			int lengthToKeep = string.length();
			for (int index = string.length() - 1; index >= 0; index--)
			{
				boolean removeChar = false;
				if (charsToTrim.length == 0)
				{
					if (Character.isWhitespace(string.charAt(index)))
					{
						lengthToKeep = index;
						removeChar = true;
					}
				}
				else
				{
					for (int trimCharIndex = 0; trimCharIndex < charsToTrim.length; trimCharIndex++)
					{
						if (string.charAt(index) == charsToTrim[trimCharIndex])
						{
							lengthToKeep = index;
							removeChar = true;
							break;
						}
					}
				}
				if ( ! removeChar)
					break;
			}
			return string.substring(0, lengthToKeep);
		}

		//------------------------------------------------------------------------------------
		//	This method replaces the .NET static string method 'TrimStart'.
		//------------------------------------------------------------------------------------
		public static String trimStart(String string, Character... charsToTrim)
		{
			if (string == null || charsToTrim == null)
				return string;

			int startingIndex = 0;
			for (int index = 0; index < string.length(); index++)
			{
				boolean removeChar = false;
				if (charsToTrim.length == 0)
				{
					if (Character.isWhitespace(string.charAt(index)))
					{
						startingIndex = index + 1;
						removeChar = true;
					}
				}
				else
				{
					for (int trimCharIndex = 0; trimCharIndex < charsToTrim.length; trimCharIndex++)
					{
						if (string.charAt(index) == charsToTrim[trimCharIndex])
						{
							startingIndex = index + 1;
							removeChar = true;
							break;
						}
					}
				}
				if ( ! removeChar)
					break;
			}
			return string.substring(startingIndex);
		}

		//------------------------------------------------------------------------------------
		//	This method replaces the .NET static string method 'Trim' when arguments are used.
		//------------------------------------------------------------------------------------
		public static String trim(String string, Character... charsToTrim)
		{
			return trimEnd(trimStart(string, charsToTrim), charsToTrim);
		}

		//------------------------------------------------------------------------------------
		//	This method is used for string equality comparisons when the option
		//	'Use helper 'stringsEqual' method to handle null strings' is selected
		//	(The Java String 'equals' method can't be called on a null instance).
		//------------------------------------------------------------------------------------
		public static boolean stringsEqual(String s1, String s2)
		{
			if (s1 == null && s2 == null)
				return true;
			else
				return s1 != null && s1.equals(s2);
		}
		
		public static boolean strDate(String s1) throws ParseException{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
			//java.util.Date date=sdf.parse(s1);
			String s = sdf.format(new Date());
			if(s1.equals(s)){
				return true;
			}
			return false;
		} 
		
		 /** 
		 添加附件
		 
		 @param athDBs
		 @param tk
*/
		private void AddTDOfFrmAttachMent(FrmAttachmentDBs athDBs, Track tk)
		{
			String strFiles = "";
			for (FrmAttachmentDB athDB : athDBs.ToJavaList())
			{
				boolean isDelete = false;
				if (!(tk.getNDFrom()+"").equals(athDB.getFK_MapData()))
				{
					continue;
				}
				//只有本节点和自己才可以删除
				if ((this.getNodeID()+"").equals(athDB.getFK_MapData()) && athDB.getRec().equals(WebUser.getNo()))
				{
					isDelete = true;
				}
				if (getDoType() != null && getDoType().equals("View"))
				{
					isDelete = false;
				}

				String href = GetFileAction(athDB);
				if (isDelete == true)
				{
					strFiles += "<div style='margin:5px;'><img alt='删除' align='middle' src='../Img/Btn/Delete.gif' onclick=\"DelWorkCheckAth('" + athDB.getMyPK() + "')\" />&nbsp;&nbsp;<a style='color:Blue; font-size:14;' href=\"" + href + "\">" + athDB.getFileName() + "&nbsp;&nbsp;<img src='../Img/FileType/" + athDB.getFileExts() + ".gif' border=0 onerror=\"src='../Img/FileType/Undefined.gif'\" /></a></div>";
					continue;
				}
				strFiles += "<div style='margin:5px;'><a style='color:Blue; font-size:14;' href=\"" + href + "\">" + athDB.getFileName() + "&nbsp;&nbsp;<img src='../Img/FileType/" + athDB.getFileExts() + ".gif' border=0 onerror=\"src='../Img/FileType/Undefined.gif'\" /></a></div>";
			}
			//存在附件则显示
			if (!strFiles.equals(""))
			{
				this.Pub1.append(AddTR());
				this.Pub1.append(Add("<td style='WORD-WRAP: break-word;'><b>附件：</b>" + strFiles + "</td>"));
				this.Pub1.append(AddTREnd());
			}
		}
		
		 /** 
		 获取文件打开方式
		 
		 @param athDB
		 @return 
*/
		private String GetFileAction(FrmAttachmentDB athDB)
		{
			if (athDB == null || athDB.getFileExts().equals(""))
			{
				return "#";
			}

			FrmAttachment athDesc = new FrmAttachment(athDB.getFK_FrmAttachment());
//			switch (athDB.FileExts)
//ORIGINAL LINE: case "doc":
			if (athDB.getFileExts().equals("doc") || athDB.getFileExts().equals("docx") || athDB.getFileExts().equals("xls") || athDB.getFileExts().equals("xlsx"))
			{
					return "javascript:AthOpenOfiice('" + athDB.getFK_FrmAttachment() + "','" + this.getWorkID() + "','" + athDB.getMyPK() + "','" + athDB.getFK_MapData() + "','" + athDB.getFK_FrmAttachment() + "','" + this.getFK_Node() + "')";
			}
//ORIGINAL LINE: case "txt":
			else if (athDB.getFileExts().equals("txt") || athDB.getFileExts().equals("jpg") || athDB.getFileExts().equals("jpeg") || athDB.getFileExts().equals("gif") || athDB.getFileExts().equals("png") || athDB.getFileExts().equals("bmp") || athDB.getFileExts().equals("ceb"))
			{
					return "javascript:AthOpenView('" + athDB.getRefPKVal() + "','" + athDB.getMyPK() + "','" + athDB.getFK_FrmAttachment() + "','" + athDB.getFileExts() + "','" + this.getFK_Flow() + "','" + athDB.getFK_MapData() + "','" + this.getWorkID() + "','false')";
			}
//ORIGINAL LINE: case "pdf":
			else if (athDB.getFileExts().equals("pdf"))
			{
					return athDesc.getSaveTo() + this.getWorkID() + "/" + athDB.getMyPK() + "." + athDB.getFileName();
			}
			return "javascript:AthDown('" + athDB.getFK_FrmAttachment() + "','" + this.getWorkID() + "','" + athDB.getMyPK() + "','" + athDB.getFK_MapData() + "','" + this.getFK_Flow() + "','" + athDB.getFK_FrmAttachment() + "')";
		}

}

// //自定义控件
// public class PostBackTextBox : System.Web.UI.WebControls.TextBox, System.Web.UI.IPostBackEventHandler
// {
//     protected override void Render(System.Web.UI.HtmlTextWriter writer)
//     {
//         Attributes["onblur"] = Page.GetPostBackEventReference(this);
//         base.Render(writer);
//     }
//
//     public event EventHandler OnBlur;
//
//     public virtual void RaisePostBackEvent(string eventArgument)
//     {
//         if (OnBlur != null)
//         {
//             OnBlur(this, null);
//         }
//     }
//}
