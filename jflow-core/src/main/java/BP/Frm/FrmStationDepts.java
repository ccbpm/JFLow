package BP.Frm;

import BP.En.Entity;
import BP.En.QueryObject;

import java.util.List;

public class FrmStationDepts extends BP.En.EntitiesMM{
    /// <summary>
        /// 单据查询岗位
        /// </summary>
        public FrmStationDepts() { }
        /// <summary>
        /// 单据查询岗位
        /// </summary>
        /// <param name="frmID">单据ID</param>
        public FrmStationDepts(String frmID) throws Exception
        {
            QueryObject qo = new QueryObject(this);
            qo.AddWhere(FrmStationDeptAttr.FK_Frm, frmID);
            qo.DoQuery();
        }
 
       /** 
        得到它的 Entity 
       */
       @Override
       public Entity getNewEntity()
       {
       	return new FrmStationDept();
       }
    /**
     转化成list

     @return List
     */
    public final java.util.ArrayList<FrmStationDept> Tolist()
    {
        java.util.ArrayList<FrmStationDept> list = new java.util.ArrayList<FrmStationDept>();
        for (int i = 0; i < this.size(); i++)
        {
            list.add((FrmStationDept)this.get(i));
        }
        return list;
    }
}
