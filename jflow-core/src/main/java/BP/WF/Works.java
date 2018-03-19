package BP.WF;

import java.util.List;

import BP.En.Attr;
import BP.En.EntitiesOID;
import BP.En.Entity;
import BP.En.FieldType;
import BP.En.QueryObject;
import BP.En.UIContralType;

/** 
 工作 集合
 
*/
public abstract class Works extends EntitiesOID
{

		
	/** 
	 信息采集基类
	 
	*/
	public Works()
	{
	}

		///#endregion


		///#region 查询方法
	/** 
	 查询工作(不适合审核节点查询)
	 
	 @param empId 工作人员
	 @param nodeStat 节点状态
	 @param fromdate 记录日期从
	 @param todate 记录日期到
	 @return 
	*/
	public final int Retrieve(String key, String empId, String fromdate, String todate)
	{
		QueryObject qo = new QueryObject(this);
			qo.AddWhere(WorkAttr.Rec, empId);

		qo.addAnd();
		qo.AddWhere(WorkAttr.RDT, ">=", fromdate);
		qo.addAnd();
		qo.AddWhere(WorkAttr.RDT, "<=", todate);

		if (key.trim().length() == 0)
		{
			return qo.DoQuery();
		}
		else
		{
			if (key.indexOf("%") == -1)
			{
				key = "%" + key + "%";
			}
			Entity en = this.getGetNewEntity();
			qo.addAnd();
			qo.addLeftBracket();
			qo.AddWhere(en.getPK(), " LIKE ", key);
			for (Attr attr : en.getEnMap().getAttrs())
			{
				if (attr.getMyFieldType() == FieldType.RefText)
				{
					continue;
				}
				if (attr.getUIContralType() == UIContralType.DDL || attr.getUIContralType() == UIContralType.CheckBok)
				{
					continue;
				}
				qo.addOr();
				qo.AddWhere(attr.getKey(), " LIKE ", key);
			}
			qo.addRightBracket();
			return qo.DoQuery();
		}
	}
	public final int Retrieve(String fromDataTime, String toDataTime)
	{
		QueryObject qo = new QueryObject(this);
		qo.setTop(90000);
		qo.AddWhere(WorkAttr.RDT, " >=", fromDataTime);
		qo.addAnd();
		qo.AddWhere(WorkAttr.RDT, " <= ", toDataTime);
		return qo.DoQuery();
	}
	public List<Work> ToJavaList()
	{
		return (List<Work>)(Object)this;
	}
}