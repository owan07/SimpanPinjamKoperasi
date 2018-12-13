package com.arnawa.owan.koperasisimpanpinjam;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


public class SimpanPinjamTambahSimpanan extends Fragment {

    private Button btn_cameraSimpanan, btn_galerySimpanan;
    private ImageView img_buktiTfsimpanan;
    private TextView tv_namaAnggotaSimpanan;
    private Spinner spinner;
    private String idsimpanan;
    EditText et_jumlahSimpanan, et_keterangansimpanan, etPilihTanggalTransaksi;
    private ProgressDialog prgDialogLogin;
    private ProgressDialog prgDialogLogins;
    private static final String TAG = SimpanPinjamTambahSimpanan.class.getSimpleName();
    private static final int CAMERA_REQUEST_CODE1 = 1111;
    private static final int PICK_IMAGE1=100;
    private AsyncTask<String, Void, JSONObject> mSendData, mAmbilData;
    List<StringWithTag> itemList;
    Uri imageUriSimpanan;
    String nosir = "";
    ArrayAdapter adapter;
    Fragment fragment = null;
    NumberFormat formatRupiah;
    Double jmlsimpanan = null;

    HashMap<Integer, String> mJenisSimpanan = new HashMap<Integer, String>();

    private static class StringWithTag {
        public String string;
        public Object tag;
        public Object jumlah;

        public StringWithTag(String string, Object tag, Object jumlah) {
            this.string = string;
            this.tag = tag;
            this.jumlah = jumlah;
        }

