package yakubenko.bstu.organizelaboratoryworks.fragments.semestr;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
//import net.sqlcipher.database.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import yakubenko.bstu.organizelaboratoryworks.DBHelper;
import yakubenko.bstu.organizelaboratoryworks.EditSemestrActivity;
import yakubenko.bstu.organizelaboratoryworks.R;
import yakubenko.bstu.organizelaboratoryworks.units.Semestr;
import yakubenko.bstu.organizelaboratoryworks.adapters.SemestrAdapter;



public class SemestrFragment extends Fragment  {
    Cursor userCursor;
    SQLiteDatabase db;
    private List<Semestr> semesters = new ArrayList();
    SemestrAdapter semestrAdapter;


    ListView listView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        db = DBHelper.getInstance(getActivity().getApplicationContext()).getReadableDatabase();
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(getActivity(), EditSemestrActivity.class);
                    startActivity(intent);

            }
        });
        View root = inflater.inflate(R.layout.fragment_semestr, container, false);
        listView=(ListView) root.findViewById(R.id.list_semestr);
        registerForContextMenu(listView);
        return root;
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        db = DBHelper.getInstance(getContext()).getReadableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON");
        userCursor = db.rawQuery("select * from SEMESTR", null);
        semesters.clear();
        if (userCursor.moveToFirst()) {
            while (!userCursor.isClosed()) {
                semesters.add(new Semestr(userCursor.getInt(0),
                        userCursor.getString(1),
                        userCursor.getString(2)));
                if (!userCursor.isLast()) {
                    userCursor.moveToNext();
                } else {
                    userCursor.close();
                }
            }
        }
        semestrAdapter = new SemestrAdapter(getActivity().getApplicationContext(), R.layout.list_item_semestr, semesters);
        listView.setAdapter(semestrAdapter);
        semestrAdapter.notifyDataSetChanged();

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.edit:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                builder2.setTitle("Внимание").setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent = new Intent(getActivity(), EditSemestrActivity.class);
                        intent.putExtra("id", (int) semesters.get(info.position).ID);
                        startActivity(intent);

                    }
                }).setNegativeButton("Назад",null).setMessage("Редактировать?");
                AlertDialog dialog2 = builder2.create();
                dialog2.show();
                return true;

            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Внимание").setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete((int) semesters.get(info.position).ID);
                    }
                }).setNegativeButton("Нет",null).setMessage("Удалить эту запись?");
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
    public void delete(int id) {
        if (!db.isOpen()) {
            db = DBHelper.getInstance(getContext()).getWritableDatabase();//----------------
            db.execSQL("PRAGMA foreign_keys=ON");
        }
        else {
            db.close();
            db = DBHelper.getInstance(getContext()).getWritableDatabase();//-----------
            db.execSQL("PRAGMA foreign_keys=ON");
        }
        db.execSQL("DELETE FROM SEMESTR WHERE ID="+id);
        db.close();
        onResume();
    }
}
