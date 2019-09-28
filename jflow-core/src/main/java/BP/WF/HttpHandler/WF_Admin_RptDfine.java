package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Difference.Handler.CommonUtils;
import BP.Difference.Handler.WebContralBase;
import BP.Sys.*;
import BP.En.*;
import BP.WF.Rpt.RptDfine;

/**
 * 页面功能实体
 */
public class WF_Admin_RptDfine extends WebContralBase {

	/**
	 * 构造函数
	 */
	public WF_Admin_RptDfine() {
	}

	/**
	 * 默认执行的方法
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	protected String DoDefaultMethod() throws Exception {
		String msg = "";
		try {
			switch (this.getDoType()) {
			case "S3ColsLabel_Init": // 顺序加载
				msg = this.S3ColsLabel_Init();
				break;
			case "S3ColsLabel_Save": // 顺序保存
				msg = this.S3ColsLabel_Save();
				break;
			default:
				msg = "err@没有判断的执行类型：" + this.getDoType();
				break;
			}
			return msg;
		} catch (RuntimeException ex) {
			throw new RuntimeException(
					"@标记[" + this.getDoType() + "]，没有找到. @RowURL:" + CommonUtils.getRequest().getRequestURI());
		}

	}

	/**
	 * 初始化方法
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String S2ColsChose_Init() throws Exception {
		DataSet ds = new DataSet();
		String rptNo = this.GetRequestVal("RptNo");

		// 所有的字段.
		String fk_mapdata = "ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt";
		MapAttrs mattrs = new MapAttrs(fk_mapdata);
		ds.Tables.add(mattrs.ToDataTableField("Sys_MapAttrOfAll"));

		// 判断rptNo是否存在于mapdata中
		MapData md = new MapData();
		md.setNo(rptNo);
		if (md.RetrieveFromDBSources() == 0) {
			RptDfine rd = new RptDfine(this.getFK_Flow());

			switch (rptNo.substring(fk_mapdata.length())) {
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

		// 选择的字段,就是报表的字段.
		MapAttrs mattrsOfRpt = new MapAttrs(rptNo);
		ds.Tables.add(mattrsOfRpt.ToDataTableField("Sys_MapAttrOfSelected"));

		// 系统字段.
		MapAttrs mattrsOfSystem = new MapAttrs();
		String sysFields = BP.WF.Glo.getFlowFields();
		for (MapAttr item : mattrs.ToJavaList()) {
			if (sysFields.contains(item.getKeyOfEn())) {
				mattrsOfSystem.AddEntity(item);
			}
		}
		ds.Tables.add(mattrsOfSystem.ToDataTableField("Sys_MapAttrOfSystem"));

		// 返回.
		return BP.Tools.Json.ToJson(ds);
	}

	/**
	 * 选择列的保存.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String S2ColsChose_Save() throws Exception {
		// 报表列表.
		String rptNo = this.GetRequestVal("RptNo");

		// 保存的字段,从外面传递过来的值. 用逗号隔开的: 比如: ,Name,Tel,Addr,
		String fields = "," + this.GetRequestVal("Fields") + ",";

		// 构造一个空的集合.
		MapAttrs mrattrsOfRpt = new MapAttrs();
		mrattrsOfRpt.Delete(MapAttrAttr.FK_MapData, rptNo);

		// 所有的字段.
		String fk_mapdata = "ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt";
		MapAttrs allAttrs = new MapAttrs(fk_mapdata);

		for (MapAttr attr : allAttrs.ToJavaList()) {
			attr.setUIVisible(true);

			/// #region 处理特殊字段.
			if (attr.getKeyOfEn().equals("FK_NY")) {
				attr.setLGType(BP.En.FieldTypeS.FK);
				attr.setUIBindKey("BP.Pub.NYs");
				attr.setUIContralType(BP.En.UIContralType.DDL);
			}

			if (attr.getKeyOfEn().equals("FK_Dept")) {
				attr.setLGType(BP.En.FieldTypeS.FK);
				attr.setUIBindKey("BP.Port.Depts");
				attr.setUIContralType(BP.En.UIContralType.DDL);
			}

			/// #endregion 处理特殊字段.

			// 增加上必要的字段.
			if (attr.getKeyOfEn().equals("Title") || attr.getKeyOfEn().equals("WorkID")
					|| attr.getKeyOfEn().equals("OID")) {
				attr.setFK_MapData(rptNo);
				attr.setMyPK(attr.getFK_MapData() + "_" + attr.getKeyOfEn());
				attr.DirectInsert();
				continue;
			}

			// 如果包含了指定的字段，就执行插入操作.
			if (fields.contains("," + attr.getKeyOfEn() + ",") == true) {
				attr.setFK_MapData(rptNo);
				attr.setMyPK(attr.getFK_MapData() + "_" + attr.getKeyOfEn());
				attr.DirectInsert();
			}
		}

		return "保存成功.";
	}

	/// #endregion

	/// #region 报表设计器. - 第3步设置列的顺序.
	/**
	 * 初始化方法
	 * 
	 * @return
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	public final String S3ColsLabel_Init() throws NumberFormatException, Exception {
		String rptNo = this.GetRequestVal("RptNo");

		// 判断rptNo是否存在于mapdata中
		MapData md = new MapData();
		md.setNo(rptNo);
		if (md.RetrieveFromDBSources() == 0) {
			RptDfine rd = new RptDfine(this.getFK_Flow());

			switch (rptNo.substring(("ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt").length())) {
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

		// 选择的字段,就是报表的字段.
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
	 * 保存列的顺序名称.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String S3ColsLabel_Save() throws Exception {
		String orders = this.GetRequestVal("Orders");
		// 格式为 @KeyOfEn,Lable,idx 比如： @DianHua,电话,1@Addr,地址,2

		String rptNo = this.GetRequestVal("RptNo");

		String[] strs = orders.split("[@]", -1);
		for (String item : strs) {
			if (DataType.IsNullOrEmpty(item) == true) {
				continue;
			}

			String[] vals = item.split("[,]", -1);

			String mypk = rptNo + "_" + vals[0];

			MapAttr attr = new MapAttr();
			attr.setMyPK(mypk);
			attr.Retrieve();

			attr.setName(vals[1]);
			attr.setIdx(Integer.parseInt(vals[2]));

			attr.Update(); // 执行更新.
		}

		MapAttr myattr = new MapAttr();
		myattr.setMyPK(rptNo + "_OID");
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

	/// #endregion

	/// #region 报表设计器 - 第4步骤.
	public final String S5SearchCond_Init() throws Exception {
		// 报表编号.
		String rptNo = this.GetRequestVal("RptNo");

		// 定义容器.
		DataSet ds = new DataSet();

		// 判断rptNo是否存在于mapdata中
		MapData md = new MapData();
		md.setNo(rptNo);
		if (md.RetrieveFromDBSources() == 0) {
			RptDfine rd = new RptDfine(this.getFK_Flow());

			switch (rptNo.substring(("ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt").length())) {
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

		// 查询出来枚举与外键类型的字段集合.
		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.FK_MapData, rptNo);
		ds.Tables.add(attrs.ToDataTableField("Sys_MapAttr"));

		/// #region 检查是否有日期字段.
		boolean isHave = false;
		for (MapAttr mattr : attrs.ToJavaList()) {
			if (mattr.getUIVisible() == false) {
				continue;
			}

			if (mattr.getMyDataType() == DataType.AppDate || mattr.getMyDataType() == DataType.AppDateTime) {
				isHave = true;
				break;
			}
		}

		if (isHave == true) {
			MapAttrs dtAttrs = new MapAttrs();
			for (MapAttr mattr : attrs.ToJavaList()) {
				if (mattr.getMyDataType() == DataType.AppDate || mattr.getMyDataType() == DataType.AppDateTime) {
					if (mattr.getUIVisible() == false) {
						continue;
					}
					dtAttrs.AddEntity(mattr);
				}
			}
			ds.Tables.add(dtAttrs.ToDataTableField("Sys_MapAttrOfDate"));
		}

		// 返回数据.
		return BP.Tools.Json.ToJson(ds);
	}

	public final String getRptNo() {
		return this.GetRequestVal("RptNo");
	}

	/**
	 * 查询条件保存.
	 * 
	 * @return
	 * @throws Exception
	 */
	public final String S5SearchCond_Save() throws Exception {
		MapData md = new MapData();
		md.setNo(this.getRptNo());
		md.RetrieveFromDBSources();

		// 报表编号.
		String fields = this.GetRequestVal("Fields");
		md.setRptSearchKeys(fields + "*");

		String IsSearchKey = this.GetRequestVal("IsSearchKey");
		if (IsSearchKey.equals("0")) {
			md.setRptIsSearchKey(false);
		} else {
			md.setRptIsSearchKey(true);
		}

		// 查询方式.
		int DTSearchWay = this.GetRequestValInt("DTSearchWay");
		md.setRptDTSearchWay(BP.Sys.DTSearchWay.forValue(DTSearchWay));

		// 日期字段.
		String DTSearchKey = this.GetRequestVal("DTSearchKey");
		md.setRptDTSearchKey(DTSearchKey);
		md.Save();

		Cash.getMap_Cash().remove(this.getRptNo());
		return "保存成功.";
	}

	/// #endregion
}