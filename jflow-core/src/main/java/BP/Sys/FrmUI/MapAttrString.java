package BP.Sys.FrmUI;

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
 实体属性
*/
public class MapAttrString extends EntityMyPK
{
	/** 
	 表单ID
	*/
	public final String getFK_MapData()
	{
		return this.GetValStringByKey(MapAttrAttr.FK_MapData);
	}
	public final void setFK_MapData(String value)
	{
		this.SetValByKey(MapAttrAttr.FK_MapData, value);
	}
	/** 
	 字段
	*/
	public final String getKeyOfEn()
	{
		return this.GetValStringByKey(MapAttrAttr.KeyOfEn);
	}
	public final void setKeyOfEn(String value)
	{
		this.SetValByKey(MapAttrAttr.KeyOfEn, value);
	}
	/** 
	 绑定的枚举ID
	*/
	public final String getUIBindKey()
	{
		return this.GetValStringByKey(MapAttrAttr.UIBindKey);
	}
	public final void setUIBindKey(String value)
	{
		this.SetValByKey(MapAttrAttr.UIBindKey, value);
	}
	/** 
	 控制权限
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.IsInsert = false;
		uac.IsUpdate = true;
		uac.IsDelete = true;
		return uac;
	}
	/** 
	 实体属性
	*/
	public MapAttrString()
	{
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

		Map map = new Map("Sys_MapAttr", "文本字段");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);

		map.AddTBStringPK(MapAttrAttr.MyPK, null, "主键", false, false, 0, 200, 20);
		map.AddTBString(MapAttrAttr.FK_MapData, null, "实体标识", false, false, 1, 100, 20);

		map.AddTBString(MapAttrAttr.Name, null, "字段中文名", true, false, 0, 200, 20);
		map.AddTBString(MapAttrAttr.KeyOfEn, null, "字段名", true, true, 1, 200, 20);


		//默认值.
		//sunxd 解决ORACLE数据为自动转大写问题 
		// SELECT No,Name FROM Sys_GloVar WHERE GroupKey='DefVal' 修改为  "SELECT No \"No\",Name \"Name\" FROM Sys_GloVar WHERE GroupKey='DefVal'"
		String sql = "SELECT No ,Name FROM Sys_GloVar WHERE GroupKey='DefVal'";
		//显示的分组.
		map.AddDDLSQL("ExtDefVal", "0", "系统默认值", sql, true);

		map.AddTBString(MapAttrAttr.DefVal, null, "默认值表达式", true, false, 0, 2000, 20);

		map.AddTBInt(MapAttrAttr.MinLen, 0, "最小长度", true, false);
		map.AddTBInt(MapAttrAttr.MaxLen, 50, "最大长度", true, false);

		map.AddTBFloat(MapAttrAttr.UIWidth, 100, "宽度", true, false);
			//map.AddTBFloat(MapAttrAttr.UIHeight, 23, "高度", false, false);
		map.AddTBFloat("ExtRows", 1, "文本框行数(决定高度)", true, false);


		map.AddBoolean(MapAttrAttr.UIVisible, true, "是否可见？", true, true);
		map.AddBoolean(MapAttrAttr.UIIsEnable, true, "是否可编辑？", true, true);
		map.AddBoolean(MapAttrAttr.UIIsInput, false, "是否必填项？", true, true);
		map.AddBoolean("IsRichText", false, "是否大块文本？", true, true);
		map.AddBoolean("IsSupperText", false, "是否富文本？", true, true);
		map.AddTBString(MapAttrAttr.Tip, null, "激活提示", true, false, 0, 500, 20,true);

		map.AddDDLSysEnum(MapAttrAttr.ColSpan, 1, "单元格数量", true, true, "ColSpanAttrString", "@1=跨1个单元格@3=跨3个单元格@4=跨4个单元格");

		//显示的分组.
		//sunxd 解决ORACLE数据为自动转大写问题 
		//
		map.AddDDLSQL(MapAttrAttr.GroupID, "0", "显示的分组", "SELECT OID No, Lab Name FROM Sys_GroupField WHERE FrmID='@FK_MapData'", true);

