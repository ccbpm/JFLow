package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.*;
import bp.web.*;
import bp.difference.*;
import bp.*;
import java.util.*;

/** 
 实体集合
*/
public class EnCfgs extends EntitiesNo
{

		///#region 构造
	/** 
	 配置信息
	*/
	public EnCfgs() throws Exception {
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity() {
		return new EnCfg();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<EnCfg> ToJavaList() {
		return (java.util.List<EnCfg>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<EnCfg> Tolist()  {
		ArrayList<EnCfg> list = new ArrayList<EnCfg>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((EnCfg)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}