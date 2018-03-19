package BP.WF.HttpHandler;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import BP.DA.Cash;
import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.DataType;
import BP.En.Entities;
import BP.En.EntityMultiTree;
import BP.En.FieldTypeS;
import BP.En.QueryObject;
import BP.Port.Depts;
import BP.Port.Emp;
import BP.Port.Station;
import BP.Port.Stations;
import BP.Sys.DTSearchWay;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapDataAttr;
import BP.Sys.MapDatas;
import BP.Sys.SysEnums;
import BP.Sys.SystemConfig;
import BP.WF.DeliveryWay;
import BP.WF.DotNetToJavaStringHelper;
import BP.WF.Flow;
import BP.WF.Glo;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.HttpHandler.Base.WebContralBase;
import BP.WF.Rpt.RptDfine;
import BP.WF.Template.Cond;
import BP.WF.Template.CondAttr;
import BP.WF.Template.CondOrAnd;
import BP.WF.Template.CondType;
import BP.WF.Template.Conds;
import BP.WF.Template.ConnDataFrom;
import BP.WF.Template.FlowFormTrees;
import BP.WF.Template.FrmEnableRole;
import BP.WF.Template.FrmNode;
import BP.WF.Template.FrmNodeAttr;
import BP.WF.Template.FrmNodeExt;
import BP.WF.Template.FrmNodeExts;
import BP.WF.Template.FrmNodes;
import BP.WF.Template.SQLTemplate;
import BP.WF.Template.SQLTemplates;
import BP.WF.Template.SpecOperWay;
import BP.WF.Template.SysFormTree;
import BP.WF.Template.SysFormTreeAttr;
import BP.WF.Template.SysFormTrees;

/** 
 页面功能实体
 
*/
public class WF_Admin_RptDfine extends WebContralBase
{
	/** 
	 初始化方法
	 
	 @return 
	*/
	public final String S2ColsChose_Init()
	{
		DataSet ds = new DataSet();
		String rptNo = this.GetRequestVal("RptNo");

		//所有的字段.
		String fk_mapdata = "ND"+Integer.parseInt(this.getFK_Flow())+"Rpt";
		MapAttrs mattrs = new MapAttrs(fk_mapdata);
		ds.Tables.add(mattrs.ToDataTableField("Sys_MapAttrOfAll"));

		//判断rptNo是否存在于mapdata中
		MapData md = new MapData();
		md.setNo(rptNo);
		if(md.RetrieveFromDBSources() == 0)
		{
			RptDfine rd = new RptDfine(this.getFK_Flow());


//			switch(rptNo.Substring(fk_mapdata.Length))
//ORIGINAL LINE: case "My":
			if (rptNo.substring(fk_mapdata.length()).equals("My"))
			{
					rd.DoReset_MyStartFlow();
			}
//ORIGINAL LINE: case "MyDept":
			else if (rptNo.substring(fk_mapdata.length()).equals("MyDept"))
			{
					rd.DoReset_MyDeptFlow();
			}
//ORIGINAL LINE: case "MyJoin":
			else if (rptNo.substring(fk_mapdata.length()).equals("MyJoin"))
			{
					rd.DoReset_MyJoinFlow();
			}
//ORIGINAL LINE: case "Adminer":
			else if (rptNo.substring(fk_mapdata.length()).equals("Adminer"))
			{
					rd.DoReset_AdminerFlow();
			}
			else
			{
					throw new RuntimeException("@未涉及的rptMark类型");
			}

			md.RetrieveFromDBSources();
		}

		//选择的字段,就是报表的字段.
		MapAttrs mattrsOfRpt = new MapAttrs(rptNo);
		ds.Tables.add(mattrsOfRpt.ToDataTableField("Sys_MapAttrOfSelected"));

		//系统字段.
		MapAttrs mattrsOfSystem = new MapAttrs();
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java:
		ArrayList<String> sysFields = BP.WF.Glo.getFlowFields();
		for (MapAttr item : mattrs.ToJavaList())
		{
			if (sysFields.contains(item.getKeyOfEn()))
			{
				mattrsOfSystem.AddEntity(item);
			}
		}
		ds.Tables.add(mattrsOfSystem.ToDataTableField("Sys_MapAttrOfSystem"));

		//返回.
		return BP.Tools.Json.DataSetToJson(ds, false);
	}
	/** 
	 选择列的保存.
	 
	 @return 
	*/
	public final String S2ColsChose_Save()
	{
		//报表列表.
		String rptNo = this.GetRequestVal("RptNo");

		//保存的字段,从外面传递过来的值. 用逗号隔开的: 比如:  ,Name,Tel,Addr,
		String fields = ","+this.GetRequestVal("Fields")+",";

		//构造一个空的集合.
		MapAttrs mrattrsOfRpt = new MapAttrs();
		mrattrsOfRpt.Delete(MapAttrAttr.FK_MapData, rptNo);

		//所有的字段.
		String fk_mapdata = "ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt";
		MapAttrs allAttrs = new MapAttrs(fk_mapdata);

		for (MapAttr attr : allAttrs.ToJavaList())
		{

				///#region 处理特殊字段.
			if (attr.getKeyOfEn().equals("FK_NY"))
			{
				attr.setLGType(BP.En.FieldTypeS.FK);
				attr.setUIBindKey("BP.Pub.NYs");
				attr.setUIContralType(BP.En.UIContralType.DDL);
			}

			if (attr.getKeyOfEn().equals("FK_Dept"))
			{
				attr.setLGType(BP.En.FieldTypeS.FK);
				attr.setUIBindKey("BP.Port.Depts");
				attr.setUIContralType(BP.En.UIContralType.DDL);
			}

				///#endregion 处理特殊字段.

			//增加上必要的字段.
			if (attr.getKeyOfEn().equals("Title") || attr.getKeyOfEn().equals("WorkID") || attr.getKeyOfEn().equals("OID"))
			{
				attr.setFK_MapData(rptNo);
				attr.setMyPK(attr.getFK_MapData() + "_" + attr.getKeyOfEn());
				attr.DirectInsert();
				continue;
			}

			//如果包含了指定的字段，就执行插入操作.
			if (fields.contains("," + attr.getKeyOfEn() + ",") == true)
			{
				attr.setFK_MapData(rptNo);
				attr.setMyPK(attr.getFK_MapData() + "_" + attr.getKeyOfEn());
				attr.DirectInsert();
			}
		}

		return "保存成功.";
	}

