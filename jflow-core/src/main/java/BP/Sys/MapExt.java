package BP.Sys;

import BP.DA.*;
import BP.Tools.StringHelper;
import BP.Web.*;
import BP.En.*;

/** 
 扩展
 
*/
public class MapExt extends EntityMyPK
{

	/** 
	 转化JSON
	 @return 
	*/
	public final String PopValToJson()
	{
		//return BP.Tools.Json.ToJson(this.PopValToHashtable(), false);
		return BP.Tools.Json.ToJsonEntityModel(this.PopValToHashtable());
	}
	public final java.util.Hashtable PopValToHashtable()
	{

		//创建一个ht, 然后把他转化成json返回出去。
		java.util.Hashtable ht = new java.util.Hashtable();
		//ht.put("EntitySQL", this.getPopValEntitySQL());
		switch (this.getPopValWorkModelNew())
		{
			case SelfUrl:
				ht.put("URL", this.getPopValUrl());
				break;
			case TableOnly:
				ht.put("EntitySQL", this.getPopValEntitySQL());
				break;
			case TablePage:
				ht.put("PopValTablePageSQL", this.getPopValTablePageSQL());
				ht.put("PopValTablePageSQLCount", this.getPopValTablePageSQLCount());
				break;
			case Group:
				ht.put("GroupSQL", this.getTag1());
				ht.put("EntitySQL", this.getPopValEntitySQL());
				break;
			case Tree:
				ht.put("TreeSQL", this.getPopValTreeSQL());
				ht.put("TreeParentNo", this.getPopValTreeParentNo());
				break;
			case TreeDouble:
				ht.put("DoubleTreeSQL", this.getPopValTreeSQL());
				ht.put("DoubleTreeParentNo", this.getPopValTreeParentNo());
				ht.put("DoubleTreeEntitySQL", this.getPopValDoubleTreeEntitySQL());
				break;
			default:
				break;
		}

		ht.put(MapExtAttr.W, this.getW());
		ht.put(MapExtAttr.H, this.getH());

		ht.put("PopValWorkModel", this.getPopValWorkModelNew()); //工作模式.
		ht.put("PopValSelectModel", this.getPopValSelectModelNew()); //单选，多选.

		ht.put("PopValFormat", this.getPopValFormat()); //返回值格式.
		ht.put("PopValTitle", this.getPopValTitle()); //窗口标题.
		ht.put("PopValColNames", this.getPopValColNames()); //列名 @No=编号@Name=名称@Addr=地址.
		ht.put("PopValSearchTip", this.getPopValSearchTip()); //搜索提示..

		//查询条件.
		ht.put("PopValSearchCond", this.getPopValSearchCond()); //查询条件..
		//转化为Json.
		return ht;
	}
	/** 
	 连接
	 
	*/
	public final String getPopValUrl()
	{
		return this.getDoc();
	}
	public final void setPopValUrl(String value)
	{
		this.setDoc(value);
	}
	/** 
	 实体SQL
	 
	*/
	public final String getPopValEntitySQL()
	{
		return this.getTag2();
	}
	public final void setPopValEntitySQL(String value)
	{
		this.setTag2(value);
	}
	/** 
	 分组SQL
	 
	*/
	public final String getPopValGroupSQL()
	{
		return this.getTag1();
	}
	public final void setPopValGroupSQL(String value)
	{
		this.setTag1(value);
	}
	/** 
	 分页SQL带有关键字
	 
	*/
	public final String getPopValTablePageSQL()
	{
		return this.getTag();
	}
	public final void setPopValTablePageSQL(String value)
	{
		this.setTag(value);
	}
	/** 
	 分页SQL获取总行数
	 
	*/
	public final String getPopValTablePageSQLCount()
	{
		return this.getTag1();
	}
	public final void setPopValTablePageSQLCount(String value)
	{
		this.setTag1(value);
	}
	/** 
	 标题
	 
	*/
	public final String getPopValTitle()
	{
		return this.GetParaString("PopValTitle");
	}
	public final void setPopValTitle(String value)
	{
		this.SetPara("PopValTitle", value);
	}

