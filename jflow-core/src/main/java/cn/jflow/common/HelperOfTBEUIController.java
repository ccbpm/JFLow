package cn.jflow.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataRowCollection;
import BP.DA.DataTable;
import BP.Sys.DefVal;
import BP.Sys.DefValAttr;
import BP.Tools.DataTableConvertJson;
import BP.Tools.StringHelper;
import BP.Web.WebUser;

@Controller
@RequestMapping("/WF/Comm")
public class HelperOfTBEUIController {
	private HttpServletRequest _request = null;
	private HttpServletResponse _response = null;

	public String getUTF8ToString(String param) {
		try {
			if("str".equals(param)|| "text".equals(param)){
				return java.net.URLDecoder.decode(_request.getParameter(param),"utf-8");
			}else{
				return _request.getParameter(param);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public int getgetTaps() {
		try {
			return Integer.parseInt(_request.getParameter("getTaps"));
		} catch (java.lang.Exception e) {
			return 100;
		}
	}

	@RequestMapping(value = "/HelperOfTBEUI", method = RequestMethod.GET)
	public void executeHelper(HttpServletRequest request,HttpServletResponse response) {
		_request = request;
		_response = response;
		treeResult.setLength(0);
		treesb.setLength(0);
		String method = "";
		// 返回值
		String s_responsetext = "";
		if (StringHelper.isNullOrEmpty(request.getParameter("method"))) {
			return;
		}

		method = request.getParameter("method").toString();

		if ("getData".equals(method)) {
			s_responsetext = getData();
		} 
		else if ("addData".equals(method)) {
			s_responsetext = addData();
		} 
		else if ("editData".equals(method)) {
			s_responsetext = editData();
		} 
		else if ("deleteData".equals(method)) {
			s_responsetext = deleteData();
		} else if ("saveHistoryData".equals(method)) {
			s_responsetext = saveHistoryData();
		}
		if (StringHelper.isNullOrEmpty(s_responsetext)) {
			s_responsetext = "";
		}
		// 组装ajax字符串格式,返回调用客户端
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		try {
			response.getOutputStream().write(s_responsetext.replace("][", "],[").getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/** 
	 保存历史数据
	 
	 @return 
	*/
	private String saveHistoryData() {
		String lb = getUTF8ToString("lb");
		if ("readWords".equals(lb)|| "hisWords".equals(lb)) {
			return "false";
		}

		String enName = getUTF8ToString("FK_MapData");
		String AttrKey = getUTF8ToString("AttrKey");
		String str = getUTF8ToString("str");


		String sql = "select * from sys_defval where LB='2' and FK_Emp='" + WebUser.getNo() + "' and FK_MapData='" + enName + "' and AttrKey='" + AttrKey + "' and CurValue='" + str + "'";

		if (DBAccess.RunSQLReturnCOUNT(sql) != 0) { //禁止添加重复数据
			return "false";
		}

		sql = "select * from sys_defval where LB='2' and FK_Emp='" + WebUser.getNo() + "' and FK_MapData='" + enName + "' and AttrKey='" + AttrKey + "'";

		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		DefVal dv = new DefVal();
		if (dt.Rows.size() == 50) { //动态更新数据，限制50条
			try {
				int minOid = Integer.parseInt(String.valueOf(dt.Rows.get(0).get("OID")));

				for (DataRow dr : dt.Rows) {
					int drOid = Integer.parseInt(dr.get("OID").toString());
					if (minOid > drOid) {
						minOid = drOid;
					}
				}

				dv = new DefVal();
				dv.RetrieveByAttr(DefValAttr.OID, minOid);

				dv.Delete();
			}
			catch (RuntimeException e) {
				if (dt.Rows.size() != 0) {
					return "false";
				}
			}
		}

		dv = new DefVal();
		dv.setFK_MapData(enName);
		dv.setAttrKey(AttrKey);
		dv.setLB("2");
		dv.setFK_Emp(WebUser.getNo());
		dv.setCurValue(str);

		dv.Insert();

		return "true";
	}

	
	
	
	
	
	
	

	/**
	 * 编辑节点---所有节点都可编辑
	 * 
	 * @return
	 */
	private String editData() {
		String lb = getUTF8ToString("lb");
		if (lb.equals("readWords") || lb.equals("hisWords")) { //文件,历史词汇
			return "false";
		}

		String oid = getUTF8ToString("oid");
		String text = getUTF8ToString("text");
		try {
			DefVal dv = new DefVal();
			dv.RetrieveByAttr(DefValAttr.OID, oid);
			dv.setCurValue(text);
			dv.Update();

			return "true";
		}
		catch (RuntimeException e) {
			return "false";
		}
	}
	

	/**
	 * 删除节点--如果父节点还有子节点--禁止删除此父节点
	 * 
	 * @return
	 */
	private String deleteData() {
		String oids = getUTF8ToString("oids");
		if (StringHelper.isNullOrEmpty(oids)) {
			return "false";
		}

		String lb = getUTF8ToString("lb");
		if (lb.equals("readWords") || lb.equals("hisWords")) {
			return "false";
		}

		try {
			String[] oidsArray = oids.split("[,]", -1);

			for (String oid : oidsArray) {
				if (StringHelper.isNullOrEmpty(oid)) {
					continue;
				}

				DefVal dv = new DefVal();
				dv.RetrieveByAttr(DefValAttr.OID, oid);

				dv.Delete();
			}

			return "true";
		}
		catch (RuntimeException e) {
			return "false";
		}
	}
	/** 
	 添加数据
	 
	 @return 
	*/
	private String addData() {
		String lb = getUTF8ToString("lb");
		if (lb.equals("readWords") || lb.equals("hisWords")) { //文件,历史词汇
			return "false";
		}

		String text = getUTF8ToString("text");
		text = DataTableConvertJson.GetFilteredStrForJSON(text);
		if (StringHelper.isNullOrEmpty(text)) {
			return "false";
		}

		String enName = getUTF8ToString("FK_MapData");
		String AttrKey = getUTF8ToString("AttrKey");


		String lbStr = "";
		String fk_emp = "";
		if (lb.equals("myWords")) { //我的词汇
			lbStr = "1";
			fk_emp = WebUser.getNo();
		}
		if (lb.equals("sysWords")) { //系统词汇
			lbStr = "3";
			fk_emp = "";
		}

		String addQue = " and FK_MapData='" + enName + "' and AttrKey='" + AttrKey + "' and CurValue='" + text + "'";
		String sql = "select * from sys_defval where LB='" + lbStr + "' and FK_Emp='" + fk_emp + "'" + addQue;
		if (DBAccess.RunSQLReturnCOUNT(sql) != 0) {
			return "false";
		}

		try {
			DefVal dv = new DefVal();
			dv.setFK_MapData(enName);
			dv.setAttrKey(AttrKey);
			dv.setLB(lbStr);
			dv.setFK_Emp(fk_emp);
			dv.setCurValue(text);
			dv.Insert();
		}
		catch (java.lang.Exception e) {
			DefVal dv = new DefVal();
			dv.RunSQL("drop table Sys_DefVal");
			dv.CheckPhysicsTable();
		}

		return "true";
	}

	//获取数据
		private String getData() {
			DefVal dv = new DefVal();
			dv.CheckPhysicsTable();

			String enName = getUTF8ToString("FK_MapData");
			String AttrKey = getUTF8ToString("AttrKey");
			String lb = getUTF8ToString("lb");

			if (lb.equals("readWords")) { //读取txt文件
				//return readTxt();
			}
			try {
				DataTable dt = new DataTable();
				String sql = "";

				String addQue = ""; //公用sql查询条件
				addQue = " and FK_MapData='" + enName + "' and AttrKey='" + AttrKey + "'";

				if (lb.equals("myWords")) { //我的词汇
					sql = "select * from sys_defval where LB='1' and FK_Emp='" + WebUser.getNo() + "'" + addQue;
				}

				if (lb.equals("hisWords")) { //历史词汇
					sql = "select * from sys_defval where LB='2' and FK_Emp='" + WebUser.getNo() + "'" + addQue;
				}

				if (lb.equals("sysWords")) { //系统词汇
					switch (DBAccess.getAppCenterDBType()) {
						case Oracle:
							sql = "select * from sys_defval where LB='3' and FK_Emp is null" + addQue;
							break;
							case MSSQL:
							sql = "select * from sys_defval where LB='3' and FK_Emp=''" + addQue;
							break;

					}
				}
				String pageNumber = getUTF8ToString("pageNumber");
				int iPageNumber = StringHelper.isNullOrEmpty(pageNumber) ? 1 : Integer.parseInt(pageNumber);
				//每页多少行
				String pageSize = getUTF8ToString("pageSize");
				int iPageSize = StringHelper.isNullOrEmpty(pageSize) ? 9999 : Integer.parseInt(pageSize);


				switch (DBAccess.getAppCenterDBType()) {
					case Oracle:
					case MSSQL:
						return DBPaging("(" + sql + ")sqlStr", iPageNumber, iPageSize, "OID", "OID");
					case MySQL:
						return DBPaging("(" + sql + " order by OID DESC )sqlStr", iPageNumber, iPageSize, "OID", "");
					default:
						throw new RuntimeException("暂不支持您的数据库类型.");
				}
			}
			catch (RuntimeException e) {
				return "";
			}
		}

		
		

	
	private String boolToValue(boolean bool)
	{
		if(bool)
			return "1";
		else
			return "0";
	}

	/**
	 * 根据DataTable生成Json树结构
	 */
	private StringBuilder treeResult = new StringBuilder();
	private StringBuilder treesb = new StringBuilder();

	public final String GetTreeJsonByTable(String sql,DataTable tabel, String idCol, String txtCol, String rela, Object pId, String IsParent, String CheckedString)
	{
		String treeJson = "";
		treeResult.append(treesb.toString());
		DataTable tmpTable = null;
		// rows = null;
		treesb.setLength(0);
		if (tabel.Rows.size() > 0)
		{
			treesb.append("[");
			String filer = "";
			if (pId.toString().equals(""))
			{
				filer = String.format("%s is null", rela);
			}
			else
			{
				filer = String.format("%s='%s'", rela, pId);
			}
			//DataRow[] rows = tabel.Select(filer);
			tmpTable = DBAccess.RunSQLReturnTable(sql+" and "+filer+ " ORDER BY No");
			DataRowCollection rows = tmpTable.Rows;
			if (rows.size() > 0)
			{
				for (DataRow row : rows)
				{
					String deptNo = row.getValue(idCol).toString();

					if (treeResult.length() == 0)
					{
						 treesb.append("{\"id\":\"" + row.getValue(idCol)
	                                + "\",\"text\":\"" + row.getValue(txtCol)
	                                  + "\",\"attributes\":{\"IsParent\":\"" + row.getValue(IsParent) + "\"}"
	                                   + ",\"checked\":" + boolToValue(CheckedString.contains("," + row.getValue(idCol) + ",")) + ",\"state\":\"open\"");	
					}
					else {
						String fi = String.format("%s='%s'", rela, row.getValue(idCol));
						DataTable tmpTable2 = DBAccess.RunSQLReturnTable(sql+" and "+fi+ " ORDER BY No");
						DataRowCollection rows2 = tmpTable2.Rows;
						if (rows2.size() > 0)
						{
								//+ "\",\"IsParent\":\"" + row.getValue(IsParent]
							treesb.append("{\"id\":\"" + row.getValue(idCol) + "\",\"text\":\"" + row.getValue(txtCol) + "\",\"attributes\":{\"IsParent\":\"" + row.getValue(IsParent) + "\"}" + ",\"checked\":" + boolToValue(CheckedString.contains("," + row.getValue(idCol) + ",")) + ",\"state\":\"open\"");
							//+ "\",\"checked\":" + CheckedString.Contains("," + row.getValue(idCol] + ",").ToString().ToLower() + ",\"state\":\"open\"");
						}
						else
						{
								//+ "\",\"IsParent\":\"" +row.getValue(IsParent]
							treesb.append("{\"id\":\"" + row.getValue(idCol) + "\",\"text\":\"" + row.getValue(txtCol) + "\",\"attributes\":{\"IsParent\":\"" + row.getValue(IsParent) + "\"}" + ",\"checked\":" + boolToValue(CheckedString.contains("," + row.getValue(idCol) + ",")));
							//+ "\",\"checked\":" + CheckedString.Contains("," + row.getValue(idCol] + ",").ToString().ToLower());
						}
					}

					String fi = String.format("%s='%s'", rela, row.getValue(idCol));
					DataTable tmpTable3 = DBAccess.RunSQLReturnTable(sql+" and "+fi+ " ORDER BY No");
					DataRowCollection rows3 = tmpTable3.Rows;
					if (rows3.size() > 0)
					{
						treesb.append(",\"children\":");
						GetTreeJsonByTable(sql,tabel, idCol, txtCol, rela, row.getValue(idCol), IsParent, CheckedString);
						treeResult.append(treesb.toString());
						treesb.setLength(0);
					}
					treeResult.append(treesb.toString());
					treesb.setLength(0);
					treesb.append("},");
				}
				treesb = treesb.deleteCharAt(treesb.length() - 1);
			}
			treesb.append("]");
			treeResult.append(treesb.toString());
			treeJson = treeResult.toString();
			treesb.setLength(0);
		}
		return treeJson;
	}
	
	
	/** 
	 以下算法只包含 oracle mysql sqlserver 三种类型的数据库 qin
	 
	 @param tableName 表名
	 @param pageNumber 当前页
	 @param pageSize 当前页数据条数
	 @param key 计算总行数需要
	 @param orderKey 排序字段(可以为空)
	 @return 
	*/
	private String DBPaging(String tableName, int pageNumber, int pageSize, String key, String orderKey) {
		String sql = "";
		String orderByStr = "";

		if (!StringHelper.isNullOrEmpty(orderKey)) {
			orderByStr = " ORDER BY " + orderKey + " desc";
		}

		switch (DBAccess.getAppCenterDBType()) {
			case Oracle:
				int beginIndex = (pageNumber - 1) * pageSize + 1;
				int endIndex = pageNumber * pageSize;
				sql = "SELECT * FROM ( SELECT A.*, ROWNUM RN " + "FROM (SELECT * FROM  " + tableName + orderByStr + ") A WHERE ROWNUM <= " + endIndex + " ) WHERE RN >=" + beginIndex;
				break;
			case MSSQL:
				sql = "SELECT TOP " + pageSize + " * FROM " + tableName + " WHERE " + key + " NOT IN  (" + "SELECT TOP (" + pageSize + "*(" + pageNumber + "-1)) " + key + " FROM " + tableName + orderByStr + ")" + orderByStr;
				break;
			case MySQL:
				int startIndex = (pageNumber - 1) * pageSize;
				sql = "select * from  " + tableName + orderByStr + " limit " + startIndex + "," + pageSize;
				break;
			default:
				throw new RuntimeException("暂不支持您的数据库类型.");
		}

		DataTable DTable = DBAccess.RunSQLReturnTable(sql);

		int totalCount = DBAccess.RunSQLReturnCOUNT("select " + key + " from " + tableName);

		return DataTableConvertJson.DataTable2Json(DTable, totalCount);
	}

}