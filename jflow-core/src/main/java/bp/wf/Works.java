package bp.wf;
import java.util.List;
import bp.en.EntitiesOID;
/** 
 工作 集合
*/
public abstract class Works extends EntitiesOID
{

		///构造方法
	/** 
	 信息采集基类
	*/
	public Works()
	{
	}

	public final List<Work> ToJavaList()
	{
		return (List<Work>)(Object)this;
	}


}