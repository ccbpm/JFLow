package bp.wf.data;

import bp.sys.*;
import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.wf.*;
import java.util.*;

/** 
 月份
*/
public class GenerWorkFlowViewNY extends EntityNoName
{

		///属性

		///


		///构造方法
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
	public GenerWorkFlowViewNY(String no) throws Exception
	{
		this.setNo( no);
		this.Retrieve();
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Pub_NY", "月份");
		map.setEnType( EnType.App);

		map.AddTBStringPK(GenerWorkFlowViewNYAttr.No, null, "编号", true, false, 2, 30, 20);
		map.AddTBString(GenerWorkFlowViewNYAttr.Name, null, "名称", true, false, 0, 3900, 20);
		this.set_enMap(map);
		return this.get_enMap();
	}

		///

}