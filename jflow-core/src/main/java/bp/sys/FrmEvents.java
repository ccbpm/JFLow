package bp.sys;

import bp.da.*;
import bp.difference.handler.CommonUtils;
import bp.difference.handler.WebContralBase;
import bp.en.*;
import bp.port.*;
import bp.tools.HttpClientUtil;
import bp.web.*;
import bp.difference.*;
import bp.*;
import java.util.*;
import java.io.*;
import java.time.*;
import java.math.*;

/** 
 事件
*/
public class FrmEvents extends EntitiesOID
{
	/** 
	 执行事件
	 
	 param dotype 执行类型
	 param en 数据实体
	 @return null 没有事件，其他为执行了事件。
	*/
	public final String DoEventNode(String dotype, Entity en) throws Exception {
		// return null; // 2019-08-27 取消节点事件 zl 
		return DoEventNode(dotype, en, null);
	}
	/** 
	 执行事件
	 
	 param dotype 执行类型
	 param en 数据实体
	 param atPara 参数
	 @return null 没有事件，其他为执行了事件。
	 
	 不再使用节点事件 2019-08-27 zl
	 原调用点有两处：（1）FrmEvent.cs 中的DoEventNode()； （2）Flow.cs中的DoFlowEventEntity()方法中，3973行 fes.DoEventNode(doType, en, atPara)。
	 现在都已经取消调用
	 
	*/
	public final String DoEventNode(String dotype, Entity en, String atPara) throws Exception {
		if (this.size() == 0)
		{
			return null;
		}
		String val = _DoEventNode(dotype, en, atPara);
		if (val != null)
		{
			val = val.trim();
		}

		if (DataType.IsNullOrEmpty(val))
		{
			return ""; // 说明有事件，执行成功了。
		}
		else
		{
			return val; // 没有事件.
		}
	}

