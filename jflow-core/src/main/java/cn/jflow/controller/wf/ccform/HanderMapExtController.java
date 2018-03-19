package cn.jflow.controller.wf.ccform;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.Sys.GEDtl;
import BP.Sys.GEDtls;
import BP.Sys.M2M;
import BP.Sys.MapDtl;
import BP.Sys.MapExt;
import BP.Sys.MapExtXmlList;
import BP.Tools.StringHelper;
import BP.Web.WebUser;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/CCForm")
@Scope("request")
public class HanderMapExtController extends BaseController{
	private String no;
	private String name;
	private String fk_dept;
	private String oid;
	private String kvs;
	private String dealSQL;
	
	public final String DealSQL(String sql, String key)
	{
		sql = sql.replace("@Key", key);
		sql = sql.replace("@key", key);
		sql = sql.replace("@Val", key);
		sql = sql.replace("@val", key);

		//sql = sql.Replace("WebUser.No", no);
		//sql = sql.Replace("@WebUser.Name", name);
		//sql = sql.Replace("@WebUser.FK_Dept", fk_dept);

		sql = sql.replace("WebUser.No", WebUser.getNo());
		sql = sql.replace("@WebUser.Name", WebUser.getName());
		sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
		if (oid != null)
		{
			sql = sql.replace("@OID", oid);
		}

		if (StringHelper.isNullOrEmpty(kvs) == false && sql.contains("@")==true)
		{
			String[] strs = kvs.split("[~]", -1);
			for (String s : strs)
			{
				if (StringHelper.isNullOrEmpty(s) || s.contains("=") == false)
				{
					continue;
				}


				String[] mykv = s.split("[=]", -1);
				sql = sql.replace("@" + mykv[0], mykv[1]);

				if (sql.contains("@") == false)
				{
					break;
				}
			}
		}
		dealSQL = sql;
		return sql;
	}
	@RequestMapping(value = "/HanderMapExt", method = RequestMethod.GET)
	public final void HanderMapExt(HttpServletRequest request, HttpServletResponse response)
	{
		ProcessRequest(request, response);
	}
	public final void ProcessRequest(HttpServletRequest request, HttpServletResponse response)
	{
		String fk_mapExt = request.getParameter("FK_MapExt");
		if (StringHelper.isNullOrEmpty(request.getParameter("Key")))
		{
			return;
		}
		no=request.getParameter("WebUserNo");
		name = request.getParameter("WebUserName");
		fk_dept = request.getParameter("WebUserFK_Dept"); 
		oid = request.getParameter("OID");
		kvs = request.getParameter("KVs");
				
		MapExt me = new MapExt(fk_mapExt);
		DataTable dt = null;
		String sql = "";
		String key= request.getParameter("Key");
		// key = System.Web.HttpUtility.UrlDecode(key, System.Text.Encoding.GetEncoding("GB2312"));
		key = key.trim();
	    // key = "周";
		
		// 动态填充ddl。
		if(me.getExtType().equals(MapExtXmlList.ActiveDDL)){
			sql = this.DealSQL(me.getDocOfSQLDeal(), key);
			dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			try {
				if(dt.Rows.size()>0){
					wirteMsg(response, JSONTODT(dt));
				}else{
					wirteMsg(response, "{ \"Head\":[]}");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		// 自动完成，级连ddl.
		}else if(me.getExtType().equals(MapExtXmlList.TBFullCtrl) || 
				 me.getExtType().equals(MapExtXmlList.DDLFullCtrl)){
			
			String doType = StringHelper.isEmpty(request.getParameter("DoType"), "");
			if (doType.equals("ReqCtrl")){
				// 获取填充 ctrl 值的信息.
				sql = this.DealSQL(me.getDocOfSQLDeal(), key);
				request.getSession().setAttribute("DtlKey", key);
				dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
				try {
					wirteMsg(response, JSONTODT(dt));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			} else if (doType.equals("ReqM2MFullList")) {
				
				// 获取填充的M2m集合. 
				DataTable dtM2M = new DataTable("Head");
				dtM2M.Columns.Add("Dtl", String.class);
				String[] strsM2M = me.getTag2().split("[$]", -1);
				for (String str : strsM2M) {
					
					if (str.equals("") || str == null)
					{
						continue;
					}

					String[] ss = str.split("[:]", -1);
					String noOfObj = ss[0];
					String mysql = ss[1];
					mysql = DealSQL(mysql, key);

					DataTable dtFull = DBAccess.RunSQLReturnTable(mysql);
					M2M m2mData = new M2M();
					m2mData.setFK_MapData(me.getFK_MapData());
					m2mData.setEnOID(Integer.parseInt(oid));
					m2mData.setM2MNo(noOfObj);
					String mystr = ",";
					String mystrT = "";
					for (DataRow dr : dtFull.Rows)
					{
						String myno = dr.get("No").toString();
						String myname = dr.get("Name").toString();
						mystr += myno + ",";
						mystrT += "@" + myno + "," + myname;
					}
					m2mData.setVals(mystr);
					m2mData.setValsName(mystrT);
					m2mData.InitMyPK();
					m2mData.setNumSelected(dtFull.Rows.size());
					m2mData.Save();

					DataRow mydr = dtM2M.NewRow();
					mydr.setValue(0, ss[0]);
					dtM2M.Rows.add(mydr);
				}
				try {
					wirteMsg(response, JSONTODT(dtM2M));
				} catch (IOException e) {
					e.printStackTrace();
				}
				
		}else if (doType.equals("ReqDtlFullList")) {
			// 获取填充的从表集合. 
			DataTable dtDtl = new DataTable("Head");
			dtDtl.Columns.Add("Dtl", String.class);
			String[] strsDtl = me.getTag1().split("[$]", -1);
			for (String str : strsDtl)
			{
				if (StringHelper.isNullOrEmpty(str))
				{
					continue;
				}

				String[] ss = str.split("[:]", -1);
				String fk_dtl = ss[0];
				Object dtlKeyObj = request.getSession().getAttribute("DtlKey");
				String dtlKey= (String)((dtlKeyObj instanceof String) ? dtlKeyObj : null);
				if (dtlKey == null)
				{
					dtlKey = key;
				}
				String mysql = DealSQL(ss[1], dtlKey);

				GEDtls dtls = new GEDtls(fk_dtl);
				MapDtl dtl = new MapDtl(fk_dtl);

				DataTable dtDtlFull = DBAccess.RunSQLReturnTable(mysql);
				BP.DA.DBAccess.RunSQL("DELETE FROM " + dtl.getPTable() + " WHERE RefPK=" + oid);
				for (DataRow dr : dtDtlFull.Rows)
				{
					BP.Sys.GEDtl mydtl = new GEDtl(fk_dtl);
				  //  mydtl.OID = dtls.Count + 1;
					dtls.AddEntity(mydtl);
					for (DataColumn dc : dtDtlFull.Columns)
					{
						mydtl.SetValByKey(dc.ColumnName, dr.get(dc.ColumnName).toString());
					}
					mydtl.setRefPKInt(Integer.parseInt(oid));
					if (mydtl.getOID() > 100)
					{
						try {
							mydtl.InsertAsOID(mydtl.getOID());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					else
					{
						mydtl.setOID(0);
						mydtl.Insert();
					}

				}
				DataRow drRe = dtDtl.NewRow();
				drRe.setValue(0, fk_dtl);;
				dtDtl.Rows.add(drRe);
			}
			try {
				wirteMsg(response, JSONTODT(dtDtl));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (doType.equals("ReqDDLFullList")) {
					// 获取要个性化填充的下拉框. 
					DataTable dt1 = new DataTable("Head");
					dt1.Columns.Add("DDL", String.class);
					//    dt1.Columns.Add("SQL", typeof(string));
					String[] strs = me.getTag().split("[$]", -1);
					for (String str : strs)
					{
						if (str.equals("") || str == null)
						{
							continue;
						}

						String[] ss = str.split("[:]", -1);
						DataRow dr = dt1.NewRow();
						dr.setValue(0, ss[0]);
						// dr.getValue(1) = ss[1];
						dt1.Rows.add(dr);
					}
					try {
						wirteMsg(response, JSONTODT(dt1));
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (doType.equals("ReqDDLFullListDB")) {
						// 获取要个性化填充的下拉框的值. 根据已经传递过来的 ddl id. 
						String myDDL = request.getParameter("MyDDL");
						sql = "";
						String[] strs1 = me.getTag().split("[$]", -1);
						for (String str : strs1)
						{
							if (str.equals("") || str == null)
							{
								continue;
							}

							String[] ss = str.split("[:]", -1);
							if (myDDL.equals(ss[0]))
							{
								sql = ss[1];
								sql = this.DealSQL(sql, key);
								break;
							}
						}
						dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
						try {
							wirteMsg(response, JSONTODT(dt));
						} catch (IOException e) {
							e.printStackTrace();
						}
				} else {
						sql = this.DealSQL(me.getDocOfSQLDeal(), key);
						//sql = this.DealSQL(me.DocOfSQLDeal, key);
						dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
						try {
							wirteMsg(response, JSONTODT(dt));
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
				return;
		}
	}
	public final boolean getIsReusable()
	{
		return false;
	}
	public final String JSONTODT(DataTable dt)
	{
		DBType dtType = BP.Sys.SystemConfig.getAppCenterDBType();
		if (( dtType== DBType.Informix || dtType == DBType.Oracle) && dealSQL!=null)
		{
			//如果数据库不区分大小写, 就要按用户输入的sql进行二次处理。
			String mysql = dealSQL.trim();
			mysql = mysql.substring(6, mysql.toLowerCase().indexOf("from"));
			mysql=mysql.replace(",", " ");
			String[] strs=mysql.split("[ ]", -1);
			for (String s : strs)
			{
				if (StringHelper.isNullOrEmpty(s))
				{
					continue;
				}
				for (DataColumn dc : dt.Columns)
				{
					if (dc.ColumnName.toLowerCase().equals(s.toLowerCase()))
					{
						dc.ColumnName = s;
						break;
					}
				}
			}
		}
		else
		{
			for (DataColumn dc : dt.Columns)
			{
				if (dc.ColumnName.toLowerCase().equals("no"))
				{
					dc.ColumnName = "No";
					continue;
				}
				if (dc.ColumnName.toLowerCase().equals("name"))
				{
					dc.ColumnName = "Name";
					continue;
				}
			}
		}

		StringBuilder JsonString = new StringBuilder();
		if (dt != null && dt.Rows.size() > 0)
		{
			JsonString.append("{ ");
			JsonString.append("\"Head\":[ ");
			for (int i = 0; i < dt.Rows.size(); i++)
			{
				JsonString.append("{ ");
				for (int j = 0; j < dt.Columns.size(); j++)
				{
					if (j < dt.Columns.size() - 1)
					{
						JsonString.append("\"" + dt.Columns.get(j).ColumnName.toString() + "\":\"" + dt.Rows.get(i).getValue(j).toString() + "\",");
					}
					else if (j == dt.Columns.size() - 1)
					{
						JsonString.append("\"" + dt.Columns.get(j).ColumnName.toString() + "\":\"" + dt.Rows.get(i).getValue(j).toString() + "\"");
					}
				}
				//
				//end Of String
				if (i == dt.Rows.size() - 1)
				{
					JsonString.append("} ");
				}
				else
				{
					JsonString.append("}, ");
				}
			}
			JsonString.append("]}");
			return JsonString.toString();
		}
		else
		{
			return null;
		}
	}
}

