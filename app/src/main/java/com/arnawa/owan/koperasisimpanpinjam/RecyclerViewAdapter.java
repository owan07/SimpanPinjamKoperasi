package com.arnawa.owan.koperasisimpanpinjam;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Date;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>  {

    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<Integer> mAngsuranKe;
    private ArrayList<Integer> mAngsuranPokok;// = new ArrayList<>();
    private ArrayList<String> mJatuhTempo;
    private Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<Integer> AngsuranKe, ArrayList<Integer> AngsuranPokok, ArrayList<String> JatuhTempo) {
        this.mAngsuranKe = AngsuranKe;
        this.mAngsuranPokok = AngsuranPokok;
        this.mJatuhTempo = JatuhTempo;
        this.mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_simulasi_pinjaman, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");
//        NumberFormat formatKurensi = NumberFormat.getCurrencyInstance();
//        System.out.printf("Harga %n", formatKurensi.format(angka));

        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
//        System.out.printf("Harga Rupiah: %s %n", kursIndonesia.format(harga));

        Integer cke = mAngsuranKe.get(position);
        Integer cname = mAngsuranPokok.get(position);
//        cname = (cname/cke);
        Integer cbunga = (cname*5/100);
        Integer cbiayaadm = (cname*3/100);

        String pokokAng = kursIndonesia.format(cname);
        String bunga = kursIndonesia.format(cbunga);
        String biayaAdmin = kursIndonesia.format(cbiayaadm);

        String cdate = mJatuhTempo.get(position);
        String lamaAng = Integer.toString(cke);
        String jmlTagihan = kursIndonesia.format(cname+cbunga+cbiayaadm);
//        String pokokAng = Integer.toString(cname);
//        String bunga = Integer.toString(cbunga);
//        String biayaAdmin = Integer.toString(cbiayaadm);


        holder.txtAngsuranKe.setText(lamaAng);
        holder.txtAngsuranPokok.setText(pokokAng);
        holder.txtJatuhTempo.setText(cdate);
        holder.txtBiayaBunga.setText(bunga);
        holder.txtBiayaAdmin.setText(biayaAdmin);
        holder.txtJumlahTagihan.setText(jmlTagihan);
//        Glide.with(mContext)
    }

    @Override
    public int getItemCount() {
        return mAngsuranPokok.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtAngsuranPokok;
        TextView txtAngsuranKe;
        TextView txtJatuhTempo;
        TextView txtBiayaBunga;
        TextView txtBiayaAdmin;
        TextView txtJumlahTagihan;
        RelativeLayout parentLayout;
        public ViewHolder(View itemView){
            super(itemView);
            txtAngsuranKe = itemView.findViewById(R.id.tvSimulasi_angsuran_ke);
            txtAngsuranPokok = itemView.findViewById(R.id.tvsimulasi_angsuranPokok);
            txtJatuhTempo = itemView.findViewById(R.id.tvSimulasi_tanggal_tempo);
            txtBiayaBunga = itemView.findViewById(R.id.tv_simulasi_biayaBunga);
            txtBiayaAdmin = itemView.findViewById(R.id.tv_simulasiBiaya_admin);
            txtJumlahTagihan = itemView.findViewById(R.id.tvSimulasiJumlahTagihan);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
