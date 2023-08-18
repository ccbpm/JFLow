package bp.ccbill;

import bp.ccbill.template.Collection;
import bp.ccbill.template.Collections;
import bp.ccbill.template.Method;
import bp.da.*;
import bp.difference.handler.CommonFileUtils;
import bp.sys.*;
import bp.tools.DateUtils;
import bp.tools.Encodes;
import bp.tools.HttpClientUtil;
import bp.tools.Json;
import bp.web.*;
import bp.en.*;
import bp.en.Map;
import bp.wf.*;
import bp.ccfast.ccmenu.*;
import bp.difference.*;
import bp.ccbill.template.*;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.*;
import java.io.*;

/**
 * 页面功能实体
 */
public class WF_CCBill extends bp.difference.handler.DirectoryPageBase {

    ///#region 构造方法.

    /**
     * 方法ID
     */
    public final String getMethodID() {
        return this.GetRequestVal("MethodID");
    }

    /**
     * 方法编号
     */
    public final String getMethodNo() {
        return this.GetRequestVal("MethodNo");
    }

    /**
     * 构造函数
     */
    public WF_CCBill() {
    }

    ///#endregion 构造方法.


    ///#region 方法处理.
    public final String MyDict_DoBill_Start() throws Exception {
        //创建单据
        long workid = bp.ccbill.Dev2Interface.CreateBlankBillID(this.getFrmID());

        String workids = GetRequestVal("WorkIDs");
        if (DataType.IsNullOrEmpty(workids) == true) {
            return "err@请选择需要操作的行";
        }
        String fromFrmID = GetRequestVal("FromFrmID");

        ///#region 把实体表单的数据集合拷贝到单据从表数据中
        GEEntitys ens = new GEEntitys(fromFrmID);
        QueryObject qo = new QueryObject(ens);
        qo.AddWhereIn("OID", "(" + workids + ")");
        qo.DoQuery();
        GEDtl gedtl = null;
        String mapdtlNo = this.getFrmID() + "Dtl1";
        GEDtls gedtls = new GEDtls(mapdtlNo);
        gedtls.Retrieve(GEDtlAttr.RefPK, workid, null);
        for (GEEntity en : ens.ToJavaList()) {
            //先判断从表中是不是存在该实体数据，存在continue;
            if (gedtls.getIsExits("DictOID", en.getOID()) == true) {
                continue;
            }
            gedtl = new GEDtl(mapdtlNo);
            gedtl.Copy(en);
            gedtl.setRefPKInt64(workid);
            gedtl.SetValByKey("DictOID", en.getOID());
            gedtl.setOID(0);
            gedtl.Insert();
        }

        ///#endregion 把实体表单的数据集合拷贝到单据从表数据中

        return "./MyBill.htm?FrmID=" + this.getFrmID() + "&WorkID=" + workid;
    }

    public final String MyDict_DoFlowBatchBaseData_StartFlow() throws Exception {
        //创建工作.
        long workid = bp.wf.Dev2Interface.Node_CreateBlankWork(this.getFlowNo());

        String workids = GetRequestVal("WorkIDs");
        if (DataType.IsNullOrEmpty(workids) == true) {
            return "err@请选择需要操作的行";
        }
        String fromFrmID = GetRequestVal("FromFrmID");

        ///#region 把实体表单的数据集合拷贝到流程从表数据中
        GEEntitys ens = new GEEntitys(fromFrmID);
        QueryObject qo = new QueryObject(ens);
        qo.AddWhereIn("OID", "(" + workids + ")");
        qo.DoQuery();
        GEDtl gedtl = null;
        String mapdtlNo = "ND" + Integer.parseInt(this.getFlowNo()) + "01" + "Dtl1";
        GEDtls gedtls = new GEDtls(mapdtlNo);
        gedtls.Retrieve(GEDtlAttr.RefPK, workid, null);
        for (GEEntity en : ens.ToJavaList()) {
            //先判断从表中是不是存在该实体数据，存在continue;
            if (gedtls.getIsExits("DictOID", en.getOID()) == true) {
                continue;
            }
            gedtl = new GEDtl(mapdtlNo);
            gedtl.Copy(en);
            gedtl.setRefPKInt64(workid);
            gedtl.SetValByKey("DictOID", en.getOID());
            gedtl.setOID(0);
            gedtl.Insert();
        }

        ///#endregion 把实体表单的数据集合拷贝到单据从表数据中

        //更新标记, 表示:该流程被谁发起.
        GenerWorkFlow gwf = new GenerWorkFlow(workid);
        gwf.setPWorkID(this.getWorkID());
        gwf.setPFlowNo(fromFrmID);

        gwf.SetPara("FlowBaseData", "1"); //启动了修改基础资料流程..
        gwf.SetPara("MethodNo", this.getMethodNo()); //启动了修改基础资料流程..
        gwf.SetPara("DictFrmID", fromFrmID); //启动了修改基础资料流程..
        gwf.SetPara("DictWorkID", workids); //启动了修改基础资料流程..
        gwf.Update();

        //写日志.
        bp.ccbill.Dev2Interface.Dict_AddTrack(fromFrmID, "0", FrmActionType.StartFlow, "启动:" + gwf.getFlowName() + ",标题:" + gwf.getTitle());
        return "../MyFlow.htm?FK_Flow=" + this.getFlowNo() + "&WorkID=" + workid;
    }

    /**
     * 执行流程:变更基础资料
     *
     * @return
     */
    public final String MyDict_DoFlowBaseData_StartFlow() throws Exception {
        Method md = new Method(this.getMethodNo());

        GEEntity en = new GEEntity(md.getFrmID(), this.getWorkID());

        Hashtable ht = new Hashtable();

        Attrs attrs = en.getEnMap().getAttrs();
        for (Attr item : attrs) {
            if (item.getKey().equals("BillNo") == false && bp.wf.Glo.getFlowFields().contains("," + item.getKey() + ",") == true) {
                continue;
            }

            String val = en.GetValStrByKey(item.getKey());
            ht.put(item.getKey(), val);
            ht.put("bak" + item.getKey(), val);
        }

        //创建工作.
        long workid = bp.wf.Dev2Interface.Node_CreateBlankWork(md.getFlowNo(), ht);

        //更新标记, 表示:该流程被谁发起.
        GenerWorkFlow gwf = new GenerWorkFlow(workid);
        gwf.setPWorkID(this.getWorkID());
        gwf.setPFlowNo(md.getFrmID());

        gwf.SetPara("FlowBaseData", "1"); //启动了修改基础资料流程..
        gwf.SetPara("MethodNo", this.getMethodNo()); //启动了修改基础资料流程..
        gwf.SetPara("DictFrmID", md.getFrmID()); //启动了修改基础资料流程..
        gwf.SetPara("DictWorkID", this.getWorkID()); //启动了修改基础资料流程..
        gwf.Update();

        //写日志.
        bp.ccbill.Dev2Interface.Dict_AddTrack(md.getFrmID(), String.valueOf(this.getWorkID()), FrmActionType.StartFlow, "启动:" + gwf.getFlowName() + ",标题:" + gwf.getTitle(), null, md.getFlowNo(), md.getName(), Integer.parseInt(md.getFlowNo() + "01"), workid);
        return "../MyFlow.htm?FK_Flow=" + md.getFlowNo() + "&WorkID=" + workid;
    }

    /**
     * 发起其他业务流程
     *
     * @return
     */
    public final String MyDict_DoFlowEtc_StartFlow() throws Exception {
        Method md = new Method(this.getMethodNo());

        GEEntity en = new GEEntity(md.getFrmID(), this.getWorkID());

        Hashtable ht = new Hashtable();

        Attrs attrs = en.getEnMap().getAttrs();
        for (Attr item : attrs) {
            if (bp.wf.Glo.getFlowFields().contains("," + item.getKey() + ",") == true) {
                continue;
            }

            String val = en.GetValStrByKey(item.getKey());
            ht.put(item.getKey(), val);
        }

        //创建工作.
        long workid = bp.wf.Dev2Interface.Node_CreateBlankWork(md.getMethodID(), ht);

        //更新标记, 表示:该流程被谁发起.
        GenerWorkFlow gwf = new GenerWorkFlow(workid);
        gwf.setPWorkID(this.getWorkID());
        gwf.setPFlowNo(this.getFrmID());
        gwf.SetPara("DictFlowEtc", "1"); //启动了其他业务流程.
        gwf.Update();

        //写日志.
        bp.ccbill.Dev2Interface.Dict_AddTrack(md.getFrmID(), String.valueOf(this.getWorkID()), FrmActionType.StartFlow, "启动:" + gwf.getFlowName() + ",标题:" + gwf.getTitle(), null, md.getFlowNo(), md.getName(), Integer.parseInt(md.getFlowNo() + "01"), workid);
        return "../MyFlow.htm?FK_Flow=" + md.getFlowNo() + "&WorkID=" + workid;
    }

    ///#endregion

    /**
     * 发起列表.
     *
     * @return
     */
    public final String Start_Init() throws Exception {
        //获得发起列表.
        DataSet ds = bp.ccbill.Dev2Interface.DB_StartBills(WebUser.getNo());

        //返回组合
        return bp.tools.Json.ToJson(ds);
    }

    /**
     * 草稿列表
     *
     * @return
     */
    public final String Draft_Init() throws Exception {
        //草稿列表.
        DataTable dt = bp.ccbill.Dev2Interface.DB_Draft(this.getFrmID(), WebUser.getNo());

        //返回组合
        return bp.tools.Json.ToJson(dt);
    }

    /**
     * 单据初始化
     *
     * @return
     */
    public final String MyBill_Init() throws Exception {
        //获得发起列表.
        DataSet ds = bp.ccbill.Dev2Interface.DB_StartBills(WebUser.getNo());

        //返回组合
        return bp.tools.Json.ToJson(ds);
    }

    /**
     * 执行
     *
     * @return 返回执行结果
     */
    public final String DoMethod_ExeSQL() throws Exception {
        MethodFunc func = new MethodFunc(this.getMyPK());
        String doc = func.getMethodDocSQL();
        String workID = this.getWorkIDStr();
        if (DataType.IsNullOrEmpty(workID) == true) {
            //批量执行方法
            String workids = this.GetRequestVal("WorkIDs");
            if (DataType.IsNullOrEmpty(workids) == true) {
                throw new RuntimeException("err@执行方法获取到的WorkID或者WorkIDs不能为空");
            }
            String[] strs = workids.split("[,]", -1);
            workID = strs[0];
            doc = doc.replace("@WorkIDs", workids);
        }
        GEEntity en = new GEEntity(func.getFrmID(), workID);

        doc = bp.wf.Glo.DealExp(doc, en, null); //替换里面的内容.
        String sql = MidStrEx(doc, "/*", "*/");
        try {
            DBAccess.RunSQLs(sql);
            if (func.getMsgSuccess().equals("")) {
                func.setMsgSuccess("执行成功.");
            }

            bp.ccbill.Dev2Interface.Dict_AddTrack(this.getFrmID(), workID, "执行方法", func.getName());

            return func.getMsgSuccess();
        } catch (RuntimeException ex) {
            if (func.getMsgErr().equals("")) {
                func.setMsgErr("执行失败(DoMethod_ExeSQL).");
            }
            return "err@" + func.getMsgErr() + " @ " + ex.getMessage();
        }
    }

    public final String DoMethod_ExecFunc() throws Exception {
        MethodFunc func = new MethodFunc(this.getMyPK());
        String doc = func.getDocs();
        BuessUnitBase en = bp.sys.base.Glo.GetBuessUnitEntityByEnName(doc);
        try {
            String workID = this.getWorkIDStr();
            en.WorkID = Long.parseLong(workID);
            en.DoIt();

            bp.ccbill.Dev2Interface.Dict_AddTrack(this.getFrmID(), workID, "执行方法", func.getName());
            return func.getMsgSuccess();
        } catch (RuntimeException ex) {
            if (func.getMsgErr().equals("")) {
                func.setMsgErr("执行失败(DoMethod_ExecFunc).");
            }
            return "err@" + func.getMsgErr() + " @ " + ex.getMessage();
        }
    }

    /**
     * 执行SQL
     *
     * @return
     */
    public final String DoMethodPara_ExeSQL() throws Exception {
        MethodFunc func = new MethodFunc(this.getPKVal());
        String doc = func.getMethodDocSQL();
        String workID = this.getWorkIDStr();
        if (DataType.IsNullOrEmpty(workID) == true) {
            //批量执行方法
            String workids = this.GetRequestVal("WorkIDs");
            if (DataType.IsNullOrEmpty(workids) == true) {
                throw new RuntimeException("err@执行方法获取到的WorkID或者WorkIDs不能为空");
            }
            String[] strs = workids.split("[,]", -1);
            workID = strs[0];
            doc = doc.replace("@WorkIDs", workids);
        }
        GEEntity en = new GEEntity(func.getFrmID(), workID);


        ///#region 替换参数变量.
        if (doc.contains("@") == true) {
            MapAttrs mattrs = new MapAttrs();
            mattrs.Retrieve(MapAttrAttr.FK_MapData, this.getPKVal(), null);
            for (MapAttr item : mattrs.ToJavaList()) {
                if (doc.contains("@") == false) {
                    break;
                }
                if (item.getUIContralType() == UIContralType.TB) {
                    doc = doc.replace("@" + item.getKeyOfEn(), this.GetRequestVal("TB_" + item.getKeyOfEn()));
                    continue;
                }

                if (item.getUIContralType() == UIContralType.DDL) {
                    doc = doc.replace("@" + item.getKeyOfEn(), this.GetRequestVal("DDL_" + item.getKeyOfEn()));
                    continue;
                }


                if (item.getUIContralType() == UIContralType.CheckBok) {
                    doc = doc.replace("@" + item.getKeyOfEn(), this.GetRequestVal("CB_" + item.getKeyOfEn()));
                    continue;
                }

                if (item.getUIContralType() == UIContralType.RadioBtn) {
                    doc = doc.replace("@" + item.getKeyOfEn(), this.GetRequestVal("RB_" + item.getKeyOfEn()));
                    continue;
                }
            }
        }

        ///#endregion 替换参数变量.

        doc = bp.wf.Glo.DealExp(doc, en, null); //替换里面的内容.
        String sql = MidStrEx(doc, "/*", "*/");

        ///#region 开始执行SQLs.
        try {
            DBAccess.RunSQLs(sql);
            if (func.getMsgSuccess().equals("")) {
                func.setMsgSuccess("执行成功.");
            }

            return func.getMsgSuccess();
        } catch (RuntimeException ex) {
            if (func.getMsgErr().equals("")) {
                func.setMsgErr("执行失败.");
            }

            return "err@" + func.getMsgErr() + " @ " + ex.getMessage();
        }

        ///#endregion 开始执行SQLs.

    }

    /**
     * 执行url.
     *
     * @return
     */
    public final String DoMethodPara_ExeUrl() throws Exception {
        MethodFunc func = new MethodFunc(this.getPKVal());
        String doc = func.getMethodDocUrl();
        if (this.getWorkID() == 0) {
            //批量执行方法
            String workids = this.GetRequestVal("WorkIDs");
            if (DataType.IsNullOrEmpty(workids) == true) {
                throw new RuntimeException("err@执行方法获取到的WorkID或者WorkIDs不能为空");
            }
            String[] strs = workids.split("[,]", -1);
            this.setWorkID(Long.parseLong(strs[0]));
            doc = doc.replace("@WorkIDs", workids);
        }
        GEEntity en = new GEEntity(func.getFrmID(), this.getWorkID());


        ///#region 替换参数变量.
        if (doc.contains("@") == true) {
            MapAttrs mattrs = new MapAttrs();
            mattrs.Retrieve(MapAttrAttr.FK_MapData, this.getPKVal(), null);
            for (MapAttr item : mattrs.ToJavaList()) {
                if (doc.contains("@") == false) {
                    break;
                }
                if (item.getUIContralType() == UIContralType.TB) {
                    doc = doc.replace("@" + item.getKeyOfEn(), this.GetRequestVal("TB_" + item.getKeyOfEn()));
                    continue;
                }

                if (item.getUIContralType() == UIContralType.DDL) {
                    doc = doc.replace("@" + item.getKeyOfEn(), this.GetRequestVal("DDL_" + item.getKeyOfEn()));
                    continue;
                }


                if (item.getUIContralType() == UIContralType.CheckBok) {
                    doc = doc.replace("@" + item.getKeyOfEn(), this.GetRequestVal("CB_" + item.getKeyOfEn()));
                    continue;
                }

                if (item.getUIContralType() == UIContralType.RadioBtn) {
                    doc = doc.replace("@" + item.getKeyOfEn(), this.GetRequestVal("RB_" + item.getKeyOfEn()));
                    continue;
                }
            }
        }

        ///#endregion 替换参数变量.

        doc = bp.wf.Glo.DealExp(doc, en, null); //替换里面的内容.


        ///#region 开始执行SQLs.
        try {
            doc += "&MethodName=" + func.getMethodID();
            DataType.ReadURLContext(doc, 99999);
            if (func.getMsgSuccess().equals("")) {
                func.setMsgSuccess("执行成功.");
            }

            return func.getMsgSuccess();
        } catch (RuntimeException ex) {
            if (func.getMsgErr().equals("")) {
                func.setMsgErr("执行失败.");
            }

            return "err@" + func.getMsgErr() + " @ " + ex.getMessage();
        }

        ///#endregion 开始执行SQLs.

    }