		map.AddDDLSysEnum(MapAttrAttr.IsSigan, 0, "签名模式", true, true, MapAttrAttr.IsSigan, "@0=无@1=图片签名@2=山东CA@3=广东CA");

		RefMethod rm = new RefMethod();
 
	 

		rm = new RefMethod();
		rm.Title = "文本框自动完成";
		rm.ClassMethodName = this.toString() + ".DoTBFullCtrl()";
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
        //  #endregion 基本功能.

        //  #region 多选.
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


        ///#region Pop返回值.
		rm = new RefMethod();
		rm.GroupName = "Pop返回值2018";
		rm.Title = "枝干叶子模式";
		rm.ClassMethodName = this.toString() + ".DoBranchesAndLeaf()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.GroupName = "Pop返回值2018";
		rm.Title = "枝干叶子模式-懒加载";
		rm.ClassMethodName = this.toString() + ".DoBranchesAndLeafLazyLoad()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.GroupName = "Pop返回值2018";
		rm.Title = "枝干模式(简单)";
		rm.ClassMethodName = this.toString() + ".DoBranches()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.GroupName = "Pop返回值2018";
		rm.Title = "枝干模式(简单)-懒加载";
		rm.ClassMethodName = this.toString() + ".DoBranchesLazyLoad()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.GroupName = "Pop返回值2018";
		rm.Title = "分组列表平铺";
		rm.ClassMethodName = this.toString() + ".DoGroupList()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.GroupName = "Pop返回值2018";
		rm.Title = "单实体平铺";
		rm.ClassMethodName = this.toString() + ".DoTableList()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.GroupName = "Pop返回值2018";
		rm.Title = "列表查询模式";
		rm.ClassMethodName = this.toString() + ".DoTableSearch()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


		rm = new RefMethod();
		rm.GroupName = "Pop返回值2018";
		rm.Title = "自定义URL";
		rm.ClassMethodName = this.toString() + ".DoSelfUrl()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);


		//  设置开窗返回值-正则表达式-文本框自动完成-脚本验证-扩展控件
 
		///#endregion
		
		rm = new RefMethod();
		rm.Title = "扩展控件";
		rm.ClassMethodName = this.toString() + ".DoEditFExtContral()";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		rm.GroupName = "高级设置";
		map.AddRefMethod(rm);


 

