package bp.gpm.home;

import bp.da.Depositary;
import bp.en.EnType;
import bp.en.EntityMyPK;
import bp.en.Map;
import bp.en.UAC;

/** 
信息块
*/
public class Window extends EntityMyPK
{

	/** 
	 控制权限
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.IsInsert = false;
		return uac;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 属性
	public final boolean getIsEnable() throws Exception
	{
		return this.GetValBooleanByKey(WindowAttr.IsEnable);
	}
	public final void setIsEnable(boolean value) throws Exception
	{
		this.SetValByKey(WindowAttr.IsEnable, value);
	}
	public final int getIdx() throws Exception
	{
		return this.GetValIntByKey(WindowAttr.Idx);
	}
	public final void setIdx(int value) throws Exception
	{
		this.SetValByKey(WindowAttr.Idx, value);
	}
	public final String getEmpNo() throws Exception
	{
		return this.GetValStrByKey(WindowAttr.EmpNo);
	}
	public final void setEmpNo(String value) throws Exception
	{
		this.SetValByKey(WindowAttr.EmpNo, value);
	}
	public final String getWindowTemplateNo() throws Exception
	{
		return this.GetValStrByKey(WindowAttr.WindowTemplateNo);
	}
	public final void setWindowTemplateNo(String value) throws Exception
	{
		this.SetValByKey(WindowAttr.WindowTemplateNo, value);
	}
	public final String getDocs() throws Exception
	{
		return this.GetValStrByKey(WindowAttr.Docs);
	}
	public final void setDocs(String value) throws Exception
	{
		this.SetValByKey(WindowAttr.Docs, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 构造方法
	/** 
	 信息块
	*/
	public Window()
	{
	}
	/** 
	 信息块
	 
	 @param mypk
	 * @throws Exception 
	*/
	public Window(String mypk) throws Exception
	{
		this.setMyPK(mypk);
		this.Retrieve();
	}
	/** 
	 EnMap
	 * @throws Exception 
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("GPM_Window");
		map.setDepositaryOfEntity(Depositary.None);
		map.setDepositaryOfMap(Depositary.Application);
		map.setEnDesc("信息块");
		map.setEnType(EnType.Sys);

			//主键.
		map.AddMyPK(false);
		map.AddTBString(WindowAttr.EmpNo, null, "用户编号", true, false, 0, 50, 20);
		map.AddTBString(WindowAttr.WindowTemplateNo, null, "模板编号", true, false, 0, 50, 20);
		map.AddTBString(WindowAttr.Docs, null, "内容", true, false, 0, 4000, 20);

		map.AddTBInt(WindowAttr.IsEnable, 0, "是否可见?", false, true);

		map.AddTBInt(WindowAttr.Idx, 0, "排序", false, true);
		map.AddTBString(WindowTemplateAttr.OrgNo, null, "OrgNo", true, false, 0, 50, 20);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region 移动方法.
	/** 
	 向上移动
	 * @throws Exception 
	*/
	public final String DoUp() throws Exception
	{
		this.DoOrderUp(WindowAttr.EmpNo, this.getEmpNo(), WindowAttr.Idx);
		return "执行成功.";

	}
	/** 
	 向下移动
	 * @throws Exception 
	*/
	public final String DoDown() throws Exception
	{
		this.DoOrderDown(WindowAttr.EmpNo, this.getEmpNo(), WindowAttr.Idx);
		return "执行成功.";

	}
	/** 
	 移动到指定的模板前面.
	 
	 @param no
	*/
	public final String DoOrderMoveTo(String no)
	{
		  //this.DoOrderMoveTo(WindowAttr.EmpNo, this.EmpNo, WindowAttr.Idx, no);
		return "执行成功.";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion 移动方法.

}
