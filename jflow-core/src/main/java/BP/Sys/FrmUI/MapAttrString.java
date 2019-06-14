package BP.Sys.FrmUI;

import java.net.URLDecoder;

import BP.DA.DBAccess;
import BP.DA.DBType;
import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.EntityMyPK;
import BP.En.Map;
import BP.En.RefMethod;
import BP.En.RefMethodType;
import BP.En.UAC;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapData;
import BP.Sys.SystemConfig;
import BP.WF.Glo;

/**
 * 实体属性
 */
public class MapAttrString extends EntityMyPK {
	 
	/**
	 * 表单ID
	 */
	public final String getFK_MapData() {
		return this.GetValStringByKey(MapAttrAttr.FK_MapData);
	}

	public final void setFK_MapData(String value) {
		this.SetValByKey(MapAttrAttr.FK_MapData, value);
	}

	/**
	 * 字段
	 */
	public final String getKeyOfEn() {
		return this.GetValStringByKey(MapAttrAttr.KeyOfEn);
	}

	public final void setKeyOfEn(String value) {
		this.SetValByKey(MapAttrAttr.KeyOfEn, value);
	}

	/**
	 * 绑定的枚举ID
	 */
	public final String getUIBindKey() {
		return this.GetValStringByKey(MapAttrAttr.UIBindKey);
	}

	public final void setUIBindKey(String value) {
		this.SetValByKey(MapAttrAttr.UIBindKey, value);
	}

	/**
	 * 控制权限
	 */
	@Override
	public UAC getHisUAC() {
		UAC uac = new UAC();
		uac.IsInsert = false;
		uac.IsUpdate = true;
		uac.IsDelete = true;
		return uac;
	}

	/**
	 * 实体属性
	 */
	public MapAttrString() {
	}
	
	  /// <summary>
    /// 实体属性
    /// </summary>
    public MapAttrString(String myPK) throws Exception
    {
        this.setMyPK(myPK);
        this.Retrieve();

    }
	/**
	 * EnMap
	 */
	@Override
	public Map getEnMap() {
		if (this.get_enMap() != null) {
			return this.get_enMap();
		}

		Map map = new Map("Sys_MapAttr", "文本字段");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);

		map.AddTBStringPK(MapAttrAttr.MyPK, null, "主键", false, false, 0, 200, 20);
		map.AddTBString(MapAttrAttr.FK_MapData, null, "实体标识", false, false, 1, 100, 20);

		map.AddTBString(MapAttrAttr.Name, null, "字段中文名", true, false, 0, 200, 20);
		map.AddTBString(MapAttrAttr.KeyOfEn, null, "字段名", true, true, 1, 200, 20);

		// 默认值.
		// sunxd 解决ORACLE数据为自动转大写问题
		// SELECT No,Name FROM Sys_GloVar WHERE GroupKey='DefVal' 修改为 "SELECT No
		// \"No\",Name \"Name\" FROM Sys_GloVar WHERE GroupKey='DefVal'"
		String sql = "SELECT No ,Name FROM Sys_GloVar WHERE GroupKey='DefVal'";
		// 显示的分组.
		map.AddDDLSQL("ExtDefVal", "0", "系统默认值", sql, true);

		map.AddTBString(MapAttrAttr.DefVal, null, "默认值表达式", true, false, 0, 2000, 20);

		map.AddTBInt(MapAttrAttr.MinLen, 0, "最小长度", true, false);
		map.AddTBInt(MapAttrAttr.MaxLen, 50, "最大长度", true, false);

		map.AddTBFloat(MapAttrAttr.UIWidth, 100, "宽度", true, false);
		// map.AddTBFloat(MapAttrAttr.UIHeight, 23, "高度", false, false);
		map.AddTBFloat("ExtRows", 1, "文本框行数(决定高度)", true, false);

		map.AddBoolean(MapAttrAttr.UIVisible, true, "是否可见？", true, true);
		map.AddBoolean(MapAttrAttr.UIIsEnable, true, "是否可编辑？", true, true);
		map.AddBoolean(MapAttrAttr.UIIsInput, false, "是否必填项？", true, true);
		map.AddBoolean("IsRichText", false, "是否大块文本？", true, true);
		map.AddBoolean("IsSupperText", false, "是否富文本？", true, true);
		map.AddTBString(MapAttrAttr.Tip, null, "激活提示", true, false, 0, 500, 20, true);

		map.AddDDLSysEnum(MapAttrAttr.ColSpan, 1, "单元格数量", true, true, "ColSpanAttrString",
				"@0=跨0个单元格@1=跨1个单元格@2=跨2个单元格@3=跨3个单元格@4=跨4个单元格@5=跨4个单元格@6=跨4个单元格");