		this.set_enMap(map);
		return this.get_enMap();
	}

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		MapAttr attr = new MapAttr();
		attr.setMyPK(this.getMyPK());
		attr.RetrieveFromDBSources();

		//高度.
		attr.setUIHeightInt(this.GetValIntByKey("ExtRows") * 23);

		attr.setIsRichText(this.GetValBooleanByKey("IsSupperText")); //是否是富文本？
		attr.setIsSupperText(this.GetValBooleanByKey("IsRichText")); //是否是大块文本？
		
		 if (attr.getIsRichText() || attr.getIsSupperText())
         {
             attr.setMaxLen(4000);
             this.SetValByKey(MapAttrAttr.MaxLen, 4000);
         }


         //#region 自动扩展字段长度.  @杜. 需要翻译.
         if (attr.getMaxLen() < 4000)
         {
             String sql = "";
             MapData md = new MapData();
             md.setNo(this.getFK_MapData());
             if (md.RetrieveFromDBSources() == 1)
             {
                 try {
					if (DBAccess.IsExitsTableCol(md.getPTable(), this.getKeyOfEn()) == true)
					 {
					     if (SystemConfig.getAppCenterDBType() == DBType.MSSQL)
					         sql = "ALTER TABLE " + md.getPTable() + " ALTER column " + this.getKeyOfEn() + " NVARCHAR(" + attr.getMaxLen() + ")";

					     if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
					         sql = "alter table " + md.getPTable() + " modify " + attr.getField() + " NVARCHAR(" + attr.getMaxLen() + ")";

					     if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
					         sql = "alter table " + md.getPTable() + " modify " + attr.getField() + " varchar2(" + attr.getMaxLen() + ")";

					     DBAccess.RunSQL(sql);
					 }
				} catch (Exception e) {
					e.printStackTrace();
				}
             }
         }
         //#endregion 自动扩展字段长度.

		//默认值.
		String defval = this.GetValStrByKey("ExtDefVal");
		if (defval.equals("") || defval.equals("0"))
		{
			String defVal = this.GetValStrByKey("DefVal");
			if (defval.contains("@") == true)
			{
				this.SetValByKey("DefVal", "");
			}
		}
		else
		{
			this.SetValByKey("DefVal", this.GetValByKey("ExtDefVal"));
		}

		//执行保存.
		attr.Save();

		return super.beforeUpdateInsertAction();
	}
	
	@Override
    protected void afterDelete() throws Exception
    {
        //删除可能存在的关联属性.
        String sql = "DELETE FROM Sys_MapAttr WHERE FK_MapData='" + this.getFK_MapData() + "' AND KeyOfEn='" + this.getKeyOfEn() + "T'";
        DBAccess.RunSQL(sql);

        super.afterDelete();
    }
	 /** 
	 简单模式多选
	 
	 @return 
*/
	public final String DoPopSimpleModel()
	{
		return "../../Admin/FoolFormDesigner/PopSetting/SimpleModel.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
	}
	  /** 
	 部门人员模式多选
	 
	 @return 
*/
	public final String DoPopDeptEmpModelAdv()
	{
		return "../../Admin/FoolFormDesigner/PopSetting/DeptEmpModelAdv.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
	}
	/** 
	 批处理
	 
	 @return 
	*/
	public final String DoEleBatch()
	{
		return Glo.getCCFlowAppPath() + "WF/MapDef/EleBatch.jsp?EleType=MapAttr&KeyOfEn=" + this.getKeyOfEn() + "&FType=1&MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData();
	}
	public final String DoOldVerAspx()
	{
		return Glo.getCCFlowAppPath() + "WF/Admin/FoolFormDesigner/EditF.htm?DoType=Edit&KeyOfEn=" + this.getKeyOfEn() + "&FType=1&MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData();
	}
	  /** 
	 小范围多选
	 
	 @return 
*/
	public final String DoMultipleChoiceSmall()
	{
		return "../../Admin/FoolFormDesigner/MapExt/MultipleChoiceSmall.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&m=s";
	}
	/** 
	 旧版本设置
	 
	 @return 
	*/
	public final String DoOldVer()
	{
		return Glo.getCCFlowAppPath() + "WF/Admin/FoolFormDesigner/EditF.htm?KeyOfEn=" + this.getKeyOfEn() + "&FType=1&MyPK=" + this.getMyPK() + "&FK_MapData=" + this.getFK_MapData();
	}
	  /** 
	 大范围多选
	 
	 @return 
*/
	public final String DoMultipleChoiceSearch()
	{
		return "../../Admin/FoolFormDesigner/MapExt/MultipleChoiceSearch.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&m=s";
	}
	  /** 
	 超链接
	 
	 @return 
*/
	public final String DoLink()
	{
		return "../../Admin/FoolFormDesigner/MapExt/Link.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=" + this.getMyPK() + "&FK_MapExt=Link_" + this.getFK_MapData() + "_" + this.getKeyOfEn();
	}
	
	 /// <summary>
    /// 绑定函数
    /// </summary>
    /// <returns></returns>
    public final String BindFunction()
    {
        return "../../Admin/FoolFormDesigner/MapExt/BindFunction.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
    }
	/** 
	 设置开窗返回值
	 
	 @return 
	*/
	public final String DoPopVal()
	{
		
		return Glo.getCCFlowAppPath() + "WF/Admin/FoolFormDesigner/MapExt/PopVal.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=" + this.getMyPK();
	}

	/** 
	 正则表达式
	 
	 @return 
	*/
	public final String DoRegularExpression()
	{
		return Glo.getCCFlowAppPath() + "WF/Admin/FoolFormDesigner/MapExt/RegularExpression.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=" + this.getMyPK();
	}
	/** 
	 文本框自动完成
	 @return 
	*/
	public final String DoTBFullCtrl()
	{
		return Glo.getCCFlowAppPath() + "WF/Admin/FoolFormDesigner/MapExt/TBFullCtrl.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=" + this.getMyPK();
		//return "/WF/Admin/FoolFormDesigner/MapExt/TBFullCtrl.htm?FK_MapData=" + this.getFK_MapData() + "&ExtType=AutoFull&KeyOfEn=" + this.getKeyOfEn() + "&RefNo=" + this.getMyPK();
	}
	 public final String DoPopFullCtrl()
		{
			return "../../Admin/FoolFormDesigner/MapExt/PopFullCtrl.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK" + this.getMyPK();
		}

	 
	 /** 多条件查询列表模式
	 
	 @return 
*/
	public final String DoPopFullCtrlAdv()
	{
		return "../../Admin/FoolFormDesigner/MapExt/PopFullCtrl.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=TBFullCtrl_" + this.getMyPK();
	}
	///#endregion 方法执行 Pop填充自动完成.
	
	
	/** 
	 设置级联
	 
	 @return 
	*/
	public final String DoInputCheck()
	{
		return Glo.getCCFlowAppPath() + "WF/Admin/FoolFormDesigner/MapExt/InputCheck.jsp?FK_MapData=" + this.getFK_MapData() + "&OperAttrKey=" + this.getKeyOfEn() + "&RefNo=" + this.getMyPK() + "&DoType=New&ExtType=InputCheck";

	   // return "/WF/Admin/FoolFormDesigner/MapExt/InputCheck.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn()  +"&RefNo="+this.getMyPK();
	  //  return "/WF/Admin/FoolFormDesigner/MapExt/InputCheck.aspx?FK_MapData=" + this.getFK_MapData() + "&ExtType=AutoFull&KeyOfEn=" + this.getKeyOfEn() + "&RefNo=" + this.getMyPK();
	}
	/** 
	 扩展控件
	 
	 @return 
	*/
	public final String DoEditFExtContral()
	{
		return Glo.getCCFlowAppPath() + "WF/Admin/FoolFormDesigner/EditFExtContral.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&MyPK=" + this.getMyPK();
		//  return "/WF/Admin/FoolFormDesigner/MapExt/InputCheck.aspx?FK_MapData=" + this.getFK_MapData() + "&ExtType=AutoFull&KeyOfEn=" + this.getKeyOfEn() + "&RefNo=" + this.getMyPK();
	}
	
	
	
			/** 
			 自定义Url.
			 
			 @return 
			*/
			public final String DoSelfUrl()
			{
				return "../../Admin/FoolFormDesigner/PopSetting/SelfUrl.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
			}
			/** 
			 实体列表模式
			 
			 @return 返回url
			*/
			public final String DoTableList()
			{
				return "../../Admin/FoolFormDesigner/PopSetting/TableList.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
			}
			/** 
			 枝干叶子模式
			 
			 @return 返回url
			*/
			public final String DoBranchesAndLeaf()
			{
				return "../../Admin/FoolFormDesigner/PopSetting/BranchesAndLeaf.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
			}
			/** 
			 枝干叶子懒加载
			 
			 @return 
			*/
			public final String DoBranchesAndLeafLazyLoad()
			{
				return "../../Admin/FoolFormDesigner/PopSetting/BranchesAndLeafLazyLoad.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn() + "&IsLazyLoad=1";
			}
			/** 
			 枝干模式
			 
			 @return 返回url
			*/
			public final String DoBranches()
			{
				return "../../Admin/FoolFormDesigner/PopSetting/Branches.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
			}
			/** 
			 枝干模式懒加载
			 
			 @return 返回url
			*/
			public final String DoBranchesLazyLoad()
			{
				return "../../Admin/FoolFormDesigner/PopSetting/Branches.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
			}
			/** 
			 分组模式
			 
			 @return 
			*/
			public final String DoGroupList()
			{
				return "../../Admin/FoolFormDesigner/PopSetting/GroupList.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
			}
			/** 
			 实体查询模式
			 
			 @return 
			*/
			public final String DoTableSearch()
			{
				return "../../Admin/FoolFormDesigner/PopSetting/TableSearch.htm?FK_MapData=" + this.getFK_MapData() + "&KeyOfEn=" + this.getKeyOfEn();
			}
}

