package BP.WF.CloudWS;

import BP.WF.*;

//------------------------------------------------------------------------------
// <auto-generated>
//     此代码由工具生成。
//     运行时版本:4.0.30319.42000
//
//     对此文件的更改可能会导致不正确的行为，并且如果
//     重新生成代码，这些更改将会丢失。
// </auto-generated>
//------------------------------------------------------------------------------


//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [System.Diagnostics.DebuggerStepThroughAttribute()][System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")][System.ComponentModel.EditorBrowsableAttribute(System.ComponentModel.EditorBrowsableState.Advanced)][System.ServiceModel.MessageContractAttribute(WrapperName="GetFlowXMLResponse", WrapperNamespace="http://tempuri.org/", IsWrapped=true)] public partial class GetFlowXMLResponse
public class GetFlowXMLResponse
{

//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://tempuri.org/", Order=0)][System.Xml.Serialization.XmlElementAttribute(DataType="base64Binary")] public byte[] GetFlowXMLResult;
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: [System.ServiceModel.MessageBodyMemberAttribute(Namespace="http://tempuri.org/", Order=0)][System.Xml.Serialization.XmlElementAttribute(DataType="base64Binary")] public byte[] GetFlowXMLResult;
	public byte[] GetFlowXMLResult;

	public GetFlowXMLResponse()
	{
	}

//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: public GetFlowXMLResponse(byte[] GetFlowXMLResult)
	public GetFlowXMLResponse(byte[] GetFlowXMLResult)
	{
		this.GetFlowXMLResult = GetFlowXMLResult;
	}
}