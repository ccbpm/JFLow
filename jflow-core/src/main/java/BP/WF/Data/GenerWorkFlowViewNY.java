package BP.WF.Data;

import BP.En.EnType;
import BP.En.EntityNoName;
import BP.En.Map;

/** 
 月份
*/
public class GenerWorkFlowViewNY extends EntityNoName
{
		
	/** 
	 月份
	*/
	public GenerWorkFlowViewNY()
	{
	}
	/** 
	 月份
	 @param mypk
	 * @throws Exception 
	*/
	public GenerWorkFlowViewNY(String no) throws Exception
	{
		this.setNo(no);
		this.Retrieve();
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Pub_NY", "月份");
		map.Java_SetEnType(EnType.View);

		map.AddTBStringPK(GenerWorkFlowViewNYAttr.No, null, "编号", true, false, 2, 30, 20);
		map.AddTBString(GenerWorkFlowViewNYAttr.Name, null, "名称", true, false, 0, 3900, 20);
		this.set_enMap(map);
		return this.get_enMap();
	}

}