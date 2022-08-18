package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.port.*;
import bp.sys.*;
import bp.*;
import java.util.*;

/** 
 独立表单树
*/
public class FrmTrees extends EntitiesTree
{
	/** 
	 独立表单树s
	*/
	public FrmTrees()  {
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity() {
		return new FrmTree();
	}

	@Override
	public int RetrieveAll() throws Exception {
		if (bp.difference.SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			return this.Retrieve("OrgNo", bp.web.WebUser.getOrgNo());
		}

		int i = super.RetrieveAll();
		if (i == 0)
		{
			FrmTree fs = new FrmTree();
			fs.setName("公文类");
			fs.setNo("01");
			fs.Insert();

			fs = new FrmTree();
			fs.setName("办公类");
			fs.setNo("02");
			fs.Insert();
			i = super.RetrieveAll();
		}
		return i;
	}


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FrmTree> ToJavaList() {
		return (java.util.List<FrmTree>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmTree> Tolist()  {
		ArrayList<FrmTree> list = new ArrayList<FrmTree>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmTree)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}