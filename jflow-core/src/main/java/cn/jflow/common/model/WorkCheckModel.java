package cn.jflow.common.model;

import java.text.SimpleDateFormat;

import cn.jflow.common.util.ContextHolderUtils;
import BP.Tools.StringHelper;
import BP.WF.ActionType;
import BP.WF.Node;
import BP.WF.Nodes;
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
import BP.WF.Template.SelectAccper;
import BP.WF.Template.SelectAccperAttr;
import BP.WF.Template.SelectAccpers;
import BP.Web.WebUser;

public class WorkCheckModel {

	public StringBuilder Pub1 = null;
	
	public final boolean getIsHidden()
	{
		try
		{
			if (getDoType().equals("View"))
			{
				return true;
				
			}
			return Boolean.parseBoolean(ContextHolderUtils.getRequest().getParameter("IsHidden"));
		}
		catch (RuntimeException e)
		{
			return false;
		}
	}
	public final int getNodeID()
	{
		try
		{
			return Integer.parseInt(ContextHolderUtils.getRequest().getParameter("FK_Node"));
		}
		catch (java.lang.Exception e)
		{
			return 0;
		}
	}

	/** 
	 工作ID
	 
	*/
	public final long getWorkID()
	{
		String workid = ContextHolderUtils.getRequest().getParameter("OID");
		if (workid == null)
		{
			workid = ContextHolderUtils.getRequest().getParameter("WorkID");
		}
		return Long.parseLong(workid);
	}
	public final long getFID()
	{
		String workid = ContextHolderUtils.getRequest().getParameter("FID");
		if (StringHelper.isNullOrEmpty(workid))
		{
			return 0;
		}
		return Long.parseLong(workid);
	}

	/** 
	 流程编号
	 
	*/
	public final String getFK_Flow()
	{
		return ContextHolderUtils.getRequest().getParameter("FK_Flow");
	}

	/** 
	 操作View
	 
	*/
	public final String getDoType()
	{
		return ContextHolderUtils.getRequest().getParameter("DoType");
	}
	/** 
	 是否是抄送.
	 
	*/
	public final boolean getIsCC()
	{
		String s = ContextHolderUtils.getRequest().getParameter("Paras");
		if (s == null)
		{
			return false;
		}

		if (s.contains("IsCC"))
		{
			return true;
		}
		return false;
	}


	public  void init( )
	{
		// 小周鹏20141227 Add---Start
		this.Pub1 = new StringBuilder();
		// 小周鹏20141227 Add---End
		
		//工作流编号不存在绑定空框架.
		if (this.getFK_Flow() == null)
		{
			ViewEmptyForm();
			return;
		}

		//审批节点.
		FrmWorkCheck wcDesc = new FrmWorkCheck(this.getNodeID());
		if (wcDesc.getHisFrmWorkShowModel() ==  FrmWorkShowModel.Free)
		{
			this.BindFreeModel(wcDesc);
		}
		else
		{
			this.BindFreeModel(wcDesc);
		}

		// this.BindTableModel(wcDesc);
	}
	/** 
	 实现的功能：
	 1，显示轨迹表。
	 2，如果启用了审核，就把审核信息显示出来。
	 3，如果启用了抄送，就把抄送的人显示出来。
	 4，可以把子流程的信息与处理的结果显示出来。
	 5，可以把子线程的信息列出来。
	 6，可以把未来到达节点处理人显示出来。
	 
	 @param wcDesc
	*/
	public final void BindFreeModel(FrmWorkCheck wcDesc)
	{
		WorkCheck wc = null;
		if (getFID() != 0)
		{
			wc = new WorkCheck(this.getFK_Flow(), this.getNodeID(), this.getFID(), 0);
		}
		else
		{
			wc = new WorkCheck(this.getFK_Flow(), this.getNodeID(), this.getWorkID(), this.getFID());
		}

		boolean isCanDo = BP.WF.Dev2Interface.Flow_IsCanDoCurrentWork(this.getFK_Flow(), this.getNodeID(), this.getWorkID(), WebUser.getNo());

		// region 处理审核意见框.
		if (getIsHidden() == false && wcDesc.getHisFrmWorkCheckSta() == FrmWorkCheckSta.Enable&& isCanDo)
		{
			this.Pub1.append(BaseModel.AddTable("border=0 style='padding:0px;width:100%;' leftMargin=0 topMargin=0"));
			this.Pub1.append(BaseModel.AddTR());
			this.Pub1.append(BaseModel.AddTDTitle("<div style='float:left'>" + wcDesc.getFWCOpLabel() + "</div><div style='float:right'><a href=javascript:TBHelp('TB_Doc')><img src='" + BP.WF.Glo.getCCFlowAppPath() + "WF/Img/Emps.gif' align='middle' border=0 />选择词汇</a>&nbsp;&nbsp;</div>"));
			this.Pub1.append(BaseModel.AddTREnd());

			
			boolean tbReadOnly = false;
			if (getDoType() != null && getDoType().equals("View"))
			{
				tbReadOnly = true;
			}
			String tbText = "";
			tbText = BP.WF.Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getNodeID());

			if (tbText.equals("同意"))
			{
				tbText = "";
			}
			

			if (StringHelper.isNullOrEmpty(tbText))
            {
				tbText = wcDesc.getFWCDefInfo();
				// 以下手机端都不要去处理
				if (this.getIsCC())
				{
					//如果当前工作是抄送. 
					BP.WF.Dev2Interface.WriteTrackWorkCheck(this.getFK_Flow(), this.getNodeID(), this.getWorkID(), this.getFID(), tbText, "抄送");

					//设置当前已经审核完成.
					BP.WF.Dev2Interface.Node_CC_SetSta(this.getNodeID(), this.getWorkID(), WebUser.getNo(), CCSta.CheckOver);

				}
				else
				{
					if (wcDesc.getFWCIsFullInfo())
					{
						BP.WF.Dev2Interface.WriteTrackWorkCheck(this.getFK_Flow(), this.getNodeID(), this.getWorkID(), this.getFID(), tbText, wcDesc.getFWCOpLabel());
					}
				}
            }
			// 以上手机端都不要去处理.


			this.Pub1.append(BaseModel.AddTR());
			this.Pub1.append(BaseModel.AddPostBackTextBox("TB_Doc", tbReadOnly, tbText));
			this.Pub1.append(BaseModel.AddTREnd());
			this.Pub1.append(BaseModel.AddTableEnd());
		}

