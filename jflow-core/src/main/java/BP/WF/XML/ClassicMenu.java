package BP.WF.XML;

import BP.XML.XmlEn;
import BP.XML.XmlEns;

/** 
 经典模式左侧菜单
 
*/
public class ClassicMenu extends XmlEn
{

		
	public final String getNo()
	{
		return this.GetValStringByKey("No");
	}
	public final String getName()
	{

//		switch (this.No)
		if (this.getNo().equals("EmpWorks"))
		{
				return this.GetValStringByKey(BP.Web.WebUser.getSysLang())+"("+BP.WF.Dev2Interface.getTodolist_EmpWorks()+")";
		}
		else if (this.getNo().equals("Sharing"))
		{
				return this.GetValStringByKey(BP.Web.WebUser.getSysLang()) + "(" + BP.WF.Dev2Interface.getTodolist_Sharing() + ")";
		}
		else if (this.getNo().equals("CC"))
		{
				return this.GetValStringByKey(BP.Web.WebUser.getSysLang()) + "(" + BP.WF.Dev2Interface.getTodolist_CCWorks() + ")";
		}
		else if (this.getNo().equals("Draft"))
		{
				return this.GetValStringByKey(BP.Web.WebUser.getSysLang()) + "(" + BP.WF.Dev2Interface.getTodolist_Draft() + ")";
		}
		else if (this.getNo().equals("Runing"))
		{
				return this.GetValStringByKey(BP.Web.WebUser.getSysLang()) + "(" + BP.WF.Dev2Interface.getTodolist_Runing() + ")";
		}
		else
		{
				return this.GetValStringByKey(BP.Web.WebUser.getSysLang());
		}
	}
	/** 
	 图片
	*/
	public final String getImg()
	{
		return this.GetValStringByKey("Img");
	}
	public final String getTitle()
	{
		return this.GetValStringByKey("Title");
	}
	public final String getUrl()
	{
		return this.GetValStringByKey("Url");
	}
	public final boolean getEnable()
	{
		return this.GetValBoolByKey("Enable");
	}
	/** 
	 节点扩展信息
	*/
	public ClassicMenu()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new ClassicMenus();
	}
}