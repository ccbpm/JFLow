package bp.wf.httphandler;

import bp.da.*;
import bp.difference.handler.CommonFileUtils;
import bp.sys.*;
import bp.tools.FileAccess;
import bp.web.*;
import bp.port.*;
import bp.en.*; import bp.en.Map;
import bp.wf.*;
import bp.wf.template.*;
import bp.difference.*;
import bp.*;
import bp.wf.*;
import net.sf.json.JSONObject;
import netscape.javascript.JSObject;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

/** 
 页面功能实体
*/
public class WF_CommTS extends bp.difference.handler.DirectoryPageBase
{

		///#region 参数
	public final String getParas()
	{
		return this.GetRequestVal("Paras");
	}
	public final String getOrderBy()
	{
		return this.GetRequestVal("OrderBy");
	}
	/*public final String getMyPK()
	{
		return this.GetRequestVal("MyPK");
	}
	public final String getNo()
	{
		return this.GetRequestVal("No");
	}
	public final int getOID()
	{
		String str = this.GetRequestVal("OID");
		if (DataType.IsNullOrEmpty(str))
		{
			str = this.GetRequestVal("WorkID");
		}

		if (DataType.IsNullOrEmpty(str))
		{
			str = this.GetRequestVal("PKVal");
		}

		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = "0";
		}
		return Integer.parseInt(str);
	}*/
	public final String getClassID()
	{
		return this.GetRequestVal("ClassID");
	}
	/** 
	 关联服务器端的实体类.
	*/
	public final String getRefEnName()
	{
		String str = this.GetRequestVal("RefEnName");
		if (!DataType.IsNullOrEmpty(str))
		{
			str = str.replace("TS.", "BP.");
		}
		return str;
	}
	public final String getKVs()
	{
		return this.GetRequestVal("KVs");
	}
	public final String getMap()
	{
		return this.GetRequestVal("Map");
	}
	public final String getPK()
	{
		return this.GetRequestVal("PK");
	}
	public final String getPKVal()
	{
		return this.GetRequestVal("PKVal");
	}
	public final int getPKValInt()
	{
		String str = this.GetRequestVal("PKVal");


		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = this.GetRequestVal("WorkID");
		}
		else
		{
			return Integer.parseInt(str);
		}

		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = this.GetRequestVal("NodeID");
		}
		else
		{
			return Integer.parseInt(str);
		}

		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = this.GetRequestVal("OID");
		}
		else
		{
			return Integer.parseInt(str);
		}

		if (DataType.IsNullOrEmpty(str) == true)
		{
			str = "0";
		}

		return Integer.parseInt(str);
	}

		///#endregion 参数

	/** 
	 构造函数
	*/
	public WF_CommTS()
	{
	}


		///#region 页面类.
	/** 
	 从表移动
	 
	 @return 
	*/
	public final String DtlSearch_UpdatIdx() throws Exception {
		Map map = bp.ents.Glo.GenerMap(this.getClassID());

		String pk = "No";
		if (map.getAttrs().contains("No") == true)
		{
			pk = "No";
		}
		else if (map.getAttrs().contains("OID") == true)
		{
			pk = "OID";
		}
		else if (map.getAttrs().contains("MyPK") == true)
		{
			pk = "MyPK";
		}
		else if (map.getAttrs().contains("NodeID") == true)
		{
			pk = "NodeID";
		}
		else if (map.getAttrs().contains("WorkID") == true)
		{
			pk = "WorkID";
		}

		String[] pks = this.GetRequestVal("PKs").split("[,]", -1);
		int idx = 0;
		for (String str : pks)
		{
			if (DataType.IsNullOrEmpty(str) == true)
			{
				continue;
			}
			idx++;
			String sql = "UPDATE " + map.getPhysicsTable() + " SET Idx=" + idx + " WHERE " + pk + "='" + str + "'";
			DBAccess.RunSQL(sql);
		}


			///#region 特殊业务处理.
		if (this.getClassID().equals("TS.WF.Cond") == true)
		{
			//判断设置的顺序是否合理？
			Cond cond = new Cond();
			String pkval = pks[0];
			cond.setMyPK(pkval);
			cond.Retrieve();

			return WF_Admin_Cond2020.List_DoCheckExt(cond.getCondTypeInt(), cond.getNodeID(), cond.getToNodeID());
		}

			///#endregion 特殊业务处理.


		return "移动成功.";
	}

	/** 
	 更新排序
	 
	 @return 
	*/
	public final String TreeEns_UpdatIdx()
	{
		String[] pks = this.GetRequestVal("PKs").split("[,]", -1);
		String ptable = this.GetRequestVal("PTable");
		String pk = this.GetRequestVal("PK");
		int idx = 0;
		for (String str : pks)
		{
			if (DataType.IsNullOrEmpty(str) == true)
			{
				continue;
			}
			idx++;
			String sql = "UPDATE " + ptable + " SET Idx='" + idx + "' WHERE " + pk + "='" + str + "'";
			DBAccess.RunSQL(sql);
		}
		return "执行成功.";
	}

		///#endregion 页面类.

	/** 
	 加入map到缓存.
	 
	 @return 
	*/
	public final String Entity_SetMap() throws Exception {
		bp.ents.Glo.SetMap(this.getClassID(), this.getMap());
		return "1";
	}
	/** 
	 检查是否存在Map
	 
	 @return 
	*/
	public final String Entity_IsExitMap()
	{
		//缓存map.
		if (bp.ents.Glo.IsExitMap(this.getClassID()) == true)
		{
			return "1";
		}

		return "0";
	}
	public final String Entity_IsExits() throws Exception {
		if (this.getPK().equals("No") == true)
		{
			TSEntityNoName en = new TSEntityNoName(this.getClassID());
			en.setNo(this.getPKVal());
			if (en.getIsExits() == true)
			{
				return "1";
			}
			return "0";
		}

		if (this.getPK().equals("MyPK"))
		{
			TSEntityMyPK en = new TSEntityMyPK(this.getClassID());
			en.setMyPK(this.getPKVal());
			if (en.getIsExits() == true)
			{
				return "1";
			}
			return "0";
		}

		if (this.getPK().equals("OID") == true)
		{
			TSEntityOID en = new TSEntityOID(this.getClassID());
			en.setOID(Integer.parseInt(this.getPKVal()));
			if (en.getIsExits()==true)
			{
				return "1";
			}
			return "0";
		}

		if (this.getPK().equals("WorkID") == true)
		{
			TSEntityWorkID en = new TSEntityWorkID(this.getClassID());
			en.setWorkID(Integer.parseInt(this.getPKVal()));
			if (en.getIsExits()==true)
			{
				return "1";
			}
			return "0";
		}
		if (this.getPK().equals("NodeID") == true)
		{
			TSEntityNodeID en = new TSEntityNodeID(this.getClassID());
			en.setNodeID(Integer.parseInt(this.getPKVal()));
			if (en.getIsExits()== true)
			{
				return "1";
			}
			return "0";
		}

		throw new RuntimeException("err@没有判断的entity类型.");
	}
	/** 
	 执行insert方法.
	 
	 @return 
	*/
	public final String Entity_Insert() throws Exception {
		JSONObject json = JSONObject.fromObject(this.GetRequestVal("Row"));
		if (this.getPK().equals("No") == true)
		{
			TSEntityNoName en = new TSEntityNoName(this.getClassID());
			for (Object key : json.keySet()) {
				if(key == null)
					continue;
				String value = json.getString(key.toString());
				en.SetValByKey(key.toString(),value);
			}
			en.Insert();
			return en.ToJson(true);
		}

		if (this.getPK().equals("MyPK") == true)
		{
			TSEntityMyPK en = new TSEntityMyPK(this.getClassID());
			for (Object key : json.keySet()) {
				if(key == null)
					continue;
				String value = json.getString(key.toString());
				en.SetValByKey(key.toString(),value);
			}
			en.Insert();
			return en.ToJson(true);
		}

		if (this.getPK().equals("OID") == true)
		{
			TSEntityOID en = new TSEntityOID(this.getClassID());
			for (Object key : json.keySet()) {
				if(key == null)
					continue;
				String value = json.getString(key.toString());
				en.SetValByKey(key.toString(),value);
			}
			en.Insert();
			return en.ToJson(true);
		}

		if (this.getPK().equals("WorkID") == true)
		{
			TSEntityWorkID en = new TSEntityWorkID(this.getClassID());
			for (Object key : json.keySet()) {
				if(key == null)
					continue;
				String value = json.getString(key.toString());
				en.SetValByKey(key.toString(),value);
			}
			en.Insert();
			return en.ToJson(true);
		}

		if (this.getPK().equals("NodeID") == true)
		{
			TSEntityNodeID en = new TSEntityNodeID(this.getClassID());
			for (Object key : json.keySet()) {
				if(key == null)
					continue;
				String value = json.getString(key.toString());
				en.SetValByKey(key.toString(),value);
			}
			en.Insert();
			return en.ToJson(true);
		}

		throw new RuntimeException("err@没有判断的entity类型.");
	}
	/** 
	 根据
	 
	 @return 
	*/
	public final String Entity_GenerSQLAttrDB() throws Exception {
		String attrKey = this.GetRequestVal("AttrKey"); //  "SELECT * FROM WHERE XX=@SortNo ";
		JSONObject json = JSONObject.fromObject(this.GetRequestVal("Row"));
		if (this.getPK().equals("NodeID") == true)
		{
			TSEntityNodeID en = new TSEntityNodeID(this.getClassID());
			for (Object key : json.keySet()) {
				if(key == null)
					continue;
				String value = json.getString(key.toString());
				en.SetValByKey(key.toString(),value);
			}

			Attr attr = en.getEnMap().GetAttrByKey(attrKey);
			String sql = attr.getUIBindKey();
			sql = bp.wf.Glo.DealExp(sql, en);
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			return bp.tools.Json.ToJson(dt);
		}

		if (this.getPK().equals("MyPK") == true)
		{
			TSEntityMyPK en = new TSEntityMyPK(this.getClassID());
			for (Object key : json.keySet()) {
				if(key == null)
					continue;
				String value = json.getString(key.toString());
				en.SetValByKey(key.toString(),value);
			}

			Attr attr = en.getEnMap().GetAttrByKey(attrKey);
			String sql = attr.getUIBindKey();
			sql = bp.wf.Glo.DealExp(sql, en);
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			return bp.tools.Json.ToJson(dt);
		}

		if (this.getPK().equals("WorkID") == true)
		{
			TSEntityWorkID en = new TSEntityWorkID(this.getClassID());
			for (Object key : json.keySet()) {
				if(key == null)
					continue;
				String value = json.getString(key.toString());
				en.SetValByKey(key.toString(),value);
			}
			Attr attr = en.getEnMap().GetAttrByKey(attrKey);
			String sql = attr.getUIBindKey();
			sql = bp.wf.Glo.DealExp(sql, en);
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			return bp.tools.Json.ToJson(dt);
		}

		if (this.getPK().equals("No") == true)
		{
			TSEntityNoName en = new TSEntityNoName(this.getClassID());
			for (Object key : json.keySet()) {
				if(key == null)
					continue;
				String value = json.getString(key.toString());
				en.SetValByKey(key.toString(),value);
			}

			Attr attr = en.getEnMap().GetAttrByKey(attrKey);
			String sql = attr.getUIBindKey();
			sql = bp.wf.Glo.DealExp(sql, en);
			DataTable dt = DBAccess.RunSQLReturnTable(sql);
			return bp.tools.Json.ToJson(dt);
		}

		return "";
	}
	public final String Entity_Save() throws Exception {
		JSONObject json = JSONObject.fromObject(this.GetRequestVal("Row"));
		if (this.getPK().equals("No") == true)
		{
			TSEntityNoName en = new TSEntityNoName(this.getClassID());
			for (Object key : json.keySet()) {
				if(key == null)
					continue;
				String value = json.getString(key.toString());
				en.SetValByKey(key.toString(),value);
			}
			int i = en.Save();

			return String.valueOf(i);
		}

		if (this.getPK().equals("OID") == true)
		{
			TSEntityOID en = new TSEntityOID(this.getClassID());
			for (Object key : json.keySet()) {
				if(key == null)
					continue;
				String value = json.getString(key.toString());
				en.SetValByKey(key.toString(),value);
			}
			int i = en.Save();
			return String.valueOf(i);
		}

		if (this.getPK().equals("MyPK") == true)
		{
			TSEntityMyPK en = new TSEntityMyPK(this.getClassID());
			for (Object key : json.keySet()) {
				if(key == null)
					continue;
				String value = json.getString(key.toString());
				en.SetValByKey(key.toString(),value);
			}
			int i = en.Save();
			return String.valueOf(i);
		}

		if (this.getPK().equals("WorkID") == true)
		{
			TSEntityWorkID en = new TSEntityWorkID(this.getClassID());
			for (Object key : json.keySet()) {
				if(key == null)
					continue;
				String value = json.getString(key.toString());
				en.SetValByKey(key.toString(),value);
			}
			int i = en.Save();
			return String.valueOf(i);
		}

		if (this.getPK().equals("NodeID") == true)
		{
			TSEntityNodeID en = new TSEntityNodeID(this.getClassID());
			for (Object key : json.keySet()) {
				if(key == null)
					continue;
				String value = json.getString(key.toString());
				en.SetValByKey(key.toString(),value);
			}
			int i = en.Save();
			return String.valueOf(i);
		}

		throw new RuntimeException("err@没有判断的entity类型.");

	}
	public final boolean checkPower(String classID, String pkval)
	{
		if (classID.contains("BP.WF.Template") == true && WebUser.getIsAdmin() == false)
		{
			throw new RuntimeException("非法用户.");
		}

		return true;
	}
	/** 
	 执行更新
	 
	 @return 
	*/
	public final String Entity_Update() throws Exception {
		JSONObject json = JSONObject.fromObject(this.GetRequestVal("Row"));
		if (this.getPK().equals("No") == true)
		{
			TSEntityNoName en = new TSEntityNoName(this.getClassID(), this.getPKVal());
			for (Object key : json.keySet()) {
				if(key == null)
					continue;
				String value = json.getString(key.toString());
				en.SetValByKey(key.toString(),value);
			}
			en.setNo(this.getPKVal());

			//判断是否有对应的后端实体类，如果有则要执行更新.
			if (DataType.IsNullOrEmpty(this.getRefEnName()) == false)
			{
				Entity enServ = ClassFactory.GetEn(this.getRefEnName());
				if (enServ == null)
				{
					throw new RuntimeException("err@TS实体类[" + this.getClassID() + "]关联的[" + this.getRefEnName() + "]拼写错误,");
				}

				enServ.setPKVal(this.getPKVal());
				enServ.RetrieveFromDBSources();
				for (Object key : json.keySet()) {
					if(key == null)
						continue;
					String value = json.getString(key.toString());
					enServ.SetValByKey(key.toString(),value);
				}

				enServ.Update();
			}

			return String.valueOf(en.Update());
		}

		if (this.getPK().equals("MyPK") == true)
		{
			TSEntityMyPK en = new TSEntityMyPK(this.getClassID(), this.getPKVal());
			for (Object key : json.keySet()) {
				if(key == null)
					continue;
				String value = json.getString(key.toString());
				en.SetValByKey(key.toString(),value);
			}
			en.setMyPK(this.getPKVal());

			//判断是否有对应的后端实体类，如果有则要执行更新.
			if (DataType.IsNullOrEmpty(this.getRefEnName()) == false)
			{
				Entity enServ = ClassFactory.GetEn(this.getRefEnName());
				if (enServ == null)
				{
					throw new RuntimeException("err@TS实体类[" + this.getClassID() + "]关联的[" + this.getRefEnName() + "]拼写错误,");
				}

				enServ.setPKVal(this.getPKVal());
				enServ.RetrieveFromDBSources();
				for (Object key : json.keySet()) {
					if(key == null)
						continue;
					String value = json.getString(key.toString());
					enServ.SetValByKey(key.toString(),value);
				}
				enServ.Update();

				//把变更后的值给,TS实体.
				Row row = enServ.getRow();
				for (String key : row.keySet())
				{
					en.getRow().SetValByKey(key, row.get(key));
				}
			}
			return String.valueOf(en.Update());
		}

		if (this.getPK().equals("OID") == true)
		{
			TSEntityOID en = new TSEntityOID(this.getClassID(), this.getPKValInt());
			for (Object key : json.keySet()) {
				if(key == null)
					continue;
				String value = json.getString(key.toString());
				en.SetValByKey(key.toString(),value);
			}

			//判断是否有对应的后端实体类，如果有则要执行更新.
			if (DataType.IsNullOrEmpty(this.getRefEnName()) == false)
			{
				Entity enServ = ClassFactory.GetEn(this.getRefEnName());
				if (enServ == null)
				{
					throw new RuntimeException("err@TS实体类[" + this.getClassID() + "]关联的[" + this.getRefEnName() + "]拼写错误,");
				}

				enServ.setPKVal(this.getPKValInt());
				enServ.RetrieveFromDBSources();
				for (Object key : json.keySet()) {
					if(key == null)
						continue;
					String value = json.getString(key.toString());
					enServ.SetValByKey(key.toString(),value);
				}
				enServ.Update();
			}

			en.setOID(this.getPKValInt());
			return String.valueOf(en.Update());
		}

		if (this.getPK().equals("NodeID") == true)
		{
			TSEntityNodeID en = new TSEntityNodeID(this.getClassID(), this.getPKValInt());
			for (Object key : json.keySet()) {
				if(key == null)
					continue;
				String value = json.getString(key.toString());
				en.SetValByKey(key.toString(),value);
			}
			//判断是否有对应的后端实体类，如果有则要执行更新.
			if (DataType.IsNullOrEmpty(this.getRefEnName()) == false)
			{
				Entity enServ = ClassFactory.GetEn(this.getRefEnName());
				if (enServ == null)
				{
					throw new RuntimeException("err@TS实体类[" + this.getClassID() + "]关联的[" + this.getRefEnName() + "]拼写错误,");
				}

				enServ.setPKVal(this.getPKVal());
				enServ.RetrieveFromDBSources();
				for (Object key : json.keySet()) {
					if(key == null)
						continue;
					String value = json.getString(key.toString());
					enServ.SetValByKey(key.toString(),value);
				}
				enServ.Update();
			}

			en.setNodeID(this.getPKValInt());
			return String.valueOf(en.Update());
		}

		if (this.getPK().equals("WorkID") == true)
		{
			TSEntityWorkID en = new TSEntityWorkID(this.getClassID(), this.getPKValInt());
			for (Object key : json.keySet()) {
				if(key == null)
					continue;
				String value = json.getString(key.toString());
				en.SetValByKey(key.toString(),value);
			}

			//判断是否有对应的后端实体类，如果有则要执行更新.
			if (DataType.IsNullOrEmpty(this.getRefEnName()) == false)
			{
				Entity enServ = ClassFactory.GetEn(this.getRefEnName());
				if (enServ == null)
				{
					throw new RuntimeException("err@TS实体类[" + this.getClassID() + "]关联的[" + this.getRefEnName() + "]拼写错误,");
				}

				enServ.setPKVal(this.getPKValInt());
				enServ.RetrieveFromDBSources();
				for (Object key : json.keySet()) {
					if(key == null)
						continue;
					String value = json.getString(key.toString());
					enServ.SetValByKey(key.toString(),value);
				}
				enServ.Update();
			}

			en.setWorkID(this.getPKValInt());
			return String.valueOf(en.Update());
		}

		throw new RuntimeException("err@没有判断的entity类型. Entity_Update ");
	}
	/** 
	 查询 
	 
	 @return 
	*/
	public final String Entity_Retrieve() throws Exception {

		if (this.getPK().equals("No") == true)
		{
			TSEntityNoName en = new TSEntityNoName(this.getClassID(), this.getPKVal());
			return en.ToJson(true);
		}

		if (this.getPK().equals("MyPK") == true)
		{
			TSEntityMyPK en = new TSEntityMyPK(this.getClassID(), this.getPKVal());
			return en.ToJson(true);
		}

		if (this.getPK().equals("OID") == true)
		{
			TSEntityOID en = new TSEntityOID(this.getClassID(), getPKValInt());
			return en.ToJson(true);
		}

		if (this.getPK().equals("WorkID") == true)
		{
			TSEntityWorkID en = new TSEntityWorkID(this.getClassID(), this.getPKValInt());
			return en.ToJson(true);
		}

		if (this.getPK().equals("NodeID") == true)
		{
			TSEntityNodeID en = new TSEntityNodeID(this.getClassID(), this.getPKValInt());
			return en.ToJson(true);
		}

		throw new RuntimeException("err@没有判断的entity类型. Entity_Retrieve ");
	}
	/** 
	 从数据库里查询.
	 
	 @return 
	*/
	public final String Entity_RetrieveFromDBSources() throws Exception {
		if (this.getPK().equals("No") == true)
		{
			TSEntityNoName en = new TSEntityNoName(this.getClassID());
			en.setNo(this.getPKVal());
			int val = en.RetrieveFromDBSources();
			if (val == 0)
			{
				return "0";
			}
			return en.ToJson(true);
		}

		if (this.getPK().equals("MyPK") == true)
		{
			TSEntityMyPK en = new TSEntityMyPK(this.getClassID());
			en.setMyPK(this.getPKVal());
			int val = en.RetrieveFromDBSources();
			if (val == 0)
			{
				return "0";
			}
			return en.ToJson(true);
		}

		if (this.getPK().equals("OID") == true)
		{
			TSEntityOID en = new TSEntityOID(this.getClassID());
			en.setOID(this.getPKValInt());
			int val = en.RetrieveFromDBSources();
			if (val == 0)
			{
				return "0";
			}
			return en.ToJson(true);
		}

		if (this.getPK().equals("WorkID") == true)
		{
			TSEntityWorkID en = new TSEntityWorkID(this.getClassID());
			en.setWorkID(this.getPKValInt());
			int val = en.RetrieveFromDBSources();
			if (val == 0)
			{
				return "0";
			}
			return en.ToJson(true);
		}

		if (this.getPK().equals("NodeID") == true)
		{
			TSEntityNodeID en = new TSEntityNodeID(this.getClassID());
			en.setNodeID(this.getPKValInt());
			int val = en.RetrieveFromDBSources();
			if (val == 0)
			{
				return "0";
			}
			return en.ToJson(true);
		}

		throw new RuntimeException("err@没有判断的entity类型. Entity_Retrieve ");
	}
	public final String Entities_RetrieveAllFromDBSource() throws Exception {
		if (this.getPK().equals("No") == true)
		{
			TSEntitiesNoName ens = new TSEntitiesNoName(this.getClassID());
			ens.RetrieveAllFromDBSource();
			return ens.ToJson("dt");
		}

		if (this.getPK().equals("OID") == true)
		{
			TSEntitiesOID ens = new TSEntitiesOID(this.getClassID());
			ens.RetrieveAllFromDBSource();
			return ens.ToJson("dt");
		}

		if (this.getPK().equals("MyPK") == true)
		{
			TSEntitiesMyPK ens = new TSEntitiesMyPK(this.getClassID());
			ens.RetrieveAllFromDBSource();
			return ens.ToJson("dt");
		}
		if (this.getPK().equals("WorkID") == true)
		{
			TSEntitiesWorkID ens = new TSEntitiesWorkID(this.getClassID());
			ens.RetrieveAllFromDBSource();
			return ens.ToJson("dt");
		}
		if (this.getPK().equals("NodeID") == true)
		{
			TSEntitiesNodeID ens = new TSEntitiesNodeID(this.getClassID());
			ens.RetrieveAllFromDBSource();
			return ens.ToJson("dt");
		}
		throw new RuntimeException("err@没有判断的entity类型. Entities_RetrieveAllFromDBSource ");
	}
	public final String Entities_RetrieveAll() throws Exception {
		if (this.getPK().equals("No") == true)
		{
			TSEntitiesNoName ens = new TSEntitiesNoName(this.getClassID());
			ens.RetrieveAll(this.getOrderBy());
			return ens.ToJson("dt");
		}

		if (this.getPK().equals("OID") == true)
		{
			TSEntitiesOID ens = new TSEntitiesOID(this.getClassID());
			ens.RetrieveAll(this.getOrderBy());
			return ens.ToJson("dt");
		}

		if (this.getPK().equals("MyPK") == true)
		{
			TSEntitiesMyPK ens = new TSEntitiesMyPK(this.getClassID());
			ens.RetrieveAll(this.getOrderBy());
			return ens.ToJson("dt");
		}

		if (this.getPK().equals("WorkID") == true)
		{
			TSEntitiesWorkID ens = new TSEntitiesWorkID(this.getClassID());
			ens.RetrieveAll(this.getOrderBy());
			return ens.ToJson("dt");
		}

		if (this.getPK().equals("NodeID") == true)
		{
			TSEntitiesNodeID ens = new TSEntitiesNodeID(this.getClassID());
			ens.RetrieveAll(this.getOrderBy());
			return ens.ToJson("dt");
		}

		throw new RuntimeException("err@没有判断的entity类型. Entities_RetrieveAll ");
	}
	public final String Entities_Retrieve() throws Exception {
		if (this.getPK().equals("No"))
		{
			TSEntitiesNoName ens = new TSEntitiesNoName(this.getClassID());
			bp.wf.httphandler.WF_Comm hand = new WF_Comm();
			return hand.Entities_Init_Ext(ens, ens.getNewEntity(), this.getParas());
		}

		if (this.getPK().equals("MyPK"))
		{
			TSEntitiesMyPK ens = new TSEntitiesMyPK(this.getClassID());
			bp.wf.httphandler.WF_Comm hand = new WF_Comm();
			return hand.Entities_Init_Ext(ens, ens.getNewEntity(), this.getParas());
		}

		if (this.getPK().equals("NodeID"))
		{
			TSEntitiesNodeID ens = new TSEntitiesNodeID(this.getClassID());
			bp.wf.httphandler.WF_Comm hand = new WF_Comm();
			return hand.Entities_Init_Ext(ens, ens.getNewEntity(), this.getParas());
		}


		if (this.getPK().equals("WorkID"))
		{
			TSEntitiesWorkID ens = new TSEntitiesWorkID(this.getClassID());
			bp.wf.httphandler.WF_Comm hand = new WF_Comm();
			return hand.Entities_Init_Ext(ens, ens.getNewEntity(), this.getParas());
		}

		if (this.getPK().equals("OID"))
		{
			TSEntitiesOID ens = new TSEntitiesOID(this.getClassID());
			bp.wf.httphandler.WF_Comm hand = new WF_Comm();
			return hand.Entities_Init_Ext(ens, ens.getNewEntity(), this.getParas());
		}


		throw new RuntimeException("err@没有判断的 entity 类型. Entities_Retrieve ");
	}

	public final String Entities_Delete()
	{
		TSEntitiesNoName ens = new TSEntitiesNoName(this.getClassID());
		bp.wf.httphandler.WF_Comm hand = new WF_Comm();
		return hand.Entities_Delete_Ext(ens);
	}

	public final String Entities_RetrieveLikeKey() throws Exception {
		String searchKey = this.GetRequestVal("SearchKey");
		String attrsScop = this.GetRequestVal("AttrsScop");
		String condAttr = this.GetRequestVal("CondAttr");
		String condVal = this.GetRequestVal("CondVal");
		String orderBy = this.GetRequestVal("OrderBy");

		if (this.getPK().equals("No") == true)
		{
			TSEntitiesNoName ens = new TSEntitiesNoName(this.getClassID());
			QueryObject qo = new QueryObject(ens);

			String[] strs = attrsScop.split("[,]", -1);

			qo.addLeftBracket();
			for (String str : strs)
			{
				if (DataType.IsNullOrEmpty(str) == true)
				{
					continue;
				}
				qo.AddWhere(str, " LIKE ", "'%" + searchKey + "%'");
				qo.addOr();
			}
			qo.AddWhere(" 1=2 ");
			qo.addRightBracket();

			if (DataType.IsNullOrEmpty(condAttr) == false)
			{
				qo.addAnd();
				qo.AddWhere(condAttr, "=", condVal);
			}
			if (DataType.IsNullOrEmpty(orderBy) == false)
			{
				qo.addOrderBy(orderBy);
			}
			qo.DoQuery();
			return ens.ToJson("dt");
		}

		return "err@没有判断的类型Entities_RetrieveLikeKey:" + this.getPKVal();
	}

	/** 
	 执行删除
	 
	 @return 
	*/
	public final String Entity_Delete() throws Exception {
		if (this.getPK().equals("No") == true)
		{
			TSEntityNoName en = new TSEntityNoName(this.getClassID());
			en.setNo(this.getPKVal());
			en.RetrieveFromDBSources();

			//判断是否有对应的后端实体类，如果有则要执行更新.
			if (DataType.IsNullOrEmpty(this.getRefEnName()) == false)
			{
				Entity enServ = ClassFactory.GetEn(this.getRefEnName());
				if (enServ == null)
				{
					throw new RuntimeException("err@TS实体类[" + this.getClassID() + "]关联的[" + this.getRefEnName() + "]拼写错误,");
				}

				enServ.setPKVal(this.getPKVal());
				enServ.RetrieveFromDBSources();
				int i = enServ.Delete();

				en.Delete(); //执行本实体的删除.
				return String.valueOf(i);
			}

			return String.valueOf(en.Delete());
		}

		if (this.getPK().equals("MyPK") == true)
		{
			TSEntityMyPK en = new TSEntityMyPK(this.getClassID());
			en.setMyPK(this.getPKVal());
			//判断是否有对应的后端实体类，如果有则要执行更新.
			if (DataType.IsNullOrEmpty(this.getRefEnName()) == false)
			{
				Entity enServ = ClassFactory.GetEn(this.getRefEnName());
				if (enServ == null)
				{
					throw new RuntimeException("err@TS实体类[" + this.getClassID() + "]关联的[" + this.getRefEnName() + "]拼写错误,");
				}

				enServ.setPKVal(this.getPKVal());
				enServ.RetrieveFromDBSources();
				int i = enServ.Delete();

				en.Delete(); //执行本实体的删除.
				return String.valueOf(i);
			}

			return String.valueOf(en.Delete());
		}

		if (this.getPK().equals("OID") == true)
		{
			TSEntityOID en = new TSEntityOID(this.getClassID());
			en.setOID(this.getPKValInt());
			en.RetrieveFromDBSources();

			//判断是否有对应的后端实体类，如果有则要执行更新.
			if (DataType.IsNullOrEmpty(this.getRefEnName()) == false)
			{
				Entity enServ = ClassFactory.GetEn(this.getRefEnName());
				if (enServ == null)
				{
					throw new RuntimeException("err@TS实体类[" + this.getClassID() + "]关联的[" + this.getRefEnName() + "]拼写错误,");
				}

				enServ.setPKVal(this.getPKVal());
				enServ.RetrieveFromDBSources();
				int i = enServ.Delete();

				en.Delete(); //执行本实体的删除.
				return String.valueOf(i);
			}

			return String.valueOf(en.Delete());
		}

		if (this.getPK().equals("WorkID") == true)
		{
			TSEntityWorkID en = new TSEntityWorkID(this.getClassID());
			en.setWorkID(this.getPKValInt());

			//判断是否有对应的后端实体类，如果有则要执行更新.
			if (DataType.IsNullOrEmpty(this.getRefEnName()) == false)
			{
				Entity enServ = ClassFactory.GetEn(this.getRefEnName());
				if (enServ == null)
				{
					throw new RuntimeException("err@TS实体类[" + this.getClassID() + "]关联的[" + this.getRefEnName() + "]拼写错误,");
				}

				enServ.setPKVal(this.getPKVal());
				enServ.RetrieveFromDBSources();
				int i = enServ.Delete();

				en.Delete(); //执行本实体的删除.
				return String.valueOf(i);
			}


			return String.valueOf(en.Delete());
		}

		if (this.getPK().equals("NodeID") == true)
		{
			TSEntityNodeID en = new TSEntityNodeID(this.getClassID());
			en.setNodeID(this.getPKValInt());

			//判断是否有对应的后端实体类，如果有则要执行更新.
			if (DataType.IsNullOrEmpty(this.getRefEnName()) == false)
			{
				Entity enServ = ClassFactory.GetEn(this.getRefEnName());
				if (enServ == null)
				{
					throw new RuntimeException("err@TS实体类[" + this.getClassID() + "]关联的[" + this.getRefEnName() + "]拼写错误,");
				}

				enServ.setPKVal(this.getPKVal());
				enServ.RetrieveFromDBSources();
				int i = enServ.Delete();

				en.Delete(); //执行本实体的删除.
				return String.valueOf(i);
			}

			return String.valueOf(en.Delete());
		}

		throw new RuntimeException("err@没有判断的entity类型. Entity_Delete ");
	}

	public final String Entity_Upload() throws Exception {
		HttpServletRequest request = ContextHolderUtils.getRequest();
		float size =CommonFileUtils.getFilesSize(request,"file");
		if (size== 0)
		{
			return "err@请选择要上传的文件。";
		}
		//获取保存文件信息的实体
		String saveTo = this.GetRequestVal("SaveTo");
		String realSaveTo = "";
		if (DataType.IsNullOrEmpty(saveTo) == true)
		{
			realSaveTo = SystemConfig.getPathOfDataUser() + "UploadFile/";
			saveTo = "/DataUser/UploadFile/";
		}
		else
		{
			if (saveTo.startsWith("/DataUser"))
			{
				realSaveTo = SystemConfig.getPathOfWebApp() + saveTo;
			}
		}
		//获取文件的名称
		String fileName = CommonFileUtils.getOriginalFilename(request,"file");
		if(fileName.indexOf("\\")>-1){
			fileName = fileName.substring(fileName.lastIndexOf("\\")+1);
		}
		if(fileName.indexOf("/")>-1){
			fileName = fileName.substring(fileName.lastIndexOf("/")+1);
		}
		fileName = fileName.replace(" ","");
		fileName = fileName.substring(0, fileName.lastIndexOf('.'));
		//文件后缀
		String ext = FileAccess.getExtensionName(fileName).toLowerCase().replace(".", "");

		//文件大小
		size = size  / 1024;

		File info = new File(saveTo);
		CommonFileUtils.upload(request,"file", new File(realSaveTo + fileName + ext));
		AtPara para = new AtPara();
		para.SetVal("FileName", fileName);
		para.SetVal("FileExt", ext);
		para.SetVal("FileSize", String.valueOf(size));
		para.SetVal("FilePath", saveTo + fileName + ext);
		String saveInfo = para.GenerAtParaStrs();
		if (this.getPK().equals("No") == true)
		{
			TSEntityNoName en = new TSEntityNoName(this.getClassID(), this.getPKVal());
			en.setNo(this.getPKVal());
			en.SetValByKey(this.getKeyOfEn(), saveInfo);
			return String.valueOf(en.DirectUpdate());
		}

		if (this.getPK().equals("MyPK") == true)
		{
			TSEntityMyPK en = new TSEntityMyPK(this.getClassID(), this.getPKVal());
			en.setMyPK(this.getPKVal());
			en.SetValByKey(this.getKeyOfEn(), saveInfo);
			return String.valueOf(en.DirectUpdate());
		}

		if (this.getPK().equals("OID") == true)
		{
			TSEntityOID en = new TSEntityOID(this.getClassID(), this.getPKValInt());
			en.setOID(this.getPKValInt());
			en.SetValByKey(this.getKeyOfEn(), saveInfo);
			return String.valueOf(en.DirectUpdate());
		}

		if (this.getPK().equals("NodeID") == true)
		{
			TSEntityNodeID en = new TSEntityNodeID(this.getClassID(), this.getPKValInt());
			en.setNodeID(this.getPKValInt());
			en.SetValByKey(this.getKeyOfEn(), saveInfo);
			return String.valueOf(en.DirectUpdate());
		}

		if (this.getPK().equals("WorkID") == true)
		{
			TSEntityWorkID en = new TSEntityWorkID(this.getClassID(), this.getPKValInt());
			en.setWorkID(this.getPKValInt());
			en.SetValByKey(this.getKeyOfEn(), saveInfo);
			return String.valueOf(en.DirectUpdate());
		}
		return "上传成功";
	}
}
