package BP;

import java.io.FileInputStream;      
import java.io.FileNotFoundException;      
import java.io.IOException;      
import java.io.InputStream;      
import java.util.ArrayList;
import java.util.Date;      
import java.util.HashMap;      
import java.util.List;
import java.util.Map;      
     
















































import org.apache.poi.hssf.usermodel.HSSFCell;      
import org.apache.poi.hssf.usermodel.HSSFRow;      
import org.apache.poi.hssf.usermodel.HSSFSheet;      
import org.apache.poi.hssf.usermodel.HSSFWorkbook;      
import org.apache.poi.poifs.filesystem.POIFSFileSystem;      
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import BP.DA.DBAccess;
import BP.DA.DBUrl;
import BP.DA.DataTable;
import BP.DA.Log;
import BP.Sys.SystemConfig;
     
/**    
 * 操作Excel表格的功能类    
 * @author：    
 * @version 1.0    
 */     
public class ExcelReader {      
    private POIFSFileSystem fs;      
    private HSSFWorkbook wb;      
    private HSSFSheet sheet;      
    private HSSFRow row;      
    /**    
     * 读取Excel表格表头的内容    
     * @param InputStream    
     * @return String 表头内容的数组    
     *     
     */     
    public String[] readExcelTitle(InputStream is) {      
        try {      
            fs = new POIFSFileSystem(is);      
            wb = new HSSFWorkbook(fs);      
        } catch (IOException e) {      
            e.printStackTrace();      
        }      
        sheet = wb.getSheetAt(0);      
        row = sheet.getRow(sheet.getFirstRowNum());      
        //标题总列数      
        int colNum = row.getPhysicalNumberOfCells();      
        String[] title = new String[colNum];      
        for (int i=0; i<colNum; i++) {      
            title[i] = getStringCellValue(row.getCell(1));      
        }      
        return title;      
    }      
          
    /**    
     * 读取Excel数据内容    
     * @param InputStream    
     * @return Map 包含单元格数据内容的Map对象    
     */     
    public Map<Integer,String> readExcelContent(InputStream is) {      
        Map<Integer,String> content = new HashMap<Integer,String>();      
        String str = "";      
        try {      
            fs = new POIFSFileSystem(is);      
            wb = new HSSFWorkbook(fs);      
        } catch (IOException e) {      
            e.printStackTrace();      
        }      
        sheet = wb.getSheetAt(0);      
        //得到总行数      
        int rowNum = sheet.getLastRowNum();   
//        System.out.println(rowNum);
        row = sheet.getRow(0);      
        int colNum = row.getPhysicalNumberOfCells();      
        //正文内容应该从第二行开始,第一行为表头的标题      
        for (int i = 1; i <= rowNum; i++) {      
            row = sheet.getRow(i);      
            int j = 0;      
            while (j<colNum) {      
        //每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据      
        //也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean      
                str += getStringCellValue(row.getCell((short) j)).trim() + "-";      
                j ++;      
            }      
            content.put(i, str);      
            str = "";      
        }      
        return content;      
    }      
          
    /**    
     * 获取单元格数据内容为字符串类型的数据    
     * @param cell Excel单元格    
     * @return String 单元格数据内容    
     */     
    private String getStringCellValue(HSSFCell cell) {      
        String strCell = "";      
        switch (cell.getCellType()) {      
        case HSSFCell.CELL_TYPE_STRING:      
            strCell = cell.getStringCellValue();      
            break;      
        case HSSFCell.CELL_TYPE_NUMERIC:      
            strCell = String.valueOf(cell.getNumericCellValue());      
            break;      
        case HSSFCell.CELL_TYPE_BOOLEAN:      
            strCell = String.valueOf(cell.getBooleanCellValue());      
            break;      
        case HSSFCell.CELL_TYPE_BLANK:      
            strCell = "";      
            break;      
        default:      
            strCell = "";      
            break;      
        }      
        if (strCell.equals("") || strCell == null) {      
            return "";      
        }      
        if (cell == null) {      
            return "";      
        }      
        return strCell;      
    }      
          
