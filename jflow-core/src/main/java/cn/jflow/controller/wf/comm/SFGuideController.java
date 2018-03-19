package cn.jflow.controller.wf.comm;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.DA.DBType;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.Sys.SFDBSrc;
import BP.Sys.SFTable;
import BP.Sys.SystemConfig;

@Controller
@RequestMapping(value = "/Comm/Sys")
public class SFGuideController {

	@RequestMapping(value = "/SF_btn_Create_Click", method = RequestMethod.GET)
	public void btn_Create_Click(HttpServletRequest req, HttpServletResponse res) throws IOException {
		// ModelAndView mv=new ModelAndView();
		// 传过来no，name，desc
		SFTable table = new SFTable();
		String path = req.getContextPath();
		String basePath = req.getScheme() + "://" + req.getServerName() + ":"
				+ req.getServerPort() + path + "/";
		// String str=req.getParameter("str");
		// HashMap<String,BaseWebControl> controls = HtmlUtils.httpParser(str,
		// req);
		// BaseModel.Copy(req, table, null, table.getEnMap(), controls);
		// table = ;
		table.setNo(req.getParameter("TB_No"));
		table.setName(req.getParameter("TB_Name"));
		table.setTableDesc(req.getParameter("TB_TableDesc"));
		table.setFK_SFDBSrc(req.getParameter("FK_SFDBSrc"));
		table.setSrcTable(req.getParameter("LB_Table"));
		table.setFK_Val("FK_" + table.getNo());
		table.setColumnText(req.getParameter("DDL_ColText"));
		table.setColumnValue(req.getParameter("DDL_ColValue"));
		table.setParentValue(req.getParameter("DDL_ColParentNo"));
		table.setSelectStatement(req.getParameter("TB_SelectStatement"));
		/*table.setSFTableType(Integer.parseInt(req
				.getParameter("DDL_SFTableType") == null ? "0" : req
				.getParameter("DDL_SFTableType")));*/
		table.setCodeStruct(req.getParameter("DDL_CodeStruct") == null ? BP.Sys.CodeStruct.forValue(0) :  BP.Sys.CodeStruct.forValue(Integer.parseInt(req
						.getParameter("DDL_CodeStruct"))));
		

		//if (table.getSFTableType() == 1) {
		if (table.getCodeStruct() == BP.Sys.CodeStruct.Tree) {
			//table.setIsTree(true);
		} else {
			//table.setIsTree(false);
			table.setParentValue(null);
		}

		if (BP.DA.DBAccess.IsExitsObject(table.getNo())) {
			// EasyUiHelper.AddEasyUiMessagerAndBack(this, "@对象（" +
			// table.getNo() + "）已经存在.",
			// "错误", "error");
			res.setHeader("Content-type", "text/html;charset=UTF-8");
			PrintWriter out = res.getWriter();
			String mess = ("@对象（" +
					 table.getNo() + "）已经存在.");
			String url = basePath
					+ "WF/Comm/Sys/SFGuide.jsp?Step=2&FK_SFDBSrc=local";
			out.print("<script language=JavaScript>alert('" + mess
					+ "');location.href='" + url + "';</script>");
			// PrintWriter out;
			// try {
			// out = res.getWriter();
			// out.write("<script>alert('已存在"+table.getNo()+"');return;</script>");
			// } catch (IOException e) {
			// 
			// e.printStackTrace();
			// }
			// return "0";
		}

		String sql = "CREATE VIEW " + table.getNo() + "" + " AS "
				+ table.getSelectStatement();

		BP.DA.DBAccess.RunSQL(sql);

		table.Save();
		res.setHeader("Content-type", "text/html;charset=UTF-8");
		PrintWriter out = res.getWriter();
		String mess = ("保存成功!");
		String url = basePath
				 + "WF/Comm/Sys/SFGuide.jsp?Step=2&FK_SFDBSrc=local";
		out.print("<script language=JavaScript>alert('" + mess
				+ "');location.href='" + url + "';</script>");
	}

