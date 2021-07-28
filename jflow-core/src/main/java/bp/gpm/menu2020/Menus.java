package bp.gpm.menu2020;

import java.util.*;

import bp.difference.SystemConfig;
import bp.en.EntitiesNoName;
import bp.en.Entity;
import bp.sys.CCBPMRunModel;
import bp.web.WebUser;

public class Menus extends EntitiesNoName
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
		/** 
		 菜单s
		*/
		public Menus()
		{
		}
		/** 
		 得到它的 Entity
		*/
		@Override
		public Entity getGetNewEntity()
		{
			return new Menu();
		}
		@Override
		public int RetrieveAll() throws Exception
		{
			if (SystemConfig.getCCBPMRunModel() != CCBPMRunModel.SAAS)
			{
				return super.RetrieveAll("Idx");
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
		public final List<Menu> ToJavaList()
		{
			return (List<Menu>)(Object)this;
		}
		/** 
		 转化成list
		 
		 @return List
		*/
		public final ArrayList<Menu> Tolist()
		{
			ArrayList<Menu> list = new ArrayList<Menu>();
			for (int i = 0; i < this.size(); i++)
			{
				list.add((Menu)this.get(i));
			}
			return list;
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}
