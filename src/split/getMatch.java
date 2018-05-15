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

	public static void main(String[] args) throws FileNotFoundException {
		try {
			String encoding = "UTF-8";
			File file = new File("E.txt");
			List<String> id = new ArrayList<>();
			List<String> information = new ArrayList<>();
			List<String> date_info = new ArrayList<>();
			

			// The account responsible for the purchase
			String localHost;

			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				String all = "";
				localHost = bufferedReader.readLine();

				while ((lineTxt = bufferedReader.readLine()) != null) {
					all = all + "\n" + lineTxt;
				}
				read.close();

				String[] splited = all.split("----------------------------");
				int i = 0;

				for (String s : splited) {
					if (i % 2 != 0) {
						// System.out.println(s);
						id.add(s);
					} else {

						String subs[] = s.split("\n");
						String compSet = "";
						
						
						Pattern p2 = Pattern.compile("20[0-9][0-9]-[0-1][0-9]-[0-3][0-9]");
						Matcher date = p2.matcher(s);
						boolean date_r = date.find();
						String date_result = null;
						
						if(date_r){
							date_result = date.group(0);
							date_info.add(date_result);
						}

						for (String ss : subs) {
							Pattern p1 = Pattern.compile(".*公司.*");
							Matcher comp = p1.matcher(ss);
							boolean result2 = comp.find();
							String company_result = null;
							
							
							if (result2) {
								company_result = comp.group(0);
								// strs.add(company_result);
							}
							if (company_result != null) {
								compSet = compSet + company_result;
							}
						}
						if (compSet.contains("抱歉哦亲， 您询价的产品还没有在这边销售")) {
							compSet = compSet.replaceAll(
									"抱歉哦亲， 您询价的产品还没有在这边销售， 所以我无法给您报价，您需要采购的话可以帮您转给西门子线下渠道报价，需要提供下您的公司名称和联系电话", "");						
						}
						compSet = compSet.replaceAll("您需要采购的话可以帮您转给西门子线下渠道报价，需要提供下您的公司名称和联系电话", "");

						information.add(compSet);
					}
					i++;
				}
			} else {
				System.out.println("Cannot find the file !");
			}

			information.remove(0);
			writeFile(id, information,date_info, 1, "writeExcel.xlsx");

			System.out.println(id);
			System.out.println(id.size());
			System.out.println(information);
			System.out.println(information.size());
			System.out.println(date_info);
			System.out.println(date_info.size());
		} catch (Exception e) {
			System.out.println("Warning ! Reading file error!");
			e.printStackTrace();
		}
	}

	private static void writeFile(List<String> idList, List<String> infoList,List<String> date_info, int cloumnCount, String finalXlsxPath) {

		OutputStream out = null;
		try {
			// Get the number of the columns 
			int columnNumCount = cloumnCount;
			// Read the Excel file
			File finalXlsxFile = new File(finalXlsxPath);
			Workbook workBook = getWorkbok(finalXlsxFile);
			// The sheet matches with workbook
			Sheet sheet = workBook.getSheetAt(0);
			
			/**
			 * Delete all properties except for the column title
			 */
			int rowNumber = sheet.getLastRowNum(); // Count the first roe start from 0
			System.out.println("The row number of the original data except for the property of the column:\n" + rowNumber);
			for (int i = 1; i <= rowNumber; i++) {
				Row row = sheet.getRow(i);
				sheet.removeRow(row);
			}

			//Create output stream for the file with the corresponding file path, show the form
			out = new FileOutputStream(finalXlsxPath);
			workBook.write(out);
			
			/**
			 * Write new data in Excel
			 */
			for (int j = 0; j < idList.size(); j++) {
				//Create one row from the second row, skip for the first row
				Row row = sheet.createRow(j + 1);
				//Get each record to be inserted
				String s = idList.get(j);
				String idInfo = s.toString();

				String i = infoList.get(j);
				String info = i.toString();
				
				String d = date_info.get(j);
				String dateInfo = d.toString();
				
				for (int k = 0; k <= columnNumCount; k++) {
					
					// Loop in one row
					Cell first = row.createCell(0);
					first.setCellValue(j+1);
					
					Cell second = row.createCell(1);
					second.setCellValue("Alibaba");
					
					Cell third =row.createCell(2);
					third.setCellValue(idInfo);
					
					Cell seventh =row.createCell(6);
					seventh.setCellValue(dateInfo);
					
					Cell eighth =row.createCell(8);
					eighth.setCellValue(info);
					

				}
			}

			out = new FileOutputStream(finalXlsxPath);
			workBook.write(out);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Write data succesfully,Please find the file in local path !\n");
	}

	//Adapt with different excel format
	public static Workbook getWorkbok(File file) throws IOException {
		Workbook wb = null;
		FileInputStream in = new FileInputStream(file);
		if (file.getName().endsWith(EXCEL_XLS)) { // Excel&nbsp;2003
			wb = new HSSFWorkbook(in);
		} else if (file.getName().endsWith(EXCEL_XLSX)) { // Excel 2007/2010
			wb = new XSSFWorkbook(in);
		}
		return wb;
	}
}
