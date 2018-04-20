package BP.Sys;

import java.util.ArrayList;

import BP.DA.Cash;
import BP.DA.DBType;
import BP.DA.DBUrl;
import BP.DA.DataType;
import BP.DA.Depositary;
import BP.DA.Paras;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.QueryObject;
import BP.Tools.StringHelper;

/** 
 纳税人集合 
*/
public class SysEnums extends Entities
{
	public static ArrayList<SysEnum> convertSysEnums(Object obj)
	{
		return (ArrayList<SysEnum>) obj;
	}
	/** 
	 此枚举类型的个数
	*/
	public int Num = -1;
	public final String ToDesc()
	{
		String strs = "";
		for (SysEnum se : this.ToJavaList())
		{
			strs += se.getIntKey() + " " + se.getLab() + ";";
		}
		return strs;
	}
	public final String GenerCaseWhenForOracle(String enName, String mTable, String key, String field, String enumKey, int def)
	{
		
		String sql = (String)Cash.GetObjFormApplication("ESQL" + enName +mTable+ key + "_" + enumKey, null);
		
		// string sql = "";
		if (sql != null)
		{
			return sql;
		}

		if (this.size() == 0)
		{
			//this.Retrieve(SysEnumAttr.EnumKey, key);
			
			//this.size() == 0
			throw new RuntimeException("@枚举值[" + enumKey + "]已被删除,无法生成期望的SQL.");
		}

		sql = " CASE NVL(" + mTable + field+","+def+")";
		for (SysEnum se1 : this.ToJavaList())
		{
			sql += " WHEN " + se1.getIntKey() + " THEN '" + se1.getLab() + "'";
		}

		SysEnum se = (SysEnum)this.GetEntityByKey(SysEnumAttr.IntKey, def);
		if (se == null)
		{
			sql += " END " + key + "Text";
		}
		else
		{
			sql += " WHEN NULL THEN '" + se.getLab() + "' END " + key + "TEXT";
		}

		Cash.AddObj("ESQL" + enName + mTable + key + "_" + enumKey, Depositary.Application, sql);
		return sql;
	}

