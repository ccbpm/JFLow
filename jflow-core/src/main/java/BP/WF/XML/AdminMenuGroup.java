package BP.WF.XML;

import BP.Tools.StringHelper;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

public class AdminMenuGroup extends XmlEn{
	
    public String getNo()
    {
    	return this.GetValStringByKey("No");
    }
    
    public void setNo(String value)
    {
    	this.SetVal("No", value);
    }
    
    public String getParentNo()
    {
        return this.GetValStringByKey("ParentNo");
    }
    
    public void setParentNo(String value)
    {
    	this.SetVal("ParentNo", value);
    }
    
    public String getName()
    {
    	return this.GetValStringByKey("Name");
    }
    
    public void setName(String value)
    {
    	this.SetVal("Name", value);
    }
    public String getFor()
    {
        return this.GetValStringByKey("For");
    }
    
    public void setFor(String value)
    {
    	this.SetVal("For", value);
    }

    //构造
    /// <summary>
    /// 节点扩展信息
    /// </summary>
    public AdminMenuGroup()
    {
    }
    /// <summary>
    /// 获取一个实例
    /// </summary>
    public XmlEns getGetNewEntities()
    {
         return new AdminMenuGroups();
    }

    /// <summary>
    /// 是否可以使用？
    /// </summary>
    /// <param name="no">操作员编号</param>
    /// <returns></returns>
    public Boolean IsCanUse(String no)
    {
        if (StringHelper.isNullOrEmpty(this.getFor()))
            return true;

        if (this.getFor() == no)
            return true;

        if (this.getFor() == "SecondAdmin")
            return true;
        
        return false;
    }
    

}
