package bp.sys.frmui;

import bp.en.*;
import bp.en.Map;
import bp.sys.*;
import bp.web.WebUser;


/** 
 评分控件
*/
public class ExtScore extends EntityMyPK
{
	private static final long serialVersionUID = 1L;
		///属性
	/** 
	 URL
	 * @throws Exception 
	*/
	public final String getURL() throws Exception
	{
		return this.GetValStringByKey(MapAttrAttr.Tag2);
	}
	public final void setURL(String value) throws Exception
	{
		this.SetValByKey(MapAttrAttr.Tag2, value);
	}
	/** 
	 FK_MapData
	*/
	public final String getFK_MapData()throws Exception
	{
		return this.GetValStrByKey(MapAttrAttr.FK_MapData);
	}
	public final void setFK_MapData(String value) throws Exception
	{
		this.SetValByKey(MapAttrAttr.FK_MapData, value);
	}
	/** 
	 Text
	*/
	public final String getName()throws Exception
	{
		return this.GetValStrByKey(MapAttrAttr.Name);
	}
	public final void setName(String value) throws Exception
	{
		this.SetValByKey(MapAttrAttr.Name, value);
	}


		///


		///构造方法
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.Readonly();
		if (WebUser.getNo().equals("admin") == true)
		{

			uac.IsUpdate = true;
			uac.IsDelete = true;
		}

		return uac;
	}
	/** 
	 评分控件
	*/
	public ExtScore()
	{
	}
	/** 
	 评分控件
	 
	 @param mypk
	*/
	public ExtScore(String mypk)throws Exception
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
		Map map = new Map("Sys_MapAttr", "评分控件");
		map.IndexField = MapAttrAttr.FK_MapData;



			///通用的属性.
		map.AddMyPK();
		map.AddTBString(MapAttrAttr.FK_MapData, null, "表单ID", true, true, 1, 100, 20);
		map.AddTBString(MapAttrAttr.KeyOfEn, null, "字段", true, true, 1, 100, 20);
		map.AddDDLSQL(MapAttrAttr.GroupID, 0, "显示的分组", MapAttrString.getSQLOfGroupAttr(), true);
		map.AddBoolean(MapAttrAttr.UIIsEnable, true, "是否可编辑？", true, true);
		map.AddBoolean(MapAttrAttr.UIIsInput, false, "是否必填项？", true, true);
		map.AddDDLSysEnum(MapAttrAttr.TextColSpan, 1, "文本单元格数量", true, true, "ColSpanAttrString", "@1=跨1个单元格@2=跨2个单元格@3=跨3个单元格@4=跨4个单元格");
		map.AddTBInt(MapAttrAttr.RowSpan, 1, "行数", true, false);

			/// 通用的属性.



			///个性化属性.
		map.AddTBString(MapAttrAttr.Name, null, "评分事项", true, false, 0, 500, 20, true);
		map.AddTBInt(MapAttrAttr.Tag2, 5, "总分", true, false);

			/// 个性化属性.


		this.set_enMap(map);
		return this.get_enMap();
	}

		///
}