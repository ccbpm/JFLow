package cn.jflow.controller.wf.mapdef;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/WF/WorkOpt")
public class TransferCustomController {
//	private HttpServletRequest _request = null;
//	private HttpServletResponse _response = null;
//	// #region 参数
//	private String FK_Flow;
//     
//     private int WorkID;
//     private int FID;
//     
//     private int FK_Node;
//     
//	
//
//	protected void Page_Load()
//     {
//		boolean haveNameText = false;
//		boolean mustFilter = false;
//		int step = 0;
//
//         //获取已经走完的节点与当前的节点
//         String sql = String.format("SELECT wgw.FK_Node,wgw.FK_NodeText,wgw.FK_Emp,wgw.FK_EmpText,wgw.RDT,wgw.IsPass,wn.Step,'' FK_CC, '' FK_CCText, 0 TodolistModel FROM WF_GenerWorkerlist wgw INNER JOIN WF_Node wn ON wn.NodeID = wgw.FK_Node WHERE WorkID = {0} ORDER BY wgw.RDT", WorkID);
//         DataTable dtWorkers = DBAccess.RunSQLReturnTable(sql);
//         DataTable ccs = DBAccess.RunSQLReturnTable("SELECT wsa.FK_Node,wsa.FK_Emp,wsa.EmpName,wsa.Idx FROM WF_SelectAccper wsa WHERE wsa.AccType = 1 AND wsa.WorkID = " + WorkID + " ORDER BY wsa.Idx");
//         DataTable customs =
//             DBAccess.RunSQLReturnTable(
//                 "SELECT wtc.FK_Node,wtc.TodolistModel,wtc.Idx FROM WF_TransferCustom wtc WHERE wtc.WorkID = " +
//                 WorkID);
//         List<DataRow> rows = new ArrayList();
//         String cc = "";
//         String cctext = "";
//
//         for(DataRow row : dtWorkers.Rows)
//         {
//         //处理抄送人
//             rows = ccs.select(String.format("FK_Node={0}", row.get("FK_Node")));
//             cc = "";
//             cctext = "";
//
//             for(DataRow r : rows)
//             {
//                 cc += r.get("FK_Emp") + ",";
//                 cctext += r.get("EmpName") + ",";
//             }      
//          int a = cc.indexOf(',');
//            cc.substring(0,a);
//             
//             row.setValue("FK_CC", cc.substring(0,cc.indexOf(',')));
//             row.setValue("FK_CCText", cctext.substring(0,cc.indexOf(',')));
//            // row["FK_CCText"] = cctext.TrimEnd(',');
//
//             //处理模式
//             rows = customs.select(String.format("FK_Node={0}", row.getValue("FK_Node")));
//             if (rows.size() == 1)
//             {
//               //  row["TodolistModel"] = rows[0]["TodolistModel"];
//            	 row.setValue("TodolistModel",rows.get(FK_Node));
//             }
//             else if(rows.size() > 1)
//             {
//                 //todo:此处处理一个流程中自定义插入多个相同结点的情况，可能有
//             }
//         }
//
//
//         DataTable overWorks = CreateDataTableFromDataRow(dtWorkers.clone(), dtWorkers.select("IsPass=1", "Step ASC"));
//         DataTable currWorks = CreateDataTableFromDataRow(dtWorkers.clone(), dtWorkers.select("IsPass=0"));
//         
//         step += overWorks.Rows.size()+ currWorks.Rows.size();
//
//         //如果是发起人，则此处为0
//         if (currWorks.Rows.size() == 0)
//         {
//             String dt = DataType.getCurrentDataTime();
//             sql = String.format("SELECT NodeID AS FK_Node,Name AS FK_NodeText, '' as  FK_Emp, '{0}' as FK_EmpText, '" + dt + "' as RDT, 0 as IsPass, Step,'' FK_CC, '' FK_CCText, 0 TodolistModel FROM WF_Node WHERE FK_Flow='{1}' AND NodeID = {2}", WebUser.getName(), FK_Flow, FK_Node);
//             currWorks = DBAccess.RunSQLReturnTable(sql);
//             step += currWorks.Rows.size();
//         }
//
//        // litCurrentStep.Text = currWorks.Rows.get(0).get("Step").toString();//[0]["Step"].ToString();
//        // lblFK_NodeText.Text = currWorks.Rows.get(0).get("FK_NodeText");//[0]["FK_NodeText"].ToString();
//        // lblFK_EmpText.Text = currWorks.Rows.get(0).get("FK_EmpText");//[0]["FK_EmpText"].ToString();
//        // lblRDT.Text = DateTime.Parse(currWorks.Rows.get(0).get("RDT").toString()).ToString("yyyy-MM-dd");
//
//         //获取流程自定义列表
//         sql = String.format("SELECT wfc.FK_Node,wn.Name AS FK_NodeText,wfc.SubFlowNo, wf.Name AS SubFlowName, wfc.Worker,'' as WorkerText,wfc.Idx + {0} + 1 as Idx,'' FK_CC, '' FK_CCText, wfc.TodolistModel  FROM WF_TransferCustom wfc INNER JOIN WF_Node wn ON wn.NodeID = wfc.FK_Node LEFT JOIN WF_Flow wf ON wf.No = wfc.SubFlowNo WHERE wfc.WorkID = {1} ORDER BY wfc.Idx ASC", step, WorkID);
//         DataTable dtTCs = DBAccess.RunSQLReturnTable(sql);
//         DataTable dtAllNodes = null;
//
//         //如果流程自定义中没有数据，则从WF_SelectAccper获取自动计算的流程各节点处理人信息
//         if (dtTCs.Rows.size() == 0)
//         {
//             haveNameText = true;
//             mustFilter = true;
//             sql = 	String.format("SELECT wsa.FK_Node,wn.Name AS FK_NodeText,'' as SubFlowNo , '——' as SubFlowName,wsa.FK_Emp AS Worker,EmpName AS WorkerText,wn.Step AS Idx,'' FK_CC, '' FK_CCText, 0 TodolistModel  FROM WF_SelectAccper wsa INNER JOIN WF_Node wn ON wn.NodeID = wsa.FK_Node WHERE wsa.WorkID = {0} AND wsa.AccType = 0", WorkID);
//             dtTCs = DBAccess.RunSQLReturnTable(sql);
//
//             //如果有数据，判断这些数据是否已经包含所有的节点，如果没有包含所有的节点，则把缺失的节点补上，added by liuxc,2015-10-15
//             if(dtTCs.Rows.size()  > 0)
//             {
//                 sql = String.format("SELECT NodeID AS FK_Node,Name AS FK_NodeText, '' as SubFlowNo, '——' as SubFlowName, '' as Worker,'' as WorkerText,Step AS Idx ,'' FK_CC, '' FK_CCText, 0 TodolistModel FROM WF_Node WHERE FK_Flow='{0}'", FK_Flow);
//                 dtAllNodes = DBAccess.RunSQLReturnTable(sql);
//
//                 //计算出所有未计算出处理人的节点
//                 DataTable dtNew = dtAllNodes.clone();
//                 for (DataRow row : dtAllNodes.Rows)
//                 {
//                     //已经计算出处理人的
//                     if (dtTCs.select("FK_Node=" + row.get("FK_Node")).size() > 0) continue;
//                     //已经完成节点工作的
//                     if (overWorks.select("FK_Node=" + row.get("FK_Node")).size() > 0) continue;
//                     //当前节点工作的
//                     if (FK_Node == (int)row.get("FK_Node")) 
//                    	 continue;
//                     
//                     dtTCs.Rows.Add(row.ItemArray);
//                 }
//
//                 //重新排序，按照Step[Idx]
//                 List<DataRow> drsSorted = dtTCs.select("1=1", "Idx ASC");
//                 for(DataRow row : drsSorted)
//                 {
//                     dtNew.Rows.Add(row.ItemArray);
//                 }
//
//                 dtTCs = dtNew;
//             }
//         }
//         else
//         {
//             for (DataRow row :dtTCs.Rows)
//             {
//                 //处理抄送人
//                 rows = ccs.select(String.format("FK_Node={0}", row.get("FK_Node")));
//                 cc = "";
//                 cctext = "";
//
//                 for (DataRow r : rows)
//                 {
//                     cc += r.get("FK_Emp") + ",";
//                     cctext += r.get("EmpName") + ",";
//                 }
//
//                 row.setValue("FK_CC", cc.substring(0,cc.indexOf(',')));//["FK_CC"] = cc.TrimEnd(',');
//                 row.setValue("FK_CCText", cctext.substring(0,cc.indexOf(',')));// = cctext.TrimEnd(',');
//             }
//         }
//
//         //如果WF_SelectAccper中也没有生成各节点的处理人信息，则从WF_Node中获取所有结点，以供选择
//         if (dtTCs.Rows.size() == 0)
//         {
//             haveNameText = true;
//             mustFilter = true;
//             sql = String.format("SELECT NodeID AS FK_Node,Name AS FK_NodeText, '' as SubFlowNo, '——' as SubFlowName, '' as Worker,'' as WorkerText,Step AS Idx ,'' FK_CC, '' FK_CCText, 0 TodolistModel FROM WF_Node WHERE FK_Flow='{0}'", FK_Flow);
//
//             if (dtAllNodes != null)
//                 dtTCs = dtAllNodes;
//             else
//                 dtTCs = DBAccess.RunSQLReturnTable(sql);
//         }
//
//         //去除已经完成+当前的结点步骤
//         if (mustFilter)
//         {
//             overWorks.Rows.Add(currWorks.Rows.get(0).ItemArray);
//             int removeSteps = 0;
//
//             for (int i = 0; i < overWorks.Rows.size(); i++)
//             {
//                 if (dtTCs.Rows.get(i).get("FK_Node").equals(overWorks.Rows.get(i).get("FK_Node")))
//                 {
//                     removeSteps++;
//                 }
//             }
//
//             overWorks.Rows.remove(overWorks.Rows.size() - 1);
//
//             while (removeSteps > 0)
//             {
//                 dtTCs.Rows.remove(0);
//                 removeSteps--;
//             }
//         }
//
//         //如果是从流程自定义中取出的数据，因为保存的是节点处理人编号，所以需要重新获取人名
//         if (!haveNameText)
//         {
//             sql = "SELECT No,Name FROM Port_Emp";
//             DataTable dtEmps = DBAccess.RunSQLReturnTable(sql);
//             String[] empArr = null;
//             List<DataRow> drs = null;
//             String wt = "";
//             for (DataRow w : dtTCs.Rows)
//             {
//                 empArr = (w.get("Worker") + "").split(",");//.toCharArray(), StringSplitOptions.RemoveEmptyEntries
//
//                 for (String emp : empArr)
//                 {
//                     drs = dtEmps.select(String.format("No='{0}'", emp));
//                    // w["WorkerText"] += (drs.Length == 0 ? emp : drs[0]["Name"].ToString()) + ",";
//                     wt += drs.size() == 0 ? emp : drs.get(0).get("Name").toString() + ",";
//                     
//                 }
//                 w.setValue("WorkerText", wt);
//                // w["WorkerText"] = w["WorkerText"].ToString().TrimEnd(',');
//                 w.setValue("WorkerText", w.get("WorkerText").toString().substring(0,cc.indexOf(',')));
//             }
//         }
//
//        //rptOverNodes.DataSource = overWorks;
//        //rptOverNodes.DataBind();
//        //rptNextNodes.DataSource = dtTCs;
//        //rptNextNodes.DataBind();
//     }
//
//     public void Cancel()
//     {
//         try {
//			this._response.sendRedirect("../MyFlow.aspx?FK_Flow=" + this.FK_Flow + "&WorkID=" + this.WorkID + "&FK_Node=" + this.FK_Node + "&FID=" + this.FID);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//     }
//
//     protected void lbtnUseAutomic_Click()
//     {
//         //Dev2InterfaceAnonymous.Flow_SetFlowTransferCustom(FK_Flow, WorkID,  BP.WF.TransferCustomType.ByCCBPMDefine, hid_idx_all.Value);
//        try {
//			this._response.sendRedirect("../MyFlow.aspx?FK_Flow=" + this.FK_Flow + "&WorkID=" + this.WorkID + "&FK_Node=" + this.FK_Node + "&FID=" + this.FID);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//     }
//     protected void lbtnUseManual_Click()
//     {
//        // Dev2InterfaceGuest.Flow_SetFlowTransferCustom(FK_Flow, WorkID, BP.WF.TransferCustomType.ByWorkerSet, hid_idx_all.Value);
//         try {
//			this._response.sendRedirect("../MyFlow.aspx?FK_Flow=" + this.FK_Flow + "&WorkID=" + this.WorkID + "&FK_Node=" + this.FK_Node + "&FID=" + this.FID);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//     }
//
//     private DataTable CreateDataTableFromDataRow(DataTable emptyTable, List<DataRow> list)
//     {
//         for (DataRow dr : list)
//         {
//             emptyTable.Rows.Add(dr.getItemArray());
//         }
//
//         return emptyTable;
//     }
//     
//     public String getFK_Flow() {
// 		return FK_Flow;
// 	}
//
// 	public void setFK_Flow(String fK_Flow) {
// 		FK_Flow = fK_Flow;
// 	}
//
// 	public int getWorkID() {
// 		return WorkID;
// 	}
//
// 	public void setWorkID(int workID) {
// 		WorkID = workID;
// 	}
//
// 	public int getFID() {
// 		return FID;
// 	}
//
// 	public void setFID(int fID) {
// 		FID = fID;
// 	}
//
// 	public int getFK_Node() {
// 		return FK_Node;
// 	}
//
// 	public void setFK_Node(int fK_Node) {
// 		FK_Node = fK_Node;
// 	}
}
