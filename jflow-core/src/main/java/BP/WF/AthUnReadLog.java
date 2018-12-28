package BP.WF;

import BP.En.EntityMyPK;
import BP.En.Map;
import BP.En.UAC;
import BP.WF.Data.GERptAttr;

public class AthUnReadLog extends EntityMyPK {
	
	 //region 基本属性
     /// <summary>
     /// 工作ID
     /// </summary>
     public long getWorkID()
     {
         
             return this.GetValIntByKey(AthUnReadLogAttr.WorkID);
         
        
     }
       public void setWorkID(long l){
       SetValByKey(AthUnReadLogAttr.WorkID, l);
     }
     /// <summary>
     /// 操作人
     /// </summary>
     public String getFK_Emp()
     {   
             return this.GetValStringByKey(AthUnReadLogAttr.FK_Emp);
      
     }
     public void setFK_Emp(String value)
     {
         SetValByKey(AthUnReadLogAttr.FK_Emp, value);
     }
     /// <summary>
     /// 删除人员
     /// </summary>
     public String getFK_EmpDept()
     {
         
            return this.GetValStringByKey(AthUnReadLogAttr.FK_EmpDept);
        
     }
     public void setFK_EmpDept(String value)
     {
         SetValByKey(AthUnReadLogAttr.FK_EmpDept, value);
     }
     public String getFK_EmpDeptName()
     {
      
             return this.GetValStringByKey(AthUnReadLogAttr.FK_EmpDeptName);
       
     }
     public void   setFK_EmpDeptName(String value)
     {
         SetValByKey(AthUnReadLogAttr.FK_EmpDeptName, value);
     }
     public String getBeiZhu()
     {
        return this.GetValStringByKey(AthUnReadLogAttr.BeiZhu);
        
        
     }
     public void setBeiZhu(String value)
     {
     
         SetValByKey(AthUnReadLogAttr.BeiZhu, value);
     }
     public String getBeiZhuHtml()
     {
        
             return this.GetValHtmlStringByKey(AthUnReadLogAttr.BeiZhu);
         
     }
     /// <summary>
     /// 记录日期
     /// </summary>
     public String getSendDT()
     {
             return this.GetValStringByKey(AthUnReadLogAttr.SendDT);
        
     }
     public void  setSendDT(String value)
     {
         SetValByKey(AthUnReadLogAttr.SendDT, value);
     }
     
     /// <summary>
     /// 流程编号
     /// </summary>
     public String getFK_Flow()
     {
             return this.GetValStringByKey(AthUnReadLogAttr.FK_Flow);
       
     }
     public void setFK_Flow(String value)
     {
         SetValByKey(AthUnReadLogAttr.FK_Flow, value);
     }
     /// <summary>
     /// 流程类别
     /// </summary>
     public String getFlowName()
     {
       return this.GetValStringByKey(AthUnReadLogAttr.FlowName);
       
     }
     public void setFlowName(String value)
     {
         SetValByKey(AthUnReadLogAttr.FlowName, value);
     }
     public int getFK_Node()
     {
           return this.GetValIntByKey(AthUnReadLogAttr.FK_Node);

     }

     public void setFK_Node(int i)
     {
         SetValByKey(AthUnReadLogAttr.FK_Node, i);
     }
     /// <summary>
     /// 节点名称
     /// </summary>
     public String getNodeName()
     {
        
             return this.GetValStringByKey(AthUnReadLogAttr.NodeName);
        
     }
     
     public void setNodeName(String value)
     {
         SetValByKey(AthUnReadLogAttr.NodeName, value);
     }
     //endregion

     //region 构造函数
     @Override
     public UAC getHisUAC() throws Exception{
     
        
        	BP.En.UAC uac = new BP.En.UAC();
             uac.Readonly();
             return uac;
         
     }
     /// <summary>
     /// 附件未读日志
     /// </summary>
     public AthUnReadLog() { }
     /// <summary>
     /// 重写基类方法
     /// </summary>
	
	
	

public Map getEnMap() {
	// TODO Auto-generated method stub
	if (this.get_enMap() != null)
        return this.get_enMap();

    Map map = new Map("WF_AthUnReadLog", "附件未读日志");
     
    // 流程基础数据。
    map.AddMyPK(false);
    map.AddDDLEntities(GenerWorkFlowAttr.FK_Dept, null, "部门", new BP.WF.Port.Depts(), false);
    map.AddTBString(GenerWorkFlowAttr.Title, null, "标题", true, true, 0, 100, 100);
    map.AddTBInt(GenerWorkFlowAttr.WorkID, 0, "WorkID", false, false);
    map.AddTBString(GERptAttr.FlowStarter, null, "发起人", true, true, 0, 100, 100);
    map.AddTBDateTime(GERptAttr.FlowStartRDT, null, "发起时间", true, true);
    map.AddDDLEntities(GenerWorkFlowAttr.FK_NY, null, "年月", new BP.Pub.NYs(), false);
    map.AddDDLEntities(GenerWorkFlowAttr.FK_Flow, null, "流程", new Flows(), false);


    map.AddTBInt(AthUnReadLogAttr.FK_Node, 0, "节点ID", true, true);
    map.AddTBString(AthUnReadLogAttr.NodeName, null, "节点名称", true, true, 0, 20, 10);

    //删除信息.
    map.AddTBString(AthUnReadLogAttr.FK_Emp, null, "人员", true, true, 0, 20, 10);
    map.AddTBString(AthUnReadLogAttr.FK_EmpDept, null, "人员部门", true, true, 0, 20, 10);
    map.AddTBString(AthUnReadLogAttr.FK_EmpDeptName, null, "人员名称", true, true, 0, 200, 10);
    map.AddTBString(AthUnReadLogAttr.BeiZhu, "", "内容", true, true, 0, 4000, 10);
    map.AddTBDateTime(AthUnReadLogAttr.SendDT, null, "日期", true, true);

    //查询.
    map.AddSearchAttr(GenerWorkFlowAttr.FK_Dept);
    map.AddSearchAttr(GenerWorkFlowAttr.FK_NY);
    map.AddSearchAttr(GenerWorkFlowAttr.FK_Flow);

   // map.AddHidden(FlowDataAttr.FlowEmps, " LIKE ", "'%@@WebUser.No%'");

    this.set_enMap(map);
    return this.get_enMap();
	

   }
}
