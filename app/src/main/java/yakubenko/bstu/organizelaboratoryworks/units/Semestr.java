package yakubenko.bstu.organizelaboratoryworks.units;

public class Semestr {
    public int ID;

    public String DATE_START;
    public String DATE_END;

    public Semestr(int ID, String DATE_START, String DATE_END){
        this.ID = ID;

        this.DATE_START = DATE_START;
        this.DATE_END = DATE_END;
    }
}
