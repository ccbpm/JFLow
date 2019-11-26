package BP.Sys;

import java.util.List;

import BP.DA.*;
import BP.Difference.SystemConfig;
import BP.En.*;
import BP.Sys.XML.*;

/** 
 多音字s
*/
public class ChMulToneXmls extends XmlEns
{

		///#region 构造
	/** 
	 多音字s
	*/
	public ChMulToneXmls()
	{
	}
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<ChMulToneXml> ToJavaList()
	{
		return (List<ChMulToneXml>)(Object)this;
	}

		///#endregion


		///#region 重写基类属性或方法。
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getNewEntity()
	{
		return new ChMulToneXml();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfData() + "\\XML\\XmlDB.xml";
	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()
	{
		return "PinYin";
	}
	@Override
	public Entities getRefEns()
	{
		return null;
	}

		///#endregion
}