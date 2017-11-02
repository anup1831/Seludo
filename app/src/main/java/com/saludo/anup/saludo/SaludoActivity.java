package com.saludo.anup.saludo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SaludoActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_CODE = 1;
    Button btnDatePicker, btnConctactPicker, btnVoiceRecorder, btnSendSaludo, btnCancelRecording;
    private DatePickerDialog datePickerDialog;
    int year, month, day;
    private String phoneNumber, name;
    private String timeTicking = "0:00.0";
    private MediaRecorder mediaRecorder;
    private long mStartTime = 0;

    private int[] amplitudes = new int[100];
    private int i = 0;

    private Handler mHandler = new Handler();
    private Runnable mTickExecutor = new Runnable() {
        @Override
        public void run() {
            tick();
            mHandler.postDelayed(mTickExecutor,100);
        }
    };



    private File mOutputFile;


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saludo);
        initView();
    }


    private void initView(){
        //dateFrag = new CustomDatePickerDialog(saludoViewPresenter);
        btnDatePicker = (Button) findViewById(R.id.btn_calender);
        btnDatePicker.setText(setCurrentDate());
        btnConctactPicker = (Button) findViewById(R.id.btn_contact);
        btnVoiceRecorder = (Button) findViewById(R.id.btn_voice_recording);
        btnVoiceRecorder.setText(getResources().getString(R.string.label_recording) +" - "+timeTicking);
        btnCancelRecording = (Button) findViewById(R.id.btn_stopl_recording);
        btnSendSaludo = (Button) findViewById(R.id.btn_send);

        btnDatePicker.setOnClickListener(this);
        btnConctactPicker.setOnClickListener(this);
        btnVoiceRecorder.setOnClickListener(this);
        btnCancelRecording.setOnClickListener(this);
        btnSendSaludo.setOnClickListener(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private String setCurrentDate() {
        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);
        return new StringBuilder()
                // Month is 0 based, just add 1
                .append(month + 1).append("/").append(day).append("/")
                .append(year).append(" ").append("- Click to change date").toString();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_calender){
          setDate();
        } else if (v.getId() == R.id.btn_contact){
           // saludoViewPresenter.showTost(this, "Contacts clicked!");
            pickContactFromContactList();
        } else if (v.getId() == R.id.btn_voice_recording){
           // saludoViewPresenter.showTost(this, "Voice clicked!");
            Log.d("Voice Recorder", "output: " + getOutputFile());
            startRecording();
        } else if (v.getId() == R.id.btn_stopl_recording) {
            if(mediaRecorder != null){
                stopRecording(false);
            }
        }else if(v.getId() == R.id.btn_send){
           // saludoViewPresenter.showTost(this, "Send btn clicked!");


        }
    }

    private void pickContactFromContactList() {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            Uri uri = data.getData();
            String[] projection = { ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME };

            Cursor cursor = getContentResolver().query(uri, projection,
                    null, null, null);
            cursor.moveToFirst();
            int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            setPhoneNumber(cursor.getString(numberIndex));
            setName(cursor.getString(nameIndex));
            btnConctactPicker.setText(getName()+ " - "+ getPhoneNumber());
            Log.d("Anup", "ZZZ number : " + getPhoneNumber() +" , name : "+getName());

        }
    }

    private void setDate() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR); // current year
        month = c.get(Calendar.MONTH); // current month
        day = c.get(Calendar.DAY_OF_MONTH); // current day
        datePickerDialog = new DatePickerDialog(SaludoActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                btnDatePicker.setText(new StringBuilder()
                        // Month is 0 based, just add 1
                        .append(month + 1).append("/").append(dayOfMonth).append("/")
                        .append(year).append(" ").toString());
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void startRecording(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
            mediaRecorder.setAudioEncodingBitRate(48000);
        } else {
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setAudioEncodingBitRate(64000);
        }
        mediaRecorder.setAudioSamplingRate(16000);
        mOutputFile = getOutputFile();
        mOutputFile.getParentFile().mkdirs();
        mediaRecorder.setOutputFile(mOutputFile.getAbsolutePath());
        try{
                mediaRecorder.prepare();
                mediaRecorder.start();
                mStartTime = SystemClock.elapsedRealtime();
                mHandler.postDelayed(mTickExecutor, 100);
                Log.d("Voice Recorder","started recording to "+mOutputFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Voice Recorder", "prepare() failed "+e.getMessage());
        }
    }
    protected void stopRecording(boolean saveFile){
        if(mediaRecorder != null){
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            mStartTime = 0;
            mHandler.removeCallbacks(mTickExecutor);
            if (!saveFile && mOutputFile != null) {
                mOutputFile.delete();
            }
        }

    }

    private File getOutputFile() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString()
                + "/Voice Recorder/RECORDING_"
                + dateFormat.format(new Date())
                + ".m4a");
    }

    private void tick() {
        long time = (mStartTime < 0) ? 0 : (SystemClock.elapsedRealtime() - mStartTime);
        int minutes = (int) (time / 60000);
        int seconds = (int) (time / 1000) % 60;
        int milliseconds = (int) (time / 100) % 10;
        btnVoiceRecorder.setText(getResources().getString(R.string.label_recording)+" - "+minutes+":"+(seconds < 10 ? "0"+seconds : seconds)+"."+milliseconds);
        if (mediaRecorder != null) {
            amplitudes[i] = mediaRecorder.getMaxAmplitude();
            //Log.d("Voice Recorder","amplitude: "+(amplitudes[i] * 100 / 32767));
            if (i >= amplitudes.length -1) {
                i = 0;
            } else {
                ++i;
            }
        }
    }

/*    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String hireDate = sdf.format(date);
        return hireDate;
    }*/
}
