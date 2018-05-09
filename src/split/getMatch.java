package split;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class getMatch {
	private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";
	
	public static void main(String[]args) throws FileNotFoundException {
		try{
			String encoding = "UTF-8";
			File file = new File("E.txt");
			List <String> id = new ArrayList<>();
			List <String> information = new ArrayList<>();
			
			// The account responsible for the purchase
			String localHost;
			
			if (file.isFile() && file.exists()){
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
				BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                String all="";
                localHost = bufferedReader.readLine();
                
                while ((lineTxt = bufferedReader.readLine()) != null)
                {
                	all = all+"\n"+lineTxt;
                }
                read.close();
                
                String[] splited= all.split("----------------------------");
                int i=0;
                
                for(String s: splited){
                	if(i%2!=0){
                	//System.out.println(s);
                		id.add(s);
                	}else{
                		
                		String subs[] = s.split("\n");
                		String compSet = "NA";
                		
                		for(String ss: subs){
                	    Pattern p1 = Pattern.compile(".*公司.*");
                		Matcher comp = p1.matcher(ss);
                		boolean result2 = comp.find();
                		String company_result = null;
                		
                		if (result2)
                        {
                            company_result=comp.group(0);                   
                           // strs.add(company_result);                   
                        }
                		if(company_result!=null){
                		compSet = compSet+company_result;
                		}
                		}
                		if(compSet.contains("抱歉哦亲， 您询价的产品还没有在这边销售")){
                			compSet = compSet.replaceAll("抱歉哦亲， 您询价的产品还没有在这边销售， 所以我无法给您报价，您需要采购的话可以帮您转给西门子线下渠道报价，需要提供下您的公司名称和联系电话","NA");
                			}
                		information.add(compSet);
                    }
                	i++;
                }                
			}
			else
            {
                System.out.println("找不到指定的文件");
            }
			
			information.remove(0);
			writeFile(id, 1, "writeExcel.xlsx");
			
			System.out.println(id);
			System.out.println(id.size());
			System.out.println(information)	;
			System.out.println(information.size())	;
		}catch(Exception e){
			System.out.println("读取文件内容出错");
            e.printStackTrace();
		}
	}
	
	
	
	private static void writeFile(List<String> dataList,int cloumnCount,String finalXlsxPath){

		OutputStream out = null;
		try {
			// 获取总列数
            int columnNumCount = cloumnCount;
            // 读取Excel文档
            File finalXlsxFile = new File(finalXlsxPath);
            Workbook workBook = getWorkbok(finalXlsxFile);
            // sheet 对应一个工作页
            Sheet sheet = workBook.getSheetAt(0);
            /**
             * 删除原有数据，除了属性列
             */
            int rowNumber = sheet.getLastRowNum();    // 第一行从0开始算
            System.out.println("原始数据总行数，除属性列：" + rowNumber);
            for (int i = 1; i <= rowNumber; i++) {
                Row row = sheet.getRow(i);
                sheet.removeRow(row);
            }
            // 创建文件输出流，输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
            out =  new FileOutputStream(finalXlsxPath);
            workBook.write(out);
            /**
             * 往Excel中写新数据
             */
            for (int j = 0; j < dataList.size(); j++) {
                // 创建一行：从第二行开始，跳过属性列
                Row row = sheet.createRow(j + 1);
                // 得到要插入的每一条记录
                String s = dataList.get(j);
                String idInfo = s.toString();
                //Map dataMap = dataList.get(j);
//                String name = dataMap.get("BankName").toString();
//                String address = dataMap.get("Addr").toString();
//                String phone = dataMap.get("Phone").toString();
                for (int k = 0; k <= columnNumCount; k++) {
                // 在一行内循环
                
                Cell first = row.createCell(0);
                first.setCellValue(idInfo);
//        
//                Cell second = row.createCell(1);
//                second.setCellValue(address);
//        
//                Cell third = row.createCell(2);
//                third.setCellValue(phone);
                }
            }
            // 创建文件输出流，准备输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
            out =  new FileOutputStream(finalXlsxPath);
            workBook.write(out);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
            try {
                if(out != null){
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("数据导出成功，请在本地文件中查找更新后的文件");
	}
	
	public static Workbook getWorkbok(File file) throws IOException{
        Workbook wb = null;
        FileInputStream in = new FileInputStream(file);
        if(file.getName().endsWith(EXCEL_XLS)){     //Excel&nbsp;2003
            wb = new HSSFWorkbook(in);
        }else if(file.getName().endsWith(EXCEL_XLSX)){    // Excel 2007/2010
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }
}
