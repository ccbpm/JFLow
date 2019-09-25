package BP.WF;

import BP.DA.*;
import BP.En.*;
import BP.Sys.*;
import BP.Port.*;
import BP.WF.XML.*;
import BP.WF.Template.*;
import java.time.*;
import java.util.List;

/** 
 工作 集合
*/
public abstract class Works extends EntitiesOID
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 信息采集基类
	*/
	public Works()
	{
	}
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<Work> ToJavaList()
	{
		return (List<Work>)(Object)this;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 查询方法
	public final int Retrieve(String fromDataTime, String toDataTime) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.setTop( 90000);
		qo.AddWhere(WorkAttr.RDT, " >=", fromDataTime);
		qo.addAnd();
		qo.AddWhere(WorkAttr.RDT, " <= ", toDataTime);
		return qo.DoQuery();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}