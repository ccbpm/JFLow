package bp.sys.frmui;

import bp.da.*;
import bp.en.*;
import bp.sys.*;
import bp.*;
import bp.sys.*;
import java.util.*;

/** 
 按钮s
*/
public class FrmBtns extends EntitiesMyPK
{

		///构造
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

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FrmBtn> ToJavaList()
	{
		return (java.util.List<FrmBtn>)(Object)this;
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

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}