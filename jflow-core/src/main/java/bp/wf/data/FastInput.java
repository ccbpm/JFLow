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
	public final String getCfgKey()throws Exception
	{
		return this.GetValStringByKey(FastInputAttr.CfgKey);
	}
	public final void setCfgKey(String value) throws Exception
	{
		this.SetValByKey(FastInputAttr.CfgKey, value);
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
	 
	
	public final String getEnsName()throws Exception
	{
		return this.GetValStringByKey(FastInputAttr.EnsName);
	}
	public final void setEnsName(String value) throws Exception
	{
		this.SetValByKey(FastInputAttr.EnsName, value);
	}
	
	public final String getAttrKey()throws Exception
	{
		return this.GetValStringByKey(FastInputAttr.AttrKey);
	}
	public final void setAttrKey(String value) throws Exception
	{
		this.SetValByKey(FastInputAttr.AttrKey, value);
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


		 /*
         * 常用语分为两个模式: 流程的常用语，与表单字段的常用语. 
         * 这两个模式都存储在同一个表里.
         * 
         * 流程的常用语存储格式为: 
         *  CfgKey=Flow,  EnsName=Flow,  AttrKey=WorkCheck,FlowBBS,WorkReturn 三种类型.
         *  
         * 表单的常用语为存储格式为:
         *  CfgKey=Frm,  EnsName=myformID, AttrKey=myFieldName, 
         * 
         */

        //该表单对应的表单ID ， 
        //CfgKey=Flow, EnsName=Flow 是流程的常用语.   Filed

        map.AddMyPK();
        map.AddTBString(FastInputAttr.CfgKey, null, "类型Flow,Frm", true, false, 0, 20, 20);

        map.AddTBString(FastInputAttr.EnsName, null, "表单ID", true, false, 0, 100, 4);
        map.AddTBString(FastInputAttr.AttrKey, null, "字段", true, false, 0, 100, 4);
        map.AddTBString(FastInputAttr.FK_Emp, null, "人员", true, false, 0, 100, 4);

        map.AddTBString(FastInputAttr.Vals, null, "值", true, false, 0, 500, 500);
        

		this.set_enMap(map);
		return this.get_enMap();
	}

  
	/** 
	 上移
	 
	 @return 
	 * @throws Exception 
	*/
	public final String DoUp() throws Exception
	{
		 this.DoOrderUp(FastInputAttr.CfgKey, "CYY", 
                 FastInputAttr.EnsName,this.getEnsName(), 
                 FastInputAttr.AttrKey, this.getAttrKey(),
               FastInputAttr.FK_Emp, WebUser.getNo(), "Idx") ;
		  

		return "移动成功.";
	}

	public final String DoDown() throws Exception
	{
		 this.DoOrderDown(FastInputAttr.CfgKey, "CYY",
	                FastInputAttr.EnsName, this.getEnsName(),
	                FastInputAttr.AttrKey, this.getAttrKey(),
	              FastInputAttr.FK_Emp, WebUser.getNo(), "Idx");
		  
		return "移动成功.";
	}


}