package BP.Sys;

import BP.En.EnType;
import BP.En.EntityMyPK;
import BP.En.Map;
import BP.En.UAC;
/** 
部门岗位对应 的摘要说明。

*/
public class EnVerDtl extends EntityMyPK{
	 
	

		///#region 基本属性
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
		 实体名称
		 
		*/
		public final String getEnName()
		{
			return this.GetValStringByKey(EnVerDtlAttr.EnName);
		}
		public final void setEnName(String value)
		{
			SetValByKey(EnVerDtlAttr.EnName, value);
		}
		/** 
		 字段
		 
		*/
		public final String getAttrKey()
		{
			return this.GetValStringByKey(EnVerDtlAttr.AttrKey);
		}

		public final void setAttrKey(String value)
		{
			SetValByKey(EnVerDtlAttr.AttrKey, value);
		}
		/** 
		 版本主表PK
		 
		*/
		public final String getEnVerPK()
		{
			return this.GetValStringByKey(EnVerDtlAttr.EnVerPK);
		}

		public final void setEnVerPK(String value)
		{
			SetValByKey(EnVerDtlAttr.EnVerPK, value);
		}
		/** 
		字段名
		 
		*/
		public final String getAttrName()
		{
			return this.GetValStringByKey(EnVerDtlAttr.AttrName);
		}
		public final void setAttrName(String value)
		{
			SetValByKey(EnVerDtlAttr.AttrName, value);
		}



		/** 
		 旧值
		 
		*/
		public final String getOldVal()
		{
			return this.GetValStringByKey(EnVerDtlAttr.OldVal);
		}
		public final void setOldVal(String value)
		{
			SetValByKey(EnVerDtlAttr.OldVal, value);
		}

		/** 
		 新值
		 
		*/
		public final String getNewVal()
		{
			return this.GetValStringByKey(EnVerDtlAttr.NewVal);
		}
		public final void setNewVal(String value)
		{
			SetValByKey(EnVerDtlAttr.NewVal, value);
		}

		/** 
		 版本号
		 
		*/
		public final int getEnVer()
		{
			return this.GetValIntByKey(EnVerDtlAttr.EnVer);
		}
		public final void setEnVer(int value)
		{
			SetValByKey(EnVerDtlAttr.EnVer, value);
		}


		public final String getRDT()
		{
			return this.GetValStringByKey(EnVerDtlAttr.RDT);
		}
		public final void setRDT(String value)
		{
			SetValByKey(EnVerDtlAttr.RDT, value);
		}
		public final String getRec()
		{
			return this.GetValStringByKey(EnVerDtlAttr.Rec);
		}
		public final void setRec(String value)
		{
			SetValByKey(EnVerDtlAttr.Rec, value);
		}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 扩展属性

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造函数
		/** 
		 工作部门岗位对应
		  
		*/
		public EnVerDtl()
		{
		}

		/** 
		 重写基类方法
		 
		*/
		@Override
		public Map getEnMap()
		{
			if (this.get_enMap() != null)
			{
				return this.get_enMap();
			}

			Map map = new Map("Sys_EnVerDtl", "实体修改明细");
			map.Java_SetEnType(EnType.Dot2Dot); //实体类型，admin 系统管理员表，PowerAble 权限管理表,也是用户表,你要想把它加入权限管理里面请在这里设置。。

			map.AddMyPK();
			map.AddTBString(EnVerDtlAttr.EnName, null, "实体名", true, false, 0, 200, 30);
			map.AddTBString(EnVerDtlAttr.EnVerPK, null, "版本主表PK", false, false, 0, 100, 100);
			map.AddTBString(EnVerDtlAttr.AttrKey, null, "字段", false, false, 0, 100, 1);
			map.AddTBString(EnVerDtlAttr.AttrName, null, "字段名", true, false, 0, 200, 30);
			map.AddTBString(EnVerDtlAttr.OldVal, null, "旧值", true, false, 0, 100, 30);
			map.AddTBString(EnVerDtlAttr.NewVal, null, "新值", true, false, 0, 100, 30);
				//map.AddTBString(EnVerDtlAttr.EnNo, null, "选中行编号", true, false, 0, 100, 30);
			map.AddTBInt(EnVerDtlAttr.EnVer, 1, "版本号(日期)", true, false);

			map.AddTBDateTime(EnVerDtlAttr.RDT, null, "日期", true, false);
			map.AddTBString(EnVerDtlAttr.Rec, null, "版本号", true, false, 0, 100, 30);

			this.set_enMap(map);
			return this.get_enMap();
		}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
	
}
