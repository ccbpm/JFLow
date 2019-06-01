package BP.Sys.FrmUI;

import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.EntityMyPK;
import BP.En.Map;
import BP.En.UAC;
import BP.Sys.MapAttrAttr;

public class ExtLink extends EntityMyPK
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
			}
			return uac;
		}
		/** 
		 超连接
		 
		*/
		public ExtLink()
		{
		}
		/** 
		 超连接
		 
		 @param mypk
		 * @throws Exception 
		*/
		public ExtLink(String mypk) throws Exception
		{
			this.setMyPK( mypk);
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
			Map map = new Map("Sys_MapAttr", "超连接");
			map.Java_SetDepositaryOfEntity(Depositary.None);
			map.Java_SetDepositaryOfMap(Depositary.Application);
			map.Java_SetEnType(EnType.Sys);

			///#region 通用的属性.
			map.AddMyPK();
			map.AddTBString(MapAttrAttr.FK_MapData, null, "表单ID", true, true, 1, 100, 20);
			map.AddTBString(MapAttrAttr.KeyOfEn, null, "字段", true, true, 1, 100, 20);
			map.AddDDLSQL(MapAttrAttr.GroupID, "0", "显示的分组", MapAttrString.SQLOfGroupAttr(), true);
			map.AddDDLSysEnum(MapAttrAttr.TextColSpan, 1, "文本单元格数量", true, true, "ColSpanAttrString", "@1=跨1个单元格@2=跨2个单元格@3=跨3个单元格@4=跨4个单元格");
			map.AddTBInt(MapAttrAttr.RowSpan, 1, "行数", true, false);
			///#endregion 通用的属性.

			///#region 个性化属性.
			map.AddTBString(MapAttrAttr.Name, "New Link", "标签", true, false, 0, 500, 20, true);
			map.AddTBString(MapAttrAttr.Tag1, "_blank", "连接目标(_blank,_parent,_self)", true, false, 0, 20, 20);
			map.AddTBString(MapAttrAttr.Tag2, null, "URL", true, false, 0, 500, 20, true);


			this.set_enMap(map);
			return this.getEnMap();
		}
	}

