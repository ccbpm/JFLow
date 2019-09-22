package BP.WF.HttpHandler;

import BP.WF.*;
import BP.Web.*;
import BP.Sys.*;
import BP.DA.*;
import BP.En.*;
import BP.WF.Template.*;
import BP.WF.XML.*;
import BP.WF.*;
import java.util.*;

public class WF_Admin_CCFormDesigner_FrmEvent extends BP.WF.HttpHandler.DirectoryPageBase
{

	/** 
	 构造函数
	*/
	public WF_Admin_CCFormDesigner_FrmEvent()
	{
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 事件基类.
	/** 
	 事件类型
	*/
	public final String getShowType()
	{
		if (this.getFK_Node() != 0)
		{
			return "Node";
		}

		if (this.getFK_Node() == 0 && DataType.IsNullOrEmpty(this.getFK_Flow()) == false && this.getFK_Flow().length() >= 3)
		{
			return "Flow";
		}

		if (this.getFK_Node() == 0 && DataType.IsNullOrEmpty(this.getFK_MapData()) == false)
		{
			return "Frm";
		}

		return "Node";
	}
	/** 
	 事件基类
	 
	 @return 
	*/
	public final String Action_Init()
	{
		DataSet ds = new DataSet();

		//事件实体.
		FrmEvents ndevs = new FrmEvents();
		if (BP.DA.DataType.IsNullOrEmpty(this.getFK_MapData()) == false)
		{
			ndevs.Retrieve(FrmEventAttr.FK_MapData, this.getFK_MapData());
		}

		////已经配置的事件类实体.
		//DataTable dtFrm = ndevs.ToDataTableField("FrmEvents");
		//ds.Tables.add(dtFrm);

		//把事件类型列表放入里面.（发送前，发送成功时.）
		EventLists xmls = new EventLists();
		xmls.Retrieve("EventType", this.getShowType());

		DataTable dt = xmls.ToDataTable();
		dt.TableName = "EventLists";
		ds.Tables.add(dt);

		return BP.Tools.Json.ToJson(ds);
	}
	/** 
	 获得该节点下已经绑定该类型的实体.
	 
	 @return 
	*/
	public final String ActionDtl_Init()
	{
		DataSet ds = new DataSet();

		//事件实体.
		FrmEvents ndevs = new FrmEvents();
		ndevs.Retrieve(FrmEventAttr.FK_MapData, this.getFK_MapData());

		DataTable dt = ndevs.ToDataTableField("FrmEvents");
		ds.Tables.add(dt);

		//业务单元集合.
		DataTable dtBuess = new DataTable();
		dtBuess.Columns.Add("No", String.class);
		dtBuess.Columns.Add("Name", String.class);
		dtBuess.TableName = "BuessUnits";
		ArrayList al = BP.En.ClassFactory.GetObjects("BP.Sys.BuessUnitBase");
		for (BuessUnitBase en : al)
		{
			DataRow dr = dtBuess.NewRow();
			dr.set("No", en.toString());
			dr.set("Name", en.Title);
			dtBuess.Rows.add(dr);
		}

		ds.Tables.add(dtBuess);

		return BP.Tools.Json.ToJson(ds);
	}
	/** 
	 执行删除
	 
	 @return 
	*/
	public final String ActionDtl_Delete()
	{
		//事件实体.
		FrmEvent en = new FrmEvent();
		en.setMyPK( this.getMyPK();
		en.Delete();
		return "删除成功.";
	}
	public final String ActionDtl_Save()
	{
		//事件实体.
		FrmEvent en = new FrmEvent();

		en.FK_Node = this.getFK_Node();
		en.FK_Event = this.GetRequestVal("FK_Event"); //事件类型.
		en.HisDoTypeInt = this.GetValIntFromFrmByKey("EventDoType"); //执行类型.
		en.setMyPK( this.getFK_Node() + "_" + en.FK_Event + "_" + en.HisDoTypeInt; //组合主键.
		en.RetrieveFromDBSources();

		en.MsgOKString = this.GetValFromFrmByKey("MsgOK"); //成功的消息.
		en.MsgErrorString = this.GetValFromFrmByKey("MsgError"); //失败的消息.

		//执行内容.
		if (en.HisDoType == EventDoType.BuessUnit)
		{
			en.DoDoc = this.GetValFromFrmByKey("DDL_Doc");
		}
		else
		{
			en.DoDoc = this.GetValFromFrmByKey("TB_Doc");
		}

		en.Save();

		return "保存成功.";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 事件基类.

}