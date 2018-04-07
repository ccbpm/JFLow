package BP.DA;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import BP.Tools.StringHelper;

public class DataTable implements Cloneable
{
	
	/**
	 * 保存DataRow的集合，在DataTable初始化時，便會建立
	 */
	public DataRowCollection Rows;
	/**
	 * 保存DataColumn的集合，在DataTable初始化時，便會建立
	 */
	public DataColumnCollection Columns;
	/**
	 * DataTable的名稱，沒什麼用到
	 */
	public String TableName;
	private DataRow[] dataRows;
	/**
	 * @return 创建并返回此对象的一个副本。
	 * @throws CloneNotSupportedException
	 */
	public DataTable clone()
	{
		try
		{
			DataTable v = (DataTable) super.clone();
			v.Rows = (DataRowCollection) this.Rows.clone();
			v.Columns = (DataColumnCollection) this.Columns.clone();
			return v;
		} catch (CloneNotSupportedException e)
		{
			// this shouldn't happen, since we are Cloneable
			throw new InternalError();
		}
	}
	
	/**
	 * 初始化DataTable，並建立DataColumnCollection，DataRowCollection
	 */
	public DataTable()
	{
		this.Columns = new DataColumnCollection(this);
		this.Rows = new DataRowCollection(this);
		
	}
	
	/**
	 * 除了初始化DataTable， 可以指定DataTable的名字(沒什麼意義)
	 * 
	 * @param dataTableName
	 *            DataTable的名字
	 */
	public DataTable(String tableName)
	{
		this();
		this.TableName = tableName;
	}
	
	/**
	 * 由此DataTable物件來建立一個DataRow物件
	 * 
	 * @return DataRow
	 */
	public DataRow NewRow()
	{
		
		DataRow row = new DataRow(this);// DataRow為呼叫此方法DataTable的成員
		
		return row;
	}
	
	/**
	 * 把DataTable當做二維陣列，給列索引和行索引，設定值的方法 <br/>
	 * (發佈者自行寫的方法)
	 * 
	 * @param rowIndex
	 *            列索引(從0算起)
	 * @param columnIndex
	 *            行索引(從0算起)
	 * @param value
	 *            要給的值
	 */
	public void setValue(int rowIndex, int columnIndex, Object value)
	{
		this.Rows.get(rowIndex).setValue(columnIndex, value);
	}
	
	/**
	 * 把DataTable當做二維陣列，給列索引和行名稱，設定值的方法 <br/>
	 * (發佈者自行寫的方法)
	 * 
	 * @param rowIndex
	 *            列索引(從0算起)
	 * @param columnIndex
	 *            行名稱
	 * @param value
	 *            要給的值
	 */
	public void setValue(int rowIndex, String columnName, Object value)
	{
		this.Rows.get(rowIndex).setValue(columnName.toLowerCase(), value);
		// this.Rows.get(rowIndex).setValue(columnName, value);
		
	}
	
	/**
	 * 把DataTable當做二維陣列，給列索引和行索引，取得值的方法 <br/>
	 * (發佈者自行寫的方法)
	 * 
	 * @param rowIndex
	 *            列索引(從0算起)
	 * @param columnIndex
	 *            行索引(從0算起)
	 * @return 回傳該位置的值
	 */
	public Object getValue(int rowIndex, int columnIndex)
	{
		return this.Rows.get(rowIndex).getValue(columnIndex);
	}
	
	public void Clear()
	{
		Rows.clear();
		Columns.clear();
	}
	
	public List<DataRow> Select(Map<String, Object> filterMap) throws Exception {
//		return Select(filterMap, false);
//	}
//	
//	public List<DataRow> Select(Map<String, Object> filterMap, boolean isOr) throws Exception {
		List<DataRow> dataRowList = new ArrayList<DataRow>();
		outer: for (int i = 0; i < Rows.size(); i++)
		{
			DataRow row = Rows.get(i);
			for (Object key : filterMap.keySet().toArray())
			{
				Object lefteql = filterMap.get(key);
				Object righteql = row.getValue(key.toString());
				if (lefteql == null || "".equals(lefteql))
				{
					if (righteql == null || "".equals(righteql))
					{
						continue;
					} else
					{
						continue outer;
					}
				} else if (!filterMap.get(key).toString().toUpperCase().equals(
						row.getValue(key.toString()).toString().toUpperCase()))
				{
					continue outer;
				}
			}
			dataRowList.add(row);
		}
		return dataRowList;
	}
	
	/**  
     * 返回符合过滤条件的数据行集合，并返回
     * @param 过滤字符串，例如  a>1 and a>=2 or a<3 and a<=4 or a!=5 and a=6 or a!='b' and a='c'
     * @return 过滤后的 List<DataRow>    
     */
   public List<DataRow> select(String filterString) {
        List<DataRow> rows = new ArrayList<DataRow>();
        if (!StringHelper.isNullOrEmpty(filterString)) {
        	 boolean bl;
             for (Object row : this.Rows) {
                 DataRow currentRow = (DataRow) row;
                 try {
                     bl = dataRowCompute(filterString, currentRow);
                 } catch (Exception e) {
                	 System.err.println("语法错误");
                	 e.printStackTrace();
                     continue;
                 }
                 if (bl) {
                     rows.add(currentRow);
                 }
             }
            return rows;

        } else {
            return rows;
        }
   }
   
