package BP.En;

import BP.XML.XmlEnNoName;
import BP.XML.XmlEns;

/**
 * 取值
 */
public class FrmPopVal extends XmlEnNoName
{
	// /#region 属性
	public final String getAtPara()
	{
		return this.GetValStringByKey(FrmPopValAttr.AtPara);
	}
	
	public final String getTag1()
	{
		return this.GetValStringByKey(FrmPopValAttr.Tag1);
	}
	
	public final String getTag2()
	{
		return this.GetValStringByKey(FrmPopValAttr.Tag2);
	}
	
	public final String getH()
	{
		return this.GetValStringByKey(FrmPopValAttr.H);
	}
	
	public final String getW()
	{
		return this.GetValStringByKey(FrmPopValAttr.W);
	}
	
	// 构造
	/**
	 * 取值
	 */
	public FrmPopVal()
	{
		
	}
	
	/**
	 * 取值
	 * 
	 * @param no
	 *            编号
	 */
	public FrmPopVal(String no)
	{
		this.RetrieveByPK(FrmPopValAttr.No, no);
	}
	
	/**
	 * 获取一个实例
	 */
	@Override
	public XmlEns getGetNewEntities()
	{
		return new FrmPopVals();
	}
}