	@RequestMapping(value = "btn_Create_Local_Click")
	public void btn_Create_Local_Click(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + path + "/";
		SFTable table = new SFTable();
		// table = this.Pub1.Copy(table) as SFTable;
		String FK_SFDBSrc = request.getParameter("FK_SFDBSrc") == null ? ""
				: request.getParameter("FK_SFDBSrc");
		String TB_No = request.getParameter("TB_No") == null ? "" : request
				.getParameter("TB_No");
		String TB_Name = request.getParameter("TB_Name") == null ? "" : request
				.getParameter("TB_Name");
		String TB_TableDesc = request.getParameter("TB_TableDesc") == null ? ""
				: request.getParameter("TB_TableDesc");
		table.setNo(TB_No);
		table.setName(TB_Name);
		table.setTableDesc(TB_TableDesc);
		table.setFK_SFDBSrc(FK_SFDBSrc);
		// table.SrcTable = table.No;
		// table.ColumnText = "Name";
		// table.ColumnValue = "No";
		table.setIsTree(false);

		if (BP.DA.DBAccess.IsExitsObject(table.getNo())) {
			// 判断已经存在的表是否符合NoName规则，如果符合，则自动加入到SFTable中
			SFDBSrc src = new SFDBSrc(FK_SFDBSrc);
			DataTable columns = src.GetColumns(table.getNo());
			List<String> cols = new ArrayList<String>();

			for (DataRow dr : columns.Rows)
				cols.add(dr.getValue("name").toString());

			// 自动判断是否符合规则
			Map<Integer, String[]> regs = new HashMap<Integer, String[]>();
			regs.put(0, new String[] { "id", "no", "pk" });
			regs.put(1, new String[] { "name", "title" });
			regs.put(2, new String[] { "parentid", "parentno" });
			String regColValue = "";
			String regColText = "";
			String regColParentNo = "";
			for (int i = 0; i < cols.size(); i++) {
				String[] arry = regs.get(0);
				for (String str : arry) {
					if (cols.get(i).toLowerCase().equals(str)) {
						regColValue = cols.get(i);
						continue;
					}
				}
				arry = regs.get(1);
				for (String str : arry) {
					if (cols.get(i).toLowerCase().equals(str)) {
						regColText = cols.get(i);
						continue;
					}
				}
				arry = regs.get(2);
				for (String str : arry) {
					if (cols.get(i).toLowerCase().equals(str)) {
						regColParentNo = cols.get(i);
						continue;
					}
				}
			}
			// var regColValue = cols.FirstOrDefault(o =>
			// regs[0).Contains(o.ToLower()));
			// var regColText = cols.FirstOrDefault(o =>
			// regs[1).Contains(o.ToLower()));
			// var regColParentNo = cols.FirstOrDefault(o =>
			// regs[2).Contains(o.ToLower()));

			if (regColValue != null && regColText != null
					&& regColParentNo != null) {
				//table.setSFTableType(1);
				//table.setIsTree(true);
				table.setCodeStruct(BP.Sys.CodeStruct.Tree);
				table.setColumnValue(regColValue);
				table.setColumnText(regColText);
				table.setParentValue(regColParentNo);
				table.setFK_SFDBSrc("local");

				table.Save();
				response.setHeader("Content-type", "text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				String mess = ("您所创建的“" + table.getNo() + "” 名称的字典表，本地库已经存在，已成功注册！编辑数据……");
				String url = basePath
						+ "WF/MapDef/SFTableEditData.jsp?RefNo="+table.getNo();
				out.print("<script language=JavaScript>alert('" + mess
						+ "');location.href='" + url + "';</script>");
			} else if (regColValue != null && regColText != null) {
				//table.setSFTableType(0);
				//table.setIsTree(false);
				table.setCodeStruct(BP.Sys.CodeStruct.forValue(0));
				table.setColumnValue(regColValue);
				table.setColumnText(regColText);
				table.setParentValue(null);
				table.setFK_SFDBSrc("local");

				table.Save();
				response.setHeader("Content-type", "text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				String mess = ("您所创建的“" + table.getNo() + "” 名称的字典表，本地库已经存在，已成功注册！编辑数据……");
				String url = basePath
						+ "WF/MapDef/SFTableEditData.jsp?RefNo="+table.getNo();
				out.print("<script language=JavaScript>alert('" + mess
						+ "');location.href='" + url + "';</script>");
			} else {
				response.setHeader("Content-type", "text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				String mess = ("@对象（" + table.getNo() + "）已经存在.");
				String url = basePath
						+ "WF/MapDef/SFTableEditData.jsp?RefNo="+table.getNo();
				out.print("<script language=JavaScript>alert('" + mess
						+ "');location.href='" + url + "';</script>");
			}
			// try {
			// response.sendRedirect(basePath
			// + "WF/Comm/Sys/SFGuide.jsp?Step=2&FK_SFDBSrc=local");
			// } catch (IOException e) {
			// 
			// e.printStackTrace();
			// }
			return;
		}

		StringBuilder sql = new StringBuilder();
		
		if(SystemConfig.getAppCenterDBType() == DBType.MySQL){
			sql.append("CREATE TABLE "+table.getNo()+"");
		}else{
			sql.append("CREATE TABLE dbo."+table.getNo()+"");
		}
		
		sql.append("(");
		sql.append("No    NVARCHAR(50) NOT NULL PRIMARY KEY,");
		sql.append("Name  NVARCHAR(100) NULL");
		sql.append(") ");

		BP.DA.DBAccess.RunSQL(sql.toString());

		sql.replace(0, sql.length(), "");
		sql.append("INSERT INTO [dbo).[{0}) ([No), [Name)) VALUES ('0{1}', 'Item{1}')");

		for (int i = 1; i < 4; i++) {
			BP.DA.DBAccess.RunSQL(String.format(sql.toString(), table.getNo(),
					i));
		}

		sql.replace(0, sql.length(), "");
		sql.append(String
				.format("EXECUTE sp_addextendedproperty N'MS_Description', N'{0}', N'SCHEMA', N'dbo', N'TABLE', N'{1}', NULL, NULL",
						table.getName(), table.getNo()));

		int i = BP.DA.DBAccess.RunSQL(sql.toString());
		table.Save();
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String mess = ("创建成功!");
		String url = basePath
				+ "WF/MapDef/SFTableEditData.jsp?RefNo="+table.getNo();
		out.print("<script language=JavaScript>alert('" + mess
				+ "');location.href='" + url + "';</script>");
	}

	@RequestMapping(value = "SF_Btn_Click", method = RequestMethod.POST)
	public void btn_Click(HttpServletRequest request,
			HttpServletResponse response) {
		String FK_SFDBSrc = request.getParameter("FK_SFDBSrc") == null ? ""
				: request.getParameter("FK_SFDBSrc");
		String DDL_ColValue = request.getParameter("DDL_ColValue");
		String LB_Table = request.getParameter("LB_Table");
		String DDL_ColText = request.getParameter("DDL_ColText");
		String DDL_ColParentNo = request.getParameter("DDL_ColParentNo");
		String TB_SelectStatement = request.getParameter("TB_SelectStatement");
		String DDL_SFTableType = request.getParameter("DDL_SFTableType");
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + path + "/";
		String url = basePath + "WF/Comm/Sys/SFGuide.jsp?Step=3&FK_SFDBSrc="
				+ FK_SFDBSrc + "&DDL_ColValue=" + DDL_ColValue + "&LB_Table="
				+ LB_Table + "&DDL_ColText=" + DDL_ColText
				+ "&DDL_ColParentNo=" + DDL_ColParentNo
				+ "&TB_SelectStatement=" + TB_SelectStatement
				+ "&DDL_SFTableType=" + DDL_SFTableType;
		try {
			response.setHeader("Content-type", "text/html;charset=UTF-8");
			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// return mv;
		// String url = "SFGuide.jsp?Step=3&FK_SFDBSrc=" + FK_SFDBSrc +
		// "&DDL_ColValue=" +
		// DDL_ColValue;
		// url += "&LB_Table=" +
		// LB_Table;
		// url += "&DDL_ColText=" +
		// DDL_ColText;
		// url += "&DDL_ColParentNo=" +
		// DDL_ColParentNo;
		// url += "&TB_SelectStatement=" +
		// TB_SelectStatement;
		// url += "&DDL_SFTableType=" +
		// DDL_SFTableType;
		//
		// Response.Redirect(url, true);
	}
}
