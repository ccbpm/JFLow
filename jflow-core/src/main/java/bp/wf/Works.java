package bp.wf;

import bp.en.*;

import java.util.List;

/** 
 工作 集合
*/
public abstract class Works extends EntitiesOID
{

		///#region 构造方法
	/** 
	 信息采集基类
	*/
	public Works() throws Exception {
	}

		///#endregion
public final List<Work> ToJavaList()
{
	return (List<Work>)(Object)this;
}

}