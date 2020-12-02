package com.ecollado.samples.mapred;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.SplitLineReader;
import org.apache.hadoop.mapreduce.lib.input.UncompressedSplitLineReader;
import org.apache.hadoop.util.LineReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@InterfaceAudience.Public
@InterfaceStability.Unstable
public class SkipLinesRecordReader extends RecordReader<LongWritable, Text> {

    private static final Logger LOG =
            LoggerFactory.getLogger(SkipLinesRecordReader.class.getName());

    public static final String  SKIP_LINES_FILTER = "mapreduce.input.skiplineinputformat.linesfilter";

    private static final int    DEFAULT_MAX_LINE_LENGTH = 1024;
    private static final int[]  DEFAULT_LINES_FILTER = {1};

    private final int       maxLineLength;
    private final int       linesPerRecord;
    private final int[]     linesFilter;

    private LineReader      lineReader;

    private SplitLineReader in;

    private Configuration       jobConf;
    private FileSplit fileSplit;
    private FSDataInputStream   fileIn;
    private long                start;
    private long                end;
    private long                pos;

    private LongWritable        currentKey = null;
    private Text                currentValue = null;

    private int                 currentRecordLine;


    public SkipLinesRecordReader(TaskAttemptContext context, FileSplit split) {
        Configuration job = context.getConfiguration();
        this.maxLineLength  = job.getInt("mapreduce.input.linerecordreader.line.maxlength", DEFAULT_MAX_LINE_LENGTH);
        this.linesPerRecord = job.getInt("mapreduce.input.skiplineinputformat.linesperrecord", 1);
        int[] filter        = job.getInts(SKIP_LINES_FILTER);

        this.linesFilter = (filter == null) ? DEFAULT_LINES_FILTER:filter;

        LOG.info("SkipLinesRecordReader with [{}] lines per record, and [{}] filter.",
                this.linesPerRecord,
                this.linesFilter);

        this.jobConf    = job;
        this.fileSplit  = split;
        this.start      = split.getStart();
        this.end        = start + split.getLength();
    }

    @Override
    public void initialize(InputSplit inputSplit, TaskAttemptContext context)
            throws IOException, InterruptedException {

        final Path file = this.fileSplit.getPath();
        // open the file and seek to the start of the split
        final FileSystem fs = file.getFileSystem(this.jobConf);
        this.fileIn = fs.open(file);

        this.fileIn.seek(start);
        this.in     = new UncompressedSplitLineReader(
                fileIn, this.jobConf, null, this.fileSplit.getLength());

        if (this.start > 0) {
            start += in.readLine(new Text(), 0, maxBytesToConsume(start));
        }

        this.pos = start;
    }

    @Override
    public boolean nextKeyValue()
            throws IOException, InterruptedException {

        if (this.currentKey == null) {
            this.currentKey = new LongWritable();
        }

        if (this.currentValue == null) {
            this.currentValue = new Text();
        }

        // We always read one extra line, which lies outside the upper
        // split limit i.e. (end - 1)
        while (this.pos <= this.end || this.in.needAdditionalRecordAfterSplit()) {
            this.currentKey.set(this.pos);

            int bytesRead = this.in.readLine(this.currentValue, this.maxLineLength, maxBytesToConsume(this.pos));
            pos += bytesRead;

            if (bytesRead == 0) {
                return false;
            }

            if (bytesRead <= this.maxLineLength) {
                if (this.currentRecordLine >= this.linesPerRecord) {
                    this.currentRecordLine = 0;
                }
                if (this.linesFilter[this.currentRecordLine++] == 1) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public LongWritable getCurrentKey()
            throws IOException, InterruptedException {
        return this.currentKey;
    }

    @Override
    public Text getCurrentValue()
            throws IOException, InterruptedException {
        return this.currentValue;
    }

    @Override
    public void close() throws IOException {
        try {
            if (in != null) {
                in.close();
            }
        } catch (Exception e) {}
    }

    @Override
    public float getProgress() throws IOException {
        if (this.start == this.end) {
            return 0.0f;
        } else {
            return Math.min(1.0f, ( this.pos - this.start) / (float)(this.end - this.start));
        }
    }

    private int maxBytesToConsume(long pos) {
        return (int) Math.max(Math.min(Integer.MAX_VALUE, end - pos), maxLineLength);
    }
}
