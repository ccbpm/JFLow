package BP;

import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.Region;

import BP.DA.DBAccess;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.Sys.SystemConfig;

public class Text {

	public static void ditialExcelExport(){
		
		
		//String sql="select ND124Rpt.FK_QX as FK_JD,ND124Rpt.DuiXiangLeiXing,COUNT(*) as HuShu,SUM(ND128Rpt.FaFangJinE) as JinE from ND124Rpt,ND128Rpt where ND124Rpt.BillNo in(select * from f_split(ND128Rpt.YinCangYu,',')) and ND124Rpt.BillNo!='' group by ND124Rpt.FK_QX,ND124Rpt.DuiXiangLeiXing";
		//String sql="select TX_JD.Name as FK_JD,Sys_Enum.Lab,COUNT(*) as HuShu,SUM(ND128Rpt.FaFangJinE) as JinE from ND124Rpt,ND128Rpt,TX_JD,Sys_Enum where ND124Rpt.BillNo in(select * from f_split(ND128Rpt.YinCangYu,',')) and ND124Rpt.BillNo!='' and TX_JD.No=ND124Rpt.FK_QX and Sys_Enum.EnumKey='DuiXiangLeiXing' and Sys_Enum.IntKey=ND124Rpt.DuiXiangLeiXing group by Sys_Enum.Lab,TX_JD.Name";
		String sql="select (select Name from TX_QX where TX_QX.No= ND124Rpt.FK_QX1) FK_QX,(select lab from Sys_Enum where Sys_Enum.IntKey=ND124Rpt.DuiXiangLeiXing and Sys_Enum.EnumKey='duixiangleixing') DuiXiangLeiXing,COUNT(*) as HuShu,SUM(ND128Rpt.FaFangJinE) as JinE from ND124Rpt,ND128Rpt where ND124Rpt.BillNo in(select * from f_split(ND128Rpt.YinCangYu,',')) and ND124Rpt.BillNo!='' group by ND124Rpt.FK_QX1,ND124Rpt.DuiXiangLeiXing";
		
		DataTable dt=DBAccess.RunSQLReturnTable(sql);
		
		try {   
            HSSFWorkbook wb = new HSSFWorkbook();   
            HSSFSheet sheet = wb.createSheet("街道查询excel");   
            HSSFCellStyle style = wb.createCellStyle(); // 样式对象   
  
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直   
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平   
            HSSFRow row = sheet.createRow((short) 0);    
  
            //sheet.addMergedRegion(new Region(0, (short) 0, 1, (short) 0));   

            //合並單元格,下標從0開始
            sheet.addMergedRegion(new Region(0,(short)0,0,(short) (dt.Columns.size()-1)));     
            
            HSSFRow rowT = sheet.createRow(0);
            HSSFCell cellT = rowT.createCell((short)0);
            
            cellT.setCellValue("Excel");
            int count=0;
            Region cra1=null;
            for(int i=1;i<=dt.Rows.size();i++){
            	HSSFRow hr=sheet.createRow(i+count);
            	DataRow dr=dt.Rows.get(i-1);
            	for(int j=0;j<dr.columns.size();j++){
            		HSSFCell hc=hr.createCell(j);
            		hc.setCellValue(""+dr.getValue(dr.columns.get(j)));
            	}
            	if(i-1>0){
            		//判断本行街道名称与上一行街道名称是否相同   相同则合并单元格
            		if(dt.Rows.get(i-1).getValue(dt.Columns.get(0)).equals(dt.Rows.get(i-2).getValue(dt.Columns.get(0)))){
            			if(cra1==null){
            				cra1=new Region((short)(i-1+count), (short)0, (short)i+count,(short)0);
            			}
            			else{
            				cra1.setRowTo(cra1.getRowTo()+1);
            			}
            		}else{
            			if(cra1!=null){
            				sheet.shiftRows(sheet.getLastRowNum(), sheet.getLastRowNum(), 1,true,false);
            				
            				HSSFRow hr1=sheet.createRow(i+count);
            				HSSFCell hc1=hr1.createCell(1);
            				hc1.setCellValue("本级汇总：");
            				int sum=0;
            				double db=0;
            				for(int k=cra1.getRowFrom();k<=cra1.getRowTo();k++){
            					sum+=Integer.parseInt(sheet.getRow(k).getCell(2).getStringCellValue());
            					db+=Double.parseDouble(sheet.getRow(k).getCell(3).getStringCellValue());
            				}
            				hr1.createCell(2).setCellValue(sum);
            				hr1.createCell(3).setCellValue(db);
            				cra1.setRowTo(cra1.getRowTo()+1);
            				sheet.addMergedRegion(cra1);
            				
            				cra1=null;
            			}
            		}
            		
            		if(i==dt.Rows.size()){
            			if(cra1!=null){
            				
            				HSSFRow hr1=sheet.createRow(sheet.getLastRowNum()+1);
            				count=count+1;
            				HSSFCell hc1=hr1.createCell(1);
            				hc1.setCellValue("本级汇总：");
            				int sum=0;
            				double db=0;
            				for(int k=cra1.getRowFrom()-count;k<=cra1.getRowTo()-count;k++){
            					sum+=Integer.parseInt(dt.getValue(k, 2).toString());
            					db+=Double.parseDouble(dt.getValue(k, 3).toString());
            				}
            				hr1.createCell(2).setCellValue(sum);
            				hr1.createCell(3).setCellValue(db);
            				cra1.setRowTo(cra1.getRowTo()+1);
            				sheet.addMergedRegion(cra1);
            				
            				cra1=null;
            			}
            		}
            	}
            }
            HSSFRow hs=sheet.createRow(sheet.getLastRowNum()+1);
            sheet.addMergedRegion(new Region(dt.Rows.size()+1,(short)0,dt.Rows.size()+1,(short) 0));
            HSSFCell hc2=hs.createCell(0);
            hc2.setCellValue("总计：");
            int i=0;
            double j=0;
            for(DataRow dr:dt.Rows){
            	i+=Integer.parseInt(dr.getValue(2).toString());
            	j+=Double.parseDouble(dr.getValue(3).toString());
            }
            hs.createCell(2).setCellValue(i);
            hs.createCell(3).setCellValue(j);
            
            FileOutputStream fileOut = new FileOutputStream("d://workbook.xls");   
            wb.write(fileOut);   
            fileOut.close();   
            System.out.print("OK");   
        } catch (Exception ex) {   
            ex.printStackTrace();   
        }  
        
    }   
		
	
	public static void main(String[] args) throws Exception {
		SystemConfig.ReadConfigFile(new Text().getClass().getResourceAsStream("/conf/web.properties"));
		Text.ditialExcelExport();
	}
}
