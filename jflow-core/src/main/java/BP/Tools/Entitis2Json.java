package BP.Tools;

import BP.En.Attr;
import BP.En.Attrs;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.EntityTree;
import BP.En.EntityTreeAttr;

public class Entitis2Json
{
	private volatile static Entitis2Json _instance = null;
	
	private Entitis2Json()
	{
	}
	
	public static Entitis2Json getInstance()
	{
		if (_instance == null)
		{
			_instance = new Entitis2Json();
		}
		return _instance;
	}
	
	/**
	 * 将实体类转为json List格式数据
	 * 
	 * @param ens
	 *            实体集合类
	 * @return
	 */
	public static String ConvertEntities2ListJson(Entities ens)
	{
		return getInstance().TranslateEntitiesToListJson(ens, null);
	}
	
	/**
	 * 将实体类转为json List格式数据
	 * 
	 * @param ens
	 *            实体集合类
	 * @param hidenKeys
	 *            需要隐藏的列，如：@No@Name
	 * @return
	 */
	public static String ConvertEntities2ListJson(Entities ens, String hidenKeys)
	{
		return getInstance().TranslateEntitiesToListJson(ens, hidenKeys);
	}
	
	/**
	 * 将Entitis转换为树形的json
	 * 
	 * @param ens
	 *            实体集合类
	 * @param rootNo
	 *            根节点编号
	 * @return
	 */
	public static String ConvertEntitis2GenerTree(Entities ens, String rootNo)
	{
		return getInstance().TansEntitiesToGenerTree(ens, rootNo);
	}
	
	/**
	 * 将实体类转为json格式数据
	 * 
	 * @param ens
	 *            实体集合类
	 * @return
	 */
	public static String ConvertEntitis2GridJsonOnlyData(Entities ens)
	{
		return getInstance().TranslateEntitiesToGridJsonOnlyData(ens, 0, null);
	}
	
	/**
	 * 将实体类转为json格式数据用于分页
	 * 
	 * @param ens
	 *            实体集合类
	 * @param totalRows
	 *            总行数
	 * @return
	 */
	public static String ConvertEntitis2GridJsonOnlyData(Entities ens,
			int totalRows)
	{
		return getInstance().TranslateEntitiesToGridJsonOnlyData(ens,
				totalRows, null);
	}
	
	/**
	 * 将实体类转为json格式数据
	 * 
	 * @param ens
	 *            实体集合类
	 * @param hidenKeys
	 *            需要隐藏的列，如：@No@Name
	 * @return
	 */
	public static String ConvertEntitis2GridJsonOnlyData(Entities ens,
			String hidenKeys)
	{
		return getInstance().TranslateEntitiesToGridJsonOnlyData(ens, 0,
				hidenKeys);
	}
	
	/**
	 * 将实体类转为json格式数据用于分页
	 * 
	 * @param ens
	 *            实体集合类
	 * @param totalRows
	 *            总行数
	 * @param hidenKeys
	 *            需要隐藏的列，如：@No@Name
	 * @return
	 */
	public static String ConvertEntitis2GridJsonOnlyData(Entities ens,
			int totalRows, String hidenKeys)
	{
		return getInstance().TranslateEntitiesToGridJsonOnlyData(ens,
				totalRows, hidenKeys);
	}
	
	/**
	 * 将实体集合类转为json格式 包含列名和数据
	 * 
	 * @param ens
	 *            实体集合类
	 * @return Json格式数据
	 */
	public static String ConvertEntitis2GridJsonAndData(Entities ens)
	{
		return getInstance()
				.TranslateEntitiesToGridJsonColAndData(ens, 0, null);
	}
	
	/**
	 * 将实体集合类转为json格式 包含列名和数据
	 * 
	 * @param ens
	 *            实体集合类
	 * @param totalRows
	 *            总行数
	 * @return Json格式数据
	 */
	public static String ConvertEntitis2GridJsonAndData(Entities ens,
			int totalRows)
	{
		return getInstance().TranslateEntitiesToGridJsonColAndData(ens,
				totalRows, null);
	}
	
	/**
	 * 将实体集合类转为json格式 包含列名和数据
	 * 
	 * @param ens
	 *            实体集合类
	 * @param hidenKeys
	 *            需要隐藏的列，如：@No@Name
	 * @return
	 */
	public static String ConvertEntitis2GridJsonAndData(Entities ens,
			String hidenKeys)
	{
		return getInstance().TranslateEntitiesToGridJsonColAndData(ens, 0,
				hidenKeys);
	}
	
