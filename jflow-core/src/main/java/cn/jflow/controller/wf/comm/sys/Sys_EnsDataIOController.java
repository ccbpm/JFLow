package cn.jflow.controller.wf.comm.sys;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import BP.DA.DataColumn;
import BP.DA.DataColumnCollection;
import BP.DA.DataRow;
import BP.DA.DataRowCollection;
import BP.DA.DataTable;
import BP.En.Attr;
import BP.En.Attrs;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.EntityOID;
import BP.Sys.PubClass;
import BP.WF.Glo;
import BP.Web.WebUser;

@Controller
@RequestMapping("/WF/Comm/Sys")
public class Sys_EnsDataIOController {

	@RequestMapping(value = "/Btn_Up", method = RequestMethod.POST)
	public ModelAndView Btn_Up(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("f") MultipartFile file) {

		if (file.isEmpty()) {
			PubClass.Alert("请选择上传文件。", response);
			return null;
		}

		try {
			String path = Glo.getFlowFields() + WebUser.getNo() + "DTS.xls";
			File filetemp = new File(path);
			if (filetemp.exists()) {
				filetemp.delete();
			}
			path = Glo.getFlowFields() + WebUser.getNo() + "DTS.xlsx";
			filetemp = new File(path);

			if (filetemp.exists()) {
				filetemp.delete();
			}

			if (!file.getOriginalFilename().contains(".xls")) {
				PubClass.Alert("请上传xls文件。 \t\n" + filetemp.getName(), response);
				return null;
			}

			String ext = ".xls";
			if (file.getOriginalFilename().contains(".xlsx")) {
				ext = ".xlsx";
			}

			String filePath = Glo.getFlowFields() + WebUser.getNo() + "DTS"
					+ ext;
			try {
				file.transferTo(new File(filePath));
			} catch (IOException e) {
				e.printStackTrace();
			}

			ModelAndView modelAndView = new ModelAndView(
					"redirect:EnsDataIO.jsp");
			modelAndView.addObject("Step", "2");
			modelAndView.addObject("filePath", filePath);
			modelAndView.addObject("EnsName", request.getParameter("EnsName"));
			return modelAndView;
		} catch (RuntimeException ex) {
			PubClass.Alert("@读取文件错误:" + ex.getMessage(), response);
		}
		return null;
	}

