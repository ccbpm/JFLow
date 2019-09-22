package BP.WF.Rpt;

import BP.DA.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/** 
 报表设计
*/
public class MapRpt extends EntityNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 报表权限控制方式
	/** 
	 报表查看权限控制.
	*/
	public final RightViewWay getRightViewWay()
	{
		return RightViewWay.forValue(this.GetValIntByKey(MapRptAttr.RightViewWay));
	}
	public final void setRightViewWay(RightViewWay value)
	{
		this.SetValByKey(MapRptAttr.RightViewWay, value.getValue());
	}
	/** 
	 报表查看权限控制-数据
	*/
	public final String getRightViewTag()
	{
		return this.GetValStringByKey(MapRptAttr.RightViewTag);
	}
	public final void setRightViewTag(String value)
	{
		this.SetValByKey(MapRptAttr.RightViewTag, value);
	}
	/** 
	 报表部门权限控制.
	*/
	public final RightDeptWay getRightDeptWay()
	{
		return RightDeptWay.forValue(this.GetValIntByKey(MapRptAttr.RightDeptWay));
	}
	public final void setRightDeptWay(RightDeptWay value)
	{
		this.SetValByKey(MapRptAttr.RightDeptWay, value.getValue());
	}
	/** 
	 报表部门权限控制-数据
	*/
	public final String getRightDeptTag()
	{
		return this.GetValStringByKey(MapRptAttr.RightDeptTag);
	}
	public final void setRightDeptTag(String value)
	{
		this.SetValByKey(MapRptAttr.RightDeptTag, value);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 报表权限控制方式

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 外键属性
	/** 
	 框架
	*/
	public final MapFrames getMapFrames()
	{
		Object tempVar = this.GetRefObject("MapFrames");
		MapFrames obj = tempVar instanceof MapFrames ? (MapFrames)tempVar : null;
		if (obj == null)
		{
			obj = new MapFrames(this.No);
			this.SetRefObject("MapFrames", obj);
		}
		return obj;
	}
	/** 
	 分组字段
	*/
	public final GroupFields getGroupFields()
	{
		Object tempVar = this.GetRefObject("GroupFields");
		GroupFields obj = tempVar instanceof GroupFields ? (GroupFields)tempVar : null;
		if (obj == null)
		{
			obj = new GroupFields(this.No);
			this.SetRefObject("GroupFields", obj);
		}
		return obj;
	}
	/** 
	 逻辑扩展
	*/
	public final MapExts getMapExts()
	{
		Object tempVar = this.GetRefObject("MapExts");
		MapExts obj = tempVar instanceof MapExts ? (MapExts)tempVar : null;
		if (obj == null)
		{
			obj = new MapExts(this.No);
			this.SetRefObject("MapExts", obj);
		}
		return obj;
	}
	/** 
	 事件
	*/
	public final FrmEvents getFrmEvents()
	{
		Object tempVar = this.GetRefObject("FrmEvents");
		FrmEvents obj = tempVar instanceof FrmEvents ? (FrmEvents)tempVar : null;
		if (obj == null)
		{
			obj = new FrmEvents(this.No);
			this.SetRefObject("FrmEvents", obj);
		}
		return obj;
	}

	/** 
	 从表
	*/
	public final MapDtls getMapDtls()
	{
		Object tempVar = this.GetRefObject("MapDtls");
		MapDtls obj = tempVar instanceof MapDtls ? (MapDtls)tempVar : null;
		if (obj == null)
		{
			obj = new MapDtls(this.No);
			this.SetRefObject("MapDtls", obj);
		}
		return obj;
	}
	/** 
	 超连接
	*/
	public final FrmLinks getFrmLinks()
	{
		Object tempVar = this.GetRefObject("FrmLinks");
		FrmLinks obj = tempVar instanceof FrmLinks ? (FrmLinks)tempVar : null;
		if (obj == null)
		{
			obj = new FrmLinks(this.No);
			this.SetRefObject("FrmLinks", obj);
		}
		return obj;
	}
	/** 
	 按钮
	*/
	public final FrmBtns getFrmBtns()
	{
		Object tempVar = this.GetRefObject("FrmLinks");
		FrmBtns obj = tempVar instanceof FrmBtns ? (FrmBtns)tempVar : null;
		if (obj == null)
		{
			obj = new FrmBtns(this.No);
			this.SetRefObject("FrmBtns", obj);
		}
		return obj;
	}
	/** 
	 元素
	*/
	public final FrmEles getFrmEles()
	{
		Object tempVar = this.GetRefObject("FrmEles");
		FrmEles obj = tempVar instanceof FrmEles ? (FrmEles)tempVar : null;
		if (obj == null)
		{
			obj = new FrmEles(this.No);
			this.SetRefObject("FrmEles", obj);
		}
		return obj;
	}
	/** 
	 线
	*/
	public final FrmLines getFrmLines()
	{
		Object tempVar = this.GetRefObject("FrmLines");
		FrmLines obj = tempVar instanceof FrmLines ? (FrmLines)tempVar : null;
		if (obj == null)
		{
			obj = new FrmLines(this.No);
			this.SetRefObject("FrmLines", obj);
		}
		return obj;
	}
	/** 
	 标签
	*/
	public final FrmLabs getFrmLabs()
	{
		Object tempVar = this.GetRefObject("FrmLabs");
		FrmLabs obj = tempVar instanceof FrmLabs ? (FrmLabs)tempVar : null;
		if (obj == null)
		{
			obj = new FrmLabs(this.No);
			this.SetRefObject("FrmLabs", obj);
		}
		return obj;
	}
	/** 
	 图片
	*/
	public final FrmImgs getFrmImgs()
	{
		Object tempVar = this.GetRefObject("FrmLabs");
		FrmImgs obj = tempVar instanceof FrmImgs ? (FrmImgs)tempVar : null;
		if (obj == null)
		{
			obj = new FrmImgs(this.No);
			this.SetRefObject("FrmLabs", obj);
		}
		return obj;
	}
	/** 
	 附件
	*/
	public final FrmAttachments getFrmAttachments()
	{
		Object tempVar = this.GetRefObject("FrmAttachments");
		FrmAttachments obj = tempVar instanceof FrmAttachments ? (FrmAttachments)tempVar : null;
		if (obj == null)
		{
			obj = new FrmAttachments(this.No);
			this.SetRefObject("FrmAttachments", obj);
		}
		return obj;
	}
	/** 
	 图片附件
	*/
	public final FrmImgAths getFrmImgAths()
	{
		Object tempVar = this.GetRefObject("FrmImgAths");
		FrmImgAths obj = tempVar instanceof FrmImgAths ? (FrmImgAths)tempVar : null;
		if (obj == null)
		{
			obj = new FrmImgAths(this.No);
			this.SetRefObject("FrmImgAths", obj);
		}
		return obj;
	}
	/** 
	 单选按钮
	*/
	public final FrmRBs getFrmRBs()
	{
		Object tempVar = this.GetRefObject("FrmRBs");
		FrmRBs obj = tempVar instanceof FrmRBs ? (FrmRBs)tempVar : null;
		if (obj == null)
		{
			obj = new FrmRBs(this.No);
			this.SetRefObject("FrmRBs", obj);
		}
		return obj;
	}
	/** 
	 属性
	*/
	public final MapAttrs getMapAttrs()
	{
		Object tempVar = this.GetRefObject("MapAttrs");
		MapAttrs obj = tempVar instanceof MapAttrs ? (MapAttrs)tempVar : null;
		if (obj == null)
		{
			obj = new MapAttrs(this.No);
			this.SetRefObject("MapAttrs", obj);
		}
		return obj;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	/** 
	 流程编号
	*/
	public final String getFK_Flow()
	{
	   String str = this.GetValStrByKey(MapRptAttr.FK_Flow);
	   if (str.equals("") || str == null)
	   {
		   str = this.No.Replace("ND", "");
		   str = str.replace("MyRpt", "");
		   str = tangible.StringHelper.padLeft(str, 3, '0');
		   this.SetValByKey(MapRptAttr.FK_Flow, str);


		   this.Update(MapRptAttr.FK_Flow, str);
	   }
	   return str;
	}
	public final void setFK_Flow(String value)
	{
		this.SetValByKey(MapRptAttr.FK_Flow, value);
	}
	/** 
	 物理表
	*/
	public final String getPTable()
	{
		String s = this.GetValStrByKey(MapRptAttr.PTable);
		if (s.equals("") || s == null)
		{
			return this.No;
		}
		return s;
	}
	public final void setPTable(String value)
	{
		this.SetValByKey(MapRptAttr.PTable, value);
	}
	/** 
	 备注
	*/
	public final String getNote()
	{
		return this.GetValStrByKey(MapRptAttr.Note);
	}
	public final void setNote(String value)
	{
		this.SetValByKey(MapRptAttr.Note, value);
	}
	private Entities _HisEns = null;
	public final Entities getHisEns()
	{
		if (_HisEns == null)
		{
			_HisEns = BP.En.ClassFactory.GetEns(this.No);
		}
		return _HisEns;
	}
	public final Entity getHisEn()
	{
		return this.getHisEns().GetNewEntity;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	private GEEntity _HisEn = null;
	public final GEEntity getHisGEEn()
	{
		if (this._HisEn == null)
		{
			_HisEn = new GEEntity(this.No);
		}
		return _HisEn;
	}
	/** 
	 生成实体
	 
	 @param ds
	 @return 
	*/
	public final GEEntity GenerGEEntityByDataSet(DataSet ds)
	{
		// New 它的实例.
		GEEntity en = this.getHisGEEn();

		// 它的table.
		DataTable dt = ds.Tables[this.No];

		//装载数据.
		en.Row.LoadDataTable(dt, dt.Rows[0]);

		// dtls.
		MapDtls dtls = this.getMapDtls();
		for (MapDtl item : dtls.ToJavaList())
		{
			DataTable dtDtls = ds.Tables[item.No];
			GEDtls dtlsEn = new GEDtls(item.No);
			for (DataRow dr : dtDtls.Rows)
			{
				// 产生它的Entity data.
				GEDtl dtl = (GEDtl)dtlsEn.GetNewEntity;
				dtl.Row.LoadDataTable(dtDtls, dr);

				//加入这个集合.
				dtlsEn.AddEntity(dtl);
			}

			//加入到他的集合里.
			en.Dtls.Add(dtDtls);
		}
		return en;
	}
	/** 
	 报表设计
	*/
	public MapRpt()
	{
	}
	/** 
	 报表设计
	 
	 @param no 映射编号
	*/
	public MapRpt(String no)
	{
		this.No = no;
		this.Retrieve();
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_MapData", "报表设计");

		map.Java_SetDepositaryOfEntity(Depositary.Application);
		map.Java_SetCodeStruct("4");
		;

		map.AddTBStringPK(MapRptAttr.No, null, "编号", true, false, 1, 200, 20);
		map.AddTBString(MapRptAttr.Name, null, "描述", true, false, 0, 500, 20);
		map.AddTBString(MapRptAttr.PTable, null, "物理表", true, false, 0, 500, 20);
		map.AddTBString(MapRptAttr.FK_Flow, null, "流程编号", true, false, 0, 3, 3);

			//Tag
			//map.AddTBString(MapRptAttr.Tag, null, "Tag", true, false, 0, 500, 20);
			//时间查询:用于报表查询.
			//  map.AddTBInt(MapRptAttr.IsSearchKey, 0, "是否需要关键字查询", true, false);
			//   map.AddTBInt(MapRptAttr.DTSearchWay, 0, "时间查询方式", true, false);
			//   map.AddTBString(MapRptAttr.DTSearchKey, null, "时间查询字段", true, false, 0, 200, 20);
		map.AddTBString(MapRptAttr.Note, null, "备注", true, false, 0, 500, 20);


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 权限控制. 2014-12-18
		map.AddTBInt(MapRptAttr.RightViewWay, 0, "报表查看权限控制方式", true, false);
		map.AddTBString(MapRptAttr.RightViewTag, null, "报表查看权限控制Tag", true, false, 0, 4000, 20);
		map.AddTBInt(MapRptAttr.RightDeptWay, 0, "部门数据查看控制方式", true, false);
		map.AddTBString(MapRptAttr.RightDeptTag, null, "部门数据查看控制Tag", true, false, 0, 4000, 20);

		map.AttrsOfOneVSM.Add(new RptStations(), new Stations(), RptStationAttr.FK_Rpt, RptStationAttr.FK_Station, DeptAttr.Name, DeptAttr.No, "岗位权限");
		map.AttrsOfOneVSM.Add(new RptDepts(), new Depts(), RptDeptAttr.FK_Rpt, RptDeptAttr.FK_Dept, DeptAttr.Name, DeptAttr.No, "部门权限");
		map.AttrsOfOneVSM.Add(new RptEmps(), new Emps(), RptEmpAttr.FK_Rpt, RptEmpAttr.FK_Emp, DeptAttr.Name, DeptAttr.No, "人员权限");
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 权限控制.

			//增加参数字段.
		map.AddTBAtParas(1000);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 其他方法.
	/** 
	 显示的列.
	*/
	public final MapAttrs getHisShowColsAttrs()
	{
		MapAttrs mattrs = new MapAttrs(this.No);
		return mattrs;
	}
	@Override
	protected boolean beforeInsert()
	{
		this.ResetIt();
		return super.beforeInsert();
	}

	/** 
	 重置设置.
	*/
	public final void ResetIt()
	{
		MapData md = new MapData(this.No);
		md.RptIsSearchKey = true;
		md.RptDTSearchWay = DTSearchWay.None;
		md.RptDTSearchKey = "";
		md.RptSearchKeys = "*FK_Dept*WFSta*FK_NY*";

		Flow fl = new Flow(this.getFK_Flow());
		this.setPTable(fl.getPTable());
		this.Update();

		String keys = "'OID','FK_Dept','FlowStarter','WFState','Title','FlowStartRDT','FlowEmps','FlowDaySpan','FlowEnder','FlowEnderRDT','FK_NY','FlowEndNode','WFSta'";
		MapAttrs attrs = new MapAttrs("ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt");

		attrs.Delete(MapAttrAttr.FK_MapData, this.No); // 删除已经有的字段。
		for (MapAttr attr : attrs)
		{
			if (keys.contains("'" + attr.KeyOfEn + "'") == false)
			{
				continue;
			}
			attr.FK_MapData = this.No;
			attr.Insert();
		}
	}
	/** 
	 删除之前.
	 
	 @return 
	*/
	@Override
	protected boolean beforeDelete()
	{
		MapAttrs attrs = new MapAttrs();
		attrs.Delete(MapAttrAttr.FK_MapData, this.No);
		return super.beforeDelete();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 其他方法.
}