package cn.jflow.controller.wf.admin.xap.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import BP.DA.DataColumn;
import BP.DA.DataColumnCollection;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;

public class CCFormUtil {
	
	public static String SaveDT(DataTable dt) throws Exception
    {
        if (dt.Rows.size() == 0)
            return "";

        String tableName = dt.TableName.replace("CopyOf", "");
//        if (tableName.equals("Sys_MapData".toUpperCase()))
//        {
//            int i = 0;
//        }
        //MaxTop,MaxLeft,MaxRight,MaxEnd合并到AtPara
        if (tableName.equalsIgnoreCase("Sys_MapData") && dt.Columns.contains("MaxLeft") && dt.Columns.contains("MaxTop"))
            return "";

        if (dt.Columns.size() <= 0)
            return null;

        TableSQL sqls = getTableSql(tableName, dt.Columns);
        String updataSQL = sqls.UPDATED;
        String pk = sqls.PK;
        String insertSQL = sqls.INSERTED;
        
        
        for(DataRow dr:dt.Rows)
        {
            BP.DA.Paras ps = new BP.DA.Paras();
            
          
	            for(DataColumn dc:dt.Columns)
	            {
	                if (dc.ColumnName.equalsIgnoreCase(pk))
	                    continue;
	
	                if (tableName.equalsIgnoreCase("Sys_MapAttr") && dc.ColumnName.equalsIgnoreCase("UIBindKey"))
	                    continue;
	
	                if (updataSQL.contains(BP.Sys.SystemConfig.getAppCenterDBVarStr() + dc.ColumnName.trim()))
	                {
	                	try {
	                		ps.Add(dc.ColumnName.trim().toUpperCase(), dr.get(dc.ColumnName.trim()));
						} catch (Exception e) {
							ps.Add(dc.ColumnName.trim(),null);
						}
	                }
	                  
	            }

            ps.Add(pk.toUpperCase(), dr.get(pk.toUpperCase()));
            ps.SQL = updataSQL;
            try
            {
                if (BP.DA.DBAccess.RunSQL(ps) == 0)
                {
                    ps.clear();
                    for(DataColumn dc:dt.Columns)
                    {
                        if (tableName.equalsIgnoreCase("Sys_MapAttr") && dc.ColumnName.equalsIgnoreCase("UIBindKey"))
                            continue;

                        if (updataSQL.contains(BP.Sys.SystemConfig.getAppCenterDBVarStr() + dc.ColumnName.trim()))
                            ps.Add(dc.ColumnName.trim().toUpperCase(), dr.get(dc.ColumnName.trim()));
                    }
                    ps.SQL = insertSQL;
                    BP.DA.DBAccess.RunSQL(ps);
                    continue;
                }
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }
        return null;
    }
	/// <summary>
    /// 保存文件
    /// </summary>
    /// <param name="FileByte">文件大小</param>
    /// <param name="fileName">文件名</param>
    /// <param name="saveTo">保存路径</param>
    /// <returns></returns>
    public static String UploadFile(byte[] FileByte, String fileName, String saveTo) throws IOException
    {
    	//创建目录.
        String pathSave = BP.Sys.SystemConfig.getPathOfTemp() + "/" + saveTo;
        File file1=new File(pathSave);
        if (file1.exists())
            file1.mkdirs();

        String filePath = pathSave + fileName;
        
        File file2=new File(filePath);
        if (file2.exists())
            file2.delete();

      //创建文件流实例，用于写入文件
		FileOutputStream stream = new FileOutputStream(filePath);

		//写入文件
		stream.write(FileByte, 0, FileByte.length);
		stream.close();

        DataSet ds = new DataSet();
        ds.readXml(filePath);

        return DataSet.ConvertDataSetToXml(ds);
    }
    
    class TableSQL
    {
    	public TableSQL(){
    		
    	}
    	
        public String PK;
        public String INSERTED;
        public String UPDATED;
    }
    
    static Hashtable<String, TableSQL> dicTableSql = new Hashtable<String, TableSQL>();
	
    public static TableSQL getTableSql(String tableName, DataColumnCollection columns)
    {
        TableSQL tableSql = null;
        if (dicTableSql.containsKey(tableName))
        {
            tableSql = dicTableSql.get(tableName);
        }
        else
        {
            if (columns != null && columns.size() > 0)
            {
                String igF = "@RowIndex@RowState@";

                //生成sqlUpdate
                String sqlUpdate = "UPDATE " + tableName + " SET ";
                for(DataColumn dc:columns)
                {
                    if (igF.contains(":" + dc.ColumnName + ":"))
                        continue;
                    
                    String str=dc.ColumnName;
                    if(str.equalsIgnoreCase("MyPK") || str.equalsIgnoreCase("OID") 
                    		||str.equalsIgnoreCase("No")){
                    	continue;
                    }

                    if (tableName.equalsIgnoreCase("Sys_MapAttr") && dc.ColumnName.equalsIgnoreCase("UIBindKey"))
                        continue;
                    try
                    {
                        sqlUpdate += dc.ColumnName + "=" + BP.Sys.SystemConfig.getAppCenterDBVarStr()
                        		+ dc.ColumnName.trim() + ",";
                    }
                    catch(Exception e)
                    {
                    }
                }
                sqlUpdate = sqlUpdate.substring(0, sqlUpdate.length() - 1);
                String pk = "";
                for(DataColumn c:columns){
                	if (c.ColumnName.equalsIgnoreCase("NodeID")){
                        pk = "NODEID";
                        break;
                	}
                    if (c.ColumnName.equalsIgnoreCase("MyPK")){
                        pk = "MYPK";
                    	break;
                    }
                    if (c.ColumnName.equalsIgnoreCase("OID")){
                        pk = "OID";
                        break;
                    }
                    if (c.ColumnName.equalsIgnoreCase("No")){
                        pk = "NO";
                        break;
                    }
                }
                
                sqlUpdate += " WHERE " + pk + "=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + pk;

                //生成sqlInsert
                String sqlInsert = "INSERT INTO " + tableName + "(";
                for(DataColumn dc:columns)
                {
                    if (igF.contains("@" + dc.ColumnName.trim() + "@"))
                        continue;

                    if (tableName.equalsIgnoreCase("Sys_MapAttr") && dc.ColumnName.trim().equalsIgnoreCase("UIBindKey"))
                        continue;

                    sqlInsert += dc.ColumnName.trim() + ",";
                }
                sqlInsert = sqlInsert.substring(0, sqlInsert.length() - 1);
                sqlInsert += ") VALUES (";
                for(DataColumn dc:columns)
                {
                    if (igF.contains("@" + dc.ColumnName + "@"))
                        continue;

                    if (tableName.equalsIgnoreCase("Sys_MapAttr") && dc.ColumnName.trim().equalsIgnoreCase("UIBindKey"))
                        continue;

                    sqlInsert += BP.Sys.SystemConfig.getAppCenterDBVarStr() + dc.ColumnName.trim() + ",";
                }
                sqlInsert = sqlInsert.substring(0, sqlInsert.length() - 1) + ")";
                CCFormUtil ccfu=new CCFormUtil();
                tableSql = ccfu.new TableSQL();
                tableSql.UPDATED = sqlUpdate;
                tableSql.INSERTED = sqlInsert;
                tableSql.PK = pk;

                dicTableSql.put(tableName, tableSql);
            }
        }

        return tableSql;
    }
	
}
