package BP.WF;

import BP.DA.DataType;
import BP.En.EntityMyPK;
import BP.En.Map;

/** 
 退回轨迹
*/
public class ReturnWork extends EntityMyPK
{
	/** 
	 工作ID
	*/
	public final long getWorkID()
	{
		return this.GetValInt64ByKey(ReturnWorkAttr.WorkID);
	}
	public final void setWorkID(long value)
	{
		SetValByKey(ReturnWorkAttr.WorkID, value);
	}
	/** 
	 退回到节点
	*/
	public final int getReturnToNode()
	{
		return this.GetValIntByKey(ReturnWorkAttr.ReturnToNode);
	}
	public final void setReturnToNode(int value)
	{
		SetValByKey(ReturnWorkAttr.ReturnToNode, value);
	}
	/** 
	 退回节点
	*/
	public final int getReturnNode()
	{
		return this.GetValIntByKey(ReturnWorkAttr.ReturnNode);
	}
	public final void setReturnNode(int value)
	{
		SetValByKey(ReturnWorkAttr.ReturnNode, value);
	}
	public final String getReturnNodeName()
	{
		return this.GetValStrByKey(ReturnWorkAttr.ReturnNodeName);
	}
	public final void setReturnNodeName(String value)
	{
		SetValByKey(ReturnWorkAttr.ReturnNodeName, value);
	}
	/** 
	 退回人
	*/
	public final String getReturner()
	{
		return this.GetValStringByKey(ReturnWorkAttr.Returner);
	}
	public final void setReturner(String value)
	{
		SetValByKey(ReturnWorkAttr.Returner, value);
	}
	public final String getReturnerName()
	{
		return this.GetValStringByKey(ReturnWorkAttr.ReturnerName);
	}
	public final void setReturnerName(String value)
	{
		SetValByKey(ReturnWorkAttr.ReturnerName, value);
	}
	/** 
	 退回给
	*/
	public final String getReturnToEmp()
	{
		return this.GetValStringByKey(ReturnWorkAttr.ReturnToEmp);
	}
	public final void setReturnToEmp(String value)
	{
		SetValByKey(ReturnWorkAttr.ReturnToEmp, value);
	}
	public final String getBeiZhu()
	{
		return this.GetValStringByKey(ReturnWorkAttr.Note);
	}
	public final void setBeiZhu(String value)
	{
		SetValByKey(ReturnWorkAttr.Note, value);
	}
	public final String getBeiZhuHtml()
	{
		return this.GetValHtmlStringByKey(ReturnWorkAttr.Note);
	}
	/** 
	 记录日期
	*/
	public final String getRDT()
	{
		return this.GetValStringByKey(ReturnWorkAttr.RDT);
	}
	public final void setRDT(String value)
	{
		SetValByKey(ReturnWorkAttr.RDT, value);
	}
	/** 
	 是否要原路返回？
	*/
	public final boolean getIsBackTracking()
	{
		return this.GetValBooleanByKey(ReturnWorkAttr.IsBackTracking);
	}
	public final void setIsBackTracking(boolean value)
	{
		SetValByKey(ReturnWorkAttr.IsBackTracking, value);
	}
		
	/** 
	 退回轨迹
	*/
	public ReturnWork()
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

		Map map = new Map("WF_ReturnWork", "退回轨迹");

		map.AddMyPK();

		map.AddTBInt(ReturnWorkAttr.WorkID, 0, "WorkID", true, true);

		map.AddTBInt(ReturnWorkAttr.ReturnNode, 0, "退回节点", true, true);
		map.AddTBString(ReturnWorkAttr.ReturnNodeName, null, "退回节点名称", true, true, 0, 200, 10);

		map.AddTBString(ReturnWorkAttr.Returner, null, "退回人", true, true, 0, 20, 10);
		map.AddTBString(ReturnWorkAttr.ReturnerName, null, "退回人名称", true, true, 0, 200, 10);

		map.AddTBInt(ReturnWorkAttr.ReturnToNode, 0, "ReturnToNode", true, true);
		map.AddTBString(ReturnWorkAttr.ReturnToEmp, null, "退回给", true, true, 0, 4000, 10);

		map.AddTBString(ReturnWorkAttr.Note, "", "退回原因", true, true, 0, 4000, 10);
		map.AddTBDateTime(ReturnWorkAttr.RDT, null, "退回日期", true, true);

		map.AddTBInt(ReturnWorkAttr.IsBackTracking, 0, "是否要原路返回?", true, true);
		this.set_enMap(map);
		return this.get_enMap();
	}
	@Override
	protected boolean beforeInsert()
	{
		this.setReturner(BP.Web.WebUser.getNo());
		this.setReturnerName(BP.Web.WebUser.getName());

		this.setRDT(DataType.getCurrentDataTime());
		return super.beforeInsert();
	}
}