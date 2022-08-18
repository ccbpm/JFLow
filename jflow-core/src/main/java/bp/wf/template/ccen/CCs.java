package bp.wf.template.ccen;

import bp.en.*;
import bp.wf.template.*;
import java.util.*;

/** 
 抄送s
*/
public class CCs extends Entities
{

		///#region 方法
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity() {
		return new CC();
	}
	/** 
	 抄送
	*/
	public CCs() {
	}
	public CCs(String fk_flow) throws Exception {
		this.Retrieve(NodeAttr.FK_Flow, fk_flow, NodeAttr.Step);
		return;
	}


		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<CC> ToJavaList() {
		return (java.util.List<CC>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<CC> Tolist()  {
		ArrayList<CC> list = new ArrayList<CC>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((CC)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}