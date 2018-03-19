package BP.WF;


/** 
 钉钉主类
*/
public class DingDing
{
//	private String corpid = BP.Sys.SystemConfig.Ding_CorpID;
//	private String corpsecret = BP.Sys.SystemConfig.Ding_CorpSecret;
//
//	public final String getAccessToken()
//	{
//		String accessToken = "";
//		String url = "https://oapi.dingtalk.com/gettoken?corpid=" + corpid + "&corpsecret=" + corpsecret;
//		try
//		{
//			String str = (new HttpWebResponseUtility()).HttpResponseGet(url);
//			AccessToken AT = new AccessToken();
//			AT = FormatToJson.<AccessToken>ParseFromJson(str);
//			if (AT != null)
//			{
//				accessToken = AT.getaccess_token();
//			}
//		}
//		catch (RuntimeException ex)
//		{
//			BP.DA.Log.DefaultLogWriteLineError(ex.getMessage());
//		}
//		return accessToken;
//	}
//
//	/** 
//	 获取用户ID
//	 
//	 @param code
//	 @param accessToken
//	 @return 
//	*/
//	public final String GetUserID(String code)
//	{
//		String access_token = getAccessToken();
//		String url = "https://oapi.dingtalk.com/user/getuserinfo?access_token=" + access_token + "&code=" + code;
//		try
//		{
//			String str = (new HttpWebResponseUtility()).HttpResponseGet(url);
//			CreateUser_PostVal user = new CreateUser_PostVal();
//			user = FormatToJson.<CreateUser_PostVal>ParseFromJson(str);
//			//BP.DA.Log.DefaultLogWriteLineError(access_token + "1." + user.userid + "2." + user.errcode + "3." + user.errmsg);
//			if (!tangible.StringHelper.isNullOrEmpty(user.getuserid()))
//			{
//				return user.getuserid();
//			}
//		}
//		catch (RuntimeException ex)
//		{
//			BP.DA.Log.DefaultLogWriteLineError(ex.getMessage());
//		}
//		return "";
//	}
//
//	/** 
//	 同步钉钉通讯录到CCGPM
//	 
//	 @return 
//	*/
//	public final boolean AnsyOrgToCCGPM()
//	{
//		String access_token = getAccessToken();
//		String url = "https://oapi.dingtalk.com/department/list?access_token=" + access_token;
//		try
//		{
//			String str = (new HttpWebResponseUtility()).HttpResponseGet(url);
//			DepartMent_List departMentList = FormatToJson.<DepartMent_List>ParseFromJson(str);
//			//部门集合
//			if (departMentList != null && departMentList.getdepartment() != null && departMentList.getdepartment().size() > 0)
//			{
//				//删除旧数据
//				ClearOrg_Old();
//				//获取根部门
//				DepartMentDetailInfo rootDepartMent = new DepartMentDetailInfo();
//				for (DepartMentDetailInfo deptMenInfo : departMentList.getdepartment())
//				{
//					if (deptMenInfo.getid().equals("1"))
//					{
//						rootDepartMent = deptMenInfo;
//						break;
//					}
//				}
//				//增加跟部门
//				int deptIdx = 0;
//				Dept rootDept = new Dept();
//				rootDept.No = rootDepartMent.getid();
//				rootDept.Name = rootDepartMent.getname();
//				rootDept.ParentNo = "0";
//				rootDept.Idx = deptIdx;
//				rootDept.DirectInsert();
//
//
//				//部门信息
//				for (DepartMentDetailInfo deptMentInfo : departMentList.getdepartment())
//				{
//					//增加部门,排除根目录
//					if (!deptMentInfo.getid().equals("1"))
//					{
//						Dept dept = new Dept();
//						dept.No = deptMentInfo.getid();
//						dept.Name = deptMentInfo.getname();
//						dept.ParentNo = deptMentInfo.getparentid();
//						dept.Idx = deptIdx++;
//						dept.DirectInsert();
//					}
//
//					//部门人员
//					DepartMentUser_List userList = GenerDeptUser_List(access_token, deptMentInfo.getid());
//					if (userList != null)
//					{
//						for (DepartMentUserInfo userInfo : userList.getuserlist())
//						{
//							Emp emp = new Emp();
//							DeptEmp deptEmp = new DeptEmp();
//							//如果账户存在则人员信息不添加，添加关联表
//							if (emp.IsExit(EmpAttr.No, userInfo.getuserid()) == true)
//							{
//								deptEmp.MyPK = deptMentInfo.getid() + "_" + emp.getNo();
//								deptEmp.FK_Dept = deptMentInfo.getid();
//								deptEmp.FK_Emp = emp.getNo();
//								deptEmp.DirectInsert();
//								continue;
//							}
//
//							//增加人员
//							emp.getNo() = userInfo.getuserid();
//							emp.EmpNo = userInfo.getjobnumber();
//							emp.Name = userInfo.getname();
//							emp.FK_Dept = deptMentInfo.getid();
//							emp.Tel = userInfo.getmobile();
//							emp.Email = userInfo.getemail();
//							//emp.Idx = string.IsNullOrEmpty(userInfo.order) == true ? 0 : Int32.Parse(userInfo.order);
//							emp.DirectInsert();
//
//							//增加人员与部门对应表
//							deptEmp.MyPK = deptMentInfo.getid() + "_" + emp.getNo();
//							deptEmp.FK_Dept = deptMentInfo.getid();
//							deptEmp.FK_Emp = emp.getNo();
//							deptEmp.DirectInsert();
//						}
//					}
//				}
//
//
//				///#region 处理部门名称全程
//				BP.WF.DTS.OrgInit_NameOfPath nameOfPath = new BP.WF.DTS.OrgInit_NameOfPath();
//				if (nameOfPath.getIsCanDo())
//				{
//					nameOfPath.Do();
//				}
//
//				///#endregion
//
//				return true;
//			}
//		}
//		catch (RuntimeException ex)
//		{
//			BP.DA.Log.DefaultLogWriteLineError(ex.getMessage());
//		}
//		return false;
//	}
//
//	/** 
//	 获取部门下的人员
//	 
//	 @return 
//	*/
//	private DepartMentUser_List GenerDeptUser_List(String access_token, String department_id)
//	{
//		String url = "https://oapi.dingtalk.com/user/list?access_token=" + access_token + "&department_id=" + department_id;
//		try
//		{
//			String str = (new HttpWebResponseUtility()).HttpResponseGet(url);
//			DepartMentUser_List departMentUserList = FormatToJson.<DepartMentUser_List>ParseFromJson(str);
//
//			//部门人员集合
//			if (departMentUserList != null && departMentUserList.getuserlist() != null && departMentUserList.getuserlist().size() > 0)
//			{
//				return departMentUserList;
//			}
//		}
//		catch (RuntimeException ex)
//		{
//			BP.DA.Log.DefaultLogWriteLineError(ex.getMessage());
//		}
//		return null;
//	}
//
//
//		///#region 组织结构同步
//	/** 
//	 钉钉，新增部门同步钉钉
//	 
//	 @param dept 部门基本信息
//	 @return 
//	*/
//	public final CreateDepartMent_PostVal GPM_Ding_CreateDept(Dept dept)
//	{
//		String access_token = getAccessToken();
//		String url = "https://oapi.dingtalk.com/department/create?access_token=" + access_token;
//		try
//		{
//			Map<String, Object> list = new HashMap<String, Object>();
//			list.put("name", dept.Name);
//			list.put("parentid", dept.ParentNo);
//			//list.Add("order", "1");
//			list.put("createDeptGroup", "true");
//
//			String str = BP.Tools.FormatToJson.ToJson_FromDictionary(list);
//			str = (new HttpWebResponseUtility()).HttpResponsePost_Json(url, str);
//			CreateDepartMent_PostVal postVal = FormatToJson.<CreateDepartMent_PostVal>ParseFromJson(str);
//
//			//请求返回信息
//			if (postVal != null)
//			{
//				if (!postVal.geterrcode().equals("0"))
//				{
//					BP.DA.Log.DefaultLogWriteLineError("钉钉新增部门失败：" + postVal.geterrcode() + "-" + postVal.geterrmsg());
//				}
//
//				return postVal;
//			}
//		}
//		catch (RuntimeException ex)
//		{
//			BP.DA.Log.DefaultLogWriteLineError(ex.getMessage());
//		}
//		return null;
//	}
//	/** 
//	 钉钉，编辑部门同步钉钉
//	 
//	 @param dept 部门基本信息
//	 @return 
//	*/
//	public final Ding_Post_ReturnVal GPM_Ding_EditDept(Dept dept)
//	{
//		String access_token = getAccessToken();
//		String url = "https://oapi.dingtalk.com/department/update?access_token=" + access_token;
//		try
//		{
//			Map<String, Object> list = new HashMap<String, Object>();
//			list.put("id", dept.No);
//			list.put("name", dept.Name);
//			//根目录不允许修改
//			if (!dept.No.equals("1"))
//			{
//				list.put("parentid", dept.ParentNo);
//			}
//			//大于零才可以
//			if (dept.Idx > 0)
//			{
//				list.put("order", dept.Idx);
//			}
//			String str = BP.Tools.FormatToJson.ToJson_FromDictionary(list);
//			str = (new HttpWebResponseUtility()).HttpResponsePost_Json(url, str);
//			Ding_Post_ReturnVal postVal = FormatToJson.<Ding_Post_ReturnVal>ParseFromJson(str);
//
//			//请求返回信息
//			if (postVal != null)
//			{
//				if (!postVal.geterrcode().equals("0"))
//				{
//					BP.DA.Log.DefaultLogWriteLineError("钉钉修改部门失败：" + postVal.geterrcode() + "-" + postVal.geterrmsg());
//				}
//
//				return postVal;
//			}
//		}
//		catch (RuntimeException ex)
//		{
//			BP.DA.Log.DefaultLogWriteLineError(ex.getMessage());
//		}
//		return null;
//	}
//	/** 
//	 钉钉，删除部门同步钉钉
//	 
//	 @param deptId 部门编号
//	 @return 
//	*/
//	public final Ding_Post_ReturnVal GPM_Ding_DeleteDept(String deptId)
//	{
//		String access_token = getAccessToken();
//		String url = "https://oapi.dingtalk.com/department/delete?access_token=" + access_token + "&id=" + deptId;
//		try
//		{
//			String str = (new HttpWebResponseUtility()).HttpResponseGet(url);
//			Ding_Post_ReturnVal postVal = FormatToJson.<Ding_Post_ReturnVal>ParseFromJson(str);
//
//			//请求返回信息
//			if (postVal != null)
//			{
//				if (!postVal.geterrcode().equals("0"))
//				{
//					BP.DA.Log.DefaultLogWriteLineError("钉钉删除部门失败：" + postVal.geterrcode() + "-" + postVal.geterrmsg());
//				}
//
//				return postVal;
//			}
//		}
//		catch (RuntimeException ex)
//		{
//			BP.DA.Log.DefaultLogWriteLineError(ex.getMessage());
//		}
//		return null;
//	}
//
//	/** 
//	 钉钉，新增人员同步钉钉
//	 
//	 @param emp 部门基本信息
//	 @return 
//	*/
//	public final CreateUser_PostVal GPM_Ding_CreateEmp(Emp emp)
//	{
//		String access_token = getAccessToken();
//		String url = "https://oapi.dingtalk.com/user/create?access_token=" + access_token;
//		try
//		{
//			Map<String, Object> list = new HashMap<String, Object>();
//			//如果用户编号存在则按照此账号进行新建
//			if (!(tangible.StringHelper.isNullOrEmpty(emp.getNo()) || tangible.StringHelper.isNullOrWhiteSpace(emp.getNo())))
//			{
//				list.put("userid", emp.getNo());
//			}
//			list.put("name", emp.Name);
//			//部门数组
//			ArrayList<String> listArrary = new ArrayList<String>();
//			listArrary.add(emp.FK_Dept);
//
//			list.put("department", listArrary);
//			list.put("mobile", emp.Tel);
//			list.put("email", emp.Email);
//			list.put("jobnumber", emp.EmpNo);
//			list.put("position", emp.FK_DutyText);
//
//			String str = BP.Tools.FormatToJson.ToJson_FromDictionary(list);
//			str = (new HttpWebResponseUtility()).HttpResponsePost_Json(url, str);
//			CreateUser_PostVal postVal = FormatToJson.<CreateUser_PostVal>ParseFromJson(str);
//
//			//请求返回信息
//			if (postVal != null)
//			{
//				if (!postVal.geterrcode().equals("0"))
//				{
//					//在钉钉通讯录已经存在
//					if (postVal.geterrcode().equals("60102"))
//					{
//						postVal.setuserid(emp.getNo());
//					}
//					BP.DA.Log.DefaultLogWriteLineError("钉钉新增人员失败：" + postVal.geterrcode() + "-" + postVal.geterrmsg());
//				}
//				return postVal;
//			}
//		}
//		catch (RuntimeException ex)
//		{
//			BP.DA.Log.DefaultLogWriteLineError(ex.getMessage());
//		}
//		return null;
//	}
//
//	/** 
//	 钉钉，编辑人员同步钉钉
//	 
//	 @param emp 部门基本信息
//	 @return 
//	*/
//
//	public final Ding_Post_ReturnVal GPM_Ding_EditEmp(Emp emp)
//	{
//		return GPM_Ding_EditEmp(emp, null);
//	}
//
////C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
////ORIGINAL LINE: public Ding_Post_ReturnVal GPM_Ding_EditEmp(Emp emp, List<string> deptIds = null)
//	public final Ding_Post_ReturnVal GPM_Ding_EditEmp(Emp emp, ArrayList<String> deptIds)
//	{
//		String access_token = getAccessToken();
//		String url = "https://oapi.dingtalk.com/user/update?access_token=" + access_token;
//		try
//		{
//			Map<String, Object> list = new HashMap<String, Object>();
//			list.put("userid", emp.getNo());
//			list.put("name", emp.Name);
//			list.put("email", emp.Email);
//			list.put("jobnumber", emp.EmpNo);
//			list.put("mobile", emp.Tel);
//			list.put("position", emp.FK_DutyText);
//			//钉钉根据此从其他部门删除或增加到其他部门
//			if (deptIds != null && deptIds.size() > 0)
//			{
//				list.put("department", deptIds);
//			}
//			String str = BP.Tools.FormatToJson.ToJson_FromDictionary(list);
//			str = (new HttpWebResponseUtility()).HttpResponsePost_Json(url, str);
//			Ding_Post_ReturnVal postVal = FormatToJson.<Ding_Post_ReturnVal>ParseFromJson(str);
//
//			//请求返回信息
//			if (postVal != null)
//			{
//				boolean create_Ding_user = false;
//				//40022企业中的手机号码和登录钉钉的手机号码不一致,暂时不支持修改用户信息,可以删除后重新添加
//				if (postVal.geterrcode().equals("40022") || postVal.geterrcode().equals("40021"))
//				{
//					create_Ding_user = true;
//					postVal = GPM_Ding_DeleteEmp(emp.getNo());
//					//删除失败
//					if (!postVal.geterrcode().equals("0"))
//					{
//						create_Ding_user = false;
//					}
//				}
//				else if (postVal.geterrcode().equals("60121")) //60121找不到该用户
//				{
//					create_Ding_user = true;
//				}
//
//				//需要新增人员
//				if (create_Ding_user == true)
//				{
//					CreateUser_PostVal postUserVal = GPM_Ding_CreateEmp(emp);
//					//消息传递
//					postVal.seterrcode(postUserVal.geterrcode());
//					postVal.seterrmsg(postUserVal.geterrmsg());
//				}
//
//				if (!postVal.geterrcode().equals("0"))
//				{
//					BP.DA.Log.DefaultLogWriteLineError("钉钉修改人员失败：" + postVal.geterrcode() + "-" + postVal.geterrmsg());
//				}
//				return postVal;
//			}
//		}
//		catch (RuntimeException ex)
//		{
//			BP.DA.Log.DefaultLogWriteLineError(ex.getMessage());
//		}
//		return null;
//	}
//
//	/** 
//	 钉钉，删除人员同步钉钉
//	 
//	 @param userid 人员编号
//	 @return 
//	*/
//	public final Ding_Post_ReturnVal GPM_Ding_DeleteEmp(String userid)
//	{
//		String access_token = getAccessToken();
//		String url = "https://oapi.dingtalk.com/user/delete?access_token=" + access_token + "&userid=" + userid;
//		try
//		{
//			String str = (new HttpWebResponseUtility()).HttpResponseGet(url);
//			Ding_Post_ReturnVal postVal = FormatToJson.<Ding_Post_ReturnVal>ParseFromJson(str);
//
//			//请求返回信息
//			if (postVal != null)
//			{
//				if (!postVal.geterrcode().equals("0"))
//				{
//					BP.DA.Log.DefaultLogWriteLineError("钉钉删除人员失败：" + postVal.geterrcode() + "-" + postVal.geterrmsg());
//				}
//
//				return postVal;
//			}
//		}
//		catch (RuntimeException ex)
//		{
//			BP.DA.Log.DefaultLogWriteLineError(ex.getMessage());
//		}
//		return null;
//	}
//
//		///#endregion
//
//	/** 
//	 清空组织结构
//	*/
//	private void ClearOrg_Old()
//	{
//		//人员
//		BP.DA.DBAccess.RunSQL("DELETE FROM Port_Emp");
//		//部门
//		BP.DA.DBAccess.RunSQL("DELETE FROM Port_Dept");
//		//部门人员
//		BP.DA.DBAccess.RunSQL("DELETE FROM Port_DeptEmp");
//		//部门人员岗位
//		BP.DA.DBAccess.RunSQL("DELETE FROM Port_DeptEmpStation");
//		//admin 是必须存在的
//		Emp emp = new Emp();
//		emp.getNo() = "admin";
//		emp.Pass = "pub";
//		emp.Name = "管理员";
//		emp.FK_Dept = "1";
//		emp.DirectInsert();
//		//部门人员关联表
//		DeptEmp deptEmp = new DeptEmp();
//		deptEmp.FK_Dept = "1";
//		deptEmp.FK_Emp = "admin";
//		deptEmp.DutyLevel = 0;
//		deptEmp.DirectInsert();
//	}
}