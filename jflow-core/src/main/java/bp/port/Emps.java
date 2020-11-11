package bp.port;

import bp.da.*;
import bp.en.*;
import bp.sys.*;
import bp.*;
import java.util.*;
import java.io.*;

/**
 * 操作员
 */
// </summary>
public class Emps extends EntitiesNoName {

	private static final long serialVersionUID = 1L;

	/// 构造方法
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity() {
		return new Emp();
	}

	/**
	 * 操作员s
	 */
	public Emps() {
	}

	/**
	 * 操作员s
	 * @throws Exception 
	 */
	public Emps(String deptNo) throws Exception {

		this.Retrieve(EmpAttr.FK_Dept, deptNo);

	}

	/// 构造方法

	public final String reseet() {
		return "ceshi";
	}

	/// 重写查询,add by stone 2015.09.30 为了适应能够从 webservice 数据源查询数据.
	/**
	 * 重写查询全部适应从WS取数据需要
	 * 
	 * @return
	 * @throws Exception 
	 */
	@Override
	public int RetrieveAll() throws Exception {
	
		return super.RetrieveAll();

	}

	/**
	 * 重写重数据源查询全部适应从WS取数据需要
	 * 
	 * @return
	 * @throws Exception 
	 */
	@Override
	public int RetrieveAllFromDBSource() throws Exception {

		return super.RetrieveAllFromDBSource();

	}

	/// 重写查询.

	/// 为了适应自动翻译成java的需要,把实体转换成List.
	/**
	 * 转化成 java list,C#不能调用.
	 * 
	 * @return List
	 */
	public final java.util.List<Emp> ToJavaList() {
		return (java.util.List<Emp>)(Object) this;
	}

	/**
	 * 转化成list
	 * 
	 * @return List
	 */
	public final ArrayList<Emp> Tolist() {
		ArrayList<Emp> list = new ArrayList<Emp>();
		for (int i = 0; i < this.size(); i++) {
			list.add((Emp) this.get(i));
		}
		return list;
	}

	/// 为了适应自动翻译成java的需要,把实体转换成List.

}