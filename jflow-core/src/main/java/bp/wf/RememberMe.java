package bp.wf;

import bp.en.*;

/** 
 记忆我
*/
public class RememberMe extends EntityMyPK
{

		///#region 属性
	/** 
	 操作员
	*/
	public final String getFK_Emp() throws Exception
	{
		return this.GetValStringByKey(RememberMeAttr.FK_Emp);
	}
	public final void setFK_Emp(String value)  throws Exception
	 {
		this.SetValByKey(RememberMeAttr.FK_Emp, value);
		this.setMyPK(this.getFK_Node() + "_" + bp.web.WebUser.getNo());
	}
	/** 
	 当前节点
	*/
	public final int getFK_Node() throws Exception
	{
		return this.GetValIntByKey(RememberMeAttr.FK_Node);
	}
	public final void setFK_Node(int value)  throws Exception
	 {
		this.SetValByKey(RememberMeAttr.FK_Node, value);
		this.setMyPK(this.getFK_Node() + "_" + bp.web.WebUser.getNo());
	}
	/** 
	 有效的工作人员
	*/
	public final String getObjs() throws Exception
	{
		return this.GetValStringByKey(RememberMeAttr.Objs);
	}
	public final void setObjs(String value)  throws Exception
	 {
		this.SetValByKey(RememberMeAttr.Objs, value);
	}
	/** 
	 有效的操作人员ext
	*/
	public final String getObjsExt() throws Exception
	{
		return this.GetValStringByKey(RememberMeAttr.ObjsExt);
	}
	public final void setObjsExt(String value)  throws Exception
	 {
		this.SetValByKey(RememberMeAttr.ObjsExt, value);
	}
	/** 
	 所有的人员数量.
	*/
	public final int getNumOfEmps() throws Exception {
		return this.getEmps().split("[@]", -1).length - 2;
	}
	/** 
	 可以处理的人员数量
	*/
	public final int getNumOfObjs() throws Exception {
		return this.getObjs().split("[@]", -1).length - 2;
	}
	/** 
	 所有的工作人员
	*/
	public final String getEmps() throws Exception
	{
		return this.GetValStringByKey(RememberMeAttr.Emps);
	}
	public final void setEmps(String value)  throws Exception
	 {
		this.SetValByKey(RememberMeAttr.Emps, value);
	}
	/** 
	 所有的工作人员ext
	*/
	public final String getEmpsExt() throws Exception {
		String str = this.GetValStringByKey(RememberMeAttr.EmpsExt).trim();
		if (str.length() == 0)
		{
			return str;
		}

		if (str.substring(str.length() - 1).equals("、"))
		{
			return str.substring(0, str.length() - 1);
		}
		else
		{
			return str;
		}
	}
	public final void setEmpsExt(String value)
	 {
		this.SetValByKey(RememberMeAttr.EmpsExt, value);
	}

		///#endregion


		///#region 构造函数
	/** 
	 RememberMe
	*/
	public RememberMe()  {
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
		Map map = new Map("WF_RememberMe", "记忆我");

		map.AddMyPK(true);

		map.AddTBInt(RememberMeAttr.FK_Node, 0, "节点", false, false);
		map.AddTBString(RememberMeAttr.FK_Emp, "", "当前操作人员", true, false, 1, 30, 10);

		map.AddTBString(RememberMeAttr.Objs, "", "分配人员", true, false, 0, 4000, 10);
		map.AddTBString(RememberMeAttr.ObjsExt, "", "分配人员Ext", true, false, 0, 4000, 10);

		map.AddTBString(RememberMeAttr.Emps, "", "所有的工作人员", true, false, 0, 4000, 10);
		map.AddTBString(RememberMeAttr.EmpsExt, "", "工作人员Ext", true, false, 0, 4000, 10);
		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception {
		this.setFK_Emp(bp.web.WebUser.getNo());
		this.setMyPK(this.getFK_Node() + "_" + this.getFK_Emp());
		return super.beforeUpdateInsertAction();
	}
}