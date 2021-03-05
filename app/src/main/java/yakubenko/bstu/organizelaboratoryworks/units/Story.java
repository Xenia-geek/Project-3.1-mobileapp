package yakubenko.bstu.organizelaboratoryworks.units;

public class Story {
    public int IDSTORY;
    public int IDLAB;
    public int IDLAB_ID;
    public String SUBJECT;
    public String TYPE;
    public int COUNT;
    public String DATE;

    public Story(int IDSTORY, int IDLAB_ID, int COUNT, String DATE){
        this.IDSTORY = IDSTORY;
        this.IDLAB_ID = IDLAB_ID;
        this.COUNT = COUNT;
        this.DATE = DATE;
    }

    public Story(int IDSTORY, String SUBJECT, int IDLAB_ID, int COUNT, String TYPE, String DATE){
        this.IDSTORY = IDSTORY;
        this.IDLAB_ID = IDLAB_ID;
        this.TYPE = TYPE;
        this.SUBJECT = SUBJECT;
        this.COUNT = COUNT;
        this.DATE = DATE;
    }
}
