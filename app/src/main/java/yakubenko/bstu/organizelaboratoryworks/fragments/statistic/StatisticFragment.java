package yakubenko.bstu.organizelaboratoryworks.fragments.statistic;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
//import net.sqlcipher.database.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import yakubenko.bstu.organizelaboratoryworks.DBHelper;
import yakubenko.bstu.organizelaboratoryworks.R;
import yakubenko.bstu.organizelaboratoryworks.units.Statistic;
import yakubenko.bstu.organizelaboratoryworks.adapters.StatisticAdapter;
import yakubenko.bstu.organizelaboratoryworks.units.Story;

public class StatisticFragment extends Fragment  {
    Cursor userCursor;
    Cursor userCursor1;
    Cursor userCursor2;
    Cursor userCursor3;
    Cursor userCursor4;
    Cursor userCursor5;


    SQLiteDatabase db;
    private List<Statistic> statistics = new ArrayList();
    StatisticAdapter statisticAdapter;


    ListView listView;

    Statistic statistic;

    ArrayAdapter<String> cards;
    List<String> idsublist = new ArrayList();
    List<String> idsubstatlist = new ArrayList();
    List<String> sumquallist = new ArrayList();
    List<String> quanallist = new ArrayList();
    List<String> quantityforUSPEX = new ArrayList();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        db = DBHelper.getInstance(getActivity().getApplicationContext()).getReadableDatabase();
        View root = inflater.inflate(R.layout.fragment_statistic, container, false);
        listView=(ListView) root.findViewById(R.id.list_statistic);
        registerForContextMenu(listView);
        return root;
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
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
        for (String str : idsublist)
        {
            userCursor5=db.rawQuery("SELECT IDSUB FROM STATISTIC where IDSUB="+str, null);
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
            if(!idsubstatlist.isEmpty())
            {
                db.execSQL("DELETE FROM STATISTIC WHERE IDSUB="+str);
            }
                db.execSQL("insert  into STATISTIC(IDSUB) VALUES(" + str + ")");

                userCursor2 = db.rawQuery("select SUM(STORY.QUANTITY) from STORY where " +
                        "STORY.IDSUB=" + str, null);
                sumquallist.clear();
                if (userCursor2.moveToFirst()) {
                    while (!userCursor2.isClosed()) {
                        sumquallist.add(userCursor2.getString(0));
                        if (!userCursor2.isLast()) {
                            userCursor2.moveToNext();
                        } else {
                            userCursor2.close();
                        }
                    }
                }

                userCursor3 = db.rawQuery("select QUANTITY from SUBJECTS  where " +
                        "IDSUB=" + str, null);
                quanallist.clear();
                if (userCursor3.moveToFirst()) {
                    while (!userCursor3.isClosed()) {
                        quanallist.add(userCursor3.getString(0));
                        if (!userCursor3.isLast()) {
                            userCursor3.moveToNext();
                        } else {
                            userCursor3.close();
                        }
                    }
                }

                String temp1 = "";
                for (String str1 : sumquallist
                ) {
                    db.execSQL("UPDATE STATISTIC set PASSED=" + str1 + " where IDSUB=" + str);

                    for (String str2 : quanallist) {
                        db.execSQL("UPDATE STATISTIC set OSTALOS=" + str2 + "-" + str1 + " where IDSUB=" + str);
                    }
                }


        }

            userCursor = db.rawQuery("select DISTINCT STATISTIC.IDSUB,SUBJECTS.SUBJECT, " +
                    " SUBJECTS.TYPE, STATISTIC.PASSED, STATISTIC.OSTALOS from STATISTIC inner join SUBJECTS" +
                    " on STATISTIC.IDSUB=SUBJECTS.IDSUB inner join PLANS on PLANS.IDSUB=SUBJECTS.IDSUB", null);
            statistics.clear();

            if (userCursor.moveToFirst())
            {
                userCursor4 = db.rawQuery("select DATE, QUANTITY FROM PLANS WHERE IDSUB="+userCursor.getInt(0), null);
                quantityforUSPEX.clear();
                if (userCursor4.moveToFirst())
                {
                    while (!userCursor4.isClosed()) {
                        quantityforUSPEX.add(userCursor4.getString(0)+" "+
                                userCursor4.getInt(1));
                        if (!userCursor4.isLast()) {
                            userCursor4.moveToNext();
                        } else {
                            userCursor4.close();
                        }
                    }
                }
                int quantityfor=0;
                for(String str10 : quantityforUSPEX)
                {
                    String date =str10.toString().split("\\s")[0];
                    String counts1 = str10.toString().split("\\s")[1];
                    int counts = Integer.parseInt(counts1);
                    LocalDate datenow = LocalDate.now();
                    LocalDate datesubplan = LocalDate.parse(date);
                    if(datesubplan.compareTo(datenow)<0)
                    {
                        System.out.println("FFFFFFFFFFFFF"+ quantityfor);
                        quantityfor=quantityfor+counts;
                    }

                }
                while (!userCursor.isClosed()) {
                    statistics.add( new Statistic(
                            userCursor.getInt(0),
                            userCursor.getString(1),
                            userCursor.getString(2),
                            userCursor.getInt(3),
                            userCursor.getInt(4),
                            quantityfor));
                    if (!userCursor.isLast()) {
                        userCursor.moveToNext();
                    } else {
                        userCursor.close();
                    }
                }
            }
            statisticAdapter = new StatisticAdapter(getActivity().getApplicationContext(), R.layout.list_item_statistic, statistics);
            listView.setAdapter(statisticAdapter);
            statisticAdapter.notifyDataSetChanged();


    }


}

