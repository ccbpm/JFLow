package BP.GPM.AD;

import BP.*;
import BP.Port.*;
import BP.DA.*;
import BP.En.*;
import BP.Sys.*;
import BP.GPM.*;
import java.util.*;

/** 
 同步AD.
*/
public class Sync extends Method
{
	/** 
	 同步
	*/
	public Sync()
	{
		this.Title = "同步AD数据到组织结构.";
		this.Help = "手工同步数据到组织结构。";
	}
	@Override
	public void Init()
	{
	}
	/** 
	 是否可以处理？
	*/
	@Override
	public boolean getIsCanDo()
	{
		if (WebUser.getNo().equals("admin"))
		{
			return true;
		}
		return false;
	}


		///#region## 同步
	private ArrayList<AdModel> list = new ArrayList<AdModel>();

	private DirectoryEntry _DirectoryEntry = null;
	public final DirectoryEntry getDirectoryEntrBasePath()
	{
		if (_DirectoryEntry == null)
		{
			_DirectoryEntry = new DirectoryEntry(Glo.getADBasePath(), Glo.getADUser(), Glo.getADPassword());
		}
		return _DirectoryEntry;
	}

	private String rootPath = "";

	private String msg = "";
	/** 
	 功能:
	 
	 @param entryOU
	*/
	@Override
	public Object Do()
	{
		//同步并获取根目录.
		SyncDeptRoot();

		//同步所有的部门.
		SyncDept(Glo.getDirectoryEntryAppRoot()); //同步跟目录 PartentNo=0;

		//同步所有的人员.
		SyncEmps();

		//同步岗位.
		 SyncStatioins();
		return "执行成功.";
	}
	/** 
	 同步根目录
	 
	 @param root
	*/
	public final void SyncDept(DirectoryEntry root)
	{
		DirectorySearcher search = new DirectorySearcher(root); //查询组织单位.
		search.Filter = "(objectclass=organizationalUnit)";
		search.SearchScope = SearchScope.Subtree;
		SearchResultCollection results = search.FindAll();

		for (SearchResult result : results)
		{
			DirectoryEntry entry = result.GetDirectoryEntry();

			String name = entry.Name.replace("OU=", "");
			if (Glo.getADPath().contains("=" + name + ",") == true)
			{
				continue;
			}

			BP.GPM.AD.Dept dept = new Dept();
			dept.Name = name;
			dept.No = entry.Guid.toString();
			dept.setNameOfPath(entry.Path);
			if (dept.IsExits == true)
			{
				continue;
			}

			dept.setParentNo(entry.Parent.Guid.toString());
			dept.Idx = idxDept++;
			dept.Insert();
		}
		search.Dispose();
	}
	public final void SyncDeptRoot()
	{
		//删除现有的数据.
		BP.DA.DBAccess.RunSQL("DELETE FROM Port_Dept");

		DirectoryEntry rootDE = Glo.getDirectoryEntryAppRoot();

		BP.GPM.AD.Dept dept = new Dept();
		dept.Name = rootDE.Name.replace("OU=", "");
		dept.No = rootDE.Guid.toString();
		dept.setParentNo("0");
		dept.Idx = idxDept++;
		dept.setNameOfPath(rootDE.Path);
		dept.Insert();
		this.rootPath = rootDE.Path;
	}
	public final void SyncDeptRoot_del()
	{
		//删除现有的数据.
		BP.DA.DBAccess.RunSQL("DELETE FROM Port_Dept");

		//DirectorySearcher search = new DirectorySearcher(Glo.RootDirectoryEntry); //查询组织单位.
		//search.Filter = "(OU=" + Glo.ADRoot + ")";
		//search.SearchScope = SearchScope.Subtree;
		//SearchResult result = search.FindOne();
		//    rootDE  = result.GetDirectoryEntry();
		//    BP.GPM.AD.Dept dept = new Dept();
		//    dept.Name = rootDE.Name.Replace("OU=", "");
		//    dept.No = rootDE.Guid.ToString();
		//    dept.setParentNo("0");
		//    dept.Idx = idxDept++;
		//    dept.Insert();
		//    this.rootPath = rootDE.Path;
		//search.Dispose();
	}
	private String err = "";

