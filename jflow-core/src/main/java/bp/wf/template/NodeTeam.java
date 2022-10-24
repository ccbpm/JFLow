package bp.wf.template;

import bp.da.*;
import bp.en.*;
import bp.en.Map;
import bp.wf.port.*;
import bp.*;
import bp.wf.*;
import java.util.*;

/** 
 节点用户组
 节点的用户组有两部分组成.	 
 记录了从一个节点到其他的多个节点.
 也记录了到这个节点的其他的节点.
*/
public class NodeTeam extends EntityMyPK
{

		///#region 基本属性
	/** 
	 UI界面上的访问控制
	*/
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();
		uac.OpenAll();
		return uac;
	}
	/** 
	节点
	*/
	public final int getFK_Node() throws Exception
	{
		return this.GetValIntByKey(NodeTeamAttr.FK_Node);
	}
	public final void setFK_Node(int value)  throws Exception
	 {
		this.SetValByKey(NodeTeamAttr.FK_Node, value);
	}
	public final String getFKTeamT() throws Exception
	{
		return this.GetValRefTextByKey(NodeTeamAttr.FK_Team);
	}
	/** 
	 用户组
	*/
	public final String getFK_Team() throws Exception
	{
		return this.GetValStringByKey(NodeTeamAttr.FK_Team);
	}
	public final void setFK_Team(String value)  throws Exception
	 {
		this.SetValByKey(NodeTeamAttr.FK_Team, value);
	}

		///#endregion


		///#region 构造方法
	/** 
	 节点用户组
	*/
	public NodeTeam()  {
	}
	/** 
	 重写基类方法
	*/
	@Override
	public bp.en.Map getEnMap() {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("WF_NodeTeam", "节点岗位");
		map.AddMyPK();
		map.AddTBInt(NodeTeamAttr.FK_Node, 0, "节点", false, false);

			// #warning ,这里为了方便用户选择，让分组都统一采用了枚举类型. edit zhoupeng. 2015.04.28. 注意jflow也要修改.
		map.AddDDLEntities(NodeTeamAttr.FK_Team, null, "用户组", new bp.port.Teams(), true);

		this.set_enMap(map);
		return this.get_enMap();
	}
	@Override
	protected  boolean beforeUpdateInsertAction() throws Exception
	{
		this.setMyPK(this.getFK_Node() + "_" + this.getFK_Team());
		return super.beforeUpdateInsertAction();
	}
	/** 
	 节点岗位发生变化，删除该节点记忆的接收人员。
	 
	 @return 
	*/
	@Override
	protected boolean beforeInsert() throws Exception {
		RememberMe remeberMe = new RememberMe();
		remeberMe.Delete(RememberMeAttr.FK_Node, this.getFK_Node());
		return super.beforeInsert();
	}

		///#endregion

}