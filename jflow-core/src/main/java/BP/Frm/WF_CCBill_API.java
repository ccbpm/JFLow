package BP.Frm;

import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.Difference.Handler.WebContralBase;
import BP.Difference.SystemConfig;
import BP.Sys.GEEntity;
import BP.Web.WebUser;

public class WF_CCBill_API extends WebContralBase {
    public WF_CCBill_API(){

    }

    /**
     目录树编号

     */
    public final String getTreeNo()
    {
        return this.GetRequestVal("TreeNo");
    }
    /**
     获得可以操作的单据列表

     @return
     */
    public final String CCFrom_GenerFrmListOfCanOption() throws Exception
    {
        String sql = "";
        String userNo = GetRequestVal("UserNo");
        if (DataType.IsNullOrEmpty(userNo) == true)
        {
            userNo = WebUser.getNo();
        }
        String powerSQL = "SELECT FrmID," + "(CASE WHEN IsEnableAll=1 THEN true " + "ELSE(CASE WHEN IsEnableUser=1 AND INSTR(IDOfUsers,'," + userNo + ",')>0 THEN true " + "ELSE(CASE WHEN IsEnableStation=1 AND (SELECT COUNT(*) From Port_DeptEmpStation D,Port_Emp E WHERE D.FK_Emp = E.No AND E.No='" + userNo + "' AND INSTR(IDOfStations,D.FK_Station))>0 THEN true " + "ELSE(CASE WHEN IsEnableDept=1 AND (SELECT COUNT(*) From Port_DeptEmp D,Port_Emp E WHERE D.FK_Emp = E.No AND E.No='" + userNo + "' AND INSTR(IDOfDepts,D.FK_Dept))>0 THEN true " + "ELSE false END)" + "END)" + "END)" + "END) AS IsView   FROM Frm_CtrlModel WHERE CtrlObj='BtnSearch'";

        sql = "SELECT No,Name,EntityType,FrmType,PTable FROM Sys_MapData M, ("+ powerSQL+") AS B WHERE M.No=B.FrmID AND (M.EntityType=1 OR M.EntityType=2) AND B.IsView=1 ORDER BY M.IDX ";
        DataTable dt = DBAccess.RunSQLReturnTable(sql);
        if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
        {
            dt.Columns.get(0).ColumnName = "No";
            dt.Columns.get(1).ColumnName = "Name";
            dt.Columns.get(2).ColumnName = "EntityType";
            dt.Columns.get(3).ColumnName = "FrmType";
            dt.Columns.get(4).ColumnName = "PTable";
        }

        return BP.Tools.Json.ToJson(dt);
    }
    /**
     获得指定的目录下可以操作的单据列表

     @return
     */
    public final String CCFrom_GenerFrmListOfCanOptionBySpecTreeNo()
    {
        String treeNo = this.GetRequestVal("TreeNo");
        return null;
    }
    /**
     获得一个表单的操作权限

     @return
     */
    public final String CCFrom_FrmPower() throws Exception
    {
        java.util.Hashtable ht = new java.util.Hashtable();
        String frmID = this.getFrmID();
        CtrlModels ctrlMs = new CtrlModels();
        ctrlMs.Retrieve(CtrlModelAttr.FrmID, frmID);
        String userNo = GetRequestVal("UserNo");
        if (DataType.IsNullOrEmpty(userNo) == true)
        {
            userNo = WebUser.getNo();
        }
        for(CtrlModel ctrlM : ctrlMs.ToJavaList())
        {
            int isTrue = 0;
            if(ctrlM.getIsEnableAll() == true)
            {
                isTrue = 1;
            }
            else
            {
                //根据设置的权限来判断
                if (ctrlM.getIsEnableStation() == true)
                {
                    String stations = ctrlM.getIDOfStations();
                    stations = stations.substring(1);
                    stations = stations.substring(0,stations.length()-1);
                    stations = stations.replace(",", "','");
                    stations = "'" + stations + "'";
                    String sql = "SELECT * From Port_DeptEmpStation DES,Port_Emp E WHERE  E.No = DES.FK_Emp AND E.No='"+ userNo + "' AND DES.FK_Station IN(" + stations+")";
                    if (DBAccess.RunSQLReturnCOUNT(sql) > 1)
                    {
                        isTrue = 1;
                    }
                }

                if (ctrlM.getIsEnableUser() == true && isTrue == 0)
                {
                    String emps = ctrlM.getIDOfUsers();
                    if(emps.contains(","+ userNo + ",") == true)
                    {
                        isTrue = 1;
                    }
                }

                if (ctrlM.getIsEnableDept() == true && isTrue == 0)
                {
                    String depts = ctrlM.getIDOfDepts();
                    depts = depts.substring(1);
                    depts = depts.substring(0,depts.length()-1);
                    depts = depts.replace(",", "','");
                    depts = "'" + depts + "'";
                    String sql = "SELECT * From Port_DeptEmp D,Port_Emp E WHERE  E.No = D.FK_Emp AND E.No='" + userNo + "' AND D.FK_Dept IN(" + depts + ")";
                    if (DBAccess.RunSQLReturnCOUNT(sql) > 1)
                    {
                        isTrue = 1;
                    }
                }

            }

            if (ctrlM.getCtrlObj().equals("BtnNew") == true)
            {
                ht.put("IsInsert", isTrue);
            }
            if (ctrlM.getCtrlObj().equals("BtnSave") == true)
            {
                ht.put("IsSave", isTrue);
            }
            if (ctrlM.getCtrlObj().equals("BtnSubmit") == true)
            {
                ht.put("IsSubmit", isTrue);
            }
            if (ctrlM.getCtrlObj().equals("BtnSearch") == true)
            {
                ht.put("IsView", isTrue);
            }
            if (ctrlM.getCtrlObj().equals("BtnDelete") == true)
            {
                ht.put("IsDelete", isTrue);
            }
        }

        return BP.Tools.Json.ToJson(ht);
    }

