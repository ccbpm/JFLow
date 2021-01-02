package bp.wf.data;

import bp.da.*;
import bp.en.*;
import bp.web.*;
import java.util.*;

/** 
 常用语s
*/
public class FastInputs extends EntitiesMyPK
{
	private static final long serialVersionUID = 1L;
	/** 
	 常用语s
	*/
	public FastInputs()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FastInput();
	}

	/** 
	 查询全部
	 
	 @return 
	 * @throws Exception 
	*/
	@Override
	public int RetrieveAll() throws Exception
	{

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


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FastInput> ToJavaList()
	{
		return (List<FastInput>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FastInput> Tolist()
	{
		ArrayList<FastInput> list = new ArrayList<FastInput>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FastInput)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}