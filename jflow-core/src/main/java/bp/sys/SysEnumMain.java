package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.*;
import bp.en.Map;

import java.util.*;

/** 
 SysEnumMain
*/
public class SysEnumMain extends EntityNoName
{

		///#region 实现基本的方法
	public final int isHaveDtl() throws Exception
	{
		return this.GetValIntByKey(SysEnumMainAttr.IsHaveDtl);
	}
	public final void setHaveDtl(int value)  throws Exception
	 {
		this.SetValByKey(SysEnumMainAttr.IsHaveDtl, value);
	}
	/** 
	 组织编号
	*/
	public final String getOrgNo() throws Exception
	{
		return this.GetValStrByKey(SysEnumMainAttr.OrgNo);
	}
	public final void setOrgNo(String value)  throws Exception
	 {
		this.SetValByKey(SysEnumMainAttr.OrgNo, value);
	}
	/** 
	 配置的值
	*/
	public final String getCfgVal() throws Exception
	{
		return this.GetValStrByKey(SysEnumMainAttr.CfgVal);
	}
	public final void setCfgVal(String value)  throws Exception
	 {
		this.SetValByKey(SysEnumMainAttr.CfgVal, value);
	}
	/** 
	 语言
	*/
	public final String getLang() throws Exception
	{
		return this.GetValStrByKey(SysEnumMainAttr.Lang);
	}
	public final void setLang(String value)  throws Exception
	 {
		this.SetValByKey(SysEnumMainAttr.Lang, value);
	}
	/** 
	 枚举值
	*/
	public final String getEnumKey() throws Exception
	{
		return this.GetValStrByKey(SysEnumMainAttr.EnumKey);
	}
	public final void setEnumKey(String value)  throws Exception
	 {
		this.SetValByKey(SysEnumMainAttr.EnumKey, value);
	}

		///#endregion


		///#region 构造方法
	/** 
	 SysEnumMain
	*/
	public SysEnumMain()  {
	}
	/** 
	 SysEnumMain
	 
	 param no
	*/
	public SysEnumMain(String no)throws Exception
	{
		try
		{
			this.setNo(no);
			this.Retrieve();
		}
		catch (RuntimeException ex)
		{
			SysEnums ses = new SysEnums(no);
			if (ses.size() == 0)
			{
				throw ex;
			}

			this.setNo(no);
			this.setName("未命名");
			String cfgVal = "";
			for (SysEnum item : ses.ToJavaList())
			{
				cfgVal += "@" + item.getIntKey() + "=" + item.getLab();
			}
			this.setCfgVal(cfgVal);
			this.Insert();
		}
	}
	@Override
	protected  void afterUpdate() throws Exception {
	//清除所有的缓存，这个位置会造成拼接SQL错误 case When
		bp.da.Cash.ClearCash();
		super.afterUpdate();
	}

	protected boolean beforeDelete() throws Exception {
		// 检查这个类型是否被使用？
		MapAttrs mattrs = new MapAttrs();
		QueryObject qo = new QueryObject(mattrs);
		qo.AddWhere(MapAttrAttr.UIBindKey, this.getNo());
		int i = qo.DoQuery();
		if (i == 0)
		{
			bp.sys.SysEnums ses = new SysEnums();
			ses.Delete(bp.sys.SysEnumAttr.EnumKey, this.getNo());
		}
		else
		{
			String msg = "错误:下列数据已经引用了枚举您不能删除它。"; // "错误:下列数据已经引用了枚举您不能删除它。";
			for (MapAttr attr : mattrs.ToJavaList())
			{
				msg += "\t\n" + attr.getField() + "" + attr.getName() + " Table = " + attr.getFK_MapData();
			}

			//抛出异常，阻止删除.
			throw new RuntimeException(msg);
		}
		return super.beforeDelete();
	}

	/** 
	 Map
	*/
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_EnumMain", "枚举");

			/*
			 * 为了能够支持cloud 我们做了如下变更.
			 * 1. 增加了OrgNo, EnumKey 字段.
			 * 2. 如果是单机版用户,原来的业务逻辑不变化.
			 * 3. 如果是SAAS模式, No=  OrgNo+"_"+EnumKey ;
			 */

		map.AddTBStringPK(SysEnumMainAttr.No, null, "编号", true, false, 1, 40, 8);
		map.AddTBString(SysEnumMainAttr.Name, null, "名称", true, false, 0, 40, 8);
		map.AddTBString(SysEnumMainAttr.CfgVal, null, "配置信息", true, false, 0, 1500, 8);
		map.AddTBString(SysEnumMainAttr.Lang, "CH", "语言", true, false, 0, 10, 8);

			//枚举值.
		map.AddTBString(SysEnumMainAttr.EnumKey, null, "EnumKey", true, false, 0, 40, 8);
			//组织编号.
		map.AddTBString(SysEnumMainAttr.OrgNo, null, "OrgNo", true, false, 0, 50, 8);

		map.AddTBInt(SysEnumMainAttr.IsHaveDtl, 0, "是否有子集?", true, false);

			//参数.
		map.AddTBString(SysEnumMainAttr.AtPara, null, "AtPara", true, false, 0, 200, 8);

		for (int i = 0; i < 30; i++)
		{
			map.AddTBString("Idx" + i, null, "EnumKey", false, false, 0, 50, 8);
			map.AddTBString("Val" + i, null, "枚举值", false, false, 0, 500, 400);
		}

		this.set_enMap(map);
		return this.get_enMap();
	}
	@Override
	protected void afterDelete() throws Exception {
		//检查是否有，父表？
		String parentKey = this.GetParaString("ParentKey");
		if (DataType.IsNullOrEmpty(parentKey) == false)
		{
			SysEnumMain en = new SysEnumMain(parentKey);
			en.setHaveDtl(0);
			en.SetValByKey(SysEnumMainAttr.AtPara, "");
			en.Update();
		}

		super.afterDelete();
	}
	@Override
	protected boolean beforeInsert() throws Exception {
		if (DataType.IsNullOrEmpty(this.getNo()) == true)
		{
			if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.Single)
			{
				this.setNo(this.getEnumKey());
			}
			else
			{
				this.setNo(bp.web.WebUser.getOrgNo() + "_" + this.getEnumKey());
			}
		}

		return super.beforeInsert();
	}


	@Override
	protected boolean beforeUpdateInsertAction() throws Exception {
		if (bp.difference.SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single)
		{
			this.setOrgNo(bp.web.WebUser.getOrgNo());
		}

		return super.beforeUpdateInsertAction();
	}

		///#endregion


		///#region 业务操作。
	public final String DoInitDtls() throws Exception {
		if (DataType.IsNullOrEmpty(this.getCfgVal()) == true)
		{
			return "err@没有cfg数据.";
		}

		//首先删除原来的数据.
		SysEnums ses = new SysEnums();
		ses.Delete(SysEnumAttr.EnumKey, this.getNo());

		//创建对象.
		SysEnum se = new SysEnum();

		//CfgVal = @0=病假@1=事假
		String[] strs = this.getCfgVal().split("[@]", -1);
		for (String str : strs)
		{
			String[] kvs = str.split("[=]", -1);

			se.setEnumKey(this.getNo());
			se.setIntKey(Integer.parseInt(kvs[0]));
			se.setLab(kvs[1].trim());
			se.setLang("CH");
			se.Insert();
			//   se.setMyPK(this.No+"_"+se
		}

		return "执行成功.";
	}

		///#endregion 业务操作。

}