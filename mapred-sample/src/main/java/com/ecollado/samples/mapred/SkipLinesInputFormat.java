package com.ecollado.samples.mapred;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.LineRecordReader;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@InterfaceAudience.Public
@InterfaceStability.Unstable
public class SkipLinesInputFormat extends FileInputFormat<LongWritable, Text> {

    private static final Logger LOG =
            LoggerFactory.getLogger(LineRecordReader.class.getName());

    public static final String RECORDS_PER_MAP  = "mapreduce.input.skiplineinputformat.recordspermap";
    public static final String LINES_PER_RECORD = "mapreduce.input.skiplineinputformat.linesperrecord";

    @Override
    public RecordReader<LongWritable, Text> createRecordReader(InputSplit inputSplit,
                                                               TaskAttemptContext context)
            throws IOException, InterruptedException {

        return new SkipLinesRecordReader(context, (FileSplit) inputSplit);
    }

    @Override
    public List<InputSplit> getSplits(JobContext job)
            throws IOException {

        int recordsPerSplit = job.getConfiguration().getInt(RECORDS_PER_MAP, 1);
        int linesPerRecord  = job.getConfiguration().getInt(LINES_PER_RECORD, 1);

        LOG.info("getSplits: recordsPerSplit [{}], linesPerRecord [{}]",
                recordsPerSplit, linesPerRecord);

        List<InputSplit> splits = new ArrayList<>();
        for (FileStatus status : listStatus(job)) {
            for (org.apache.hadoop.mapreduce.lib.input.FileSplit split :
                    org.apache.hadoop.mapreduce.lib.input.
                            NLineInputFormat.getSplitsForFile(status, job.getConfiguration(),
                            recordsPerSplit * linesPerRecord)) {
                splits.add(split);
            }
        }
        return splits;
    }
}