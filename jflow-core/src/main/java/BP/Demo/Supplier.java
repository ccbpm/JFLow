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

public class Supplier extends EntityNoName
{
	
		
		/** 
		 密码
		*/
		public final String getPWD()
		{
			return this.GetValStringByKey(SupplierAttr.PWD);
		}
		public final void setPWD(String value)
		{
			this.SetValByKey(SupplierAttr.PWD, value);
		}
		/** 
		 地址
		*/
		public final String getAddr()
		{
			return this.GetValStringByKey(SupplierAttr.Addr);
		}
		public final void setAddr(String value)
		{
			this.SetValByKey(SupplierAttr.Addr, value);
		}
		/** 
		 电话
		*/
		public final String getTel()
		{
			return this.GetValStringByKey(SupplierAttr.Tel);
		}
		public final void setTel(String value)
		{
			this.SetValByKey(SupplierAttr.Tel, value);
		}
		/** 
		 邮件
		*/
		public final String getEmail()
		{
			return this.GetValStringByKey(SupplierAttr.Email);
		}
		public final void setEmail(String value)
		{
			this.SetValByKey(SupplierAttr.Email, value);
		}
	
		///#endregion

	
		

		@Override
		public UAC getHisUAC()
		{
			UAC uac = new UAC();
			uac.OpenForSysAdmin();
			return uac;
		}
		/** 
		 供应商
		*/
		public Supplier()
		{
		}
		/** 
		 供应商
		 
		 @param no
		*/
		public Supplier(String no)
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

	
			
			map.setEnDBUrl(new DBUrl(DBUrlType.AppCenterDSN));
			map.setPhysicsTable("Demo_Supplier");
			map.setAdjunctType(AdjunctType.AllType);
			map.setDepositaryOfMap(Depositary.Application);
			map.setDepositaryOfEntity(Depositary.None);
			map.setIsCheckNoLength(false);
			map.setEnDesc("供应商");
			map.setEnType(EnType.App);
			map.setCodeStruct("4");
	
			///#endregion

	
			///#region 字段
			map.AddTBStringPK(SupplierAttr.No, null, "编号", true, false, 0, 50, 50);
			map.AddTBString(SupplierAttr.Name, null, "名称", true, false, 0, 50, 200);
			map.AddTBString(SupplierAttr.PWD, null, "密码", true, false, 0, 50, 200);

			map.AddTBString(SupplierAttr.Addr, null, "地址", true, false, 0, 50, 200);
			map.AddTBString(SupplierAttr.Tel, null, "电话", true, false, 0, 50, 200);
			map.AddTBString(SupplierAttr.Email, null, "邮件", true, false, 0, 50, 200);

	
			///#endregion

			this.set_enMap(map);
			return this.get_enMap();
		}
		/** 
		 获得他的实体集合
		*/
		@Override
		public Entities getGetNewEntities()
		{
			return new Suppliers();
		}
	
		///#endregion

		/** 
		 校验密码
		 
		 @param pwd
		 @return 
		*/
		public final boolean CheckPass(String pwd)
		{
			return this.getPWD().equals(pwd);
		}
	}
