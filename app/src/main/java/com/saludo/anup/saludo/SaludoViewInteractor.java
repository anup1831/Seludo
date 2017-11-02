package com.saludo.anup.saludo;

/**
 * Created by Anup on 10/30/2017.
 */

public interface SaludoViewInteractor {

    //create a listener interface where will have a methods for onSuccess(successfullyDataPushed message), onFaliure(Failure message)
    interface OnSaludoPushListener{
        void onPushSuccessful();
        void onPushFailure();
    }

    public String pushSaludoToServer(String strDate, String contactNumber, String ContactName, String recordedVoiceMsg);
}
