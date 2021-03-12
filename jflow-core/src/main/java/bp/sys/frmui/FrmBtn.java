package bp.sys.frmui;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.sys.*;
import bp.*;
import bp.sys.*;
import java.util.*;

/** 
 按钮
*/
public class FrmBtn extends EntityMyPK
{

		///属性
	public final String getFK_MapData() throws Exception
	{
		return this.GetValStrByKey(FrmBtnAttr.FK_MapData);
	}

		///


		///构造方法
	/** 
	 按钮
	*/
	public FrmBtn()
	{
	}
	/** 
	 按钮
	 
	 @param mypk
	*/
	public FrmBtn(String mypk)throws Exception
	{
		this.setMyPK(mypk);
		this.Retrieve();
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Sys_FrmBtn", "按钮");
		map.IndexField = MapAttrAttr.FK_MapData;


		map.AddMyPK();
		map.AddTBString(FrmBtnAttr.FK_MapData, null, "表单ID", true, false, 1, 100, 20);
		map.AddTBString(FrmBtnAttr.Lab, null, "标签", true, false, 0, 3900, 20);

		map.AddTBInt(FrmBtnAttr.IsView, 0, "是否可见", false, false);
		map.AddTBInt(FrmBtnAttr.IsEnable, 0, "是否起用", false, false);

		map.AddTBInt(FrmBtnAttr.UAC, 0, "控制类型", false, false);
		map.AddTBString(FrmBtnAttr.UACContext, null, "控制内容", false, false, 0, 3900, 20);

		map.AddDDLSysEnum(FrmBtnAttr.EventType, 0, "事件类型", true, true, FrmBtnAttr.EventType, "@0=禁用@1=执行URL@2=执行CCFromRef.js");
		map.AddTBString(FrmBtnAttr.EventContext, null, "事件内容", true, false, 0, 3900, 20);

		map.AddTBString(FrmBtnAttr.MsgOK, null, "运行成功提示", true, false, 0, 500, 20);
		map.AddTBString(FrmBtnAttr.MsgErr, null, "运行失败提示", true, false, 0, 500, 20);

		map.AddTBString(FrmBtnAttr.BtnID, null, "按钮ID", true, false, 0, 128, 20);

			//显示的分组.
		map.AddDDLSQL(FrmBtnAttr.GroupID, 0, "所在分组", "SELECT OID as No, Lab as Name FROM Sys_GroupField WHERE FrmID='@FK_MapData'", true);

		this.set_enMap(map);
		return this.get_enMap();
	}

	@Override
	protected void afterInsertUpdateAction() throws Exception
	{
		//在属性实体集合插入前，clear父实体的缓存.
		bp.sys.Glo.ClearMapDataAutoNum(this.getFK_MapData());

		bp.sys.FrmBtn frmBtn = new bp.sys.FrmBtn();
		frmBtn.setMyPK(this.getMyPK());
		frmBtn.RetrieveFromDBSources();
		frmBtn.Update();

		//调用frmEditAction, 完成其他的操作.
		bp.sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());

		super.afterInsertUpdateAction();
	}

	/** 
	 删除后清缓存
	*/
	@Override
	protected void afterDelete()throws Exception
	{
		//@sly
		MapAttr mapAttr = new MapAttr();
		mapAttr.setMyPK(this.getMyPK());
		if (mapAttr.RetrieveFromDBSources() != 0)
		{
			mapAttr.Delete();
		}

		//调用frmEditAction, 完成其他的操作.
		bp.sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());
		super.afterDelete();
	}



		///
}