		///#endregion


		///#region 报表设计器. - 第3步设置列的顺序.
	/** 
	 初始化方法
	 
	 @return 
	*/
	public final String S3ColsLabel_Init()
	{
		String rptNo = this.GetRequestVal("RptNo");

		//判断rptNo是否存在于mapdata中
		MapData md = new MapData();
		md.setNo(rptNo);
		if (md.RetrieveFromDBSources() == 0)
		{
			RptDfine rd = new RptDfine(this.getFK_Flow());


//			switch (rptNo.Substring(("ND" + int.Parse(this.getFK_Flow()) + "Rpt").Length))
//ORIGINAL LINE: case "My":
			if (rptNo.substring(("ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt").length()).equals("My"))
			{
					rd.DoReset_MyStartFlow();
			}
//ORIGINAL LINE: case "MyDept":
			else if (rptNo.substring(("ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt").length()).equals("MyDept"))
			{
					rd.DoReset_MyDeptFlow();
			}
//ORIGINAL LINE: case "MyJoin":
			else if (rptNo.substring(("ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt").length()).equals("MyJoin"))
			{
					rd.DoReset_MyJoinFlow();
			}
//ORIGINAL LINE: case "Adminer":
			else if (rptNo.substring(("ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt").length()).equals("Adminer"))
			{
					rd.DoReset_AdminerFlow();
			}
			else
			{
					throw new RuntimeException("@未涉及的rptMark类型");
			}

			md.RetrieveFromDBSources();
		}

		//选择的字段,就是报表的字段.
		MapAttrs mattrsOfRpt = new MapAttrs(rptNo);
		mattrsOfRpt.RemoveEn(rptNo + "_OID");
		mattrsOfRpt.RemoveEn(rptNo + "_Title");

		return mattrsOfRpt.ToJson();
	}
	/** 
	 保存列的顺序名称.
	 
	 @return 
	*/
	public final String S3ColsLabel_Save()
	{
		String orders = this.GetRequestVal("Orders");
		//格式为  @KeyOfEn,Lable,idx  比如： @DianHua,电话,1@Addr,地址,2

		String rptNo=this.GetRequestVal("RptNo");

		String[] strs = orders.split("[@]", -1);
		for (String item : strs)
		{
			if (DotNetToJavaStringHelper.isNullOrEmpty(item) == true)
			{
				continue;
			}

			String[] vals = item.split("[,]", -1);

			String mypk = rptNo + "_" + vals[0];

			MapAttr attr = new MapAttr();
			attr.setMyPK(mypk);
			attr.Retrieve();

			attr.setName(vals[1]);
			attr.setIdx(Integer.parseInt(vals[2]));

			attr.Update(); //执行更新.
		}

		MapAttr myattr = new MapAttr();
		myattr.setMyPK(rptNo+"_OID");
		myattr.RetrieveFromDBSources();
		myattr.setIdx(200);
		myattr.setName("工作ID");
		myattr.Update();

		myattr = new MapAttr();
		myattr.setMyPK(rptNo + "_Title");
		myattr.RetrieveFromDBSources();
		myattr.setIdx(-100);
		myattr.setName("标题");
		myattr.Update();

		return "保存成功..";
	}

