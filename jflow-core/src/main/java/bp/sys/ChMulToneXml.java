package bp.sys;

import bp.sys.xml.*;

import java.util.List;


/** 
 多音字
*/
public class ChMulToneXml extends XmlEn
{

		///#region 属性
	public final String getNo()  throws Exception
	{
		return this.GetValStringByKey("No");
	}
	public final String getName()  throws Exception
	{
		return this.GetValStringByKey("Name");
	}
	public final String getDesc()  throws Exception
	{
		return this.GetValStringByKey("No");
	}

		///#endregion


		///#region 构造
	/** 
	 节点扩展信息
	*/
	public ChMulToneXml()
	{
	}
	/** 
	 获取一个实例s
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new ChMulToneXmls();
	}


}