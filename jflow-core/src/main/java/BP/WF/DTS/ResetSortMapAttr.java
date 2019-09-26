package BP.WF.DTS;

import BP.DA.*;
import BP.Web.Controls.*;
import BP.Port.*;
import BP.En.*;
import BP.Sys.*;
import BP.WF.*;

/** 
 根据坐标排序字段
*/
public class ResetSortMapAttr extends Method
{
	/** 
	 根据坐标排序字段
	*/
	public ResetSortMapAttr()
	{
		this.Title = "根据坐标排序MapAttr字段-用于手机端";
		this.Help = "重置MapAttr表中Idx字段，根据坐标y,x排序";
	}
	/** 
	 设置执行变量
	 
	 @return 
	*/
	@Override
	public void Init()
	{
	}
	/** 
	 当前的操纵员是否可以执行这个方法
	*/
	@Override
	public boolean getIsCanDo()
	{
		return true;
	}
	/** 
	 执行
	 
	 @return 返回执行结果
	 * @throws Exception 
	*/
	@Override
	public Object Do() throws Exception
	{
		try
		{
			String sql = "select NO from Sys_MapData where No not in(select No from Sys_MapDtl) and No not like '%Rpt'";
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			if (dt != null)
			{
				for (DataRow row : dt.Rows)
				{
					MapAttrs attrs = new MapAttrs();
					QueryObject qo = new QueryObject(attrs);
					qo.AddWhere(MapAttrAttr.FK_MapData, row.get("NO").toString());
					qo.addAnd();
					qo.AddWhere(MapAttrAttr.UIVisible, true);
					qo.addOrderBy(MapAttrAttr.Y, MapAttrAttr.X);
					qo.DoQuery();
					int rowIdx = 0;
					for (MapAttr mapAttr : attrs.ToJavaList())
					{
						mapAttr.setIdx(rowIdx);
						mapAttr.DirectUpdate();
						rowIdx++;
					}
				}
			}
			return "执行成功...";
		}
		catch (RuntimeException ex)
		{
		}
		return "执行失败...";
	}
}