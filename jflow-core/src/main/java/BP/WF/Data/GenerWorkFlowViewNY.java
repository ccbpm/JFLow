package BP.WF.Data;

import BP.Sys.*;
import BP.DA.*;
import BP.En.*;
import BP.WF.*;
import java.util.*;

/** 
 月份
*/
public class GenerWorkFlowViewNY extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 月份
	*/
	public GenerWorkFlowViewNY()
	{
	}
	/** 
	 月份
	 
	 @param mypk
	*/
	public GenerWorkFlowViewNY(String no)
	{
		this.No = no;
		this.Retrieve();
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap()
	{
		if (this._enMap != null)
		{
			return this._enMap;
		}
		Map map = new Map("Pub_NY", "月份");
		map.Java_SetEnType(EnType.View);

		map.AddTBStringPK(GenerWorkFlowViewNYAttr.No, null, "编号", true, false, 2, 30, 20);
		map.AddTBString(GenerWorkFlowViewNYAttr.Name, null, "名称", true, false, 0, 3900, 20);
		this._enMap = map;
		return this._enMap;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

}