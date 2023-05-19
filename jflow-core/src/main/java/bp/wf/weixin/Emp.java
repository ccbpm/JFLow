package bp.wf.weixin;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.port.*;
import bp.*;
import bp.wf.*;
import java.util.*;

/** 
 Emp 的摘要说明。
*/
public class Emp extends EntityNoName
{

		///#region 扩展属性
	public final String getOpenID() throws Exception
	{
		return this.GetValStrByKey(EmpAttr.OpenID);
	}
	public final void setOpenID(String value)  throws Exception
	 {
		this.SetValByKey(EmpAttr.OpenID, value);
	}
	public final String getFK_Dept() throws Exception
	{
		return this.GetValStrByKey(EmpAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)  throws Exception
	 {
		this.SetValByKey(EmpAttr.FK_Dept, value);
	}

	/** 
	 手机号码
	*/
	public final String getTel() throws Exception
	{
		return this.GetValStrByKey(EmpAttr.Tel);
	}
	public final void setTel(String value)  throws Exception
	 {
		this.SetValByKey(EmpAttr.Tel, value);
	}


		///#endregion

	/** 
	 工作人员
	*/
	public Emp()  {
	}

	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();
		uac.OpenForAppAdmin();
		return uac;
	}
	/** 
	 重写基类方法
	*/
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Port_Emp", "用户");


			///#region 字段
			/*关于字段属性的增加 */
		map.AddTBStringPK(EmpAttr.No, null, "编号", true, false, 1, 50, 100);
		map.AddTBString(EmpAttr.Name, null, "名称", true, false, 0, 100, 100);
		map.AddTBString(EmpAttr.OpenID, null, "OpenID", true, false, 0, 100, 100);

		map.AddDDLEntities(EmpAttr.FK_Dept, null, "部门", new Depts(), true);
		map.AddTBString(EmpAttr.Tel, null, "手机号码", false, false, 0, 11, 30);

			///#endregion 字段


		this.set_enMap(map);
		return this.get_enMap();
	}

	public Entities getNewEntities()  {
		return new Emps();
	}
}