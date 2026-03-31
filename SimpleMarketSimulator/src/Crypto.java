/*  Crypto.java
Deskripsi :berisi atribut dan method dalam class Crypto
Pembuat  :Johan Reinhart Calvin
Tanggal   :28/03/2026
*/

public class Crypto extends InstrumenBase{
    //Atribut
    private double totalSupply;
    //Method
    public Crypto(String ticker, String nama, double CPrice,double Supply){
        super(ticker,nama,CPrice);
        this.totalSupply = Supply;
    }
    @Override
    public double getUkuranKontrak(){
        return 1;
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
    public String getNamaCrypto(){
        return nama;
    }
    public double getStok(){
        return totalSupply;
    }
    public void printValuasi(){
        System.out.println("Crypto     : " + nama);
        System.out.println("Harga      : " + CPrice);
        System.out.println("Stok       : " + totalSupply);
        System.out.println("Market Cap : " + getValuasi());
    }
}

