package split;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class getMatch {
	public static void main(String[]args) throws FileNotFoundException {
		try{
			String encoding = "UTF-8";
			File file = new File("E.txt");
			List <String> id = new ArrayList<>();
			List <String> information = new ArrayList<>();
			
			if (file.isFile() && file.exists()){
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
				BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                String all="";
                
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
                	i=i+1;
                }                
			}
			else
            {
                System.out.println("找不到指定的文件");
            }
			
			information.remove(0);
			
			
			System.out.println(id);
			System.out.println(id.size());
			System.out.println(information)	;
			System.out.println(information.size())	;
		}catch(Exception e){
			System.out.println("读取文件内容出错");
            e.printStackTrace();
		}
	}
}
