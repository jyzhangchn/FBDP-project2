import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.HashMap;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;

public class NaiveBayesTrainData
{
	public HashMap<String, Integer> freq;
	public NaiveBayesTrainData()
	{
		freq = new HashMap<String, Integer>();
	}
	public void getData(String file, Configuration conf) throws IOException
	{
		int i;
		Path data_path = new Path(file);
		Path file_path;
		String temp[], line;
		FileSystem hdfs = data_path.getFileSystem(conf);
		
		FileStatus[] status = hdfs.listStatus(data_path);
		
		for(i = 0; i<status.length; i++)
		{
			file_path = status[i].getPath();			
			if(hdfs.getFileStatus(file_path).isDir() == true)
				continue;
			line = file_path.toString();
			temp = line.split("/");
			if(temp[temp.length-1].substring(0,5).equals("part-") == false)
				continue;
			System.err.println(line);
			FSDataInputStream fin = hdfs.open(file_path);
			InputStreamReader inr = new InputStreamReader(fin);
			BufferedReader bfr = new BufferedReader(inr);
			while((line = bfr.readLine()) != null)
			{	
				String res[] = line.split("\t");
				freq.put(res[0], new Integer(res[1]));
				System.out.println(line);
			}
			bfr.close();
			inr.close();
			fin.close();
		}
	}
	
}
