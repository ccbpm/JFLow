package BP.GPM;

public abstract class BarBase
{
	/** 
	 编号
	 
	*/
	public abstract String getNo();
	/** 
	 名称
	 
	*/
	public abstract String getName();
	/** 
	 权限控制-是否可以查看
	 
	*/
	public abstract boolean getIsCanView();

		
	/** 
	 标题
	 
	*/
	public abstract String getTitle();
	/** 
	 更多连接
	 
	*/
	public abstract String getMore();
	/** 
	 内容信息
	 
	*/
	public abstract String getDocuments();
	/** 
	 宽度
	 
	*/
	public abstract String getWidth();
	/** 
	 高度
	 
	*/
	public abstract String getHeight();
		///#endregion 外观行为.

}