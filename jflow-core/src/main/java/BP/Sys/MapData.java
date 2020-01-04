package BP.Sys;

import BP.DA.*;
import BP.Difference.SystemConfig;
import BP.En.*;
import BP.En.Map;
import BP.Tools.DateUtils;
import BP.WF.RefObject;
import BP.Web.WebUser;

import java.util.*;
import java.io.*;
import java.time.*;

/** 
 映射基础
*/
public class MapData extends EntityNoName
{

		///#region entity 相关属性(参数属性)
	/** 
	 属性ens
	 * @throws Exception 
	*/
	public final String getEnsName() throws Exception
	{
		return this.GetValStringByKey(MapDataAttr.EnsName);
	}
	public final void setEnsName(String value) throws Exception
	{
		this.SetPara(MapDataAttr.EnsName, value);
	}

		///#endregion entity 相关操作.



		///#region weboffice文档属性(参数属性)
	/** 
	 是否启用锁定行
	 * @throws Exception 
	*/
	public final boolean getIsRowLock() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsRowLock, false);
	}
	public final void setIsRowLock(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsRowLock, value);
	}

	/** 
	 是否启用打印
	 * @throws Exception 
	*/
	public final boolean getIsWoEnablePrint() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnablePrint);
	}
	public final void setIsWoEnablePrint(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnablePrint, value);
	}
	/** 
	 是否启用只读
	 * @throws Exception 
	*/
	public final boolean getIsWoEnableReadonly() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableReadonly);
	}
	public final void setIsWoEnableReadonly(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableReadonly, value);
	}
	/** 
	 是否启用修订
	 * @throws Exception 
	*/
	public final boolean getIsWoEnableRevise() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableRevise);
	}
	public final void setIsWoEnableRevise(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableRevise, value);
	}
	/** 
	 是否启用保存
	 * @throws Exception 
	*/
	public final boolean getIsWoEnableSave() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableSave);
	}
	public final void setIsWoEnableSave(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableSave, value);
	}
	/** 
	 是否查看用户留痕
	 * @throws Exception 
	*/
	public final boolean getIsWoEnableViewKeepMark() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableViewKeepMark);
	}
	public final void setIsWoEnableViewKeepMark(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableViewKeepMark, value);
	}
	/** 
	 是否启用weboffice
	 * @throws Exception 
	*/
	public final boolean getIsWoEnableWF() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableWF);
	}
	public final void setIsWoEnableWF(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableWF, value);
	}

	/** 
	 是否启用套红
	 * @throws Exception 
	*/
	public final boolean getIsWoEnableOver() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableOver);
	}
	public final void setIsWoEnableOver(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableOver, value);
	}

	/** 
	 是否启用签章
	 * @throws Exception 
	*/
	public final boolean getIsWoEnableSeal() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableSeal);
	}
	public final void setIsWoEnableSeal(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableSeal, value);
	}

	/** 
	 是否启用公文模板
	 * @throws Exception 
	*/
	public final boolean getIsWoEnableTemplete() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableTemplete);
	}
	public final void setIsWoEnableTemplete(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableTemplete, value);
	}

	/** 
	 是否记录节点信息
	 * @throws Exception 
	*/
	public final boolean getIsWoEnableCheck() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableCheck);
	}
	public final void setIsWoEnableCheck(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableCheck, value);
	}

	/** 
	 是否插入流程图
	 * @throws Exception 
	*/
	public final boolean getIsWoEnableInsertFlow() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableInsertFlow);
	}
	public final void setIsWoEnableInsertFlow(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableInsertFlow, value);
	}

	/** 
	 是否插入风险点
	 * @throws Exception 
	*/
	public final boolean getIsWoEnableInsertFengXian() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableInsertFengXian);
	}
	public final void setIsWoEnableInsertFengXian(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableInsertFengXian, value);
	}

	/** 
	 是否启用留痕模式
	 * @throws Exception 
	*/
	public final boolean getIsWoEnableMarks() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableMarks);
	}
	public final void setIsWoEnableMarks(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableMarks, value);
	}
	/** 
	 是否插入风险点
	 * @throws Exception 
	*/
	public final boolean getIsWoEnableDown() throws Exception
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableDown);
	}
	public final void setIsWoEnableDown(boolean value) throws Exception
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableDown, value);
	}

		///#endregion weboffice文档属性


		///#region 自动计算属性.
	public final float getMaxLeft() throws Exception
	{
		return this.GetParaFloat(MapDataAttr.MaxLeft);
	}
	public final void setMaxLeft(float value) throws Exception
	{
		this.SetPara(MapDataAttr.MaxLeft, value);
	}
	public final float getMaxRight() throws Exception
	{
		return this.GetParaFloat(MapDataAttr.MaxRight);
	}
	public final void setMaxRight(float value) throws Exception
	{
		this.SetPara(MapDataAttr.MaxRight, value);
	}
	public final float getMaxTop() throws Exception
	{
		return this.GetParaFloat(MapDataAttr.MaxTop);
	}
	public final void setMaxTop(float value) throws Exception
	{
		this.SetPara(MapDataAttr.MaxTop, value);
	}
	public final float getMaxEnd() throws Exception
	{
		return this.GetParaFloat(MapDataAttr.MaxEnd);
	}
	public final void setMaxEnd(float value) throws Exception
	{
		this.SetPara(MapDataAttr.MaxEnd, value);
	}

		///#endregion 自动计算属性.


		///#region 报表属性(参数方式存储).
	/** 
	 是否关键字查询
	 * @throws Exception 
	*/
	public final boolean getRptIsSearchKey() throws Exception
	{
		return this.GetParaBoolen(MapDataAttr.RptIsSearchKey, true);
	}
	public final void setRptIsSearchKey(boolean value) throws Exception
	{
		this.SetPara(MapDataAttr.RptIsSearchKey, value);
	}
	/** 
	 时间段查询方式
	 * @throws Exception 
	*/
	public final DTSearchWay getRptDTSearchWay() throws Exception
	{
		return DTSearchWay.forValue(this.GetParaInt(MapDataAttr.RptDTSearchWay));
	}
	public final void setRptDTSearchWay(DTSearchWay value) throws Exception
	{
		this.SetPara(MapDataAttr.RptDTSearchWay, value.getValue());
	}
	/** 
	 时间字段
	 * @throws Exception 
	*/
	public final String getRptDTSearchKey() throws Exception
	{
		return this.GetParaString(MapDataAttr.RptDTSearchKey);
	}
	public final void setRptDTSearchKey(String value) throws Exception
	{
		this.SetPara(MapDataAttr.RptDTSearchKey, value);
	}
	/** 
	 查询外键枚举字段
	 * @throws Exception 
	*/
	public final String getRptSearchKeys() throws Exception
	{
		return this.GetParaString(MapDataAttr.RptSearchKeys, "*");
	}
	public final void setRptSearchKeys(String value) throws Exception
	{
		this.SetPara(MapDataAttr.RptSearchKeys, value);
	}

		///#endregion 报表属性(参数方式存储).


		///#region 外键属性
	/** 
	版本号.
	 * @throws Exception 
	*/
	public final String getVer() throws Exception
	{
		return this.GetValStringByKey(MapDataAttr.Ver);
	}
	public final void setVer(String value) throws Exception
	{
		this.SetValByKey(MapDataAttr.Ver, value);
	}
	/** 
	 顺序号
	 * @throws Exception 
	*/
	public final int getIdx() throws Exception
	{
		return this.GetValIntByKey(MapDataAttr.Idx);
	}
	public final void setIdx(int value) throws Exception
	{
		this.SetValByKey(MapDataAttr.Idx, value);
	}
	/** 
	 扩展控件
	 * @throws Exception 
	*/
	public final FrmEles getHisFrmEles() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmEles");
		FrmEles obj = tempVar instanceof FrmEles ? (FrmEles)tempVar : null;
		if (obj == null)
		{
			obj = new FrmEles(this.getNo());
			this.SetRefObject("FrmEles", obj);
		}
		return obj;
	}
	/** 
	 框架
	 * @throws Exception 
	*/
	public final MapFrames getMapFrames() throws Exception
	{
		Object tempVar = this.GetRefObject("MapFrames");
		MapFrames obj = tempVar instanceof MapFrames ? (MapFrames)tempVar : null;
		if (obj == null)
		{
			obj = new MapFrames(this.getNo());
			this.SetRefObject("MapFrames", obj);
		}
		return obj;
	}
	/** 
	 分组字段
	 * @throws Exception 
	*/
	public final GroupFields getGroupFields() throws Exception
	{
		Object tempVar = this.GetRefObject("GroupFields");
		GroupFields obj = tempVar instanceof GroupFields ? (GroupFields)tempVar : null;
		if (obj == null)
		{
			obj = new GroupFields(this.getNo());
			this.SetRefObject("GroupFields", obj);
		}
		return obj;
	}
	/** 
	 逻辑扩展
	 * @throws Exception 
	*/
	public final MapExts getMapExts() throws Exception
	{
		Object tempVar = this.GetRefObject("MapExts");
		MapExts obj = tempVar instanceof MapExts ? (MapExts)tempVar : null;
		if (obj == null)
		{
			obj = new MapExts(this.getNo());
			this.SetRefObject("MapExts", obj);
		}
		return obj;
	}
	/** 
	 事件
	 * @throws Exception 
	*/
	public final FrmEvents getFrmEvents() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmEvents");
		FrmEvents obj = tempVar instanceof FrmEvents ? (FrmEvents)tempVar : null;
		if (obj == null)
		{
			obj = new FrmEvents(this.getNo());
			this.SetRefObject("FrmEvents", obj);
		}
		return obj;
	}
	/** 
	 从表原始属性的获取
	 * @throws Exception 
	*/
	public final MapDtls getOrigMapDtls() throws Exception
	{
		Object tempVar = this.GetRefObject("MapDtls");
		MapDtls obj = tempVar instanceof MapDtls ? (MapDtls)tempVar : null;
		if (obj == null)
		{
			obj = new MapDtls();
			obj.Retrieve(MapDtlAttr.FK_MapData, this.getNo(), MapDtlAttr.FK_Node, 0);
			this.SetRefObject("MapDtls", obj);
		}
		return obj;
	}
	/** 
	 查询给MapData下的所有从表数据
	 * @throws Exception 
	*/
	public final MapDtls getMapDtls() throws Exception
	{
		Object tempVar = this.GetRefObject("MapDtls");
		MapDtls obj = tempVar instanceof MapDtls ? (MapDtls)tempVar : null;
		if (obj == null)
		{
			obj = new MapDtls(this.getNo());
			this.SetRefObject("MapDtls", obj);
		}
		return obj;
	}
	/** 
	 枚举值
	 * @throws Exception 
	*/
	public final SysEnums getSysEnums() throws Exception
	{
		Object tempVar = this.GetRefObject("SysEnums");
		SysEnums obj = tempVar instanceof SysEnums ? (SysEnums)tempVar : null;
		if (obj == null)
		{
			obj = new SysEnums();
			if (SystemConfig.getAppCenterDBType() == DBType.MySQL)
			{
				String strs = "";

				DataTable dt = DBAccess.RunSQLReturnTable("SELECT UIBindKey FROM Sys_MapAttr WHERE FK_MapData='" + this.getNo() + "' AND LGType=1  ");

				for (DataRow dr : dt.Rows)
				{
					strs += "'" + dr.getValue(0).toString() + "',";
				}

				if (dt.Rows.size() >= 1)
				{
					strs += "'ssss'";
					obj.RetrieveInOrderBy("EnumKey", strs, SysEnumAttr.IntKey);
				}


			}
			else
			{
				obj.RetrieveInSQL(SysEnumAttr.EnumKey, "SELECT UIBindKey FROM Sys_MapAttr WHERE FK_MapData='" + this.getNo() + "' AND LGType=1 ", SysEnumAttr.IntKey);
			}
			this.SetRefObject("SysEnums", obj);

		}
		return obj;
	}
	/** 
	 报表
	 * @throws Exception 
	*/
	public final FrmRpts getFrmRpts() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmRpts");
		FrmRpts obj = tempVar instanceof FrmRpts ? (FrmRpts)tempVar : null;
		if (obj == null)
		{
			obj = new FrmRpts(this.getNo());
			this.SetRefObject("FrmRpts", obj);
		}
		return obj;
	}
	/** 
	 超连接
	 * @throws Exception 
	*/
	public final FrmLinks getFrmLinks() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmLinks");
		FrmLinks obj = tempVar instanceof FrmLinks ? (FrmLinks)tempVar : null;
		if (obj == null)
		{
			obj = new FrmLinks(this.getNo());
			this.SetRefObject("FrmLinks", obj);
		}
		return obj;
	}
	/** 
	 按钮
	 * @throws Exception 
	*/
	public final FrmBtns getFrmBtns() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmBtns");
		FrmBtns obj = tempVar instanceof FrmBtns ? (FrmBtns)tempVar : null;
		if (obj == null)
		{
			obj = new FrmBtns(this.getNo());
			this.SetRefObject("FrmBtns", obj);
		}
		return obj;
	}

	/** 
	 元素
	 * @throws Exception 
	*/
	public final FrmEles getFrmEles() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmEles");
		FrmEles obj = tempVar instanceof FrmEles ? (FrmEles)tempVar : null;
		if (obj == null)
		{
			obj = new FrmEles(this.getNo());
			this.SetRefObject("FrmEles", obj);
		}
		return obj;
	}
	/** 
	 线
	 * @throws Exception 
	*/
	public final FrmLines getFrmLines() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmLines");
		FrmLines obj = tempVar instanceof FrmLines ? (FrmLines)tempVar : null;
		if (obj == null)
		{
			obj = new FrmLines(this.getNo());
			this.SetRefObject("FrmLines", obj);
		}
		return obj;
	}
	/** 
	 标签
	 * @throws Exception 
	*/
	public final FrmLabs getFrmLabs() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmLabs");
		FrmLabs obj = tempVar instanceof FrmLabs ? (FrmLabs)tempVar : null;
		if (obj == null)
		{
			obj = new FrmLabs(this.getNo());
			this.SetRefObject("FrmLabs", obj);
		}
		return obj;
	}
	/** 
	 图片
	 * @throws Exception 
	*/
	public final FrmImgs getFrmImgs() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmImgs");
		FrmImgs obj = tempVar instanceof FrmImgs ? (FrmImgs)tempVar : null;
		if (obj == null)
		{
			obj = new FrmImgs(this.getNo());
			this.SetRefObject("FrmImgs", obj);
		}
		return obj;
	}
	/** 
	 附件
	*/
	public final FrmAttachments getFrmAttachments() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmAttachments");
		FrmAttachments obj = tempVar instanceof FrmAttachments ? (FrmAttachments)tempVar : null;
		if (obj == null)
		{
			obj = new FrmAttachments(this.getNo());
			this.SetRefObject("FrmAttachments", obj);
		}
		return obj;
	}
	/** 
	 图片附件
	*/
	public final FrmImgAths getFrmImgAths() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmImgAths");
		FrmImgAths obj = tempVar instanceof FrmImgAths ? (FrmImgAths)tempVar : null;
		if (obj == null)
		{
			obj = new FrmImgAths(this.getNo());
			this.SetRefObject("FrmImgAths", obj);
		}
		return obj;
	}

	// <summary>
	/** 图片附件记录
	*/
	public final FrmImgAthDBs getFrmImgAthDB() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmImgAthDBs");
		FrmImgAthDBs obj = tempVar instanceof FrmImgAthDBs ? (FrmImgAthDBs)tempVar : null;
		if (obj == null)
		{
			obj = new FrmImgAthDBs(this.getNo());
			this.SetRefObject("FrmImgAthDBs", obj);
		}
		return obj;
	}
	/** 
	 单选按钮
	*/
	public final FrmRBs getFrmRBs() throws Exception
	{
		Object tempVar = this.GetRefObject("FrmRBs");
		FrmRBs obj = tempVar instanceof FrmRBs ? (FrmRBs)tempVar : null;
		if (obj == null)
		{
			obj = new FrmRBs(this.getNo());
			this.SetRefObject("FrmRBs", obj);
		}
		return obj;
	}
	/** 
	 属性
	*/
	public final MapAttrs getMapAttrs() throws Exception
	{
		Object tempVar = this.GetRefObject("MapAttrs");
		MapAttrs obj = tempVar instanceof MapAttrs ? (MapAttrs)tempVar : null;
		if (obj == null)
		{
			obj = new MapAttrs(this.getNo());
			this.SetRefObject("MapAttrs", obj);
		}
		return obj;
	}

		///#endregion

	public final void CleanObject() throws Exception
	{
		this.getRow().SetValByKey("FrmEles", null);
		this.getRow().SetValByKey("MapFrames", null);
		this.getRow().SetValByKey("GroupFields", null);
		this.getRow().SetValByKey("MapExts", null);
		this.getRow().SetValByKey("FrmEvents", null);
		this.getRow().SetValByKey("MapDtls", null);
		this.getRow().SetValByKey("SysEnums", null);
		this.getRow().SetValByKey("FrmRpts", null);
		this.getRow().SetValByKey("FrmLinks", null);
		this.getRow().SetValByKey("FrmBtns", null);
		this.getRow().SetValByKey("FrmEles", null);
		this.getRow().SetValByKey("FrmLines", null);
		this.getRow().SetValByKey("FrmLabs", null);
		this.getRow().SetValByKey("FrmAttachments", null);
		this.getRow().SetValByKey("FrmImgAthDBs", null);
		this.getRow().SetValByKey("FrmRBs", null);
		this.getRow().SetValByKey("MapAttrs", null);
		return;
 
	}

	/** 
	 清空缓存
	*/
	public final void ClearCash() throws Exception
	{
		  //zhoupeng add2020.1.4
        BP.DA.Cash.getSQL_Cash().remove(this.getNo());
        BP.DA.Cash.getMap_Cash().remove(this.getNo());        
		BP.DA.CashFrmTemplate.Remove(this.getNo());
		
		CleanObject();
	}


		///#region 基本属性.
	/** 
	 事件实体
	*/
	public final String getFormEventEntity() throws Exception
	{
		return this.GetValStringByKey(MapDataAttr.FormEventEntity);
	}
	public final void setFormEventEntity(String value) throws Exception
	{
		this.SetValByKey(MapDataAttr.FormEventEntity, value);
	}

	public static boolean getIsEditDtlModel() throws Exception
	{
		String s = WebUser.GetSessionByKey("IsEditDtlModel", "0");
		if (s.equals("0"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	public static void setIsEditDtlModel(boolean value)
	{
		WebUser.SetSessionByKey("IsEditDtlModel", "1");
	}

	/** 
	 生成Frm
	 
	 @return 
	 * @throws Exception 
	*/
	public final String GenerHisFrm() throws Exception
	{
		String body = BP.DA.DataType.ReadTextFile(SystemConfig.getPathOfWebApp() + "\\WF\\Admin\\CCFormDesigner\\EleTemplate\\Body.txt");

		//替换高度宽度.
		body = body.replace("@FrmH", String.valueOf(this.getFrmH()));
		body = body.replace("@FrmW", String.valueOf(this.getFrmW()));

		String labTemplate = BP.DA.DataType.ReadTextFile(SystemConfig.getPathOfWebApp() + "\\WF\\Admin\\CCFormDesigner\\EleTemplate\\Label.txt");
		String myLabs = "";
		FrmLabs labs = new FrmLabs(this.getNo());
		for (FrmLab lab : labs.ToJavaList())
		{
			Object tempVar = labTemplate;
			String labTxt = tempVar instanceof String ? (String)tempVar : null;

			labTxt = labTxt.replace("@MyPK", lab.getMyPK());

			labTxt = labTxt.replace("@Label", lab.getText());
			labTxt = labTxt.replace("@X", String.valueOf(lab.getX()));
			labTxt = labTxt.replace("@Y", String.valueOf(lab.getY()));

			float y1 = lab.getY() - 20;
			labTxt = labTxt.replace("@Y1", String.valueOf(y1));
			myLabs += labTxt + ",";
		}

		if (myLabs.equals(""))
		{
			body = body.replace("@Labels", myLabs);
		}
		else
		{
			myLabs = myLabs.substring(0, myLabs.length() - 1);
			body = body.replace("@Labels", myLabs);
		}
		return body;
	}

		///#endregion


		///#region 属性
	/** 
	 物理表
	 * @throws Exception 
	*/
	public final String getPTable() throws Exception
	{
		String s = this.GetValStrByKey(MapDataAttr.PTable);
		if (s == null || s.equals("") )
		{
			return this.getNo();
		}
		return s;
	}
	public final void setPTable(String value) throws Exception
	{
		this.SetValByKey(MapDataAttr.PTable, value);
	}
	/** 
	 表存储模式0=自定义表,1,指定的表,2=指定的表不能修改表结构.
	 * @throws Exception 
	 @周朋
	*/
	public final int getPTableModel() throws Exception
	{
		return this.GetValIntByKey(MapDataAttr.PTableModel);
	}
	public final void setPTableModel(int value) throws Exception
	{
		this.SetValByKey(MapDataAttr.PTableModel, value);
	}
	/** 
	 URL
	 * @throws Exception 
	*/
	public final String getUrl() throws Exception
	{
		return this.GetValStrByKey(MapDataAttr.Url);
	}
	public final void setUrl(String value) throws Exception
	{
		this.SetValByKey(MapDataAttr.Url, value);
	}
	public final DBUrlType getHisDBUrl()
	{
		return DBUrlType.AppCenterDSN;
			// return (DBUrlType)this.GetValIntByKey(MapDataAttr.DBURL);
	}
	public final int getHisFrmTypeInt() throws Exception
	{
		return this.GetValIntByKey(MapDataAttr.FrmType);
	}
	public final void setHisFrmTypeInt(int value) throws Exception
	{
		this.SetValByKey(MapDataAttr.FrmType, value);
	}
	public final FrmType getHisFrmType() throws Exception
	{
		return FrmType.forValue(this.GetValIntByKey(MapDataAttr.FrmType));
	}
	public final void setHisFrmType(FrmType value) throws Exception
	{
		this.SetValByKey(MapDataAttr.FrmType, value.getValue());
	}
	/** 
	 表单类型名称
	 * @throws Exception 
	*/
	public final String getHisFrmTypeText() throws Exception
	{
		return this.getHisFrmType().toString();

			//  SysEnum se = new SysEnum("FrmType", this.HisFrmTypeInt);
			// return se.Lab;
	}
	/** 
	 备注
	 * @throws Exception 
	*/
	public final String getNote() throws Exception
	{
		return this.GetValStrByKey(MapDataAttr.Note);
	}
	public final void setNote(String value) throws Exception
	{
		this.SetValByKey(MapDataAttr.Note, value);
	}
	/** 
	 是否有CA.
	 * @throws Exception 
	*/
	public final boolean getIsHaveCA() throws Exception
	{
		return this.GetParaBoolen("IsHaveCA", false);

	}
	public final void setIsHaveCA(boolean value) throws Exception
	{
		this.SetPara("IsHaveCA", value);
	}
	/** 
	 类别，可以为空.
	 * @throws Exception 
	*/
	public final String getFK_FrmSort() throws Exception
	{
		return this.GetValStrByKey(MapDataAttr.FK_FrmSort);
	}
	public final void setFK_FrmSort(String value) throws Exception
	{
		this.SetValByKey(MapDataAttr.FK_FrmSort, value);
	}
	/** 
	 数据源
	*/
	public final String getDBSrc() throws Exception
	{
		return this.GetValStrByKey(MapDataAttr.DBSrc);
	}
	public final void setDBSrc(String value) throws Exception
	{
		this.SetValByKey(MapDataAttr.DBSrc, value);
	}

	/** 
	 类别，可以为空.
	*/
	public final String getFK_FormTree() throws Exception
	{
		return this.GetValStrByKey(MapDataAttr.FK_FormTree);
	}
	public final void setFK_FormTree(String value) throws Exception
	{
		this.SetValByKey(MapDataAttr.FK_FormTree, value);
	}
	/** 
	 类别名称
	 * @throws Exception 
	*/
	public final String getFK_FormTreeText() throws Exception
	{
		return DBAccess.RunSQLReturnStringIsNull("SELECT Name FROM Sys_FormTree WHERE No='" + this.getFK_FormTree() + "'", "目录错误");
	}
	/** 
	 从表集合.
	*/
	public final String getDtls() throws Exception
	{
		return this.GetValStrByKey(MapDataAttr.Dtls);
	}
	public final void setDtls(String value) throws Exception
	{
		this.SetValByKey(MapDataAttr.Dtls, value);
	}
	/** 
	 主键
	*/
	public final String getEnPK() throws Exception
	{
		String s = this.GetValStrByKey(MapDataAttr.EnPK);
		if (DataType.IsNullOrEmpty(s))
		{
			return "OID";
		}
		return s;
	}
	public final void setEnPK(String value) throws Exception
	{
		this.SetValByKey(MapDataAttr.EnPK, value);
	}
	private Entities _HisEns = null;
	public final Entities getHisEns() throws Exception
	{
		if (_HisEns == null)
		{
			_HisEns = BP.En.ClassFactory.GetEns(this.getNo());
		}
		return _HisEns;
	}
	public final Entity getHisEn() throws Exception
	{
		return this.getHisEns().getNewEntity();
	}
	public final float getFrmW() throws Exception
	{
		return this.GetValFloatByKey(MapDataAttr.FrmW);
	}
	public final void setFrmW(float value) throws Exception
	{
		this.SetValByKey(MapDataAttr.FrmW, value);
	}
	public final float getFrmH() throws Exception
	{
		return this.GetValFloatByKey(MapDataAttr.FrmH);
	}
	public final void setFrmH(float value) throws Exception
	{
		this.SetValByKey(MapDataAttr.FrmH, value);
	}
	/** 
	 应用类型.  0独立表单.1节点表单
	*/
	public final String getAppType() throws Exception
	{
		return this.GetValStrByKey(MapDataAttr.AppType);
	}
	public final void setAppType(String value)throws Exception
	{
		this.SetValByKey(MapDataAttr.AppType, value);
	}
	/** 
	 表单body属性.
	*/
	public final String getBodyAttr() throws Exception
	{
		String str = this.GetValStrByKey(MapDataAttr.BodyAttr);
		str = str.replace("~", "'");
		return str;
	}
	public final void setBodyAttr(String value) throws Exception
	{
		this.SetValByKey(MapDataAttr.BodyAttr, value);
	}
	/** 
	 流程控件s.
	*/
	public final String getFlowCtrls() throws Exception
	{
		return this.GetValStrByKey(MapDataAttr.FlowCtrls);
	}
	public final void setFlowCtrls(String value) throws Exception
	{
		this.SetValByKey(MapDataAttr.FlowCtrls, value);
	}

	public final int getTableCol() throws Exception
	{
		return this.GetValIntByKey(MapDataAttr.TableCol);
	}
	public final void setTableCol(int value) throws Exception
	{
		this.SetValByKey(MapDataAttr.TableCol, value);
	}


		///#endregion


		///#region 构造方法
	public final Map GenerHisMap() throws Exception
	{
		MapAttrs mapAttrs = this.getMapAttrs();
		if (mapAttrs.size() == 0)
		{
			this.RepairMap();
			mapAttrs = this.getMapAttrs();
		}

		Map map = new Map(this.getPTable(), this.getName());
		map.setEnDBUrl(new DBUrl(this.getHisDBUrl()));
		map.Java_SetEnType(EnType.App);
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);

		Attrs attrs = new Attrs();
		for (MapAttr mapAttr : mapAttrs.ToJavaList())
		{
			map.AddAttr(mapAttr.getHisAttr());
		}

		// 产生从表。
		MapDtls dtls = this.getMapDtls(); // new MapDtls(this.No);
		for (MapDtl dtl : dtls.ToJavaList())
		{
			GEDtls dtls1 = new GEDtls(dtl.getNo());
			map.AddDtl(dtls1, "RefPK");
		}


			///#region 查询条件.
		map.IsShowSearchKey = this.getRptIsSearchKey(); //是否启用关键字查询.
		// 按日期查询.
		map.DTSearchWay = this.getRptDTSearchWay(); //日期查询方式.
		map.DTSearchKey = this.getRptDTSearchKey(); //日期字段.

		//加入外键查询字段.
		String[] keys = this.getRptSearchKeys().split("[*]", -1);
		for (String key : keys)
		{
			if (DataType.IsNullOrEmpty(key))
			{
				continue;
			}

			map.AddSearchAttr(key);
		}

			///#endregion 查询条件.

		return map;
	}
	private GEEntity _HisEn = null;
	public final GEEntity getHisGEEn() throws Exception
	{
		if (this._HisEn == null)
		{
			_HisEn = new GEEntity(this.getNo());
		}
		return _HisEn;
	}
	/** 
	 生成实体
	 
	 @param ds
	 @return 
	 * @throws Exception 
	*/
	public final GEEntity GenerGEEntityByDataSet(DataSet ds) throws Exception
	{
		// New 它的实例.
		GEEntity en = this.getHisGEEn();

		// 它的table.
		DataTable dt = ds.GetTableByName(this.getNo());

		//装载数据.
		en.getRow().LoadDataTable(dt, dt.Rows.get(0));

		// dtls.
		MapDtls dtls = this.getMapDtls();
		for (MapDtl item : dtls.ToJavaList())
		{
			DataTable dtDtls = ds.GetTableByName(item.getNo());
			GEDtls dtlsEn = new GEDtls(item.getNo());
			for (DataRow dr : dtDtls.Rows)
			{
				// 产生它的Entity data.
				GEDtl dtl = (GEDtl)dtlsEn.getNewEntity();
				dtl.getRow().LoadDataTable(dtDtls, dr);

				//加入这个集合.
				dtlsEn.AddEntity(dtl);
			}

			//加入到他的集合里.
			en.getDtls().add(dtDtls);
		}
		return en;
	}
	/** 
	 生成map.
	 
	 @param no
	 @return 
	 * @throws Exception 
	*/
	public static Map GenerHisMap(String no) throws Exception
	{
		if (SystemConfig.getIsDebug())
		{
			MapData md = new MapData();
			md.setNo(no);
			md.Retrieve();
			return md.GenerHisMap();
		}
		else
		{
			Map map = BP.DA.Cash.GetMap(no);
			if (map == null)
			{
				MapData md = new MapData();
				md.setNo(no);
				md.Retrieve();
				map = md.GenerHisMap();
				BP.DA.Cash.SetMap(no, map);
			}
			return map;
		}
	}
	/** 
	 映射基础
	*/
	public MapData()
	{
	}
	/** 
	 映射基础
	 
	 @param no 映射编号
	 * @throws Exception 
	*/
	public MapData(String no) throws Exception
	{
		super(no);
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

		Map map = new Map("Sys_MapData", "表单注册表");
			//map.Java_SetDepositaryOfEntity(Depositary.None);  会出错如果放入到内存.
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);
		map.Java_SetCodeStruct("4");


			///#region 基础信息.
		map.AddTBStringPK(MapDataAttr.No, null, "编号", true, false, 1, 200, 100);
		map.AddTBString(MapDataAttr.Name, null, "描述", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.FormEventEntity, null, "事件实体", true, true, 0, 100, 20, true);

		map.AddTBString(MapDataAttr.EnPK, null, "实体主键", true, false, 0, 200, 20);
		map.AddTBString(MapDataAttr.PTable, null, "物理表", true, false, 0, 500, 20);

			//@周朋 表存储格式0=自定义表,1=指定表,可以修改字段2=执行表不可以修改字段.
		map.AddTBInt(MapDataAttr.PTableModel, 0, "表存储模式", true, true);


		map.AddTBString(MapDataAttr.Url, null, "连接(对嵌入式表单有效)", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.Dtls, null, "从表", true, false, 0, 500, 20);

			//格式为: @1=方案名称1@2=方案名称2@3=方案名称3
			//map.AddTBString(MapDataAttr.Slns, null, "表单控制解决方案", true, false, 0, 500, 20);

		map.AddTBInt(MapDataAttr.FrmW, 900, "FrmW", true, true);
		map.AddTBInt(MapDataAttr.FrmH, 1200, "FrmH", true, true);

		map.AddTBInt(MapDataAttr.TableCol, 0, "傻瓜表单显示的列", true, true);

			//Tag
		map.AddTBString(MapDataAttr.Tag, null, "Tag", true, false, 0, 500, 20);

			// 可以为空这个字段。
		map.AddTBString(MapDataAttr.FK_FrmSort, null, "表单类别", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.FK_FormTree, null, "表单树类别", true, false, 0, 500, 20);


			// enumFrmType  @自由表单，@傻瓜表单，@嵌入式表单.  
		map.AddDDLSysEnum(MapDataAttr.FrmType, BP.Sys.FrmType.FreeFrm.getValue(), "表单类型", true, false, MapDataAttr.FrmType);

		map.AddTBInt(MapDataAttr.FrmShowType, 0, "表单展示方式", true, true);

			// 应用类型.  0独立表单.1节点表单
		map.AddTBInt(MapDataAttr.AppType, 0, "应用类型", true, false);
		map.AddTBString(MapDataAttr.DBSrc, "local", "数据源", true, false, 0, 100, 20);
		map.AddTBString(MapDataAttr.BodyAttr, null, "表单Body属性", true, false, 0, 100, 20);

			///#endregion 基础信息.


			///#region 设计者信息.
		map.AddTBString(MapDataAttr.Note, null, "备注", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.Designer, null, "设计者", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.DesignerUnit, null, "单位", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.DesignerContact, null, "联系方式", true, false, 0, 500, 20);

		map.AddTBInt(MapDataAttr.Idx, 100, "顺序号", true, true);
		map.AddTBString(MapDataAttr.GUID, null, "GUID", true, false, 0, 128, 20);
		map.AddTBString(MapDataAttr.Ver, null, "版本号", true, false, 0, 30, 20);

			//流程控件.
		map.AddTBString(MapDataAttr.FlowCtrls, null, "流程控件", true, true, 0, 200, 20);

			//增加参数字段.
		map.AddTBAtParas(4000);

			///#endregion

		this.set_enMap(map);
		return this.get_enMap();
	}

	/** 
	 上移
	 * @throws Exception 
	*/
	public final void DoUp() throws Exception
	{
		this.DoOrderUp(MapDataAttr.FK_FormTree, this.getFK_FormTree(), MapDataAttr.Idx);
	}
	/** 
	 下移
	 * @throws Exception 
	*/
	public final void DoOrderDown() throws Exception
	{
		this.DoOrderDown(MapDataAttr.FK_FormTree, this.getFK_FormTree(), MapDataAttr.Idx);
	}

	//检查表单
	public final void CheckPTableSaveModel(String filed) throws Exception
	{
		if (this.getPTableModel() == 2)
		{
			/*如果是存储格式*/
			if (DBAccess.IsExitsTableCol(this.getPTable(), filed) == false)
			{
				throw new RuntimeException("@表单的表存储模式不允许您创建不存在的字段(" + filed + ")，不允许修改表结构.");
			}
		}
	}

	/** 
	 获得PTableModel=2模式下的表单，没有被使用的字段集合.
	 
	 @param frmID
	 @return 
	 * @throws Exception 
	*/
	public static DataTable GetFieldsOfPTableMode2(String frmID) throws Exception
	{
		String pTable = "";

		MapDtl dtl = new MapDtl();
		dtl.setNo(frmID);
		if (dtl.RetrieveFromDBSources() == 1)
		{
			pTable = dtl.getPTable();
		}
		else
		{
			MapData md = new MapData();
			md.setNo(frmID);
			md.RetrieveFromDBSources();
			pTable = md.getPTable();
		}

		//获得原始数据.
		DataTable dt = BP.DA.DBAccess.GetTableSchema(pTable);

		//创建样本表结构.
		DataTable mydt = BP.DA.DBAccess.GetTableSchema(pTable);
		mydt.Rows.clear();

		//获得现有的列..
		MapAttrs attrs = new MapAttrs(frmID);

		String flowFiels = ",GUID,PRI,PrjNo,PrjName,PEmp,AtPara,FlowNote,WFSta,PNodeID,FK_FlowSort,FK_Flow,OID,FID,Title,WFState,CDT,FlowStarter,FlowStartRDT,FK_Dept,FK_NY,FlowDaySpan,FlowEmps,FlowEnder,FlowEnderRDT,FlowEndNode,MyNum,PWorkID,PFlowNo,BillNo,ProjNo,";

		//排除已经存在的列. 把所有的列都输出给前台，让前台根据类型分拣.
		for (DataRow dr : dt.Rows)
		{
			String key = dr.getValue("FName").toString();
			if (attrs.Contains(MapAttrAttr.KeyOfEn, key) == true)
			{
				continue;
			}

			if (flowFiels.contains("," + key + ",") == true)
			{
				continue;
			}

			DataRow mydr = mydt.NewRow();
			mydr.setValue("FName", dr.getValue("FName"));
			mydr.setValue("FType", dr.getValue("FType"));
			mydr.setValue("FLen", dr.getValue("FLen"));
			mydr.setValue("FDesc", dr.getValue("FDesc"));
			mydt.Rows.add(mydr);
		}
		return mydt;
	}



		///#endregion


		///#region 常用方法.
	private FormEventBase _HisFEB = null;
	public final FormEventBase getHisFEB() throws Exception
	{
		if (this.getFormEventEntity().equals(""))
		{
			return null;
		}

		if (_HisFEB == null)
		{
			_HisFEB = BP.Sys.Glo.GetFormEventBaseByEnName(this.getNo());
		}

		return _HisFEB;
	}
	/** 
	 执行事件.
	 
	 @param eventType
	 @param en
	 @return 
	 * @throws Exception 
	*/

	public final String DoEvent(String eventType, Entity en) throws Exception
	{
		return DoEvent(eventType, en, null);
	}

	public final String DoEvent(String eventType, Entity en, String atParas) throws Exception
	{
			///#region 首先执行通用的事件重载方法.
		if (FrmEventList.FrmLoadBefore.equals(eventType) == true)
		{
			BP.En.OverrideFile.FrmEvent_LoadBefore(this.getNo(), en);
		}

		//装载之后.
		if (FrmEventList.FrmLoadAfter.equals(eventType) == true)
		{
			BP.En.OverrideFile.FrmEvent_FrmLoadAfter(this.getNo(), en);
		}

		/**保存之前.
		*/
		if (FrmEventList.SaveBefore.equals(eventType) == true)
		{
			BP.En.OverrideFile.FrmEvent_SaveBefore(this.getNo(), en);
		}

		//保存之后.
		if (FrmEventList.SaveAfter.equals(eventType) == true)
		{
			BP.En.OverrideFile.FrmEvent_SaveAfter(this.getNo(), en);
		}

			///#endregion 首先执行通用的事件重载方法.

		String str = this.getFrmEvents().DoEventNode(eventType, en);

		String mystrs = null;
		if (this.getHisFEB() != null)
		{
			mystrs = this.getHisFEB().DoIt(eventType, en, atParas);
		}

		if (str == null)
		{
			return mystrs;
		}

		if (mystrs == null)
		{
			return str;
		}

		return str + "@" + mystrs;
	}
	/** 
	 升级逻辑.
	 * @throws Exception 
	*/
	public final void Upgrade() throws Exception
	{
		String sql = "";

			///#region 升级ccform控件.
		if (BP.DA.DBAccess.IsExitsObject("Sys_FrmLine") == true)
		{
			//重命名.
			BP.Sys.SFDBSrc dbsrc = new SFDBSrc("local");
			dbsrc.Rename("Table", "Sys_FrmLine", "Sys_FrmLineBak");

			/*检查是否升级.*/
			sql = "SELECT * FROM Sys_FrmLineBak ORDER BY FK_MapData ";
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			for (DataRow dr : dt.Rows)
			{
				BP.Sys.FrmEle ele = new FrmEle();
				ele.Copy(dr);
				ele.setEleType(BP.Sys.FrmEle.Line);
				if (ele.getIsExits() == true)
				{
					ele.setMyPK(BP.DA.DBAccess.GenerGUID());
				}
				ele.Insert();
			}
		}
		if (BP.DA.DBAccess.IsExitsObject("Sys_FrmLab") == true)
		{
			//重命名.
			BP.Sys.SFDBSrc dbsrc = new SFDBSrc("local");
			dbsrc.Rename("Table", "Sys_FrmLab", "Sys_FrmLabBak");

			/*检查是否升级.*/
			sql = "SELECT * FROM Sys_FrmLabBak ORDER BY FK_MapData ";
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			for (DataRow dr : dt.Rows)
			{
				BP.Sys.FrmEle ele = new FrmEle();
				ele.Copy(dr);
				ele.setEleType(BP.Sys.FrmEle.Label);

				ele.setEleName(dr.getValue(FrmLabAttr.Text).toString());

				if (ele.getIsExits() == true)
				{
					ele.setMyPK(BP.DA.DBAccess.GenerGUID());
				}
				ele.Insert();
			}
		}
		if (BP.DA.DBAccess.IsExitsObject("Sys_FrmBtn") == true)
		{
			//重命名.
			BP.Sys.SFDBSrc dbsrc = new SFDBSrc("local");
			dbsrc.Rename("Table", "Sys_FrmLab", "Sys_FrmLabBak");

			/*检查是否升级.*/
			sql = "SELECT * FROM Sys_FrmLabBak ORDER BY FK_MapData ";
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			for (DataRow dr : dt.Rows)
			{
				BP.Sys.FrmEle ele = new FrmEle();
				ele.Copy(dr);
				ele.setEleType(BP.Sys.FrmEle.Line);
				if (ele.getIsExits() == true)
				{
					ele.setMyPK(BP.DA.DBAccess.GenerGUID());
				}
				ele.Insert();
			}
		}

	}
	/** 
	 导入数据
	 
	 @param ds
	 @return 
	 * @throws Exception 
	*/
	public static MapData ImpMapData(DataSet ds) throws Exception
	{
		String errMsg = "";
		if (ds.Tables.contains("WF_Flow") == true)
		{
			errMsg += "@此模板文件为流程模板。";
		}

		if (ds.Tables.contains("Sys_MapAttr") == false)
		{
			errMsg += "@缺少表:Sys_MapAttr";
		}

		if (ds.Tables.contains("Sys_MapData") == false)
		{
			errMsg += "@缺少表:Sys_MapData";
		}

		if (!errMsg.equals(""))
		{
			throw new RuntimeException(errMsg);
		}

		DataTable dt = ds.GetTableByName("Sys_MapData");
		String fk_mapData = dt.Rows.get(0).getValue("No").toString();
		MapData md = new MapData();
		md.setNo(fk_mapData);
		if (md.getIsExits())
		{
			throw new RuntimeException("@已经存在(" + fk_mapData + ")的表单ID，所以您不能导入。");
		}

		//导入.
		return ImpMapData(fk_mapData, ds);
	}
	/** 
	 设置表单为只读属性
	 
	 @param fk_mapdata 表单ID
	 * @throws Exception 
	*/
	public static void SetFrmIsReadonly(String fk_mapdata) throws Exception
	{
		//把主表字段设置为只读.
		MapAttrs attrs = new MapAttrs(fk_mapdata);
		for (MapAttr attr : attrs.ToJavaList())
		{
			if (attr.getDefValReal().contains("@"))
			{
				attr.setUIIsEnable(false);
				attr.setDefValReal(""); //清空默认值.
				attr.SetValByKey("ExtDefVal", ""); //设置默认值.
				attr.Update();
				continue;
			}

			if (attr.getUIIsEnable() == true)
			{
				attr.setUIIsEnable(false);
				attr.Update();
				continue;
			}
		}

		//把从表字段设置为只读.
		MapDtls dtls = new MapDtls(fk_mapdata);
		for (MapDtl dtl : dtls.ToJavaList())
		{
			dtl.setIsInsert(false);
			dtl.setIsUpdate(false);
			dtl.setIsDelete(false);
			dtl.Update();

			attrs = new MapAttrs(dtl.getNo());
			for (MapAttr attr : attrs.ToJavaList())
			{
				if (attr.getDefValReal().contains("@"))
				{
					attr.setUIIsEnable(false);
					attr.setDefValReal(""); //清空默认值.
					attr.SetValByKey("ExtDefVal", ""); //设置默认值.
					attr.Update();
				}

				if (attr.getUIIsEnable() == true)
				{
					attr.setUIIsEnable(false);
					attr.Update();
					continue;
				}
			}
		}

		//把附件设置为只读.
		FrmAttachments aths = new FrmAttachments(fk_mapdata);
		for (FrmAttachment item : aths.ToJavaList())
		{
			item.setIsUpload(false);
			item.setHisDeleteWay(AthDeleteWay.DelSelf);

			//如果是从开始节点表单导入的,就默认为, 按照主键PK的方式显示.
			if (fk_mapdata.indexOf("ND") == 0)
			{
				item.setHisCtrlWay(AthCtrlWay.PK);
				item.setDataRefNoOfObj("AttachM1");
			}
			item.Update();
		}
	}


	/** 
	 导入表单
	 
	 @param fk_mapdata 表单ID
	 @param ds 表单数据
	 @return 
	 * @throws Exception 
	*/
	public static MapData ImpMapData(String fk_mapdata, DataSet ds) throws Exception
	{

		if(ds==null || ds.Tables.size()==0)
			throw new Exception("err@导入的表单中内容为空");
			///#region 检查导入的数据是否完整.
		String errMsg = "";
		
		List<DataTable> listD = ds.getTables();
		ArrayList<String> arr = new ArrayList<String>();
		for(DataTable dt:listD){
			arr.add(dt.TableName);
		}
		
		if (arr.contains("WF_Flow") == true)
		{
			errMsg += "@此模板文件为流程模板。";
		}

		if (arr.contains("Sys_MapAttr") == false)
		{
			errMsg += "@缺少表:Sys_MapAttr";
		}

		if (arr.contains("Sys_MapData") == false)
		{
			errMsg += "@缺少表:Sys_MapData";
		}

		int num = arr.indexOf("Sys_MapAttr");
		DataTable dtCheck = ds.Tables.get(num);
		boolean isHave = false;
		for (DataRow dr : dtCheck.Rows)
		{
			if (dr.getValue("KeyOfEn").toString().equals("OID"))
			{
				isHave = true;
				break;
			}
		}

		if (isHave == false)
		{
			errMsg += "@表单模版缺少列:OID";
		}

		if (!errMsg.equals(""))
		{
			throw new RuntimeException("err@以下错误不可导入，可能的原因是非表单模板文件:" + errMsg);
		}

			///#endregion

		// 定义在最后执行的sql.
		String endDoSQL = "";

		//检查是否存在OID字段.
		MapData mdOld = new MapData();
		mdOld.setNo(fk_mapdata);
		mdOld.RetrieveFromDBSources();
		mdOld.Delete();

		// 求出dataset的map.
		String oldMapID = "";
		DataTable dtMap = ds.GetTableByName("Sys_MapData");
		if (dtMap.Rows.size() == 1)
		{
			oldMapID = dtMap.Rows.get(0).getValue("No").toString();
		}
		else
		{
			// 求旧的表单ID.
			for (DataRow dr : dtMap.Rows)
			{
				oldMapID = dr.getValue("No").toString();
			}

			if (DataType.IsNullOrEmpty(oldMapID) == true)
			{
				oldMapID = dtMap.Rows.get(0).getValue("No").toString();
			}
		}
		String timeKey = DateUtils.format(new Date(),"MMddHHmmss");




			///#region 表单元素
		for (DataTable dt : ds.Tables)
		{
			int idx = 0;
			switch (dt.TableName)
			{
				case "Sys_MapDtl":

					for (DataRow dr : dt.Rows)
					{

						MapDtl dtl = new MapDtl();
						for (DataColumn dc : dt.Columns)
						{
							Object val = dr.getValue(dc.ColumnName) instanceof Object ? (Object)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							dtl.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));

						}
						dtl.Insert();
					}
					break;
				case "Sys_MapData":
					for (DataRow dr : dt.Rows)
					{
						MapData md = new MapData();
						for (DataColumn dc : dt.Columns)
						{
							Object val = dr.getValue(dc.ColumnName) instanceof Object ? (Object)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							md.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
						}

						//如果物理表为空，则使用编号为物理数据表
						if (DataType.IsNullOrEmpty(md.getPTable().trim()) == true)
						{
							md.setPTable(md.getNo());
						}

						//表单类别编号不为空，则用原表单类别编号
						if (DataType.IsNullOrEmpty(mdOld.getFK_FormTree()) == false)
						{
							md.setFK_FormTree(mdOld.getFK_FormTree());
						}

						//表单类别编号不为空，则用原表单类别编号
						if (DataType.IsNullOrEmpty(mdOld.getFK_FrmSort()) == false)
						{
							md.setFK_FrmSort(mdOld.getFK_FrmSort());
						}

						if (DataType.IsNullOrEmpty(mdOld.getPTable()) == false)
						{
							md.setPTable(mdOld.getPTable());
						}
						if (DataType.IsNullOrEmpty(mdOld.getName()) == false)
						{
							md.setName(mdOld.getName());
						}

						md.setHisFrmType(mdOld.getHisFrmType());
						//表单应用类型保持不变
						md.setAppType(mdOld.getAppType());

						md.DirectInsert();
					}
					break;
				case "Sys_FrmBtn":
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmBtn en = new FrmBtn();
						for (DataColumn dc : dt.Columns)
						{
							Object val = dr.getValue(dc.ColumnName) instanceof Object ? (Object)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}



							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
						}

						//en.setMyPK( "Btn_" + idx + "_" + fk_mapdata;
						en.setMyPK(DBAccess.GenerGUID());
						en.Insert();
					}
					break;
				case "Sys_FrmLine":
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmLine en = new FrmLine();
						for (DataColumn dc : dt.Columns)
						{
							Object val = dr.getValue(dc.ColumnName) instanceof Object ? (Object)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}



							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
						}
						//en.setMyPK( "LE_" + idx + "_" + fk_mapdata;
						en.setMyPK(DBAccess.GenerGUID());
						en.Insert();
					}
					break;
				case "Sys_FrmLab":
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmLab en = new FrmLab();
						for (DataColumn dc : dt.Columns)
						{
							Object val = dr.getValue(dc.ColumnName) instanceof Object ? (Object)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
						}
						//  en.FK_MapData = fk_mapdata; 删除此行解决从表lab的问题。
						//en.setMyPK( "LB_" + idx + "_" + fk_mapdata;
						en.setMyPK(DBAccess.GenerGUID());
						en.Insert();
					}
					break;
				case "Sys_FrmLink":
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmLink en = new FrmLink();
						for (DataColumn dc : dt.Columns)
						{
							Object val = dr.getValue(dc.ColumnName) instanceof Object ? (Object)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}



							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
						}
						//en.setMyPK( "LK_" + idx + "_" + fk_mapdata;
						en.setMyPK(DBAccess.GenerGUID());
						en.Insert();
					}
					break;
				case "Sys_FrmEle":
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmEle en = new FrmEle();
						for (DataColumn dc : dt.Columns)
						{
							Object val = dr.getValue(dc.ColumnName) instanceof Object ? (Object)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}



							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
						}


						en.Insert();
					}
					break;
				case "Sys_FrmImg":
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmImg en = new FrmImg();
						for (DataColumn dc : dt.Columns)
						{
							Object val = dr.getValue(dc.ColumnName) instanceof Object ? (Object)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
						}
						if (DataType.IsNullOrEmpty(en.getKeyOfEn()) == true)
						{
							en.setMyPK(DBAccess.GenerGUID());
						}

						en.Insert();
					}
					break;
				case "Sys_FrmImgAth":
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmImgAth en = new FrmImgAth();
						for (DataColumn dc : dt.Columns)
						{
							Object val = dr.getValue(dc.ColumnName) instanceof Object ? (Object)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
						}

						if (DataType.IsNullOrEmpty(en.getCtrlID()))
						{
							en.setCtrlID("ath" + idx);
						}

						en.Insert();
					}
					break;
				case "Sys_FrmRB":
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmRB en = new FrmRB();
						for (DataColumn dc : dt.Columns)
						{
							Object val = dr.getValue(dc.ColumnName) instanceof Object ? (Object)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
						}


						try
						{
							en.Save();
						}
						catch (java.lang.Exception e)
						{
						}
					}
					break;
				case "Sys_FrmAttachment":
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmAttachment en = new FrmAttachment();
						for (DataColumn dc : dt.Columns)
						{
							Object val = dr.getValue(dc.ColumnName) instanceof Object ? (Object)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
						}
						en.setMyPK(fk_mapdata + "_" + en.GetValByKey("NoOfObj"));


						try
						{
							en.Insert();
						}
						catch (java.lang.Exception e2)
						{
						}
					}
					break;
				case "Sys_MapFrame":
					for (DataRow dr : dt.Rows)
					{
						idx++;
						MapFrame en = new MapFrame();
						for (DataColumn dc : dt.Columns)
						{
							Object val = dr.getValue(dc.ColumnName) instanceof Object ? (Object)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}


							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
						}
						en.DirectInsert();
					}
					break;
				case "Sys_MapExt":
					for (DataRow dr : dt.Rows)
					{
						idx++;
						MapExt en = new MapExt();
						for (DataColumn dc : dt.Columns)
						{
							Object val = dr.getValue(dc.ColumnName) instanceof Object ? (Object)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}
							if (DataType.IsNullOrEmpty(val.toString()) == true)
							{
								continue;
							}
							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
						}

						//执行保存，并统一生成PK的规则.
						en.InitPK();
						en.Save();
					}
					break;
				case "Sys_MapAttr":
					for (DataRow dr : dt.Rows)
					{
						MapAttr en = new MapAttr();
						for (DataColumn dc : dt.Columns)
						{
							Object val = dr.getValue(dc.ColumnName) instanceof Object ? (Object)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}

							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
						}

						en.setMyPK(en.getFK_MapData() + "_" + en.getKeyOfEn());

						//直接插入.
						try
						{
							en.DirectInsert();
						}
						catch (java.lang.Exception e3)
						{
						}
					}
					break;
				case "Sys_GroupField":
					for (DataRow dr : dt.Rows)
					{
						idx++;
						GroupField en = new GroupField();
						for (DataColumn dc : dt.Columns)
						{
							Object val = dr.getValue(dc.ColumnName) instanceof Object ? (Object)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}
							try
							{
								en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
							}
							catch (java.lang.Exception e4)
							{
								throw new RuntimeException("val:" + val.toString() + "oldMapID:" + oldMapID + "fk_mapdata:" + fk_mapdata);
							}
						}
						int beforeID = (int) en.getOID();
						en.setOID(0);
						en.DirectInsert();
						endDoSQL += "@UPDATE Sys_MapAttr SET GroupID=" + en.getOID() + " WHERE FK_MapData='" + en.GetValStrByKey("FrmID").toString() + "' AND GroupID='" + beforeID + "'";

					}
					break;
				case "Sys_Enum":
					for (DataRow dr : dt.Rows)
					{
						SysEnum se = new SysEnum();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							se.SetValByKey(dc.ColumnName, val);
						}
						se.setMyPK(se.getEnumKey() + "_" + se.getLang() + "_" + se.getIntKey());
						if (se.getIsExits())
						{
							continue;
						}
						se.Insert();
					}
					break;
				case "Sys_EnumMain":
					for (DataRow dr : dt.Rows)
					{
						SysEnumMain sem = new SysEnumMain();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) instanceof String ? (String)dr.getValue(dc.ColumnName) : null;
							if (val == null)
							{
								continue;
							}
							sem.SetValByKey(dc.ColumnName, val);
						}
						if (sem.getIsExits())
						{
							continue;
						}
						sem.Insert();
					}
					break;
				case "WF_Node":
					if (dt.Rows.size() > 0)
					{
						endDoSQL += "@UPDATE WF_Node SET FWCSta=2"
							+ ",FWC_X=" + dt.Rows.get(0).getValue("FWC_X") + ",FWC_Y=" + dt.Rows.get(0).getValue("FWC_Y") + ",FWC_H=" + dt.Rows.get(0).getValue("FWC_H") + ",FWC_W=" + dt.Rows.get(0).getValue("FWC_W") + ",FWCType=" + dt.Rows.get(0).getValue("FWCType") + " WHERE NodeID=" + fk_mapdata.replace("ND", "");
					}
					break;
				default:
					break;
			}
		}

			///#endregion

		//执行最后结束的sql.
		DBAccess.RunSQLs(endDoSQL);

		MapData mdNew = new MapData(fk_mapdata);
		mdNew.RepairMap();

		if (mdNew.getNo().indexOf("ND") == 0)
		{
			mdNew.setFK_FrmSort("");
			mdNew.setFK_FormTree("");
		}

		mdNew.Update();
		return mdNew;
	}
	/**
	 修复map.
	 * @throws Exception
	*/
	public final void RepairMap() throws Exception
	{
		if(DataType.IsNullOrEmpty(this.getNo()))
			return;
		GroupFields gfs = new GroupFields(this.getNo());
		if (gfs.size() == 0)
		{
			GroupField gf = new GroupField();
			gf.setFrmID(this.getNo());
			gf.setLab(this.getName());
			gf.Insert();

			String sqls = "";
			sqls += "@UPDATE Sys_MapDtl SET GroupID=" + gf.getOID() + " WHERE FK_MapData='" + this.getNo() + "'";
			sqls += "@UPDATE Sys_MapAttr SET GroupID=" + gf.getOID() + " WHERE FK_MapData='" + this.getNo() + "'";
			//sqls += "@UPDATE Sys_MapFrame SET GroupID=" + gf.OID + " WHERE FK_MapData='" + this.getNo()+ "'";
			sqls += "@UPDATE Sys_FrmAttachment SET GroupID=" + gf.getOID() + " WHERE FK_MapData='" + this.getNo() + "'";
			DBAccess.RunSQLs(sqls);
		}
		else
		{
			if (SystemConfig.getAppCenterDBType() != DBType.Oracle)
			{
				GroupField gfFirst = gfs.get(0) instanceof GroupField ? (GroupField)gfs.get(0) : null;

				String sqls = "";
				//   sqls += "@UPDATE Sys_MapAttr SET GroupID=" + gfFirst.OID + "       WHERE  MyPK IN (SELECT X.MyPK FROM (SELECT MyPK FROM Sys_MapAttr       WHERE GroupID NOT IN (SELECT OID FROM Sys_GroupField WHERE FrmID=' " + this.getNo()+ " ') or GroupID is null) AS X) AND FK_MapData=' " + this.getNo()+ " ' ";
				sqls += "@UPDATE Sys_FrmAttachment SET GroupID=" + gfFirst.getOID() + " WHERE  MyPK IN (SELECT X.MyPK FROM (SELECT MyPK FROM Sys_FrmAttachment WHERE GroupID NOT IN (SELECT OID FROM Sys_GroupField WHERE FrmID='" + this.getNo() + "')) AS X) AND FK_MapData='" + this.getNo() + "' ";


///#warning 这些sql 对于Oracle 有问题，但是不影响使用.
				try
				{
					DBAccess.RunSQLs(sqls);
				}
				catch (java.lang.Exception e)
				{
				}
			}
		}

		BP.Sys.MapAttr attr = new BP.Sys.MapAttr();
		if (this.getEnPK().equals("OID"))
		{
			if (attr.IsExit(MapAttrAttr.KeyOfEn, "OID", MapAttrAttr.FK_MapData, this.getNo()) == false)
			{
				attr.setFK_MapData(this.getNo());
				attr.setKeyOfEn("OID");
				attr.setName("OID");
				attr.setMyDataType(BP.DA.DataType.AppInt);
				attr.setUIContralType(UIContralType.TB);
				attr.setLGType(FieldTypeS.Normal);
				attr.setUIVisible(false);
				attr.setUIIsEnable(false);
				attr.setDefVal("0");
				attr.setHisEditType(BP.En.EditType.Readonly);
				attr.Insert();
			}
		}
		if (this.getEnPK().equals("No") || this.getEnPK().equals("MyPK"))
		{
			if (attr.IsExit(MapAttrAttr.KeyOfEn, this.getEnPK(), MapAttrAttr.FK_MapData, this.getNo()) == false)
			{
				attr.setFK_MapData(this.getNo());
				attr.setKeyOfEn(this.getEnPK());
				attr.setName(this.getEnPK());
				attr.setMyDataType(BP.DA.DataType.AppInt);
				attr.setUIContralType(UIContralType.TB);
				attr.setLGType(FieldTypeS.Normal);
				attr.setUIVisible(false);
				attr.setUIIsEnable(false);
				attr.setDefVal("0");
				attr.setHisEditType(BP.En.EditType.Readonly);
				attr.Insert();
			}
		}

		if (attr.IsExit(MapAttrAttr.KeyOfEn, "RDT", MapAttrAttr.FK_MapData, this.getNo()) == false)
		{
			attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(this.getNo());
			attr.setHisEditType(BP.En.EditType.UnDel);
			attr.setKeyOfEn("RDT");
			attr.setName("更新时间");
			attr.setGroupID(0);
			attr.setMyDataType(BP.DA.DataType.AppDateTime);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setDefVal("@RDT");
			attr.setTag("1");
			attr.Insert();
		}

		//检查特殊UIBindkey丢失的问题.
		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.FK_MapData, this.getNo());
		for (MapAttr item : attrs.ToJavaList())
		{
			if (item.getLGType() == FieldTypeS.Enum || item.getLGType() == FieldTypeS.FK)
			{
				if (DataType.IsNullOrEmpty(item.getUIBindKey()) == true)
				{
					item.setLGType(FieldTypeS.Normal);
					item.setUIContralType(UIContralType.TB);
					item.Update();
				}
			}
		}

	}
	@Override
	protected boolean beforeInsert() throws Exception
	{
		if (this.getHisFrmType() == FrmType.Url || this.getHisFrmType() == FrmType.Entity)
		{

		}
		else
		{
			this.setPTable(PubClass.DealToFieldOrTableNames(this.getPTable()));
		}
		return super.beforeInsert();
	}
	/**
	 设置Para 参数.
	 * @throws Exception
	*/
	public final void ResetMaxMinXY() throws Exception
	{
		if (this.getHisFrmType() != FrmType.FreeFrm)
		{
			return;
		}


			///#region 计算最左边,与最右边的值。
		// 求最左边.
		float i1 = DBAccess.RunSQLReturnValFloat("SELECT MIN(X1) FROM Sys_FrmLine WHERE FK_MapData='" + this.getNo() + "'", 0);
		if (i1 == 0) //没有线，只有图片的情况下。
		{
			i1 = DBAccess.RunSQLReturnValFloat("SELECT MIN(X) FROM Sys_FrmImg WHERE FK_MapData='" + this.getNo() + "'", 0);
		}

		float i2 = DBAccess.RunSQLReturnValFloat("SELECT MIN(X)  FROM Sys_FrmLab  WHERE FK_MapData='" + this.getNo() + "'", 0);
		if (i1 > i2)
		{
			this.setMaxLeft(i2);
		}
		else
		{
			this.setMaxLeft(i1);
		}

		// 求最右边.
		i1 = DBAccess.RunSQLReturnValFloat("SELECT Max(X2) FROM Sys_FrmLine WHERE FK_MapData='" + this.getNo() + "'", 0);
		if (i1 == 0)
		{
			/*没有线的情况，按照图片来计算。*/
			i1 = DBAccess.RunSQLReturnValFloat("SELECT Max(X+W) FROM Sys_FrmImg WHERE FK_MapData='" + this.getNo() + "'", 0);
		}
		this.setMaxRight(i1);

		// 求最top.
		i1 = DBAccess.RunSQLReturnValFloat("SELECT MIN(Y1) FROM Sys_FrmLine WHERE FK_MapData='" + this.getNo() + "'", 0);
		i2 = DBAccess.RunSQLReturnValFloat("SELECT MIN(Y)  FROM Sys_FrmLab  WHERE FK_MapData='" + this.getNo() + "'", 0);

		if (i1 > i2)
		{
			this.setMaxTop(i2);
		}
		else
		{
			this.setMaxTop(i1);
		}

		// 求最end.
		i1 = DBAccess.RunSQLReturnValFloat("SELECT Max(Y1) FROM Sys_FrmLine WHERE FK_MapData='" + this.getNo() + "'", 0);
		/*小周鹏添加2014/10/23-----------------------START*/
		if (i1 == 0) //没有线，只有图片的情况下。
		{
			i1 = DBAccess.RunSQLReturnValFloat("SELECT Max(Y+H) FROM Sys_FrmImg WHERE FK_MapData='" + this.getNo() + "'", 0);
		}

		/*小周鹏添加2014/10/23-----------------------END*/
		i2 = DBAccess.RunSQLReturnValFloat("SELECT Max(Y)  FROM Sys_FrmLab  WHERE FK_MapData='" + this.getNo() + "'", 0);
		if (i2 == 0)
		{
			i2 = DBAccess.RunSQLReturnValFloat("SELECT Max(Y+H) FROM Sys_FrmImg WHERE FK_MapData='" + this.getNo() + "'", 0);
		}
		//求出最底部的 附件
		float endFrmAtt = DBAccess.RunSQLReturnValFloat("SELECT Max(Y+H)  FROM Sys_FrmAttachment  WHERE FK_MapData='" + this.getNo() + "'", 0);
		//求出最底部的明细表
		float endFrmDtl = DBAccess.RunSQLReturnValFloat("SELECT Max(Y+H)  FROM Sys_MapDtl  WHERE FK_MapData='" + this.getNo() + "'", 0);

		//求出最底部的扩展控件
		float endFrmEle = DBAccess.RunSQLReturnValFloat("SELECT Max(Y+H)  FROM Sys_FrmEle  WHERE FK_MapData='" + this.getNo() + "'", 0);
		//求出最底部的textbox
		float endFrmAttr = DBAccess.RunSQLReturnValFloat("SELECT Max(Y+UIHeight)  FROM  Sys_MapAttr  WHERE FK_MapData='" + this.getNo() + "' and UIVisible='1'", 0);

		if (i1 > i2)
		{
			this.setMaxEnd(i1);
		}
		else
		{
			this.setMaxEnd(i2);
		}

		this.setMaxEnd(this.getMaxEnd() > endFrmAtt ? this.getMaxEnd() : endFrmAtt);
		this.setMaxEnd(this.getMaxEnd() > endFrmDtl ? this.getMaxEnd() : endFrmDtl);
		this.setMaxEnd(this.getMaxEnd() > endFrmEle ? this.getMaxEnd() : endFrmEle);
		this.setMaxEnd(this.getMaxEnd() > endFrmAtt ? this.getMaxEnd() : endFrmAttr);


			///#endregion

		this.DirectUpdate();
	}
	/**
	 求位移.

	 @param md
	 @param scrWidth
	 @return
	 * @throws Exception
	*/
	public static float GenerSpanWeiYi(MapData md, float scrWidth) throws Exception
	{
		if (scrWidth == 0)
		{
			scrWidth = 900;
		}

		float left = md.getMaxLeft();
		if (left == 0)
		{
			md.ResetMaxMinXY();
			md.RetrieveFromDBSources();
			md.Retrieve();

			left = md.getMaxLeft();
		}
		//取不到左侧参考值，则不进行位移
		if (left == 0)
		{
			return left;
		}

		float right = md.getMaxRight();
		float withFrm = right - left;
		if (withFrm >= scrWidth)
		{
			/* 如果实际表单宽度大于屏幕宽度 */
			return -left;
		}

		//计算位移大小
		float space = (scrWidth - withFrm) / 2; //空白的地方.

		return -(left - space);
	}
	/**
	 求屏幕宽度

	 @param md
	 @param scrWidth
	 @return
	 * @throws Exception
	*/
	public static float GenerSpanWidth(MapData md, float scrWidth) throws Exception
	{
		if (scrWidth == 0)
		{
			scrWidth = 900;
		}
		float left = md.getMaxLeft();
		if (left == 0)
		{
			md.ResetMaxMinXY();
			left = md.getMaxLeft();
		}

		float right = md.getMaxRight();
		float withFrm = right - left;
		if (withFrm >= scrWidth)
		{
			return withFrm;
		}
		return scrWidth;
	}
	/**
	 求屏幕高度

	 @param md
	 @param scrHeight
	 @return
	 * @throws Exception
	*/
	public static float GenerSpanHeight(MapData md, float scrHeight) throws Exception
	{
		if (scrHeight == 0)
		{
			scrHeight = 1200;
		}

		float end = md.getMaxEnd();
		if (end > scrHeight)
		{
			return end + 10;
		}
		else
		{
			return scrHeight;
		}
	}
	@Override
	protected boolean beforeUpdateInsertAction() throws Exception
	{
		if (this.getHisFrmType() == FrmType.Url || this.getHisFrmType() == FrmType.Entity)
		{
			return super.beforeUpdateInsertAction();
		}

		this.setPTable(PubClass.DealToFieldOrTableNames(this.getPTable()));
		getMapAttrs().Retrieve(MapAttrAttr.FK_MapData, getPTable());

		//更新版本号.
		this.setVer(DataType.getCurrentDataTimess());


			///#region  检查是否有ca认证设置.
		boolean isHaveCA = false;
		for (MapAttr item : this.getMapAttrs().ToJavaList())
		{
			if (item.getSignType() == SignType.CA)
			{
				isHaveCA = true;
				break;
			}
		}
		this.setIsHaveCA(isHaveCA);
		if (getIsHaveCA() == true)
		{
			//就增加隐藏字段.
			//MapAttr attr = new BP.Sys.MapAttr();
			// attr.setMyPK( this.No + "_SealData";
			// attr.setFK_MapData(this.getNo());
			// attr.setHisEditType(BP.En.EditType.UnDel;
			//attr.setKeyOfEn ("SealData";
			// attr.setName("SealData";
			// attr.setMyDataType (BP.DA.DataType.AppString;
			// attr.setUIContralType (UIContralType.TB;
			//  attr.setLGType(FieldTypeS.Normal;
			// attr.setUIVisible(false;
			// attr.setUIIsEnable(false;
			// attr.setMaxLen(4000;
			// attr.setMinLen(0;
			// attr.Save();
		}

			///#endregion  检查是否有ca认证设置.

		return super.beforeUpdateInsertAction();
	}
	/**
	 更新版本
	 * @throws Exception
	*/
	public final void UpdateVer() throws Exception
	{
		String sql = "UPDATE Sys_MapData SET VER='" + BP.DA.DataType.getCurrentDataTimess() + "' WHERE No='" + this.getNo() + "'";
		BP.DA.DBAccess.RunSQL(sql);
	}
	@Override
	protected boolean beforeDelete() throws Exception
	{
		String sql = "";
		sql = "SELECT * FROM Sys_MapDtl WHERE FK_MapData ='" + this.getNo() + "'";
		DataTable Sys_MapDtl = DBAccess.RunSQLReturnTable(sql);

		String whereFK_MapData = "FK_MapData= '" + this.getNo() + "' ";
		String whereEnsName = "FrmID= '" + this.getNo() + "' ";
		String whereNo = "No='" + this.getNo() + "' ";

		for (DataRow dr : Sys_MapDtl.Rows)
		{
			// ids += ",'" + dr["No"] + "'";
			whereFK_MapData += " OR FK_MapData='" + dr.getValue("No") + "' ";
			whereEnsName += " OR FrmID='" + dr.getValue("No") + "' ";
			whereNo += " OR No='" + dr.getValue("No") + "' ";
		}

		//	string where = " FK_MapData IN (" + ids + ")";


			///#region 删除相关的数据。
		sql = "DELETE FROM Sys_MapDtl WHERE FK_MapData='" + this.getNo() + "'";
		sql += "@DELETE FROM Sys_FrmLine WHERE " + whereFK_MapData;
		sql += "@DELETE FROM Sys_FrmEle WHERE " + whereFK_MapData;
		sql += "@DELETE FROM Sys_FrmEvent WHERE " + whereFK_MapData;
		sql += "@DELETE FROM Sys_FrmBtn WHERE " + whereFK_MapData;
		sql += "@DELETE FROM Sys_FrmLab WHERE " + whereFK_MapData;
		sql += "@DELETE FROM Sys_FrmLink WHERE " + whereFK_MapData;
		sql += "@DELETE FROM Sys_FrmImg WHERE " + whereFK_MapData;
		sql += "@DELETE FROM Sys_FrmImgAth WHERE " + whereFK_MapData;
		sql += "@DELETE FROM Sys_FrmRB WHERE " + whereFK_MapData;
		sql += "@DELETE FROM Sys_FrmAttachment WHERE " + whereFK_MapData;
		sql += "@DELETE FROM Sys_MapFrame WHERE " + whereFK_MapData;

		if (this.getNo().contains("BP.") == false)
		{
			sql += "@DELETE FROM Sys_MapExt WHERE " + whereFK_MapData;
		}

		sql += "@DELETE FROM Sys_MapAttr WHERE " + whereFK_MapData;
		sql += "@DELETE FROM Sys_GroupField WHERE " + whereEnsName;
		sql += "@DELETE FROM Sys_MapData WHERE " + whereNo;
		// sql += "@DELETE FROM Sys_M2M WHERE " + whereFK_MapData;
		sql += "@DELETE FROM WF_FrmNode WHERE FK_Frm='" + this.getNo() + "'";
		sql += "@DELETE FROM Sys_FrmSln WHERE " + whereFK_MapData;
		DBAccess.RunSQLs(sql);

			///#endregion 删除相关的数据。


			///#region 删除物理表。
		//如果存在物理表.
		if (DBAccess.IsExitsObject(this.getPTable()) && this.getPTable().indexOf("ND") == 0)
		{
			//如果其他表单引用了该表，就不能删除它.
			sql = "SELECT COUNT(No) AS NUM  FROM Sys_MapData WHERE PTable='" + this.getPTable() + "' OR ( PTable='' AND No='" + this.getPTable() + "')";
			if (DBAccess.RunSQLReturnValInt(sql, 0) > 1)
			{
				/*说明有多个表单在引用.*/
			}
			else
			{
				// edit by zhoupeng 误删已经有数据的表.
				if (DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM " + this.getPTable() + " WHERE 1=1 ") == 0)
				{
					DBAccess.RunSQL("DROP TABLE " + this.getPTable());
				}
			}
		}

		MapDtls dtls = new MapDtls(this.getNo());
		for (MapDtl dtl : dtls.ToJavaList())
		{
			dtl.Delete();
		}


			///#endregion



			///#region 删除注册到的外检表.
		SFTables sfs = new SFTables();
		sfs.Retrieve(SFTableAttr.SrcTable, this.getPTable());
		for (SFTable item : sfs.ToJavaList())
		{
			if (item.IsCanDelete() == null)
			{
				item.Delete();
			}
		}

			///#endregion 删除注册到的外检表.

		return super.beforeDelete();
	}

		///#endregion 常用方法.


		///#region 与Excel相关的操作 .
	/** 
	 获得Excel文件流
	 
	 @param pkValue
	 @param bytes
	 @return 
	 * @throws Exception 
	*/
	public final boolean ExcelGenerFile(int pkValue, RefObject<byte[]> bytes) throws Exception
	{
		byte[] by = BP.DA.DBAccess.GetByteFromDB(this.getPTable(), this.getEnPK(), (new Integer(pkValue)).toString(), "DBFile");
		if (by != null)
		{
			bytes.argvalue = by;
			return true;
		}
		else //说明当前excel文件没有生成.
		{
			String tempExcel = SystemConfig.getPathOfDataUser() + "FrmOfficeTemplate/" + this.getNo() + ".xlsx";
			File file = new File(tempExcel);
			if (file.exists() == true)
			{
				bytes.argvalue = BP.DA.DataType.ConvertFileToByte(tempExcel);
				return false;
			}
			else //模板文件也不存在时
			{
				throw new RuntimeException("@没有找到模版文件." + tempExcel + " 请确认表单配置.");
			}
		}
	}
	/** 
	 保存excel文件
	 
	 @param pkValue
	 @param bty
	 @param saveTo
	 * @throws Exception 
	 * @throws NumberFormatException 
	*/
	public final void ExcelSaveFile(String pkValue, byte[] bty, String saveTo) throws NumberFormatException, Exception
	{
		BP.DA.DBAccess.SaveFileToDB(bty.toString(), this.getPTable(), this.getEnPK(), (new Integer(pkValue)).toString(), "DBFile");
	}

		///#endregion 与Excel相关的操作 .


		///#region 与Word相关的操作 .
	/** 
	 获得Excel文件流
	 
	 @param pkValue
	 @return 
	 * @throws Exception 
	*/
	public final void WordGenerFile(String pkValue, RefObject<byte[]> bytes, String saveTo) throws Exception
	{
		byte[] by = BP.DA.DBAccess.GetByteFromDB(this.getPTable(), this.getEnPK(), pkValue, saveTo);
		if (by != null)
		{
			bytes.argvalue = by;
			return;
		}
		else //说明当前excel文件没有生成.
		{
			String tempExcel = SystemConfig.getPathOfDataUser() + "FrmOfficeTemplate\\" + this.getNo() + ".docx";

			if ((new File(tempExcel)).isFile() == false)
			{
				tempExcel = SystemConfig.getPathOfDataUser() + "FrmOfficeTemplate\\NDxxxRpt.docx";
			}

			bytes.argvalue = BP.DA.DataType.ConvertFileToByte(tempExcel);
			return;
		}
	}
	/** 
	 保存excel文件
	 
	 @param pkValue
	 @param bty
	 * @throws Exception 
	*/
	public final void WordSaveFile(String pkValue, byte[] bty, String saveTo) throws Exception
	{
		BP.DA.DBAccess.SaveBytesToDB(bty,null, this.getPTable(), this.getEnPK(), pkValue, saveTo);
	}

	public final void setDesignerTool(String value) throws Exception
	{
		this.SetValByKey(MapDataAttr.DesignerTool, value);
	}

}