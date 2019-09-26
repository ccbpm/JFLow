package BP.Sys;


///#region Copyright
//------------------------------------------------------------------------------
// <copyright file="ConfigReaders.cs" company="BP">
//     
//      Copyright (c) 2002 Microsoft Corporation.  All rights reserved.
//     
//      BP ZHZS Team
//      Purpose: config system: finds config files, loads config factories,
//               filters out relevant config file sections
//      Date: Oct 14, 2003
//      Author: peng zhou (pengzhoucn@hotmail.com) 
//      http://www.BP.com.cn
//
// </copyright>                                                                
//------------------------------------------------------------------------------

///#endregion

//using System.Data.OracleClient;
//using IBM;
//using IBM.Data;
//using IBM.Data.Informix;

/** 
 组织解构数据来源
*/
public enum OSDBSrc
{
	/** 
	 数据库.
	*/
	Database,
	/** 
	 WebServices
	*/
	WebServices;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static OSDBSrc forValue(int value)
	{
		return values()[value];
	}
}