package bp.gpm;

import bp.da.*;
import bp.en.*;
import bp.en.Map;

/** 
 人员信息块
*/
public class BarEmp extends EntityMyPK
{
	private static final long serialVersionUID = 1L;
		///属性
	public final int getIdx()throws Exception
	{
		return this.GetValIntByKey(BarEmpAttr.Idx);
	}
	public final void setIdx(int value) throws Exception
	{
		this.SetValByKey(BarEmpAttr.Idx, value);
	}
	public final String getFK_Bar()throws Exception
	{
		return this.GetValStringByKey(BarEmpAttr.FK_Bar);
	}
	public final void setFK_Bar(String value) throws Exception
	{
		this.SetValByKey(BarEmpAttr.FK_Bar, value);
	}
	public final String getFK_Emp()throws Exception
	{
		return this.GetValStringByKey(BarEmpAttr.FK_Emp);
	}
	public final void setFK_Emp(String value) throws Exception
	{
		this.SetValByKey(BarEmpAttr.FK_Emp, value);
	}
	public final boolean getIsShow()throws Exception
	{
		return this.GetValBooleanByKey(BarEmpAttr.IsShow);
	}
	public final void setIsShow(boolean value) throws Exception
	{
		this.SetValByKey(BarEmpAttr.IsShow, value);
	}

	public final String getTitle()throws Exception
	{
		return this.GetValStringByKey(BarEmpAttr.Title);
	}
	public final void setTitle(String value) throws Exception
	{
		this.SetValByKey(BarEmpAttr.Title, value);
	}


		///


		///构造方法
	/** 
	 人员信息块
	*/
	public BarEmp()
	{
	}
	/** 
	 人员信息块
	 
	 @param no
	 * @throws Exception 
	*/
	public BarEmp(String no) throws Exception
	{
		this.Retrieve();
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("GPM_BarEmp");
		map.setDepositaryOfEntity( Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		map.setEnDesc("人员信息块");
		map.setEnType( EnType.Sys);

		map.AddMyPK(); // 主键是由:  FK_Bar+"_"+FK_Emp 组成的，它是一个复合主键.
		map.AddTBString(BarEmpAttr.FK_Bar, null, "信息块编号", true, false, 0, 90, 20);
		map.AddTBString(BarEmpAttr.FK_Emp, null, "人员编号", true, false, 0, 90, 20);
		map.AddTBString(BarEmpAttr.Title, null, "标题", true, false, 0, 3900, 20);
		map.AddTBInt(BarEmpAttr.IsShow, 1, "是否显示", false, true);
		map.AddTBInt(BarEmpAttr.Idx, 0, "显示顺序", false, true);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///


		///显示与隐藏.
	public final void DoUp()throws Exception
	{
		this.DoOrderUp(BarEmpAttr.FK_Bar, this.getFK_Bar(), BarEmpAttr.Idx);
	}
	public final void DoDown()throws Exception
	{
		this.DoOrderDown(BarEmpAttr.FK_Bar, this.getFK_Bar(), BarEmpAttr.Idx);
	}
	public final void DoHidShow() throws Exception
	{
		this.setIsShow(this.getIsShow());
		this.Update();
	}




		/// 显示与隐藏.
}