		 //文本占单元格数量
        map.AddDDLSysEnum(MapAttrAttr.TextColSpan, 1, "文本单元格数量", true, true, "ColSpanAttrString",
            "@1=跨1个单元格@2=跨2个单元格@3=跨3个单元格@4=跨4个单元格@5=跨4个单元格@6=跨4个单元格");

        //文本跨行
        map.AddDDLSysEnum(MapAttrAttr.RowSpan, 1, "行数", true, true, "RowSpanAttrString",
           "@1=跨1行@2=跨2行@3=跨3行");
        
		
		map.AddDDLSQL(MapAttrAttr.GroupID, "0", "显示的分组", MapAttrString.SQLOfGroupAttr(), true);

		map.AddDDLSysEnum(MapAttrAttr.IsSigan, 0, "签名模式", true, true, MapAttrAttr.IsSigan,
				"@0=无@1=图片签名@2=山东CA@3=广东CA@4=图片盖章");

		RefMethod rm = new RefMethod();


		rm = new RefMethod();
		rm.Title = "文本框自动完成";
		rm.ClassMethodName = this.toString() + ".DoTBFullCtrl2019()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "Pop自动完成";
		rm.ClassMethodName = this.toString() + ".DoPopFullCtrl()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "脚本验证";
		rm.ClassMethodName = this.toString() + ".DoInputCheck()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "事件绑函数";
		rm.ClassMethodName = this.toString() + ".BindFunction()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "超链接";
		rm.ClassMethodName = this.toString() + ".DoLink()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);
		
		rm = new RefMethod();
        rm.Title = "快速录入";
        rm.ClassMethodName = this.toString() + ".DoFastEnter()";
        rm.refMethodType = RefMethodType.RightFrameOpen;
        map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "Pop返回值";
		rm.ClassMethodName = this.toString() + ".DoPop2019()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		// #endregion 基本功能.

		// #region 多选.
		rm = new RefMethod();
		rm.GroupName = "输入内容多选";
		rm.Title = "小范围多选";
		rm.ClassMethodName = this.toString() + ".DoMultipleChoiceSmall()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.GroupName = "输入内容多选";
		rm.Title = "搜索多选";
		rm.ClassMethodName = this.toString() + ".DoMultipleChoiceSearch()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);
		// #endregion

		// #region Pop返回值.
		rm = new RefMethod();
		rm.GroupName = "Pop返回值";
		rm.Title = "简单模式";
		rm.ClassMethodName = this.toString() + ".DoPopSimpleModel()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.GroupName = "Pop返回值";
		rm.Title = "部门员工模式(高级)";
		rm.ClassMethodName = this.toString() + ".DoPopDeptEmpModelAdv()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.Title = "扩展控件";
		rm.ClassMethodName = this.toString() + ".DoEditFExtContral()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "高级设置";
		map.AddRefMethod(rm);
		