	/**
	 * 将实体集合类转为json格式 包含列名和数据
	 * 
	 * @param ens
	 *            实体集合类
	 * @param totalRows
	 *            总行数
	 * @param hidenKeys
	 *            需要隐藏的列，如：@No@Name
	 * @return
	 */
	public static String ConvertEntitis2GridJsonAndData(Entities ens,
			int totalRows, String hidenKeys)
	{
		return getInstance().TranslateEntitiesToGridJsonColAndData(ens,
				totalRows, hidenKeys);
	}
	
	/**
	 * 将实体类转为json格式List
	 * 
	 * @param ens
	 * @param hidenKeys
	 *            隐藏字段
	 * @return
	 */
	public final String TranslateEntitiesToListJson(BP.En.Entities ens,
			String hidenKeys)
	{
		Attrs attrs = ens.getNewEntity().getEnMap().getAttrs();
		StringBuilder append = new StringBuilder();
		append.append("[");
		
		for (Object en : ens)
		{
			append.append("{");
			for (Attr attr : attrs)
			{
				if (!StringHelper.isNullOrEmpty(hidenKeys)
						&& hidenKeys.contains("@" + attr.getKey()))
				{
					continue;
				}
				
				String strValue = ((Entity) en).GetValStrByKey(attr.getKey());
				if (!StringHelper.isNullOrEmpty(strValue)
						&& strValue.lastIndexOf("\\") > -1)
				{
					strValue = strValue
							.substring(0, strValue.lastIndexOf("\\"));
				}
				append.append(attr.getKey() + ":'" + strValue + "',");
			}
			append = append.deleteCharAt(append.length() - 1);
			append.append("},");
		}
		if (append.length() > 1)
		{
			append = append.deleteCharAt(append.length() - 1);
		}
		append.append("]");
		return ReplaceIllgalChart(append.toString());
	}
	
	/**
	 * 将实体类转为json格式
	 * 
	 * @param ens
	 * @param hidenKeys
	 * @return
	 */
	public final String TranslateEntitiesToGridJsonOnlyData(BP.En.Entities ens,
			int totalRows, String hidenKeys)
	{
		Attrs attrs = ens.getNewEntity().getEnMap().getAttrs();
		StringBuilder append = new StringBuilder();
		append.append("{rows:[");
		
		for (Object en : ens)
		{
			append.append("{");
			for (Attr attr : attrs)
			{
				if (!StringHelper.isNullOrEmpty(hidenKeys)
						&& hidenKeys.contains("@" + attr.getKey()))
				{
					continue;
				}
				
				String strValue = ((Entity) en).GetValStrByKey(attr.getKey());
				if (!StringHelper.isNullOrEmpty(strValue)
						&& strValue.lastIndexOf("\\") > -1)
				{
					strValue = strValue
							.substring(0, strValue.lastIndexOf("\\"));
				}
				append.append(attr.getKey() + ":'" + strValue + "',");
			}
			append = append.deleteCharAt(append.length() - 1);
			append.append("},");
		}
		// 长度超过{rows:[才进行截取
		if (append.length() > 7)
		{
			append = append.deleteCharAt(append.length() - 1);
		}
		
		if (totalRows == 0)
		{
			append.append("],total:");
			append.append(ens != null ? ens.size() : 0);
		} else
		{
			append.append("],total:" + totalRows);
		}
		append.append("}");
		return ReplaceIllgalChart(append.toString());
	}
	
