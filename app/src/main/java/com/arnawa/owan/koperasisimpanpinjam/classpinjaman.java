package com.arnawa.owan.koperasisimpanpinjam;

public class classpinjaman {
    private Double Spinjaman;
    private Double SBunga;
    private String Sket;
    private String Stgl;
    private Double SAngsuran;
    private Double Sadm;
    private String SLama ;
    private Double Stagihan;
    private String Sidpelanggan;
    private String Sstatus;
    private String Stempo;
    private String Sid;
    public classpinjaman(String id, String idpelanggan,Double pinjaman,Double Bunga,String ket,String tgl,
                         Double Angsuran,Double adm,String Lama,Double tagihan, String status,
                         String tempo)
    {
        Sid = id;
        Spinjaman = pinjaman;
        SBunga = Bunga;
        Sket = ket;
        Stgl = tgl;
        SAngsuran = Angsuran;
        Sadm = adm;
        Stagihan = tagihan;
        SLama = Lama;
        Sidpelanggan = idpelanggan;
        Sstatus = status;
        Stempo = tempo;

    }
    public Double Getpinjaman()
    {
        return this.Spinjaman;
    }
    public Double GetBunga()
    {
        return this.SBunga;
    }
    public String Getket()
    {
        return this.Sket;
    }
    public String Getlama()
    {
        return this.SLama;
    }
    public Double Gettagihan()
    {
        return this.Stagihan;
    }
    public Double Getadm()
    {
        return this.Sadm;
    }
    public Double GetAnsuran()
    {
        return this.SAngsuran;
    }
    public String Gettgl()
    {
        return this.Stgl;
    }
    public String Getidpelanggan()
    {
        return this.Sidpelanggan;
    }
    public String Getid()
    {
        return this.Sid;
    }
    public String Getstatus()
    {
        return this.Sstatus;
    }
    public String Gettempo()
    {
        return this.Stempo;
    }
}