	public final String getPopValTreeSQL()
	{
		return this.getPopValEntitySQL();
	}
	public final void setPopValTreeSQL(String value)
	{
		  this.setPopValEntitySQL(value);
	}
	/** 
	 根目录
	 
	*/
	public final String getPopValTreeParentNo()
	{
		return this.GetParaString("PopValTreeParentNo");
	}
	public final void setPopValTreeParentNo(String value)
	{
		this.SetPara("PopValTreeParentNo", value);
	}
	/** 
	 Pop 返回值的格式.
	*/
	public final int getPopValFormat()
	{
		return this.GetParaInt("PopValFormat");
	}
	
	public final void setPopValFormat(int value)
	{
		this.SetPara("PopValFormat", value);
	}
	public PopValFormat getPopValFormatNew()
	{
		System.out.println(PopValFormat.forValue(this.GetParaInt("PopValFormat")));
		
		return PopValFormat.forValue(this.GetParaInt("PopValFormat"));
	}
	
	public final void setPopValFormatNew(PopValFormat value)
	{
		this.SetPara("PopValFormat", value.getValue());
	}
	

	/** 
	 双实体树的实体
	 
	*/
	public final String getPopValDoubleTreeEntitySQL()
	{
		return this.getTag1();
	}
	public final void setPopValDoubleTreeEntitySQL(String value)
	{
		this.setTag1(value);

	}

	/** 
	 pop 选择方式
	 0,多选,1=单选.
	 
	*/
	public final int getPopValSelectModel()
	{
		return this.GetParaInt("PopValSelectModel");
	}
	
	public final void setPopValSelectModel(int value)
	{
		this.SetPara("PopValSelectModel", value);
	}
	
	public PopValSelectModel getPopValSelectModelNew()
    {
        return PopValSelectModel.forValue(this.GetParaInt("PopValSelectModel"));
    }
	public  final void setPopValSelectModelNew(PopValSelectModel value)
    {
        this.SetPara("PopValSelectModel", value.getValue());
    }
	
	
	/** 
	 PopVal工作模式
	*/
	
	public final PopValWorkModel getPopValWorkModelNew()
	{
		 return PopValWorkModel.values()[this.GetParaInt("PopValWorkModel")];
	}
	
	public final void setPopValWorkModelNew(PopValWorkModel value)
	{
		this.SetPara("PopValWorkModel", value.getValue());
	}
	
	
	public int getPopValWorkModel()
	{
		 return this.GetParaInt("PopValWorkModel");
	}
	public void setPopValWorkModel(int value)
	{
		 this.SetPara("PopValWorkModel",value);
	}
	/** 
	 开窗的列中文名称.
	 
	*/
	public final String getPopValColNames()
	{
	  return this.getTag3();
	}
	public final void setPopValColNames(String value)
	{
		this.setTag3(value);
	}
	
	/**
	 * pop 呈现方式 0,表格,1=目录.
	 */
	public final int getPopValShowModel()
	{
		return this.GetParaInt("PopValShowModel");
	}
	
	public final void setPopValShowModel(int value)
	{
		this.SetPara("PopValShowModel", value);
	}
	/** 
	 查询条件
	 
	*/
	public final String getPopValSearchCond()
	{
	  return this.getTag4();
	}
	public final void setPopValSearchCond(String value)
	{
		this.setTag4(value);
	}
	/** 
	 搜索提示关键字
	 
	*/
	public final String getPopValSearchTip()
	{
		return this.GetParaString("PopValSearchTip", "请输入关键字");
	}
	public final void setPopValSearchTip(String value)
	{
		this.SetPara("PopValSearchTip", value);
	}


