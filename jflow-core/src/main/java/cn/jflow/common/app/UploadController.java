package cn.jflow.common.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import BP.DA.DBAccess;
import BP.WF.Glo;


@Controller
@RequestMapping(value="/DES")
public class UploadController {
	
	@RequestMapping(value="/uploadFile",method=RequestMethod.POST)  
    public void uploadFile(HttpServletResponse response,HttpServletRequest request,@RequestParam(value="fileupload", required=false) MultipartFile file) throws IOException{  
        byte[] bytes = file.getBytes();  
//        System.out.println(file.getOriginalFilename());  
        String uploadDir = Glo.getIntallPath() + "/Temp/";  
        File dirPath = new File(uploadDir);  
        if (!dirPath.exists()) {  
            dirPath.mkdirs();  
        }  
        String sep = System.getProperty("file.separator");  
        File uploadedFile = new File(uploadDir + sep  
                + file.getOriginalFilename());  
        FileCopyUtils.copy(bytes, uploadedFile);  
        String msg = "true";  
        response.getWriter().write(msg);  
        //数据库写入数据
        try {
            String filePath=Glo.getIntallPath() + "/Temp/"+file.getOriginalFilename();
    		Workbook book = null;  
    		try {  
    			book = new XSSFWorkbook(filePath);  
    		} catch (Exception ex) {  
    			book = new HSSFWorkbook(new FileInputStream(filePath));  
    		}  

    		Sheet sheet = book.getSheet("Sheet1");  
    		//获取到Excel文件中的所有行数  
    		int rows = sheet.getPhysicalNumberOfRows();
    		
    		//遍历行  
    		for (int i = sheet.getFirstRowNum()+1; i < sheet.getFirstRowNum()+rows; i++) {  
    			// 读取左上端单元格  
    			Row row = sheet.getRow(i);  
    			// 行不为空  
    			if (row != null) {  
    				//获取到Excel文件中的所有的列  
    				int cells = row.getPhysicalNumberOfCells();  
    				String value = "";       
    				//遍历列  
    				for (int j = 0; j < cells; j++) {  
    					//获取到列的值  
    					Cell cell = row.getCell(j);  
    					if (cell != null) {  
    						switch (cell.getCellType()) {  
    						case Cell.CELL_TYPE_FORMULA:  
    							break;  
    						case Cell.CELL_TYPE_NUMERIC:  
    							value += cell.getNumericCellValue() + "&";          
    							break;    
    						case Cell.CELL_TYPE_STRING:  
    							value += cell.getStringCellValue() + "&";  
    							break;  
    						default:  
    							value += "0";  
    							break;  
    						}  
    					}        
    				}  
    				// 将数据插入到mysql数据库中  
    				List<Object> ls=new ArrayList<Object>();
    				String[] val = value.split("&");  
    				for(int j=0;j<val.length;j++){  
    					//每一行列数中的值遍历出来  
    					  ls.add(val[j]);
    				}  
    				//编号
    				String BillNo=(String) ls.get(6);
    				//时间
    				String CDT=(String) ls.get(3);
    				int CWorkID=0;
    				int FID=0;
    				//部门编号
    				String FK_Dept=(String) ls.get(0);
    				int FlowDaySpan=0;
    				//参与人
    				String FlowEmps=(String) ls.get(1);
    				String[] emps=FlowEmps.split("@");
    				String FlowEnder=emps[emps.length-1].substring(emps[emps.length-1].indexOf(",")+1);
    				
    				String Emps=emps[emps.length-1].substring(0,emps[emps.length-1].indexOf(","));
    				String FlowEnderRDT=CDT;
    				int FlowEndNode=12403;
    				String FlowStarter=emps[1].substring(0,emps[1].indexOf(","));
    				String FlowStartRDT=(String) ls.get(5);
    				int MyNum=1;
    				String sql="update Sys_Serial set IntVal=IntVal+1 where CfgKey='WorkID'";
    				int num=DBAccess.RunSQL(sql);
    				int OID;
    				int PNodeID=0;
    				int PWorkID=0;
    				String RDT=CDT;
    				String Rec=FlowEmps;
    				int WFSta=1;
    				int WFState=3;
    				String BeiZhu=(String) ls.get(24);
//    				System.out.println(BeiZhu);
    				int DuiXiangLeiXing=Integer.parseInt(ls.get(19).toString());
    				String FK_NY=CDT.substring(0,CDT.indexOf("-"));
    				String JiaTingQingKuang=(String) ls.get(23);
    				int JiaTingRenKou=Integer.parseInt(ls.get(17).toString());
    				String JieDaoShenHeRen=(String) ls.get(27);
    				String JieDaoShenHeRiQi=(String) ls.get(25);
    				String JieDaoShenHeYiJian=(String) ls.get(26);
    				int JieDaoXiangZhen=0;
    				String KaiHuXing=(String) ls.get(20);
    				String LianXiDianHua=(String) ls.get(15);
    				String NianLing=(String) ls.get(14);
    				String QuXian=(String) ls.get(9);
    				String ShenFenZhengHao=(String) ls.get(16);
    				String ShenQingDanHao=(String) ls.get(6);
    				String TianXinQuShenHeRen=(String) ls.get(28);
    				String TianXinQuShenHeRiQi=(String) ls.get(30);
    				String TianXinQuShenHeYiJian=(String) ls.get(29);
    				String Title=emps[1]+RDT+"发起";
    				int XingBie=Integer.parseInt(ls.get(13).toString());
    				String XingMing=(String) ls.get(12);
    				String YinXingZhangHu=(String) ls.get(21);
    				String ZhuZhi=(String) ls.get(18);
    				int DuiXiangLeiXing1=0;
    				String YinXingHuMing=(String) ls.get(21);
    				String LuRuZhangHao=(String) ls.get(8);
    				String NianDiShenBao=(String) ls.get(7);
    				String FK_JD=(String) ls.get(10);
    				String FK_QX=(String) ls.get(11);
    				String FK_QX1=(String) ls.get(9);
    				String FlowStarterName=emps[1].substring(emps[1].indexOf(",")+1);
    				String FK_FlowSort="01";
    				String FK_Flow="124";
    				String FK_Node="12403";
    				String NodeName="天心区审核";
    				String FlowName="天心区民政局春节慰问流程";
    				int PRI=1;
    				int TodoEmpsNum=1;
    				int TaskSta=0;
    				int PFID=0;
    				String DeptName=DBAccess.RunSQLReturnString("select Name from Port_Dept where No="+Integer.parseInt(FK_Dept));
    	
    				String FaQiRenEn=FlowEmps.split("@")[1].substring(0,FlowEmps.split("@")[1].indexOf(","));
    				String FaQiRenCh=FlowEmps.split("@")[1].substring(FlowEmps.split("@")[1].indexOf(",")+1);
    				String JieShouRenEn=FlowEmps.split("@")[2].substring(0,FlowEmps.split("@")[2].indexOf(","));
    				String JieShouRenCh=FlowEmps.split("@")[2].substring(FlowEmps.split("@")[2].indexOf(",")+1);
    				String JieShuRenEn=FlowEmps.split("@")[3].substring(0,FlowEmps.split("@")[3].indexOf(","));
    				String JieShuRenCh=FlowEmps.split("@")[3].substring(FlowEmps.split("@")[3].indexOf(",")+1);
    				String Exer1=FlowEmps.split("@")[1];
    				String Exer2=FlowEmps.split("@")[2];
    				String Exer3=FlowEmps.split("@")[3];
    				if(num>0){
    					String str="select IntVal from Sys_Serial where CfgKey='WorkID'";
    					OID=Integer.parseInt(DBAccess.RunSQLReturnVal(str).toString());
    					String trackSql1="INSERT INTO ND124Track(MyPK,ActionType,ActionTypeText,FID,WorkID,NDFrom,NDFromT,NDTo,NDToT,EmpFrom,EmpFromT,EmpTo,EmpToT,RDT,WorkTimeSpan ,Msg,NodeData,Tag,Exer) values("+DBAccess.GenerOIDByGUID()+",1,'前进',0,"+OID+",12401,'社区审核',12402,'街道审核','"+FaQiRenEn+"','"+FaQiRenCh+"','"+JieShouRenEn+"','"+JieShouRenCh+"','"+RDT+"',0,'','','','"+Exer1+"')";
    					DBAccess.RunSQL(trackSql1);
    					String trackSql2="INSERT INTO ND124Track(MyPK,ActionType,ActionTypeText,FID,WorkID,NDFrom,NDFromT,NDTo,NDToT,EmpFrom,EmpFromT,EmpTo,EmpToT,RDT,WorkTimeSpan ,Msg,NodeData,Tag,Exer) values("+DBAccess.GenerOIDByGUID()+",1,'前进',0,"+OID+",12402,'街道审核',12403,'天心区审核','"+JieShouRenEn+"','"+JieShouRenCh+"','"+JieShuRenEn+"','"+JieShuRenCh+"','"+RDT+"',0,'','','','"+Exer2+"')";
    					DBAccess.RunSQL(trackSql2);
    					String trackSql3="INSERT INTO ND124Track(MyPK,ActionType,ActionTypeText,FID,WorkID,NDFrom,NDFromT,NDTo,NDToT,EmpFrom,EmpFromT,EmpTo,EmpToT,RDT,WorkTimeSpan ,Msg,NodeData,Tag,Exer) values("+DBAccess.GenerOIDByGUID()+",8,'流程结束',0,"+OID+",12403,'天心区审核',12403,'天心区审核','"+JieShuRenEn+"','"+JieShuRenCh+"','"+JieShuRenEn+"','"+JieShuRenCh+"','"+RDT+"',0,'流程已经走到最后一个节点，流程成功结束。','','','"+Exer3+"')";
    					DBAccess.RunSQL(trackSql3);
    					int f=DBAccess.RunSQL("INSERT INTO ND124Rpt(BillNo,CDT,CWorkID ,Emps,FID,FK_Dept,FlowDaySpan,FlowEmps ,FlowEnder,FlowEnderRDT,FlowEndNode,FlowStarter,FlowStartRDT,MyNum,OID,PNodeID,PWorkID,RDT,Rec,WFSta,WFState,BeiZhu,DuiXiangLeiXing,FK_NY,JiaTingQingKuang,JiaTingRenKou ,JieDaoShenHeRen,JieDaoShenHeRiQi,JieDaoShenHeYiJian,JieDaoXiangZhen,KaiHuXing,LianXiDianHua,NianLing ,QuXian,ShenFenZhengHao,ShenQingDanHao,TianXinQuShenHeRen,TianXinQuShenHeRiQi,TianXinQuShenHeYiJian,Title,XingBie ,XingMing,YinXingHuMing ,YinXingZhangHu,ZhuZhi ,DuiXiangLeiXing1,LuRuZhangHao,NianDiShenBao,FK_JD ,FK_QX ,FK_QX1) values('"+BillNo+"','"+CDT+"',"+CWorkID +",'"+Emps+"',"+FID+",'"+FK_Dept+"',"+FlowDaySpan+",'"+FlowEmps +"','"+FlowEnder+"','"+FlowEnderRDT+"','"+FlowEndNode+"','"+FlowStarter+"','"+FlowStartRDT+"',"+MyNum+","+OID+","+PNodeID+","+PWorkID+",'"+RDT+"','"+Rec+"',"+WFSta+","+WFState+",'"+BeiZhu+"',"+DuiXiangLeiXing+",'"+FK_NY+"','"+JiaTingQingKuang+"',"+JiaTingRenKou +",'"+JieDaoShenHeRen+"','"+JieDaoShenHeRiQi+"','"+JieDaoShenHeYiJian+"',"+JieDaoXiangZhen+","+KaiHuXing+",'"+LianXiDianHua+"',"+NianLing+","+QuXian+",'"+ShenFenZhengHao+"','"+ShenQingDanHao+"','"+TianXinQuShenHeRen+"','"+TianXinQuShenHeRiQi+"','"+TianXinQuShenHeYiJian+"','"+Title+"',"+XingBie +",'"+XingMing+"','"+YinXingHuMing +"','"+YinXingZhangHu+"','"+ZhuZhi+"',"+DuiXiangLeiXing1+",'"+LuRuZhangHao+"','"+NianDiShenBao+"','"+FK_JD+"','"+FK_QX+"','"+FK_QX1+"')");
    					if(f>0){
    						String ss="INSERT INTO WF_GenerWorkFlow(WorkID,FID,FK_FlowSort,FK_Flow,FlowName,Title,WFState,WFSta,Starter,StarterName,RDT,FK_Node,NodeName,FK_Dept,DeptName,PRI,SDTOfNode,SDTOfFlow,PWorkID,PNodeID,CWorkID,BillNo,TodoEmps,TodoEmpsNum,TaskSta,Emps,MyNum,PFID) values("+OID+","+FID+",'"+FK_FlowSort+"','"+FK_Flow+"','"+FlowName+"','"+Title+"',"+WFState+","+WFSta+",'"+FlowStarter+"','"+FlowStarterName+"','"+RDT+"',"+FK_Node+",'"+NodeName+"','"+FK_Dept+"','"+DeptName+"',"+PRI+",'"+CDT+"','"+CDT+"',"+PWorkID+","+PNodeID+","+CWorkID+" ,'"+BillNo+"','"+Emps+"',"+TodoEmpsNum+","+TaskSta+",'"+FlowEmps+"',"+MyNum+","+PFID+")";
    						int k=DBAccess.RunSQL(ss);
    						if(k>0){
    							System.out.println("成功"+i);
    						}else{
    							System.out.println("失败"+i);
    						}
    					}
    				}
    			}  
    		} 
		} catch (Exception e) {
			System.err.println("写入失败");
		}
        
    } 

	
}
