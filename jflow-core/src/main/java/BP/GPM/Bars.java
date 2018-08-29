package BP.GPM;

import java.util.ArrayList;

import BP.En.*;

/**
 * 信息块s
 * 
 */
public class Bars extends EntitiesNoName {
	/// #region 构造
	/**
	 * 信息块s
	 * 
	 */
	public Bars() {
	}

	/**
	 * 得到它的 Entity
	 * 
	 */
	@Override
	public Entity getGetNewEntity() {
		return new Bar();
	}

	/**
	 * 为了适应自动翻译成java的需要,把实体转换成List.
	 * 
	 * @return List
	 */
	public final java.util.List<Bar> ToJavaList() {
		return (java.util.List<Bar>) (Object)this;
	}

	/**
	 * 转化成list
	 * 
	 * @return List
	 */
	public final java.util.ArrayList<Bar> Tolist() {
		java.util.ArrayList<Bar> list = new java.util.ArrayList<Bar>();
		for (int i = 0; i < this.size(); i++) {
			list.add((Bar) this.get(i));
		}
		return list;
	}
	/// #endregion 为了适应自动翻译成java的需要,把实体转换成List.

	@Override
	public int RetrieveAll() throws Exception {
		// 初始化数据到，表里面去.
		ArrayList als = ClassFactory.GetObjects("BP.GPM.BarBase");

		for (Object item : als) {
			if (item == null) {
				continue;
			}

			BarBase en = (BarBase) item;
			if (en == null) {
				continue;
			}

			Bar bar = new Bar();
			bar.setNo(en.getNo());
			bar.setName(en.getName());
			bar.setTitle(en.getTitle());
			bar.setMoreUrl(en.getMore());
			bar.setHeight(en.getHeight());
			bar.setWidth(en.getWidth());
			bar.Save();

			if (en.getIsCanView() == false) {
				continue;
			}

			BarEmp barEmp = new BarEmp();
			barEmp.setMyPK(en.getNo() + "_" + BP.Web.WebUser.getNo());
			int i = barEmp.RetrieveFromDBSources();

			barEmp.setFK_Bar(en.getNo());
			barEmp.setFK_Emp(BP.Web.WebUser.getNo());
			barEmp.setIsShow(true);
			barEmp.setTitle(en.getName());
			if (i == 0) {
				barEmp.Insert();
			}

		}
		return super.RetrieveAll();
	}
}