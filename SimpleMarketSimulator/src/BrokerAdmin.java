/*  BrokerAdmin.java
Deskripsi :berisi atribut dan method dalam class BrokerAdmin
Pembuat  :Johan Reinhart Calvin
Tanggal   :28/03/2026
*/


class BrokerAdmin extends User {
    //Method
    public BrokerAdmin(){
        super();
    }
    public BrokerAdmin(String id,String nama){
        super(id, nama);
    }
    @Override
    public void displayInfo(){
        System.out.println("Info BrokerAdmin:");
        System.out.println("ID Admin                  :" + this.getID());
        System.out.println("Nama BrokerAdmin       :" + this.getNama());
    }    
}