	/** 
	 执行事件，事件标记是 EventList.
	 
	 param dotype 执行类型
	 param en 数据实体
	 param atPara 特殊的参数格式@key=value 方式.
	 @return 
	*/
	private String _DoEventNode(String dotype, Entity en, String atPara) throws Exception {
		if (this.size() == 0)
		{
			return null;
		}

		bp.en.Entity tempVar = this.GetEntityByKey(FrmEventAttr.FK_Event, dotype);
		FrmEvent nev = tempVar instanceof FrmEvent ? (FrmEvent)tempVar : null;

		if (nev == null || nev.getHisDoType() == EventDoType.Disable)
		{
			return null;
		}


			///#region 执行的是业务单元.
		if (nev.getHisDoType() == EventDoType.BuessUnit)
		{
			/* 获得业务单元，开始执行他 */
			BuessUnitBase enBuesss = bp.sys.base.Glo.GetBuessUnitEntityByEnName(nev.getDoDoc());
			enBuesss.WorkID = Long.parseLong(en.getPKVal().toString());
			return enBuesss.DoIt();
		}

			///#endregion 执行的是业务单元.


		String doc = nev.getDoDoc().trim();
		if ((doc == null || doc.equals("")) && nev.getHisDoType() != EventDoType.SpecClass) //edited by liuxc,2016-01-16,执行DLL文件不需要判断doc为空
		{
			return null;
		}


			///#region 处理执行内容
		Attrs attrs = en.getEnMap().getAttrs();
		String MsgOK = "";
		String MsgErr = "";

		doc = doc.replace("~", "'");
		doc = doc.replace("@WebUser.No", WebUser.getNo());
		doc = doc.replace("@WebUser.Name", WebUser.getName());
		doc = doc.replace("@WebUser.FK_Dept", WebUser.getFK_Dept());
		doc = doc.replace("@FK_Node", nev.getFK_MapData().replace("ND", ""));
		doc = doc.replace("@FK_MapData", nev.getFK_MapData());
		doc = doc.replace("@WorkID", en.GetValStrByKey("OID", "@WorkID"));
		doc = doc.replace("@WebUser.OrgNo", WebUser.getOrgNo());

		if (doc.contains("@") == true)
		{
			for (Attr attr : attrs.ToJavaList())
			{
				if (doc.contains("@" + attr.getKey()) == false)
				{
					continue;
				}
				doc = doc.replace("@" + attr.getKey(), en.GetValStrByKey(attr.getKey()));
			}
		}




		//SDK表单上服务器地址,应用到使用ccflow的时候使用的是sdk表单,该表单会存储在其他的服务器上. 
		doc = doc.replace("@SDKFromServHost", SystemConfig.getAppSettings().get("SDKFromServHost").toString());

		if (doc.contains("@") == true)
		{
			/*如果是 bs 系统, 有可能参数来自于url ,就用url的参数替换它们 .*/
				//string url = BP.Sys.Base.Glo.Request.RawUrl;
				//2019-07-25 zyt改造
				String url = bp.sys.base.Glo.getRequest().getRemoteAddr();
				if (url.indexOf('?') != -1)
				{
					url = url.substring(url.indexOf('?'));//.indexOf('?');
				}

				String[] paras = url.split("[&]", -1);
				for (String s : paras)
				{
					String[] mys = s.split("[=]", -1);

					if (doc.contains("@" + mys[0]) == false)
					{
						continue;
					}

					doc = doc.replace("@" + mys[0], mys[1]);
				}
		}

		if (nev.getHisDoType() == EventDoType.URLOfSelf)
		{
			if (doc.contains("?") == false)
			{
				doc += "?1=2";
			}

			doc += "&UserNo=" + WebUser.getNo();
			doc += "&Token=" + WebUser.getToken();
			doc += "&FK_Dept=" + WebUser.getFK_Dept();
			// doc += "&FK_Unit=" + WebUser.FK_Unit;
			doc += "&OID=" + en.getPKVal();

			if (SystemConfig.getIsBSsystem())
			{
				/*是bs系统，并且是url参数执行类型.*/                    
				//2019-07-25 zyt改造
				String url = bp.sys.base.Glo.getRequest().getRemoteAddr();
				if (url.indexOf('?') != -1)
				{
					url = StringHelper.trimStart(url.substring(url.indexOf('?')), '?');
				}

				String[] paras = url.split("[&]", -1);
				for (String s : paras)
				{
					String[] mys = s.split("[=]", -1);

					if (doc.contains(mys[0] + "="))
					{
						continue;
					}

					doc += "&" + s;
				}

				doc = doc.replace("&?", "&");
			}

			if (SystemConfig.getIsBSsystem() == false)
			{
				/*非bs模式下调用,比如在cs模式下调用它,它就取不到参数. */
			}

			if (doc.startsWith("http") == false)
			{
				/*如果没有绝对路径 */
				if (SystemConfig.getIsBSsystem())
				{
					/*在cs模式下自动获取*/
					//string host = BP.Sys.Base.Glo.Request.Url.Host;
					//2019-07-25 zyt改造
					String host = SystemConfig.getHostURL();
					if (doc.contains("@AppPath"))
					{
						doc = doc.replace("@AppPath", "http://" + host + CommonUtils.getRequest().getRequestURL());
					}
					else
					{
						doc = "http://" + CommonUtils.getRequest().getRequestURI() + doc;
					}

				}

				if (SystemConfig.getIsBSsystem() == false)
				{
					/*在cs模式下它的baseurl 从web.config中获取.*/
					String cfgBaseUrl = SystemConfig.getHostURL();
					if (DataType.IsNullOrEmpty(cfgBaseUrl))
					{
						String err = "调用url失败:没有在web.config中配置BaseUrl,导致url事件不能被执行.";
						Log.DebugWriteError(err);
						throw new RuntimeException(err);
					}
					doc = cfgBaseUrl + doc;
				}
			}

			//增加上系统约定的参数.
			doc += "&EntityName=" + en.toString() + "&EntityPK=" + en.getPK() + "&EntityPKVal=" + en.getPKVal() + "&FK_Event=" + nev.getMyPK();
		}

			///#endregion 处理执行内容

		if (atPara != null && doc.contains("@") == true)
		{
			AtPara ap = new AtPara(atPara);
			for (String s : ap.getHisHT().keySet())
			{
				doc = doc.replace("@" + s, ap.GetValStrByKey(s));
			}
		}

		if (EventListFrm.FrmLoadBefore.equals(dotype))
		{
			en.Retrieve(); //如果不执行，就会造成实体的数据与查询的数据不一致.
		}

		switch (nev.getHisDoType())
		{
			case SP:
			case SQL:
				//SQLServer数据库中执行带参的存储过程
				try
				{
					if (nev.getHisDoType() == EventDoType.SP && doc.contains("=") == true && SystemConfig.getAppCenterDBType() == DBType.MSSQL)
					{
						RunSQL(doc);
						return nev.MsgOK(en);
					}
					if (DataType.IsNullOrEmpty(nev.getFK_DBSrc()) == false && nev.getFK_DBSrc().equals("local") == false)
					{
						SFDBSrc sfdb = new SFDBSrc(nev.getFK_DBSrc());
						sfdb.RunSQLs(doc);

					}
					else
					{
						// 允许执行带有GO的sql.
						DBAccess.RunSQLs(doc);
					}
					return nev.MsgOK(en);
				}
				catch (RuntimeException ex)
				{
					throw new RuntimeException(nev.MsgError(en));
				}
				//break;

			case URLOfSelf:
				Object tempVar2 = doc;
				String myURL = tempVar2 instanceof String ? (String)tempVar2 : null;
				if (myURL.contains("http") == false)
				{
					if (SystemConfig.getIsBSsystem())
					{
						//string host = BP.Sys.Base.Glo.Request.Url.Host;
						//2019-07-25 zyt改造
						String host = SystemConfig.getHostURL();
						if (myURL.contains("@AppPath"))
						{
							myURL = myURL.replace("@AppPath", "http://" + host + CommonUtils.getRequest().getRequestURL());
						}
						else
						{
							myURL = "http://" + CommonUtils.getRequest().getRequestURL() + myURL;
						}
					}
					else
					{
						String cfgBaseUrl = SystemConfig.getHostURL();
						if (DataType.IsNullOrEmpty(cfgBaseUrl))
						{
							String err = "调用url失败:没有在web.config中配置BaseUrl,导致url事件不能被执行.";
							Log.DebugWriteError(err);
							throw new RuntimeException(err);
						}
						myURL = cfgBaseUrl + myURL;
					}
				}
				myURL = myURL.replace("@SDKFromServHost", SystemConfig.getAppSettings().get("SDKFromServHost").toString());

				if (myURL.contains("&FID=") == false && en.getRow().containsKey("FID") == true)
				{
					String str = en.getRow().get("FID").toString();
					myURL = myURL + "&FID=" + str;
				}

				if (myURL.contains("&FK_Flow=") == false && en.getRow().containsKey("FK_Flow") == true)
				{
					String str = en.getRow().get("FK_Flow").toString();
					myURL = myURL + "&FK_Flow=" + str;
				}

				if (myURL.contains("&WorkID=") == false && en.getRow().containsKey("WorkID") == true)
				{
					String str = en.getRow().get("WorkID").toString();
					myURL = myURL + "&WorkID=" + str;
				}

				try
				{
					//Encoding encode = System.Text.Encoding.GetEncoding("gb2312");
					String text = DataType.ReadURLContext(myURL, 600000);
					if (text == null)
					{
						throw new RuntimeException("@流程设计错误，执行的url错误:" + myURL + ", 返回为null, 请检查url设置是否正确。提示：您可以copy出来此url放到浏览器里看看是否被正确执行。");
					}

					if (text != null && text.length() > 7 && text.substring(0, 7).toLowerCase().contains("err"))
					{
						throw new RuntimeException(text);
					}

					if (text == null || text.trim().equals(""))
					{
						return null;
					}
					return text;
				}
				catch (RuntimeException ex)
				{
					throw new RuntimeException("@" + nev.MsgError(en) + " Error:" + ex.getMessage());
				}
				//break;
			case EventBase: //执行事件类.
				// 获取事件类.
				Object tempVar3 = doc;
				String evName = tempVar3 instanceof String ? (String)tempVar3 : null;
				bp.sys.base.EventBase ev = null;
				try
				{
					ev = ClassFactory.GetEventBase(evName);
				}
				catch (RuntimeException ex)
				{
					throw new RuntimeException("@事件名称:" + evName + "拼写错误，或者系统不存在。说明：事件所在的类库必须是以BP.开头，并且类库的BP.xxx.dll。");
				}

				if (ev == null)
				{
					throw new RuntimeException("@事件名称:" + evName + "拼写错误，或者系统不存在。说明：事件所在的类库必须是以BP.开头，并且类库的BP.xxx.dll。");
				}

				//开始执行.
				try
				{

						///#region 处理整理参数.
					Row r = en.getRow();
					try
					{
						//系统参数.
						r.put("FK_MapData", en.getClassID());
					}
					catch (java.lang.Exception e)
					{
						r.put("FK_MapData", en.getClassID());
					}

					try
					{
						r.put("EventSource", nev.getFK_Event());
					}
					catch (java.lang.Exception e2)
					{
						r.put("EventSource", nev.getFK_Event());
					}

					if (atPara != null)
					{
						AtPara ap = new AtPara(atPara);
						for (String s : ap.getHisHT().keySet())
						{
							try
							{
								r.put(s, ap.GetValStrByKey(s));
							}
							catch (java.lang.Exception e3)
							{
								r.put(s, ap.GetValStrByKey(s));
							}
						}
					}

					if (SystemConfig.getIsBSsystem() == true)
					{
						/*如果是bs系统, 就加入外部url的变量.*/
						//2019 - 07 - 25 zyt改造
						for (String key : ContextHolderUtils.getRequest().getParameterMap().keySet())
						{
							String val = ContextHolderUtils.getRequest().getParameter(key);
							try
							{
								r.put(key, val);
							}
							catch (java.lang.Exception e4)
							{
								r.put(key, val);
							}
						}
					}

						///#endregion 处理整理参数.

					ev.setSysPara(r);
					ev.HisEn = en;
					ev.Do();
					return ev.SucessInfo;
				}
				catch (RuntimeException ex)
				{
					throw new RuntimeException("@执行事件(" + ev.getTitle() + ")期间出现错误:" + ex.getMessage());
				}
				//break;
			case WSOfSelf: //执行webservices.. 为石油修改.
				String[] strs = doc.split("[@]", -1);
				String url = "";
				String method = "";
				Hashtable paras = new Hashtable();
				for (String str : strs)
				{
					if (str.contains("=") && str.contains("Url"))
					{
						url = str.split("[=]", -1)[2];
						continue;
					}

					if (str.contains("=") && str.contains("Method"))
					{
						method = str.split("[=]", -1)[2];
						continue;
					}

					//处理参数.
					String[] paraKeys = str.split("[,]", -1);

					if (paraKeys[3].equals("Int"))
					{
						paras.put(paraKeys[0], Integer.parseInt(paraKeys[1]));
					}

					if (paraKeys[3].equals("String"))
					{
						paras.put(paraKeys[0], paraKeys[1]);
					}

					if (paraKeys[3].equals("Float"))
					{
						paras.put(paraKeys[0], Float.parseFloat(paraKeys[1]));
					}

					if (paraKeys[3].equals("Double"))
					{
						paras.put(paraKeys[0], Double.parseDouble(paraKeys[1]));
					}
				}
				return null;
				//开始执行webserives.
				//break;
			case WebApi:
				try
				{
					//接收返回值
					String postData = "";
					//获取webapi接口地址
					Object tempVar4 = doc;
					String apiUrl = tempVar4 instanceof String ? (String)tempVar4 : null;
					if (apiUrl.contains("@WebApiHost")) //可以替换配置文件中配置的webapi地址
					{
						apiUrl = apiUrl.replace("@WebApiHost", SystemConfig.getAppSettings().get("WebApiHost").toString());
					}

					if (apiUrl.contains("?") == true)
					{
						apiUrl += "&WorkID=" + en.getPKVal() + "&UserNo=" + WebUser.getNo() + "&Token=" + WebUser.getToken();
					}
					else
					{
						apiUrl += "?WorkID=" + en.getPKVal() + "&UserNo=" + WebUser.getNo() + "&Token=" + WebUser.getToken();
					}

					//api接口地址
					String apiHost = apiUrl.split("[?]", -1)[0];
					//api参数
					String apiParams = apiUrl.split("[?]", -1)[1];
					//参数替换
					apiParams = bp.tools.PubGlo.DealExp(apiParams, en);
					//执行POST
					postData = HttpClientUtil.doPost(apiHost, apiParams,null);
					return postData;
				}
				catch (RuntimeException ex)
				{
					throw new RuntimeException("@" + nev.MsgError(en) + " Error:" + ex.getMessage());
				}
				//break;
//			case SpecClass:
//
//					///#region //执行dll文件中指定类的指定方法，added by liuxc,2016-01-16
//				String evdll = nev.getMonthedDLL();
//				String evclass = nev.getMonthedClass();
//				String evmethod = nev.getMonthedName();
//				String evparams = nev.getMonthedParas();
//
//				if (DataType.IsNullOrEmpty(evdll) || !(new File(evdll)).isFile())
//				{
//					throw new RuntimeException("@DLL文件【MonthedDLL】“" + (evdll != null ? evdll : "") + "”设置不正确，请重新设置！");
//				}
//
//				Assembly abl = Assembly.LoadFrom(evdll);
//
//				//判断类是否是静态类
//				java.lang.Class type = abl.GetType(evclass, false);
//
//				if (type == null)
//				{
//					throw new RuntimeException("@DLL文件【MonthedDLL】“" + evdll + "”中的类名【MonthedClass】“" + (evclass != null ? evclass : "") + "”设置不正确，未检索到此类，请重新设置！");
//				}
//
//				//方法
//				if (DataType.IsNullOrEmpty(evmethod))
//				{
//					throw new RuntimeException("@DLL文件【MonthedDLL】“" + evdll + "”中类【MonthedClass】“" + evclass + "”的方法名【MonthedName】不能为空，请重新设置！");
//				}
//				java.lang.Class tp = evmethod.getClass();
//				java.lang.reflect.Method mp = tp.getMethod(methodName);
//				Class[] paramInfos =mp.getParameterTypes();
//				java.lang.reflect.Method md = null; //当前方法
//				ParameterInfo[] pis = null; //方法的参数集合
//
//				HashMap<String, String> pss = new HashMap<String, String>(); //参数名，参数值类型名称字典，如：Name,String
//				String mdName = evmethod.split("[(]", -1)[0]; //方法名称
//
//				//获取method对象
//				if (mdName.length() == evmethod.length() - 2)
//				{
//					md = type.getMethod(mdName);
//				}
//				else
//				{
//					String[] pssArr = null;
//
//					//获取设置里的参数信息
//					for (String pstr : StringHelper.substring(evmethod, mdName.length() + 1, evmethod.length() - mdName.length() - 2).split("[,]", -1))
//					{
//						pssArr = pstr.split("[ ]", -1);
//						pss.put(pssArr[1], pssArr[0]);
//					}
//
//					//与设置里的参数信息对比，取得MethodInfo对象
//					for (java.lang.reflect.Method m : type.getMethods())
//					{
//						if (!m.Name.equals(mdName))
//						{
//							continue;
//						}
//
//						pis = m.GetParameters();
//						boolean isOK = true;
//						int idx = 0;
//
//						for (java.util.Map.Entry<String, String> ps : pss.entrySet())
//						{
//							if (!pis[idx].Name.equals(ps.getKey()) || !ps.getValue().equals(pis[idx].ParameterType.toString().replace("System.IO.", "").Replace("System.", "").Replace("System.Collections.Generic.", "").Replace("System.Collections.", "")))
//							{
//								isOK = false;
//								break;
//							}
//
//							idx++;
//						}
//
//						if (isOK)
//						{
//							md = m;
//							break;
//						}
//					}
//				}
//
//				if (md == null)
//				{
//					throw new RuntimeException("@DLL文件【MonthedDLL】“" + evdll + "”中类【MonthedClass】“" + evclass + "”的方法名【MonthedName】“" + evmethod + "”设置不正确，未检索到此方法，请重新设置！");
//				}
//
//				//处理参数
//				Object[] pvs = new Object[pss.size()]; //invoke，传递的paramaters参数，数组中的项顺序与方法参数顺序一致
//
//				if (!pss.isEmpty())
//				{
//					if (DataType.IsNullOrEmpty(evparams))
//					{
//						throw new RuntimeException("@DLL文件【MonthedDLL】“" + evdll + "”中类【MonthedClass】“" + evclass + "”的方法【MonthedName】“" + evmethod + "”的参数【MonthedParas】不能为空，请重新设置！");
//					}
//
//					HashMap<String, String> pds = new HashMap<String, String>(); //MonthedParas中保存的参数信息集合，格式如：title,@Title
//					int idx = 0;
//					int pidx = -1;
//					String[] pdsArr = evparams.split("[;]", -1);
//					String val;
//
//					//将参数中的名称与值分开
//					for (String p : pdsArr)
//					{
//						pidx = p.indexOf('=');
//						if (pidx == -1)
//						{
//							continue;
//						}
//
//						pds.put(p.substring(0, pidx), p.substring(pidx + 1));
//					}
//
//					for (java.util.Map.Entry<String, String> ps : pss.entrySet())
//					{
//						if (!pds.containsKey(ps.getKey()))
//						{
//							//设置中没有此参数的值信息，则将值赋为null
//							pvs[idx] = null;
//						}
//						else
//						{
//							val = pds.get(ps.getKey());
//
//							for (Attr attr : en.getEnMap().getAttrs())
//							{
//								if (pds.get(ps.getKey()).equals("`" + attr.getKey() + "`"))
//								{
//									//表示此参数与该attr的值一致，类型也一致
//									pvs[idx] = en.getRow().get(attr.getKey());
//									break;
//								}
//
//								//替换@属性
//								Row tempVar5 = en.getRow().get(attr.getKey());
//								val = val.replace("`" + attr.getKey() + "`", (tempVar5 != null ? tempVar5 : "").toString());
//							}
//
//							//转换参数类型，从字符串转换到参数的实际类型，NOTE:此处只列出了简单类型的转换，其他类型暂未考虑
//							switch (ps.getValue())
//							{
//								case "String":
//									pvs[idx] = val;
//									break;
//								case "Int32":
//									pvs[idx] = Integer.parseInt(val);
//									break;
//								case "Int64":
//									pvs[idx] = Long.parseLong(val);
//									break;
//								case "Double":
//									pvs[idx] = Double.parseDouble(val);
//									break;
//								case "Single":
//									pvs[idx] = Float.parseFloat(val);
//									break;
//								case "Decimal":
//									pvs[idx] =  new BigDecimal(val);
//									break;
//								case "DateTime":
//									pvs[idx] = Date.parse(val);
//									break;
//								default:
//									pvs[idx] = val;
//									break;
//							}
//						}
//
//						idx++;
//					}
//				}
//
//				if (type.IsSealed && type.IsAbstract)
//				{
//					//静态类
//					return (md.Invoke(null, pvs) != null ? md.Invoke(null, pvs) : "").toString();
//				}
//
//				//非静态类
//				//虚类必须被重写，不能直接使用
//				if (type.IsAbstract)
//				{
//					return null;
//				}
//
//				//静态方法
//				if (md.IsStatic)
//				{
//					return (md.Invoke(null, pvs) != null ? md.Invoke(null, pvs) : "").toString();
//				}
//
//				//非静态方法
//				return (md.Invoke(abl.CreateInstance(evclass), pvs) != null ? md.Invoke(abl.CreateInstance(evclass), pvs) : "").toString();

				///#endregion
			default:
				throw new RuntimeException("@no such way." + nev.getHisDoType().toString());
		}
	}
	/** 
	 事件
	*/
	public FrmEvents()  {
	}
	/** 
	 事件
	 
	 param FK_MapData FK_MapData
	*/
	public FrmEvents(String fk_MapData) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FrmEventAttr.FK_MapData, fk_MapData);
		qo.DoQuery();
	}
	/** 
	 事件
	 
	 param FK_MapData 按节点ID查询
	*/
	public FrmEvents(int nodeID) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FrmEventAttr.FK_Node, nodeID);
		qo.DoQuery();
	}
	public FrmEvents(int nodeID, String fk_flow) throws Exception {
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(FrmEventAttr.FK_Node, nodeID);
		qo.addOr();
		qo.addLeftBracket();
		qo.AddWhere(FrmEventAttr.FK_Flow, fk_flow);
		qo.addAnd();
		qo.AddWhere(FrmEventAttr.FK_Node, 0);
		qo.addRightBracket();
		qo.DoQuery();
	}

	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity() {
		return new FrmEvent();
	}


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<FrmEvent> ToJavaList() {
		return (java.util.List<FrmEvent>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<FrmEvent> Tolist()  {
		ArrayList<FrmEvent> list = new ArrayList<FrmEvent>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((FrmEvent)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}