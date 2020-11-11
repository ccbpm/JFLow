package bp.wf.data;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.web.*;

/** 
 常用语
*/
public class FastInput extends EntityMyPK
{
	private static final long serialVersionUID = 1L;
	///基本属性
	/** 
	 字段
	*/
	public final String getContrastKey()throws Exception
	{
		return this.GetValStringByKey(FastInputAttr.ContrastKey);
	}
	public final void setContrastKey(String value) throws Exception
	{
		this.SetValByKey(FastInputAttr.ContrastKey, value);
	}
	/** 
	 人员
	*/
	public final String getFK_Emp()throws Exception
	{
		return this.GetValStringByKey(FastInputAttr.FK_Emp);
	}
	public final void setFK_Emp(String value) throws Exception
	{
		this.SetValByKey(FastInputAttr.FK_Emp, value);
	}
	public final String getVals()throws Exception
	{
		return this.GetValStringByKey(FastInputAttr.Vals);
	}
	public final void setVals(String value) throws Exception
	{
		this.SetValByKey(FastInputAttr.Vals, value);
	}

		///


		///构造方法
	/** 
	 常用语
	*/
	public FastInput()
	{
	}
	/** 
	 常用语
	 
	 @param no
	 * @throws Exception 
	*/
	public FastInput(String mypk) throws Exception
	{
		super(mypk);
	}
	/** 
	 更新前做的事情
	 
	 @return 
	 * @throws Exception 
	*/

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		if (this.getMyPK().equals(""))
		{
			this.setMyPK(DBAccess.GenerGUID());
		}

		return super.beforeUpdateInsertAction();
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

		Map map = new Map("Sys_UserRegedit", "常用语");

		map.AddMyPK();

			//该表单对应的表单ID
		map.AddTBString(FastInputAttr.ContrastKey, null, "类型CYY", true, false, 0, 20, 20);
		map.AddTBString(FastInputAttr.FK_Emp, null, "人员编号", true, false, 0, 100, 4);
		map.AddTBString(FastInputAttr.Vals, null, "值", true, false, 0, 500, 500);
			//map.AddTBInt(FastInputAttr.Idx, 0, "Idx", true, false);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///

	/** 
	 上移
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoUp() throws Exception
	{
		  this.DoOrderUp(FastInputAttr.ContrastKey, "CYY", FastInputAttr.FK_Emp, WebUser.getNo(), "Idx");

		return "移动成功.";
	}

	public final String DoDown() throws Exception
	{
		this.DoOrderDown(FastInputAttr.ContrastKey, "CYY", FastInputAttr.FK_Emp, WebUser.getNo(), "Idx");
		return "移动成功.";
	}


}