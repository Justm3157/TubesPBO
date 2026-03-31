/*  Portofolio.java
Deskripsi :berisi atribut dan method dalam class Portofolio
Pembuat  :Johan Reinhart Calvin
Tanggal   :28/03/2026
*/
import java.util.ArrayList;
import java.util.Random;

class Portofolio{
    //Atribut
    private ArrayList<Kepemilikan> daftaraset;
    private String id;
    //Method
    public Portofolio(){
        this.daftaraset = new ArrayList<>();
        this.id = "0";
    }
    public ArrayList<Kepemilikan> getDaftarAset(){
        return daftaraset;
    }
    public Portofolio(String idinvestor){
        this.id = idinvestor;
        this.daftaraset = new ArrayList<>();
    }

    public void cekSLTP(Investor investor){
        ArrayList<Kepemilikan> temp = new ArrayList<>(daftaraset);
        for(Kepemilikan k: temp){
            double CPrice = k.getInstrumen().getHargaSekarang();
            double sl = k.getSL();
            double tp = k.getTP();
            double pnl = k.getPnL();

            if(tp != 0 && CPrice >= tp){
                System.out.println("Take Profit Executed: " +
                    k.getInstrumen().getKodeInstrumen() + " @ " + CPrice
                    + " | P&L: " + String.format("%.2f",pnl));
                investor.jual("TP-" + k.getInstrumen().getKodeInstrumen(),k.getInstrumen(), k.getUnit(), k.getPosisi());
            }
            if(sl != 0 && CPrice <= sl){
                System.out.println("STOP LOSS triggered: "
                    + k.getInstrumen().getKodeInstrumen()
                    + " @ " + CPrice
                    + " | P&L: " + String.format("%.2f", pnl));
                investor.jual("SL-" + k.getInstrumen().getKodeInstrumen(),k.getInstrumen(), k.getUnit(), k.getPosisi());
            }
        }
    }

    public void viewPortofolio(){
        String[] urutan = {"Saham", "Crypto", "Index", "Forex","Komoditas"};
        double totalPnL = 0;
        for (String kategori : urutan){
            boolean headerudah = false;
            for (Kepemilikan k : daftaraset){
                if(!k.getInstrumen().getClass().getSimpleName().equals(kategori)){
                    continue;
                }
                if(!headerudah){
                    System.out.println("============================== " + kategori + " ============================== ");
                    System.out.printf("%-10s %-8s %-15s %-15s %-15s %-15s %-15s %-15s%n",
                "Ticker", "Posisi", "Harga Beli", "Harga Kini", "Gain (%)", "P&L","Unit","Leverage");
                    headerudah = true;
                }

                double hargaBeli = k.gethargaBeli();
                double hargasekarang = k.getInstrumen().getHargaSekarang();
                double unit = k.getUnit();
                double pnl = k.getPnL();
                double kapital = (hargaBeli * unit)/k.getLeverage();
                double gain = (pnl/kapital)*100;
                totalPnL += pnl;
                double leverage = k.getLeverage();
                System.out.printf("%-10s %-8s %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f%n",
                    k.getInstrumen().getKodeInstrumen(),
                    k.getPosisi(),
                    hargaBeli, hargasekarang, gain, pnl,unit,leverage);
            }
        }
        System.out.println("==============================");
        System.out.printf("Total P&L: %.2f%n", totalPnL);
    }

    public Kepemilikan findKepemilikan(String ticker, String posisi){
        for(Kepemilikan k : daftaraset){
            if(k.getInstrumen().getKodeInstrumen().equals(ticker) &&
                k.getPosisi().equals(posisi)){
                return k;
            }
        }
        return null;
    }

    public void tambahInstrumen(Instrumen instrumen, double unit,double leverage,String posisi){
        for(Kepemilikan k : daftaraset){
            if(k.getInstrumen().getKodeInstrumen().equals(instrumen.getKodeInstrumen()) && k.getPosisi().equals(posisi)){
                k.tambahUnit(unit);
                return;
            }
        }
        daftaraset.add(new Kepemilikan(instrumen, unit,leverage,posisi));
    }

    public void tambahInstrumen(Instrumen instrumen, double unit,String posisi) {
        tambahInstrumen(instrumen, unit, 1.0,posisi);
    }

    public void simulasiBeberapa(Instrumen[] instrumen,double[][] hargaSelanjutnya,int langkah, double maxFluktuasi, ArrayList<Investor> investors){
        Random random = new Random();
        System.out.println("--- Simulasi fluktuasi harga JRC Broker ---");

        for(int hari = 0;hari < langkah;hari++){
            System.out.println("--- Hari ke-" + (hari + 1) + " ---");
            System.out.println("--- Perubahan Harga Instrumen ---");
            System.out.printf("%-10s %-20s %-15s %-15s %-10s%n",
            "Kode", "Nama", "Harga Lama", "Harga Baru", "Perubahan (%)");
            for(int i = 0;i < instrumen.length; i++){
                double hargaBaru;
                double hargaLama = instrumen[i].getHargaSekarang();

                if(hari < hargaSelanjutnya[i].length){
                    hargaBaru = hargaSelanjutnya[i][hari];
                }
                else{
                    double persen = (random.nextDouble() * 2 - 1)* maxFluktuasi;
                    hargaBaru = hargaLama*(1 + persen);
                }
                double perubahan = ((hargaBaru - hargaLama)/hargaLama)*100;
                String arah = perubahan >= 0? "^":"v";
                System.out.printf("%-10s %-20s %-15.2f %-15.2f %s %.2f%%%n",
                instrumen[i].getKodeInstrumen(),instrumen[i].getNamaInstrumen(),hargaLama,
                hargaBaru,arah,Math.abs(perubahan));
                instrumen[i].nextHarga(hargaBaru);
            }
            for(Investor investor : investors){
                if(investor.getPorto().getDaftarAset().size() > 0){
                    investor.getPorto().cekSLTP(investor);
                    investor.updateEquity();
                    System.out.println(">>> Portofolio: " + investor.getNama());
                    investor.getPorto().viewPortofolio();
                }
            }
        }
    }

    public double getHolding(String ticker,String posisi){
        for(Kepemilikan k:daftaraset){
            if(k.getInstrumen().getKodeInstrumen().equals(ticker) && k.getPosisi().equals(posisi)){
                return k.getUnit();
            }
        }
        return 0;
    }

    public void kurangiHolding(String ticker,String posisi, double unit){
        for(Kepemilikan k:daftaraset){
            if(k.getInstrumen().getKodeInstrumen().equals(ticker) && k.getPosisi().equals(posisi)){
                if(k.getUnit() - unit <= 0){
                    daftaraset.remove(k);
                    return;
                }
                else{
                    k.kurangiUnit(unit);
                }
            }
        }
    }
    public double getTotalPnL(){
        double total = 0;
        for(Kepemilikan k : daftaraset){
            total += k.getPnL();
        }
        return total;
    }

}

