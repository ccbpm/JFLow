package bp.gpm.home;

import java.util.*;

import bp.en.EntitiesMyPK;
import bp.en.Entity;
import bp.web.WebUser;

/** 
	 信息块s
*/
	public class Windows extends EntitiesMyPK
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造
		/** 
		 信息块s
		*/
		public Windows()
		{
		}
		/** 
		 得到它的 Entity
		*/
		@Override
		public Entity getGetNewEntity()
		{
			return new Window();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
		/** 
		 转化成 java list,C#不能调用.
		 
		 @return List
		*/
		public final List<Window> ToJavaList()
		{
			return (List<Window>)(Object)this;
		}
		/** 
		 转化成list
		 
		 @return List
		*/
		public final ArrayList<Window> Tolist()
		{
			ArrayList<Window> list = new ArrayList<Window>();
			for (int i = 0; i < this.size(); i++)
			{
				list.add((Window)list.get(i));
			}
			return list;
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.

		@Override
		public int RetrieveAll() throws Exception
		{
			//查询全部模板.
			WindowTemplates templates = new WindowTemplates();
			templates.RetrieveAll();

			Windows windows = new Windows();
			windows.Retrieve(WindowAttr.EmpNo, WebUser.getNo(), "Idx");

			//检查是否有新增的？
			for (WindowTemplate en : templates.ToJavaList())
			{
				if (en.getIsEnable() == false)
				{
					continue; //如果是不启用的.
				}

				//从实例里面获取window..
				Object tempVar = windows.Filter(WindowAttr.WindowTemplateNo, en.getNo());
				Window window = tempVar instanceof Window ? (Window)tempVar : null;
				if (window == null)
				{
					/*不存在，就Insrt.*/
					window = new Window();
					window.setMyPK(en.getNo() + "_" + WebUser.getNo());
					window.setEmpNo(WebUser.getNo());
					window.setWindowTemplateNo(en.getNo());
					window.setIsEnable(true);
					window.Insert();
					continue;
				}

				//如果 个人设置 他是启用的.
				if (window.getIsEnable() == true)
				{
					en.setIdx(window.getIdx()); //就给他顺序号.
				}
				else
				{
					en.setIsEnable(false);
				}
			}


			//把模板放入到这里.
			this.AddEntities(templates);
			return templates.size();
		}
	}

