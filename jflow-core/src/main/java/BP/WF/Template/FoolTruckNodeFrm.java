package BP.WF.Template;

import BP.DA.DBAccess;
import BP.En.EntityMyPK;
import BP.En.Map;
import BP.En.UAC;

public class FoolTruckNodeFrm  extends EntityMyPK{
	
	 ///#region 基本属性
     /// <summary>
     /// UI界面上的访问控制
     /// </summary>
	public UAC getHisUAC() throws Exception{
         UAC uac = new UAC();
         uac.OpenForSysAdmin();
         return uac;
     }
     /// <summary>
     ///节点
     /// </summary>
     public final int getFK_Node(){
    	 return this.GetValIntByKey(FrmNodeAttr.FK_Node);
     }
     public final void setFK_Node(String value){
    	 this.SetValByKey(FrmNodeAttr.FK_Node, value);
     }
     
     /// <summary>
     /// 表单ID
     /// </summary>
     public final String getFK_Frm(){
          return this.GetValStringByKey(FrmNodeAttr.FK_Frm);
     }
      public final void setFK_Frm(String value){
            this.SetValByKey(FrmNodeAttr.FK_Frm, value);
     }
     /// <summary>
     /// 对应的解决方案
     /// 0=默认方案.节点编号= 自定义方案, 1=不可编辑.
     /// </summary>
     public final int getFrmSln(){
          return this.GetValIntByKey(FrmNodeAttr.FrmSln);
     }
      public final void setFrmSln(String value){
           this.SetValByKey(FrmNodeAttr.FrmSln, value);
     }
     /// <summary>
     /// 流程编号
     /// </summary>
     public final String getFK_Flow(){
         return this.GetValStringByKey(FrmNodeAttr.FK_Flow);
     }
     public final void setFK_Flow(String value){
         this.SetValByKey(FrmNodeAttr.FK_Flow, value);
     }
     ///#endregion

     ///#region 构造方法
     /// <summary>
     /// 累加表单方案
     /// </summary>
     public FoolTruckNodeFrm() { }
     /// <summary>
     /// 累加表单方案
     /// </summary>
     /// <param name="mypk"></param>
     public FoolTruckNodeFrm(String mypk) throws Exception{
    	 super(mypk);
    	 
     }
     /// <summary>
     /// 重写基类方法
     /// </summary>
     public Map getEnMap(){

    	 if (this.get_enMap() != null)
 		{
 			return this.get_enMap();
 		}


         Map map = new Map("WF_FrmNode", "累加表单方案");
         map.AddMyPK();

         map.AddTBInt(FrmNodeAttr.FK_Node, 0, "要作用的节点ID", true, false);
         map.AddTBString(FrmNodeAttr.FK_Frm, null, "表单ID", true, true, 1, 200, 200);
         map.AddDDLSysEnum(FrmNodeAttr.FrmSln, 0, "表单控制方案", true, true, FrmNodeAttr.FrmSln,
            "@0=默认方案@1=只读方案@2=自定义方案");

         map.AddTBString(FrmNodeAttr.FK_Flow, null, "流程编号", true, true, 1, 20, 20);

         this.set_enMap(map);
 		return this.get_enMap();

     }
    

     /// <summary>
     /// 修改前的操作
     /// </summary>
     /// <returns></returns>
     @Override
     protected  boolean beforeUpdate() throws Exception
     {
         //表单方案如果是只读或者默认方案时，删除对应的设置的权限
         if (this.getFrmSln() == 0 || this.getFrmSln() == 1)
         {
             String sql = "";
             sql += "@DELETE FROM Sys_FrmSln WHERE FK_MapData='" + this.getFK_Frm() + "' AND FK_Node='"+this.getFK_Node()+"'";
             sql += "@DELETE FROM Sys_FrmAttachment WHERE FK_MapData='" + this.getFK_Frm() + "' AND FK_Node='" + this.getFK_Node() + "'";
             sql += "@DELETE FROM Sys_MapDtl WHERE FK_MapData='" + this.getFK_Frm() + "' AND FK_Node='" + this.getFK_Node() + "'";
             DBAccess.RunSQLs(sql);
            
         }
         return super.beforeUpdate();
     }

}
