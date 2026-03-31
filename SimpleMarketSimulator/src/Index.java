/*  Index.java
Deskripsi :berisi atribut dan method dalam class Index
Pembuat  :Johan Reinhart Calvin
Tanggal   :28/03/2026
*/

public class Index extends InstrumenBase{
    //Atribut

    //Method
    public Index(String ticker, String nama, double CPrice){
        super(ticker,nama,CPrice);
    }
    @Override
    public double getUkuranKontrak(){
        return 10;
    }
    @Override
    public double getValuasi(){
        return 0;
    }
    public String getNamaTicker(){
        return ticker;
    }
    public String getNamaIndex(){
        return nama;
    }
    public void printValuasi(){
        System.out.println("Index      : " + nama);
        System.out.println("Harga      : " + CPrice);
    }
}

