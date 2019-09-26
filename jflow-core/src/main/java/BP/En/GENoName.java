package BP.En;

import BP.DA.*;
import BP.En.*;
import java.util.*;

/** 
 
*/
public class GENoName extends EntityNoName
{

		///#region 构造
	@Override
	public String toString()
	{
		return this.PhysicsTable;
	}
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	public GENoName()
	{

	}
	/** 
	 编号
	 
	 @param no 编号
	 * @throws Exception 
	*/
	public GENoName(String no) throws Exception
	{
		super(no);

	}
	public GENoName(String sftable, String tableDesc)
	{
		this.PhysicsTable = sftable;
		this.Desc = tableDesc;
	}
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map(this.PhysicsTable, this.Desc);
		map.setIsAutoGenerNo(true);

		map.Java_SetDepositaryOfEntity(Depositary.Application);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.App);
		map.setIsAutoGenerNo(true);

		map.AddTBStringPK(GENoNameAttr.No, null, "编号", true, true, 1, 30, 3);
		map.AddTBString(GENoNameAttr.Name, null, "名称", true, false, 1, 60, 500);
			//return map;
		this.set_enMap(map);
		return this.get_enMap();
	}
	public String PhysicsTable = null;
	public String Desc = null;


		///#endregion
}