   /**  
    * 返回符合过滤条件的数据行集合，并返回
    * @param 过滤字符串，例如  a>1 and a>=2 or a<3 and a<=4 or a!=5 and a=6 or a!='b' and a='c'
    * @return 过滤后的 List<DataRow>    
    */
  public List<DataRow> selectx(String filterString) {
       List<DataRow> rows = new ArrayList<DataRow>();
       if (!StringHelper.isNullOrEmpty(filterString)) {
       	 boolean bl = false;
            for (Object row : Rows) {
                DataRow currentRow = (DataRow) row;
                try {
                    //bl = dataRowCompute(filterString, currentRow);
                	if(filterString.split("=").length>1)
                		bl = currentRow.getValue(filterString.split("=")[0].trim()).equals(filterString.split("=")[1].trim());
                } catch (Exception e) {
               	 System.err.println("语法错误");
               	 e.printStackTrace();
                    continue;
                }
                if (bl) {
                    rows.add(currentRow);
                }
            }
           return rows;

       } else {
           return rows;
       }
  }
   
   /**
    * 数据行计算，是否符合filterString过滤条件
    * @param filterString 过滤条件，支持  and or > >= < <= != = 操作符，暂不支持括号
    * @param row 数据行
    * @return true 符合
    */
   private boolean dataRowCompute(String filterString, DataRow row){
	   if (filterString == null || row == null){
		   return false;
	   }
	   boolean orResult = false;
	   try {
		   String[] or = filterString.split(" (?i)or ");	// 忽略大小写
		   for (String o : or){
			   boolean andResult = true;
			   String[] and = o.split(" (?i)and ");	// 忽略大小写
			   for (String a : and){
				   String[] kv = null;
				   if (a.contains(">=")){
					   kv = a.split(">=");
				   }else if (a.contains(">")){
					   kv = a.split(">");
				   }else if (a.contains("<=")){
					   kv = a.split("<=");
				   }else if (a.contains("<")){
					   kv = a.split("<");
				   }else if (a.contains("!=")){
					   kv = a.split("!=");
				   }else if (a.contains("=")){
					   kv = a.split("=");
				   }
				   
				   
				   if (kv != null && kv.length == 2){
					   String key = kv[0].trim(), value = kv[1].trim();
					   if (key != null && value != null){
						   Object val = row.getValue(key);
						   // is null
						   if (value.equalsIgnoreCase("is null")){
							   if (!(val == null)){
								   andResult = false;
								   break;
							   }
						   }
						   // is not null
						   else if (value.equalsIgnoreCase("is not null")){
							   if (!(val != null)){
								   andResult = false;
								   break;
							   }
						   }
						   // is string
						   else if (value.startsWith("'") && value.endsWith("'")){
							   String v = value.replaceAll("'", "");
							   if (a.contains("!=")){
								   if (!(!val.toString().equalsIgnoreCase(v))){
									   andResult = false;
									   break;
								   }
							   }else if (a.contains("=")){
								   if (!(val.toString().equalsIgnoreCase(v))){
									   andResult = false;
									   break;
								   }
							   }
						   }
						   // is number
						   else{
							   Integer v = Integer.valueOf(value.toString());
							   Integer dbVal = Integer.valueOf(val.toString());
							   if (a.contains(">=")){
								   if (!(dbVal >= v)){
									   andResult = false;
									   break;
								   }
							   }else if (a.contains(">")){
								   if (!(dbVal > v)){
									   andResult = false;
									   break;
								   }
							   }else if (a.contains("<=")){
								   if (!(dbVal <= v)){
									   andResult = false;
									   break;
								   }
							   }else if (a.contains("<")){
								   if (!(dbVal < v)){
									   andResult = false;
									   break;
								   }
							   }else if (a.contains("!=")){
								   if (!(dbVal != v)){
									   andResult = false;
									   break;
								   }
							   }else if (a.contains("=")){
								   if (!(dbVal == v)){
									   andResult = false;
									   break;
								   }
							   }
						   }
					   }
				   }
			   }
			   // 如果有一个and成立，则成立。
			   if (andResult){
				   orResult = true;
				   break;
			   }
		   }
       } catch (Exception e) {
			System.err.println("语法错误");
			e.printStackTrace();
       }
	   return orResult;
    }
	
	public String getTableName() {
		return TableName;
	}

	public void setTableName(String tableName) {
		TableName = tableName;
	}

	/**
	 * 把DataTable當做二維陣列，給列索引和行名稱，取得值的方法 <br/>
	 * (發佈者自行寫的方法)
	 * 
	 * @param rowIndex
	 *            列索引(從0算起)
	 * @param columnName
	 *            行名稱
	 * @return 回傳該位置的值
	 */
	public Object getValue(int rowindex, String columnName)
	{
		return this.Rows.get(rowindex).getValue(columnName);
		// return this.Rows.get(rowindex).getValue(columnName);
	}

	public DataRow[] Select(String string) {
		DataRow[] dataRowsx = null;
		List<DataRow> dataRowList = new ArrayList<DataRow>();
        if (StringHelper.isNullOrEmpty(string)==false) {
        	 boolean bl;
             for (Object row : Rows) {
                 DataRow currentRow = (DataRow) row;
                 try {
                     bl = dataRowCompute(string, currentRow);
                 } catch (Exception e) {
                	 System.err.println("语法错误");
                	 e.printStackTrace();
                     continue;
                 }
                 if (bl) {
                	 dataRowList.add(currentRow);
                 }
             }
             dataRowsx = new DataRow[dataRowList.size()];
             for(int i=0;i<dataRowList.size();i++){
            	 dataRowsx[i] = dataRowList.get(i);
             }
            return dataRowsx;

        } else {
            return dataRowsx;
        }
	}
	
}
