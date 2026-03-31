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
    protected Long totalSupply;
    protected double liquidity;
    //Method
    public InstrumenBase(String ticker, String nama, double CPrice, long totalSupply){
        this.ticker = ticker;
        this.nama = nama;
        this.CPrice = CPrice;
        this.PriceHistory = new ArrayList<>();
        this.PriceHistory.add(CPrice);
        this.totalSupply = totalSupply;
        this.liquidity = totalSupply;
    }

    public void kurangiLiquidity(double unit){
        if(unit > liquidity){
            throw new IllegalArgumentException("Liquidity tidak mencukupi");
        }
        this.liquidity -= unit;
    }

    public void tambahLiquidity(double unit){
        this.liquidity = Math.min(liquidity + unit, totalSupply);
    }

    public double getLiquidity(){
        return liquidity; 
    }
    public double getTotalSupply(){
        return totalSupply;
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
