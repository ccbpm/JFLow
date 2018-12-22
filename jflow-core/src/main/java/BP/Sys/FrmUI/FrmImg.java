package BP.Sys.FrmUI;

import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.EntityMyPK;
import BP.En.Map;
import BP.En.UAC;
import BP.Sys.FrmBtnAttr;
import BP.Sys.FrmImgAttr;
import BP.Sys.MapAttrAttr;

/** 
装饰图片
*/
public class FrmImg extends EntityMyPK
{

		///#region 构造方法
	/** 
	 控制权限
	*/
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.IsInsert = false;
		uac.IsUpdate = true;
		uac.IsDelete = true;
		return uac;
	}
	/** 
	 装饰图片
	*/
	public FrmImg()
	{
	}
	/** 
	 装饰图片
	 
	 @param mypk
	 * @throws Exception 
	*/
	public FrmImg(String mypk) throws Exception
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
		Map map = new Map("Sys_FrmImg", "装饰图片");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);

		map.AddMyPK();

		map.AddDDLSysEnum(FrmImgAttr.ImgSrcType, 0, "装饰图片来源", true, true, FrmImgAttr.ImgSrcType, "@0=本地@1=URL");

		map.AddTBString(FrmImgAttr.ImgURL, null, "装饰图片URL", true, false, 0, 200, 20);
		map.AddTBString(FrmImgAttr.ImgPath, null, "装饰图片路径", true, false, 0, 200, 20);

		map.AddTBString(FrmImgAttr.LinkURL, null, "连接到URL", true, false, 0, 200, 20);
		map.AddTBString(FrmImgAttr.LinkTarget, "_blank", "连接目标", true, false, 0, 200, 20);

		//如果是 seal 就是岗位集合。
		map.AddTBString(FrmImgAttr.Tag0, null, "参数", true, false, 0, 500, 20);
		map.AddTBInt(FrmImgAttr.IsEdit, 0, "是否可以编辑", true, false);
		map.AddTBString(FrmImgAttr.Name, null, "中文名称", true, false, 0, 500, 20);
		map.AddTBString(FrmImgAttr.EnPK, null, "英文名称", true, false, 0, 500, 20);

		map.AddTBString(FrmBtnAttr.GUID, null, "GUID", true, false, 0, 128, 20);


		//显示的分组.
		map.AddDDLSQL(MapAttrAttr.GroupID, "0", "所在分组", MapAttrString.SQLOfGroupAttr(), true);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}

