package BP.WF.Template;

import BP.DA.*;
import BP.En.*;
import BP.WF.Template.*;
import BP.WF.*;
import BP.Sys.*;
import BP.WF.*;
import java.util.*;

/**
 * 轨迹图标组件s
 */
public class FrmTracks extends Entities {
	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	/// #region 构造
	/**
	 * 轨迹图标组件s
	 */
	public FrmTracks() {
	}

	/**
	 * 轨迹图标组件s
	 * 
	 * @param fk_mapdata
	 *            s
	 * @throws Exception 
	 */
	public FrmTracks(String fk_mapdata) throws Exception {

		this.RetrieveFromCash("No", fk_mapdata);

	}

	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity() {
		return new FrmTrack();
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
	public final List<FrmTrack> ToJavaList() {
		return (List<FrmTrack>) (Object) this;
	}

	/**
	 * 转化成list
	 * 
	 * @return List
	 */
	public final ArrayList<FrmTrack> Tolist() {
		ArrayList<FrmTrack> list = new ArrayList<FrmTrack>();
		for (int i = 0; i < this.size(); i++) {
			list.add((FrmTrack) this.get(i));
		}
		return list;
	}
	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	/// #endregion 为了适应自动翻译成java的需要,把实体转换成List.
}