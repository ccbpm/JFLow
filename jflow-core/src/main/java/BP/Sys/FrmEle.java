package BP.Sys;

import BP.DA.*;
import BP.En.*;
import BP.En.Map;
import BP.Web.WebUser;

import java.util.*;

/** 
 表单元素扩展
*/
public class FrmEle extends EntityMyPK
{
	/** 
	 参数字段.
	 * @throws Exception 
	*/
	public final String getAtPara() throws Exception
	{
		return this.GetValStrByKey("AtPara");
	}
	public final void setAtPara(String value) throws Exception
	{
		this.SetValByKey("AtPara", value);
	}



		///#region 与 label 相关的属性 都存储到 AtParas 里面.
	/** 
	 文本
	 * @throws Exception 
	*/
	public final String getLabText() throws Exception
	{
		return this.GetValStrByKey(FrmEleAttr.EleName);
	}
	public final void setLabText(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.EleName, value);
	}
	/** 
	 风格
	 * @throws Exception 
	*/
	public final String getLabStyle() throws Exception
	{
		return this.GetValStrByKey(FrmEleAttr.AtPara);
	}
	public final void setLabStyle(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.AtPara, value);
	}

		///#endregion 与 label 相关的属性.


		///#region 与 link 相关的属性.
	public final String getLinkText() throws Exception
	{
		return this.GetValStrByKey(FrmEleAttr.EleName);
	}
	public final void setLinkText(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.EleName, value);
	}
	/** 
	 连接URL
	 * @throws Exception 
	*/
	public final String getLinkURL() throws Exception
	{
		return this.GetValStrByKey(FrmEleAttr.Tag1);
	}
	public final void setLinkURL(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.Tag1, value);
	}
	/** 
	 连接目标
	 * @throws Exception 
	*/
	public final String getLinkTarget() throws Exception
	{
		return this.GetValStrByKey(FrmEleAttr.Tag2);
	}
	public final void setLinkTarget(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.Tag2, value);
	}
	/** 
	 风格
	*/
	public final String getLinkStyle() throws Exception
	{
		return this.GetValStrByKey(FrmEleAttr.AtPara);
	}
	public final void setLinkStyle(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.AtPara, value);
	}

		///#endregion 与 link 相关的属性.


		///#region 与 line 相关的属性.
	/** 
	 线的颜色
	*/
	public final String getLineBorderColor() throws Exception
	{
		return this.GetValStrByKey(FrmEleAttr.Tag2, "Black");
	}
	public final void setLineBorderColor(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.Tag2, value);
	}
	/** 
	 宽度
	*/
	public final String getLineBorderWidth() throws Exception
	{
		return this.GetValStrByKey(FrmEleAttr.Tag1, "1");
	}
	public final void setLineBorderWidth(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.Tag1, value);
	}
	public final String getLineX1() throws Exception
	{
		return this.GetValStrByKey(FrmEleAttr.X);
	}
	public final void setLineX1(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.X, value);
	}
	public final String getLineY1() throws Exception
	{
		return this.GetValStrByKey(FrmEleAttr.Y);
	}
	public final void setLineY1(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.Y, value);
	}


	public final String getLineX2() throws Exception
	{
		return this.GetValStrByKey(FrmEleAttr.W);
	}
	public final void setLineX2(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.W, value);
	}
	public final String getLineY2() throws Exception
	{
		return this.GetValStrByKey(FrmEleAttr.H);
	}
	public final void setLineY2(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.H, value);
	}


		///#endregion 与线相关的属性.


		///#region 与 Btn 相关的属性.
	/** 
	 按钮ID
	*/
	public final String getBtnID() throws Exception
	{
		return this.GetValStrByKey(FrmEleAttr.EleID);
	}
	public final void setBtnID(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.EleID, value);
	}
	/** 
	 按钮文本
	*/
	public final String getBtnText() throws Exception
	{
		return this.GetValStrByKey(FrmEleAttr.EleName);
	}
	public final void setBtnText(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.EleName, value);
	}
	/** 
	 按钮类型
	*/
	public final int getBtnType() throws Exception
	{
		return this.GetValIntByKey(FrmEleAttr.Tag1);
	}
	public final void setBtnType(int value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.Tag1, value);
	}
	/** 
	 按钮事件类型
	*/
	public final BtnEventType getBtnEventType() throws Exception
	{
		return BtnEventType.forValue(this.getBtnType());
	}
	/** 
	 按钮事件内容
	*/
	public final String getBtnEventContext() throws Exception
	{
		return this.GetValStringByKey(FrmEleAttr.Tag2);
	}
	public final void setBtnEventContext(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.Tag2, value);
	}
	///// <summary>
	///// 执行成功
	///// </summary>
	//public string BtnMsgOK
	//{
	//    get
	//    {
	//        return this.GetParaString(FrmBtnAttr.MsgOK);
	//    }
	//    set
	//    {
	//        this.SetPara(FrmBtnAttr.MsgOK, value);
	//    }
	//}
	///// <summary>
	///// 执行失败
	///// </summary>
	//public string BtnMsgErr
	//{
	//    get
	//    {
	//        return this.GetParaString(FrmBtnAttr.MsgErr);
	//    }
	//    set
	//    {
	//        this.SetPara(FrmBtnAttr.MsgErr, value);
	//    }
	//}

		///#endregion 与线相关的属性.


		///#region 与 Img 相关的属性.
	/** 
	 路径
	*/
	public final String getImgPath() throws Exception
	{
		return this.GetValStringByKey(FrmEleAttr.Tag1);
	}
	public final void setImgPath(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.Tag1, value);
	}
	/** 
	 图片链接地址
	 * @throws Exception 
	*/
	public final String getImgLinkUrl() throws Exception
	{
		return this.GetValStringByKey(FrmEleAttr.Tag2);
	}
	public final void setImgLinkUrl(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.Tag2, value);
	}
	/** 
	 链接目标
	*/
	public final String getImgLinkTarget() throws Exception
	{
		return this.GetValStringByKey(FrmEleAttr.Tag3);
	}
	public final void setImgLinkTarget(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.Tag3, value);
	}

		///#endregion 与 Img 相关的属性.


		///#region 与 seal 相关的属性.
	/** 
	 签章ID
	*/
	public final String getSealID() throws Exception
	{
		return this.GetValStringByKey(FrmEleAttr.EleID);
	}
	public final void setSealID(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.EleID, value);
	}
	/** 
	 签章名称
	*/
	public final String getSealName() throws Exception
	{
		return this.GetValStringByKey(FrmEleAttr.EleName);
	} 
	public final void setSealName(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.EleName, value);
	}
	/** 
	 签章的岗位.
	 * @throws Exception 
	*/
	public final String getSealStations() throws Exception
	{
		return this.GetValStringByKey(FrmEleAttr.Tag1);
	}
	public final void setSealStations(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.Tag1, value);
	}
	/** 
	 来源
	 * @throws Exception 
	*/
	public final int getSealSrcType() throws Exception
	{
		return this.GetParaInt(FrmEleAttr.SrcType);
	}
	public final void setSealSrcType(int value) throws Exception
	{
		this.SetPara(FrmEleAttr.SrcType, value);
	}
	/** 
	 是否可以编辑?
	 * @throws Exception 
	*/
	public final boolean getSealIsEdit() throws Exception
	{
		return this.GetParaBoolen(FrmImgAttr.IsEdit);
	}
	public final void setSealIsEdit(boolean value) throws Exception
	{
		this.SetPara(FrmImgAttr.IsEdit, value);
	}

		///#endregion 与 seal 相关的属性.


		///#region  签名存储位置.
	public final String getHandSigantureSavePath() throws Exception
	{
		return this.GetValStrByKey(FrmEleAttr.Tag1);
	}
	public final String getHandSiganture_WinOpenH() throws Exception
	{
		return this.GetValStrByKey(FrmEleAttr.Tag2);
	}
	public final String getHandSiganture_WinOpenW() throws Exception
	{
		return this.GetValStrByKey(FrmEleAttr.Tag3);
	}
	public final String getHandSiganture_UrlPath() throws Exception
	{
		return this.GetValStrByKey(FrmEleAttr.Tag4);
	}

		///#endregion  签名存储位置


		///#region 类型
	/** 
	 标签
	*/
	public static final String Label = "Label";
	/** 
	 线
	*/
	public static final String Line = "Line";
	/** 
	 按钮
	*/
	public static final String Button = "Button";
	/** 
	 超连接
	*/
	public static final String Link = "Link";
	/** 
	 手工签名
	*/
	public static final String HandSiganture = "HandSiganture";
	/** 
	 电子签名
	*/
	public static final String EleSiganture = "EleSiganture";
	/** 
	 网页框架
	*/
	public static final String iFrame = "iFrame";
	/** 
	 分组
	*/
	public static final String Fieldset = "Fieldset";
	/** 
	 子线程
	*/
	public static final String SubThread = "SubThread";
	/** 
	 地图定位
	*/
	public static final String MapPin = "MapPin";
	/** 
	 录音
	*/
	public static final String Microphonehot = "Microphonehot";

		///#endregion 类型


		///#region 属性
	/** 
	 是否起用
	*/
	public final boolean getIsEnable() throws Exception
	{
		return this.GetValBooleanByKey(FrmEleAttr.IsEnable);
	}
	public final void setIsEnable(boolean value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.IsEnable, value);
	}
	/** 
	 EleID
	*/
	public final String getEleID() throws Exception
	{
		return this.GetValStrByKey(FrmEleAttr.EleID);
	}
	public final void setEleID(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.EleID, value);
	}
	/** 
	 EleName
	*/
	public final String getEleName() throws Exception
	{
		return this.GetValStringByKey(FrmEleAttr.EleName);
	}
	public final void setEleName(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.EleName, value);
	}
	/** 
	 Tag1
	*/
	public final String getTag1() throws Exception
	{
		return this.GetValStringByKey(FrmEleAttr.Tag1);
	}
	public final void setTag1(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.Tag1, value);
	}
	/** 
	 Tag2
	*/
	public final String getTag2() throws Exception
	{
		return this.GetValStringByKey(FrmEleAttr.Tag2);
	}
	public final void setTag2(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.Tag2, value);
	}
	/** 
	 Tag3
	*/
	public final String getTag3() throws Exception
	{
		return this.GetValStringByKey(FrmEleAttr.Tag3);
	}
	public final void setTag3(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.Tag3, value);
	}
	public final String getTag4() throws Exception
	{
		return this.GetValStringByKey(FrmEleAttr.Tag4);
	}
	public final void setTag4(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.Tag4, value);
	}

	/** 
	 FK_MapData
	*/
	public final String getFK_MapData() throws Exception
	{
		return this.GetValStrByKey(FrmEleAttr.FK_MapData);
	}
	public final void setFK_MapData(String value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.FK_MapData, value);
	}
	/** 
	 EleType
	*/
	public final String getEleType() throws Exception
	{
		return this.GetValStrByKey(FrmEleAttr.EleType);
	}
	public final void setEleType(String value) throws Exception
	{ 
		this.SetValByKey(FrmEleAttr.EleType, value);
	}
	public final float getX() throws Exception
	{
		return this.GetValFloatByKey(FrmEleAttr.X);
	}
	public final void setX(float value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.X, value);
	}
	public final float getY() throws Exception
	{
		return this.GetValFloatByKey(FrmEleAttr.Y);
	}
	public final void setY(float value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.Y, value);
	}
	public final float getH() throws Exception
	{
		return this.GetValFloatByKey(FrmEleAttr.H);
	}
	public final void setH(float value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.H, value);
	}
	public final float getW() throws Exception
	{
		return this.GetValFloatByKey(FrmEleAttr.W);
	}
	public final void setW(float value) throws Exception
	{
		this.SetValByKey(FrmEleAttr.W, value);
	}

	public final int getHOfInt() throws Exception
	{
		return Integer.parseInt((new Float(this.getH())).toString());
	}
	public final int getWOfInt() throws Exception
	{
		return Integer.parseInt((new Float(this.getW())).toString());
	}

		///#endregion


		///#region 权限问题
	/** 
	 实体的权限控制
	 * @throws Exception 
	*/
	@Override
	public UAC getHisUAC() throws Exception
	{

		UAC uac = new UAC();
		if (WebUser.getNo().equals("admin"))
		{
			uac.IsDelete = true;
			uac.IsUpdate = true;
			uac.IsInsert = false;
			uac.IsView = true;
		}
		else
		{
			uac.IsView = true;
		}
		uac.IsImp = true;
		return uac;
	}

		///#endregion 权限问题


		///#region 构造方法
	/** 
	 表单元素扩展
	*/
	public FrmEle()
	{
	}
	/** 
	 表单元素扩展
	 
	 @param mypk
	 * @throws Exception 
	*/
	public FrmEle(String mypk) throws Exception
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

		Map map = new Map("Sys_FrmEle", "表单元素扩展");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);
		map.IndexField = FrmEleAttr.FK_MapData;


			//主键.
		map.AddMyPK();

		map.AddTBString(FrmEleAttr.FK_MapData, null, "表单ID", true, false, 1, 100, 20);
		map.AddTBString(FrmEleAttr.EleType, null, "类型", true, false, 0, 50, 20);
		map.AddTBString(FrmEleAttr.EleID, null, "控件ID值(对部分控件有效)", true, false, 0, 50, 20);
		map.AddTBString(FrmEleAttr.EleName, null, "控件名称(对部分控件有效)", true, false, 0, 200, 20);


			///#region 位置其他.
		map.AddTBFloat(FrmEleAttr.X, 5, "X", true, false);
		map.AddTBFloat(FrmEleAttr.Y, 5, "Y", false, false);

		map.AddTBFloat(FrmEleAttr.H, 20, "H", true, false);
		map.AddTBFloat(FrmEleAttr.W, 20, "W", false, false);

			///#endregion 位置其他.


			///#region 参数.
		map.AddTBString(FrmEleAttr.Tag1, null, "链接URL", true, false, 0, 50, 20);
		map.AddTBString(FrmEleAttr.Tag2, null, "Tag2", true, false, 0, 50, 20);
		map.AddTBString(FrmEleAttr.Tag3, null, "Tag3", true, false, 0, 50, 20);
		map.AddTBString(FrmEleAttr.Tag4, null, "Tag4", true, false, 0, 50, 20);
		map.AddTBString(FrmEleAttr.GUID, null, "GUID", true, false, 0, 128, 20);

			///#endregion 参数.


			///#region 控制属性.
		map.AddTBInt(FrmEleAttr.IsEnable, 1, "是否启用", false, false);

			///#endregion 控制属性.


			//参数.
		map.AddTBAtParas(4000);

		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		if (!this.getEleID().equals(""))
		{
			this.setMyPK(this.getFK_MapData() + "_" + this.getEleType() + "_" + this.getEleID());
		}
		else
		{
			this.setMyPK(this.getFK_MapData() + "_" + this.getEleType());
		}
		return super.beforeUpdateInsertAction();
	}
}