/*  InstrumenBase.java
Deskripsi :berisi atribut dan method dalam class InstrumenBase
Pembuat  :Johan Reinhart Calvin
Tanggal   :28/03/2026
*/
import java.util.ArrayList;

public abstract class InstrumenBase implements Instrumen{
    //Atribut
    protected String ticker;
    protected String nama;
    protected double CPrice;
    protected ArrayList<Double> PriceHistory;
    //Method
    public InstrumenBase(String ticker, String nama, double CPrice){
        this.ticker = ticker;
        this.nama = nama;
        this.CPrice = CPrice;
        this.PriceHistory = new ArrayList<>();
        this.PriceHistory.add(CPrice);
    }

    @Override
    public String getKodeInstrumen(){
        return ticker;
    }
    @Override
    public String getNamaInstrumen(){
        return nama;
    }

    @Override
    public double getHargaSekarang(){
        return CPrice;
    }
    @Override
    public ArrayList<Double> getHistoriHarga(){
        return PriceHistory;
    }
    @Override
    public void nextHarga(double hargaBaru){
        this.CPrice =  hargaBaru;
        this.PriceHistory.add(hargaBaru);
    }
    @Override
    public double getValuasi(){
        return CPrice;
    }
    @Override
    public abstract double getUkuranKontrak();
} 
