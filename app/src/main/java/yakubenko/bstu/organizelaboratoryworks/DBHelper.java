package yakubenko.bstu.organizelaboratoryworks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper instance;
    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="labs1.db";

    public  DBHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    static public synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }
    @Override

    public void onCreate(SQLiteDatabase db) {

        db.execSQL("PRAGMA foreign_keys=ON");

        db.execSQL("CREATE TABLE IF NOT EXISTS SEMESTR(ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "DATE_START TEXT NOT NULL,"+
                "DATE_END TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS SUBJECTS( IDSUB INTEGER PRIMARY KEY AUTOINCREMENT," +
                "TYPE TEXT NOT NULL," +
                "SUBJECT TEXT NOT NULL," +
                "QUANTITY NUMERIC NOT NULL,"+
                "WEEKNAME TEXT NOT NULL,"+
                "NAMESEM INTEGER REFERENCES SEMESTR(ID) ON UPDATE CASCADE ON DELETE CASCADE)");

        db.execSQL("CREATE INDEX TYPE_INDEX ON SUBJECTS(IDSUB)");



        db.execSQL("CREATE TABLE IF NOT EXISTS STORY(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "IDSUB INTEGER REFERENCES SUBJECTS(IDSUB) ON UPDATE CASCADE ON DELETE CASCADE," +
                "QUANTITY NUMERIC NOT NULL," +
                "DATE TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS STATISTIC("+
                "IDSUB INTEGER REFERENCES SUBJECTS(IDSUB) ON UPDATE CASCADE ON DELETE CASCADE PRIMARY KEY," +
                "PASSED NUMERIC ,"+
                "OSTALOS NUMERIC)");

        db.execSQL("CREATE TABLE IF NOT EXISTS PLANS(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "IDSUB INTEGER REFERENCES SUBJECTS(IDSUB) ON UPDATE CASCADE ON DELETE CASCADE," +
                "QUANTITY NUMERIC NOT NULL," +
                "DATE TEXT NOT NULL)");

        db.execSQL("INSERT INTO SEMESTR (ID, DATE_START, DATE_END) VALUES (5,'2020-09-01','2020-12-26')");

        db.execSQL("INSERT INTO SUBJECTS (SUBJECT, TYPE, QUANTITY, WEEKNAME,NAMESEM) VALUES ('БД', 'Лабораторная', 17, 'четверг',5)");
        db.execSQL("INSERT INTO SUBJECTS (SUBJECT, TYPE, QUANTITY, WEEKNAME,NAMESEM) VALUES ('БД', 'Коллоквиум', 2, 'вторник',5)");
        db.execSQL("INSERT INTO SUBJECTS (SUBJECT, TYPE, QUANTITY, WEEKNAME,NAMESEM) VALUES ('СТПМС', 'Лабораторная', 13, 'пятница',5)");
        db.execSQL("INSERT INTO SUBJECTS (SUBJECT, TYPE, QUANTITY, WEEKNAME,NAMESEM) VALUES ('ПБИСП', 'Контрольная', 3, 'среда',5)");

        db.execSQL("INSERT INTO STORY(IDSUB, QUANTITY, DATE) VALUES (1, 8, '2020-11-02')");
        db.execSQL("INSERT INTO STORY(IDSUB, QUANTITY, DATE) VALUES (2, 1, '2020-11-02')");
        db.execSQL("INSERT INTO STORY(IDSUB, QUANTITY, DATE) VALUES (3, 2, '2020-09-02')");
        db.execSQL("INSERT INTO STORY(IDSUB, QUANTITY, DATE) VALUES (4, 1, '2020-12-05')");

    }
    public void onUpgrade(SQLiteDatabase db,int OldV,int NewV) {
        db.execSQL("drop table if exists STORY");
        db.execSQL("drop table if exists SUBJECTS");
        db.execSQL("drop table if exists SEMESTR");
        db.execSQL("drop table if exists STATISTIC");
        db.execSQL("drop table if exists PLANS");
        onCreate(db);
    }


}

