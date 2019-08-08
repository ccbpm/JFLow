package BP.WF.Data;

import java.util.ArrayList;
import java.util.List;
import BP.En.EntitiesMyPK;
import BP.En.Entity;
	/** 
	 逾期流程s
	 
	*/
	public class Delays extends EntitiesMyPK
	{
		///#region 构造方法属性
		/** 
		 逾期流程s
		 
		*/
		public Delays()
		{
		}
		///#endregion

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
		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
	}