		if (!wcDesc.getFWCListEnable())
		{
			return; // 历史审核信息是否显示? 不显示就return. 
		}


		//求轨迹表.
		BP.WF.Entity.Tracks tks = wc.getHisWorkChecks();

		//求抄送列表,把抄送的信息与抄送的读取状态显示出来.
		CCLists ccls = new CCLists(this.getFK_Flow(), this.getWorkID(), this.getFID());

		//查询出来未来节点处理人信息,以方便显示未来没有运动到节点轨迹.
		long wfid = this.getWorkID();
		if (this.getFID() != 0)
		{
			wfid = this.getFID();
		}

		//获得 节点处理人数据。
		SelectAccpers accepts = new SelectAccpers(wfid);

		//取出来该流程的所有的节点。
		Nodes nds = new Nodes(this.getFK_Flow());
		Nodes ndsOrder = new Nodes();
		//求出已经出现的步骤.
		String nodes = ""; //已经出现的步骤.

		for (BP.WF.Entity.Track tk : tks.ToJavaList())
		{
			switch (tk.getHisActionType())
			{
				case Forward:
				case WorkCheck:
					if (nodes.contains(tk.getNDFrom() + ",") == false)
					{
						//ndsOrder.AddEntity(nds.GetEntityByKey(tk.getNDFrom()));
						nodes += tk.getNDFrom() + ",";
					}
					break;
				case StartChildenFlow:
					if (nodes.contains(tk.getNDFrom() + ",") == false)
					{
						//ndsOrder.AddEntity(nds.GetEntityByKey(tk.getNDFrom()));
						nodes += tk.getNDFrom() + ",";
					}
					break;
				default:
					continue;
			}
		}
		this.Pub1.append(BaseModel.AddTable("border=0 style='padding:0px;width:100%;' leftMargin=0 topMargin=0"));

