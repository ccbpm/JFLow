package BP.En;

import BP.En.*;

/** 
 AttrsOfOneVSM 集合
*/
public class AttrsOfOneVSM extends System.Collections.CollectionBase
{
	public AttrsOfOneVSM()
	{
	}
	public final AttrOfOneVSM get(int index)
	{
		return (AttrOfOneVSM)this.InnerList[index];
	}
	/** 
	 增加一个SearchKey .
	 
	 @param r SearchKey
	*/
	public final void Add(AttrOfOneVSM attr)
	{
		if (this.IsExits(attr))
		{
			return;
		}
		this.InnerList.add(attr);
	}

	/** 
	 是不是存在集合里面
	 
	 @param en 要检查的EnDtl
	 @return true/false
	*/
	public final boolean IsExits(AttrOfOneVSM en)
	{
		for (AttrOfOneVSM attr : this)
		{
			if (attr.getEnsOfMM() == en.getEnsOfMM())
			{
				return true;
			}
		}
		return false;
	}

	/** 
	 增加一个属性
	 
	 @param _ensOfMM 多对多的实体
	 @param _ensOfM 多实体
	 @param AttrOfOneInMM 点实体,在MM中的属性
	 @param AttrOfMInMM 多实体主键在MM中的属性
	 @param AttrOfMText
	 @param AttrOfMValue
	 @param desc 描述
	*/

	public final void Add(Entities _ensOfMM, Entities _ensOfM, String AttrOfOneInMM, String AttrOfMInMM, String AttrOfMText, String AttrOfMValue, String desc, Dot2DotModel model, EntitiesTree ensTree)
	{
		Add(_ensOfMM, _ensOfM, AttrOfOneInMM, AttrOfMInMM, AttrOfMText, AttrOfMValue, desc, model, ensTree, null);
	}

	public final void Add(Entities _ensOfMM, Entities _ensOfM, String AttrOfOneInMM, String AttrOfMInMM, String AttrOfMText, String AttrOfMValue, String desc, Dot2DotModel model)
	{
		Add(_ensOfMM, _ensOfM, AttrOfOneInMM, AttrOfMInMM, AttrOfMText, AttrOfMValue, desc, model, null, null);
	}

