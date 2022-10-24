package bp.wf.port.admin2group;

import bp.da.*;
import bp.en.*;
import bp.tools.Encodes;

/** 
 操作员 的摘要说明。
*/
public class User extends EntityNoName
{

		///#region 扩展属性

		public final String getUnionid() throws Exception {
			return this.GetValStrByKey(UserAttr.unionid);
		}
		public final void setUnionid(String value)throws Exception
	{this.SetValByKey(UserAttr.unionid, value);
		}
	public final String getOpenID() throws Exception
	{
		return this.GetValStrByKey(UserAttr.OpenID);
	}
	public final void setOpenID(String value)  throws Exception
	 {
		this.SetValByKey(UserAttr.OpenID, value);
	}
	public final String getOpenID2() throws Exception
	{
		return this.GetValStrByKey(UserAttr.OpenID2);
	}
	public final void setOpenID2(String value)  throws Exception
	 {
		this.SetValByKey(UserAttr.OpenID2, value);
	}

	public final String getSID() throws Exception
	{
		return this.GetValStrByKey(UserAttr.SID);
	}
	public final void setSID(String value)  throws Exception
	 {
		this.SetValByKey(UserAttr.SID, value);
	}
	/** 
	 拼音
	*/
	public final String getPinYin() throws Exception
	{
		return this.GetValStrByKey(UserAttr.PinYin);
	}
	public final void setPinYin(String value)  throws Exception
	 {
		this.SetValByKey(UserAttr.PinYin, value);
	}
	/** 
	 主要的部门。
	*/
	public final Dept getHisDept() throws Exception {
		try
		{
			return new Dept(this.getFK_Dept());
		}
		catch (RuntimeException ex)
		{
			throw new RuntimeException("@获取操作员" + this.getNo() + "部门[" + this.getFK_Dept() + "]出现错误,可能是系统管理员没有给他维护部门.@" + ex.getMessage());
		}
	}
	/** 
	 部门
	*/
	public final String getFK_Dept() throws Exception
	{
		return this.GetValStrByKey(UserAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)  throws Exception
	 {
		this.SetValByKey(UserAttr.FK_Dept, value);
	}
	public final String getFK_DeptText() throws Exception
	{
		return this.GetValRefTextByKey(UserAttr.FK_Dept);
	}
	public final String getTel() throws Exception
	{
		return this.GetValStrByKey(UserAttr.Tel);
	}
	public final void setTel(String value)  throws Exception
	 {
		this.SetValByKey(UserAttr.Tel, value);
	}
	public final String getEmail() throws Exception
	{
		return this.GetValStrByKey(UserAttr.Email);
	}
	public final void setEmail(String value)  throws Exception
	 {
		this.SetValByKey(UserAttr.Email, value);
	}
	/** 
	 密码
	*/
	public final String getPass() throws Exception
	{
		return this.GetValStrByKey(UserAttr.Pass);
	}
	public final void setIsPass(String value)  throws Exception
	 {
		this.SetValByKey(UserAttr.Pass, value);
	}
	/** 
	 顺序号
	*/
	public final int getIdx() throws Exception
	{
		return this.GetValIntByKey(UserAttr.Idx);
	}
	public final void setIdx(int value)  throws Exception
	 {
		this.SetValByKey(UserAttr.Idx, value);
	}
	/** 
	 组织结构编码
	*/
	public final String getOrgNo() throws Exception
	{
		return this.GetValStrByKey(UserAttr.OrgNo);
	}
	public final void setOrgNo(String value)  throws Exception
	 {
		this.SetValByKey(UserAttr.OrgNo, value);
	}
	public final String getOrgName() throws Exception
	{
		return this.GetValStrByKey(UserAttr.OrgName);
	}
	public final void setOrgName(String value)  throws Exception
	 {
		this.SetValByKey(UserAttr.OrgName, value);
	}

		///#endregion


		///#region 公共方法
	/** 
	 检查密码(可以重写此方法)
	 
	 param pass 密码
	 @return 是否匹配成功
	*/
	public final boolean CheckPass(String pass) throws Exception {
		//启用加密
		if (bp.difference.SystemConfig.getIsEnablePasswordEncryption())
		{
			pass = Encodes.encodeBase64(pass);
		}

		/*使用数据库校验.*/
		if (this.getPass().equals(pass) == true)
		{
			return true;
		}
		return false;
	}

		///#endregion 公共方法


		///#region 构造函数
	/** 
	 操作员
	*/
	public User()  {
	}
	/** 
	 操作员
	 
	 param no 编号
	*/
	public User(String no) throws Exception {
		try
		{
			this.setNo(no);
			this.Retrieve();
		}
		catch (RuntimeException ex)
		{
			int i = this.RetrieveFromDBSources();
			if (i == 0)
			{
				throw new RuntimeException("err@用户账号[" + this.getNo() + "]错误:" + ex.getMessage());
			}
			throw ex;
		}
	}

	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();
		if (bp.web.WebUser.getIsAdmin() == true)
		{
			uac.OpenAll();
		}
		else
		{
			uac.Readonly();
		}
		return uac;
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

		Map map = new Map("Port_User", "用户");
		map.setEnType(EnType.App);
		map.IndexField = UserAttr.FK_Dept;


			///#region 字段。
			/*关于字段属性的增加 */
		map.AddTBStringPK(UserAttr.No, null, "手机号/ID", true, false, 1, 150, 90);
		map.AddTBString(UserAttr.Name, null, "姓名", true, false, 0, 500, 130);
		map.AddTBString(UserAttr.Pass, null, "密码", false, false, 0, 100, 10);
		map.AddTBString(UserAttr.FK_Dept, null, "部门", false, false, 0, 100, 10);
		map.AddTBString(UserAttr.SID, null, "Token", false, false, 0, 36, 36);
		map.AddTBString(UserAttr.Tel, null, "电话", true, false, 0, 20, 130);
		map.AddTBString(UserAttr.Email, null, "邮箱", true, false, 0, 100, 132, true);
		map.AddTBString(UserAttr.PinYin, null, "拼音", true, false, 0, 1000, 132, true);

		map.AddTBString(UserAttr.OrgNo, null, "OrgNo", true, false, 0, 500, 132, true);
		map.AddTBString(UserAttr.OrgName, null, "OrgName", true, false, 0, 500, 132, true);
		map.AddTBString(UserAttr.unionid, null, "unionid", true, false, 0, 500, 132, true);
		map.AddTBString(UserAttr.OpenID, null, "小程序的OpenID", true, false, 0, 500, 132, true);
		map.AddTBString(UserAttr.OpenID2, null, "公众号的OpenID", true, false, 0, 500, 132, true);


		map.AddTBInt(UserAttr.Idx, 0, "序号", true, false);

			///#endregion 字段

		this.set_enMap(map);
		return this.get_enMap();
	}


