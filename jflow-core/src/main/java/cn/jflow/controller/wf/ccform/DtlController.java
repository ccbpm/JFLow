package cn.jflow.controller.wf.ccform;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.DA.DBAccess;
import BP.En.Map;
import BP.En.QueryObject;
import BP.Sys.FrmEvent;
import BP.Sys.FrmEventAttr;
import BP.Sys.FrmEvents;
import BP.Sys.GEDtl;
import BP.Sys.GEDtlAttr;
import BP.Sys.GEDtls;
import BP.Sys.GEEntity;
import BP.Sys.MapDtl;
import BP.WF.XML.EventListDtlList;
import BP.Web.WebUser;
import cn.jflow.common.model.BaseModel;
import cn.jflow.common.util.ContextHolderUtils;
import cn.jflow.controller.wf.workopt.BaseController;
import cn.jflow.system.ui.core.BaseWebControl;
import cn.jflow.system.ui.core.HtmlUtils;

@Controller
@RequestMapping("/WF/CCForm")
@Scope("request")
public class DtlController extends BaseController{
	
	private boolean isAddDDLSelectIdxChange = false;
	public final int getDtlRowCount() {
		if(ContextHolderUtils.getRequest().getParameter("DtlRowCount")==null)
			return 0;
		else
		{
			int count = 0;
			try
			{
				count = Integer.parseInt(ContextHolderUtils.getRequest().getParameter("DtlRowCount"));
			}catch(Exception e)
			{
				count = 0;
			}
			
			return count;
		}
	}
	
