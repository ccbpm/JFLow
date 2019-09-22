package BP.WF.CCInterface;

import BP.WF.*;

//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//     Runtime Version:4.0.30319.42000
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------


//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [System.Diagnostics.DebuggerStepThroughAttribute()][System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")] public partial class PortalInterfaceSoapClient: System.ServiceModel.ClientBase<BP.WF.CCInterface.PortalInterfaceSoap>, BP.WF.CCInterface.PortalInterfaceSoap
public class PortalInterfaceSoapClient extends System.ServiceModel.ClientBase<BP.WF.CCInterface.PortalInterfaceSoap> implements BP.WF.CCInterface.PortalInterfaceSoap
{

	public PortalInterfaceSoapClient()
	{
	}

	public PortalInterfaceSoapClient(String endpointConfigurationName)
	{
		super(endpointConfigurationName);
	}

	public PortalInterfaceSoapClient(String endpointConfigurationName, String remoteAddress)
	{
		super(endpointConfigurationName, remoteAddress);
	}

	public PortalInterfaceSoapClient(String endpointConfigurationName, System.ServiceModel.EndpointAddress remoteAddress)
	{
		super(endpointConfigurationName, remoteAddress);
	}

	public PortalInterfaceSoapClient(System.ServiceModel.Channels.Binding binding, System.ServiceModel.EndpointAddress remoteAddress)
	{
		super(binding, remoteAddress);
	}

	public final boolean SendToWebServices(String msgPK, String sender, String sendToEmpNo, String tel, String msgInfo, String title, String openUrl)
	{
		return super.getChannel().SendToWebServices(msgPK, sender, sendToEmpNo, tel, msgInfo, title, openUrl);
	}

	public final boolean SendToCCMSG(String msgPK, String sender, String sendToEmpNo, String tel, String msgInfo, String title, String openUrl)
	{
		return super.getChannel().SendToCCMSG(msgPK, sender, sendToEmpNo, tel, msgInfo, title, openUrl);
	}

	public final boolean SendWhen(String flowNo, int nodeID, long workid, String userNo, String userName)
	{
		return super.getChannel().SendWhen(flowNo, nodeID, workid, userNo, userName);
	}

	public final boolean FlowOverBefore(String flowNo, int nodeID, long workid, String userNo, String userName)
	{
		return super.getChannel().FlowOverBefore(flowNo, nodeID, workid, userNo, userName);
	}

	public final boolean SendToDingDing(String mypk, String sender, String sendToEmpNo, String tel, String msgInfo, String title, String openUrl)
	{
		return super.getChannel().SendToDingDing(mypk, sender, sendToEmpNo, tel, msgInfo, title, openUrl);
	}

	public final boolean SendToWeiXin(String mypk, String sender, String sendToEmpNo, String tel, String msgInfo, String title, String openUrl)
	{
		return super.getChannel().SendToWeiXin(mypk, sender, sendToEmpNo, tel, msgInfo, title, openUrl);
	}

	public final boolean SendToEmail(String mypk, String sender, String sendToEmpNo, String email, String title, String maildoc)
	{
		return super.getChannel().SendToEmail(mypk, sender, sendToEmpNo, email, title, maildoc);
	}

	public final boolean SendToCCIM(String mypk, String sender, String sendToEmpNo, String tel, String msgInfo, String title, String openUrl)
	{
		return super.getChannel().SendToCCIM(mypk, sender, sendToEmpNo, tel, msgInfo, title, openUrl);
	}

	public final void Print(String billFilePath)
	{
		super.getChannel().Print(billFilePath);
	}

	public final boolean WriteUserSID(String miyue, String userNo, String sid)
	{
		return super.getChannel().WriteUserSID(miyue, userNo, sid);
	}

	public final int CheckUserNoPassWord(String userNo, String password)
	{
		return super.getChannel().CheckUserNoPassWord(userNo, password);
	}

	public final System.Data.DataTable GetDept(String deptNo)
	{
		return super.getChannel().GetDept(deptNo);
	}

	public final System.Data.DataTable GetDepts()
	{
		return super.getChannel().GetDepts();
	}

	public final System.Data.DataTable GetDeptsByParentNo(String parentDeptNo)
	{
		return super.getChannel().GetDeptsByParentNo(parentDeptNo);
	}

	public final System.Data.DataTable GetStations()
	{
		return super.getChannel().GetStations();
	}

	public final System.Data.DataTable GetStation(String stationNo)
	{
		return super.getChannel().GetStation(stationNo);
	}

	public final System.Data.DataTable GetEmps()
	{
		return super.getChannel().GetEmps();
	}

	public final System.Data.DataTable GetEmpsByDeptNo(String deptNo)
	{
		return super.getChannel().GetEmpsByDeptNo(deptNo);
	}

	public final System.Data.DataTable GetEmp(String no)
	{
		return super.getChannel().GetEmp(no);
	}

	public final System.Data.DataTable GetDeptEmp()
	{
		return super.getChannel().GetDeptEmp();
	}

	public final System.Data.DataTable GetEmpHisDepts(String empNo)
	{
		return super.getChannel().GetEmpHisDepts(empNo);
	}

	public final System.Data.DataTable GetEmpHisStations(String empNo)
	{
		return super.getChannel().GetEmpHisStations(empNo);
	}

	public final System.Data.DataTable GetDeptEmpStations()
	{
		return super.getChannel().GetDeptEmpStations();
	}

	public final System.Data.DataTable GenerEmpsByStations(String stationNos)
	{
		return super.getChannel().GenerEmpsByStations(stationNos);
	}

	public final System.Data.DataTable GenerEmpsByDepts(String deptNos)
	{
		return super.getChannel().GenerEmpsByDepts(deptNos);
	}

	public final System.Data.DataTable GenerEmpsBySpecDeptAndStats(String deptNo, String stations)
	{
		return super.getChannel().GenerEmpsBySpecDeptAndStats(deptNo, stations);
	}

	public final String SendSuccess(String flowNo, int nodeID, long workid, String userNo, String userName)
	{
		return super.getChannel().SendSuccess(flowNo, nodeID, workid, userNo, userName);
	}
}