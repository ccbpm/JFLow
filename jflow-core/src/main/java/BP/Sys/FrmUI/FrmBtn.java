package BP.Sys.FrmUI;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Sys.*;
import BP.Sys.*;
import java.util.*;

/** 
 按钮
*/
public class FrmBtn extends EntityMyPK
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 属性
	public final String getFK_MapData() throws Exception
	{
		return this.GetValStrByKey(FrmBtnAttr.FK_MapData);
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region 构造方法
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
		map.IndexField = MapAttrAttr.FK_MapData;


		map.AddMyPK();
		map.AddTBString(FrmBtnAttr.FK_MapData, null, "表单ID", true, false, 1, 100, 20);
		map.AddTBString(FrmBtnAttr.Text, null, "标签", true, false, 0, 3900, 20);

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
		BP.Sys.FrmBtn frmBtn = new BP.Sys.FrmBtn();
		frmBtn.setMyPK(this.getMyPK());
		frmBtn.RetrieveFromDBSources();
		frmBtn.Update();

		//调用frmEditAction, 完成其他的操作.
		BP.Sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());

		super.afterInsertUpdateAction();
	}

	/** 
	 删除后清缓存
	 * @throws Exception 
	*/
	@Override
	protected void afterDelete() throws Exception
	{
		//调用frmEditAction, 完成其他的操作.
		BP.Sys.CCFormAPI.AfterFrmEditAction(this.getFK_MapData());
		super.afterDelete();
	}


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}