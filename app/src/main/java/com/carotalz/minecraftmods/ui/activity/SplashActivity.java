package com.carotalz.minecraftmods.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

import com.carotalz.minecraftmods.R;
import com.carotalz.minecraftmods.controller.helpers.TinyDB;
import com.carotalz.minecraftmods.controller.helpers.Utils;
import com.carotalz.minecraftmods.databinding.ActivitySplashBinding;
import com.carotalz.minecraftmods.models.Constants;
import com.carotalz.minecraftmods.models.ModModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_splash);
        init();
    }

    private void init() {
        TinyDB tinyDB=new TinyDB(this);
        if (!tinyDB.getBoolean(Constants.FIRST_OPEN_KEY)) {
            String jsonData = Utils.loadJSONFromAsset(SplashActivity.this);
            Gson gson = new Gson();
            Type listUserType = new TypeToken<List<ModModel>>() {
            }.getType();
            ArrayList<ModModel> modModels = gson.fromJson(jsonData, listUserType);
            if (modModels!=null) {
                tinyDB.putListObject(Constants.MOD_DATA_KEY, modModels);
                tinyDB.putBoolean(Constants.FIRST_OPEN_KEY,true);
            } else {
                Toast.makeText(this, getString(R.string.something_error), Toast.LENGTH_SHORT).show();
            }
        }

        new CountDownTimer(2800,1000){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }.start();
    }
}