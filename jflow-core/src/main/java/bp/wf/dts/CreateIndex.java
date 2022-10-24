package bp.wf.dts;

import bp.en.*;

import bp.wf.*;

/** 
 创建索引
*/
public class CreateIndex extends Method
{
	/** 
	 创建索引
	*/
	public CreateIndex()throws Exception
	{
		this.Title = "创建索引（为所有的流程,NDxxxTrack, NDxxRpt, 创建索引.）";
		this.Help = "创建索引字段,调高流程的运行效率.";
	}
	/** 
	 设置执行变量
	 
	 @return 
	*/
	@Override
	public void Init()
	{
	}
	/** 
	 当前的操纵员是否可以执行这个方法
	*/
	@Override
	public boolean getIsCanDo()
	{
		if (bp.web.WebUser.getNo().equals("admin") == true)
		{
			return true;
		}
		return false;
	}
	/** 
	 执行
	 
	 @return 返回执行结果
	*/
	@Override
	public Object Do()throws Exception
	{
		String info = "开始为Track表创建索引.";

		Flows fls = new Flows();
		for (Flow fl : fls.ToJavaList())
		{
			info += fl.CreateIndex();
		}
		return info;

	}
}