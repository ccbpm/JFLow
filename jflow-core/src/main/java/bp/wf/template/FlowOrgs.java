package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.wf.port.*;
import bp.wf.*;
import java.util.*;

/** 
 流程对应组织
*/
public class FlowOrgs extends EntitiesMM
{
	/** 
	 流程对应组织
	*/
	public FlowOrgs()
	{
	}
	/** 
	 流程对应组织
	 
	 @param orgNo orgNo 
	 * @throws Exception 
	*/
	public FlowOrgs(String orgNo) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FlowOrgAttr.OrgNo, orgNo);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FlowOrg();
	}


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FlowOrg> ToJavaList()
	{
		return (List<FlowOrg>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FlowOrg> Tolist()
	{
		ArrayList<FlowOrg> list = new ArrayList<FlowOrg>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FlowOrg)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}