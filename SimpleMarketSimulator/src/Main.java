/*  Main.java
Deskripsi : Simulasi pasar JRC Broker
Pembuat   : Johan Reinhart Calvin
Tanggal   : 28/03/2026
*/
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        // Taruh Instrumen disini
        Saham bbca = new Saham("BBCA", "Bank BCA", 9500, 1200, 1500,24655010000L);
        Saham tlkm = new Saham("TLKM", "Telkom Indonesia", 3800, 200, 400,99062216600L);
        Crypto btc = new Crypto("BTCUSD", "Bitcoin / US Dolar", 67000, 20000000L);
        Crypto eth = new Crypto("ETHUSD", "Ethereum / US Dollar", 2076, 20000000L); 
        Forex eurousd = new Forex("EURUSD", "Euro / US Dollar", 1.147);
        Forex usdjpy = new Forex("USDJPY", "US Dollar / Japanese Yen ", 155.906);
        Index spx = new Index("SPX", "S&P500", 6340);
        Index ndq = new Index("NDQ", "Nasdaq 100", 22950);
        Komoditas emas = new Komoditas("Gold", "Gold", 4500);
        Komoditas minyak = new Komoditas("BZ", "Brent Crude Oil", 112);

        //Deklarasi investor disini
        try{
            Investor Jokowi  = new Investor("INV001", "jkw",  -500000);
        } catch (IllegalArgumentException e){
            System.out.println("Gagal membuat investor" + e.getMessage());
        }
        Investor trump  = new Investor("INV001", "trump",  500000);
        Investor netanyahu  = new Investor("INV002", "netanyahu",  500000);
        Investor johan = new Investor("INV003", "Johan", 500000);


        System.out.println("\n========== INFO AWAL ==========");
        trump.displayInfo();
        netanyahu.displayInfo();
        johan.displayInfo();


        System.out.println("\n========== TRANSAKSI ==========");
        trump.beli("TRX101", bbca, 10, 1.0, "LONG");
        trump.beli("TRX102", emas, 0.5, 5.0, "SHORT");
        trump.beli("TRX103", spx, 0.5, 5.0, "LONG");

        netanyahu.beli("TRX201", tlkm, 20, 2.0, "LONG");
        netanyahu.beli("TRX202", btc, 2, 3.0, "LONG");
        netanyahu.beli("TRX203",ndq,2,6.0,"SHORT");

        johan.beli("TRX301", bbca, 5, 1.0, "SHORT");
        johan.beli("TRX302",usdjpy,0.1,5.0,"LONG");
        johan.beli("TRX303", minyak, 5, 5.0, "LONG");

        //Atur TP/SL jika ada, index berdasarkan aset dimasukkan urutan ke berapa, pada trump array porto = {bbca,emas,spx}
        //Netanyahu = {tlkm,btc,ndq} ; Johan = {bbca,usdjpy,minyak}
        trump.getPorto().getDaftarAset().get(0).setRiskManagement(9000, 10500);
        netanyahu.getPorto().getDaftarAset().get(1).setRiskManagement(62000, 73000);

        System.out.println("\n========== PORTOFOLIO SEBELUM SIMULASI ==========");
        trump.getPorto().viewPortofolio();
        netanyahu.getPorto().viewPortofolio();
        johan.getPorto().viewPortofolio();

        // ===== Setup simulation =====
        //Masukan instrumen yang akan berubah harga di array di bawah
        Instrumen[] instrumen = {bbca,tlkm,btc,usdjpy,spx,ndq,emas,minyak};
        //Urut berdasarkan urutuan array instrumen
        //Nilai dimasukkan berdasarkan hari/langkah, karena simulasi 5 langkah, maka tiap array ada 5 input, jika < langkah, maka simulasi akan
        //Membuat angka random dengan fluktuasi maksimum dari harga sebelumnya sebesasr 7%
        double[][] hargaSelanjutnya = {
            {9700, 9900, 10100, 10300, 10600}, //bbca
            {3900, 3850, 3950, 4000, 4100}, //tlkm
            {65170,63280,74148,68000,65940}, //btc
            {157.885,156.960,158.090,157.380,159.030}, //usdjpy
            {}, //spx
            {23363,24380,24811}, //ndq
            {4561,4731,4291,4185,4651}, //emas
            {105,102,99.5,110,120}, //minyak

        };

        //List investor yang akan dipengaruhi perubahan pasar
        ArrayList<Investor> semuaInvestor = new ArrayList<>();
        semuaInvestor.add(trump);
        semuaInvestor.add(netanyahu);
        semuaInvestor.add(johan);

        System.out.println("\n========== SIMULASI PASAR ==========");
        //fungsi simulasi beberapa bisa dipake siapa aja, pada kali ini berdasarkan netanyahu, mengganti tidak akan mempengaruhi sama sekali
        netanyahu.getPorto().simulasiBeberapa(instrumen,hargaSelanjutnya, 5, 0.07, semuaInvestor);


        System.out.println("\n========== TRANSAKSI MANUAL SETELAH SIMULASI ==========");
        netanyahu.jual("TRX004", tlkm, 10, "LONG");

        //Info setelah simulasi pasar
        System.out.println("\n========== FINAL INFO ==========");
        trump.displayInfo();
        netanyahu.displayInfo();
        johan.displayInfo();

        //Ga tahu mau diapain admin nya
        BrokerAdmin admin = new BrokerAdmin("ADM001", "Admin JRC");
        admin.displayInfo();
    }
}