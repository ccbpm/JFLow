package BP.Demo;

import BP.DA.DBUrl;
import BP.DA.DBUrlType;
import BP.DA.Depositary;
import BP.En.AdjunctType;
import BP.En.EnType;
import BP.En.Entities;
import BP.En.EntityNoName;
import BP.En.Map;
import BP.En.UAC;

public class AnQuanItem extends EntityNoName
{
		//#region 构造函数
		@Override
		public UAC getHisUAC() throws Exception
		{
			UAC uac = new UAC();
			uac.OpenForSysAdmin();
			return uac;
		}
		/** 
		 安全项目
		*/
		public AnQuanItem()
		{
		}
		/** 
		 安全项目
		 
		 @param no
		 * @throws Exception 
		*/
		public AnQuanItem(String no) throws Exception
		{
			super(no);

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
			Map map = new Map();

			//#region 基本属性
			map.setEnDBUrl(new DBUrl(DBUrlType.AppCenterDSN));
			map.setPhysicsTable("Demo_AnQuanItem");
			map.setAdjunctType(AdjunctType.AllType);
			map.setDepositaryOfMap(Depositary.Application);
			map.setDepositaryOfEntity(Depositary.None);
			map.setIsCheckNoLength(false);
			map.setEnDesc("安全项目");
			map.setEnType(EnType.App);
			map.setCodeStruct("2");
			//#endregion

			//#region 字段
			map.AddTBStringPK(AnQuanItemAttr.No, null, "编号", true, false, 2, 2, 50);
			map.AddTBString(AnQuanItemAttr.Name, null, "名称", true, false, 0, 100, 200);
			//#endregion

			this.set_enMap(map);
			return this.get_enMap();
		}
		@Override
		public Entities getGetNewEntities()
		{
			return new AnQuanItems();
		}

}
