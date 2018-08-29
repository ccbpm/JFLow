package BP.En;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import BP.DA.DataType;
import BP.DA.Log;
import BP.Sys.EventBase;
import BP.Sys.GEDtl;
import BP.Sys.GEEntity;
import BP.Sys.SystemConfig;
import cn.jflow.common.util.ClassUtils;

/**
 * ClassFactory 的摘要说明。
 */
public class ClassFactory {
	
	// 构造函数， 属性
	//	static
	//	{
	//		/*
	//		 * warning String path = AppDomain.CurrentDomain.BaseDirectory;
	//		 */
	//		String path = TL.ConvertTools.getPorjectPath();
	//		File file = new File(path + "bin\\");
	//		if (file.exists())
	//		{
	//			String ccFlowAppPath = SystemConfig.getAppSettings()
	//					.get("CCFlowAppPath").toString();
	//			file = new File(path + ccFlowAppPath + "bin\\");
	//			if (!StringHelper.isNullOrEmpty(ccFlowAppPath) && file.exists())
	//			{
	//				_BasePath = path + ccFlowAppPath + "bin\\";
	//			} else
	//			{
	//				_BasePath = path + "bin\\";
	//			}
	//		} else
	//		{
	//			_BasePath = path;
	//		}
	//	}
	//	private static String _BasePath = null;
	//	
	//	public static String getBasePath()
	//	{
	//		String installPath = SystemConfig.getAppSettings().get("InstallPath")
	//				.toString();
	//		if (_BasePath == null)
	//		{
	//			if (installPath == null)
	//			{
	//				_BasePath = "D:\\";
	//			} else
	//			{
	//				_BasePath = installPath;
	//			}
	//		}
	//		return _BasePath;
	//	}

	public static Hashtable Htable_Evbase;

	/**
	 * 得到一个事件实体
	 * 
	 * @param className 类名称
	 * @return BP.Sys.EventBase
	 */
	public static BP.Sys.EventBase GetEventBase(String className) {
		if (Htable_Evbase == null || Htable_Evbase.isEmpty()) {
			Htable_Evbase = new Hashtable();
			String cl = "BP.Sys.EventBase";
			ArrayList al = ClassFactory.GetObjects(cl);
			Htable_Evbase.clear();
			for (Object en : al) {
				try {
					Htable_Evbase.put(((EventBase) en).getClass().getName(), en);
				} catch (java.lang.Exception e) {}
			}
		}
		BP.Sys.EventBase ens = (EventBase) ((Htable_Evbase.get(className) instanceof EventBase) ? Htable_Evbase.get(className) : null);
		return ens;
	}

	// /**
	// * 获取取程序集[dll]
	// *
	// * @return
	// */
	// public static Assembly[] getBPAssemblies() {
	// if (_BPAssemblies == null) {
	// String[] fs = System.IO.Directory.GetFiles(getBasePath(),
	// "BP.*.dll");
	// String[] fs1 = System.IO.Directory
	// .GetFiles(getBasePath(), "*.ssss");
	//
	// String strs = "";
	// for (String str : fs) {
	// strs += str + ";";
	// }
	//
	// for (String str : fs1) {
	// strs += str + ";";
	// }
	//
	// fs = strs.split("[;]", -1);
	// // 有多少个 不包含 .Web. 的ddl .
	// int fsCount = 0;
	// for (String s : fs) {
	// if (s.length() == 0) {
	// continue;
	// }
	//
	// // if (s.IndexOf(".Web.") != -1)
	// // continue;
	//
	// fsCount++;
	// }
	//
	// // 把它们加入到 asss 里面去。
	// Assembly[] asss = new Assembly[fsCount];
	// int idx = 0;
	// int fsIndex = -1;
	// for (String s : fs) {
	// fsIndex++;
	// // if (s.IndexOf(".Web.") != -1)
	// // continue;
	//
	// if (s.length() == 0) {
	// continue;
	// }
	//
	// asss[idx] = Assembly.LoadFrom(fs[fsIndex]);
	// idx++;
	// }
	// _BPAssemblies = asss;
	// }
	// return _BPAssemblies;
	// }

	// 程序集
	// 类型
	// public static java.lang.Class GetBPType(String className) {
	// java.lang.Class typ = null;
	// for (Assembly ass : getBPAssemblies()) {
	// typ = ass.GetType(className);
	// if (typ != null) {
	// return typ;
	// }
	// }
	// return typ;
	// }

