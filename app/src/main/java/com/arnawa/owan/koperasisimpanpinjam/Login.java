package com.arnawa.owan.koperasisimpanpinjam;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private Button myBtLogin;
    private ProgressDialog prgDialogLogin;
    private AsyncTask<String, Void, JSONObject> mSendData;

    EditText eTHpEmail, eTPswwd;
    SharedPreferences preferences;

    public static final String KEYPREF = "Key Preferences";
    public static final String KEYUSERNAME = "Key Username";
    public static final String KEYPASSWORD = "Key Password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eTHpEmail = (EditText) findViewById(R.id.et_no_hp_atau_email);
        eTPswwd = (EditText) findViewById(R.id.et_password);
        myBtLogin = (Button) findViewById(R.id.my_btnLogin);
        prgDialogLogin = new ProgressDialog(this);
        prgDialogLogin.setMessage("Please Wait ... !");

        preferences = getSharedPreferences(KEYPREF, Context.MODE_PRIVATE);

        if (preferences.contains(KEYUSERNAME) && (preferences.contains(KEYPASSWORD))) {
            eTHpEmail.setText(preferences.getString(KEYUSERNAME, ""));
            eTPswwd.setText(preferences.getString(KEYPASSWORD, ""));

        }

        myBtLogin.setOnClickListener(this);
    }

    private void loginUser(){
        mSendData = new mSendData().execute(eTHpEmail.getText().toString(), eTPswwd.getText().toString());
        String user = eTHpEmail.getText().toString();
        String pass = eTPswwd.getText().toString();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEYUSERNAME, user);
        editor.putString(KEYPASSWORD, pass);
        editor.apply();
    }

    private class mSendData extends AsyncTask<String, Void, JSONObject>{
        @Override
        protected void onPreExecute() {
            prgDialogLogin = ProgressDialog.show(Login.this, "Please Wait", "Retrieving data ...", true);
        }

        @Override
        protected void onPostExecute(JSONObject hasil) {
            AlertDialog.Builder aldBuild = new AlertDialog.Builder(Login.this);
            prgDialogLogin.dismiss();

            try {
                if (hasil !=null){
                    JSONArray juve = null;
                    JSONObject merda = null;

                    juve = hasil.getJSONArray("item");
                    if (juve.length() > 0){
                        Intent intentLogin = new Intent(Login.this, SimpanPinjam.class);
                        Bundle bndle = new Bundle();
                        bndle.putString("my_key", "My String");
                        SimpanPinjamDashboard mySPBFrag = new SimpanPinjamDashboard();

                        for (int a = 0; a < juve.length(); ++a ){
                            merda = juve.getJSONObject(a);
                        }

                        intentLogin.putExtra("uid", eTHpEmail.getText());
                        intentLogin.putExtra("id", merda.getString("id"));
                        intentLogin.putExtra("nor_sir", merda.getString("nor_sir"));
                        intentLogin.putExtra("nama", merda.getString("nama") );
                        intentLogin.putExtra("jk", merda.getString("jk"));
                        intentLogin.putExtra("jabatan_id", merda.getString("jabatan_id"));
                        intentLogin.putExtra("alamat", merda.getString("alamat"));
                        intentLogin.putExtra("notelp", merda.getString("notelp"));

                        mySPBFrag.setArguments(bndle);

                        startActivity(intentLogin);
                        finish();
                    }

                    else
                    {
                        prgDialogLogin.dismiss();
                        aldBuild.setMessage("User atau Password Salah !!!");
                        aldBuild.show();
                    }
                }
            } catch (JSONException e) {
                prgDialogLogin.setMessage("User atau Password Salah !!!");
                prgDialogLogin.show();
            }
        }

        @Override
        protected JSONObject doInBackground(String... strings) {

            try {
                return arwanaapiutils.send_data(strings[0],strings[1]);

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    @Override
    public void onClick(View v) {
        if (v ==myBtLogin){
            loginUser();
        }
    }

    @Override
    public void onBackPressed() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Anda yakin ingin keluar?");
        builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();

    }
}
