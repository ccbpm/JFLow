package bp.port;

import bp.da.*;
import bp.difference.SystemConfig;
import bp.en.*;
import bp.sys.*;
import bp.tools.Cryptos;
import bp.tools.Encodes;
import bp.web.WebUser;


import java.util.regex.*;
/**
 Emp 的摘要说明。
*/
public class Emp extends EntityNoName
{

		///#region 扩展属性
	public final String getNo()
	{
		return this.GetValStringByKey(EmpAttr.No);
	}
	public final void setNo(String value)
	 {
		this.SetValByKey(EmpAttr.No, value);
	}
	public final String getPinYin()
	{
		return this.GetValStringByKey(EmpAttr.PinYin);
	}
	public final void setPinYin(String value)
	 {
		this.SetValByKey(EmpAttr.PinYin, value);
	}

	public final int getEmpSta()
	{
		return this.GetValIntByKey(EmpAttr.EmpSta);
	}
	public final void setEmpSta(int value)
	 {
		this.SetValByKey(EmpAttr.EmpSta, value);
	}
	public final String getSID()
	{
		return this.GetValStrByKey(EmpAttr.SID);
	}
	public final void setSID(String value)
	{
		this.SetValByKey(EmpAttr.SID, value);
	}
	/**
	 用户ID:SAAS模式下UserID是可以重复的.
	*/
	public final String getUserID()  {
		if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
		{
			return this.GetValStringByKey(EmpAttr.UserID);
		}

		return this.GetValStringByKey(EmpAttr.No);
	}
	public final void setUserID(String value)
	 {
		this.SetValByKey(EmpAttr.UserID, value);
		if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
		{
			if (value.startsWith(WebUser.getOrgNo() + "_") == true)
			{
				this.SetValByKey(EmpAttr.UserID, value.replace(WebUser.getOrgNo() + "_",""));
				this.SetValByKey(EmpAttr.No, value);
			}
			else
			{
				this.SetValByKey(EmpAttr.No, WebUser.getOrgNo() + "_" + value);
			}
		}
		else
		{
			this.SetValByKey(EmpAttr.No, value);
		}
	}
	/**
	 组织编号
	*/
	public final String getOrgNo()
	{
		return this.GetValStringByKey(EmpAttr.OrgNo);
	}
	public final void setOrgNo(String value)
	 {
		this.SetValByKey(EmpAttr.OrgNo, value);
	}

	/**
	 部门编号
	*/
	public final String getFK_Dept()
	{
		return this.GetValStrByKey(EmpAttr.FK_Dept);
	}
	public final void setFK_Dept(String value)
	 {
		this.SetValByKey(EmpAttr.FK_Dept, value);
	}
	/**
	 部门编号
	*/
	public final String getFK_DeptText()
	{
		return this.GetValRefTextByKey(EmpAttr.FK_Dept);
	}
	/**
	 密码
	*/
	public final String getPass()
	{
		return this.GetValStrByKey(EmpAttr.Pass);
	}
	public final void setIsPass(String value)
	 {
		this.SetValByKey(EmpAttr.Pass, value);
	}
	public final String getToken()
	{
		return this.GetValStrByKey(EmpAttr.Token);
	}
	public final void setToken(String value)
	 {
		this.SetValByKey(EmpAttr.Token, value);
	}
	public final String getTel()
	{
		return this.GetValStrByKey(EmpAttr.Tel);
	}
	public final void setTel(String value)
	 {
		this.SetValByKey(EmpAttr.Tel, value);
	}
	public final String getEmail()
	{
		return this.GetValStrByKey(EmpAttr.Email);
	}
	public final void setEmail(String value)
	 {
		this.SetValByKey(EmpAttr.Email, value);
	}
		///#endregion

		///#region 公共方法
	/**
	 权限管理.
	*/
	@Override
	public UAC getHisUAC()  {
		UAC uac = new UAC();
		uac.OpenForAppAdmin();
		return uac;
	}
	/**
	 检查密码(可以重写此方法)

	 param pass 密码
	 @return 是否匹配成功
	*/
		public final boolean CheckPass(String pass) throws Exception {

		//启用加密
		if (bp.difference.SystemConfig.getIsEnablePasswordEncryption() == true) {
			if(bp.difference.SystemConfig.getPasswordEncryptionType().equals("0"))
				pass= Encodes.encodeBase64(pass);
			if(bp.difference.SystemConfig.getPasswordEncryptionType().equals("1"))
				pass = Cryptos.aesEncrypt(pass);
		}

		if (this.getPass().equals(pass) == true)
		{
			return true;
		}
		return false;
	}