		///#endregion

	public final void SyncEmps()
	{
		DBAccess.RunSQL("DELETE FROM Port_Emp");

		Depts depts = new Depts();
		depts.RetrieveAll();

		for (Dept mydept : depts.ToJavaList())
		{

			DirectoryEntry deptDE = new DirectoryEntry(mydept.getNameOfPath(), Glo.getADUser(), Glo.getADPassword());

			DirectorySearcher ds = new DirectorySearcher(deptDE);
			ds.SearchScope = SearchScope.OneLevel; //搜索当前..
			ds.Filter = "(objectClass=user)";

			SearchResultCollection rss = ds.FindAll();
			DBAccess.RunSQL("DELETE FROM Port_Emp WHERE FK_Dept='" + mydept.No + "'");
			if (rss.size() == 0)
			{
				continue;
			}

			BP.GPM.AD.Emp emp = new Emp();

			for (SearchResult result : rss)
			{
				DirectoryEntry entity = result.GetDirectoryEntry();
				if (entity.Name.contains("CN=") == false)
				{
					continue;
				}

				String name = entity.Name.replace("CN=", "");

				//判断是 group 还是 user.
				// emp.setNo (name;// this.GetValFromDirectoryEntryByKey(entry, "samaccountname");
				//emp.c = name;// this.GetValFromDirectoryEntryByKey(entry, "cn");

				emp.setNo (this.GetValFromDirectoryEntryByKey(entity, "sAMAccountName");
				emp.Name = this.GetValFromDirectoryEntryByKey(entity, "displayName");

				emp.SetValByKey("Manager", this.GetValFromDirectoryEntryByKey(entity, "Manager"));
				emp.SetValByKey("mobile", this.GetValFromDirectoryEntryByKey(entity, "mobile"));

				if (emp.IsExits == true)
				{
					continue;
				}

				emp.setFK_Dept(mydept.No); // entity.Parent.Guid.ToString();
				if (emp.No.Length > 20)
				{
					continue;
				}

				emp.setIdx(idxEmp++);
				emp.Insert();
			}
			//断开.
			ds.Dispose();
		}


		//增加 admin 
		BP.GPM.AD.Dept dept = new Dept();
		dept.Retrieve(BP.GPM.AD.DeptAttr.ParentNo, "0");

		BP.GPM.AD.Emp empAdmin = new Emp();
		empAdmin.No = "admin";
		empAdmin.Name = "admin";
		if (empAdmin.RetrieveFromDBSources() == 0)
		{
			empAdmin.setFK_Dept(dept.No);
			empAdmin.Insert();
		}
		else
		{
			empAdmin.setFK_Dept(dept.No);
			empAdmin.Update();
		}
	}
	/** 
	 同步岗位
	*/
	public final void SyncStatioins()
	{
		DirectorySearcher ds = new DirectorySearcher();
		ds.SearchRoot = Glo.getDirectoryEntryAppRoot();

		ds.SearchScope = SearchScope.Subtree; //搜索全部对象.
		ds.Filter = ("(objectClass=group)");

		//ds.Filter = "(&(objectClass=group)(cn=" + "YBS" + "))";  //YBS组名
		//ds.Filter = ("(objectCategory=YBS)(objectClass=user)") ;
		//. Find("ybs", "Group")) ;

		DBAccess.RunSQL("DELETE FROM Port_Station ");
		DBAccess.RunSQL("DELETE FROM Port_DeptEmpStation ");

		Station sta = new Station();
		BP.GPM.DeptEmpStation des = new DeptEmpStation();

		for (SearchResult result : ds.FindAll())
		{
			DirectoryEntry deGroup = result.GetDirectoryEntry(); // new DirectoryEntry(result.Path, Glo.ADUser, Glo.ADPassword, AuthenticationTypes.Secure);

			sta.No = deGroup.Guid.toString();

			String name = deGroup.Name;
			name = name.replace("CN=", "");
			sta.Name = name;
			sta.FK_StationType = "01";
			sta.DirectInsert();

			System.DirectoryServices.PropertyCollection pcoll = deGroup.Properties;
			int n = pcoll.get("member").size();
			for (int l = 0; l < n; l++)
			{
				try
				{
					DirectoryEntry deUser = new DirectoryEntry(Glo.getADBasePath() + "/" + pcoll.get("member").get(l).toString(), Glo.getADUser(), Glo.getADPassword());
					des.FK_Dept = deUser.Parent.Guid.toString();
					des.FK_Station = deGroup.Guid.toString(); //  result.GetDirectoryEntry()
					des.FK_Emp = this.GetValFromDirectoryEntryByKey(deUser, "sAMAccountName");
					if (des.FK_Emp.Length > 20)
					{
						continue;
					}
				}
				catch (RuntimeException ex)
				{
					err += "err@SyncStatioins 错误:" + ex.getMessage();
					continue;
				}
				des.Insert();
			}
		}

		//岗位类型.
		StationTypes typs = new StationTypes();
		typs.RetrieveAll();
		if (typs.size() == 0)
		{
			StationType st = new StationType();
			st.Name = "未分组";
			st.No = "01";
			st.Insert();
		}

	}

	public final String GetProperty(DirectoryEntry oDE, String PropertyName)
	{
		try
		{
			if (oDE.Properties.Contains(PropertyName))
			{
				return oDE.Properties[PropertyName][0].toString();
			}
			else
			{
				return "";
			}
		}
		catch (RuntimeException ee)
		{
			throw ee;
		}
	}



	public final String GetValFromDirectoryEntryByKey(DirectoryEntry en, String key)
	{
		return GetValFromDirectoryEntryByKey(en, key, "");
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public string GetValFromDirectoryEntryByKey(DirectoryEntry en, string key, string isNullAsVal = "")
	public final String GetValFromDirectoryEntryByKey(DirectoryEntry en, String key, String isNullAsVal)
	{
		if (en.Properties.Contains(key) == false)
		{
			return isNullAsVal;
		}

		PropertyValueCollection valueCollection = en.Properties[key];

		if (valueCollection.Value == null)
		{
			return isNullAsVal;
		}

		return valueCollection.Value.toString();
	}


		///#region## 同步根组织单位
	private String rootDeptNo = "";
	private int idxDept = 0;
	private int idxStation = 0;
	private int idxEmp = 0;
	/** 
	 功能: 同步根组织单位
	 创建人:Wilson
	 创建时间:2012-12-15
	 
	 @param entry
	*/
	private void SyncRootOU(DirectoryEntry entry)
	{
		msg += "<hr>开始同步:" + entry.Name;

		String myInfo = "";
		//foreach (string elmentName in entry.Properties.PropertyNames)
		//{
		//    PropertyValueCollection valueCollection = entry.Properties[elmentName];
		//    myInfo += "<br>KEY=" + elmentName + ",   " + valueCollection.Value; // +valueCollection[i].ToString();
		//}
		msg += " 属性：" + myInfo;

		//更目录.
		if (entry.Name.indexOf("DC=") == 0)
		{
			BP.GPM.AD.Dept dept = new Dept();
			dept.No = entry.Guid.toString();
			dept.Name = SystemConfig.CustomerShortName;
			if (dept.Name.equals(""))
			{
				dept.Name = SystemConfig.CustomerName;
			}

			if (dept.Name.equals(""))
			{
				dept.Name = "总部";
			}

			dept.setParentNo("0");
			if (dept.IsExits == true)
			{
				return;
			}

			dept.Idx = idxDept++;
			dept.Insert();

			for (DirectoryEntry item : entry.Children)
			{
				SyncRootOU(item);
			}
			return;
		}
		//组织解构,更新跟目录的.
		if (entry.Name.indexOf("OU=") == 0)
		{

			BP.GPM.AD.Dept dept = new Dept();
			dept.Name = entry.Name.replace("OU=", "");


			dept.No = entry.Guid.toString();
			dept.setParentNo(entry.Parent.Guid.toString());
			dept.Idx = idxDept++;
			dept.Insert();

			for (DirectoryEntry item : entry.Children)
			{
				SyncRootOU(item);
			}
			return;
		}

		//用户.
		if (entry.Name.indexOf("CN=") == 0)
		{
			String name = entry.Name.replace("CN=", "");
			String objectCategory = this.GetValFromDirectoryEntryByKey(entry, "objectCategory");

			if (objectCategory.contains("CN=Group") == true)
			{

				//判断是 group 还是 user.
				BP.GPM.Station station = new Station();
				// emp.setNo (name;// this.GetValFromDirectoryEntryByKey(entry, "samaccountname");
				station.No = entry.Guid.toString();
				station.Name = name; // this.GetValFromDirectoryEntryByKey(entry, "cn");
				// station.Idx = idxStation++;
				station.Insert();
				return;
			}
			else
			{
				//判断是 group 还是 user.
				BP.GPM.AD.Emp emp = new Emp();
				// emp.setNo (name;// this.GetValFromDirectoryEntryByKey(entry, "samaccountname");
				emp.setNo (name; // this.GetValFromDirectoryEntryByKey(entry, "cn");
				emp.Name = this.GetValFromDirectoryEntryByKey(entry, "displayName");

				if (emp.IsExits == true)
				{
					return;
				}

				emp.setFK_Dept(entry.Parent.Guid.toString());

				if (emp.No.Length > 20)
				{
					return;
				}

				emp.setIdx(idxEmp++);
				emp.Insert();
				return;
			}
		}
	}

	public final void SyncRootOU(DirectoryEntry en, String parentEn)
	{

	}

		///#endregion


		///#region## 同步下属组织单位及下属用户
	/** 
	 功能: 同步下属组织单位及下属用户
	 创建人:Wilson
	 创建时间:2012-12-15
	 
	 @param entry
	 @param parentId
	*/
	private void SyncSubOU(DirectoryEntry entry, String parentId)
	{
		for (DirectoryEntry subEntry : entry.Children)
		{
			String entrySchemaClsName = subEntry.SchemaClassName;

			String[] arr = subEntry.Name.split("[=]", -1);
			String categoryStr = arr[0];
			String nameStr = arr[1];
			String id = "";

			if (subEntry.Properties.Contains("objectGUID")) //SID
			{
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] bGUID = subEntry.Properties["objectGUID"][0] instanceof byte[] ? (byte[])subEntry.Properties["objectGUID"][0] : null;
				byte[] bGUID = subEntry.Properties["objectGUID"][0] instanceof byte[] ? (byte[])subEntry.Properties["objectGUID"][0] : null;

				id = BitConverter.toString(bGUID);
			}

			boolean isExist = tangible.ListHelper.exists(list, d = id.equals(> d.Id));

			switch (entrySchemaClsName)
			{
				case "organizationalUnit":

					if (!isExist)
					{
						list.add(new AdModel(id, nameStr, TypeEnum.OU.getValue(), parentId));
					}

					SyncSubOU(subEntry, id);
					break;
				case "user":
					String accountName = "";

					if (subEntry.Properties.Contains("samaccountName"))
					{
						accountName = subEntry.Properties["samaccountName"][0].toString();
					}

					if (!isExist)
					{
						list.add(new AdModel(id, accountName, TypeEnum.USER.getValue(), parentId));
					}
					break;
			}
		}
	}

		///#endregion
}