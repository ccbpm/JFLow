package bp.port;

import bp.en.*;
import java.util.*;

/**
 * 用户组人员s
 */
public class TeamEmps extends EntitiesMM {

	private static final long serialVersionUID = 1L;

	/// 构造
	/**
	 * 用户组s
	 */
	public TeamEmps() {
	}

	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity() {
		return new TeamEmp();
	}

	///

	/// 为了适应自动翻译成java的需要,把实体转换成List.
	/**
	 * 转化成 java list,C#不能调用.
	 * 
	 * @return List
	 */
	public final java.util.List<TeamEmp> ToJavaList() {
		return (java.util.List<TeamEmp>)(Object)this;
	}

	/**
	 * 转化成list
	 * 
	 * @return List
	 */
	public final ArrayList<TeamEmp> Tolist() {
		ArrayList<TeamEmp> list = new ArrayList<TeamEmp>();
		for (int i = 0; i < this.size(); i++) {
			list.add((TeamEmp) this.get(i));
		}
		return list;
	}

	/// 为了适应自动翻译成java的需要,把实体转换成List.
}