	private static byte[] Keys = {0x12, (byte)0xCD, 0x3F, 0x34, 0x78, (byte)0x90, 0x56, 0x7B};

	/**
	 加密字符串

	 param pass
	 @return
	*/
	/*public static String EncryptPass(String pass)
	{
		DESCryptoServiceProvider descsp = new DESCryptoServiceProvider(); //实例化加/解密类对象


//ORIGINAL LINE: byte[] data = Encoding.Unicode.GetBytes(pass);
		byte[] data = pass.getBytes(java.nio.charset.StandardCharsets.UTF_16LE); //定义字节数组，用来存储要加密的字符串
		ByteArrayOutputStream MStream = new ByteArrayOutputStream(); //实例化内存流对象
		//使用内存流实例化加密流对象
		CryptoStream CStream = new CryptoStream(MStream, descsp.CreateEncryptor(Keys, Keys), CryptoStreamMode.Write);
		CStream.Write(data, 0, data.length); //向加密流中写入数据
		CStream.FlushFinalBlock(); //释放加密流
		return Convert.ToBase64String(MStream.ToArray()); //返回加密后的字符串
	}

	*//**
	 解密字符串

	 param pass
	 @return
	*//*
	public static String DecryptPass(String pass)
	{
		DESCryptoServiceProvider descsp = new DESCryptoServiceProvider(); //实例化加/解密类对象

//ORIGINAL LINE: byte[] data = Convert.FromBase64String(pass);
		byte[] data = Convert.FromBase64String(pass); //定义字节数组，用来存储要解密的字符串
		ByteArrayOutputStream MStream = new ByteArrayOutputStream(); //实例化内存流对象
		//使用内存流实例化解密流对象
		CryptoStream CStream = new CryptoStream(MStream, descsp.CreateDecryptor(Keys, Keys), CryptoStreamMode.Write);
		CStream.Write(data, 0, data.length); //向解密流中写入数据
		CStream.FlushFinalBlock(); //释放解密流
		return Encoding.Unicode.GetString(MStream.ToArray()); //返回解密后的字符串
	}*/

		///#endregion 公共方法


		///#region 构造函数
	/**
	 操作员
	*/
	public Emp() {
	}
	/**
	 操作员

	 param userID 编号
	*/
	public Emp(String userID) throws Exception {
		if (userID == null || userID.length() == 0)
		{
			throw new RuntimeException("@要查询的操作员编号为空。");
		}

		userID = userID.trim();
		if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
		{
			if (userID.equals("admin") == true)
			{
				this.SetValByKey("No", userID);
			}
			else
			{
				this.SetValByKey("No", WebUser.getOrgNo() + "_" + userID);
			}
		}
		else
		{
			this.SetValByKey("No", userID);
		}
		this.Retrieve();
	}
	/**
	 重写基类方法
	*/
	@Override
	public Map getEnMap()  {
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}

		Map map = new Map("Port_Emp", "用户");


			///#region 基本属性
		map.setEnDBUrl(new DBUrl(DBUrlType.AppCenterDSN)); //要连接的数据源（表示要连接到的那个系统数据库）。
		map.IndexField = EmpAttr.FK_Dept;

			///#endregion
			///#region 字段
			/* 关于字段属性的增加 .. */
			//map.IsEnableVer = true;
		map.AddTBStringPK(EmpAttr.No, null, "编号", true, false, 1, 50, 30);

			//如果是集团模式或者是SAAS模式.
		if (bp.difference.SystemConfig.getCCBPMRunModel()  == CCBPMRunModel.SAAS)
			map.AddTBString(EmpAttr.UserID, null, "用户ID", true, false, 0, 50, 30);

		map.AddTBString(EmpAttr.Name, null, "名称", true, false, 0, 200, 30);

		map.AddTBString(EmpAttr.PinYin, null, "拼音", false, false, 0, 200, 30);

		map.AddTBString(EmpAttr.Pass, "123", "密码", false, false, 0, 20, 10);

		map.AddDDLEntities(EmpAttr.FK_Dept, null, "部门", new bp.port.Depts(), false);
		map.AddTBString(EmpAttr.Token, null, "Token", false, false, 0, 36, 36);
		map.AddTBString(EmpAttr.Tel, null, "手机号", false, false, 0, 36, 36);
		map.AddTBString(EmpAttr.Email, null, "邮箱", false, false, 0, 36, 36);

		map.AddTBString(EmpAttr.Leader, null, "直属部门领导", false, false, 0, 20, 130);
		map.SetHelperAlert(EmpAttr.Leader, "这里是领导的登录帐号，不是中文名字，用于流程的接受人规则中。");
		map.AddTBString(EmpAttr.LeaderName, null, "直属部门领导", true, true, 0, 20, 130);

