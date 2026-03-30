/*  User.java
Deskripsi :berisi atribut dan method dalam class User
Pembuat  :Johan Reinhart Calvin
Tanggal   :28/03/2026
*/


abstract class User {
    //Atribut
    private String id;
    private String nama;
    //Method
    public User(){
        id = "placeholder";
        nama = "placeholder";
    }
    public User(String id,String nama){
        this.id = id;
        this.nama = nama;
    }
    public String getID(){
        return id;
    }
    public String getNama(){
        return nama;
    }
    public void setID(String id){
        this.id = id;
    }
    public void setNama(String nama){
        this.nama = nama;
    }
    abstract public void displayInfo();
}
