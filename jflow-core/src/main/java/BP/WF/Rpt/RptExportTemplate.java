package BP.WF.Rpt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import BP.Tools.StringHelper;

/** 
 报表导出模板
 
*/
public class RptExportTemplate
{
	/** 
	 模板最后修改时间
	*/
	private java.util.Date privateLastModify = new java.util.Date(0);
	public final java.util.Date getLastModify()
	{
		return privateLastModify;
	}
	public final void setLastModify(java.util.Date value)
	{
		privateLastModify = value;
	}

	/** 
	 导出填充方向
	 
	*/
	private FillDirection privateDirection = FillDirection.forValue(0);
	public final FillDirection getDirection()
	{
		return privateDirection;
	}
	public final void setDirection(FillDirection value)
	{
		privateDirection = value;
	}

	/** 
	 导出开始填充的行/列号
	 
	*/
	private int privateBeginIdx;
	public final int getBeginIdx()
	{
		return privateBeginIdx;
	}
	public final void setBeginIdx(int value)
	{
		privateBeginIdx = value;
	}

	/** 
	 字段与单元格绑定信息集合
	 
	*/
	private java.util.ArrayList<RptExportTemplateCell> privateCells;
	public final java.util.ArrayList<RptExportTemplateCell> getCells()
	{
		return privateCells;
	}
	public final void setCells(java.util.ArrayList<RptExportTemplateCell> value)
	{
		privateCells = value;
	}

	/** 
	 是否有单元格绑定了指定的表单中的字段
	 
	 @param fk_mapdata 表单对应FK_MapData
	 @return 
	*/
	public final boolean HaveCellInMapData(String fk_mapdata)
	{
		for(RptExportTemplateCell cell : getCells())
		{
			if (cell.getFK_MapData().equals(fk_mapdata))
			{
				return true;
			}
		}

		return false;
	}

	public final RptExportTemplateCell GetBeginHeaderCell(FillDirection direction)
	{
		if (getCells() == null || getCells().isEmpty())
		{
			return null;
		}

		RptExportTemplateCell cell = getCells().get(0);

		if(direction == FillDirection.Vertical)
		{
			for(int i = 1;i<getCells().size();i++)
			{
				if (getCells().get(i).getColumnIdx() < cell.getColumnIdx())
				{
					cell = getCells().get(i);
				}
			}

			return cell;
		}

		for(int i = 1;i<getCells().size();i++)
		{
			if (getCells().get(i).getRowIdx() < cell.getRowIdx())
			{
				cell = getCells().get(i);
			}
		}

		return cell;
	}

	/** 
	 保存到xml文件中
	 
	 @param fileName xml文件路径
	 @return 
	*/
	public final boolean SaveXml(String fileName)
	{
		try
		{
			//StreamWriter sw = new StreamWriter(fileName, false, Encoding.UTF8);
			FileOutputStream fos = new FileOutputStream(fileName);
			OutputStreamWriter sw1 = new OutputStreamWriter(fos,"UTF-8");
//			try
//			{
//				new XmlSerializer(RptExportTemplate.class).Serialize(sw, this);
//			}

			return true;
		}
		catch (java.lang.Exception e)
		{
			return false;
		}
	}

	/** 
	 获取定义的填充明细表NO
	 
	 @return 
	*/
	public final String GetDtl()
	{
		for(RptExportTemplateCell cell : getCells())
		{
			if (!StringHelper.isNullOrWhiteSpace(cell.getDtlKeyOfEn()))
			{
				return cell.getFK_DtlMapData();
			}
		}

		return null;
	}

	/** 
	 从xml文件加载报表导出模板信息对象
	 
	 @param fileName xml文件路径
	 @return 
	*/
	public static RptExportTemplate FromXml(String fileName)
	{
		RptExportTemplate t = null;

		//if(! File.Exists(fileName))
		if(new File(fileName).exists())
		{
			RptExportTemplate tempVar = new RptExportTemplate();
			tempVar.setLastModify(new java.util.Date());
			tempVar.setDirection(FillDirection.Vertical);
			tempVar.setCells(new java.util.ArrayList<RptExportTemplateCell>());
			t = tempVar;

			t.SaveXml(fileName);
			return t;
		}

		/*
		try
		{

//			using (var sr = new StreamReader(fileName, Encoding.UTF8))
			StreamReader sr = new StreamReader(fileName, Encoding.UTF8);
			try
			{
				Object tempVar2 = XmlSerializer(RptExportTemplate.class).Deserialize(sr);
				t = new (RptExportTemplate)((tempVar2 instanceof RptExportTemplate) ? tempVar2 : null);
			}
			finally
			{
				sr.dispose();
			}
		}
		catch (java.lang.Exception e)
		{
			RptExportTemplate tempVar3 = new RptExportTemplate();
			tempVar3.setLastModify(new java.util.Date());
			tempVar3.setDirection(FillDirection.Vertical);
			tempVar3.setCells(new java.util.ArrayList<RptExportTemplateCell>());
			t = tempVar3;
		}
		 */
		return t;
	}
}