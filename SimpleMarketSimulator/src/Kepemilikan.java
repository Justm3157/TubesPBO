/*  Kepemilikan.java
Deskripsi :berisi atribut dan method dalam class Kepemilikan
Pembuat  :Johan Reinhart Calvin
Tanggal   :28/03/2026
*/


class Kepemilikan{
    //Atribut
    private Instrumen nama;
    private double unit;
    private double hargaBeli;
    private double leverage;
    private double SL;
    private double TP;
    private String posisi;

    public Kepemilikan(Instrumen nama,double unit,double leverage,String posisi){
        this.nama = nama;
        this.unit = unit;
        this.hargaBeli = nama.getHargaSekarang();
        this.leverage = leverage;
        this.posisi = posisi;
        this.SL = 0;
        this.TP = 0;

    }
    public Instrumen getInstrumen(){
        return nama;
    }
    public double getUnit(){
        return unit;
    }
    public double gethargaBeli(){
        return hargaBeli;
    }
    public double getLeverage(){
        return leverage;
    }
    public double getNilaiInvestasi(){
        return nama.getHargaSekarang()*unit*leverage;
    }

    public void tambahUnit(double unitBaru){
        double hargaSekarang = nama.getHargaSekarang();
        hargaBeli = ((hargaBeli*unit) + (hargaSekarang*unitBaru))/(unit + unitBaru);
        unit += unitBaru;
    }

    public void kurangiUnit(double unitJual){
        this.unit -= unitJual;
    }
    public void setRiskManagement(double sl,double tp){
        this.SL = sl;
        this.TP = tp;
    }
    public double getSL(){
        return SL;
    }
    public double getTP(){
        return TP;
    }
    public double getPnL(){
        assert unit > 0: "Unit tidak boleh nol atau negatif" + unit;
        double hargaKini = nama.getHargaSekarang();
        double UkuranKontrak = nama.getUkuranKontrak();
        if(posisi.equals("LONG")){
            return (hargaKini - hargaBeli)*unit*UkuranKontrak;
        }
        else{
            return (hargaBeli - hargaKini)*unit*UkuranKontrak;
        }
    }
    public String getPosisi(){
        return posisi;
    }
}