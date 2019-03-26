package BP.WF.Data;

import java.util.ArrayList;
import java.util.List;

import BP.En.Entities;
import BP.En.EntitiesMyPK;
import BP.En.Entity;
	/** 
	 逾期流程s
	 
	*/
	public class Delays extends EntitiesMyPK
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法属性
		/** 
		 逾期流程s
		 
		*/
		public Delays()
		{
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
		/** 
		 逾期流程
		 
		*/
		@Override
		public Entity getGetNewEntity()
		{
			return new Delay();
		}
		

		// 构造方法属性
		public static ArrayList<Delay> convertBills(Object obj)
		{
			return (ArrayList<Delay>) obj;
		}
		public List<Bill> ToJavaList()
		{
			return (List<Bill>)(Object)this;
		}
		
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
	}