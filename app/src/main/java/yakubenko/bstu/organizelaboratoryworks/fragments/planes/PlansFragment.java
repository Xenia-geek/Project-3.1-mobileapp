package yakubenko.bstu.organizelaboratoryworks.fragments.planes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Calendar;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import yakubenko.bstu.organizelaboratoryworks.CategoryActivity;
import yakubenko.bstu.organizelaboratoryworks.DBHelper;
import yakubenko.bstu.organizelaboratoryworks.EditStoryActivity;
import yakubenko.bstu.organizelaboratoryworks.R;
import yakubenko.bstu.organizelaboratoryworks.adapters.PlansAdapter;
import yakubenko.bstu.organizelaboratoryworks.units.Plans;

public class PlansFragment extends Fragment {
    Cursor userCursor;
    Cursor userCursor1;
    Cursor userCursor2;
    Cursor userCursor3;
    Cursor userCursor4;
    Cursor userCursor5;
    Cursor userCursor7;
    Cursor userCursor8;

    SQLiteDatabase db;
    private List<Plans> planss = new ArrayList();
    PlansAdapter plansAdapter;


    ListView listView;

    Plans plans;

    ArrayAdapter<String> cards;
    List<String> idsublist = new ArrayList();
    List<String> idsubstatlist = new ArrayList();
    List<String> weekdaycount = new ArrayList();
    List<String> dayssemestrlist = new ArrayList();
    List<String> dateforplans = new ArrayList();
    List<String> quantlabslist = new ArrayList();


    Button SortBySub, SortByDate;
    Spinner subject;
    List<String> cardslist = new ArrayList();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        new AsyncTaskMy().execute();

        db = DBHelper.getInstance(getActivity().getApplicationContext()).getReadableDatabase();
        View root = inflater.inflate(R.layout.fragment_plans, container, false);
        subject =root.findViewById(R.id.fororder);

        SortBySub = root.findViewById(R.id.sortsub);
        SortBySub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = DBHelper.getInstance(getContext()).getReadableDatabase();
                db.execSQL("PRAGMA foreign_keys=ON");

