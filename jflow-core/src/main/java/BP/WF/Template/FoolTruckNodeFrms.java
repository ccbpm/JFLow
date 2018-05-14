package BP.WF.Template;

import BP.En.EntitiesMyPK;
import BP.En.Entity;

public class FoolTruckNodeFrms  extends EntitiesMyPK{
	 ///#region 构造方法..
     /// <summary>
     /// 累加表单方案
     /// </summary>
     public FoolTruckNodeFrms() { }
     ///#endregion 构造方法..

     ///#region 公共方法.
     /// <summary>
     /// 得到它的 Entity 
     /// </summary>
     public  Entity getGetNewEntity(){
             return new FoolTruckNodeFrm();
     }
     ///#endregion 公共方法.

     ///#region 为了适应自动翻译成java的需要,把实体转换成List.
     /// <summary>
     /// 转化成 java list,C#不能调用.
     /// </summary>
     /// <returns>List</returns>
     public java.util.List<FoolTruckNodeFrm> ToJavaList()
     {
         return (java.util.List<FoolTruckNodeFrm>)(Object)this;
     }
     /// <summary>
     /// 转化成list
     /// </summary>
     /// <returns>List</returns>
     public java.util.List<FoolTruckNodeFrm> Tolist()
     {
    	 java.util.List<FoolTruckNodeFrm> list = new java.util.ArrayList<FoolTruckNodeFrm>();
         for (int i = 0; i < this.size(); i++)
         {
             list.add((FoolTruckNodeFrm)this.get(i));
         }
         return list;
     }
     ///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

}