	// public static ArrayList GetBPTypes(String baseEnsName) {
	// ArrayList arr = new ArrayList();
	// java.lang.Class baseClass = null;
	// for (Assembly ass : getBPAssemblies()) {
	// if (baseClass == null) {
	// baseClass = ass.GetType(baseEnsName);
	// }
	// java.lang.Class[] tps = ass.GetTypes();
	// for (int i = 0; i < tps.length; i++) {
	// if (tps[i].IsAbstract || tps[i].getSuperclass() == null
	// || !tps[i].IsClass || !tps[i].IsPublic) {
	// continue;
	// }
	// java.lang.Class tmp = tps[i].getSuperclass();
	//
	// if (tmp.Namespace == null) {
	// throw new RuntimeException(tmp.FullName);
	// }
	//
	// while (tmp != null && tmp.Namespace.indexOf("BP") != -1) {
	// if (baseEnsName.equals(tmp.FullName)) {
	// arr.add(tps[i]);
	// }
	// tmp = tmp.getSuperclass();
	// }
	// }
	// }
	// if (baseClass == null) {
	// throw new RuntimeException("@找不到类型:" + baseEnsName + "！");
	// }
	// return arr;
	//
	// }

	// public static boolean IsFromType(String childTypeFullName,
	// String parentTypeFullName) {
	// for (Assembly ass : getBPAssemblies()) {
	// java.lang.Class childType = ass.GetType(childTypeFullName);
	// while (childType != null && childType.getSuperclass() != null) {
	// if (parentTypeFullName
	// .equals(childType.getSuperclass().FullName)) {
	// return true;
	// }
	// childType = childType.getSuperclass();
	// }
	// }
	// return false;
	// }

	private static Hashtable objects = new Hashtable();

	/// <summary>
    /// 尽量不用此方法来获取事例
    /// </summary>
    /// <param name="className"></param>
    /// <returns></returns>
    public static Object GetObject_OK(String className) throws Exception
    {
        if (className == "" || className == null)
            return "err@要转化类名称为空...";
        Class clazz = Class.forName(className);
        return clazz.newInstance();
    }

	/**
	 * 根据一个抽象的基类，取出此系统中从他上面继承的子类集合。 非抽象的类。
	 * 
	 * @param baseEnsName 抽象的类名称
	 * @return ArrayList
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList GetObjects(String baseEnsName) {

		ArrayList list = (ArrayList) objects.get(baseEnsName);

		if (list != null) {
			return list;
		}

		try {
			list = new ArrayList();
			Class parent = Class.forName(baseEnsName);
			Set<Class<?>> set = ClassUtils.findImplementations(parent, "BP");
			for (Iterator<Class<?>> it = set.iterator(); it.hasNext();) {
				Class<?> clazz = it.next();
				try {
					list.add(clazz.newInstance());
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			Collections.sort(list, new Comparator<Object>() {
				@Override
				public int compare(Object o1, Object o2) {
					return o1.getClass().getName().compareTo(o2.getClass().getName());
				}
			});
			Log.DebugWriteInfo("扫描 " + baseEnsName + " 父类，共 " + set.size() + " 子类：" + set);
			objects.put(baseEnsName, list);
			return list;
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		return null;
	}

	private static Hashtable<String, Object> Htable_En;

	/**
	 * 得到一个实体
	 * 
	 * @param className 类名称
	 * @return En
	 */
	public static Entity GetEn(String className) {
		//判断标记初始化实体.
		if (className.contains(".") == false)
		{
			if (className.contains("Dtl") == true)
			{
				return new GEDtl(className); //明细表.
			}
			else
			{
				return new GEEntity(className); //表单实体.
			}
		}
		
		if (Htable_En == null) {
			Htable_En = new Hashtable<String, Object>();
			String cl = "BP.En.EnObj";
			ArrayList al = ClassFactory.GetObjects(cl);
			for (Object en : al) {
                if (null == en || StringUtils.isEmpty(en.getClass().getName()))
                    continue;
                if (Htable_En.containsKey(en.getClass().getName()) == false){
                	Htable_En.put(en.getClass().getName(), en);
                }else
                {
                    continue;
                }
			}
		}
		Object tmp = Htable_En.get(className);
		return ((Entity) ((tmp instanceof Entity) ? tmp : null));
	}

	private static Hashtable<String, Object> Htable_Method;

