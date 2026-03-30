/*  Investor.java
Deskripsi :berisi atribut dan method dalam class Investor
Pembuat  :Johan Reinhart Calvin
Tanggal   :28/03/2026
*/

import java.util.ArrayList;

class Investor extends User {
    //Atribut
    private double balance;
    private double equity;
    private double freeMargin;
    private double totalKomisi;
    private Portofolio porto;
    private ArrayList<Transaksi> riwayat;
    //Method
    public Investor(){
        super();
        this.porto = new Portofolio();
        this.balance = 0;
        this.equity = 0;
        this.freeMargin = 0;
        this.totalKomisi = 0;
        this.riwayat = new ArrayList<>();
    }
    public Investor(String id,String nama,double balance){
        super(id, nama);
        this.balance = balance;
        this.freeMargin = balance;
        this.equity = balance;
        this.totalKomisi = 0;
        this.porto = new Portofolio();
        this.riwayat = new ArrayList<>();
    }

    public Portofolio getPorto(){
        return porto;
    }
    public boolean cekBuyingPower(double totalHarga,double leverage){
        double buyingPower = freeMargin*leverage;
        if(buyingPower >= totalHarga){
            return true;
        }
        else{
            System.out.println("Order DITOLAK: Buying Power tidak mencukupi.");
            System.out.printf("Buying Power : %.2f | Dibutuhkan : %.2f%n",
                buyingPower, totalHarga);
            return false;
        }
    }

    public void updateEquity(){
        double PnLnonFinal = porto.getTotalPnL();
        equity = balance + PnLnonFinal;
        freeMargin = equity;
    }
    public void beli(String idTransaksi, Instrumen instrumen, double unit,double leverage,String posisi){
        InstrumenBase base = (InstrumenBase) instrumen;
        if(unit > base.getLiquidity()){
            System.out.println("Order DITOLAK: Liquidity tidak mencukupi.");
            System.out.printf("Liquidity tersedia : %.2f | Dibutuhkan : %.2f%n", base.getLiquidity(), unit);
            return;
        }

        double totalHarga = instrumen.getHargaSekarang()*unit;
        Transaksi transaksi = new Transaksi(idTransaksi, "Buy", totalHarga);
        double totalBayar = transaksi.getTotalHargaSetelahFee();

        if(!cekBuyingPower(totalBayar, leverage)) return;

        base.kurangiLiquidity(unit);
        balance -= totalBayar;
        totalKomisi += transaksi.getFee();
        freeMargin = balance;

        porto.tambahInstrumen(instrumen, unit,leverage,posisi);
        transaksi.cetakResi();
        riwayat.add(transaksi);
    }

    public void jual(String idTransaksi, Instrumen instrumen,double unit,String posisi){
        double holding = porto.getHolding(instrumen.getNamaInstrumen(), posisi);
        if (unit > holding){
            System.out.println("Order DITOLAK: Holding tidak mencukupi.");
            System.out.printf("Holding tersedia : %.2f | Ingin dijual : %.2f%n",holding, unit);
            return;
        }
        double totalHarga = instrumen.getHargaSekarang()*unit;
        Transaksi transaksi = new Transaksi(idTransaksi,"Sell",totalHarga);

        double Setelahkomisi = transaksi.getTotalHargaSetelahFee();

        InstrumenBase base = (InstrumenBase) instrumen;
        base.tambahLiquidity(unit);
        balance += Setelahkomisi;
        totalKomisi += transaksi.getFee();
        freeMargin = balance;

        porto.kurangiHolding(instrumen.getNamaInstrumen(), posisi, unit);
        transaksi.cetakResi();
        riwayat.add(transaksi);
    }
    @Override
    public void displayInfo(){
        updateEquity(); // always recalculate before displaying
        System.out.println("========== Info Investor ==========");
        System.out.println("ID Investor    : " + this.getID());
        System.out.println("Nama Investor  : " + this.getNama());
        System.out.printf ("Balance        : %.2f%n", balance);
        System.out.printf ("Equity         : %.2f%n", equity);
        System.out.printf ("Free Margin    : %.2f%n", freeMargin);
        System.out.printf ("Total Komisi   : %.2f%n", totalKomisi);
        System.out.println("===================================");
    }    
}