    /**
     获取菜单列表

     @return
     */
    public final String CCForm_Power_ViewList()throws Exception
    {
        String userNo = GetRequestVal("UserNo");
        if (DataType.IsNullOrEmpty(userNo) == true)
        {
            userNo = WebUser.getNo();
        }
        String sql = "SELECT FrmID," + "(CASE WHEN IsEnableAll=1 THEN true " + "ELSE(CASE WHEN IsEnableUser=1 AND INSTR(IDOfUsers,'," + userNo + ",')>0 THEN true " + "ELSE(CASE WHEN IsEnableStation=1 AND (SELECT COUNT(*) From Port_DeptEmpStation D,Port_Emp E WHERE D.FK_Emp = E.No AND E.No='" + userNo + "' AND INSTR(IDOfStations,D.FK_Station))>0 THEN true " + "ELSE(CASE WHEN IsEnableDept=1 AND (SELECT COUNT(*) From Port_DeptEmp D,Port_Emp E WHERE D.FK_Emp = E.No AND E.No='" + userNo + "' AND INSTR(IDOfDepts,D.FK_Dept))>0 THEN true " + "ELSE false END)" + "END)" + "END)" + "END) AS IsView   FROM Frm_CtrlModel WHERE CtrlObj='BtnSearch'";
        DataTable dt = DBAccess.RunSQLReturnTable(sql);
        dt.TableName = "FrmView";
        return BP.Tools.Json.ToJson(dt);


    }

    /**
     删除实体根据BillNo

     @return
     */
    public final String CCFrom_DeleteFrmEntityByBillNo()throws Exception
    {

        GEEntity en = new GEEntity(this.getFrmID());
        int i= en.Retrieve("BillNo", this.GetRequestVal("BillNo"));
        if (i == 0)
        {
            return "err@单据编号为" + this.GetRequestVal("BillNo") + "的数据不存在.";
        }

        en.Delete();
        return "删除成功";
    }
    /**
     删除实体根据 OID

     @return
     */
    public final String CCFrom_DeleteFrmEntityByOID()throws Exception
    {
        GEEntity en = new GEEntity(this.getFrmID(),this.getOID());
        en.Delete();
        return "删除成功";
    }
    /**
     获得所有的单据、表单 @lizhen 转移代码.

     @return
     */
    public final String CCBillAdmin_Admin_GenerAllBills()
    {
        String sql = "";
        sql = "SELECT No,Name,EntityType,FrmType,PTable FROM Sys_MapData WHERE (EntityType=1 OR EntityType=2) ORDER BY IDX ";
        DataTable dt = DBAccess.RunSQLReturnTable(sql);
        if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
        {
            dt.Columns.get(0).ColumnName = "No";
            dt.Columns.get(1).ColumnName = "Name";
            dt.Columns.get(2).ColumnName = "EntityType";
            dt.Columns.get(3).ColumnName = "FrmType";
            dt.Columns.get(4).ColumnName = "PTable";
        }

        return BP.Tools.Json.ToJson(dt);
    }

}
