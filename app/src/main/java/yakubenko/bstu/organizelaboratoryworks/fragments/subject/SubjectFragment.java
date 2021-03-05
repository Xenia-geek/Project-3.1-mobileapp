package yakubenko.bstu.organizelaboratoryworks.fragments.subject;

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
import yakubenko.bstu.organizelaboratoryworks.EditSubjectActivity;
import yakubenko.bstu.organizelaboratoryworks.R;
import yakubenko.bstu.organizelaboratoryworks.units.Subject;
import yakubenko.bstu.organizelaboratoryworks.adapters.SubjectAdapter;



public class SubjectFragment extends Fragment {
    Cursor userCursor;
    SQLiteDatabase db;
    private List<Subject> subjects = new ArrayList();
    SubjectAdapter subjectAdapter;


    ListView listView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        db = DBHelper.getInstance(getActivity().getApplicationContext()).getReadableDatabase();
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                db = DBHelper.getInstance(getActivity().getApplicationContext()).getReadableDatabase();
                userCursor = db.rawQuery("select ID, DATE_START, DATE_END from SEMESTR", null);
                if (userCursor.getCount() > 0) {
                    Intent intent = new Intent(getActivity(), EditSubjectActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Добавьте запись", Toast.LENGTH_SHORT).show();
                }
            }
        });
        View root = inflater.inflate(R.layout.fragment_subject, container, false);
        listView=(ListView) root.findViewById(R.id.list_subject);
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
            userCursor = db.rawQuery("select * from SUBJECTS", null);
            subjects.clear();
            if (userCursor.moveToFirst()) {
                while (!userCursor.isClosed()) {
                    subjects.add(new Subject(userCursor.getInt(0), userCursor.getString(1),
                            userCursor.getString(2),userCursor.getInt(3),
                            userCursor.getString(4),userCursor.getInt(5)));
                    if (!userCursor.isLast()) {
                        userCursor.moveToNext();
                    } else {
                        userCursor.close();
                    }
                }
            }
            subjectAdapter = new SubjectAdapter(getActivity().getApplicationContext(), R.layout.list_item_subjects, subjects);
            listView.setAdapter(subjectAdapter);
            subjectAdapter.notifyDataSetChanged();

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

                        Intent intent = new Intent(getActivity(), EditSubjectActivity.class);
                        intent.putExtra("id", (int) subjects.get(info.position).IDSUB);
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
                        delete((int) subjects.get(info.position).IDSUB);
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
        db.execSQL("DELETE FROM SUBJECTS WHERE IDSUB="+id);
        db.close();
        onResume();
    }
}