package BP.WF.Template;

import BP.DA.AtPara;
import BP.DA.DBAccess;
import BP.Tools.StringHelper;
import BP.WF.Flow;
import BP.Sys.MapData;

/** 
 工作流定义管理
 
*/
public class WorkflowDefintionManager
{
	/** 
	 保存流程, 用 ~ 隔开。
	 
	 @param flowNo 流程编号
	 @param nodes 节点信息，格式为:@NodeID=xxxx@X=xxx@Y=xxx@Name=xxxx@RunModel=1
	 @param dirs 方向信息，格式为: @Node=xxxx@ToNode=xxx@X=xxx@Y=xxx@Name=xxxx   
	 @param labes 标签信息，格式为:@MyPK=xxxxx@Label=xxx@X=xxx@Y=xxxx
	 * @throws Exception 
	*/
	public static String SaveFlow(String fk_flow, String nodes, String dirs, String labes) throws Exception
	{
		try
		{

				///#region 处理方向.
			String sql = "DELETE FROM WF_Direction WHERE FK_Flow='" + fk_flow + "'";
			DBAccess.RunSQL(sql);

			String[] mydirs = dirs.split("[~]", -1);
			for (String dir : mydirs)
			{
				if (StringHelper.isNullOrEmpty(dir))
				{
					continue;
				}

				AtPara ap = new AtPara(dir);

				String dots = ap.GetValStrByKey("Dots").replace('#', '@');
				if (StringHelper.isNullOrEmpty(dots) == true)
				{
					dots = "";
				}

				Direction enDir = new Direction();
				enDir.setNode(ap.GetValIntByKey(DirectionAttr.Node));
				enDir.setToNode(ap.GetValIntByKey(DirectionAttr.ToNode));
				enDir.setIsCanBack(ap.GetValBoolenByKey(DirectionAttr.IsCanBack));
				//enDir.setDirType(ap.GetValIntByKey(DirectionAttr.DirType));
				enDir.setFK_Flow(fk_flow);
				enDir.setDots(dots);
				try
				{
					enDir.Insert();
				}
				catch (java.lang.Exception e)
				{
				   // enDir.Update();
				}
			}

				///#endregion 处理方向.


			   ///#region 保存节点
			String[] nds = nodes.split("[~]", -1);
			for (String nd : nds)
			{
				if (StringHelper.isNullOrEmpty(nd))
				{
					continue;
				}

				AtPara ap = new AtPara(nd);
				sql = "UPDATE WF_Node SET X=" + ap.GetValStrByKey("X") + ",Y=" + ap.GetValStrByKey("Y") + ", Name='"+ap.GetValStrByKey("Name")+"' WHERE NodeID="+ap.GetValIntByKey("NodeID");
				DBAccess.RunSQL(sql);
			}
			Flow.UpdateVer(fk_flow);

				///#endregion 保存节点


				///#region 处理标签。
			sql = "DELETE FROM WF_LabNote WHERE FK_Flow='" + fk_flow + "'";
			DBAccess.RunSQL(sql);

			String[] mylabs = labes.split("[~]", -1);
			for (String lab : mylabs)
			{
				if (StringHelper.isNullOrEmpty(lab))
				{
					continue;
				}

				AtPara ap = new AtPara(lab);
				LabNote ln = new LabNote();
				ln.setMyPK(BP.DA.DBAccess.GenerGUID()); // ap.GetValStrByKey("MyPK");
				ln.setFK_Flow(fk_flow);
				ln.setName(ap.GetValStrByKey("Label"));
				ln.setX(ap.GetValIntByKey("X"));
				ln.setY(ap.GetValIntByKey("Y"));
				ln.Insert();
			}

				///#endregion 处理标签。

			// 备份文件
			//f1.WriteToXml();
		}
		catch (RuntimeException ex)
		{
			return ex.getMessage();
		}
		return null;
	}
	/** 
	 导出流程模版
	 
	 @param flowNo
	 @param saveToPath
	*/
	public static void ExpWorkFlowTemplete(String flowNo, String saveToPath)
	{
	}
	/** 
	 导入流程模版
	 
	 @param flowNo 流程编号
	 @param filePath 文件路径
	*/
	public static void ImpWorkFlowTemplete(String flowNo, String filePath)
	{
	}
	/** 
	 执行新建一个流程模版
	 
	 @param flowSort 流程类别
	*/
	public static void CreateFlowTemplete(String flowSort)
	{
	}
	/** 
	 删除一个流程模版
	 
	 @param flowNo 流程编号
	 * @throws Exception 
	*/
	public static String DeleteFlowTemplete(String flowNo) throws Exception
	{
		BP.WF.Flow fl1 = new BP.WF.Flow(flowNo);
		try
		{
			fl1.DoDelete();
			return "删除成功.";
		}
		catch (RuntimeException ex)
		{
			BP.DA.Log.DefaultLogWriteLineError("Do Method DelFlow Branch has a error , para:\t" + flowNo + ex.getMessage());
			return ex.getMessage();
		}
	}
	public static String DeleteFrmTemplate(String v1) throws Exception{
		
		MapData md4 = new MapData();
		try{
		md4.setNo(v1);
		md4.Delete();
		return null;
		}catch(RuntimeException e){
			BP.DA.Log.DefaultLogWriteLineError("Do Method DelFrm Branch has a error , para:\t" + v1 + e.getMessage());
			return e.getMessage();
		}
		
	}
}