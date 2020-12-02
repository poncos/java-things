package com.ecollado.samples.mapred;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KmerCounterProcess {

    private static final Logger LOG =
            LoggerFactory.getLogger(KmerCounterProcess.class.getName());

    public static class KmerMapper
        extends Mapper<LongWritable, Text, Text, LongWritable> {

        private final static String KMER_LENGTH  = "mapreduce.kmercounter.k";

        private final static int DEFAULT_KMER_LENGTH = 22;

        private final static LongWritable ONE = new LongWritable(1);

        private Text kmerValue = new Text();

        private int kLength;

        @Override
        public void setup(Context context) throws IOException, InterruptedException {
            Configuration configuration = context.getConfiguration();
            this.kLength = configuration.getInt(KMER_LENGTH, DEFAULT_KMER_LENGTH);
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] kmers = this.getKmers(value.toString(), this.kLength);

            for (String kmer:kmers) {
                this.kmerValue.set(kmer);
                context.write(this.kmerValue, ONE);
            }
        }

        private String[] getKmers(String text, int k) {
            String[] kmers = new String[text.length() - k + 1];

            for (int i=0; i<kmers.length; i++) {
                kmers[i] = text.substring(i, i+k);
            }

            return kmers;
        }
    }

    public static class KmerReducer
        extends Reducer<Text,LongWritable,Text,LongWritable> {

        private LongWritable result = new LongWritable();

        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context)
                throws IOException, InterruptedException {
            long sum = 0;
            for (LongWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }


    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        GenericOptionsParser optionParser = new GenericOptionsParser(conf, args);

        String[] remainingArgs = optionParser.getRemainingArgs();
        if (remainingArgs.length != 8) {
            System.err.println("Wrong number of arguments {" + remainingArgs.length + "}");
            System.err.println("Usage: FastqProcess <in> <out> -N numberofrecords -M linesperrecord -F linesfilter");
            System.exit(2);
        }

        Job job = Job.getInstance(conf, "kmer count");
        job.setJarByClass(KmerCounterProcess.class);
        job.setInputFormatClass(SkipLinesInputFormat.class);
        job.setMapperClass(KmerCounterProcess.KmerMapper.class);
        job.setCombinerClass(KmerCounterProcess.KmerReducer.class);
        job.setReducerClass(KmerCounterProcess.KmerReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        List<String> otherArgs = new ArrayList<String>();
        for (int i=0; i < remainingArgs.length; ++i) {
            if ("-N".equals(remainingArgs[i])) {
                int recordsPerMap = Integer.parseInt(remainingArgs[++i]);
                job.getConfiguration().setInt(SkipLinesInputFormat.RECORDS_PER_MAP, recordsPerMap);
            } else if ("-M".equals(remainingArgs[i])) {
                int linesPerRecord = Integer.parseInt(remainingArgs[++i]);
                job.getConfiguration().setInt(SkipLinesInputFormat.LINES_PER_RECORD, linesPerRecord);
            } else if("-T".equals(remainingArgs[i])) {
                String[] filter = remainingArgs[++i].split(",");
                job.getConfiguration().setStrings(SkipLinesRecordReader.SKIP_LINES_FILTER, filter);
            } else {
                otherArgs.add(remainingArgs[i]);
            }
        }
        FileInputFormat.addInputPath(job, new Path(otherArgs.get(0)));
        FileOutputFormat.setOutputPath(job, new Path(otherArgs.get(1)));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
