package BP.Sys.FrmUI;

import BP.DA.*;
import BP.En.*;
import BP.Sys.*;
import BP.Tools.StringHelper;

/** 
 框架
 
*/
public class MapFrameExt extends EntityMyPK
{
  
		///#region 构造方法
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.IsUpdate = true;
		uac.IsDelete = true;
		uac.IsInsert = false;
		return uac;
	}
	/** 
	 框架
	 
	*/
	public MapFrameExt()
	{

	}
	/** 
	 框架
	 
	 @param mypk
	 * @throws Exception 
	*/
	public MapFrameExt(String mypk) throws Exception
	{
		this.setMyPK( mypk);
		this.Retrieve();
	}
	
	/**
	 * FK_MapData
	 */
	public final String getFK_MapData() {
		return this.GetValStrByKey(FrmAttachmentAttr.FK_MapData);
	}

	public final void setFK_MapData(String value) {
		this.SetValByKey(FrmAttachmentAttr.FK_MapData, value);
	}
	
	/**
	 * 附件名称
	 */
	public final String getName() {
		String str = this.GetValStringByKey(FrmAttachmentAttr.Name);
		if (StringHelper.isNullOrEmpty(str) == true) {
			str = "我的框架";
		}
		return str;
	}

	public final void setName(String value) {
		this.SetValByKey(FrmAttachmentAttr.Name, value);
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

		Map map = new Map("Sys_MapFrame", "框架");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);

 

        map.AddMyPK();
        map.AddTBString(MapFrameAttr.FK_MapData, null, "表单ID", true, true, 0, 100, 20);
        map.AddTBString(MapFrameAttr.Name, null, "名称", true, false, 0, 200, 20, true);
		map.AddTBString(MapFrameAttr.FrameURL, null, "URL", true, false, 0, 3000, 20, true);
        map.AddTBString(MapFrameAttr.URL, null, "URL", false, false, 0, 3000, 20, true);

        map.AddDDLSysEnum(MapFrameAttr.UrlSrcType, 0, "URL来源", true, true, MapFrameAttr.UrlSrcType, "@0=自定义@1=表单库");
        //显示的分组.
        map.AddDDLSQL(MapFrameAttr.FrmID, "0", "表单表单","SELECT No, Name FROM Sys_Mapdata  WHERE  FrmType=3 ", true);

        map.AddTBString(FrmEleAttr.Y, null, "Y", true, false, 0, 20, 20);
        map.AddTBString(FrmEleAttr.X, null, "x", true, false, 0, 20, 20);

        map.AddTBString(FrmEleAttr.W, null, "宽度", true, false, 0, 20, 20);
        map.AddTBString(FrmEleAttr.H, null, "高度", true, false, 0, 20, 20);

        map.AddBoolean(MapFrameAttr.IsAutoSize, true, "是否自动设置大小", false, false);

        map.AddTBString(FrmEleAttr.EleType, null, "类型", false, false, 0, 50, 20, true);

        // map.AddTBInt(MapFrameAttr.RowIdx, 99, "位置", false, false);
        // map.AddTBInt(MapFrameAttr.GroupID, 0, "GroupID", false, false);

        map.AddTBString(FrmBtnAttr.GUID, null, "GUID", false, false, 0, 128, 20);
        
		

		this.set_enMap( map);
		return map;
	}
	
	@Override
    protected void afterDelete() throws Exception
    {
		  //删除分组信息.
        GroupField gf = new GroupField();
        gf.Delete(GroupFieldAttr.CtrlID,this.getMyPK());

        super.afterDelete();
    }
	
	@Override
	protected boolean beforeUpdateInsertAction() throws Exception {
			int val = this.GetValIntByKey(MapFrameAttr.UrlSrcType, 0);
			if (val == 1)
			{
				String sql = "SELECT Url FROM Sys_MapData WHERE No='" + this.GetValStrByKey(MapFrameAttr.FrmID) + "'";
				String url = DBAccess.RunSQLReturnStringIsNull(sql, "");
				this.SetValByKey(MapFrameAttr.FrameURL, url);
				this.SetValByKey(MapFrameAttr.URL, url);
			}
			else
			{
				this.SetValByKey(MapFrameAttr.URL, this.GetValByKey(MapFrameAttr.FrameURL));
			}
			// 更新相关的分组信息.
			GroupField gf = new GroupField();
			int i = gf.Retrieve(GroupFieldAttr.FrmID, this.getFK_MapData(), GroupFieldAttr.CtrlID, this.getMyPK());
			if (i == 0) {
				gf.setLab(this.getName());
				gf.setFrmID(this.getFK_MapData());
				gf.setCtrlType("Frame");
				gf.Insert();
			} else {
				gf.setLab(this.getName());
				gf.setFrmID(this.getFK_MapData());
				gf.setCtrlType("Frame");
				gf.Update();
			}

			return super.beforeUpdateInsertAction();
	}

	@Override
	 protected void afterInsertUpdateAction() throws Exception
     {
         MapFrame mapframe = new MapFrame();
         mapframe.setMyPK(this.getMyPK());
         mapframe.RetrieveFromDBSources();
         mapframe.Update();

         super.afterInsertUpdateAction();
     }
}