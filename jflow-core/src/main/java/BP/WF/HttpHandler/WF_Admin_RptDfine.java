package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Sys.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import BP.Web.Controls.*;
import BP.WF.Data.*;
import BP.WF.*;

/** 
 页面功能实体
*/
public class WF_Admin_RptDfine extends DirectoryPageBase
{


	/** 
	 构造函数
	*/
	public WF_Admin_RptDfine()
	{
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 执行父类的重写方法.
	/** 
	 默认执行的方法
	 
	 @return 
	*/
	@Override
	protected String DoDefaultMethod()
	{
		String msg = "";
		try
		{
			switch (this.getDoType())
			{
				case "S3ColsLabel_Init": //顺序加载
					msg = this.S3ColsLabel_Init();
					break;
				case "S3ColsLabel_Save": //顺序保存
					msg = this.S3ColsLabel_Save();
					break;
				default:
					msg = "err@没有判断的执行类型：" + this.getDoType();
					break;
			}
			HttpContextHelper.ResponseWrite(msg);
		}
		catch (RuntimeException ex)
		{
			HttpContextHelper.ResponseWrite("err@" + ex.getMessage());
		}

		//找不不到标记就抛出异常.
		throw new RuntimeException("@标记[" + this.getDoType() + "]，没有找到. @RowURL:" + HttpContextHelper.RequestRawUrl);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 执行父类的重写方法.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 报表设计器. - 第2步选择列.
	/** 
	 初始化方法
	 
	 @return 
	*/
	public final String S2ColsChose_Init()
	{
		DataSet ds = new DataSet();
		String rptNo = this.GetRequestVal("RptNo");

		//所有的字段.
		String fk_mapdata = "ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt";
		MapAttrs mattrs = new MapAttrs(fk_mapdata);
		ds.Tables.add(mattrs.ToDataTableField("Sys_MapAttrOfAll"));

		//判断rptNo是否存在于mapdata中
		MapData md = new MapData();
		md.No = rptNo;
		if (md.RetrieveFromDBSources() == 0)
		{
			Rpt.RptDfine rd = new Rpt.RptDfine(this.getFK_Flow());

			switch (rptNo.substring(fk_mapdata.length()))
			{
				case "My":
					rd.DoReset_MyStartFlow();
					break;
				case "MyDept":
					rd.DoReset_MyDeptFlow();
					break;
				case "MyJoin":
					rd.DoReset_MyJoinFlow();
					break;
				case "Adminer":
					rd.DoReset_AdminerFlow();
					break;
				default:
					throw new RuntimeException("@未涉及的rptMark类型");
			}

			md.RetrieveFromDBSources();
		}

		//选择的字段,就是报表的字段.
		MapAttrs mattrsOfRpt = new MapAttrs(rptNo);
		ds.Tables.add(mattrsOfRpt.ToDataTableField("Sys_MapAttrOfSelected"));

		//系统字段.
		MapAttrs mattrsOfSystem = new MapAttrs();
		String sysFields = BP.WF.Glo.getFlowFields();
		for (MapAttr item : mattrs)
		{
			if (sysFields.contains(item.KeyOfEn))
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
		String fields = "," + this.GetRequestVal("Fields") + ",";

		//构造一个空的集合.
		MapAttrs mrattrsOfRpt = new MapAttrs();
		mrattrsOfRpt.Delete(MapAttrAttr.FK_MapData, rptNo);

		//所有的字段.
		String fk_mapdata = "ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt";
		MapAttrs allAttrs = new MapAttrs(fk_mapdata);

		for (MapAttr attr : allAttrs)
		{
			attr.UIVisible = true;
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 处理特殊字段.
			if (attr.KeyOfEn.equals("FK_NY"))
			{
				attr.LGType = BP.En.FieldTypeS.FK;
				attr.UIBindKey = "BP.Pub.NYs";
				attr.UIContralType = BP.En.UIContralType.DDL;
			}

			if (attr.KeyOfEn.equals("FK_Dept"))
			{
				attr.LGType = BP.En.FieldTypeS.FK;
				attr.UIBindKey = "BP.Port.Depts";
				attr.UIContralType = BP.En.UIContralType.DDL;
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 处理特殊字段.

			//增加上必要的字段.
			if (attr.KeyOfEn.equals("Title") || attr.KeyOfEn.equals("WorkID") || attr.KeyOfEn.equals("OID"))
			{
				attr.FK_MapData = rptNo;
				attr.setMyPK( attr.FK_MapData + "_" + attr.KeyOfEn;
				attr.DirectInsert();
				continue;
			}

			//如果包含了指定的字段，就执行插入操作.
			if (fields.contains("," + attr.KeyOfEn + ",") == true)
			{
				attr.FK_MapData = rptNo;
				attr.setMyPK( attr.FK_MapData + "_" + attr.KeyOfEn;
				attr.DirectInsert();
			}
		}

		return "保存成功.";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
		md.No = rptNo;
		if (md.RetrieveFromDBSources() == 0)
		{
			Rpt.RptDfine rd = new Rpt.RptDfine(this.getFK_Flow());

			switch (rptNo.substring(("ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt").length()))
			{
				case "My":
					rd.DoReset_MyStartFlow();
					break;
				case "MyDept":
					rd.DoReset_MyDeptFlow();
					break;
				case "MyJoin":
					rd.DoReset_MyJoinFlow();
					break;
				case "Adminer":
					rd.DoReset_AdminerFlow();
					break;
				default:
					throw new RuntimeException("@未涉及的rptMark类型");
			}

			md.RetrieveFromDBSources();
		}

		//选择的字段,就是报表的字段.
		MapAttrs mattrsOfRpt = new MapAttrs();
		QueryObject qo = new QueryObject(mattrsOfRpt);
		qo.AddWhere(MapAttrAttr.FK_MapData, rptNo);
		qo.addOrderBy(MapAttrAttr.Idx);
		qo.DoQuery();

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

		String rptNo = this.GetRequestVal("RptNo");

		String[] strs = orders.split("[@]", -1);
		for (String item : strs)
		{
			if (DataType.IsNullOrEmpty(item) == true)
			{
				continue;
			}

			String[] vals = item.split("[,]", -1);

			String mypk = rptNo + "_" + vals[0];

			MapAttr attr = new MapAttr();
			attr.setMyPK( mypk;
			attr.Retrieve();

			attr.Name = vals[1];
			attr.Idx = Integer.parseInt(vals[2]);

			attr.Update(); //执行更新.
		}

		MapAttr myattr = new MapAttr();
		myattr.setMyPK( rptNo + "_OID";
		myattr.RetrieveFromDBSources();
		myattr.Idx = 200;
		myattr.setName("工作ID";
		myattr.Update();

		myattr = new MapAttr();
		myattr.setMyPK( rptNo + "_Title";
		myattr.RetrieveFromDBSources();
		myattr.Idx = -100;
		myattr.setName("标题";
		myattr.Update();

		return "保存成功..";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 报表设计器 - 第4步骤.
	public final String S5SearchCond_Init()
	{
		//报表编号.
		String rptNo = this.GetRequestVal("RptNo");

		//定义容器.
		DataSet ds = new DataSet();

		//判断rptNo是否存在于mapdata中
		MapData md = new MapData();
		md.No = rptNo;
		if (md.RetrieveFromDBSources() == 0)
		{
			Rpt.RptDfine rd = new Rpt.RptDfine(this.getFK_Flow());

			switch (rptNo.substring(("ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt").length()))
			{
				case "My":
					rd.DoReset_MyStartFlow();
					break;
				case "MyDept":
					rd.DoReset_MyDeptFlow();
					break;
				case "MyJoin":
					rd.DoReset_MyJoinFlow();
					break;
				case "Adminer":
					rd.DoReset_AdminerFlow();
					break;
				default:
					throw new RuntimeException("@未涉及的rptMark类型");
			}

			md.RetrieveFromDBSources();
		}

		ds.Tables.add(md.ToDataTableField("Main"));

		//查询出来枚举与外键类型的字段集合.
		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.FK_MapData, rptNo);
		ds.Tables.add(attrs.ToDataTableField("Sys_MapAttr"));

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 检查是否有日期字段.
		boolean isHave = false;
		for (MapAttr mattr : attrs)
		{
			if (mattr.UIVisible == false)
			{
				continue;
			}

			if (mattr.MyDataType == DataType.AppDate || mattr.MyDataType == DataType.AppDateTime)
			{
				isHave = true;
				break;
			}
		}

		if (isHave == true)
		{
			DataTable dt = new DataTable();
			MapAttrs dtAttrs = new MapAttrs();
			for (MapAttr mattr : attrs)
			{
				if (mattr.MyDataType == DataType.AppDate || mattr.MyDataType == DataType.AppDateTime)
				{
					if (mattr.UIVisible == false)
					{
						continue;
					}
					dtAttrs.AddEntity(mattr);
				}
			}
			ds.Tables.add(dtAttrs.ToDataTableField("Sys_MapAttrOfDate"));
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
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
		md.No = this.getRptNo();
		md.RetrieveFromDBSources();

		//报表编号.
		String fields = this.GetRequestVal("Fields");
		md.RptSearchKeys = fields + "*";

		String IsSearchKey = this.GetRequestVal("IsSearchKey");
		if (IsSearchKey.equals("0"))
		{
			md.RptIsSearchKey = false;
		}
		else
		{
			md.RptIsSearchKey = true;
		}

		//查询方式.
		int DTSearchWay = this.GetRequestValInt("DTSearchWay");
		md.RptDTSearchWay = (DTSearchWay)DTSearchWay;

		//日期字段.
		String DTSearchKey = this.GetRequestVal("DTSearchKey");
		md.RptDTSearchKey = DTSearchKey;
		md.Save();

		Cash.Map_Cash.Remove(this.getRptNo());
		return "保存成功.";
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}