	/** 
	 数据源
	 
	*/
	public final String getFK_DBSrc()
	{
		return this.GetValStrByKey(MapExtAttr.FK_DBSrc);
	}
	public final void setFK_DBSrc(String value)
	{
		this.SetValByKey(MapExtAttr.FK_DBSrc, value);
	}

		///#endregion


		
	public final String getExtDesc()
	{
		String dec = "";

//		switch (this.ExtType)
//ORIGINAL LINE: case MapExtXmlList.ActiveDDL:
		if (this.getExtType().equals(MapExtXmlList.ActiveDDL))
		{
				dec += "字段" + this.getAttrOfOper();
		}
//ORIGINAL LINE: case MapExtXmlList.TBFullCtrl:
		else if (this.getExtType().equals(MapExtXmlList.TBFullCtrl))
		{
				dec += this.getAttrOfOper();
		}
//ORIGINAL LINE: case MapExtXmlList.DDLFullCtrl:
		else if (this.getExtType().equals(MapExtXmlList.DDLFullCtrl))
		{
				dec += "" + this.getAttrOfOper();
		}
//ORIGINAL LINE: case MapExtXmlList.InputCheck:
		else if (this.getExtType().equals(MapExtXmlList.InputCheck))
		{
				dec += "字段：" + this.getAttrOfOper() + " 检查内容：" + this.getTag1();
		}
//ORIGINAL LINE: case MapExtXmlList.PopVal:
		else if (this.getExtType().equals(MapExtXmlList.PopVal))
		{
				dec += "字段：" + this.getAttrOfOper() + " Url：" + this.getTag();
		}
		else
		{
		}
		return dec;
	}
	/** 
	 是否自适应大小
	 
	*/
	public final boolean getIsAutoSize()
	{
		return this.GetValBooleanByKey(MapExtAttr.IsAutoSize);
	}
	public final void setIsAutoSize(boolean value)
	{
		this.SetValByKey(MapExtAttr.IsAutoSize, value);
	}
	/** 
	 数据源
	*/
	public final String getDBType()
	{
		return this.GetValStrByKey(MapExtAttr.DBType);
	}
	public final void setDBSrc(String value)
	{
		this.SetValByKey(MapExtAttr.DBType, value);
	}
	public final void setDBType(String value)
	{
		this.SetValByKey(MapExtAttr.DBType, value);
	}
	public final String getAtPara()
	{
		return this.GetValStrByKey(MapExtAttr.AtPara);
	}
	public final void setAtPara(String value)
	{
		this.SetValByKey(MapExtAttr.AtPara, value);
	}

	public final String getExtType()
	{
		return this.GetValStrByKey(MapExtAttr.ExtType);
	}
	public final void setExtType(String value)
	{
		this.SetValByKey(MapExtAttr.ExtType, value);
	}
	public final int getDoWay()
	{
		return this.GetValIntByKey(MapExtAttr.DoWay);
	}
	public final void setDoWay(int value)
	{
		this.SetValByKey(MapExtAttr.DoWay, value);
	}
	/** 
	 操作的attrs
	 
	*/
	public final String getAttrOfOper()
	{
		return this.GetValStrByKey(MapExtAttr.AttrOfOper);
	}
	public final void setAttrOfOper(String value)
	{
		this.SetValByKey(MapExtAttr.AttrOfOper, value);
	}
	public final String getAttrOfOperToLowerCase()
	{
		return getAttrOfOper().toLowerCase();
	}
	/** 
	 激活的attrs
	 
	*/
	public final String getAttrsOfActive()
	{
		  //  return this.GetValStrByKey(MapExtAttr.AttrsOfActive).replace("~", "'");
		return this.GetValStrByKey(MapExtAttr.AttrsOfActive);
	}
	public final void setAttrsOfActive(String value)
	{
		this.SetValByKey(MapExtAttr.AttrsOfActive, value);
	}
	public final String getFK_MapData()
	{
		return this.GetValStrByKey(MapExtAttr.FK_MapData);
	}
	public final void setFK_MapData(String value)
	{
		this.SetValByKey(MapExtAttr.FK_MapData, value);
	}
	/** 
	 															
	 
	*/
	public final String getDoc()
	{
		return this.GetValStrByKey("Doc").replace("~","'");
	}
	public final void setDoc(String value)
	{
		this.SetValByKey("Doc", value);
	}

