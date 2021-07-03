package bp.wf;

import bp.da.*;
import bp.web.*;
import bp.en.*;
import bp.en.Map;
import bp.port.*;
import bp.sys.*;
import bp.wf.port.WFEmp;

import java.util.*;

/** 
 授权
*/
public class Auth extends EntityMyPK
{

		///基本属性
	/** 
	 流程编号
	 * @throws Exception 
	*/
	public final String getFlowNo() throws Exception
	{
		return this.GetValStringByKey(AuthAttr.FlowNo);
	}
	public final void setFlowNo(String value) throws Exception
	{
		this.SetValByKey(AuthAttr.FlowNo, value);
	}
	/** 
	 取回日期
	*/
	public final String getTakeBackDT()throws Exception
	{
		return this.GetValStringByKey(AuthAttr.TakeBackDT);
	}
	public final void setTakeBackDT(String value) throws Exception
	{
		this.SetValByKey(AuthAttr.TakeBackDT, value);
	}
	public final String getAuther()throws Exception
	{
		return this.GetValStringByKey(AuthAttr.Auther);
	}
	public final void setAuther(String value) throws Exception
	{
		this.SetValByKey(AuthAttr.Auther, value);
	}
	public final String getAutherToEmpNo()throws Exception
	{
		return this.GetValStringByKey(AuthAttr.AutherToEmpNo);
	}
	public final void setAutherToEmpNo(String value) throws Exception
	{
		this.SetValByKey(AuthAttr.AutherToEmpNo, value);
	}

	public final AuthorWay getAuthType() throws Exception{
		return AuthorWay.forValue(this.GetValIntByKey(AuthAttr.AuthType));
	}
		///


		///构造方法
	/** 
	 授权
	*/
	public Auth()
	{
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

		Map map = new Map("WF_Auth", "授权");

		map.AddMyPK();

		map.AddTBString(AuthAttr.Auther, null, "授权人", true, false, 0, 100, 10);
		map.AddTBInt(AuthAttr.AuthType, 0, "类型(0=全部流程1=指定流程)", true, false);

		map.AddTBString(AuthAttr.AutherToEmpNo, null, "授权给谁?", true, false, 0, 100, 10);
		map.AddTBString(AuthAttr.AutherToEmpName, null, "授权给谁?", true, false, 0, 100, 10);

		map.AddTBString(AuthAttr.FlowNo, null, "流程编号", true, false, 0, 100, 10);
		map.AddTBString(AuthAttr.FlowName, null, "流程名称", true, false, 0, 100, 10);

		map.AddTBDate(AuthAttr.TakeBackDT, null, "取回日期", true, false);
		map.AddTBDate(AuthAttr.RDT, null, "记录日期", true, false);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///

	@Override
	protected boolean beforeInsert()throws Exception
	{
		//this.setMyPK(DBAccess.GenerGUID());
		return super.beforeInsert();
	}
	@Override
	protected void afterInsertUpdateAction() throws Exception{
		WFEmp emp = new WFEmp(this.getAuther());
		emp.setAuthorWay(this.getAuthType().getValue());
		emp.Update();
		super.afterInsertUpdateAction();
	}


}