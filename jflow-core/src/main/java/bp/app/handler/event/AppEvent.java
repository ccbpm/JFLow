package bp.app.handler.event;

import bp.app.handler.Entity.Emp;
import bp.app.handler.Entity.EmpAttr;
import bp.app.handler.Entity.XZCJ;
import bp.app.handler.Entity.XZCJAttr;
import bp.da.DataType;
import bp.da.LogType;
import bp.sys.EventListFlow;
import bp.sys.EventListNode;
import bp.sys.GEEntity;
import bp.wf.GenerWorkFlow;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AppEvent extends bp.sys.base.EventBase{
    public AppEvent() throws Exception{
        this.setTitle("行政裁决系统通用事件");
    }

    @Override
    public void Do() {
        switch(this.getEventSource())
        {
            case EventListNode.SendSuccess://发送成功后
                DoEventBase();
                break;
            case EventListNode.UndoneAfter://撤销发送之后
                break;
            case EventListNode.SendWhen://发送前
                break;
            case EventListNode.ReturnAfter://退回后
                break;
            case EventListFlow.FlowOverAfter://流程结束后
                DoEventBase();
                break;
        }
    }
    /**
     * 暂定为通用发送后处理事件
     */
    public void DoEventBase(){
        try{
            GenerWorkFlow gwf=new GenerWorkFlow(this.getWorkID());

            switch (gwf.getFK_Node()){
                case 103://审查通过
                    AgreeOrDisagreeToCase("Init",gwf);
                    break;
                case 105: //同意立案
                    AgreeOrDisagreeToCase("Agree",gwf);
                    break;
                case 104://不同意立案
                    AgreeOrDisagreeToCase("Disagree",gwf);
                    break;
                case 108://立案流程结束，进入调查取证阶段
                    AgreeOrDisagreeToCase("",gwf);
                    break;
                case 205://设置待审理
                    AgreeOrDisagreeToCase("PendingTrial",gwf);
                    break;
            }
        }
        catch (Exception ex){
            //记录错误日志
            bp.da.Log.DefaultLogWriteLine(LogType.Info, "执行DoEventBase失败:" + ex.getMessage());
        }
    }

    /**
     * 同意or不同意立案
     * @param gwf
     * @throws Exception
     */
    public void AgreeOrDisagreeToCase(String AgreeOrDisagree,GenerWorkFlow gwf) throws Exception{
        //获取立案申请表中的数据
        GEEntity geEntity=new GEEntity("Frm_LASQ");
        geEntity.setOID(gwf.getWorkID());
        geEntity.Retrieve();
        CaseEvent(AgreeOrDisagree,geEntity,gwf);
    }

    /**
     * 立案相关操作
     * @param AgreeOrDisagree
     * @param geEntity
     * @param gwf
     * @throws Exception
     */
    public void CaseEvent(String AgreeOrDisagree,
                          GEEntity geEntity,
                          GenerWorkFlow gwf) throws Exception{
        Boolean isUpdate=false;
        XZCJ xzcj=new XZCJ();
        //强制更新缓存
        xzcj.CheckPhysicsTable();
        if(!xzcj.IsExit(XZCJAttr.AJBH,gwf.getBillNo())){
            isUpdate=true;
        }
        //案件编号
        xzcj.setAJBH(gwf.getBillNo());
        //案件标题
        xzcj.setTitle(gwf.getTitle());
        //请求人
        xzcj.setQQR(geEntity.GetValStringByKey("XM"));
        //被请求人
        xzcj.setBQQR(geEntity.GetValStringByKey("BQQRXM"));
        //请求日期
        xzcj.setQQRQ(gwf.getRDT());
        //专利号
        xzcj.setZLH(geEntity.GetValStringByKey("ZLH"));
        //专利名称
        xzcj.setZLMC(geEntity.GetValStringByKey("ZLMC"));
        //专利权人
        xzcj.setZLQR(geEntity.GetValStringByKey("ZLQR"));
        //专利申请日期
        xzcj.setZLSQRQ(geEntity.GetValStringByKey("ZLSQRQ"));
        //授权公告日
        xzcj.setSQGGR(geEntity.GetValStringByKey("SQGGR"));
        //授权公告号
        xzcj.setSQGGH(geEntity.GetValStringByKey("SQGGH"));
        //证书号
        xzcj.setZSH(geEntity.GetValStringByKey("ZSH"));

        //审查通过
        if(AgreeOrDisagree.equals("Init")){
            //案件状态
            xzcj.setState("0");
            if(isUpdate) {
                xzcj.setMyPK(UUID.randomUUID().toString());
                xzcj.Insert();
            }
            else
                xzcj.Update();
        }
        //同意立案
        else if(AgreeOrDisagree.equals("Agree")){
            //案件状态
            xzcj.setState("1");
            if(isUpdate) {
                xzcj.setMyPK(UUID.randomUUID().toString());
                //立案时间
                xzcj.setLASJ(DataType.getCurrentDateTime());
                xzcj.Insert();
            }
            else
                xzcj.Update();

            //判断是否需要生产被请求人临时登录帐号
            createUserNo(geEntity.GetValStringByKey("BQQRZJHM"),
                    geEntity.GetValStringByKey("BQQRXM"));
        }
        //不同意立案
        else if(AgreeOrDisagree.equals("Disagree")){
            //案件状态
            xzcj.setState("2");
            if(isUpdate) {
                xzcj.setMyPK(UUID.randomUUID().toString());
                xzcj.Insert();
            }
            else
                xzcj.Update();
        }
        //设置待审理
        else if(AgreeOrDisagree.equals("PendingTrial")){
            //案件状态
            xzcj.setState("4");
            if(isUpdate) {
                xzcj.setMyPK(UUID.randomUUID().toString());
                xzcj.Insert();
            }
            else
                xzcj.Update();
        }
        //调查取证
        else{
            //案件状态
            xzcj.setState("3");
            if(isUpdate) {
                xzcj.setMyPK(UUID.randomUUID().toString());
                xzcj.Insert();
            }
            else
                xzcj.Update();
        }
    }

    /**
     * 为没有系统帐号的被请求人自动生成帐号
     * @param openId
     * @param userName
     */
    public void createUserNo(String openId,String userName){
        try{
            Emp emp=new Emp();
            //判断是否存在身份证号，如果不存在，则创建
            if(!emp.IsExit(EmpAttr.OpenID,openId)) {
                //获取当前时间戳
                Date dt = new Date();
                SimpleDateFormat matter = new SimpleDateFormat("hhmm");
                String date = matter.format(dt);
                //临时帐号：bqqr+hhmm
                String userCode="bqqr"+date;
                emp.setNo(userCode);
                emp.setName(userName);
                emp.setOpenID(openId);
                //默认初始密码123456
                emp.setPass("123456");
                //外部用户所在部门，默认为外部单位
                emp.setFK_Dept("1099");
                emp.Insert();
            }
        }
        catch (Exception ex){
            //记录错误日志
            bp.da.Log.DefaultLogWriteLine(LogType.Info, "执行createUserNo失败:" + ex.getMessage());
        }
    }
}
