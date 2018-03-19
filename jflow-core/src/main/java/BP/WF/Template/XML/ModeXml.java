package BP.WF.Template.XML;

import BP.XML.XmlEnNoName;
import BP.XML.XmlEns;

/** 
 模式
 
*/
public class ModeXml extends XmlEnNoName
{

		
	/** 
	 设置描述
	 
	*/
	public final String getSetDesc()
	{
		return this.GetValStringByKey("SetDesc");
	}
	/** 
	 类别
	 
	*/
	public final String getFK_ModeSort()
	{
		return this.GetValStringByKey("FK_ModeSort");
	}
	/** 
	 类别
	 
	*/
	public final String getNote()
	{
		return this.GetValStringByKey("Note");
	}
	public final String getParaType()
	{
		return this.GetValStringByKey("ParaType");
	}

		///#endregion


		
	/** 
	 模式
	 
	*/
	public ModeXml()
	{
	}
	/** 
	 模式
	 
	*/
	public ModeXml(String no)
	{
		super(no);
	}
	/** 
	 获取一个实例
	 
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new ModeXmls();
	}

		///#endregion
}