        @Override
        public String toString() {
            return string;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View tambahSimpananView = inflater.inflate(R.layout.fragment_simpan_pinjam_tambah_simpanan,container,false);

        tv_namaAnggotaSimpanan = (TextView) tambahSimpananView.findViewById(R.id.tv_namaAnggotaSimpanan);
        et_jumlahSimpanan = (EditText) tambahSimpananView.findViewById(R.id.et_jumlahSimpanan);
        et_keterangansimpanan = (EditText) tambahSimpananView.findViewById(R.id.et_keterangansimpanan);
        etPilihTanggalTransaksi = (EditText) tambahSimpananView.findViewById(R.id.etPilihTanggalTransaksi);

        formatRupiah = NumberFormat.getCurrencyInstance();

        Calendar cal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("dd MMM yyyy");
        String date_str = df.format(cal.getTime());
        etPilihTanggalTransaksi.setText(date_str);

        btn_cameraSimpanan = (Button) tambahSimpananView.findViewById(R.id.btn_cameraSimpanan);
        btn_galerySimpanan = (Button) tambahSimpananView.findViewById(R.id.btn_galerySimpanan);
        img_buktiTfsimpanan = (ImageView) tambahSimpananView.findViewById(R.id.img_buktiTfsimpanan);

        btn_cameraSimpanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentcamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentcamera, CAMERA_REQUEST_CODE1);
            }
        });

        btn_galerySimpanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imgBuktiSimpanan = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(imgBuktiSimpanan, PICK_IMAGE1);
            }
        });
        spinner = (Spinner)tambahSimpananView.findViewById(R.id.sp_jenissimpanan);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                StringWithTag swt = (StringWithTag) adapterView.getItemAtPosition(i);
                Object jml = (Object) swt.jumlah;
                Object id = (Object) swt.tag;
                idsimpanan = id.toString();
                et_jumlahSimpanan.setText(jml.toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

        CharSequence[] itemArray =
                getResources().getTextArray(R.array.jnsSimpanan);
        itemList = new ArrayList<StringWithTag>();


        Bundle thebundle = getActivity().getIntent().getExtras();

        nosir=thebundle.getString("id");

        mSendData = new mSendData().execute(nosir);
        mAmbilData = new mAmbilData().execute("");

        Button btn_bayarSimpanan = (Button) tambahSimpananView.findViewById(R.id.btn_bayarSimpanan);
        btn_bayarSimpanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSendData = new mBayarSimpan().execute(nosir, etPilihTanggalTransaksi.getText().toString(),
                        et_jumlahSimpanan.getText().toString(),idsimpanan, et_keterangansimpanan.getText().toString());

                fragment = new SimpanPinjamDashboard();
                replaceFragment(fragment);
            }
        });

        return tambahSimpananView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case(CAMERA_REQUEST_CODE1) :
                if(resultCode == Activity.RESULT_OK)
                {
                    Bitmap bitmap;
                    bitmap = (Bitmap) data.getExtras().get("data");
                    img_buktiTfsimpanan.setImageBitmap(bitmap);
                    img_buktiTfsimpanan.setAdjustViewBounds(true);
                    img_buktiTfsimpanan.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                break;
        }

        switch (requestCode) {
            case(PICK_IMAGE1) :
                if(resultCode == Activity.RESULT_OK)
                {
                    imageUriSimpanan = data.getData();
                    img_buktiTfsimpanan.setImageURI(imageUriSimpanan);
                    img_buktiTfsimpanan.setAdjustViewBounds(true);
                    img_buktiTfsimpanan.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                break;
        }
    }

    private class mBayarSimpan extends AsyncTask<String, Void, JSONObject>{
        @Override
        protected void onPreExecute() {
            prgDialogLogins = ProgressDialog.show(getActivity(), "Please Waiting", "Retrieving data ...", true);
        }

        @Override
        protected void onPostExecute(JSONObject hasil) {
            AlertDialog.Builder aldBuild = new AlertDialog.Builder(getActivity());
            prgDialogLogins.dismiss();

        }

        @Override
        protected JSONObject doInBackground(String... strings) {

            try {
                return arwanaapiutils.datatambahsimpanan(strings[0],strings[1],strings[2],strings[3],strings[4]);

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class mAmbilData extends AsyncTask<String, Void, JSONObject>{
        @Override
        protected void onPreExecute() {
            prgDialogLogins = ProgressDialog.show(getActivity(), "Please Waiti", "Retrieving data ...", true);
        }

        @Override
        protected void onPostExecute(JSONObject hasil) {
            AlertDialog.Builder aldBuild = new AlertDialog.Builder(getActivity());
            prgDialogLogins.dismiss();

            try {
                if (hasil != null) {
                    JSONArray juve = null;
                    JSONObject merda = null;

                    juve = hasil.getJSONArray("itemjnssimpanan");
                    if (juve.length() > 0) {

                        for (int a = 0; a < juve.length(); ++a) {
                            merda = juve.getJSONObject(a);
                                merda.getString("id");
                                merda.getString("jns_simpan");
                                merda.getDouble("jumlah");
                                itemList.add(new StringWithTag(merda.getString("jns_simpan"), merda.getString("id"), merda.getString("jumlah")));
                        }

                    } else
                    {
                        prgDialogLogins.dismiss();
                        aldBuild.setMessage("User atau Password Salah !!!");
                        aldBuild.show();
                    }
                    prgDialogLogins.dismiss();
                    ArrayAdapter<StringWithTag> spinnerAdapter = new ArrayAdapter<StringWithTag>(getActivity(), android.R.layout.simple_spinner_item, itemList);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(spinnerAdapter);

                }

            }catch (JSONException e) {
                prgDialogLogins.setMessage(e.toString());
                prgDialogLogins.show();
            }

        }

        @Override
        protected JSONObject doInBackground(String... strings) {

            try {
                return arwanaapiutils.datajenissimpanan(strings[0]);

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class mSendData extends AsyncTask<String, Void, JSONObject>{
        @Override
        protected void onPreExecute() {
            prgDialogLogin = ProgressDialog.show(getActivity(), "Please Wait", "Retrieving data ...", true);
        }

        @Override
        protected void onPostExecute(JSONObject hasil) {
            AlertDialog.Builder aldBuild = new AlertDialog.Builder(getActivity());
            prgDialogLogin.dismiss();

            try {
                if (hasil != null) {
                    JSONArray juves = null;
                    JSONObject merda = null;

                    juves = hasil.getJSONArray("itemnamanggota");
                    if (juves.length() > 0) {

                        for (int a = 0; a < juves.length(); ++a) {
                            if (a == 0) {
                                merda = juves.getJSONArray(a).getJSONObject(0);
                                tv_namaAnggotaSimpanan.setText(merda.getString("nama"));
                            }

                        }

                    } else
                        {
                        prgDialogLogin.dismiss();
                        aldBuild.setMessage("User atau Password Salah !!!");
                        aldBuild.show();
                        }

                    }

                }catch (JSONException e) {
                    prgDialogLogin.setMessage(e.toString());
                    prgDialogLogin.show();
            }

        }

        @Override
        protected JSONObject doInBackground(String... strings) {

            try {
                return arwanaapiutils.dataanggotasp(strings[0]);

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
