package com.saludo.anup.saludo;

import android.content.Context;

/**
 * Created by Anup on 10/30/2017.
 */

public interface SaludoViewPresenter {
    String todayDate = new String();
    void showTost(Context context, String message);
    void validateFieds(String date, String contact, String voiceMsg);
    void setDate(String date);
    String getDate();

}