	/*
	 * 得到一个实体
	 * 
	 * @param className 类名称
	 * 
	 * @return En
	 */
	public static Method GetMethod(String className) {
		if (Htable_Method == null) {
			Htable_Method = new Hashtable();
			String cl = "BP.En.Method";
			ArrayList<Method> al = ClassFactory.GetObjects(cl);
			for (Method en : al) {
				Htable_Method.put(en.getClass().getName(), en);
			}
		}
		Object tmp = Htable_Method.get(className);
		return ((BP.En.Method) ((tmp instanceof BP.En.Method) ? tmp : null));
	}

	// 获取 ens
	public static Hashtable<String, Object> Htable_Ens;

	/**
	 * 得到一个实体
	 * 
	 * @param className 类名称
	 * @return En
	 */
	public static Entities GetEns(String className) {
		
		if (className.contains(".")==false) {
			BP.Sys.GEEntitys myens = new BP.Sys.GEEntitys(className);
			return myens;
		}

		//if (Htable_Ens == null || Htable_Ens.isEmpty()) {
			Htable_Ens = new Hashtable<String, Object>();
			String cl = "BP.En.Entities";
			ArrayList al = ClassFactory.GetObjects(cl);

			Htable_Ens.clear();
			for (Object en : al) {
				try {
					Htable_Ens.put(en.getClass().getName(), en);
				} catch (java.lang.Exception e) {}
			}
		//}
		Entities ens = (Entities) ((Htable_Ens.get(className) instanceof Entities) ? Htable_Ens.get(className) : null);

		
		// /#warning 会清除 cash 中的数据。
		return ens;
	}

	// 获取 ens
	public static Hashtable<String, Object> Htable_XmlEns;

	/**
	 * 得到一个实体
	 * 
	 * @param className 类名称
	 * @return En
	 */
	public static BP.XML.XmlEns GetXmlEns(String className) {
		if (Htable_XmlEns == null) {
			Htable_XmlEns = new Hashtable<String, Object>();
			String cl = "BP.XML.XmlEns";
			ArrayList al = ClassFactory.GetObjects(cl);
			for (Object en : al) {
				Htable_XmlEns.put(en.getClass().getName(), en);
			}
		}
		Object tmp = Htable_XmlEns.get(className);
		return ((BP.XML.XmlEns) ((tmp instanceof BP.XML.XmlEns) ? tmp : null));
	}

	// 获取 en
	public static Hashtable<String, Object> Htable_XmlEn;

	/**
	 * 得到一个实体
	 * 
	 * @param className 类名称
	 * @return En
	 */
	public static BP.XML.XmlEn GetXmlEn(String className) {
		if (Htable_XmlEn == null) {
			Htable_XmlEn = new Hashtable<String, Object>();
			String cl = "BP.XML.XmlEn";
			ArrayList al = ClassFactory.GetObjects(cl);
			for (Object en : al) {
				Htable_XmlEn.put(en.getClass().getName(), en);
			}
		}
		Object tmp = Htable_XmlEn.get(className);
		return ((BP.XML.XmlEn) ((tmp instanceof BP.XML.XmlEn) ? tmp : null));
	}

	public static String getClassPath() {
		String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
		path = path.substring(path.indexOf("/") + 1);
		int i = 0;
		while (path.indexOf("/") != -1 && path.length() > 0) {
			path = path.substring(0, path.lastIndexOf("/"));
			if (1 == i) {

				String service = SystemConfig.getAppSettings().get("Service").toString().toLowerCase();
				if (service.equals("tomcat")) {
					path += System.getProperty("file.separator") + "lib" + System.getProperty("file.separator");
					return path;
				} else if (service.equals("jetty")) {
					path += System.getProperty("file.separator");// +"lib"+ System.getProperty("file.separator");
					return path;
				}
			}
			i++;
		}
		return path.substring(path.indexOf("/") + 1, path.lastIndexOf("/") - 7);
	}
	 

	 // #region 获取 HandlerBase
     private static Hashtable Htable_HandlerPage;
     /// <summary>
     /// 得到一个实体
     /// </summary>
     /// <param name="className">类名称</param>
     /// <returns>En</returns>
     public static Object GetHandlerPage(String className)
     {

         if (Htable_HandlerPage == null)
         {
             Htable_HandlerPage = new Hashtable();
             String cl = "BP.WF.HttpHandler.DirectoryPageBase";
             ArrayList al = ClassFactory.GetObjects(cl);
             for (Object en : al)
             {
                 String key = "";
                 if (null == en || DataType.IsNullOrEmpty(key = en.toString()))
                     continue;

                 if (Htable_HandlerPage.containsKey(key) == false)
                     Htable_HandlerPage.put(key, en);

             }
         }
         return Htable_HandlerPage.get(className);
     }
}