	@RequestMapping(value = "/DtlSave", method = RequestMethod.POST)
	private void execute(HttpServletRequest request,
			HttpServletResponse response)
	{
		//此处合计内容已出，但是保存维保存到 2016年7月19日
		HashMap<String,BaseWebControl> controls = HtmlUtils.httpParser(ContextHolderUtils.getRequest().getParameter("pData").toString(), request);
		MapDtl mdtl = new MapDtl(this.getEnsName());
		GEDtls dtls = new GEDtls(this.getEnsName());
		FrmEvents fes = new FrmEvents(this.getEnsName()); // 获得事件.
		GEEntity mainEn = mdtl.GenerGEMainEntity(this.getRefPKVal());

		
		// /#region 从表保存前处理事件.
		if (fes.size() > 0) {
			try {
				String msg = fes.DoEventNode(EventListDtlList.DtlSaveEnd,
						mainEn);
				if (msg != null) {
					// this.Alert(msg);
				}
			} catch (RuntimeException ex) {
				// this.Alert(ex.getMessage());
				return;
			}
		}
		
		// /#endregion 从表保存前处理事件.

		QueryObject qo = new QueryObject(dtls);
		switch (mdtl.getDtlOpenType()) {
		case ForEmp:
			qo.AddWhere(GEDtlAttr.RefPK, this.getRefPKVal());
			qo.addAnd();
			qo.AddWhere(GEDtlAttr.Rec, WebUser.getNo());
			break;
		case ForWorkID:
			qo.AddWhere(GEDtlAttr.RefPK, this.getRefPKVal());
			break;
		case ForFID:
			qo.AddWhere(GEDtlAttr.FID, this.getRefPKVal());
			break;
		}

		int num = qo.DoQuery("OID", mdtl.getRowsOfList(), this.getPageIdx(),
				false);
		int dtlCount = dtls.size();
		if (getallRowCount() == 0) {
			mdtl.setRowsOfList(mdtl.getRowsOfList() + this.getaddRowNum());
		} else {
			mdtl.setRowsOfList(getallRowCount());
		}
		for (int i = 0; i < mdtl.getRowsOfList() - dtlCount; i++) {
			BP.Sys.GEDtl dt = new GEDtl(this.getEnsName());
			dt.ResetDefaultVal();
			dt.setOID(i);
			dtls.AddEntity(dt);
		}

		// if (num == mdtl.getRowsOfList())
		// {
		// BP.Sys.GEDtl dt1 = new GEDtl(this.EnsName);
		// dt1.ResetDefaultVal();
		// dt1.OID = mdtl.getRowsOfList() + 1;
		// dtls.AddEntity(dt1);
		// }

		Map map = dtls.getGetNewEntity().getEnMap();
		boolean isTurnPage = false;
		String err = "";
		int idx = 0;

		// 判断是否有事件.
		boolean isHaveBefore = false;
		boolean isHaveEnd = false;
		Object tempVar = fes.GetEntityByKey(FrmEventAttr.FK_Event,
				EventListDtlList.DtlItemSaveBefore);
		FrmEvent fe_Before = (FrmEvent) ((tempVar instanceof FrmEvent) ? tempVar
				: null);
		if (fe_Before == null) {
			isHaveBefore = false;
		} else {
			isHaveBefore = true;
		}

		Object tempVar2 = fes.GetEntityByKey(FrmEventAttr.FK_Event,
				EventListDtlList.DtlItemSaveAfter);
		FrmEvent fe_End = (FrmEvent) ((tempVar2 instanceof FrmEvent) ? tempVar2
				: null);
		if (fe_End == null) {
			isHaveEnd = false;
		} else {
			isHaveEnd = true;
		}

		// ...................................
		boolean isRowLock = mdtl.getIsRowLock();
		for (GEDtl dtl : dtls.ToJavaList()) {
			idx++;
			try {
				BaseModel.CopyRow(request,dtl, String.valueOf(dtl.getOID()), map,controls);

				// 如果是行锁定,就不执行.
				if (isRowLock && dtl.getIsRowLock()) {
					continue;
				}

				if (dtl.getOID() < mdtl.getRowsOfList() + 2) {
					int myOID = (int) dtl.getOID();
					dtl.setOID(0);
					if (dtl.getIsBlank()) {
						continue;
					}

					dtl.setOID(myOID);
					if (dtl.getOID() == mdtl.getRowsOfList() + 1) {
						isTurnPage = true;
					}

					dtl.setRefPK(this.getRefPKVal());
					if (this.getFID() != 0) {
						dtl.setFID(this.getFID());
					}

					if (isHaveBefore) {
						try {
							String r = fes.DoEventNode(EventListDtlList.DtlItemSaveBefore, dtl);
							if (r.equals("false") || r.equals("0")) {
								continue;
							}
							err += r;
						} catch (RuntimeException ex) {
							err += ex.getMessage();
							continue;
						}
					}
					try {
						dtl.InsertAsOID(DBAccess.GenerOID("Dtl"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					if (this.getFID() != 0) {
						dtl.setFID( this.getFID());
					}
					if (isHaveBefore) {
						try {
							err += fes.DoEventNode(
									EventListDtlList.DtlItemSaveBefore, dtl);
						} catch (RuntimeException ex) {
							err += ex.getMessage();
							continue;
						}
					}
					dtl.Update();
				}

				if (isHaveEnd) {
					// 如果有保存后的事件。
					try {
						fes.DoEventNode(EventListDtlList.DtlItemSaveAfter, dtl);
					} catch (RuntimeException ex) {
						err += ex.getMessage();
					}
				}
			} catch (RuntimeException ex) {
				//dtl.CheckPhysicsTable();
				//err += "Row: " + idx + " Error \r\n" + ex.getMessage();
				ex.printStackTrace();
			}
		}

		if (!err.equals("")) {
			BP.DA.Log.DefaultLogWriteLineInfo(err);
			//this.Alert(err);
			return;
		}

		if (isAddDDLSelectIdxChange) {
			return;
		}

		
		// /#region 从表保存后处理事件。
		if (fes.size() > 0) {
			try {
				String msg = fes.DoEventNode(EventListDtlList.DtlSaveEnd,mainEn);
				if (msg != null) {
					//this.Alert(msg);
				}
			} catch (RuntimeException ex) {
				//this.Alert(ex.getMessage());
				return;
			}
		}
		
		// /#endregion 处理事件.

		if (isTurnPage) {
			int pageNum = 0;
			int count = dtlCount + 1;
			java.math.BigDecimal pageCountD = java.math.BigDecimal
					.valueOf(count/mdtl.getRowsOfList()); // 页面个数。
			
			DecimalFormat mformat = new DecimalFormat("0.0000");
			String[] strs = mformat.format(pageCountD).split(".");
			if (Integer.parseInt(strs[1]) > 0) {
				pageNum = Integer.parseInt(strs[0]) + 1;
			} else {
				pageNum = Integer.parseInt(strs[0]);
			}
			try {
				response.sendRedirect(
						"Dtl2.jsp?EnsName=" + this.getEnsName() + "&RefPKVal="
								+ this.getRefPKVal() + "&PageIdx=" + pageNum
								+ "&IsWap=" + this.getIsWap() + "&FK_Node="
								+ this.getFK_Node() + "&FID=" + this.getFID()
								+ "&Key=" + this.getKey());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				response.sendRedirect(
						"Dtl2.jsp?EnsName=" + this.getEnsName() + "&RefPKVal="
								+ this.getRefPKVal() + "&PageIdx=" + this.getPageIdx()
								+ "&IsWap=" + this.getIsWap() + "&FK_Node="
								+ this.getFK_Node() + "&FID=" + this.getFID()
								+ "&Key=" + this.getKey());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	@RequestMapping(value = "/DtlRow", method = RequestMethod.POST)
	private void myAdd_SelectedIndexChanged(HttpServletRequest request,
			HttpServletResponse response)
    {
//        DDL ddl = sender as DDL;
//        string val = ddl.SelectedItemStringVal;
//        string url = "";
//        isAddDDLSelectIdxChange = true;
//        this.Save();
//        try
//        {
//            int addRow = getDtlRowCount();
//            _allRowCount += addRow;
//        }
//        catch
//        {
//
//        }

//        if (val.Contains("+"))
//            url = "Dtl2.jsp?EnsName=" + this.getEnsName() + "&RefPKVal=" + this.getRefPKVal() + "&PageIdx=" + this.getPageIdx() + "&rowCount=" + _allRowCount + "&AddRowNum=" + ddl.SelectedItemStringVal.Replace("+", "").Replace("-", "") + "&IsCut=0&IsWap=" + this.IsWap + "&FK_Node=" + this.getFK_Node() + "&Key=" + this.Request.QueryString["Key"];
//        else
            String url = "Dtl2.jsp?EnsName=" + this.getEnsName() + "&RefPKVal=" + this.getRefPKVal() + "&PageIdx=" + this.getPageIdx() + "&rowCount=13" +"&AddRowNum=3"+ "&IsWap=" + this.getIsWap() + "&FK_Node=" + this.getFK_Node() + "&Key=" ;

        try {
			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}