	@Override
	protected boolean beforeInsert() throws Exception {
		if (bp.difference.SystemConfig.getIsEnablePasswordEncryption() == true)
		{
			if (this.getPass().equals(""))
			{
				this.setIsPass("123");
			}
			this.setIsPass(Encodes.encodeBase64(this.getPass()));
		}
		return super.beforeInsert();
	}

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception {
		//增加拼音，以方便查找.
		if (DataType.IsNullOrEmpty(this.getName()) == true)
		{
			throw new RuntimeException("err@名称不能为空.");
		}

		String pinyinQP = DataType.ParseStringToPinyin(this.getName()).toLowerCase();
		String pinyinJX = DataType.ParseStringToPinyinJianXie(this.getName()).toLowerCase();
		this.setPinYin("," + pinyinQP + "," + pinyinJX + ",");
		return super.beforeUpdateInsertAction();
	}
	@Override
	protected boolean beforeDelete() throws Exception {
		if (!this.getOrgNo().equals(bp.web.WebUser.getOrgNo()))
		{
			throw new RuntimeException("err@您不能删除别人的数据.");
		}

		return super.beforeDelete();
	}
	/** 
	 删除之后要做的事情
	*/
	@Override
	protected void afterDelete() throws Exception {
		super.afterDelete();
	}

	public static String GenerPinYin(String no, String name)
	{
		//增加拼音，以方便查找.
		String pinyinQP = DataType.ParseStringToPinyin(name).toLowerCase();
		String pinyinJX = DataType.ParseStringToPinyinJianXie(name).toLowerCase();
		String py = "," + pinyinQP + "," + pinyinJX + ",";

		////处理岗位信息.
		//DeptUserStations des = new DeptUserStations();
		//des.Retrieve(DeptUserStationAttr.FK_User, no);

		//string depts = "";
		//string stas = "";

		//foreach (DeptUserStation item in des)
		//{
		//    bp.port.Dept dept = new bp.port.Dept();
		//    dept.No = item.FK_Dept;
		//    if (dept.RetrieveFromDBSources() == 0)
		//    {
		//        item.Delete();
		//        continue;
		//    }

		//    //给拼音重新定义值,让其加上部门的信息.
		//    py = py + pinyinJX + "/" + bp.da.DataType.ParseStringToPinyinJianXie(dept.getName()).ToLower() + ",";

		//    bp.port.Station sta = new Port.Station();
		//    sta.No = item.FK_Station;
		//    if (sta.RetrieveFromDBSources() == 0)
		//    {
		//        item.Delete();
		//        continue;
		//    }

		//    stas += "@" + dept.NameOfPath + "|" + sta.Name;
		//    depts += "@" + dept.NameOfPath;
		//}

		return py;
	}

	/** 
	 向上移动
	*/
	public final String DoUp() throws Exception {
		this.DoOrderUp(UserAttr.FK_Dept, this.getFK_Dept(), UserAttr.Idx);
		return "执行成功.";
	}
	/** 
	 向下移动
	*/
	public final String DoDown() throws Exception {
		this.DoOrderDown(UserAttr.FK_Dept, this.getFK_Dept(), UserAttr.Idx);
		return "执行成功.";
	}

	public final String DoResetpassword(String pass1, String pass2) throws Exception {
		if (pass1.equals(pass2) == false)
		{
			return "两次密码不一致";
		}

		if (bp.difference.SystemConfig.getIsEnablePasswordEncryption() == true)
		{
			pass1 = Encodes.encodeBase64(pass1);
		}

		this.setIsPass(pass1);

		this.Update();
		return "密码设置成功";
	}
	/** 
	 获取集合
	*/

	public Entities getNewEntities() throws Exception {
		return new Users();
	}

		///#endregion 构造函数
}