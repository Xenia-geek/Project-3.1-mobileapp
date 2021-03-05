package yakubenko.bstu.organizelaboratoryworks.units;

public class Statistic {
    public int ID;
    public int IDSUB;
    public int PASSED;
    public int OSTALOS;
    public String SUBJECT;
    public int QUANTITY;
    public String TYPE;
    public String DATEPLAN;
    public int QUANTPLAN;

    public Statistic(int IDSUB, int PASSED, int OSTALOS){

        this.IDSUB = IDSUB;
        this.PASSED = PASSED;
        this.OSTALOS=OSTALOS;
    }
    public Statistic(int IDSUB, String SUBJECT, String TYPE, int PASSED, int OSTALOS, int QUANTPLAN){
        this.IDSUB = IDSUB;
        this.SUBJECT=SUBJECT;
        this.TYPE=TYPE;
        this.PASSED = PASSED;
        this.OSTALOS=OSTALOS;
        this.QUANTPLAN=QUANTPLAN;
    }
}
