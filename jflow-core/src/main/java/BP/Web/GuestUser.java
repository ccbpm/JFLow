package BP.Web;

/**
 * User 的摘要说明。
 */
public class GuestUser
{
	/**
	 * 通用的登录
	 * 
	 * @param guestNo
	 * @param guestName
	 * @param lang
	 * @param isRememberMe
	 */
	public static void SignInOfGener(String guestNo, String guestName,
			String lang, boolean isRememberMe)
	{
		SignInOfGener(guestNo, guestName, "deptNo", "deptName", lang,
				isRememberMe);
	}
	
	/**
	 * 通用的登录
	 * 
	 * @param guestNo
	 *            客户编号
	 * @param guestName
	 *            客户名称
	 * @param deptNo
	 *            部门编号
	 * @param deptName
	 *            部门名称
	 * @param lang
	 *            语言
	 * @param isRememberMe
	 *            是否记忆我
	 */
	public static void SignInOfGener(String guestNo, String guestName,
			String deptNo, String deptName, String lang, boolean isRememberMe)
	{
		return; // 本期不处理
		// 可以在第一期不处理
	}
	
	/**
	 * 退回
	 */
	public static void Exit()
	{
	}
	
	/**
	 * 编号
	 */
	public static String getNo()
	{
		return "123"; // 本期不处理.
	}
	
	public static void setNo(String value)
	{
		// SetSessionByKey("GuestNo", value);
	}
	
	/**
	 * 名称
	 */
	public static String getName()
	{
		return "123"; // 本期不处理.
	}
	
	public static void setName(String value)
	{
		// SetSessionByKey("GuestName", value);
	}
}
