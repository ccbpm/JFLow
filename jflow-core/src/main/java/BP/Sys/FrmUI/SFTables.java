package BP.Sys.FrmUI;

import java.util.ArrayList;
import java.util.List;

import BP.En.EntitiesNoName;
import BP.En.Entity;
import BP.Sys.SFTable;

/** 用户自定义表s

*/
	public class SFTables extends EntitiesNoName
	{

		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		///#region 构造
		/** 
		 用户自定义表s
		 
		*/
		public SFTables()
		{
		}
		/** 
		 得到它的 Entity
		 
		*/
		@Override
		public Entity getGetNewEntity()
		{
			return new SFTable();
		}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
		/** 
		 转化成 java list,C#不能调用.
		 
		 @return List
		*/
		public final List<SFTable> ToJavaList()
		{
			return (List<SFTable>)(Object)this;
		}
		/** 
		 转化成list
		 
		 @return List
		*/
		public final ArrayList<SFTable> Tolist()
		{
			ArrayList<SFTable> list = new ArrayList<SFTable>();
			for (int i = 0; i < this.size(); i++)
			{
				list.add((SFTable)this.get(i));
			}
			return list;
		}
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
	}
