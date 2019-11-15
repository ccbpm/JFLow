package BP.DA;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import BP.Tools.StringHelper;

public class DataSet {

	private String name;

	public List<DataTable> Tables;

	public Hashtable<String, DataTable> hashTables;


	public DataSet() {
		if (Tables == null) {
			Tables = new ArrayList<DataTable>();
			hashTables = new Hashtable<String, DataTable>();
		}
	}
	
	public boolean removeTableByName(String tableName)
	{
		
		for (DataTable dtb : this.Tables)
			{
				if( tableName.equals(dtb.getTableName()))
				{
					this.Tables.remove(dtb);
					return true;
							 
				}
			}	
		
		return false;
		 
	}
	
	public DataTable GetTableByName(String tableName)
	{
		
		for (DataTable dtb : this.Tables)
			{
				if( tableName.equals(dtb.getTableName()))
				{
					return dtb;
				 
				}
			}	
		
		return null;
	   
	}

	public DataSet(String name) {
		if (Tables == null) {
			Tables = new ArrayList<DataTable>();
			hashTables = new Hashtable<String, DataTable>();
		}
		this.name = name;
	}

	/**
	 * DataSet 以xml形式写入文件
	 * 
	 * @param file
	 */
	public void WriteXml(String file) {
		WriteXml(file, XmlWriteMode.IgnoreSchema, new DataSet("NewDataSet"));
	}