    /**    
     * 获取单元格数据内容为日期类型的数据    
     * @param cell Excel单元格    
     * @return String 单元格数据内容    
     */     
    private String getDateCellValue(HSSFCell cell) {      
        String result = "";      
        try {      
            int cellType = cell.getCellType();      
            if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {      
                Date date = cell.getDateCellValue();      
                result = (date.getYear() + 1900) + "-" + (date.getMonth() + 1)       
                + "-" + date.getDate();      
            } else if (cellType == HSSFCell.CELL_TYPE_STRING) {      
                String date = getStringCellValue(cell);      
                result = date.replaceAll("[年月]", "-").replace("日", "").trim();      
            } else if (cellType == HSSFCell.CELL_TYPE_BLANK) {      
                result = "";      
            }      
        } catch (Exception e) {      
            Log.DefaultLogWriteLineError("日期格式不正确!" + e.getMessage());
        }      
        return result;      
    }      
          
//    public static void main(String[] args) {      
//        try {      
//            //对读取Excel表格标题测试      
//            InputStream is = new FileInputStream("C:\\Users\\Administrator\\Desktop\\测试.xls");      
//            ExcelReader excelReader = new ExcelReader();      
//            String[] title = excelReader.readExcelTitle(is);
//           
//            System.out.println("获得Excel表格的标题:");      
//            for (String s : title) {      
//                System.out.print(s + " ");      
//            }      
//            
////            System.out.println();     
////            //对读取Excel表格内容测试      
////            InputStream is2 = new FileInputStream("C:\\Users\\Administrator\\Desktop\\测试.xls");      
////            Map<Integer,String> map = excelReader.readExcelContent(is2);      
////            System.out.println("获得Excel表格的内容:");      
////            for (int i=1; i<=map.size(); i++) {      
////                System.out.println(map.get(i));      
////            }      
//        } catch (FileNotFoundException e) {      
//            System.out.println("未找到指定路径的文件!");      
//            e.printStackTrace();      
//        }      
//    } 
    
//    public static void main(String[] args) throws Exception {  
//    	try {  
//    		// 创建对Excel工作簿文件  
//    		//HSSFWorkbook wookbook= new HSSFWorkbook(new FileInputStream(filePath));  
//    		// 在Excel文档中，第一张工作表的缺省索引是0，  
//    		// 其语句为：HSSFSheet sheet = workbook.getSheetAt(0);  
//    		//  HSSFSheet sheet = wookbook.getSheet("Sheet1");  
//    		String filePath="C:\\Users\\Administrator\\Desktop\\测试.xls";
//    		Workbook book = null;  
//    		try {  
//    			book = new XSSFWorkbook(filePath);  
//    		} catch (Exception ex) {  
//    			book = new HSSFWorkbook(new FileInputStream(filePath));  
//    		}  
//
//    		Sheet sheet = book.getSheet("Sheet1");  
//    		//获取到Excel文件中的所有行数  
//    		int rows = sheet.getPhysicalNumberOfRows();
//    		
//    		//遍历行  
//    		for (int i = sheet.getFirstRowNum()+1; i < sheet.getFirstRowNum()+rows; i++) {  
//    			// 读取左上端单元格  
//    			Row row = sheet.getRow(i);  
//    			// 行不为空  
//    			if (row != null) {  
//    				//获取到Excel文件中的所有的列  
//    				int cells = row.getPhysicalNumberOfCells();  
//    				String value = "";       
//    				//遍历列  
//    				for (int j = 0; j < cells; j++) {  
//    					//获取到列的值  
//    					Cell cell = row.getCell(j);  
//    					if (cell != null) {  
//    						switch (cell.getCellType()) {  
//    						case Cell.CELL_TYPE_FORMULA:  
//    							break;  
//    						case Cell.CELL_TYPE_NUMERIC:  
//    							value += cell.getNumericCellValue() + "&";          
//    							break;    
//    						case Cell.CELL_TYPE_STRING:  
//    							value += cell.getStringCellValue() + "&";  
//    							break;  
//    						default:  
//    							value += "0";  
//    							break;  
//    						}  
//    					}        
//    				}  
//    				// 将数据插入到mysql数据库中  
//    				List<Object> ls=new ArrayList<Object>();
//    				String[] val = value.split("&");  
//    				for(int j=0;j<val.length;j++){  
//    					//每一行列数中的值遍历出来  
//    					  ls.add(val[j]);
//    				}  
//    				String BillNo=(String) ls.get(6);
//    				String CDT=(String) ls.get(3);
//    				int CWorkID=0;
//    				String Emps=(String) ls.get(31);
//    				int FID=0;
//    				String FK_Dept=(String) ls.get(0);
//    				int FlowDaySpan=0;
//    				//参与人
//    				String FlowEmps=(String) ls.get(1);
//    				String FlowEnder=(String) ls.get(2);
//    				String FlowEnderRDT=CDT;
//    				int FlowEndNode=12403;
//    				String FlowStarter=(String) ls.get(4);
//    				String FlowStartRDT=(String) ls.get(5);
//    				int MyNum=1;
//    				SystemConfig.ReadConfigFile(new ExcelReader().getClass().getResourceAsStream("/conf/web.properties"));
//    				String sql="update Sys_Serial set IntVal=IntVal+1 where CfgKey='WorkID'";
//    				int num=DBAccess.RunSQL(sql);
//    				int OID;
//    				int PNodeID=0;
//    				int PWorkID=0;
//    				String RDT=CDT;
//    				String Rec=FlowEmps;
//    				int WFSta=1;
//    				int WFState=3;
//    				String BeiZhu=(String) ls.get(24);
//    				System.out.println(BeiZhu);
//    				int DuiXiangLeiXing=Integer.parseInt(ls.get(19).toString());
//    				String FK_NY=CDT.substring(0,CDT.indexOf("-"));
//    				String JiaTingQingKuang=(String) ls.get(23);
//    				int JiaTingRenKou=Integer.parseInt(ls.get(17).toString());
//    				String JieDaoShenHeRen=(String) ls.get(27);
//    				String JieDaoShenHeRiQi=(String) ls.get(25);
//    				String JieDaoShenHeYiJian=(String) ls.get(26);
//    				int JieDaoXiangZhen=0;
//    				String KaiHuXing=(String) ls.get(20);
//    				String LianXiDianHua=(String) ls.get(15);
//    				String NianLing=(String) ls.get(14);
//    				String QuXian=(String) ls.get(9);
//    				String ShenFenZhengHao=(String) ls.get(16);
//    				String ShenQingDanHao=(String) ls.get(6);
//    				String TianXinQuShenHeRen=(String) ls.get(28);
//    				String TianXinQuShenHeRiQi=(String) ls.get(30);
//    				String TianXinQuShenHeYiJian=(String) ls.get(29);
//    				String Title=FlowStarter+RDT+"发起";
//    				int XingBie=Integer.parseInt(ls.get(13).toString());
//    				String XingMing=(String) ls.get(12);
//    				String YinXingZhangHu=(String) ls.get(21);
//    				String ZhuZhi=(String) ls.get(18);
//    				int DuiXiangLeiXing1=0;
//    				String YinXingHuMing=(String) ls.get(21);
//    				String LuRuZhangHao=(String) ls.get(8);
//    				String NianDiShenBao=(String) ls.get(7);
//    				String FK_JD=(String) ls.get(10);
//    				String FK_QX=(String) ls.get(11);
//    				String FK_QX1=(String) ls.get(9);
//    				String FlowStarterName=(String) ls.get(32);
//    				String FK_FlowSort="01";
//    				String FK_Flow="124";
//    				String FK_Node="12403";
//    				String NodeName="天心区审核";
//    				String FlowName="天心区民政局春节慰问流程";
//    				int PRI=1;
//    				int TodoEmpsNum=1;
//    				int TaskSta=0;
//    				int PFID=0;
//    				String DeptName="人力资源部";
//    				String FaQiRenEn=FlowEmps.split("@")[1].substring(0,FlowEmps.split("@")[1].indexOf(","));
//    				String FaQiRenCh=FlowEmps.split("@")[1].substring(FlowEmps.split("@")[1].indexOf(",")+1);
//    				String JieShouRenEn=FlowEmps.split("@")[2].substring(0,FlowEmps.split("@")[2].indexOf(","));
//    				String JieShouRenCh=FlowEmps.split("@")[2].substring(FlowEmps.split("@")[2].indexOf(",")+1);
//    				String JieShuRenEn=FlowEmps.split("@")[3].substring(0,FlowEmps.split("@")[3].indexOf(","));
//    				String JieShuRenCh=FlowEmps.split("@")[3].substring(FlowEmps.split("@")[3].indexOf(",")+1);
//    				String Exer1=FlowEmps.split("@")[1];
//    				String Exer2=FlowEmps.split("@")[2];
//    				String Exer3=FlowEmps.split("@")[3];
//    				if(num>0){
//    					String str="select IntVal from Sys_Serial where CfgKey='WorkID'";
//    					OID=Integer.parseInt(DBAccess.RunSQLReturnVal(str).toString());
//    					String trackSql1="INSERT INTO ND124Track(MyPK,ActionType,ActionTypeText,FID,WorkID,NDFrom,NDFromT,NDTo,NDToT,EmpFrom,EmpFromT,EmpTo,EmpToT,RDT,WorkTimeSpan ,Msg,NodeData,Tag,Exer) values("+DBAccess.GenerOIDByGUID()+",1,'前进',0,"+OID+",12401,'社区审核',12402,'街道审核','"+FaQiRenEn+"','"+FaQiRenCh+"','"+JieShouRenEn+"','"+JieShouRenCh+"','"+RDT+"',0,'','','','"+Exer1+"')";
//    					DBAccess.RunSQL(trackSql1);
//    					String trackSql2="INSERT INTO ND124Track(MyPK,ActionType,ActionTypeText,FID,WorkID,NDFrom,NDFromT,NDTo,NDToT,EmpFrom,EmpFromT,EmpTo,EmpToT,RDT,WorkTimeSpan ,Msg,NodeData,Tag,Exer) values("+DBAccess.GenerOIDByGUID()+",1,'前进',0,"+OID+",12402,'街道审核',12403,'天心区审核','"+JieShouRenEn+"','"+JieShouRenCh+"','"+JieShuRenEn+"','"+JieShuRenCh+"','"+RDT+"',0,'','','','"+Exer2+"')";
//    					DBAccess.RunSQL(trackSql2);
//    					String trackSql3="INSERT INTO ND124Track(MyPK,ActionType,ActionTypeText,FID,WorkID,NDFrom,NDFromT,NDTo,NDToT,EmpFrom,EmpFromT,EmpTo,EmpToT,RDT,WorkTimeSpan ,Msg,NodeData,Tag,Exer) values("+DBAccess.GenerOIDByGUID()+",8,'流程结束',0,"+OID+",12403,'天心区审核',12403,'天心区审核','"+JieShuRenEn+"','"+JieShuRenCh+"','"+JieShuRenEn+"','"+JieShuRenCh+"','"+RDT+"',0,'流程已经走到最后一个节点，流程成功结束。','','','"+Exer3+"')";
//    					DBAccess.RunSQL(trackSql3);
//    					int f=DBAccess.RunSQL("INSERT INTO ND124Rpt(BillNo,CDT,CWorkID ,Emps,FID,FK_Dept,FlowDaySpan,FlowEmps ,FlowEnder,FlowEnderRDT,FlowEndNode,FlowStarter,FlowStartRDT,MyNum,OID,PNodeID,PWorkID,RDT,Rec,WFSta,WFState,BeiZhu,DuiXiangLeiXing,FK_NY,JiaTingQingKuang,JiaTingRenKou ,JieDaoShenHeRen,JieDaoShenHeRiQi,JieDaoShenHeYiJian,JieDaoXiangZhen,KaiHuXing,LianXiDianHua,NianLing ,QuXian,ShenFenZhengHao,ShenQingDanHao,TianXinQuShenHeRen,TianXinQuShenHeRiQi,TianXinQuShenHeYiJian,Title,XingBie ,XingMing,YinXingHuMing ,YinXingZhangHu,ZhuZhi ,DuiXiangLeiXing1,LuRuZhangHao,NianDiShenBao,FK_JD ,FK_QX ,FK_QX1) values('"+BillNo+"','"+CDT+"',"+CWorkID +",'"+Emps+"',"+FID+",'"+FK_Dept+"',"+FlowDaySpan+",'"+FlowEmps +"','"+FlowEnder+"','"+FlowEnderRDT+"','"+FlowEndNode+"','"+FlowStarter+"','"+FlowStartRDT+"',"+MyNum+","+OID+","+PNodeID+","+PWorkID+",'"+RDT+"','"+Rec+"',"+WFSta+","+WFState+",'"+BeiZhu+"',"+DuiXiangLeiXing+",'"+FK_NY+"','"+JiaTingQingKuang+"',"+JiaTingRenKou +",'"+JieDaoShenHeRen+"','"+JieDaoShenHeRiQi+"','"+JieDaoShenHeYiJian+"',"+JieDaoXiangZhen+","+KaiHuXing+",'"+LianXiDianHua+"',"+NianLing+","+QuXian+",'"+ShenFenZhengHao+"','"+ShenQingDanHao+"','"+TianXinQuShenHeRen+"','"+TianXinQuShenHeRiQi+"','"+TianXinQuShenHeYiJian+"','"+Title+"',"+XingBie +",'"+XingMing+"','"+YinXingHuMing +"','"+YinXingZhangHu+"','"+ZhuZhi+"',"+DuiXiangLeiXing1+",'"+LuRuZhangHao+"','"+NianDiShenBao+"','"+FK_JD+"','"+FK_QX+"','"+FK_QX1+"')");
//    					if(f>0){
//    						String ss="INSERT INTO WF_GenerWorkFlow(WorkID,FID,FK_FlowSort,FK_Flow,FlowName,Title,WFState,WFSta,Starter,StarterName,RDT,FK_Node,NodeName,FK_Dept,DeptName,PRI,SDTOfNode,SDTOfFlow,PWorkID,PNodeID,CWorkID,BillNo,TodoEmps,TodoEmpsNum,TaskSta,Emps,MyNum,PFID) values("+OID+","+FID+",'"+FK_FlowSort+"','"+FK_Flow+"','"+FlowName+"','"+Title+"',"+WFState+","+WFSta+",'"+FlowStarter+"','"+FlowStarterName+"','"+RDT+"',"+FK_Node+",'"+NodeName+"','"+FK_Dept+"','"+DeptName+"',"+PRI+",'"+CDT+"','"+CDT+"',"+PWorkID+","+PNodeID+","+CWorkID+" ,'"+BillNo+"','"+Emps+"',"+TodoEmpsNum+","+TaskSta+",'"+FlowEmps+"',"+MyNum+","+PFID+")";
//    						int k=DBAccess.RunSQL(ss);
//    						if(k>0){
//    							System.out.println("成功"+i);
//    						}else{
//    							System.out.println("失败"+i);
//    						}
//    					}
//    					
//    					
//    				}
//    				
//    				
//    			}  
//    		}  
//    	} catch (FileNotFoundException e) {  
//    		e.printStackTrace();  
//    	} catch (IOException e) {  
//    		e.printStackTrace();  
//    	}  
//    }
   
}    