/*  Main.java
Deskripsi : Simulasi pasar JRC Broker
Pembuat   : Johan Reinhart Calvin
Tanggal   : 28/03/2026
*/
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        // ===== Setup Instruments =====
        Saham bbca = new Saham("BBCA", "Bank BCA", 9500, 24655010000L, 1200, 1500);
        Saham tlkm = new Saham("TLKM", "Telkom Indonesia", 3800, 99062216600L, 200, 400);

        // ===== Setup Investors =====
        Investor budi  = new Investor("INV001", "Budi",  100000);
        Investor sari  = new Investor("INV002", "Sari",  200000);
        Investor johan = new Investor("INV003", "Johan", 150000);

        // ===== Display Initial Info =====
        System.out.println("\n========== INITIAL INFO ==========");
        budi.displayInfo();
        sari.displayInfo();
        johan.displayInfo();

        // ===== Budi buys BBCA LONG =====
        System.out.println("\n========== TRANSAKSI ==========");
        budi.beli("TRX001", bbca, 10, 1.0, "LONG");

        // ===== Sari buys TLKM LONG with leverage =====
        sari.beli("TRX002", tlkm, 20, 2.0, "LONG");

        // ===== Johan shorts BBCA =====
        johan.beli("TRX003", bbca, 5, 1.0, "SHORT");

        // ===== Set Risk Management =====
        // Budi sets TP at 10500 and SL at 9000 on his BBCA position
        budi.getPorto().getDaftarAset().get(0).setRiskManagement(9000, 10500);

        // ===== View portfolios before simulation =====
        System.out.println("\n========== PORTOFOLIO SEBELUM SIMULASI ==========");
        budi.getPorto().viewPortofolio();
        sari.getPorto().viewPortofolio();
        johan.getPorto().viewPortofolio();

        // ===== Setup simulation =====
        // Price history for simulation
        // Row 0 = BBCA future prices
        // Row 1 = TLKM future prices
        //Instrumen BBCA dan TLKM yang akan berubah harganya
        Instrumen[] instrumen = {bbca,tlkm};
        double[][] hargaSelanjutnya = {
            {9700, 9900, 10100, 10300, 10600}, // BBCA — will hit Budi's TP of 10500
            {3900, 3850, 3950, 4000, 4100}     // TLKM
        };

        // ===== Run simulation using Budi's portfolio as the market =====
        // (since it contains both instruments being simulated)
        ArrayList<Investor> semuaInvestor = new ArrayList<>();
        semuaInvestor.add(budi);
        semuaInvestor.add(sari);
        semuaInvestor.add(johan);

        System.out.println("\n========== SIMULASI PASAR ==========");
        budi.getPorto().simulasiBeberapa(instrumen,hargaSelanjutnya, 5, 0.05, semuaInvestor);

        // ===== Sari manually sells half her TLKM =====
        System.out.println("\n========== TRANSAKSI MANUAL SETELAH SIMULASI ==========");
        sari.jual("TRX004", tlkm, 10, "LONG");

        // ===== Final info =====
        System.out.println("\n========== FINAL INFO ==========");
        budi.displayInfo();
        sari.displayInfo();
        johan.displayInfo();

        // ===== BrokerAdmin =====
        BrokerAdmin admin = new BrokerAdmin("ADM001", "Admin JRC");
        admin.displayInfo();
    }
}