	/**
	 * DataSet 以xml形式写入文件
	 * 
	 * @param path
	 * @param mode
	 *            暂不支持DiffGram格式
	 * @throws Exception
	 */
	public void WriteXml(String path, XmlWriteMode mode, DataSet ds) {
		StringBuilder str = new StringBuilder("<?xml version=\"1.0\" standalone=\"yes\"?>");
		str.append("<NewDataSet>");
		// 输出表架构
		for (int i = 0; i < ds.Tables.size(); i++) {
			DataTable dt = ds.Tables.get(i);
			for (int k = 0; k < dt.Rows.size(); k++) {
				str.append("<");
				str.append(dt.TableName);
				str.append(">");
				for (int j = 0; j < dt.Columns.size(); j++) {
					DataColumn dc = dt.Columns.get(j);

					str.append("<");
					str.append(dc.ColumnName);
					str.append(">");
					try {
						Object value = dt.Rows.get(k).getValue(dc);
						if (value.toString().contains(">") || value.toString().contains("<") || value.toString().contains("&")
								|| value.toString().contains("'") || value.toString().contains("\"")) {
							value = value.toString().replace(">", "&gt;");
							value = value.toString().replace("<", "&lt;");
							value = value.toString().replace("&", "&amp;");
							value = value.toString().replace("'", "&apos;");
							value = value.toString().replace("\"", "&quot;");
						}
						str.append(value);
					} catch (Exception e) {

					}
					str.append("</");
					str.append(dc.ColumnName);
					str.append(">");

				}
				str.append("</");
				str.append(dt.TableName);
				str.append(">");
			}
		}
		// }
		str.append("</NewDataSet>");
		String temp = str.toString();
		// String temp = formatXml(str.toString());

		// 写入文件
		File file = new File(path);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
			BufferedWriter br = new BufferedWriter(osw);
			br.write(temp.toString());
			fos.flush();
			br.close();
			fos.close();
			osw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String formatXml(String str) throws Exception {
		Document document = null;
		document = DocumentHelper.parseText(str);
		// 格式化输出格式
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("gbk");
		StringWriter writer = new StringWriter();
		// 格式化输出流
		XMLWriter xmlWriter = new XMLWriter(writer, format);
		// 将document写入到输出流
		xmlWriter.write(document);
		xmlWriter.close();

		return writer.toString();
	}

	public void readXmls(String xmlPath) throws Exception {
		if (StringHelper.isNullOrEmpty(xmlPath)) {
			return;
		}

		SAXReader reader = new SAXReader();

		File file = new File(xmlPath);
		// DataSet ds=new DataSet();
		if (file.exists()) {
			Document document = reader.read(file);// 读取XML文件
			Element root = document.getRootElement();// 得到根节点
			for (Iterator i = root.elementIterator(); i.hasNext();) {
				Element e = (Element) i.next();
				boolean type = false;
				for (int k = 0; k < this.Tables.size(); k++) {
					if (this.Tables.get(k).TableName.equals(e.getName())) {
						DataTable dt = this.Tables.get(k);
						DataRow dr = dt.NewRow();
						DataColumn dc = null;
						for (Iterator j = e.elementIterator(); j.hasNext();) {
							Element cn = (Element) j.next();
							dc = new DataColumn(cn.getName());
							dr.setValue(dc, cn.getText());
						}
						dt.Columns.Add(dc);
						dt.Rows.add(dr);
						type = true;
						break;
					}
				}
				if (type) {
					continue;
				}
				DataTable dt = new DataTable(e.getName());
				DataRow dr = dt.NewRow();
				DataColumn dc = null;
				for (Iterator j = e.elementIterator(); j.hasNext();) {
					Element cn = (Element) j.next();
					dc = new DataColumn(cn.getName());
					dt.Columns.Add(dc);
					dr.setValue(dc, cn.getText());
				}
				dt.Rows.add(dr);
				this.Tables.add(dt);
				this.hashTables.put(e.getName(), dt);
			}
		}
	}

	public void readXmlm(String xml) {
		if (StringHelper.isNullOrEmpty(xml))
			return;
		try {
			xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n <NewDataSets>" + xml + "</NewDataSets>";
			// 创建xml解析对象
			SAXReader reader = new SAXReader();
			// 定义一个文档
			Document document = null;
			// 将字符串转换为
			document = reader.read(new ByteArrayInputStream(xml.getBytes("UTF-8")));
			@SuppressWarnings("unchecked")
			List<Element> elements = document.selectNodes("//NewDataSets/NewDataSet");

			int i = 0;
			DataTable oratb = new DataTable();
			for (Element element : elements) {
				DataRow dr = oratb.NewRow();
				@SuppressWarnings("unchecked")
				Iterator<Element> iter = element.elementIterator();
				int j = 0;
				while (iter.hasNext()) {
					Element itemEle = (Element) iter.next();
					if (i == 0) {
						oratb.Columns.Add(itemEle.getName());
					}
					dr.setValue(j, itemEle.getTextTrim());
					j++;
				}
				oratb.Rows.add(dr);
				i++;
			}
			this.Tables.add(oratb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DataTable> getTables() {
		return Tables;
	}

	public void setTables(List<DataTable> tables) {
		Tables = tables;
	}

	public Hashtable<String, DataTable> getHashTabless() {
		return hashTables;
	}

	public void setHashTables(Hashtable<String, DataTable> hashTables) {
		this.hashTables = hashTables;
	}

	/**
	 * 获取文件的XML文件内容。
	 * @param path xml文件路径
	 * @return xml文件内容
	 */
	public String xmlToString(String path) {
		String line = null;
		StringBuffer strBuffer = new StringBuffer();
		try {
			String encoding = "UTF-8"; // 字符编码
			/*InputStream in = BP.Tools.HttpClientUtil.getInputStream(path);
			if(in!=null){
				InputStreamReader read = new InputStreamReader(in);
				BufferedReader bufferedReader = new BufferedReader(read);
				while ((line = bufferedReader.readLine()) != null) {
					strBuffer.append(line + "\n");
				}
				read.close();
			}*/

			File file = new File(path);
			if (file.isFile() && file.exists()) {

				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
				BufferedReader bufferedReader = new BufferedReader(read);
				while ((line = bufferedReader.readLine()) != null) {
					strBuffer.append(line + "\n");
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件！" + path);
			}
		} catch (Exception e) {
			System.out.println("读取文件内容操作出错！" + e.getMessage());
		}
		return strBuffer.toString();
	}

	/**
	 * 读取XML文件
	 * @param xmlpath xml文件路径
	 * @author ThinkGem
	 */
	@SuppressWarnings("rawtypes")
	public void readXml(String xmlpath) {
		if (StringHelper.isNullOrEmpty(xmlpath)){
			return;
		}
		try {
			String xml = xmlToString(xmlpath);
			Document document = new SAXReader().read(new ByteArrayInputStream(xml.getBytes("UTF-8")));
			Element element = document.getRootElement();
			
			// 遍历 DataTable
			for (Iterator iterator = element.elementIterator(); iterator.hasNext();) {
				Element el = (Element) iterator.next();
//				System.out.println(" ===================== " + el.getName());
				
				// 如果没有获取到DataTable则新建一个
				DataTable dt = hashTables.get(el.getName());
				if (dt == null){
					dt = new DataTable(el.getName());
					hashTables.put(el.getName(), dt);
					Tables.add(dt);
				}
				
				// 新增一行数据
				DataRow dr = dt.NewRow();
				dt.Rows.add(dr);

				// 遍历该DataTable的属性
				for (Iterator it = el.attributeIterator(); it.hasNext();) {
					Attribute at = (Attribute)it.next();
//					System.out.println(" ======= " + at.getName());
					//sunxd 修改
					//由于"at.getName().toLowerCase()"语句导至  isContains 方法判断永远不成立，会给TABLE插入很多重复列
					//将at.getName().toLowerCase() 修改为  at.getName()
					if (!isContains(dt.Columns.subList(0, dt.Columns.size()), at.getName())) {
						dt.Columns.Add(at.getName());
					}
					dr.setValue(at.getName(), at.getValue());
				}
				
				// 遍历该DataTable的子元素
				for (Iterator it = el.elementIterator(); it.hasNext();) {
					Element at = (Element) it.next();

					if (!isContains(dt.Columns.subList(0, dt.Columns.size()), at.getName())) {
						dt.Columns.Add(at.getName());
					}
					String value = at.getText();
					try {
						//导出模板进行了转义，现在进行反转 dgq 2018-7-5
						if (value.toString().contains("&gt;") || value.toString().contains("&lt;") 
								|| value.toString().contains("&amp;") || value.toString().contains("&apos;") 
								|| value.toString().contains("&quot;")) {
							value = value.toString().replace("&amp;", "&");
							value = value.toString().replace("&gt;",">");
							value = value.toString().replace("&lt;","<");
							value = value.toString().replace("&apos;","'");
							value = value.toString().replace("&quot;","\"");
						}
					} catch (Exception e) {

					}
					dr.setValue(at.getName(), value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isContains(List<DataColumn> dcList, String column) {
		for (DataColumn dc : dcList) {
			if (dc.ColumnName.equals(column)) {
				return true;
			}
		}
		return false;
	}

	public static String ConvertDataSetToXml(DataSet dataSet) {
		if (dataSet != null) {
			String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
			str += "<xs:schema id=\"NewDataSet\" xmlns=\"\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:msdata=\"urn:schemas-microsoft-com:xml-msdata\">";

			str += "<xs:element name=\"NewDataSet\" msdata:IsDataSet=\"true\" msdata:UseCurrentLocale=\"true\">";
			str += "<xs:complexType><xs:choice minOccurs=\"0\" maxOccurs=\"unbounded\">";
			// 循环每一个表的列

			for (int n = 0; n < dataSet.getTables().size(); n++) {
				str += "<xs:element name=\"" + dataSet.getTables().get(n).TableName + "\">";
				str += "<xs:complexType><xs:sequence>";
				DataTable table = dataSet.getTables().get(n);
				for (DataColumn col : table.Columns) {
					str += "<xs:element name=\"" + col.ColumnName;
					str += "\" type=\"";
					try {
						col.DataType.toString();
						str += IsType(col.DataType.toString());
					} catch (Exception e) {
						str += "xs:string";
					}
					str += "\" minOccurs=\"0\" />";
				}
				str += "</xs:sequence></xs:complexType></xs:element>";
			}
			str += "</xs:choice></xs:complexType></xs:element><_NewDataSet>";
			for (int i = 0; i < dataSet.getTables().size(); i++) {
				DataTable dt = dataSet.getTables().get(i);
				DataTable table = dataSet.getTables().get(i);
				for (int j = 0; j < table.Rows.size(); j++) {
					str += "<_" + i + ">";
					DataRow row = table.Rows.get(j);
					for (int a = 0; a < row.columns.size(); a++) {
						DataColumn col = table.Columns.get(a);
						str += "<_" + a + ">";
						if (row.getValue(col) == null) {
							str += "";
						} else {
							if (col.ColumnName.equals("icon")) {
								if (row.getValue(col).equals("")) {
									str += "审核";
								} else if (row.getValue(col).equals("Default")) {
									str += "审核";
								} else {
									str += row.getValue(col);
								}
							} else {
								str += row.getValue(col);
							}
						}
						str += "</_" + a + ">";

					}
					str += "</_" + i + ">";
				}
			}
			str += "</_NewDataSet>";
			str += "</xs:schema>";
			return str;
		}
		return null;
	}

	public static String IsType(String name) {
		if (name.equals("class java.lang.Integer")) {
			name = "xs:int";
		}
		if (name.equals("class java.lang.Long")) {
			name = "xs:long";
		}
		if (name.equals("class java.lang.Float")) {
			name = "xs:float";
		}
		if (name.equals("class java.lang.Double")) {
			name = "xs:double";
		}
		if (name.equals("class java.lang.String")) {
			name = "xs:string";
		}
		if (name.equals("class java.math.BigDecimal")) {
			name = "xs:int";
		}
		return name;
	}

	public static void main(String[] args) throws Exception {

		// List<DataTable> tableList = new ArrayList<DataTable>();
		// DataTable table = new DataTable("Emp");
		// DataColumn col = new DataColumn("id", Integer.class);
		// DataColumn col1 = new DataColumn("name", String.class);
		// DataColumn col2 = new DataColumn("sex", String.class);
		// DataColumn col3 = new DataColumn("age", Integer.class);
		// table.Columns.Add(col);
		// table.Columns.Add(col1);
		// table.Columns.Add(col2);
		// table.Columns.Add(col3);
		// DataRow dr1 = table.NewRow();
		// dr1.setValue(col, 1);
		// dr1.setValue(col1, "付强");
		// dr1.setValue(col2, "男");
		// dr1.setValue(col3, 21);
		// DataRow dr2 = table.NewRow();
		// dr2.setValue(col, 2);
		// dr2.setValue(col1, "熊伟");
		// dr2.setValue(col2, "男");
		// dr2.setValue(col3, 21);
		// table.Rows.add(dr1);
		// table.Rows.add(dr2);
		//
		// DataTable dept = new DataTable("dept");
		// DataColumn deptName = new DataColumn("name", String.class);
		// DataColumn deptDesc = new DataColumn("desc", String.class);
		// dept.Columns.Add(deptName);
		// dept.Columns.Add(deptDesc);
		// DataRow row = dept.NewRow();
		// row.setValue(deptName, "java开发部");
		// row.setValue(deptDesc, "开发");
		// dept.Rows.add(row);
		// DataRow row1 = dept.NewRow();
		// row1.setValue(deptName, ".net开发部");
		// row1.setValue(deptDesc, "开发");
		// dept.Rows.add(row1);
		//
		// tableList.add(table);
		// tableList.add(dept);
		// set.setTables(tableList);
		// System.out.println(ConvertDataSetToXml(set));
		
		DataSet set = new DataSet();
		set.readXml("D:/JFlow/JFlow/jflow-web/src/main/webapp/WF/Data/FlowDemo/Flow/01.线性流程/表单数据copy测试案例.xml");
		DataSet set2 = new DataSet();
		set2.readXml("D:/JFlow/JFlow/jflow-web/src/main/webapp/DataUser/XML/RegularExpression.xml");
		System.out.println();

	}

}