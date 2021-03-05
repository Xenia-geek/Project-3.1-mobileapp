package yakubenko.bstu.organizelaboratoryworks.units;

public class Subject {
    public int IDSUB;
    public String SUBJECT;
    public String TYPE;
    public int QUANTITY;
    public String WEEKNAME;
    public int NAMESEM;
    public Subject(int IDSUB,String SUBJECT, String TYPE,int QUANTITY, String WEEKNAME, int NAMESEM){
        this.IDSUB =IDSUB;
        this.SUBJECT =SUBJECT;
        this.TYPE=TYPE;
        this.QUANTITY=QUANTITY;
        this.WEEKNAME=WEEKNAME;
        this.NAMESEM=NAMESEM;
    }
}
