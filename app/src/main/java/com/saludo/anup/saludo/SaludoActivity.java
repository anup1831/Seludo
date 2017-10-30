package com.saludo.anup.saludo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SaludoActivity extends AppCompatActivity implements SaludoView{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saludo);
    }

    @Override
    public void pickDateFromCalender() {

    }

    @Override
    public void pickContactFromPhoneContact() {

    }

    @Override
    public void startVoiceMessageRecording() {

    }

    @Override
    public void PrepareToPushDataToServer() {

    }
}
