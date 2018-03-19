package cn.jflow.common.controller;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import BP.DA.Log;
import BP.Sys.GroupField;
import BP.Sys.GroupFieldAttr;
import BP.Sys.MapAttr;
import BP.Sys.MapExt;
import BP.Sys.MapExtXmlList;
import BP.Tools.StringHelper;
import cn.jflow.controller.wf.workopt.BaseController;

@Controller
@RequestMapping("/WF/Admin/FoolFormDesigner")
public class FoolFormDesingerController extends BaseController{
	
	@RequestMapping(value="/foolForm" , method = RequestMethod.POST)
	@ResponseBody
	public void execute()
	{
		String doType  = getRequest().getParameter("DoType");
		String msg = "";
		PrintWriter out  = null;
		if("TBFullCtrl_Init".equals(doType))
		{
			msg = TBFullCtrl_Init();
		}else if("TBFullCtrl_Save".equals(doType))   //移动位置..
		{
			msg = TBFullCtrl_Save();
		} else if("Up".equals(doType))   //移动位置..
		{
			MapAttr attr = new MapAttr(getMyPK());
            attr.DoUp();
		}else if("Down".equals(doType)) //移动位置..
		{
			MapAttr attrDown = new MapAttr(getMyPK());
            attrDown.DoDown();
		}else if("GFDoUp".equals(doType)) //移动位置..
		{
			GroupField gf = new GroupField(getRefOID());
            gf.DoUp();
            gf.Retrieve();
            if (gf.getIdx() == 0)
                return;

            int oidIdx = gf.getIdx();
            gf.setIdx(gf.getIdx() - 1);
            GroupField gfUp = new GroupField();
            if (gfUp.Retrieve(GroupFieldAttr.EnName, gf.getEnName(), GroupFieldAttr.Idx, gf.getIdx()) == 1)
            {
                gfUp.setIdx(oidIdx);
                gfUp.Update();
            }
            gf.Update();
		}else if("GFDoDown".equals(doType)) //移动位置..
		{
			 GroupField mygf = new GroupField(getRefOID());
             mygf.DoDown();
             mygf.Retrieve();
             int oidIdx1 = mygf.getIdx();
             mygf.setIdx(mygf.getIdx() + 1);
             GroupField gfDown = new GroupField();
             if (gfDown.Retrieve(GroupFieldAttr.EnName, mygf.getEnName(), GroupFieldAttr.Idx, mygf.getIdx()) == 1)
             {
                 gfDown.setIdx(oidIdx1);
                 gfDown.Update();
             }
             mygf.Update();
		}else if("DtlFieldUp".equals(doType)) //字段上移
		{
			MapAttr attrU = new MapAttr(getMyPK());
            attrU.DoUp();
		}else if("DtlFieldDown".equals(doType)) //字段下移.
		{
			MapAttr attrD = new MapAttr(getMyPK());
            attrD.DoDown();
		}else
		{
			msg = "err@没有判断的标记:" + this.getDoType();
		}
			
		try {
			out = getResponse().getWriter();
			out.write(msg);
		} catch (IOException e) {
			msg = "foolForm err@" + e.getMessage();
			Log.DebugWriteError(msg);
		}finally
		{
			if(null != out)
			{
				out.close();
			}
		}
	}
	
	/**
	 * 初始化
	 * @return
	 */
    public String TBFullCtrl_Init()
    {
        MapExt ext = new MapExt();
        ext.setMyPK(getFK_MapData() + "_" + MapExtXmlList.TBFullCtrl + "_" + getKeyOfEn());
        ext.setFK_MapData(getFK_MapData());
        ext.setExtType(MapExtXmlList.TBFullCtrl);
        if (ext.RetrieveFromDBSources() == 0)
            return "";

        return ext.ToJson();
    }
    
    public String TBFullCtrl_Save()
    {
    	MapExt me = new MapExt();
        me.setMyPK(getFK_MapData() + "_" + MapExtXmlList.TBFullCtrl + "_" + getKeyOfEn());
        me.RetrieveFromDBSources();

        me.setExtType(MapExtXmlList.TBFullCtrl);
        me.setDoc(this.GetValFromFrmByKey("TB_SQL"));
        me.setAttrOfOper(getKeyOfEn());
        me.setFK_MapData(getFK_MapData());
        me.setFK_DBSrc(this.GetValFromFrmByKey("DDL_DBSrc"));
        me.Save();

        return "操作成功...";
    }
    
    /**
     * 获得Form数据.
     * @param key
     * @return 返回值
     */
    public String GetValFromFrmByKey(String key)
    {
        String val = getRequest().getParameter(key);
        val = val.replace("'", "~");
        return val;
    }
    
    public String getKeyOfEn()
	{
		String str  = getRequest().getParameter("KeyOfEn");
		if(StringHelper.isNullOrEmpty(str))
		{
			return "";
		}
		return str;
	}
	
    public String getFK_MapData()
	{
		String str  = getRequest().getParameter("FK_MapData");
		if(StringHelper.isNullOrEmpty(str))
		{
			return getMyPK();
		}
		return str;
	}
    
	public String getMyPK()
	{
		String MyPK = getRequest().getParameter("MyPK");
		if(StringHelper.isNullOrEmpty(MyPK))
		{
			return "";
		}
		return MyPK;
	}
	
	public int getRefOID()
	{
		String refOID = getRequest().getParameter("RefOID");
		if(StringHelper.isNullOrEmpty(refOID))
		{
			return 0;
		}
		return Integer.parseInt(refOID);
	}
	
}
