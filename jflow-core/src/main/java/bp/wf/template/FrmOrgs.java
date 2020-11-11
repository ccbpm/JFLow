package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.wf.port.*;
import bp.wf.*;
import java.util.*;

/** 
 表单对应组织
*/
public class FrmOrgs extends EntitiesMM
{
	/** 
	 表单对应组织
	*/
	public FrmOrgs()
	{
	}
	/** 
	 表单对应组织
	 
	 @param EmpNo EmpNo 
	 * @throws Exception 
	*/
	public FrmOrgs(String orgNo) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FrmOrgAttr.OrgNo, orgNo);
		qo.DoQuery();
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new FrmOrg();
	}


		///为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<FrmOrg> ToJavaList()
	{
		return (List<FrmOrg>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmOrg> Tolist()
	{
		ArrayList<FrmOrg> list = new ArrayList<FrmOrg>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmOrg)this.get(i));
		}
		return list;
	}

		/// 为了适应自动翻译成java的需要,把实体转换成List.
}