package split;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class getMatch {
	public static void main(String[]args) throws FileNotFoundException {
		try{
			String encoding = "UTF-8";
			File file = new File("E.txt");
			
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
                	}else{
                		Pattern p1 = Pattern.compile(".*公司.*");
                		Matcher comp = p1.matcher(s);
                		boolean result2 = comp.find();
                		String company_result = null;
                		
                		if (result2)
                        {
                            company_result=comp.group(0);
                            //str.add(company_result);
                        }
                		//System.out.println(s);
                	}
                	i=i+1;
                }
                //System.out.println(splited[18]);
                
			}
			else
            {
                System.out.println("找不到指定的文件");
            }
			
			
		}catch(Exception e){
			System.out.println("读取文件内容出错");
            e.printStackTrace();
		}
		
	}
}
