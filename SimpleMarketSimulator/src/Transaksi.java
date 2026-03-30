/*  Transaksi.java
Deskripsi :berisi atribut dan method dalam class Transaksi
Pembuat  :Johan Reinhart Calvin
Tanggal   :28/03/2026
*/


class Transaksi{
    //Atribut
    private String idTransaksi;
    private String jenisOrder;
    private double totalHarga;
    private double SL;
    private double TP;
    private double fee;
    private double hargasetelahFee;
    private static final double feePersen = 0.0015;
    //Method
    public Transaksi(String id, String jenis, double total){
        this.idTransaksi = id;
        this.jenisOrder  = jenis;
        this.totalHarga  = total;
        this.fee         = total *feePersen;
        this.hargasetelahFee = total + this.fee;
        this.SL = 0;
        this.TP = 0;
    }
    public void setRiskManagement(double sl, double tp) {
        this.SL = sl;
        this.TP = tp;
    }
    public void cetakResi() {
        System.out.println("====== RESI TRANSAKSI ======");
        System.out.println("ID Transaksi : " + idTransaksi);
        System.out.println("Jenis Order  : " + jenisOrder);
        System.out.printf ("Total Harga  : %.2f%n", totalHarga);
        System.out.printf ("Fee (0.15%%) : %.2f%n", fee);
        System.out.printf ("Total + Fee  : %.2f%n", hargasetelahFee);
        if (SL != 0) System.out.printf("Stop Loss    : %.2f%n", SL);
        if (TP != 0) System.out.printf("Take Profit  : %.2f%n", TP);
        System.out.println("============================");
    }
    public String getIdTransaksi(){
        return idTransaksi; 
    }
    public String getJenisOrder(){
        return jenisOrder; 
    }
    public double getTotalHarga(){
        return totalHarga;
    }
    public double getFee(){
        return fee;
    }
    public double getTotalHargaSetelahFee(){
        return hargasetelahFee; 
    }
    public double getSL(){
        return SL;
    }
    public double getTP(){
        return TP;
    }
}

