package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.Template.*;
import BP.WF.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/**
 * 子线程组件s
 */
public class FrmThreads extends Entities {
	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	/// #region 构造
	/**
	 * 子线程组件s
	 */
	public FrmThreads() {
	}

	 
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getNewEntity() {
		return new FrmThread();
	}
	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	/// #endregion

	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	/// #region 为了适应自动翻译成java的需要,把实体转换成List
	/**
	 * 转化成 java list,C#不能调用.
	 * 
	 * @return List
	 */
	public final List<FrmThread> ToJavaList() {
		return (List<FrmThread>) (Object) this;
	}

	/**
	 * 转化成list
	 * 
	 * @return List
	 */
	public final ArrayList<FrmThread> Tolist() {
		ArrayList<FrmThread> list = new ArrayList<FrmThread>();
		for (int i = 0; i < this.size(); i++) {
			list.add((FrmThread) this.get(i));
		}
		return list;
	}
	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	/// #endregion 为了适应自动翻译成java的需要,把实体转换成List.
}