package BP.Sys.FrmUI;

import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.EntityMyPK;
import BP.En.Map;
import BP.Sys.FrmBtnAttr;

/** 
按钮
*/
public class FrmBtn extends EntityMyPK
{
	//#region 构造方法
	/** 
	 按钮
	*/
	public FrmBtn()
	{
	}
	/** 
	 按钮
	 
	 @param mypk
	 * @throws Exception 
	*/
	public FrmBtn(String mypk) throws Exception
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

		Map map = new Map("Sys_FrmBtn", "按钮");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);

		map.AddMyPK();
		map.AddTBString(FrmBtnAttr.FK_MapData, null, "表单ID", true, false, 1, 100, 20);
		map.AddTBString(FrmBtnAttr.Text, null, "标签", true, false, 0, 3900, 20);

		map.AddTBInt(FrmBtnAttr.IsView, 0, "是否可见", false, false);
		map.AddTBInt(FrmBtnAttr.IsEnable, 0, "是否起用", false, false);

		map.AddTBInt(FrmBtnAttr.UAC, 0, "控制类型", false, false);
		map.AddTBString(FrmBtnAttr.UACContext, null, "控制内容", false, false, 0, 3900, 20);

		map.AddDDLSysEnum(FrmBtnAttr.EventType, 0, "事件类型", true, true,FrmBtnAttr.EventType,"@0=禁用@1=执行URL@2=执行CCFromRef.js");
		map.AddTBString(FrmBtnAttr.EventContext, null, "事件内容", true, false, 0, 3900, 20);

		map.AddTBString(FrmBtnAttr.MsgOK, null, "运行成功提示", true, false, 0, 500, 20);
		map.AddTBString(FrmBtnAttr.MsgErr, null, "运行失败提示", true, false, 0, 500, 20);

		map.AddTBString(FrmBtnAttr.GUID, null, "GUID", true, false, 0, 128, 20);

		//显示的分组.
		map.AddDDLSQL(FrmBtnAttr.GroupID, "0", "所在分组", MapAttrString.SQLOfGroupAttr(), true);

		this.set_enMap(map);
		return this.get_enMap();
	}
	@Override
	 protected final  void afterInsertUpdateAction() throws Exception
     {
         BP.Sys.FrmBtn frmBtn = new BP.Sys.FrmBtn();
         frmBtn.setMyPK(this.getMyPK());
         frmBtn.RetrieveFromDBSources();
         frmBtn.Update();

         super.afterInsertUpdateAction();
     }
}

