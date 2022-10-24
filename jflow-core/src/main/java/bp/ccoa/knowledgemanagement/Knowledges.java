package bp.ccoa.knowledgemanagement;

import bp.da.*;
import bp.web.*;
import bp.en.*;
import bp.port.*;
import bp.sys.*;
import java.util.*;

/** 
 知识管理 s
*/
public class Knowledges extends EntitiesNoName
{

		///#region 查询.
	/** 
	 所有的知识管理
	 
	 @return 
	*/
	public final String Default_Init() throws Exception {

		//初始化实体.
		Knowledges ens = new Knowledges();

		QueryObject qo = new QueryObject(ens);
		//  qo.addLeftBracket();
		//qo.AddWhere(KnowledgeAttr.KnowledgeSta, 0);

		//qo.addOr();
		//qo.AddWhere(KnowledgeAttr.KnowledgeSta, 1);


		//qo.addLeftBracket();
		//qo.AddWhere(KnowledgeAttr.KnowledgeSta, 1);
		//qo.addAnd();
		//qo.AddWhere(KnowledgeAttr.Emps, " like ", "%," + WebUser.getNo() + ",%");
		//qo.addRightBracket();

		if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
		{
			qo.addAnd();
			qo.AddWhere(KnowledgeAttr.OrgNo, " = ", WebUser.getOrgNo());
		}
		qo.DoQuery();

		return ens.ToJson("dt");
	}

	public final String Default_Init11() throws Exception {

		//初始化实体.
		Knowledges ens = new Knowledges();

		QueryObject qo = new QueryObject(ens);
		//  qo.addLeftBracket();
		qo.AddWhere(KnowledgeAttr.KnowledgeSta, 0);

		//   qo.addOr();

		//qo.addLeftBracket();
		//qo.AddWhere(KnowledgeAttr.KnowledgeSta, 1);
		//qo.addAnd();
		//qo.AddWhere(KnowledgeAttr.Emps, " like ", "%," + WebUser.getNo() + ",%");
		//qo.addRightBracket();

		if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
		{
			qo.addAnd();
			qo.AddWhere(KnowledgeAttr.OrgNo, " = ", WebUser.getOrgNo());
		}
		qo.DoQuery();

		return ens.ToJson("dt");
	}
	/** 
	 关注的知识点
	 
	 @return 
	*/
	public final String Default_KMDtlFoucs() throws Exception {
		KMDtls dtls = new KMDtls();
		dtls.Retrieve(KMDtlAttr.Foucs, " LIKE ", "%" + WebUser.getNo() + "%");
		return dtls.ToJson("dt");
	}

		///#endregion 重写.


		///#region 重写.
	/** 
	 知识管理
	*/
	public Knowledges() throws Exception {
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity() {
		return new Knowledge();
	}

		///#endregion 重写.


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<Knowledge> ToJavaList() {
		return (java.util.List<Knowledge>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<Knowledge> Tolist()  {
		ArrayList<Knowledge> list = new ArrayList<Knowledge>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((Knowledge)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.


	public final String Selecter_DeptEmps() throws Exception {
		DataSet ds = new DataSet();

		Depts depts = new Depts();
		depts.RetrieveAll();

		Emps emps = new Emps();
		emps.RetrieveAll();

		ds.Tables.add(depts.ToDataTableField("Depts"));
		ds.Tables.add(emps.ToDataTableField("Emps"));

		return bp.tools.Json.ToJson(ds);

	}
}