/*  Forex.java
Deskripsi :berisi atribut dan method dalam class Forex
Pembuat  :Johan Reinhart Calvin
Tanggal   :28/03/2026
*/

public class Forex extends InstrumenBase{
    //Atribut

    //Method
    public Forex(String ticker, String nama, double CPrice){
        super(ticker,nama,CPrice);
    }
    @Override
    public double getUkuranKontrak(){
        return 100000;
    }
    @Override
    public double getValuasi(){
        return 0;
    }
    public String getNamaTicker(){
        return ticker;
    }
    public String getNamaForex(){
        return nama;
    }
    public void printValuasi(){
        System.out.println("Forex     : " + nama);
        System.out.println("Harga      : " + CPrice);
    }
}