    ///#region 单据处理.

    /**
     * 创建空白的WorkID.
     *
     * @return
     */
    public final String MyBill_CreateBlankBillID() throws Exception {
        String billNo = this.GetRequestVal("BillNo");
        String PFrmID = this.GetRequestVal("PFrmID");
        String PWorkID = this.GetRequestVal("PWorkID");
        if (DataType.IsNullOrEmpty(PWorkID) == true) {
            PWorkID = "0";
        }
        return String.valueOf(bp.ccbill.Dev2Interface.CreateBlankBillID(this.getFrmID(), WebUser.getNo(), null, PFrmID, Long.parseLong(PWorkID)));
    }

    /**
     * 创建空白的DictID.
     *
     * @return
     */
    public final String MyDict_CreateBlankDictID() throws Exception {
        return String.valueOf(bp.ccbill.Dev2Interface.CreateBlankDictID(this.getFrmID(), null, null));
    }

    /**
     * 执行保存
     *
     * @return
     */
    public final String MyBill_SaveIt() throws Exception {
        //创建entity 并执行copy方法.
        GEEntity rpt = new GEEntity(this.getFrmID(), this.getWorkID());
        Attrs attrs = rpt.getEnMap().getAttrs();

        //try
        //{
        //    //执行保存.
        //    rpt = BP.Pub.PubClass.CopyFromRequest(rpt) as GEEntity;
        //}
        //catch (Exception ex)
        //{
        //    return "err@方法：MyBill_SaveIt错误，在执行 CopyFromRequest 期间" + ex.Message;
        //}
        //执行copy ，这部分与上一个方法重复了.
        try {
            Hashtable ht = this.GetMainTableHT();
            for (Object item : ht.keySet()) {
                if (item == null)
                    continue;
                rpt.SetValByKey(item.toString(), ht.get(item));
            }
        } catch (RuntimeException ex) {
            return "err@方法：MyBill_SaveIt错误，在执行  GetMainTableHT 期间" + ex.getMessage();
        }
        //执行保存.
        try {
            rpt.setOID(this.getWorkID());
            rpt.SetValByKey("BillState", BillState.Editing.getValue());
            rpt.Update();
            String str = bp.ccbill.Dev2Interface.SaveBillWork(this.getFrmID(), this.getWorkID());
            return str;
        } catch (RuntimeException ex) {
            return "err@方法：MyBill_SaveIt 错误，在执行 SaveWork 期间出现错误:" + ex.getMessage();
        }
    }

    public final String MyBill_Submit() throws Exception {
        //执行保存.
        GEEntity rpt = new GEEntity(this.getFrmID(), this.getWorkID());
        Hashtable ht = GetMainTableHT();
        for (Object item : ht.keySet()) {
            if (item == null)
                continue;
            rpt.SetValByKey(item.toString(), ht.get(item));
        }

        rpt.setOID(this.getWorkID());
        rpt.SetValByKey("BillState", BillState.Over.getValue());
        rpt.Update();

        String str = bp.ccbill.Dev2Interface.SubmitWork(this.getFrmID(), this.getWorkID());
        return str;
    }

    /**
     * 执行保存
     *
     * @return
     */
    public final String MyDict_SaveIt() throws Exception {
        //执行保存.
        MapData md = new MapData(this.getFrmID());
        GEEntity rpt = new GEEntity(this.getFrmID(), this.getWorkID());
        //rpt = BP.Pub.PubClass.CopyFromRequest(rpt) as GEEntity;
        Hashtable ht = GetMainTableHT();
        for (Object item : ht.keySet()) {
            if (item == null)
                continue;
            rpt.SetValByKey(item.toString(), ht.get(item));
        }

        //执行保存前事件
        ExecEvent.DoFrm(md, EventListFrm.SaveBefore, rpt, null);

        rpt.setOID(this.getWorkID());
        rpt.SetValByKey("BillState", 100);
        rpt.Update();


        //执行保存后事件
        ExecEvent.DoFrm(md, EventListFrm.SaveAfter, rpt, null);
        return "保存成功.";
    }

    /**
     * 执行保存
     *
     * @return
     */
    public final String MyDict_Submit() throws Exception {
        return "err@不在支持提交功能.";
        //  throw new Exception("dddssds");
        //执行保存.
		/*MapData md = new MapData(this.getFrmID());
		GEEntity rpt = new GEEntity(this.getFrmID(), this.getWorkID());
		//rpt = BP.Pub.PubClass.CopyFromRequest(rpt) as GEEntity;

		Hashtable ht = GetMainTableHT();
		for (Object item : ht.keySet())
		{
			if(item==null)
				continue;
			rpt.SetValByKey(item.toString(), ht.get(item));
		}

		//执行保存前事件
		ExecEvent.DoFrm(md, EventListFrm.SaveBefore, rpt, null);

		rpt.setOID(this.getWorkID());
		rpt.SetValByKey("BillState", BillState.Over.getValue());
		rpt.Update();

		//执行保存后事件
		ExecEvent.DoFrm(md, EventListFrm.SaveAfter, rpt, null);
		return "提交";*/
    }

    public final String GetFrmEntitys() throws Exception {
        GEEntitys rpts = new GEEntitys(this.getFrmID());
        QueryObject qo = new QueryObject(rpts);
        qo.AddWhere("BillState", " != ", 0);
        qo.DoQuery();
        return bp.tools.Json.ToJson(rpts.ToDataTableField("dt"));
    }

    private Hashtable GetMainTableHT() throws Exception {
        Hashtable htMain = new Hashtable();
        for (String key : ContextHolderUtils.getRequest().getParameterMap().keySet()) {
            if (key == null) {
                continue;
            }

            String myKey = key;
            String val = ContextHolderUtils.getRequest().getParameter(key);
            myKey = myKey.replace("TB_", "");
            myKey = myKey.replace("DDL_", "");
            myKey = myKey.replace("CB_", "");
            myKey = myKey.replace("RB_", "");
            val = URLDecoder.decode(val, "UTF-8");

            if (htMain.containsKey(myKey) == true) {
                htMain.put(myKey, val);
            } else {
                htMain.put(myKey, val);
            }
        }

        return htMain;
    }

    public final String MyBill_SaveAsDraft() throws Exception {
        String str = bp.ccbill.Dev2Interface.SaveBillWork(this.getFrmID(), this.getWorkID());
        return str;
    }

    //删除单据
    public final String MyBill_Delete() throws Exception {
        return bp.ccbill.Dev2Interface.MyBill_Delete(this.getFrmID(), this.getWorkID());
    }

    public final String MyBill_Deletes() throws Exception {
        return bp.ccbill.Dev2Interface.MyBill_DeleteBills(this.getFrmID(), this.GetRequestVal("WorkIDs"));
    }


    //删除实体
    public final String MyDict_Delete() throws Exception {
        return bp.ccbill.Dev2Interface.MyDict_Delete(this.getFrmID(), this.getWorkID());
    }

    /**
     * 删除多个
     *
     * @return
     */
    public final String MyDict_Deletes() throws Exception {
        return bp.ccbill.Dev2Interface.MyDict_DeleteDicts(this.getFrmID(), this.GetRequestVal("WorkIDs"));
    }

    public final String MyEntityTree_Delete() throws Exception {
        return bp.ccbill.Dev2Interface.MyEntityTree_Delete(this.getFrmID(), this.GetRequestVal("BillNo"));
    }

    /**
     * 复制单据数据
     *
     * @return
     */
    public final String MyBill_Copy() throws Exception {
        return bp.ccbill.Dev2Interface.MyBill_Copy(this.getFrmID(), this.getWorkID());
    }

    ///#endregion 单据处理.


    ///#region 获取查询条件
    public final String Search_ToolBar() throws Exception {
        DataSet ds = new DataSet();

        DataTable dt = new DataTable();

        //根据FrmID获取Mapdata
        MapData md = new MapData(this.getFrmID());
        if (md.getEntityType() == EntityType.DBList) {
            DBListDBSrc dbList = new DBListDBSrc(this.getFrmID());
            ds.Tables.add(dbList.ToDataTableField("Sys_DBList"));
        }
        //如果设置按照时间字段的月度，季度，年度查询数据，需要查询数据显示的最小年份
        if (md.getDTSearchWay() != DTSearchWay.None && md.GetParaInt("DTShowWay", 0) == 1) {
            GEEntity en = new GEEntity(this.getFrmID());
            try {
                String sql = "SELECT min(" + md.getDTSearchWay() + ") From " + en.getEnMap().getPhysicsTable();
                md.SetPara("DateShowYear", DBAccess.RunSQLReturnStringIsNull(sql, ""));
            } catch (RuntimeException e) {
                GEEntity rpt = new GEEntity(this.getFrmID());
                rpt.CheckPhysicsTable();
                String sql = "SELECT min(" + md.getDTSearchWay() + ") From " + en.getEnMap().getPhysicsTable();
                md.SetPara("DateShowYear", DBAccess.RunSQLReturnStringIsNull(sql, ""));
            }

        }
        ds.Tables.add(md.ToDataTableField("Sys_MapData"));
        //获取字段属性
        MapAttrs attrs = new MapAttrs(this.getFrmID());


        ///#region //增加枚举/外键字段信息
        dt.Columns.Add("Field", String.class);
        dt.Columns.Add("Name", String.class);
        dt.Columns.Add("Width", Integer.class);
        dt.TableName = "Attrs";
        //dt.PrimaryKey = new DataColumn[] {dt.Columns.get("Field")};
        ds.Tables.add(dt);
        String[] ctrls = md.getRptSearchKeys().split("[*]", -1);
        DataTable dtNoName = null;

        MapAttr mapattr;
        DataRow dr = null;
        MapExts mapExts = new MapExts();
        QueryObject qo = new QueryObject(mapExts);
        qo.AddWhere("FK_MapData", this.getFrmID());
        qo.addAnd();
        qo.AddWhereIn("ExtType", "('ActiveDDLSearchCond','AutoFullDLLSearchCond')");
        qo.DoQuery();
        ds.Tables.add(mapExts.ToDataTableField("Sys_MapExt"));
        for (String ctrl : ctrls) {
            //增加判断，如果URL中有传参，则不进行此SearchAttr的过滤条件显示
            if (DataType.IsNullOrEmpty(ctrl) || !DataType.IsNullOrEmpty(this.GetRequestVal(ctrl))) {
                continue;
            }

            Object tempVar = attrs.GetEntityByKey(MapAttrAttr.KeyOfEn, ctrl);
            mapattr = tempVar instanceof MapAttr ? (MapAttr) tempVar : null;
            if (mapattr == null) {
                continue;
            }

            dr = dt.NewRow();
            dr.setValue("Field", mapattr.getKeyOfEn());
            dr.setValue("Name", mapattr.getName());
            dr.setValue("Width", mapattr.getUIWidth());
            dt.Rows.add(dr);

            Attr attr = mapattr.getHisAttr();
            if (mapattr == null) {
                continue;
            }

            if (attr.getKey().equals("FK_Dept")) {
                continue;
            }

            if (attr.getItIsEnum() == true) {
                SysEnums ses = new SysEnums(mapattr.getUIBindKey());
                DataTable dtEnum = ses.ToDataTableField("dt");
                dtEnum.TableName = mapattr.getKeyOfEn();
                ds.Tables.add(dtEnum);
                continue;
            }
            if (attr.getItIsFK() == true) {
                Entities ensFK = attr.getHisFKEns();
                if (ensFK != null) {
                    ensFK.RetrieveAll();
                    DataTable dtEn = ensFK.ToDataTableField("dt");
                    dtEn.TableName = attr.getKey();
                    ds.Tables.add(dtEn);
                }


            }
            //绑定SQL的外键
            if (ds.contains(attr.getKey()) == false) {
                DataTable dtSQl = null;
                Object tempVar2 = mapExts.GetEntityByKey(MapExtAttr.ExtType, MapExtXmlList.AutoFullDLLSearchCond, MapExtAttr.AttrOfOper, attr.getKey());
                MapExt mapExt = tempVar2 instanceof MapExt ? (MapExt) tempVar2 : null;
                if (mapExt != null) {
                    Object tempVar3 = mapExt.getDoc();
                    String fullSQL = tempVar3 instanceof String ? (String) tempVar3 : null;
                    if (fullSQL == null) {
                        throw new RuntimeException("err@字段[" + attr.getKey() + "]下拉框AutoFullDLLSearchCond，没有配置SQL");
                    }

                    fullSQL = fullSQL.replace("~", "'");
                    fullSQL = bp.wf.Glo.DealExp(fullSQL, null, null);
                    dtSQl = DBAccess.RunSQLReturnTable(fullSQL);
                } else if (DataType.IsNullOrEmpty(attr.getUIBindKey()) == false) {
                    dtSQl = bp.pub.PubClass.GetDataTableByUIBineKey(attr.getUIBindKey(), null);
                }
                if (dtSQl != null) {
                    for (DataColumn col : dtSQl.Columns) {
                        String colName = col.ColumnName.toLowerCase();
                        switch (colName) {
                            case "no":
                            case "NO":
                                col.ColumnName = "No";
                                break;
                            case "name":
                            case "NAME":
                                col.ColumnName = "Name";
                                break;
                            case "parentno":
                            case "PARENTNO":
                                col.ColumnName = "ParentNo";
                                break;
                            default:
                                break;
                        }
                    }
                    dtSQl.TableName = attr.getKey();
                    ds.Tables.add(dtSQl);
                }
            }

        }


        //数据查询权限除只查看自己创建的数据外增加部门的查询条件
        SearchDataRole searchDataRole = SearchDataRole.forValue(md.GetParaInt("SearchDataRole", 0));
        if (searchDataRole != SearchDataRole.ByOnlySelf) {
            DataTable dd = GetDeptDataTable(searchDataRole, md);
            if (dd.Rows.size() == 0 && md.GetParaInt("SearchDataRoleByDeptStation", 0) == 1) {
                dd = GetDeptAndSubLevel();
            }
            if (dd.Rows.size() != 0) {
                //增加部门的查询条件
                if (dt.Rows.contains("FK_Dept") == false) {
                    dr = dt.NewRow();
                    dr.setValue("Field", "FK_Dept");
                    dr.setValue("Name", "部门");
                    dr.setValue("Width", 120);
                    dt.Rows.add(dr);
                }

                dd.TableName = "FK_Dept";
                ds.Tables.add(dd);

            }
        }
        Methods methods = new Methods();
        //实体类方法
        try {
            methods.Retrieve(MethodAttr.FrmID, this.getFrmID(), MethodAttr.IsSearchBar, 1, MethodAttr.Idx);
        } catch (RuntimeException e) {
            methods.getNewEntity().CheckPhysicsTable();
            methods.Retrieve(MethodAttr.FrmID, this.getFrmID(), MethodAttr.IsSearchBar, 1, MethodAttr.IsEnable, 1, MethodAttr.Idx);

        }
        ds.Tables.add(methods.ToDataTableField("Frm_Method"));


        Collections colls = Search_BtnPower();
        ds.Tables.add(methods.ToDataTableField("ToolBtns"));
        ds.Tables.add(colls.ToDataTableField("Frm_Collection"));

        return bp.tools.Json.ToJson(ds);

    }

    ///#endregion 查询条件

    private DataTable GetDeptDataTable(SearchDataRole searchDataRole, MapData md) throws Exception {
        //增加部门的外键
        DataTable dt = new DataTable();
        String sql = "";
        if (searchDataRole == SearchDataRole.ByDept) {
            sql = "SELECT D.No,D.Name From Port_Dept D,Port_DeptEmp E WHERE D.No=E.FK_Dept AND E.FK_Emp='" + WebUser.getNo() + "'";
            dt = DBAccess.RunSQLReturnTable(sql);
        }
        if (searchDataRole == SearchDataRole.ByDeptAndSSubLevel) {
            dt = GetDeptAndSubLevel();
        }
        if (searchDataRole == SearchDataRole.ByStationDept) {
            sql = "SELECT D.No,D.Name From Port_Dept D WHERE D.No IN(SELECT F.FK_Dept FROM Frm_StationDept F,Port_DeptEmpStation P Where F.FK_Station = P.FK_Station AND F.FK_Frm='" + md.getNo() + "' AND P.FK_Emp='" + WebUser.getUserID() + "')";
            dt = DBAccess.RunSQLReturnTable(sql);
        }
        for (DataColumn col : dt.Columns) {
            String colName = col.ColumnName.toLowerCase();
            switch (colName) {
                case "no":
                    col.ColumnName = "No";
                    break;
                case "name":
                    col.ColumnName = "Name";
                    break;

                default:
                    break;
            }

        }
        return dt;
    }

