package bp.port;

import bp.da.AtPara;
import bp.da.DBAccess;
import bp.da.DataType;
import bp.wf.port.admin2group.OrgAdminer;

public class OrganizationAPI {
    public static String Port_Emp_Save(String orgNo, String userNo, String userName, String deptNo, String kvs, String stats) throws Exception {
        if (bp.web.WebUser.getIsAdmin() == false)
            return "0";
        if (bp.difference.SystemConfig.getCCBPMRunModel() == bp.sys.CCBPMRunModel.SAAS)
        {
            if (kvs == null || kvs.indexOf("@UserID=") == -1)
                return "err@saas模式下，需要在kvs参数里，增加@UserID=xxxx 属性.";
        }

        if (bp.difference.SystemConfig.getCCBPMRunModel() != bp.sys.CCBPMRunModel.Single)
        {
            if (DataType.IsNullOrEmpty(orgNo) == true)
                return "err@组织编号不能为空.";

            bp.wf.admin.Org org = new bp.wf.admin.Org();
            org.setNo(orgNo);
            if (org.RetrieveFromDBSources() == 0)
                return "err@组织编号错误:" + orgNo;
        }
        else
        {
            orgNo = "";
        }

        if (DataType.IsNullOrEmpty(userNo) || DataType.IsNullOrEmpty(userName) || DataType.IsNullOrEmpty(deptNo) == true)
            throw new Exception("err@用户编号，名称，部门不能为空.");

        bp.port.Dept dept = new bp.port.Dept();
        dept.setNo(deptNo);
        if (dept.RetrieveFromDBSources() == 0)
            throw new Exception("err@部门编号错误:" + deptNo);

        try
        {
            //增加人员信息.
            bp.port.Emp emp = new bp.port.Emp();
            emp.setNo(userNo);
            if (emp.RetrieveFromDBSources() == 0)
            {
                emp.setName(userName);
                emp.setFK_Dept( deptNo);
                emp.Insert();
            }

            bp.da.AtPara ap = new AtPara(kvs);
            for (String key : ap.getHisHT().keySet())
            {
                if (DataType.IsNullOrEmpty(key) == true)
                    continue;
                emp.SetValByKey(key, ap.GetValStrByKey(key));
            }
            emp.setFK_Dept( deptNo);
            emp.setName ( userName);
            emp.setOrgNo ( orgNo);
            emp.Update();

            if (bp.difference.SystemConfig.getCCBPMRunModel() == bp.sys.CCBPMRunModel.Single)
            {
                bp.da.DBAccess.RunSQL("DELETE FROM Port_DeptEmp WHERE FK_Emp='" + userNo + "'");
                bp.da.DBAccess.RunSQL("DELETE FROM Port_DeptEmpStation WHERE FK_Emp='" + userNo + "'");
            }
            else
            {
                bp.da.DBAccess.RunSQL("DELETE FROM Port_DeptEmp WHERE FK_Emp='" + userNo + "' AND OrgNo='" + orgNo + "'");
                bp.da.DBAccess.RunSQL("DELETE FROM Port_DeptEmpStation WHERE FK_Emp='" + userNo + "' AND OrgNo='" + orgNo + "'");
            }

            //插入部门.
            bp.port.DeptEmp de = new bp.port.DeptEmp();
            de.setFK_Dept( deptNo);
            de.setFK_Emp(userNo);
            de.setOrgNo(orgNo);
            //    de.IsMainDept = true;
            de.setMyPK(de.getFK_Dept() + "_" + userNo);
            de.DirectInsert();

            //更新岗位.
            if (stats == null)
                stats = "";
            String[] strs = stats.split(",");
            for (int i = 0; i < strs.length; i++)
            {
                String str = strs[i];
                if (DataType.IsNullOrEmpty(str))
                    continue;

                Station st = new Station();
                st.setNo(str);
                if (st.RetrieveFromDBSources() == 0)
                    throw new Exception("err@岗位编号错误." + str);

                //插入部门.
                DeptEmpStation des = new DeptEmpStation();
                des.setFK_Dept( deptNo);
                des.setFK_Emp(userNo);
                des.setFK_Station (str);
                des.setOrgNo(orgNo);
                des.setMyPK(de.getFK_Dept() + "_" + des.getFK_Emp() + "_" + des.getFK_Station());
                des.DirectInsert();
            }
            DBAccess.RunSQL("UPDATE Port_Emp SET OrgNo='" + orgNo + "' WHERE No='" + emp.getNo() + "'");
            return "1";
        }
        catch (Exception ex)
        {
            return "err@" + ex.getMessage();
        }
    }
    public static String Port_Emp_Delete(String orgNo, String userNo) throws Exception {

        if (bp.web.WebUser.getIsAdmin() == false)
            return "0";
        try
        {
            //增加人员信息.
            bp.port.Emp emp = new bp.port.Emp();
            emp.setNo(userNo);
            emp.setOrgNo(orgNo);
            if (emp.RetrieveFromDBSources() == 0)
                return "err@该用户【" + userNo + "】不存在.";

            //删除岗位.
            bp.da.DBAccess.RunSQL("delete from port_deptemp where fk_emp='" + userNo + "' AND OrgNo='" + orgNo + "'");
            bp.da.DBAccess.RunSQL("delete from port_deptempStation where fk_emp='" + userNo + "' AND OrgNo='" + orgNo + "'");
            emp.Delete();
            return "1";
        }
        catch (Exception ex)
        {
            return "err@" + ex.getMessage();
        }
    }
    public static String Port_Org_Save(String orgNo, String name, String adminer, String adminerName, String keyVals) throws Exception {

        if (bp.web.WebUser.getIsAdmin() == false)
            return "0";

        if (bp.difference.SystemConfig.getCCBPMRunModel() != bp.sys.CCBPMRunModel.Single) {
            AtPara ap = new AtPara(keyVals);
            bp.wf.port.admin2group.Org org = new bp.wf.port.admin2group.Org();
            org.setNo(orgNo);
            if(org.RetrieveFromDBSources()==0){
                org.setName(name);
                org.setAdminer(adminer);
                org.setAdminerName(adminerName);
                for (String key : ap.getHisHT().keySet())
                {
                    if (DataType.IsNullOrEmpty(key) == true)
                        continue;
                    org.SetValByKey(key, ap.GetValStrByKey(key));
                }
                org.Insert();
                org.DoCheck();
            }

            OrgAdminer oa = new OrgAdminer();
            oa.setMyPK(orgNo + "_" + adminer);
            if(oa.RetrieveFromDBSources()==0){
                oa.setOrgNo(orgNo);
                oa.setFK_Emp(adminer);
                oa.setEmpName(adminerName);
                oa.Insert();
            }
        }
        return "1";
    }
    /// 保存部门, 如果有此数据则修改，无此数据则增加.
    /// </summary>
    /// <param name="orgNo">组织编号</param>
    /// <param name="no">部门编号</param>
    /// <param name="name">名称</param>
    /// <param name="parntNo">父节点编号</param>
    /// <param name="keyval">比如：@Leaer=zhangsan@Tel=12233333@Idx=1</param>
    /// <returns>return 1 增加成功，其他的增加失败.</returns>
    public static String Port_Dept_Save(String orgNo, String no, String name, String parntNo, String keyVals) throws Exception {

        if (bp.web.WebUser.getIsAdmin() == false)
            return "0";

        if (bp.difference.SystemConfig.getCCBPMRunModel() != bp.sys.CCBPMRunModel.Single)
        {
            if (DataType.IsNullOrEmpty(orgNo) == true)
                return "err@组织编号不能为空.";

            bp.wf.admin.Org org = new bp.wf.admin.Org();
            org.setNo(orgNo);
            if (org.RetrieveFromDBSources() == 0)
                return "err@组织编号错误:" + orgNo;
        }


        try
        {
            //增加人员信息.
            bp.port.Dept deptP = new bp.port.Dept(parntNo);
            AtPara ap = new AtPara(keyVals);
            //增加部门.
            bp.port.Dept dept = new bp.port.Dept();
            dept.setNo(no);
            if (dept.RetrieveFromDBSources() == 0)
            {
                dept.setName(name);
                dept.setParentNo(parntNo);
                dept.setOrgNo( orgNo);

                for (String key : ap.getHisHT().keySet())
                {
                    if (DataType.IsNullOrEmpty(key) == true)
                        continue;
                    dept.SetValByKey(key, ap.GetValStrByKey(key));
                }
                dept.Insert();
            }
            else
            {
                dept.setName(name);
                dept.setParentNo(parntNo);
                dept.setOrgNo(orgNo);

                for (String key : ap.getHisHT().keySet())
                {
                    if (DataType.IsNullOrEmpty(key) == true)
                        continue;
                    dept.SetValByKey(key, ap.GetValStrByKey(key));
                }

                dept.Update();
            }
            DBAccess.RunSQL("UPDATE Port_Dept SET OrgNo='" + orgNo + "' WHERE No='" + dept.getNo() + "'");
            return "1";
        }
        catch (Exception ex)
        {
            return "err@" + ex.getMessage();
        }
    }
    public static String Port_Dept_Delete(String no) throws Exception {
        if (bp.web.WebUser.getIsAdmin() == false)
            return "0";
        try
        {
            //删除部门.
            bp.port.Dept dept = new bp.port.Dept(no);
            dept.Delete();

            return "1";
        }
        catch (Exception ex)
        {
            return "err@" + ex.getMessage();
        }
    }
    public static String Port_Station_Save(String orgNo, String no, String name, String keyVals) throws Exception {

        if (bp.web.WebUser.getIsAdmin() == false)
            return "0";

        if (bp.difference.SystemConfig.getCCBPMRunModel() != bp.sys.CCBPMRunModel.Single)
        {
            if (DataType.IsNullOrEmpty(orgNo) == true)
                return "err@组织编号不能为空.";

            bp.wf.admin.Org org = new bp.wf.admin.Org();
            org.setNo(orgNo);
            if (org.RetrieveFromDBSources() == 0)
                return "err@组织编号错误:" + orgNo;
        }

        try
        {
            AtPara ap = new AtPara(keyVals);

            //增加部门.
            bp.port.Station en = new bp.port.Station();
            en.setNo( no);
            if (en.RetrieveFromDBSources() == 0)
            {
                for (String item : ap.getHisHT().keySet())
                {
                    if (DataType.IsNullOrEmpty(item) == true)
                        continue;
                    en.SetValByKey(item, ap.GetValStrByKey(item));
                }
                en.setName(name);
                en.setOrgNo (orgNo);
                en.Insert();
            }

            en.setName(name);
            en.setOrgNo(orgNo);
            en.Update();
            DBAccess.RunSQL("UPDATE Port_Station SET OrgNo='" + orgNo + "' WHERE No='" + no + "'");
            return "1";
        }
        catch (Exception ex)
        {
            return "err@" + ex.getMessage();
        }
    }
    public static String Port_Station_Delete(String no)
    {
        if (bp.web.WebUser.getIsAdmin() == false)
            return "0";
        try{
            //删除部门.
            bp.port.Station dept = new bp.port.Station(no);
            dept.Delete();

            return "1";
        }
        catch (Exception ex)
        {
            return "err@" + ex.getMessage();
        }
    }
}
