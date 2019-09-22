package BP.WF.DTS;

import BP.DA.*;
import BP.Web.Controls.*;
import BP.Port.*;
import BP.En.*;
import BP.Sys.*;
import BP.WF.*;

/** 
 扩充Doc字段的长度 的摘要说明
*/
public class DocFieldAddLengthTo1000 extends Method
{
	/** 
	 不带有参数的方法
	*/
	public DocFieldAddLengthTo1000()
	{
		this.Title = "扩充Doc字段的长度";
		this.Help = "为doc类型的字段扩充长度，低于1000的字符扩充为1000.";
		this.Help += "<br>减少因为实施的原因忽略了字符长度导致的界面报错。";
		this.GroupName = "系统维护";

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
		if (BP.Web.WebUser.No.equals("admin"))
		{
			return true;
		}
		return false;
	}
	/** 
	 执行
	 
	 @return 返回执行结果
	*/
	@Override
	public Object Do()
	{
		String strs = "开始执行....";
		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.MyDataType, DataType.AppString, MapAttrAttr.FK_MapData);
		strs += "<br>@如下字段受到了影响。";
		for (MapAttr attr : attrs)
		{
			if (attr.UIHeightInt > 50 && attr.MaxLen < 1000)
			{
				strs += " @ 类:" + attr.FK_MapData + " 字段:" + attr.KeyOfEn + " , " + attr.Name + " ";
				attr.MaxLen = 1000;
				attr.Update();
			}
		}
		return "执行成功..." + strs;
	}
}