import java.util.ArrayList;
import java.util.Scanner;
import java.io.File; 
import java.io.FileNotFoundException;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.conf.Configuration;

public class NaiveBayesConf
{
	public int dimen;
	public int class_num;
	public ArrayList<String> classNames;
	public ArrayList<String> proNames;
	public ArrayList<Integer>	proRanges;
	
	public NaiveBayesConf()
	{
		dimen = class_num = 0;
		classNames = new ArrayList<String>();
		proNames = new ArrayList<String>();
		proRanges = new ArrayList<Integer>();
	}
	
	public void ReadNaiveBayesConf(String file, Configuration conf) throws Exception
	{	
		Path conf_path = new Path(file);
		FileSystem hdfs = conf_path.getFileSystem(conf);
		FSDataInputStream fsdt = hdfs.open(conf_path);
		Scanner scan = new Scanner(fsdt);
		String str = scan.nextLine();
		String[] vals = str.split(" ");
		
		class_num = Integer.parseInt(vals[0]);
		
		int i;
		
		for(i = 1; i<vals.length; i++)
		{
			classNames.add(vals[i]);
		}
		
		str = scan.nextLine();
		vals = str.split(" ");
		dimen = Integer.parseInt(vals[0]);
		
		for(i = 1; i<vals.length; i+=2)
		{
			proNames.add(vals[i]);
			proRanges.add(new Integer(vals[i+1]));
		}
		fsdt.close();
		scan.close();
	}
}