    private DataTable GetDeptAndSubLevel() {
        //获取本部门和兼职部门
        String sql = "SELECT D.No,D.Name From Port_Dept D,Port_DeptEmp E WHERE D.No=E.FK_Dept AND E.FK_Emp='" + WebUser.getNo() + "'";
        DataTable dt = DBAccess.RunSQLReturnTable(sql);
        //dt.PrimaryKey = new DataColumn[] {dt.Columns.get("No"]};
        DataTable dd = dt.copy();
        for (DataRow dr : dd.Rows) {
            GetSubLevelDeptByParentNo(dt, dr.getValue(0).toString());
        }
        return dt;
    }

    private void GetSubLevelDeptByParentNo(DataTable dt, String parentNo) {
        String sql = "SELECT No,Name FROM Port_Dept Where ParentNo='" + parentNo + "'";
        DataTable dd = DBAccess.RunSQLReturnTable(sql);

        for (DataRow dr : dd.Rows) {
            if (dt.Rows.contains(dr.getValue(0).toString()) == true) {
                continue;
            }
            dt.Rows.add(dr);

            GetSubLevelDeptByParentNo(dt, dr.getValue(0).toString());

        }
    }

    public final String Search_TreeData() throws Exception {
        MapData mapData = new MapData(this.getFrmID());
        int listShowWay = mapData.GetParaInt("ListShowWay", 0);
        String listShowWayKey = mapData.GetParaString("ListShowWayKey");
        if (DataType.IsNullOrEmpty(listShowWayKey) == true) {
            return "err@树形结构展示的字段不存在，请检查查询条件设置中展示方式配置是否正确";
        }
        MapAttr mapAttr = new MapAttr(this.getFrmID() + "_" + listShowWayKey);
        //获取绑定的数据源
        if (DataType.IsNullOrEmpty(mapAttr.getUIBindKey()) == true) {
            return "err@字段" + mapAttr.getName() + "绑定的外键或者外部数据源不存在,请检查字段属性[外键SFTable]是否为空";
        }
        DataTable dt = bp.pub.PubClass.GetDataTableByUIBineKey(mapAttr.getUIBindKey(), null);
        return bp.tools.Json.ToJson(dt);

    }

    /**
     * 实体、单据列表显示的字段
     *
     * @return
     */
    public final String Search_MapAttr() throws Exception {
        DBList dblist = new DBList(this.getFrmID());
        if (dblist.getEntityType() == EntityType.DBList) {
            return Search_MapAttrForDB();
        }

        ///#region 查询显示的列
        MapAttrs mattrs = new MapAttrs();
        mattrs.Retrieve(MapAttrAttr.FK_MapData, this.getFrmID(), MapAttrAttr.Idx);

        MapExts mapExts = new MapExts();
        QueryObject qo = new QueryObject(mapExts);
        qo.AddWhere(MapExtAttr.FK_MapData, this.getFrmID());
        qo.addAnd();
        qo.AddWhereIn(MapExtAttr.ExtType, "('MultipleChoiceSmall','SingleChoiceSmall')");
        qo.DoQuery();
        for (MapExt mapExt : mapExts.ToJavaList()) {
            //获取mapAttr
            Object tempVar = mattrs.GetEntityByKey(this.getFrmID() + "_" + mapExt.getAttrOfOper());
            MapAttr mapAttr = tempVar instanceof MapAttr ? (MapAttr) tempVar : null;
            String searchVisable = mapAttr.getatPara().GetValStrByKey("SearchVisable");
            if (Objects.equals(searchVisable, "0")) {
                continue;
            }
            mapAttr.SetPara("SearchVisable", 0);
            mapAttr.Update();
            Object tempVar2 = mattrs.GetEntityByKey(this.getFrmID() + "_" + mapExt.getAttrOfOper() + "T");
            mapAttr = tempVar2 instanceof MapAttr ? (MapAttr) tempVar2 : null;
            mapAttr.SetPara("SearchVisable", 1);
            mapAttr.Update();
        }
        DataRow row = null;
        DataTable dt = new DataTable("Attrs");
        dt.Columns.Add("KeyOfEn", String.class);
        dt.Columns.Add("Name", String.class);
        dt.Columns.Add("Width", Integer.class);
        dt.Columns.Add("UIContralType", Integer.class);
        dt.Columns.Add("LGType", Integer.class);
        dt.Columns.Add("MyDataType", Integer.class);
        dt.Columns.Add("UIBindKey", String.class);
        dt.Columns.Add("AtPara", String.class);
        dt.Columns.Add("IsRichText", Integer.class);

        //设置标题、单据号位于开始位置
        for (MapAttr attr : mattrs.ToJavaList()) {
            String searchVisable = attr.getatPara().GetValStrByKey("SearchVisable");
            if (Objects.equals(searchVisable, "0")) {
                continue;
            }
            if (DataType.IsNullOrEmpty(searchVisable) == true && attr.getUIVisible() == false) {
                continue;
            }
            row = dt.NewRow();
            row.setValue("KeyOfEn", attr.getKeyOfEn());
            row.setValue("Name", attr.getName());
            row.setValue("Width", attr.getUIWidthInt());
            row.setValue("UIContralType", attr.getUIContralType().getValue());
            row.setValue("LGType", attr.getLGType().getValue());
            row.setValue("MyDataType", attr.getMyDataType());
            row.setValue("UIBindKey", attr.getUIBindKey());
            row.setValue("AtPara", attr.GetValStringByKey("AtPara"));
            row.setValue("IsRichText", attr.getTextModel() == 3 ? 1 : 0);
            dt.Rows.add(row);
        }


        ///#endregion 查询显示的列
        DataSet ds = new DataSet();
        ds.Tables.add(dt);
        //增加枚举
        MapData mapData = new MapData(this.getFrmID());
        ds.Tables.add(mapData.getSysEnums().ToDataTableField("Sys_Enum"));
        //查询一行数据的操作
        Methods methods = new Methods();
        methods.Retrieve(MethodAttr.FrmID, this.getFrmID(), MethodAttr.IsList, 1, MethodAttr.Idx);

        ds.Tables.add(methods.ToDataTableField("Frm_Method"));

        return bp.tools.Json.ToJson(ds);
    }

    /**
     * 获取查询列表的按钮权限
     *
     * @return
     */
    public final Collections Search_BtnPower() throws Exception {
        //获取该表单所有操作按钮的权限
        Collections colls = new Collections();
        colls.Retrieve(CollectionAttr.FrmID, this.getFrmID(), CollectionAttr.IsEnable, 1, "Idx");
        if (colls.size() == 0) {
            //查询
            Collection collection = new Collection();
            collection.setFrmID(this.getFrmID());
            collection.setMethodID("Search");
            collection.setName("查询");
            collection.setMethodModel("Search");
            collection.setMark("Search");
            collection.setNo(collection.getFrmID() + "_" + collection.getMethodID());
            collection.SetValByKey("Idx", 0);
            collection.Insert();

            //新建
            collection = new Collection();
            collection.setFrmID(this.getFrmID());
            collection.setMethodID("New");
            collection.setName("新建");
            collection.setMethodModel("New");
            collection.setMark("New");
            collection.setNo(collection.getFrmID() + "_" + collection.getMethodID());
            collection.SetValByKey("Idx", 1);
            collection.Insert();

            //删除
            collection = new Collection();
            collection.setFrmID(this.getFrmID());
            collection.setMethodID("Delete");
            collection.setName("删除");
            collection.setMethodModel("Delete");
            collection.setMark("Delete");
            collection.setNo(collection.getFrmID() + "_" + collection.getMethodID());
            collection.SetValByKey("Idx", 2);
            collection.Insert();

            collection = new Collection();
            collection.setFrmID(this.getFrmID());
            collection.setMethodID("Group");
            collection.setName("分析");
            collection.setMethodModel("Group");
            collection.setMark("Group");
            collection.setNo(collection.getFrmID() + "_" + collection.getMethodID());
            collection.SetValByKey("Idx", 3);
            collection.SetValByKey("IsEnable", false);
            collection.Insert();

            //导出
            collection = new Collection();
            collection.setFrmID(this.getFrmID());
            collection.setMethodID("ExpExcel");
            collection.setName("导出Excel");
            collection.setMethodModel("ExpExcel");
            collection.setMark("ExpExcel");
            collection.setNo(collection.getFrmID() + "_" + collection.getMethodID());
            collection.SetValByKey("Idx", 4);
            collection.Insert();

            //导入
            collection = new Collection();
            collection.setFrmID(this.getFrmID());
            collection.setMethodID("ImpExcel");
            collection.setName("导入Excel");
            collection.setMethodModel("ImpExcel");
            collection.setMark("ImpExcel");
            collection.setNo(collection.getFrmID() + "_" + collection.getMethodID());
            collection.SetValByKey("Idx", 5);
            collection.Insert();
            colls.Retrieve(GroupMethodAttr.FrmID, this.getFrmID(), "Idx");
            return colls;


        }

        //获取针对按钮设置的操作权限
        PowerCenters pcs = new PowerCenters();
        pcs.Retrieve(PowerCenterAttr.CtrlObj, this.getFrmID(), PowerCenterAttr.CtrlGroup, "SearchBtn", null);

        String mydepts = "" + WebUser.getDeptNo() + ","; //我的部门.
        String mystas = ""; //我的角色.

        DataTable mydeptsDT = DBAccess.RunSQLReturnTable("SELECT FK_Dept,FK_Station FROM Port_DeptEmpStation WHERE FK_Emp='" + WebUser.getUserID() + "'");
        for (DataRow dr : mydeptsDT.Rows) {
            mydepts += dr.getValue(0).toString() + ",";
            mystas += dr.getValue(1).toString() + ",";
        }

        Collections newCollections = new Collections();
        String empIds = "";
        for (Collection collection : colls.ToJavaList()) {
            //找到关于系统的控制权限集合.
            bp.en.Entities tempVar = pcs.GetEntitiesByKey(PowerCenterAttr.CtrlPKVal, collection.getNo());
            PowerCenters mypcs = tempVar instanceof PowerCenters ? (PowerCenters) tempVar : null;
            //如果没有权限控制的描述，就默认有权限.
            if (mypcs == null) {
                newCollections.AddEntity(collection);
                continue;
            }

            //控制遍历权限.
            for (PowerCenter pc : mypcs.ToJavaList()) {
                if (pc.getCtrlModel().equals("Anyone") == true) {
                    newCollections.AddEntity(collection);
                    break;
                }
                if (pc.getCtrlModel().equals("Adminer") == true && WebUser.getNo().equals("admin") == true) {
                    newCollections.AddEntity(collection);
                    break;
                }

                if (pc.getCtrlModel().equals("AdminerAndAdmin2") == true && WebUser.getIsAdmin() == true) {
                    newCollections.AddEntity(collection);
                    break;
                }
                empIds = "," + pc.getIDs() + ",";
                if (pc.getCtrlModel().equals("Emps") == true && empIds.contains("," + WebUser.getNo() + ",") == true) {
                    newCollections.AddEntity(collection);
                    break;
                }

                //是否包含部门？
                if (pc.getCtrlModel().equals("Depts") == true && DataType.IsHaveIt(pc.getIDs(), mydepts) == true) {
                    newCollections.AddEntity(collection);
                    break;
                }

                //是否包含角色？
                if (pc.getCtrlModel().equals("Stations") == true && DataType.IsHaveIt(pc.getIDs(), mystas) == true) {
                    newCollections.AddEntity(collection);
                    break;
                }

                //SQL？
                if (pc.getCtrlModel().equals("SQL") == true) {
                    String sql = bp.wf.Glo.DealExp(pc.getIDs(), null, "");
                    if (DBAccess.RunSQLReturnValFloat(sql) > 0) {
                        newCollections.AddEntity(collection);
                    }
                    break;
                }
            }
        }
        return newCollections;
    }

    /**
     * 获取数据源实体列表显示的列及操作列方法
     *
     * @return
     */
    public final String Search_MapAttrForDB() throws Exception {
        DBList dblist = new DBList(this.getFrmID());

        ///#region 查询显示的列
        MapAttrs mapattrs = new MapAttrs();
        mapattrs.Retrieve(MapAttrAttr.FK_MapData, this.getFrmID(), MapAttrAttr.Idx);

        //查询列表数据源显示的列
        if (DataType.IsNullOrEmpty(dblist.getExpList()) == true) {
            return "err@数据源实体的列表数据源不能为空，请联系设计人员，检查错误原因.";
        }

        //查询结果集返回的字段列表
        DataTable listDT = null;
        String explist = dblist.getExpList();
        //替换
        if ((new Integer(dblist.getDBType())).equals("local")) {

            if (explist.toUpperCase().contains("WHERE") == false) {
                explist += " WHERE 1=2";
            } else {
                explist += " AND 1=2";
            }
            if (DataType.IsNullOrEmpty(dblist.getDBSrc()) == true) {
                dblist.setDBSrc("local");
            }
            explist = bp.wf.Glo.DealExp(explist, null);
            SFDBSrc dbSrc = new SFDBSrc(dblist.getDBSrc());
            listDT = dbSrc.RunSQLReturnTable(explist);
        }


        DataRow row = null;
        DataTable dt = new DataTable("Attrs");
        dt.Columns.Add("KeyOfEn", String.class);
        dt.Columns.Add("Name", String.class);
        dt.Columns.Add("Width", Integer.class);
        dt.Columns.Add("UIContralType", Integer.class);
        dt.Columns.Add("LGType", Integer.class);
        dt.Columns.Add("MyDataType", Integer.class);
        dt.Columns.Add("UIBindKey", String.class);
        dt.Columns.Add("AtPara", String.class);
        if (listDT == null) {
            for (MapAttr attr : mapattrs.ToJavaList()) {
                String searchVisable = attr.getatPara().GetValStrByKey("SearchVisable");
                if (Objects.equals(searchVisable, "0")) {
                    continue;
                }
                if (DataType.IsNullOrEmpty(searchVisable) == true && attr.getUIVisible() == false) {
                    continue;
                }
                row = dt.NewRow();
                row.setValue("KeyOfEn", attr.getKeyOfEn());
                row.setValue("Name", attr.getName());
                row.setValue("Width", attr.getUIWidthInt());
                row.setValue("UIContralType", attr.getUIContralType().getValue());
                row.setValue("LGType", attr.getLGType().getValue());
                row.setValue("MyDataType", attr.getMyDataType());
                row.setValue("UIBindKey", attr.getUIBindKey());
                row.setValue("AtPara", attr.GetValStringByKey("AtPara"));
                dt.Rows.add(row);
            }
        } else {
            //设置标题、单据号位于开始位置
            for (DataColumn col : listDT.Columns) {
                //获取key
                String key = col.ColumnName;
                if (DataType.IsNullOrEmpty(key) == true) {
                    continue;
                }
                Object tempVar = mapattrs.GetEntityByKey(this.getFrmID() + "_" + key);
                MapAttr attr = tempVar instanceof MapAttr ? (MapAttr) tempVar : null;
                row = dt.NewRow();
                if (attr == null) {
                    row.setValue("KeyOfEn", key);
                    row.setValue("Name", key);
                    row.setValue("Width", 120);
                    row.setValue("UIContralType", UIContralType.TB.getValue());
                    row.setValue("LGType", FieldTypeS.Normal.getValue());
                    row.setValue("MyDataType", DataType.AppString);
                    row.setValue("UIBindKey", "");
                    row.setValue("AtPara", "");
                    dt.Rows.add(row);
                    continue;
                }
                String searchVisable = attr.getatPara().GetValStrByKey("SearchVisable");
                if (Objects.equals(searchVisable, "0")) {
                    continue;
                }
                if (DataType.IsNullOrEmpty(searchVisable) == true && attr.getUIVisible() == false) {
                    continue;
                }
                row.setValue("KeyOfEn", attr.getKeyOfEn());
                row.setValue("Name", attr.getName());
                row.setValue("Width", attr.getUIWidthInt());
                row.setValue("UIContralType", attr.getUIContralType().getValue());
                row.setValue("LGType", attr.getLGType().getValue());
                row.setValue("MyDataType", attr.getMyDataType());
                row.setValue("UIBindKey", attr.getUIBindKey());
                row.setValue("AtPara", attr.GetValStringByKey("AtPara"));
                dt.Rows.add(row);
            }

        }


        ///#endregion 查询显示的列
        DataSet ds = new DataSet();
        ds.Tables.add(dt);
        //增加枚举
        MapData mapData = new MapData(this.getFrmID());
        ds.Tables.add(mapData.getSysEnums().ToDataTableField("Sys_Enum"));

        ///#region 把外键表加入 DataSet
        DataTable ddlTable = new DataTable();
        ddlTable.Columns.Add("No");

        for (MapAttr attr : mapattrs.ToJavaList()) {
            //为空、枚举值就continue.
            if (DataType.IsNullOrEmpty(attr.getUIBindKey()) == true || attr.getLGType() == FieldTypeS.Enum) {
                continue;
            }
            DataTable mydt = bp.pub.PubClass.GetDataTableByUIBineKey(attr.getUIBindKey(), null);
            if (mydt == null) {
                DataRow ddldr = ddlTable.NewRow();
                ddldr.setValue("No", attr.getUIBindKey());
                ddlTable.Rows.add(ddldr);
            } else {
                ds.Tables.add(mydt);
            }
        }
        ddlTable.TableName = "UIBindKey";
        ds.Tables.add(ddlTable);

        ///#endregion End把外键表加入DataSet

        //查询一行数据的操作
        Methods methods = new Methods();
        methods.Retrieve(MethodAttr.FrmID, this.getFrmID(), MethodAttr.IsList, 1, MethodAttr.Idx);

        ds.Tables.add(methods.ToDataTableField("Frm_Method"));

        return bp.tools.Json.ToJson(ds);
    }

