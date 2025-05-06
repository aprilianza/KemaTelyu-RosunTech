import java.util.Date;

public class Registration {
    private int id;
    private Event event;
    private Mahasiswa mahasiswa;
    private Date date;
    private String status;
    private boolean isVerified;

    public void approve(){
        this.status = "Diterima";
        this.isVerified = true;
    }
    public void cancel(){
        this.status = "Dibatalkan";
        this.isVerified = false;
    }
    public String getStatus(){
        return status;
    }
    public boolean isActive(){
        return isVerified;
    }
    public int getID(){
        return id;
    }
    public Event getEvent(){
        return event;
    }
    public Mahasiswa getMahasiswa(){
        return mahasiswa;
    }
    public Date getDate(){
        return date;
    }
    public void setEvent(Event event){
        this.event = event;
    }
    public void setMahasiswa(Mahasiswa m){
        this.mahasiswa = m;
    }
    public void setDate(Date date){
        this.date = date;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public void setIsVerified(boolean verified){
        this.isVerified = verified;
    }
}
