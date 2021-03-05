package yakubenko.bstu.organizelaboratoryworks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import yakubenko.bstu.organizelaboratoryworks.units.Subject;

public class EditSubjectActivity extends AppCompatActivity {

    SQLiteDatabase db;
    Cursor userCursor;
    Cursor userCursor1;
    EditText quantity;
    EditText subjectName;
    Spinner type;
    Spinner semestr;
    Spinner weekname;
    Subject subject;

    ArrayAdapter<String> types;
    ArrayAdapter<String> weeks;
    String[] typesarray = new String[]{"Лабораторная","Коллоквиум","Контрольная"} ;
    String[] weeksarray = new String[]{"понедельник","вторник","среда","четверг","пятница","суббота"} ;

    ArrayAdapter<String> cards;
    List<String> cardslist = new ArrayList();


    int idgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subject);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        idgr = intent.getIntExtra("id", -1);
        subjectName =(EditText)findViewById(R.id.nedit);
        quantity =(EditText)findViewById(R.id.balans);
        type=(Spinner)findViewById(R.id.typespinner);
        weekname=(Spinner)findViewById(R.id.weekdayname);
        semestr=(Spinner)findViewById(R.id.namesemestr);



        db = DBHelper.getInstance(getApplicationContext()).getReadableDatabase();
    }


    @Override
    public void onResume() {
        super.onResume();

        db.execSQL("PRAGMA foreign_keys=ON");


        weeks = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,weeksarray);
        weeks.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekname.setAdapter(weeks);
        weeks.notifyDataSetChanged();

        types = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,typesarray);
        types.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(types);
        types.notifyDataSetChanged();


        userCursor1 =  db.rawQuery("select ID from SEMESTR", null);
        cardslist.clear();
        if (userCursor1.moveToFirst()) {
            while(!userCursor1.isClosed()) {
                cardslist.add(userCursor1.getString(0));
                if (!userCursor1.isLast()) {userCursor1.moveToNext();}
                else {userCursor1.close();}
            }
        }
        cards = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,cardslist);
        cards.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semestr.setAdapter(cards);
        cards.notifyDataSetChanged();

        if (idgr!=-1) {

            userCursor = db.rawQuery("select IDSUB,SUBJECT,TYPE,QUANTITY,WEEKNAME, NAMESEM from SUBJECTS  where IDSUB="+ String.valueOf(idgr), null);
            if (userCursor.moveToFirst()) {
                while (!userCursor.isClosed()) {
                    subject =new Subject(userCursor.getInt(0), userCursor.getString(1),
                            userCursor.getString(2),userCursor.getInt(3),
                            userCursor.getString(4),userCursor.getInt(5));
                    if (!userCursor.isLast()) {
                        userCursor.moveToNext();
                    } else {
                        userCursor.close();
                    }
                }
            }

            quantity.setText(String.valueOf(subject.QUANTITY));
            subjectName.setText(String.valueOf(subject.SUBJECT));

            String temp1="";
            for (String str: cardslist
            ) {

                if (str.split("\\s")[0].equals(String.valueOf(subject.NAMESEM))) {
                    temp1=str;
                    break;
                }
            }

            semestr.setSelection(cardslist.indexOf(temp1));



            int index2=-1;
            for (String str: typesarray
            ) {
                index2++;
                if (str.equals(String.valueOf(subject.TYPE))) {
                    break;
                }
            }
            type.setSelection(index2);

            int index3=-1;
            for (String str: weeksarray
            ) {
                index3++;
                if (str.equals(String.valueOf(subject.WEEKNAME))) {
                    break;
                }
            }
            weekname.setSelection(index3);


        }






    }


    public void addClick(View view) {
        if (!quantity.getText().toString().isEmpty() || !subjectName.getText().toString().isEmpty()) {
            db.execSQL("PRAGMA foreign_keys=ON");
            if (idgr == -1) {
                db.execSQL("insert into SUBJECTS(SUBJECT,TYPE,QUANTITY,WEEKNAME,NAMESEM) VALUES('" + subjectName.getText().toString() +
                        "','" + type.getSelectedItem().toString() + "'," + quantity.getText().toString() + ",'" +
                        weekname.getSelectedItem().toString()+"',"+ semestr.getSelectedItem().toString()+")");
            } else {
                db.execSQL("UPDATE SUBJECTS set SUBJECT='" +subjectName.getText().toString() + "',TYPE='" +type.getSelectedItem().toString()  +
                        "',QUANTITY=" + quantity.getText().toString() +
                        ",WEEKNAME='" + weekname.getSelectedItem().toString()+
                        "',NAMESEM="+ semestr.getSelectedItem().toString()+
                        " where IDSUB="+String.valueOf(idgr));

            }
            Toast toast = Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }
        else {
            Toast toast = Toast.makeText(this,"Заполните все поля",Toast.LENGTH_SHORT);
            toast.show();
        }
    }



    @Override
    public void onDestroy(){
        // Закрываем подключение и курсор
        if(db.isOpen()) db.close();
        super.onDestroy();
    }

    public void exitClick(View view) {
        finish();
    }
}
