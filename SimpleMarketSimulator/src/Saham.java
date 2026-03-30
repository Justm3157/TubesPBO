/*  Saham.java
Deskripsi :berisi atribut dan method dalam class Saham
Pembuat  :Johan Reinhart Calvin
Tanggal   :28/03/2026
*/

public class Saham extends InstrumenBase{
    //Atribut
    private double eps;
    private double bvps;
    //Method
    public Saham(String ticker, String nama, double CPrice,long lembar,double eps,double bvps){
        super(ticker,nama,CPrice,lembar);
        this.eps = eps;
        this.bvps = bvps;
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
        return this.getLiquidity();
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
        System.out.println("Lembar    : " + this.getLiquidity());
        System.out.println("Market Cap: " + this.getValuasi());
    }
}

