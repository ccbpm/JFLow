package BP.WF.MS;

import BP.En.EnType;
import BP.En.EntityNoName;
import BP.En.Map;
import BP.En.UAC;

/** 
 章节
*/
public class ZhiDuDtl extends EntityNoName
{
		
	/** 
	 序号
	*/
	public final int getIdx()
	{
		return this.GetValIntByKey(ZhiDuDtlAttr.Idx);
	}
	public final void setIdx(int value)
	{
		this.SetValByKey(ZhiDuDtlAttr.Idx, value);
	}
	/** 
	 文档级别
	*/
	public final int getDocLevel()
	{
		return this.GetValIntByKey(ZhiDuDtlAttr.DocLevel);
	}
	public final void setDocLevel(int value)
	{
		this.SetValByKey(ZhiDuDtlAttr.DocLevel, value);
	}

	/** 
	 子级
	*/
	public final int getParagraphIndex()
	{
		return this.GetValIntByKey(ZhiDuDtlAttr.ParagraphIndex);
	}
	public final void setParagraphIndex(int value)
	{
		this.SetValByKey(ZhiDuDtlAttr.ParagraphIndex, value);
	}
	/** 
	 文档父级
	*/
	public final int getParentParagraphIndex()
	{
		return this.GetValIntByKey(ZhiDuDtlAttr.ParentParagraphIndex);
	}
	public final void setParentParagraphIndex(int value)
	{
		this.SetValByKey(ZhiDuDtlAttr.ParentParagraphIndex, value);
	}
	/** 
	 制度外键
	*/
	public final String getFK_Main()
	{
		return this.GetValStringByKey(ZhiDuDtlAttr.FK_Main);
	}
	public final void setFK_Main(String value)
	{
		this.SetValByKey(ZhiDuDtlAttr.FK_Main, value);
	}
	/** 
	 word存放目录
	*/
	public final String getDocText()
	{
		return this.GetValStringByKey(ZhiDuDtlAttr.DocText);
	}
	public final void setDocText(String value)
	{
		this.SetValByKey(ZhiDuDtlAttr.DocText, value);
	}
	/** 
	 html存放目录
	*/
	public final String getDocHtml()
	{
		return this.GetValStringByKey(ZhiDuDtlAttr.DocHtml);
	}
	public final void setDocHtml(String value)
	{
		this.SetValByKey(ZhiDuDtlAttr.DocHtml, value);
	}
	/** 
	 UI界面上的访问控制
	 * @throws Exception 
	*/
	@Override
	public UAC getHisUAC() throws Exception
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
	 Dtl
	*/
	public ZhiDuDtl()
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
		Map map = new Map("MS_ZhiDuDtl", "章节");
		map.Java_SetEnType(EnType.Admin);

		map.AddTBStringPK(ZhiDuDtlAttr.No, null, "编号", true, true, 5, 5, 5);
		map.AddTBString(ZhiDuDtlAttr.Name, null, "名称", true, true, 0, 200, 4);
		map.AddTBString(ZhiDuDtlAttr.DocLevel, null, "文档级别", true, true, 20, 20, 20);
		map.AddTBString(ZhiDuDtlAttr.ParagraphIndex, null, "文档级", true, true, 20, 20, 20);
		map.AddTBString(ZhiDuDtlAttr.ParentParagraphIndex, null, "文档父级", true, true, 20, 20, 20);
		map.AddTBInt(ZhiDuDtlAttr.IsDir, 0, "是否是章节", true, true);
		map.AddTBInt(ZhiDuDtlAttr.Idx, 0, "序号", true, true);

		map.AddTBString(ZhiDuDtlAttr.FK_Main, null, "制度", true, true, 0, 5, 5);

		map.AddTBString(ZhiDuDtlAttr.DocText, null, "DocText", true, true, 0, 4000, 4);
		map.AddTBString(ZhiDuDtlAttr.DocHtml, null, "DocHtml", true, true, 0, 4000, 4);


		this.set_enMap(map);
		return this.get_enMap();
	}
		///#endregion
}