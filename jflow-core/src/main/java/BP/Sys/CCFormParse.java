package BP.Sys;

import BP.DA.*;
import BP.En.FieldTypeS;
import BP.En.UIContralType;
import BP.Tools.Json;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.math.*;

/** 
 解析控件并保存.
*/
public class CCFormParse
{
	/** 
	 保存元素
	 
	 @param fk_mapdata 表单ID
	 @param eleType 元素类型
	 @param ctrlID 控件ID
	 @param x 位置
	 @param y 位置
	 @param h 高度
	 @param w 宽度
	 * @throws Exception 
	*/
	public static void SaveFrmEle(String fk_mapdata, String eleType, String ctrlID, float x, float y, float h, float w) throws Exception
	{
		FrmEle en = new FrmEle();

		en.setEleType(eleType);
		en.setFK_MapData(fk_mapdata);
		en.setEleID(ctrlID);

		int i = en.Retrieve(FrmEleAttr.FK_MapData, fk_mapdata, FrmEleAttr.EleID, ctrlID);
		en.setX(x);
		en.setY(y);
		en.setW(w);
		en.setH(h);

		if (i == 0)
		{
			en.Insert();
		}
		else
		{
			en.Update();
		}
	}

	/** 
	 保存元素
	 
	 @param fk_mapdata 表单ID
	 @param eleType 元素类型
	 @param ctrlID 控件ID
	 @param x 位置
	 @param y 位置
	 @param h 高度
	 @param w 宽度
	 * @throws Exception 
	*/
	public static void SaveMapFrame(String fk_mapdata, String eleType, String ctrlID, float x, float y, float h, float w) throws Exception
	{
		MapFrame en = new MapFrame();
		en.setMyPK(ctrlID);
		int i = en.RetrieveFromDBSources();
		en.setEleType(eleType);
		en.setFK_MapData(fk_mapdata);
		en.setFrmID(ctrlID);

		en.setX(x);
		en.setY(y);
		en.setW(w);
		en.setH(h);

		if (i == 0)
		{
			en.Insert();
		}
		else
		{
			en.Update();
		}
	}