		int biaoji = 0;
		for (Node nd : nds.ToJavaList())
		{
			if (nodes.contains(nd.getNodeID() + ",") == true)
			{
				//已经处理过..
				this.Pub1.append(BaseModel.AddTR());

				this.Pub1.append(BaseModel.AddTDBegin("colspan=4"));
				this.Pub1.append(BaseModel.AddTable("border=0 style='padding:0px;width:100%;' leftMargin=0 topMargin=0 id='tb" + nd.getNodeID() + "'"));

				//未出现的节点.
				this.Pub1.append(BaseModel.AddTR());
				this.Pub1.append(BaseModel.AddTDTitle("colspan=4", "<div style='float:left'><image src='../Img/Tree/Cut.gif' onclick=\"show_and_hide_tr('tb" + nd.getNodeID() + "',this);\" style='cursor:pointer;'></image>" + nd.getName() + "</div>"));
				this.Pub1.append(BaseModel.AddTREnd());

				this.Pub1.append(BaseModel.AddTR("style='font-size:14px;font-weight:bold;'"));
				this.Pub1.append(BaseModel.AddTD("width='50' style='font-weight:bold;'", "事件"));
				this.Pub1.append(BaseModel.AddTD("width='150' style='font-weight:bold;'", "时间"));
				this.Pub1.append(BaseModel.AddTD("width='100' style='font-weight:bold;'", "操作员"));
				this.Pub1.append(BaseModel.AddTD("style='font-weight:bold;'", "信息"));
				this.Pub1.append(BaseModel.AddTREnd());

				//输出发送审核信息与抄送信息.
				String emps = "";
				String empsorder = ""; //保存队列显示中的人员，做判断，避免重复显示
				String empcheck = ""; //记录当前节点已经输出的

				for (Track tk : tks.ToJavaList())
				{
					if (tk.getNDFrom() != nd.getNodeID())
					{
						continue;
					}

					///#region 如果是前进，并且当前节点没有启用审核组件
					if (tk.getHisActionType() == ActionType.Forward)
					{
						FrmWorkCheck fwc = new FrmWorkCheck(nd.getNodeID());
						if (fwc.getHisFrmWorkCheckSta() == FrmWorkCheckSta.Disable)
						{
							this.Pub1.append(BaseModel.AddTR());
							this.Pub1.append(BaseModel.AddTD("<img src='../Img/Mail_Read.png' border=0 />" + tk.getActionTypeText()));
							this.Pub1.append(BaseModel.AddTD(tk.getRDT()));

							if (wcDesc.getSigantureEnabel())
							{
								this.Pub1.append(BaseModel.AddTD(BP.WF.Glo.GenerUserSigantureHtml(tk.getEmpFrom(), tk.getEmpFromT())));
							}
							else
							{
								this.Pub1.append(BaseModel.AddTD(BP.WF.Glo.GenerUserImgSmallerHtml(tk.getEmpFrom(), tk.getEmpFromT())));
							}

							this.Pub1.append(BaseModel.AddTD(tk.getMsgHtml()));
							this.Pub1.append(BaseModel.AddTREnd());
							continue;
						}
					}
					///#endregion

					if (tk.getHisActionType() != ActionType.WorkCheck && tk.getHisActionType() != ActionType.StartChildenFlow)
					{
						continue;
					}

					emps += tk.getEmpFrom() + ",";

					if (tk.getHisActionType() == ActionType.WorkCheck)
					{
						///#region 显示出来队列流程中未审核的那些人.
						if (nd.getTodolistModel()== TodolistModel.Order)
						{
							// 如果是队列流程就要显示出来未审核的那些人.
							String empsNodeOrder = ""; //记录当前节点队列访问未执行的人员

							GenerWorkerLists gwls = new GenerWorkerLists(this.getWorkID());
							for (GenerWorkerList item : gwls.ToJavaList())
							{
								if (item.getFK_Node() == nd.getNodeID())
								{
									empsNodeOrder += item.getFK_Emp();
								}
							}

							for (SelectAccper accper : accepts.ToJavaList())
							{
								if (empsorder.contains(accper.getFK_Emp()) == true)
								{
									continue;
								}
								if (empsNodeOrder.contains(accper.getFK_Emp()) == false)
								{
									continue;
								}
								if (tk.getEmpFrom() == accper.getFK_Emp())
								{
									//审核信息,首先输出它.
									this.Pub1.append(BaseModel.AddTR());
									this.Pub1.append(BaseModel.AddTD("<img src='../Img/Mail_Read.png' border=0/>" + tk.getActionTypeText()));
									this.Pub1.append(BaseModel.AddTD(tk.getRDT()));
									//this.Pub1.append(BaseControlModel.AddTD(tk.getEmpFrom()T);
									this.Pub1.append(BaseModel.AddTD(BP.WF.Glo.GenerUserImgSmallerHtml(tk.getEmpFrom(), tk.getEmpFromT())));
									this.Pub1.append(BaseModel.AddTD(tk.getMsgHtml()));
									this.Pub1.append(BaseModel.AddTREnd());
									empcheck += tk.getEmpFrom();
								}
								else
								{
									this.Pub1.append(BaseModel.AddTR());
									if (accper.getAccType() == 0)
									{
										this.Pub1.append(BaseModel.AddTD("style='color:Red;'", "执行"));
									}
									else
									{
										this.Pub1.append(BaseModel.AddTD("style='color:Red;'", "抄送"));
									}
									this.Pub1.append(BaseModel.AddTD("style='color:Red;'", "无"));
									this.Pub1.append(BaseModel.AddTD("style='color:Red;'", BP.WF.Glo.GenerUserImgSmallerHtml(accper.getFK_Emp(), accper.getEmpName())));
									this.Pub1.append(BaseModel.AddTD("style='color:Red;'", accper.getInfo()));
									this.Pub1.append(BaseModel.AddTREnd());
									empsorder += accper.getFK_Emp();
								}
							}
						}
						///#endregion 显示出来队列流程中未审核的那些人.
						else
						{
							//审核信息,首先输出它.
							this.Pub1.append(BaseModel.AddTR());
							this.Pub1.append(BaseModel.AddTD("<img src='../Img/Mail_Read.png' border=0/>" + tk.getActionTypeText()));
							this.Pub1.append(BaseModel.AddTD(tk.getRDT()));
							//this.Pub1.append(BaseControlModel.AddTD(tk.getEmpFrom()T);

							if (wcDesc.getSigantureEnabel() == true)
							{
								this.Pub1.append(BaseModel.AddTD(BP.WF.Glo.GenerUserSigantureHtml(tk.getEmpFrom(), tk.getEmpFromT())));
							}
							else
							{
								this.Pub1.append(BaseModel.AddTD(BP.WF.Glo.GenerUserImgSmallerHtml(tk.getEmpFrom(), tk.getEmpFromT())));
							}
							this.Pub1.append(BaseModel.AddTDBigDoc(tk.getMsgHtml()));
							this.Pub1.append(BaseModel.AddTREnd());
							empcheck += tk.getEmpFrom();
						}
					}

					///#region 检查是否有调用子流程的情况。如果有就输出调用子流程信息. (手机部分的翻译暂时不考虑).
					// int atTmp = (int)ActionType.StartChildenFlow;
					WorkCheck wc2 = new WorkCheck(getFK_Flow(), tk.getNDFrom(), tk.getWorkID(), tk.getFID());
					if (wc2.FID != 0)
					{
						//Tracks ztks = wc2.HisWorkChecks;    //重复循环！
						//foreach (BP.WF.Track subTK in ztks)
						//{
						if (tk.getHisActionType() == ActionType.StartChildenFlow)
						{
							//说明有子流程
							//如果是调用子流程,就要从参数里获取到都是调用了那个子流程，并把他们显示出来.
							String[] paras = tk.getTag().split("[@]", -1);
							String[] p1 = paras[1].split("[=]", -1);
							String fk_flow = p1[1]; //子流程编号

							String[] p2 = paras[2].split("[=]", -1);
							String workId = p2[1]; //子流程ID.

							WorkCheck subwc = new WorkCheck(fk_flow, Integer.parseInt(fk_flow + "01"), Long.parseLong(workId), 0);

							Tracks subtks = subwc.getHisWorkChecks();

							//取出来子流程的所有的节点。
							Nodes subNds = new Nodes(fk_flow);
							for (Node item : subNds.ToJavaList()) //主要按顺序显示
							{
								for (Track mysubtk : subtks.ToJavaList())
								{
									if (item.getNodeID() != mysubtk.getNDFrom())
									{
										continue;
									}
									//输出该子流程的审核信息，应该考虑子流程的子流程信息, 就不考虑那样复杂了.
									if (mysubtk.getHisActionType() == ActionType.WorkCheck)
									{
										//biaojie  发起多个子流程时，发起人只显示一次
										if (mysubtk.getNDFrom() == Integer.parseInt(fk_flow + "01") && biaoji == 1)
										{
											continue;
										}
										//如果是审核.
										this.Pub1.append(BaseModel.AddTR());
										this.Pub1.append(BaseModel.AddTD(mysubtk.getActionTypeText() + "<img src='../Img/Mail_Read.png' border=0/>"));
										this.Pub1.append(BaseModel.AddTD(mysubtk.getRDT()));
										//this.Pub1.append(BaseControlModel.AddTD(subtk.getEmpFrom()T);
										this.Pub1.append(BaseModel.AddTD(BP.WF.Glo.GenerUserImgSmallerHtml(mysubtk.getEmpFrom(), mysubtk.getEmpFromT())));
										this.Pub1.append(BaseModel.AddTDBigDoc(mysubtk.getMsgHtml()));
										this.Pub1.append(BaseModel.AddTREnd());
										if (mysubtk.getNDFrom() == Integer.parseInt(fk_flow + "01"))
										{
											biaoji = 1;
										}
									}
								}
							}

						}
						//}
					}
					///#endregion 检查是否有调用子流程的情况。如果有就输出调用子流程信息.
				}

				for (SelectAccper item : accepts.ToJavaList())
				{
					if (item.getFK_Node() != nd.getNodeID())
					{
						continue;
					}
					if (empcheck.contains(item.getFK_Emp()) == true)
					{
						continue;
					}
					if (item.getAccType() == 0)
					{
						continue;
					}
					if (ccls.IsExits(CCListAttr.FK_Node, nd.getNodeID()) == true)
					{
						continue;
					}

					this.Pub1.append(BaseModel.AddTR());
					this.Pub1.append(BaseModel.AddTD("style='color:Red;'", "执行"));
					//else
					//this.Pub1.append(BaseControlModel.AddTD("style='color:Red;'", "抄送");
					this.Pub1.append(BaseModel.AddTD("style='color:Red;'", "无"));

					// 显示要执行的人员。
					this.Pub1.append(BaseModel.AddTD("style='color:Red;'", BP.WF.Glo.GenerUserImgSmallerHtml(item.getFK_Emp(), item.getEmpName())));

					//info.
					this.Pub1.append(BaseModel.AddTD("style='color:Red;'", item.getInfo()));
					this.Pub1.append(BaseModel.AddTREnd());
				}


				for (SelectAccper item : accepts.ToJavaList())
				{
					if (item.getFK_Node() != nd.getNodeID())
					{
						continue;
					}
					if (item.getAccType() != 1)
					{
						continue;
					}
					if (ccls.IsExits(CCListAttr.FK_Node, nd.getNodeID()) == false)
					{
						this.Pub1.append(BaseModel.AddTR());
						this.Pub1.append(BaseModel.AddTD("style='color:Red;'", "抄送"));
						this.Pub1.append(BaseModel.AddTD("style='color:Red;'", "无"));
						// 显示要执行的人员。
						this.Pub1.append(BaseModel.AddTD("style='color:Red;'", BP.WF.Glo.GenerUserImgSmallerHtml(item.getFK_Emp(), item.getEmpName())));

						//info.
						this.Pub1.append(BaseModel.AddTD("style='color:Red;'", item.getInfo()));
						this.Pub1.append(BaseModel.AddTREnd());
					}
					else
					{
						for (CCList cc : ccls.ToJavaList())
						{
							if (cc.getFK_Node() != nd.getNodeID())
							{
								continue;
							}

							if (cc.getHisSta() == CCSta.CheckOver)
							{
								continue;
							}
							if (cc.getCCTo() != item.getFK_Emp())
							{
								continue;
							}

							this.Pub1.append(BaseModel.AddTR());
							if (cc.getHisSta() == CCSta.Read)
							{
								if (nd.getIsEndNode() == true)
								{
									this.Pub1.append(BaseModel.AddTD("<img src='../Img/Mail_Read.png' border=0/>抄送已阅"));
									this.Pub1.append(BaseModel.AddTD(cc.getCDT())); //读取时间.
									this.Pub1.append(BaseModel.AddTD(BP.WF.Glo.GenerUserImgSmallerHtml(cc.getCCTo(), cc.getCCToName())));
									this.Pub1.append(BaseModel.AddTD(cc.getCheckNoteHtml()));
								}
								else
								{
									continue;
								}
							}
							else
							{
								if (WebUser.getNo() == cc.getCCTo())
								{
									continue;

									//如果打开的是我,
//									if (cc.getHisSta() == CCSta.UnRead)
//									{
//										BP.WF.Dev2Interface.Node_CC_SetRead(cc.getMyPK());
//									}
//									this.Pub1.append(BaseControlModel.AddTD("<img src='../Img/Mail_Read.png' border=0/>正在查阅"));
								}
								else
								{
									this.Pub1.append(BaseModel.AddTD("<img src='../Img/Mail_UnRead.png' border=0/>抄送未阅"));
								}

								this.Pub1.append(BaseModel.AddTD("无"));
								this.Pub1.append(BaseModel.AddTD(BP.WF.Glo.GenerUserImgSmallerHtml(cc.getCCTo(), cc.getCCToName())));
								this.Pub1.append(BaseModel.AddTD("无"));
							}
							this.Pub1.append(BaseModel.AddTREnd());
						}
					}
				}
				///#endregion

				this.Pub1.append(BaseModel.AddTableEnd());
				this.Pub1.append(BaseModel.AddTDEnd());
				this.Pub1.append(BaseModel.AddTREnd());
			}
			else
			{
				if (!wcDesc.getFWCIsShowAllStep())
				{
					continue;
				}

				//判断该节点下是否有人访问，或者已经设置了抄送与接收人对象, 如果没有就不输出
				if (!accepts.IsExits(SelectAccperAttr.FK_Node, nd.getNodeID()))
				{
					continue;
				}

				//未出现的节点.
				this.Pub1.append(BaseModel.AddTR());
				this.Pub1.append(BaseModel.AddTDBegin("colspan=4"));
				this.Pub1.append(BaseModel.AddTable("border=0 style='padding:0px;width:100%;' leftMargin=0 topMargin=0 id='tb" + nd.getNodeID() + "'"));

				//未出现的节点.
				this.Pub1.append(BaseModel.AddTR());
				this.Pub1.append(BaseModel.AddTDTitle("colspan=4", "<div style='float:left'><image src='../Img/Tree/Cut.gif' onclick=\"show_and_hide_tr('tb" + nd.getNodeID() + "',this);\" style='cursor:pointer;'></image>" + nd.getName() + "</div>"));
				this.Pub1.append(BaseModel.AddTREnd());

				this.Pub1.append(BaseModel.AddTR("style='font-size:14px;font-weight:bold;'"));
				this.Pub1.append(BaseModel.AddTD("width='50'", "事件"));
				this.Pub1.append(BaseModel.AddTD("width='150'", "时间"));
				this.Pub1.append(BaseModel.AddTD("width='100'", "操作员"));
				this.Pub1.append(BaseModel.AddTD("信息"));
				this.Pub1.append(BaseModel.AddTREnd());

				//是否输出了.
				boolean isHaveIt = false;
				for (SelectAccper item : accepts.ToJavaList())
				{
					if (item.getFK_Node() != nd.getNodeID())
					{
						continue;
					}
					if (item.getAccType() != 0)
					{
						continue;
					}
					this.Pub1.append(BaseModel.AddTR());
					this.Pub1.append(BaseModel.AddTD("style='color:Red;'", "执行"));
					//else
					//this.Pub1.append(BaseControlModel.AddTD("style='color:Red;'", "抄送");
					this.Pub1.append(BaseModel.AddTD("style='color:Red;'", "无"));

					// 显示要执行的人员。
					this.Pub1.append(BaseModel.AddTD("style='color:Red;'", BP.WF.Glo.GenerUserImgSmallerHtml(item.getFK_Emp(), item.getEmpName())));

					//info.
					this.Pub1.append(BaseModel.AddTD("style='color:Red;'", item.getInfo()));
					this.Pub1.append(BaseModel.AddTREnd());
					isHaveIt = true;
				}

				///#region 输出抄送
				for (SelectAccper item : accepts.ToJavaList())
				{
					if (item.getFK_Node() != nd.getNodeID())
					{
						continue;
					}
					if (item.getAccType() != 1)
					{
						continue;
					}
					if (ccls.IsExits(CCListAttr.FK_Node, nd.getNodeID()) == false)
					{
						this.Pub1.append(BaseModel.AddTR());
						this.Pub1.append(BaseModel.AddTD("style='color:Red;'", "抄送"));
						this.Pub1.append(BaseModel.AddTD("style='color:Red;'", "无"));
						// 显示要执行的人员。
						this.Pub1.append(BaseModel.AddTD("style='color:Red;'", BP.WF.Glo.GenerUserImgSmallerHtml(item.getFK_Emp(), item.getEmpName())));

						//info.
						this.Pub1.append(BaseModel.AddTD("style='color:Red;'", item.getInfo()));
						this.Pub1.append(BaseModel.AddTREnd());
						isHaveIt = true;
					}
					else
					{
						for (CCList cc : ccls.ToJavaList())
						{
							if (cc.getFK_Node() != nd.getNodeID())
							{
								continue;
							}

							if (cc.getHisSta() == CCSta.CheckOver)
							{
								continue;
							}
							if (cc.getCCTo() != item.getFK_Emp())
							{
								continue;
							}

							this.Pub1.append(BaseModel.AddTR());
							if (cc.getHisSta() == CCSta.Read)
							{
								if (nd.getIsEndNode())
								{
									this.Pub1.append(BaseModel.AddTD("<img src='../Img/Mail_Read.png' border=0/>抄送已阅"));
									this.Pub1.append(BaseModel.AddTD(cc.getCDT())); //读取时间.
									this.Pub1.append(BaseModel.AddTD(BP.WF.Glo.GenerUserImgSmallerHtml(cc.getCCTo(), cc.getCCToName())));
									this.Pub1.append(BaseModel.AddTD(cc.getCheckNoteHtml()));
								}
								else
								{
									continue;
								}
							}
							else
							{
								if (WebUser.getNo() == cc.getCCTo())
								{
									continue;

									//如果打开的是我,
//									if (cc.getHisSta() == CCSta.UnRead)
//									{
//										BP.WF.Dev2Interface.Node_CC_SetRead(cc.getMyPK());
//									}
//									this.Pub1.append(BaseControlModel.AddTD("<img src='../Img/Mail_Read.png' border=0/>正在查阅"));
								}
								else
								{
									this.Pub1.append(BaseModel.AddTD("<img src='../Img/Mail_UnRead.png' border=0/>抄送未阅"));
								}

								this.Pub1.append(BaseModel.AddTD("无"));
								this.Pub1.append(BaseModel.AddTD(BP.WF.Glo.GenerUserImgSmallerHtml(cc.getCCTo(), cc.getCCToName())));
								this.Pub1.append(BaseModel.AddTD("无"));
							}
							this.Pub1.append(BaseModel.AddTREnd());
						}
					}
				}
				///#endregion

				this.Pub1.append(BaseModel.AddTableEnd());
				this.Pub1.append(BaseModel.AddTDEnd());
				this.Pub1.append(BaseModel.AddTREnd());
			}



		}
		this.Pub1.append(BaseModel.AddTableEnd());

	}
	public final void BindFreeModelV1_del(FrmWorkCheck wcDesc)
	{
		WorkCheck wc = new WorkCheck(this.getFK_Flow(), this.getNodeID(), this.getWorkID(), this.getFID());
		this.Pub1.append(BaseModel.AddTable("border=0 style='padding:0px;width:100%;' leftMargin=0 topMargin=0"));
		if (getIsHidden() == false && wcDesc.getHisFrmWorkCheckSta() == FrmWorkCheckSta.Enable)
		{
			this.Pub1.append(BaseModel.AddTR());
			this.Pub1.append(BaseModel.AddTD("<div style='float:right'><img src='../Img/Btn/Save.gif' border=0 />保存</div>"));
			this.Pub1.append(BaseModel.AddTREnd());

			boolean tbReadOnly = false;
			if (getDoType() != null && getDoType().equals("View"))
			{
				tbReadOnly = true;
			}
			String tbText = "";
			tbText = BP.WF.Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getNodeID());

			if (tbText.equals("同意"))
			{
				tbText = "";
			}
			
			if (tbText.equals(""))
			{
				tbText = wcDesc.getFWCDefInfo();
				BP.WF.Dev2Interface.WriteTrackWorkCheck(this.getFK_Flow(), this.getNodeID(), this.getWorkID(), this.getFID(), tbText, wcDesc.getFWCOpLabel());
			}

			this.Pub1.append(BaseModel.AddTR());
			this.Pub1.append(BaseModel.AddPostBackTextBox("TB_Doc", tbReadOnly, tbText));
			this.Pub1.append(BaseModel.AddTREnd());
		}

		if (!wcDesc.getFWCListEnable())
		{
			this.Pub1.append(BaseModel.AddTableEnd());
			return;
		}

		int i = 0;
		Tracks tks = wc.getHisWorkChecks();
		for (Track tk : tks.ToJavaList())
		{
			///#region 输出审核.
			if (tk.getHisActionType() == ActionType.WorkCheck)
			{
				//如果是审核.
				i++;
				ActionType at = tk.getHisActionType();
				java.util.Date dtt = BP.DA.DataType.ParseSysDateTime2DateTime(tk.getRDT());

				this.Pub1.append(BaseModel.AddTR());
				this.Pub1.append(BaseModel.AddTDBegin());
				this.Pub1.append(BaseModel.AddB(tk.getNDFromT()));
				this.Pub1.append(BaseModel.AddBR(tk.getMsgHtml()));
				SimpleDateFormat df = new SimpleDateFormat("yy年MM月dd日HH时mm分");
				this.Pub1.append(BaseModel.AddBR("<div style='float:right'>" + BP.WF.Glo.GenerUserImgSmallerHtml(tk.getEmpFrom(), tk.getEmpFromT()) + " &nbsp;&nbsp;&nbsp; " + df.format(dtt) + "</div>"));
				this.Pub1.append(BaseModel.AddTDEnd());
				this.Pub1.append(BaseModel.AddTREnd());
			}
			///#endregion 输出审核.

			///#region 检查是否有子流程.
			if (tk.getHisActionType() == ActionType.StartChildenFlow)
			{
				//如果是调用子流程,就要从参数里获取到都是调用了那个子流程，并把他们显示出来.
				String[] paras = tk.getTag().split("[@]", -1);
				String[] p1 = paras[1].split("[=]", -1);
				String fk_flow = p1[1];

				String[] p2 = paras[2].split("[=]", -1);
				String workId = p2[1];

				WorkCheck subwc = new WorkCheck(fk_flow, Integer.parseInt(fk_flow + "01"), Long.parseLong(workId), 0);
				Tracks subtks = subwc.getHisWorkChecks();
				for (Track subtk : subtks.ToJavaList())
				{
					if (subtk.getHisActionType() == ActionType.WorkCheck)
					{
						//如果是审核.
						i++;
						ActionType at = subtk.getHisActionType();
						java.util.Date dtt = BP.DA.DataType.ParseSysDateTime2DateTime(subtk.getRDT());

						this.Pub1.append(BaseModel.AddTR());
						this.Pub1.append(BaseModel.AddTDBegin());

						this.Pub1.append(BaseModel.AddB(subtk.getNDFromT()));
						this.Pub1.append(BaseModel.AddBR(subtk.getMsgHtml()));
						SimpleDateFormat df = new SimpleDateFormat("yy年MM月dd日HH时mm分");
						this.Pub1.append(BaseModel.AddBR("<div style='float:right'>" + BP.WF.Glo.GenerUserImgSmallerHtml(subtk.getEmpFrom(), subtk.getEmpFromT()) + " &nbsp;&nbsp;&nbsp; " + df.format(dtt) + "</div>"));

						this.Pub1.append(BaseModel.AddTDEnd());
						this.Pub1.append(BaseModel.AddTREnd());

					}
				}
			}
		}
		this.Pub1.append(BaseModel.AddTableEnd());
	}
	public final void BindTableModel(FrmWorkCheck wcDesc)
	{
		WorkCheck wc = new WorkCheck(this.getFK_Flow(), this.getNodeID(), this.getWorkID(), this.getFID());

		this.Pub1.append(BaseModel.AddTable("border=1 style='padding:0px;width:100%;'"));
		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTD("colspan=8", "<div style='float:left'>审批意见</div> <div style='float:right'><img src='../Img/Btn/Save.gif' border=0 /></div>"));
		this.Pub1.append(BaseModel.AddTREnd());

		if (!getIsHidden())
		{
			boolean tbReadOnly = false;
			if (getDoType() != null && getDoType().equals("View"))
			{
				tbReadOnly = true;
			}
			String tbText = "";
			tbText = BP.WF.Dev2Interface.GetCheckInfo(this.getFK_Flow(), this.getWorkID(), this.getNodeID());
			this.Pub1.append("\n<TD colspan=8>");
			this.Pub1.append(BaseModel.AddPostBackTextBox("TB_Doc", tbReadOnly, tbText));

            this.Pub1.append("</TD>");

			this.Pub1.append(BaseModel.AddTREnd());
		}

		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTD("IDX"));
		this.Pub1.append(BaseModel.AddTD("发生时间"));
		this.Pub1.append(BaseModel.AddTD("发生节点"));
		//   this.Pub1.append(BaseControlModel.AddTD("人员");
		this.Pub1.append(BaseModel.AddTD("活动"));
		this.Pub1.append(BaseModel.AddTD("信息/审批意见"));
		this.Pub1.append(BaseModel.AddTD("执行人"));
		this.Pub1.append(BaseModel.AddTREnd());

		int i = 0;
		Tracks tks = wc.getHisWorkChecks();
		for (Track tk : tks.ToJavaList())
		{
			if (tk.getHisActionType() == ActionType.Forward || tk.getHisActionType() == ActionType.ForwardFL || tk.getHisActionType() == ActionType.ForwardHL)
			{
				String nd = String.valueOf(tk.getNDFrom());
				if ( ! nd.substring(nd.length() - 2).equals("01"))
				{
					continue;
				}
				//string len=tk.getNDFrom().ToString();
				//if (
				//if (tk.getNDFrom().ToString().Contains
			}

			if (tk.getHisActionType() != ActionType.WorkCheck)
			{
				continue;
			}

			i++;
			this.Pub1.append(BaseModel.AddTR());
			this.Pub1.append(BaseModel.AddTD(i));
			java.util.Date dtt = BP.DA.DataType.ParseSysDateTime2DateTime(tk.getRDT());
			SimpleDateFormat df = new SimpleDateFormat("MM月dd日HH时mm分");
			this.Pub1.append(BaseModel.AddTD(df.format(dtt)));
			this.Pub1.append(BaseModel.AddTD(tk.getNDFromT()));
			//  this.Pub1.append(BaseControlModel.AddTD(tk.getEmpFrom()T);
			ActionType at = tk.getHisActionType();
			//this.Pub1.append(BaseControlModel.AddTD("<img src='./../Img/Action/" + at.ToString() + ".png' class='ActionType' width='16px' border=0/>" + BP.WF.Track.GetActionTypeT(at));
			this.Pub1.append(BaseModel.AddTD("<img src='./../Img/Action/" + at.toString() + ".png' class='ActionType' width='16px' border=0/>" + tk.getActionTypeText()));
			this.Pub1.append(BaseModel.AddTD(tk.getMsgHtml()));
			this.Pub1.append(BaseModel.AddTD(tk.getExer())); //如果是委托，增加一个 人员(委托)
			this.Pub1.append(BaseModel.AddTREnd());
		}
		this.Pub1.append(BaseModel.AddTableEnd());
	}

	//展示空表单
	private void ViewEmptyForm()
	{
		this.Pub1.append(BaseModel.AddTable(" border=1 style='padding:0px;width:100%;'"));
		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTD("colspan=6 style='text-align:left' ", "审批意见"));
		this.Pub1.append(BaseModel.AddTREnd());

		this.Pub1.append(BaseModel.AddTR());
		this.Pub1.append(BaseModel.AddTD("IDX"));
		this.Pub1.append(BaseModel.AddTD("发生时间"));
		this.Pub1.append(BaseModel.AddTD("发生节点"));
		this.Pub1.append(BaseModel.AddTD("活动"));
		this.Pub1.append(BaseModel.AddTD("信息/审批意见"));
		this.Pub1.append(BaseModel.AddTD("执行人"));
		this.Pub1.append(BaseModel.AddTREnd());

		this.Pub1.append(BaseModel.AddTableEnd());
	}

	
}
