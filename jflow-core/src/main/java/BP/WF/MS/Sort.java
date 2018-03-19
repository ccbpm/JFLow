package BP.WF.MS;

import BP.En.EnType;
import BP.En.EntityTree;
import BP.En.Map;
import BP.En.UAC;

/** 
 目录
*/
public class Sort extends EntityTree
{
		

	public final String getSortAbbr()
	{
		return this.GetValStringByKey(SortAttr.Abbr);
	}
	public final void setSortAbbr(String value)
	{
		this.SetValByKey(SortAttr.Abbr, value);
	}
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		if (!BP.Web.WebUser.getNo().equals("admin"))
		{
			uac.IsView = false;
			return uac;
		}
		uac.IsDelete = false;
		uac.IsInsert = false;
		uac.IsUpdate = true;
		return uac;
	}
		///#endregion

		
	/** 
	 Sort
	*/
	public Sort()
	{
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("MS_Sort", "目录");
		map.Java_SetEnType(EnType.Admin);

		map.AddTBStringPK(SortAttr.No, null, "编号", true, true, 4, 4, 4);
		map.AddTBString(SortAttr.Name, null, "名称", true, true, 0, 200, 4);
		map.AddTBString(SortAttr.ParentNo, null, "父编号", true, true, 0, 4, 4);
		map.AddTBString(SortAttr.TreeNo, null, "树编号", true, true, 0, 60, 400);
		map.AddTBInt(SortAttr.IsDir, 0, "是否是目录", true, true);
		map.AddTBString(SortAttr.Abbr, null, "简称", true, true, 0, 60, 400);
		map.AddTBInt(SortAttr.Idx, 0, "序号", true, true);

		this.set_enMap(map);
		return this.get_enMap();
	}
		///#endregion
}