	@RequestMapping(value = "/Btn_DataIO_Click", method = RequestMethod.POST)
	public void btn_DataIO_Click(HttpServletRequest request,
			HttpServletResponse response, String EnsName) {
		Entities ens = BP.En.ClassFactory.GetEns(EnsName);
		ens.RetrieveAll();
		String msg = "执行信息如下：<hr>";
		try {
			String filePath = Glo.getFlowFields() + WebUser.getNo()
					+ "DTS.xls";
			if (new File(filePath).exists() == false) {
				filePath = Glo.getFlowFields() + WebUser.getNo() + "DTS.xlsx";
			}

			DataTable dt = BP.DA.DBLoad.GetTableByExt(filePath);

			Entity en = ens.getGetNewEntity();
			Attrs attrs = en.getEnMap().getAttrs();

			// this.ResponseWriteRedMsg(dt.Rows.Count.ToString() );
			// return;
			// 开始执行导入。

			try {
				ens.ClearTable();
			} catch (Exception e) {
				e.printStackTrace();
			}

			int idx = 0;
			DataColumnCollection collection = dt.Columns;
			DataRowCollection rows = dt.Rows;
			for (DataRow dr : rows) {
				idx++;
				Entity en1 = ens.getGetNewEntity();
				StringBuffer rowMsg = new StringBuffer();
				for (Attr attr : attrs) {
					if (request.getParameter("CB_" + attr.getKey()) == null) {
						continue;
					}
					String item = request.getParameter("DDL_" + attr.getKey());
					DataColumn column = collection.get(Integer.parseInt(item));
					en1.SetValByKey(attr.getKey(), dr.getValue(column.ColumnName));
					rowMsg.append(attr.getKey()).append(" = ") .append(dr.getValue(column.ColumnName)) .append(" , ");
				}

				try {
					en1.Insert();
					msg += "@行号：" + idx + "执行成功。";
				} catch (RuntimeException ex) {
					msg += "<font color=red>@行号：" + idx + "执行失败。" + rowMsg
							+ " @失败信息:" + ex.getMessage() + "</font>";
					msg += ex.getMessage();
				}
			}
			Glo.ToMsg(msg, response);
			// this.ResponseWriteBlueMsg(msg);
		} catch (RuntimeException ex) {
			try {
				ens.ClearTable();
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (Entity myen : ens) {
				if (myen.getIsOIDEntity()) {
					EntityOID enOId = (EntityOID) myen;
					try {
						enOId.InsertAsOID(enOId.getOID());
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					myen.Insert();
				}
			}
			Glo.ToMsgErr("执行错误：数据已经回滚回来。错误信息：" + ex.getMessage() + "。 MSG= "
					+ msg);
			// this.ResponseWriteRedMsg("执行错误：数据已经回滚回来。错误信息：" + ex.getMessage()
			// +"。 MSG= "+msg);
		}
	}

	/**
	 * 更新方式
	 * 
	 * @param sender
	 * @param e
	 */
	@RequestMapping(value = "/Btn_UpdateIO_Click", method = RequestMethod.POST)
	public void btn_UpdateIO_Click(HttpServletRequest request,
			HttpServletResponse response, String EnsName) {
		Entities ens = BP.En.ClassFactory.GetEns(EnsName);
		ens.RetrieveAll();
		String msg = "执行信息如下：<hr>";
		try {
			String filePath = Glo.getFlowFields()+ WebUser.getNo() + "DTS.xls";
			if (new File(filePath).exists() == false) {
				filePath = Glo.getFlowFields() + WebUser.getNo() + "DTS.xlsx";
			}
			
			DataTable dt = BP.DA.DBLoad.GetTableByExt(filePath);
			Entity en = ens.getGetNewEntity();
			Attrs attrs = en.getEnMap().getAttrs();
			
			String updateKey = request.getParameter("DDL_PK");
			DataColumnCollection collection = dt.Columns;
			int idx = 0;
			for (DataRow dr : dt.Rows) {
				idx++;
				Entity en1 = ens.getGetNewEntity();
				// 查询出来数据,根据要更新的主键。
				for (Attr attr : attrs) {
					if (updateKey.equals(attr.getKey())) {
						if(request.getParameter("CB_" + attr.getKey()) != null){
							
						}
//						this.Pub1.GetCBByID("CB_" + attr.getKey()).Checked = true;
						String item = request.getParameter("DDL_" + attr.getKey());
						DataColumn column = collection.get(Integer.parseInt(item));
						en1.SetValByKey(attr.getKey(), dr.getValue(column.ColumnName));
						en1.Retrieve(attr.getKey(), dr.getValue(column.ColumnName));
						break;
					}
				}

				StringBuffer rowMsg = new StringBuffer();
				for (Attr attr : attrs) {
					if (request.getParameter("CB_" + attr.getKey()) == null) {
						continue;
					}
					String item = request.getParameter("DDL_" + attr.getKey());
					DataColumn column = collection.get(Integer.parseInt(item));
					en1.SetValByKey(attr.getKey(), dr.getValue(column.ColumnName));
					rowMsg.append(attr.getKey()).append(" = ") .append(dr.getValue(column.ColumnName)) .append(" , ");
				}

				try {
					en1.Save();
					msg += "@row：" + idx + " OK。";
				} catch (RuntimeException ex) {
					msg += "<font color=red>@Row：" + idx + "error。" + rowMsg
							+ " @error:" + ex.getMessage() + "</font>";
					msg += ex.getMessage();
				}
			}
			Glo.ToMsg(msg, response);
		} catch (RuntimeException ex) {
			try {
				ens.ClearTable();
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (Entity myen : ens) {
				if (myen.getIsOIDEntity()) {
					EntityOID enOId = (EntityOID) myen;
					try {
						enOId.InsertAsOID(enOId.getOID());
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					myen.Insert();
				}
			}
			Glo.ToMsgErr("执行错误：数据已经回滚回来。错误信息：" + ex.getMessage() + "。 MSG= "
					+ msg);
		}
	}
}
