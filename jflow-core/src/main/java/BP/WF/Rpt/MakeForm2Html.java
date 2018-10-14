package BP.WF.Rpt;

import BP.*;
import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.DataType;


import BP.En.*;
import BP.Sys.*;
import BP.WF.ActionType;
import BP.WF.GenerWorkFlow;
import BP.WF.Node;
import BP.WF.NodeFormType;
import BP.WF.WFState;
import BP.Web.WebUser;

import java.lang.*;
import java.awt.*;
import java.awt.color.*;

import java.io.OutputStream ;
import java.io.File;

import java.nio.channels.FileChannel;
import java.nio.file.Files;
import org.apache.commons.io.FileUtils;

import java.text.SimpleDateFormat;
import java.util.*; 

import java.util.UUID;
import javax.tools.Diagnostic;

/**

using ICSharpCode.SharpZipLib.Zip;

 * 
 */


/***
 * 
 * @author Administrator
 * 打印PDF翻译
 */
public class MakeForm2Html 
{
	 /// <summary>
    /// 生成
    /// </summary>
    /// <param name="mapData"></param>
    /// <param name="frmID"></param>
    /// <param name="workid"></param>
    /// <param name="en"></param>
    /// <param name="path"></param>
    /// <param name="flowNo"></param>
    /// <returns></returns>
    public static StringBuilder GenerHtmlOfFree(MapData mapData, String frmID, int workid, Entity en, String path
    		, String flowNo )throws Exception {
    
        StringBuilder sb = new StringBuilder();//  

        //字段集合.
        MapAttrs attrs = new MapAttrs(frmID);

        String appPath = "";
        float wtX = MapData.GenerSpanWeiYi(mapData, 1200);
        //float wtX = 0;
        float x = 0;

        /*region 输出Ele */
        FrmEles eles = mapData.getFrmEles();
        if (eles.size()>= 1)
        {
            FrmEleDBs dbs = new FrmEleDBs(frmID,String.valueOf(workid));
           
            for( BP.Sys.FrmEle ele :eles.ToJavaList())
            {
            	  float y = ele.getY();
                  x = ele.getX() + wtX;
                  sb.append("<DIV id=" + ele.getMyPK() + " style='position:absolute;left:" + x + "px;top:" + y + "px;text-align:left;vertical-align:top' >");
	          
                  /***
	            
                  switch(ele.getEleType()){
		             case FrmEle.HandSiganture:
		            	  FrmEleDB db =(FrmEleDB)dbs.GetEntityByKey(FrmEleDBAttr.EleID, ele.getEleID());
		                  String dbFile = appPath + "DataUser/BPPaint/Def.png";
		                  if (db != null)
		                      dbFile = db.getTag1();                  
		                  break;             
		              default:
		            	  break;
	             	}
	             	 * 
	             */
                  sb.append("\t\n</DIV>");
            }
        }
        /*region 输出竖线与标签 & 超连接 Img.*/
        FrmLabs labs = mapData.getFrmLabs();
        for( FrmLab lab:labs.ToJavaList())
        {
        	 java.awt.Color col =  Color.decode(lab.getFontColor()) ;//ColorTranslator.FromHtml(lab.getFontColor());
              x = lab.getX() + wtX;
              sb.append("\t\n<DIV id=u2 style='position:absolute;left:" + x + "px;top:" + lab.getY() + "px;text-align:left;' >");
              sb.append("\t\n<span style='color:" + lab.getFontColorHtml()+ ";font-family: " + lab.getFontName() + ";font-size: " + lab.getFontSize() + "px;' >" + lab.getTextHtml() + "</span>");
              sb.append("\t\n</DIV>");
        }
      

        FrmLines lines = mapData.getFrmLines();
        for(FrmLine line :lines.ToJavaList() )
        {
        	 if (line.getX1() == line.getX2())
             {
            	  /* 一道竖线 */
                 float h = line.getY1() - line.getY2();
                 h = Math.abs(h);
                 if (line.getY1() < line.getY2())
                 {
                     x = line.getX1() + wtX;
                     sb.append("\t\n<img id='" + line.getMyPK() + "'  style=\"padding:0px;position:absolute; left:" + x + "px; top:" + line.getY1()+ "px; width:" + line.getBorderWidth() + "px; height:" + h + "px;background-color:" + line.getBorderColorHtml() + "\" />");
                 }
                 else
                 {
                     x = line.getX2() + wtX;
                     sb.append("\t\n<img id='" + line.getMyPK() + "'  style=\"padding:0px;position:absolute; left:" + x + "px; top:" + line.getY2() + "px; width:" + line.getBorderWidth() + "px; height:" + h + "px;background-color:" + line.getBorderColorHtml()  + "\" />");
                 }
             }
        	 else
             {
                 /* 一道横线 */
                 float w = line.getX2() - line.getX1();

                 if (line.getX1() < line.getX2())
                 {
                     x = line.getX1() + wtX;
                     sb.append("\t\n<img id='" + line.getMyPK() + "'  style=\"padding:0px;position:absolute; left:" + x + "px; top:" + line.getY1()+ "px; width:" + w + "px; height:" + line.getBorderWidth() + "px;background-color:" + line.getBorderColorHtml() + "\" />");
                 }
                 else
                 {
                     x = line.getX2() + wtX;
                     sb.append("\t\n<img id='" + line.getMyPK() + "'  style=\"padding:0px;position:absolute; left:" + x + "px; top:" + line.getY2() + "px; width:" + w + "px; height:" + line.getBorderWidth() + "px;background-color:" + line.getBorderColorHtml() + "\" />");
                 }
             }
        }
      

        FrmLinks links = mapData.getFrmLinks();
        for(FrmLink link :links.ToJavaList())
        {
        	  String url = link.getURL();
              if (url.contains("@"))
              {
                  for (MapAttr attr : attrs.ToJavaList())
                  {
                      if (url.contains("@") == false)
                          break;
                      url = url.replace("@" + attr.getKeyOfEn(), en.GetValStrByKey(attr.getKeyOfEn()));
                  }
              }
              x = link.getX() + wtX;
              sb.append("\t\n<DIV id=u2 style='position:absolute;left:" + x + "px;top:" + link.getY() + "px;text-align:left;' >");
              sb.append("\t\n<span style='color:" + link.getFontColorHtml() + ";font-family: " + link.getFontName() + ";font-size: " + link.getFontSize() + "px;' > <a href=\"" + url + "\" target='" + link.getTarget() + "'> " + link.getText() + "</a></span>");
              sb.append("\t\n</DIV>");
        }
       

        FrmImgs imgs = mapData.getFrmImgs();
        for(BP.Sys.FrmImg img :imgs.ToJavaList())
        {
            float y = img.getY();
            String imgSrc = "";

           //  ////#region 图片类型
            if (img.getHisImgAppType() == ImgAppType.Img)
            {
                //数据来源为本地.
                if (img.getImgSrcType() == 0)
                {
                    if (img.getImgPath().contains(";") == false)
                        imgSrc = img.getImgPath();
                }

                //数据来源为指定路径.
                if (img.getImgSrcType() == 1)
                {
                    //图片路径不为默认值
                    imgSrc = img.getImgURL();
                    if (imgSrc.contains("@"))
                    {
                        /*如果有变量*/
                        imgSrc = BP.WF.Glo.DealExp(imgSrc, en, "");
                    }
                }

                x = img.getX() + wtX;
                // 由于火狐 不支持onerror 所以 判断图片是否存在
                imgSrc = "icon.png";

                sb.append("\t\n<div id=" + img.getMyPK() + " style='position:absolute;left:" + x + "px;top:" + y + "px;text-align:left;vertical-align:top' >");
                if (DataType.IsNullOrEmpty(img.getLinkURL()) == false)
                    sb.append("\t\n<a href='" + img.getLinkURL() + "' target=" + img.getLinkTarget() + " ><img src='" + imgSrc + "'  onerror=\"this.src='/DataUser/ICON/CCFlow/LogBig.png'\"  style='padding: 0px;margin: 0px;border-width: 0px;width:" + img.getW() + "px;height:" + img.getH() + "px;' /></a>");
                else
                    sb.append("\t\n<img src='" + imgSrc + "'  onerror=\"this.src='/DataUser/ICON/CCFlow/LogBig.png'\"  style='padding: 0px;margin: 0px;border-width: 0px;width:" + img.getW() + "px;height:" + img.getH() + "px;' />");
                sb.append("\t\n</div>");
                
                continue;
            }
         

           //  ////#region 二维码
            if (img.getHisImgAppType() == ImgAppType.QRCode)
            {
                x = img.getX() + wtX;
                String pk = String.valueOf(en.getPKVal());
                String myPK = frmID + "_" + img.getMyPK() + "_" + pk;
                FrmEleDB frmEleDB = new FrmEleDB();
                frmEleDB.setMyPK(myPK);
                if (frmEleDB.RetrieveFromDBSources() == 0)
                {
                    //生成二维码
                }

                sb.append("\t\n<DIV id=" + img.getMyPK() + " style='position:absolute;left:" + x + "px;top:" + y + "px;text-align:left;vertical-align:top' >");
                sb.append("\t\n<img src='" + frmEleDB.getTag2() + "' style='padding: 0px;margin: 0px;border-width: 0px;width:" + img.getW() + "px;height:" + img.getH() + "px;' />");
                sb.append("\t\n</DIV>");

                continue;
            }
          //  ////#endregion

           //  ////#region 电子签章
            //图片类型
            if (img.getHisImgAppType() == ImgAppType.Seal)
            {
                //获取登录人岗位
                String stationNo = "";
                //签章对应部门
                String fk_dept = WebUser.getFK_Dept();
                //部门来源类别
                String sealType = "0";
                //签章对应岗位
                String fk_station = img.getTag0();
                //表单字段
                String sealField = "";
                String sql = "";

                //重新加载 可能有缓存
                img.RetrieveFromDBSources();
                //0.不可以修改，从数据表中取，1可以修改，使用组合获取并保存数据
                if ((img.getIsEdit() == 1))
                {
                   //  ////#region 加载签章
                    //如果设置了部门与岗位的集合进行拆分
                    if (!DataType.IsNullOrEmpty(img.getTag0()) && img.getTag0().contains("^") && img.getTag0().split("^").length == 4)
                    {
                        fk_dept = img.getTag0().split("^")[0];
                        fk_station = img.getTag0().split("^")[1];
                        sealType = img.getTag0().split("^")[2];
                        sealField = img.getTag0().split("^")[3];
                        //如果部门没有设定，就获取部门来源
                        if (fk_dept == "all")
                        {
                            //默认当前登陆人
                            fk_dept = WebUser.getFK_Dept();
                            //发起人
                            if (sealType == "1")
                            {
                                sql = "SELECT FK_Dept FROM WF_GenerWorkFlow WHERE WorkID=" + en.GetValStrByKey("OID");
                                fk_dept = BP.DA.DBAccess.RunSQLReturnString(sql);
                            }
                            //表单字段
                            if (sealType == "2" && !DataType.IsNullOrEmpty(sealField))
                            {
                                //判断字段是否存在
                                for (MapAttr attr: attrs.ToJavaList())
                                {
                                    if (attr.getKeyOfEn() == sealField)
                                    {
                                        fk_dept = en.GetValStrByKey(sealField);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    //判断本部门下是否有此人
                    //sql = "SELECT fk_station from port_deptEmpStation where fk_dept='" + fk_dept + "' and fk_emp='" + WebUser.No + "'";
                    sql = String.format(" select FK_Station from Port_DeptStation where FK_Dept ='{0}' and FK_Station in (select FK_Station from " + BP.WF.Glo.getEmpStation()+ " where FK_Emp='{1}')", fk_dept, WebUser.getNo());
                    DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
                    for (DataRow dr : dt.Rows)
                    {
                        if (fk_station.contains(dr.getValue(0).toString() + ","))
                        {
                            stationNo = dr.getValue(0).toString();
                            break;
                        }
                    }
                    ////#endregion 加载签章

                    imgSrc = CCFlowAppPath + "DataUser/Seal/" + fk_dept + "_" + stationNo + ".png";
                    //设置主键
                    String myPK = DataType.IsNullOrEmpty(img.getEnPK()) ? "seal" : img.getEnPK();
                    myPK = myPK + "_" + en.GetValStrByKey("OID") + "_" + img.getMyPK();

                    FrmEleDB imgDb = new FrmEleDB();
                    QueryObject queryInfo = new QueryObject(imgDb);
                    queryInfo.AddWhere(FrmEleAttr.MyPK, myPK);
                    queryInfo.DoQuery();
                    //判断是否存在
                    if (imgDb == null || DataType.IsNullOrEmpty(imgDb.getFK_MapData()))
                    {
                        imgDb.setFK_MapData( DataType.IsNullOrEmpty(img.getEnPK()) ? "seal" : img.getEnPK());
                        imgDb.setEleID( en.GetValStrByKey("OID"));
                        imgDb.setRefPKVal(img.getMyPK());
                        imgDb.setTag1(imgSrc);
                        imgDb.Insert();
                    }

                    //添加控件
                    x = img.getX() + wtX;
                    sb.append("\t\n<DIV id=" + img.getMyPK() + " style='position:absolute;left:" + x + "px;top:" + y + "px;text-align:left;vertical-align:top' >");
                    sb.append("\t\n<img src='" + imgSrc + "' onerror=\"javascript:this.src='" + appPath + "DataUser/Seal/Def.png'\" style=\"padding: 0px;margin: 0px;border-width: 0px;width:" + img.getW() + "px;height:" + img.getH() + "px;\" />");
                    sb.append("\t\n</DIV>");
                }
                else
                {
                    FrmEleDB realDB = null;
                    FrmEleDB imgDb = new FrmEleDB();
                    QueryObject objQuery = new QueryObject(imgDb);
                    objQuery.AddWhere(FrmEleAttr.FK_MapData, img.getEnPK());
                    objQuery.addAnd();
                    objQuery.AddWhere(FrmEleAttr.EleID, en.GetValStrByKey("OID"));

                    if (objQuery.DoQuery() == 0)
                    {
                        FrmEleDBs imgdbs = new FrmEleDBs();
                        QueryObject objQuerys = new QueryObject(imgdbs);
                        objQuerys.AddWhere(FrmEleAttr.EleID, en.GetValStrByKey("OID"));
                        if (objQuerys.DoQuery() > 0)
                        {
                            for (FrmEleDB single : imgdbs.ToJavaList())
                            {
                                if (single.getFK_MapData().substring(6, single.getFK_MapData().length() - 6).equals(img.getEnPK().substring(6, img.getEnPK().length() - 6)))
                                {
                                    single.setFK_MapData(img.getEnPK());
                                    single.setMyPK(img.getEnPK() + "_" + en.GetValStrByKey("OID") + "_" + img.getEnPK());
                                    single.setRefPKVal( img.getEnPK());
                                    //  single.DirectInsert();
                                    //  realDB = single; cut by zhoupeng .没有看明白.
                                    break;
                                }
                            }
                        }
                        else
                        {
                            realDB = imgDb;
                        }
                    }
                    else
                    {
                        realDB = imgDb;
                    }

                    if (realDB != null)
                    {
                        imgSrc = realDB.getTag1();
                        //如果没有查到记录，控件不显示。说明没有走盖章的一步
                        x =  img.getX() + wtX;
                        sb.append("\t\n<DIV id=" + img.getMyPK() + " style='position:absolute;left:" + x + "px;top:" + y + "px;text-align:left;vertical-align:top' >");
                        sb.append("\t\n<img src='" + imgSrc + "' onerror='javascript:this.src='" + appPath + "DataUser/ICON/" + BP.Sys.SystemConfig.getCustomerNo() + "/LogBiger.png';' style='padding: 0px;margin: 0px;border-width: 0px;width:" + img.getW() + "px;height:" + img.getH() + "px;' />");
                        sb.append("\t\n</DIV>");
                    }
                }
            }
         //   ////#endregion
        }


        FrmBtns btns = mapData.getFrmBtns();
        for (FrmBtn btn : btns.ToJavaList())
        {
            x = btn.getX() + wtX;
            sb.append("\t\n<DIV id=u2 style='position:absolute;left:" + x + "px;top:" + btn.getY() + "px;text-align:left;' >");
            sb.append("\t\n<span >");

            String doDoc = BP.WF.Glo.DealExp(btn.getEventContext(), en, null);
            doDoc = doDoc.replace("~", "'");
            switch (btn.getHisBtnEventType())
            {
                case Disable:
                    sb.append("<input type=button class=Btn value='" + btn.getText().replace("&nbsp;", " ") + "' disabled='disabled'/>");
                    break;
                case RunExe:
                case RunJS:
                    sb.append("<input type=button class=Btn value=\"" +btn.getText().replace("&nbsp;", " ") + "\" enable=true onclick=\"" + doDoc + "\" />");
                    break;
                default:
                    sb.append("<input type=button value='" + btn.getText() + "' />");
                    break;
            }
            sb.append("\t\n</span>");
            sb.append("\t\n</DIV>");
        }
        ////#endregion 输出竖线与标签
         ////#region 输出数据控件.
        int fSize = 0;
        for (MapAttr attr : attrs.ToJavaList())
        {
            //处理隐藏字段，如果是不可见并且是启用的就隐藏.
            if (attr.getUIVisible() == false && attr.getUIIsEnable())
            {
                sb.append("<input type=text value='" + en.GetValStrByKey(attr.getKeyOfEn()) + "' style='display:none;' />");
                continue;
            }

            if (attr.getUIVisible() == false)
                continue;

            x = attr.getX() + wtX;
            if (attr.getLGType() == FieldTypeS.Enum || attr.getLGType() == FieldTypeS.FK)
                sb.append("<DIV id='F" + attr.getKeyOfEn() + "' style='position:absolute; left:" + x + "px; top:" + attr.getY() + "px;  height:16px;text-align: left;word-break: keep-all;' >");
            else
                sb.append("<DIV id='F" + attr.getKeyOfEn() + "' style='position:absolute; left:" + x + "px; top:" + attr.getY() + "px; width:auto" + attr.getUIWidth() + "px; height:16px;text-align: left;word-break: keep-all;' >");

            sb.append("<span>");

             ////#region add contrals.
            if (attr.getMaxLen() >= 3999 && attr.getTBModel() == TBModel.RichText.getValue())
            {
                sb.append(en.GetValStrByKey(attr.getKeyOfEn()));

                sb.append("</span>");
                sb.append("</DIV>");
                continue;
            }

             ////#region 通过逻辑类型，输出相关的控件.
            String text = "";
            switch (attr.getLGType())
            {
                case Normal:  // 输出普通类型字段.
                   text = en.GetValStrByKey(attr.getKeyOfEn());
                    break;
                case Enum:
                case FK:
                   text = en.GetValRefTextByKey(attr.getKeyOfEn());
                    break;
                default:
                    break;
            }
            ////#endregion 通过逻辑类型，输出相关的控件.

            ////#endregion add contrals.


            if (attr.getIsBigDoc())
            {
                //这几种字体生成 pdf都乱码
                text = text.replace("仿宋,", "宋体,");
                text = text.replace("仿宋;", "宋体;");
                text = text.replace("仿宋\"", "宋体\"");
                text = text.replace("黑体,", "宋体,");
                text = text.replace("黑体;", "宋体;");
                text = text.replace("黑体\"", "宋体\"");
                text = text.replace("楷体,", "宋体,");
                text = text.replace("楷体;", "宋体;");
                text = text.replace("楷体\"", "宋体\"");
                text = text.replace("隶书,", "宋体,");
                text = text.replace("隶书;", "宋体;");
                text = text.replace("隶书\"", "宋体\"");
            }

            if (attr.getMyDataType() == DataType.AppBoolean)
            {
                if (text == "0")
                    text = "[&#10005]";
                else
                    text = "[&#10004]";
            }

             sb.append(text);

            sb.append("</span>");
            sb.append("</DIV>");
        }

       //  ////#region  输出 rb.
        BP.Sys.FrmRBs myrbs = mapData.getFrmRBs();
        MapAttr attrRB = new MapAttr();
        for (BP.Sys.FrmRB rb : myrbs.ToJavaList())
        {
            x = rb.getX() + wtX;
            sb.append("<DIV id='F" + rb.getMyPK() + "' style='position:absolute; left:" + x + "px; top:" + rb.getY() + "px; height:16px;text-align: left;word-break: keep-all;' >");
            sb.append("<span style='word-break: keep-all;font-size:12px;'>");

            if (rb.getIntKey() == en.GetValIntByKey(rb.getKeyOfEn()))
                sb.append("<b>" + rb.getLab() + "</b>");
            else
                sb.append(rb.getLab());

            sb.append("</span>");
            sb.append("</DIV>");
        }
        ////#endregion  输出 rb.

        ////#endregion 输出数据控件.

         ////#region 输出明细.
        int dtlsCount = 0;
        MapDtls dtls = new MapDtls(frmID);
        for (MapDtl dtl :dtls.ToJavaList())
        {
            if (dtl.getIsView() == false)
                continue;

            dtlsCount++;
            x = dtl.getX() + wtX;
            float y = dtl.getY();

            sb.append("<DIV id='Fd" + dtl.getNo() + "' style='position:absolute; left:" + x + "px; top:" + y + "px; width:" + dtl.getW() + "px; height:" + dtl.getH() + "px;text-align: left;' >");
            sb.append("<span>");

            //String paras = this.RequestParas;
            //String strs = "";
            //for (String str in Glo.Request.QueryString.Keys)
            //{
            //    if (str == "EnsName" || str == "RefPKVal" || str == "IsReadonly")
            //        continue;
            //    strs += "&" + str + "=" + this.Request.QueryString[str];
            //}

            String src = "";
            src = appPath + "WF/CCForm/dtl.Htm?EnsName=" + dtl.getNo() + "&RefPKVal=" + en.getPKVal() + "&IsReadonly=1";
            /*
            if (dtl.getHisEditModel() == TableModel)
            {
                src = appPath + "WF/CCForm/dtl.getH()tm?EnsName=" + dtl.getNo() + "&RefPKVal=" + en.getPKVal() + "&IsReadonly=1";
            }
            else
            {
                src = appPath + "WF/CCForm/DtlCard.htm?EnsName=" + dtl.getNo() + "&RefPKVal=" + en.getPKVal() + "&IsReadonly=1";
            }
            */

            sb.append("<iframe ID='F" + dtl.getNo() + "' onload= 'F" + dtl.getNo() + "load();'  src='" + src + "' frameborder=0  style='position:absolute;width:" + dtl.getW() + "px; height:" + dtl.getH() + "px;text-align: left;'  leftMargin='0'  topMargin='0' scrolling=auto /></iframe>");

            sb.append("</span>");
            sb.append("</DIV>");
        }
        ////#endregion 输出明细.

         ////#region 审核组件
        if (flowNo != null)
        {
            FrmWorkCheck fwc = new FrmWorkCheck(frmID);
            if (fwc.getHisFrmWorkCheckSta() != FrmWorkCheckSta.Disable.getValue())
            {
                x = fwc.getFWC_X ()+ wtX;
                sb.append("<DIV id='DIVWC" + fwc.getNo() + "' style='position:absolute; left:" + x + "px; top:" + fwc.getFWC_Y() + "px; width:" + fwc.getFWC_W() + "px; height:" + fwc.getFWC_H() + "px;text-align: left;' >");
                sb.append("<span>");

                sb.append("<table   style='border: 1px outset #C0C0C0;padding: inherit; margin: 0;border-collapse:collapse;width:100%;' >");

                 ////#region 生成审核信息.
                if (flowNo != null)
                {
                    String sql = "SELECT EmpFrom, EmpFromT,RDT,Msg,NDFrom FROM ND" +Integer.parseInt(flowNo) + "Track WHERE WorkID=" + workid + " AND ActionType=" + ActionType.WorkCheck.getValue() + " ORDER BY RDT ";
                    DataTable dt = DBAccess.RunSQLReturnTable(sql);

                    //获得当前待办的人员,把当前审批的人员排除在外,不然就有默认同意的意见可以打印出来.
                    sql = "SELECT FK_Emp, FK_Node FROM WF_GenerWorkerList WHERE IsPass!=1 AND WorkID="+workid;
                    DataTable dtOfTodo = DBAccess.RunSQLReturnTable(sql);

                    for (DataRow dr: dt.Rows)
                    {

                         ////#region 排除正在审批的人员.
                        String nodeID = dr.getValue("NDFrom").toString();
                        String empFrom = dr.getValue("EmpFrom").toString();
                        if (dtOfTodo.Rows.size() != 0)
                        {
                            Boolean isHave = false;
                            for (DataRow mydr : dtOfTodo.Rows)
                            {
                                if (mydr.getValue("FK_Node").toString() != nodeID)
                                    continue;

                                if (mydr.getValue("FK_Emp").toString() != empFrom)
                                    continue;
                                isHave = true;
                            }

                            if (isHave == true)
                                continue;
                        }
                        ////#endregion 排除正在审批的人员.

                        sb.append("<tr>");
                        sb.append("<td valign=middle style='border-style: solid;padding: 4px;text-align: left;color: #333333;font-size: 12px;border-width: 1px;border-color: #C2D5E3;' >" + dr.getValue("NDFromT").toString() + "</td>");

                        sb.append("<br><br>");

                        String msg =dr.getValue("Msg") .toString();

                        msg += "<br>";
                        msg += "<br>";
                        msg += "审核人:" +dr.getValue("EmpFromT") .toString() + " &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日期:" + dr.getValue("RDT") .toString();

                        sb.append("<td colspan=3 valign=middle style='border-style: solid;padding: 4px;text-align: left;color: #333333;font-size: 12px;border-width: 1px;border-color: #C2D5E3;' >" + msg + "</td>");
                        sb.append("</tr>");
                    }
                }
                sb.append("</table>");
                ////#endregion 生成审核信息.

                sb.append("</span>");
                sb.append("</DIV>");
            }
        }
        ////#endregion 审核组件

         ////#region 父子流程组件
        if (flowNo != null)
        {
            FrmSubFlow subFlow = new FrmSubFlow(frmID);
            if (subFlow.getHisFrmSubFlowSta() != FrmSubFlowSta.Disable.getValue())
            {
                x = subFlow.getSF_X() + wtX;
                sb.append("<DIV id='DIVWC" + subFlow.getNo() + "' style='position:absolute; left:" + x + "px; top:" + subFlow.getSF_Y() + "px; width:" + subFlow.getSF_W() + "px; height:" + subFlow.getSF_H() + "px;text-align: left;' >");
                sb.append("<span>");

                String src = appPath + "WF/WorkOpt/subFlow.getaspx?s=2";
                String fwcOnload = "";
                //String paras = this.RequestParas;
                //if (paras.Contains("FID=") == false && en.EnMap.Attrs.Contains("FID") == true)
                //    paras += "&FID=" + en.GetValStrByKey("FID");
                //if (paras.Contains("OID=") == false)
                //    paras += "&OID=" + en.GetValStrByKey("OID");
                if (subFlow.getHisFrmSubFlowSta() == FrmSubFlowSta.Readonly.getValue())
                {
                    src += "&DoType=View";
                }

                src += "&r=q";
                sb.append("<iframe ID='FSF" + subFlow.getNo() + "' " + fwcOnload + "  src='" + src + "' frameborder=0 style='padding:0px;border:0px;'  leftMargin='0'  topMargin='0' width='" + subFlow.getSF_W() + "' height='" + subFlow.getSF_H() + "'   scrolling=auto/></iframe>");

                sb.append("</span>");
                sb.append("</DIV>");
            }
        }
        ////#endregion 父子流程组件

         ////#region 输出附件
        FrmAttachments aths = new FrmAttachments(frmID);
        FrmAttachmentDBs athDBs = new FrmAttachmentDBs(frmID, en.getPKVal().toString());

        for(FrmAttachment ath : aths.ToJavaList())
        {

            if (ath.getUploadType() == AttachmentUploadType.Single)
            {
                /* 单个文件 */
                FrmAttachmentDB athDB = (FrmAttachmentDB)athDBs.GetEntityByKey(FrmAttachmentDBAttr.FK_FrmAttachment, ath.getMyPK());
                x = ath.getX() + wtX;
                float y = ath.getY();
                sb.append("<DIV id='Fa" + ath.getMyPK() + "' style='position:absolute; left:" + x + "px; top:" + y + "px; text-align: left;float:left' >");
                //  sb.append("<span>");
                sb.append("<DIV>");

                sb.append("附件没有转化:" + athDB.getFileName());

                //String node = this.Request.QueryString["FK_Node"];
                //if (DataType.IsNullOrEmpty(node) && en.EnMap.Attrs.Contains("FK_Node"))
                //{
                //    node = en.GetValStrByKey("FK_Node");
                //    if (node == "0" || node == "")
                //        node = ((Work)en).NodeID.ToString();
                //}
                //if (athDB != null)
                //{
                //    if (ath.IsWoEnableWF)
                //        lab.Text = "<a  href=\"javascript:OpenOfiice('" + athDB.FK_FrmAttachment + "','" + this.HisEn.GetValStrByKey("OID") + "','" + athDB.MyPK + "','" + this.FK_MapData + "','" + ath.NoOfObj + "','" + node + "','" + athDB.FileExts + "')\"><img src='" + BP.WF.Glo.CCFlowAppPath + "WF/Img/FileType/" + athDB.FileExts + ".gif' border=0/>" + athDB.FileName + "</a>";
                //    else
                //        lab.Text = "<img src='" + BP.WF.Glo.CCFlowAppPath + "WF/Img/FileType/" + athDB.FileExts + ".gif' border=0/>" + athDB.FileName;
                //}
                sb.append("</DIV>");
                sb.append("</DIV>");
            }

            if (ath.getUploadType() == AttachmentUploadType.Multi)
            {
                x = ath.getX() + wtX;
                sb.append("<DIV id='Fd" + ath.getMyPK() + "' style='position:absolute; left:" + x + "px; top:" + ath.getY()+ "px; width:" + ath.getW() + "px; height:" + ath.getH() + "px;text-align: left;' >");
                sb.append("<span>");
                sb.append("<ul>");

                //判断是否有这个目录.
                File pathfile = new File(path + "\\pdf\\");
                if (!pathfile.exists()) {
                	pathfile.mkdirs();
                }

                for (FrmAttachmentDB item :athDBs.ToJavaList())
                {
                    if (ath.getAthSaveWay() == AthSaveWay.FTPServer)
                    {
                        try
                        {
                            String toFile = path + "\\pdf\\" + item.getFileName() ;
                            if (new File(toFile).exists() == false)
                            {
                                //把文件copy到,
                                String file = item.GenerTempFile(ath.getAthSaveWay());
                                Files.copy(new File(file).toPath(), new File(toFile).toPath());
                            }

                            sb.append("<li><a href='" + item.getFileName()  + "'>" + item.getFileName()  + "</a></li>");
                        }
                        catch (Exception ex)
                        {
                            sb.append("<li>" + item.getFileName()  + "(<font color=red>文件未从ftp下载成功{" + ex.getMessage() + "}</font>)</li>");
                        }
                    }
//IIS 取消了。。。
                   
                    if (ath.getAthSaveWay() == AthSaveWay.WebServer)
                    {
                        try
                        {
                            String toFile = path + "\\pdf\\" + item.getFileName() ;
                            File fileto = new File(toFile);
                            if (fileto.exists()==false)
                            {
                                //把文件copy到,
                            	Files.copy(new File(item.getFileFullName()).toPath()
                            		,new File(path + "\\pdf\\" + item.getFileName()).toPath());
                               // System.IO.File.Copy(item.getFileFullName(), path + "\\pdf\\" + item.getFileName() , true);
                            }
                            sb.append("<li><a href='" + item.getFileName() + "'>" + item.getFileName()  + "</a></li>");
                        }
                        catch (Exception ex)
                        {
                            sb.append("<li>" + item.getFileName()  + "(<font color=red>文件未从ftp下载成功{" + ex.getMessage() + "}</font>)</li>");
                        }
                    }

                }
                sb.append("</ul>");

                sb.append("</span>");
                sb.append("</DIV>");
            }
        }
        ////#endregion 输出附件.
        return sb;
}
        
    private static StringBuilder GenerHtmlOfFool(MapData mapData, String frmID, Integer workid, Entity en
    		, String path, String flowNo) throws Exception
    {
        StringBuilder sb =new StringBuilder();

        //字段集合.
        MapAttrs attrs = new MapAttrs(frmID);

        String appPath = "";
        float wtX =0;
        try
        {
        	wtX = MapData.GenerSpanWeiYi(mapData, 1200);
        }catch(Exception ex)
        {}
        //float wtX = 0;
        float x = 0;

        //生成表头.
        String frmName = mapData.getName();
        if (SystemConfig.getCustomerNo() == "TianYe")
            frmName = "";

        sb.append(" <table style='width:950px;height:auto;' >");

        //#region 生成头部信息.
        sb.append("<tr>");

        sb.append("<td colspan=4 >");

        sb.append("<table border=0 style='width:950px;'>");

        sb.append("<tr  style='border:0px;' >");

        sb.append("<td>");
        sb.append("<img src='icon.png' style='height:100px;border:0px;' />");
        sb.append("</td>");

        sb.append("<td  colspan=4>");
        sb.append("<br><h2><b>" + frmName + "</b></h2>");
        sb.append("</td>");

        sb.append("<td>");
        sb.append(" <img src='QR.png' style='height:100px;'  />");
        sb.append("</td>");

        sb.append("</tr>");
        sb.append("</table>");

        sb.append("</td>");
        //sb.append("</tr>");
        //#endregion 生成头部信息.

        GroupFields gfs =null;
        try
        {
        	new GroupFields(frmID);
        }
        catch(Exception ex){
        	
        }
        for (GroupField gf : gfs.ToJavaList())
        {
            //输出标题.
            sb.append(" <tr>");
            sb.append("  <th colspan=4><b>" + gf.getLab() + "</b></th>");
            sb.append(" </tr>");

            //#region 输出字段.
            if (gf.getCtrlID() == "" && gf.getCtrlType() == "")
            {
                Boolean isDropTR = true;
                String html = "";
                for (MapAttr attr : attrs.ToJavaList())
                {
                    //处理隐藏字段，如果是不可见并且是启用的就隐藏.
                    if (attr.getUIVisible() == false)
                        continue;
                    if (attr.getGroupID() != attr.getGroupID())
                        continue;
                    //处理分组数据，非当前分组的数据不输出
                    if (attr.getGroupID() != gf.getOID())
                        continue;

                    String text = "";
                    switch (attr.getLGType())
                    {
                        case Normal:  // 输出普通类型字段.
                            text = en.GetValStrByKey(attr.getKeyOfEn());
                            break;
                        case Enum:
                        case FK:
                            text = en.GetValRefTextByKey(attr.getKeyOfEn());
                            break;
                        default:
                            break;
                    }

                    if (attr.getIsBigDoc())
                    {
                        //这几种字体生成 pdf都乱码
                        text = text.replace("仿宋,", "宋体,");
                        text = text.replace("仿宋;", "宋体;");
                        text = text.replace("仿宋\"", "宋体\"");
                        text = text.replace("黑体,", "宋体,");
                        text = text.replace("黑体;", "宋体;");
                        text = text.replace("黑体\"", "宋体\"");
                        text = text.replace("楷体,", "宋体,");
                        text = text.replace("楷体;", "宋体;");
                        text = text.replace("楷体\"", "宋体\"");
                        text = text.replace("隶书,", "宋体,");
                        text = text.replace("隶书;", "宋体;");
                        text = text.replace("隶书\"", "宋体\"");
                    }

                    if (attr.getMyDataType() == DataType.AppBoolean)
                    {
                        if (text == "0")
                            text = "[&#10005]";
                        else
                            text = "[&#10004]";
                    }

                    // text = text.Replace("font-family: 楷体", "font-family: 宋体");
                    // text = text.Replace("font-family: 隶书", "font-family: 宋体");
                    //text = System.Web.HttpUtility.UrlDecode(text, System.Text.Encoding.UTF8);
                    // text = System.Web.HttpUtility.UrlDecode(text, System.Text.Encoding.GetEncoding("gb2312"));
                    //  System.Text.UTF8Encoding utf = new System.Text.UTF8Encoding();
                    // utf.GetChars(

                    //线性展示并且colspan=3
                    if (attr.getColSpan() == 3 || (attr.getColSpan()==4 && attr.getUIHeightInt() < 30))
                    {
                        isDropTR = true;
                        html += " <tr>";
                        html += " <td  class='FDesc'  >" + attr.getName() + "</td>";
                        html += " <td ColSpan=3>";
                        html += text;
                        html += " </td>";
                        html += " </tr>";
                        continue;
                    }

                    //线性展示并且colspan=4
                    if (attr.getColSpan() == 4)
                    {
                        isDropTR = true;
                        html += " <tr>";
                        html += " <td ColSpan=4 style='width:100%' >" + attr.getName() + "</br>";
                        html += text;
                        html += " </td>";
                        html += " </tr>";
                        continue;
                    }

                    if (isDropTR == true)
                    {
                        html += " <tr>";
                        html += " <td class='FDesc' >" + attr.getName() + "</td>";
                        html += " <td class='FContext'  >";
                        html += text;
                        html += " </td>";
                        isDropTR = !isDropTR;
                        continue;
                    }

                    if (isDropTR == false)
                    {
                        html += " <td  class='FDesc'>" + attr.getName() + "</td>";
                        html += " <td class='FContext'  >";
                        html += text;
                        html += " </td>";
                        html += " </tr>";
                        isDropTR = !isDropTR;
                        continue;
                    }
                }
                sb.append(html); //增加到里面.
                continue;
            }
            //#endregion 输出字段.
            
            //#region 如果是从表.
            if (gf.getCtrlType() == "Dtl")
            {
                /* 如果是从表 */
                MapAttrs attrsOfDtls =null;
                try{
                	attrsOfDtls = 	new MapAttrs(gf.getCtrlID());
                }
catch(Exception ex){}

                //#region 输出标题.
                sb.append("<tr><td valign=top colspan=4 >");

                sb.append("<table style='wdith:100%' >");
                sb.append("<tr>");
                for (MapAttr item :attrsOfDtls.ToJavaList())
                {
                    if (item.getKeyOfEn() == "OID")
                        continue;
                    if (item.getUIVisible() == false)
                        continue;

                    sb.append("<th>" + item.getName() + "</th>");
                }
                sb.append("</tr>");
                //#endregion 输出标题.


                //#region 输出数据.
                GEDtls dtls = new GEDtls(gf.getCtrlID());
                dtls.Retrieve(GEDtlAttr.RefPK, workid);
                for (GEDtl dtl :dtls.ToJavaList())
                {
                    sb.append("<tr>");

                    for (MapAttr item :attrsOfDtls.ToJavaList())
                    {
                        if (item.getKeyOfEn() == "OID" || item.getUIVisible() == false)
                            continue;

                        if (item.getUIContralType() == UIContralType.DDL)
                        {
                            sb.append("<td>" + dtl.GetValRefTextByKey(item.getKeyOfEn()) + "</td>");
                            continue;
                        }

                        if (item.getIsNum())
                        {
                            sb.append("<td style='text-align:right' >" + dtl.GetValStrByKey(item.getKeyOfEn()) + "</td>");
                            continue;
                        }

                        sb.append("<td>" + dtl.GetValStrByKey(item.getKeyOfEn()) + "</td>");
                    }
                    sb.append("</tr>");
                }
                //#endregion 输出数据.


                sb.append("</table>");

                sb.append(" </td>");
                sb.append(" </tr>");
            }
            //#endregion 如果是从表.
            
            //#region 如果是附件.
            if (gf.getCtrlType() == "Ath")
            {
                FrmAttachments aths = new FrmAttachments(frmID);
           

                for (FrmAttachment ath : aths.ToJavaList())
                {
                    if (ath.getMyPK() != gf.getCtrlID())
                        continue;

                    BP.Sys.FrmAttachmentDBs athDBs = null;
                    try
                    {
                    	athDBs = BP.WF.Glo.GenerFrmAttachmentDBs(ath,String.valueOf(workid), ath.getMyPK());
                    }
                    catch(Exception ex){
                    	
                    }

                    if (ath.getUploadType() == AttachmentUploadType.Single)
                    {
                        /* 单个文件 */
                        sb.append("<tr><td colspan=4>单附件没有转化:" + ath.getMyPK() + "</td></td>");
                        continue;
                    }

                    if (ath.getUploadType() == AttachmentUploadType.Multi)
                    {
                        sb.append("<tr><td valign=top colspan=4 >");
                        sb.append("<ul>");

                        //判断是否有这个目录.
                        File pathfile = new  File(path + "\\pdf\\");
                        if (pathfile.exists() == false)
                            pathfile.mkdirs();

                        for (FrmAttachmentDB item : athDBs.ToJavaList())
                        {
                            String fileTo = path + "\\pdf\\" + item.getFileName();

                            //#region 从ftp服务器上下载.
                            if (ath.getAthSaveWay() == AthSaveWay.FTPServer)
                            {
                                try
                                {
                                	File pathfileTo = new  File(fileTo);
                                    if (pathfileTo.exists() == true)
                                    	pathfileTo.delete();//rn "err@删除已经存在的文件错误,请检查iis的权限:" + ex.getMessage();

                                    //把文件copy到,                                  
                                        String file = item.GenerTempFile(ath.getAthSaveWay());
                                        Files.copy(new File(file).toPath(),pathfileTo.toPath());

                                    sb.append("<li><a href='" + item.getFileName() + "'>" + item.getFileName() + "</a></li>");
                                }
                             catch(Exception ex )
                                {
                                    sb.append("<li>" + item.getFileName() + "(<font color=red>文件未从ftp下载成功{" + ex.getMessage() + "}</font>)</li>");
                                }
                            }
                            //#endregion 从ftp服务器上下载.


                            //#region 从iis服务器上下载.
                            if (ath.getAthSaveWay() == AthSaveWay.WebServer)
                            {
                                try
                                {
                                    //把文件copy到,
                                	File pathfileTo = new File(fileTo);
                                    if (pathfileTo.exists()== false)
                                    	Files.copy(new File(item.getFileFullName()).toPath(),pathfileTo.toPath());

                                    sb.append("<li><a href='" + item.getFileName() + "'>" + item.getFileName() + "</a></li>");
                                }
                                catch (Exception ex)
                                {
                                    sb.append("<li>" + item.getFileName() + "(<font color=red>文件未从web下载成功{" + ex.getMessage() + "}</font>)</li>");
                                }
                            }
                           
                            //#endregion 从iis服务器上下载.

                        }
                        sb.append("</ul>");
                        sb.append("</td></tr>");
                    }
                }
            }
            //#endregion 如果是附件.

            //#region 审核组件
            if (gf.getCtrlType() == "FWC" && flowNo != null)
            {
                //sb.append("  <tr><td colspan=4 valign=top style='width:100%;valign:middle;height:auto;'  >");
            	FrmWorkCheck fwc =null;
            	try
               {
            fwc = new FrmWorkCheck(frmID);
               }
            		   catch(Exception ex){
            			   
            		   }
                String sql = "";
                DataTable dtTrack = null;
                Boolean bl = false;
                try
                {
                	bl = DBAccess.IsExitsTableCol("Port_Emp", "SignType");
                }
                catch(Exception ex){
                	
                }
                if (bl)
                {
                    String tTable = "ND" + Integer.parseInt(flowNo) + "Track";
                    sql = "SELECT a.No, a.SignType FROM Port_Emp a, " + tTable + " b WHERE a.No=b.EmpFrom AND B.WorkID=" + workid;

                    dtTrack = DBAccess.RunSQLReturnTable(sql);
                    dtTrack.TableName = "SignType";
/***
 * dtTrack.Columns[0].ColumnName = "No";
                        dtTrack.Columns[1].ColumnName = "SignType";
 */
                    /***
                     * dtTrack.Columns.Add("No");
                    dtTrack.Columns.Add("SignType");
                     */
                    dtTrack.Columns.Add("No");
                    dtTrack.Columns.Add("SignType");
                }

                String html = ""; // "<table style='width:100%;valign:middle;height:auto;' >";

                //#region 生成审核信息.
                sql = "SELECT NDFromT,Msg,RDT,EmpFromT,EmpFrom,NDFrom FROM ND" + Integer.parseInt(flowNo) + "Track WHERE WorkID=" + workid + " AND ActionType=" + ActionType.WorkCheck.getValue() + " ORDER BY RDT ";
                DataTable dt = DBAccess.RunSQLReturnTable(sql);

                //获得当前待办的人员,把当前审批的人员排除在外,不然就有默认同意的意见可以打印出来.
                sql = "SELECT FK_Emp, FK_Node FROM WF_GenerWorkerList WHERE IsPass!=1 AND WorkID=" + workid;
                DataTable dtOfTodo = DBAccess.RunSQLReturnTable(sql);

                for (DataRow dr : dt.Rows)
                {
                    //#region 排除正在审批的人员.
                    String nodeID =  dr.getValue("NDFrom").toString();
                    String empFrom = dr.getValue("EmpFrom").toString();
                    if (dtOfTodo.Rows.size() != 0)
                    {
                        Boolean isHave = false;
                        for (DataRow mydr :dtOfTodo.Rows)
                        {
                            if (mydr.getValue("FK_Node").toString() != nodeID)
                                continue;

                            if (mydr.getValue("FK_Emp").toString() != empFrom)
                                continue;
                            isHave = true;
                        }

                        if (isHave == true)
                            continue;
                    }
                    //#endregion 排除正在审批的人员.


                    html += "<tr>";
                    html += " <td valign=middle >" + dr.getValue("NDFromT").toString() + "</td>";

                    String msg =dr.getValue("Msg").toString();

                    msg += "<br>";
                    msg += "<br>";

                    String empStrs = "";
                    if (dtTrack == null)
                    {
                        empStrs = dr.getValue("EmpFromT").toString();
                    }
                    else
                    {
                        String singType = "0";
                        for (DataRow drTrack :dtTrack.Rows)
                        {
                            if (drTrack.getValue("No").toString() == dr.getValue("EmpFrom").toString())
                            {
                                singType = drTrack.getValue("SignType").toString();
                                break;
                            }
                        }

                        if (singType == "0" || singType == "2")
                        {
                            empStrs = dr.getValue("EmpFromT").toString();
                        }


                        if (singType == "1")
                        {
                            empStrs = "<img src='../../../../../DataUser/Siganture/" + dr.getValue("EmpFrom").toString() + ".jpg' title='" + dr.getValue("EmpFromT").toString() + "' style='height:60px;' border=0 onerror=\"src='../../../../../DataUser/Siganture/UnName.JPG'\" /> " + dr.getValue("EmpFromT").toString();
                        }

                    }

                    msg += "审核人:" + empStrs + " &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日期:" + dr.getValue("RDT").toString();

                    html += " <td colspan=3 valign=middle >" + msg + "</td>";
                    html += " </tr>";
                }
                //#endregion 生成审核信息.

                sb.append(" " + html);
            }
            //#endregion 审核组件
        }

        sb.append("</table>");
        return sb;
    }
    /***
     * zip文件路径.
     */
    public static String ZipFilePath = "";

    public static String CCFlowAppPath = "/";
    public static String MakeHtmlDocument(String frmID, Integer workid, String flowNo , String fileNameFormat , Boolean urlIsHostUrl)
    {
        try
        {
            GenerWorkFlow gwf = null;
            if(flowNo!=null)
            gwf = new GenerWorkFlow(workid);
            
          //获得该文件的访问权限
           // System.Security.AccessControl.FileSecurity fileSecurity = fileInfo.GetAccessControl();
            //添加ereryone用户组的访问权限规则 完全控制权限
           // fileSecurity.AddAccessRule(new FileSystemAccessRule("Everyone", FileSystemRights.FullControl, AccessControlType.Allow));
            //添加Users用户组的访问权限规则 完全控制权限
          //  fileSecurity.AddAccessRule(new FileSystemAccessRule("Users", FileSystemRights.FullControl, AccessControlType.Allow));
            //设置访问权限
          //  fileInfo.SetAccessControl(fileSecurity);
            

            //#region 准备目录文件.
            String path = SystemConfig.getPathOfDataUser() + "InstancePacketOfData\\" + frmID + "\\";
            try
            {
                path = SystemConfig.getPathOfDataUser() + "InstancePacketOfData\\" + frmID + "\\";
                File pathFile =new File(path);
                if (pathFile.exists()== false)
                	pathFile.mkdirs();
                
                path = SystemConfig.getPathOfDataUser() + "InstancePacketOfData\\" + frmID + "\\" + workid;
                if (new File(path).exists() == false)
                	new File(path).mkdirs();

                //把模版文件copy过去.
                String templateFilePath = SystemConfig.getPathOfDataUser() + "InstancePacketOfData\\Template\\";
                File baseFile = new File(templateFilePath);
                File[]  finfos = baseFile.listFiles();
                for (File fl:finfos)
                {
                    if (fl.getName().contains("ShuiYin"))
                        continue;

                    if (fl.getName().contains("htm"))
                        continue;
/***
 * System.IO.File.Copy(fl.FullName, path + "\\" + fl.Name, true);
 */
                    //判断之前是否存在该文件 就删除掉
                    if(new File(path + "\\" + fl.getName()).exists())
                    	new File(path + "\\" + fl.getName()).delete();
                    
                    Files.copy( fl.getAbsoluteFile().toPath()
                    		, new File(path + "\\" + fl.getName()).toPath());
                }
                /***
                //把ccs文件copy过去.
                Files.copy(new File(SystemConfig.getPathOfDataUser()+ "InstancePacketOfData\\Template\\ccbpm.css").toPath()
                		,new File(path + "\\ccbpm.css").toPath());
                */
            }
            catch (Exception ex)
            {
                return "err@读写文件出现权限问题，请联系管理员解决。" + ex.getMessage();
            }
            //#endregion 准备目录文件 需要获取网站的地址 http://localhost:8080/jflow-web/DataUser/InstancePacketOfData/ND20601/121/index.htm
          //  String url = request.getRequestURL() 
            String billUrl =SystemConfig.GetValByKey("HostURL","") + "/DataUser/InstancePacketOfData/" + frmID + "/" + workid + "/index.htm";
            		//SystemConfig.getPathOfDataUser()  + "InstancePacketOfData\\" + frmID + "\\" + workid + "\\index.htm";/***

            /**
            //#region 生成二维码.
           
           String pathQR = path + "\\QR.png"; // key.Replace("OID.Img@AppPath", SystemConfig.PathOfWebApp);
            String billUrl = SystemConfig.HostURLOfBS + "/DataUser/InstancePacketOfData/" + frmID + "/" + workid + "/index.htm";

            String qrUrl = SystemConfig.HostURL + "/WF/WorkOpt/PrintDocQRGuide.htm?FrmID=" + frmID + "&WorkID=" + workid + "&FlowNo="+flowNo;
            if (flowNo != null)
            {
                gwf = new GenerWorkFlow(workid);
                //qrUrl = SystemConfig.HostURLOfBS + "WF/WorkOpt/PrintDocQRGuide.htm?FrmID=" + frmID + "&WorkID=" + workid + "&FK_Flow=" + flowNo + "&FK_Node=" + gwf.FK_Node + "&Starter=" + gwf.Starter + "&BillNo=" + gwf.BillNo + "&FK_Dept=" + gwf.FK_Dept;
                qrUrl = SystemConfig.HostURL + "/WF/WorkOpt/PrintDocQRGuide.htm?AP=" + frmID + "$" + workid + "_" + flowNo + "_" + gwf.FK_Node + "_" + gwf.Starter + "_" + gwf.FK_Dept;
                // qrUrl = SystemConfig.HostURLOfBS + "WF/WorkOpt/PrintDocQRGuide.htm?WorkID="+workid;
            }

            ThoughtWorks.QRCode.Codec.QRCodeEncoder qrc = new ThoughtWorks.QRCode.Codec.QRCodeEncoder();
            qrc.QRCodeEncodeMode = ThoughtWorks.QRCode.Codec.QRCodeEncoder.ENCODE_MODE.BYTE;
            qrc.QRCodeScale = 4;
            qrc.QRCodeVersion = 7;
            qrc.QRCodeErrorCorrect = ThoughtWorks.QRCode.Codec.QRCodeEncoder.ERROR_CORRECTION.M;
            System.Drawing.Bitmap btm = qrc.Encode(qrUrl, System.Text.Encoding.UTF8);
            btm.Save(pathQR);
            //#endregion
            * 
            */
            //#region 定义变量做准备.
            //生成表单信息.
            GEEntity en = new GEEntity(frmID, workid);

            MapData mapData = new MapData(frmID);
            //#endregion

           
/***

            //#region 生成水文.
            String rdt = "";
            if (en.getEnMap().getAttrs().contains("RDT"))
            {
                rdt = en.GetValStringByKey("RDT");
                if (rdt.length() > 10)
                    rdt = rdt.substring(0, 10);
            }
            String words = Glo..PrintBackgroundWord;
            words = words.replace("@RDT", rdt);

            if (words.contains("@") == true)
                words = Glo.DealExp(words, en, null);

            String templateFilePathMy = SystemConfig.PathOfDataUser + "InstancePacketOfData\\Template\\";
            WaterImageManage wim = new WaterImageManage();
            wim.DrawWords(templateFilePathMy + "ShuiYin.png", words,Float.parseFloat("0.15"), ImagePosition.Center, path + "\\ShuiYin.png");
            //#endregion
 * 
 */
            //生成 表单的 html.
            StringBuilder sb = new StringBuilder();
          
            //#region 替换模版文件..

            //首先判断是否有约定的文件.
            String docs = "";
            String tempFile = SystemConfig.getPathOfDataUser() + "\\InstancePacketOfData\\Template\\" + mapData.getNo() + ".htm";
            if (new File(tempFile).exists() == true)
            {

            }
            else
            {
                if (gwf != null)
                {
                    Node nd = new Node(gwf.getFK_Node());

                    if (nd.getHisFormType() == NodeFormType.FreeForm)
                        mapData.setHisFrmType(FrmType.FreeFrm);
                    else
                        mapData.setHisFrmType( FrmType.FoolForm);
                }

                if (mapData.getHisFrmType() == FrmType.FoolForm)
                {
                    docs = BP.DA.DataType.ReadTextFile(SystemConfig.getPathOfDataUser() + "\\InstancePacketOfData\\Template\\indexFool.htm");
                    sb =GenerHtmlOfFool(mapData, frmID, workid, en, path, flowNo);
                }
                else
                {
                    docs = BP.DA.DataType.ReadTextFile(SystemConfig.getPathOfDataUser() + "\\InstancePacketOfData\\Template\\indexFree.htm");
                    sb = GenerHtmlOfFree(mapData, frmID, workid, en, path, flowNo);
                }
            }


            docs = docs.replace("@Docs", sb.toString());
            docs = docs.replace("@Width", String.valueOf(mapData.getFrmW()));
            docs = docs.replace("@Height", String.valueOf(mapData.getFrmH()));
            
            Date date = new Date(); 
            SimpleDateFormat sy1=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
            String dateFormat=sy1.format(date); 
            docs = docs.replace("@PrintDT", dateFormat );

            if (flowNo != null )
            {
            	gwf = new GenerWorkFlow(workid);
                gwf.setWorkID(workid);
                gwf.RetrieveFromDBSources();

                docs = docs.replace("@Title", gwf.getTitle());

                if (gwf.getWFState() == WFState.Runing)
                {
                    if ( SystemConfig.getCustomerNo()=="TianYe" && gwf.getNodeName().contains("反馈") == true)
                    {
                        Node nd = new Node(gwf.getFK_Node());
                        if (nd.getIsEndNode() == true)
                        {
                            //让流程自动结束.
                            BP.WF.Dev2Interface.Flow_DoFlowOver(gwf.getFK_Flow(), workid, "打印并自动结束");
                        }
                    }
                }

                //替换模版尾部的打印说明信息.
                String pathInfo = SystemConfig.getPathOfDataUser() + "\\InstancePacketOfData\\Template\\EndInfo\\" + flowNo + ".txt";
                if (new File(pathInfo).exists() == false)
                    pathInfo = SystemConfig.getPathOfDataUser() + "\\InstancePacketOfData\\Template\\EndInfo\\Default.txt";

                docs = docs.replace("@EndInfo", DataType.ReadTextFile(pathInfo));
            }

            String indexFile = SystemConfig.getPathOfDataUser() + "\\InstancePacketOfData\\" + frmID + "\\" + workid + "\\index.htm";
            BP.DA.DataType.WriteFile(indexFile, docs);
            //#endregion 替换模版文件..

            //#region 处理正确的文件名.
            if (fileNameFormat == null)
            {
                if (flowNo != null)
                    fileNameFormat = DBAccess.RunSQLReturnStringIsNull("SELECT Title FROM WF_GenerWorkFlow WHERE WorkID=" + workid, "" +String.valueOf(workid));
                else
                    fileNameFormat =String.valueOf(workid);
            }

            if (DataType.IsNullOrEmpty(fileNameFormat) == true)
                fileNameFormat = String.valueOf(workid);

            fileNameFormat = BP.DA.DataType.PraseStringToFileName(fileNameFormat);
            //#endregion
            
            Hashtable ht = new Hashtable();
            ht.put("htm", billUrl);

            //#region 把所有的文件做成一个zip文件.
            //生成pdf文件
            String pdfPath = path + "\\pdf";
            if (new File(pdfPath).exists() == false)
            	new File(pdfPath).mkdirs();

            fileNameFormat = fileNameFormat.substring(0, fileNameFormat.length() - 1);
            String pdfFile = pdfPath + "\\" + fileNameFormat + ".pdf";       
            String pdfFileExe = SystemConfig.getPathOfDataUser() + "ThirdpartySoftware\\wkhtmltox\\wkhtmltopdf.exe";
            try
            {
                Html2Pdf(pdfFileExe, billUrl, pdfFile);
                if (urlIsHostUrl == false)
                    ht.put("pdf", SystemConfig.GetValByKey("HostURLOfBS","") + "InstancePacketOfData//" + frmID + "//" + workid + "//pdf//" + DataType.PraseStringToUrlFileName(fileNameFormat) + ".pdf");
                else
                {
                	 ht.put("pdf", SystemConfig.GetValByKey("HostURL","") + "/DataUser/InstancePacketOfData/" + frmID + "/" + workid + "/pdf/" + DataType.PraseStringToUrlFileName(fileNameFormat) + ".pdf");
                   // ht.put("pdf", SystemConfig.GetValByKey("HostURL","") + "/DataUser/InstancePacketOfData/" + frmID + "/" + workid + "/pdf/" + DataType.PraseStringToUrlFileName(fileNameFormat) + ".pdf");
                	// ht.put("pdf", SystemConfig.getPathOfDataUser() + "InstancePacketOfData//" + frmID + "//" + workid + "//pdf//" + DataType.PraseStringToUrlFileName(fileNameFormat) + ".pdf");
                }
                }
            catch (Exception ex)
            {
                /*有可能是因为文件路径的错误， 用补偿的方法在执行一次, 如果仍然失败，按照异常处理. */
                fileNameFormat = DBAccess.GenerGUID();
                pdfFile = pdfPath + "\\" + fileNameFormat + ".pdf";
                
                Html2Pdf(pdfFileExe, billUrl, pdfFile);
                ht.put("pdf", SystemConfig.GetValByKey("HostURLOfBS","") + "//InstancePacketOfData//" + frmID + "//" + workid + "//pdf//" + fileNameFormat + ".pdf");
            }
            return BP.Tools.Json.ToJsonEntitiesNoNameMode(ht);
        }
        catch (Exception ex)
        {
            return "err@报表生成错误:" + ex.getMessage();
        }
    }
    public static void Html2Pdf(String pdfFileExe, String htmFile, String pdf) throws Exception
    {
        BP.DA.Log.DebugWriteInfo("@开始生成PDF:" + pdfFileExe + "@pdf=" + pdf + "@htmFile=" + htmFile);
        try
        {
        	String command = String.format(pdfFileExe+" %s %s", htmFile, pdf);
            Runtime rt = Runtime.getRuntime();
            Process p =  rt.exec(command);
          //  p.waitFor();
        }
        catch (RuntimeException ex)
        {
        	throw new RuntimeException(ex.getMessage());
        }
    }
}
