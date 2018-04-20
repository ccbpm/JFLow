package BP.WF.Rpt;

import org.apache.commons.lang.StringUtils;

import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.Depositary;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.EntityNoName;
import BP.En.Map;
import BP.Sys.DTSearchWay;
import BP.Sys.FrmAttachments;
import BP.Sys.FrmBtns;
import BP.Sys.FrmEles;
import BP.Sys.FrmEvents;
import BP.Sys.FrmImgAths;
import BP.Sys.FrmImgs;
import BP.Sys.FrmLabs;
import BP.Sys.FrmLines;
import BP.Sys.FrmLinks;
import BP.Sys.FrmRBs;
import BP.Sys.GEDtl;
import BP.Sys.GEDtls;
import BP.Sys.GEEntity;
import BP.Sys.GroupFields;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Sys.MapDtl;
import BP.Sys.MapDtls;
import BP.Sys.MapExts;
import BP.Sys.MapFrames;
import BP.WF.Flow;

/** 
 报表设计
*/
public class MapRpt extends EntityNoName
{
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
	/** 
	 框架
	 * @throws Exception 
	*/
	public final MapFrames getMapFrames() throws Exception
	{
		Object tempVar = this.GetRefObject("MapFrames");
		MapFrames obj = (MapFrames)((tempVar instanceof MapFrames) ? tempVar : null);
		if (obj == null)
		{
			obj = new MapFrames(this.getNo());
			this.SetRefObject("MapFrames", obj);
		}
		return obj;
	}
	/** 
	 分组字段
	 * @throws Exception 
	*/
	public final GroupFields getGroupFields() throws Exception
	{
		Object tempVar = this.GetRefObject("GroupFields");
		GroupFields obj = (GroupFields)((tempVar instanceof GroupFields) ? tempVar : null);
		if (obj == null)
		{
			obj = new GroupFields(this.getNo());
			this.SetRefObject("GroupFields", obj);
		}
		return obj;
	}
	/** 
	 逻辑扩展
	 * @throws Exception 
	*/
	public final MapExts getMapExts() throws Exception
	{
		Object tempVar = this.GetRefObject("MapExts");
		MapExts obj = (MapExts)((tempVar instanceof MapExts) ? tempVar : null);
		if (obj == null)
		{
			obj = new MapExts(this.getNo());
			this.SetRefObject("MapExts", obj);
		}
		return obj;
	}
	/** 
	 事件
	 * @throws Exception 
	*/
	public final FrmEvents getFrmEvents() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmEvents");
		FrmEvents obj = (FrmEvents)((tempVar instanceof FrmEvents) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmEvents(this.getNo());
			this.SetRefObject("FrmEvents", obj);
		}
		return obj;
	}
	 
	/** 
	 从表
	 * @throws Exception 
	*/
	public final MapDtls getMapDtls() throws Exception
	{
		Object tempVar = this.GetRefObject("MapDtls");
		MapDtls obj = (MapDtls)((tempVar instanceof MapDtls) ? tempVar : null);
		if (obj == null)
		{
			obj = new MapDtls(this.getNo());
			this.SetRefObject("MapDtls", obj);
		}
		return obj;
	}
	/** 
	 超连接
	 * @throws Exception 
	*/
	public final FrmLinks getFrmLinks() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmLinks");
		FrmLinks obj = (FrmLinks)((tempVar instanceof FrmLinks) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmLinks(this.getNo());
			this.SetRefObject("FrmLinks", obj);
		}
		return obj;
	}
	/** 
	 按钮
	 * @throws Exception 
	*/
	public final FrmBtns getFrmBtns() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmLinks");
		FrmBtns obj = (FrmBtns)((tempVar instanceof FrmBtns) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmBtns(this.getNo());
			this.SetRefObject("FrmBtns", obj);
		}
		return obj;
	}
	/** 
	 元素
	 * @throws Exception 
	*/
	public final FrmEles getFrmEles() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmEles");
		FrmEles obj = (FrmEles)((tempVar instanceof FrmEles) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmEles(this.getNo());
			this.SetRefObject("FrmEles", obj);
		}
		return obj;
	}
	/** 
	 线
	 * @throws Exception 
	 
	*/
	public final FrmLines getFrmLines() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmLines");
		FrmLines obj = (FrmLines)((tempVar instanceof FrmLines) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmLines(this.getNo());
			this.SetRefObject("FrmLines", obj);
		}
		return obj;
	}
	/** 
	 标签
	 * @throws Exception 
	*/
	public final FrmLabs getFrmLabs() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmLabs");
		FrmLabs obj = (FrmLabs)((tempVar instanceof FrmLabs) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmLabs(this.getNo());
			this.SetRefObject("FrmLabs", obj);
		}
		return obj;
	}
	/** 
	 图片
	 * @throws Exception 
	*/
	public final FrmImgs getFrmImgs() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmLabs");
		FrmImgs obj = (FrmImgs)((tempVar instanceof FrmImgs) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmImgs(this.getNo());
			this.SetRefObject("FrmLabs", obj);
		}
		return obj;
	}
	/** 
	 附件
	 * @throws Exception 
	*/
	public final FrmAttachments getFrmAttachments() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmAttachments");
		FrmAttachments obj = (FrmAttachments)((tempVar instanceof FrmAttachments) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmAttachments(this.getNo());
			this.SetRefObject("FrmAttachments", obj);
		}
		return obj;
	}
	/** 
	 图片附件
	 * @throws Exception 
	*/
	public final FrmImgAths getFrmImgAths() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmImgAths");
		FrmImgAths obj = (FrmImgAths)((tempVar instanceof FrmImgAths) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmImgAths(this.getNo());
			this.SetRefObject("FrmImgAths", obj);
		}
		return obj;
	}
	/** 
	 单选按钮
	 * @throws Exception 
	*/
	public final FrmRBs getFrmRBs() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmRBs");
		FrmRBs obj = (FrmRBs)((tempVar instanceof FrmRBs) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmRBs(this.getNo());
			this.SetRefObject("FrmRBs", obj);
		}
		return obj;
	}
	/** 
	 属性
	 * @throws Exception 
	*/
	public final MapAttrs getMapAttrs() throws Exception
	{
		Object tempVar = this.GetRefObject("MapAttrs");
		MapAttrs obj = (MapAttrs)((tempVar instanceof MapAttrs) ? tempVar : null);
		if (obj == null)
		{
			obj = new MapAttrs(this.getNo());
			this.SetRefObject("MapAttrs", obj);
		}
		return obj;
	}
	/** 
	 流程编号
	 * @throws Exception 
	*/
	public final String getFK_Flow() throws Exception
	{
	   String str= this.GetValStrByKey(MapRptAttr.FK_Flow);
	   if (str != null && !str.equals(""))
	   {
		   str = this.getNo().replace("ND", "");
		   str = str.replace("MyRpt", "");
		   str = str.replace("RptMy", "");
		   str=StringUtils.leftPad(str,3, "0");
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
			return this.getNo();
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
	public Entities _HisEns = null;
	public final Entities getHisEns()
	{
		if (_HisEns == null)
		{
			_HisEns = BP.En.ClassFactory.GetEns(this.getNo());
		}
		return _HisEns;
	}
	public final Entity getHisEn()
	{
		return this.getHisEns().getGetNewEntity();
	}


	private GEEntity _HisEn = null;
	public final GEEntity getHisGEEn()
	{
		if (this._HisEn == null)
		{
			_HisEn = new GEEntity(this.getNo());
		}
		return _HisEn;
	}
	/** 
	 生成实体
	 @param ds
	 @return 
	 * @throws Exception 
	*/
	public final GEEntity GenerGEEntityByDataSet(DataSet ds) throws Exception
	{
		// New 它的实例.
		GEEntity en = this.getHisGEEn();

		// 它的table.
		DataTable dt = ds.Tables.get(Integer.parseInt(this.getNo()));

		//装载数据.
		en.getRow().LoadDataTable(dt, dt.Rows.get(0));

		// dtls.
		MapDtls dtls = this.getMapDtls();
		for (Object item : dtls)
		{
			DataTable dtDtls = ds.Tables.get(Integer.parseInt(((MapDtl) item)
					.getNo()));
			GEDtls dtlsEn = new GEDtls(((MapDtl) item).getNo());
			for (DataRow dr : dtDtls.Rows)
			{
				// 产生它的Entity data.
				GEDtl dtl = (GEDtl) dtlsEn.getGetNewEntity();
				dtl.getRow().LoadDataTable(dtDtls, dr);

				//加入这个集合.
				dtlsEn.AddEntity(dtl);
			}

			//加入到他的集合里.
			en.getDtls().add(dtDtls);
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
	 * @throws Exception 
	*/
	public MapRpt(String no) throws Exception
	{
		this.setNo(no);
		this.Retrieve();
	}
	public MapRpt(String no,String dbDesc )
	{
		this.setNo(no);
		this.setNote(dbDesc);
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
			//     map.AddTBString(MapRptAttr.SearchKeys, null, "查询键", true, false, 0, 500, 20);
		map.AddTBString(MapRptAttr.PTable, null, "物理表", true, false, 0, 500, 20);
		map.AddTBString(MapRptAttr.FK_Flow, null, "流程编号", true, false, 0, 3, 3);

			//Tag
			//map.AddTBString(MapRptAttr.Tag, null, "Tag", true, false, 0, 500, 20);
			//时间查询:用于报表查询.
			//  map.AddTBInt(MapRptAttr.IsSearchKey, 0, "是否需要关键字查询", true, false);
			//   map.AddTBInt(MapRptAttr.DTSearchWay, 0, "时间查询方式", true, false);
			//   map.AddTBString(MapRptAttr.DTSearchKey, null, "时间查询字段", true, false, 0, 200, 20);
		map.AddTBString(MapRptAttr.Note, null, "备注", true, false, 0, 500, 20);



			///#region 权限控制. 2014-12-18
		map.AddTBInt(MapRptAttr.RightViewWay, 0, "报表查看权限控制方式", true, false);
		map.AddTBString(MapRptAttr.RightViewTag, null, "报表查看权限控制Tag", true, false, 0, 4000, 20);
		map.AddTBInt(MapRptAttr.RightDeptWay, 0, "部门数据查看控制方式", true, false);
		map.AddTBString(MapRptAttr.RightDeptTag, null, "部门数据查看控制Tag", true, false, 0, 4000, 20);

//		map.AttrsOfOneVSM.Add(new RptStations(), new Stations(), RptStationAttr.FK_Rpt, RptStationAttr.FK_Station, DeptAttr.Name, DeptAttr.No, "岗位权限");
//		map.AttrsOfOneVSM.Add(new RptDepts(), new Depts(), RptDeptAttr.FK_Rpt, RptDeptAttr.FK_Dept, DeptAttr.Name, DeptAttr.No, "部门权限");
//		map.AttrsOfOneVSM.Add(new RptEmps(), new Emps(), RptEmpAttr.FK_Rpt, RptEmpAttr.FK_Emp, DeptAttr.Name, DeptAttr.No, "人员权限");

			///#endregion 权限控制.

			//增加参数字段.
		map.AddTBAtParas(1000);

		this.set_enMap(map);
		return this.get_enMap();
	}
	/** 
	 显示的列.
	 * @throws Exception 
	*/
	public final MapAttrs getHisShowColsAttrs() throws Exception
	{
		MapAttrs mattrs = new MapAttrs(this.getNo());
		return mattrs;
	}
	@Override
	protected boolean beforeInsert() throws Exception
	{
		this.ResetIt();
		return super.beforeInsert();
	}

	/** 
	 重置设置.
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final void ResetIt() throws NumberFormatException, Exception
	{
		MapData md = new MapData(this.getNo());
		md.setRptIsSearchKey(true);
		md.setRptDTSearchWay( DTSearchWay.None);
		md.setRptDTSearchKey("");
		md.setRptSearchKeys ("*FK_Dept*WFSta*FK_NY*");

		Flow fl = new Flow(this.getFK_Flow());
		this.setPTable(fl.getPTable());
		this.Update();

		String keys = "'OID','FK_Dept','FlowStarter','WFState','Title','FlowStartRDT','FlowEmps','FlowDaySpan','FlowEnder','FlowEnderRDT','FK_NY','FlowEndNode','WFSta'";
		MapAttrs attrs = new MapAttrs("ND"+Integer.parseInt(this.getFK_Flow())+"Rpt");

		attrs.Delete(MapAttrAttr.FK_MapData, this.getNo()); // 删除已经有的字段。
		for (MapAttr attr : attrs.ToJavaList())
		{
			if (keys.contains("'" + attr.getKeyOfEn() + "'") == false)
			{
				continue;
			}
			attr.setFK_MapData( this.getNo());
			attr.Insert();
		}
	}
	/** 
	 删除之前.
	 @return 
	 * @throws Exception 
	*/
	@Override
	protected boolean beforeDelete() throws Exception
	{
		MapAttrs attrs = new MapAttrs();
		attrs.Delete(MapAttrAttr.FK_MapData, this.getNo());
		return super.beforeDelete();
	}

}