		rm = new RefMethod();
        rm.Title = "扩展控件2019";
        rm.ClassMethodName = this.toString() + ".DoEditFExtContral2019()";
        rm.refMethodType = RefMethodType.RightFrameOpen;
        rm.GroupName = "高级设置";
        map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "批处理";
		rm.ClassMethodName = this.toString() + ".DoEleBatch()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "高级设置";
		map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}

	/// <summary>
	/// 字段分组查询语句
	/// </summary>
	public static String SQLOfGroupAttr() {

		return "SELECT OID as No, Lab as Name FROM Sys_GroupField WHERE FrmID='@FK_MapData'  AND (CtrlType IS NULL OR CtrlType='')  ";

	}

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception {
		MapAttr attr = new MapAttr();
		attr.setMyPK(this.getMyPK());
		attr.RetrieveFromDBSources();

		// 高度.
		attr.setUIHeightInt(this.GetValIntByKey("ExtRows") * 23);

		attr.setIsRichText(this.GetValBooleanByKey("IsSupperText")); // 是否是富文本？
		attr.setIsSupperText(this.GetValBooleanByKey("IsRichText")); // 是否是大块文本？

		if (attr.getIsRichText() || attr.getIsSupperText()) {
			attr.setMaxLen(4000);
			this.SetValByKey(MapAttrAttr.MaxLen, 4000);
		}

		// #region 自动扩展字段长度. @杜. 需要翻译.
		if (attr.getMaxLen() < 4000) {
			String sql = "";
			MapData md = new MapData();
			md.setNo(this.getFK_MapData());
			if (md.RetrieveFromDBSources() == 1) {
				try {
					if (DBAccess.IsExitsTableCol(md.getPTable(), this.getKeyOfEn()) == true) {
						if (SystemConfig.getAppCenterDBType() == DBType.MSSQL)
							sql = "ALTER TABLE " + md.getPTable() + " ALTER column " + this.getKeyOfEn() + " NVARCHAR("
									+ attr.getMaxLen() + ")";

						if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
							sql = "alter table " + md.getPTable() + " modify " + attr.getField() + " NVARCHAR("
									+ attr.getMaxLen() + ")";

						if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
							sql = "alter table " + md.getPTable() + " modify " + attr.getField() + " varchar2("
									+ attr.getMaxLen() + ")";

						DBAccess.RunSQL(sql);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		// #endregion 自动扩展字段长度.

		// 默认值.
		String defval = this.GetValStrByKey("ExtDefVal");
		if (defval.equals("") || defval.equals("0")) {
			String defVal = this.GetValStrByKey("DefVal");
			if (defval.contains("@") == true) {
				this.SetValByKey("DefVal", "");
			}
		} else {
			this.SetValByKey("DefVal", this.GetValByKey("ExtDefVal"));
		}

		// 执行保存.
		attr.Save();

		return super.beforeUpdateInsertAction();
	}

	@Override
	protected void afterDelete() throws Exception {
		// 删除可能存在的关联属性.
		String sql = "DELETE FROM Sys_MapAttr WHERE FK_MapData='" + this.getFK_MapData() + "' AND KeyOfEn='"
				+ this.getKeyOfEn() + "T'";
		DBAccess.RunSQL(sql);
		
		  //删除相关的图片信息.
        if (DBAccess.IsExitsTableCol("Sys_FrmImg", "KeyOfEn") == true)
            sql = "DELETE FROM Sys_FrmImg WHERE FK_MapData='" + this.getFK_MapData() + "' AND KeyOfEn='" + this.getKeyOfEn() + "T'";
        DBAccess.RunSQL(sql);

        //删除相对应的rpt表中的字段
        if (this.getFK_MapData().contains("ND") == true)
        {
            String fk_mapData = this.getFK_MapData().substring(0, this.getFK_MapData().length() - 2) + "Rpt";
            sql = "DELETE FROM Sys_MapAttr WHERE FK_MapData='" + fk_mapData + "' AND( KeyOfEn='" + this.getKeyOfEn() + "T' OR KeyOfEn='" + this.getKeyOfEn()+"')";
            DBAccess.RunSQL(sql);
        }

        //调用frmEditAction, 完成其他的操作.
        BP.Sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());

		super.afterDelete();
	}

	/**
	 * 简单模式多选
	 * 
	 * @return
	 */
	public final String DoPopSimpleModel() {
		return "../../Admin/FoolFormDesigner/PopSetting/SimpleModel.htm?FK_MapData=" + this.getFK_MapData()
				+ "&KeyOfEn=" + this.getKeyOfEn();
	}

	/**
	 * 部门人员模式多选
	 * 
	 * @return
	 */
	public final String DoPopDeptEmpModelAdv() {
		return "../../Admin/FoolFormDesigner/PopSetting/DeptEmpModelAdv.htm?FK_MapData=" + this.getFK_MapData()
				+ "&KeyOfEn=" + this.getKeyOfEn();
	}

	/**
	 * 批处理
	 * 
	 * @return
	 */
	public final String DoEleBatch() {
		return "../../Admin/FoolFormDesigner/EleBatch.htm?EleType=MapAttr&KeyOfEn=" + this.getKeyOfEn()
				+ "&FType=1&MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData();
	}

	public final String DoOldVerAspx() {
		return Glo.getCCFlowAppPath() + "WF/Admin/FoolFormDesigner/EditF.htm?DoType=Edit&KeyOfEn=" + this.getKeyOfEn()
				+ "&FType=1&MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData();
	}

	/**
	 * 小范围多选
	 * 
	 * @return
	 */
	public final String DoMultipleChoiceSmall() {
		return "../../Admin/FoolFormDesigner/MapExt/MultipleChoiceSmall.htm?FK_MapData=" + this.getFK_MapData()
				+ "&KeyOfEn=" + this.getKeyOfEn() + "&m=s";
	}

	/**
	 * 旧版本设置
	 * 
	 * @return
	 */
	public final String DoOldVer() {
		return Glo.getCCFlowAppPath() + "WF/Admin/FoolFormDesigner/EditF.htm?KeyOfEn=" + this.getKeyOfEn()
				+ "&FType=1&MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData();
	}

	/**
	 * 大范围多选
	 * 
	 * @return
	 */
	public final String DoMultipleChoiceSearch() {
		return "../../Admin/FoolFormDesigner/MapExt/MultipleChoiceSearch.htm?FK_MapData=" + this.getFK_MapData()
				+ "&KeyOfEn=" + this.getKeyOfEn() + "&m=s";
	}

	/**
	 * 超链接
	 * 
	 * @return
	 */
	public final String DoLink() {
		return "../../Admin/FoolFormDesigner/MapExt/Link.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn="
				+ this.getKeyOfEn() + "&MyPK=" + this.getMyPK() + "&FK_MapExt=Link_" + this.getFK_MapData() + "_"
				+ this.getKeyOfEn();
	}

	/// <summary>
	/// 绑定函数
	/// </summary>
	/// <returns></returns>
	public final String BindFunction() {
		return "../../Admin/FoolFormDesigner/MapExt/BindFunction.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn="
				+ this.getKeyOfEn();
	}
	
	 /**
	  * 快速录入
	  * @return
	  */
    public final String DoFastEnter()
    {
        return "../../Admin/FoolFormDesigner/MapExt/FastInput.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
    }
    /**
     * Pop窗返回值2019
     * @return
     */
    public final String DoPop2019()
    {
        return "../../Admin/FoolFormDesigner/Pop/Default.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
    }

	/**
	 * 设置开窗返回值
	 * 
	 * @return
	 */
	public final String DoPopVal() {

		return Glo.getCCFlowAppPath() + "WF/Admin/FoolFormDesigner/MapExt/PopVal.htm?FK_MapData=" + this.getFK_MapData()
				+ "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=" + this.getMyPK() + "&FK_MapExt=PopVal_"
				+ this.getFK_MapData() + "_" + this.getKeyOfEn();
	}

	/**
	 * 正则表达式
	 * 
	 * @return
	 */
	public final String DoRegularExpression() {
		return Glo.getCCFlowAppPath() + "WF/Admin/FoolFormDesigner/MapExt/RegularExpression.htm?FK_MapData="
				+ this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=" + this.getMyPK();
	}

	/**
	 * 文本框自动完成
	 * 
	 * @return
	 */
	public final String DoTBFullCtrl() {
		return Glo.getCCFlowAppPath() + "WF/Admin/FoolFormDesigner/MapExt/TBFullCtrl.htm?FK_MapData="
				+ this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=TBFullCtrl_" + this.getMyPK();
	}

	public String DoTBFullCtrl2019() {
		return "../../Admin/FoolFormDesigner/TBFullCtrl/Default.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn="
				+ this.getKeyOfEn() + "&MyPK=TBFullCtrl_" + this.getMyPK();
	}

	public final String DoPopFullCtrl() {
		return "../../Admin/FoolFormDesigner/MapExt/PopFullCtrl.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn="
				+ this.getKeyOfEn() + "&MyPK" + this.getMyPK();
	}

	/**
	 * 多条件查询列表模式
	 * 
	 * @return
	 */
	public final String DoPopFullCtrlAdv() {
		return "../../Admin/FoolFormDesigner/MapExt/PopFullCtrl.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn="
				+ this.getKeyOfEn() + "&MyPK=TBFullCtrl_" + this.getMyPK();
	}


	/**
	 * 设置级联
	 * 
	 * @return
	 */
	public final String DoInputCheck() {
		return Glo.getCCFlowAppPath() + "WF/Admin/FoolFormDesigner/MapExt/InputCheck.jsp?FK_MapData="
				+ this.getFK_MapData() + "&OperAttrKey=" + this.getKeyOfEn() + "&RefNo=" + this.getMyPK()
				+ "&DoType=New&ExtType=InputCheck";

	}

	/**
	 * 扩展控件
	 * 
	 * @return
	 */
	public final String DoEditFExtContral() {
		return Glo.getCCFlowAppPath() + "WF/Admin/FoolFormDesigner/EditFExtContral.htm?FK_MapData="
				+ this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=" + this.getMyPK();

	}
	
	/**
	 * 扩展控件2019
	 * 
	 * @return
	 */
	public final String DoEditFExtContral2019() {
		return Glo.getCCFlowAppPath() + "WF/Admin/FoolFormDesigner/EditFExtContral/Default.htm?FK_MapData="
				+ this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=" + this.getMyPK();

	}

	@Override
	protected  void afterInsertUpdateAction() throws Exception
    {
        MapAttr mapAttr = new MapAttr();
        mapAttr.setMyPK(this.getMyPK());
        mapAttr.RetrieveFromDBSources();
        mapAttr.Update();
        //调用frmEditAction, 完成其他的操作.
        BP.Sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());
        super.afterInsertUpdateAction();
    }
	
}
