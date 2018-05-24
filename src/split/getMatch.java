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
	private static final String BRS = "转出BRS";
	private static final String DISTRIBUTOR = "推荐分销商";
	private static final String UNSETTLED = "Eshop产品未成交";

	public static void main(String[] args) throws FileNotFoundException {
		try {
			String encoding = "UTF-8";
			File file = new File("eshopali.txt");

			List<String> id = new ArrayList<>();
			List<String> information = new ArrayList<>();
			List<String> date_info = new ArrayList<>();
			List<String> phone = new ArrayList<>();
			List<String> status = new ArrayList<>();
			List<String> region = new ArrayList<>();
			List<String> bu = new ArrayList<>();
			List<String> product = new ArrayList<>();

			// The account responsible for the purchase
			String localHost = null;

			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				String all = "";
				localHost = bufferedReader.readLine();
				localHost = filterHost(localHost);

				// System.out.println(localHost);
				// System.out.println("localHost 长度："+localHost.length());

				while ((lineTxt = bufferedReader.readLine()) != null) {
					all = all + "\n" + lineTxt;
				}
				read.close();

				String[] splited = all.split("----------------------------");
				int i = 0;

				for (String s : splited) {
					if (i % 2 != 0) {
						id.add(s);

					} else {

						String sub[] = s.split("\n");

						matchDate(s, date_info);

						matchInfo(sub, information, localHost);

						matchPhone(sub, phone, localHost);

						matchStatus(sub, status);
						
						matchProductBu(sub,product,bu);

					}
					i++;
				}
			} else {
				System.out.println("Cannot find the file !");
			}

			information.remove(0);
			phone.remove(0);
			status.remove(0);
			product.remove(0);
			bu.remove(0);
			writeFile(id, information, date_info, phone, status,bu,product,1, "writeExcel.xlsx");

			try {
				System.out.println(id);
				System.out.println(id.size());
				System.out.println(information);
				System.out.println(information.size());
				System.out.println(date_info);
				System.out.println(date_info.size());
				System.out.println(phone);
				System.out.println(phone.size());
				System.out.println(localHost);
			} catch (Exception e) {
				System.out.println("Error!");
				e.printStackTrace();
			}

			dataQuery dq = new dataQuery();
			File read_file = new File("region.xls");
			List excelList = dq.readExcel(read_file);

			for (int i = 0; i < excelList.size(); i++) {
				List list = (List) excelList.get(i);
				for (int j = 0; j < list.size(); j++) {
					// System.out.print(list.get(j));
				}
				// System.out.println();
			}
			System.out.println(excelList);

		} catch (Exception e) {
			System.out.println("Warning ! Reading file error!");
			e.printStackTrace();
		}
	}

	private static String filterHost(String localHost) {
		Pattern p1 = Pattern.compile("[a-zA-Z0-9].*");
		Matcher m = p1.matcher(localHost);
		boolean result = m.find();

		if (result) {
			localHost = m.group(0);
		}
		return localHost;
	}

	private static void writeFile(List<String> idList, List<String> infoList, List<String> date_info,
			List<String> phone, List<String> status,List<String> bu,List<String> product, int cloumnCount, String finalXlsxPath) {

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
			int rowNumber = sheet.getLastRowNum(); // Count the first roe start
													// from 0
			System.out.println(
					"The row number of the original data except for the property of the column:\n" + rowNumber);
			for (int i = 1; i <= rowNumber; i++) {
				Row row = sheet.getRow(i);
				sheet.removeRow(row);
			}

			// Create output stream for the file with the corresponding file
			// path, show the form
			out = new FileOutputStream(finalXlsxPath);
			workBook.write(out);

			/**
			 * Write new data in Excel
			 */
			for (int j = 0; j < idList.size(); j++) {
				// Create one row from the second row, skip for the first row
				Row row = sheet.createRow(j + 1);
				// Get each record to be inserted
				String s = idList.get(j);
				String idInfo = s.toString();

				String i = infoList.get(j);
				String info = i.toString();

				String d = date_info.get(j);
				String dateInfo = d.toString();

				String p = phone.get(j);
				String phoneInfo = p.toString();

				String sta = status.get(j);
				String statusInfo = sta.toString();
				
				String pro = product.get(j);
				String productInfo = pro.toString();
				
				String b =bu.get(j);
				String buInfo = b.toString();

				for (int k = 0; k <= columnNumCount; k++) {

					// Loop in one row
					Cell first = row.createCell(0);
					first.setCellValue(j + 1);

					Cell second = row.createCell(1);
					second.setCellValue("Alibaba");

					Cell third = row.createCell(2);
					third.setCellValue(idInfo);
					
					Cell forth = row.createCell(3);
					forth.setCellValue(buInfo);

					Cell fifth = row.createCell(4);
					fifth.setCellValue(statusInfo);

					Cell seventh = row.createCell(6);
					seventh.setCellValue(dateInfo);

					Cell eighth = row.createCell(7);
					eighth.setCellValue(productInfo);
					
					Cell ninth = row.createCell(8);
					ninth.setCellValue(info);

					Cell tenth = row.createCell(9);
					tenth.setCellValue(phoneInfo);

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

	// Adapt with different excel format
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

	private static void matchDate(String s, List<String> date_info) {
		Pattern p2 = Pattern.compile("20[0-9][0-9]-[0-1][0-9]-[0-3][0-9]");
		Matcher date = p2.matcher(s);
		boolean date_r = date.find();
		String date_result = null;

		if (date_r) {
			date_result = date.group(0);
			date_info.add(date_result);
		}
	}

	private static void matchInfo(String subs[], List<String> information, String localHost) {

		// String subs[] = s.split("\n");
		String compSet = "";

		for (String ss : subs) {
			if (!ss.contains(localHost)) {

				Pattern p1 = Pattern.compile(".{13}公司.*");
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
			} else {
				continue;
			}
		}
		// if (compSet.contains("抱歉哦亲， 您询价的产品还没有在这边销售")) {
		// compSet = compSet.replaceAll(
		// "抱歉哦亲， 您询价的产品还没有在这边销售，
		// 所以我无法给您报价，您需要采购的话可以帮您转给西门子线下渠道报价，需要提供下您的公司名称和联系电话", "");
		// }
		compSet = compSet.replaceAll("您需要采购的话可以帮您转给西门子线下渠道报价，需要提供下贵司名称和联系电话", "");

		information.add(compSet);

	}

	private static void matchPhone(String subs[], List<String> phone, String localHost) {
		// String subs[] = s.split("\n");
		String phoneSet = "";

		for (String ss : subs) {
			if (!ss.contains(localHost)) {
				Pattern p = Pattern.compile("1[0-9]{10}");
				Matcher m = p.matcher(ss);
				boolean result = m.find();
				String phone_result = null;

				if (result) {
					phone_result = m.group(0);
					// System.out.println(m.group(0));
				}
				if (phone_result != null) {
					phoneSet = phoneSet + phone_result + "\n";
				}
			}
		}
		phone.add(phoneSet);

	}

	private static void matchStatus(String subs[], List<String> status) {
		String statusSet = "";

		for (String ss : subs) {
			// String status_result=null;
			if (ss.contains("帮您转出")) {
				statusSet = BRS;
				//System.out.println("wawawawaw");
			}else if (ss.contains("为您推荐官方授权代理商")||ss.contains("帮您转给")) {
				if(!statusSet.contains(DISTRIBUTOR)) {
					statusSet += DISTRIBUTOR;
				}
			}else if(ss.contains("https")){
				statusSet = UNSETTLED;
			}
		}
		status.add(statusSet);
	}

	
	private static void matchRegion(String s,List<String> region){
		
	}
	
	private static void matchProductBu(String subs[],List<String> product,List<String> bu){
		String productSet = "";
		String buSet ="";
		
		for(String ss:subs){
			Pattern pFA = Pattern.compile("(6[e|E][S|s|5]+[0-9A-Za-z\\-\\s]{0,16})|(触摸屏)|(面板)|(工控机)|([P|p][L|l][C|c])");
			Matcher mFA = pFA.matcher(ss);
			boolean resultFA = mFA.find();			
			
			Pattern pCP = Pattern.compile("接触器|断路器|继电器|按钮|指示灯|信号灯|软启|开关|3TF40");
			Matcher mCP = pCP.matcher(ss);
			boolean resultCP = mCP.find();
			
			Pattern pMC = Pattern.compile("");
			Matcher mMC = pMC.matcher(ss);
			boolean resultMC = mMC.find();
			
			Pattern pCS = Pattern.compile("");
			Matcher mCS = pMC.matcher(ss);
			boolean resultCS = mCS.find();
			
			Pattern pLD = Pattern.compile("");
			Matcher mLD = pLD.matcher(ss);
			boolean resultLD = mLD.find();
			
			String product_result=null;
			

			if (resultFA) {
				product_result = mFA.group(0);
				// strs.add(company_result);
			}else if(resultCP){
				product_result = mCP.group(0);
			}
			
			if (product_result != null) {
				if(!productSet.contains(product_result)){
					productSet = productSet + product_result+"\n";
				}
				if(resultFA){ 
					buSet = "FA";
				}else if(resultCP){
					if(!buSet.contains("CP")) buSet += "CP";
				}
			}
		}
		product.add(productSet);
		bu.add(buSet);
		
		
		System.out.println(productSet);
	}
}
