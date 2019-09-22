package BP.Sys;

import BP.DA.*;
import BP.Web.*;
import BP.En.*;
import java.util.*;

/** 
 扩展
*/
public class MapExt extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 关于 Pop at 参数
	/** 
	 转化JSON
	 
	 @return 
	*/
	public final String PopValToJson()
	{
		return BP.Tools.Json.ToJsonEntityModel(this.PopValToHashtable());
	}
	public final Hashtable PopValToHashtable()
	{

		//创建一个ht, 然后把他转化成json返回出去。
		Hashtable ht = new Hashtable();

		switch (this.getPopValWorkModel())
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

		ht.put("PopValWorkModel", this.getPopValWorkModel().toString()); //工作模式.
		ht.put("PopValSelectModel", this.getPopValSelectModel().toString()); //单选，多选.

		ht.put("PopValFormat", this.getPopValFormat().toString()); //返回值格式.
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
	public final PopValFormat getPopValFormat()
	{
		return PopValFormat.forValue(this.GetParaInt("PopValFormat"));
	}
	public final void setPopValFormat(PopValFormat value)
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
	public final PopValSelectModel getPopValSelectModel()
	{
		return PopValSelectModel.forValue(this.GetParaInt("PopValSelectModel"));
	}
	public final void setPopValSelectModel(PopValSelectModel value)
	{
		this.SetPara("PopValSelectModel", value.getValue());
	}
	/** 
	 PopVal工作模式
	*/
	public final PopValWorkModel getPopValWorkModel()
	{
		return PopValWorkModel.forValue(this.GetParaInt("PopValWorkModel"));
	}
	public final void setPopValWorkModel(PopValWorkModel value)
	{
		this.SetPara("PopValWorkModel", value.getValue());
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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	public final String getExtDesc()
	{
		String dec = "";
		switch (this.getExtType())
		{
			case MapExtXmlList.ActiveDDL:
				dec += "字段" + this.getAttrOfOper();
				break;
			case MapExtXmlList.TBFullCtrl:
				dec += this.getAttrOfOper();
				break;
			case MapExtXmlList.DDLFullCtrl:
				dec += "" + this.getAttrOfOper();
				break;
			case MapExtXmlList.InputCheck:
				dec += "字段：" + this.getAttrOfOper() + " 检查内容：" + this.getTag1();
				break;
			case MapExtXmlList.PopVal:
				dec += "字段：" + this.getAttrOfOper() + " Url：" + this.getTag();
				break;
			default:
				break;
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
	 数据格式
	*/
	public final String getDBType()
	{
		return this.GetValStrByKey(MapExtAttr.DBType);
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
	 Doc
	*/
	public final String getDoc()
	{
		String str = this.GetValStrByKey("Doc").replace("~","'");
		str = str.replace("~", "'");
		return str;
	}
	public final void setDoc(String value)
	{
		String str = value.replace("'", "~");
		this.SetValByKey("Doc", str);
	}

   /** 
	 处理自动填充SQL
	
	@param ht
	@return 
   */
	public final String AutoFullDLL_SQL_ForDtl(Hashtable htMainEn, Hashtable htDtlEn)
	{
		String fullSQL = this.getDoc().replace("@WebUser.getNo()", WebUser.getNo());
		fullSQL = fullSQL.replace("@WebUser.getName()", WebUser.getName());
		fullSQL = fullSQL.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
		fullSQL = fullSQL.replace("@WebUser.getFK_DeptName", WebUser.getFK_DeptName());

		if (fullSQL.contains("@"))
		{
			for (String key : htDtlEn.keySet())
			{
				if (fullSQL.contains("@") == false)
				{
					break;
				}
				if (fullSQL.contains("@" + key + ";") == true)
				{
					fullSQL = fullSQL.replace("@" + key + ";", htDtlEn.get(key) instanceof String ? (String)htDtlEn.get(key) : null);
				}

				if (fullSQL.contains("@" + key) == true)
				{
					fullSQL = fullSQL.replace("@" + key, htDtlEn.get(key) instanceof String ? (String)htDtlEn.get(key) : null);
				}
			}
		}

		if (fullSQL.contains("@"))
		{
			for (String key : htMainEn.keySet())
			{
				if (fullSQL.contains("@") == false)
				{
					break;
				}

				if (fullSQL.contains("@" + key + ";") == true)
				{
					fullSQL = fullSQL.replace("@" + key + ";", htMainEn.get(key) instanceof String ? (String)htMainEn.get(key) : null);
				}

				if (fullSQL.contains("@" + key) == true)
				{
					fullSQL = fullSQL.replace("@" + key, htMainEn.get(key) instanceof String ? (String)htMainEn.get(key) : null);
				}
			}
		}
		return fullSQL;
	}

	public final String getTagOfSQL_autoFullTB() throws Exception
	{
		if (DataType.IsNullOrEmpty(this.getTag()))
		{
			return this.getDocOfSQLDeal();
		}

		String sql = this.getTag();
		sql = sql.replace("@WebUser.getNo()", WebUser.getNo());
		sql = sql.replace("@WebUser.getName()", WebUser.getName());
		sql = sql.replace("@WebUser.getFK_DeptNameOfFull", WebUser.getFK_DeptNameOfFull());
		sql = sql.replace("@WebUser.getFK_DeptName", WebUser.getFK_DeptName());
		sql = sql.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
		return sql;
	}

	public final String getDocOfSQLDeal()
	{
		String sql = this.getDoc();
		sql = sql.replace("@WebUser.getNo()", WebUser.getNo());
		sql = sql.replace("@WebUser.getName()", WebUser.getName());
		sql = sql.replace("@WebUser.getFK_DeptNameOfFull", WebUser.getFK_DeptNameOfFull());
		sql = sql.replace("@WebUser.getFK_DeptName", WebUser.getFK_DeptName());
		sql = sql.replace("@WebUser.getFK_Dept()", WebUser.getFK_Dept());
		return sql;
	}
	public final String getTag()
	{
		String s = this.GetValStrByKey("Tag").replace("~", "'");

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
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
	/** 
	 扩展
	*/
	public MapExt()
	{
	}
	/** 
	 扩展
	 
	 @param no
	*/
	public MapExt(String mypk)
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

		map.IndexField = MapDtlAttr.FK_MapData;

		map.AddMyPK();

		map.AddTBString(MapExtAttr.FK_MapData, null, "主表", true, false, 0, 100, 20);
		map.AddTBString(MapExtAttr.ExtType, null, "类型", true, false, 0, 30, 20);

		map.AddTBInt(MapExtAttr.DoWay, 0, "执行方式", true, false);

		map.AddTBString(MapExtAttr.AttrOfOper, null, "操作的Attr", true, false, 0, 30, 20);
		map.AddTBString(MapExtAttr.AttrsOfActive, null, "激活的字段", true, false, 0, 900, 20);

		map.AddTBStringDoc();

		map.AddTBString(MapExtAttr.Tag, null, "Tag", true, false, 0, 2000, 20);

		map.AddTBString(MapExtAttr.Tag1, null, "Tag1", true, false, 0, 2000, 20);
		map.AddTBString(MapExtAttr.Tag2, null, "Tag2", true, false, 0, 2000, 20);
		map.AddTBString(MapExtAttr.Tag3, null, "Tag3", true, false, 0, 2000, 20);
		map.AddTBString(MapExtAttr.Tag4, null, "Tag4", true, false, 0, 2000, 20);
		map.AddTBString(MapExtAttr.Tag5, null, "Tag5", true, false, 0, 2000, 20);


		map.AddTBInt(MapExtAttr.H, 500, "高度", false, false);
		map.AddTBInt(MapExtAttr.W, 400, "宽度", false, false);

			// 数据类型 @0=SQL@1=URLJSON@2=FunctionJSON.
		map.AddTBInt(MapExtAttr.DBType, 0, "数据类型", true, false);
		map.AddTBString(MapExtAttr.FK_DBSrc, null, "数据源", true, false, 0, 100, 20);

			// add by stone 2013-12-21 计算的优先级,用于js的计算. 
			// 也可以用于 字段之间的计算 优先级.
		map.AddTBInt(MapExtAttr.PRI, 0, "PRI/顺序号", false, false);
		map.AddTBString(MapExtAttr.AtPara, null, "参数", true, false, 0, 3999, 20);

		this.set_enMap(map);
		return this.get_enMap();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 其他方法.
	/** 
	 统一生成主键的规则.
	*/
	public final void InitPK()
	{
		switch (this.getExtType())
		{
			case MapExtXmlList.FullData:
				break;
			case MapExtXmlList.ActiveDDL:
				this.setMyPK(MapExtXmlList.ActiveDDL + "_" + this.getFK_MapData() + "_" + this.getAttrOfOper());
				break;
			case MapExtXmlList.DDLFullCtrl:
				this.setMyPK(MapExtXmlList.DDLFullCtrl + "_" + this.getFK_MapData() + "_" + this.getAttrOfOper());
				break;
			case MapExtXmlList.PopVal:
				this.setMyPK(MapExtXmlList.PopVal + "_" + this.getFK_MapData() + "_" + this.getAttrOfOper());
				break;
			case MapExtXmlList.TBFullCtrl:
				this.setMyPK(MapExtXmlList.TBFullCtrl + "_" + this.getFK_MapData() + "_" + this.getAttrOfOper());
				break;
			case MapExtXmlList.PopFullCtrl:
				this.setMyPK(MapExtXmlList.PopFullCtrl + "_" + this.getFK_MapData() + "_" + this.getAttrOfOper());
				break;
			case MapExtXmlList.AutoFull:
				this.setMyPK(MapExtXmlList.AutoFull + "_" + this.getFK_MapData() + "_" + this.getAttrOfOper());
				break;
			case MapExtXmlList.AutoFullDLL:
				this.setMyPK(MapExtXmlList.AutoFullDLL + "_" + this.getFK_MapData() + "_" + this.getAttrOfOper());
				break;
			case MapExtXmlList.InputCheck:
				this.setMyPK(MapExtXmlList.InputCheck + "_" + this.getFK_MapData() + "_" + this.getAttrOfOper());
				break;
			case MapExtXmlList.PageLoadFull:
				this.setMyPK(MapExtXmlList.PageLoadFull + "_" + this.getFK_MapData());
				break;
			case MapExtXmlList.RegularExpression:
				this.setMyPK(MapExtXmlList.RegularExpression + "_" + this.getFK_MapData() + "_" + this.getAttrOfOper() + "_" + this.getTag());
				break;
			case MapExtXmlList.BindFunction:
				this.setMyPK(MapExtXmlList.BindFunction + "_" + this.getFK_MapData() + "_" + this.getAttrOfOper() + "_" + this.getTag());
				break;
			case MapExtXmlList.Link:
				this.setMyPK(MapExtXmlList.Link + "_" + this.getFK_MapData() + "_" + this.getAttrOfOper());
				break;
			default:
				//这里要去掉，不然组合组主键，会带来错误.
				if (DataType.IsNullOrEmpty(this.getAttrOfOper()) == true)
				{
					this.setMyPK(this.getExtType() + "_" + this.getFK_MapData());
				}
				else
				{
					this.setMyPK(this.getExtType() + "_" + this.getFK_MapData() + "_" + this.getAttrOfOper());
				}
				break;
		}
	}

	@Override
	protected boolean beforeInsert()
	{
		if (this.getMyPK().equals(""))
		{
			this.setMyPK(DBAccess.GenerGUID()); //@李国文
		}

		return super.beforeInsert();
	}

	@Override
	protected boolean beforeUpdate()
	{
		this.InitPK();

		switch (this.getExtType())
		{
			case MapExtXmlList.ActiveDDL:
			case MapExtXmlList.DDLFullCtrl:
			case MapExtXmlList.TBFullCtrl:
				if (this.getDoc().contains("@Key") == false)
				{
					throw new RuntimeException("@SQL表达式错误，您必须包含@Key ,这个关键字. ");
				}
				break;
			case MapExtXmlList.AutoFullDLL:
				if (this.getDoc().length() <= 13)
				{
					throw new RuntimeException("@必须填写SQL表达式. ");
				}
				break;
			case MapExtXmlList.AutoFull:
				if (this.getDoc().length() <= 3)
				{
					throw new RuntimeException("@必须填写表达式. 比如 @单价;*@数量; ");
				}
				break;
			case MapExtXmlList.PopVal:
				break;
			default:
				break;
		}

		return super.beforeUpdate();
	}

	@Override
	protected void afterInsertUpdateAction()
	{
		if (this.getExtType().equals("MultipleChoiceSmall") == true)
		{
			//给该字段增加一个KeyOfEnT
			String mypk = this.getFK_MapData() + "_" + this.getAttrOfOper() + "T";
			MapAttr attrH = new MapAttr();
			attrH.setMyPK(mypk);
			if (attrH.RetrieveFromDBSources() == 0)
			{
				MapAttr attr = new MapAttr(this.getFK_MapData() + "_" + this.getAttrOfOper());
				attrH.Copy(attr);
				attrH.setKeyOfEn(attr.getKeyOfEn() + "T");
				attrH.setName(attr.getName());
				attrH.setUIContralType(BP.En.UIContralType.TB);
				attrH.setMinLen(0);
				attrH.setMaxLen(500);
				attrH.setMyDataType(BP.DA.DataType.AppString);
				attrH.setUIVisible(false);
				attrH.setUIIsEnable(true);
				attrH.setMyPK(attrH.getFK_MapData() + "_" + attrH.getKeyOfEn());
				attrH.Save();
				attr.SetPara("MultipleChoiceSmall", "1");
			}
		}
		super.afterInsertUpdateAction();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 删除垃圾数据.
	*/
	public static void DeleteDB()
	{
		MapExts exts = new MapExts();
		exts.RetrieveAll();
		return;

		for (MapExt ext : exts)
		{
			if (ext.getExtType().equals(MapExtXmlList.ActiveDDL))
			{
				if (ext.getAttrOfOper().trim().length() == 0)
				{
					ext.Delete();
					continue;
				}

				MapAttr attr = new MapAttr();
				attr.setMyPK(ext.getAttrOfOper());
				if (attr.getIsExits() == true)
				{
					ext.setAttrOfOper(attr.getKeyOfEn());
					ext.Delete();

					ext.setMyPK(ext.getExtType() + "_" + ext.getFK_MapData() + "_" + ext.getAttrOfOper() + "_" + ext.getAttrsOfActive());
					ext.Save();
				}

				if (ext.getMyPK().equals(ext.getExtType() + "_" + ext.getFK_MapData() + "_" + ext.getFK_MapData() + "_" + ext.getAttrOfOper()))
				{
					ext.Delete(); //直接删除.

					ext.setMyPK(ext.getExtType() + "_" + ext.getFK_MapData() + "_" + ext.getAttrOfOper() + "_" + ext.getAttrsOfActive());
					ext.Save();
					continue;
				}

				if (ext.getMyPK().equals(ext.getExtType() + "_" + ext.getFK_MapData() + "_" + ext.getFK_MapData() + "_" + ext.getAttrOfOper() + "_" + ext.getAttrsOfActive()))
				{
					ext.Delete(); //直接删除.
					ext.setMyPK(ext.getExtType() + "_" + ext.getFK_MapData() + "_" + ext.getAttrOfOper() + "_" + ext.getAttrsOfActive());
					ext.Save();
					continue;
				}

				if (ext.getMyPK().equals(ext.getExtType() + "_" + ext.getFK_MapData() + "_" + ext.getFK_MapData() + "_" + ext.getAttrsOfActive() + "_" + ext.getAttrOfOper()))
				{
					ext.Delete(); //直接删除.
					ext.setMyPK(ext.getExtType() + "_" + ext.getFK_MapData() + "_" + ext.getAttrOfOper() + "_" + ext.getAttrsOfActive());
					ext.Save();
					continue;
				}


				//三个主键的情况.
				if (ext.getMyPK().equals(ext.getExtType() + "_" + ext.getFK_MapData() + "_" + ext.getAttrOfOper()))
				{
					ext.Delete();
					ext.setMyPK(ext.getExtType() + "_" + ext.getFK_MapData() + "_" + ext.getAttrOfOper() + "_" + ext.getAttrsOfActive());
					ext.Save();
					continue;
				}

				//三个主键的情况.
				if (ext.getMyPK().equals(ext.getExtType() + "_" + ext.getFK_MapData() + "_" + ext.getAttrsOfActive()))
				{
					ext.Delete();
					ext.setMyPK(ext.getExtType() + "_" + ext.getFK_MapData() + "_" + ext.getAttrOfOper() + "_" + ext.getAttrsOfActive());
					ext.Save();
					continue;
				}

			}
		}
	}
}