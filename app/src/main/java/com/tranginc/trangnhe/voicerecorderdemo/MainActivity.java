package com.tranginc.trangnhe.voicerecorderdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Activity {

    private Context mContext;

    private TextView tvTitle, tvTimeKeeper, tvNameRecord, tvDateRecord, tvDone;
    private Button btnRecorder;
    private ListView lvRecord;

    private static final int RECORDER_SAMPLERATE = 8000; //44100, 22050, 11025, 8000
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    //ENCODING_PCM_16BIT – 16 bit per sample
    //ENCODING_PCM_8BIT – 8 bit per sample
    //ENCODING_DEFAUL – default encoding

    private AudioRecord recorder = null;
    private boolean isRecording = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTimeKeeper = (TextView) findViewById(R.id.tvTimeKeeper);
        tvNameRecord = (TextView) findViewById(R.id.tvNameRecord);
        tvDateRecord = (TextView) findViewById(R.id.tvDateRecord);
        tvDone = (TextView) findViewById(R.id.tvDone);
        btnRecorder = (Button) findViewById(R.id.btnRecorder);
        lvRecord = (ListView) findViewById(R.id.lvRecord);

        btnRecorder.setOnClickListener(new ButtonRecordListener());
        tvDone.setOnClickListener(new DoneRecordListener());
//
//        MediaRecorder mediaRecorder = new MediaRecorder();
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
//        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
//        mediaRecorder.setOutputFile(Environment.getExternalStorageDirectory()
//                .getAbsolutePath() + "/record.mp3");
//        //recorder.re.
//        mediaRecorder.start();
//        mediaRecorder.stop();
    }

    private void stopRecording() {
        if (recorder == null) {
            return;
        }

        isRecording = false;

        recorder.stop();
        recorder.release();
        recorder = null;

    }

    class TimeRecordingKeeper extends AsyncTask<Void, String, Void> {
        private long startTime = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            startTime = System.currentTimeMillis();

            while (isRecording) {
                long recordingTime = System.currentTimeMillis();
                String text = "";
                long total = recordingTime - startTime;

                // total = 5456454;
                //
                long milisecond = (total % 1000) / 10;

                long totalSecond = total / 1000;
                long second = totalSecond % 60;
                long minute = totalSecond / 60;

                if (minute == 0)
                    text = "00:";
                else if (minute < 10)
                    text = "0" + minute + ":";
                else text = "" + minute + ":";

                if (second == 0)
                    text = text + "00.";
                else if (second < 10)
                    text = text + "0" + second + ".";
                else text = text + second + ".";

                if (milisecond == 0)
                    text = text + "00";
                else if (milisecond < 10)
                    text = text + "0" + milisecond;
                else text = text + milisecond;


                publishProgress(text);

                try {
                    Thread.sleep(5);
                } catch (Exception e){

                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            String text = values[0];
            tvTimeKeeper.setText(text);
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }

    class StartRecording extends Thread {

        @Override
        public void run() {
            isRecording = true;
            TimeRecordingKeeper timeRecordingKeeper = new TimeRecordingKeeper();
            timeRecordingKeeper.execute();

            MyData.recordData = new ArrayList<>();
            short shortData[] = new short[MyData.BufferElements2Rec];

            /*
            *
            * AudioRecord(
	                int audioSource, int sampleRateInHz,
	                int channelConfig, int audioFormat, int bufferSizeInBytes)
            *
            *
            * */

            recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                    RECORDER_AUDIO_ENCODING, MyData.BufferElements2Rec * MyData.BytesPerElement);

            recorder.startRecording();

            while (isRecording) {
                recorder.read(shortData, 0, MyData.BufferElements2Rec);

                Log.i("cuongquoc", shortData.length + "" + Arrays.toString(shortData));
                //byte byteData[] = short2byte(shortData);
                MyData.recordData.add(shortData);
            }
        }
    }

    class ButtonRecordListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (isRecording) {
                btnRecorder.setBackground(getDrawable(R.drawable.start_button_backgound));
                stopRecording();
                return;
            }

            btnRecorder.setBackground(getDrawable(R.drawable.pause_button_background));

            StartRecording startRecording = new StartRecording();
            startRecording.start();
        }
    }


    class DoneRecordListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, SaveVoiceRecordDialog.class);
            mContext.startActivity(intent);
        }
    }


    @Override
    protected void onStop() {
        stopRecording();
        super.onStop();
    }
}
