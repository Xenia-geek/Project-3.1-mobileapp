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
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import yakubenko.bstu.organizelaboratoryworks.units.Story;

import static java.security.AccessController.getContext;

public class EditStoryActivity extends AppCompatActivity {

    SQLiteDatabase db;
    Cursor userCursor;
    Cursor userCursor1;
    Cursor userCursor2;
    EditText quantity;
    EditText date;
    Spinner subject;

    List<String> quantityforstory = new ArrayList();
    List<String> quantityforupdatestory = new ArrayList();

    Story story;

    ArrayAdapter<String> cards;

    List<String> cardslist = new ArrayList();
    int idgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_story);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        idgr = intent.getIntExtra("id", -1);
        quantity =(EditText)findViewById(R.id.summ);
        date=(EditText)findViewById(R.id.date);
        subject =(Spinner)findViewById(R.id.cardspinner);
        db = DBHelper.getInstance(getApplicationContext()).getReadableDatabase();

    }

    @Override
    public void onResume() {
        super.onResume();

         db.execSQL("PRAGMA foreign_keys=ON");

        userCursor =  db.rawQuery("select IDSUB,SUBJECT,TYPE from SUBJECTS", null);
        cardslist.clear();
        if (userCursor.moveToFirst()) {
            while(!userCursor.isClosed()) {
                cardslist.add(userCursor.getString(0)+" "+userCursor.getString(1)+
                        " "+userCursor.getString(2));
                if (!userCursor.isLast()) {userCursor.moveToNext();}
                else {userCursor.close();}
            }
        }
        cards = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,cardslist);
        cards.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subject.setAdapter(cards);
        cards.notifyDataSetChanged();



        String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        date.setText(timeStamp);
        if (idgr!=-1) {


            userCursor = db.rawQuery("select ID,SUBJECTS.SUBJECT,STORY.IDSUB,STORY.QUANTITY, SUBJECTS.TYPE, DATE  from STORY join SUBJECTS on STORY.IDSUB=SUBJECTS.IDSUB where ID="+String.valueOf(idgr), null);
            if (userCursor.moveToFirst()) {
                while (!userCursor.isClosed()) {
                    story = new Story(userCursor.getInt(0), userCursor.getString(1),userCursor.getInt(2),
                            userCursor.getInt(3),userCursor.getString(4),userCursor.getString(5));
                    if (!userCursor.isLast()) {
                        userCursor.moveToNext();
                    } else {
                        userCursor.close();
                    }
                }
            }


            //subject.setText(story.IDLAB_ID);
            quantity.setText(String.valueOf(story.COUNT));
            date.setText(story.DATE);
            String temp="";
            for (String str: cardslist
            ) {

                if (str.split("\\s")[0].equals(String.valueOf(story.IDLAB_ID))) {
                    temp=str;
                    break;
                }
            }

            subject.setSelection(cardslist.indexOf(temp));



        }


        userCursor1 = db.rawQuery("select OSTALOS FROM STATISTIC WHERE IDSUB="+ subject.getSelectedItem().toString().split("\\s")[0], null);
        quantityforstory.clear();
        if (userCursor1.moveToFirst())
        {
            while (!userCursor1.isClosed()) {
                quantityforstory.add(userCursor1.getString(0));
                if (!userCursor1.isLast()) {
                    userCursor1.moveToNext();
                } else {
                    userCursor1.close();
                }
            }
        }

        userCursor2 = db.rawQuery("select QUANTITY FROM SUBJECTS WHERE IDSUB="+ subject.getSelectedItem().toString().split("\\s")[0], null);
        quantityforupdatestory.clear();
        if (userCursor2.moveToFirst())
        {
            while (!userCursor2.isClosed()) {
                quantityforupdatestory.add(userCursor2.getString(0));
                if (!userCursor2.isLast()) {
                    userCursor2.moveToNext();
                } else {
                    userCursor2.close();
                }
            }
        }

    }


    public void addClick(View view) {
        if (!quantity.getText().toString().isEmpty() || !date.getText().toString().isEmpty()) {
            db.execSQL("PRAGMA foreign_keys=ON");
            if (idgr == -1) {
                for (String str2 : quantityforstory) {
                    int quantmax = Integer.parseInt(str2);
                    int storyquant = Integer.parseInt(quantity.getText().toString().replace(",", "."));
                    if (quantmax >= storyquant) {

                            db.execSQL("insert into STORY(IDSUB,QUANTITY,DATE) VALUES('"
                                    + subject.getSelectedItem().toString().split("\\s")[0] +
                                    "','" + quantity.getText().toString().replace(",", ".") +
                                    "','" + date.getText().toString() + "')");
                            //db.execSQL("UPDATE SUBJECTS set QUANTITY=QUANTITY-"+(Double.parseDouble( quantity.getText().toString().replace(",",".")))+
                            //        " where IDSUB="+ subject.getSelectedItem().toString().split("\\s")[0]);

                            Toast toast = Toast.makeText(this, "Добавлено/изменено", Toast.LENGTH_SHORT);
                            toast.show();
                            finish();


                    } else {
                        Toast toast = Toast.makeText(this, "Вы ввели слишком большое число сданых лаб", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
            else {
                for (String str3 : quantityforupdatestory) {
                    int quantmax = Integer.parseInt(str3);
                    int storyquant = Integer.parseInt(quantity.getText().toString().replace(",", "."));
                    if (quantmax >= storyquant) {


                            db.execSQL("UPDATE STORY set IDSUB=" + subject.getSelectedItem().toString().split("\\s")[0] +
                                    ",QUANTITY=" + quantity.getText().toString().replace(",", ".") + ",DATE='" + date.getText().toString() + "'" + " where ID=" + String.valueOf(idgr));
                            //db.execSQL("UPDATE SUBJECTS set QUANTITY=QUANTITY+"+(story.COUNT-Double.parseDouble( quantity.getText().toString().replace(",",".")))+
                            //        " where IDSUB="+ story.IDLAB_ID);



                        Toast toast = Toast.makeText(this, "Добавлено/изменено", Toast.LENGTH_SHORT);
                        toast.show();
                        finish();
                    } else {
                        Toast toast = Toast.makeText(this, "Вы ввели слишком большое число сданых лаб", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }

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
