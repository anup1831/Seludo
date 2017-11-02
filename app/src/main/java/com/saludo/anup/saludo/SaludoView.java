package com.saludo.anup.saludo;

/**
 * Created by Anup on 10/30/2017.
 */

public interface SaludoView {

    //void showToastOnError();
    void showProgress();
    void hideProgress();

    //presenter's call
    void pickDateFromCalender();
    void pickContactFromPhoneContact();
    void startVoiceMessageRecording();
    void PrepareToPushDataToServer();
}
