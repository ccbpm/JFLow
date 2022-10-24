package bp.ccfast.portal.windowext;

import bp.en.*;

/** 
 环形图
*/
public class ChartRing extends EntityNoName
{

		///#region 权限控制.
	/** 
	 控制权限
	*/
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();
		if (bp.web.WebUser.getIsAdmin() == true)
		{
			uac.OpenAll();
		}
		else
		{
			uac.IsView = false;
		}
		uac.IsInsert = false;
		uac.IsDelete = false;
		return uac;
	}

		///#endregion 权限控制.


		///#region 属性

		///#endregion 属性


		///#region 构造方法
	/** 
	 环形图
	*/
	public ChartRing()  {
	}
	/** 
	 环形图
	 
	 param no
	*/
	public ChartRing(String no)
	{
		this.setNo(no);
		try {
			this.Retrieve();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/** 
	 EnMap
	*/
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		this.set_enMap(Glo.StationDBSrcMap("ChartRing环形图"));

		return this.get_enMap();
	}

		///#endregion
}