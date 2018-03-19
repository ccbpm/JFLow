package BP.En;

/**
 * 按钮类型
 */
public enum BtnType
{
	/**
	 * 确认 ,需要给　hit 赋值。 ＸＸＸ　确认马？
	 */
	ConfirmHit,
	/**
	 * 正常
	 */
	Normal,
	/**
	 * 确定
	 */
	Confirm,
	/**
	 * 保存
	 */
	Save,
	/**
	 * 保存并新建
	 */
	SaveAndNew,
	/**
	 * 查找
	 */
	Search,
	/**
	 * 取消
	 */
	Cancel,
	/**
	 * 删除
	 */
	Delete,
	/**
	 * 更新
	 */
	Update,
	/**
	 * 插入
	 */
	Insert,
	/**
	 * 编辑
	 */
	Edit,
	/**
	 * 新建
	 */
	New,
	/**
	 * 浏览
	 */
	View,
	/**
	 * 关闭
	 */
	Close,
	/**
	 * 导出
	 */
	Export,
	/**
	 * 打印
	 */
	Print,
	/**
	 * 增加
	 */
	Add,
	/**
	 * 一处
	 */
	Reomve,
	/**
	 * 返回
	 */
	Back,
	/**
	 * 刷新
	 */
	Refurbish,
	/**
	 * 申请任务
	 */
	ApplyTask,
	/**
	 * 选者全部
	 */
	SelectAll,
	/**
	 * 全不选
	 */
	SelectNone;
	
	public int getValue()
	{
		return this.ordinal();
	}
	
	public static BtnType forValue(int value)
	{
		return values()[value];
	}
}