    public final String SearchDB_UrlSearchData(String urlExt, String postData) throws Exception {
        urlExt = bp.wf.Glo.DealExp(urlExt, null);
        if (urlExt.contains("http") == false) {
            /*如果没有绝对路径 */
            if (SystemConfig.isBSsystem()) {
                /*在cs模式下自动获取*/
                String host = SystemConfig.getHostURL(); //BP.Sys.Base.Glo.Request.Url.Host;
                if (urlExt.contains("@AppPath")) {
                    urlExt = urlExt.replace("@AppPath", "http://" + host + getRequest().getRequestURI()); //BP.Sys.Base.Glo.Request.ApplicationPath
                } else {
                    urlExt = "http://" + getRequest().getRequestURI() + urlExt;
                }
            }

            if (SystemConfig.isBSsystem() == false) {
                /*在cs模式下它的baseurl 从web.config中获取.*/
                String cfgBaseUrl = (String) SystemConfig.getAppSettings().get("HostURL");
                if (DataType.IsNullOrEmpty(cfgBaseUrl)) {
                    String err = "调用url失败:没有在web.config中配置BaseUrl,导致url事件不能被执行.";
                    Log.DebugWriteError(err);
                    throw new RuntimeException(err);
                }
                urlExt = cfgBaseUrl + urlExt;
            }
        }

        String json = HttpClientUtil.doPostJson(urlExt, postData);
        return json;
    }