		///#endregion


		///#region 报表设计器 - 第4步骤.
	public final String S5SearchCond_Init()
	{
		//报表编号.
		String rptNo = this.GetRequestVal("RptNo");

		//定义容器.
		DataSet ds = new DataSet();

		//判断rptNo是否存在于mapdata中
		MapData md = new MapData(rptNo);
		if (md.RetrieveFromDBSources2017() == 0)
		{
			RptDfine rd = new RptDfine(this.getFK_Flow());


//			switch (rptNo.Substring(("ND" + int.Parse(this.getFK_Flow()) + "Rpt").Length))
//ORIGINAL LINE: case "My":
			if (rptNo.substring(("ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt").length()).equals("My"))
			{
					rd.DoReset_MyStartFlow();
			}
//ORIGINAL LINE: case "MyDept":
			else if (rptNo.substring(("ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt").length()).equals("MyDept"))
			{
					rd.DoReset_MyDeptFlow();
			}
//ORIGINAL LINE: case "MyJoin":
			else if (rptNo.substring(("ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt").length()).equals("MyJoin"))
			{
					rd.DoReset_MyJoinFlow();
			}
//ORIGINAL LINE: case "Adminer":
			else if (rptNo.substring(("ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt").length()).equals("Adminer"))
			{
					rd.DoReset_AdminerFlow();
			}
			else
			{
					throw new RuntimeException("@未涉及的rptMark类型");
			}

			md.RetrieveFromDBSources2017();
		}

		ds.Tables.add(md.ToDataTableField("Main"));

		//查询出来枚举与外键类型的字段集合.
		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.FK_MapData, rptNo);
		ds.Tables.add(attrs.ToDataTableField("Sys_MapAttr"));


			///#region 检查是否有日期字段.
		boolean isHave = false;
		for (MapAttr mattr : attrs.ToJavaList())
		{
			if (mattr.getUIVisible() == false)
			{
				continue;
			}

			if (mattr.getMyDataType() == DataType.AppDate || mattr.getMyDataType() == DataType.AppDateTime)
			{
				isHave = true;
				break;
			}
		}

		if (isHave == true)
		{
			DataTable dt = new DataTable();
			MapAttrs dtAttrs = new MapAttrs();
			for (MapAttr mattr : attrs.ToJavaList())
			{
				if (mattr.getMyDataType() == DataType.AppDate || mattr.getMyDataType() == DataType.AppDateTime)
				{
					if (mattr.getUIVisible() == false)
					{
						continue;
					}
					dtAttrs.AddEntity(mattr);
				}
			}
			ds.Tables.add(dtAttrs.ToDataTableField("Sys_MapAttrOfDate"));
		}

			///#endregion

		//返回数据.
		return BP.Tools.Json.DataSetToJson(ds, false);
	}
	public final String getRptNo()
	{
		return this.GetRequestVal("RptNo");
	}
	/** 
	 查询条件保存.
	 
	 @return 
	*/
	public final String S5SearchCond_Save()
	{
		MapData md = new MapData();
		md.setNo(this.getRptNo());
		md.RetrieveFromDBSources();

		//报表编号.
		String fields = this.GetRequestVal("Fields");
		md.setRptSearchKeys(fields + "*");

		String IsSearchKey = this.GetRequestVal("IsSearchKey");
		if (IsSearchKey.equals("0"))
		{
			md.setRptIsSearchKey(false);
		}
		else
		{
			md.setRptIsSearchKey(true);
		}

		//查询方式.
		int dTSearchWay = this.GetRequestValInt("DTSearchWay");
		md.setRptDTSearchWay(DTSearchWay.forValue(dTSearchWay));

		//日期字段.
		String DTSearchKey = this.GetRequestVal("DTSearchKey");
		md.setRptDTSearchKey(DTSearchKey);
		md.Save();

		Cash.getMap_Cash().remove(this.getRptNo());
		return "保存成功.";
	}
}