			map.AddTBString(EmpAttr.OrgNo, null, "组织编号", true, true, 0, 50, 50);

		map.AddTBInt(EmpAttr.EmpSta, 0, "EmpSta", false, false);
		map.AddTBInt(EmpAttr.Idx, 0, "Idx", false, false);
		//if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.GroupInc)
		//	map.AddBoolean(EmpAttr.IsOfficer, false, "是否是联络员", true, true);
			///#endregion 字段

		if (bp.difference.SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single && WebUser.getNo().equals("admin")==false)
			map.AddHidden("OrgNo", "=", "@WebUser.OrgNo");

		map.AddSearchAttr(EmpAttr.FK_Dept);

		RefMethod rm = new RefMethod();
		rm.Title = "设置图片签名";
		rm.ClassMethodName = this.toString() + ".DoSinger";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "部门岗位";
		rm.ClassMethodName = this.toString() + ".DoEmpDepts";
		rm.refMethodType = RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

			//节点绑定部门. 节点绑定部门.
		//map.getAttrsOfOneVSM().AddBranches(new DeptEmps(), new Depts(), bp.port.DeptEmpAttr.FK_Emp, bp.port.DeptEmpAttr.FK_Dept, "部门维护", EmpAttr.Name, EmpAttr.No, "@WebUser.FK_Dept");

			//用户组
		//map.getAttrsOfOneVSM().Add(new TeamEmps(), new Teams(), TeamEmpAttr.FK_Emp, TeamEmpAttr.FK_Team, TeamAttr.Name, TeamAttr.No, "用户组", Dot2DotModel.Default);

		rm = new RefMethod();
		rm.Title = "修改密码";
		rm.ClassMethodName = this.toString() + ".DoResetpassword";
		rm.getHisAttrs().AddTBString("pass1", null, "输入密码", true, false, 0, 100, 100);
		rm.getHisAttrs().AddTBString("pass2", null, "再次输入", true, false, 0, 100, 100);
		map.AddRefMethod(rm);

		//平铺模式.
		try {
			map.getAttrsOfOneVSM().AddGroupPanelModel(new TeamEmps(), new Teams(),
					TeamEmpAttr.FK_Emp,
					TeamEmpAttr.FK_Team, "用户组", TeamAttr.FK_TeamType);
		} catch (Exception e) {
			e.printStackTrace();
		}

		rm = new RefMethod();
		rm.Title = "修改主部门";
		rm.ClassMethodName = this.toString() + ".DoEditMainDept";
		rm.RefAttrKey = EmpAttr.FK_Dept;
		rm.refMethodType = RefMethodType.LinkModel;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "设置部门直属领导";
		rm.ClassMethodName = this.toString() + ".DoEditLeader";
		rm.RefAttrKey = EmpAttr.LeaderName;
		rm.refMethodType = RefMethodType.LinkModel;
		map.AddRefMethod(rm);

