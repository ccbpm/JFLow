package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.en.Map;

/** 
 标签.	 
*/
public class LabNote extends EntityMyPK
{

		///基本属性
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.IsUpdate = true;
		return uac;
	}

	/** 
	 x
	 * @throws Exception 
	*/
	public final int getX() throws Exception
	{
		return this.GetValIntByKey(NodeAttr.X);
	}
	public final void setX(int value) throws Exception
	{
		this.SetValByKey(NodeAttr.X, value);
	}

	/** 
	 y
	 * @throws Exception 
	*/
	public final int getY() throws Exception
	{
		return this.GetValIntByKey(NodeAttr.Y);
	}
	public final void setY(int value) throws Exception
	{
		this.SetValByKey(NodeAttr.Y, value);
	}
	/** 
	 标签的事务编号
	 * @throws Exception 
	*/
	public final String getFK_Flow() throws Exception
	{
		return this.GetValStringByKey(NodeAttr.FK_Flow);
	}
	public final void setFK_Flow(String value) throws Exception
	{
		SetValByKey(NodeAttr.FK_Flow, value);
	}
	public final String getName() throws Exception
	{
		return this.GetValStringByKey(NodeAttr.Name);
	}
	public final void setName(String value) throws Exception
	{
		SetValByKey(NodeAttr.Name, value);
	}

		///


		///构造函数
	/** 
	 标签
	*/
	public LabNote()
	{
	}
	/** 
	 标签
	 
	 @param _oid 标签ID	
	 * @throws Exception 
	*/
	public LabNote(String mypk) throws Exception
	{
		this.setMyPK(mypk);
		this.Retrieve();
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_LabNote", "标签");
		map.IndexField = NodeAttr.FK_Flow;

		map.AddMyPK();

		map.AddTBString(NodeAttr.Name, null, null, true, false, 0, 3000, 10, true);
		map.AddTBString(NodeAttr.FK_Flow, null, "流程", false, true, 0, 10, 10);

		map.AddTBInt(NodeAttr.X, 0, "X坐标", false, false);
		map.AddTBInt(NodeAttr.Y, 0, "Y坐标", false, false);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///

	@Override
	protected boolean beforeInsert() throws Exception
	{
		this.setMyPK(String.valueOf(DBAccess.GenerOID()));
		return super.beforeInsert();
	}
}