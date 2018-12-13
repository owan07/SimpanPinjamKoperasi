package com.arnawa.owan.koperasisimpanpinjam;

public class classsimpanan {
    private String Oanidanggota;
    private String Oantgl;
    private String Oanjenis;
    private String Oanketerangan;
    private Double Oanjumlahsimpanan;

    public classsimpanan(String idanggota, String tgl, String jenis, String keterangan, Double jumlahsimpanan)
    {
        Oanidanggota = idanggota;
        Oantgl = tgl;
        Oanjenis = jenis;
        Oanketerangan = keterangan;
        Oanjumlahsimpanan = jumlahsimpanan;
    }

    public String Getidanggota(){
        return this.Oanidanggota;
    }
    public String Gettgl() {
        return this.Oantgl;
    }
    public String Getjenis() {
        return this.Oanjenis;
    }
    public String Getketerangan() {
        return this.Oanketerangan;
    }
    public Double Getjumlahsimpanan() {
        return this.Oanjumlahsimpanan;
    }
}
