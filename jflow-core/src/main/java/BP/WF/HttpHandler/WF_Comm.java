package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Sys.*;
import BP.Sys.XML.*;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import BP.NetPlatformImpl.*;
import BP.WF.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.math.*;

/** 
 页面功能实体
*/
public class WF_Comm extends DirectoryPageBase
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 树的实体.
	/** 
	 获得树的结构
	 
	 @return 
	*/
	public final String Tree_Init()
	{
		Object tempVar = ClassFactory.GetEns(this.getEnsName());
		EntitiesTree ens = tempVar instanceof EntitiesTree ? (EntitiesTree)tempVar : null;
		if (ens == null)
		{
			return "err@该实体[" + this.getEnsName() + "]不是一个树形实体.";
		}

		ens.RetrieveAll(EntityTreeAttr.Idx);
		//return ens.ToJsonOfTree();
		return BP.Tools.Json.ToJson(ens.ToDataTableField("TreeTable"));
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 树的实体

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 部门-人员关系.

	public final String Tree_MapBaseInfo()
	{
		Object tempVar = ClassFactory.GetEns(this.getTreeEnsName());
		EntitiesTree enTrees = tempVar instanceof EntitiesTree ? (EntitiesTree)tempVar : null;
		EntityTree enenTree = enTrees.GetNewEntity instanceof EntityTree ? (EntityTree)enTrees.GetNewEntity : null;
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		Entity en = ens.GetNewEntity;
		Hashtable ht = new Hashtable();
		ht.put("TreeEnsDesc", enenTree.EnDesc);
		ht.put("EnsDesc", en.EnDesc);
		ht.put("EnPK", en.PK);
		return BP.Tools.Json.ToJson(ht);
	}

	/** 
	 获得树的结构
	 
	 @return 
	*/
	public final String TreeEn_Init()
	{
		Object tempVar = ClassFactory.GetEns(this.getTreeEnsName());
		EntitiesTree ens = tempVar instanceof EntitiesTree ? (EntitiesTree)tempVar : null;
		ens.RetrieveAll(EntityTreeAttr.Idx);
		return ens.ToJsonOfTree();
	}

	/** 
	 获取树关联的集合
	 
	 @return 
	*/
	public final String TreeEmp_Init()
	{
		DataSet ds = new DataSet();
		String RefPK = this.GetRequestVal("RefPK");
		String FK = this.GetRequestVal("FK");
		//获取关联的信息集合
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		ens.RetrieveByAttr(RefPK, FK);
		DataTable dt = ens.ToDataTableField("GridData");
		ds.Tables.Add(dt);

		//获取实体对应的列明
		Entity en = ens.GetNewEntity;
		Map map = en.EnMapInTime;
		MapAttrs attrs = map.Attrs.ToMapAttrs;
		//属性集合.
		DataTable dtAttrs = attrs.ToDataTableField();
		dtAttrs.TableName = "Sys_MapAttrs";

		dt = new DataTable("Sys_MapAttr");
		dt.Columns.Add("field", String.class);
		dt.Columns.Add("title", String.class);
		dt.Columns.Add("Width", Integer.class);
		dt.Columns.Add("UIContralType", Integer.class);
		DataRow row = null;
		for (MapAttr attr : attrs)
		{
			if (attr.UIVisible == false)
			{
				continue;
			}

			if (this.getRefPK().equals(attr.KeyOfEn))
			{
				continue;
			}

			row = dt.NewRow();
			row.set("field", attr.KeyOfEn);
			row.set("title", attr.Name);
			row.set("Width", attr.UIWidthInt * 2);
			row.set("UIContralType", attr.UIContralType);

			if (attr.HisAttr.IsFKorEnum)
			{
				row.set("field", attr.KeyOfEn + "Text");
			}
			dt.Rows.Add(row);
		}

		ds.Tables.Add(dt);

		return BP.Tools.Json.ToJson(ds);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 部门-人员关系
	/** 
	 构造函数
	*/
	public WF_Comm()
	{
	}

	/** 
	 部门编号
	*/
	public final String getFK_Dept()
	{
		String str = this.GetRequestVal("FK_Dept");
		if (str == null || str.equals("") || str.equals("null"))
		{
			return null;
		}
		return str;
	}
	public final void setFK_Dept(String value)
	{
		String val = value;
		if (val.equals("all"))
		{
			return;
		}

		if (this.getFK_Dept() == null)
		{
			this.setFK_Dept(value);
			return;
		}
	}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 统计分析组件.
	/** 
	 初始化数据
	 
	 @return 
	*/
	public final String ContrastDtl_Init()
	{
		//获得.
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		Entity en = ens.GetNewEntity;
		Map map = en.EnMapInTime;

		MapAttrs attrs = map.Attrs.ToMapAttrs;

		//属性集合.
		DataTable dtAttrs = attrs.ToDataTableField();
		dtAttrs.TableName = "Sys_MapAttrs";

		DataSet ds = new DataSet();
		ds.Tables.Add(dtAttrs); //把描述加入.

		//查询结果
		QueryObject qo = new QueryObject(ens);

		String[] strs = HttpContextHelper.Request.Form.toString().split("[&]", -1);
		for (String str : strs)
		{
			if (str.indexOf("EnsName") != -1)
			{
				continue;
			}

			String[] mykey = str.split("[=]", -1);
			String key = mykey[0];


			if (key.equals("OID") || key.equals("MyPK"))
			{
				continue;
			}

			if (key.equals("FK_Dept"))
			{
				this.setFK_Dept(mykey[1]);
				continue;
			}
			boolean isExist = false;
			boolean IsInt = false;
			boolean IsDouble = false;
			boolean IsFloat = false;
			boolean IsMoney = false;
			for (Attr attr : en.EnMap.Attrs)
			{
				if (attr.Key.equals(key))
				{
					isExist = true;
					if (attr.MyDataType == DataType.AppInt)
					{
						IsInt = true;
					}
					if (attr.MyDataType == DataType.AppDouble)
					{
						IsDouble = true;
					}
					if (attr.MyDataType == DataType.AppFloat)
					{
						IsFloat = true;
					}
					if (attr.MyDataType == DataType.AppMoney)
					{
						IsMoney = true;
					}
					break;
				}
			}

			if (isExist == false)
			{
				continue;
			}

			if (mykey[1].equals("mvals"))
			{
				//如果用户多项选择了，就要找到它的选择项目.

				UserRegedit sUr = new UserRegedit();
				sUr.MyPK = WebUser.No + this.getEnsName() + "_SearchAttrs";
				sUr.RetrieveFromDBSources();

				/* 如果是多选值 */
				String cfgVal = sUr.MVals;
				AtPara ap = new AtPara(cfgVal);
				String instr = ap.GetValStrByKey(key);
				String val = "";
				if (instr == null || instr.equals(""))
				{
					if (key.equals("FK_Dept") || key.equals("FK_Unit"))
					{
						if (key.equals("FK_Dept"))
						{
							val = WebUser.FK_Dept;
						}
					}
					else
					{
						continue;
					}
				}
				else
				{
					instr = instr.replace("..", ".");
					instr = instr.replace(".", "','");
					instr = instr.substring(2);
					instr = instr.substring(0, instr.length() - 2);
					qo.AddWhereIn(mykey[0], instr);
				}
			}
			else
			{
				if (IsInt == true && DataType.IsNullOrEmpty(mykey[1]) == false)
				{
					qo.AddWhere(mykey[0], Integer.parseInt(mykey[1]));
				}
				else if (IsDouble == true && DataType.IsNullOrEmpty(mykey[1]) == false)
				{
					qo.AddWhere(mykey[0], Double.parseDouble(mykey[1]));
				}
				else if (IsFloat == true && DataType.IsNullOrEmpty(mykey[1]) == false)
				{
					qo.AddWhere(mykey[0], Float.parseFloat(mykey[1]));
				}
				else if (IsMoney == true && DataType.IsNullOrEmpty(mykey[1]) == false)
				{
					qo.AddWhere(mykey[0], BigDecimal.Parse(mykey[1]));
				}
				else
				{
					qo.AddWhere(mykey[0], mykey[1]);
				}
			}
			qo.addAnd();
		}

		if (this.getFK_Dept() != null && (this.GetRequestVal("FK_Emp") == null || this.GetRequestVal("FK_Emp").equals("all")))
		{
			if (this.getFK_Dept().length() == 2)
			{
				qo.AddWhere("FK_Dept", " = ", "all");
				qo.addAnd();
			}
			else
			{
				if (this.getFK_Dept().length() == 8)
				{
					qo.AddWhere("FK_Dept", " = ", this.getFK_Dept());
				}
				else
				{
					qo.AddWhere("FK_Dept", " like ", this.getFK_Dept() + "%");
				}

				qo.addAnd();
			}
		}

		qo.AddHD();
		qo.DoQuery();
		DataTable dt = ens.ToDataTableField();
		dt.TableName = "Group_Dtls";
		ds.Tables.Add(dt);

		return BP.Tools.Json.ToJson(ds);
	}

	/** 
	 执行导出
	 
	 @return 
	*/
	public final String GroupDtl_Exp()
	{
		//获得.
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		Entity en = ens.GetNewEntity;

		//查询结果
		QueryObject qo = new QueryObject(ens);
		String[] strs = HttpContextHelper.Request.Form.toString().split("[&]", -1);
		for (String str : strs)
		{
			if (str.indexOf("EnsName") != -1)
			{
				continue;
			}

			String[] mykey = str.split("[=]", -1);
			String key = mykey[0];

			if (key.equals("OID") || key.equals("MyPK"))
			{
				continue;
			}

			if (key.equals("FK_Dept"))
			{
				this.setFK_Dept(mykey[1]);
				continue;
			}

			boolean isExist = false;
			boolean IsInt = false;
			boolean IsDouble = false;
			boolean IsFloat = false;
			boolean IsMoney = false;
			for (Attr attr : en.EnMap.Attrs)
			{
				if (attr.Key.equals(key))
				{
					isExist = true;
					if (attr.MyDataType == DataType.AppInt)
					{
						IsInt = true;
					}
					if (attr.MyDataType == DataType.AppDouble)
					{
						IsDouble = true;
					}
					if (attr.MyDataType == DataType.AppFloat)
					{
						IsFloat = true;
					}
					if (attr.MyDataType == DataType.AppMoney)
					{
						IsMoney = true;
					}
					break;
				}
			}

			if (isExist == false)
			{
				continue;
			}

			if (mykey[1].equals("mvals"))
			{
				//如果用户多项选择了，就要找到它的选择项目.

				UserRegedit sUr = new UserRegedit();
				sUr.MyPK = WebUser.No + this.getEnsName() + "_SearchAttrs";
				sUr.RetrieveFromDBSources();

				/* 如果是多选值 */
				String cfgVal = sUr.MVals;
				AtPara ap = new AtPara(cfgVal);
				String instr = ap.GetValStrByKey(key);
				String val = "";
				if (instr == null || instr.equals(""))
				{
					if (key.equals("FK_Dept") || key.equals("FK_Unit"))
					{
						if (key.equals("FK_Dept"))
						{
							val = WebUser.FK_Dept;
						}
					}
					else
					{
						continue;
					}
				}
				else
				{
					instr = instr.replace("..", ".");
					instr = instr.replace(".", "','");
					instr = instr.substring(2);
					instr = instr.substring(0, instr.length() - 2);
					qo.AddWhereIn(mykey[0], instr);
				}
			}
			else
			{
				if (IsInt == true && DataType.IsNullOrEmpty(mykey[1]) == false)
				{
					qo.AddWhere(mykey[0], Integer.parseInt(mykey[1]));
				}
				else if (IsDouble == true && DataType.IsNullOrEmpty(mykey[1]) == false)
				{
					qo.AddWhere(mykey[0], Double.parseDouble(mykey[1]));
				}
				else if (IsFloat == true && DataType.IsNullOrEmpty(mykey[1]) == false)
				{
					qo.AddWhere(mykey[0], Float.parseFloat(mykey[1]));
				}
				else if (IsMoney == true && DataType.IsNullOrEmpty(mykey[1]) == false)
				{
					qo.AddWhere(mykey[0], BigDecimal.Parse(mykey[1]));
				}
				else
				{
					qo.AddWhere(mykey[0], mykey[1]);
				}
			}
			qo.addAnd();
		}

		if (this.getFK_Dept() != null && (this.GetRequestVal("FK_Emp") == null || this.GetRequestVal("FK_Emp").equals("all")))
		{
			if (this.getFK_Dept().length() == 2)
			{
				qo.AddWhere("FK_Dept", " = ", "all");
				qo.addAnd();
			}
			else
			{
				if (this.getFK_Dept().length() == 8)
				{
					qo.AddWhere("FK_Dept", " = ", this.getFK_Dept());
				}
				else
				{
					qo.AddWhere("FK_Dept", " like ", this.getFK_Dept() + "%");
				}

				qo.addAnd();
			}
		}

		qo.AddHD();

		DataTable dt = qo.DoQueryToTable();

		String filePath = ExportDGToExcel(dt, en, en.EnDesc);


		return filePath;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 统计分析组件.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Entity 公共类库.
	/** 
	 实体类名
	*/
	public final String getEnName()
	{
		return this.GetRequestVal("EnName");
	}
	/** 
	 获得实体
	 
	 @return 
	*/
	public final String Entity_Init()
	{
		try
		{
			String pkval = this.getPKVal();
			Entity en = ClassFactory.GetEn(this.getEnName());

			if (pkval.equals("0") || pkval.equals("") || pkval == null || pkval.equals("undefined"))
			{
				Map map = en.EnMap;
				for (Attr attr : en.EnMap.Attrs)
				{
					en.SetValByKey(attr.Key, attr.DefaultVal);
				}
				//设置默认的数据.
				en.ResetDefaultVal();
			}
			else
			{
				en.PKVal = pkval;
				en.Retrieve();

				//int i=en.RetrieveFromDBSources();
				//if (i == 0)
				//  return "err@实体:["+"]";
			}

			return en.ToJson(false);
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 删除
	 
	 @return 
	*/
	public final String Entity_Delete()
	{
		try
		{
			Entity en = ClassFactory.GetEn(this.getEnName());

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 首先判断参数删除.
			String key1 = this.GetRequestVal("Key1");
			String val1 = this.GetRequestVal("Val1");

			String key2 = this.GetRequestVal("Key2");
			String val2 = this.GetRequestVal("Val2");

			if (DataType.IsNullOrEmpty(key1) == false && key1.equals("undefined") == false)
			{
				int num = 0;
				if (DataType.IsNullOrEmpty(key2) == false && key2.equals("undefined") == false)
				{
					num = en.Delete(key1, val1, key2, val2);
				}
				else
				{
					num = en.Delete(key1, val1);
				}
				return String.valueOf(num);
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 首先判断参数删除.

			if (en.PKCount != 1)
			{
				/*多个主键的情况. 遍历属性，循环赋值.*/
				for (Attr attr : en.EnMap.Attrs)
				{
					en.SetValByKey(attr.Key, this.GetRequestVal(attr.Key));
				}
			}
			else
			{
				en.PKVal = this.getPKVal();
			}

			int i = en.RetrieveFromDBSources(); //查询出来再删除.
			return en.Delete().toString(); //返回影响行数.
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 更新
	 
	 @return 
	*/
	public final String Entity_Update()
	{
		try
		{
			Entity en = ClassFactory.GetEn(this.getEnName());
			en.PKVal = this.getPKVal();
			en.RetrieveFromDBSources();

			//遍历属性，循环赋值.
			for (Attr attr : en.EnMap.Attrs)
			{
				en.SetValByKey(attr.Key, this.GetRequestVal(attr.Key));
			}

			//返回数据.
			//return en.ToJson(false);

			return en.Update().toString(); //返回影响行数.
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 从数据源查询.
	 
	 @return 
	*/
	public final String Entity_RetrieveFromDBSources()
	{
		try
		{
			Entity en = ClassFactory.GetEn(this.getEnName());
			en.PKVal = this.getPKVal();
			int i = en.RetrieveFromDBSources();

			if (i == 0)
			{
				en.ResetDefaultVal();
				en.PKVal = this.getPKVal();
			}

			if (en.Row.ContainsKey("RetrieveFromDBSources") == true)
			{
				en.Row["RetrieveFromDBSources"] = i;
			}
			else
			{
				en.Row.Add("RetrieveFromDBSources", i);
			}

			return en.ToJson(false);
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 从数据源查询.
	 
	 @return 
	*/
	public final String Entity_Retrieve()
	{
		try
		{
			Entity en = ClassFactory.GetEn(this.getEnName());
			en = en.CreateInstance();
			en.PKVal = this.getPKVal();
			en.Retrieve();

			if (en.Row.ContainsKey("Retrieve") == true)
			{
				en.Row["Retrieve"] = "1";
			}
			else
			{
				en.Row.Add("Retrieve", "1");
			}

			return en.ToJson(false);
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 是否存在
	 
	 @return 
	*/
	public final String Entity_IsExits()
	{
		try
		{
			Entity en = ClassFactory.GetEn(this.getEnName());
			en.PKVal = this.getPKVal();
			boolean isExit = en.IsExits;
			if (isExit == true)
			{
				return "1";
			}
			return "0";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 执行保存
	 
	 @return 返回保存影响的行数
	*/
	public final String Entity_Save()
	{
		try
		{
			Entity en = ClassFactory.GetEn(this.getEnName());
			if (en == null)
			{
				return "err@实体类名错误[" + this.getEnName() + "].";
			}

			en.PKVal = this.getPKVal();
			en.RetrieveFromDBSources();

			//遍历属性，循环赋值.
			for (Attr attr : en.EnMap.Attrs)
			{
				en.SetValByKey(attr.Key, this.GetValFromFrmByKey(attr.Key));
			}

			//保存参数属性.
			String frmParas = this.GetValFromFrmByKey("frmParas", "");
			if (DataType.IsNullOrEmpty(frmParas) == false)
			{
				AtPara ap = new AtPara(frmParas);
				for (String key : ap.HisHT.keySet())
				{
					en.SetPara(key, ap.GetValStrByKey(key));
				}
			}

			return en.Save().toString();
		}
		catch (RuntimeException ex)
		{
			return "err@保存错误:" + ex.getMessage();
		}
	}
	/** 
	 执行插入.
	 
	 @return 
	*/
	public final String Entity_Insert()
	{
		try
		{
			Entity en = ClassFactory.GetEn(this.getEnName());

			//遍历属性，循环赋值.
			for (Attr attr : en.EnMap.Attrs)
			{
				en.SetValByKey(attr.Key, this.GetRequestVal(attr.Key));
			}

			//插入数据库.
			int i = en.Insert();
			if (i == 1)
			{
				en.Retrieve(); //执行查询.
			}

			//返回数据.
			return en.ToJson(false);
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}

	/** 
	 执行插入.
	 
	 @return 
	*/
	public final String Entity_DirectInsert()
	{
		try
		{
			Entity en = ClassFactory.GetEn(this.getEnName());

			//遍历属性，循环赋值.
			for (Attr attr : en.EnMap.Attrs)
			{
				en.SetValByKey(attr.Key, this.GetRequestVal(attr.Key));
			}

			//插入数据库.
			int i = en.DirectInsert();
			if (i == 1)
			{
				en.Retrieve(); //执行查询.
			}

			//返回数据.
			return en.ToJson(false);
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 查询
	 
	 @return 
	*/
	public final String Entity_DoMethodReturnString()
	{
		//创建类实体.
		BP.En.Entity en = ClassFactory.GetEn(this.getEnName()); // Activator.CreateInstance(System.Type.GetType("BP.En.Entity")) as BP.En.Entity;
		en.PKVal = this.getPKVal();
		en.RetrieveFromDBSources();

		String methodName = this.GetRequestVal("MethodName");

		java.lang.Class tp = en.getClass();
		java.lang.reflect.Method mp = tp.getMethod(methodName);
		if (mp == null)
		{
			return "err@没有找到类[" + this.getEnName() + "]方法[" + methodName + "].";
		}

		String paras = this.GetRequestVal("paras");

		//执行该方法.
		Object[] myparas = new Object[0];

		if (DataType.IsNullOrEmpty(paras) == false)
		{
			myparas = paras.split("[~]", -1);
		}

		Object tempVar = mp.Invoke(en, myparas);
		String result = tempVar instanceof String ? (String)tempVar : null; //调用由此 MethodInfo 实例反射的方法或构造函数。
		return result;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Entities 公共类库.
	/** 
	 调用参数.
	*/
	public final String getParas()
	{
		return this.GetRequestVal("Paras");
	}
	/** 
	 查询全部
	 
	 @return 
	*/
	public final String Entities_RetrieveAll()
	{
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		ens.RetrieveAll();
		return ens.ToJson();
	}
	/** 
	 获得实体集合s
	 
	 @return 
	*/
	public final String Entities_Init()
	{
		try
		{
			Entities ens = ClassFactory.GetEns(this.getEnsName());
			if (this.getParas() == null)
			{
				return "0";
			}

			Entity en = ens.GetNewEntity;

			QueryObject qo = new QueryObject(ens);
			String[] myparas = this.getParas().split("[@]", -1);

			int idx = 0;
			for (int i = 0; i < myparas.length; i++)
			{
				String para = myparas[i];
				if (DataType.IsNullOrEmpty(para) || para.contains("=") == false)
				{
					continue;
				}

				String[] strs = para.split("[=]", -1);
				String key = strs[0];
				String val = strs[1];


				if (key.toLowerCase().equals("orderby") == true)
				{
					//多重排序
					if (val.indexOf(",") != -1)
					{
						String[] strs1 = val.split("[,]", -1);
						for (String str : strs1)
						{
							if (DataType.IsNullOrEmpty(str) == true)
							{
								continue;
							}
							if (str.toUpperCase().indexOf("DESC") != -1)
							{
								String str1 = str.replace("DESC", "").replace("desc", "");
								qo.addOrderByDesc(str1.trim());
							}
							else
							{
								if (str.toUpperCase().indexOf("ASC") != -1)
								{
									String str1 = str.replace("ASC", "").replace("asc", "");
									qo.addOrderBy(str1.trim());
								}
								else
								{
									qo.addOrderBy(str.trim());
								}
							}

						}
					}
					else
					{
						qo.addOrderBy(val);
					}

					continue;
				}

				Object valObj = val;

				if (SystemConfig.AppCenterDBType == DBType.PostgreSQL)
				{
					valObj = BP.Sys.Glo.GenerRealType(en.EnMap.Attrs, key, val);
				}

				if (idx == 0)
				{
					qo.AddWhere(key, valObj);
				}
				else
				{
					qo.addAnd();
					qo.AddWhere(key, valObj);
				}
				idx++;
			}

			qo.DoQuery();
			return ens.ToJson();
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}

	/** 
	 获得实体集合s
	 
	 @return 
	*/
	public final String Entities_RetrieveCond()
	{
		try
		{
			Entities ens = ClassFactory.GetEns(this.getEnsName());
			if (this.getParas() == null)
			{
				return "0";
			}

			QueryObject qo = new QueryObject(ens);
			String[] myparas = this.getParas().split("[@]", -1);

			Attrs attrs = ens.GetNewEntity.EnMap.Attrs;

			int idx = 0;
			for (int i = 0; i < myparas.length; i++)
			{
				String para = myparas[i];
				if (DataType.IsNullOrEmpty(para))
				{
					continue;
				}

				String[] strs = para.split("[|]", -1);
				String key = strs[0];
				String oper = strs[1];
				String val = strs[2];

				if (key.toLowerCase().equals("orderby") == true)
				{
					qo.addOrderBy(val);
					continue;
				}

				//获得真实的数据类型.
				Object typeVal = val;
				if (SystemConfig.AppCenterDBType == DBType.PostgreSQL)
				{
					typeVal = BP.Sys.Glo.GenerRealType(attrs, key, val);
				}

				if (idx == 0)
				{
					qo.AddWhere(key, oper, typeVal);
				}
				else
				{
					qo.addAnd();
					qo.AddWhere(key, oper, typeVal);
				}
				idx++;
			}

			qo.DoQuery();
			return ens.ToJson();
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 执行方法
	 
	 @return 
	*/
	public final String Entities_DoMethodReturnString()
	{
		//创建类实体.
		BP.En.Entities ens = ClassFactory.GetEns(this.getEnsName()); // Activator.CreateInstance(System.Type.GetType("BP.En.Entity")) as BP.En.Entity;

		String methodName = this.GetRequestVal("MethodName");

		java.lang.Class tp = ens.getClass();
		java.lang.reflect.Method mp = tp.getMethod(methodName);
		if (mp == null)
		{
			return "err@没有找到类[" + this.getEnsName() + "]方法[" + methodName + "].";
		}

		String paras = this.GetRequestVal("paras");
		if ("un".equals(paras) == true)
		{
			paras = "";
		}

		//执行该方法.
		Object[] myparas = new Object[0];

		if (DataType.IsNullOrEmpty(paras) == false)
		{
			myparas = paras.split("[~]", -1);
		}

		Object tempVar = mp.Invoke(ens, myparas);
		String result = tempVar instanceof String ? (String)tempVar : null; //调用由此 MethodInfo 实例反射的方法或构造函数。
		return result;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 功能执行.
	/** 
	 初始化.
	 
	 @return 
	*/
	public final String Method_Init()
	{
		String ensName = this.GetRequestVal("M");
		Method rm = BP.En.ClassFactory.GetMethod(ensName);
		if (rm == null)
		{
			return "err@方法名错误或者该方法已经不存在" + ensName;
		}

		if (rm.HisAttrs.size() == 0)
		{
			Hashtable ht = new Hashtable();
			ht.put("No", ensName);
			ht.put("Title", rm.Title);
			ht.put("Help", rm.Help);
			ht.put("Warning", rm.Warning);
			return BP.Tools.Json.ToJson(ht);
		}

		DataTable dt = new DataTable();

		//转化为集合.
		MapAttrs attrs = rm.HisAttrs.ToMapAttrs;

		return "";
	}
	public final String Method_Done()
	{
		String ensName = this.GetRequestVal("M");
		Method rm = BP.En.ClassFactory.GetMethod(ensName);
		// rm.Init();
		int mynum = 0;
		for (Attr attr : rm.HisAttrs)
		{
			if (attr.MyFieldType == FieldType.RefText)
			{
				continue;
			}
			mynum++;
		}
		int idx = 0;
		for (Attr attr : rm.HisAttrs)
		{
			if (attr.MyFieldType == FieldType.RefText)
			{
				continue;
			}
			if (attr.UIVisible == false)
			{
				continue;
			}
			try
			{
				switch (attr.UIContralType)
				{
					case UIContralType.TB:
						switch (attr.MyDataType)
						{
							case BP.DA.DataType.AppString:
							case BP.DA.DataType.AppDate:
							case BP.DA.DataType.AppDateTime:
								String str1 = this.GetValFromFrmByKey(attr.Key);
								rm.SetValByKey(attr.Key, str1);
								break;
							case BP.DA.DataType.AppInt:
								int myInt = this.GetValIntFromFrmByKey(attr.Key); //int.Parse(this.UCEn1.GetTBByID("TB_" + attr.Key).Text);
								rm.Row[idx] = myInt;
								rm.SetValByKey(attr.Key, myInt);
								break;
							case BP.DA.DataType.AppFloat:
								float myFloat = this.GetValFloatFromFrmByKey(attr.Key); // float.Parse(this.UCEn1.GetTBByID("TB_" + attr.Key).Text);
								rm.SetValByKey(attr.Key, myFloat);
								break;
							case BP.DA.DataType.AppDouble:
							case BP.DA.DataType.AppMoney:
								BigDecimal myDoub = this.GetValDecimalFromFrmByKey(attr.Key); // decimal.Parse(this.UCEn1.GetTBByID("TB_" + attr.Key).Text);
								rm.SetValByKey(attr.Key, myDoub);
								break;
							case BP.DA.DataType.AppBoolean:
								boolean myBool = this.GetValBoolenFromFrmByKey(attr.Key); // decimal.Parse(this.UCEn1.GetTBByID("TB_" + attr.Key).Text);
								rm.SetValByKey(attr.Key, myBool);
								break;
							default:
								return "err@没有判断的数据类型．";
						}
						break;
					case UIContralType.DDL:
						try
						{
							String str = this.GetValFromFrmByKey(attr.Key); // decimal.Parse(this.UCEn1.GetTBByID("TB_" + attr.Key).Text);
							// string str = this.UCEn1.GetDDLByKey("DDL_" + attr.Key).SelectedItemStringVal;
							rm.SetValByKey(attr.Key, str);
						}
						catch (java.lang.Exception e)
						{
							rm.SetValByKey(attr.Key, "");
						}
						break;
					case UIContralType.CheckBok:
						boolean myBoolval = this.GetValBoolenFromFrmByKey(attr.Key); // decimal.Parse(this.UCEn1.GetTBByID("TB_" + attr.Key).Text);
						rm.SetValByKey(attr.Key, myBoolval);
						break;
					default:
						break;
				}
				idx++;
			}
			catch (RuntimeException ex)
			{
				return "err@获得参数错误" + "attr=" + attr.Key + " attr = " + attr.Key + ex.getMessage();
			}
		}

		try
		{
			Object obj = rm.Do();
			if (obj != null)
			{
				return obj.toString();
			}
			else
			{
				return "err@执行完成没有返回信息.";
			}
		}
		catch (RuntimeException ex)
		{
			return "err@执行错误:" + ex.getMessage();
		}
	}
	public final String MethodLink_Init()
	{
		ArrayList al = BP.En.ClassFactory.GetObjects("BP.En.Method");
		int i = 1;
		String html = "";

		//DataTable dt = new DataTable();
		//dt.Columns.Add("No", typeof(string));
		//dt.Columns.Add("Name", typeof(string));
		//dt.Columns.Add("Icon", typeof(string));
		//dt.Columns.Add("Note", typeof(string));

		for (BP.En.Method en : al)
		{
			if (en.IsCanDo == false || en.IsVisable == false)
			{
				continue;
			}

			//DataRow dr = dt.NewRow();

			html += "<li><a href=\"javascript:ShowIt('" + en.toString() + "');\"  >" + en.GetIcon("/") + en.Title + "</a><br><font size=2 color=Green>" + en.Help + "</font><br><br></li>";
		}

		return html;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 查询.
	/** 
	 获得查询的基本信息.
	 
	 @return 
	*/
	public final String Search_MapBaseInfo()
	{
		//获得
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		if (ens == null)
		{
			return "err@类名:" + this.getEnsName() + "错误";
		}

		Entity en = ens.GetNewEntity;
		Map map = ens.GetNewEntity.EnMapInTime;

		Hashtable ht = new Hashtable();

		//把权限信息放入.
		UAC uac = en.HisUAC;
		ht.put("IsUpdata", uac.IsUpdate);
		ht.put("IsInsert", uac.IsInsert);
		ht.put("IsDelete", uac.IsDelete);
		ht.put("IsView", uac.IsView);
		ht.put("IsExp", uac.IsExp); //是否可以导出?
		ht.put("IsImp", uac.IsImp); //是否可以导入?

		ht.put("EnDesc", en.EnDesc); //描述?
		ht.put("EnName", en.toString()); //类名?


		//把map信息放入
		ht.put("PhysicsTable", map.PhysicsTable);
		ht.put("CodeStruct", map.CodeStruct);
		ht.put("CodeLength", map.CodeLength);

		//查询条件.
		if (map.IsShowSearchKey == true)
		{
			ht.put("IsShowSearchKey", 1);
		}
		else
		{
			ht.put("IsShowSearchKey", 0);
		}

		//按日期查询.
		ht.put("DTSearchWay", (int)map.DTSearchWay);
		ht.put("DTSearchLable", map.DTSearchLable);
		ht.put("DTSearchKey", map.DTSearchKey);

		return BP.Tools.Json.ToJson(ht);
	}
	/** 
	 外键或者枚举的查询.
	 
	 @return 
	*/
	public final String Search_SearchAttrs()
	{
		//获得
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		if (ens == null)
		{
			return "err@类名错误:" + this.getEnsName();
		}

		Entity en = ens.GetNewEntity;
		Map map = ens.GetNewEntity.EnMapInTime;

		DataSet ds = new DataSet();

		//构造查询条件集合.
		DataTable dt = new DataTable();
		dt.Columns.Add("Field");
		dt.Columns.Add("Name");
		dt.Columns.Add("Width");
		dt.Columns.Add("UIContralType");
		dt.TableName = "Attrs";

		AttrSearchs attrs = map.SearchAttrs;
		for (AttrSearch item : attrs)
		{
			DataRow dr = dt.NewRow();
			dr.set("Field", item.Key);
			dr.set("Name", item.HisAttr.Desc);
			dr.set("Width", item.Width); //下拉框显示的宽度.
			dr.set("UIContralType", item.HisAttr.UIContralType);

			dt.Rows.Add(dr);
		}
		ds.Tables.Add(dt);

		//把外键枚举增加到里面.
		for (AttrSearch item : attrs)
		{
			if (item.HisAttr.IsEnum == true)
			{
				SysEnums ses = new SysEnums(item.HisAttr.UIBindKey);
				DataTable dtEnum = ses.ToDataTableField();
				dtEnum.TableName = item.Key;
				ds.Tables.Add(dtEnum);
				continue;
			}

			if (item.HisAttr.IsFK == true)
			{
				Entities ensFK = item.HisAttr.HisFKEns;
				ensFK.RetrieveAll();

				DataTable dtEn = ensFK.ToDataTableField();
				dtEn.TableName = item.Key;
				ds.Tables.Add(dtEn);
			}
			//绑定SQL的外键
			if (item.HisAttr.UIDDLShowType == BP.Web.Controls.DDLShowType.BindSQL)
			{
				//获取SQl
				String sql = item.HisAttr.UIBindKey;
				sql = BP.WF.Glo.DealExp(sql, null, null);
				DataTable dtSQl = DBAccess.RunSQLReturnTable(sql);
				for (DataColumn col : dtSQl.Columns)
				{
					String colName = col.ColumnName.toLowerCase();
					switch (colName)
					{
						case "no":
							col.ColumnName = "No";
							break;
						case "name":
							col.ColumnName = "Name";
							break;
						case "parentno":
							col.ColumnName = "ParentNo";
							break;
						default:
							break;
					}
				}
				dtSQl.TableName = item.Key;
				if (ds.Tables.Contains(item.Key) == false)
				{
					ds.Tables.Add(dtSQl);
				}
			}

		}

		return BP.Tools.Json.ToJson(ds);
	}
	/** 
	 执行查询 - 初始化查找数据
	 
	 @return 
	*/
	public final String Search_SearchIt()
	{
		return BP.Tools.Json.ToJson(Search_Search());
	}
	/** 
	 执行查询.这个方法也会被导出调用.
	 
	 @return 
	*/
	public final DataSet Search_Search()
	{
		//获得.
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		Entity en = ens.GetNewEntity;
		Map map = en.EnMapInTime;

		MapAttrs attrs = new MapAttrs();
		;
		//DataTable dtAttrs = attrs.ToDataTableField();
		//dtAttrs.TableName = "Attrs";


		MapData md = new MapData();
		md.No = this.getEnsName();
		int count = md.RetrieveFromDBSources();
		if (count == 0)
		{
			attrs = map.Attrs.ToMapAttrs;
		}
		else
		{
			attrs.Retrieve(MapAttrAttr.FK_MapData, this.getEnsName(), MapAttrAttr.Idx);
		}

		//根据设置的显示列显示字段
		DataRow row = null;
		DataTable dtAttrs = new DataTable("Attrs");
		dtAttrs.Columns.Add("KeyOfEn", String.class);
		dtAttrs.Columns.Add("Name", String.class);
		dtAttrs.Columns.Add("Width", Integer.class);
		dtAttrs.Columns.Add("UIContralType", Integer.class);
		for (MapAttr attr : attrs)
		{
			String searchVisable = attr.atPara.GetValStrByKey("SearchVisable");
			if (searchVisable.equals("0"))
			{
				continue;
			}
			if ((count != 0 && DataType.IsNullOrEmpty(searchVisable)) || attr.UIVisible == false)
			{
				continue;
			}
			row = dtAttrs.NewRow();
			row.set("KeyOfEn", attr.KeyOfEn);
			row.set("Name", attr.Name);
			row.set("Width", attr.UIWidthInt);
			row.set("UIContralType", attr.UIContralType);

			dtAttrs.Rows.Add(row);
		}

		DataSet ds = new DataSet();
		ds.Tables.Add(dtAttrs); //把描述加入.

		md.Name = map.EnDesc;

		//附件类型.
		md.SetPara("BPEntityAthType", (int)map.HisBPEntityAthType);

		//获取实体类的主键
		md.SetPara("PK", en.PK);

		ds.Tables.Add(md.ToDataTableField("Sys_MapData"));

		//取出来查询条件.
		BP.Sys.UserRegedit ur = new UserRegedit();
		ur.MyPK = WebUser.No + "_" + this.getEnsName() + "_SearchAttrs";
		ur.RetrieveFromDBSources();

		//获得关键字.
		AtPara ap = new AtPara(ur.Vals);

		//关键字.
		String keyWord = ur.SearchKey;
		QueryObject qo = new QueryObject(ens);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 关键字字段.
		if (en.EnMap.IsShowSearchKey && DataType.IsNullOrEmpty(keyWord) == false && keyWord.length() >= 1)
		{
			Attr attrPK = new Attr();
			for (Attr attr : map.Attrs)
			{
				if (attr.IsPK)
				{
					attrPK = attr;
					break;
				}
			}
			int i = 0;
			String enumKey = ","; //求出枚举值外键.
			for (Attr attr : map.Attrs)
			{
				switch (attr.MyFieldType)
				{
					case FieldType.Enum:
						enumKey = "," + attr.Key + "Text,";
						break;
					case FieldType.FK:
						// case FieldType.PKFK:
						continue;
					default:
						break;
				}

				if (attr.MyDataType != DataType.AppString)
				{
					continue;
				}

				//排除枚举值关联refText.
				if (attr.MyFieldType == FieldType.RefText)
				{
					if (enumKey.contains("," + attr.Key + ",") == true)
					{
						continue;
					}
				}

				if (attr.Key.equals("FK_Dept"))
				{
					continue;
				}

				i++;
				if (i == 1)
				{
					/* 第一次进来。 */
					qo.addLeftBracket();
					if (SystemConfig.AppCenterDBVarStr.equals("@") || SystemConfig.AppCenterDBVarStr.equals("?"))
					{
						qo.AddWhere(attr.Key, " LIKE ", SystemConfig.AppCenterDBType == DBType.MySQL ? (" CONCAT('%'," + SystemConfig.AppCenterDBVarStr + "SKey,'%')") : (" '%'+" + SystemConfig.AppCenterDBVarStr + "SKey+'%'"));
					}
					else
					{
						qo.AddWhere(attr.Key, " LIKE ", " '%'||" + SystemConfig.AppCenterDBVarStr + "SKey||'%'");
					}
					continue;
				}
				qo.addOr();

				if (SystemConfig.AppCenterDBVarStr.equals("@") || SystemConfig.AppCenterDBVarStr.equals("?"))
				{
					qo.AddWhere(attr.Key, " LIKE ", SystemConfig.AppCenterDBType == DBType.MySQL ? ("CONCAT('%'," + SystemConfig.AppCenterDBVarStr + "SKey,'%')") : ("'%'+" + SystemConfig.AppCenterDBVarStr + "SKey+'%'"));
				}
				else
				{
					qo.AddWhere(attr.Key, " LIKE ", "'%'||" + SystemConfig.AppCenterDBVarStr + "SKey||'%'");
				}

			}
			qo.MyParas.Add("SKey", keyWord);
			qo.addRightBracket();

		}
		else
		{
			qo.AddHD();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

		if (map.DTSearchWay != DTSearchWay.None && DataType.IsNullOrEmpty(ur.DTFrom) == false)
		{
			String dtFrom = ur.DTFrom; // this.GetTBByID("TB_S_From").Text.Trim().Replace("/", "-");
			String dtTo = ur.DTTo; // this.GetTBByID("TB_S_To").Text.Trim().Replace("/", "-");

			//按日期查询
			if (map.DTSearchWay == DTSearchWay.ByDate)
			{
				qo.addAnd();
				qo.addLeftBracket();
				dtTo += " 23:59:59";
				qo.SQL = map.DTSearchKey + " >= '" + dtFrom + "'";
				qo.addAnd();
				qo.SQL = map.DTSearchKey + " <= '" + dtTo + "'";
				qo.addRightBracket();
			}

			if (map.DTSearchWay == DTSearchWay.ByDateTime)
			{
				//取前一天的24：00
				if (dtFrom.trim().length() == 10) //2017-09-30
				{
					dtFrom += " 00:00:00";
				}
				if (dtFrom.trim().length() == 16) //2017-09-30 00:00
				{
					dtFrom += ":00";
				}

				dtFrom = LocalDateTime.parse(dtFrom).AddDays(-1).toString("yyyy-MM-dd") + " 24:00";

				if (dtTo.trim().length() < 11 || dtTo.trim().indexOf(' ') == -1)
				{
					dtTo += " 24:00";
				}

				qo.addAnd();
				qo.addLeftBracket();
				qo.SQL = map.DTSearchKey + " >= '" + dtFrom + "'";
				qo.addAnd();
				qo.SQL = map.DTSearchKey + " <= '" + dtTo + "'";
				qo.addRightBracket();
			}
		}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 普通属性
		String opkey = ""; // 操作符号。
		for (AttrOfSearch attr : en.EnMap.AttrsOfSearch)
		{
			if (attr.IsHidden)
			{
				qo.addAnd();
				qo.addLeftBracket();

				//获得真实的数据类型.
				if (SystemConfig.AppCenterDBType == DBType.PostgreSQL)
				{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
					var valType = BP.Sys.Glo.GenerRealType(en.EnMap.Attrs, attr.RefAttrKey, attr.DefaultValRun);
					qo.AddWhere(attr.RefAttrKey, attr.DefaultSymbol, valType);
				}
				else
				{
					qo.AddWhere(attr.RefAttrKey, attr.DefaultSymbol, attr.DefaultValRun);
				}
				qo.addRightBracket();
				continue;
			}

			if (attr.SymbolEnable == true)
			{
				opkey = ap.GetValStrByKey("DDL_" + attr.Key);
				if (opkey.equals("all"))
				{
					continue;
				}
			}
			else
			{
				opkey = attr.DefaultSymbol;
			}

			qo.addAnd();
			qo.addLeftBracket();

			if (attr.DefaultVal.Length >= 8)
			{
				String date = "2005-09-01";
				try
				{
					/* 就可能是年月日。 */
					String y = ap.GetValStrByKey("DDL_" + attr.Key + "_Year");
					String m = ap.GetValStrByKey("DDL_" + attr.Key + "_Month");
					String d = ap.GetValStrByKey("DDL_" + attr.Key + "_Day");
					date = y + "-" + m + "-" + d;

					if (opkey.equals("<="))
					{
						LocalDateTime dt = DataType.ParseSysDate2DateTime(date).AddDays(1);
						date = dt.toString(DataType.SysDataFormat);
					}
				}
				catch (java.lang.Exception e)
				{
				}

				qo.AddWhere(attr.RefAttrKey, opkey, date);
			}
			else
			{
				qo.AddWhere(attr.RefAttrKey, opkey, ap.GetValStrByKey("TB_" + attr.Key));
			}
			qo.addRightBracket();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 获得查询数据.
		for (String str : ap.HisHT.keySet())
		{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
			var val = ap.GetValStrByKey(str);
			if (val.equals("all"))
			{
				continue;
			}
			qo.addAnd();
			qo.addLeftBracket();

			//获得真实的数据类型.
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
			var valType = BP.Sys.Glo.GenerRealType(en.EnMap.Attrs, str, ap.GetValStrByKey(str));

			qo.AddWhere(str, valType);
			qo.addRightBracket();
		}

		//获得行数.
		ur.SetPara("RecCount", qo.GetCount());
		ur.Save();

		//获取配置信息
		EnCfg encfg = new EnCfg(this.getEnsName());
		//增加排序
		if (encfg != null)
		{
			String orderBy = encfg.GetParaString("OrderBy");
			boolean isDesc = encfg.GetParaBoolen("IsDeSc");

			if (DataType.IsNullOrEmpty(orderBy) == false)
			{
				if (isDesc)
				{
					qo.addOrderByDesc(encfg.GetParaString("OrderBy"));
				}
				else
				{
					qo.addOrderBy(encfg.GetParaString("OrderBy"));
				}
			}

		}

		if (GetRequestVal("DoWhat") != null && GetRequestVal("DoWhat").equals("Batch"))
		{
			qo.DoQuery(en.PK, 500, 1);
		}
		else
		{
			qo.DoQuery(en.PK, this.getPageSize(), this.getPageIdx());
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 获得查询数据.

		DataTable mydt = ens.ToDataTableField();
		mydt.TableName = "DT";

		ds.Tables.Add(mydt); //把数据加入里面.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 获得方法的集合
		DataTable dtM = new DataTable("dtM");
		dtM.Columns.Add("No");
		dtM.Columns.Add("Title");
		dtM.Columns.Add("Tip");
		dtM.Columns.Add("Visable");

		dtM.Columns.Add("Url");
		dtM.Columns.Add("Target");
		dtM.Columns.Add("Warning");
		dtM.Columns.Add("RefMethodType");
		dtM.Columns.Add("GroupName");
		dtM.Columns.Add("W");
		dtM.Columns.Add("H");
		dtM.Columns.Add("Icon");
		dtM.Columns.Add("IsCanBatch");
		dtM.Columns.Add("RefAttrKey");
		dtM.Columns.Add("ClassMethodName");

		RefMethods rms = map.HisRefMethods;
		for (RefMethod item : rms)
		{
			if (item.IsForEns == false)
			{
				continue;
			}

			if (item.Visable == false)
			{
				continue;
			}

			String myurl = "";

			myurl = "RefMethod.htm?Index=" + item.Index + "&EnName=" + en.toString() + "&EnsName=" + en.GetNewEntities.toString() + "&PKVal=";

			DataRow dr = dtM.NewRow();

			dr.set("No", item.Index);
			dr.set("Title", item.Title);
			dr.set("Tip", item.ToolTip);
			dr.set("Visable", item.Visable);
			dr.set("Warning", item.Warning);
			dr.set("RefMethodType", (int)item.RefMethodType);
			dr.set("RefAttrKey", item.RefAttrKey);
			dr.set("URL", myurl);
			dr.set("W", item.Width);
			dr.set("H", item.Height);
			dr.set("Icon", item.Icon);
			dr.set("IsCanBatch", item.IsCanBatch);
			dr.set("GroupName", item.GroupName);
			dr.set("ClassMethodName", item.ClassMethodName);

			dtM.Rows.Add(dr); //增加到rows.
		}
		ds.Tables.Add(dtM); //把数据加入里面.
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

		return ds;

	}

	private DataTable Search_Data(Entities ens, Entity en)
	{
		//获得.
		Map map = en.EnMapInTime;

		MapAttrs attrs = map.Attrs.ToMapAttrs;
		//取出来查询条件.
		BP.Sys.UserRegedit ur = new UserRegedit();
		ur.MyPK = WebUser.No + "_" + this.getEnsName() + "_SearchAttrs";
		ur.RetrieveFromDBSources();

		//获得关键字.
		AtPara ap = new AtPara(ur.Vals);

		//关键字.
		String keyWord = ur.SearchKey;
		QueryObject qo = new QueryObject(ens);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 关键字字段.
		if (en.EnMap.IsShowSearchKey && DataType.IsNullOrEmpty(keyWord) == false && keyWord.length() > 1)
		{
			Attr attrPK = new Attr();
			for (Attr attr : map.Attrs)
			{
				if (attr.IsPK)
				{
					attrPK = attr;
					break;
				}
			}
			int i = 0;
			for (Attr attr : map.Attrs)
			{
				switch (attr.MyFieldType)
				{
					case FieldType.Enum:
					case FieldType.FK:
					case FieldType.PKFK:
						continue;
					default:
						break;
				}

				if (attr.MyDataType != DataType.AppString)
				{
					continue;
				}

				if (attr.MyFieldType == FieldType.RefText)
				{
					continue;
				}

				if (attr.Key.equals("FK_Dept"))
				{
					continue;
				}

				i++;
				if (i == 1)
				{
					/* 第一次进来。 */
					qo.addLeftBracket();
					if (SystemConfig.AppCenterDBVarStr.equals("@"))
					{
						qo.AddWhere(attr.Key, " LIKE ", SystemConfig.AppCenterDBType == DBType.MySQL ? (" CONCAT('%'," + SystemConfig.AppCenterDBVarStr + "SKey,'%')") : (" '%'+" + SystemConfig.AppCenterDBVarStr + "SKey+'%'"));
					}
					else
					{
						qo.AddWhere(attr.Key, " LIKE ", " '%'||" + SystemConfig.AppCenterDBVarStr + "SKey||'%'");
					}
					continue;
				}
				qo.addOr();

				if (SystemConfig.AppCenterDBVarStr.equals("@"))
				{
					qo.AddWhere(attr.Key, " LIKE ", SystemConfig.AppCenterDBType == DBType.MySQL ? ("CONCAT('%'," + SystemConfig.AppCenterDBVarStr + "SKey,'%')") : ("'%'+" + SystemConfig.AppCenterDBVarStr + "SKey+'%'"));
				}
				else
				{
					qo.AddWhere(attr.Key, " LIKE ", "'%'||" + SystemConfig.AppCenterDBVarStr + "SKey||'%'");
				}

			}
			qo.MyParas.Add("SKey", keyWord);
			qo.addRightBracket();

		}
		else
		{
			qo.AddHD();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

		if (map.DTSearchWay != DTSearchWay.None && DataType.IsNullOrEmpty(ur.DTFrom) == false)
		{
			String dtFrom = ur.DTFrom; // this.GetTBByID("TB_S_From").Text.Trim().Replace("/", "-");
			String dtTo = ur.DTTo; // this.GetTBByID("TB_S_To").Text.Trim().Replace("/", "-");

			if (map.DTSearchWay == DTSearchWay.ByDate)
			{
				qo.addAnd();
				qo.addLeftBracket();
				//设置开始查询时间
				dtFrom += " 00:00:00";
				//结束查询时间
				dtTo += " 23:59:59";
				qo.SQL = map.DTSearchKey + " >= '" + dtFrom + "'";
				qo.addAnd();
				qo.SQL = map.DTSearchKey + " <= '" + dtTo + "'";
				qo.addRightBracket();
			}

			if (map.DTSearchWay == DTSearchWay.ByDateTime)
			{
				//取前一天的24：00
				if (dtFrom.trim().length() == 10) //2017-09-30
				{
					dtFrom += " 00:00:00";
				}
				if (dtFrom.trim().length() == 16) //2017-09-30 00:00
				{
					dtFrom += ":00";
				}

				dtFrom = LocalDateTime.parse(dtFrom).AddDays(-1).toString("yyyy-MM-dd") + " 24:00";

				if (dtTo.trim().length() < 11 || dtTo.trim().indexOf(' ') == -1)
				{
					dtTo += " 24:00";
				}

				qo.addAnd();
				qo.addLeftBracket();
				qo.SQL = map.DTSearchKey + " >= '" + dtFrom + "'";
				qo.addAnd();
				qo.SQL = map.DTSearchKey + " <= '" + dtTo + "'";
				qo.addRightBracket();
			}
		}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 普通属性
		String opkey = ""; // 操作符号。
		for (AttrOfSearch attr : en.EnMap.AttrsOfSearch)
		{
			if (attr.IsHidden)
			{
				qo.addAnd();
				qo.addLeftBracket();
				qo.AddWhere(attr.RefAttrKey, attr.DefaultSymbol, attr.DefaultValRun);
				qo.addRightBracket();
				continue;
			}

			if (attr.SymbolEnable == true)
			{
				opkey = ap.GetValStrByKey("DDL_" + attr.Key);
				if (opkey.equals("all"))
				{
					continue;
				}
			}
			else
			{
				opkey = attr.DefaultSymbol;
			}

			qo.addAnd();
			qo.addLeftBracket();

			if (attr.DefaultVal.Length >= 8)
			{
				String date = "2005-09-01";
				try
				{
					/* 就可能是年月日。 */
					String y = ap.GetValStrByKey("DDL_" + attr.Key + "_Year");
					String m = ap.GetValStrByKey("DDL_" + attr.Key + "_Month");
					String d = ap.GetValStrByKey("DDL_" + attr.Key + "_Day");
					date = y + "-" + m + "-" + d;

					if (opkey.equals("<="))
					{
						LocalDateTime dt = DataType.ParseSysDate2DateTime(date).AddDays(1);
						date = dt.toString(DataType.SysDataFormat);
					}
				}
				catch (java.lang.Exception e)
				{
				}

				qo.AddWhere(attr.RefAttrKey, opkey, date);
			}
			else
			{
				qo.AddWhere(attr.RefAttrKey, opkey, ap.GetValStrByKey("TB_" + attr.Key));
			}
			qo.addRightBracket();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 获得查询数据.
		for (String str : ap.HisHT.keySet())
		{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
			var val = ap.GetValStrByKey(str);
			if (val.equals("all"))
			{
				continue;
			}
			qo.addAnd();
			qo.addLeftBracket();
			qo.AddWhere(str, ap.GetValStrByKey(str));
			qo.addRightBracket();
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 获得查询数据.
		return qo.DoQueryToTable();


	}
	public final String Search_GenerPageIdx()
	{

		BP.Sys.UserRegedit ur = new UserRegedit();
		ur.MyPK = WebUser.No + "_" + this.getEnsName() + "_SearchAttrs";
		ur.RetrieveFromDBSources();

		String url = "?EnsName=" + this.getEnsName();
		int pageSpan = 10;
		int recNum = ur.GetParaInt("RecCount"); //获得查询数量.
		int pageSize = 12;
		if (recNum <= pageSize)
		{
			return "1";
		}

		String html = "";
		html += "<ul class='pagination'>";

		String appPath = ""; // this.Request.ApplicationPath;
		int myidx = 0;
		if (getPageIdx() <= 1)
		{
			html += "<li><img style='vertical-align:middle' src='" + Glo.getCCFlowAppPath() + "WF/Img/Arr/LeftEnd.png' border=0/><img style='vertical-align:middle' src='" + Glo.getCCFlowAppPath() + "WF/Img/Arr/Left.png' border=0/></li>";
		}
		else
		{
			myidx = getPageIdx() - 1;
			//this.Add("<a href='" + url + "&PageIdx=1' >《-</a> <a href='" + url + "&PageIdx=" + myidx + "'>《-</a>");
			html += "<li><a href='" + url + "&PageIdx=1' ><img style='vertical-align:middle' src='" + Glo.getCCFlowAppPath() + "WF/Img/Arr/LeftEnd.png' border=0/></a><a href='" + url + "&PageIdx=" + myidx + "'><img style='vertical-align:middle' src='" + Glo.getCCFlowAppPath() + "WF/Img/Arr/Left.png' border=0/></a></li>";
		}


		//分页采用java默认方式分页，采用bigdecimal分页报错
		int pageNum = 0;
		BigDecimal pageCountD = BigDecimal.Parse(String.valueOf(recNum)).divide(BigDecimal.Parse(String.valueOf(pageSize))); // 页面个数。
		String[] strs = pageCountD.toString("0.0000").split("[.]", -1);
		if (Integer.parseInt(strs[1]) > 0)
		{
			pageNum = Integer.parseInt(strs[0]) + 1;
		}
		else
		{
			pageNum = Integer.parseInt(strs[0]);
		}

		int from = 0;
		int to = 0;
		BigDecimal spanTemp = BigDecimal.Parse(String.valueOf(getPageIdx())).divide(BigDecimal.Parse(String.valueOf(pageSpan))); // 页面个数。

		strs = spanTemp.toString("0.0000").split("[.]", -1);
		from = Integer.parseInt(strs[0]) * pageSpan;
		to = from + pageSpan;
		for (int i = 1; i <= pageNum; i++)
		{
			if (i >= from && i <= to)
			{
				if (getPageIdx() == i)
				{
					html += "<li class='active' ><a href='#'><b>" + i + "</b></a></li>";
				}
				else
				{
					html += "<li><a href='" + url + "&PageIdx=" + i + "'>" + i + "</a></li>";
				}
			}
		}

		if (getPageIdx() != pageNum)
		{
			myidx = getPageIdx() + 1;
			html += "<li><a href='" + url + "&PageIdx=" + myidx + "'><img style='vertical-align:middle' src='" + Glo.getCCFlowAppPath() + "WF/Img/Arr/Right.png' border=0/></a>&nbsp;<a href='" + url + "&PageIdx=" + pageNum + "'><img style='vertical-align:middle' src='" + Glo.getCCFlowAppPath() + "WF/Img/Arr/RightEnd.png' border=0/></a> &nbsp;&nbsp;页数:" + getPageIdx() + "/" + pageNum + "&nbsp;&nbsp;总数:" + recNum + "</li>";
		}
		else
		{
			html += "<li><img style='vertical-align:middle' src='" + Glo.getCCFlowAppPath() + "WF/Img/Arr/Right.png' border=0/></li>";
			html += "<li><img style='vertical-align:middle' src='" + Glo.getCCFlowAppPath() + "WF/Img/Arr/RightEnd.png' border=0/>&nbsp;&nbsp;页数:" + getPageIdx() + "/" + pageNum + "&nbsp;&nbsp;总数:" + recNum + "</li>";
		}
		html += "</ul>";
		return html;
	}
	/** 
	 执行导出
	 
	 @return 
	*/
	public final String Search_Exp()
	{
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		Entity en = ens.GetNewEntity;
		String name = "数据导出";
		String filename = name + "_" + BP.DA.DataType.CurrentDataTimeCNOfLong + "_" + WebUser.Name + ".xls";
		String filePath = ExportDGToExcel(Search_Data(ens, en), en, name);
		//DataTableToExcel(Search_Data(ens, en),en, filename, name,
		//                                                  BP.Web.WebUser.Name, true, true, true);

		return filePath;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 查询.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Refmethod.htm 相关功能.
	public final String Refmethod_Init()
	{
		String ensName = this.getEnsName();
		int index = this.getIndex();
		Entities ens = BP.En.ClassFactory.GetEns(ensName);
		Entity en = ens.GetNewEntity;
		BP.En.RefMethod rm = en.EnMap.HisRefMethods[index];

		String pk = this.getPKVal();
		if (pk == null)
		{
			pk = this.GetRequestVal(en.PK);
		}

		if (pk == null)
		{
			pk = this.getPKVal();
		}

		if (pk == null)
		{
			return "err@错误pkval 没有值。";
		}

		//获取主键集合
		String[] pks = pk.split("[,]", -1);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 处理无参数的方法.
		if (rm.HisAttrs == null || rm.HisAttrs.size() == 0)
		{

			String infos = "";
			for (String mypk : pks)
			{
				if (DataType.IsNullOrEmpty(mypk) == true)
				{
					continue;
				}

				en.PKVal = mypk;
				en.Retrieve();
				rm.HisEn = en;

				// 如果是link.
				if (rm.RefMethodType == RefMethodType.LinkModel || rm.RefMethodType == RefMethodType.LinkeWinOpen || rm.RefMethodType == RefMethodType.RightFrameOpen)
				{
					Object tempVar = rm.Do(null);
					String url = tempVar instanceof String ? (String)tempVar : null;
					if (DataType.IsNullOrEmpty(url))
					{
						infos += "err@应该返回的url.";
						break;
					}

					infos += "url@" + url;
					break;
				}

				Object obj = rm.Do(null);
				if (obj == null)
				{
					infos += "close@info";
					break;
				}

				String result = obj.toString();
				if (result.indexOf("url@") != -1)
				{
					infos += result;
					break;
				}

				result = result.replace("@", "\t\n");
				infos += "close@" + result;
			}

			return infos;
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 处理无参数的方法.
		DataSet ds = new DataSet();

		//转化为json 返回到前台解析. 处理有参数的方法.
		MapAttrs attrs = rm.HisAttrs.ToMapAttrs;

		//属性.
		DataTable mapAttrs = attrs.ToDataTableField("Sys_MapAttrs");
		ds.Tables.Add(mapAttrs);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 该方法的默认值.
		DataTable dtMain = new DataTable();
		dtMain.TableName = "MainTable";
		for (MapAttr attr : attrs)
		{
			dtMain.Columns.Add(attr.KeyOfEn, String.class);
		}

		DataRow mydrMain = dtMain.NewRow();
		for (MapAttr item : attrs)
		{
			String v = item.DefValReal;
			if (v.indexOf('@') == -1)
			{
				mydrMain.set(item.KeyOfEn, item.DefValReal);
			}
			//替换默认值的@的
			else
			{
				if (v.equals("@WebUser.No"))
				{
					mydrMain.set(item.KeyOfEn, Web.WebUser.No);
				}
				else if (v.equals("@WebUser.Name"))
				{
					mydrMain.set(item.KeyOfEn, Web.WebUser.Name);
				}
				else if (v.equals("@WebUser.FK_Dept"))
				{
					mydrMain.set(item.KeyOfEn, Web.WebUser.FK_Dept);
				}
				else if (v.equals("@WebUser.FK_DeptName"))
				{
					mydrMain.set(item.KeyOfEn, Web.WebUser.FK_DeptName);
				}
				else if (v.equals("@WebUser.FK_DeptNameOfFull") || v.equals("@WebUser.FK_DeptFullName"))
				{
					mydrMain.set(item.KeyOfEn, Web.WebUser.FK_DeptNameOfFull);
				}
				else if (v.equals("@RDT"))
				{
					if (item.MyDataType == DataType.AppDate)
					{
						mydrMain.set(item.KeyOfEn, DataType.CurrentData);
					}
					if (item.MyDataType == DataType.AppDateTime)
					{
						mydrMain.set(item.KeyOfEn, DataType.CurrentDataTime);
					}
				}
				else
				{
					//如果是EnsName中字段
					if (en.GetValByKey(v.replace("@", "")) != null)
					{
						mydrMain.set(item.KeyOfEn, en.GetValByKey(v.replace("@", "")).toString());
					}

				}


			}

		}
		dtMain.Rows.Add(mydrMain);
		ds.Tables.Add(dtMain);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 该方法的默认值.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 加入该方法的外键.
		for (DataRow dr : mapAttrs.Rows)
		{
			String lgType = dr.get("LGType").toString();
			if (lgType.equals("2") == false)
			{
				continue;
			}

			String UIIsEnable = dr.get("UIVisible").toString();
			if (UIIsEnable.equals("0"))
			{
				continue;
			}

			String uiBindKey = dr.get("UIBindKey").toString();
			if (DataType.IsNullOrEmpty(uiBindKey) == true)
			{
				String myPK = dr.get("MyPK").toString();
				/*如果是空的*/
				//   throw new Exception("@属性字段数据不完整，流程:" + fl.No + fl.Name + ",节点:" + nd.NodeID + nd.Name + ",属性:" + myPK + ",的UIBindKey IsNull ");
			}

			// 检查是否有下拉框自动填充。
			String keyOfEn = dr.get("KeyOfEn").toString();
			String fk_mapData = dr.get("FK_MapData").toString();
			if (ds.Tables.Contains(uiBindKey) == false)
			{
				ds.Tables.Add(BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey));
			}

		}

		//加入sql模式的外键.
		for (Attr attr : rm.HisAttrs)
		{
			if (attr.IsRefAttr == true)
			{
				continue;
			}

			if (DataType.IsNullOrEmpty(attr.UIBindKey) || attr.UIBindKey.Length <= 10)
			{
				continue;
			}

			if (attr.UIIsReadonly == true)
			{
				continue;
			}

			if (attr.UIBindKey.Contains("SELECT") == true || attr.UIBindKey.Contains("select") == true)
			{
				/*是一个sql*/
				Object tempVar2 = attr.UIBindKey.Clone();
				String sqlBindKey = tempVar2 instanceof String ? (String)tempVar2 : null;
				sqlBindKey = BP.WF.Glo.DealExp(sqlBindKey, en, null);

				DataTable dt1 = DBAccess.RunSQLReturnTable(sqlBindKey);
				dt1.TableName = attr.Key;

				//@杜. 翻译当前部分.
				if (SystemConfig.AppCenterDBType == DBType.Oracle || SystemConfig.AppCenterDBType == DBType.PostgreSQL)
				{
					dt1.Columns["NO"].ColumnName = "No";
					dt1.Columns["NAME"].ColumnName = "Name";
				}
				if (ds.Tables.Contains(attr.Key) == false)
				{
					ds.Tables.Add(dt1);
				}

			}
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 加入该方法的外键.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 加入该方法的枚举.
		DataTable dtEnum = new DataTable();
		dtEnum.Columns.Add("Lab", String.class);
		dtEnum.Columns.Add("EnumKey", String.class);
		dtEnum.Columns.Add("IntKey", String.class);
		dtEnum.TableName = "Sys_Enum";

		for (MapAttr item : attrs)
		{
			if (item.LGType != FieldTypeS.Enum)
			{
				continue;
			}

			SysEnums ses = new SysEnums(item.UIBindKey);
			for (SysEnum se : ses)
			{
				DataRow drEnum = dtEnum.NewRow();
				drEnum.set("Lab", se.Lab);
				drEnum.set("EnumKey", se.EnumKey);
				drEnum.set("IntKey", se.IntKey);
				dtEnum.Rows.Add(drEnum);
			}

		}

		ds.Tables.Add(dtEnum);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 加入该方法的枚举.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 增加该方法的信息
		DataTable dt = new DataTable();
		dt.TableName = "RM";
		dt.Columns.Add("Title", String.class);
		dt.Columns.Add("Warning", String.class);

		DataRow mydr = dt.NewRow();
		mydr.set("Title", rm.Title);
		mydr.set("Warning", rm.Warning);
		dt.Rows.Add(mydr);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 增加该方法的信息

		//增加到里面.
		ds.Tables.Add(dt);

		return BP.Tools.Json.ToJson(ds);
	}

	public final String Entitys_Init()
	{
		//定义容器.
		DataSet ds = new DataSet();

		//查询出来从表数据.
		Entities dtls = ClassFactory.GetEns(this.getEnsName());
		Entity en = dtls.GetNewEntity;
		QueryObject qo = new QueryObject(dtls);
		qo.addOrderBy(en.PK);
		qo.DoQuery();
		ds.Tables.Add(dtls.ToDataTableField("Ens"));

		//实体.
		Entity dtl = dtls.GetNewEntity;
		//定义Sys_MapData.
		MapData md = new MapData();
		md.No = this.getEnName();
		md.Name = dtl.EnDesc;

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 加入权限信息.
		//把权限加入参数里面.
		if (dtl.HisUAC.IsInsert)
		{
			md.SetPara("IsInsert", "1");
		}
		if (dtl.HisUAC.IsUpdate)
		{
			md.SetPara("IsUpdate", "1");
		}
		if (dtl.HisUAC.IsDelete)
		{
			md.SetPara("IsDelete", "1");
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 加入权限信息.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 判断主键是否为自增长

		if (en.IsNoEntity == true && en.EnMap.IsAutoGenerNo)
		{
			md.SetPara("IsNewRow", "0");
		}
		else
		{
			md.SetPara("IsNewRow", "1");
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 添加EN的主键
		md.SetPara("PK", en.PK);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

		ds.Tables.Add(md.ToDataTableField("Sys_MapData"));

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 字段属性.
		MapAttrs attrs = dtl.EnMap.Attrs.ToMapAttrs;
		DataTable sys_MapAttrs = attrs.ToDataTableField("Sys_MapAttr");
		ds.Tables.Add(sys_MapAttrs);
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 字段属性.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 把外键与枚举放入里面去.
		for (DataRow dr : sys_MapAttrs.Rows)
		{
			String uiBindKey = dr.get("UIBindKey").toString();
			String lgType = dr.get("LGType").toString();
			if (lgType.equals("2") == false)
			{
				continue;
			}

			String UIIsEnable = dr.get("UIVisible").toString();
			if (UIIsEnable.equals("0"))
			{
				continue;
			}

			if (DataType.IsNullOrEmpty(uiBindKey) == true)
			{
				String myPK = dr.get("MyPK").toString();
				/*如果是空的*/
				//   throw new Exception("@属性字段数据不完整，流程:" + fl.No + fl.Name + ",节点:" + nd.NodeID + nd.Name + ",属性:" + myPK + ",的UIBindKey IsNull ");
			}

			// 检查是否有下拉框自动填充。
			String keyOfEn = dr.get("KeyOfEn").toString();
			String fk_mapData = dr.get("FK_MapData").toString();


			// 判断是否存在.
			if (ds.Tables.Contains(uiBindKey) == true)
			{
				continue;
			}

			ds.Tables.Add(BP.Sys.PubClass.GetDataTableByUIBineKey(uiBindKey));
		}

		String enumKeys = "";
		for (Attr attr : dtl.EnMap.Attrs)
		{
			if (attr.MyFieldType == FieldType.Enum)
			{
				enumKeys += "'" + attr.UIBindKey + "',";
			}
		}

		if (enumKeys.length() > 2)
		{
			enumKeys = enumKeys.substring(0, enumKeys.length() - 1);

			String sqlEnum = "SELECT * FROM Sys_Enum WHERE EnumKey IN (" + enumKeys + ")";
			DataTable dtEnum = DBAccess.RunSQLReturnTable(sqlEnum);

			dtEnum.TableName = "Sys_Enum";

			if (SystemConfig.AppCenterDBType == DBType.Oracle || SystemConfig.AppCenterDBType == DBType.PostgreSQL)
			{
				dtEnum.Columns["MYPK"].ColumnName = "MyPK";
				dtEnum.Columns["LAB"].ColumnName = "Lab";
				dtEnum.Columns["ENUMKEY"].ColumnName = "EnumKey";
				dtEnum.Columns["INTKEY"].ColumnName = "IntKey";
				dtEnum.Columns["LANG"].ColumnName = "Lang";
			}
			ds.Tables.Add(dtEnum);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 把外键与枚举放入里面去.

		return BP.Tools.Json.ToJson(ds);
	}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 实体集合的保存.
	/** 
	 实体集合的删除
	 
	 @return 
	*/
	public final String Entities_Delete()
	{
		try
		{
			if (this.getParas() == null)
			{
				return "err@删除实体，参数不能为空";
			}
			String[] myparas = this.getParas().split("[@]", -1);

			Entities ens = ClassFactory.GetEns(this.getEnsName());

			ArrayList<String[]> paras = new ArrayList<String[]>();
			int idx = 0;
			for (int i = 0; i < myparas.length; i++)
			{
				String para = myparas[i];
				if (DataType.IsNullOrEmpty(para) || para.contains("=") == false)
				{
					continue;
				}

				String[] strs = para.split("[=]", -1);
				paras.add(strs);
			}

			if (paras.size() == 1)
			{
				ens.Delete(paras.get(0)[0], paras.get(0)[1]);
			}

			if (paras.size() == 2)
			{
				ens.Delete(paras.get(0)[0], paras.get(0)[1], paras.get(1)[0], paras.get(1)[1]);
			}

			if (paras.size() == 3)
			{
				ens.Delete(paras.get(0)[0], paras.get(0)[1], paras.get(1)[0], paras.get(1)[1], paras.get(2)[0], paras.get(2)[1]);
			}

			if (paras.size() == 4)
			{
				ens.Delete(paras.get(0)[0], paras.get(0)[1], paras.get(1)[0], paras.get(1)[1], paras.get(2)[0], paras.get(2)[1], paras.get(3)[0], paras.get(3)[1]);
			}

			if (paras.size() > 4)
			{
				return "err@实体类的删除，条件不能大于4个";
			}

			return "删除成功";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}

	}
	/** 
	 初始化
	 
	 @return 
	*/
	public final String Entities_Save()
	{
		try
		{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region  查询出来s实体数据.
			Entities dtls = BP.En.ClassFactory.GetEns(this.getEnsName());
			Entity en = dtls.GetNewEntity;
			QueryObject qo = new QueryObject(dtls);
			//qo.DoQuery(en.PK, BP.Sys.SystemConfig.PageSize, this.PageIdx, false);
			qo.DoQuery();
			Map map = en.EnMap;
			for (Entity item : dtls)
			{
				String pkval = item.PKVal.toString();

				for (Attr attr : map.Attrs)
				{
					if (attr.IsRefAttr == true)
					{
						continue;
					}

					if (attr.MyDataType == DataType.AppDateTime || attr.MyDataType == DataType.AppDate)
					{
						if (attr.UIIsReadonly == false)
						{
							continue;
						}

						String val = this.GetValFromFrmByKey("TB_" + pkval + "_" + attr.Key, null);
						item.SetValByKey(attr.Key, val);
						continue;
					}


					if (attr.UIContralType == UIContralType.TB && attr.UIIsReadonly == false)
					{
						String val = this.GetValFromFrmByKey("TB_" + pkval + "_" + attr.Key, null);
						item.SetValByKey(attr.Key, val);
						continue;
					}

					if (attr.UIContralType == UIContralType.DDL && attr.UIIsReadonly == true)
					{
						String val = this.GetValFromFrmByKey("DDL_" + pkval + "_" + attr.Key);
						item.SetValByKey(attr.Key, val);
						continue;
					}

					if (attr.UIContralType == UIContralType.CheckBok && attr.UIIsReadonly == true)
					{
						String val = this.GetValFromFrmByKey("CB_" + pkval + "_" + attr.Key, "-1");
						if (val.equals("-1"))
						{
							item.SetValByKey(attr.Key, 0);
						}
						else
						{
							item.SetValByKey(attr.Key, 1);
						}
						continue;
					}
				}

				item.Update(); //执行更新.
			}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion  查询出来实体数据.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 保存新加行.
			//没有新增行
			if (this.GetRequestValBoolen("InsertFlag") == false)
			{
				return "保存成功.";
			}

			String valValue = "";


			for (Attr attr : map.Attrs)
			{

				if (attr.MyDataType == DataType.AppDateTime || attr.MyDataType == DataType.AppDate)
				{
					if (attr.UIIsReadonly == false)
					{
						continue;
					}

					valValue = this.GetValFromFrmByKey("TB_" + 0 + "_" + attr.Key, null);
					en.SetValByKey(attr.Key, valValue);
					continue;
				}

				if (attr.UIContralType == UIContralType.TB && attr.UIIsReadonly == false)
				{
					valValue = this.GetValFromFrmByKey("TB_" + 0 + "_" + attr.Key);
					en.SetValByKey(attr.Key, valValue);
					continue;
				}

				if (attr.UIContralType == UIContralType.DDL && attr.UIIsReadonly == true)
				{
					valValue = this.GetValFromFrmByKey("DDL_" + 0 + "_" + attr.Key);
					en.SetValByKey(attr.Key, valValue);
					continue;
				}

				if (attr.UIContralType == UIContralType.CheckBok && attr.UIIsReadonly == true)
				{
					valValue = this.GetValFromFrmByKey("CB_" + 0 + "_" + attr.Key, "-1");
					if (valValue.equals("-1"))
					{
						en.SetValByKey(attr.Key, 0);
					}
					else
					{
						en.SetValByKey(attr.Key, 1);
					}
					continue;
				}
			}

			if (en.IsNoEntity)
			{
				if (en.EnMap.IsAutoGenerNo)
				{
					en.SetValByKey("No", en.GenerNewNoByKey("No"));
				}
			}

			try
			{
				if (en.PKVal.toString().equals("0"))
				{
				}
				else
				{
					en.Insert();
				}
			}
			catch (RuntimeException ex)
			{
				//异常处理..
				Log.DebugWriteInfo(ex.getMessage());
				//msg += "<hr>" + ex.Message;
			}



//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 保存新加行.

			return "保存成功.";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 获取批处理的方法.
	public final String Refmethod_BatchInt()
	{
		String ensName = this.getEnsName();
		Entities ens = BP.En.ClassFactory.GetEns(ensName);
		Entity en = ens.GetNewEntity;
		BP.En.RefMethods rms = en.EnMap.HisRefMethods;
		DataTable dt = new DataTable();
		dt.TableName = "RM";
		dt.Columns.Add("No");
		dt.Columns.Add("Title");
		dt.Columns.Add("Tip");
		dt.Columns.Add("Visable");

		dt.Columns.Add("Url");
		dt.Columns.Add("Target");
		dt.Columns.Add("Warning");
		dt.Columns.Add("RefMethodType");
		dt.Columns.Add("GroupName");
		dt.Columns.Add("W");
		dt.Columns.Add("H");
		dt.Columns.Add("Icon");
		dt.Columns.Add("IsCanBatch");
		dt.Columns.Add("RefAttrKey");
		for (RefMethod item : rms)
		{
			if (item.IsCanBatch == false)
			{
				continue;
			}
			DataRow mydr = dt.NewRow();
			String myurl = "";
			if (item.RefMethodType != RefMethodType.Func)
			{
				Object tempVar = item.Do(null);
				myurl = tempVar instanceof String ? (String)tempVar : null;
				if (myurl == null)
				{
					continue;
				}
			}
			else
			{
				myurl = "../Comm/RefMethod.htm?Index=" + item.Index + "&EnName=" + en.toString() + "&EnsName=" + en.GetNewEntities.toString() + "&PKVal=" + this.getPKVal();
			}

			DataRow dr = dt.NewRow();

			dr.set("No", item.Index);
			dr.set("Title", item.Title);
			dr.set("Tip", item.ToolTip);
			dr.set("Visable", item.Visable);
			dr.set("Warning", item.Warning);

			dr.set("RefMethodType", (int)item.RefMethodType);
			dr.set("RefAttrKey", item.RefAttrKey);
			dr.set("URL", myurl);
			dr.set("W", item.Width);
			dr.set("H", item.Height);
			dr.set("Icon", item.Icon);
			dr.set("IsCanBatch", item.IsCanBatch);
			dr.set("GroupName", item.GroupName);
			dt.Rows.Add(dr);
		}

		return BP.Tools.Json.ToJson(dt);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	public final String Refmethod_Done()
	{
		Entities ens = BP.En.ClassFactory.GetEns(this.getEnsName());
		Entity en = ens.GetNewEntity;
		String msg = "";

		String pk = this.getPKVal();

		if (pk.contains(",") == false)
		{
			/*批处理的方式.*/
			en.PKVal = pk;

			en.Retrieve();
			msg = DoOneEntity(en, this.getIndex());
			if (msg == null)
			{
				return "close@info";
			}
			else
			{
				return "info@" + msg;
			}
		}

		//如果是批处理.
		String[] pks = pk.split("[,]", -1);
		for (String mypk : pks)
		{
			if (DataType.IsNullOrEmpty(mypk) == true)
			{
				continue;
			}

			en.PKVal = mypk;
			en.Retrieve();

			String s = DoOneEntity(en, this.getIndex());
			if (s != null)
			{
				msg += "@" + s;
			}
		}

		if (msg.equals(""))
		{
			return "close@info";
		}
		else
		{
			return "info@" + msg;
		}
	}
	public final String DoOneEntity(Entity en, int rmIdx)
	{
		BP.En.RefMethod rm = en.EnMap.HisRefMethods[rmIdx];
		rm.HisEn = en;
		int mynum = 0;
		for (Attr attr : rm.HisAttrs)
		{
			if (attr.MyFieldType == FieldType.RefText)
			{
				continue;
			}
			mynum++;
		}

		Object[] objs = new Object[mynum];

		int idx = 0;
		for (Attr attr : rm.HisAttrs)
		{
			if (attr.MyFieldType == FieldType.RefText)
			{
				continue;
			}

			switch (attr.UIContralType)
			{
				case UIContralType.TB:
					switch (attr.MyDataType)
					{
						case BP.DA.DataType.AppString:
						case BP.DA.DataType.AppDate:
						case BP.DA.DataType.AppDateTime:
							String str1 = this.GetValFromFrmByKey(attr.Key);
							objs[idx] = str1;
							//attr.DefaultVal=str1;
							break;
						case BP.DA.DataType.AppInt:
							int myInt = this.GetValIntFromFrmByKey(attr.Key);
							objs[idx] = myInt;
							//attr.DefaultVal=myInt;
							break;
						case BP.DA.DataType.AppFloat:
							float myFloat = this.GetValFloatFromFrmByKey(attr.Key);
							objs[idx] = myFloat;
							//attr.DefaultVal=myFloat;
							break;
						case BP.DA.DataType.AppDouble:
						case BP.DA.DataType.AppMoney:
							BigDecimal myDoub = this.GetValDecimalFromFrmByKey(attr.Key);
							objs[idx] = myDoub;
							//attr.DefaultVal=myDoub;
							break;
						case BP.DA.DataType.AppBoolean:
							objs[idx] = this.GetValBoolenFromFrmByKey(attr.Key);
							attr.DefaultVal = false;
							break;
						default:
							throw new RuntimeException("没有判断的数据类型．");

					}
					break;
				case UIContralType.DDL:
					try
					{
						if (attr.MyDataType == DataType.AppString)
						{
							String str = this.GetValFromFrmByKey(attr.Key);
							objs[idx] = str;
							attr.DefaultVal = str;
						}
						else
						{
							int enumVal = this.GetValIntFromFrmByKey(attr.Key);
							objs[idx] = enumVal;
							attr.DefaultVal = enumVal;
						}

					}
					catch (java.lang.Exception e)
					{
						objs[idx] = null;
					}
					break;
				case UIContralType.CheckBok:
					objs[idx] = this.GetValBoolenFromFrmByKey(attr.Key);

					attr.DefaultVal = objs[idx].toString();

					break;
				default:
					break;
			}
			idx++;
		}

		try
		{
			Object obj = rm.Do(objs);
			if (obj != null)
			{
				return obj.toString();
			}

			return null;
		}
		catch (RuntimeException ex)
		{
			String msg = "";
			for (Object obj : objs)
			{
				msg += "@" + obj.toString();
			}
			String err = "@执行[" + this.getEnsName() + "][" + rm.ClassMethodName + "]期间出现错误：" + ex.getMessage() + " InnerException= " + ex.getCause() + "[参数为：]" + msg;
			return "<font color=red>" + err + "</font>";
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 相关功能.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region  公共方法。
	public final String SFTable()
	{
		SFTable sftable = new SFTable(this.GetRequestVal("SFTable"));
		DataTable dt = sftable.GenerHisDataTable();
		return BP.Tools.Json.ToJson(dt);
	}
	/** 
	 获得一个实体的数据
	 
	 @return 
	*/
	public final String EnsData()
	{
		Entities ens = ClassFactory.GetEns(this.getEnsName());

		String filter = this.GetRequestVal("Filter");

		if (filter == null || filter.equals("") || filter.contains("=") == false)
		{
			ens.RetrieveAll();
		}
		else
		{
			QueryObject qo = new QueryObject(ens);
			String[] strs = filter.split("[=]", -1);
			qo.AddWhere(strs[0], strs[1]);
			qo.DoQuery();
		}
		return ens.ToJson();
	}
	/** 
	 执行一个SQL，然后返回一个列表.
	 用于gener.js 的公共方法.
	 
	 @return 
	*/
	public final String SQLList()
	{
		String sqlKey = this.GetRequestVal("SQLKey"); //SQL的key.
		String paras = this.GetRequestVal("Paras"); //参数. 格式为 @para1=paraVal@para2=val2

		BP.Sys.XML.SQLList sqlXml = new BP.Sys.XML.SQLList(sqlKey);

		//获得SQL
		String sql = sqlXml.SQL;
		String[] strs = paras.split("[@]", -1);
		for (String str : strs)
		{
			if (str == null || str.equals(""))
			{
				continue;
			}

			//参数.
			String[] p = str.split("[=]", -1);
			sql = sql.replace("@" + p[0], p[1]);
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		return BP.Tools.Json.ToJson(dt);
	}
	public final String EnumList()
	{
		SysEnums ses = new SysEnums(this.getEnumKey());
		return ses.ToJson();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion  公共方法。

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 执行方法.
	/** 
	 执行方法
	 
	 @param clsName 类名称
	 @param monthodName 方法名称
	 @param paras 参数，可以为空.
	 @return 执行结果
	*/

	public final String Exec(String clsName, String methodName)
	{
		return Exec(clsName, methodName, null);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public string Exec(string clsName, string methodName, string paras = null)
	public final String Exec(String clsName, String methodName, String paras)
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 处理 HttpHandler 类.
		if (clsName.contains(".HttpHandler.") == true)
		{
			//创建类实体.
			Object tempVar = java.lang.Class.forName("BP.WF.HttpHandler.DirectoryPageBase").newInstance();
			DirectoryPageBase ctrl = tempVar instanceof DirectoryPageBase ? (DirectoryPageBase)tempVar : null;
			//ctrl.context = this.context;

			try
			{
				//执行方法返回json.
				String data = ctrl.DoMethod(ctrl, methodName);
				return data;
			}
			catch (RuntimeException ex)
			{
				String parasStr = "";
				for (String key : HttpContextHelper.RequestParamKeys)
				{
					parasStr += "@" + key + "=" + HttpContextHelper.RequestParams(key);
				}
				return "err@" + ex.getMessage() + " 参数:" + parasStr;
			}
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 处理 page 类.

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 执行entity类的方法.
		try
		{
			//创建类实体.
			Object tempVar2 = java.lang.Class.forName("BP.En.Entity").newInstance();
			BP.En.Entity en = tempVar2 instanceof BP.En.Entity ? (BP.En.Entity)tempVar2 : null;
			en.PKVal = this.getPKVal();
			en.RetrieveFromDBSources();

			java.lang.Class tp = en.getClass();
			java.lang.reflect.Method mp = tp.getMethod(methodName);
			if (mp == null)
			{
				return "err@没有找到类[" + clsName + "]方法[" + methodName + "].";
			}

			//执行该方法.
			Object[] myparas = null;
			Object tempVar3 = mp.Invoke(this, myparas);
			String result = tempVar3 instanceof String ? (String)tempVar3 : null; //调用由此 MethodInfo 实例反射的方法或构造函数。
			return result;
		}
		catch (RuntimeException ex)
		{
			return "err@执行实体类的方法错误:" + ex.getMessage();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion 执行entity类的方法.
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 数据库相关.
	/** 
	 运行SQL
	 
	 @return 返回影响行数
	*/
	public final String DBAccess_RunSQL()
	{
		String sql = this.GetRequestVal("SQL");
		sql = sql.replace("~", "'");
		sql = sql.replace("[%]", "%"); //防止URL编码

		return DBAccess.RunSQL(sql).toString();
	}
	/** 
	 运行SQL返回DataTable
	 
	 @return DataTable转换的json
	*/
	public final String DBAccess_RunSQLReturnTable()
	{
		String sql = this.GetRequestVal("SQL");
		sql = sql.replace("~", "'");
		sql = sql.replace("[%]", "%"); //防止URL编码

		sql = sql.replace("@WebUser.No", WebUser.No); //替换变量.
		sql = sql.replace("@WebUser.Name", WebUser.Name); //替换变量.
		sql = sql.replace("@WebUser.FK_Dept", WebUser.FK_Dept); //替换变量.
		sql = sql.replace("@WebUser.DeptParentNo)", WebUser.DeptParentNo); //替换变量.



//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
///#warning zhoupeng把这个去掉了. 2018.10.24
		// sql = sql.Replace("-", "%"); //为什么？

		sql = sql.replace("/#", "+"); //为什么？
		sql = sql.replace("/$", "-"); //为什么？


		if (sql.equals(null) || sql.equals(""))
		{
			return "err@查询sql为空";
		}
		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		//暂定
		if (SystemConfig.AppCenterDBType == DBType.Oracle || SystemConfig.AppCenterDBType == DBType.PostgreSQL)
		{
			//获取SQL的字段
			//获取 from 的位置
			sql = sql.replace(" ", "");
			int index = sql.toUpperCase().indexOf("FROM");
			int indexAs = 0;
			sql = sql.substring(6, index);
			String[] keys = sql.split("[,]", -1);
			for (String key : keys)
			{
				String realkey = key.replace("Case", "").replace("case", "").replace("CASE", "");
				indexAs = realkey.toUpperCase().indexOf("AS");
				if (indexAs != -1)
				{
					realkey = realkey.substring(indexAs + 2);
				}
				if (dt.Columns[realkey.toUpperCase()] != null)
				{
					dt.Columns[realkey.toUpperCase()].ColumnName = realkey;
				}
			}

		}

		return BP.Tools.Json.ToJson(dt);
	}
	public final String RunUrlCrossReturnString()
	{
		String url = this.GetRequestVal("url");
		String strs = DataType.ReadURLContext(url, 9999, System.Text.Encoding.UTF8);
		return strs;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	//执行方法.
	public final String HttpHandler()
	{
		//@樊雷伟 , 这个方法需要同步.

		//获得两个参数.
		String httpHandlerName = this.GetRequestVal("HttpHandlerName");
		String methodName = this.GetRequestVal("DoMethod");

//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		var type = java.lang.Class.forName(httpHandlerName);
		if (type == null)
		{
			Object tempVar = ClassFactory.GetHandlerPage(httpHandlerName);
			BP.WF.HttpHandler.DirectoryPageBase obj = tempVar instanceof BP.WF.HttpHandler.DirectoryPageBase ? (BP.WF.HttpHandler.DirectoryPageBase)tempVar : null;
			if (obj == null)
			{
				return "err@页面处理类名[" + httpHandlerName + "],没有获取到，请检查拼写错误？";
			}
			// obj.context = this.context;
			return obj.DoMethod(obj, methodName);
		}
		else
		{
			Object tempVar2 = type.newInstance();
			BP.WF.HttpHandler.DirectoryPageBase en = tempVar2 instanceof BP.WF.HttpHandler.DirectoryPageBase ? (BP.WF.HttpHandler.DirectoryPageBase)tempVar2 : null;
			//en.context = this.context;
			return en.DoMethod(en, methodName);
		}
	}
	/** 
	 当前登录人员信息
	 
	 @return 
	*/
	public final String WebUser_Init()
	{
		Hashtable ht = new Hashtable();

		String userNo = Web.WebUser.No;
		if (DataType.IsNullOrEmpty(userNo) == true)
		{
			ht.put("No", "");
			ht.put("Name", "");
			ht.put("FK_Dept", "");
			ht.put("FK_DeptName", "");
			ht.put("FK_DeptNameOfFull", "");

			ht.put("CustomerNo", BP.Sys.SystemConfig.CustomerNo);
			ht.put("CustomerName", BP.Sys.SystemConfig.CustomerName);
			return BP.Tools.Json.ToJson(ht);
		}

		ht.put("No", WebUser.No);
		ht.put("Name", WebUser.Name);
		ht.put("FK_Dept", WebUser.FK_Dept);
		ht.put("FK_DeptName", WebUser.FK_DeptName);
		ht.put("FK_DeptNameOfFull", WebUser.FK_DeptNameOfFull);
		ht.put("CustomerNo", BP.Sys.SystemConfig.CustomerNo);
		ht.put("CustomerName", BP.Sys.SystemConfig.CustomerName);
		ht.put("SID", WebUser.SID);

		//检查是否是授权状态.
		if (WebUser.IsAuthorize == true)
		{
			ht.put("IsAuthorize", "1");
			ht.put("Auth", WebUser.Auth);
			ht.put("AuthName", WebUser.AuthName);
		}
		else
		{
			ht.put("IsAuthorize", "0");
		}
		return BP.Tools.Json.ToJson(ht);
	}

	public final String WebUser_BackToAuthorize()
	{
		BP.WF.Dev2Interface.Port_Login(WebUser.Auth);
		return "登录成功";
	}
	/** 
	 当前登录人员信息
	 
	 @return 
	*/
	public final String GuestUser_Init()
	{
		Hashtable ht = new Hashtable();
		ht.put("No", GuestUser.No);
		ht.put("Name", GuestUser.Name);
		ht.put("DeptNo", GuestUser.DeptNo);
		ht.put("DeptName", GuestUser.DeptName);
		return BP.Tools.Json.ToJson(ht);
	}


	/** 
	 实体Entity 文件上传
	 
	 @return 
	*/

	public final String EntityAth_Upload()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		var files = HttpContextHelper.RequestFiles();
		if (files.size() == 0)
		{
			return "err@请选择要上传的文件。";
		}
		//获取保存文件信息的实体

		String enName = this.getEnName();
		Entity en = null;

		//是否是空白记录.
		boolean isBlank = DataType.IsNullOrEmpty(this.getPKVal());
		if (isBlank == true)
		{
			return "err@请先保存实体信息然后再上传文件";
		}
		else
		{
			en = ClassFactory.GetEn(this.getEnName());
		}

		if (en == null)
		{
			return "err@参数类名不正确.";
		}
		en.PKVal = this.getPKVal();
		int i = en.RetrieveFromDBSources();
		if (i == 0)
		{
			return "err@数据[" + this.getEnName() + "]主键为[" + en.PKVal + "]不存在，或者没有保存。";
		}

		//获取文件的名称
		String fileName = files[0].FileName;
		if (fileName.indexOf("\\") >= 0)
		{
			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
		}
		fileName = fileName.substring(0, fileName.lastIndexOf('.'));
		//文件后缀
		String ext = System.IO.Path.GetExtension(files[0].FileName);
		ext = ext.replace(".", ""); //去掉点 @李国文

		//文件大小
		float size = HttpContextHelper.RequestFileLength(files[0]) / 1024;

		//保存位置
		String filepath = "";


		//如果是天业集团则保存在ftp服务器上
		if (SystemConfig.CustomerNo.equals("TianYe") || SystemConfig.IsUploadFileToFTP == true)
		{
			String guid = DBAccess.GenerGUID();

			//把文件临时保存到一个位置.
			String temp = SystemConfig.PathOfTemp + guid + ".tmp";
			try
			{
				//files[0].SaveAs(temp);
				HttpContextHelper.UploadFile(files[0], temp);
			}
			catch (RuntimeException ex)
			{
				(new File(temp)).delete();
				//files[0].SaveAs(temp);
				HttpContextHelper.UploadFile(files[0], temp);
			}

			/*保存到fpt服务器上.*/
			FtpSupport.FtpConnection ftpconn = new FtpSupport.FtpConnection(SystemConfig.FTPServerIP, SystemConfig.FTPUserNo, SystemConfig.FTPUserPassword);

			if (ftpconn == null)
			{
				return "err@FTP服务器连接失败";
			}

			String ny = LocalDateTime.now().toString("yyyy_MM");

			//判断目录年月是否存在.
			if (ftpconn.DirectoryExist(ny) == false)
			{
				ftpconn.CreateDirectory(ny);
			}
			ftpconn.SetCurrentDirectory(ny);

			//判断目录是否存在.
			if (ftpconn.DirectoryExist("Helper") == false)
			{
				ftpconn.CreateDirectory("Helper");
			}

			//设置当前目录，为操作的目录。
			ftpconn.SetCurrentDirectory("Helper");

			//把文件放上去.
			ftpconn.PutFile(temp, guid + "." + ext);
			ftpconn.Close();

			//删除临时文件
			(new File(temp)).delete();

			//设置路径.
			filepath = ny + "//Helper//" + guid + "." + ext;

		}
		else
		{
			String fileSavePath = en.EnMap.FJSavePath;

			if (DataType.IsNullOrEmpty(fileSavePath) == true)
			{
				fileSavePath = BP.Sys.SystemConfig.PathOfDataUser + enName;
			}

			if ((new File(fileSavePath)).isDirectory() == false)
			{
				(new File(fileSavePath)).mkdirs();
			}

			filepath = fileSavePath + "\\" + this.getPKVal() + "." + ext;

			//存在文件则删除
			if ((new File(filepath)).isFile() == true)
			{
				(new File(filepath)).delete();
			}

			File info = new File(filepath);
			//files[0].SaveAs(filepath);
			HttpContextHelper.UploadFile(files[0], filepath);
		}

		//需要这样写 @李国文.
		en.SetValByKey("MyFileName", fileName);
		en.SetValByKey("MyFilePath", filepath);
		en.SetValByKey("MyFileExt", ext);
		en.SetValByKey("MyFileSize", size);
		en.SetValByKey("WebPath", filepath);

		en.Update();
		return "文件保存成功";
	}


	public final String EntityMultiAth_Upload()
	{
		//HttpFileCollection files = context.Request.Files;
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
		var files = HttpContextHelper.RequestFiles();
		if (files.size() == 0)
		{
			return "err@请选择要上传的文件。";
		}
		//获取保存文件信息的实体

		String enName = this.getEnName();
		Entity en = null;

		//是否是空白记录.
		boolean isBlank = DataType.IsNullOrEmpty(this.getPKVal());
		if (isBlank == true)
		{
			return "err@请先保存实体信息然后再上传文件";
		}
		else
		{
			en = ClassFactory.GetEn(this.getEnName());
		}

		if (en == null)
		{
			return "err@参数类名不正确.";
		}
		en.PKVal = this.getPKVal();
		int i = en.RetrieveFromDBSources();
		if (i == 0)
		{
			return "err@数据[" + this.getEnName() + "]主键为[" + en.PKVal + "]不存在，或者没有保存。";
		}

		//获取文件的名称
		String fileName = files[0].FileName;
		if (fileName.indexOf("\\") >= 0)
		{
			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
		}
		fileName = fileName.substring(0, fileName.lastIndexOf('.'));
		//文件后缀
		String ext = System.IO.Path.GetExtension(files[0].FileName);

		//文件大小
		float size = HttpContextHelper.RequestFileLength(files[0]) / 1024;

		//保存位置
		String filepath = "";

		//如果是天业集团则保存在ftp服务器上
		if (SystemConfig.CustomerNo.equals("TianYe") || SystemConfig.IsUploadFileToFTP == true)
		{
			String guid = DBAccess.GenerGUID();

			//把文件临时保存到一个位置.
			String temp = SystemConfig.PathOfTemp + guid + ".tmp";
			try
			{
				//files[0].SaveAs(temp);
				HttpContextHelper.UploadFile(files[0], temp);
			}
			catch (RuntimeException ex)
			{
				(new File(temp)).delete();
				//files[0].SaveAs(temp);
				HttpContextHelper.UploadFile(files[0], temp);
			}

			/*保存到fpt服务器上.*/
			FtpSupport.FtpConnection ftpconn = new FtpSupport.FtpConnection(SystemConfig.FTPServerIP, SystemConfig.FTPUserNo, SystemConfig.FTPUserPassword);

			if (ftpconn == null)
			{
				return "err@FTP服务器连接失败";
			}

			String ny = LocalDateTime.now().toString("yyyy_MM");

			//判断目录年月是否存在.
			if (ftpconn.DirectoryExist(ny) == false)
			{
				ftpconn.CreateDirectory(ny);
			}
			ftpconn.SetCurrentDirectory(ny);

			//判断目录是否存在.
			if (ftpconn.DirectoryExist("Helper") == false)
			{
				ftpconn.CreateDirectory("Helper");
			}

			//设置当前目录，为操作的目录。
			ftpconn.SetCurrentDirectory("Helper");

			//把文件放上去.
			ftpconn.PutFile(temp, guid + ext);
			ftpconn.Close();

			//删除临时文件
			(new File(temp)).delete();

			//设置路径.
			filepath = ny + "//Helper//" + guid + ext;

		}
		else
		{

			String savePath = BP.Sys.SystemConfig.PathOfDataUser + enName + this.getPKVal();

			if ((new File(savePath)).isDirectory() == false)
			{
				(new File(savePath)).mkdirs();
			}
			filepath = savePath + "\\" + fileName + ext;
			//存在文件则删除
			if ((new File(filepath)).isDirectory() == true)
			{
				(new File(filepath)).delete();
			}

			File info = new File(filepath);

			//files[0].SaveAs(filepath);
			HttpContextHelper.UploadFile(files[0], filepath);
		}
		//保存上传的文件
		SysFileManager fileManager = new SysFileManager();
		fileManager.AttrFileNo = this.GetRequestVal("FileNo");
		fileManager.AttrFileName = HttpUtility.UrlDecode(this.GetRequestVal("FileName"), System.Text.Encoding.UTF8);
		fileManager.EnName = this.getEnName();
		fileManager.RefVal = this.getPKVal();
		fileManager.MyFileName = fileName;
		fileManager.MyFilePath = filepath;
		fileManager.MyFileExt = ext;
		fileManager.MyFileSize = size;
		fileManager.WebPath = filepath;
		fileManager.Insert();
		return fileManager.ToJson();
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 分组统计.
	/** 
	 获得分组统计的查询条件.
	 
	 @return 
	*/
	public final String Group_MapBaseInfo()
	{
		//获得
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		if (ens == null)
		{
			return "err@类名:" + this.getEnsName() + "错误";
		}

		Entity en = ens.GetNewEntity;
		Map map = ens.GetNewEntity.EnMapInTime;

		Hashtable ht = new Hashtable();

		//把权限信息放入.
		UAC uac = en.HisUAC;
		if (this.GetRequestValBoolen("IsReadonly"))
		{
			ht.put("IsUpdata", false);

			ht.put("IsInsert", false);
			ht.put("IsDelete", false);
		}
		else
		{
			ht.put("IsUpdata", uac.IsUpdate);

			ht.put("IsInsert", uac.IsInsert);
			ht.put("IsDelete", uac.IsDelete);
			ht.put("IsView", uac.IsView);
		}

		ht.put("IsExp", uac.IsExp); //是否可以导出?
		ht.put("IsImp", uac.IsImp); //是否可以导入?

		ht.put("EnDesc", en.EnDesc); //描述?
		ht.put("EnName", en.toString()); //类名?


		//把map信息放入
		ht.put("PhysicsTable", map.PhysicsTable);
		ht.put("CodeStruct", map.CodeStruct);
		ht.put("CodeLength", map.CodeLength);

		//查询条件.
		if (map.IsShowSearchKey == true)
		{
			ht.put("IsShowSearchKey", 1);
		}
		else
		{
			ht.put("IsShowSearchKey", 0);
		}

		//按日期查询.
		ht.put("DTSearchWay", (int)map.DTSearchWay);
		ht.put("DTSearchLable", map.DTSearchLable);
		ht.put("DTSearchKey", map.DTSearchKey);

		return BP.Tools.Json.ToJson(ht);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 外键或者枚举的分组查询条件.
	 
	 @return 
	*/
	public final String Group_SearchAttrs()
	{
		//获得
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		if (ens == null)
		{
			return "err@类名错误:" + this.getEnsName();
		}

		Entity en = ens.GetNewEntity;
		Map map = ens.GetNewEntity.EnMapInTime;

		DataSet ds = new DataSet();

		//构造查询条件集合.
		DataTable dt = new DataTable();
		dt.Columns.Add("Field");
		dt.Columns.Add("Name");
		dt.Columns.Add("MyFieldType");
		dt.TableName = "Attrs";

		AttrSearchs attrs = map.SearchAttrs;
		for (AttrSearch item : attrs)
		{
			DataRow dr = dt.NewRow();
			dr.set("Field", item.Key);
			dr.set("Name", item.HisAttr.Desc);
			dr.set("MyFieldType", item.HisAttr.MyFieldType);
			dt.Rows.Add(dr);
		}
		ds.Tables.Add(dt);

		//把外键枚举增加到里面.
		for (AttrSearch item : attrs)
		{
			if (item.HisAttr.IsEnum == true)
			{
				SysEnums ses = new SysEnums(item.HisAttr.UIBindKey);
				DataTable dtEnum = ses.ToDataTableField();
				dtEnum.TableName = item.Key;
				ds.Tables.Add(dtEnum);
				continue;
			}

			if (item.HisAttr.IsFK == true)
			{
				Entities ensFK = item.HisAttr.HisFKEns;
				ensFK.RetrieveAll();

				DataTable dtEn = ensFK.ToDataTableField();
				dtEn.TableName = item.Key;

				ds.Tables.Add(dtEn);
			}
		}

		return BP.Tools.Json.ToJson(ds);
	}

	/** 
	获取分组的外键、枚举
	 
	 @return 
	*/
	public final String Group_ContentAttrs()
	{
		//获得
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		if (ens == null)
		{
			return "err@类名错误:" + this.getEnsName();
		}

		Entity en = ens.GetNewEntity;
		Map map = ens.GetNewEntity.EnMapInTime;
		Attrs attrs = map.Attrs;
		DataTable dt = new DataTable();
		dt.Columns.Add("Field");
		dt.Columns.Add("Name");
		dt.Columns.Add("Checked");
		dt.TableName = "Attrs";

		//获取注册信心表
		UserRegedit ur = new UserRegedit(WebUser.No, this.getEnsName() + "_Group");
		String reAttrs = this.GetRequestVal("Attrs");

		//判断是否已经选择分组
		boolean contentFlag = false;
		for (Attr attr : attrs)
		{
			if (attr.UIContralType == UIContralType.DDL)
			{
				DataRow dr = dt.NewRow();
				dr.set("Field", attr.Key);
				dr.set("Name", attr.Desc);

				// 根据状态 设置信息.
				if (ur.Vals.indexOf(attr.Key) != -1)
				{
					dr.set("Checked", "true");
					contentFlag = true;
				}
				dt.Rows.Add(dr);
			}

		}

		if (contentFlag == false && dt.Rows.size() != 0)
		{
			dt.Rows[0]["Checked"] = "true";
		}

		return BP.Tools.Json.ToJson(dt);
	}

	public final String Group_Analysis()
	{
		//获得
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		if (ens == null)
		{
			return "err@类名错误:" + this.getEnsName();
		}

		Entity en = ens.GetNewEntity;
		Map map = ens.GetNewEntity.EnMapInTime;
		DataSet ds = new DataSet();
		//// 查询出来关于它的活动列配置。
		//ActiveAttrs aas = new ActiveAttrs();
		//aas.RetrieveBy(ActiveAttrAttr.For, this.EnsName);

		//获取注册信息表
		UserRegedit ur = new UserRegedit(WebUser.No, this.getEnsName() + "_Group");

		DataTable dt = new DataTable();
		dt.Columns.Add("Field");
		dt.Columns.Add("Name");
		dt.Columns.Add("Checked");

		dt.TableName = "Attrs";

		//如果不存在分析项手动添加一个分析项
		DataRow dtr = dt.NewRow();
		dtr.set("Field", "Group_Number");
		dtr.set("Name", "数量");
		dtr.set("Checked", "true");
		dt.Rows.Add(dtr);

		DataTable ddlDt = new DataTable();
		ddlDt.TableName = "Group_Number";
		ddlDt.Columns.Add("No");
		ddlDt.Columns.Add("Name");
		ddlDt.Columns.Add("Selected");
		DataRow ddlDr = ddlDt.NewRow();
		ddlDr.set("No", "SUM");
		ddlDr.set("Name", "求和");
		ddlDr.set("Selected", "true");
		ddlDt.Rows.Add(ddlDr);
		ds.Tables.Add(ddlDt);

		for (Attr attr : map.Attrs)
		{
			if (attr.IsPK || attr.IsNum == false)
			{
				continue;
			}
			if (attr.UIContralType == UIContralType.TB == false)
			{
				continue;
			}
			if (attr.UIVisible == false)
			{
				continue;
			}
			if (attr.MyFieldType == FieldType.FK)
			{
				continue;
			}
			if (attr.MyFieldType == FieldType.Enum)
			{
				continue;
			}
			if (attr.Key.equals("OID") || attr.Key.equals("WorkID") || attr.Key.equals("MID"))
			{
				continue;
			}


			//bool isHave = false;
			//// 有没有配置抵消它的属性。
			//foreach (ActiveAttr aa in aas)
			//{
			//    if (aa.AttrKey != attr.Key)
			//        continue;
			//    DataRow dr = dt.NewRow();
			//    dr["Field"] = attr.Key;
			//    dr["Name"] = attr.Desc;

			//    // 根据状态 设置信息.
			//    if (ur.Vals.IndexOf(attr.Key) != -1)
			//        dr["Checked"] = "true";

			//    dt.Rows.Add(dr);

			//    isHave = true;
			//}

			//if (isHave)
			//    continue;


			dtr = dt.NewRow();
			dtr.set("Field", attr.Key);
			dtr.set("Name", attr.Desc);


			// 根据状态 设置信息.
			if (ur.Vals.indexOf(attr.Key) != -1)
			{
				dtr.set("Checked", "true");
			}

			dt.Rows.Add(dtr);

			ddlDt = new DataTable();
			ddlDt.Columns.Add("No");
			ddlDt.Columns.Add("Name");
			ddlDt.Columns.Add("Selected");
			ddlDt.TableName = attr.Key;

			ddlDr = ddlDt.NewRow();
			ddlDr.set("No", "SUM");
			ddlDr.set("Name", "求和");
			if (ur.Vals.indexOf("@" + attr.Key + "=SUM") != -1)
			{
				ddlDr.set("Selected", "true");
			}
			ddlDt.Rows.Add(ddlDr);

			ddlDr = ddlDt.NewRow();
			ddlDr.set("No", "AVG");
			ddlDr.set("Name", "求平均");
			if (ur.Vals.indexOf("@" + attr.Key + "=AVG") != -1)
			{
				ddlDr.set("Selected", "true");
			}
			ddlDt.Rows.Add(ddlDr);

			if (this.getIsContainsNDYF())
			{
				ddlDr = ddlDt.NewRow();
				ddlDr.set("No", "AMOUNT");
				ddlDr.set("Name", "求累计");
				if (ur.Vals.indexOf("@" + attr.Key + "=AMOUNT") != -1)
				{
					ddlDr.set("Selected", "true");
				}
				ddlDt.Rows.Add(ddlDr);
			}

			ds.Tables.Add(ddlDt);


		}
		ds.Tables.Add(dt);
		return BP.Tools.Json.ToJson(ds);
	}

	public final String Group_Search()
	{
		//获得
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		if (ens == null)
		{
			return "err@类名错误:" + this.getEnsName();
		}

		Entity en = ens.GetNewEntity;
		Map map = ens.GetNewEntity.EnMapInTime;
		DataSet ds = new DataSet();

		//获取注册信息表
		UserRegedit ur = new UserRegedit(WebUser.No, this.getEnsName() + "_Group");

		// 查询出来关于它的活动列配置.
		ActiveAttrs aas = new ActiveAttrs();
		aas.RetrieveBy(ActiveAttrAttr.For, this.getEnsName());

		ds = GroupSearchSet(ens, en, map, ur, ds, aas);
		if (ds == null)
		{
			return "info@<img src='../Img/Warning.gif' /><b><font color=red> 您没有选择显示内容/分析项目</font></b>";
		}

		//不显示合计列。
		String NoShowSum = SystemConfig.GetConfigXmlEns("NoShowSum", this.getEnsName());
		DataTable showSum = new DataTable("NoShowSum");
		showSum.Columns.Add("NoShowSum");
		DataRow sumdr = showSum.NewRow();
		sumdr.set("NoShowSum", NoShowSum);
		showSum.Rows.Add(sumdr);

		DataTable activeAttr = aas.ToDataTable();
		activeAttr.TableName = "ActiveAttr";
		ds.Tables.Add(activeAttr);
		ds.Tables.Add(showSum);

		return BP.Tools.Json.ToJson(ds);
	}

	private DataSet GroupSearchSet(Entities ens, Entity en, Map map, UserRegedit ur, DataSet ds, ActiveAttrs aas)
	{

		//查询条件
		//分组
		String groupKey = "";
		Attrs AttrsOfNum = new Attrs(); //列
		String Condition = ""; //处理特殊字段的条件问题。


		//根据注册表信息获取里面的分组信息
		String StateNumKey = ur.Vals.substring(ur.Vals.indexOf("@StateNumKey") + 1);

		String[] statNumKeys = StateNumKey.split("[@]", -1);
		for (String ct : statNumKeys)
		{
			if (ct.split("[=]", -1).length != 2)
			{
				continue;
			}
			String[] paras = ct.split("[=]", -1);

			//判断paras[0]的类型
			int dataType = 2;
			if (paras[0].equals("Group_Number"))
			{
				AttrsOfNum.Add(new Attr("Group_Number", "Group_Number", 1, DataType.AppInt, false, "数量"));
			}
			else
			{
				Attr attr = map.GetAttrByKey(paras[0]);
				AttrsOfNum.Add(attr);
				dataType = attr.MyDataType;
			}

			if (this.GetRequestVal("DDL_" + paras[0]) == null)
			{
				ActiveAttr aa = (ActiveAttr)aas.GetEnByKey(ActiveAttrAttr.AttrKey, paras[0]);
				if (aa == null)
				{
					continue;
				}

				Condition += aa.Condition;
				if (SystemConfig.AppCenterDBType == DBType.PostgreSQL)
				{
					groupKey += " round ( cast (" + aa.Exp + " as  numeric), 4)  \"" + paras[0] + "\",";
				}
				else
				{
					groupKey += " round (" + aa.Exp + ", 4)  \"" + paras[0] + "\",";
				}
				StateNumKey += paras[0] + "=Checked@"; // 记录状态
				continue;
			}

			if (paras[0].equals("Group_Number"))
			{
				groupKey += " count(*) \"" + paras[0] + "\",";
			}
			else
			{
				switch (paras[1])
				{
					case "SUM":
						if (dataType == 2)
						{
							groupKey += " SUM(" + paras[0] + ") \"" + paras[0] + "\",";
						}
						else
						{
							if (SystemConfig.AppCenterDBType == DBType.PostgreSQL)
							{
								groupKey += " round ( cast (SUM(" + paras[0] + ") as  numeric), 4)  \"" + paras[0] + "\",";
							}
							else
							{
								groupKey += " round ( SUM(" + paras[0] + "), 4) \"" + paras[0] + "\",";
							}
						}

						break;
					case "AVG":
						if (SystemConfig.AppCenterDBType == DBType.PostgreSQL)
						{
							groupKey += " round ( cast (AVG(" + paras[0] + ") as  numeric), 4)  \"" + paras[0] + "\",";
						}
						else
						{
							groupKey += " round (AVG(" + paras[0] + "), 4)  \"" + paras[0] + "\",";
						}
						break;
					case "AMOUNT":
						if (dataType == 2)
						{
							groupKey += " SUM(" + paras[0] + ") \"" + paras[0] + "\",";
						}
						else
						{
							if (SystemConfig.AppCenterDBType == DBType.PostgreSQL)
							{
								groupKey += " round ( cast (SUM(" + paras[0] + ") as  numeric), 4)  \"" + paras[0] + "\",";
							}
							else
							{
								groupKey += " round ( SUM(" + paras[0] + "), 4) \"" + paras[0] + "\",";
							}
						}

						break;
					default:
						throw new RuntimeException("没有判断的情况.");
				}

			}

		}
		boolean isHaveLJ = false; // 是否有累计字段。
		if (StateNumKey.indexOf("AMOUNT@") != -1)
		{
			isHaveLJ = true;
		}


		if (groupKey.equals(""))
		{
			return null;
		}

		/* 如果包含累计数据，那它一定需要一个月份字段。业务逻辑错误。*/
		groupKey = groupKey.substring(0, groupKey.length() - 1);
		BP.DA.Paras ps = new Paras();
		// 生成 sql.
		String selectSQL = "SELECT ";
		String groupBy = " GROUP BY ";
		Attrs AttrsOfGroup = new Attrs();

		String SelectedGroupKey = ur.Vals.substring(0, ur.Vals.indexOf("@StateNumKey")); // 为保存操作状态的需要。
		if (!DataType.IsNullOrEmpty(SelectedGroupKey))
		{
			boolean isSelected = false;
			String[] SelectedGroupKeys = SelectedGroupKey.split("[@]", -1);
			for (String key : SelectedGroupKeys)
			{
				if (key.contains("=") == true)
				{
					continue;
				}
				//if (key.Equals("Group_Number"))

				selectSQL += key + " \"" + key + "\",";
				groupBy += key + ",";
				// 加入组里面。
				AttrsOfGroup.Add(map.GetAttrByKey(key), false, false);

			}
		}

		String groupList = this.GetRequestVal("GroupList");
		if (!DataType.IsNullOrEmpty(SelectedGroupKey))
		{
			/* 如果是年月 分组， 并且如果内部有 累计属性，就强制选择。*/
			if (groupList.indexOf("FK_NY") != -1 && isHaveLJ)
			{
				selectSQL += "FK_NY,";
				groupBy += "FK_NY,";
				SelectedGroupKey += "@FK_NY";
				// 加入组里面。
				AttrsOfGroup.Add(map.GetAttrByKey("FK_NY"), false, false);
			}
		}

		groupBy = groupBy.substring(0, groupBy.length() - 1);

		if (groupBy.trim().equals("GROUP BY"))
		{
			return null;
		}

		// 查询语句的生成
		String where = " WHERE ";
		String whereOfLJ = " WHERE "; // 累计的where.
		String url = "";
		Paras myps = new Paras();
		//获取查询的注册表
		BP.Sys.UserRegedit searchUr = new UserRegedit();
		searchUr.MyPK = WebUser.No + "_" + this.getEnsName() + "_SearchAttrs";
		searchUr.RetrieveFromDBSources();

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 查询条件
		//关键字查询
		String keyWord = searchUr.SearchKey;
		AtPara ap = new AtPara(searchUr.Vals);
		if (en.EnMap.IsShowSearchKey && DataType.IsNullOrEmpty(keyWord) == false && keyWord.length() >= 1)
		{
			String whereLike = "";
			boolean isAddAnd = false;
			for (Attr attr : map.Attrs)
			{
				if (attr.IsNum)
				{
					continue;
				}
				if (attr.IsRefAttr)
				{
					continue;
				}

				switch (attr.Field)
				{
					case "MyFileExt":
					case "MyFilePath":
					case "WebPath":
						continue;
					default:
						break;
				}
				if (isAddAnd == false)
				{
					isAddAnd = true;
					whereLike += "      " + attr.Field + " LIKE '%" + keyWord + "%' ";
				}
				else
				{
					whereLike += "   AND   " + attr.Field + " LIKE '%" + keyWord + "%'";
				}
			}
			whereLike += "          ";
			where += whereLike;
		}

		//其余查询条件
		//时间
		if (map.DTSearchWay != DTSearchWay.None && DataType.IsNullOrEmpty(ur.DTFrom) == false)
		{
			String dtFrom = ur.DTFrom; // this.GetTBByID("TB_S_From").Text.Trim().Replace("/", "-");
			String dtTo = ur.DTTo; // this.GetTBByID("TB_S_To").Text.Trim().Replace("/", "-");

			//按日期查询
			if (map.DTSearchWay == DTSearchWay.ByDate)
			{

				dtTo += " 23:59:59";
				where += " and (" + map.DTSearchKey + " >= '" + dtFrom + "'";
				where += " and " + map.DTSearchKey + " <= '" + dtTo + "'";
				where += ")";
			}

			if (map.DTSearchWay == DTSearchWay.ByDateTime)
			{
				//取前一天的24：00
				if (dtFrom.trim().length() == 10) //2017-09-30
				{
					dtFrom += " 00:00:00";
				}
				if (dtFrom.trim().length() == 16) //2017-09-30 00:00
				{
					dtFrom += ":00";
				}

				dtFrom = LocalDateTime.parse(dtFrom).AddDays(-1).toString("yyyy-MM-dd") + " 24:00";

				if (dtTo.trim().length() < 11 || dtTo.trim().indexOf(' ') == -1)
				{
					dtTo += " 24:00";
				}

				where += " and (" + map.DTSearchKey + " >= '" + dtFrom + "'";
				where += " and " + map.DTSearchKey + " <= '" + dtTo + "'";
				where += ")";
			}
		}
		/** #region 获得查询数据.
		*/
		for (String str : ap.HisHT.keySet())
		{
			Object val = ap.GetValStrByKey(str);
			if (val.equals("all"))
			{
				continue;
			}
			where += " " + str + "=" + SystemConfig.AppCenterDBVarStr + str + "   AND ";
			if (!str.equals("FK_NY"))
			{
				whereOfLJ += " " + str + " =" + SystemConfig.AppCenterDBVarStr + str + "   AND ";
			}

			myps.Add(str, val);

		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

		if (where.equals(" WHERE "))
		{
			where = "" + Condition.replace("and", "");
			whereOfLJ = "" + Condition.replace("and", "");
		}
		else
		{
			where = where.substring(0, where.length() - " AND ".length()) + Condition;
			whereOfLJ = whereOfLJ.substring(0, whereOfLJ.length() - " AND ".length()) + Condition;
		}

		String orderByReq = this.GetRequestVal("OrderBy");

		String orderby = "";

		if (orderByReq != null && (selectSQL.contains(orderByReq) || groupKey.contains(orderByReq)))
		{
			orderby = " ORDER BY " + orderByReq;
			String orderWay = this.GetRequestVal("OrderWay");
			if (!DataType.IsNullOrEmpty(orderWay) && !orderWay.equals("Up"))
			{
				orderby += " DESC ";
			}
		}

		// 组装成需要的 sql 
		String sql = selectSQL + groupKey + " FROM " + map.PhysicsTable + where + groupBy + orderby;


		myps.SQL = sql;
		//  物理表。

		DataTable dt2 = DBAccess.RunSQLReturnTable(myps);

		DataTable dt1 = dt2.Clone();

		dt1.Columns.Add("IDX", Integer.class);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 对他进行分页面

		int myIdx = 0;
		for (DataRow dr : dt2.Rows)
		{
			myIdx++;
			DataRow mydr = dt1.NewRow();
			mydr.set("IDX", myIdx);
			for (DataColumn dc : dt2.Columns)
			{
				mydr.set(dc.ColumnName, dr.get(dc.ColumnName));
			}
			dt1.Rows.Add(mydr);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#region 处理 Int 类型的分组列。
		DataTable dt = dt1.Clone();
		dt.TableName = "GroupSearch";
		dt.Rows.Clear();
		for (Attr attr : AttrsOfGroup)
		{
			dt.Columns[attr.Key].DataType = String.class;
		}
		for (DataRow dr : dt1.Rows)
		{
			dt.ImportRow(dr);
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
			///#endregion

		// 处理这个物理表 , 如果有累计字段, 就扩展它的列。
		if (isHaveLJ)
		{
			// 首先扩充列.
			for (Attr attr : AttrsOfNum)
			{
				if (StateNumKey.indexOf(attr.Key + "=AMOUNT") == -1)
				{
					continue;
				}

				switch (attr.MyDataType)
				{
					case DataType.AppInt:
						dt.Columns.Add(attr.Key + "Amount", Integer.class);
						break;
					default:
						dt.Columns.Add(attr.Key + "Amount", BigDecimal.class);
						break;
				}
			}

			// 添加累计汇总数据.
			for (DataRow dr : dt.Rows)
			{
				for (Attr attr : AttrsOfNum)
				{
					if (StateNumKey.indexOf(attr.Key + "=AMOUNT") == -1)
					{
						continue;
					}

					//形成查询sql.
					if (whereOfLJ.length() > 10)
					{
						sql = "SELECT SUM(" + attr.Key + ") FROM " + map.PhysicsTable + whereOfLJ + " AND ";
					}
					else
					{
						sql = "SELECT SUM(" + attr.Key + ") FROM " + map.PhysicsTable + " WHERE ";
					}

					for (Attr attr1 : AttrsOfGroup)
					{
						switch (attr1.Key)
						{
							case "FK_NY":
								sql += " FK_NY <= '" + dr.get("FK_NY") + "' AND FK_ND='" + dr.get("FK_NY").toString().substring(0, 4) + "' AND ";
								break;
							case "FK_Dept":
								sql += attr1.Key + "='" + dr.get(attr1.Key) + "' AND ";
								break;
							case "FK_SJ":
							case "FK_XJ":
								sql += attr1.Key + " LIKE '" + dr.get(attr1.Key) + "%' AND ";
								break;
							default:
								sql += attr1.Key + "='" + dr.get(attr1.Key) + "' AND ";
								break;
						}
					}

					sql = sql.substring(0, sql.length() - "AND ".length());
					if (attr.MyDataType == DataType.AppInt)
					{
						dr.set(attr.Key + "Amount", DBAccess.RunSQLReturnValInt(sql, 0));
					}
					else
					{
						dr.set(attr.Key + "Amount", DBAccess.RunSQLReturnValDecimal(sql, 0, 2));
					}
				}
			}
		}

		// 为表扩充外键
		for (Attr attr : AttrsOfGroup)
		{
			dt.Columns.Add(attr.Key + "T", String.class);
		}
		for (Attr attr : AttrsOfGroup)
		{
			if (attr.UIBindKey.indexOf(".") == -1)
			{
				/* 说明它是枚举类型 */
				SysEnums ses = new SysEnums(attr.UIBindKey);
				for (DataRow dr : dt.Rows)
				{
					int val = 0;
					try
					{
						val = Integer.parseInt(dr.get(attr.Key).toString());
					}
					catch (java.lang.Exception e)
					{
						dr.set(attr.Key + "T", " ");
						continue;
					}

					for (SysEnum se : ses)
					{
						if (se.IntKey == val)
						{
							dr.set(attr.Key + "T", se.Lab);
						}
					}
				}
				continue;
			}
			for (DataRow dr : dt.Rows)
			{
				Entity myen = attr.HisFKEn;
				String val = dr.get(attr.Key).toString();
				myen.SetValByKey(attr.UIRefKeyValue, val);
				try
				{
					myen.Retrieve();
					dr.set(attr.Key + "T", myen.GetValStrByKey(attr.UIRefKeyText));
				}
				catch (java.lang.Exception e2)
				{
					if (val == null || val.length() <= 1)
					{
						dr.set(attr.Key + "T", val);
					}
					else if (val.substring(0, 2).equals("63"))
					{
						try
						{
							BP.Port.Dept Dept = new BP.Port.Dept(val);
							dr.set(attr.Key + "T", Dept.Name);
						}
						catch (java.lang.Exception e3)
						{
							dr.set(attr.Key + "T", val);
						}
					}
					else
					{
						dr.set(attr.Key + "T", val);
					}
				}
			}
		}
		ds.Tables.Add(dt);
		ds.Tables.Add(AttrsOfNum.ToMapAttrs.ToDataTableField("AttrsOfNum"));
		ds.Tables.Add(AttrsOfGroup.ToMapAttrs.ToDataTableField("AttrsOfGroup"));
		return ds;
	}
	public final String ParseExpToDecimal()
	{
		String exp = this.GetRequestVal("Exp");

		BigDecimal d = DataType.ParseExpToDecimal(exp);
		return d.toString();
	}

	/** 
	 执行导出
	 
	 @return 
	*/
	public final String Group_Exp()
	{
		//获得
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		if (ens == null)
		{
			return "err@类名错误:" + this.getEnsName();
		}

		Entity en = ens.GetNewEntity;
		Map map = ens.GetNewEntity.EnMapInTime;
		DataSet ds = new DataSet();

		//获取注册信息表
		UserRegedit ur = new UserRegedit(WebUser.No, this.getEnsName() + "_Group");

		// 查询出来关于它的活动列配置.
		ActiveAttrs aas = new ActiveAttrs();
		aas.RetrieveBy(ActiveAttrAttr.For, this.getEnsName());

		ds = GroupSearchSet(ens, en, map, ur, ds, aas);
		if (ds == null)
		{
			return "info@<img src='../Img/Warning.gif' /><b><font color=red> 您没有选择分析的数据</font></b>";
		}

		String filePath = ExportGroupExcel(ds, en.EnDesc, ur.Vals);


		return filePath;
	}

	public final boolean getIsContainsNDYF()
	{
		if (this.GetValFromFrmByKey("IsContainsNDYF").toString().toUpperCase().equals("TRUE"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 常用词汇功能开始
	/** 
	 常用词汇
	 
	 @return 
	*/
	public final String HelperWordsData()
	{

		String FK_MapData = this.GetRequestVal("FK_MapData");
		String AttrKey = this.GetRequestVal("AttrKey");
		String lb = this.GetRequestVal("lb");

		//读取txt文件
		if (lb.equals("readWords"))
		{
			return readTxt();
		}

		//读取其他常用词汇
		DataSet ds = new DataSet();
		//我的词汇
		if (lb.equals("myWords"))
		{
			DefVals dvs = new DefVals();
			QueryObject qo = new QueryObject(dvs);
			qo.AddHD();

			qo.addAnd();
			qo.AddWhere("FK_MapData", "=", FK_MapData);
			qo.addAnd();
			qo.AddWhere("AttrKey", "=", AttrKey);
			qo.addAnd();
			qo.AddWhere("FK_Emp", "=", WebUser.No);
			qo.addAnd();
			qo.AddWhere("LB", "=", "1");

			String pageNumber = GetRequestVal("pageNumber");
			int iPageNumber = DataType.IsNullOrEmpty(pageNumber) ? 1 : Integer.parseInt(pageNumber);
			//每页多少行
			String pageSize = GetRequestVal("pageSize");
			int iPageSize = DataType.IsNullOrEmpty(pageSize) ? 9999 : Integer.parseInt(pageSize);

			DataTable dt = new DataTable("DataCount");
			dt.Columns.Add("DataCount", Integer.class);
			DataRow dr = dt.NewRow();
			dr.set("DataCount", qo.GetCount());
			dt.Rows.Add(dr);
			ds.Tables.Add(dt);

			qo.DoQuery("MyPK", iPageSize, iPageNumber);

			ds.Tables.Add(dvs.ToDataTableField("MainTable")); //把描述加入.
		}
		if (lb.equals("hisWords"))
		{
			Node nd = new Node(this.getFK_Node());
			String rptNo = "ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt";
			if (nd.getHisFormType() == NodeFormType.SheetTree || nd.getHisFormType() == NodeFormType.RefOneFrmTree)
			{
				MapData mapData = new MapData(this.getFK_MapData());
				rptNo = mapData.PTable;
			}


			GEEntitys ges = new GEEntitys(rptNo);
			QueryObject qo = new QueryObject(ges);
			String fk_emp = this.GetRequestVal("FK_Emp");
			qo.AddWhere(fk_emp, "=", WebUser.No);
			qo.addAnd();
			qo.AddWhere(AttrKey, "!=", "");
			String pageNumber = GetRequestVal("pageNumber");
			int iPageNumber = DataType.IsNullOrEmpty(pageNumber) ? 1 : Integer.parseInt(pageNumber);
			//每页多少行
			String pageSize = GetRequestVal("pageSize");
			int iPageSize = DataType.IsNullOrEmpty(pageSize) ? 9999 : Integer.parseInt(pageSize);

			DataTable dt = new DataTable("DataCount");
			dt.Columns.Add("DataCount", Integer.class);
			DataRow dr = dt.NewRow();
			dr.set("DataCount", qo.GetCount());
			dt.Rows.Add(dr);
			ds.Tables.Add(dt);

			qo.DoQuery("OID", iPageSize, iPageNumber);

			dt = ges.ToDataTableField();
			DataTable newDt = new DataTable("MainTable");
			newDt.Columns.Add("CurValue");
			newDt.Columns.Add("MyPk");
			for (DataRow drs : dt.Rows)
			{
				if (DataType.IsNullOrEmpty(drs.get(AttrKey).toString()))
				{
					continue;
				}
				dr = newDt.NewRow();
				dr.set("CurValue", drs.get(AttrKey));
				dr.set("MyPK", drs.get("OID"));
				newDt.Rows.Add(dr);
			}

			ds.Tables.Add(newDt); //把描述加入.


		}
		return BP.Tools.Json.ToJson(ds);


	}

	/** 
	 注意特殊字符的处理
	 
	 @return 
	*/
	private String readTxt()
	{
		try
		{
			String path = BP.Sys.SystemConfig.PathOfDataUser + "Fastenter\\" + getFK_MapData() + "\\" + GetRequestVal("AttrKey");
			;
			if (!(new File(path)).isDirectory())
			{
				(new File(path)).mkdirs();
			}

			String[] folderArray = (new File(path)).list(File::isFile);
			if (folderArray.length == 0)
			{
				return "";
			}

			String fileName;
			String[] strArray;

			String pageNumber = GetRequestVal("pageNumber");
			int iPageNumber = DataType.IsNullOrEmpty(pageNumber) ? 1 : Integer.parseInt(pageNumber);
			String pageSize = GetRequestVal("pageSize");
			int iPageSize = DataType.IsNullOrEmpty(pageSize) ? 9999 : Integer.parseInt(pageSize);

			DataSet ds = new DataSet();
			DataTable dt = new DataTable("MainTable");
			dt.Columns.Add("MyPk", String.class);
			dt.Columns.Add("TxtStr", String.class);
			dt.Columns.Add("CurValue", String.class);

			String liStr = "";
			int count = 0;
			int index = iPageSize * (iPageNumber - 1);
			for (String folder : folderArray)
			{
				dt.Rows.Add("", "", "");
				if (count >= index && count < iPageSize * iPageNumber)
				{
					dt.Rows[count]["MyPk"] = BP.DA.DBAccess.GenerGUID();

					strArray = folder.split("[\\\\]", -1);
					fileName = strArray[strArray.length - 1].replace("\"", "").replace("'", "");
					liStr += String.format("{id:\"%1$s\",value:\"%2$s\"},", DataTableConvertJson.GetFilteredStrForJSON(fileName, true), DataTableConvertJson.GetFilteredStrForJSON(Files.readString(folder,), false));

					dt.Rows[count]["CurValue"] = DataTableConvertJson.GetFilteredStrForJSON(fileName, true);
					dt.Rows[count]["TxtStr"] = DataTableConvertJson.GetFilteredStrForJSON(Files.readString(folder,), false);
				}
				count += 1;
			}

			ds.Tables.Add(dt);
			dt = new DataTable("DataCount");
			dt.Columns.Add("DataCount", Integer.class);
			DataRow dr = dt.NewRow();
			dr.set("DataCount", folderArray.length);
			dt.Rows.Add(dr);
			ds.Tables.Add(dt);
			return BP.Tools.Json.ToJson(ds);
		}
		catch (RuntimeException e)
		{
			return "";
		}
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 常用词汇结束

}