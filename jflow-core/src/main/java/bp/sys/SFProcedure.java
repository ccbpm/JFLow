package bp.sys;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.web.*;
import bp.difference.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.*;

/** 
 用户自定义表
*/
public class SFProcedure extends EntityNoName
{
	public final String getFKSFDBSrc()  {
		return this.GetValStringByKey("FK_SFDBSrc");
	}
	public final String getSelectStatement()  {
		return this.GetValStringByKey("SelectStatement");
	}


		///#region 数据源属性.
	/** 
	 获得外部数据表
	*/

	public final DataTable GenerHisDataTable() throws Exception {
		return GenerHisDataTable(null);
	}

	public final DataTable GenerHisDataTable(Hashtable ht) throws Exception {
		//创建数据源.
		SFDBSrc src = new SFDBSrc(this.getFKSFDBSrc());


			///#region WebApi接口
		if (src.getDBSrcType().equals("WebApi") == true)
		{
			//执行POST
			String post = this.GetValStringByKey("RequestMethod");
			String postData = bp.tools.HttpClientUtil.HttpPostConnect_Data(this.getFKSFDBSrc(), this.getSelectStatement(), ht, post);

			String jsonNode = this.GetValStringByKey("JsonNode"); //json的节点.
			JSONArray jArr = new JSONArray();
			try
			{
				JSONObject json = JSONObject.fromObject(postData);
				if (!DataType.IsNullOrEmpty(jsonNode))
				{
					jArr = JSONArray.fromObject(json.get(jsonNode).toString());
				}
				else
				{
					jArr = JSONArray.fromObject(postData);
				}
			}
			catch (RuntimeException ex)
			{
				throw new RuntimeException("err@转化JSON出现错误Data=【" + postData + "】" + ex.getMessage());
			}
			return bp.tools.Json.ToDataTable(jArr.toString());
		}

			///#endregion WebApi接口


			///#region SQL接口
		String runObj = this.getSelectStatement();
		if (DataType.IsNullOrEmpty(runObj))
		{
			throw new RuntimeException("@外键类型SQL配置错误," + this.getNo() + " " + this.getName() + " 是一个(SQL)类型(" + this.GetValStrByKey("SrcType") + ")，但是没有配置sql.");
		}

		if (runObj == null)
		{
			runObj = "";
		}

		runObj = runObj.replace("~", "'");
		runObj = runObj.replace("/#", "+"); //为什么？
		runObj = runObj.replace("/$", "-"); //为什么？
		if (runObj.contains("@WebUser.No"))
		{
			runObj = runObj.replace("@WebUser.No", WebUser.getNo());
		}

		if (runObj.contains("@WebUser.Name"))
		{
			runObj = runObj.replace("@WebUser.Name", WebUser.getName());
		}

		if (runObj.contains("@WebUser.FK_DeptName"))
		{
			runObj = runObj.replace("@WebUser.FK_DeptName", WebUser.getDeptName());
		}

		if (runObj.contains("@WebUser.FK_Dept"))
		{
			runObj = runObj.replace("@WebUser.FK_Dept", WebUser.getDeptNo());
		}

		if (runObj.contains("@") == true && ht != null)
		{
			for (Object key : ht.keySet())
			{
				//值为空或者null不替换
				if (ht.get(key) == null || ht.get(key).equals("") == true)
				{
					continue;
				}

				if (runObj.contains("@" + key))
				{
					runObj = runObj.replace("@" + key, ht.get(key).toString());
				}
				//不包含@则返回SQL语句
				if (runObj.contains("@") == false)
				{
					break;
				}
			}
		}
		if (runObj.contains("@") && SystemConfig.isBSsystem() == true)
		{
			/*如果是bs*/
			for (String key : ContextHolderUtils.getRequest().getParameterMap().keySet())
			{
				if (DataType.IsNullOrEmpty(key))
				{
					continue;
				}
				runObj = runObj.replace("@" + key, ContextHolderUtils.getRequest().getParameter(key));
			}
		}
		if (runObj.contains("@") == true)
		{
			throw new RuntimeException("@外键类型SQL错误," + runObj + "部分过程条件没有被替换.");
		}
		DataTable dt = null;
		try
		{
			dt = src.RunSQLReturnTable(runObj);
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("err@获得SFProcedure(" + this.getNo() + "," + this.getName() + ")出现错误:SQL[" + runObj + "],数据库异常信息:" + ex.getMessage());
		}
		if (SystemConfig.getAppCenterDBFieldCaseModel() != FieldCaseModel.None)
		{
			dt.Columns.get(0).ColumnName = "No";
			dt.Columns.get(1).ColumnName = "Name";
			if (dt.Columns.size()== 3)
			{
				dt.Columns.get(2).ColumnName = "ParentNo";
			}
		}
		return dt;

			///#endregion SQL接口
	}