                userCursor8 = db.rawQuery("select PLANS.ID, SUBJECTS.SUBJECT, PLANS.IDSUB, PLANS.QUANTITY, SUBJECTS.TYPE, PLANS.DATE  " +
                        "from PLANS inner join SUBJECTS on PLANS.IDSUB=SUBJECTS.IDSUB WHERE PLANS.IDSUB="+subject.getSelectedItem().toString().split("\\s")[0] +" ORDER BY PLANS.DATE", null);
                planss.clear();
                if (userCursor8.moveToFirst())
                {
                    while (!userCursor8.isClosed()) {

                            planss.add(new Plans(userCursor8.getInt(0), userCursor8.getString(1),
                                    userCursor8.getInt(2),
                                    userCursor8.getInt(3), userCursor8.getString(4), userCursor8.getString(5)));

                        if (!userCursor8.isLast()) {
                            userCursor8.moveToNext();
                        } else {
                            userCursor8.close();
                        }
                    }
                }
                System.out.println(planss);
                plansAdapter = new PlansAdapter(getActivity().getApplicationContext(), R.layout.list_item_plans, planss);
                listView.setAdapter(plansAdapter);
                plansAdapter.notifyDataSetChanged();
            }
        });
        SortByDate = root.findViewById(R.id.sortdate);
        SortByDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                db = DBHelper.getInstance(getContext()).getReadableDatabase();
                db.execSQL("PRAGMA foreign_keys=ON");

                userCursor4 = db.rawQuery("select PLANS.ID, SUBJECTS.SUBJECT, PLANS.IDSUB, PLANS.QUANTITY, SUBJECTS.TYPE, PLANS.DATE  " +
                        "from PLANS inner join SUBJECTS on PLANS.IDSUB=SUBJECTS.IDSUB ORDER BY PLANS.DATE", null);
                planss.clear();
                LocalDate nowdate = LocalDate.now();
                if (userCursor4.moveToFirst())
                {
                    while (!userCursor4.isClosed()) {
                        String pland = userCursor4.getString(5);
                        LocalDate plandate=LocalDate.parse(pland);
                        if(nowdate.compareTo(plandate) < 0) {
                            planss.add(new Plans(userCursor4.getInt(0), userCursor4.getString(1),
                                    userCursor4.getInt(2),
                                    userCursor4.getInt(3), userCursor4.getString(4), userCursor4.getString(5)));
                        }
                        if (!userCursor4.isLast()) {
                            userCursor4.moveToNext();
                        } else {
                            userCursor4.close();
                        }
                    }
                }
                System.out.println(planss);
                plansAdapter = new PlansAdapter(getActivity().getApplicationContext(), R.layout.list_item_plans, planss);
                listView.setAdapter(plansAdapter);
                plansAdapter.notifyDataSetChanged();
            }
        });


        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = DBHelper.getInstance(getContext()).getReadableDatabase();
                db.execSQL("PRAGMA foreign_keys=ON");

                userCursor4 = db.rawQuery("select PLANS.ID, SUBJECTS.SUBJECT, PLANS.IDSUB, PLANS.QUANTITY, SUBJECTS.TYPE, PLANS.DATE  " +
                        "from PLANS inner join SUBJECTS on PLANS.IDSUB=SUBJECTS.IDSUB ORDER BY PLANS.DATE", null);
                planss.clear();

                if (userCursor4.moveToFirst())
                {
                    while (!userCursor4.isClosed()) {
                        planss.add(new Plans(userCursor4.getInt(0), userCursor4.getString(1),
                                userCursor4.getInt(2),
                                userCursor4.getInt(3),userCursor4.getString(4),userCursor4.getString(5)));

                        if (!userCursor4.isLast()) {
                            userCursor4.moveToNext();
                        } else {
                            userCursor4.close();
                        }
                    }
                }
                System.out.println(planss);
                plansAdapter = new PlansAdapter(getActivity().getApplicationContext(), R.layout.list_item_plans, planss);
                listView.setAdapter(plansAdapter);
                plansAdapter.notifyDataSetChanged();
            }
        });


        listView=(ListView) root.findViewById(R.id.list_plans);
        registerForContextMenu(listView);
        return root;
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    class AsyncTaskMy extends AsyncTask<Void, Void, Void> {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {

            db = DBHelper.getInstance(getContext()).getReadableDatabase();
            db.execSQL("PRAGMA foreign_keys=ON");


            userCursor1 = db.rawQuery("select IDSUB from SUBJECTS ", null);
            idsublist.clear();
            if (userCursor1.moveToFirst()) {
                while (!userCursor1.isClosed()) {
                    idsublist.add(userCursor1.getString(0));
                    if (!userCursor1.isLast()) {
                        userCursor1.moveToNext();
                    } else {
                        userCursor1.close();
                    }
                }
            }

            String temp = "";
            for (String str : idsublist) {

                userCursor2=db.rawQuery("SELECT SEMESTR.DATE_START, SEMESTR.DATE_END FROM SEMESTR inner join SUBJECTS" +
                        " on SEMESTR.ID=SUBJECTS.NAMESEM where SUBJECTS.IDSUB="+str,null);
                dayssemestrlist.clear();
                if (userCursor2.moveToFirst()) {
                    while (!userCursor2.isClosed()) {
                        dayssemestrlist.add(userCursor2.getString(0)+" "+userCursor2.getString(1));
                        if (!userCursor2.isLast()) {
                            userCursor2.moveToNext();
                        } else {
                            userCursor2.close();
                        }
                    }
                }

                for (String str2 : dayssemestrlist) {
                    String datestart=str2.toString().split("\\s")[0];
                    String dateend=str2.toString().split("\\s")[1];


                    userCursor3=db.rawQuery("SELECT WEEKNAME FROM SUBJECTS " +
                            "where IDSUB="+str,null);
                    weekdaycount.clear();
                    if (userCursor3.moveToFirst()) {
                        while (!userCursor3.isClosed()) {
                            weekdaycount.add(userCursor3.getString(0));
                            if (!userCursor3.isLast()) {
                                userCursor3.moveToNext();
                            } else {
                                userCursor3.close();
                            }
                        }
                    }

                    String weekdaynames = null;


                    for (String str4 : weekdaycount) {


                        if (str4.equals("понедельник")) {
                            weekdaynames = "MONDAY";
                        } else if (str4.equals("вторник")) {
                            weekdaynames = "TUESDAY";
                        } else if (str4.equals("среда")) {
                            weekdaynames = "WEDNESDAY";
                        } else if (str4.equals("четверг")) {
                            weekdaynames = "THURSDAY";
                        } else if (str4.equals("пятница")) {
                            weekdaynames = "FRIDAY";
                        } else if (str4.equals("суббота")) {
                            weekdaynames = "SATURDAY";
                        }

                    }
                       int countdaysweek = 0;
                        LocalDate datestartd;
                        LocalDate dateendd;

                        datestartd = LocalDate.parse(datestart);
                        dateendd = LocalDate.parse(dateend);

                        dateendd = dateendd.plusDays(1);
                        dateforplans.clear();
                        while (datestartd.compareTo(dateendd) < 0) {

                            if (datestartd.getDayOfWeek().toString() == weekdaynames) {
                                String dates = datestartd.toString();
                                dateforplans.add(dates);
                                countdaysweek++;
                            }
                            datestartd = datestartd.plusDays(1);
                        }

                        System.out.println(weekdaynames);
                        userCursor = db.rawQuery("SELECT QUANTITY FROM SUBJECTS " +
                                " where IDSUB=" + str, null);
                        quantlabslist.clear();
                        if (userCursor.moveToFirst()) {
                            while (!userCursor.isClosed()) {
                                quantlabslist.add(userCursor.getString(0));
                                if (!userCursor.isLast()) {
                                    userCursor.moveToNext();
                                } else {
                                    userCursor.close();
                                }
                            }
                        }
                        System.out.println(quantlabslist);
                        double kolsdach;
                        for (String str9 : quantlabslist) {
                            userCursor5 = db.rawQuery("SELECT IDSUB FROM PLANS where IDSUB=" + str, null);
                            idsubstatlist.clear();
                            if (userCursor5.moveToFirst()) {
                                while (!userCursor5.isClosed()) {
                                    idsubstatlist.add(userCursor5.getString(0));
                                    if (!userCursor5.isLast()) {
                                        userCursor5.moveToNext();
                                    } else {
                                        userCursor5.close();
                                    }
                                }
                            }
                            int count = Integer.parseInt(str9);
                            System.out.println(count);
                            System.out.println(countdaysweek);
                            kolsdach = countdaysweek / count;
                            System.out.println(kolsdach);
                            System.out.println(kolsdach % 1);
                            double plussind1=kolsdach;
                            if (kolsdach % 1 == 0 && kolsdach >0.0 && kolsdach <=1.0) {
                                if (!idsubstatlist.isEmpty()) {
                                    db.execSQL("DELETE FROM PLANS WHERE IDSUB=" + str);
                                }
                                int podschot = 0;
                                while (podschot < countdaysweek) {
                                    int index = (int) (kolsdach - 1);
                                    String dateplans = dateforplans.get(index);
                                    db.execSQL("insert  into PLANS(IDSUB,QUANTITY,DATE) VALUES(" + str + ", 1,'" + dateplans + "')");
                                    //вставь код
                                    podschot++;
                                    System.out.println("kol"+kolsdach);
                                    kolsdach++;
                                }
                            }
                            else if (kolsdach < 1.0) {
                                if (!idsubstatlist.isEmpty()) {
                                    db.execSQL("DELETE FROM PLANS WHERE IDSUB=" + str);
                                }
                                int podschot = 0;
                                kolsdach = 1.0;
                                while (podschot < countdaysweek) {
                                    int index = (int) (kolsdach - 1);
                                    System.out.println("in"+index);
                                    System.out.println("po"+podschot);
                                    String dateplans = dateforplans.get(index);
                                    db.execSQL("insert  into PLANS(IDSUB,QUANTITY,DATE) VALUES(" + str + ", 1,'" + dateplans + "')");
                                    //вставь код
                                    podschot++;
                                    kolsdach++;
                                }
                                //вставь код
                                int newcount = count - countdaysweek;
                                System.out.println("newcount"+newcount);
                                float newkolsdach = countdaysweek / newcount;
                                float plussind=newkolsdach;
                                if (newkolsdach % 1 == 0 && newkolsdach> 0.0 && newkolsdach<= 1.0) {
                                    while (newkolsdach <= countdaysweek && podschot<count) {
                                        int index1 = (int) (newkolsdach-1);
                                        String dateplans1 = dateforplans.get(index1);
                                        db.execSQL("UPDATE PLANS set QUANTITY=2 WHERE IDSUB=" + str + " AND DATE='" + dateplans1+"'");
                                        //вставь код
                                        System.out.println("newkolsdach"+newkolsdach);
                                        newkolsdach=newkolsdach+plussind;
                                        podschot++;
                                    }
                                } else if (newkolsdach > 1.0) {
                                    while (newkolsdach <= countdaysweek && podschot<count) {
                                        int index = (int) (newkolsdach-1);
                                        String dateplans2 = dateforplans.get(index);
                                        db.execSQL("UPDATE PLANS set QUANTITY=2 WHERE IDSUB=" + str + " AND DATE='" + dateplans2+"'");
                                        //вставь код
                                        System.out.println("newkolsdach"+newkolsdach);
                                        newkolsdach=newkolsdach+plussind;
                                        podschot++;
                                    }
                                }
                            } else if (kolsdach > 1.0) {
                                if (!idsubstatlist.isEmpty()) {
                                    db.execSQL("DELETE FROM PLANS WHERE IDSUB=" + str);
                                }
                                int podschot = 0;
                                while (podschot <= count && kolsdach<=countdaysweek) {
                                    int index = (int) (kolsdach-1);
                                    String dateplans = dateforplans.get(index);
                                    db.execSQL("insert  into PLANS(IDSUB,QUANTITY,DATE) VALUES(" + str + ", 1,'" + dateplans + "')");
                                    //вставь код
                                    podschot++;
                                    kolsdach=kolsdach+plussind1;
                                }
                            }
                        }


                }
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            db = DBHelper.getInstance(getContext()).getReadableDatabase();
            db.execSQL("PRAGMA foreign_keys=ON");

            userCursor4 = db.rawQuery("select PLANS.ID, SUBJECTS.SUBJECT, PLANS.IDSUB, PLANS.QUANTITY, SUBJECTS.TYPE, PLANS.DATE  " +
                    "from PLANS inner join SUBJECTS on PLANS.IDSUB=SUBJECTS.IDSUB ORDER BY PLANS.DATE", null);
            planss.clear();

            if (userCursor4.moveToFirst())
            {
                while (!userCursor4.isClosed()) {
                    planss.add(new Plans(userCursor4.getInt(0), userCursor4.getString(1),
                            userCursor4.getInt(2),
                            userCursor4.getInt(3),userCursor4.getString(4),userCursor4.getString(5)));

                    if (!userCursor4.isLast()) {
                        userCursor4.moveToNext();
                    } else {
                        userCursor4.close();
                    }
                }
            }
            System.out.println(planss);
            plansAdapter = new PlansAdapter(getActivity().getApplicationContext(), R.layout.list_item_plans, planss);
            listView.setAdapter(plansAdapter);
            plansAdapter.notifyDataSetChanged();
        }

    }

        @Override
    public void onResume() {
        super.onResume();
    db = DBHelper.getInstance(getContext()).getReadableDatabase();
            db.execSQL("PRAGMA foreign_keys=ON");
    userCursor7 =  db.rawQuery("select IDSUB,SUBJECT,TYPE from SUBJECTS", null);
        cardslist.clear();
        if (userCursor7.moveToFirst()) {
        while(!userCursor7.isClosed()) {
            cardslist.add(userCursor7.getString(0)+" "+userCursor7.getString(1)+
                    " "+userCursor7.getString(2));
            if (!userCursor7.isLast()) {userCursor7.moveToNext();}
            else {userCursor7.close();}
        }
    }
        System.out.println(cardslist);
        cards = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,cardslist);
        cards.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subject.setAdapter(cards);
        cards.notifyDataSetChanged();

        }
}


