package cn.jflow.common.model;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.Log;
import BP.Tools.StringHelper;
import BP.WF.GenerWorkFlow;
import BP.WF.GenerWorkFlowAttr;
import BP.WF.Node;

public class TransferCustomSimpleModel extends BaseModel{

	HttpServletRequest request;
	HttpServletResponse response;
	
	public TransferCustomSimpleModel(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}

	public final void pageLoad(HttpServletRequest request,
			HttpServletResponse response)
	{
		String method = request.getParameter("method");
		String re = "";

		if (StringHelper.isNullOrWhiteSpace(method))
		{
			return;
		}

		try
		{
			long workId = 0;
			
			if("findemps".equals(method))
			{
				String nid = request.getParameter("nodeId");
				String wid = request.getParameter("workId");

				if (StringHelper.isNullOrWhiteSpace(nid))
				{
					re = "[]"; // ReturnJson(false, "nodeid不能为空", false);
				} else if (StringHelper.isNullOrWhiteSpace(wid) || !StringHelper.isNumeric(wid))
				{
					
					re = "[]"; // ReturnJson(false, "workId参数格式不正确", false);
				}
				else
				{
					workId = Long.parseLong(wid);
					GenerWorkFlow gwf = new GenerWorkFlow();
					Node node = null;
					if (gwf.Retrieve(GenerWorkFlowAttr.WorkID, workId) == 0)
					{
						re = "[]"; // ReturnJson(false, "workId参数不正确，未找互此WorkId的发起流程", false);
					}
					else
					{
						node = new Node(Integer.parseInt(nid));
						re = "[";
						DataSet ds = null;
						DataTable dtEmp = null;
						DataTable dtDept = null;
						DataRow rd = null;
						String deptName = null;
						ds = BP.WF.Dev2Interface.WorkOpt_AccepterDB("",node.getNodeID(), workId,0);
						for(DataTable dt :ds.getTables())
						{
							if("Port_Emp".equals(dt.TableName))
							{
								dtEmp = dt;
							}
							if("Port_Dept".equals(dt.TableName))
							{
								dtDept = dt;
							}
						}
						//dtEmp = ds.getTables().Tables[];
						//dtDept = ds.Tables["Port_Dept"];
						 DataTable dtEmpNews = dtEmp.clone();
                         String dept ="";
                         List<DataRow> empRows = null;
                       
                         if (dtDept != null){
                        	 
						 for(DataRow r:dtDept.Rows)
                         {
                             dept = r.getValue("No").toString();
                             empRows = dtEmp.select(String.format("FK_Dept='{0}'", dept));
                            // rd = dtDept.select(String.format("No='{0}'", r.getValue("FK_Dept"))).get(0);

                             for(DataRow r1:empRows)
                             {
                     			DataRow dr = dtEmpNews.NewRow();
                     			for (DataColumn dc : r1.columns) {
                     				dr.put(dc.ColumnName, r1.getValue(dc.ColumnName));
                     			}
                     			dtEmpNews.Rows.add(dr);
                             	}
                         	}
						 }else{
							 for(DataRow r:dtEmp.Rows)
							 {dept = r.getValue("FK_Dept").toString();

                                 if (dtEmpNews.select(String.format("FK_Dept='{0}'", dept)).size() > 0)
                                     continue;

                                 empRows = dtEmp.select(String.format("FK_Dept='{0}'", dept));

                                 for(DataRow r1:empRows)
                                 {
                                	 DataRow dr = dtEmpNews.NewRow();
                          			for (DataColumn dc : r1.columns) 
                          			{
                          			dr.put(dc.ColumnName, r1.getValue(dc.ColumnName));
                          			}
                          			dtEmpNews.Rows.add(dr);
                                 	}
                             	}
							}
                         
							for (DataRow r : dtEmpNews.Rows)
							{
								if (dtEmp.Columns.contains("DeptName"))
								{
									deptName = r.getValue("DeptName").toString();
								}	
								else
								{
									rd = dtDept.select(String.format("No='%1$s'", r.getValue("FK_Dept"))).get(0);
									deptName = rd.getValue("Name").toString();
								}

								re += "{\"no\": \"" + r.getValue("No") + "\", \"name\": \"" + r.getValue("Name") + "\", \"dept\":\"" + deptName + "\"},";
								}
					}

					re = StringHelper.trimEnd(re, ',') + "]";
				}
			}
		}
		catch (RuntimeException ex)
		{
			re = ReturnJson(false, ex.getMessage(), false);
		}

		/**
		Response.Charset = "UTF-8";
		Response.ContentEncoding = System.Text.Encoding.UTF8;
		Response.ContentType = "text/html";
		Response.Expires = 0;
		Response.Write(re);
		Response.End();
		 */
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out ;
		try{
			out = response.getWriter();
			out.write(re);
			out.close();
		}catch(Exception ex)
		{
			Log.DebugWriteError(ex.getMessage());
			ex.printStackTrace();
		}
		
		
	}

	/** 
	 生成返给前台页面的JSON字符串信息
	 
	 @param success 是否操作成功
	 @param msg 消息
	 @param haveMsgJsoned msg是否已经JSON化
	 @return 
	*/
	private String ReturnJson(boolean success, String msg, boolean haveMsgJsoned)
	{
		String kh = haveMsgJsoned ? "" : "\"";
		return "{\"success\":" + String.valueOf(success).toLowerCase() + ",\"msg\":" + kh + (haveMsgJsoned ? msg : msg.replace("\"", "'")) + kh + "}";
	}
}
