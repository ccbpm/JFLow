package BP.En;

import java.util.ArrayList;
import java.util.List;

/**
 * 树实体s
 */
public abstract class EntitiesTree extends Entities
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	public static ArrayList<EntityTree> convertEntityTree(Object obj)
	{
		return (ArrayList<EntityTree>) obj;
	}
	public List<EntityTree> ToJavaListEnTree()
	{
		return (List<EntityTree>)(Object)this;
	}	
	 //#region 转化为树结构的tree.
	public StringBuilder appendMenus = null;
	public StringBuilder appendMenuSb = null;
      /// <summary>
      /// 转化为json树
      /// </summary>
      /// <returns></returns>
      public String ToJsonOfTree(String rootNo)
      {
    	  if(rootNo==null || rootNo==""){
    		  rootNo="0";
    	  }
          appendMenus = new StringBuilder();
          appendMenuSb = new StringBuilder();
          EntityTree root = (EntityTree) this.GetEntityByKey(EntityTreeAttr.ParentNo, rootNo) ;
          if (root == null)
              return "err@没有找到rootNo=" + rootNo + "的entity.";

          appendMenus.append("[{");
          appendMenus.append("'id':'" + root.getNo() + "',");
          appendMenus.append("'pid':'" + root.getParentNo() + "',");
          appendMenus.append("'text':'" + root.getName() + "'");
         // appendMenus.Append(IsPermissionsNodes(ens, dms, root.No));

          // 增加它的子级.
          appendMenus.append(",'children':");
          AddChildren(root, this);
          appendMenus.append(appendMenuSb);
          appendMenus.append("}]");

          return ReplaceIllgalChart(appendMenus.toString());
      }
      public void AddChildren(EntityTree parentEn, EntitiesTree ens)
      {
          appendMenus.append(appendMenuSb);
          appendMenuSb.delete( 0, appendMenuSb.length());

          appendMenuSb.append("[");
          for (EntityTree item : ens.ToJavaListEnTree())
          {
              if (!item.getParentNo().equals(parentEn.getNo()))
                  continue;

              appendMenuSb.append("{'id':'" + item.getNo() + "','pid':'"+item.getParentNo()+"','text':'" + item.getName() + "'");
              //appendMenuSb.Append(IsPermissionsNodes(ens, dms, item.No));
              EntityTree treeNode = item ;
              // 增加它的子级.
              appendMenuSb.append(",'children':");
              AddChildren(item, ens);
              appendMenuSb.append("},");
          }
          if (appendMenuSb.length() > 1)
              appendMenuSb = appendMenuSb.deleteCharAt(appendMenuSb.length() - 1);
          appendMenuSb.append("]");
          appendMenus.append(appendMenuSb);
          appendMenuSb.delete( 0, appendMenuSb.length());

      }
      public String ReplaceIllgalChart(String s)
      {
          StringBuilder sb = new StringBuilder();
          for (int i = 0, j = s.length(); i < j; i++)
          {

              char c = s.charAt(i);
              switch (c)
              {
                  case '\"':
                      sb.append("\\\"");
                      break;
                  case '\\':
                      sb.append("\\\\");
                      break;
                  case '/':
                      sb.append("\\/");
                      break;
                  case '\b':
                      sb.append("\\b");
                      break;
                  case '\f':
                      sb.append("\\f");
                      break;
                  case '\n':
                      sb.append("\\n");
                      break;
                  case '\r':
                      sb.append("\\r");
                      break;
                  case '\t':
                      sb.append("\\t");
                      break;
                  default:
                      sb.append(c);
                      break;
              }
          }
          return sb.toString();
      }
      //#endregion 转化为树结构的tree.


	/**
	 * 查询他的子节点
	 * 
	 * @param en
	 * @return
	 * @throws Exception 
	 */
	public final int RetrieveHisChinren(EntityTree en) throws Exception
	{
		int i = this.Retrieve(EntityTreeAttr.ParentNo, en.getNo());
		this.AddEntity(en);
		return i + 1;
	}
	
	/**
	 * 获取它的子节点
	 * 
	 * @param en
	 * @return
	 */
	public final EntitiesTree GenerHisChinren(EntityTree en)
	{
		Entities tempVar = this.CreateInstance();
		EntitiesTree ens = (EntitiesTree) ((tempVar instanceof EntitiesTree) ? tempVar
				: null);
		for (EntityTree item : convertEntityTree(ens))
		{
			if (en.getNo().equals(en.getParentNo()))
			{
				ens.AddEntity(item);
			}
		}
		return ens;
	}
	
	/**
	 * 根据位置取得数据
	 */
	public final EntityTree getItem(int index)
	{
		return (EntityTree) this.get(index);
	}
	
	/**
	 * 构造
	 */
	public EntitiesTree()
	{
	}
	

}