package bp.wf.httphandler;

import bp.wf.*;
import bp.web.*;
import bp.sys.*;
import bp.da.*;
import bp.difference.SystemConfig;
import bp.difference.handler.WebContralBase;
import bp.en.*;
import bp.wf.template.*;
import bp.ccbill.*;
import java.io.*;
import java.net.URLDecoder;

public class WF_Admin_DevelopDesigner extends WebContralBase
{


		///执行父类的重写方法.
	/** 
	 构造函数
	*/
	public WF_Admin_DevelopDesigner()
	{

	}
	/** 
	 表单初始化
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Designer_Init() throws Exception
	{
		String htmlCode = DBAccess.GetBigTextFromDB("Sys_MapData", "No", this.getFK_MapData(), "HtmlTemplateFile");
		//把数据同步到DataUser/CCForm/HtmlTemplateFile/文件夹下
		String filePath = SystemConfig.getPathOfDataUser() + "CCForm/HtmlTemplateFile/";
		if ((new File(filePath)).isDirectory() == false)
		{
			(new File(filePath)).mkdirs();
		}
		filePath = filePath + this.getFK_MapData() + ".htm";
		//写入到html 中
		bp.da.DataType.WriteFile(filePath, htmlCode);
		return htmlCode;

	}
	/** 
	 保存表单
	 
	 @return 
	 * @throws Exception 
	*/
	public final String SaveForm() throws Exception
	{
		//获取html代码
		String htmlCode = this.GetRequestVal("HtmlCode");
		if (DataType.IsNullOrEmpty(htmlCode) == false)
		{
			htmlCode = URLDecoder.decode(htmlCode, "UTF-8");
			//保存到DataUser/CCForm/HtmlTemplateFile/文件夹下
			String filePath = SystemConfig.getPathOfDataUser() + "CCForm/HtmlTemplateFile/";
			if ((new File(filePath)).isDirectory() == false)
			{
				(new File(filePath)).mkdirs();
			}

			filePath = filePath + this.getFK_MapData() + ".htm";
			//写入到html 中
			bp.da.DataType.WriteFile(filePath, htmlCode);

			//保存类型。
			MapData md = new MapData(this.getFK_MapData());
			if (md.getHisFrmType() != FrmType.Develop)
			{
				md.setHisFrmType(FrmType.Develop);
				md.Update();
			}
			// HtmlTemplateFile 保存到数据库中
			DBAccess.SaveBigTextToDB(htmlCode, "Sys_MapData", "No", this.getFK_MapData(), "HtmlTemplateFile");

			//检查数据完整性
			GEEntity en = new GEEntity(this.getFK_MapData());
			en.CheckPhysicsTable();
			return "保存成功";
		}
		return "保存成功.";
	}

		///

	public final String Fields_Init() throws Exception
	{
		String html = DBAccess.GetBigTextFromDB("Sys_MapData", "No", this.getFrmID(), "HtmlTemplateFile");
		return html;
	}

	/** 
	 格式化html的文档.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String Designer_FormatHtml() throws Exception
	{
		String html = DBAccess.GetBigTextFromDB("Sys_MapData", "No", this.getFrmID(), "HtmlTemplateFile");




		return "替换成功.";
		//return html;
	}

	/** 
	 表单重置
	 
	 @return 
	*/
	public final String ResetFrm_Init()
	{
		//删除html
		String filePath = SystemConfig.getPathOfDataUser() + "CCForm/HtmlTemplateFile/" + this.getFK_MapData() + ".htm";
		if ((new File(filePath)).isFile() == true)
		{
			(new File(filePath)).delete();
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


		///复制表单
	/** 
	 复制表单属性和表单内容
	 
	 @param frmId 新表单ID
	 @param frmName 新表单内容
	 * @throws Exception 
	*/
	public final void DoCopyFrm() throws Exception
	{
		String fromFrmID = GetRequestVal("FromFrmID");
		String toFrmID = GetRequestVal("ToFrmID");
		String toFrmName = GetRequestVal("ToFrmName");

			///原表单信息
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

			/// 原表单信息


			///复制表单
		MapData toMapData = new MapData();
		toMapData = fromMap;
		toMapData.setNo(toFrmID);
		toMapData.setName( toFrmName);
		toMapData.Insert();
		if (billCount != 0)
		{
			FrmBill toBill = new FrmBill();
			toBill = fromBill;
			toBill.setNo(toFrmID);
			toBill.setName( toFrmName);
			toBill.setEntityType(EntityType.FrmBill);
			toBill.Update();
		}
		if (DictCount != 0)
		{
			FrmDict toDict = new FrmDict();
			toDict = fromDict;
			toDict.setNo(toFrmID);
			toDict.setName( toFrmName);
			toDict.setEntityType(EntityType.FrmDict);
			toDict.Update();
		}

			/// 复制表单

		MapData.ImpMapData(toFrmID, bp.sys.CCFormAPI.GenerHisDataSet_AllEleInfo(fromFrmID));

		//清空缓存
		toMapData.RepairMap();
		SystemConfig.DoClearCash();


	}

		/// 复制表单

}