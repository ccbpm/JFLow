package bp.ccbill.template;

import bp.en.*;
import java.util.*;

/** 
 单据查询岗位
*/
public class FrmStationDepts extends EntitiesMM
{

	/** 
	 单据查询岗位
	*/
	public FrmStationDepts() throws Exception {
	}
	/** 
	 单据查询岗位
	 
	 param frmID 单据ID
	*/
	public FrmStationDepts(String frmID) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FrmStationDeptAttr.FK_Frm, frmID);
		qo.DoQuery();
	}

	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()  {
		return new FrmStationDept();
	}


		///#region 为了适应自动翻译成java的需要,把实体转换成 List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FrmStationDept> ToJavaList() {
		return (java.util.List<FrmStationDept>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmStationDept> Tolist()  {
		ArrayList<FrmStationDept> list = new ArrayList<FrmStationDept>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmStationDept)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成 List.
}