    public final String Search_Init() throws Exception {
        DataSet ds = new DataSet();
        DataTable dt = null;
        try {

            ///#region 查询语句
            MapData md = new MapData(this.getFrmID());

            //取出来查询条件.
            UserRegedit ur = new UserRegedit(WebUser.getNo(), this.getFrmID() + "_SearchAttrs");

            GEEntitys rpts = new GEEntitys(this.getFrmID());

            Attrs attrs = rpts.getNewEntity().getEnMap().getAttrs();

            QueryObject qo = new QueryObject(rpts);
            boolean isFirst = true; //是否第一次拼接SQL


            ///#region 关键字字段.
            String keyWord = ur.getSearchKey();

            if (md.GetParaBoolen("IsSearchKey") && DataType.IsNullOrEmpty(keyWord) == false && keyWord.length() >= 1) {
                Attr attrPK = new Attr();
                for (Attr attr : attrs) {
                    if (attr.getItIsPK()) {
                        attrPK = attr;
                        break;
                    }
                }
                int i = 0;
                String enumKey = ","; //求出枚举值外键.
                for (Attr attr : attrs) {
                    switch (attr.getMyFieldType()) {
                        case Enum:
                            enumKey = "," + attr.getKey() + "Text,";
                            break;
                        case FK:
                            continue;
                        default:
                            break;
                    }

                    if (attr.getMyDataType() != DataType.AppString) {
                        continue;
                    }

                    //排除枚举值关联refText.
                    if (attr.getMyFieldType() == FieldType.RefText) {
                        if (enumKey.contains("," + attr.getKey() + ",") == true) {
                            continue;
                        }
                    }

                    if (Objects.equals(attr.getKey(), "FK_Dept")) {
                        continue;
                    }

                    i++;
                    if (i == 1) {
                        isFirst = false;
                        /* 第一次进来。 */
                        qo.addLeftBracket();
                        if (Objects.equals(SystemConfig.getAppCenterDBVarStr(), "@") || Objects.equals(SystemConfig.getAppCenterDBVarStr(), "?")) {
                            qo.AddWhere(attr.getKey(), " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? (" CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + "SKey,'%')") : (" '%'+" + SystemConfig.getAppCenterDBVarStr() + "SKey+'%'"));
                        } else {
                            qo.AddWhere(attr.getKey(), " LIKE ", " '%'||" + SystemConfig.getAppCenterDBVarStr() + "SKey||'%'");
                        }
                        continue;
                    }
                    qo.addOr();

                    if (Objects.equals(SystemConfig.getAppCenterDBVarStr(), "@") || Objects.equals(SystemConfig.getAppCenterDBVarStr(), "?")) {
                        qo.AddWhere(attr.getKey(), " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? ("CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + "SKey,'%')") : ("'%'+" + SystemConfig.getAppCenterDBVarStr() + "SKey+'%'"));
                    } else {
                        qo.AddWhere(attr.getKey(), " LIKE ", "'%'||" + SystemConfig.getAppCenterDBVarStr() + "SKey||'%'");
                    }

                }
                qo.getMyParas().Add("SKey", keyWord, false);
                qo.addRightBracket();
            } else if (DataType.IsNullOrEmpty(md.GetParaString("StringSearchKeys")) == false) {
                String field = ""; //字段名
                String fieldValue = ""; //字段值
                int idx = 0;

                //获取查询的字段
                String[] searchFields = md.GetParaString("StringSearchKeys").split("[*]", -1);
                for (String str : searchFields) {
                    if (DataType.IsNullOrEmpty(str) == true) {
                        continue;
                    }

                    //字段名
                    String[] items = str.split("[,]", -1);
                    if (items.length == 2 && DataType.IsNullOrEmpty(items[0]) == true) {
                        continue;
                    }
                    field = items[0];
                    //字段名对应的字段值
                    fieldValue = ur.GetParaString(field);
                    if (DataType.IsNullOrEmpty(fieldValue) == true) {
                        continue;
                    }
                    idx++;
                    if (idx == 1) {
                        isFirst = false;
                        /* 第一次进来。 */
                        qo.addLeftBracket();
                        if (Objects.equals(SystemConfig.getAppCenterDBVarStr(), "@") || Objects.equals(SystemConfig.getAppCenterDBVarStr(), "?")) {
                            qo.AddWhere(field, " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? (" CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + field + ",'%')") : (" '%'+" + SystemConfig.getAppCenterDBVarStr() + field + "+'%'"));
                        } else {
                            qo.AddWhere(field, " LIKE ", " '%'||" + SystemConfig.getAppCenterDBVarStr() + field + "||'%'");
                        }
                        qo.getMyParas().Add(field, fieldValue, false);
                        continue;
                    }
                    qo.addAnd();

                    if (Objects.equals(SystemConfig.getAppCenterDBVarStr(), "@") || Objects.equals(SystemConfig.getAppCenterDBVarStr(), "?")) {
                        qo.AddWhere(field, " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? ("CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + field + ",'%')") : ("'%'+" + SystemConfig.getAppCenterDBVarStr() + field + "+'%'"));
                    } else {
                        qo.AddWhere(field, " LIKE ", "'%'||" + SystemConfig.getAppCenterDBVarStr() + field + "||'%'");
                    }
                    qo.getMyParas().Add(field, fieldValue, false);


                }
                if (idx != 0) {
                    qo.addRightBracket();
                }
            }


            ///#endregion 关键字段查询


            ///#region 时间段的查询
            if (md.GetParaInt("DTSearchWay", 0) != DTSearchWay.None.getValue() && DataType.IsNullOrEmpty(ur.getDTFrom()) == false) {
                String dtFrom = ur.getDTFrom(); // this.GetTBByID("TB_S_From").Text.Trim().replace("/", "-");
                String dtTo = ur.getDTTo(); // this.GetTBByID("TB_S_To").Text.Trim().replace("/", "-");

                //按日期查询
                if (md.GetParaInt("DTSearchWay", 0) == DTSearchWay.ByDate.getValue()) {
                    if (isFirst == false) {
                        qo.addAnd();
                    } else {
                        isFirst = false;
                    }
                    qo.addLeftBracket();
                    dtTo += " 23:59:59";
                    qo.setSQL(md.GetParaString("DTSearchKey") + " >= '" + dtFrom + "'");
                    qo.addAnd();
                    qo.setSQL(md.GetParaString("DTSearchKey") + " <= '" + dtTo + "'");
                    qo.addRightBracket();
                }

                if (md.GetParaInt("DTSearchWay", 0) == DTSearchWay.ByDateTime.getValue()) {
                    //取前一天的24：00
                    if (dtFrom.trim().length() == 10) //2017-09-30
                    {
                        dtFrom += " 00:00:00";
                    }
                    if (dtFrom.trim().length() == 16) //2017-09-30 00:00
                    {
                        dtFrom += ":00";
                    }

                    dtFrom = DateUtils.addDay(DateUtils.parse(dtFrom, "yyyy-MM-dd"), -1) + " 24:00";

                    if (dtTo.trim().length() < 11 || dtTo.trim().indexOf(' ') == -1) {
                        dtTo += " 24:00";
                    }

                    if (isFirst == false) {
                        qo.addAnd();
                    } else {
                        isFirst = false;
                    }
                    qo.addLeftBracket();
                    qo.setSQL(md.GetParaString("DTSearchKey") + " >= '" + dtFrom + "'");
                    qo.addAnd();
                    qo.setSQL(md.GetParaString("DTSearchKey") + " <= '" + dtTo + "'");
                    qo.addRightBracket();
                }
            }

            ///#endregion 时间段的查询


            ///#region 外键或者枚举的查询

            //获得关键字.
            AtPara ap = new AtPara(ur.getVals());
            Attr ddattr = null;
            for (String str : ap.getHisHT().keySet()) {
                String val = ap.GetValStrByKey(str);
                if (val.equals("all") || val.equals("null")) {
                    continue;
                }
                if (isFirst == false) {
                    qo.addAnd();
                } else {
                    isFirst = false;
                }

                qo.addLeftBracket();
                ddattr = attrs.GetAttrByKeyOfEn(str);
                if (val.indexOf(",") != -1) {
                    if (ddattr.getItIsNum() == true) {
                        qo.AddWhere(str, "IN", "(" + val + ")");
                        qo.addRightBracket();
                        continue;
                    }
                    val = "('" + val.replace(",", "','") + "')";
                    qo.AddWhere(str, "IN", val);
                    qo.addRightBracket();
                    continue;
                }
                if (SystemConfig.getAppCenterDBFieldIsParaDBType() == true) {
                    String typeVal = (String) bp.sys.base.Glo.GenerRealType(attrs, str, ap.GetValStrByKey(str));
                    qo.AddWhere(str, typeVal);

                } else {
                    qo.AddWhere(str, ap.GetValStrByKey(str));
                }

                qo.addRightBracket();
            }

            ///#endregion 外键或者枚举的查询


            ///#region 设置隐藏字段的过滤查询
            FrmBill frmBill = new FrmBill(this.getFrmID());
            String hidenField = frmBill.GetParaString("HidenField");

            if (DataType.IsNullOrEmpty(hidenField) == false) {
                hidenField = hidenField.replace("_WebUser.No", WebUser.getNo());
                hidenField = hidenField.replace("_WebUser.Name", WebUser.getName());
                hidenField = hidenField.replace("_WebUser.FK_DeptName", WebUser.getDeptName());
                hidenField = hidenField.replace("_WebUser.FK_Dept", WebUser.getDeptNo());
                hidenField = hidenField.replace("_WebUser.OrgNo", WebUser.getOrgNo());
                if (hidenField.indexOf("_") != -1) {
                    return "err@隐藏条件" + hidenField + "还有未替换的_符号";
                }

                if (isFirst == false) {
                    qo.addAnd();
                } else {
                    isFirst = false;
                }
                qo.addSQL(hidenField);
            }


            ///#endregion 设置隐藏字段的查询


            ///#endregion 查询语句

            if (isFirst == false) {
                qo.addAnd();
            }


            qo.AddWhere("BillState", "!=", 0);
            isFirst = false;
            if (md.GetParaInt("SearchDataRole", 0) != SearchDataRole.SearchAll.getValue()) {
                //默认查询本部门的单据
                if (SearchDataRole.forValue(md.GetParaInt("SearchDataRole", 0)) == SearchDataRole.ByOnlySelf && DataType.IsNullOrEmpty(hidenField) == true || (md.GetParaInt("SearchDataRoleByDeptStation", 0) == 0 && DataType.IsNullOrEmpty(ap.GetValStrByKey("FK_Dept")) == true)) {
                    //qo.addAnd();
                    //qo.AddWhere("Starter", "=", WebUser.getNo());
                }
            }

            //增加表单字段的查询
            for (String key : ContextHolderUtils.getRequest().getParameterMap().keySet()) {
                if (DataType.IsNullOrEmpty(key) || key.equals("T") == true || key.equals("t") == true || key.equals("HttpHandlerName") == true || key.equals("DoMethod") == true || key.equals("DoType") == true) {
                    continue;
                }
                if (attrs.contains(key) == true) {
                    if (isFirst == false) {
                        qo.addAnd();
                    }
                    qo.AddWhere(key, ContextHolderUtils.getRequest().getParameter(key));
                    continue;
                }

            }

            //获得行数.
            ur.SetPara("RecCount", qo.GetCount());
            ur.Save();

            //获取配置信息
            String fieldSet = frmBill.getFieldSet();
            String oper = "";
            if (DataType.IsNullOrEmpty(fieldSet) == false) {
                String ptable = rpts.getNewEntity().getEnMap().getPhysicsTable();
                dt = new DataTable("Search_FieldSet");
                dt.Columns.Add("Field");
                dt.Columns.Add("Type");
                dt.Columns.Add("Value");
                DataRow dr;
                String[] strs = fieldSet.split("[@]", -1);
                for (String str : strs) {
                    if (DataType.IsNullOrEmpty(str) == true) {
                        continue;
                    }
                    String[] item = str.split("[=]", -1);
                    if (item.length == 2) {
                        if (item[1].contains(",") == true) {
                            String[] ss = item[1].split("[,]", -1);
                            for (String s : ss) {
                                dr = dt.NewRow();
                                dr.setValue("Field", attrs.GetAttrByKey(s).getDesc());
                                dr.setValue("Type", item[0]);
                                dt.Rows.add(dr);

                                oper += item[0] + "(" + ptable + "." + s + ")" + ",";
                            }
                        } else {
                            dr = dt.NewRow();
                            dr.setValue("Field", attrs.GetAttrByKey(item[1]).getDesc());
                            dr.setValue("Type", item[0]);
                            dt.Rows.add(dr);

                            oper += item[0] + "(" + ptable + "." + item[1] + ")" + ",";
                        }
                    }
                }
                oper = oper.substring(0, oper.length() - 1);
                DataTable dd = qo.GetSumOrAvg(oper);

                for (int i = 0; i < dt.Rows.size(); i++) {
                    DataRow ddr = dt.Rows.get(i);
                    ddr.setValue("Value", dd.Rows.get(0).getValue(i));
                }
                ds.Tables.add(dt);
            }


            if (DataType.IsNullOrEmpty(ur.getOrderBy()) == false && DataType.IsNullOrEmpty(ur.getOrderWay()) == false) {
                qo.DoQuery("OID", this.getPageSize(), this.getPageIdx(), ur.getOrderBy(), ur.getOrderWay());
            } else {
                qo.DoQuery("OID", this.getPageSize(), this.getPageIdx());
            }

            DataTable mydt = rpts.ToDataTableField("dt");
            mydt.TableName = "DT";

            ds.Tables.add(mydt); //把数据加入里面.

        } catch (Exception ex) {
            if (ex.getMessage() != null && (ex.getMessage().contains("无效") == true || ex.getMessage().toLowerCase().contains("unknown column") == true)) {
                GEEntity en = new GEEntity(this.getFrmID());
                en.CheckPhysicsTable();
                return Search_Init();
            }
            return ex.getMessage();
        }


        return bp.tools.Json.ToJson(ds);
    }

    public final String SearchDB_Init() throws Exception {

        DataSet ds = new DataSet();

        ///#region 查询语句
        DBList md = new DBList(this.getFrmID());
        if (DataType.IsNullOrEmpty(md.getExpList()) == true) {
            return "err@列表数据源和的查询不能为空";
        }

        String expList = md.getExpList();
        expList = bp.wf.Glo.DealExp(expList, null);
        //取出来查询条件.
        UserRegedit ur = new UserRegedit(WebUser.getNo(), this.getFrmID() + "_SearchAttrs");

        GEEntitys rpts = new GEEntitys(this.getFrmID());

        Attrs attrs = rpts.getNewEntity().getEnMap().getAttrs();
        String systemKeys = "BillState,RDT,Starter,StarterName,OrgNo,AtPara,"; //创建表单时的系统字段

        //获取查询条件
        DataTable whereDT = new DataTable();
        whereDT.Columns.Add("Key");
        whereDT.Columns.Add("Oper");
        whereDT.Columns.Add("Value");
        whereDT.Columns.Add("Type");
        DataRow dr;

        ///#region 关键字字段.
        String keyWord = ur.getSearchKey();
        Hashtable ht = new Hashtable();

        if (md.GetParaBoolen("IsSearchKey") == true) {
            if (DataType.IsNullOrEmpty(keyWord) == false && keyWord.length() >= 1) {
                ht.put("SearchKey", keyWord);
            } else {
                Attr attrPK = new Attr();
                for (Attr attr : attrs) {
                    if (attr.getItIsPK()) {
                        attrPK = attr;
                        break;
                    }
                }
                int i = 0;
                String enumKey = ","; //求出枚举值外键.
                for (Attr attr : attrs) {
                    if (systemKeys.indexOf(attr.getKey() + ",") != -1) {
                        continue;
                    }
                    switch (attr.getMyFieldType()) {
                        case Enum:
                            enumKey = "," + attr.getKey() + "Text,";
                            break;
                        case FK:
                            continue;
                        default:
                            break;
                    }

                    if (attr.getMyDataType() != DataType.AppString) {
                        continue;
                    }

                    //排除枚举值关联refText.
                    if (attr.getMyFieldType() == FieldType.RefText) {
                        if (enumKey.contains("," + attr.getKey() + ",") == true) {
                            continue;
                        }
                    }

                    if (Objects.equals(attr.getKey(), "FK_Dept")) {
                        continue;
                    }
                    i++;
                    dr = whereDT.NewRow();
                    dr.setValue("Key", attr.getKey());
                    dr.setValue("Oper", "like");
                    dr.setValue("Value", keyWord);
                    dr.setValue("Type", "SearchKey");
                    whereDT.Rows.add(dr);

                }
                ht.put("SearchKey", keyWord);

            }

        } else if (DataType.IsNullOrEmpty(md.GetParaString("StringSearchKeys")) == false) {
            String field = ""; //字段名
            String fieldValue = ""; //字段值

            //获取查询的字段
            String[] searchFields = md.GetParaString("StringSearchKeys").split("[*]", -1);
            for (String str : searchFields) {
                if (DataType.IsNullOrEmpty(str) == true) {
                    continue;
                }

                //字段名
                String[] items = str.split("[,]", -1);
                if (items.length == 2 && DataType.IsNullOrEmpty(items[0]) == true) {
                    continue;
                }
                field = items[0];
                //字段名对应的字段值
                fieldValue = ur.GetParaString(field);
                if (DataType.IsNullOrEmpty(fieldValue) == true) {
                    ht.put(field, "");
                    continue;
                }

                dr = whereDT.NewRow();
                dr.setValue("Key", field);
                dr.setValue("Oper", "like");
                dr.setValue("Value", fieldValue);
                dr.setValue("Type", "StringKey");
                whereDT.Rows.add(dr);
                ht.put(field, fieldValue);
            }

        }


        ///#endregion 关键字段查询


        ///#region 时间段的查询
        if (md.GetParaInt("DTSearchWay", 0) != DTSearchWay.None.getValue()) {
            if (DataType.IsNullOrEmpty(ur.getDTFrom()) == true) {
                ht.put("DTFrom", ur.getDTFrom());
                ht.put("DTTo", ur.getDTTo());

            } else {
                String dtFrom = ur.getDTFrom(); // this.GetTBByID("TB_S_From").Text.Trim().replace("/", "-");
                String dtTo = ur.getDTTo(); // this.GetTBByID("TB_S_To").Text.Trim().replace("/", "-");

                //按日期查询
                if (md.GetParaInt("DTSearchWay", 0) == DTSearchWay.ByDate.getValue()) {
                    dr = whereDT.NewRow();
                    dr.setValue("Key", md.GetParaString("DTSearchKey"));
                    dr.setValue("Oper", ">=");
                    dr.setValue("Value", dtFrom);
                    dr.setValue("Type", "Date");
                    whereDT.Rows.add(dr);
                    dtTo += " 23:59:59";
                    dr = whereDT.NewRow();
                    dr.setValue("Key", md.GetParaString("DTSearchKey"));
                    dr.setValue("Oper", "<=");
                    dr.setValue("Value", dtTo);
                    dr.setValue("Type", "Date");
                    whereDT.Rows.add(dr);
                    ht.put("DTFrom", dtFrom);
                    ht.put("DTTo", dtTo);
                }

                if (md.GetParaInt("DTSearchWay", 0) == DTSearchWay.ByDateTime.getValue()) {
                    //取前一天的24：00
                    if (dtFrom.trim().length() == 10) //2017-09-30
                    {
                        dtFrom += " 00:00:00";
                    }
                    if (dtFrom.trim().length() == 16) //2017-09-30 00:00
                    {
                        dtFrom += ":00";
                    }

                    dtFrom = DateUtils.addDay(DateUtils.parse(dtFrom, "yyyy-MM-dd"), -1) + " 24:00";

                    if (dtTo.trim().length() < 11 || dtTo.trim().indexOf(' ') == -1) {
                        dtTo += " 24:00";
                    }

                    dr = whereDT.NewRow();
                    dr.setValue("Key", md.GetParaString("DTSearchKey"));
                    dr.setValue("Oper", ">=");
                    dr.setValue("Value", dtFrom);
                    dr.setValue("Type", "Date");
                    whereDT.Rows.add(dr);
                    dr = whereDT.NewRow();
                    dr.setValue("Key", md.GetParaString("DTSearchKey"));
                    dr.setValue("Oper", "<=");
                    dr.setValue("Value", dtTo);
                    dr.setValue("Type", "Date");
                    whereDT.Rows.add(dr);
                    ht.put("DTFrom", dtFrom);
                    ht.put("DTTo", dtTo);
                }
            }

        }

        ///#endregion 时间段的查询


        ///#region 外键或者枚举的查询

        //获得关键字.
        AtPara ap = new AtPara(ur.getVals());
        Attr ddattr = null;
        for (String str : ap.getHisHT().keySet()) {
            String val = ap.GetValStrByKey(str);
            if (val.equals("all")) {
                ht.put(str, "");
                continue;
            }

            dr = whereDT.NewRow();
            dr.setValue("Key", str);
            dr.setValue("Oper", "=");
            if (val.indexOf(",") != -1) {
                dr.setValue("Oper", "IN");
            }

            dr.setValue("Value", val);
            dr.setValue("Type", "Select");
            whereDT.Rows.add(dr);
            ht.put(str, ap.GetValStrByKey(str));
        }

        ///#endregion 外键或者枚举的查询


        //增加表单字段的查询
        for (String key : ContextHolderUtils.getRequest().getParameterMap().keySet()) {
            if (DataType.IsNullOrEmpty(key) || key.equals("T") == true || key.equals("t") == true || key.equals("HttpHandlerName") == true || key.equals("DoMethod") == true || key.equals("DoType") == true) {
                continue;
            }
            if (attrs.contains(key) == true) {
                dr = whereDT.NewRow();
                dr.setValue("Key", key);
                dr.setValue("Oper", "=");
                dr.setValue("Value", ContextHolderUtils.getRequest().getParameter(key));
                dr.setValue("Type", "Normal");
                whereDT.Rows.add(dr);
                ht.put(key, ContextHolderUtils.getRequest().getParameter(key));
                continue;
            }

        }

        ///#endregion

        ///#region 数据源SQL
        if (md.getDBType() == 0) {

            String mainTable = "A.";
            String mainTablePK = md.getMainTablePK();

            String whereSQL = "";
            boolean isFirstSearchKey = true;
            boolean isFirstDateKey = true;

            for (DataRow dataRow : whereDT.Rows) {
                String key = dataRow.get("Key").toString();

                if (expList.indexOf("@" + getKey()) != -1) {
                    expList = expList.replace("@" + getKey(), dataRow.get("Value").toString());
                    continue;
                }
                String type = dataRow.get("Type").toString();
                if (type.equals("SearchKey") == true) {
                    if (isFirstSearchKey) {
                        isFirstSearchKey = false;
                        whereSQL += " AND (" + mainTable + key + " like '%" + dataRow.get("Value").toString() + "%' ";
                    } else {
                        whereSQL += " OR " + mainTable + key + " like '%" + dataRow.get("Value").toString() + "%' ";
                    }
                }
                if (isFirstSearchKey == false && type.equals("SearchKey") == false) {
                    whereSQL += ")";
                    isFirstSearchKey = true;
                }

                if (type.equals("StringKey") == true) {
                    whereSQL += " AND " + mainTable + key + " like '%" + dataRow.get("Value").toString() + "%' ";
                }
                //时间解析
                if (type.equals("Date") == true) {

                    if (isFirstDateKey == true) {
                        isFirstDateKey = false;
                        whereSQL += " AND (" + mainTable + key + " " + dataRow.get("Oper").toString() + " '" + dataRow.get("Value").toString() + "' ";
                        continue;
                    }
                    if (isFirstDateKey == false) {
                        whereSQL += " AND " + mainTable + key + " " + dataRow.get("Oper").toString() + " '" + dataRow.get("Value").toString() + "')";
                    }
                }
                if (type.equals("Select") == true || type.equals("Normal") == true) {
                    String oper = dataRow.get("Oper").toString();
                    String val = dataRow.get("Value").toString();
                    if (oper.equals("IN") == true) {
                        ddattr = attrs.GetAttrByKeyOfEn(key);
                        if (ddattr != null) {
                            if (ddattr.getItIsNum()) {
                                whereSQL += " AND " + mainTable + key + " " + oper + " (" + val + ") ";
                            } else {
                                val = "('" + val.replace(",", "','") + "')";
                                whereSQL += " AND " + mainTable + key + " " + oper + val;
                            }
                        }

                    } else {
                        whereSQL += " AND " + mainTable + key + " " + oper + " '" + val + "'";
                    }

                }
            }

            if (isFirstSearchKey == false) {
                whereSQL += ")";
            }
            //expCount = expCount + whereSQL;
            //expList = expList + whereSQL;
            String hidenField = md.GetParaString("HidenField");

            if (DataType.IsNullOrEmpty(hidenField) == false) {
                hidenField = hidenField.replace("_WebUser.No", WebUser.getNo());
                hidenField = hidenField.replace("_WebUser.Name", WebUser.getName());
                hidenField = hidenField.replace("_WebUser.FK_DeptName", WebUser.getDeptName());
                hidenField = hidenField.replace("_WebUser.FK_Dept", WebUser.getDeptNo());
                hidenField = hidenField.replace("_WebUser.OrgNo", WebUser.getOrgNo());
                if (hidenField.indexOf("@") != -1) {
                    return "err@隐藏条件" + hidenField + "还有未替换的_符号";
                }
                whereSQL += " AND (" + hidenField + ")";
            }

            expList = "SELECT * From(" + expList + ") AS A WHERE 1=1 " + whereSQL; //查询列数的
            String expCount = "SELECT Count(*) From(" + expList + ") AS A WHERE 1=1 " + whereSQL; //查询总条数的
            String expPageSize = "SELECT A.OID  From(" + expList + ") AS A WHERE 1=1 " + whereSQL; //查询分页使用的SQL语句

            if (DataType.IsNullOrEmpty(md.getDBSrc()) == true) {
                md.setDBSrc("local");
            }

            expCount = bp.wf.Glo.DealExp(expCount, null, null);
            expPageSize = bp.wf.Glo.DealExp(expPageSize, null, null);


            SFDBSrc dbsrc = new SFDBSrc(md.getDBSrc());
            int count = dbsrc.RunSQLReturnInt(expCount, 0);


            dbsrc.DoQuery(rpts, expList, expPageSize, "OID", attrs, count, this.getPageSize(), this.getPageIdx(), ur.getOrderBy(), false);
            ur.SetPara("RecCount", count);
            ur.Save();
            DataTable dt = rpts.ToDataTableField("DT");
            ds.Tables.add(dt); //把数据加入里面.

        }

        ///#endregion 数据源SQL

        ///#region URL请求数据
        if (md.getDBType() == 1) {
            ht.put("PageSize", this.getPageSize());
            ht.put("PageIdx", this.getPageIdx());
            // 请求的参数作为JSON字符串发送给列表URL
            String postData = bp.tools.Json.ToJson(ht);
            if (DataType.IsNullOrEmpty(md.getExpList()) == true) {
                return "err@根据URL请求数据的URL为空，请检查配置";
            }
            String json = SearchDB_UrlSearchData(md.getExpList(), postData);
            if (DataType.IsNullOrEmpty(json) == true) {
                return "err@根据URL请求数据列表数据为空";
            }
            DataTable jd = Json.ToDataTable(json);
            String count = jd.Rows.get(0).getValue("count").toString();
            if (DataType.IsNullOrEmpty(count) == true) {
                ur.SetPara("RecCount", 0);
            } else {
                ur.SetPara("RecCount", Integer.parseInt(count));
            }
            ur.Save();
            String data = jd.Rows.get(0).getValue("data").toString();
            DataTable dt = new DataTable("DT");
            if (DataType.IsNullOrEmpty(data) == false) {
                dt = bp.tools.Json.ToDataTable(data);
                dt.TableName = "DT";
                ds.Tables.add(dt); //把数据加入里面.
            }

        }

        ///#endregion URL请求数据


        ///#region 存储过程的查询
        if (md.getDBType() == 2) {
            String sql = md.getExpList();
            sql = sql.replace("~", "'");
            Paras paras = new Paras();
            for (Object key : ht.keySet()) {
                paras.add(Integer.valueOf(key.toString()), (Para) ht.get(key));
            }
            String hidenField = md.GetParaString("HidenField");

            if (DataType.IsNullOrEmpty(hidenField) == false) {
                hidenField = hidenField.replace("_WebUser.No", WebUser.getNo());
                hidenField = hidenField.replace("_WebUser.Name", WebUser.getName());
                hidenField = hidenField.replace("_WebUser.FK_DeptName", WebUser.getDeptName());
                hidenField = hidenField.replace("_WebUser.FK_Dept", WebUser.getDeptNo());
                hidenField = hidenField.replace("_WebUser.OrgNo", WebUser.getOrgNo());
                if (hidenField.indexOf("@") != -1) {
                    return "err@隐藏条件" + hidenField + "还有未替换的_符号";
                }
                String[] strs = hidenField.split("[,]", -1);
                for (String str : strs) {
                    if (DataType.IsNullOrEmpty(str) == true) {
                        continue;
                    }
                    String[] strVal = str.split("[=]", -1);
                    if (strVal.length == 1) {
                        paras.Add(strVal[0], "", false);
                    } else {
                        paras.Add(strVal[0], strVal[1], false);
                    }
                }

            }
            DataTable dt = DBAccess.RunProcReturnTable(sql, paras);
            dt.TableName = "DT";
            ds.Tables.add(dt); //把数据加入里面.
        }

        ///#endregion 存储过程的查询

        return bp.tools.Json.ToJson(ds);
    }

    /**
     * 初始化
     *
     * @return
     */
    public final String GenerBill_Init() throws Exception {
        GenerBills bills = new GenerBills();
        bills.Retrieve(GenerBillAttr.Starter, WebUser.getNo(), null);
        return bills.ToJson("dt");
    }

    /**
     * 查询初始化
     *
     * @return
     */
    public final String SearchData_Init() throws Exception {
        DataSet ds = new DataSet();
        String sql = "";

        String tSpan = this.GetRequestVal("TSpan");
        if (Objects.equals(tSpan, "")) {
            tSpan = null;
        }


        ///#region 1、获取时间段枚举/总数.
        SysEnums ses = new SysEnums("TSpan");
        DataTable dtTSpan = ses.ToDataTableField("dt");
        dtTSpan.TableName = "TSpan";
        ds.Tables.add(dtTSpan);

        GenerBill gb = new GenerBill();
        gb.CheckPhysicsTable();

        sql = "SELECT TSpan as No, COUNT(WorkID) as Num FROM Frm_GenerBill WHERE FrmID='" + this.getFrmID() + "'  AND Starter='" + WebUser.getNo() + "' AND BillState >= 1 GROUP BY TSpan";

        DataTable dtTSpanNum = DBAccess.RunSQLReturnTable(sql);
        for (DataRow drEnum : dtTSpan.Rows) {
            String no = drEnum.get("IntKey").toString();
            for (DataRow dr : dtTSpanNum.Rows) {
                if (Objects.equals(dr.getValue("No").toString(), no)) {
                    drEnum.setValue("Lab", drEnum.get("Lab").toString() + "(" + dr.getValue("Num") + ")");
                    break;
                }
            }
        }

        ///#endregion


        ///#region 2、处理流程类别列表.
        sql = " SELECT  A.BillState as No, B.Lab as Name, COUNT(WorkID) as Num FROM Frm_GenerBill A, " + bp.sys.base.Glo.SysEnum() + " B ";
        sql += " WHERE A.BillState=B.IntKey AND B.EnumKey='BillState' AND  A.Starter='" + WebUser.getNo() + "' AND BillState >=1";
        if (tSpan.equals("-1") == false) {
            sql += "  AND A.TSpan=" + tSpan;
        }

        sql += "  GROUP BY A.BillState, B.Lab  ";

        DataTable dtFlows = DBAccess.RunSQLReturnTable(sql);
        if (SystemConfig.getAppCenterDBFieldCaseModel() != FieldCaseModel.None) {
            dtFlows.Columns.get(0).setColumnName("No");
            dtFlows.Columns.get(1).setColumnName("Name");
            dtFlows.Columns.get(2).setColumnName("Num");
        }
        dtFlows.TableName = "Flows";
        ds.Tables.add(dtFlows);

        ///#endregion


        ///#region 3、处理流程实例列表.
        String sqlWhere = "";
        sqlWhere = "(1 = 1)AND Starter = '" + WebUser.getNo() + "' AND BillState >= 1";
        if (tSpan.equals("-1") == false) {
            sqlWhere += "AND (TSpan = '" + tSpan + "') ";
        }

        if (this.getFlowNo() != null) {
            sqlWhere += "AND (FrmID = '" + this.getFrmID() + "')  ";
        } else {
            // sqlWhere += ")";
        }
        sqlWhere += "ORDER BY RDT DESC";

        String fields = " WorkID,FrmID,FrmName,Title,BillState,Starter,StarterName,Sender,RDT ";

        switch (SystemConfig.getAppCenterDBType()) {
            case MySQL:
            case PostgreSQL:
            case UX:
            case HGDB:
                sql = "SELECT  " + fields + " FROM Frm_GenerBill WHERE " + sqlWhere + " LIMIT 50";
                break;
            case MSSQL:
                sql = "SELECT  TOP 50 " + fields + " FROM Frm_GenerBill WHERE " + sqlWhere;
                break;
            case Oracle:
            case KingBaseR3:
            case KingBaseR6:
                sql = "SELECT " + fields + " FROM (SELECT * FROM Frm_GenerBill WHERE " + sqlWhere + ") WHERE rownum <= 50";
                break;
            default:
                throw new RuntimeException("err@没有判断的数据库类型.");
        }


        DataTable mydt = DBAccess.RunSQLReturnTable(sql);
        if (SystemConfig.getAppCenterDBFieldCaseModel() != FieldCaseModel.None) {
            mydt.Columns.get(0).ColumnName = "WorkID";
            mydt.Columns.get(1).ColumnName = "FrmID";
            mydt.Columns.get(2).ColumnName = "FrmName";
            mydt.Columns.get(3).ColumnName = "Title";
            mydt.Columns.get(4).ColumnName = "BillState";
            mydt.Columns.get(5).ColumnName = "Starter";
            mydt.Columns.get(6).ColumnName = "StarterName";
            mydt.Columns.get(7).ColumnName = "Sender";
            mydt.Columns.get(8).ColumnName = "RDT";
        }

        mydt.TableName = "Frm_Bill";
        if (mydt != null) {
            mydt.Columns.Add("TDTime");
            for (DataRow dr : mydt.Rows) {
                //   dr["TDTime"] =  GetTraceNewTime(dr["FK_Flow").toString(), int.Parse(dr["WorkID").toString()), int.Parse(dr["FID").toString()));
            }
        }

        ///#endregion

        ds.Tables.add(mydt);

        return bp.tools.Json.ToJson(ds);
    }

    ///#endregion 查询.


    ///#region 单据导出
    public final String Search_Exp() throws Exception {
        FrmBill frmBill = new FrmBill(this.getFrmID());
        GEEntitys rpts = new GEEntitys(this.getFrmID());

        String name = "数据导出";
        String filename = frmBill.getName() + "_" + DataType.getCurrentDateTime() + ".xls";
        String filePath = bp.tools.ExportExcelUtil.ExportDGToExcel(Search_Data(), rpts.getNewEntity(), null, null, filename);
        return filePath;
    }

    public final DataTable Search_Data() throws Exception {
        DataSet ds = new DataSet();


        ///#region 查询语句

        MapData md = new MapData(this.getFrmID());


        //取出来查询条件.
        UserRegedit ur = new UserRegedit(WebUser.getNo(), this.getFrmID() + "_SearchAttrs");

        GEEntitys rpts = new GEEntitys(this.getFrmID());

        Attrs attrs = rpts.getNewEntity().getEnMap().getAttrs();

        QueryObject qo = new QueryObject(rpts);


        ///#region 关键字字段.
        String keyWord = ur.getSearchKey();
        boolean isFirst = true; //是否第一次拼接SQL

        if (md.GetParaBoolen("IsSearchKey") && DataType.IsNullOrEmpty(keyWord) == false && keyWord.length() >= 1) {
            Attr attrPK = new Attr();
            for (Attr attr : attrs) {
                if (attr.getItIsPK()) {
                    attrPK = attr;
                    break;
                }
            }
            int i = 0;
            String enumKey = ","; //求出枚举值外键.
            for (Attr attr : attrs) {
                switch (attr.getMyFieldType()) {
                    case Enum:
                        enumKey = "," + attr.getKey() + "Text,";
                        break;
                    case FK:
                        continue;
                    default:
                        break;
                }

                if (attr.getMyDataType() != DataType.AppString) {
                    continue;
                }

                //排除枚举值关联refText.
                if (attr.getMyFieldType() == FieldType.RefText) {
                    if (enumKey.contains("," + attr.getKey() + ",") == true) {
                        continue;
                    }
                }

                if (Objects.equals(attr.getKey(), "FK_Dept")) {
                    continue;
                }

                i++;
                if (i == 1) {
                    isFirst = false;
                    /* 第一次进来。 */
                    qo.addLeftBracket();
                    if (Objects.equals(SystemConfig.getAppCenterDBVarStr(), "@") || Objects.equals(SystemConfig.getAppCenterDBVarStr(), "?")) {
                        qo.AddWhere(attr.getKey(), " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? (" CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + "SKey,'%')") : (" '%'+" + SystemConfig.getAppCenterDBVarStr() + "SKey+'%'"));
                    } else {
                        qo.AddWhere(attr.getKey(), " LIKE ", " '%'||" + SystemConfig.getAppCenterDBVarStr() + "SKey||'%'");
                    }
                    continue;
                }
                qo.addOr();

                if (Objects.equals(SystemConfig.getAppCenterDBVarStr(), "@") || Objects.equals(SystemConfig.getAppCenterDBVarStr(), "?")) {
                    qo.AddWhere(attr.getKey(), " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? ("CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + "SKey,'%')") : ("'%'+" + SystemConfig.getAppCenterDBVarStr() + "SKey+'%'"));
                } else {
                    qo.AddWhere(attr.getKey(), " LIKE ", "'%'||" + SystemConfig.getAppCenterDBVarStr() + "SKey||'%'");
                }

            }
            qo.getMyParas().Add("SKey", keyWord, false);
            qo.addRightBracket();
        } else if (DataType.IsNullOrEmpty(md.GetParaString("StringSearchKeys")) == false) {
            String field = ""; //字段名
            String fieldValue = ""; //字段值
            int idx = 0;

            //获取查询的字段
            String[] searchFields = md.GetParaString("StringSearchKeys").split("[*]", -1);
            for (String str : searchFields) {
                if (DataType.IsNullOrEmpty(str) == true) {
                    continue;
                }

                //字段名
                String[] items = str.split("[,]", -1);
                if (items.length == 2 && DataType.IsNullOrEmpty(items[0]) == true) {
                    continue;
                }
                field = items[0];
                //字段名对应的字段值
                fieldValue = ur.GetParaString(field);
                if (DataType.IsNullOrEmpty(fieldValue) == true) {
                    continue;
                }
                idx++;
                if (idx == 1) {
                    isFirst = false;
                    /* 第一次进来。 */
                    qo.addLeftBracket();
                    if (Objects.equals(SystemConfig.getAppCenterDBVarStr(), "@") || Objects.equals(SystemConfig.getAppCenterDBVarStr(), "?")) {
                        qo.AddWhere(field, " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? (" CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + field + ",'%')") : (" '%'+" + SystemConfig.getAppCenterDBVarStr() + field + "+'%'"));
                    } else {
                        qo.AddWhere(field, " LIKE ", " '%'||" + SystemConfig.getAppCenterDBVarStr() + field + "||'%'");
                    }
                    qo.getMyParas().Add(field, fieldValue, false);
                    continue;
                }
                qo.addAnd();

                if (Objects.equals(SystemConfig.getAppCenterDBVarStr(), "@") || Objects.equals(SystemConfig.getAppCenterDBVarStr(), "?")) {
                    qo.AddWhere(field, " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? ("CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + field + ",'%')") : ("'%'+" + SystemConfig.getAppCenterDBVarStr() + field + "+'%'"));
                } else {
                    qo.AddWhere(field, " LIKE ", "'%'||" + SystemConfig.getAppCenterDBVarStr() + field + "||'%'");
                }
                qo.getMyParas().Add(field, fieldValue, false);


            }
            if (idx != 0) {
                qo.addRightBracket();
            }
        }


        ///#endregion 关键字段查询


        ///#region 时间段的查询
        if (md.GetParaInt("DTSearchWay", 0) != DTSearchWay.None.getValue() && DataType.IsNullOrEmpty(ur.getDTFrom()) == false) {
            String dtFrom = ur.getDTFrom(); // this.GetTBByID("TB_S_From").Text.Trim().replace("/", "-");
            String dtTo = ur.getDTTo(); // this.GetTBByID("TB_S_To").Text.Trim().replace("/", "-");

            //按日期查询
            if (md.GetParaInt("DTSearchWay", 0) == DTSearchWay.ByDate.getValue()) {
                if (isFirst == false) {
                    qo.addAnd();
                } else {
                    isFirst = false;
                }
                qo.addLeftBracket();
                dtTo += " 23:59:59";
                qo.setSQL(md.GetParaString("DTSearchKey") + " >= '" + dtFrom + "'");
                qo.addAnd();
                qo.setSQL(md.GetParaString("DTSearchKey") + " <= '" + dtTo + "'");
                qo.addRightBracket();
            }

            if (md.GetParaInt("DTSearchWay", 0) == DTSearchWay.ByDateTime.getValue()) {
                //取前一天的24：00
                if (dtFrom.trim().length() == 10) //2017-09-30
                {
                    dtFrom += " 00:00:00";
                }
                if (dtFrom.trim().length() == 16) //2017-09-30 00:00
                {
                    dtFrom += ":00";
                }

                dtFrom = DateUtils.addDay(DateUtils.parse(dtFrom, "yyyy-MM-dd"), -1) + " 24:00";

                if (dtTo.trim().length() < 11 || dtTo.trim().indexOf(' ') == -1) {
                    dtTo += " 24:00";
                }

                if (isFirst == false) {
                    qo.addAnd();
                } else {
                    isFirst = false;
                }
                qo.addLeftBracket();
                qo.setSQL(md.GetParaString("DTSearchKey") + " >= '" + dtFrom + "'");
                qo.addAnd();
                qo.setSQL(md.GetParaString("DTSearchKey") + " <= '" + dtTo + "'");
                qo.addRightBracket();
            }
        }

        ///#endregion 时间段的查询


        ///#region 外键或者枚举的查询

        //获得关键字.
        AtPara ap = new AtPara(ur.getVals());
        for (String str : ap.getHisHT().keySet()) {

            String val = ap.GetValStrByKey(str);
            if (val.equals("all")) {
                continue;
            }
            if (isFirst == false) {
                qo.addAnd();
            } else {
                isFirst = false;
            }

            qo.addLeftBracket();


            if (SystemConfig.getAppCenterDBFieldIsParaDBType() == true) {
                Object typeVal = bp.sys.base.Glo.GenerRealType(attrs, str, ap.GetValStrByKey(str));
                qo.AddWhere(str, typeVal);

            } else {
                qo.AddWhere(str, ap.GetValStrByKey(str));
            }

            qo.addRightBracket();
        }

        ///#endregion 外键或者枚举的查询


        ///#region 设置隐藏字段的过滤查询
        FrmBill frmBill = new FrmBill(this.getFrmID());
        String hidenField = frmBill.GetParaString("HidenField");

        if (DataType.IsNullOrEmpty(hidenField) == false) {
            hidenField = hidenField.replace("_WebUser.No", WebUser.getNo());
            hidenField = hidenField.replace("_WebUser.Name", WebUser.getName());
            hidenField = hidenField.replace("_WebUser.FK_DeptName", WebUser.getDeptName());
            hidenField = hidenField.replace("_WebUser.FK_Dept", WebUser.getDeptNo());
            hidenField = hidenField.replace("_WebUser.OrgNo", WebUser.getOrgNo());

            if (isFirst == false) {
                qo.addAnd();
            } else {
                isFirst = false;
            }
            qo.addSQL(hidenField);
        }


        ///#endregion 设置隐藏字段的查询


        if (isFirst == false) {
            qo.addAnd();
        }

        qo.AddWhere("BillState", "!=", 0);

        if (md.GetParaInt("SearchDataRole", 0) != SearchDataRole.SearchAll.getValue()) {
            //默认查询本部门的单据
            if (SearchDataRole.forValue(md.GetParaInt("SearchDataRole", 0)) == SearchDataRole.ByOnlySelf && DataType.IsNullOrEmpty(hidenField) == true || (md.GetParaInt("SearchDataRoleByDeptStation", 0) == 0 && DataType.IsNullOrEmpty(ap.GetValStrByKey("FK_Dept")) == true)) {
                //qo.addAnd();
                //qo.AddWhere("Starter", "=", WebUser.getNo());
            }

        }


        ///#endregion 查询语句
        qo.addOrderBy("OID");
        qo.DoQuery();
        return rpts.ToDataTableField();

    }

    ///#endregion  执行导出


    ///#region 单据导入
    public final String ImpData_Done() throws Exception {
        if (SystemConfig.getCustomerNo().equals("ASSET") == true) {
            return ImpData_ASSETDone();
        }
        HttpServletRequest request = getRequest();
        if (CommonFileUtils.getFilesSize(request, "File_Upload") == 0) {
            return "err@请选择要导入的数据信息。";
        }
        String fileName = CommonFileUtils.getOriginalFilename(request, "File_Upload");
        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!prefix.equals("xls") && !prefix.equals("xlsx")) {

            return "err@上传的文件必须是Excel文件.";
        }

        String errInfo = "";
        String ext = ".xls";
        if (fileName.contains(".xlsx")) {
            ext = ".xlsx";
        }


        //设置文件名
        String fileNewName = DateUtils.format(new Date(), "yyyyMMddHHmmss");

        //文件存放路径
        String filePath = SystemConfig.getPathOfTemp() + "/" + fileNewName;
        try {
            CommonFileUtils.upload(request, "File_Upload", new File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
            return "err@执行失败";
        }

        //从excel里面获得数据表.
        DataTable dt = DBLoad.ReadExcelFileToDataTable(filePath, 0);

        //删除临时文件
        (new File(filePath)).delete();

        if (dt.Rows.size() == 0) {
            return "err@无导入的数据";
        }

        //获得entity.
        FrmBill bill = new FrmBill(this.getFrmID());
        GEEntitys rpts = new GEEntitys(this.getFrmID());
        GEEntity en = new GEEntity(this.getFrmID());


        String noColName = ""; //编号(针对实体表单).
        String nameColName = ""; //名称(针对实体表单).

        Map map = en.getEnMap();
        Attr attr = map.GetAttrByKey("BillNo");
        noColName = attr.getDesc();
        String codeStruct = bill.getEnMap().getCodeStruct();
        attr = map.GetAttrByKey("Title");
        nameColName = attr.getDesc();

        //定义属性.
        Attrs attrs = map.getAttrs();

        int impWay = this.GetRequestValInt("ImpWay");


        ///#region 清空方式导入.
        //清空方式导入.
        int count = 0; //导入的行数
        int changeCount = 0; //更新的行数
        String successInfo = "";
        if (impWay == 0) {
            rpts.ClearTable();
            GEEntity myen = new GEEntity(this.getFrmID());

            for (DataRow dr : dt.Rows) {
                //如果是实体单据,导入的excel必须包含BillNo
                if (bill.getEntityType() == EntityType.FrmDict && dt.Columns.contains(noColName) == false) {
                    return "err@导入的excel不包含编号列";
                }
                String no = "";
                if (dt.Columns.contains(noColName) == true) {
                    no = dr.getValue(noColName).toString();
                }
                String name = "";
                if (dt.Columns.contains(nameColName) == true) {
                    name = dr.getValue(nameColName).toString();
                }
                myen.setOID(0);

                //判断是否是自增序列，序列的格式
                if (DataType.IsNullOrEmpty(codeStruct) == false && DataType.IsNullOrEmpty(no) == false) {
                    no = StringHelper.padLeft(no, Integer.parseInt(codeStruct), '0');
                }

                myen.SetValByKey("BillNo", no);
                if (bill.getEntityType() == EntityType.FrmDict) {
                    if (myen.Retrieve("BillNo", no) == 1) {
                        errInfo += "err@编号[" + no + "][" + name + "]重复.";
                        continue;
                    }
                }


                //给实体赋值
                errInfo += SetEntityAttrVal(no, dr, attrs, myen, dt, 0, bill);
                count++;
                successInfo += "&nbsp;&nbsp;<span>" + noColName + "为" + no + "," + nameColName + "为" + name + "的导入成功</span><br/>";
            }
        }


        ///#endregion 清空方式导入.


        ///#region 更新方式导入
        if (impWay == 1 || impWay == 2) {
            for (DataRow dr : dt.Rows) {
                //如果是实体单据,导入的excel必须包含BillNo
                if (bill.getEntityType() == EntityType.FrmDict && dt.Columns.contains(noColName) == false) {
                    return "err@导入的excel不包含编号列";
                }
                String no = "";
                if (dt.Columns.contains(noColName) == true) {
                    no = dr.getValue(noColName).toString();
                }

                String name = "";
                if (dt.Columns.contains(nameColName) == true) {
                    name = dr.getValue(nameColName).toString();
                }
                //判断是否是自增序列，序列的格式
                if (DataType.IsNullOrEmpty(codeStruct) == false && DataType.IsNullOrEmpty(no) == false) {
                    no = StringHelper.padLeft(no, Integer.parseInt(codeStruct), '0');
                }
                bp.en.Entity tempVar = rpts.getNewEntity();
                GEEntity myen = tempVar instanceof GEEntity ? (GEEntity) tempVar : null;
                myen.SetValByKey("BillNo", no);
                if (myen.Retrieve("BillNo", no) == 1 && bill.getEntityType() == EntityType.FrmDict) {
                    //给实体赋值
                    errInfo += SetEntityAttrVal(no, dr, attrs, myen, dt, 1, bill);
                    changeCount++;
                    successInfo += "&nbsp;&nbsp;<span>" + noColName + "为" + no + "," + nameColName + "为" + name + "的更新成功</span><br/>";
                    continue;
                }


                //给实体赋值
                errInfo += SetEntityAttrVal(no, dr, attrs, myen, dt, 0, bill);
                count++;
                successInfo += "&nbsp;&nbsp;<span>" + noColName + "为" + no + "," + nameColName + "为" + name + "的导入成功</span><br/>";
            }
        }

        ///#endregion

        return "errInfo=" + errInfo + "@Split" + "count=" + count + "@Split" + "successInfo=" + successInfo + "@Split" + "changeCount=" + changeCount;
    }

    private String SetEntityAttrVal(String no, DataRow dr, Attrs attrs, GEEntity en, DataTable dt, int saveType, FrmBill fbill) throws Exception {

        //单据数据不存在
        if (saveType == 0) {
            long oid = 0;
            if (fbill.getEntityType() == EntityType.FrmDict) {
                oid = bp.ccbill.Dev2Interface.CreateBlankDictID(fbill.getNo(), WebUser.getNo(), null);
            }
            if (fbill.getEntityType() == EntityType.FrmBill) {
                oid = bp.ccbill.Dev2Interface.CreateBlankBillID(fbill.getNo(), WebUser.getNo(), null);
            }
            en.setOID(oid);
            en.RetrieveFromDBSources();
        }

        String errInfo = "";
        //按照属性赋值.
        for (Attr item : attrs) {
            if (item.getKey().equals("BillNo") && dt.Columns.contains(item.getDesc()) == true) {
                en.SetValByKey(item.getKey(), no);
                continue;
            }
            if (item.getKey().equals("Title") && dt.Columns.contains(item.getDesc()) == true) {
                en.SetValByKey(item.getKey(), dr.getValue(item.getDesc()).toString());
                continue;
            }

            if (dt.Columns.contains(item.getDesc()) == false) {
                continue;
            }

            //枚举处理.
            if (item.getMyFieldType() == FieldType.Enum) {
                String val = dr.getValue(item.getDesc()).toString();

                SysEnum se = new SysEnum();
                int i = se.Retrieve(SysEnumAttr.EnumKey, item.getUIBindKey(), SysEnumAttr.Lab, val);

                if (i == 0) {
                    errInfo += "err@枚举[" + item.getKey() + "][" + item.getDesc() + "]，值[" + val + "]不存在.";
                    continue;
                }

                en.SetValByKey(item.getKey(), se.getIntKey());
                continue;
            }

            //外键处理.
            if (item.getMyFieldType() == FieldType.FK) {
                String val = dr.getValue(item.getDesc()).toString();
                Entity attrEn = item.getHisFKEn();
                int i = attrEn.Retrieve("Name", val);
                if (i == 0) {
                    errInfo += "err@外键[" + item.getKey() + "][" + item.getDesc() + "]，值[" + val + "]不存在.";
                    continue;
                }

                if (i != 1) {
                    errInfo += "err@外键[" + item.getKey() + "][" + item.getDesc() + "]，值[" + val + "]重复..";
                    continue;
                }

                //把编号值给他.
                en.SetValByKey(item.getKey(), attrEn.GetValByKey("No"));
                continue;
            }

            //boolen类型的处理..
            if (item.getMyDataType() == DataType.AppBoolean) {
                String val = dr.getValue(item.getDesc()).toString();
                if (Objects.equals(val, "是") || Objects.equals(val, "有")) {
                    en.SetValByKey(item.getKey(), 1);
                } else {
                    en.SetValByKey(item.getKey(), 0);
                }
                continue;
            }

            String myval = dr.getValue(item.getDesc()).toString();
            en.SetValByKey(item.getKey(), myval);
        }
        if (DataType.IsNullOrEmpty(en.GetValStrByKey("BillNo")) == true && DataType.IsNullOrEmpty(fbill.getBillNoFormat()) == false) {
            en.SetValByKey("BillNo", Dev2Interface.GenerBillNo(fbill.getBillNoFormat(), en.getOID(), en, fbill.getNo()));
        }

        if (DataType.IsNullOrEmpty(en.GetValStrByKey("Title")) == true && DataType.IsNullOrEmpty(fbill.getTitleRole()) == false) {
            en.SetValByKey("Title", Dev2Interface.GenerTitle(fbill.getTitleRole(), en));
        }

        en.SetValByKey("BillState", BillState.Editing.getValue());
        en.Update();

        GenerBill gb = new GenerBill();
        gb.setWorkID(en.getOID());
        if (gb.RetrieveFromDBSources() == 0) {
            gb.setBillState(BillState.Over); //初始化状态.
            gb.setStarter(WebUser.getNo());
            gb.setStarterName(WebUser.getName());
            gb.setFrmName(fbill.getName()); //单据名称.
            gb.setFrmID(fbill.getNo()); //单据ID
            if (en.getRow().containsKey("Title") == true) {
                gb.setTitle(en.GetValStringByKey("Title"));
            }
            if (en.getRow().containsKey("BillNo") == true) {
                gb.setBillNo(en.GetValStringByKey("BillNo"));
            }
            gb.setFrmTreeNo(fbill.getFormTreeNo()); //单据类别.
            gb.setRDT(DataType.getCurrentDateTime());
            gb.setNDStep(1);
            gb.setNDStepName("启动");
            gb.Insert();

        } else {
            gb.setBillState(BillState.Editing);
            if (en.getRow().containsKey("Title") == true) {
                gb.setTitle(en.GetValStringByKey("Title"));
            }
            if (en.getRow().containsKey("BillNo") == true) {
                gb.setBillNo(en.GetValStringByKey("BillNo"));
            }
            gb.Update();
        }

        return errInfo;
    }


    ///#endregion

    /**
     * 针对于北京农芯科技的单据导入的处理
     */
    public final String ImpData_ASSETDone() throws Exception {
        HttpServletRequest request = getRequest();
        if (CommonFileUtils.getFilesSize(request, "File_Upload") == 0) {
            return "err@请选择要导入的数据信息。";
        }

        String fileName = CommonFileUtils.getOriginalFilename(request, "File_Upload");
        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!prefix.equals("xls") && !prefix.equals("xlsx")) {

            return "err@上传的文件必须是Excel文件.";
        }

        String errInfo = "";
        String ext = ".xls";
        if (fileName.contains(".xlsx")) {
            ext = ".xlsx";
        }


        //设置文件名
        String fileNewName = DateUtils.format(new Date(), "yyyyMMddHHmmss") + ext;

        //文件存放路径
        String filePath = SystemConfig.getPathOfTemp() + "/" + fileNewName;
        try {
            CommonFileUtils.upload(request, "File_Upload", new File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
            return "err@执行失败";
        }

        //从excel里面获得数据表.
        DataTable dt = DBLoad.ReadExcelFileToDataTable(filePath, 0);

        //删除临时文件
        (new File(filePath)).delete();

        if (dt.Rows.size() == 0) {
            return "err@无导入的数据";
        }

        //获得entity.
        FrmBill bill = new FrmBill(this.getFrmID());
        GEEntitys rpts = new GEEntitys(this.getFrmID());
        GEEntity en = new GEEntity(this.getFrmID());


        String noColName = ""; //编号(唯一值)
        String nameColName = ""; //名称
        Map map = en.getEnMap();
        //获取表单的主键，合同类的(合同编号),人员信息类的(身份证号),其他(BillNo)
        boolean isContractBill = false;
        boolean isPersonBill = false;

        if (dt.Columns.contains("合同编号") == true) {
            noColName = "合同编号";
            isContractBill = true;
        } else if (dt.Columns.contains("身份证号") == true) {
            noColName = "身份证号";
            isPersonBill = true;
        } else {

            Attr attr = map.GetAttrByKey("BillNo");
            noColName = attr.getDesc();
            attr = map.GetAttrByKey("Title");
            nameColName = attr.getDesc();
        }


        String codeStruct = bill.getEnMap().getCodeStruct();


        //定义属性.
        Attrs attrs = map.getAttrs();

        int impWay = this.GetRequestValInt("ImpWay");


        ///#region 清空方式导入.
        //清空方式导入.
        int count = 0; //导入的行数
        int changeCount = 0; //更新的行数
        String successInfo = "";
        if (impWay == 0) {
            rpts.ClearTable();
            GEEntity myen = new GEEntity(this.getFrmID());

            for (DataRow dr : dt.Rows) {
                //如果是实体单据,导入的excel必须包含BillNo
                if (bill.getEntityType() == EntityType.FrmDict && dt.Columns.contains(noColName) == false) {
                    return "err@导入的excel不包含编号列";
                }
                String no = "";
                if (dt.Columns.contains(noColName) == true) {
                    no = dr.getValue(noColName).toString();
                }
                String name = "";
                if (dt.Columns.contains(nameColName) == true) {
                    name = dr.getValue(nameColName).toString();
                }
                myen.setOID(0);

                if (isContractBill == false && isPersonBill == false) {
                    //判断是否是自增序列，序列的格式
                    if (DataType.IsNullOrEmpty(codeStruct) == false && DataType.IsNullOrEmpty(no) == false) {
                        no = StringHelper.padLeft(no, Integer.parseInt(codeStruct), '0');
                    }

                    myen.SetValByKey("BillNo", no);
                    if (bill.getEntityType() == EntityType.FrmDict) {
                        if (myen.Retrieve("BillNo", no) == 1) {
                            errInfo += "err@编号[" + no + "][" + name + "]重复.";
                            continue;
                        }
                    }
                }


                //给实体赋值
                errInfo += SetEntityAttrValForASSET(no, dr, attrs, myen, dt, 0, bill);
                count++;
                successInfo += "&nbsp;&nbsp;<span>" + noColName + "为" + no + "," + nameColName + "为" + name + "的导入成功</span><br/>";
            }
        }


        ///#endregion 清空方式导入.


        ///#region 更新方式导入
        if (impWay == 1 || impWay == 2) {
            for (DataRow dr : dt.Rows) {
                //如果是实体单据,导入的excel必须包含BillNo
                if (bill.getEntityType() == EntityType.FrmDict && dt.Columns.contains(noColName) == false) {
                    return "err@导入的excel不包含编号列";
                }
                String no = "";
                if (dt.Columns.contains(noColName) == true) {
                    no = dr.getValue(noColName).toString();
                }

                String name = "";
                if (dt.Columns.contains(nameColName) == true) {
                    name = dr.getValue(nameColName).toString();
                }

                bp.en.Entity tempVar = rpts.getNewEntity();
                GEEntity myen = tempVar instanceof GEEntity ? (GEEntity) tempVar : null;
                //合同类
                if (isContractBill == true || isPersonBill == true) {
                    Attr attr = map.GetAttrByDesc(noColName);
                    myen.SetValByKey(attr.getKey(), no);
                    //存在就编辑修改数据
                    if (myen.Retrieve(attr.getKey(), no) == 1) {
                        //给实体赋值
                        errInfo += SetEntityAttrValForASSET(no, dr, attrs, myen, dt, 1, bill);
                        changeCount++;
                        successInfo += "&nbsp;&nbsp;<span>" + noColName + "为" + no + "," + nameColName + "为" + name + "的更新成功</span><br/>";
                        continue;
                    } else {
                        //给实体赋值
                        errInfo += SetEntityAttrValForASSET(no, dr, attrs, myen, dt, 0, bill);
                        count++;
                        successInfo += "&nbsp;&nbsp;<span>" + noColName + "为" + no + "," + nameColName + "为" + name + "的导入成功</span><br/>";
                        continue;
                    }

                } else {
                    //判断是否是自增序列，序列的格式
                    if (DataType.IsNullOrEmpty(codeStruct) == false && DataType.IsNullOrEmpty(no) == false) {
                        no = StringHelper.padLeft(no, Integer.parseInt(codeStruct), '0');
                    }
                    myen.SetValByKey("BillNo", no);
                    if (myen.Retrieve("BillNo", no) == 1 && bill.getEntityType() == EntityType.FrmDict) {
                        //给实体赋值
                        errInfo += SetEntityAttrValForASSET(no, dr, attrs, myen, dt, 1, bill);
                        changeCount++;
                        successInfo += "&nbsp;&nbsp;<span>" + noColName + "为" + no + "," + nameColName + "为" + name + "的更新成功</span><br/>";
                        continue;
                    }
                }

                //给实体赋值
                errInfo += SetEntityAttrValForASSET(no, dr, attrs, myen, dt, 0, bill);
                count++;
                successInfo += "&nbsp;&nbsp;<span>" + noColName + "为" + no + "," + nameColName + "为" + name + "的导入成功</span><br/>";
            }
        }

        ///#endregion

        return "errInfo=" + errInfo + "@Split" + "count=" + count + "@Split" + "successInfo=" + successInfo + "@Split" + "changeCount=" + changeCount;
    }

    private String SetEntityAttrValForASSET(String no, DataRow dr, Attrs attrs, GEEntity en, DataTable dt, int saveType, FrmBill fbill) throws Exception {

        //单据数据不存在
        if (saveType == 0) {
            long oid = 0;
            if (fbill.getEntityType() == EntityType.FrmDict) {
                oid = bp.ccbill.Dev2Interface.CreateBlankDictID(fbill.getNo(), WebUser.getNo(), null);
            }
            if (fbill.getEntityType() == EntityType.FrmBill) {
                oid = bp.ccbill.Dev2Interface.CreateBlankBillID(fbill.getNo(), WebUser.getNo(), null);
            }
            en.setOID(oid);
            en.RetrieveFromDBSources();
        }

        String errInfo = "";
        //按照属性赋值.
        for (Attr item : attrs) {
            if (item.getKey().equals("BillNo") && dt.Columns.contains(item.getDesc()) == true) {
                en.SetValByKey(item.getKey(), no);
                continue;
            }
            if (item.getKey().equals("Title") && dt.Columns.contains(item.getDesc()) == true) {
                en.SetValByKey(item.getKey(), dr.getValue(item.getDesc()).toString());
                continue;
            }

            if (dt.Columns.contains(item.getDesc()) == false) {
                continue;
            }
            String val = dr.getValue(item.getDesc()).toString();
            //枚举处理.
            if (item.getMyFieldType() == FieldType.Enum) {
                SysEnum se = new SysEnum();
                int i = se.Retrieve(SysEnumAttr.EnumKey, item.getUIBindKey(), SysEnumAttr.Lab, val);

                if (i == 0) {
                    errInfo += "err@枚举[" + item.getKey() + "][" + item.getDesc() + "]，值[" + val + "]不存在.";
                    continue;
                }

                en.SetValByKey(item.getKey(), se.getIntKey());
                //en.SetValByKey(item.getKey().replace("Code",""), val);
                continue;
            }


            //外键处理.
            if (item.getMyFieldType() == FieldType.FK) {
                Entity attrEn = item.getHisFKEn();
                int i = attrEn.Retrieve("Name", val);
                if (i == 0) {
                    errInfo += "err@外键[" + item.getKey() + "][" + item.getDesc() + "]，值[" + val + "]不存在.";
                    continue;
                }

                if (i != 1) {
                    errInfo += "err@外键[" + item.getKey() + "][" + item.getDesc() + "]，值[" + val + "]重复..";
                    continue;
                }

                //把编号值给他.
                en.SetValByKey(item.getKey(), attrEn.GetValByKey("No"));
                if (item.getKey().endsWith("BaseCode") == true) {
                    en.SetValByKey(item.getKey().replace("BaseCode", "BaseName"), val);
                } else {
                    en.SetValByKey(item.getKey().replace("Code", ""), val);
                }
                continue;
            }
            //外部数据源
            if (item.getMyFieldType() == FieldType.Normal && item.getMyDataType() == DataType.AppString && item.getUIContralType() == UIContralType.DDL) {
                String uiBindKey = item.getUIBindKey();
                if (DataType.IsNullOrEmpty(uiBindKey) == true) {
                    errInfo += "err@外部数据源[" + item.getKey() + "][" + item.getDesc() + "]，绑定的外键为空";
                }
                DataTable mydt = bp.pub.PubClass.GetDataTableByUIBineKey(uiBindKey, null);
                if (mydt.Rows.size() == 0) {
                    errInfo += "err@外部数据源[" + item.getKey() + "][" + item.getDesc() + "],对应的外键没有获取到外键列表";
                }
                boolean isHave = false;

                //给赋值名称
                if (item.getKey().endsWith("BaseCode") == true) {
                    en.SetValByKey(item.getKey().replace("BaseCode", "BaseName"), val);
                } else {
                    en.SetValByKey(item.getKey().replace("Code", ""), val);
                }

                en.SetValByKey(item.getKey() + "T", val);
                for (DataRow mydr : mydt.Rows) {
                    if (mydr.getValue("Name").toString().equals(val) == true) {
                        en.SetValByKey(item.getKey(), mydr.getValue("No").toString());
                        isHave = true;
                        break;
                    }
                }

                if (isHave == false) {
                    errInfo += "err@外部数据源[" + item.getKey() + "][" + item.getDesc() + "],没有获取到" + val + "对应的Code值";
                }


                continue;
            }

            //boolen类型的处理..
            if (item.getMyDataType() == DataType.AppBoolean) {
                if (Objects.equals(val, "是") || Objects.equals(val, "有")) {
                    en.SetValByKey(item.getKey(), 1);
                } else {
                    en.SetValByKey(item.getKey(), 0);
                }
                continue;
            }
            if (item.getMyDataType() == DataType.AppDate) {
                if (DataType.IsNullOrEmpty(val) == false) {

                }

            }

            if (item.getKey().endsWith("BaseName") == true) {
                bp.port.Depts depts = new bp.port.Depts();
                depts.Retrieve(bp.port.DeptAttr.Name, val, null);
                if (!depts.isEmpty()) {
                    en.SetValByKey(item.getKey().replace("BaseName", "BaseCode"), (depts.get(0) instanceof bp.port.Dept ? (bp.port.Dept) depts.get(0) : null).getNo());
                }
                en.SetValByKey(item.getKey(), val);
                continue;
            } else {
                if (item.getKey().equals("CI_SmallBusinessFormatCode")) {
                    String mypk = "MultipleChoiceSmall_" + fbill.getNo() + "_" + item.getKey();
                    MapExt mapExt = new MapExt();
                    mapExt.setMyPK(mypk);
                    if (mapExt.RetrieveFromDBSources() == 1 && mapExt.getDoWay().equals("3") && DataType.IsNullOrEmpty(mapExt.getTag3()) == false) {
                        String newVal = "," + val + ",";
                        String keyVal = "";
                        DataTable dataTable = bp.pub.PubClass.GetDataTableByUIBineKey(mapExt.getTag3(), null);
                        for (DataRow drr : dataTable.Rows) {
                            if (drr.getValue("Name") != null && newVal.contains("," + drr.getValue("Name").toString() + ",") == true) {
                                keyVal += drr.getValue("No").toString() + ",";
                            }
                        }
                        keyVal = keyVal.substring(0, keyVal.length() - 1);

                        en.SetValByKey(item.getKey(), keyVal);
                        en.SetValByKey(item.getKey().replace("Code", ""), val);
                        en.SetValByKey(item.getKey() + "T", val);
                    } else {
                        en.SetValByKey(item.getKey(), val);
                    }
                } else {
                    if (item.getItIsNum()) {
                        if (DataType.IsNullOrEmpty(val) == true || val.equals("null") == true) {
                            val = "0";
                        }
                    }
                    en.SetValByKey(item.getKey(), val);
                }


            }


        }
        if (DataType.IsNullOrEmpty(en.GetValStrByKey("BillNo")) == true && DataType.IsNullOrEmpty(fbill.getBillNoFormat()) == false) {
            en.SetValByKey("BillNo", Dev2Interface.GenerBillNo(fbill.getBillNoFormat(), en.getOID(), en, fbill.getNo()));
        }

        if (DataType.IsNullOrEmpty(en.GetValStrByKey("Title")) == true && DataType.IsNullOrEmpty(fbill.getTitleRole()) == false) {
            en.SetValByKey("Title", Dev2Interface.GenerTitle(fbill.getTitleRole(), en));
        }
        en.SetValByKey("Rec", WebUser.getNo());
        en.SetValByKey("BillState", BillState.Editing.getValue());
        en.SetValByKey("WFState", WFState.Complete);
        en.Update();

        GenerBill gb = new GenerBill();
        gb.setWorkID(en.getOID());
        if (gb.RetrieveFromDBSources() == 0) {
            gb.setBillState(BillState.Over); //初始化状态.
            gb.setStarter(WebUser.getNo());
            gb.setStarterName(WebUser.getName());
            gb.setFrmName(fbill.getName()); //单据名称.
            gb.setFrmID(fbill.getNo()); //单据ID
            if (en.getRow().containsKey("Title") == true) {
                gb.setTitle(en.GetValStringByKey("Title"));
            }
            if (en.getRow().containsKey("BillNo") == true) {
                gb.setBillNo(en.GetValStringByKey("BillNo"));
            }
            gb.setFrmTreeNo(fbill.getFormTreeNo()); //单据类别.
            gb.setRDT(DataType.getCurrentDateTime());
            gb.setNDStep(1);
            gb.setNDStepName("启动");
            gb.Insert();

        } else {
            gb.setBillState(BillState.Editing);
            if (en.getRow().containsKey("Title") == true) {
                gb.setTitle(en.GetValStringByKey("Title"));
            }
            if (en.getRow().containsKey("BillNo") == true) {
                gb.setBillNo(en.GetValStringByKey("BillNo"));
            }
            gb.Update();
        }

        return errInfo;
    }


    ///#region 执行父类的重写方法.

    /**
     * 默认执行的方法
     *
     * @return
     */
    @Override
    protected String DoDefaultMethod() {
        switch (this.getDoType()) {
            case "DtlFieldUp": //字段上移
                return "执行成功.";
            default:
                break;
        }

        //找不不到标记就抛出异常.
        throw new RuntimeException("@标记[" + this.getDoType() + "]，没有找到. @RowURL:" + getRequest().getRequestURL().toString() + "?" + getRequest().getQueryString());
    }

    ///#endregion 执行父类的重写方法.


    ///#region 获得demo信息.
    public final String MethodDocDemoJS_Init() throws Exception {
        MethodFunc func = new MethodFunc(this.getMyPK());
        return func.getMethodDocJavaScriptDemo();
    }

    public final String MethodDocDemoSQL_Init() throws Exception {
        MethodFunc func = new MethodFunc(this.getMyPK());
        return func.getMethodDocSQLDemo();
    }

    ///#endregion 获得demo信息.


    ///#region 处理SQL文中注释信息.
    public static String MidStrEx(String sourse, String startstr, String endstr) {
        int startindex, endindex;
        String tmpstr = "";
        String tmpstr2 = "";
        try {
            startindex = sourse.indexOf(startstr);
            if (startindex == -1) {
                return sourse;
            }
            int i = 0;
            while (startindex != -1) {
                if (i == 0) {
                    endindex = sourse.indexOf(endstr);
                    if (startindex != 0) {
                        endindex = endindex - startindex;
                    }
                    tmpstr = StringHelper.remove(sourse, startindex, endindex + endstr.length());
                } else {
                    endindex = tmpstr.indexOf(endstr);
                    if (startindex != 0) {
                        endindex = endindex - startindex;
                    }
                    tmpstr = StringHelper.remove(tmpstr, startindex, endindex + endstr.length());

                }

                if (endindex == -1) {
                    return tmpstr;
                }
                // tmpstr = tmpstr.Substring(endindex + endstr.length());
                startindex = tmpstr.indexOf(startstr);
                i++;
            }
            //result = tmpstr.Remove(endindex);

        } catch (RuntimeException ex) {
            Log.DebugWriteError("MidStrEx Err:" + ex.getMessage());
        }
        return tmpstr;
    }

    ///#endregion 处理SQL文中注释信息..


    ///#region 实体单据查询启动指定子流程显示的字段
    public final String DictFlow_MapAttrs() throws Exception {
        DataSet ds = new DataSet();
        String fk_mapData = "ND" + Integer.parseInt(this.getFlowNo()) + "01";

        //查询出单流程的所有字段
        MapAttrs mattrs = new MapAttrs();
        mattrs.Retrieve(MapAttrAttr.FK_MapData, fk_mapData, MapAttrAttr.Idx);

        ds.Tables.add(mattrs.ToDataTableField("Sys_MapAttr"));

        MapAttrs mattrsOfSystem1 = new MapAttrs();
        //判断表单中是否存在默认值@WebUser.getNo(),@WebUser.FK_Dept,@RDT
        boolean isHaveNo = false;
        boolean isHaveRDT = false;
        boolean isHaveTitle = false;

        //系统字段字符串
        String sysFields = "";
        for (MapAttr mapAttr : mattrs.ToJavaList()) {

            if (mapAttr.getKeyOfEn().equals(GERptAttr.Rec) || mapAttr.getKeyOfEn().equals(GERptAttr.RDT) || mapAttr.getKeyOfEn().equals(GERptAttr.CDT)) {
                continue;
            }
            if (mapAttr.getKeyOfEn().equals(GERptAttr.Title) == true) {
                mattrsOfSystem1.AddEntity(mapAttr);
                isHaveTitle = true;
                continue;
            }

            switch (mapAttr.getDefValReal()) {

                case "@WebUser.No":
                case "@WebUser.Name":
                    sysFields += "," + mapAttr.getKeyOfEn();
                    isHaveNo = true;
                    mattrsOfSystem1.AddEntity(mapAttr);

                    break;

                case "@RDT":
                    mattrsOfSystem1.AddEntity(mapAttr);
                    isHaveRDT = true;
                    sysFields += "," + mapAttr.getKeyOfEn();
                    break;
                default:
                    break;
            }
        }


        //默认显示的系统字段 标题、发起人、发起时间、当前所在节点、状态 , 系统字段需要在RPT中查找
        String fields = "(";
        if (isHaveTitle == false) {
            fields += "'" + GERptAttr.Title + "',";
        }
        if (isHaveNo == false) {
            fields += "'" + GERptAttr.FlowStarter + "',";
        }

        if (isHaveRDT == false) {
            fields += "'" + GERptAttr.FlowStartRDT + "',";
        }
        fields += "'" + GERptAttr.WFState + "','" + GERptAttr.FlowEndNode + "')";
        MapAttrs mattrsOfSystem = new MapAttrs();
        QueryObject qo = new QueryObject(mattrsOfSystem);
        qo.AddWhere(MapAttrAttr.FK_MapData, "ND" + Integer.parseInt(this.getFlowNo()) + "Rpt");
        qo.addAnd();
        qo.AddWhereIn(MapAttrAttr.KeyOfEn, fields);
        //qo.addOrderBy(MapAttrAttr.Idx);
        //qo.addOrderByOfSelf("CHARINDEX(" + MapAttrAttr.getKeyOfEn() + ",'" + fields.replace("'", "") + "')");
        qo.DoQuery();
        mattrsOfSystem.AddEntities(mattrsOfSystem1);

        ds.Tables.add(mattrsOfSystem.ToDataTableField("Sys_MapAttrOfSystem"));

        //系统字段字符串
        fields = fields.replace("(", "").replace(")", "").replace("'", "") + ",";
        sysFields += ",OID,FID,RDT,CDT,Rec,FK_Dept,MyNum,FK_NY,Emps,Title," + fields;
        DataTable dt = new DataTable();
        dt.Columns.Add("Field");
        dt.TableName = "Sys_Fields";
        DataRow dr = dt.NewRow();
        dr.setValue("Field", sysFields);
        dt.Rows.add(dr);
        ds.Tables.add(dt);

        //用户查询注册信息中记录使用到的流程业务表中的字段
        UserRegedit ur = new UserRegedit(WebUser.getNo(), "ND" + Integer.parseInt(this.getFlowNo()) + "Rpt_SearchAttrs");
        ur.SetPara("RptField", "," + fields);
        ur.Update();

        return bp.tools.Json.ToJson(ds);
    }

    ///#endregion


    ///#region 实体单据启动多个子流程的查询
    public final String DictFlow_Search() throws Exception {
        //实体单据的信息
        String frmID = this.GetRequestVal("FrmID");
        String frmOID = this.GetRequestVal("FrmOID");

        //表单编号
        String fk_mapData = "ND" + Integer.parseInt(this.getFlowNo()) + "01";

        //当前用户查询信息表
        UserRegedit ur = new UserRegedit(WebUser.getNo(), "ND" + Integer.parseInt(this.getFlowNo()) + "Rpt_SearchAttrs");

        //表单属性
        MapData mapData = new MapData(fk_mapData);

        //流程的系统字段
        String rptFields = ur.GetParaString("RptField");
        rptFields = rptFields.substring(1, rptFields.length());
        rptFields = "('" + rptFields.replace(",", "','") + "'" + ",'" + GERptAttr.FlowStarter + "','" + GERptAttr.FK_Dept + "','" + GERptAttr.FlowEmps + "','" + GERptAttr.FlowEndNode + "','" + GERptAttr.PWorkID + "','" + GERptAttr.PFlowNo + "')";
        MapAttrs mattrsOfSystem = new MapAttrs();
        QueryObject qo = new QueryObject(mattrsOfSystem);
        qo.AddWhere(MapAttrAttr.FK_MapData, "ND" + Integer.parseInt(this.getFlowNo()) + "Rpt");
        qo.addAnd();
        qo.AddWhereIn(MapAttrAttr.KeyOfEn, rptFields);
        qo.DoQuery();

        //流程表单对应的所有字段
        MapAttrs attrs = new MapAttrs();
        attrs.Retrieve(MapAttrAttr.FK_MapData, fk_mapData, MapAttrAttr.Idx);
        attrs.AddEntities(mattrsOfSystem);

        //流程表单对应的流程数据
        GEEntitys ens = new GEEntitys(fk_mapData);
        bp.en.Entity tempVar = ens.getNewEntity();
        GEEntity en = tempVar instanceof GEEntity ? (GEEntity) tempVar : null;
        for (MapAttr mapAttr : mattrsOfSystem.ToJavaList()) {
            en.getEnMap().AddAttr(mapAttr.getHisAttr());
        }
        Cache.getSQL_Cache().remove(fk_mapData);

        qo = new QueryObject(ens);
        qo.AddWhere(GERptAttr.PWorkID, frmOID);
        qo.addAnd();
        qo.AddWhere(GERptAttr.PFlowNo, frmID);
        qo.AddWhere(" AND  WFState > 1 ");
        //qo.addAnd();
        qo.AddWhere(" AND FID = 0 ");
        if (DataType.IsNullOrEmpty(ur.getOrderBy()) == false) {
            if (ur.getOrderWay().toUpperCase().equals("DESC") == true) {
                qo.addOrderByDesc(ur.getOrderBy());
            } else {
                qo.addOrderBy(ur.getOrderBy());
            }
        }
        ur.Update();
        qo.DoQuery();

        return bp.tools.Json.ToJson(ens.ToDataTableField("FlowSearch_Data"));
    }

    ///#endregion  实体单据启动多个子流程的查询

    public final String RefDict_CreateBillWorkID() throws Exception {
        long refOID = GetRequestValInt64("RefOID");
        String refDict = GetRequestVal("RefDict");
        //获取关联实体表单的数据信息
        GERpt refRpt = new GERpt(refDict, refOID);
        String billNo = this.GetRequestVal("BillNo");
        long workID = bp.ccbill.Dev2Interface.CreateBlankBillID(this.getFrmID(), WebUser.getNo(), null, billNo);

        GenerBill gb = new GenerBill(workID);
        gb.setBillState(BillState.Draft);
        gb.Update();
        //获取当前单据表单的数据信息
        GERpt rpt = new GERpt(this.getFrmID(), workID);
        rpt.Copy(refRpt);
        rpt.SetValByKey("BillState", gb.getBillState().getValue());
        rpt.Update();
        return String.valueOf(workID);
    }


    ///#region 外部流程网页授权URL
    public final String DictFlow_Qcode() throws Exception {
        String state = "FlowNo_" + this.getFlowNo() + "|OrgNo_" + WebUser.getOrgNo() + "|FrmID_" + this.getFrmID() + "|FrmOID_" + this.GetRequestVal("FrmOID");
        //回调url
        String redirect_uri = Encodes.urlEncode("http://www.ccbpm.cn/WF/CCBill/DictFlowStart.htm");
        //授权链接
        String oatuth2 = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + SystemConfig.getAppID() + "&redirect_uri=" + redirect_uri + "&response_type=code&scope=snsapi_userinfo&&state=" + state + "#wechat_redirect";
        return oatuth2;
    }

    ///#endregion 外部流程网页授权URL
}