	/** 
	 保存一个rb
	 
	 @param fk_mapdata 表单ID
	 @param ctrlID 控件ID
	 @param x 位置x
	 @param y 位置y
	 * @throws Exception 
	*/
	public static String SaveFrmRadioButton(String fk_mapdata, String ctrlID, float x, float y) throws Exception
	{
		FrmRB en = new FrmRB();
		en.setMyPK(fk_mapdata + "_" + ctrlID);
		int i = en.RetrieveFromDBSources();
		if (i == 0)
		{
			return null;
		}

		en.setFK_MapData(fk_mapdata);
		en.setX(x);
		en.setY(y);
		en.Update();
		return en.getKeyOfEn();
	}
	/** 
	 保存图片
	 
	 @param fk_mapdata 表单ID
	 @param ctrlID 空间ID
	 @param x 位置x
	 @param y 位置y
	 @param h 高度h
	 @param w 宽度w
	 * @throws Exception 
	*/
	public static void SaveAthImg(String fk_mapdata, String ctrlID, float x, float y, float h, float w) throws Exception
	{
		FrmImgAth en = new FrmImgAth();
		en.setMyPK(fk_mapdata + "_" + ctrlID);
		en.setFK_MapData(fk_mapdata);
		en.setCtrlID(ctrlID);
		en.RetrieveFromDBSources();

		en.setX(x);
		en.setY(y);
		en.setW(w);
		en.setH(h);
		en.Update();
	}
	/** 
	 保存多附件
	 
	 @param fk_mapdata 表单ID
	 @param ctrlID 控件ID
	 @param x 位置x
	 @param y 位置y
	 @param h 高度
	 @param w 宽度
	 * @throws Exception 
	*/
	public static void SaveAthMulti(String fk_mapdata, String ctrlID, float x, float y, float h, float w) throws Exception
	{
		FrmAttachment en = new FrmAttachment();
		en.setMyPK(fk_mapdata + "_" + ctrlID);
		en.setFK_MapData(fk_mapdata);
		en.setNoOfObj(ctrlID);
		en.RetrieveFromDBSources();

		en.setX(x);
		en.setY(y);
		en.setW(w);
		en.setH(h);
		en.Update();
	}
	public static void SaveDtl(String fk_mapdata, String ctrlID, float x, float y, float h, float w) throws Exception
	{
		MapDtl dtl = new MapDtl();
		dtl.setNo(ctrlID);
		dtl.RetrieveFromDBSources();

		dtl.setFK_MapData(fk_mapdata);
		dtl.setX(x);
		dtl.setY(y);
		dtl.setW(w);
		dtl.setH(h);
		dtl.Update();
	}
	public static void SaveiFrame(String fk_mapdata, String ctrlID, float x, float y, float h, float w) throws Exception
	{
		FrmEle en = new FrmEle();
		en.setFK_MapData(fk_mapdata);
		en.setEleID(ctrlID);
		en.setMyPK(en.getFK_MapData() + "_" + en.getEleID());
		if (en.RetrieveFromDBSources() == 0)
		{
			en.Insert();
		}

		en.setX(x);
		en.setY(y);
		en.setW(w);
		en.setH(h);
		en.Update();
	}
	public static void SaveMapAttr(String fk_mapdata, String fieldID, String shape, JSONObject control, JSONArray properties, String pks) throws Exception
	{
		MapAttr attr = new MapAttr();
		attr.setFK_MapData(fk_mapdata);
		attr.setKeyOfEn(fieldID);
		attr.setMyPK(fk_mapdata + "_" + fieldID);
		attr.RetrieveFromDBSources();

		switch (shape)
		{
			case "TextBoxStr": //文本类型.
			case "TextBoxSFTable":
				attr.setLGType(FieldTypeS.Normal);
				attr.setUIContralType(UIContralType.TB);
				break;
			case "TextBoxInt": //数值
				attr.setLGType(FieldTypeS.Normal);
				attr.setMyDataType(DataType.AppInt);
				attr.setUIContralType(UIContralType.TB);
				break;
			case "TextBoxBoolean":
				attr.setMyDataType(DataType.AppBoolean);
				attr.setUIContralType(UIContralType.CheckBok);
				attr.setLGType(FieldTypeS.Normal);
				break;
			case "TextBoxFloat":
				attr.setLGType(FieldTypeS.Normal);
				attr.setUIContralType(UIContralType.TB);
				break;
			case "TextBoxMoney":
				attr.setMyDataType(DataType.AppMoney);
				attr.setLGType(FieldTypeS.Normal);
				attr.setUIContralType(UIContralType.TB);
				break;
			case "TextBoxDate":
				attr.setMyDataType(DataType.AppDate);
				attr.setLGType(FieldTypeS.Normal);
				attr.setUIContralType(UIContralType.TB);
				break;
			case "TextBoxDateTime":
				attr.setMyDataType(DataType.AppDateTime);
				attr.setLGType(FieldTypeS.Normal);
				attr.setUIContralType(UIContralType.TB);
				break;
			case "DropDownListEnum": //枚举类型.
				attr.setMyDataType(BP.DA.DataType.AppInt);
				attr.setLGType(FieldTypeS.Enum);
				attr.setUIContralType(UIContralType.DDL);
				break;
			case "DropDownListTable": //外键类型.
				attr.setMyDataType(BP.DA.DataType.AppString);
				if (pks.contains("@" + attr.getKeyOfEn() + "@") == false)
				{
					attr.setLGType(FieldTypeS.FK);
				}
				attr.setUIContralType(UIContralType.DDL);
				attr.setMaxLen(100);
				attr.setMinLen(0);
				break;
			default:
				break;
		}

		//坐标
		JSONObject style = control.getJSONObject("style");
		JSONArray vector = style.getJSONArray("gradientBounds");
		attr.setX(Float.parseFloat(vector.get(0).toString()));
		attr.setY(Float.parseFloat(vector.get(1).toString()));

		for (int iProperty = 0; iProperty < properties.size(); iProperty++)
		{
			JSONObject property = (JSONObject) properties.getJSONObject(iProperty); //获得一个属性.
			if (property == null || !property.containsKey("property") || property.get("property") == null || property.get("property").toString().equals("group"))
			{
				continue;
			}

			String val = null;
			if (property.get("PropertyValue") != null)
			{
				val = property.get("PropertyValue").toString();
			}
			String propertyName = property.get("property").toString();
			switch (propertyName)
			{
				case "Name":
					if (attr.getName().equals(""))
					{
						attr.setName(val);
					}
					break;
				case "MinLen":
				case "MaxLen":
				case "DefVal":
					attr.SetValByKey(propertyName, val);
					break;
				case "UIIsEnable":
				case "UIVisible":
					attr.SetValByKey(propertyName, val);
					break;
				case "FieldText":
					if (attr.getName().equals(""))
					{
					   attr.setName(val);
					}
					break;
				case "UIIsInput":
					if (val.equals("true"))
					{
						attr.setUIIsInput(true);
					}
					else
					{
						attr.setUIIsInput(false);
					}
					break;
				case "UIBindKey":
					attr.setUIBindKey(val);
					break;
				default:
					break;
			}
		}


		//Textbox 高、宽.
		BigDecimal minX = new BigDecimal(vector.get(0).toString());
		BigDecimal minY = new BigDecimal(vector.get(1).toString());
		BigDecimal maxX = new BigDecimal(vector.get(2).toString());
		BigDecimal maxY = new BigDecimal(vector.get(3).toString());
		BigDecimal imgWidth = new BigDecimal(maxX.intValue() - minX.intValue());
		BigDecimal imgHeight =new BigDecimal(maxY.intValue() - minY.intValue());
		attr.setUIWidth(Float.parseFloat(String.format("%.2f",imgWidth)));
		attr.setUIHeight(Float.parseFloat(String.format("%.2f",imgHeight)));

	  //  attr.ColSpan

		if (pks.contains("@" + attr.getKeyOfEn() + "@") == true)
		{
			attr.Update();
		}
		else
		{
			attr.Insert();
		}
	}


