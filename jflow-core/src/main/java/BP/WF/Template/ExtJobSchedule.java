package BP.WF.Template;

import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.EntityMyPK;
import BP.En.Map;
import BP.En.UAC;
import BP.Sys.MapAttrAttr;
import BP.Sys.FrmUI.MapAttrString;

public class ExtJobSchedule extends EntityMyPK
{
		/** 
		 目标
		 
		*/
		public final String getTarget()
		{
			return this.GetValStringByKey(MapAttrAttr.Tag1);
		}
		public final void setTarget(String value)
		{
			this.SetValByKey(MapAttrAttr.Tag1, value);
		}
		/** 
		 URL
		 
		*/
		public final String getURL()
		{
			return this.GetValStringByKey(MapAttrAttr.Tag2).replace("#", "@");
		}
		public final void setURL(String value)
		{
			this.SetValByKey(MapAttrAttr.Tag2, value);
		}
		/** 
		 FK_MapData
		 
		*/
		public final String getFK_MapData()
		{
			return this.GetValStrByKey(MapAttrAttr.FK_MapData);
		}
		public final void setFK_MapData(String value)
		{
			this.SetValByKey(MapAttrAttr.FK_MapData, value);
		}
		/** 
		 Text
		 
		*/
		public final String getName()
		{
			return this.GetValStrByKey(MapAttrAttr.Name);
		}
		public final void setName(String value)
		{
			this.SetValByKey(MapAttrAttr.Name, value);
		}

		@Override
		public UAC getHisUAC() throws Exception
		{
			UAC uac = new UAC();
			uac.Readonly();
			if (BP.Web.WebUser.getNo().equals("admin"))
			{
				uac.IsUpdate = true;
				uac.IsDelete = true;
			}
			return uac;
		}
		/** 
		 流程进度图
		 
		*/
		public ExtJobSchedule()
		{
		}
		/** 
		 流程进度图
		 
		 @param mypk
		 * @throws Exception 
		*/
		public ExtJobSchedule(String mypk) throws Exception
		{
			this.setMyPK(mypk);
			this.Retrieve();
		}
		/** 
		 EnMap
		 
		*/
		@Override
		public Map getEnMap()
		{
			if (this.get_enMap() != null)
			{
				return this.get_enMap();
			}
			Map map = new Map("Sys_MapAttr", "流程进度图");
			map.Java_SetDepositaryOfEntity(Depositary.None);
			map.Java_SetDepositaryOfMap(Depositary.Application);
			map.Java_SetEnType(EnType.Sys);
			///#region 通用的属性.
			map.AddMyPK();
			map.AddTBString(MapAttrAttr.FK_MapData, null, "表单ID", true, true, 1, 100, 20);
			map.AddTBString(MapAttrAttr.KeyOfEn, null, "字段", true, true, 1, 100, 20);
			map.AddDDLSQL(MapAttrAttr.GroupID, "0", "显示的分组", MapAttrString.SQLOfGroupAttr(), true);

			map.AddTBInt(MapAttrAttr.UIHeight, 1, "高度", true, false);
			map.AddTBInt(MapAttrAttr.UIWidth, 1, "宽度", true, false);

			map.AddTBString(MapAttrAttr.Name, null, "名称", true, false, 0, 500, 20, true);

			this.set_enMap (map);
			return this.get_enMap();
		}
	}

