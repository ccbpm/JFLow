package BP.WF.DTS;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.DA.Log;
import BP.En.Method;
import BP.WF.Flow;
import BP.WF.Flows;
import BP.WF.ImpFlowTempleteModel;
import BP.WF.Template.FlowSort;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;

/** 
 修改人员编号 的摘要说明
 
*/
public class RelFlowTemplateToSubInc extends Method
{
	private static final String Flow = null;
	/** 
	 不带有参数的方法
	 
	*/
	public RelFlowTemplateToSubInc()
	{
		this.Title = "发布流程模版到子公司";
		this.Help = "集团公司的流程模版发布到子公司里面去.";
	}
	 
	/** 
	 当前的操纵员是否可以执行这个方法
	 * @throws Exception 
	 
	*/
	@Override
	public boolean getIsCanDo() throws Exception
	{
		 
			if (BP.Web.WebUser.getNo().equals("admin"))
			{
				return true;
			}
			else
			{
				return false;
			}
	 
	}
	/** 
	 执行
	 
	 @return 返回执行结果
	 * @throws Exception 
	*/
	@Override
	public Object Do() throws Exception
	{

		 //找到根目录.
        String sql = "SELECT No FROM wf_flowsort where parentNo='0'";
        String rootNo = DBAccess.RunSQLReturnString(sql, null);
        if (rootNo == null)
            return "没有找到根目录节点"+sql;

        //求出分公司集合(组织结构集合)
        sql = "SELECT No,Name FROM Port_Dept where xxx=000";
        DataTable dtInc = DBAccess.RunSQLReturnTable(sql);

        //取得所有根目录下的流程模版.
        Flows fls = new Flows(rootNo);

        String infos = "";

        for (Flow fl : fls.ToJavaList())
        {
            //不是模版.
            if (fl.getFlowMark().equals("") ==true)
                continue;

            infos += "@开始发布流程模版:" + fl.getName() +" 标记:"+fl.getFlowMark();

            //把该模版发布到子公司里面去.
            for (DataRow dr : dtInc.Rows)
            {
                String incNo = dr.getValue("No").toString();
                String incName = dr.getValue("Name").toString();

                //检查该公司是否创建了树节点, 如果没有就插入一个.
                BP.WF.Template.FlowSort fs = new FlowSort();
                fs.setNo(incNo);
                if (fs.RetrieveFromDBSources() == 0)
                {
                    fs.setName(incName);
                    fs.setOrgNo(incNo);
                    fs.setParentNo(rootNo);
                    fs.Insert();
                }

                //开始把该模版发布到该公司下.
                Flow flInc = new Flow();
                int num= flInc.Retrieve(BP.WF.Template.FlowAttr.FK_FlowSort, incNo,
                    BP.WF.Template.FlowAttr.FlowMark, fl.getFlowMark());
                if (num == 1)
                    continue; //模版已经存在.

                String filePath = fl.GenerFlowXmlTemplete();
                try {
                	//作为一个新的流程编号，导入该流程树的子节点下.
                	BP.WF.Flow.DoLoadFlowTemplate(incNo,filePath, ImpFlowTempleteModel.AsNewFlow);
				} catch (Exception e) {
					Log.DebugWriteError(e.getMessage());
					infos += e.getMessage();
				}

                infos += "@模版:" + fl.getName() + " 标记:" + fl.getFlowMark()+" 已经发布到子公司:"+incName;
            }
        }

        return infos;
	}
	
	@Override
	public void Init(){
		
	}
}