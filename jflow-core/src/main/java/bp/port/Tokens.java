package bp.port;

import bp.en.*;
import java.util.*;

/** 
 用户组类型
*/
public class Tokens extends EntitiesMyPK
{
	/** 
	 用户组类型s
	*/
	public Tokens()  {
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity() {
		return new Token();
	}


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<Token> ToJavaList() {
		return (java.util.List<Token>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Token> Tolist()  {
		ArrayList<Token> list = new ArrayList<Token>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Token)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}