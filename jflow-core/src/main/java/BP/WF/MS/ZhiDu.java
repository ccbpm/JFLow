package BP.WF.MS;

import BP.En.EnType;
import BP.En.EntityNoName;
import BP.En.Map;
import BP.En.UAC;

/** 
 制度
*/
public class ZhiDu extends EntityNoName
{
		
	/** 
	 存储路径
	*/
	public final String getWebPath()
	{
		return this.GetValStringByKey(ZhiDuAttr.WebPath);
	}
	public final void setWebPath(String value)
	{
		this.SetValByKey(ZhiDuAttr.WebPath, value);
	}
	public final String getFK_Sort()
	{
		return this.GetValStringByKey(ZhiDuAttr.FK_Sort);
	}
	public final void setFK_Sort(String value)
	{
		this.SetValByKey(ZhiDuAttr.FK_Sort, value);
	}
	/** 
	 发布单位
	*/
	public final String getRelDept()
	{
		return this.GetValStringByKey(ZhiDuAttr.RelDept);
	}
	public final void setRelDept(String value)
	{
		this.SetValByKey(ZhiDuAttr.RelDept, value);
	}
	/** 
	 制度版本号
	*/
	public final String getZDVersion()
	{
		return this.GetValStringByKey(ZhiDuAttr.ZDVersion);
	}
	public final void setZDVersion(String value)
	{
		this.SetValByKey(ZhiDuAttr.ZDVersion, value);

	}
	/** 
	 文件属性
	*/
	public final String getZDProperty()
	{
		return this.GetValStringByKey(ZhiDuAttr.ZDProperty);
	}
	public final void setZDProperty(String value)
	{
		this.SetValByKey(ZhiDuAttr.ZDProperty, value);
	}
	/** 
	 制度编号
	*/
	public final String getZDNo()
	{
		return this.GetValStringByKey(ZhiDuAttr.ZDNo);
	}
	public final void setZDNo(String value)
	{
		this.SetValByKey(ZhiDuAttr.ZDNo, value);
	}
	/** 
	 判断子表是否包含记录
	*/
	public final boolean getHisHaveDtl()
	{
		ZhiDuDtl dtl = new ZhiDuDtl();
		return dtl.RetrieveByAttr("FK_Main", this.getNo());
	}
	//外部平台编号
	public final String getExternalNo()
	{
		return this.GetValStringByKey(ZhiDuAttr.ExternalNo);
	}
	public final void setExternalNo(String value)
	{
		this.SetValByKey(ZhiDuAttr.ExternalNo, value);
	}

	//流程编号
	public final String getOID()
	{
		return this.GetValStringByKey(ZhiDuAttr.OID);
	}
	public final void setOID(String value)
	{
		this.SetValByKey(ZhiDuAttr.OID, value);
	}
	/** 
	 是否删除
	*/
	public final boolean getIsDelete()
	{
		return this.GetValBooleanByKey(ZhiDuAttr.IsDelete);
	}
	public final void setIsDelete(boolean value)
	{
		this.SetValByKey(ZhiDuAttr.IsDelete, value);
	}
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		if (!BP.Web.WebUser.getNo().equals("admin"))
		{
			uac.IsView = false;
			return uac;
		}
		uac.IsDelete = false;
		uac.IsInsert = false;
		uac.IsUpdate = true;
		return uac;
	}


		///#endregion

		
	/** 
	 Main
	*/
	public ZhiDu()
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
		Map map = new Map("MS_ZhiDu", "制度");
		map.Java_SetEnType(EnType.Admin);

		map.AddTBStringPK(ZhiDuAttr.No, null, "编号", true, true, 5, 5, 5);
		map.AddTBString(ZhiDuAttr.Name, null, "名称", true, true, 0, 200, 5);
		map.AddTBString(ZhiDuAttr.FK_Sort, null, "类别", true, true, 0, 200, 4);
		map.AddTBString(ZhiDuAttr.WebPath, null, "路径", true, true, 0, 400, 4);
		map.AddTBString(ZhiDuAttr.RelDept, null, "发布单位", true, true, 0, 200, 4);
		map.AddTBString(ZhiDuAttr.ZDNo, null, "制度编号", true, true, 0, 200, 4);
		map.AddTBString(ZhiDuAttr.ZDVersion, null, "制度版本号", true, true, 0, 200, 4);
		map.AddTBString(ZhiDuAttr.ZDProperty, null, "文件属性", true, true, 0, 200, 4);
		map.AddTBString(ZhiDuAttr.ExternalNo, null, "外部系统编号", true, true, 0, 200, 4);
		map.AddTBString(ZhiDuAttr.OID, null, "流程编号", true, true, 0, 200, 4);
		map.AddBoolean(ZhiDuAttr.IsDelete,false,"是否作废",true,false);


			// map.AddTBString(MainAttr.FK_Sort, null, "制度类别", true, false, 0, 500, 10,true);
			// map.AddTBStringDoc(MainAttr.MainDoc, null, "制度内容(类别与内容支持变量)", true, false,true);
			// map.AddSearchAttr(MainAttr.RelDept);

		this.set_enMap(map);
		return this.get_enMap();
	}
		///#endregion
}