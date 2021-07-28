package bp.gpm.menu2020;

import java.util.ArrayList;
import java.util.List;

import bp.da.DataRow;
import bp.da.DataSet;
import bp.difference.SystemConfig;
import bp.en.EntitiesNoName;
import bp.en.Entity;
import bp.gpm.MenuCtrlWay;
import bp.sys.CCBPMRunModel;
import bp.web.WebUser;

/** 
	 系统s
*/
	public class MySystems extends EntitiesNoName
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
		/** 
		 系统s
		*/
		public MySystems()
		{
		}
		/** 
		 得到它的 Entity
		*/
		@Override
		public Entity getGetNewEntity()
		{
			return new MySystem();
		}
		@Override
		public int RetrieveAll() throws Exception
		{
			if (SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
			{
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java unless the Java 10 inferred typing option is selected:
				int i = super.RetrieveAll("Idx");
				if (i != 0)
				{
					return i;
				}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#region 初始化菜单.

				String file = SystemConfig.getPathOfData() + "\\XML\\AppFlowMenu.xml";
				DataSet ds = new DataSet();
				ds.readXml(file);

				//增加系统.
				for (DataRow dr : ds.GetTableByName("MySystem").Rows)
				{
					MySystem en = new MySystem();
					en.setNo(dr.get("No").toString());
					en.setName(dr.get("Name").toString());
					en.setIcon(dr.get("Icon").toString());
					en.setIsEnable(true);
					en.Insert();
				}

				//增加模块.
				for (DataRow dr : ds.GetTableByName("Module").Rows)
				{
					Module en = new Module();
					en.setNo(dr.get("No").toString());
					en.setName(dr.get("Name").toString());
					en.setSystemNo(dr.get("SystemNo").toString());
					en.setIcon(dr.get("Icon").toString());

					// en.MenuCtrlWay = 1; 
					//en.IsEnable = true;
					en.Insert();
				}

				//增加连接.
				for (DataRow dr : ds.GetTableByName("Item").Rows)
				{
					Menu en = new Menu();
					en.setNo(dr.get("No").toString());
					en.setName(dr.get("Name").toString());
					//   en.SystemNo = dr["SystemNo"].ToString();
					en.setModuleNo(dr.get("ModuleNo").toString());
					en.setUrlExt(dr.get("Url").toString());
					en.setIcon(dr.get("Icon").toString());
					en.setMenuCtrlWay(MenuCtrlWay.Anyone);
					//en.IsEnable = true;
					en.Insert();
				}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
				///#endregion 初始化菜单.

				return RetrieveAll();
			}

			//集团模式下的岗位体系: @0=每套组织都有自己的岗位体系@1=所有的组织共享一套岗则体系.
			if (SystemConfig.getGroupStationModel() == 1)
			{
				return super.RetrieveAll("Idx");
			}

			//按照orgNo查询.
			return this.Retrieve("OrgNo", WebUser.getOrgNo(), "Idx");
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
		/** 
		 转化成 java list,C#不能调用.
		 
		 @return List
		*/
		public final List<MySystem> ToJavaList()
		{
			return (List<MySystem>)(Object)this;
		}
		/** 
		 转化成list
		 
		 @return List
		*/
		public final ArrayList<MySystem> Tolist()
		{
			ArrayList<MySystem> list = new ArrayList<MySystem>();
			for (int i = 0; i < this.size(); i++)
			{
				list.add((MySystem)this.get(i));
			}
			return list;
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
	}