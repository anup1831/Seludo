package com.saludo.anup.saludo;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Anup on 10/30/2017.
 */

public class SaludoViewPresenterImpl implements SaludoViewPresenter {
    SaludoView saludoView;
    SaludoViewInteractor saludoViewInteractor;
    String todayDate = new String();

    public SaludoViewPresenterImpl(SaludoView saludoView) {
        this.saludoView = saludoView;
        saludoViewInteractor = new SaludoViewInteractorImpl();
    }


    @Override
    public void showTost(Context context, String message) {
        if(saludoView !=  null ){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void validateFieds(String date, String contact, String voiceMsg) {

    }

    @Override
    public void setDate(String date) {
        this.todayDate = date;
    }

    @Override
    public String getDate() {
        return this.todayDate;
    }


}
