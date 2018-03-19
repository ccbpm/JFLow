package BP.DA;

import java.util.ArrayList;
import java.util.Map;

public class DataRowCollection extends ArrayList<DataRow>
{
	
	/**  
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * DataRowCollection所屬的DataTable，唯讀
	 */
	private transient DataTable Table;
	
	/**
	 * DataRowCollection被建立時，一定要指定所屬的DataTable
	 * 
	 * @param table
	 */
	public DataRowCollection(DataTable table)
	{
		this.Table = table;
		
	}
	
	/**
	 * 取得所屬的DataTable
	 * 
	 * @return DataTable
	 */
	public DataTable getTable()
	{
		return this.Table;
	}
	
	public DataRow Add(Object... vals){
		DataRow row = new DataRow(Table);
		if (vals != null){
			int i = 0;
			for (Object val : vals){
				/*
				 * 20171023
				 * 设置接收人选择'与指定节点处理人相同'
				 * 当提交到此节点时, 该方法传入的参数vals为DataRow的集合
				 */
//				row.put(Table.Columns.get(i++).ColumnName, val);
				
				String key = Table.Columns.get(i++).ColumnName;
				Object value;
				if (val instanceof Map) {
					value = ((Map<String, Object>) val).get(key);
				} else {
					value = val;
				}
				row.put(key, value);
			}
			this.add(row);
		}
		return row;
	}
}
