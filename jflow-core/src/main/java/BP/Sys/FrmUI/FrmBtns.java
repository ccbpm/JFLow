package BP.Sys.FrmUI;

import java.util.ArrayList;
import java.util.List;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

/** 
按钮s
*/
public class FrmBtns extends EntitiesMyPK
{
	//#region 构造
	/** 
	 按钮s
	*/
	public FrmBtns()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FrmBtn();
	}
	//#endregion

	//#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmBtn> ToJavaList()
	{
		return (List<FrmBtn>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmBtn> Tolist()
	{
		ArrayList<FrmBtn> list = new ArrayList<FrmBtn>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmBtn)this.get(i));
		}
		return list;
	}
	//#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}

