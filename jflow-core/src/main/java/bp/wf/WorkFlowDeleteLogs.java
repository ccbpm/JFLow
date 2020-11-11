package bp.wf;
import bp.en.*;
import java.util.*;

/** 
 流程删除日志s 
*/
public class WorkFlowDeleteLogs extends Entities
{
	private static final long serialVersionUID = 1L;
	///构造
	/** 
	 流程删除日志s
	*/
	public WorkFlowDeleteLogs()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new WorkFlowDeleteLog();
	}

		///


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<WorkFlowDeleteLog> ToJavaList()
	{
		return (List<WorkFlowDeleteLog>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<WorkFlowDeleteLog> Tolist()
	{
		ArrayList<WorkFlowDeleteLog> list = new ArrayList<WorkFlowDeleteLog>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((WorkFlowDeleteLog)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}