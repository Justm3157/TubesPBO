/*  Instrumen.java
Deskripsi :berisi atribut dan method dalam class Instrumen
Pembuat  :Johan Reinhart Calvin
Tanggal   :28/03/2026
*/
import java.util.ArrayList;

public interface Instrumen {
    //Method
    abstract public double getValuasi();
    abstract public String getKodeInstrumen();
    abstract public String getNamaInstrumen();
    public abstract double getHargaSekarang();   // add this
    public abstract ArrayList<Double> getHistoriHarga();  // add this
    public abstract void nextHarga(double hargaBaru);
    public abstract double getUkuranKontrak();
} 
