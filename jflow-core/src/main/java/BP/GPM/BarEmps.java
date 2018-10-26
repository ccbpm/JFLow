package BP.GPM;

import BP.En.*;

/**
 * 人员信息块s
 * 
 */
public class BarEmps extends EntitiesMyPK {
	/// #region 构造
	/**
	 * 人员信息块s
	 * 
	 */
	public BarEmps() {
	}

	/**
	 * 得到它的 Entity
	 * 
	 */
	@Override
	public Entity getGetNewEntity() {
		return new BarEmp();
	}
	/// #endregion

	public final void InitMyBars() throws Exception {
		Bars bars = new Bars();
		bars.RetrieveAll();
		for (Bar b : bars.ToJavaList()) {
			BarEmp be = new BarEmp();
			be.setMyPK(b.getNo() + "_" + BP.Web.WebUser.getNo());
			if (be.RetrieveFromDBSources() == 1) {
				continue;
			}

			be.setFK_Bar(b.getNo());
			be.setFK_Emp(BP.Web.WebUser.getNo());
			be.setIsShow(true);
			be.setTitle(b.getName());
			be.Insert();
		}
	}

	/**
	 *  为了适应自动翻译成java的需要,把实体转换成List.
	 * 
	 * @return List
	 */
	public final java.util.List<BarEmp> ToJavaList() {
		return (java.util.List<BarEmp>) (Object)this;
	}

	/**
	 * 转化成list
	 * 
	 * @return List
	 */
	public final java.util.ArrayList<BarEmp> Tolist() {
		java.util.ArrayList<BarEmp> list = new java.util.ArrayList<BarEmp>();
		for (int i = 0; i < this.size(); i++) {
			list.add((BarEmp) this.get(i));
		}
		return list;
	}

	/// #endregion 为了适应自动翻译成java的需要,把实体转换成List.
}