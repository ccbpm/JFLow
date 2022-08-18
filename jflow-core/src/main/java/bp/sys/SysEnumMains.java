package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.*;
import java.util.*;

/** 
 纳税人集合 
*/
public class SysEnumMains extends EntitiesNoName
{
	/** 
	 SysEnumMains
	*/
	public SysEnumMains() throws Exception {
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity() {
		return new SysEnumMain();
	}
	/** 
	 查询所有枚举值，根据不同的运行平台.
	 
	 @return 
	*/
	@Override
	public int RetrieveAll() throws Exception {
		// 获取平台的类型. 0=单机版, 1=集团版，2=SAAS。
		int val = bp.difference.SystemConfig.GetValByKeyInt("CCBPMRunModel", 0);
		if (val != 2)
		{
			return super.RetrieveAll();
		}

		// 返回他组织下的数据.
		return this.Retrieve(SysEnumMainAttr.OrgNo, bp.web.WebUser.getFK_Dept());
	}


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<SysEnumMain> ToJavaList() {
		return (java.util.List<SysEnumMain>)(Object)this;
	}

	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<SysEnumMain> Tolist()  {
		ArrayList<SysEnumMain> list = new ArrayList<SysEnumMain>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((SysEnumMain)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}