		///#endregion


		///#region 构造方法
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		uac.Readonly();
		return uac;
	}
	/** 
	 用户自定义表
	*/
	public SFProcedure()
	{
	}
	public SFProcedure(String no) throws Exception {
		this.setNo(no);
		this.Retrieve();
	}
	/** 
	 EnMap
	*/
	@Override
	public Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Sys_SFProcedure", "过程");

		map.AddTBStringPK(SFTableAttr.No, null, "表英文名称", true, false, 1, 200, 20);
		map.AddTBString(SFTableAttr.Name, null, "表中文名称", true, false, 0, 200, 20);

		map.AddDDLEntities(SFTableAttr.FK_SFDBSrc, "local", "数据源", new bp.sys.SFDBSrcs(), true);

		map.AddTBString("ConnString", null, "ConnString", true, false, 0, 200, 150, true);
		map.AddDDLSysEnum("IsPara", 0, "参数个数", true, true, "IsPara", "@0=无参数@1=有参数");

		map.AddTBString("ExpDoc", null, "表达式内容", true, false, 0, 1000, 600, true);
		map.AddTBString("ExpNote", null, "表达式说明", true, false, 0, 1000, 600, true);
		map.AddDDLStringEnum("RequestMethod", "Get", "请求模式", "@Get=Get@POST=POST", true);
		map.AddTBStringDoc("ColumnsRemark", "", "格式备注", true, false, true);

		// 创建信息
		map.AddGroupAttr("创建信息");
		map.AddTBString("Remark", null, "备注", true, false, 0, 100, 20, true);
		map.AddTBDateTime("RDT", null, "创建日期", true, true);
		map.AddTBString("OrgNo", null, "组织编号", true, true, 0, 100, 20);
		map.AddTBAtParas();
		//查找.
		map.AddSearchAttr(SFTableAttr.FK_SFDBSrc);


		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	/** 
	 执行SQL
	 
	 @param ht 参数
	 @return 返回执行结果
	*/
	public final String ExecSQL(Hashtable ht, SFDBSrc src) throws Exception {
		String sql = this.getSelectStatement();
		try
		{
			sql = bp.difference.Glo.DealExp(sql, ht); //处理sql.

			int num = src.RunSQL(sql);
			if (num == 0)
			{
				return "执行失败:" + this.GetValStrByKey("MsgOfErr") + "，返回结果为0";
			}
			return "执行成功:" + this.GetValStrByKey("MsgOfOK") + "，返回结果为" + num;
		}
		catch (RuntimeException ex)
		{
			return "执行错误:" + this.GetValStrByKey("MsgOfErr") + "，请检查配置的SQL是否正确:[" + sql + "]";
		}
	}
	public final String Exec(String paras) throws Exception {
		SFDBSrc src = new SFDBSrc();
		src.setNo(this.getFKSFDBSrc());
		src.RetrieveFromDBSources();

		Hashtable ht = DataType.ParseParasToHT(paras);
		if (src.getDBSrcType().equals("WebApi") == true)
		{
			return ExecWebApi(ht, src);
		}
		return ExecSQL(ht, src);
	}

	public final String ExecWebApi(Hashtable ht, SFDBSrc src) throws Exception {
		try
		{
			String postData = SFTable.Data_WebApi(ht, this.GetValStringByKey("RequestMethod"), this.getFKSFDBSrc(), this.getSelectStatement());
			return "" + this.GetValByKey("MsgOfOK") + " -:" + postData;
		}
		catch (RuntimeException ex)
		{
			return "err@[" + this.GetValByKey("MsgOfErr") + "]:失败信息" + ex.getMessage();
		}

		//// 需要替换的参数
		//String fieldNo = this.GetValStringByKey("FieldNo");
		//if (DataType.IsNullOrEmpty(fieldNo))
		//    fieldNo = "No";

		//String fieldName = this.GetValStringByKey("FieldName");
		//if (DataType.IsNullOrEmpty(fieldName))
		//    fieldName = "Name";

		//String fieldParentNo = this.GetValStringByKey("FieldParentNo");
		//if (DataType.IsNullOrEmpty(fieldParentNo))
		//    fieldParentNo = "ParentNo";

		//// 根节点
		//String jsonNode = this.GetValStringByKey("JsonNode");

		//JToken jToken = JToken.Parse(postData);
		//// 如果是JSON数组
		//if (jToken.Type == JTokenType.Array)
		//{
		//    // 新的对象，用来删除原对象无用字段
		//    JArray newJsonArr = new JArray();
		//    JArray arr = (JArray)jToken;
		//    JObject firstItem = (JObject)arr[0];
		//    checkJsonField(firstItem, fieldNo, fieldName, fieldParentNo);
		//    foreach (JObject obj in arr)
		//    {
		//        newJsonArr.Add(getJSONByTargetName(obj, fieldNo, fieldName, fieldParentNo));
		//    }
		//    return BP.Tools.Json.ConvertToDataTable(newJsonArr);
		//}
		//// 如果是JSON对象
		//if (jToken.Type == JTokenType.Object)
		//{
		//    JObject jsonItem = (JObject)jToken;
		//    // 判断是不是有根节点
		//    // 如果有
		//    if (!DataType.IsNullOrEmpty(jsonNode) && jsonItem.containsKey(jsonNode))
		//    {
		//        // 新的对象，用来删除原对象无用字段
		//        JArray newJsonArr = new JArray();
		//        JToken jToken1 = jsonItem[jsonNode];
		//        // 判断当前是不是数组或者
		//        if (jToken1.Type == JTokenType.Array)
		//        {
		//            JObject firstItem = (JObject)jToken1[0];
		//            checkJsonField(firstItem, fieldNo, fieldName, fieldParentNo);
		//            foreach (JObject obj in jToken1)
		//            {
		//                newJsonArr.Add(getJSONByTargetName(obj, fieldNo, fieldName, fieldParentNo));
		//            }
		//            return BP.Tools.Json.ToDataTable(newJsonArr.ToString());
		//        }
		//        if (jToken1.Type == JTokenType.Object)
		//        {
		//            JObject targetObj = (JObject)jToken;
		//            checkJsonField(targetObj, fieldNo, fieldName, fieldParentNo);
		//            JObject itemOfRootNode = getJSONByTargetName(targetObj, fieldNo, fieldName, fieldParentNo);
		//            return BP.Tools.Json.ToDataTable(itemOfRootNode.ToString());
		//        }
		//        throw new Exception("指定的RootNode下不是JSON数组 或 JSON对象");

		//    }
		//    // 如果没有配置rootNode，检查他本身有没有所需属性
		//    JObject currObj = (JObject)jToken;
		//    checkJsonField(currObj, fieldNo, fieldName, fieldParentNo);
		//    JObject itemOfResponse = getJSONByTargetName(currObj, fieldNo, fieldName, fieldParentNo);
		//    return BP.Tools.Json.ToDataTable(itemOfResponse.ToString());
		//}
		//throw new Exception("Web_API 没有正确返回JSON字符串");
	}
	/** 
	 检查是否有依赖的引用？
	 
	 @return 
	*/
	public final String IsCanDelete() throws Exception {
		MapAttrs mattrs = new MapAttrs();
		mattrs.Retrieve(MapAttrAttr.UIBindKey, this.getNo());
		if (mattrs.size()!= 0)
		{
			String err = "";
			for (MapAttr item : mattrs.ToJavaList())
			{
				err += " @ " + item.getMyPK() + " " + item.getName();
			}
			return "err@如下实体字段在引用:" + err + "。您不能删除该表。";
		}
		return null;
	}
	@Override
	protected boolean beforeDelete() throws Exception
	{
		String delMsg = this.IsCanDelete();
		if (delMsg != null)
		{
			throw new RuntimeException(delMsg);
		}

		return super.beforeDelete();
	}
}
