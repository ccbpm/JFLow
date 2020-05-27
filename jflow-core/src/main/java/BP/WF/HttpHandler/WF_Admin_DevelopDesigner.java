package BP.WF.HttpHandler;

import BP.DA.DBAccess;
import BP.DA.DataType;
import BP.DA.Log;
import BP.Difference.Handler.WebContralBase;
import BP.Frm.EntityType;
import BP.Frm.FrmBill;
import BP.Frm.FrmDict;
import BP.Sys.FrmType;
import BP.Sys.GEEntity;
import BP.Sys.MapData;

import java.io.File;
import java.net.URLDecoder;


/** 
 初始化函数
*/
public class WF_Admin_DevelopDesigner extends WebContralBase
{
	   /**
	 构造函数
	   */
	public WF_Admin_DevelopDesigner()
	{
	}

	/**
	 表单初始化

	 @return
	 */
	public final String Designer_Init()throws Exception
	{
		String htmlCode = BP.DA.DBAccess.GetBigTextFromDB("Sys_MapData", "No", this.getFK_MapData(), "HtmlTemplateFile");
		if(DataType.IsNullOrEmpty(htmlCode) ==true)
			return "";
		String filePath = BP.Difference.SystemConfig.getPathOfDataUser() + "CCForm/";
		Log.DebugWriteInfo("Designer_Init"+filePath);
		File file = new File(filePath);
        if (file.exists() == false)
        {
            file.mkdir();
        }

        filePath =filePath+"HtmlTemplateFile/";
        if (new File(filePath).exists() == false)
        {
            new File(filePath).mkdir();
        }
		filePath = filePath + this.getFK_MapData() + ".htm";
		//写入到html 中
		BP.DA.DataType.WriteFile(filePath, htmlCode);
		return htmlCode;

	}
	/**
	 保存表单

	 @return
	 */
	public final String SaveForm()throws Exception
	{
		//获取html代码
		String htmlCode = this.GetRequestVal("HtmlCode");
		if (DataType.IsNullOrEmpty(htmlCode) == false)
		{
			//保存到DataUser/CCForm/HtmlTemplateFile/文件夹下
			htmlCode = URLDecoder.decode(htmlCode, "UTF-8");
			String filePath = BP.Difference.SystemConfig.getPathOfDataUser() + "CCForm/";
			if (new File(filePath).exists() == false)
			{
				new File(filePath).mkdir();
			}
            filePath =filePath+"HtmlTemplateFile/";
            if (new File(filePath).exists() == false)
            {
                new File(filePath).mkdir();
            }
			filePath = filePath + this.getFK_MapData() + ".htm";
			//写入到html 中
			BP.DA.DataType.WriteFile(filePath, htmlCode);

			//保存类型。
			MapData md = new MapData(this.getFK_MapData());
			if (md.getHisFrmType() != FrmType.Develop)
			{
				md.setHisFrmType(FrmType.Develop);
				md.Update();
			}

			//保存到数据库中
			BP.DA.DBAccess.SaveBigTextToDB(htmlCode, "Sys_MapData", "No", this.getFK_MapData(), "HtmlTemplateFile");
			//检查数据完整性
			GEEntity en = new GEEntity(this.getFK_MapData());
			en.CheckPhysicsTable();
			return "保存成功";
		}
		return "保存成功.";
	}

	public final String Fields_Init()throws Exception
	{
		String html = BP.DA.DBAccess.GetBigTextFromDB("Sys_MapData", "No", this.getFrmID(), "HtmlTemplateFile");
		return html;
	}

	/**
	 表单重置

	 @return
	 */
	public final String ResetFrm_Init()throws Exception
	{
		//删除html
		String filePath = BP.Difference.SystemConfig.getPathOfDataUser() + "CCForm/HtmlTemplateFile/" + this.getFK_MapData() + ".htm";
		if (new File(filePath).exists() == true)
		{
			new File(filePath).delete();
		}

		//删除存储的html代码
		String sql = "UPDATE Sys_MapData SET HtmlTemplateFile='' WHERE No='" + this.getFK_MapData() + "'";
		DBAccess.RunSQL(sql);
		//删除MapAttr中的数据
		sql = "Delete Sys_MapAttr WHERE FK_MapData='" + this.getFK_MapData() + "'";
		DBAccess.RunSQL(sql);
		//删除MapExt中的数据
		sql = "Delete Sys_MapExt WHERE FK_MapData='" + this.getFK_MapData() + "'";
		DBAccess.RunSQL(sql);

		return "重置成功";
	}

	///#region 复制表单
	/**
	 复制表单属性和表单内容

	 @param
	 @param
	 */
	public final void DoCopyFrm()throws Exception
	{
		String fromFrmID = GetRequestVal("FromFrmID");
		String toFrmID = GetRequestVal("ToFrmID");
		String toFrmName = GetRequestVal("ToFrmName");
		///#region 原表单信息
		//表单信息
		MapData fromMap = new MapData(fromFrmID);
		//单据信息
		FrmBill fromBill = new FrmBill();
		fromBill.setNo(fromFrmID);
		int billCount = fromBill.RetrieveFromDBSources();
		//实体单据
		FrmDict fromDict = new FrmDict();
		fromDict.setNo(fromFrmID);
		int DictCount = fromDict.RetrieveFromDBSources();
		///#endregion 原表单信息

		///#region 复制表单
		MapData toMapData = new MapData();
		toMapData = fromMap;
		toMapData.setNo(toFrmID);
		toMapData.setName(toFrmName);
		toMapData.Insert();
		if (billCount != 0)
		{
			FrmBill toBill = new FrmBill();
			toBill = fromBill;
			toBill.setNo(toFrmID);
			toBill.setName(toFrmName);
			toBill.setEntityType(EntityType.FrmBill);
			toBill.Update();
		}
		if (DictCount != 0)
		{
			FrmDict toDict = new FrmDict();
			toDict = fromDict;
			toDict.setNo(toFrmID);
			toDict.setName(toFrmName);
			toDict.setEntityType(EntityType.FrmDict);
			toDict.Update();
		}
		///#endregion 复制表单

		MapData.ImpMapData(toFrmID, BP.Sys.CCFormAPI.GenerHisDataSet_AllEleInfo(fromFrmID));

		//清空缓存
		toMapData.RepairMap();
		BP.Difference.SystemConfig.DoClearCash();


	}


}