		this.set_enMap(map);
		return this.get_enMap();
	}

	public final String DoEditMainDept()  {
		return bp.difference.SystemConfig.getCCFlowWebPath() + "GPM/EmpDeptMainDept.htm?FK_Emp=" + this.getNo() + "&FK_Dept=" + this.getFK_Dept();
	}

	public final String DoEditLeader()  {
		return bp.difference.SystemConfig.getCCFlowWebPath() + "GPM/EmpLeader.htm?FK_Emp=" + this.getNo() + "&FK_Dept=" + this.getFK_Dept();
	}

	public final String DoEmpDepts()  {
		return bp.difference.SystemConfig.getCCFlowWebPath() + "GPM/EmpDepts.htm?FK_Emp=" + this.getNo();
	}

	public final String DoSinger()  {
		//路径
		return bp.difference.SystemConfig.getCCFlowWebPath() + "GPM/Siganture.htm?EmpNo=" + this.getNo();
	}
	@Override
	protected boolean beforeInsert() throws Exception {
		if (bp.difference.SystemConfig.getIsEnablePasswordEncryption() == true)
		{
			this.setIsPass(Encodes.encodeBase64(this.getPass()));
		}
		return super.beforeInsert();
	}

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception {
		if (DataType.IsNullOrEmpty(this.getOrgNo()) == true)
			this.setOrgNo(WebUser.getOrgNo());
		//增加拼音，以方便查找.
		if (DataType.IsNullOrEmpty(this.getName()) == true)
			throw new RuntimeException("名称不能为空.");

		if (WebUser.getIsAdmin() == false && this.getNo().equals(WebUser.getNo())==false)
		{
			throw new RuntimeException("非管理员无法操作.");
		}

		if (DataType.IsNullOrEmpty(this.getEmail()) == false)
		{
			//邮箱格式

			if (Pattern.matches("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$",
					this.getEmail()) == false)
			{
				throw new RuntimeException("邮箱格式不正确.");
			}
		}

		//设置orgNo.
		if (bp.difference.SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single && DataType.IsNullOrEmpty(this.getFK_Dept())==false)
		{

			Dept dept = new Dept();
			dept.setNo(this.getFK_Dept());
			dept.RetrieveFromDBSources();
			this.setOrgNo(dept.getOrgNo());
		}

		String pinyinQP = DataType.ParseStringToPinyin(this.getName()).toLowerCase();
		String pinyinJX = DataType.ParseStringToPinyinJianXie(this.getName()).toLowerCase();
		this.setPinYin("," + pinyinQP + "," + pinyinJX + ",");

		//处理岗位信息.
		DeptEmpStations des = new DeptEmpStations();
		des.Retrieve(DeptEmpStationAttr.FK_Emp, this.getNo());

		String depts = "";
		String stas = "";
		for (DeptEmpStation item : des.ToJavaList())
		{
			Dept dept = new Dept();
			dept.setNo(item.getFK_Dept());
			if (dept.RetrieveFromDBSources() == 0)
			{
				item.Delete();
				continue;
			}

			//给拼音重新定义值,让其加上部门的信息.
			this.setPinYin(this.getPinYin() + pinyinJX + "/" + DataType.ParseStringToPinyinJianXie(dept.getName()).toLowerCase() + ",");

			bp.port.Station sta = new bp.port.Station();
			sta.setNo(item.getFK_Station());
			if (sta.RetrieveFromDBSources() == 0)
			{
				item.Delete();
				continue;
			}

			stas += "@" + dept.getNameOfPath() + "|" + sta.getName();
			depts += "@" + dept.getNameOfPath();
		}
		return super.beforeUpdateInsertAction();
	}
	@Override
	protected boolean beforeDelete() throws Exception {
		if (this.getNo().toLowerCase().equals("admin") == true)
			throw new RuntimeException("err@管理员账号不能删除.");
		if(SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single){
			//如果是组织的主要管理员时不能删除
			String sql="SELECT COUNT(*) From Port_Org WHERE No='"+WebUser.getOrgNo()+"' AND Adminer='"+this.getNo()+"'";
			if(DBAccess.RunSQLReturnValInt(sql)==1)
				throw new RuntimeException("err@组织"+WebUser.getOrgName()+"的主要管理员账号不能删除.");
		}
		return super.beforeDelete();
	}

	/**
	 保存后修改WF_Emp中的邮箱
	*/
	@Override
	protected void afterInsertUpdateAction() throws Exception {
		String sql = "Select Count(*) From WF_Emp Where No='" + this.getNo() + "'";
		int count = DBAccess.RunSQLReturnValInt(sql);
		if (count == 0)
		{
			sql = "INSERT INTO WF_Emp (No,Name,Email) VALUES('" + this.getNo() + "','" + this.getName() + "','" + this.getEmail() + "')";
		}
		else
		{
			sql = "UPDATE WF_Emp SET Email='" + this.getEmail() + "'";
		}
		DBAccess.RunSQL(sql);

		DeptEmp deptEmp = new DeptEmp();
		deptEmp.setFK_Dept(this.getFK_Dept());
		deptEmp.setFK_Emp(this.getNo());
		deptEmp.setMyPK(this.getFK_Dept() + "_" + this.getNo());
		if (bp.difference.SystemConfig.getCCBPMRunModel() != bp.sys.CCBPMRunModel.Single)
		{
			deptEmp.setOrgNo(this.getOrgNo());
		}
		if (deptEmp.IsExit("MyPK", deptEmp.getMyPK()) == false)
		{
			deptEmp.Insert();
		}


		super.afterInsertUpdateAction();
	}
	/**
	 删除之后要做的事情
	*/
	@Override
	protected void afterDelete() throws Exception {
		DeptEmps des = new DeptEmps();
		des.Delete(DeptEmpAttr.FK_Emp, this.getNo());

		DeptEmpStations stas = new DeptEmpStations();
		stas.Delete(DeptEmpAttr.FK_Emp, this.getNo());

		super.afterDelete();
	}

	public static String GenerPinYin(String no, String name) throws Exception {
		//增加拼音，以方便查找.
		String pinyinQP = DataType.ParseStringToPinyin(name).toLowerCase();
		String pinyinJX = DataType.ParseStringToPinyinJianXie(name).toLowerCase();
		String py = "," + pinyinQP + "," + pinyinJX + ",";

		//处理岗位信息.
		DeptEmpStations des = new DeptEmpStations();
		des.Retrieve(DeptEmpStationAttr.FK_Emp, no);

		String depts = "";
		String stas = "";

		for (DeptEmpStation item : des.ToJavaList())
		{
			Dept dept = new Dept();
			dept.setNo(item.getFK_Dept());
			if (dept.RetrieveFromDBSources() == 0)
			{
				item.Delete();
				continue;
			}

			//给拼音重新定义值,让其加上部门的信息.
			py = py + pinyinJX + "/" + DataType.ParseStringToPinyinJianXie(dept.getName()).toLowerCase() + ",";

			bp.port.Station sta = new bp.port.Station();
			sta.setNo(item.getFK_Station());
			if (sta.RetrieveFromDBSources() == 0)
			{
				item.Delete();
				continue;
			}

			stas += "@" + dept.getNameOfPath() + "|" + sta.getName();
			depts += "@" + dept.getNameOfPath();
		}

		return py;
	}

	/**
	 向上移动
	*/
	public final void DoUp()  {
		this.DoOrderUp(EmpAttr.FK_Dept, this.getFK_Dept(), EmpAttr.Idx);
	}
	/**
	 向下移动
	*/
	public final void DoDown()  {
		this.DoOrderDown(EmpAttr.FK_Dept, this.getFK_Dept(), EmpAttr.Idx);
	}

	public final String DoResetpassword(String pass1, String pass2) throws Exception {
		if (pass1.equals(pass2) == false)
		{
			return "两次密码不一致";
		}

		this.setIsPass(pass1);

		if (bp.difference.SystemConfig.getIsEnablePasswordEncryption() == true)
		{
			this.setIsPass(Encodes.encodeBase64(this.getPass()));
		}

		this.Update();
		return "密码设置成功";
	}


	/**
	 获取集合
	*/
	@Override
	public Entities getGetNewEntities() {
		return new Emps();
	}

		///#endregion 构造函数






		///#region 方法测试代码.
	/**
	 禁用、启用用户

	 @return
	*/
	public final String DoUnEnable() throws Exception {
		String userNo = this.getNo();
		if(userNo.equals("admin"))
			throw new RuntimeException("err@管理员账号不能禁用.");
		if(SystemConfig.getCCBPMRunModel() != CCBPMRunModel.Single){
			//如果是组织的主要管理员时不能删除
			String sql="SELECT COUNT(*) From Port_Org WHERE No='"+WebUser.getOrgNo()+"' AND Adminer='"+this.getNo()+"'";
			if(DBAccess.RunSQLReturnValInt(sql)==1)
				throw new RuntimeException("err@组织"+WebUser.getOrgName()+"的主要管理员账号不能禁用.");
		}
		//判断当前人员是否有待办
		String wfSql = "";
		if (bp.difference.SystemConfig.getCCBPMRunModel() == CCBPMRunModel.SAAS)
		{
			wfSql = " AND OrgNo='" + WebUser.getOrgNo() + "'";
			userNo = this.getUserID();
		}
		String sql = "";
		/*不是授权状态*/
		if (bp.difference.SystemConfig.GetValByKeyBoolen("IsEnableTaskPool", false) == true)
		{
			sql = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE FK_Emp='" + userNo + "' AND TaskSta!=1 " + wfSql;
		}
		else
		{
			sql = "SELECT count(WorkID) as Num FROM WF_EmpWorks WHERE  FK_Emp='" + userNo + "' " + wfSql;
		}

		int count = DBAccess.RunSQLReturnValInt(sql);
		if (count != 0)
		{
			return this.getName() + "还存在待办，不能禁用该用户";
		}
		sql = "UPDATE WF_Emp SET UseSta=0 WHERE No='" + this.getNo() + "'";
		DBAccess.RunSQL(sql);
		//this.Delete();
		this.setEmpSta(1);
		this.Update();
		return this.getName() + "已禁用";

	}

	/**
	 测试

	 @return
	*/
	public final String ResetIsPass()  {
		return "执行成功.";
	}
	/**
	 ChangePass

	 param oldpass
	 param pass1
	 param pass2
	 @return
	*/
	public final String ChangePass(String oldpass, String pass1, String pass2)
	{
		if (WebUser.getNo().equals(this.getUserID()) == false)
		{
			return "err@sss";
		}

		return "执行成功.";
	}


		///#endregion 方法测试代码.

}