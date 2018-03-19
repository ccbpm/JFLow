package cn.jflow.services;


public class Glo
	{
		///#region 向CCIM发送消息
		/** 
		 产生消息,userid是为了保证写入消息的唯一性，receiveid才是真正的接收者
		 
		 @param userid
		 @param now
		 @param msg
		 @param sendToEmpNo 接受人
		*/
		public static void SendMessage_CCIM(String userid, String now, String msg, String sendToEmpNo)
		{
			//保存系统通知消息
			StringBuilder strHql1 = new StringBuilder();

			//加密处理
			 //msg = CCFlowServices.SecurityDES.Encrypt(msg);

			//BP.IM.RecordMsg rm = new BP.IM.RecordMsg();
			//rm.MyPK = userid; // 主键(保障消息的唯一性.)
			//rm.SendID = "SYSTEM"; //发送人.
			//rm.MsgDateTime = now; //发送日期.
			//rm.MsgContent = msg; // 消息内容.
			//rm.FontName = "宋体";
			//rm.FontSize = 10;
			//rm.FontBold = false;
			//rm.InfoClass = 15;
			//rm.GroupID = -1;
			//rm.Insert();

			//strHql1.Append("Insert into " + BP.WF.Glo.CCIMDBName + ".RecordMsg ([sendID],[msgDateTime],[msgContent],[ImageInfo],[fontName],[fontSize],[fontBold],");
			//strHql1.Append("[fontColor],[InfoClass],[GroupID],[SendUserID]) values(");
			//strHql1.Append("'SYSTEM',");
			//strHql1.Append("'").Append(now).Append("',");
			//strHql1.Append("'").Append(msg).Append("',");
			//strHql1.Append("'',");
			//strHql1.Append("'宋体',");
			//strHql1.Append("10,");
			//strHql1.Append("0,");
			//strHql1.Append("-16777216,");
			//strHql1.Append("15,");
			//strHql1.Append("-1,");
			//strHql1.Append("'").Append(userid).Append("')");
			//BP.DA.DBAccess.RunSQL(strHql1.ToString());

			//取出刚保存的msgID.
			int msgID = BP.DA.DBAccess.RunSQLReturnValInt("SELECT MsgID FROM RecordMsg WHERE SendID='SYSTEM' AND MsgDateTime='" + now + "' AND SendUserID='" + userid + "'", 0);

			if (msgID != 0)
			{
				//保存消息发送对象
				String sql = "INSERT INTO RecordMsgUser (MsgId,ReceiveID) VALUES (" + msgID + ", '" + sendToEmpNo + "' )";
				BP.DA.DBAccess.RunSQL(sql);
			}
		}
		///#endregion

		/*public static boolean IsExitProcess(String name)
		{
			System.Diagnostics.Process[] processes = System.Diagnostics.Process.GetProcesses();
			for (System.Diagnostics.Process pro : processes)
			{
				if (pro.ProcessName + name.equals(".exe"))
				{
					return true;
				}
			}
			return false;
		}*/
		/*public static boolean KillProcess(String name)
		{
			System.Diagnostics.Process[] processes = System.Diagnostics.Process.GetProcesses();
			for (System.Diagnostics.Process pro : processes)
			{
				if (name.equals(pro.ProcessName))
				{
					pro.Kill();
					return true;
				}
			}
			return false;
		}*/
		/*public static String getPathOfVisualFlow()
		{
				//return @"D:\ccflow\VisualFlow";
			String path= Application.StartupPath + "\\.\\..\\..\\..\\CCFlow\\";
			if (System.IO.Directory.Exists(path) == false)
			{
				throw new RuntimeException("@没有找到web的应用程序文件夹，此程序需要读取web.config文件才能运行。");
			}
			return path;
		}*/
		/*public static void LoadConfigByFile()
		{
			// BP.WF.Glo.IntallPath = PathOfVisualFlow;

			BP.Sys.SystemConfig.IsBSsystem_Test = false;
			BP.Sys.SystemConfig.getIsBSsystem() = false;
			SystemConfig.getIsBSsystem() = false;

			String path = getPathOfVisualFlow() + "\\web.config"; //如果有这个文件就装载它。
			if (System.IO.File.Exists(path) == false)
			{
				JOptionPane.showConfirmDialog(null, "配置文件没有找到:" + path);
				return;
				//throw new Exception("配置文件没有找到:" + path);
			}

			ClassFactory.LoadConfig(path);
			try
			{
				try
				{
					BP.Port.Emp em = new BP.Port.Emp("admin");
				}
				catch (java.lang.Exception e)
				{
					BP.Port.Emp em = new BP.Port.Emp("admin");
				}
			}
			catch (RuntimeException ex)
			{
				JOptionPane.showConfirmDialog(null, "连接数据库出现异常:" + ex.getMessage());
				return;
			}

			SystemConfig.IsBSsystem_Test = false;
			SystemConfig.getIsBSsystem() = false;
			SystemConfig.getIsBSsystem() = false;
			//   BP.Win.WF.Global.FlowImagePath = BP.WF.Global.PathOfVisualFlow + "\\Data\\FlowDesc\\";
			BP.Web.WebUser.getSysLang() = "CH";

			BP.Sys.SystemConfig.IsBSsystem_Test = false;
			BP.Sys.SystemConfig.getIsBSsystem() = false;
			SystemConfig.getIsBSsystem() = false;
		}*/
	}