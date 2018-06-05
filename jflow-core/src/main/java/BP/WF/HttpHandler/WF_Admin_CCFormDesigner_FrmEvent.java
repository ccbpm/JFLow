package BP.WF.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.http.protocol.HttpContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import BP.DA.DBType;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.Sys.BuessUnitBase;
import BP.Sys.EventDoType;
import BP.Sys.FrmEvent;
import BP.Sys.FrmEventAttr;
import BP.Sys.FrmEvents;
import BP.Sys.FrmLab;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.SFDBSrc;
import BP.Sys.SFTable;
import BP.Sys.SysEnumAttr;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.WF.Node;
import BP.WF.HttpHandler.Base.WebContralBase;
import BP.WF.XML.EventLists;

public class WF_Admin_CCFormDesigner_FrmEvent extends WebContralBase {
    /// <summary>
    /// 初始化数据
    /// </summary>
    /// <param name="mycontext"></param>
    public WF_Admin_CCFormDesigner_FrmEvent(HttpContext mycontext)
    {
        this.context = mycontext;
    }

    //#region 事件基类.
    /// <summary>
    /// 事件类型
    /// </summary>
    public String getShowType(){
    	 if (this.getFK_Node() != 0)
             return "Node";

         if (this.getFK_Node() == 0 && DataType.IsNullOrEmpty(this.getFK_Flow()) == false && this.getFK_Flow().length() >= 3)
             return "Flow";

         if (this.getFK_Node() == 0 && DataType.IsNullOrEmpty(this.getFK_MapData()) == false)
             return "Frm";

         return "Node";
    }

    /// <summary>
    /// 事件基类
    /// </summary>
    /// <returns></returns>
    public String Action_Init()
    {
        DataSet ds = new DataSet();

        //事件实体.
        FrmEvents ndevs = new FrmEvents();
        if (BP.DA.DataType.IsNullOrEmpty(this.getFK_MapData()) == false)
			try {
				ndevs.Retrieve(FrmEventAttr.FK_MapData, this.getFK_MapData());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        ////已经配置的事件类实体.
        //DataTable dtFrm = ndevs.ToDataTableField("FrmEvents");
        //ds.Tables.Add(dtFrm);

        //把事件类型列表放入里面.（发送前，发送成功时.）
        EventLists xmls = new EventLists();
        xmls.Retrieve("EventType", this.getShowType());

        DataTable dt = xmls.ToDataTable();
        dt.TableName = "EventLists";
        //ds.Tables.Add(dt);
        ds.Tables.add(dt);

        return BP.Tools.Json.ToJson(ds);
    }
    /// <summary>
    /// 获得该节点下已经绑定该类型的实体.
    /// </summary>
    /// <returns></returns>
    public String ActionDtl_Init()
    {
        DataSet ds = new DataSet();

        //事件实体.
        FrmEvents ndevs = new FrmEvents();
        try {
			ndevs.Retrieve(FrmEventAttr.FK_MapData, this.getFK_MapData());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        DataTable dt = ndevs.ToDataTableField("FrmEvents");
        //ds.Tables.add(dt);
        ds.Tables.add(dt);

        //业务单元集合.
        DataTable dtBuess = new DataTable();
        dtBuess.Columns.Add("No", toString());
        dtBuess.Columns.Add("Name", toString());
        dtBuess.TableName = "BuessUnits";
        @SuppressWarnings("unchecked")
		ArrayList<BuessUnitBase> al = BP.En.ClassFactory.GetObjects("BP.Sys.BuessUnitBase");
        
        for (BuessUnitBase en : al)
        {
            DataRow dr = dtBuess.NewRow();
            dr.setValue("No", en.toString());
            dr.setValue("Name", en.getTitle());
            //dr["No"] = en.toString();

            dtBuess.Rows.AddRow(dr);
        }

        ds.Tables.add(dtBuess);

        return BP.Tools.Json.ToJson(ds);
    }
    /// <summary>
    /// 执行删除
    /// </summary>
    /// <returns></returns>
    public String ActionDtl_Delete()
    {
        //事件实体.
        FrmEvent en = new FrmEvent();
        en.setMyPK(this.getMyPK()); 
        try {
			en.Delete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return "删除成功.";
    }
    public String ActionDtl_Save()
    {
        //事件实体.
        FrmEvent en = new FrmEvent();

        en.setFK_Node(this.getFK_Node());
        en.setFK_Event(this.GetRequestVal("FK_Event")); //事件类型.
        en.setHisDoTypeInt(this.GetValIntFromFrmByKey("EventDoType")); //执行类型.
        en.setMyPK(this.getFK_Node() + "_" + en.getFK_Event() + "_" + en.getHisDoTypeInt()); //组合主键.
        try {
			en.RetrieveFromDBSources();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        en.setMsgOKString(this.GetValFromFrmByKey("MsgOK")); //成功的消息.
        en.setMsgErrorString(this.GetValFromFrmByKey("MsgError")); //失败的消息.

        //执行内容.
        if (en.getHisDoType() == EventDoType.BuessUnit)
            en.setDoDoc(this.GetValFromFrmByKey("DDL_Doc"));
        else
            en.setDoDoc(this.GetValFromFrmByKey("TB_Doc"));

        try {
			en.Save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return "保存成功.";
    }
    //#endregion 事件基类.
     
}
    
  

