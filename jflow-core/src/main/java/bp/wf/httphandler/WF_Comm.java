package bp.wf.httphandler;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.difference.handler.WebContralBase;
import bp.sys.*;
import bp.sys.xml.ActiveAttr;
import bp.sys.xml.ActiveAttrAttr;
import bp.sys.xml.ActiveAttrs;
import bp.tools.DataTableConvertJson;
import bp.tools.DateUtils;
import bp.web.*;
import bp.en.*;
import bp.en.Map;
import bp.wf.*;
import bp.wf.Glo;
import bp.wf.port.WFEmp;
import java.util.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.math.*;

/** 
 页面功能实体
*/
public class WF_Comm extends WebContralBase
{

		///树的实体.
	/** 
	 获得树的结构
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Tree_Init() throws Exception
	{
		Object tempVar = ClassFactory.GetEns(this.getEnsName());
		EntitiesTree ens = tempVar instanceof EntitiesTree ? (EntitiesTree)tempVar : null;
		if (ens == null)
		{
			return "err@该实体[" + this.getEnsName() + "]不是一个树形实体.";
		}
		//获取ParentNo
		 ens.RetrieveAll(EntityTreeAttr.Idx);

		return bp.tools.Json.ToJson(ens.ToDataTableField("TreeTable"));
	}

		/// 树的实体


		///部门-人员关系.

	public final String Tree_MapBaseInfo() throws Exception
	{
		Object tempVar = ClassFactory.GetEns(this.getTreeEnsName());
		EntitiesTree enTrees = tempVar instanceof EntitiesTree ? (EntitiesTree)tempVar : null;
		EntityTree enenTree = enTrees.getGetNewEntity() instanceof EntityTree ? (EntityTree)enTrees.getGetNewEntity() : null;
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		Entity en = ens.getGetNewEntity();
		Hashtable ht = new Hashtable();
		ht.put("TreeEnsDesc", enenTree.getEnDesc());
		ht.put("EnsDesc", en.getEnDesc());
		ht.put("EnPK", en.getPK());
		return bp.tools.Json.ToJson(ht);
	}

	/** 
	 获得树的结构
	 
	 @return 
	 * @throws Exception 
	*/
	public final String TreeEn_Init() throws Exception
	{
		Object tempVar = ClassFactory.GetEns(this.getTreeEnsName());
		EntitiesTree ens = tempVar instanceof EntitiesTree ? (EntitiesTree)tempVar : null;
		ens.RetrieveAll(EntityTreeAttr.Idx);
		return ens.ToJsonOfTree();
	}

	/** 
	 获取树关联的集合
	 
	 @return 
	 * @throws Exception 
	*/
	public final String TreeEmp_Init() throws Exception
	{
		DataSet ds = new DataSet();
		String RefPK = this.GetRequestVal("RefPK");
		String FK = this.GetRequestVal("FK");
		//获取关联的信息集合
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		ens.RetrieveByAttr(RefPK, FK);
		DataTable dt = ens.ToDataTableField("GridData");
		ds.Tables.add(dt);

		//获取实体对应的列明
		Entity en = ens.getGetNewEntity();
		Map map = en.getEnMapInTime();
		MapAttrs attrs = map.getAttrs().ToMapAttrs();
		//属性集合.
		DataTable dtAttrs = attrs.ToDataTableField();
		dtAttrs.TableName = "Sys_MapAttrs";

		dt = new DataTable("Sys_MapAttr");
		dt.Columns.Add("field", String.class);
		dt.Columns.Add("title", String.class);
		dt.Columns.Add("Width", Integer.class);
		dt.Columns.Add("UIContralType", Integer.class);
		DataRow row = null;
		for (MapAttr attr : attrs.ToJavaList())
		{
			if (attr.getUIVisible()== false)
			{
				continue;
			}

			if (this.getRefPK().equals(attr.getKeyOfEn()))
			{
				continue;
			}

			row = dt.NewRow();
			row.setValue("field", attr.getKeyOfEn());
			row.setValue("title", attr.getName());
			row.setValue("Width", attr.getUIWidthInt() * 2);
			row.setValue("UIContralType", attr.getUIContralType());

			if (attr.getHisAttr().getIsFKorEnum())
			{
				row.setValue("field", attr.getKeyOfEn() + "Text");
			}
			dt.Rows.add(row);
		}

		ds.Tables.add(dt);

		return bp.tools.Json.ToJson(ds);
	}

		/// 部门-人员关系
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
	public final void setFK_Dept(String value) throws Exception
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