	public final void Add(Entities _ensOfMM, Entities _ensOfM, String AttrOfOneInMM, String AttrOfMInMM, String AttrOfMText, String AttrOfMValue, String desc)
	{
		Add(_ensOfMM, _ensOfM, AttrOfOneInMM, AttrOfMInMM, AttrOfMText, AttrOfMValue, desc, Dot2DotModel.Default, null, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public void Add(Entities _ensOfMM, Entities _ensOfM, string AttrOfOneInMM, string AttrOfMInMM, string AttrOfMText, string AttrOfMValue, string desc, Dot2DotModel model= Dot2DotModel.Default, EntitiesTree ensTree=null, string refTreeAttr=null)
	public final void Add(Entities _ensOfMM, Entities _ensOfM, String AttrOfOneInMM, String AttrOfMInMM, String AttrOfMText, String AttrOfMValue, String desc, Dot2DotModel model, EntitiesTree ensTree, String refTreeAttr)
	{

		//属性.
		AttrOfOneVSM en = new AttrOfOneVSM(_ensOfMM, _ensOfM, AttrOfOneInMM, AttrOfMInMM, AttrOfMText, AttrOfMValue, desc);

		//工作模式.
		en.dot2DotModel = model;
		en.EnsTree = ensTree;
		en.RefTreeAttr = refTreeAttr;

		this.Add(en);
	}
	/** 
	 绑定树模式
	 
	 @param _ensOfMM 比如 BP.WF.NodeDepts 
	 @param _ensOfM 比如: BP.Port.Depts
	 @param rootNo 跟节点
	 @param AttrOfOneInMM 比如:FK_Node
	 @param AttrOfMInMM 比如:FK_Dept
	 @param desc 比如:节点绑定部门
	 @param AttrOfMText 一般是Name
	 @param AttrOfMValue 一般是No
	*/

	public final void AddBranches(Entities _ensOfMM, Entities _ensOfM, String AttrOfOneInMM, String AttrOfMInMM, String desc, String AttrOfMText, String AttrOfMValue)
	{
		AddBranches(_ensOfMM, _ensOfM, AttrOfOneInMM, AttrOfMInMM, desc, AttrOfMText, AttrOfMValue, "0");
	}

	public final void AddBranches(Entities _ensOfMM, Entities _ensOfM, String AttrOfOneInMM, String AttrOfMInMM, String desc, String AttrOfMText)
	{
		AddBranches(_ensOfMM, _ensOfM, AttrOfOneInMM, AttrOfMInMM, desc, AttrOfMText, "No", "0");
	}

	public final void AddBranches(Entities _ensOfMM, Entities _ensOfM, String AttrOfOneInMM, String AttrOfMInMM, String desc)
	{
		AddBranches(_ensOfMM, _ensOfM, AttrOfOneInMM, AttrOfMInMM, desc, "Name", "No", "0");
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public void AddBranches(Entities _ensOfMM, Entities _ensOfM, string AttrOfOneInMM, string AttrOfMInMM, string desc, string AttrOfMText = "Name", string AttrOfMValue = "No", string rootNo = "0")
	public final void AddBranches(Entities _ensOfMM, Entities _ensOfM, String AttrOfOneInMM, String AttrOfMInMM, String desc, String AttrOfMText, String AttrOfMValue, String rootNo)
	{
		//属性.
		AttrOfOneVSM en = new AttrOfOneVSM(_ensOfMM, _ensOfM, AttrOfOneInMM, AttrOfMInMM, AttrOfMText, AttrOfMValue, desc);

		//工作模式.
		en.dot2DotModel = Dot2DotModel.TreeDept; //分组模式.

		en.RootNo = rootNo; //默认的根目录.
		this.Add(en);
	}
	/** 
	 增加树杆叶子类型
	 
	 @param _ensOfMM
	 @param _ensOfM
	 @param AttrOfOneInMM
	 @param AttrOfMInMM
	 @param desc
	 @param defaultGroupKey
	 @param AttrOfMText
	 @param AttrOfMValue
	*/

	public final void AddBranchesAndLeaf(Entities _ensOfMM, Entities _ensOfM, String AttrOfOneInMM, String AttrOfMInMM, String desc, String defaultGroupKey, String AttrOfMText, String AttrOfMValue)
	{
		AddBranchesAndLeaf(_ensOfMM, _ensOfM, AttrOfOneInMM, AttrOfMInMM, desc, defaultGroupKey, AttrOfMText, AttrOfMValue, "0");
	}

	public final void AddBranchesAndLeaf(Entities _ensOfMM, Entities _ensOfM, String AttrOfOneInMM, String AttrOfMInMM, String desc, String defaultGroupKey, String AttrOfMText)
	{
		AddBranchesAndLeaf(_ensOfMM, _ensOfM, AttrOfOneInMM, AttrOfMInMM, desc, defaultGroupKey, AttrOfMText, "No", "0");
	}

	public final void AddBranchesAndLeaf(Entities _ensOfMM, Entities _ensOfM, String AttrOfOneInMM, String AttrOfMInMM, String desc, String defaultGroupKey)
	{
		AddBranchesAndLeaf(_ensOfMM, _ensOfM, AttrOfOneInMM, AttrOfMInMM, desc, defaultGroupKey, "Name", "No", "0");
	}

	public final void AddBranchesAndLeaf(Entities _ensOfMM, Entities _ensOfM, String AttrOfOneInMM, String AttrOfMInMM, String desc)
	{
		AddBranchesAndLeaf(_ensOfMM, _ensOfM, AttrOfOneInMM, AttrOfMInMM, desc, null, "Name", "No", "0");
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public void AddBranchesAndLeaf(Entities _ensOfMM, Entities _ensOfM, string AttrOfOneInMM, string AttrOfMInMM, string desc, string defaultGroupKey = null, string AttrOfMText = "Name", string AttrOfMValue = "No", string rootNo="0")
	public final void AddBranchesAndLeaf(Entities _ensOfMM, Entities _ensOfM, String AttrOfOneInMM, String AttrOfMInMM, String desc, String defaultGroupKey, String AttrOfMText, String AttrOfMValue, String rootNo)
	{
		//属性.
		AttrOfOneVSM en = new AttrOfOneVSM(_ensOfMM, _ensOfM, AttrOfOneInMM, AttrOfMInMM, AttrOfMText, AttrOfMValue, desc);

		//工作模式.
		en.dot2DotModel = Dot2DotModel.TreeDeptEmp; //分组模式.

		//默认的分组字段，可以是一个类名或者枚举.
		en.DefaultGroupAttrKey = defaultGroupKey;
		en.RootNo = rootNo; //默认的根目录.

		this.Add(en);
	}
	/** 
	 增加分组列表模式
	 
	 @param _ensOfMM
	 @param _ensOfM
	 @param AttrOfOneInMM
	 @param AttrOfMInMM
	 @param desc
	 @param defaultGroupKey
	 @param AttrOfMText
	 @param AttrOfMValue
	 @param rootNo
	*/

	public final void AddGroupListModel(Entities _ensOfMM, Entities _ensOfM, String AttrOfOneInMM, String AttrOfMInMM, String desc, String defaultGroupKey, String AttrOfMText)
	{
		AddGroupListModel(_ensOfMM, _ensOfM, AttrOfOneInMM, AttrOfMInMM, desc, defaultGroupKey, AttrOfMText, "No");
	}

	public final void AddGroupListModel(Entities _ensOfMM, Entities _ensOfM, String AttrOfOneInMM, String AttrOfMInMM, String desc, String defaultGroupKey)
	{
		AddGroupListModel(_ensOfMM, _ensOfM, AttrOfOneInMM, AttrOfMInMM, desc, defaultGroupKey, "Name", "No");
	}

	public final void AddGroupListModel(Entities _ensOfMM, Entities _ensOfM, String AttrOfOneInMM, String AttrOfMInMM, String desc)
	{
		AddGroupListModel(_ensOfMM, _ensOfM, AttrOfOneInMM, AttrOfMInMM, desc, null, "Name", "No");
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public void AddGroupListModel(Entities _ensOfMM, Entities _ensOfM, string AttrOfOneInMM, string AttrOfMInMM, string desc, string defaultGroupKey = null, string AttrOfMText = "Name", string AttrOfMValue = "No")
	public final void AddGroupListModel(Entities _ensOfMM, Entities _ensOfM, String AttrOfOneInMM, String AttrOfMInMM, String desc, String defaultGroupKey, String AttrOfMText, String AttrOfMValue)
	{
		//属性.
		AttrOfOneVSM en = new AttrOfOneVSM(_ensOfMM, _ensOfM, AttrOfOneInMM, AttrOfMInMM, AttrOfMText, AttrOfMValue, desc);

		//工作模式.
		en.dot2DotModel = Dot2DotModel.TreeDeptEmp; //分组模式.

		//默认的分组字段，可以是一个类名或者枚举.
		en.DefaultGroupAttrKey = defaultGroupKey;
		en.RootNo = "0";

		this.Add(en);
	}
	/** 
	 绑定分组列表平铺模式
	 
	 @param _ensOfMM
	 @param _ensOfM
	 @param AttrOfOneInMM
	 @param AttrOfMInMM
	 @param desc 标签或者描述
	 @param AttrOfMText 显示的标签,一般为 Name
	 @param AttrOfMValue 存储的值字段,一般为 No
	 @param defaultGroupKey 默认的分组外键或者枚举,如果为空就不分组.
	*/

	public final void AddGroupPanelModel(Entities _ensOfMM, Entities _ensOfM, String AttrOfOneInMM, String AttrOfMInMM, String desc, String defaultGroupKey, String AttrOfMText)
	{
		AddGroupPanelModel(_ensOfMM, _ensOfM, AttrOfOneInMM, AttrOfMInMM, desc, defaultGroupKey, AttrOfMText, "No");
	}

	public final void AddGroupPanelModel(Entities _ensOfMM, Entities _ensOfM, String AttrOfOneInMM, String AttrOfMInMM, String desc, String defaultGroupKey)
	{
		AddGroupPanelModel(_ensOfMM, _ensOfM, AttrOfOneInMM, AttrOfMInMM, desc, defaultGroupKey, "Name", "No");
	}

	public final void AddGroupPanelModel(Entities _ensOfMM, Entities _ensOfM, String AttrOfOneInMM, String AttrOfMInMM, String desc)
	{
		AddGroupPanelModel(_ensOfMM, _ensOfM, AttrOfOneInMM, AttrOfMInMM, desc, null, "Name", "No");
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public void AddGroupPanelModel(Entities _ensOfMM, Entities _ensOfM, string AttrOfOneInMM, string AttrOfMInMM, string desc, string defaultGroupKey = null, string AttrOfMText = "Name", string AttrOfMValue = "No")
	public final void AddGroupPanelModel(Entities _ensOfMM, Entities _ensOfM, String AttrOfOneInMM, String AttrOfMInMM, String desc, String defaultGroupKey, String AttrOfMText, String AttrOfMValue)
	{
		//属性.
		AttrOfOneVSM en = new AttrOfOneVSM(_ensOfMM, _ensOfM, AttrOfOneInMM, AttrOfMInMM, AttrOfMText, AttrOfMValue, desc);

		//工作模式.
		en.dot2DotModel = Dot2DotModel.Default; //分组模式.

		//默认的分组字段，可以是一个类名或者枚举.
		en.DefaultGroupAttrKey = defaultGroupKey;

		this.Add(en);
	}

}