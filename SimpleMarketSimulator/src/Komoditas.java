/*  Komoditas.java
Deskripsi :berisi atribut dan method dalam class Komoditas
Pembuat  :Johan Reinhart Calvin
Tanggal   :28/03/2026
*/

public class Komoditas extends InstrumenBase{
    //Atribut

    //Method
    public Komoditas(String ticker, String nama, double CPrice){
        super(ticker,nama,CPrice);
    }
    @Override
    public double getUkuranKontrak(){
        return 100;
    }
    @Override
    public double getValuasi(){
        return 0;
    }
    public String getNamaTicker(){
        return ticker;
    }
    public String getNamaKomoditas(){
        return nama;
    }
    public void printValuasi(){
        System.out.println("Komoditas  : " + nama);
        System.out.println("Harga      : " + CPrice);
    }
}

