package split;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;


import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;



public class dataQuery {
	
public dataQuery(){
	
}
	 
	 public List readExcel(File file) {
	        try {
	        	// Create input stream to read excel
	            InputStream is = new FileInputStream(file.getAbsolutePath());
	            // jxl offers the class Workbook
	            Workbook wb = Workbook.getWorkbook(is);
	            // The number of the page for excel
	            int sheet_size = wb.getNumberOfSheets();
	            for (int index = 0; index < sheet_size; index++) {
	                List<List> outerList=new ArrayList<List>();
	                // Create sheet object for each sheet
	                Sheet sheet = wb.getSheet(index);
	                // sheet.getRows()return the number of the row
	                for (int i = 0; i < sheet.getRows(); i++) {
	                    List innerList=new ArrayList();
	                    // sheet.getColumns()return the number of the column 
	                    for (int j = 0; j < sheet.getColumns(); j++) {
	                        String cellinfo = sheet.getCell(j, i).getContents();
	                        if(cellinfo.isEmpty()){
	                            continue;
	                        }
	                        innerList.add(cellinfo);
	                       // System.out.print(cellinfo);
	                    }
	                    outerList.add(i, innerList);
	                    //System.out.println();
	                }
	                return outerList;
	            }
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (BiffException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	
	 public void query() throws Exception {
	        String sql = "select * from tb_user where id>=?";
	        Connection conn = DriverManager.getConnection("a","b","c");
	        PreparedStatement pst = conn.prepareStatement(sql);
	        pst.setInt(1, 1);

	        ResultSet rs = pst.executeQuery();
	        while(rs.next()) {
	            int id = rs.getInt("id");
	            String name = rs.getString("name");
	            int age = rs.getInt("age");
	            System.out.printf("id:%d, name:%s, age:%d\n", id, name, age);
	        }

	        rs.close();
	        pst.close();
	        conn.close();
	    }


}