	/**
	 * 将实体类转为json格式 包含列名和数据
	 * 
	 * @param ens
	 * @param hidenKeys
	 * @return
	 */
	public final String TranslateEntitiesToGridJsonColAndData(Entities ens,
			int totalRows, String hidenKeys)
	{
		Attrs attrs = ens.getNewEntity().getEnMap().getAttrs();
		StringBuilder append = new StringBuilder();
		append.append("{");
		// 整理列名
		append.append("columns:[");
		for (Attr attr : attrs)
		{
			if (!attr.getUIVisible())
			{
				continue;
			}
			if (!StringHelper.isNullOrEmpty(hidenKeys)
					&& hidenKeys.contains("@" + attr.getKey()))
			{
				continue;
			}
			if (attr.getIsRefAttr() || attr.getIsFK() || attr.getIsEnum())
			{
				append.append("{");
				append.append(String.format(
						"field:'%1$s',title:'%2$s',width:%3$s,sortable:true",
						attr.getKey() + "Text", attr.getDesc(),
						attr.getUIWidth() * 2));
				append.append("},");
				continue;
			}
			append.append("{");
			append.append(String.format(
					"field:'%1$s',title:'%2$s',width:%3$s,sortable:true",
					attr.getKey(), attr.getDesc(), attr.getUIWidth() * 2));
			append.append("},");
		}
		if (append.length() > 10)
		{
			append = append.deleteCharAt(append.length() - 1);
		}
		append.append("]");
		
		// 整理数据
		boolean bHaveData = false;
		append.append(",data:{rows:[");
		for (Object en : ens)
		{
			bHaveData = true;
			append.append("{");
			for (Attr attr : attrs)
			{
				if (attr.getIsRefAttr() || attr.getIsFK() || attr.getIsEnum())
				{
					append.append(attr.getKey() + "Text:'"
							+ ((Entity) en).GetValRefTextByKey(attr.getKey())
							+ "',");
					continue;
				}
				append.append(attr.getKey() + ":'"
						+ ((Entity) en).GetValStrByKey(attr.getKey()) + "',");
			}
			append = append.deleteCharAt(append.length() - 1);
			append.append("},");
		}
		if (append.length() > 11 && bHaveData)
		{
			append = append.deleteCharAt(append.length() - 1);
		}
		
		append.append("],total:" + totalRows + "}");
		append.append("}");
		
		return ReplaceIllgalChart(append.toString());
	}
	
	/**
	 * 将实体转为树形
	 * 
	 * @param ens
	 * @param rootNo
	 */
	private StringBuilder appendMenus = new StringBuilder();
	private StringBuilder appendMenuSb = new StringBuilder();
	
	public final String TansEntitiesToGenerTree(Entities ens, String rootNo)
	{
		appendMenus = new StringBuilder();
		appendMenuSb = new StringBuilder();

		Entity tempVar = ens.GetEntityByKey(EntityTreeAttr.ParentNo, rootNo);
		EntityTree root = (EntityTree) ((tempVar instanceof EntityTree) ? tempVar
				: null);
		if (root == null)
		{
			throw new RuntimeException("@没有找到rootNo=" + rootNo + "的entity.");
		}
		appendMenus.append("[{");
		appendMenus.append("'id':'" + root.getNo() + "'");
		appendMenus.append(",'text':'" + root.getName() + "'");
		
		// 增加它的子级.
		appendMenus.append(",'children':");
		AddChildren(root, ens);
		appendMenus.append(appendMenuSb);
		appendMenus.append("}]");
		
		return ReplaceIllgalChart(appendMenus.toString());
	}
	
	public final void AddChildren(EntityTree parentEn, Entities ens)
	{
		appendMenus.append(appendMenuSb);
		appendMenuSb.setLength(0);
		
		appendMenuSb.append("[");
		for (Object item : ens)
		{
			if (!parentEn.getNo().equals(((EntityTree) item).getParentNo()))
			{
				continue;
			}
			
			appendMenuSb.append("{'id':'" + ((EntityTree) item).getNo()
					+ "','text':'" + ((EntityTree) item).getName()
					+ "','state':'closed'");
			EntityTree treeNode = (EntityTree) ((item instanceof EntityTree) ? item
					: null);
			// 增加它的子级.
			appendMenuSb.append(",'children':");
			AddChildren(((EntityTree) item), ens);
			appendMenuSb.append("},");
		}
		if (appendMenuSb.length() > 1)
		{
			appendMenuSb = appendMenuSb.deleteCharAt(appendMenuSb.length() - 1);
		}
		appendMenuSb.append("]");
		appendMenus.append(appendMenuSb);
		appendMenuSb.setLength(0);
	}
	
	/**
	 * 去除特殊字符
	 * 
	 * @param s
	 * @return
	 */
	public final String ReplaceIllgalChart(String s)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0, j = s.length(); i < j; i++)
		{
			
			char c = s.charAt(i);
			switch (c)
			{
				case '\"':
					sb.append("\\\"");
					break;
				case '\\':
					sb.append("\\\\");
					break;
				case '/':
					sb.append("\\/");
					break;
				case '\b':
					sb.append("\\b");
					break;
				case '\f':
					sb.append("\\f");
					break;
				case '\n':
					sb.append("\\n");
					break;
				case '\r':
					sb.append("\\r");
					break;
				case '\t':
					sb.append("\\t");
					break;
				default:
					sb.append(c);
					break;
			}
		}
		return sb.toString();
	}
}