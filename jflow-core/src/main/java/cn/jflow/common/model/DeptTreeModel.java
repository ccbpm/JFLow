package cn.jflow.common.model;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.Log;
import BP.En.QueryObject;
import BP.Tools.StringHelper;
import BP.WF.Template.NodeDept;
import BP.WF.Template.NodeDeptAttr;
import BP.WF.Template.NodeDepts;

public class DeptTreeModel{
	
	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	private StringBuilder treeResult = new StringBuilder();
	
	private StringBuilder treesb = new StringBuilder();
	
	public DeptTreeModel(HttpServletRequest request, HttpServletResponse response) {
		this.response = response;
		this.request = request;
	}
	
	public final String getFK_Node() {
		String nodeId= "";
		try {
			nodeId = request.getParameter("NodeID");
			if(StringHelper.isNullOrEmpty(nodeId)){
				nodeId = request.getParameter("FK_Node");
			}
		} catch (Exception e) {
			Log.DebugWriteError("nodeId为空："+e.getMessage());
		}
		return nodeId;
	}	
	
	public void Page_Load()
    {
		String method = "";
		
		//返回值
		String s_responsetext = "";
		if (!StringHelper.isNullOrEmpty(request.getParameter("method")))
		{
			method = request.getParameter("method").toString();
		}
		method = request.getParameter("method");
		if ("TreeDateMet".equals(method))
		{
			s_responsetext = getTreeDateMet();
		}
		else if ("insertMet".equals(method))
		{
			s_responsetext = insertMet();
		}
		if (StringHelper.isNullOrEmpty(s_responsetext))
		{
			s_responsetext = "";
		}

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out =null;
		try {
			out = response.getWriter();
			out.write(s_responsetext);
		} catch (IOException e) {
			Log.DebugWriteError("response.getWriter() 异常:"+e.getMessage());
		}finally
		{
			if(null != out){
				out.close();
			}
		}
    }
	
	private String getTreeDateMet(){
		String sql = "";
		String s_responsetext = "";
        String s_checkded = "";
		
        if (BP.WF.Glo.getOSModel() == BP.Sys.OSModel.OneMore)
            sql = "select NO,NAME,ParentNo from port_dept ORDER BY Idx";
        else
            sql = "select NO,NAME,ParentNo from port_dept  ";

        DataTable dt_dept = DBAccess.RunSQLReturnTable(sql);
       
        //节点部门集合
        NodeDepts nodeDepts = new NodeDepts();
        QueryObject obj = new QueryObject(nodeDepts);
        obj.AddWhere(NodeDeptAttr.FK_Node,this.getFK_Node());
        obj.DoQuery();
        
        //将已有部门，拼接字符串
        if (nodeDepts != null && nodeDepts.size() > 0)
        {
            for(NodeDept item : nodeDepts.ToJavaList())
            {
                s_checkded += "," + item.getFK_Dept() + ",";
            }
        }
        s_responsetext = GetTreeJsonByTable(dt_dept, "NO", "NAME", "ParentNo", "0", s_checkded);
        if (StringHelper.isNullOrEmpty(s_responsetext)||"[]".equals(s_responsetext))//如果为空，使用另一种查询
        {
            treeResult.delete(0, treeResult.length());
            s_responsetext = GetTreeJsonByTable(dt_dept, "NO", "NAME", "ParentNo", "O0", s_checkded);
        }
         return s_responsetext;
	}
	
	/**
	 * 保存数据
	 */
	private String insertMet(){
		String getId = request.getParameter("getId");
	    NodeDept nodeDept = new NodeDept();
	    nodeDept.Delete(NodeDeptAttr.FK_Node, this.getFK_Node());
	    if (!StringHelper.isNullOrEmpty(getId))
	    {
	    	String[] Depts = getId.split(",");
	        for (int i = 0; i < Depts.length; i++)
	        {
	            nodeDept = new NodeDept();
	            nodeDept.setFK_Node(Integer.parseInt(this.getFK_Node()));
	            nodeDept.setFK_Dept(Depts[i]);
	            nodeDept.Insert();
	        }
	    }
	    return "true";
	}
	
	 /**
	  * 数据源
	  * ID列
	  * Text列
	  * 关系字段
	  * 父ID
	  * easyui tree json格式
	  */
     public String GetTreeJsonByTable(DataTable tabel, String idCol, String txtCol, String rela, Object pId, String CheckedString)
     {
    	 String treeJson = "";
         treeResult.append(treesb.toString());
         treesb.delete(0, treesb.length());
         if (tabel.Rows.size() > 0)
         {
             treesb.append("[");
             String filer = "";
             if ("".equals(pId.toString()))
             {
            	 filer = String.format("%1$s", rela.toLowerCase());
             }
             else
             {
            	 filer = String.format("%1$s='%2$s'", rela.toLowerCase(), pId);
             }
             List<DataRow> rows = tabel.select(filer);
             if (rows.size() > 0)
             {
                 for(DataRow row : rows)
                 {
                     if (treeResult.length() == 0)
                     {
                         treesb.append("{\"id\":\"" + row.getValue(idCol)
                             + "\",\"text\":\"" + row.getValue(txtCol)
                              + "\",\"checked\":" + CheckedString.contains("," + row.getValue(idCol) + ",") + ",\"state\":\"open\"");
                     }else if (tabel.select(String.format("%1$s='%2$s'", rela.toLowerCase(), row.getValue(idCol))).size() > 0)
                     {
                         treesb.append("{\"id\":\"" + row.getValue(idCol)
                             + "\",\"text\":\"" + row.getValue(txtCol)
                              + "\",\"checked\":" + CheckedString.contains("," + row.getValue(idCol) + ",") + ",\"state\":\"closed\"");
                     }else
                     {
                         treesb.append("{\"id\":\"" + row.getValue(idCol)
                             + "\",\"text\":\"" + row.getValue(txtCol)
                              + "\",\"checked\":" + CheckedString.contains("," + row.getValue(idCol) + ","));
                     }
                     if (tabel.select(String.format("%1$s='%2$s'", rela.toLowerCase(), row.getValue(idCol))).size() > 0)
                     {
                         treesb.append(",\"children\":");
                         GetTreeJsonByTable(tabel, idCol, txtCol, rela, row.getValue(idCol), CheckedString);
                         treeResult.append(treesb.toString());
                         treesb.delete(0, treesb.length());
                     }
                     treeResult.append(treesb.toString());
                     treesb.delete(0, treesb.length());
                     treesb.append("},");
                 }
                 treesb.deleteCharAt(treesb.length() - 1);
             }
             treesb.append("]");
             treeResult.append(treesb.toString());
             treeJson = treeResult.toString();
             treesb.delete(0, treesb.length());
         }
         return treeJson;
     }
 }
	
