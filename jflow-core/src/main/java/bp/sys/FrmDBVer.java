package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.*;
import bp.*;
import bp.en.Map;

import java.util.*;

/**
 数据版本
 */
public class FrmDBVer extends EntityMyPK
{

	///#region 基本属性
	/**
	 权限控制
	 */
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();
		uac.Readonly();
		uac.IsView = false;
		return uac;
	}
	public final String getTrackID() throws Exception
	{
		return this.GetValStringByKey(FrmDBVerAttr.TrackID);
	}
	public final void setTrackID(String value)  throws Exception
	{
		this.SetValByKey(FrmDBVerAttr.TrackID, value);
	}
	public final String getKeyOfEn() throws Exception
	{
		return this.GetValStringByKey(FrmDBVerAttr.KeyOfEn);
	}
	public final void setKeyOfEn(String value)  throws Exception
	{
		this.SetValByKey(FrmDBVerAttr.KeyOfEn, value);
	}
	public final int getVer() throws Exception
	{
		return this.GetValIntByKey(FrmDBVerAttr.Ver);
	}
	public final void setVer(int value)  throws Exception
	{
		this.SetValByKey(FrmDBVerAttr.Ver, value);
	}
	public final String getFrmID() throws Exception
	{
		return this.GetValStringByKey(FrmDBVerAttr.FrmID);
	}
	public final void setFrmID(String value)  throws Exception
	{
		this.SetValByKey(FrmDBVerAttr.FrmID, value);
	}
	/**
	 主键值键
	 */
	public final String getRefPKVal() throws Exception
	{
		return this.GetValStringByKey(FrmDBVerAttr.RefPKVal);
	}
	public final void setRefPKVal(String value)  throws Exception
	{
		this.SetValByKey(FrmDBVerAttr.RefPKVal, value);
	}
	/**
	 FK_Emp
	 */
	public final String getRecNo() throws Exception
	{
		return this.GetValStringByKey(FrmDBVerAttr.RecNo);
	}
	public final void setRecNo(String value)  throws Exception
	{
		this.SetValByKey(FrmDBVerAttr.RecNo, value);
	}
	public final String getRecName() throws Exception
	{
		return this.GetValStringByKey(FrmDBVerAttr.RecName);
	}
	public final void setRecName(String value)  throws Exception
	{
		this.SetValByKey(FrmDBVerAttr.RecName, value);
	}
	public final String getRDT() throws Exception
	{
		return this.GetValStringByKey(FrmDBVerAttr.RDT);
	}
	public final void setRDT(String value)  throws Exception
	{
		this.SetValByKey(FrmDBVerAttr.RDT, value);
	}


	///#endregion


	///#region 构造方法
	/**
	 数据版本
	 */
	public FrmDBVer()  {
	}
	public FrmDBVer(String mypk)throws Exception
	{
		this.setMyPK(mypk);
		this.Retrieve();
	}
	/**
	 EnMap
	 */
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Sys_FrmDBVer", "数据版本");
		map.AddMyPK();

		map.AddTBString(FrmDBVerAttr.FrmID, null, "表单ID", true, true, 0, 100, 20);
		map.AddTBString(FrmDBVerAttr.RefPKVal, null, "主键值", true, true, 0, 40, 20);

		map.AddTBString(FrmDBVerAttr.ChangeFields, null, "修改的字段", true, true, 0, 3900, 20);
		map.AddTBInt(FrmDBVerAttr.ChangeNum, 0, "修改的字段数量", true, true);


		map.AddTBString(FrmDBVerAttr.TrackID, null, "TrackID", true, true, 0, 40, 20);

		map.AddTBString(FrmDBVerAttr.RecNo, null, "记录人", true, true, 0, 30, 20);
		map.AddTBString(FrmDBVerAttr.RecName, null, "用户名", true, true, 0, 30, 20);
		map.AddTBDateTime(FrmDBVerAttr.RDT, null, "记录时间", true, true);

		map.AddTBInt(FrmDBVerAttr.Ver, 0, "版本号", true, true);
		map.AddTBString(FrmDBVerAttr.KeyOfEn, null, "章节字段有效", true, true, 0, 100, 20);

		this.set_enMap(map);
		return this.get_enMap();
	}

	///#endregion


	///#region 增加版本.

	/**
	 * 增加版本
	 * @param ver 版本号
	 * @param frmID 表单
	 * @param refPKVal 关联的OID
	 * @param trackID NDXXTrack 的MyPK
	 * @param jsonOfFrmDB 表单的JSON字符串
	 * @param frmDtlDB 从表的
	 * @param frmAthDB 附件的
	 * @throws Exception
	 */
	public static void AddFrmDBTrack(int ver,String frmID, String refPKVal, String trackID, String jsonOfFrmDB, String frmDtlDB,String frmAthDB) throws Exception{
		AddFrmDBTrack(ver,frmID, refPKVal, trackID, jsonOfFrmDB,frmDtlDB,frmAthDB, false);
	}
	public static void AddFrmDBTrack(int ver,String frmID, String refPKVal, String trackID, String jsonOfFrmDB, String frmDtlDB,String frmAthDB, boolean isChartFrm ) throws Exception {
		if (jsonOfFrmDB == null && isChartFrm == false)
			return;

		FrmDBVer en = new FrmDBVer();
		en.setFrmID(frmID);
		en.setRefPKVal(refPKVal);
		en.setTrackID(trackID);
		en.setVer(ver);
		en.Insert();

		//保存主表数据.
		if (DataType.IsNullOrEmpty(jsonOfFrmDB) == false)
			DBAccess.SaveBigTextToDB(jsonOfFrmDB, "Sys_FrmDBVer", "MyPK", en.getMyPK(), "FrmDB");

		//保存从表数据
		if(DataType.IsNullOrEmpty(frmDtlDB)==false)
			DBAccess.SaveBigTextToDB(frmDtlDB, "Sys_FrmDBVer", "MyPK", en.getMyPK(), "FrmDtlDB");

		//保存附件数据
		if (DataType.IsNullOrEmpty(frmAthDB) == false)
			DBAccess.SaveBigTextToDB(frmAthDB, "Sys_FrmDBVer", "MyPK", en.getMyPK(), "FrmAthDB");
	}
	/// <summary>
	/// 保存章节表单的字段数据
	/// </summary>
	/// <param name="frmID"></param>
	/// <param name="refPKVal"></param>
	/// <param name="trackID"></param>
	/// <param name="chartValue"></param>
	/// <param name="keyOfEn"></param>
	public static void AddKeyOfEnDBTrack(int ver,String frmID, String refPKVal, String trackID, String chartValue, String keyOfEn) throws Exception {

		FrmDBVer en = new FrmDBVer();
		en.setFrmID(frmID);
		en.setRefPKVal(refPKVal);
		en.setTrackID(trackID);
		en.setKeyOfEn(keyOfEn);
		en.setVer(ver);

		en.Insert();

		//保存章节表单字段的数据.
		DBAccess.SaveBigTextToDB(chartValue, "Sys_FrmDBVer", "MyPK", en.getMyPK(), "FrmDB");
	}
	@Override
	protected boolean beforeInsert() throws Exception {
		this.setMyPK(DBAccess.GenerGUID());
		this.setRDT(DataType.getCurrentDateTime());

		if (DataType.IsNullOrEmpty(this.getRecNo()) == true)
		{
			this.setRecNo(bp.web.WebUser.getNo());
			this.setRecName(bp.web.WebUser.getName());
		}

		return super.beforeInsert();
	}

	///#endregion 增加版本.


	///#region 重写

	public Entities getNewEntities()  {
		return new FrmDBVers();
	}

	///#endregion 重写
}