		///#region 装饰类控件.
	/** 
	 保存线.
	 
	 @param fk_mapdata
	 @param form_Lines
	 * @throws Exception 
	*/
	public static void SaveLine(String fk_mapdata, JSONArray form_Lines) throws Exception
	{
		//标签.
		String linePKs = "@";
		FrmLines lines = new FrmLines();
		lines.Retrieve(FrmLabAttr.FK_MapData, fk_mapdata);
		for (FrmLine item : lines.ToJavaList())
		{
			linePKs += item.getMyPK() + "@";
		}

		if (form_Lines.isArray() == true && form_Lines.size() > 0)
		{
			for (int idx = 0, jLine = form_Lines.size(); idx < jLine; idx++)
			{
				JSONObject line = (JSONObject) form_Lines.getJSONObject(idx);
				if (line.isNullObject())
				{
					continue;
				}

				FrmLine lineEn = new FrmLine();

				lineEn.setMyPK(line.get("CCForm_MyPK").toString());
				lineEn.setFK_MapData(fk_mapdata);

				JSONArray turningPoints = line.getJSONArray("turningPoints");
				lineEn.setX1(Float.parseFloat(turningPoints.getJSONObject(0).getString("x")));
				lineEn.setX2(Float.parseFloat(turningPoints.getJSONObject(1).getString("x")));
				lineEn.setY1(Float.parseFloat(turningPoints.getJSONObject(0).getString("y")));
				lineEn.setY2(Float.parseFloat(turningPoints.getJSONObject(1).getString("y")));

				JSONArray properties = line.getJSONArray("properties");
				JSONObject borderWidth = Json.GetObjectFromArrary_ByKeyValue(properties,"type", "LineWidth");
				JSONObject borderColor = Json.GetObjectFromArrary_ByKeyValue(properties,"type", "Color");
				String strborderWidth = "2";
				if (borderWidth != null && borderWidth.get("PropertyValue") != null && !DataType.IsNullOrEmpty(borderWidth.get("PropertyValue").toString()))
				{
					strborderWidth = borderWidth.get("PropertyValue").toString();
				}
				String strBorderColor = "Black";
				if (borderColor != null && borderColor.get("PropertyValue") != null && !DataType.IsNullOrEmpty(borderColor.get("PropertyValue").toString()))
				{
					strBorderColor = borderColor.get("PropertyValue").toString();
				}
				lineEn.setBorderWidth(Float.parseFloat(strborderWidth));
				lineEn.setBorderColor(strBorderColor);
				if (linePKs.contains("@" + lineEn.getMyPK() + "@") == true)
				{
					linePKs = linePKs.replace(lineEn.getMyPK() + "@", "");
					lineEn.DirectUpdate();
				}
				else
				{
					lineEn.DirectInsert();
				}
			}
		}

		//删除找不到的Line.
		String[] strs = linePKs.split("[@]", -1);
		String sqls = "";
		for (String str : strs)
		{
			if (DataType.IsNullOrEmpty(str))
			{
				continue;
			}
			sqls += "@DELETE FROM Sys_FrmLine WHERE MyPK='" + str + "'";
		}
		if (!sqls.equals(""))
		{
			BP.DA.DBAccess.RunSQLs(sqls);
		}
	}
	public static void SaveLabel(String fk_mapdata, JSONObject control, JSONArray properties, String pks, String ctrlID) throws Exception
	{
		// New lab 对象.
		FrmLab lab = new FrmLab();
		lab.setMyPK(ctrlID);
		lab.setFK_MapData(fk_mapdata);

		//坐标.
		JSONObject style = control.getJSONObject("style");
		JSONArray vector = style.getJSONArray("gradientBounds");
		lab.setX(Float.parseFloat(vector.get(0).toString()));
		lab.setY(Float.parseFloat(vector.get(1).toString()));

		StringBuilder fontStyle = new StringBuilder();
		for (int iProperty = 0; iProperty < properties.size(); iProperty++)
		{
			JSONObject property = (JSONObject) properties.getJSONObject(iProperty);
			if (property == null || !property.containsKey("type") || property.optString("type") == null)
			{
				continue;
			}

			String type = property.get("type").toString().trim();
			String val = null;
			if (property.get("PropertyValue") != null)
			{
				val = property.get("PropertyValue").toString();
			}

			switch (type)
			{
				case "SingleText":
					lab.setText((val == null || val.equals("null")) ? "" : val.toString().replace(" ", "&nbsp;").replace("\n", "@"));
					break;
				case "Color":
					// lab.FontColor = val == null ? "#FF000000" : val.ToString();
					lab.setFontColor((val == null || val.equals("null"))  ? "#000000" : val.toString());
					fontStyle.append(String.format("color:%1$s;", lab.getFontColor()));
					break;
				case "TextFontFamily":
					lab.setFontName((val == null || val.equals("null"))  ? "Portable User Interface" : val.toString());
					if (val != null)
					{
						fontStyle.append(String.format("font-family:%1$s;", property.optString("PropertyValue")));
					}
					break;
				case "TextFontSize":
					lab.setFontSize((val == null || val.equals("null"))  ? 14 : Integer.parseInt(val.toString()));
					fontStyle.append(String.format("font-size:%1$s;", lab.getFontSize()));
					break;
				case "FontWeight":
					if ((val == null || val.equals("null"))  || val.toString().equals("normal"))
					{
						lab.setIsBold(false);
						fontStyle.append(String.format("font-weight:normal;"));
					}
					else
					{
						lab.setIsBold(true);
						fontStyle.append(String.format("font-weight:%1$s;", val));
					}
					break;
				default:
					break;
			}
		}

		if (lab.getText() == null || lab.getText().equals(""))
		{
			/*如果没有取到标签， 从这里获取，系统有一个. */
			JSONObject primitives = (JSONObject) control.getJSONArray("primitives").get(0);
			lab.setText(primitives.get("str").toString().trim());
			lab.setFontName(primitives.get("font").toString().trim());
			lab.setFontSize(Integer.parseInt(primitives.get("size").toString().trim()));
		}

		lab.setFontStyle(fontStyle.toString());
		if (pks.contains(lab.getMyPK() + "@") == true)
		{
			lab.DirectUpdate();
		}
		else
		{
			lab.DirectInsert();
		}
	}
	public static void SaveButton(String fk_mapdata, JSONObject control, JSONArray properties, String pks, String ctrlID) throws Exception
	{
		FrmBtn btn = new FrmBtn(ctrlID);
		btn.setMyPK(ctrlID);
		btn.setFK_MapData(fk_mapdata);

		//坐标
		JSONObject style = control.getJSONObject("style");
		JSONArray vector = style.getJSONArray("gradientBounds");
		btn.setX(Float.parseFloat(vector.get(0).toString()));
		btn.setY(Float.parseFloat(vector.get(1).toString()));
		btn.setIsEnable(true);
		/*for (int iProperty = 0; iProperty < properties.size(); iProperty++)
		{
		    JsonData property = properties[iProperty];
		    if (property == null || !property.Keys.Contains("property") || property["property"] == null)
		        continue;

		    string val = null;
		    if (property["PropertyValue"] != null)
		        val = property["PropertyValue"].ToString();

		    string propertyBtn = property["property"].ToString();
		    switch (propertyBtn)
		    {
		        case "primitives.1.str":
		            btn.Text = val == null ? "" : val.Replace(" ", "&nbsp;").replace("\n", "@");
		            break;
		        case "ButtonEvent":
		            btn.EventType = val == null ? 0 : int.Parse(val);
		            break;
		        case "BtnEventDoc":
		            btn.EventContext = val == null ? "" : val;
		            break;
		        default:
		            break;
		    }
		}*/
		if (pks.contains("@" + btn.getMyPK() + "@") == true)
		{
			btn.DirectUpdate();
		}
		else
		{
			btn.DirectInsert();
		}
	}

