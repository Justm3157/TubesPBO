/*  Saham.java
Deskripsi :berisi atribut dan method dalam class Saham
Pembuat  :Johan Reinhart Calvin
Tanggal   :28/03/2026
*/

public class Saham extends InstrumenBase{
    //Atribut
    private double eps;
    private double bvps;
    private double totalSupply;
    private double liquidity;
    //Method
    public Saham(String ticker, String nama, double CPrice,double eps,double bvps,double Supply){
        super(ticker,nama,CPrice);
        this.eps = eps;
        this.bvps = bvps;
        this.totalSupply = Supply;
        this.liquidity = Supply;
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
    
    @Override
    public double getUkuranKontrak(){
        return 1;
    }

    public double getLiquidity(){
        return liquidity; 
    }
    public double getTotalSupply(){
        return totalSupply;
    }
    @Override
    public double getValuasi(){
        return CPrice*totalSupply;
    }
    public String getNamaTicker(){
        return ticker;
    }
    public String getNamaSaham(){
        return nama;
    }
    public double getLembar(){
        return totalSupply;
    }
    public double getEPS(){
        return eps;
    }
    public double getBVPS(){
        return bvps;
    }
    public double hitungPER(){
        return CPrice/eps;
    }
    public double hitungPBV(){
        return CPrice/bvps;
    }
    public void printValuasi(){
        System.out.println("Saham     : " + nama);
        System.out.println("Harga     : " + CPrice);
        System.out.println("Lembar    : " + totalSupply);
        System.out.println("Market Cap: " + getValuasi());
    }
}