   /** 
	 处理自动填充SQL
	
	@param ht
	@return 
 * @throws Exception 
   */
	public final String AutoFullDLL_SQL_ForDtl(java.util.Hashtable htMainEn, java.util.Hashtable htDtlEn) throws Exception
	{
		String fullSQL = this.getDoc().replace("WebUser.No", WebUser.getNo());
		fullSQL = fullSQL.replace("@WebUser.Name", WebUser.getName());
		fullSQL = fullSQL.replace("@WebUser.FK_DeptName", WebUser.getFK_DeptName());
		fullSQL = fullSQL.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());

		if (fullSQL.contains("@"))
		{
			for (Object key : htDtlEn.keySet())
			{
				if (fullSQL.contains("@") == false)
				{
					break;
				}
				if (fullSQL.contains("@" + key + ";") == true)
				{
					fullSQL = fullSQL.replace("@" + key + ";", (String)((htDtlEn.get(key) instanceof String) ? htDtlEn.get(key) : null));
				}

				if (fullSQL.contains("@" + key) == true)
				{
					fullSQL = fullSQL.replace("@" + key, (String)((htDtlEn.get(key) instanceof String) ? htDtlEn.get(key) : null));
				}
			}
		}

		if (fullSQL.contains("@"))
		{
			for (Object key : htMainEn.keySet())
			{
				if (fullSQL.contains("@") == false)
				{
					break;
				}

				if (fullSQL.contains("@" + key + ";") == true)
				{
					fullSQL = fullSQL.replace("@" + key + ";", (String)((htMainEn.get(key) instanceof String) ? htMainEn.get(key) : null));
				}

				if (fullSQL.contains("@" + key) == true)
				{
					fullSQL = fullSQL.replace("@" + key, (String)((htMainEn.get(key) instanceof String) ? htMainEn.get(key) : null));
				}
			}
		}
		return fullSQL;
	}

	public final String getTagOfSQL_autoFullTB() throws Exception
	{
		if (StringHelper.isNullOrEmpty(this.getTag()))
		{
			return this.getDocOfSQLDeal();
		}

		String sql = this.getTag();
		sql = sql.replace("WebUser.No", BP.Web.WebUser.getNo());
		sql = sql.replace("@WebUser.Name", BP.Web.WebUser.getName());
		sql = sql.replace("@WebUser.FK_DeptNameOfFull", BP.Web.WebUser.getFK_DeptNameOfFull());
		sql = sql.replace("@WebUser.FK_DeptName", BP.Web.WebUser.getFK_DeptName());
		sql = sql.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
		return sql;
	}

	public final String getDocOfSQLDeal() throws Exception
	{
		String sql = this.getDoc();
		sql = sql.replace("WebUser.No", BP.Web.WebUser.getNo());
		sql = sql.replace("@WebUser.Name", BP.Web.WebUser.getName());
		sql = sql.replace("@WebUser.FK_DeptNameOfFull", BP.Web.WebUser.getFK_DeptNameOfFull());
		sql = sql.replace("@WebUser.FK_DeptName", BP.Web.WebUser.getFK_DeptName());
		sql = sql.replace("@WebUser.FK_Dept", BP.Web.WebUser.getFK_Dept());
		return sql;
	}
	public final String getTag()
	{
		String s= this.GetValStrByKey("Tag").replace("~", "'");

		s = s.replace("\\\\", "\\");
		s = s.replace("\\\\", "\\");

		s = s.replace("CCFlow\\Data\\", "CCFlow\\WF\\Data\\");

		return s;
	}
	public final void setTag(String value)
	{
		this.SetValByKey("Tag", value);
	}
	public final String getTag1()
	{
		return this.GetValStrByKey("Tag1").replace("~", "'");
	}
	public final void setTag1(String value)
	{
		this.SetValByKey("Tag1", value);
	}
	public final String getTag2()
	{
		return this.GetValStrByKey("Tag2").replace("~", "'");
	}
	public final void setTag2(String value)
	{
		this.SetValByKey("Tag2", value);
	}
	public final String getTag3()
	{
		return this.GetValStrByKey("Tag3").replace("~", "'");
	}
	public final void setTag3(String value)
	{
		this.SetValByKey("Tag3", value);
	}
	public final String getTag4()
	{
		return this.GetValStrByKey("Tag4").replace("~", "'");
	}
	public final void setTag4(String value)
	{
		this.SetValByKey("Tag4", value);
	}
	public final String getTag5()
	{
		return this.GetValStrByKey("Tag5").replace("~", "'");
	}
	public final void setTag5(String value)
	{
		this.SetValByKey("Tag5", value);
	}
	public final String getDBSrc()
	{
		return this.GetValStrByKey("DBSrc");
	}
	

	public final int getH()
	{
		return this.GetValIntByKey(MapExtAttr.H);
	}
	public final void setH(int value)
	{
		this.SetValByKey(MapExtAttr.H, value);
	}
	public final int getW()
	{
		return this.GetValIntByKey(MapExtAttr.W);
	}
	public final void setW(int value)
	{
		this.SetValByKey(MapExtAttr.W, value);
	}

		///#endregion


		
	/** 
	 扩展
	 
	*/
	public MapExt()
	{
	}
	/** 
	 扩展
	 
	 @param no
	 * @throws Exception 
	*/
	public MapExt(String mypk) throws Exception
	{
		this.setMyPK(mypk);
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

		Map map = new Map("Sys_MapExt", "业务逻辑");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);

		map.AddMyPK();

		map.AddTBString(MapExtAttr.FK_MapData, null, "主表", true, false, 0, 100, 20);
		map.AddTBString(MapExtAttr.ExtType, null, "类型", true, false, 0, 30, 20);
		map.AddTBInt(MapExtAttr.DoWay, 0, "执行方式", true, false);

		map.AddTBString(MapExtAttr.AttrOfOper, null, "操作的Attr", true, false, 0, 30, 20);
		map.AddTBString(MapExtAttr.AttrsOfActive, null, "激活的字段", true, false, 0, 900, 20);

		map.AddTBString(MapExtAttr.FK_DBSrc, null, "数据源", true, false, 0, 100, 20);
		map.AddTBStringDoc();
		map.AddTBString(MapExtAttr.Tag, null, "Tag", true, false, 0, 2000, 20);
		map.AddTBString(MapExtAttr.Tag1, null, "Tag1", true, false, 0, 2000, 20);
		map.AddTBString(MapExtAttr.Tag2, null, "Tag2", true, false, 0, 2000, 20);
		map.AddTBString(MapExtAttr.Tag3, null, "Tag3", true, false, 0, 2000, 20);
		map.AddTBString(MapExtAttr.Tag4, null, "Tag4", true, false, 0, 2000, 20);
		map.AddTBString(MapExtAttr.Tag5, null, "Tag5", true, false, 0, 2000, 20);
		
		map.AddTBString(MapExtAttr.AtPara, null, "参数", true, false, 0, 2000, 20);
		

        // 数据类型 @0=SQL@1=URLJSON@2=FunctionJSON.
        //map.AddTBString(MapExtAttr.DBType, null, "数据类型", true, false, 0, 20, 20);
        map.AddTBInt(MapExtAttr.DBType, 0, "数据类型", true, false);
		map.AddTBString(MapExtAttr.DBSrc, null, "数据源", true, false, 0, 20, 20);

		map.AddTBInt(MapExtAttr.H, 500, "高度", false, false);
		map.AddTBInt(MapExtAttr.W, 400, "宽度", false, false);

			// add by stone 2013-12-21 计算的优先级,用于js的计算.
		map.AddTBInt(MapExtAttr.PRI, 0, "PRI", false, false);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion


		///#region 其他方法.
	/** 
	 统一生成主键的规则.
	 
	*/
	public final void InitPK()
	{
		String extType = this.getExtType();
	    if(extType.equals(MapExtXmlList.FullData)){
	    	//不做任何处理
	    }
	    else if (extType.equals(MapExtXmlList.ActiveDDL))
				this.setMyPK(MapExtXmlList.ActiveDDL +"_"+this.getFK_MapData() + "_" + this.getAttrsOfActive());
		
		else if (extType.equals(MapExtXmlList.DDLFullCtrl))
				this.setMyPK(MapExtXmlList.DDLFullCtrl + "_" + this.getFK_MapData() + "_" + this.getAttrOfOper());
		
		else if (extType.equals(MapExtXmlList.PopVal))
			this.setMyPK(MapExtXmlList.PopVal + "_" + this.getFK_MapData() + "_" + this.getAttrOfOper());
		
		else if(extType.equals(MapExtXmlList.TBFullCtrl))
              this.setMyPK(MapExtXmlList.TBFullCtrl + "_" + this.getFK_MapData() + "_" + this.getAttrOfOper());
		
		else if(extType.equals(MapExtXmlList.AutoFull))
			  this.setMyPK(MapExtXmlList.AutoFull + "_" + this.getFK_MapData() + "_" + this.getAttrOfOper());
		
		else if(extType.equals(MapExtXmlList.AutoFullDLL))
            this.setMyPK(MapExtXmlList.AutoFullDLL + "_" + this.getFK_MapData() + "_" + this.getAttrOfOper());
		
		else if(extType.equals(MapExtXmlList.InputCheck))
            this.setMyPK(MapExtXmlList.InputCheck + "_" + this.getFK_MapData() + "_" + this.getAttrOfOper());
		
		else if(extType.equals(MapExtXmlList.PageLoadFull))
            this.setMyPK(MapExtXmlList.PageLoadFull + "_" + this.getFK_MapData());
		
		else if(extType.equals(MapExtXmlList.RegularExpression))
            this.setMyPK(MapExtXmlList.RegularExpression + "_" + this.getFK_MapData() + "_" + this.getAttrOfOper() + "_" + this.getTag());
		
		else if(extType.equals(MapExtXmlList.Link))
            this.setMyPK(MapExtXmlList.Link + "_" + this.getFK_MapData() + "_" + this.getAttrOfOper());  
		
		else if(extType.equals(MapExtXmlList.BindFunction))
			this.setMyPK(MapExtXmlList.BindFunction + "_" + this.getFK_MapData() + "_" + this.getAttrOfOper() + "_" + this.getTag());
		else
		{
			  //这里要去掉，不然组合组主键，会带来错误.
            if (DataType.IsNullOrEmpty(this.getAttrOfOper()) == true)
                this.setMyPK( this.getExtType() + "_" + this.getFK_MapData());
            else
                this.setMyPK( this.getExtType() + "_" + this.getFK_MapData() + "_" + this.getAttrOfOper());
		}
	}
	

	/** 
	 删除垃圾数据.
	 * @throws Exception 
	 
	*/
	public static void DeleteDB() throws Exception
	{
		MapExts exts = new MapExts();
		exts.RetrieveAll();

		for (Object ext : exts)
		{
			if (((MapExt) ext).getExtType().equals(MapExtXmlList.ActiveDDL))
			{
				if (((MapExt) ext).getAttrOfOper().trim().length() == 0)
				{
					((Entity) ext).Delete();
					continue;
				}

				MapAttr attr = new MapAttr();
				attr.setMyPK(((MapExt) ext).getAttrOfOper());
				if (attr.getIsExits() == true)
				{
					((MapExt) ext).setAttrOfOper(attr.getKeyOfEn());
					((Entity) ext).Delete();

					((EntityMyPK) ext).setMyPK(((MapExt) ext).getExtType() + "_" + ((MapExt) ext).getFK_MapData() + "_" + ((MapExt) ext).getAttrOfOper() + "_" + ((MapExt) ext).getAttrsOfActive());
					((Entity) ext).Save();
				}

				if (((EntityMyPK) ext).getMyPK().equals(((MapExt) ext).getExtType()) + "_" + ((MapExt) ext).getFK_MapData() + "_" + ((MapExt) ext).getFK_MapData() + "_" + ((MapExt) ext).getAttrOfOper() != null)
				{
					((Entity) ext).Delete(); //直接删除.

					((EntityMyPK) ext).setMyPK(((MapExt) ext).getExtType() + "_" + ((MapExt) ext).getFK_MapData() + "_" + ((MapExt) ext).getAttrOfOper() + "_" + ((MapExt) ext).getAttrsOfActive());
					((Entity) ext).Save();
					continue;
				}

				if (((EntityMyPK) ext).getMyPK().equals(((MapExt) ext).getExtType()) + "_" + ((MapExt) ext).getFK_MapData() + "_" + ((MapExt) ext).getFK_MapData() + "_" + ((MapExt) ext).getAttrOfOper() + "_" + ((MapExt) ext).getAttrsOfActive() != null)
				{
					((Entity) ext).Delete(); //直接删除.
					((EntityMyPK) ext).setMyPK(((MapExt) ext).getExtType() + "_" + ((MapExt) ext).getFK_MapData() + "_" + ((MapExt) ext).getAttrOfOper() + "_" + ((MapExt) ext).getAttrsOfActive());
					((Entity) ext).Save();
					continue;
				}

				if (((EntityMyPK) ext).getMyPK().equals(((MapExt) ext).getExtType()) + "_" + ((MapExt) ext).getFK_MapData() + "_" + ((MapExt) ext).getFK_MapData() + "_" + ((MapExt) ext).getAttrsOfActive() + "_" + ((MapExt) ext).getAttrOfOper() != null)
				{
					((Entity) ext).Delete(); //直接删除.
					((EntityMyPK) ext).setMyPK(((MapExt) ext).getExtType() + "_" + ((MapExt) ext).getFK_MapData() + "_" + ((MapExt) ext).getAttrOfOper() + "_" + ((MapExt) ext).getAttrsOfActive());
					((Entity) ext).Save();
					continue;
				}


				//三个主键的情况.
				if (((EntityMyPK) ext).getMyPK().equals(((MapExt) ext).getExtType()) + "_" + ((MapExt) ext).getFK_MapData() + "_" + ((MapExt) ext).getAttrOfOper() != null)
				{
					((Entity) ext).Delete();
					((EntityMyPK) ext).setMyPK(((MapExt) ext).getExtType() + "_" + ((MapExt) ext).getFK_MapData() + "_" + ((MapExt) ext).getAttrOfOper() + "_" + ((MapExt) ext).getAttrsOfActive());
					((Entity) ext).Save();
					continue;
				}

				//三个主键的情况.
				if (((EntityMyPK) ext).getMyPK().equals(((MapExt) ext).getExtType()) + "_" + ((MapExt) ext).getFK_MapData() + "_" + ((MapExt) ext).getAttrsOfActive() != null)
				{
					((Entity) ext).Delete();
					((EntityMyPK) ext).setMyPK(((MapExt) ext).getExtType() + "_" + ((MapExt) ext).getFK_MapData() + "_" + ((MapExt) ext).getAttrOfOper() + "_" + ((MapExt) ext).getAttrsOfActive());
					((Entity) ext).Save();
					continue;
				}

			}
		}
	}
}