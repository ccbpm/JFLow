package bp.wf.template;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.en.Map;
import bp.wf.*;
import java.util.*;
import java.io.*;

/** 
 单据模板
*/
public class BillTemplate2019 extends EntityNoName
{

		///#region  属性
	/** 
	 UI界面上的访问控制
	 * @throws Exception 
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	 
	/** 
	 生成的单据类型
	 * @throws Exception 
	*/
	public final BillFileType getHisBillFileType() throws Exception
	{
		return BillFileType.forValue(this.GetValIntByKey(BillTemplate2019Attr.BillFileType));
	}
	public final void setHisBillFileType(BillFileType value) throws Exception
	{
		this.SetValByKey(BillTemplate2019Attr.BillFileType, value.getValue());
	}
	/** 
	 二维码生成方式
	 * @throws Exception 
	*/
	public final QRModel getQRModel() throws Exception
	{
		return QRModel.forValue(this.GetValIntByKey(BillTemplate2019Attr.QRModel));
	}
	public final void setQRModel(QRModel value) throws Exception
	{
		this.SetValByKey(BillTemplate2019Attr.QRModel, value.getValue());
	}
	public final TemplateFileModel getTemplateFileModel() throws Exception
	{
		return TemplateFileModel.forValue(this.GetValIntByKey(BillTemplate2019Attr.TemplateFileModel));
	}
	public final void setTemplateFileModel(TemplateFileModel value) throws Exception
	{
		this.SetValByKey(BillTemplate2019Attr.TemplateFileModel, value.getValue());
	}

	/** 
	 生成的单据打开方式
	 * @throws Exception 
	*/
	public final BillOpenModel getBillOpenModel() throws Exception
	{
		return BillOpenModel.forValue(this.GetValIntByKey(BillTemplate2019Attr.BillOpenModel));
	}
	public final void setBillOpenModel(BillOpenModel value) throws Exception
	{
		this.SetValByKey(BillTemplate2019Attr.BillOpenModel, value.getValue());
	}
	/** 
	 打开的连接
	 * @throws Exception 
	*/
	public final String getTempFilePath() throws Exception
	{
		String s = this.GetValStrByKey(BillTemplate2019Attr.TempFilePath);
		if (s == null || s.equals("") )
		{
			return this.getNo();
		}
		return s;
	}
	public final void setTempFilePath(String value) throws Exception
	{
		this.SetValByKey(BillTemplate2019Attr.TempFilePath, value);
	}
	/** 
	 节点名称
	 * @throws Exception 
	*/
	public final String getNodeName()  throws Exception
	{
		Node nd = new Node(this.getNodeID());
		return nd.getName();
	}
	/** 
	 节点ID
	 * @throws Exception 
	*/
	public final int getNodeID()  throws Exception
	{
		return this.GetValIntByKey(BillTemplate2019Attr.NodeID);
	}
	public final void setNodeID(int value) throws Exception
	{
		this.SetValByKey(BillTemplate2019Attr.NodeID, value);
	}

	public final String getMyFrmID() throws Exception
	{
		return this.GetValStringByKey(BillTemplate2019Attr.MyFrmID);
	}
	public final void setMyFrmID(String value) throws Exception
	{
		this.SetValByKey(BillTemplate2019Attr.MyFrmID, value);
	}


		///#endregion


		///#region 构造函数
	/** 
	 单据模板
	*/
	public BillTemplate2019()
	{
	}
	public BillTemplate2019(String no) throws Exception
	{
		super(no.replace("\n","").trim());
	}
	/** 
	 获得单据文件流
	 
	 @param oid
	 @return 
	 * @throws Exception 
	*/
	public final byte[] GenerTemplateFile() throws Exception
	{
		byte[] bytes = bp.da.DBAccess.GetByteFromDB(this.getEnMap().getPhysicsTable(), "No", this.getNo(), "DBFile");
		if (bytes != null)
		{
			return bytes;
		}

		//如果没有找到，就看看默认的文件是否有.
		String tempExcel = SystemConfig.getPathOfDataUser() + "CyclostyleFile/" + this.getNo() + ".rtf";
		if ((new File(tempExcel)).isFile() == false)
		{
			tempExcel = SystemConfig.getPathOfDataUser() + "CyclostyleFile/Word单据模版定义演示.docx";
		}

		bytes = bp.da.DataType.ConvertFileToByte(tempExcel);
		return bytes;
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap() throws Exception
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("WF_BillTemplate", "单据模板");

		map.Java_SetCodeStruct("6");

		map.IndexField = BillTemplate2019Attr.MyFrmID;

		map.AddTBStringPK(BillTemplate2019Attr.No, null, "No", true, true, 6, 6, 20);
		map.AddTBString(BillTemplate2019Attr.Name, null, "名称", true, false, 0, 200, 20);
		map.AddTBString(BillTemplate2019Attr.TempFilePath, null, "模板路径", true, false, 0, 200, 20);
		map.AddTBInt(BillTemplate2019Attr.NodeID, 0, "NodeID", true, false);
		map.AddTBString(BillTemplate2019Attr.MyFrmID, null, "表单编号", true, true, 0,300,300);

		map.AddDDLSysEnum(BillTemplate2019Attr.BillFileType, 0, "生成的文件类型", true, false, "BillFileType","@0=Word@1=PDF@2=Excel(未完成)@3=Html(未完成)");

		map.AddDDLSysEnum(BillTemplate2019Attr.BillOpenModel, 0, "生成的文件打开方式", true, true, "BillOpenModel", "@0=下载本地@1=在线WebOffice打开");

		map.AddDDLSysEnum(BillTemplate2019Attr.QRModel, 0, "二维码生成方式", true, true, BillTemplate2019Attr.QRModel, "@0=不生成@1=生成二维码");

		map.AddDDLSysEnum(BillTemplate2019Attr.TemplateFileModel, 1, "模版模式", true, false, BillTemplate2019Attr.TemplateFileModel, "@0=rtf模版@1=vsto模式的word模版@2=vsto模式的excel模版");

		map.AddTBString("Idx", null, "Idx", false, false, 0, 200, 20);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion
}