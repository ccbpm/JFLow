package BP.WF.XML;

import BP.En.Entities;
import BP.Sys.SystemConfig;
import BP.XML.XmlEn;
import BP.XML.XmlEns;

public class AdminMenuGroups extends XmlEns{

        /// <summary>
        /// 考核率的数据元素
        /// </summary>
        public AdminMenuGroups() { }

        //重写基类属性或方法。
        /// <summary>
        /// 得到它的 Entity 
        /// </summary>
        public XmlEn getGetNewEntity()
        {
                return new AdminMenuGroup();
        }
        public String getFile()
        {
                return SystemConfig.getPathOfWebApp() + "/DataUser/XML/AdminMenu.xml";
        }
        /// <summary>
        /// 物理表名
        /// </summary>
        public String getTableName()
        {
                return "Group";
        }
        public  Entities getRefEns()
        {
                return null; //new BP.ZF1.AdminAdminMenus();
        }
        
        public final java.util.List<AdminMenuGroup> ToJavaList()
    	{
    		return (java.util.List<AdminMenuGroup>)(Object)this;
    	}
}