		///统计分析组件.
	/** 
	 初始化数据
	 
	 @return 
	 * @throws Exception 
	*/
	public final String ContrastDtl_Init() throws Exception
	{
		//获得.
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		Entity en = ens.getGetNewEntity();
		Map map = en.getEnMapInTime();

		MapAttrs attrs = map.getAttrs().ToMapAttrs();

		//属性集合.
		DataTable dtAttrs = attrs.ToDataTableField();
		dtAttrs.TableName = "Sys_MapAttrs";

		DataSet ds = new DataSet();
		ds.Tables.add(dtAttrs); //把描述加入.

		//查询结果
		QueryObject qo = new QueryObject(ens);

		Enumeration enu = getRequest().getParameterNames();
		while (enu.hasMoreElements()) 
		{
			String key = (String) enu.nextElement();
			if (key.indexOf("EnsName") != -1)
			{
				continue;
			}

		
			if (key.equals("OID") || key.equals("MyPK"))
			{
				continue;
			}

			if (key.equals("FK_Dept"))
			{
				this.setFK_Dept(getRequest().getParameter(key));
				continue;
			}
			boolean isExist = false;
			boolean IsInt = false;
			boolean IsDouble = false;
			boolean IsFloat = false;
			boolean IsMoney = false;
			for (Attr attr : en.getEnMap().getAttrs())
			{
				if (attr.getKey().equals(key))
				{
					isExist = true;
					if (attr.getMyDataType() == DataType.AppInt)
					{
						IsInt = true;
					}
					if (attr.getMyDataType() == DataType.AppDouble)
					{
						IsDouble = true;
					}
					if (attr.getMyDataType() == DataType.AppFloat)
					{
						IsFloat = true;
					}
					if (attr.getMyDataType() == DataType.AppMoney)
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

			if (getRequest().getParameter(key).equals("mvals"))
			{
				//如果用户多项选择了，就要找到它的选择项目.

				UserRegedit sUr = new UserRegedit();
				sUr.setMyPK(WebUser.getNo() + this.getEnsName() + "_SearchAttrs");
				sUr.RetrieveFromDBSources();

				/* 如果是多选值 */
				String cfgVal = sUr.getMVals();
				AtPara ap = new AtPara(cfgVal);
				String instr = ap.GetValStrByKey(key);
				String val = "";
				if (instr == null || instr.equals(""))
				{
					if (key.equals("FK_Dept") || key.equals("FK_Unit"))
					{
						if (key.equals("FK_Dept"))
						{
							val = WebUser.getFK_Dept();
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
					qo.AddWhereIn(key, instr);
				}
			}
			else
			{
				String val = getRequest().getParameter(key);
				if (IsInt == true && DataType.IsNullOrEmpty(val) == false)
				{
					qo.AddWhere(key, Integer.parseInt(val));
				}
				else if (IsDouble == true && DataType.IsNullOrEmpty(val) == false)
				{
					qo.AddWhere(key, Double.parseDouble(val));
				}
				else if (IsFloat == true && DataType.IsNullOrEmpty(val) == false)
				{
					qo.AddWhere(key, Float.parseFloat(val));
				}
				else if (IsMoney == true && DataType.IsNullOrEmpty(val) == false)
				{
					qo.AddWhere(key, new BigDecimal(val));
				}
				else
				{
					qo.AddWhere(key, val);
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
		ds.Tables.add(dt);

		return bp.tools.Json.ToJson(ds);
	}

	/** 
	 执行导出
	 
	 @return 
	 * @throws Exception 
	*/
	public final String GroupDtl_Exp() throws Exception
	{
		//获得.
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		Entity en = ens.getGetNewEntity();

		//查询结果
		QueryObject qo = new QueryObject(ens);

		Enumeration enu = getRequest().getParameterNames();
		while (enu.hasMoreElements()) 
		{
			String key = (String) enu.nextElement();
			if (key.indexOf("EnsName") != -1)
			{
				continue;
			}

		
			if (key.equals("OID") || key.equals("MyPK"))
			{
				continue;
			}

			if (key.equals("FK_Dept"))
			{
				this.setFK_Dept(getRequest().getParameter(key));
				continue;
			}
			boolean isExist = false;
			boolean IsInt = false;
			boolean IsDouble = false;
			boolean IsFloat = false;
			boolean IsMoney = false;
			for (Attr attr : en.getEnMap().getAttrs())
			{
				if (attr.getKey().equals(key))
				{
					isExist = true;
					if (attr.getMyDataType() == DataType.AppInt)
					{
						IsInt = true;
					}
					if (attr.getMyDataType() == DataType.AppDouble)
					{
						IsDouble = true;
					}
					if (attr.getMyDataType() == DataType.AppFloat)
					{
						IsFloat = true;
					}
					if (attr.getMyDataType() == DataType.AppMoney)
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

			if (getRequest().getParameter(key).equals("mvals"))
			{
				//如果用户多项选择了，就要找到它的选择项目.

				UserRegedit sUr = new UserRegedit();
				sUr.setMyPK(WebUser.getNo() + this.getEnsName() + "_SearchAttrs");
				sUr.RetrieveFromDBSources();

				/* 如果是多选值 */
				String cfgVal = sUr.getMVals();
				AtPara ap = new AtPara(cfgVal);
				String instr = ap.GetValStrByKey(key);
				String val = "";
				if (instr == null || instr.equals(""))
				{
					if (key.equals("FK_Dept") || key.equals("FK_Unit"))
					{
						if (key.equals("FK_Dept"))
						{
							val = WebUser.getFK_Dept();
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
					qo.AddWhereIn(key, instr);
				}
			}
			else
			{
				String val = getRequest().getParameter(key);
				if (IsInt == true && DataType.IsNullOrEmpty(val) == false)
				{
					qo.AddWhere(key, Integer.parseInt(val));
				}
				else if (IsDouble == true && DataType.IsNullOrEmpty(val) == false)
				{
					qo.AddWhere(key, Double.parseDouble(val));
				}
				else if (IsFloat == true && DataType.IsNullOrEmpty(val) == false)
				{
					qo.AddWhere(key, Float.parseFloat(val));
				}
				else if (IsMoney == true && DataType.IsNullOrEmpty(val) == false)
				{
					qo.AddWhere(key, new BigDecimal(val));
				}
				else
				{
					qo.AddWhere(key, val);
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

		String filePath = ExportDGToExcel(dt, en, en.getEnDesc(), null);


		return filePath;
	}

		/// 统计分析组件.


		///Entity 公共类库.
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
	 * @throws Exception 
	*/
	public final String Entity_Init() throws Exception
	{
		try
		{
			String pkval = this.getPKVal();
			Entity en = ClassFactory.GetEn(this.getEnName());

			if (DataType.IsNullOrEmpty(pkval)== true ||pkval.equals("0")  || pkval.equals("undefined"))
			{
				Map map = en.getEnMap();
				for (Attr attr : en.getEnMap().getAttrs())
				{
					en.SetValByKey(attr.getKey(), attr.getDefaultVal());
				}
				//设置默认的数据.
				en.ResetDefaultVal();
			}
			else
			{
				en.setPKVal(pkval);
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
	 * @throws Exception 
	*/
	public final String Entity_Delete() throws Exception
	{
		try
		{
			Entity en = ClassFactory.GetEn(this.getEnName());


				///首先判断参数删除.
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

				/// 首先判断参数删除.

			if (en.getPKCount() != 1)
			{
				/*多个主键的情况. 遍历属性，循环赋值.*/
				for (Attr attr : en.getEnMap().getAttrs())
				{
					en.SetValByKey(attr.getKey(), this.GetRequestVal(attr.getKey()));
				}
			}
			else
			{
				en.setPKVal(this.getPKVal());
			}

			int i = en.RetrieveFromDBSources(); //查询出来再删除.
			return String.valueOf(en.Delete()); //返回影响行数.
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 更新
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Entity_Update() throws Exception
	{
		try
		{
			Entity en = ClassFactory.GetEn(this.getEnName());
			en.setPKVal(this.getPKVal());
			en.RetrieveFromDBSources();

			//遍历属性，循环赋值.
			for (Attr attr : en.getEnMap().getAttrs())
			{
				en.SetValByKey(attr.getKey(), this.GetRequestVal(attr.getKey()));
			}

			//返回数据.
			//return en.ToJson(false);

			return String.valueOf(en.Update()); //返回影响行数.
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}
	/** 
	 从数据源查询.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Entity_RetrieveFromDBSources() throws Exception
	{
		try
		{
			Entity en = ClassFactory.GetEn(this.getEnName());
			en.setPKVal(this.getPKVal());
			int i = en.RetrieveFromDBSources();

			if (i == 0)
			{
				en.ResetDefaultVal();
				en.setPKVal(this.getPKVal());
			}

			if (en.getRow().containsKey("RetrieveFromDBSources") == true)
			{
				en.getRow().SetValByKey("RetrieveFromDBSources",i);
			}
			else
			{
				en.getRow().put("RetrieveFromDBSources", i);
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
	 * @throws Exception 
	*/
	public final String Entity_Retrieve() throws Exception
	{
		try
		{
			Entity en = ClassFactory.GetEn(this.getEnName());
			en = en.CreateInstance();
			en.setPKVal(this.getPKVal());
			en.Retrieve();

			if (en.getRow().containsKey("Retrieve") == true)
			{
				en.getRow().SetValByKey("Retrieve","1");
			}
			else
			{
				en.getRow().put("Retrieve", "1");
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
	 * @throws Exception 
	*/
	public final String Entity_IsExits() throws Exception
	{
		try
		{
			Entity en = ClassFactory.GetEn(this.getEnName());
			en.setPKVal(this.getPKVal());
			boolean isExit = en.getIsExits();
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
	 * @throws Exception 
	*/
	public final String Entity_Save() throws Exception
	{
		try
		{
			Entity en = ClassFactory.GetEn(this.getEnName());
			if (en == null)
			{
				return "err@实体类名错误[" + this.getEnName() + "].";
			}

			en.setPKVal(this.getPKVal());
			en.RetrieveFromDBSources();

			//遍历属性，循环赋值.
			for (Attr attr : en.getEnMap().getAttrs())
			{
				en.SetValByKey(attr.getKey(), this.GetValFromFrmByKey(attr.getKey()));
			}

			//保存参数属性.
			String frmParas = this.GetValFromFrmByKey("frmParas", "");
			if (DataType.IsNullOrEmpty(frmParas) == false)
			{
				AtPara ap = new AtPara(frmParas);
				for (String key : ap.getHisHT().keySet())
				{
					en.SetPara(key, ap.GetValStrByKey(key));
				}
			}

			return String.valueOf(en.Save());
		}
		catch (RuntimeException ex)
		{
			return "err@保存错误:" + ex.getMessage();
		}
	}
	/** 
	 执行插入.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Entity_Insert() throws Exception
	{
		try
		{
			Entity en = ClassFactory.GetEn(this.getEnName());

			//遍历属性，循环赋值.
			for (Attr attr : en.getEnMap().getAttrs())
			{
				en.SetValByKey(attr.getKey(), this.GetRequestVal(attr.getKey()));
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
	 * @throws Exception 
	*/
	public final String Entity_DirectInsert() throws Exception
	{
		try
		{
			Entity en = ClassFactory.GetEn(this.getEnName());

			//遍历属性，循环赋值.
			for (Attr attr : en.getEnMap().getAttrs())
			{
				en.SetValByKey(attr.getKey(), this.GetRequestVal(attr.getKey()));
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
	 * @throws Exception 
	*/
	public final String Entity_DoMethodReturnString() throws Exception
	{
		//创建类实体.
		Entity en = ClassFactory.GetEn(this.getEnName());
		en.setPKVal(this.getPKVal());
		en.RetrieveFromDBSources();

		String methodName = this.GetRequestVal("MethodName");

		java.lang.Class tp = en.getClass();
		java.lang.reflect.Method mp = null;
		for (java.lang.reflect.Method m : tp.getMethods()) {
			if (m.getName().equals(methodName)==true) {
				mp = m;
				break;
			}
		}

		if (mp == null) {
			return "err@没有找到类[" + this.getEnName() + "]方法[" + methodName + "].";
		}


		String paras = this.GetRequestVal("paras");

		//执行该方法.
		Object[] myparas = new Object[0];

		if (DataType.IsNullOrEmpty(paras) == false)
		{
			String[] str = paras.split("[~]", -1);
			myparas = new Object[str.length];
			RefMethod rm = null;
			for (RefMethod item : en.getEnMap().getHisRefMethods())
			{
				if (item.ClassMethodName.replace(this.getEnName() + ".", "").equals(methodName + "()"))
				{
					rm = item;
					break;
				}
			}
			if (rm != null)
			{
				Attrs attrs = rm.getHisAttrs();
				int idx = 0;

				for (Attr attr : attrs)
				{
					if (idx >= str.length)
					{
						break;
					}
					myparas[idx] = str[idx];
					if (attr.getMyDataType() == DataType.AppInt)
					{
						myparas[idx] = Integer.parseInt(str[idx]);
					}
					if (attr.getMyDataType() == DataType.AppFloat)
					{
						myparas[idx] = Float.parseFloat(str[idx]);
					}
					if (attr.getMyDataType() == DataType.AppDouble)
					{
						myparas[idx] = Double.parseDouble(str[idx]);
					}
					if (attr.getMyDataType() == DataType.AppMoney)
					{
						myparas[idx] = new BigDecimal(Double.parseDouble(str[idx]));
					}
					idx++;
				}
			}
			else
			{
				myparas = str;
			}

		}



		Object tempVar = mp.invoke(en, myparas);
		String result = tempVar instanceof String ? (String)tempVar : null; //调用由此 MethodInfo 实例反射的方法或构造函数。
		return result;
	}

		///


		///Entities 公共类库.
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
	 * @throws Exception 
	*/
	public final String Entities_RetrieveAll() throws Exception
	{
		try
		{
			Entities ens = ClassFactory.GetEns(this.getEnsName());
			ens.RetrieveAll();
			return ens.ToJson();
		}
		catch (RuntimeException e)
		{
			return "err@[Entities_RetrieveAll][" + this.getEnsName() + "]类名错误，或者其他异常:" + e.getMessage();
		}
	}
	/** 
	 获得实体集合s
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Entities_Init() throws Exception
	{
		try
		{
			Entities ens = ClassFactory.GetEns(this.getEnsName());
			if (this.getParas() == null)
			{
				return "0";
			}

			Entity en = ens.getGetNewEntity();

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

				if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
				{
					valObj = bp.sys.Glo.GenerRealType(en.getEnMap().getAttrs(), key, val);
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
	 * @throws Exception 
	*/
	public final String Entities_RetrieveCond() throws Exception
	{
		try
		{
			Entities ens = ClassFactory.GetEns(this.getEnsName());
			if (this.getParas() == null)
			{
				return "0";
			}

			QueryObject qo = new QueryObject(ens);
			String[] myparas = this.getParas().replace("[%]","%").split("[@]", -1);

			Attrs attrs = ens.getGetNewEntity().getEnMap().getAttrs();

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
				if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
				{
					typeVal = bp.sys.Glo.GenerRealType(attrs, key, val);
				}

				String[] keys = key.trim().split("[,]", -1);
				int count = 0;
				for (String str : keys)
				{
					count++;
					if (DataType.IsNullOrEmpty(str) == true)
					{
						continue;
					}
					if (idx == 0 && count == 1)
					{
						qo.AddWhere(str, oper, typeVal);
					}
					else
					{
						if (count != 1)
						{
							qo.addOr();
						}
						else
						{
							qo.addAnd();
						}
						qo.AddWhere(str, oper, typeVal);
					}

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
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	*/
	public final String Entities_DoMethodReturnString() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{

		//创建类实体.
		Entities ens = ClassFactory.GetEns(this.getEnsName()); 
		// Activator.CreateInstance(System.Type.GetType("bp.en.*;

		String methodName = this.GetRequestVal("MethodName");

		java.lang.Class tp = ens.getClass();
		java.lang.reflect.Method mp = tp.getMethod(methodName);
		if (mp == null)
		{
			return "err@没有找到类[" + this.getEnsName() + "]方法[" + methodName + "].";
		}

		String paras = this.GetRequestVal("paras");
		if ("un".equals(paras) == true || "undefined".equals(paras) == true)
		{
			paras = "";
		}

		//执行该方法.
		Object[] myparas = new Object[0];

		if (DataType.IsNullOrEmpty(paras) == false)
		{
			myparas = paras.split("[~]", -1);
		}

		Object tempVar = mp.invoke(ens, myparas);
		String result = tempVar instanceof String ? (String)tempVar : null; //调用由此 MethodInfo 实例反射的方法或构造函数。
		return result;

	}

		///


		///功能执行.
	/** 
	 初始化.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Method_Init() throws Exception
	{
		String ensName = this.GetRequestVal("M");
		Method rm = ClassFactory.GetMethod(ensName);
		if (rm == null)
		{
			return "err@方法名错误或者该方法已经不存在" + ensName;
		}

		if (rm.getHisAttrs().size() == 0)
		{
			Hashtable ht = new Hashtable();
			ht.put("No", ensName);
			ht.put("Title", rm.Title);
			ht.put("Help", rm.Help);
			ht.put("Warning", rm.Warning==null?"":rm.Warning);
			return bp.tools.Json.ToJson(ht);
		}

		DataTable dt = new DataTable();

		//转化为集合.
		MapAttrs attrs = rm.getHisAttrs().ToMapAttrs();

		return "";
	}
	public final String Method_Done() throws Exception
	{
		String ensName = this.GetRequestVal("M");
		Method rm =ClassFactory.GetMethod(ensName);
		// rm.Init();
		int mynum = 0;
		for (Attr attr : rm.getHisAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			mynum++;
		}
		int idx = 0;
		for (Attr attr : rm.getHisAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			if (attr.getUIVisible()== false)
			{
				continue;
			}
			try
			{
				switch (attr.getUIContralType())
				{
					case TB:
						switch (attr.getMyDataType())
						{
							case bp.da.DataType.AppString:
							case bp.da.DataType.AppDate:
							case bp.da.DataType.AppDateTime:
								String str1 = this.GetValFromFrmByKey(attr.getKey());
								rm.SetValByKey(attr.getKey(), str1);
								break;
							case bp.da.DataType.AppInt:
								int myInt = this.GetValIntFromFrmByKey(attr.getKey()); //int.Parse(this.UCEn1.GetTBByID("TB_" + attr.getKey()).Text);
								rm.getRow().SetValByKey(String.valueOf(idx), Integer.valueOf(myInt));
								rm.SetValByKey(attr.getKey(), myInt);
								break;
							case bp.da.DataType.AppFloat:
								float myFloat = this.GetValFloatFromFrmByKey(attr.getKey()); // float.Parse(this.UCEn1.GetTBByID("TB_" + attr.getKey()).Text);
								rm.SetValByKey(attr.getKey(), myFloat);
								break;
							case bp.da.DataType.AppDouble:
							case bp.da.DataType.AppMoney:
								BigDecimal myDoub = this.GetValDecimalFromFrmByKey(attr.getKey()); // decimal.Parse(this.UCEn1.GetTBByID("TB_" + attr.getKey()).Text);
								rm.SetValByKey(attr.getKey(), myDoub);
								break;
							case bp.da.DataType.AppBoolean:
								boolean myBool = this.GetValBoolenFromFrmByKey(attr.getKey()); // decimal.Parse(this.UCEn1.GetTBByID("TB_" + attr.getKey()).Text);
								rm.SetValByKey(attr.getKey(), myBool);
								break;
							default:
								return "err@没有判断的数据类型．";
						}
						break;
					case DDL:
						try
						{
							String str = this.GetValFromFrmByKey(attr.getKey()); // decimal.Parse(this.UCEn1.GetTBByID("TB_" + attr.getKey()).Text);
							// string str = this.UCEn1.GetDDLByKey("DDL_" + attr.getKey()).SelectedItemStringVal;
							rm.SetValByKey(attr.getKey(), str);
						}
						catch (java.lang.Exception e)
						{
							rm.SetValByKey(attr.getKey(), "");
						}
						break;
					case CheckBok:
						boolean myBoolval = this.GetValBoolenFromFrmByKey(attr.getKey()); // decimal.Parse(this.UCEn1.GetTBByID("TB_" + attr.getKey()).Text);
						rm.SetValByKey(attr.getKey(), myBoolval);
						break;
					default:
						break;
				}
				idx++;
			}
			catch (RuntimeException ex)
			{
				return "err@获得参数错误" + "attr=" + attr.getKey() + " attr = " + attr.getKey() + ex.getMessage();
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
	public final String MethodLink_Init() throws Exception
	{
		ArrayList<Method> al =ClassFactory.GetObjects("bp.en.Method");
		int i = 1;
		String html = "";

		DataTable dt = new DataTable();
		dt.Columns.Add("Name", String.class);
		dt.Columns.Add("Title", String.class);
		dt.Columns.Add("GroupName", String.class);
		dt.Columns.Add("Icon", String.class);
		dt.Columns.Add("Note", String.class);

		DataRow dr;
		for (Method en : al)
		{
			if (en.getIsCanDo() == false || en.IsVisable == false)
			{
				continue;
			}

			 dr = dt.NewRow();
			dr.setValue("Name",en.getClass().getName());
			dr.setValue("Title", en.Title);
			dr.setValue("GroupName", en.GroupName);
			dr.setValue("Icon", en.Icon);
			dr.setValue("Note", en.Help);
			dt.Rows.add(dr);

		}

		return bp.tools.Json.ToJson(dt);
	}

		///


		///查询.
	/** 
	 获得查询的基本信息.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Search_MapBaseInfo() throws Exception
	{
		//获得
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		if (ens == null)
		{
			return "err@类名:" + this.getEnsName() + "错误";
		}

		Entity en = ens.getGetNewEntity();
		Map map = ens.getGetNewEntity().getEnMapInTime();

		Hashtable ht = new Hashtable();

		//把权限信息放入.
		UAC uac = en.getHisUAC();
		ht.put("IsUpdata", uac.IsUpdate);
		ht.put("IsInsert", uac.IsInsert);
		ht.put("IsDelete", uac.IsDelete);
		ht.put("IsView", uac.IsView);
		ht.put("IsExp", uac.IsExp); //是否可以导出?
		ht.put("IsImp", uac.IsImp); //是否可以导入?

		ht.put("EnDesc", en.getEnDesc()); //描述?
		ht.put("EnName", en.toString()); //类名?


		//把map信息放入
		ht.put("PhysicsTable", map.getPhysicsTable());
		ht.put("CodeStruct", map.getCodeStruct());
		ht.put("CodeLength", map.getCodeLength());

		//查询条件.
		if (map.IsShowSearchKey == true)
		{
			ht.put("IsShowSearchKey", 1);
		}
		else
		{
			ht.put("IsShowSearchKey", 0);
		}

		ht.put("SearchFields", map.SearchFields);
		ht.put("SearchFieldsOfNum", map.SearchFieldsOfNum);

		//按日期查询.
		ht.put("DTSearchWay", map.DTSearchWay.getValue());
		ht.put("DTSearchLable", map.DTSearchLable);
		ht.put("DTSearchKey", map.DTSearchKey);


		//把实体类中的主键放在hashtable中
		ht.put("EntityPK", en.getPKField());

		return bp.tools.Json.ToJson(ht);
	}
	/** 
	 外键或者枚举的查询.
	   
	 @return 
	 * @throws Exception 
	*/
	public final String Search_SearchAttrs() throws Exception
	{
		//获得
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		if (ens == null)
		{
			return "err@类名错误:" + this.getEnsName();
		}

		Entity en = ens.getGetNewEntity();
		Map map = ens.getGetNewEntity().getEnMapInTime();

		DataSet ds = new DataSet();

		//构造查询条件集合.
		DataTable dt = new DataTable();
		dt.Columns.Add("Field");
		dt.Columns.Add("Name");
		dt.Columns.Add("Width");
		dt.Columns.Add("UIContralType");
		dt.TableName = "Attrs";

		AttrSearchs attrs = map.getSearchAttrs();
		for (AttrSearch item : attrs)
		{
			DataRow dr = dt.NewRow();
			dr.setValue("Field", item.Key);
			dr.setValue("Name", item.HisAttr.getDesc());
			dr.setValue("Width", item.Width); //下拉框显示的宽度.
			dr.setValue("UIContralType", item.HisAttr.getUIContralType());

			dt.Rows.add(dr);
		}
		ds.Tables.add(dt);

		//把外键枚举增加到里面.
		for (AttrSearch item : attrs)
		{
			Attr attr = item.HisAttr;
			if (attr.getIsEnum() == true)
			{
				SysEnums ses = new SysEnums(attr.getUIBindKey());
				DataTable dtEnum = ses.ToDataTableField();
				dtEnum.TableName = item.Key;
				ds.Tables.add(dtEnum);
				continue;
			}

			if (attr.getIsFK() == true)
			{
				Entities ensFK = attr.getHisFKEns();
				ensFK.RetrieveAll();

				DataTable dtEn = ensFK.ToDataTableField();
				dtEn.TableName = item.Key;
				ds.Tables.add(dtEn);
				continue;
			}
			//绑定SQL的外键
			if (DataType.IsNullOrEmpty(attr.getUIBindKey()) == false && ds.GetTableByName(attr.getKey()) == null)
			{
				//获取SQl
				String sql = bp.wf.Glo.DealExp(attr.getUIBindKey(), null, null);
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
				ds.Tables.add(dtSQl);
			}

		}

		return bp.tools.Json.ToJson(ds);
	}
	/** 
	 执行查询 - 初始化查找数据
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Search_SearchIt() throws Exception
	{
		return bp.tools.Json.ToJson(Search_Search());
	}
	/** 
	 执行查询.这个方法也会被导出调用.
	 
	 @return 
	 * @throws Exception 
	*/
	public final DataSet Search_Search() throws Exception
	{
		//获得.
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		Entity en = ens.getGetNewEntity();
		Map map = en.getEnMapInTime();

		MapAttrs attrs = new MapAttrs();



		MapData md = new MapData();
		md.setNo(this.getEnsName());
		int count = md.RetrieveFromDBSources();
		if (count == 0)
		{
			attrs = map.getAttrs().ToMapAttrs();
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
		for (MapAttr attr : attrs.ToJavaList())
		{
			String searchVisable = attr.getatPara().GetValStrByKey("SearchVisable");
			if (searchVisable.equals("0"))
			{
				continue;
			}
			if ((count != 0 && DataType.IsNullOrEmpty(searchVisable)) || (count == 0 && attr.getUIVisible()== false))
			{
				continue;
			}
			row = dtAttrs.NewRow();
			row.setValue("KeyOfEn", attr.getKeyOfEn());
			row.setValue("Name", attr.getName());
			row.setValue("Width", attr.getUIWidthInt());
			row.setValue("UIContralType", attr.getUIContralType().getValue());

			dtAttrs.Rows.add(row);
		}

		DataSet ds = new DataSet();
		ds.Tables.add(dtAttrs); //把描述加入.

		md.setName( map.getEnDesc());

		//附件类型.
		md.SetPara("BPEntityAthType", map.HisBPEntityAthType.getValue());

		//获取实体类的主键
		md.SetPara("PK", en.getPK());

		ds.Tables.add(md.ToDataTableField("Sys_MapData"));

		//取出来查询条件.
		bp.sys.UserRegedit ur = new UserRegedit();
		ur.setMyPK(WebUser.getNo() + "_" + this.getEnsName() + "_SearchAttrs");
		ur.RetrieveFromDBSources();

		//获得关键字.
		AtPara ap = new AtPara(ur.getVals());

		//关键字.
		String keyWord = ur.getSearchKey();
		QueryObject qo = new QueryObject(ens);
		boolean isFirst = true; //是否第一次拼接SQL


			///关键字字段.
		if (DataType.IsNullOrEmpty(map.SearchFields) == false)
		{
			String field = ""; //字段名
			String fieldValue = ""; //字段值
			int idx = 0;

			//获取查询的字段
			String[] searchFields = map.SearchFields.split("[@]", -1);
			for (String str : searchFields)
			{
				if (DataType.IsNullOrEmpty(str) == true)
				{
					continue;
				}

				//字段名
				field = str.split("[=]", -1)[1];
				if (DataType.IsNullOrEmpty(field) == true)
				{
					continue;
				}

				//字段名对应的字段值
				fieldValue = ur.GetParaString(field);
				if (DataType.IsNullOrEmpty(fieldValue) == true)
				{
					continue;
				}
				fieldValue = fieldValue.trim();
				fieldValue = fieldValue.replace(",", ";").replace(" ", ";");
				String[] fieldValues = fieldValue.split("[;]", -1);
				int valIdx = 0;
				idx++;
				for (String val : fieldValues)
				{
					valIdx++;

					if (idx == 1 && valIdx == 1)
					{
						isFirst = false;
						/* 第一次进来。 */
						qo.addLeftBracket();
						
						if (SystemConfig.getAppCenterDBVarStr().equals("@") 
								|| SystemConfig.getAppCenterDBVarStr().equals(":"))
						{
							qo.AddWhere(field, " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? (" CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + field + valIdx + ",'%')") : (" '%'+" + SystemConfig.getAppCenterDBVarStr() + field + valIdx + "+'%'"));
						}
						else
						{
							qo.AddWhere(field, " LIKE ", " '%'||" + SystemConfig.getAppCenterDBVarStr() + field + valIdx + "||'%'");
						}
						qo.getMyParas().Add(field + valIdx, val);

						if (valIdx == fieldValues.length)
						{
							qo.addRightBracket();
						}

						continue;
					}
					if (valIdx == 1 && idx != 1)
					{
						qo.addAnd();
						qo.addLeftBracket();
					}
					else
					{
						qo.addOr();
					}

					if (SystemConfig.getAppCenterDBVarStr().equals("@") || SystemConfig.getAppCenterDBVarStr().equals(":"))
					{
						qo.AddWhere(field, " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? ("CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + field + ",'%')") : ("'%'+" + SystemConfig.getAppCenterDBVarStr() + field + valIdx + "+'%'"));
					}
					else
					{
						qo.AddWhere(field, " LIKE ", "'%'||" + SystemConfig.getAppCenterDBVarStr() + field + valIdx + "||'%'");
					}
					qo.getMyParas().Add(field + valIdx, val);

					if (valIdx == fieldValues.length)
					{
						qo.addRightBracket();
					}
				}
			}

		}
		else
		{
			if (en.getEnMap().IsShowSearchKey && DataType.IsNullOrEmpty(keyWord) == false && keyWord.length() >= 1)
			{
				Attr attrPK = new Attr();
				for (Attr attr : map.getAttrs())
				{
					if (attr.getIsPK())
					{
						attrPK = attr;
						break;
					}
				}
				int i = 0;
				String enumKey = ","; //求出枚举值外键.
				keyWord = keyWord.replace(",", ";").replace(" ", ";");
				String[] strVals = keyWord.split("[;]", -1);
				for (Attr attr : map.getAttrs())
				{
					switch (attr.getMyFieldType())
					{
						case Enum:
							enumKey = "," + attr.getKey() + "Text,";
							break;
						case FK:
							// case FieldType.PKFK:
							continue;
						default:
							break;
					}

					if (attr.getMyDataType() != DataType.AppString)
					{
						continue;
					}

					//排除枚举值关联refText.
					if (attr.getMyFieldType() == FieldType.RefText)
					{
						if (enumKey.contains("," + attr.getKey() + ",") == true)
						{
							continue;
						}
					}

					if (attr.getKey().equals("FK_Dept"))
					{
						continue;
					}
					int valIdx = 0;
					for (String val : strVals)
					{
						i++;
						valIdx++;
						if (i == 1)
						{
							isFirst = false;
							/* 第一次进来。 */
							qo.addLeftBracket();
							if (SystemConfig.getAppCenterDBVarStr().equals("@") || SystemConfig.getAppCenterDBVarStr().equals(":"))
							{
								qo.AddWhere(attr.getKey(), " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? (" CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + "SKey" + valIdx + ", '%')") : (" '%'+" + SystemConfig.getAppCenterDBVarStr() + "SKey" + valIdx + "+'%'"));
							}
							else
							{
								qo.AddWhere(attr.getKey(), " LIKE ", " '%'||" + SystemConfig.getAppCenterDBVarStr() + "SKey" + valIdx + "|| '%'");
							}

							qo.getMyParas().Add("SKey" + valIdx, val);

							continue;
						}
						qo.addOr();

						if (SystemConfig.getAppCenterDBVarStr().equals("@") || SystemConfig.getAppCenterDBVarStr().equals(":"))
						{
							qo.AddWhere(attr.getKey(), " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? ("CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + "SKey" + valIdx + ", '%')") : ("'%'+" + SystemConfig.getAppCenterDBVarStr() + "SKey" + valIdx + "+'%'"));
						}
						else
						{
							qo.AddWhere(attr.getKey(), " LIKE ", "'%'||" + SystemConfig.getAppCenterDBVarStr() + "SKey" + valIdx + "|| '%'");
						}

						qo.getMyParas().Add("SKey" + valIdx, val);
					}

				}

				qo.addRightBracket();

			}

		}


			///

			///增加数值型字段的查询
		if (DataType.IsNullOrEmpty(map.SearchFieldsOfNum) == false)
		{
			String field = ""; //字段名
			String fieldValue = ""; //字段值
			int idx = 0;

			//获取查询的字段
			String[] searchFieldsOfNum = map.SearchFieldsOfNum.split("[@]", -1);
			for (String str : searchFieldsOfNum)
			{
				if (DataType.IsNullOrEmpty(str) == true)
				{
					continue;
				}

				//字段名
				field = str.split("[=]", -1)[1];
				if (DataType.IsNullOrEmpty(field) == true)
				{
					continue;
				}

				//字段名对应的字段值
				fieldValue = ur.GetParaString(field);
				if (DataType.IsNullOrEmpty(fieldValue) == true)
				{
					continue;
				}
				String[] strVals = fieldValue.split("[,]", -1);

				//判断是否是第一次进入
				if (isFirst == false)
				{
					qo.addAnd();
				}
				else
				{
					isFirst = false;
				}
				qo.addLeftBracket();
				if (DataType.IsNullOrEmpty(strVals[0]) == false)
				{

					if (DataType.IsNullOrEmpty(strVals[1]) == true)
					{
						qo.AddWhere(field, ">=", strVals[0]);
					}
					else
					{
						qo.AddWhere(field, ">=", strVals[0], field + "1");
						qo.addAnd();
						qo.AddWhere(field, "<=", strVals[1], field + "2");
					}

				}
				else
				{
					qo.AddWhere(field, "<=", strVals[1]);
				}

				qo.addRightBracket();

			}


		}

			///

		if (map.DTSearchWay != DTSearchWay.None && DataType.IsNullOrEmpty(ur.getDTFrom()) == false)
		{
			String dtFrom = ur.getDTFrom(); // this.GetTBByID("TB_S_From").Text.Trim().replace("/", "-");
			String dtTo = ur.getDTTo(); // this.GetTBByID("TB_S_To").Text.Trim().replace("/", "-");

			if (DataType.IsNullOrEmpty(dtTo) == true)
			{
				dtTo = DataType.getCurrentDate();
			}

			//按日期查询
			if (map.DTSearchWay == DTSearchWay.ByDate)
			{
				if (isFirst == false)
				{
					qo.addAnd();
				}
				else
				{
					isFirst = false;
				}
				if (DataType.IsNullOrEmpty(dtFrom) == true)
				{
					qo.addLeftBracket();
					qo.setSQL(map.DTSearchKey + " <= '" + dtTo + "'");
					qo.addRightBracket();
				}
				else
				{
					qo.addLeftBracket();
					dtTo += " 23:59:59";
					qo.setSQL(map.DTSearchKey + " >= '" + dtFrom + "'");
					qo.addAnd();
					qo.setSQL(map.DTSearchKey + " <= '" + dtTo + "'");
					qo.addRightBracket();
				}

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

				Date date = DateUtils.addDay(new Date(), -1);
				String dttime = DateUtils.format(date, "yyyy-MM-dd");
				dtFrom = dttime + " 24:00";
				
				if (dtTo.trim().length() < 11 || dtTo.trim().indexOf(' ') == -1)
				{
					dtTo += " 24:00";
				}
				if (isFirst == false)
				{
					qo.addAnd();
				}
				else
				{
					isFirst = false;
				}
				if (DataType.IsNullOrEmpty(dtFrom) == true)
				{
					qo.addLeftBracket();
					qo.setSQL(map.DTSearchKey + " <= '" + dtTo + "'");
					qo.addRightBracket();
				}
				else
				{
					qo.addLeftBracket();
					qo.setSQL(map.DTSearchKey + " >= '" + dtFrom + "'");
					qo.addAnd();
					qo.setSQL(map.DTSearchKey + " <= '" + dtTo + "'");
					qo.addRightBracket();
				}

			}
		}



			///普通属性
		String opkey = ""; // 操作符号。
		for (AttrOfSearch attr : en.getEnMap().getAttrsOfSearch())
		{
			if (attr.getIsHidden())
			{
				if (isFirst == false)
				{
					qo.addAnd();
				}
				else
				{
					isFirst = false;
				}
				qo.addLeftBracket();

				//获得真实的数据类型.
				if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
				{
					Object valType = bp.sys.Glo.GenerRealType(en.getEnMap().getAttrs(), attr.getRefAttrKey(), attr.getDefaultValRun());
					qo.AddWhere(attr.getRefAttrKey(), attr.getDefaultSymbol(), valType);
				}
				else
				{
					qo.AddWhere(attr.getRefAttrKey(), attr.getDefaultSymbol(), attr.getDefaultValRun());
				}
				qo.addRightBracket();
				continue;
			}

			if (attr.getSymbolEnable() == true)
			{
				opkey = ap.GetValStrByKey("DDL_" + attr.getKey());
				if (opkey.equals("all"))
				{
					continue;
				}
			}
			else
			{
				opkey = attr.getDefaultSymbol();
			}

			if (isFirst == false)
			{
				qo.addAnd();
			}
			else
			{
				isFirst = false;
			}
			qo.addLeftBracket();

			if (attr.getDefaultVal().length() >= 8)
			{
				String date = "2005-09-01";
				try
				{
					/* 就可能是年月日。 */
					String y = ap.GetValStrByKey("DDL_" + attr.getKey() + "_Year");
					String m = ap.GetValStrByKey("DDL_" + attr.getKey() + "_Month");
					String d = ap.GetValStrByKey("DDL_" + attr.getKey() + "_Day");
					date = y + "-" + m + "-" + d;

					if (opkey.equals("<="))
					{
						Date dt = DateUtils.addDay(DateUtils.parse(date), 1);
						date = DateUtils.format(dt, DataType.getSysDataFormat());
					}
				}
				catch (java.lang.Exception e)
				{
				}

				qo.AddWhere(attr.getRefAttrKey(), opkey, date);
			}
			else
			{
				qo.AddWhere(attr.getRefAttrKey(), opkey, ap.GetValStrByKey("TB_" + attr.getKey()));
			}
			qo.addRightBracket();
		}

			///


			///获得查询数据.
		for (String str : ap.getHisHT().keySet())
		{
			Object val = ap.GetValStrByKey(str);
			if (val.equals("all"))
			{
				continue;
			}

			if (isFirst == false)
			{
				qo.addAnd();
			}
			else
			{
				isFirst = false;
			}
			isFirst = false;
			qo.addLeftBracket();

			//获得真实的数据类型.
			Object valType = bp.sys.Glo.GenerRealType(en.getEnMap().getAttrs(), str, ap.GetValStrByKey(str));

			qo.AddWhere(str, valType);
			qo.addRightBracket();
		}

		//获得行数.
		ur.SetPara("RecCount", qo.GetCount());
		ur.Save();

		//获取配置信息
		EnCfg encfg = new EnCfg(this.getEnsName());
		String fieldSet = encfg.getFieldSet();
		String oper = "";
		if (DataType.IsNullOrEmpty(fieldSet) == false)
		{
			String ptable = en.getEnMap().getPhysicsTable();
			DataTable dt = new DataTable("Search_HeJi");
			dt.Columns.Add("Field");
			dt.Columns.Add("Type");
			dt.Columns.Add("Value");
			DataRow dr;
			String[] strs = fieldSet.split("[@]", -1);
			for (String str : strs)
			{
				String[] item = str.split("[=]", -1);
				if (item.length == 2)
				{
				   if (item[1].contains(",") == true)
				   {
						String[] ss = item[1].split("[,]", -1);
						for (String s : ss)
						{
							dr = dt.NewRow();
							dr.setValue("Field", ((MapAttr)attrs.GetEntityByKey(s)).getName());
							dr.setValue("Type", item[0]);
							dt.Rows.add(dr);

							oper += item[0] + "(" + ptable + "." + s + ")" + ",";
						}
				   }
					else
					{
						dr = dt.NewRow();
						dr.setValue("Field", ((MapAttr)attrs.GetEntityByKey(item[1])).getName());
						dr.setValue("Type", item[0]);
						dt.Rows.add(dr);

						oper += item[0] + "(" + ptable + "." + item[1] + ")" + ",";
					}
				}
			}
			oper = oper.substring(0, oper.length() - 1);
			DataTable dd = qo.GetSumOrAvg(oper);

			for (int i = 0;i < dt.Rows.size();i++)
			{
				DataRow ddr = dt.Rows.get(i);
				ddr.setValue("Value", dd.Rows.get(0).getValue(i).toString());
			}
			ds.Tables.add(dt);
		}


		//


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
			qo.DoQuery(en.getPK(), 500, 1);
		}
		else
		{
			qo.DoQuery(en.getPK(), this.getPageSize(), this.getPageIdx());
		}

			/// 获得查询数据.

		DataTable mydt = ens.ToDataTableField();
		mydt.TableName = "DT";

		ds.Tables.add(mydt); //把数据加入里面.


			///获得方法的集合
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

		RefMethods rms = map.getHisRefMethods();
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

			myurl = "RefMethod.htm?Index=" + item.Index + "&EnName=" + en.toString() + "&EnsName=" + en.getGetNewEntities().toString() + "&PKVal=";

			DataRow dr = dtM.NewRow();

			dr.setValue("No", item.Index);
			dr.setValue("Title", item.Title);
			dr.setValue("Tip", item.ToolTip);
			dr.setValue("Visable", item.Visable);
			dr.setValue("Warning", item.Warning);
			dr.setValue("RefMethodType", item.refMethodType.getValue());
			dr.setValue("RefAttrKey", item.RefAttrKey);
			dr.setValue("URL", myurl);
			dr.setValue("W", item.Width);
			dr.setValue("H", item.Height);
			dr.setValue("Icon", item.Icon);
			dr.setValue("IsCanBatch", item.IsCanBatch);
			dr.setValue("GroupName", item.GroupName);
			dr.setValue("ClassMethodName", item.ClassMethodName);

			dtM.Rows.add(dr); //增加到rows.
		}
		ds.Tables.add(dtM); //把数据加入里面.

			///

		return ds;

	}
	private DataTable SearchDtl_Data(Entities ens, Entity en, String workId, String fid) throws Exception
	{
		//获得.
		Map map = en.getEnMapInTime();

		MapAttrs attrs = map.getAttrs().ToMapAttrs();

		QueryObject qo = new QueryObject(ens);

		qo.AddWhere("RefPK","=",workId);
		//qo.addAnd();
		//qo.AddWhere("FID", "=", fid);


			/// 获得查询数据.
		return qo.DoQueryToTable();
	}
		private DataTable Search_Data(Entities ens, Entity en) throws Exception
		{
		//获得.
		Map map = en.getEnMapInTime();

		MapAttrs attrs = map.getAttrs().ToMapAttrs();
		//取出来查询条件.
		bp.sys.UserRegedit ur = new UserRegedit();
		ur.setMyPK(WebUser.getNo() + "_" + this.getEnsName() + "_SearchAttrs");
		ur.RetrieveFromDBSources();

		//获得关键字.
		AtPara ap = new AtPara(ur.getVals());
		//关键字.
		String keyWord = ur.getSearchKey();
		QueryObject qo = new QueryObject(ens);
		boolean isFirst = true; //是否第一次拼接SQL


			///关键字字段.
		if (DataType.IsNullOrEmpty(map.SearchFields) == false)
		{
			String field = ""; //字段名
			String fieldValue = ""; //字段值
			int idx = 0;

			//获取查询的字段
			String[] searchFields = map.SearchFields.split("[@]", -1);
			for (String str : searchFields)
			{
				if (DataType.IsNullOrEmpty(str) == true)
				{
					continue;
				}

				//字段名
				field = str.split("[=]", -1)[1];
				if (DataType.IsNullOrEmpty(field) == true)
				{
					continue;
				}

				//字段名对应的字段值
				fieldValue = ur.GetParaString(field);
				if (DataType.IsNullOrEmpty(fieldValue) == true)
				{
					continue;
				}
				fieldValue = fieldValue.trim();
				fieldValue = fieldValue.replace(",", ";").replace(" ", ";");
				String[] fieldValues = fieldValue.split("[;]", -1);
				int valIdx = 0;
				idx++;
				for (String val : fieldValues)
				{
					valIdx++;

					if (idx == 1 && valIdx == 1)
					{
						isFirst = false;
						/* 第一次进来。 */
						qo.addLeftBracket();
						if (SystemConfig.getAppCenterDBVarStr().equals("@") || SystemConfig.getAppCenterDBVarStr().equals("?"))
						{
							qo.AddWhere(field, " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? (" CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + field + valIdx + ",'%')") : (" '%'+" + SystemConfig.getAppCenterDBVarStr() + field + valIdx + "+'%'"));
						}
						else
						{
							qo.AddWhere(field, " LIKE ", " '%'||" + SystemConfig.getAppCenterDBVarStr() + field + valIdx + "||'%'");
						}
						qo.getMyParas().Add(field + valIdx, val);

						if (valIdx == fieldValues.length)
						{
							qo.addRightBracket();
						}

						continue;
					}
					if (valIdx == 1 && idx != 1)
					{
						qo.addAnd();
						qo.addLeftBracket();
					}
					else
					{
						qo.addOr();
					}

					if (SystemConfig.getAppCenterDBVarStr().equals("@") || SystemConfig.getAppCenterDBVarStr().equals("?"))
					{
						qo.AddWhere(field, " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? ("CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + field + ",'%')") : ("'%'+" + SystemConfig.getAppCenterDBVarStr() + field + valIdx + "+'%'"));
					}
					else
					{
						qo.AddWhere(field, " LIKE ", "'%'||" + SystemConfig.getAppCenterDBVarStr() + field + valIdx + "||'%'");
					}
					qo.getMyParas().Add(field + valIdx, val);

					if (valIdx == fieldValues.length)
					{
						qo.addRightBracket();
					}
				}
			}

		}
		else
		{
			if (en.getEnMap().IsShowSearchKey && DataType.IsNullOrEmpty(keyWord) == false && keyWord.length() >= 1)
			{
				Attr attrPK = new Attr();
				for (Attr attr : map.getAttrs())
				{
					if (attr.getIsPK())
					{
						attrPK = attr;
						break;
					}
				}
				int i = 0;
				String enumKey = ","; //求出枚举值外键.
				keyWord = keyWord.replace(",", ";").replace(" ", ";");
				String[] strVals = keyWord.split("[;]", -1);
				for (Attr attr : map.getAttrs())
				{
					switch (attr.getMyFieldType())
					{
						case Enum:
							enumKey = "," + attr.getKey() + "Text,";
							break;
						case FK:
							// case FieldType.PKFK:
							continue;
						default:
							break;
					}

					if (attr.getMyDataType() != DataType.AppString)
					{
						continue;
					}

					//排除枚举值关联refText.
					if (attr.getMyFieldType() == FieldType.RefText)
					{
						if (enumKey.contains("," + attr.getKey() + ",") == true)
						{
							continue;
						}
					}

					if (attr.getKey().equals("FK_Dept"))
					{
						continue;
					}
					int valIdx = 0;
					for (String val : strVals)
					{
						i++;
						valIdx++;
						if (i == 1)
						{
							isFirst = false;
							/* 第一次进来。 */
							qo.addLeftBracket();
							if (SystemConfig.getAppCenterDBVarStr().equals("@") || SystemConfig.getAppCenterDBVarStr().equals(":"))
							{
								qo.AddWhere(attr.getKey(), " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? (" CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + "SKey" + valIdx + ", '%')") : (" '%'+" + SystemConfig.getAppCenterDBVarStr() + "SKey" + valIdx + "+'%'"));
							}
							else
							{
								qo.AddWhere(attr.getKey(), " LIKE ", " '%'||" + SystemConfig.getAppCenterDBVarStr() + "SKey" + valIdx + "|| '%'");
							}

							qo.getMyParas().Add("SKey" + valIdx, val);

							continue;
						}
						qo.addOr();

						if (SystemConfig.getAppCenterDBVarStr().equals("@") || SystemConfig.getAppCenterDBVarStr().equals(":"))
						{
							qo.AddWhere(attr.getKey(), " LIKE ", SystemConfig.getAppCenterDBType() == DBType.MySQL ? ("CONCAT('%'," + SystemConfig.getAppCenterDBVarStr() + "SKey" + valIdx + ", '%')") : ("'%'+" + SystemConfig.getAppCenterDBVarStr() + "SKey" + valIdx + "+'%'"));
						}
						else
						{
							qo.AddWhere(attr.getKey(), " LIKE ", "'%'||" + SystemConfig.getAppCenterDBVarStr() + "SKey" + valIdx + "|| '%'");
						}

						qo.getMyParas().Add("SKey" + valIdx, val);
					}

				}

				qo.addRightBracket();

			}

		}


			///

			///增加数值型字段的查询
		if (DataType.IsNullOrEmpty(map.SearchFieldsOfNum) == false)
		{
			String field = ""; //字段名
			String fieldValue = ""; //字段值
			int idx = 0;

			//获取查询的字段
			String[] searchFieldsOfNum = map.SearchFieldsOfNum.split("[@]", -1);
			for (String str : searchFieldsOfNum)
			{
				if (DataType.IsNullOrEmpty(str) == true)
				{
					continue;
				}

				//字段名
				field = str.split("[=]", -1)[1];
				if (DataType.IsNullOrEmpty(field) == true)
				{
					continue;
				}

				//字段名对应的字段值
				fieldValue = ur.GetParaString(field);
				if (DataType.IsNullOrEmpty(fieldValue) == true)
				{
					continue;
				}
				String[] strVals = fieldValue.split("[,]", -1);

				//判断是否是第一次进入
				if (isFirst == false)
				{
					qo.addAnd();
				}
				else
				{
					isFirst = false;
				}
				qo.addLeftBracket();
				if (DataType.IsNullOrEmpty(strVals[0]) == false)
				{

					if (DataType.IsNullOrEmpty(strVals[1]) == true)
					{
						qo.AddWhere(field, ">=", strVals[0]);
					}
					else
					{
						qo.AddWhere(field, ">=", strVals[0], field + "1");
						qo.addAnd();
						qo.AddWhere(field, "<=", strVals[1], field + "2");
					}

				}
				else
				{
					qo.AddWhere(field, "<=", strVals[1]);
				}

				qo.addRightBracket();

			}


		}

			///

		if (map.DTSearchWay != DTSearchWay.None && DataType.IsNullOrEmpty(ur.getDTFrom()) == false)
		{
			String dtFrom = ur.getDTFrom(); // this.GetTBByID("TB_S_From").Text.Trim().replace("/", "-");
			String dtTo = ur.getDTTo(); // this.GetTBByID("TB_S_To").Text.Trim().replace("/", "-");

			if (DataType.IsNullOrEmpty(dtTo) == true)
			{
				dtTo = DataType.getCurrentDate();
			}

			//按日期查询
			if (map.DTSearchWay == DTSearchWay.ByDate)
			{
				if (isFirst == false)
				{
					qo.addAnd();
				}
				else
				{
					isFirst = false;
				}
				qo.addLeftBracket();
				dtTo += " 23:59:59";
				qo.setSQL(map.DTSearchKey + " >= '" + dtFrom + "'");
				qo.addAnd();
				qo.setSQL(map.DTSearchKey + " <= '" + dtTo + "'");
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

				
				Date date = DateUtils.parse(dtFrom);
				date = DateUtils.addDay(date, -1);
				dtFrom = DateUtils.format(date, "yyyy-MM-dd") + " 24:00";

				if (dtTo.trim().length() < 11 || dtTo.trim().indexOf(' ') == -1)
				{
					dtTo += " 24:00";
				}
				if (isFirst == false)
				{
					qo.addAnd();
				}
				else
				{
					isFirst = false;
				}

				qo.addLeftBracket();
				qo.setSQL(map.DTSearchKey + " >= '" + dtFrom + "'");
				qo.addAnd();
				qo.setSQL(map.DTSearchKey + " <= '" + dtTo + "'");
				qo.addRightBracket();
			}
		}



			///普通属性
		String opkey = ""; // 操作符号。
		for (AttrOfSearch attr : en.getEnMap().getAttrsOfSearch())
		{
			if (attr.getIsHidden())
			{
				if (isFirst == false)
				{
					qo.addAnd();
				}
				else
				{
					isFirst = false;
				}
				qo.addLeftBracket();

				//获得真实的数据类型.
				if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
				{
					Object valType = bp.sys.Glo.GenerRealType(en.getEnMap().getAttrs(), attr.getRefAttrKey(), attr.getDefaultValRun());
					qo.AddWhere(attr.getRefAttrKey(), attr.getDefaultSymbol(), valType);
				}
				else
				{
					qo.AddWhere(attr.getRefAttrKey(), attr.getDefaultSymbol(), attr.getDefaultValRun());
				}
				qo.addRightBracket();
				continue;
			}

			if (attr.getSymbolEnable() == true)
			{
				opkey = ap.GetValStrByKey("DDL_" + attr.getKey());
				if (opkey.equals("all"))
				{
					continue;
				}
			}
			else
			{
				opkey = attr.getDefaultSymbol();
			}

			if (isFirst == false)
			{
				qo.addAnd();
			}
			else
			{
				isFirst = false;
			}
			qo.addLeftBracket();

			if (attr.getDefaultVal().length() >= 8)
			{
				String date = "2005-09-01";
				try
				{
					/* 就可能是年月日。 */
					String y = ap.GetValStrByKey("DDL_" + attr.getKey() + "_Year");
					String m = ap.GetValStrByKey("DDL_" + attr.getKey() + "_Month");
					String d = ap.GetValStrByKey("DDL_" + attr.getKey() + "_Day");
					date = y + "-" + m + "-" + d;

					if (opkey.equals("<="))
					{
						Date dt = DateUtils.addDay(DataType.ParseSysDate2DateTime(date), 1);
						date = DateUtils.format(dt, DataType.getSysDataFormat());

					}
				}
				catch (java.lang.Exception e)
				{
				}

				qo.AddWhere(attr.getRefAttrKey(), opkey, date);
			}
			else
			{
				qo.AddWhere(attr.getRefAttrKey(), opkey, ap.GetValStrByKey("TB_" + attr.getKey()));
			}
			qo.addRightBracket();
		}

			///


			///隐藏字段的查询.
		for (String str : ap.getHisHT().keySet())
		{
			Object val = ap.GetValStrByKey(str);
			if (val.equals("all"))
			{
				continue;
			}

			if (isFirst == false)
			{
				qo.addAnd();
			}
			else
			{
				isFirst = false;
			}
			isFirst = false;
			qo.addLeftBracket();

			//获得真实的数据类型.
			Object valType = bp.sys.Glo.GenerRealType(en.getEnMap().getAttrs(), str, ap.GetValStrByKey(str));

			qo.AddWhere(str, valType);
			qo.addRightBracket();
		}

			/// 隐藏字段的查询

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
		return qo.DoQueryToTable();


		}
	public final String Search_GenerPageIdx() throws Exception
	{

		bp.sys.UserRegedit ur = new UserRegedit();
		ur.setMyPK(WebUser.getNo() + "_" + this.getEnsName() + "_SearchAttrs");
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
		int pageNum = (recNum + pageSize - 1) / pageSize;// 页面个数。

		int from = getPageIdx() < 1 ? 0 : (getPageIdx() - 1) * pageSize + 1;// 从

		int to = getPageIdx() < 1 ? pageSize : getPageIdx() * pageSize;// 到

		for (int i = 1; i <= pageNum; i++) {
			if (i >= from && i < to) {
			} else {
				continue;
			}

			if (getPageIdx() == i) {
				html += "&nbsp;<font style='font-weight:bloder;color:#f00'>" + i + "</font>&nbsp;";
			} else {
				html += "&nbsp;<a href='" + url + "&PageIdx=" + i + "'>" + i + "</a>";
			}
		}

		if (getPageIdx() != pageNum) {
			myidx = getPageIdx() + 1;
			html += "&nbsp;<a href='" + url + "&PageIdx=" + myidx + "'><img style='vertical-align:middle' src='"
					+ Glo.getCCFlowAppPath() + "WF/Img/Arr/Right.png' border=0/></a>&nbsp;<a href='" + url + "&PageIdx="
					+ pageNum + "'><img style='vertical-align:middle' src='" + Glo.getCCFlowAppPath()
					+ "WF/Img/Arr/RightEnd.png' border=0/></a>&nbsp;&nbsp;页数:" + getPageIdx() + "/" + pageNum
					+ "&nbsp;&nbsp;总数:" + recNum;
		} else {
			html += "&nbsp;<img style='vertical-align:middle' src='" + Glo.getCCFlowAppPath()
					+ "WF/Img/Arr/Right.png' border=0/>&nbsp;&nbsp;";
			html += "&nbsp;<img style='vertical-align:middle' src='" + Glo.getCCFlowAppPath()
					+ "WF/Img/Arr/RightEnd.png' border=0/>&nbsp;&nbsp;页数:" + getPageIdx() + "/" + pageNum
					+ "&nbsp;&nbsp;总数:" + recNum;
		}
		html += "</div>";
		return html;
	}
	/** 
	 执行导出
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Search_Exp() throws Exception
	{
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		Entity en = ens.getGetNewEntity();
		String name = "数据导出";
		MapAttrs mapAttrs = new MapAttrs();
		Attrs attrs = null;
		MapData md = new MapData();
		md.setNo(this.getEnsName());
		int count = md.RetrieveFromDBSources();
		if (count == 1)
		{
			mapAttrs.Retrieve(MapAttrAttr.FK_MapData, this.getEnsName(), MapAttrAttr.Idx);
			attrs = new Attrs();
			for (MapAttr attr : mapAttrs.ToJavaList())
			{
				String searchVisable = attr.getatPara().GetValStrByKey("SearchVisable");
				if (searchVisable.equals("0"))
				{
					continue;
				}
				if ((count != 0 && DataType.IsNullOrEmpty(searchVisable)) || (count == 0 && attr.getUIVisible()== false))
				{
					continue;
				}
				attrs.Add(attr.getHisAttr());

			}

		}

		String filename = name + "_" + DataType.getCurrentDataTimeCNOfLong() + "_" + WebUser.getName() + ".xls";
		String filePath = ExportDGToExcel(Search_Data(ens, en), en, name, attrs);

		return filePath;
	}
	/** 
	 从表执行导出
	 
	 @return 
	 * @throws Exception 
	*/
	public final String SearchDtl_Exp() throws Exception
	{
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		Entity en = ens.getGetNewEntity();

		String workId = this.GetRequestVal("WorkId");
		String fid = this.GetRequestVal("FID");
		String name = "从表数据导出";
		String filename = name + "_" + DataType.getCurrentDataTimeCNOfLong() + "_" + WebUser.getName() + ".xls";
		String filePath = ExportDGToExcel(SearchDtl_Data(ens, en, workId, fid), en, name,null);

		return filePath;
	}



		///Refmethod.htm 相关功能.
	public final String Refmethod_Init() throws Exception
	{
		String ensName = this.getEnsName();
		int index = this.getIndex();
		Entities ens = ClassFactory.GetEns(ensName);
		Entity en = ens.getGetNewEntity();
		RefMethod rm = en.getEnMap().getHisRefMethods().get(index);

		String pk = this.getPKVal();
		if (pk == null)
		{
			pk = this.GetRequestVal(en.getPK());
		}

		if (pk == null)
		{
			pk = this.getPKVal();
		}

		if (pk == null)
		{
			return "err@错误pkval 没有值。";
		}

		en.setPKVal(pk);
		en.Retrieve();

		//获取主键集合
		String[] pks = pk.split("[,]", -1);


			///处理无参数的方法.
		if (rm.getHisAttrs() == null || rm.getHisAttrs().size() == 0)
		{

			String infos = "";
			for (String mypk : pks)
			{
				if (DataType.IsNullOrEmpty(mypk) == true)
				{
					continue;
				}

				en.setPKVal(mypk);
				en.Retrieve();
				rm.HisEn = en;

				// 如果是link.
				if (rm.refMethodType == RefMethodType.LinkModel || rm.refMethodType == RefMethodType.LinkeWinOpen || rm.refMethodType == RefMethodType.RightFrameOpen)
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

			/// 处理无参数的方法.
		DataSet ds = new DataSet();

		//转化为json 返回到前台解析. 处理有参数的方法.
		MapAttrs attrs = rm.getHisAttrs().ToMapAttrs();

		//属性.
		DataTable mapAttrs = attrs.ToDataTableField("Sys_MapAttrs");
		ds.Tables.add(mapAttrs);


			///该方法的默认值.
		DataTable dtMain = new DataTable();
		dtMain.TableName = "MainTable";
		for (MapAttr attr : attrs.ToJavaList())
		{
			dtMain.Columns.Add(attr.getKeyOfEn(), String.class);
		}

		DataRow mydrMain = dtMain.NewRow();
		for (MapAttr item : attrs.ToJavaList())
		{
			String v = item.getDefValReal();
			if (v.indexOf('@') == -1)
			{
				mydrMain.setValue(item.getKeyOfEn(), item.getDefValReal());
			}
			//替换默认值的@的
			else
			{
				if (v.equals("@WebUser.No"))
				{
					mydrMain.setValue(item.getKeyOfEn(), WebUser.getNo());
				}
				else if (v.equals("@WebUser.Name"))
				{
					mydrMain.setValue(item.getKeyOfEn(), WebUser.getName());
				}
				else if (v.equals("@WebUser.FK_Dept"))
				{
					mydrMain.setValue(item.getKeyOfEn(), WebUser.getFK_Dept());
				}
				else if (v.equals("@WebUser.FK_DeptName"))
				{
					mydrMain.setValue(item.getKeyOfEn(), WebUser.getFK_DeptName());
				}
				else if (v.equals("@WebUser.FK_DeptNameOfFull") || v.equals("@WebUser.FK_DeptFullName"))
				{
					mydrMain.setValue(item.getKeyOfEn(), WebUser.getFK_DeptNameOfFull());
				}
				else if (v.equals("@RDT"))
				{
					if (item.getMyDataType() == DataType.AppDate)
					{
						mydrMain.setValue(item.getKeyOfEn(), DataType.getCurrentDate());
					}
					if (item.getMyDataType() == DataType.AppDateTime)
					{
						mydrMain.setValue(item.getKeyOfEn(), DataType.getCurrentDataTime());
					}
				}
				else
				{
					//如果是EnsName中字段
					if (en.GetValByKey(v.replace("@", "")) != null)
					{
						mydrMain.setValue(item.getKeyOfEn(), en.GetValByKey(v.replace("@", "")).toString());
					}

				}


			}

		}
		dtMain.Rows.add(mydrMain);
		ds.Tables.add(dtMain);

			/// 该方法的默认值.


			///加入该方法的外键.
		for (DataRow dr : mapAttrs.Rows)
		{
			String lgType = dr.getValue("LGType").toString();
			if (lgType.equals("2") == false)
			{
				continue;
			}

			String UIIsEnable = dr.getValue("UIVisible").toString();
			if (UIIsEnable.equals("0"))
			{
				continue;
			}

			String uiBindKey = dr.getValue("UIBindKey").toString();
			if (DataType.IsNullOrEmpty(uiBindKey) == true)
			{
				String myPK = dr.getValue("MyPK").toString();
				/*如果是空的*/
				//   throw new Exception("@属性字段数据不完整，流程:" + fl.getNo() + fl.Name + ",节点:" + nd.getNodeID() + nd.Name + ",属性:" + myPK + ",的UIBindKey IsNull ");
			}

			// 检查是否有下拉框自动填充。
			String keyOfEn = dr.getValue("KeyOfEn").toString();
			String fk_mapData = dr.getValue("FK_MapData").toString();
			if (ds.GetTableByName(uiBindKey) == null)
			{
				ds.Tables.add(PubClass.GetDataTableByUIBineKey(uiBindKey));
			}

		}

		//加入sql模式的外键.
		for (Attr attr : rm.getHisAttrs())
		{
			if (attr.getIsRefAttr() == true)
			{
				continue;
			}

			if (DataType.IsNullOrEmpty(attr.getUIBindKey()) || attr.getUIBindKey().length() <= 10)
			{
				continue;
			}

			if (attr.getUIIsReadonly() == true)
			{
				continue;
			}

			if (attr.getUIBindKey().contains("SELECT") == true || attr.getUIBindKey().contains("select") == true)
			{
				/*是一个sql*/
				Object tempVar2 = attr.getUIBindKey();
				String sqlBindKey = tempVar2 instanceof String ? (String)tempVar2 : null;
				sqlBindKey = bp.wf.Glo.DealExp(sqlBindKey, en, null);

				DataTable dt1 = DBAccess.RunSQLReturnTable(sqlBindKey);
				dt1.TableName = attr.getKey();

				//@杜. 翻译当前部分.
				if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
				{
					dt1.Columns.get("NO").setColumnName("No");
					dt1.Columns.get("NAME").setColumnName("Name");
				}
				if (ds.GetTableByName(attr.getKey()) == null)
				{
					ds.Tables.add(dt1);
				}

			}
		}


			/// 加入该方法的外键.


			///加入该方法的枚举.
		DataTable dtEnum = new DataTable();
		dtEnum.Columns.Add("Lab", String.class);
		dtEnum.Columns.Add("EnumKey", String.class);
		dtEnum.Columns.Add("IntKey", String.class);
		dtEnum.TableName = "Sys_Enum";

		for (MapAttr item : attrs.ToJavaList())
		{
			if (item.getLGType() != FieldTypeS.Enum)
			{
				continue;
			}

			SysEnums ses = new SysEnums(item.getUIBindKey());
			for (SysEnum se : ses.ToJavaList())
			{
				DataRow drEnum = dtEnum.NewRow();
				drEnum.setValue("Lab", se.getLab());
				drEnum.setValue("EnumKey", se.getEnumKey());
				drEnum.setValue("IntKey", se.getIntKey());
				dtEnum.Rows.add(drEnum);
			}

		}

		ds.Tables.add(dtEnum);

			/// 加入该方法的枚举.


			///增加该方法的信息
		DataTable dt = new DataTable();
		dt.TableName = "RM";
		dt.Columns.Add("Title", String.class);
		dt.Columns.Add("Warning", String.class);

		DataRow mydr = dt.NewRow();
		mydr.setValue("Title", rm.Title);
		mydr.setValue("Warning", rm.Warning);
		dt.Rows.add(mydr);

			/// 增加该方法的信息

		//增加到里面.
		ds.Tables.add(dt);

		return bp.tools.Json.ToJson(ds);
	}

	public final String Entitys_Init() throws Exception
	{
		//定义容器.
		DataSet ds = new DataSet();

		//查询出来从表数据.
		Entities dtls = ClassFactory.GetEns(this.getEnsName());
		Entity en = dtls.getGetNewEntity();
		QueryObject qo = new QueryObject(dtls);
		qo.addOrderBy(en.getPK());
		qo.DoQuery();
		ds.Tables.add(dtls.ToDataTableField("Ens"));

		//实体.
		Entity dtl = dtls.getGetNewEntity();
		//定义Sys_MapData.
		MapData md = new MapData();
		md.setNo(this.getEnName());
		md.setName( dtl.getEnDesc());


			///加入权限信息.
		//把权限加入参数里面.
		if (dtl.getHisUAC().IsInsert)
		{
			md.SetPara("IsInsert", "1");
		}
		if (dtl.getHisUAC().IsUpdate)
		{
			md.SetPara("IsUpdate", "1");
		}
		if (dtl.getHisUAC().IsDelete)
		{
			md.SetPara("IsDelete", "1");
		}

			/// 加入权限信息.


			///判断主键是否为自增长

		if (en.getIsNoEntity() == true && en.getEnMap().getIsAutoGenerNo())
		{
			md.SetPara("IsNewRow", "0");
		}
		else
		{
			md.SetPara("IsNewRow", "1");
		}

			///


			///添加EN的主键
		md.SetPara("PK", en.getPK());

			///

		ds.Tables.add(md.ToDataTableField("Sys_MapData"));


			///字段属性.
		MapAttrs attrs = dtl.getEnMap().getAttrs().ToMapAttrs();
		DataTable sys_MapAttrs = attrs.ToDataTableField("Sys_MapAttr");
		ds.Tables.add(sys_MapAttrs);

			/// 字段属性.


			///把外键与枚举放入里面去.
		for (DataRow dr : sys_MapAttrs.Rows)
		{
			String uiBindKey = dr.getValue("UIBindKey").toString();
			String lgType = dr.getValue("LGType").toString();
			if (lgType.equals("2") == false)
			{
				continue;
			}

			String UIIsEnable = dr.getValue("UIVisible").toString();
			if (UIIsEnable.equals("0"))
			{
				continue;
			}

			if (DataType.IsNullOrEmpty(uiBindKey) == true)
			{
				String myPK = dr.getValue("MyPK").toString();
				/*如果是空的*/
				//   throw new Exception("@属性字段数据不完整，流程:" + fl.getNo() + fl.Name + ",节点:" + nd.getNodeID() + nd.Name + ",属性:" + myPK + ",的UIBindKey IsNull ");
			}

			// 检查是否有下拉框自动填充。
			String keyOfEn = dr.getValue("KeyOfEn").toString();
			String fk_mapData = dr.getValue("FK_MapData").toString();


			// 判断是否存在.
			if (ds.GetTableByName(uiBindKey) !=null)
			{
				continue;
			}

			ds.Tables.add(PubClass.GetDataTableByUIBineKey(uiBindKey));
		}

		String enumKeys = "";
		for (Attr attr : dtl.getEnMap().getAttrs())
		{
			if (attr.getMyFieldType() == FieldType.Enum)
			{
				enumKeys += "'" + attr.getUIBindKey() + "',";
			}
		}

		if (enumKeys.length() > 2)
		{
			enumKeys = enumKeys.substring(0, enumKeys.length() - 1);

			String sqlEnum = "SELECT * FROM Sys_Enum WHERE EnumKey IN (" + enumKeys + ")";
			DataTable dtEnum = DBAccess.RunSQLReturnTable(sqlEnum);

			dtEnum.TableName = "Sys_Enum";

			if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
			{
				dtEnum.Columns.get("MYPK").setColumnName("MyPK");
				dtEnum.Columns.get("LAB").setColumnName("Lab");
				dtEnum.Columns.get("ENUMKEY").setColumnName("EnumKey");
				dtEnum.Columns.get("INTKEY").setColumnName("IntKey");
				dtEnum.Columns.get("LANG").setColumnName("Lang");
			}
			ds.Tables.add(dtEnum);
		}

			/// 把外键与枚举放入里面去.

		return bp.tools.Json.ToJson(ds);
	}



		///实体集合的保存.
	/** 
	 实体集合的删除
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Entities_Delete() throws Exception
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
	 * @throws Exception 
	*/
	public final String Entities_Save() throws Exception
	{
		try
		{

				/// 查询出来s实体数据.
			Entities dtls = ClassFactory.GetEns(this.getEnsName());
			Entity en = dtls.getGetNewEntity();
			QueryObject qo = new QueryObject(dtls);
			qo.DoQuery();
			Map map = en.getEnMap();
			for (Entity item : dtls)
			{
				String pkval = item.getPKVal().toString();

				for (Attr attr : map.getAttrs())
				{
					if (attr.getIsRefAttr() == true)
					{
						continue;
					}

					if (attr.getMyDataType() == DataType.AppDateTime || attr.getMyDataType() == DataType.AppDate)
					{
						if (attr.getUIIsReadonly() == false)
						{
							continue;
						}

						String val = this.GetValFromFrmByKey("TB_" + pkval + "_" + attr.getKey(), null);
						item.SetValByKey(attr.getKey(), val);
						continue;
					}


					if (attr.getUIContralType()== UIContralType.TB && attr.getUIIsReadonly() == false)
					{
						String val = this.GetValFromFrmByKey("TB_" + pkval + "_" + attr.getKey(), null);
						item.SetValByKey(attr.getKey(), val);
						continue;
					}

					if (attr.getUIContralType()== UIContralType.DDL && attr.getUIIsReadonly() == true)
					{
						String val = this.GetValFromFrmByKey("DDL_" + pkval + "_" + attr.getKey());
						item.SetValByKey(attr.getKey(), val);
						continue;
					}

					if (attr.getUIContralType()== UIContralType.CheckBok && attr.getUIIsReadonly() == true)
					{
						String val = this.GetValFromFrmByKey("CB_" + pkval + "_" + attr.getKey(), "-1");
						if (val.equals("-1"))
						{
							item.SetValByKey(attr.getKey(), 0);
						}
						else
						{
							item.SetValByKey(attr.getKey(), 1);
						}
						continue;
					}
				}

				item.Update(); //执行更新.
			}

				///  查询出来实体数据.


				///保存新加行.
			String newPKVal = this.GetRequestVal("NewPKVal");
			//没有新增行
			if (this.GetRequestValBoolen("InsertFlag") == false || DataType.IsNullOrEmpty(newPKVal) == true)
			{
				return "保存成功.";
			}

			String valValue = "";


			for (Attr attr : map.getAttrs())
			{

				if (attr.getMyDataType() == DataType.AppDateTime || attr.getMyDataType() == DataType.AppDate)
				{
					if (attr.getUIIsReadonly() == false)
					{
						continue;
					}

					valValue = this.GetValFromFrmByKey("TB_" + newPKVal + "_" + attr.getKey(), null);
					en.SetValByKey(attr.getKey(), valValue);
					continue;
				}

				if (attr.getUIContralType()== UIContralType.TB && attr.getUIIsReadonly() == false)
				{
					valValue = this.GetValFromFrmByKey("TB_" + newPKVal + "_" + attr.getKey());
					en.SetValByKey(attr.getKey(), valValue);
					continue;
				}

				if (attr.getUIContralType()== UIContralType.DDL && attr.getUIIsReadonly() == true)
				{
					valValue = this.GetValFromFrmByKey("DDL_" + newPKVal + "_" + attr.getKey());
					en.SetValByKey(attr.getKey(), valValue);
					continue;
				}

				if (attr.getUIContralType()== UIContralType.CheckBok && attr.getUIIsReadonly() == true)
				{
					valValue = this.GetValFromFrmByKey("CB_" + newPKVal + "_" + attr.getKey(), "-1");
					if (valValue.equals("-1"))
					{
						en.SetValByKey(attr.getKey(), 0);
					}
					else
					{
						en.SetValByKey(attr.getKey(), 1);
					}
					continue;
				}
			}

			if (en.getIsNoEntity())
			{
				if (en.getEnMap().getIsAutoGenerNo())
				{
					en.SetValByKey("No", en.GenerNewNoByKey("No"));
				}
			}

			try
			{
				if (en.getPKVal().toString().equals("0"))
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
				return ex.getMessage();
			}




				/// 保存新加行.

			return "保存成功.";
		}
		catch (RuntimeException ex)
		{
			return "err@" + ex.getMessage();
		}
	}

		///

		///获取批处理的方法.
	public final String Refmethod_BatchInt() throws Exception
	{
		String ensName = this.getEnsName();
		Entities ens = ClassFactory.GetEns(ensName);
		Entity en = ens.getGetNewEntity();
		RefMethods rms = en.getEnMap().getHisRefMethods();
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
			if (item.refMethodType != RefMethodType.Func)
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
				myurl = "../Comm/RefMethod.htm?Index=" + item.Index + "&EnName=" + en.toString() + "&EnsName=" + en.getGetNewEntities().toString() + "&PKVal=" + this.getPKVal();
			}

			DataRow dr = dt.NewRow();

			dr.setValue("No", item.Index);
			dr.setValue("Title", item.Title);
			dr.setValue("Tip", item.ToolTip);
			dr.setValue("Visable", item.Visable);
			dr.setValue("Warning", item.Warning);

			dr.setValue("RefMethodType", item.refMethodType.getValue());
			dr.setValue("RefAttrKey", item.RefAttrKey);
			dr.setValue("URL", myurl);
			dr.setValue("W", item.Width);
			dr.setValue("H", item.Height);
			dr.setValue("Icon", item.Icon);
			dr.setValue("IsCanBatch", item.IsCanBatch);
			dr.setValue("GroupName", item.GroupName);
			dt.Rows.add(dr);
		}

		return bp.tools.Json.ToJson(dt);
	}

		///

	public final String Refmethod_Done() throws Exception
	{
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		Entity en = ens.getGetNewEntity();
		String msg = "";

		String pk = this.getPKVal();

		if (pk.contains(",") == false)
		{
			/*批处理的方式.*/
			en.setPKVal(pk);

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

			en.setPKVal(mypk);
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
	public final String DoOneEntity(Entity en, int rmIdx) throws Exception
	{
		RefMethod rm = en.getEnMap().getHisRefMethods().get(rmIdx);
		rm.HisEn = en;
		int mynum = 0;
		for (Attr attr : rm.getHisAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}
			mynum++;
		}

		Object[] objs = new Object[mynum];

		int idx = 0;
		for (Attr attr : rm.getHisAttrs())
		{
			if (attr.getMyFieldType() == FieldType.RefText)
			{
				continue;
			}

			switch (attr.getUIContralType())
			{
				case TB:
					switch (attr.getMyDataType())
					{
						case bp.da.DataType.AppString:
						case bp.da.DataType.AppDate:
						case bp.da.DataType.AppDateTime:
							String str1 = this.GetValFromFrmByKey(attr.getKey());
							objs[idx] = str1;
							//attr.getDefaultVal()=str1;
							break;
						case bp.da.DataType.AppInt:
							int myInt = this.GetValIntFromFrmByKey(attr.getKey());
							objs[idx] = myInt;
							//attr.getDefaultVal()=myInt;
							break;
						case bp.da.DataType.AppFloat:
							float myFloat = this.GetValFloatFromFrmByKey(attr.getKey());
							objs[idx] = myFloat;
							//attr.getDefaultVal()=myFloat;
							break;
						case bp.da.DataType.AppDouble:
						case bp.da.DataType.AppMoney:
							BigDecimal myDoub = this.GetValDecimalFromFrmByKey(attr.getKey());
							objs[idx] = myDoub;
							//attr.getDefaultVal()=myDoub;
							break;
						case bp.da.DataType.AppBoolean:
							objs[idx] = this.GetValBoolenFromFrmByKey(attr.getKey());
							attr.setDefaultVal(false);
							break;
						default:
							throw new RuntimeException("没有判断的数据类型．");

					}
					break;
				case DDL:
					try
					{
						if (attr.getMyDataType() == DataType.AppString)
						{
							String str = this.GetValFromFrmByKey(attr.getKey());
							objs[idx] = str;
							attr.setDefaultVal(str);
						}
						else
						{
							int enumVal = this.GetValIntFromFrmByKey(attr.getKey());
							objs[idx] = enumVal;
							attr.setDefaultVal(enumVal);
						}

					}
					catch (java.lang.Exception e)
					{
						objs[idx] = null;
					}
					break;
				case CheckBok:
					objs[idx] = this.GetValBoolenFromFrmByKey(attr.getKey());

					attr.setDefaultVal(objs[idx].toString());

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

		/// 相关功能.


		/// 公共方法。
	public final String SFTable() throws Exception
	{
		SFTable sftable = new SFTable(this.GetRequestVal("SFTable"));
		DataTable dt = sftable.GenerHisDataTable();
		return bp.tools.Json.ToJson(dt);
	}
	/** 
	 获得一个实体的数据
	 
	 @return 
	 * @throws Exception 
	*/
	public final String EnsData() throws Exception
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
	 * @throws Exception 
	*/
	public final String SQLList() throws Exception
	{
		String sqlKey = this.GetRequestVal("SQLKey"); //SQL的key.
		String paras = this.GetRequestVal("Paras"); //参数. 格式为 @para1=paraVal@para2=val2

		bp.sys.xml.SQLList sqlXml = new bp.sys.xml.SQLList(sqlKey);

		//获得SQL
		String sql = sqlXml.getSQL();
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
		return bp.tools.Json.ToJson(dt);
	}
	public final String EnumList() throws Exception
	{
		SysEnums ses = new SysEnums(this.getEnumKey());
		return ses.ToJson();
	}

		///  公共方法。


		///执行方法.
	/** 
	 执行方法
	 
	 @param clsName 类名称
	 @param monthodName 方法名称
	 @param paras 参数，可以为空.
	 @return 执行结果
	 * @throws Exception 
	*/

	public final String Exec(String clsName, String methodName) throws Exception
	{
		return Exec(clsName, methodName, null);
	}


	public final String Exec(String clsName, String methodName, String paras) throws Exception {

		/// #region 处理 HttpHandler 类.
		if (clsName.contains(".HttpHandler.") == true) {
			// 创建类实体.
			Object tempVar = null;
			try {
				tempVar = java.lang.Class.forName("BP.WF.HttpHandler.DirectoryPageBase").newInstance();
			} catch (InstantiationException e) {
				String parasStr = "";
				while (getRequest().getParameterNames().hasMoreElements()) {
					String key = (String) bp.sys.Glo.getRequest().getParameterNames().nextElement();
					String val = bp.sys.Glo.getRequest().getParameter(key);
					parasStr += "@" + key + "=" + val;
				}
				e.printStackTrace();
				return "err@" + e.getMessage() + " 参数:" + parasStr;
			} catch (IllegalAccessException e) {
				String parasStr = "";
				while (getRequest().getParameterNames().hasMoreElements()) {
					String key = (String) bp.sys.Glo.getRequest().getParameterNames().nextElement();
					String val = bp.sys.Glo.getRequest().getParameter(key);
					parasStr += "@" + key + "=" + val;
				}
				e.printStackTrace();
				return "err@" + e.getMessage() + " 参数:" + parasStr;
			} catch (ClassNotFoundException e) {
				String parasStr = "";
				while (getRequest().getParameterNames().hasMoreElements()) {
					String key = (String) bp.sys.Glo.getRequest().getParameterNames().nextElement();
					String val = bp.sys.Glo.getRequest().getParameter(key);
					parasStr += "@" + key + "=" + val;
				}
				e.printStackTrace();
				return "err@" + e.getMessage() + " 参数:" + parasStr;
			}
			WebContralBase ctrl = (WebContralBase) ((tempVar instanceof WebContralBase) ? tempVar : null);
			ctrl.context = this.context;

			try {
				// 执行方法返回json.
				String data = ctrl.DoMethod(ctrl, methodName);
				return data;
			} catch (RuntimeException ex) {
				String parasStr = "";
				while (getRequest().getParameterNames().hasMoreElements()) {
					String key = (String)bp.sys.Glo.getRequest().getParameterNames().nextElement();
					String val =bp.sys.Glo.getRequest().getParameter(key);
					parasStr += "@" + key + "=" + val;
				}
				return "err@" + ex.getMessage() + " 参数:" + parasStr;
			}
		}

		/// #endregion 处理 page 类.

		/// #region 执行entity类的方法.
		try {
			// 创建类实体.
			Object tempVar2 = java.lang.Class.forName("bp.en.Entity").newInstance();
			Entity en = (Entity) ((tempVar2 instanceof Entity) ? tempVar2 : null);
			en.setPKVal(this.getPKVal());
			en.RetrieveFromDBSources();

			java.lang.Class tp = en.getClass();
			java.lang.reflect.Method mp = tp.getMethod(methodName);
			if (mp == null) {
				return "err@没有找到类[" + clsName + "]方法[" + methodName + "].";
			}

			// 执行该方法.
			Object[] myparas = null;
			Object tempVar3 = mp.invoke(this, myparas);
			String result = (String) ((tempVar3 instanceof String) ? tempVar3 : null); // 调用由此
																						// MethodInfo
																						// 实例反射的方法或构造函数。
			return result;
		} catch (RuntimeException ex) {
			return "err@执行实体类的方法错误:" + ex.getMessage();
		} catch (InstantiationException e) {
			String parasStr = "";
			while (getRequest().getParameterNames().hasMoreElements()) {
				String key = (String) bp.sys.Glo.getRequest().getParameterNames().nextElement();
				String val = bp.sys.Glo.getRequest().getParameter(key);
				parasStr += "@" + key + "=" + val;
			}
			return "err@" + e.getMessage() + " 参数:" + parasStr;
		} catch (IllegalAccessException e) {
			String parasStr = "";
			while (getRequest().getParameterNames().hasMoreElements()) {
				String key = (String) bp.sys.Glo.getRequest().getParameterNames().nextElement();
				String val = bp.sys.Glo.getRequest().getParameter(key);
				parasStr += "@" + key + "=" + val;
			}
			return "err@" + e.getMessage() + " 参数:" + parasStr;
		} catch (ClassNotFoundException e) {
			String parasStr = "";
			while (getRequest().getParameterNames().hasMoreElements()) {
				String key = (String) bp.sys.Glo.getRequest().getParameterNames().nextElement();
				String val = bp.sys.Glo.getRequest().getParameter(key);
				parasStr += "@" + key + "=" + val;
			}
			return "err@" + e.getMessage() + " 参数:" + parasStr;
		} catch (NoSuchMethodException e) {
			String parasStr = "";
			while (getRequest().getParameterNames().hasMoreElements()) {
				String key = (String) bp.sys.Glo.getRequest().getParameterNames().nextElement();
				String val = bp.sys.Glo.getRequest().getParameter(key);
				parasStr += "@" + key + "=" + val;
			}
			return "err@" + e.getMessage() + " 参数:" + parasStr;
		} catch (InvocationTargetException e) {
			String parasStr = "";
			while (getRequest().getParameterNames().hasMoreElements()) {
				String key = (String) bp.sys.Glo.getRequest().getParameterNames().nextElement();
				String val = bp.sys.Glo.getRequest().getParameter(key);
				parasStr += "@" + key + "=" + val;
			}
			return "err@" + e.getMessage() + " 参数:" + parasStr;
		}

		/// #endregion 执行entity类的方法.
	}

		///


		///数据库相关.
	/** 
	 运行SQL
	 
	 @return 返回影响行数
	*/
	public final String DBAccess_RunSQL()
	{
		String sql = this.GetRequestVal("SQL");
		sql = sql.replace("~", "'");
		sql = sql.replace("[%]", "%"); //防止URL编码

		return String.valueOf(DBAccess.RunSQL(sql));
	}
	/** 
	 运行SQL返回DataTable
	 
	 @return DataTable转换的json
	 * @throws Exception 
	*/
	public final String DBAccess_RunSQLReturnTable() throws Exception
	{
		String sql = this.GetRequestVal("SQL");
		sql = sql.replace("~", "'");
		sql = sql.replace("[%]", "%"); //防止URL编码

		sql = sql.replace("@WebUser.No", WebUser.getNo()); //替换变量.
		sql = sql.replace("@WebUser.Name", WebUser.getName()); //替换变量.
		sql = sql.replace("@WebUser.FK_Dept", WebUser.getFK_Dept()); //替换变量.
		sql = sql.replace("@WebUser.DeptParentNo)", WebUser.getDeptParentNo()); //替换变量.




///#warning zhoupeng把这个去掉了. 2018.10.24
		// sql = sql.replace("-", "%"); //为什么？

		sql = sql.replace("/#", "+"); //为什么？
		sql = sql.replace("/$", "-"); //为什么？


		if (sql.equals(null) || sql.equals(""))
		{
			return "err@查询sql为空";
		}
		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		//暂定
		if (SystemConfig.getAppCenterDBType() == DBType.Oracle || SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
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
				if (dt.Columns.get(realkey.toUpperCase()) != null)
				{
					dt.Columns.get(realkey.toUpperCase()).setColumnName( realkey);
				}
			}

		}

		return bp.tools.Json.ToJson(dt);
	}
	public final String RunUrlCrossReturnString()
	{
		String url = this.GetRequestVal("url");
		String strs = DataType.ReadURLContext(url, 9999);		
		return strs;
	}

		///

	//执行方法.
	public final String HttpHandler() throws Exception
	{
		//@樊雷伟 , 这个方法需要同步.

		//获得两个参数.
		String httpHandlerName = this.GetRequestVal("HttpHandlerName");
		httpHandlerName = httpHandlerName.replace("BP.WF.HttpHandler","bp.wf.httphandler");
		httpHandlerName = httpHandlerName.replace("BP.CCBill","bp.ccbill");
		String methodName = this.GetRequestVal("DoMethod");

		Object type = java.lang.Class.forName(httpHandlerName);
		if (type == null)
		{
			Object tempVar = ClassFactory.GetHandlerPage(httpHandlerName);
			WebContralBase en = tempVar instanceof WebContralBase ? (WebContralBase)tempVar : null;
			if (en == null)
			{
				return "err@页面处理类名[" + httpHandlerName + "],没有获取到，请检查拼写错误？";
			}
			en.context = this.context;
			return en.DoMethod(en, methodName);
		}
		else
		{
			Object tempVar2 = ((Class) type).newInstance();
			WebContralBase en = tempVar2 instanceof WebContralBase ? (WebContralBase)tempVar2 : null;
			//en.context = this.context;
			return en.DoMethod(en, methodName);
		}
	}

	/** 
	 当前登录人员信息
	 
	 @return 
	 * @throws Exception 
	*/
	public final String GuestUser_Init() throws Exception
	{
		Hashtable ht = new Hashtable();

		String userNo = GuestUser.getNo();
		if (DataType.IsNullOrEmpty(userNo) == true)
		{
			ht.put("No", "");
			ht.put("Name", "");
			return bp.tools.Json.ToJson(ht);
		}

		ht.put("No", GuestUser.getNo());
		ht.put("Name", GuestUser.getName());
		return bp.tools.Json.ToJson(ht);
	}

	/** 
	 当前登录人员信息
	 
	 @return 
	 * @throws Exception 
	*/
	public final String WebUser_Init() throws Exception
	{
		Hashtable ht = new Hashtable();

		String userNo = WebUser.getNo();
		if (DataType.IsNullOrEmpty(userNo) == true)
		{
			ht.put("No", "");
			ht.put("Name", "");
			ht.put("FK_Dept", "");
			ht.put("FK_DeptName", "");
			ht.put("FK_DeptNameOfFull", "");
			ht.put("Tel","");

			ht.put("CustomerNo", SystemConfig.getCustomerNo());
			ht.put("CustomerName", SystemConfig.getCustomerName());
			ht.put("IsAdmin", 0);
			ht.put("OrgNo", "");
			ht.put("OrgName","");
			return bp.tools.Json.ToJson(ht);
		}

		ht.put("No", WebUser.getNo());
		ht.put("Name", WebUser.getName());
		ht.put("FK_Dept", WebUser.getFK_Dept());
		ht.put("FK_DeptName", WebUser.getFK_DeptName());
		ht.put("FK_DeptNameOfFull", WebUser.getFK_DeptNameOfFull());
		ht.put("CustomerNo", SystemConfig.getCustomerNo());
		ht.put("CustomerName", SystemConfig.getCustomerName());
		ht.put("IsAdmin", WebUser.getIsAdmin() == true ? 1 : 0);
		ht.put("SID", WebUser.getSID());
		ht.put("Tel",WebUser.getTel());
		ht.put("OrgNo", WebUser.getOrgNo());
		ht.put("OrgName", WebUser.getOrgName());

		//检查是否是授权状态.
		if (WebUser.getIsAuthorize() == true)
		{
			ht.put("IsAuthorize", "1");
			ht.put("Auth", WebUser.getAuth());
			ht.put("AuthName", WebUser.getAuthName());
		}
		else
		{
			ht.put("IsAuthorize", "0");
		}

		//每次访问表很消耗资源.
		WFEmp emp = new WFEmp(WebUser.getNo());
		ht.put("Theme", emp.GetParaString("Theme"));

		//增加运行模式. add by zhoupeng 2020.03.10 适应saas模式.
		ht.put("CCBPMRunModel", SystemConfig.GetValByKey("CCBPMRunModel", "0"));

		return bp.tools.Json.ToJson(ht);
	}

	public final String WebUser_BackToAuthorize() throws Exception
	{
		bp.wf.Dev2Interface.Port_Login(WebUser.getAuth());
		return "登录成功";
	}




	

		///分组统计.
	/** 
	 获得分组统计的查询条件.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Group_MapBaseInfo() throws Exception
	{
		//获得
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		if (ens == null)
		{
			return "err@类名:" + this.getEnsName() + "错误";
		}

		Entity en = ens.getGetNewEntity();
		Map map = ens.getGetNewEntity().getEnMapInTime();

		Hashtable ht = new Hashtable();

		//把权限信息放入.
		UAC uac = en.getHisUAC();
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

		ht.put("EnDesc", en.getEnDesc()); //描述?
		ht.put("EnName", en.toString()); //类名?

		MapData mapData = new MapData();
		mapData.setNo(this.getEnsName());

			///查询条件
		//单据，实体单据
		if (mapData.RetrieveFromDBSources() != 0 && DataType.IsNullOrEmpty(mapData.getFK_FormTree()) == false)
		{
			//查询条件.
			ht.put("IsShowSearchKey", mapData.GetParaInt("IsSearchKey"));
			ht.put("SearchFields", mapData.GetParaString("RptStringSearchKeys"));

			//按日期查询.
			ht.put("DTSearchWay", mapData.GetParaInt("DTSearchWay"));
			ht.put("DTSearchLable", mapData.GetParaString("DTSearchLabel"));

		}
		else
		{
			if (map.IsShowSearchKey == true)
			{
				ht.put("IsShowSearchKey", 1);
			}
			else
			{
				ht.put("IsShowSearchKey", 0);
			}

			ht.put("SearchFields", map.SearchFields);
			ht.put("SearchFieldsOfNum", map.SearchFieldsOfNum);

			//按日期查询.
			ht.put("DTSearchWay", map.DTSearchWay.getValue());
			ht.put("DTSearchLable", map.DTSearchLable);
			ht.put("DTSearchKey", map.DTSearchKey);
		}

			///  查询条件

		//把map信息放入
		ht.put("PhysicsTable", map.getPhysicsTable());
		ht.put("CodeStruct", map.getCodeStruct());
		ht.put("CodeLength", map.getCodeLength());

		return bp.tools.Json.ToJson(ht);
	}

		///

	/** 
	 外键或者枚举的分组查询条件.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Group_SearchAttrs() throws Exception
	{
		//获得
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		if (ens == null)
		{
			return "err@类名错误:" + this.getEnsName();
		}

		Entity en = ens.getGetNewEntity();
		Map map = ens.getGetNewEntity().getEnMapInTime();

		DataSet ds = new DataSet();

		//构造查询条件集合.
		DataTable dt = new DataTable();
		dt.Columns.Add("Field");
		dt.Columns.Add("Name");
		dt.Columns.Add("MyFieldType");
		dt.TableName = "Attrs";

		AttrSearchs attrs = map.getSearchAttrs();
		for (AttrSearch item : attrs)
		{
			DataRow dr = dt.NewRow();
			dr.setValue("Field", item.Key);
			dr.setValue("Name", item.HisAttr.getDesc());
			dr.setValue("MyFieldType", item.HisAttr.getMyFieldType());
			dt.Rows.add(dr);
		}
		ds.Tables.add(dt);

		//把外键枚举增加到里面.
		for (AttrSearch item : attrs)
		{
			Attr attr = item.HisAttr;
			if (attr.getIsEnum() == true)
			{
				SysEnums ses = new SysEnums(attr.getUIBindKey());
				DataTable dtEnum = ses.ToDataTableField();
				dtEnum.TableName = item.Key;
				ds.Tables.add(dtEnum);
				continue;
			}

			if (attr.getIsFK() == true)
			{
				Entities ensFK = item.HisAttr.getHisFKEns();
				ensFK.RetrieveAll();

				DataTable dtEn = ensFK.ToDataTableField();
				dtEn.TableName = item.Key;

				ds.Tables.add(dtEn);
			}
			//绑定SQL的外键
			if (DataType.IsNullOrEmpty(attr.getUIBindKey()) == false && ds.GetTableByName(attr.getKey()) == null)
			{
				String sql = attr.getUIBindKey();
				DataTable dtSQl = null;
				//说明是实体类绑定的外部数据源
				if (sql.toUpperCase().contains("SELECT") == true)
				{
					//sql数据
					sql = bp.wf.Glo.DealExp(attr.getUIBindKey(), null, null);
					dtSQl = DBAccess.RunSQLReturnTable(sql);
				}
				else
				{
					dtSQl = PubClass.GetDataTableByUIBineKey(attr.getUIBindKey());
				}
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
				ds.Tables.add(dtSQl);
			}


		}

		return bp.tools.Json.ToJson(ds);
	}

	/** 
	获取分组的外键、枚举
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Group_ContentAttrs() throws Exception
	{
		//获得
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		if (ens == null)
		{
			return "err@类名错误:" + this.getEnsName();
		}

		Entity en = ens.getGetNewEntity();
		Map map = ens.getGetNewEntity().getEnMapInTime();
		Attrs attrs = map.getAttrs();
		DataTable dt = new DataTable();
		dt.Columns.Add("Field");
		dt.Columns.Add("Name");
		dt.Columns.Add("Checked");
		dt.TableName = "Attrs";

		//获取注册信心表
		UserRegedit ur = new UserRegedit(WebUser.getNo(), this.getEnsName() + "_Group");
		String reAttrs = this.GetRequestVal("Attrs");

		//判断是否已经选择分组
		boolean contentFlag = false;
		for (Attr attr : attrs)
		{
			if (attr.getUIContralType()== UIContralType.DDL)
			{
				DataRow dr = dt.NewRow();
				dr.setValue("Field", attr.getKey());
				dr.setValue("Name", attr.getDesc());

				// 根据状态 设置信息.
				if (ur.getVals().indexOf(attr.getKey()) != -1)
				{
					dr.setValue("Checked", "true");
					contentFlag = true;
				}
				dt.Rows.add(dr);
			}

		}

		if (contentFlag == false && dt.Rows.size() != 0)
		{
			dt.Rows.get(0).setValue("Checked","true");
		}

		return bp.tools.Json.ToJson(dt);
	}

	public final String Group_Analysis() throws Exception
	{
		//获得
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		if (ens == null)
		{
			return "err@类名错误:" + this.getEnsName();
		}

		Entity en = ens.getGetNewEntity();
		Map map = ens.getGetNewEntity().getEnMapInTime();
		DataSet ds = new DataSet();
		//// 查询出来关于它的活动列配置。
		//ActiveAttrs aas = new ActiveAttrs();
		//aas.RetrieveBy(ActiveAttrAttr.For, this.EnsName);

		//获取注册信息表
		UserRegedit ur = new UserRegedit(WebUser.getNo(), this.getEnsName() + "_Group");

		DataTable dt = new DataTable();
		dt.Columns.Add("Field");
		dt.Columns.Add("Name");
		dt.Columns.Add("Checked");

		dt.TableName = "Attrs";

		//如果不存在分析项手动添加一个分析项
		DataRow dtr = dt.NewRow();
		dtr.setValue("Field", "Group_Number");
		dtr.setValue("Name", "数量");
		dtr.setValue("Checked", "true");
		dt.Rows.add(dtr);

		DataTable ddlDt = new DataTable();
		ddlDt.TableName = "Group_Number";
		ddlDt.Columns.Add("No");
		ddlDt.Columns.Add("Name");
		ddlDt.Columns.Add("Selected");
		DataRow ddlDr = ddlDt.NewRow();
		ddlDr.setValue("No", "SUM");
		ddlDr.setValue("Name", "求和");
		ddlDr.setValue("Selected", "true");
		ddlDt.Rows.add(ddlDr);
		ds.Tables.add(ddlDt);

		for (Attr attr : map.getAttrs())
		{
			if (attr.getIsPK()|| attr.getIsNum() == false)
			{
				continue;
			}
			if (attr.getUIContralType() != UIContralType.TB)
			{
				continue;
			}
			if (attr.getUIVisible()== false)
			{
				continue;
			}
			if (attr.getMyFieldType() == FieldType.FK)
			{
				continue;
			}
			if (attr.getMyFieldType() == FieldType.Enum)
			{
				continue;
			}
			if (attr.getKey().equals("OID") || attr.getKey().equals("WorkID") || attr.getKey().equals("MID"))
			{
				continue;
			}


			//bool isHave = false;
			//// 有没有配置抵消它的属性。
			//foreach (ActiveAttr aa in aas)
			//{
			//    if (aa.AttrKey != attr.getKey())
			//        continue;
			//    DataRow dr = dt.NewRow();
			//    dr["Field"] = attr.getKey();
			//    dr["Name"] = attr.getDesc();

			//    // 根据状态 设置信息.
			//    if (ur.Vals.IndexOf(attr.getKey()) != -1)
			//        dr["Checked"] = "true";

			//    dt.Rows.add(dr);

			//    isHave = true;
			//}

			//if (isHave)
			//    continue;


			dtr = dt.NewRow();
			dtr.setValue("Field", attr.getKey());
			dtr.setValue("Name", attr.getDesc());


			// 根据状态 设置信息.
			if (ur.getVals().indexOf(attr.getKey()) != -1)
			{
				dtr.setValue("Checked", "true");
			}

			dt.Rows.add(dtr);

			ddlDt = new DataTable();
			ddlDt.Columns.Add("No");
			ddlDt.Columns.Add("Name");
			ddlDt.Columns.Add("Selected");
			ddlDt.TableName = attr.getKey();

			ddlDr = ddlDt.NewRow();
			ddlDr.setValue("No", "SUM");
			ddlDr.setValue("Name", "求和");
			if (ur.getVals().indexOf("@" + attr.getKey() + "=SUM") != -1)
			{
				ddlDr.setValue("Selected", "true");
			}
			ddlDt.Rows.add(ddlDr);

			ddlDr = ddlDt.NewRow();
			ddlDr.setValue("No", "AVG");
			ddlDr.setValue("Name", "求平均");
			if (ur.getVals().indexOf("@" + attr.getKey() + "=AVG") != -1)
			{
				ddlDr.setValue("Selected", "true");
			}
			ddlDt.Rows.add(ddlDr);

			if (this.getIsContainsNDYF())
			{
				ddlDr = ddlDt.NewRow();
				ddlDr.setValue("No", "AMOUNT");
				ddlDr.setValue("Name", "求累计");
				if (ur.getVals().indexOf("@" + attr.getKey() + "=AMOUNT") != -1)
				{
					ddlDr.setValue("Selected", "true");
				}
				ddlDt.Rows.add(ddlDr);
			}

			ds.Tables.add(ddlDt);


		}
		ds.Tables.add(dt);
		return bp.tools.Json.ToJson(ds);
	}

	public final String Group_Search() throws Exception
	{
		//获得
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		if (ens == null)
		{
			return "err@类名错误:" + this.getEnsName();
		}

		Entity en = ens.getGetNewEntity();
		Map map = ens.getGetNewEntity().getEnMapInTime();
		DataSet ds = new DataSet();

		//获取注册信息表
		UserRegedit ur = new UserRegedit(WebUser.getNo(), this.getEnsName() + "_Group");

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
		sumdr.setValue("NoShowSum", NoShowSum);
		showSum.Rows.add(sumdr);

		DataTable activeAttr = aas.ToDataTable();
		activeAttr.TableName = "ActiveAttr";
		ds.Tables.add(activeAttr);
		ds.Tables.add(showSum);

		return bp.tools.Json.ToJson(ds);
	}

	private DataSet GroupSearchSet(Entities ens, Entity en, Map map, UserRegedit ur, DataSet ds, ActiveAttrs aas) throws Exception
	{

		//查询条件
		//分组
		String groupKey = "";
		Attrs AttrsOfNum = new Attrs(); //列
		String Condition = ""; //处理特殊字段的条件问题。


		//根据注册表信息获取里面的分组信息
		String StateNumKey = ur.getVals().substring(ur.getVals().indexOf("@StateNumKey") + 1);

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
				dataType = attr.getMyDataType();
			}

			if (this.GetRequestVal("DDL_Aly_" + paras[0]) == null)
			{
				ActiveAttr aa = (ActiveAttr)aas.GetEnByKey(ActiveAttrAttr.AttrKey, paras[0]);
				if (aa == null)
				{
					continue;
				}

				Condition += aa.getCondition();
				if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
				{
					groupKey += " round ( cast (" + aa.getExp() + " as  numeric), 4)  \"" + paras[0] + "\",";
				}
				else
				{
					groupKey += " round (" + aa.getExp() + ", 4)  \"" + paras[0] + "\",";
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
							if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
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
						if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
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
							if (SystemConfig.getAppCenterDBType() == DBType.PostgreSQL)
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
		bp.da.Paras ps = new Paras();
		// 生成 sql.
		String selectSQL = "SELECT ";
		String groupBy = " GROUP BY ";
		Attrs AttrsOfGroup = new Attrs();

		String SelectedGroupKey = ur.getVals().substring(0, ur.getVals().indexOf("@StateNumKey")); // 为保存操作状态的需要。
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
		bp.sys.UserRegedit searchUr = new UserRegedit();
		searchUr.setMyPK(WebUser.getNo() + "_" + this.getEnsName() + "_SearchAttrs");
		searchUr.RetrieveFromDBSources();


			///查询条件
		//关键字查询
		String keyWord = searchUr.getSearchKey();
		AtPara ap = new AtPara(searchUr.getVals());
		MapData mapData = new MapData();
		mapData.setNo(this.getEnsName());
		boolean IsShowSearchKey = en.getEnMap().IsShowSearchKey;
		DTSearchWay dtSearchWay = map.DTSearchWay;
		String DTSearchKey = map.DTSearchKey;

		//单据，实体单据
		if (mapData.RetrieveFromDBSources() != 0 && DataType.IsNullOrEmpty(mapData.getFK_FormTree()) == false)
		{
			IsShowSearchKey = mapData.GetParaBoolen("IsSearchKey");
			dtSearchWay = DTSearchWay.forValue(mapData.GetParaInt("DTSearchWay"));
			DTSearchKey = mapData.GetParaString("DTSearchKey");
		}

		if (IsShowSearchKey && DataType.IsNullOrEmpty(keyWord) == false && keyWord.length() >= 1)
		{
			String whereLike = "";
			boolean isAddAnd = false;
			for (Attr attr : map.getAttrs())
			{
				if (attr.getIsNum())
				{
					continue;
				}
				if (attr.getIsRefAttr())
				{
					continue;
				}

				switch (attr.getField())
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
					whereLike += "     (" + attr.getField() + " LIKE '%" + keyWord + "%' ";
				}
				else
				{
					whereLike += "   OR   " + attr.getField() + " LIKE '%" + keyWord + "%'";
				}
			}
			whereLike += ")          ";
			where += whereLike;
		}
		else
		{
			where += "1=1";
		}



		//其余查询条件
		//时间
		if (dtSearchWay != DTSearchWay.None)
		{
			String dtFrom = searchUr.getDTFrom(); // this.GetTBByID("TB_S_From").Text.Trim().replace("/", "-");
			String dtTo = searchUr.getDTTo(); // this.GetTBByID("TB_S_To").Text.Trim().replace("/", "-");

			if (DataType.IsNullOrEmpty(dtTo) == true)
			{
				dtTo = DataType.getCurrentDate();
			}

			//按日期查询
			if (dtSearchWay == DTSearchWay.ByDate)
			{
				dtTo += " 23:59:59";
				if (DataType.IsNullOrEmpty(dtFrom) == true)
				{
					where += " and (" + DTSearchKey + " <= '" + dtTo + "')";
				}
				else
				{
					where += " and (" + DTSearchKey + " >= '" + dtFrom + "'";
					where += " and " + DTSearchKey + " <= '" + dtTo + "'";
					where += ")";
				}

			}

			if (dtSearchWay == DTSearchWay.ByDateTime)
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
				
				Date date = DateUtils.parse(dtFrom);
				date = DateUtils.addDay(date, -1);
				dtFrom = DateUtils.format(date, "yyyy-MM-dd") + " 24:00";



				if (dtTo.trim().length() < 11 || dtTo.trim().indexOf(' ') == -1)
				{
					dtTo += " 24:00";
				}
				if (DataType.IsNullOrEmpty(dtFrom) == true)
				{
					where += " and (" + DTSearchKey + " <= '" + dtTo + "')";
				}
				else
				{
					where += " and (" + DTSearchKey + " >= '" + dtFrom + "'";
					where += " and " + DTSearchKey + " <= '" + dtTo + "'";
					where += ")";
				}


			}
		}
		/** 获得查询数据.
		*/
		if (ap.getHisHT().keySet().size() > 0)
		{
			for (String str : ap.getHisHT().keySet())
			{
				Object val = ap.GetValStrByKey(str);
				if (val.equals("all"))
				{
					continue;
				}
				where += "  and " + str + "=" + SystemConfig.getAppCenterDBVarStr() + str;
				if (!str.equals("FK_NY"))
				{
					whereOfLJ += "  and " + str + " =" + SystemConfig.getAppCenterDBVarStr() + str;
				}

				myps.Add(str, val);

			}
			if (where.endsWith(" AND ") == true)
			{
				where = where.substring(0, where.length() - " AND ".length());
			}
			whereOfLJ = whereOfLJ.substring(0, whereOfLJ.length() - " AND ".length());
		}



			///

		if (where.equals(" WHERE "))
		{
			where = "" + Condition.replace("and", "");
			whereOfLJ = "" + Condition.replace("and", "");
		}
		else
		{
			where = where + Condition;
			whereOfLJ = whereOfLJ + Condition;
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
		String sql = selectSQL + groupKey + " FROM " + map.getPhysicsTable() + where + groupBy + orderby;


		myps.SQL=sql;
		//  物理表。

		DataTable dt2 = DBAccess.RunSQLReturnTable(myps);

		DataTable dt1 = dt2.clone();

		dt1.Columns.Add("IDX", Integer.class);


			///对他进行分页面

		int myIdx = 0;
		for (DataRow dr : dt2.Rows)
		{
			myIdx++;
			DataRow mydr = dt1.NewRow();
			mydr.setValue("IDX", myIdx);
			for (DataColumn dc : dt2.Columns)
			{
				mydr.setValue(dc.ColumnName, dr.getValue(dc.ColumnName));
			}
			dt1.Rows.add(mydr);
		}

			///


			///处理 Int 类型的分组列。
		DataTable dt = dt1.clone();
		dt.TableName = "GroupSearch";
		dt.Rows.clear();
		for (Attr attr : AttrsOfGroup)
		{
			dt.Columns.get(attr.getKey()).DataType = String.class;
		}
		for (DataRow dr : dt1.Rows)
		{
			dt.Rows.add(dr);
		}

			///

		// 处理这个物理表 , 如果有累计字段, 就扩展它的列。
		if (isHaveLJ)
		{
			// 首先扩充列.
			for (Attr attr : AttrsOfNum)
			{
				if (StateNumKey.indexOf(attr.getKey() + "=AMOUNT") == -1)
				{
					continue;
				}

				switch (attr.getMyDataType())
				{
					case DataType.AppInt:
						dt.Columns.Add(attr.getKey() + "Amount", Integer.class);
						break;
					default:
						dt.Columns.Add(attr.getKey() + "Amount", BigDecimal.class);
						break;
				}
			}

			// 添加累计汇总数据.
			for (DataRow dr : dt.Rows)
			{
				for (Attr attr : AttrsOfNum)
				{
					if (StateNumKey.indexOf(attr.getKey() + "=AMOUNT") == -1)
					{
						continue;
					}

					//形成查询sql.
					if (whereOfLJ.length() > 10)
					{
						sql = "SELECT SUM(" + attr.getKey() + ") FROM " + map.getPhysicsTable() + whereOfLJ + " AND ";
					}
					else
					{
						sql = "SELECT SUM(" + attr.getKey() + ") FROM " + map.getPhysicsTable() + " WHERE ";
					}

					for (Attr attr1 : AttrsOfGroup)
					{
						switch (attr1.getKey())
						{
							case "FK_NY":
								sql += " FK_NY <= '" + dr.getValue("FK_NY") + "' AND FK_ND='" + dr.getValue("FK_NY").toString().substring(0, 4) + "' AND ";
								break;
							case "FK_Dept":
								sql += attr1.getKey() + "='" + dr.getValue(attr1.getKey()) + "' AND ";
								break;
							case "FK_SJ":
							case "FK_XJ":
								sql += attr1.getKey() + " LIKE '" + dr.getValue(attr1.getKey()) + "%' AND ";
								break;
							default:
								sql += attr1.getKey() + "='" + dr.getValue(attr1.getKey()) + "' AND ";
								break;
						}
					}

					sql = sql.substring(0, sql.length() - "AND ".length());
					if (attr.getMyDataType() == DataType.AppInt)
					{
						dr.setValue(attr.getKey() + "Amount", DBAccess.RunSQLReturnValInt(sql, 0));
					}
					else
					{
						dr.setValue(attr.getKey() + "Amount", DBAccess.RunSQLReturnValDecimal(sql,new BigDecimal(0), 2));
					}
				}
			}
		}

		// 为表扩充外键
		for (Attr attr : AttrsOfGroup)
		{
			dt.Columns.Add(attr.getKey() + "T", String.class);
		}
		for (Attr attr : AttrsOfGroup)
		{
			/* 说明它是枚举类型 */
			if (attr.getMyFieldType() == FieldType.Enum && DataType.IsNullOrEmpty(attr.getUIBindKey()) == false)
			{
				for (DataRow dr : dt.Rows)
				{
					int val = 0;
					try
					{
						val = Integer.parseInt(dr.getValue(attr.getKey()).toString());
					}
					catch (java.lang.Exception e)
					{
						dr.setValue(attr.getKey() + "T", " ");
						continue;
					}
					SysEnum se = new SysEnum(attr.getUIBindKey(),val);
					dr.setValue(attr.getKey() + "T", se.getLab());
				}
				continue;
			}
			/**是外键*/
			if (attr.getMyFieldType() == FieldType.FK)
			{
				for (DataRow dr : dt.Rows)
				{
					Entity myen = attr.getHisFKEn();
					String val = dr.getValue(attr.getKey()).toString();
					myen.SetValByKey(attr.getUIRefKeyValue(), val);
					try
					{
						myen.Retrieve();
						dr.setValue(attr.getKey() + "T", myen.GetValStrByKey(attr.getUIRefKeyText()));
					}
					catch (java.lang.Exception e2)
					{
						if (val == null || val.length() <= 1)
						{
							dr.setValue(attr.getKey() + "T", val);
						}
						else if (val.substring(0, 2).equals("63"))
						{
							try
							{
								bp.port.Dept Dept = new bp.port.Dept(val);
								dr.setValue(attr.getKey() + "T", Dept.getName());
							}
							catch (java.lang.Exception e3)
							{
								dr.setValue(attr.getKey() + "T", val);
							}
						}
						else
						{
							dr.setValue(attr.getKey() + "T", val);
						}
					}
				}
				continue;
			}
			/**外部数据源*/
		   if (DataType.IsNullOrEmpty(attr.getUIBindKey()) == false)
		   {
				String sqlKey = attr.getUIBindKey();
				DataTable dtSQl = null;
				if (sqlKey.toUpperCase().contains("SELECT") == true)
				{
					//sql数据
					sqlKey = bp.wf.Glo.DealExp(attr.getUIBindKey(), null, null);
					dtSQl = DBAccess.RunSQLReturnTable(sqlKey);
				}
				else
				{
					dtSQl =PubClass.GetDataTableByUIBineKey(attr.getUIBindKey());
				}
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
				for (DataRow dr : dt.Rows)
				{
					Object val = dr.getValue(attr.getKey());
					if (val == null || DataType.IsNullOrEmpty(val.toString()) == true)
					{
						dr.setValue(attr.getKey() + "T", "");
						continue;
					}
					for (DataRow ddr : dtSQl.Rows)
					{
						if (ddr.getValue("No").toString().equals(val.toString()))
						{
							dr.setValue(attr.getKey() + "T", ddr.getValue("Name"));
							break;
						}
					}
				}
				continue;
		   }

		}
		ds.Tables.add(dt);
		ds.Tables.add(AttrsOfNum.ToMapAttrs().ToDataTableField("AttrsOfNum"));
		ds.Tables.add(AttrsOfGroup.ToMapAttrs().ToDataTableField("AttrsOfGroup"));
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
	 * @throws Exception 
	*/
	public final String Group_Exp() throws Exception
	{
		//获得
		Entities ens = ClassFactory.GetEns(this.getEnsName());
		if (ens == null)
		{
			return "err@类名错误:" + this.getEnsName();
		}

		Entity en = ens.getGetNewEntity();
		Map map = ens.getGetNewEntity().getEnMapInTime();
		DataSet ds = new DataSet();

		//获取注册信息表
		UserRegedit ur = new UserRegedit(WebUser.getNo(), this.getEnsName() + "_Group");

		// 查询出来关于它的活动列配置.
		ActiveAttrs aas = new ActiveAttrs();
		aas.RetrieveBy(ActiveAttrAttr.For, this.getEnsName());

		ds = GroupSearchSet(ens, en, map, ur, ds, aas);
		if (ds == null)
		{
			return "err@您没有选择分析的数据";
		}

		String filePath = ExportDGToExcel(ds, en.getEnDesc(), ur.getVals());


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



		///常用词汇功能开始
	/** 
	 常用词汇
	 
	 @return 
	 * @throws Exception 
	*/
	public final String HelperWordsData() throws Exception
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
			qo.AddWhere("FK_Emp", "=", WebUser.getNo());
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
			dr.setValue("DataCount", qo.GetCount());
			dt.Rows.add(dr);
			ds.Tables.add(dt);

			qo.DoQuery("MyPK", iPageSize, iPageNumber);


			//bp.tools.Json.ToJson(dvs.ToDataTableField("MainTable"));
			ds.Tables.add(dvs.ToDataTableField("MainTable")); //把描述加入.
		}
		if (lb.equals("hisWords"))
		{
			Node nd = new Node(this.getFK_Node());
			String rptNo = "ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt";
			if (nd.getHisFormType() == NodeFormType.SheetTree || nd.getHisFormType() == NodeFormType.RefOneFrmTree)
			{
				MapData mapData = new MapData(this.getFK_MapData());
				rptNo = mapData.getPTable();
			}


			GEEntitys ges = new GEEntitys(rptNo);
			QueryObject qo = new QueryObject(ges);
			String fk_emp = this.GetRequestVal("FK_Emp");
			qo.AddWhere(fk_emp, "=", WebUser.getNo());
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
			dr.setValue("DataCount", qo.GetCount());
			dt.Rows.add(dr);
			ds.Tables.add(dt);

			qo.DoQuery("OID", iPageSize, iPageNumber);

			dt = ges.ToDataTableField();
			DataTable newDt = new DataTable("MainTable");
			newDt.Columns.Add("CurValue");
			newDt.Columns.Add("MyPk");
			for (DataRow drs : dt.Rows)
			{
				if (DataType.IsNullOrEmpty(drs.getValue(AttrKey).toString()))
				{
					continue;
				}
				dr = newDt.NewRow();
				dr.setValue("CurValue", drs.getValue(AttrKey));
				dr.setValue("MyPK", drs.getValue("OID"));
				newDt.Rows.add(dr);
			}

			ds.Tables.add(newDt); //把描述加入.


		}

		return DataTableConvertJson.DataTable2Json(ds.GetTableByName("MainTable"), Integer.parseInt(ds.GetTableByName("DataCount").Rows.get(0).getValue(0).toString()));
		//return bp.tools.Json.ToJson(ds);
	}

	/** 
	 注意特殊字符的处理
	 
	 @return 
	*/
	private String readTxt() throws Exception {

		try {
			String path = SystemConfig.getPathOfDataUser() + "Fastenter\\" + getFK_MapData() + "\\"
					+ GetRequestVal("AttrKey");
			File folder = new File(path);
			if (folder.exists() == false)
				folder.mkdirs();

			File[] folderArray = folder.listFiles();
			if (folderArray.length == 0) {
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
			for (File file : folderArray) {
				if (count >= index && count < iPageSize * iPageNumber) {
					dt.Rows.get(count).setValue("MyPk", DBAccess.GenerGUID());

					fileName = file.getName().replace("\"", "").replace("'", "");

					BufferedReader reader = null;
					String tempString = null;
					try {
						reader = new BufferedReader(new FileReader(file));
						int line = 1;
						// 一次读入一行，直到读入null为文件结束
						while ((tempString = reader.readLine()) != null) {
							tempString += tempString;
							line++;
						}
						reader.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					liStr += String.format("%s},", DataTableConvertJson.GetFilteredStrForJSON(fileName),
							DataTableConvertJson.GetFilteredStrForJSON(tempString));

					dt.Rows.get(count).setValue("CurValue", DataTableConvertJson.GetFilteredStrForJSON(fileName));
					dt.Rows.get(count).setValue("TxtStr", DataTableConvertJson.GetFilteredStrForJSON(tempString));
				}
				count += 1;
			}

			ds.Tables.add(dt);
			dt = new DataTable("DataCount");
			dt.Columns.Add("DataCount", Integer.class);
			DataRow dr = dt.NewRow();
			dr.setValue("DataCount", folderArray.length);
			dt.Rows.add(dr);
			ds.Tables.add(dt);
			return bp.tools.Json.ToJson(ds);
		} catch (RuntimeException e) {
			return "";
		}

	}

		/// 常用词汇结束


		///前台SQL转移处理
	public final String RunSQL_Init()throws Exception
	{
		String sql = GetRequestVal("SQL");
		DBAccess.RunSQLReturnTable(sql);
		DataTable dt = new DataTable();
		return bp.tools.Json.ToJson(dt);
	}

		///

}