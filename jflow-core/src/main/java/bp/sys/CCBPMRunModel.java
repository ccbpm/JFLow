package bp.sys;

import bp.da.*;
import bp.web.*;
import bp.*;
import java.util.*;
import java.io.*;
import java.time.*;


///Copyright
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

///

//using System.Data.OracleClient;
//using IBM;
//using IBM.Data;
//using IBM.Data.Informix;


/** 
 运行模式
*/
public enum CCBPMRunModel
{
	/** 
	 单机版
	*/
	Single,
	/** 
	 集团模式
	*/
	GroupInc,
	/** 
	 多租户模式
	*/
	SAAS;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static CCBPMRunModel forValue(int value) throws Exception
	{
		return values()[value];
	}
}