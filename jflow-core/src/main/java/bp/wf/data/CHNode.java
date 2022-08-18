package bp.wf.data;

import bp.en.*;
import bp.en.Map;


/** 
 节点时限
*/
public class CHNode extends EntityMyPK
{

		///#region 基本属性
	/** 
	 工作ID
	*/
	public final long getWorkID()  throws Exception
	{
		return this.GetValInt64ByKey(CHAttr.WorkID);
	}
	public final void setWorkID(long value) throws Exception
	{
		this.SetValByKey(CHAttr.WorkID, value);
	}



	/** 
	 节点ID
	*/
	public final int getFK_Node()  throws Exception
	{
		return this.GetValIntByKey(CHAttr.FK_Node);
	}
	public final void setFK_Node(int value) throws Exception
	{
		this.SetValByKey(CHAttr.FK_Node, value);
	}
	/** 
	 节点名称
	*/
	public final String getNodeName()  throws Exception
	{
		return this.GetValStrByKey(CHNodeAttr.NodeName);
	}
	public final void setNodeName(String value) throws Exception
	{
		this.SetValByKey(CHNodeAttr.NodeName, value);
	}

	/** 
	 操作人员
	*/
	public final String getFK_Emp()  throws Exception
	{
		return this.GetValStringByKey(CHAttr.FK_Emp);
	}
	public final void setFK_Emp(String value) throws Exception
	{
		this.SetValByKey(CHAttr.FK_Emp, value);
	}
	/** 
	 人员
	*/
	public final String getFK_EmpT()  throws Exception
	{
		return this.GetValStringByKey(CHAttr.FK_EmpT);
	}
	public final void setFK_EmpT(String value) throws Exception
	{
		this.SetValByKey(CHAttr.FK_EmpT, value);
	}

	/** 
	 计划开始时间
	*/
	public final String getStartDT()  throws Exception
	{
		return this.GetValStringByKey(CHNodeAttr.StartDT);
	}
	public final void setStartDT(String value) throws Exception
	{
		this.SetValByKey(CHNodeAttr.StartDT, value);
	}
	/** 
	 计划完成时间
	*/
	public final String getEndDT()  throws Exception
	{
		return this.GetValStringByKey(CHNodeAttr.EndDT);
	}
	public final void setEndDT(String value) throws Exception
	{
		this.SetValByKey(CHNodeAttr.EndDT, value);
	}

	/** 
	 工天
	*/
	public final int getGT()  throws Exception
	{
		return this.GetValIntByKey(CHNodeAttr.GT);
	}
	public final void setGT(int value) throws Exception
	{
		this.SetValByKey(CHNodeAttr.GT, value);
	}
	/** 
	 阶段占比
	*/
	public final float getScale()  throws Exception
	{
		return this.GetValFloatByKey(CHNodeAttr.Scale);
	}
	public final void setScale(float value) throws Exception
	{
		this.SetValByKey(CHNodeAttr.Scale, value);
	}

	/** 
	 总体进度
	*/
	public final float getTotalScale()  throws Exception
	{
		return this.GetValFloatByKey(CHNodeAttr.TotalScale);
	}
	public final void setTotalScale(float value) throws Exception
	{
		this.SetValByKey(CHNodeAttr.TotalScale, value);
	}
	/** 
	 产值
	*/
	public final float getChanZhi()  throws Exception
	{
		return this.GetValFloatByKey(CHNodeAttr.ChanZhi);
	}
	public final void setChanZhi(float value) throws Exception
	{
		this.SetValByKey(CHNodeAttr.ChanZhi, value);
	}


		///#endregion


		///#region 构造方法
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.IsDelete = false;
		uac.IsInsert = false;
		uac.IsUpdate=false;
		uac.IsView = true;
		return uac;
	}
	/** 
	 节点时限
	*/
	public CHNode()
	{
	}
	/** 
	 
	 
	 param pk
	*/
	public CHNode(String pk) throws Exception 
	{
		super(pk);
	}


		///#endregion


		///#region Map
	/** 
	 EnMap
	*/
	@Override
	public bp.en.Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_CHNode", "节点时限");

		map.AddTBInt(CHNodeAttr.WorkID, 0, "WorkID", true, true);
		map.AddTBInt(CHNodeAttr.FK_Node, 0, "节点", true, true);
		map.AddTBString(CHNodeAttr.NodeName, null, "节点名称", true, true, 0, 50, 5);

		map.AddTBString(CHAttr.FK_Emp, null, "处理人", true, true, 0, 30, 3);
		map.AddTBString(CHAttr.FK_EmpT, null, "处理人名称", true, true, 0, 200, 5);

		map.AddTBString(CHNodeAttr.StartDT, null, "计划开始时间", true, true, 0, 50, 5);
		map.AddTBString(CHNodeAttr.EndDT, null, "计划结束时间", true, true, 0, 50, 5);
		map.AddTBInt(CHNodeAttr.GT, 0, "工天", true, true);
		map.AddTBFloat(CHNodeAttr.Scale, 0, "阶段占比", true, true);
		map.AddTBFloat(CHNodeAttr.TotalScale, 0, "总进度", true, true);
		map.AddTBFloat(CHNodeAttr.ChanZhi, 0, "产值", true, true);

		map.AddTBAtParas(500);

		map.AddTBStringPK(CHNodeAttr.MyPK, null, "MyPK", false, false, 0, 50, 5);


		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		this.setMyPK(this.getWorkID() + "_" + this.getFK_Node());
		return super.beforeUpdateInsertAction();
	}

}