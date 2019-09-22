package BP.En;

import java.util.ArrayList;

/**
 * AttrsOfOneVSM 集合
 */
public class AttrsOfOneVSM extends ArrayList<AttrOfOneVSM>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AttrsOfOneVSM()
	{
	}
	
	public final AttrOfOneVSM getItem(int index)
	{
		return (AttrOfOneVSM) this.get(index);
		/*
		 * warning return (AttrOfOneVSM)this.get(index);
		 */
	}
	
	/**
	 * 增加一个SearchKey .
	 * 
	 * @param r
	 *            SearchKey
	 */
	public final void Add(AttrOfOneVSM attr)
	{
		if (this.IsExits(attr))
		{
			return;
		}
		this.add(attr);
		/*
		 * warning this.add(attr);
		 */
	}
	
	/**
	 * 是不是存在集合里面
	 * 
	 * @param en
	 *            要检查的EnDtl
	 * @return true/false
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
	
	 
	 
    public void AddBranches(Entities _ensOfMM, Entities _ensOfM, String AttrOfOneInMM, String AttrOfMInMM,
       String desc, String AttrOfMText  , String AttrOfMValue  , String rootNo )
    {
        //属性.
        AttrOfOneVSM en = new AttrOfOneVSM(_ensOfMM, _ensOfM, AttrOfOneInMM,
            AttrOfMInMM, AttrOfMText, AttrOfMValue, desc);

        //工作模式.
        en.dot2DotModel =   Dot2DotModel.TreeDept; //分组模式.

        en.RootNo = rootNo; //默认的根目录.
        
        this.add(en);
    }
    /*
    /// <summary>
    /// 增加树杆叶子类型
    /// </summary>
    /// <param name="_ensOfMM"></param>
    /// <param name="_ensOfM"></param>
    /// <param name="AttrOfOneInMM"></param>
    /// <param name="AttrOfMInMM"></param>
    /// <param name="desc"></param>
    /// <param name="defaultGroupKey"></param>
    /// <param name="AttrOfMText"></param>
    /// <param name="AttrOfMValue"></param> */
    public void AddBranchesAndLeaf(Entities _ensOfMM, Entities _ensOfM, String AttrOfOneInMM, String AttrOfMInMM,
        String desc, String defaultGroupKey , String AttrOfMText, String AttrOfMValue , String rootNo)
    {
    	
        //属性.
        AttrOfOneVSM en = new AttrOfOneVSM(_ensOfMM, _ensOfM, AttrOfOneInMM,
            AttrOfMInMM, AttrOfMText, AttrOfMValue, desc);

        //工作模式.
        en.dot2DotModel =   Dot2DotModel.TreeDeptEmp; //分组模式.

        //默认的分组字段，可以是一个类名或者枚举.
        en.DefaultGroupAttrKey = defaultGroupKey;
        en.RootNo = rootNo; //默认的根目录.

        this.add(en);
    }
   
    public void AddGroupListModel(Entities _ensOfMM, Entities _ensOfM, String AttrOfOneInMM, String AttrOfMInMM,
      String desc, String defaultGroupKey , String AttrOfMText, String AttrOfMValue)
    {
        //属性.
        AttrOfOneVSM en = new AttrOfOneVSM(_ensOfMM, _ensOfM, AttrOfOneInMM,
            AttrOfMInMM, AttrOfMText, AttrOfMValue, desc);

        //工作模式.
        en.dot2DotModel = Dot2DotModel.TreeDeptEmp; //分组模式.

        //默认的分组字段，可以是一个类名或者枚举.
        en.DefaultGroupAttrKey = defaultGroupKey;
        en.RootNo = "0";

        this.add(en);
    }
    
    public void AddGroupPanelModel(Entities _ensOfMM, Entities _ensOfM, String AttrOfOneInMM, String AttrOfMInMM,
    		String desc, String defaultGroupKey , String AttrOfMText, String AttrOfMValue )
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