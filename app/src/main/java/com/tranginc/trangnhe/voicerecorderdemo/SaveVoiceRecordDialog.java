package com.tranginc.trangnhe.voicerecorderdemo;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

public class SaveVoiceRecordDialog extends Activity {

    private Context mContext;

    private EditText editName;
    private Button btnDelete, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_save_voice_record);
        mContext = this;


        editName = (EditText) findViewById(R.id.editName);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnSave = (Button) findViewById(R.id.btnSave);

        ButtonClickListener listener = new ButtonClickListener(SaveVoiceRecordDialog.this);
        btnDelete.setOnClickListener(listener);
        btnSave.setOnClickListener(listener);

        Calendar now = Calendar.getInstance();
        int month = now.get(Calendar.MONTH) + 1; // Note: zero based!
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        editName.setText(hour + "." + minute + " " + day + " thang " + month);
        editName.selectAll();
        editName.requestFocus();
    }

    class SaveVoiceRecord extends AsyncTask<Void, Void, Boolean>{
        private String filePath;

        public SaveVoiceRecord(String filePath){
            this.filePath = filePath;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            FileOutputStream os = null;


            try {
                os = new FileOutputStream(filePath);
                int length = MyData.BufferElements2Rec * MyData.BytesPerElement;

                for (int i = 0; i < MyData.recordData.size(); i++) {
                    os.write(short2byte(MyData.recordData.get(i)), 0, length);
                }

                os.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("cuongquoc", e.toString());
                return false;
            }
            Log.e("cuongquoc", "-----------YES-----------");
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {

        }
    }


    //convert short to byte
    public byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;
    }


    class ButtonClickListener implements View.OnClickListener {

        private Activity activity;

        public ButtonClickListener(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onClick(View v) {

            char c = '"';

            if (v.getId() == R.id.btnDelete) {

                activity.finish();
                return;
            }

            if (v.getId() == R.id.btnSave) {
                String fileName = editName.getText().toString();
                if (fileName.isEmpty())
                    return;

                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "VoiceRecorder/" + fileName + ".raw";
                Log.i("quoccuong", filePath);
                SaveVoiceRecord saveVoiceRecord = new SaveVoiceRecord(filePath);
                saveVoiceRecord.execute();
                activity.finish();
                return;
            }
        }
    }
}
