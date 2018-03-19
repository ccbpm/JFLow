package BP;

import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.Sys.SystemConfig;

public class Text1 {
	

	public static void ditialExcelExport(){
		
		
		//String sql="select ND124Rpt.FK_QX as FK_JD,ND124Rpt.FK_JD as FK_SQ,ND124Rpt.DuiXiangLeiXing,COUNT(*) as HuShu,SUM(ND128Rpt.FaFangJinE) as JinE from ND124Rpt,ND128Rpt where ND124Rpt.BillNo in(select * from f_split(ND128Rpt.YinCangYu,',')) and ND124Rpt.BillNo!='' group by ND124Rpt.FK_QX,ND124Rpt.FK_JD,ND124Rpt.DuiXiangLeiXing";
		
		String sql="select TX_JD.Name as FK_JD,TX_SQ.Name as FK_SQ,Sys_Enum.Lab,COUNT(*) as HuShu,SUM(ND128Rpt.FaFangJinE) as JinE from ND124Rpt,ND128Rpt,TX_JD,TX_SQ,Sys_Enum where ND124Rpt.BillNo in(select * from f_split(ND128Rpt.YinCangYu,',')) and ND124Rpt.BillNo!='' and Sys_Enum.EnumKey='DuiXiangLeiXing' and ND124Rpt.FK_QX=TX_JD.No and ND124Rpt.FK_JD=TX_SQ.No and ND124Rpt.DuiXiangLeiXing=Sys_Enum.IntKey group by Sys_Enum.Lab,TX_SQ.Name,TX_JD.Name";
		
		
		DataTable dt=DBAccess.RunSQLReturnTable(sql);
		
		try {   
            HSSFWorkbook wb = new HSSFWorkbook();   
            HSSFSheet sheet = wb.createSheet("街道查询excel"); 
            
            // 设置字体
            HSSFFont font = wb.createFont();
            font.setFontHeightInPoints((short) 20); //字体高度
            font.setColor(HSSFFont.COLOR_RED); //字体颜色
           font.setFontName("黑体"); //字体
           font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //宽度
           font.setItalic(true); //是否使用斜体
//         font.setStrikeout(true); //是否使用划线
           // 设置单元格类型
           HSSFCellStyle cellStyle = wb.createCellStyle();
           cellStyle.setFont(font);
           cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中中      cellStyle.setWrapText(true);    
            
            

            sheet.setVerticallyCenter(true);  

            HSSFRow rowT = sheet.createRow(0);
            rowT.createCell((short)0).setCellValue("街道");
            rowT.createCell((short)1).setCellValue("社区");
            rowT.createCell((short)2).setCellValue("对象类型");
            rowT.createCell((short)3).setCellValue("户数");
            rowT.createCell((short)4).setCellValue("金额");
  
       
            
            HSSFCellStyle cellstyle = (HSSFCellStyle) wb.createCellStyle();// 设置表头样式  
            cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 设置居中 
            
            //sheet.addMergedRegion(new Region(0, (short) 0, 1, (short) 0));   

            //合並單元格,下標從0開始
            //sheet.addMergedRegion(new Region(0,(short)0,0,(short) (dt.Columns.size()-1)));     
            
            
            int count=0;
            Region cra1=null;
            Region cra2=null;
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
            			
            			//判断社区是否相等
            			if(dt.Rows.get(i-1).getValue(dt.Columns.get(1)).equals(dt.Rows.get(i-2).getValue(dt.Columns.get(1)))){
	            			if(cra2==null){
	            				cra2=new Region((short)(i-1+count), (short)1, (short)i+count,(short)1);
	            			}else{
	            				cra2.setRowTo(cra2.getRowTo()+1);
	            			}
            			}else{
            				if(cra2!=null){
                				sheet.shiftRows(i+count, sheet.getLastRowNum(), 1,true,false);
                				HSSFRow hr1=sheet.createRow(i+count);
                				//添加行
                				cra1.setRowTo(cra1.getRowTo()+1);
                				count=count+1;
                				HSSFCell hc1=hr1.createCell(2);
                				hc1.setCellValue(sheet.getRow(cra2.getRowFrom()).getCell(cra2.getColumnFrom()).getStringCellValue()+"社区本级汇总：");
                				int sum=0;
                				double db=0;
                				for(int k=cra2.getRowFrom();k<=cra2.getRowTo();k++){
                					sum+=Integer.parseInt(sheet.getRow(k).getCell(3).getStringCellValue());
                					db+=Double.parseDouble(sheet.getRow(k).getCell(4).getStringCellValue());
                				}
                				hr1.createCell(3).setCellValue(sum);
                				hr1.createCell(4).setCellValue(db);
                				cra2.setRowTo(cra2.getRowTo()+1);
                				sheet.addMergedRegion(cra2);
                				cra2=null;
                			}
            			}
            			
            		}else{
            			if(cra2!=null){
            				sheet.shiftRows(i+count, sheet.getLastRowNum(), 1,true,false);
            				HSSFRow hr1=sheet.createRow(i+count);
            				//添加行
            				cra1.setRowTo(cra1.getRowTo()+1);
            				count=count+1;
            				HSSFCell hc1=hr1.createCell(2);
            				hc1.setCellValue(sheet.getRow(cra2.getRowFrom()).getCell(cra2.getColumnFrom()).getStringCellValue()+"社区本级汇总：");
            				int sum=0;
            				double db=0;
            				for(int k=cra2.getRowFrom()-count+1;k<=cra2.getRowTo()-count+1;k++){
            					sum+=Integer.parseInt(dt.getValue(k, 3).toString());
            					db+=Double.parseDouble(dt.getValue(k, 4).toString());
            				}
            				hr1.createCell(3).setCellValue(""+sum);
            				hr1.createCell(4).setCellValue(""+db);
            				cra2.setRowTo(cra2.getRowTo()+1);
            				sheet.addMergedRegion(cra2);
            				cra2=null;
            			}
            			
            			if(cra1!=null){
            			
            				sheet.shiftRows(i+count, sheet.getLastRowNum(), 1,true,false);
            				sheet.addMergedRegion(new Region(i+count,(short)1,i+count,(short)2));
            				HSSFRow hr1=sheet.createRow(i+count);
            				count=count+1;
            				HSSFCell hc1=hr1.createCell(1);
            				hc1.setCellValue(sheet.getRow(cra1.getRowFrom()).getCell(cra1.getColumnFrom()).getStringCellValue()+"本级汇总：");
            				int sum=0;
            				double db=0;
            				for(int k=cra1.getRowFrom()-count+1;k<cra1.getRowTo()-count+1;k++){
            					sum+=Integer.parseInt(dt.getValue(k, 3).toString());
            					db+=Double.parseDouble(dt.getValue(k, 4).toString());
            				}
            				hr1.createCell(3).setCellValue(""+sum);
            				hr1.createCell(4).setCellValue(""+db);
            				cra1.setRowTo(cra1.getRowTo()+1);
            				sheet.addMergedRegion(cra1);
            				sheet.getRow(cra1.getRowFrom()).getCell(cra1.getColumnFrom()).setCellValue(""+dt.Rows.get(i-3).getValue(dt.Columns.get(0)));
            				cra1=null;
            			}
            			
            			
            		}
            		
            		if(i==dt.Rows.size()){
            			if(cra2!=null){
            				sheet.shiftRows(i+count, sheet.getLastRowNum(), 1,true,false);
            				HSSFRow hr1=sheet.createRow(i+count);
            				//添加行
            				cra1.setRowTo(cra1.getRowTo()+1);
            				count=count+1;
            				HSSFCell hc1=hr1.createCell(2);
            				hc1.setCellValue(sheet.getRow(cra2.getRowFrom()).getCell(cra2.getColumnFrom()).getStringCellValue()+"社区本级汇总：");
            				int sum=0;
            				double db=0;
            				for(int k=cra2.getRowFrom()-count;k<=cra2.getRowTo()-count;k++){
            					sum+=Integer.parseInt(dt.getValue(k, 3).toString());
            					db+=Double.parseDouble(dt.getValue(k, 4).toString());
            				}
            				hr1.createCell(3).setCellValue(""+sum);
            				hr1.createCell(4).setCellValue(""+db);
            				cra2.setRowTo(cra2.getRowTo()+1);
            				sheet.addMergedRegion(cra2);
            				cra2=null;
            			}
            			
            			if(cra1!=null){
            			
            				sheet.shiftRows(i+count, sheet.getLastRowNum(), 1,true,false);
            				sheet.addMergedRegion(new Region(i+count,(short)1,i+count,(short)2));
            				HSSFRow hr1=sheet.createRow(i+count);
            				count=count+1;
            				HSSFCell hc1=hr1.createCell(1);
            				hc1.setCellValue(sheet.getRow(cra1.getRowFrom()).getCell(cra1.getColumnFrom()).getStringCellValue()+"本级汇总：");
            				int sum=0;
            				double db=0;
            				for(int k=cra1.getRowFrom()-count+1;k<cra1.getRowTo()-count+1;k++){
            					sum+=Integer.parseInt(dt.getValue(k, 3).toString());
            					db+=Double.parseDouble(dt.getValue(k, 4).toString());
            				}
            				hr1.createCell(3).setCellValue(""+sum);
            				hr1.createCell(4).setCellValue(""+db);
            				cra1.setRowTo(cra1.getRowTo()+1);
            				sheet.addMergedRegion(cra1);
            				sheet.getRow(cra1.getRowFrom()).getCell(cra1.getColumnFrom()).setCellValue(""+dt.Rows.get(i-3).getValue(dt.Columns.get(0)));
            				cra1=null;
            			}
            		}
            	}
            }

            sheet.addMergedRegion(new Region(sheet.getLastRowNum()+1,(short)0,sheet.getLastRowNum()+1,(short) 2));
            HSSFRow hs=sheet.createRow(sheet.getLastRowNum()+1);
            HSSFCell hc2=hs.createCell(0);
            hc2.setCellValue("总计：");
            int i=0;
            double j=0;
            for(DataRow dr:dt.Rows){
            	i+=Integer.parseInt(dr.getValue(3).toString());
            	j+=Double.parseDouble(dr.getValue(4).toString());
            }
            hs.createCell(3).setCellValue(""+i);
            hs.createCell(4).setCellValue(""+j);
            

            sheet.setColumnWidth((short) 0, (short) 5000);
            sheet.setColumnWidth((short) 1, (short) 6000);
            sheet.setColumnWidth((short) 2, (short) 4000);
            sheet.setColumnWidth((short) 3, (short) 3000);
            sheet.setColumnWidth((short) 4, (short) 3000);
            
            FileOutputStream fileOut = new FileOutputStream("d://excel//workbook.xls");   
            wb.write(fileOut);   
            fileOut.close();   
            System.out.print("OK");   
        } catch (Exception ex) {   
            ex.printStackTrace();   
        }  
        
    }   
		
	
	public static void main(String[] args) throws Exception {
		SystemConfig.ReadConfigFile(new Text1().getClass().getResourceAsStream("/conf/web.properties"));
		Text1.ditialExcelExport();
	}
	
}
