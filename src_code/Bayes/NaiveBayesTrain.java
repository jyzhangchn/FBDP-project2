import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Scanner;

public class NaiveBayesTrain {
    public static class TrainMapper
            extends Mapper<Object, Text, Text, IntWritable> {
        public NaiveBayesConf nBConf;
        private final static IntWritable one = new IntWritable(1);
        private Text word;

        public void setup(Context context) {
            try {
                nBConf = new NaiveBayesConf();
                Configuration conf = context.getConfiguration();
                nBConf.ReadNaiveBayesConf(conf.get("conf"), conf);
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
            System.out.println("setup");
        }

        public void map(Object key, Text value, Context context)
                throws IOException, InterruptedException {
            Scanner scan = new Scanner(value.toString());
            String str, vals[], temp;
            int i;
            word = new Text();
            while (scan.hasNextLine()) {
                str = scan.nextLine();
                vals = str.split(" ");
                word.set(vals[0]);
                context.write(word, one);
                for (i = 1; i < vals.length; i++) {
                    word = new Text();
                    temp = vals[0] + "#" + nBConf.proNames.get(i - 1);
                    temp += "#" + vals[i];
                    word.set(temp);
                    context.write(word, one);
                }
            }
        }
    }

    public static class TrainReducer
            extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values,
                           Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }
}

