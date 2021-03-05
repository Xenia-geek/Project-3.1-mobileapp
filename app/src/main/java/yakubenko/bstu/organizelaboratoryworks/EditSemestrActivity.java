package yakubenko.bstu.organizelaboratoryworks;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import yakubenko.bstu.organizelaboratoryworks.units.Semestr;


public class EditSemestrActivity extends AppCompatActivity {

    SQLiteDatabase db;
    Cursor userCursor;
    Cursor userCursor1;
    EditText namesem;
    EditText datestart;
    EditText dateend;
    Semestr semestr;

    int idg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_semestr);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        idg = intent.getIntExtra("id", -1);
        namesem =(EditText)findViewById(R.id.namesemestrs);
        datestart =(EditText)findViewById(R.id.startdate);
        dateend =(EditText)findViewById(R.id.enddate);

        db = DBHelper.getInstance(getApplicationContext()).getReadableDatabase();
    }


    @Override
    public void onResume() {
        super.onResume();

        db.execSQL("PRAGMA foreign_keys=ON");


        if (idg!=-1) {

            userCursor = db.rawQuery("select ID, DATE_START, DATE_END from SEMESTR where ID="+ String.valueOf(idg), null);
            if (userCursor.moveToFirst()) {
                while (!userCursor.isClosed()) {
                    semestr =new Semestr(userCursor.getInt(0),
                            userCursor.getString(1), userCursor.getString(2));
                    if (!userCursor.isLast()) {
                        userCursor.moveToNext();
                    } else {
                        userCursor.close();
                    }
                }
            }

            namesem.setText(String.valueOf(semestr.ID));
            datestart.setText(String.valueOf(semestr.DATE_START));
            dateend.setText(String.valueOf(semestr.DATE_END));

        }

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addClick(View view) {
        if (!datestart.getText().toString().isEmpty() || !dateend.getText().toString().isEmpty()) {
            db.execSQL("PRAGMA foreign_keys=ON");
            LocalDate datastart1 = LocalDate.parse(datestart.getText().toString());
            LocalDate dateend1 =LocalDate.parse(dateend.getText().toString());
            if(datastart1.compareTo(dateend1)<0) {
                if (idg == -1) {
                    db.execSQL("insert into SEMESTR(ID,DATE_START,DATE_END) VALUES(" + namesem.getText().toString() +
                            ",'" + datestart.getText().toString() + "','" + dateend.getText().toString() + "')");
                } else {
                    db.execSQL("UPDATE SEMESTR set DATE_START='" + datestart.getText().toString() +
                            "',DATE_END='" + dateend.getText().toString() +
                            "' , ID=" + namesem.getText().toString());

                }
                Toast toast = Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
            else{
                Toast toast = Toast.makeText(this,"Не правильный промежуток дат",Toast.LENGTH_SHORT);
                toast.show();
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
