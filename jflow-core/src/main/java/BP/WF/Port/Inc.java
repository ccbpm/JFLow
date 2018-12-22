package BP.WF.Port;

import BP.DA.Depositary;
import BP.En.AdjunctType;
import BP.En.EntityNoName;
import BP.En.Map;
import BP.En.UAC;

/** 独立组织
	 
*/
	public class Inc extends EntityNoName
	{

		///#region 属性
		/** 
		 父节点编号
		 
		*/
		public final String getParentNo()
		{
			return this.GetValStrByKey(IncAttr.ParentNo);
		}
		public final void setParentNo(String value)
		{
			this.SetValByKey(IncAttr.ParentNo, value);
		}


		///#endregion


		///#region 构造函数
		/** 
		 独立组织
		 
		*/
		public Inc()
		{
		}
		/** 
		 独立组织
		 
		 @param no 编号
		 * @throws Exception 
		*/
		public Inc(String no) throws Exception
		{
			super(no);
		}

		///#endregion


		///#region 重写方法
		/** 
		 UI界面上的访问控制
		 * @throws Exception 
		 
		*/
		@Override
		public UAC getHisUAC() throws Exception
		{
			UAC uac = new UAC();
			uac.OpenForSysAdmin();
			return uac;
		}
		/** 
		 Map
		 
		*/
		@Override
		public Map getEnMap()
		{
			if (this.get_enMap() != null)
			{
				return this.get_enMap();
			}

			Map map = new Map("Port_Inc", "独立组织");

			map.Java_SetDepositaryOfEntity(Depositary.Application); //实体map的存放位置.
			map.Java_SetDepositaryOfMap(Depositary.Application); // Map 的存放位置.

			map.setAdjunctType(AdjunctType.None);

			map.AddTBStringPK(IncAttr.No, null, "编号", true, false, 1, 30, 40);
			map.AddTBString(IncAttr.Name, null,"名称", true, false, 0, 60, 200);
			map.AddTBString(IncAttr.ParentNo, null, "父节点编号", true, false, 0, 30, 40);

			this.set_enMap(map);
			return this.get_enMap();
		}

		///#endregion
	}