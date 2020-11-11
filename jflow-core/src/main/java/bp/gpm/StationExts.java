package bp.gpm;
import bp.en.*;
import java.util.*;

/** 
 岗位s
*/
public class StationExts extends EntitiesNoName
{
	private static final long serialVersionUID = 1L;
	/** 
	 岗位
	*/
	public StationExts()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new bp.gpm.StationExt();
	}


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<StationExt> ToJavaList()
	{
		return (List<StationExt>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<StationExt> Tolist()
	{
		ArrayList<StationExt> list = new ArrayList<StationExt>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((StationExt)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}