	public final String GenerCaseWhenForOracle(String mTable, String key, String field, String enumKey, int def) throws Exception
	{
		
		if (this.size() == 0)
		{
			SysEnumMain sem=new SysEnumMain();
			sem.setNo(enumKey);
			if (sem.RetrieveFromDBSources()==1)
			{
				SysEnums ens=new SysEnums();
				ens.RegIt(enumKey, sem.getCfgVal());
				this.Retrieve(SysEnumAttr.EnumKey, enumKey);
				 
			}else
			{
			 	throw new RuntimeException("@枚举值（" + enumKey + "）已被删除，无法形成期望的SQL。");
			}
		}


		String sql = "";
		sql = " CASE " + mTable + field;
		for (SysEnum se1 : this.ToJavaList())
		{
			sql += " WHEN " + se1.getIntKey() + " THEN '" + se1.getLab() + "'";
		}

		SysEnum se = (SysEnum)this.GetEntityByKey(SysEnumAttr.IntKey, def);
		if (se == null)
		{
			sql += " END " + key + "Text";
		}
		else
		{
			sql += " WHEN NULL THEN '" + se.getLab() + "' END " + key + "TEXT";
		}

		// Cash.AddObj("ESQL" + enName + key + "_" + enumKey, Depositary.Application, sql);
		return sql;
	}
	public final void LoadIt(String enumKey) throws Exception
	{
		if (this.Full(enumKey) == false)
		{

			try
			{
				BP.DA.DBAccess.RunSQL("UPDATE Sys_Enum SET Lang='" + 	BP.Web.WebUser.getSysLang() + "' WHERE LANG IS NULL ");
				
				
				//增加数据库类型判断
				DBUrl dbUrl = new DBUrl();
				if(DBType.MSSQL == dbUrl.getDBType())
				{
					BP.DA.DBAccess.RunSQL("UPDATE Sys_Enum SET MyPK=EnumKey+'_'+Lang+'_'+cast(IntKey as NVARCHAR )");
				}else if(DBType.Oracle == dbUrl.getDBType())
				{
					BP.DA.DBAccess.RunSQL("UPDATE Sys_Enum SET MyPK = EnumKey || '_' || Lang || '_' || cast(IntKey  as VARCHAR(5))");
				}else if(DBType.MySQL == dbUrl.getDBType())
				{
					BP.DA.DBAccess.RunSQL("UPDATE Sys_Enum SET MyPK = CONCAT (EnumKey,'_', Lang,'_',CAST(IntKey AS CHAR(5)))");
				}
				
			}
			catch (java.lang.Exception e)
			{

			}

			try
			{
				SysEnumMain en =new SysEnumMain();
				en.setNo(enumKey);
				
				if (en.RetrieveFromDBSources()==1)
				{
					this.RegIt(enumKey, en.getCfgVal());
					return;
				}
				
				BP.Sys.XML.EnumInfoXml xml = new BP.Sys.XML.EnumInfoXml(enumKey);
				this.RegIt(enumKey, xml.getVals());
			}
			catch (RuntimeException ex)
			{
				throw new RuntimeException("@你没有预制[" + enumKey + "]枚举值。@在修复枚举值出现错误:" + ex.getMessage());
			}
		}
	}
	/** 
	 SysEnums
	 @param EnumKey
	 * @throws Exception 
	*/
	public SysEnums(String enumKey) throws Exception
	{
		this.LoadIt(enumKey);
	}
	public SysEnums(String enumKey, String vals) throws Exception
	{
		if (vals == null || vals.equals(""))
		{
			this.LoadIt(enumKey);
			return;
		}

		if (this.Full(enumKey) == false)
		{
			this.RegIt(enumKey, vals);
		}
	}
	public final void RegIt(String EnumKey, String vals) throws Exception
	{
		try
		{
			String[] strs = vals.split("[@]", -1);
			SysEnums ens = new SysEnums();
			ens.Delete(SysEnumAttr.EnumKey, EnumKey);
			this.clear();

			for (String s : strs)
			{
				if (s.equals("") || s == null)
				{
					continue;
				}

				String[] vk = s.split("[=]", -1);
				SysEnum se = new SysEnum();
				se.setIntKey(Integer.parseInt(vk[0]));
			   //杨玉慧
			   //解决当  枚举值含有 ‘=’号时，保存不进去的方法
				String[] kvsValues = new String[vk.length - 1];
				for (int i = 0; i < kvsValues.length; i++)
				{
					kvsValues[i] = vk[i + 1];
				}
				se.setLab(StringHelper.join("=", kvsValues));
				se.setEnumKey(EnumKey);
				se.setLang(BP.Web.WebUser.getSysLang());
				se.Insert();
				this.AddEntity(se);
			}
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException(ex.getMessage() + " - " + vals);
		}
		//  this.Full(EnumKey);
	}
	public final boolean Full(String enumKey) throws Exception
	{
		Entities ens = (Entities)Cash.GetObjFormApplication("EnumOf" + enumKey + BP.Web.WebUser.getSysLang(), null);
		if (ens != null)
		{
			this.AddEntities(ens);
			return true;
		}

		QueryObject qo = new QueryObject(this);
		qo.AddWhere(SysEnumAttr.EnumKey, enumKey);
	//	qo.addAnd();
	//	qo.AddWhere(SysEnumAttr.Lang, BP.Web.WebUser.getSysLang());
		
		qo.addOrderBy(SysEnumAttr.IntKey);
		if (qo.DoQuery() == 0)
		{
			// 看看xml配置里面是否有?
			return false;
		}

		Cash.AddObj("EnumOf" + enumKey + BP.Web.WebUser.getSysLang(), Depositary.Application, this);
		return true;
	}
	///// <summary>
	///// DBSimpleNoNames
	///// </summary>
	///// <returns></returns>
	//public DBSimpleNoNames ToEntitiesNoName()
	//{
	//    DBSimpleNoNames ens = new DBSimpleNoNames();
	//    foreach (SysEnum en in this)
	//    {
	//        ens.AddByNoName(en.IntKey.ToString(), en.Lab);
	//    }
	//    return ens;
	//}
	/** 
	 @param key
	 @param val
	 @return 
	*/
	public final int Delete(String key, Object val)
	{
		try
		{
			Entity en = this.getGetNewEntity();
			Paras ps = new Paras();

			ps.SQL = "DELETE FROM " + en.getEnMap().getPhysicsTable() + " WHERE " + key + "=" + en.getHisDBVarStr() + "p";
			ps.Add("p", val);
			return en.RunSQL(ps);
		}
		catch (java.lang.Exception e)
		{
			Entity en = this.getGetNewEntity();
			
			try {
				en.CheckPhysicsTable();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			Paras ps = new Paras();
			ps.SQL = "DELETE FROM " + en.getEnMap().getPhysicsTable() + " WHERE " + key + "=" + en.getHisDBVarStr() + "p";
			ps.Add("p", val);
			return en.RunSQL(ps);
		}
	}
	/** 
	 SysEnums
	*/
	public SysEnums()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new SysEnum();
	}
	/** 
	 通过int 得到Lab
	 
	 @param val val
	 @return string val
	*/
	public final String GetLabByVal(int val)
	{
		for(SysEnum en : this.ToJavaList())
		{
			if (en.getIntKey() == val)
			{
				return en.getLab();
			}
		}
		return null;
	}
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<SysEnum> ToJavaList()
	{
		return (java.util.List<SysEnum>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<SysEnum> Tolist()
	{
		java.util.ArrayList<SysEnum> list = new java.util.ArrayList<SysEnum>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((SysEnum)this.get(i));
		}
		return list;
	}
}