	public static void SaveHyperLink(String fk_mapdata, JSONObject control, JSONArray properties, String pks, String ctrlID) throws Exception
	{
		FrmLink link = new FrmLink(ctrlID);
		link.setMyPK(ctrlID);
		link.setFK_MapData(fk_mapdata);
		//坐标
		JSONArray vector = control.getJSONObject("style").getJSONArray("gradientBounds");
		link.setX(Float.parseFloat(vector.get(0).toString()));
		link.setY(Float.parseFloat(vector.get(1).toString()));


		//属性集合
		StringBuilder fontStyle = new StringBuilder();
		for (int iProperty = 0; iProperty < properties.size(); iProperty++)
		{
			JSONObject property = (JSONObject) properties.getJSONObject(iProperty);
			if (property == null || !property.containsKey("property") || property.optString("property") == null)
			{
				continue;
			}

			String propertyLink = property.get("property").toString();
			String valLink = property.optString("PropertyValue");

			switch (propertyLink)
			{
				//case "primitives.0.str":
				//case "SingleText":
				//    link.Text  = valLink == null ? "" : valLink.ToString();
				//    break;
				case "primitives.0.style.fillStyle":
					link.setFontColor(valLink == null ? "#FF000000" : valLink.toString());
					fontStyle.append(String.format("color:%1$s;", link.getFontColor()));
					break;
				case "FontName":
					link.setFontName(valLink == null ? "Portable User Interface" : valLink.toString());
					if (valLink != null)
					{
						fontStyle.append(String.format("font-family:%1$s;", Json.ToJson(valLink)));
					}
					continue;
				case "FontSize":
				   link.setFontSize(valLink == null ? 14 : Integer.parseInt(valLink.toString()));
				   fontStyle.append(String.format("font-size:%1$s;", link.getFontSize()));
					continue;
				case "primitives.0.fontWeight":
					if (valLink == null || valLink.toString().equals("normal"))
					{
						link.setIsBold(false);
						fontStyle.append(String.format("font-weight:normal;"));
					}
					else
					{
						link.setIsBold(true);
						fontStyle.append(String.format("font-weight:%1$s;", valLink.toString()));
					}
					continue;
				case "FontColor":
					link.setFontColor(valLink == null ? "" : valLink.toString());
					continue;
				//case "URL":
				 //   link.URL = valLink == null ? "" : valLink.ToString();
				 //   continue;
				//case "WinOpenModel":
				 /**   link.Target = valLink == null ? "_blank" : valLink.ToString();
				 */
				//    continue;
				default:
					break;
			}
		}
		link.setFontStyle(fontStyle.toString());

		if (link.getText() == null || link.getText().equals(""))
		{
			/*如果没有取到标签， 从这里获取，系统有一个. */
			JSONObject primitives = (JSONObject) control.getJSONArray("primitives").get(0);
			link.setText(primitives.get("str").toString().trim());
			link.setFontName(primitives.get("font").toString().trim());
			link.setFontSize(Integer.parseInt(primitives.get("size").toString().trim()));
		}

		//执行保存.
		if (pks.contains("@" + link.getMyPK() + "@") == true)
		{
			link.DirectUpdate();
		}
		else
		{
			link.DirectInsert();
		}
	}
	public static void SaveImage(String fk_mapdata, JSONObject control, JSONArray properties, String pks, String ctrlID) throws Exception
	{
		FrmImg img = new FrmImg();
		img.setMyPK(ctrlID);
		int count = img.RetrieveFromDBSources();
		img.setFK_MapData(fk_mapdata);
		img.setIsEdit(1);
		img.setHisImgAppType(ImgAppType.Img);

		//坐标
		JSONArray vector = control.getJSONObject("style").getJSONArray("gradientBounds");
		img.setX(Float.parseFloat(vector.get(0).toString()));
		img.setY(Float.parseFloat(vector.get(1).toString()));
		//图片高、宽
		java.math.BigDecimal minX = new java.math.BigDecimal(vector.get(0).toString());
		java.math.BigDecimal minY = new java.math.BigDecimal(vector.get(1).toString());
		java.math.BigDecimal maxX = new java.math.BigDecimal(vector.get(2).toString());
		java.math.BigDecimal maxY = new java.math.BigDecimal(vector.get(3).toString());
		
		java.math.BigDecimal imgWidth = new java.math.BigDecimal(maxX.intValue() - minX.intValue());
		java.math.BigDecimal imgHeight =new java.math.BigDecimal(maxY.intValue() - minY.intValue());
		img.setW(Float.parseFloat(String.format("%.2f",imgWidth)));
		img.setH(Float.parseFloat(String.format("%.2f",imgHeight)));

		StringBuilder fontStyle = new StringBuilder();
		for (int iProperty = 0; iProperty < properties.size(); iProperty++)
		{
			JSONObject property = (JSONObject) properties.getJSONObject(iProperty);
			if (property == null || !property.containsKey("property") || property.get("property") == null)
			{
				continue;
			}

			if (property.get("property").toString().equals("LinkURL"))
			{
				//图片连接到
				img.setLinkURL(property.get("PropertyValue") == null ? "" : property.get("PropertyValue").toString());
			}
			else if (property.get("property").toString().equals("WinOpenModel"))
			{
				//打开窗口方式
				img.setLinkTarget(property.get("PropertyValue") == null ? "_blank" : property.get("PropertyValue").toString());
			}
			else if (property.get("property").toString().equals("ImgAppType"))
			{
				//应用类型：0本地图片，1指定路径.
				img.setImgSrcType(property.get("PropertyValue") == null || property.get("PropertyValue").equals("null")  ? 0 : Integer.parseInt(property.get("PropertyValue").toString()));
			}
			else if (property.get("property").toString().equals("ImgPath"))
			{
				//指定图片路径
				img.setImgURL(property.get("PropertyValue") == null ? "" : property.get("PropertyValue").toString());
			}
		}
		//ImageFrame 本地图片路径
		JSONArray primitives = control.getJSONArray("primitives");
		JSONObject primitive = null;
		for (int i=0; i<primitives.size(); i++)
		{
			primitive = primitives.getJSONObject(i);
			if(null == primitive.optString("oType"))
			{
				continue;
			}
			if (primitive.optString("oType").equals("ImageFrame")  && count ==0)
			{
				img.setImgPath(primitive == null ? "" : primitive.optString("url"));
			}
		}

		//执行保存.
		if (pks.contains(img.getMyPK() + "@") == true)
		{
			img.DirectUpdate();
		}
		else
		{
			img.DirectInsert();
		}
	}

		///#endregion 装饰类控件.

}