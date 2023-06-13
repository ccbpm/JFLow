package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.web.*;
import bp.*;
import java.util.*;

/** 
 常用语s
*/
public class FastInputs extends EntitiesMyPK
{
	/** 
	 常用语s
	*/
	public FastInputs() throws Exception {
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity() {
		return new FastInput();
	}

	/**
	 * 获得已经有的数据
	 * @return
	 */
	public String InitData_Flow() throws Exception {
		String userNo = WebUser.getNo();
		if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
			userNo = WebUser.getOrgNo() + "_" + userNo;

		int i = this.Retrieve("CfgKey", "Flow", "FK_Emp", userNo);
		if (i == 0)
		{
			FastInput en = new FastInput();
			en.setMyPK("Flow" + userNo + "_1");
			en.SetValByKey("CfgKey", "Flow");

			en.SetValByKey("FK_Emp", userNo);
			en.SetValByKey("Vals", "同意");
			en.Insert();

			en = new FastInput();
			en.setMyPK("Flow" + userNo + "_2");
			en.SetValByKey("CfgKey", "Flow");

			en.SetValByKey("FK_Emp", userNo);
			en.SetValByKey("Vals", "不同意");
			en.Insert();

			en = new FastInput();
			en.setMyPK("Flow" + userNo + "_3");
			en.SetValByKey("CfgKey", "Flow");

			en.SetValByKey("FK_Emp", userNo);
			en.SetValByKey("Vals", "请领导斟酌");
			en.Insert();

			this.Retrieve("CfgKey", "Flow", "FK_Emp", userNo);
		}

		if (i < 12)
		{
			for (int idx = 0; idx < 12 - this.size(); idx++)
			{
				FastInput en = new FastInput();
				en.setMyPK("Flow" + userNo + "_11" + idx);
				en.SetValByKey("CfgKey", "Flow");
				en.SetValByKey("FK_Emp", userNo);
				en.SetValByKey("Vals", "");
				en.Insert();
			}
		}

		this.Retrieve("CfgKey", "Flow", "FK_Emp", userNo);
		return this.ToJson();
	}

	/** 
	 查询全部
	 
	 @return 
	*/
	@Override
	public int RetrieveAll() throws Exception {

		int val = this.Retrieve(FastInputAttr.CfgKey, "CYY", FastInputAttr.FK_Emp, WebUser.getNo());

		if (val == 0)
		{
			FastInput en = new FastInput();
			en.setMyPK(DBAccess.GenerGUID());
			en.setVals("同意");
			en.setFK_Emp(WebUser.getNo());
			en.Insert();

			en = new FastInput();
			en.setMyPK(DBAccess.GenerGUID());
			en.setVals("不同意");
			en.setFK_Emp(WebUser.getNo());
			en.Insert();

			en = new FastInput();
			en.setMyPK(DBAccess.GenerGUID());
			en.setVals("同意，请领导批示");
			en.setFK_Emp(WebUser.getNo());
			en.Insert();

			en = new FastInput();
			en.setMyPK(DBAccess.GenerGUID());
			en.setVals("同意办理");
			en.setFK_Emp(WebUser.getNo());
			en.Insert();

			en = new FastInput();
			en.setMyPK(DBAccess.GenerGUID());
			en.setVals("情况属实报领导批准");
			en.setFK_Emp(WebUser.getNo());
			en.Insert();

			val = this.Retrieve(FastInputAttr.CfgKey, "CYY", FastInputAttr.FK_Emp, WebUser.getNo());
		}
		return val;
	}


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FastInput> ToJavaList() {
		return (java.util.List<FastInput>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FastInput> Tolist()  {
		ArrayList<FastInput> list = new ArrayList<FastInput>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FastInput)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}