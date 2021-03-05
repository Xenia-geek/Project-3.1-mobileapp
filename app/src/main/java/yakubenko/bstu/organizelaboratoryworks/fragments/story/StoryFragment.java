package yakubenko.bstu.organizelaboratoryworks.fragments.story;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

//import net.sqlcipher.Cursor;
//import net.sqlcipher.database.SQLiteDatabase;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

import yakubenko.bstu.organizelaboratoryworks.DBHelper;
import yakubenko.bstu.organizelaboratoryworks.EditStoryActivity;
import yakubenko.bstu.organizelaboratoryworks.R;
import yakubenko.bstu.organizelaboratoryworks.units.Story;
import yakubenko.bstu.organizelaboratoryworks.adapters.StoryAdapter;

public class StoryFragment extends Fragment {
    Cursor userCursor;
    SQLiteDatabase db;
    private List<Story> stories = new ArrayList();
    StoryAdapter storyAdapter;


    ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

       db = DBHelper.getInstance(getActivity().getApplicationContext()).getReadableDatabase();
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    db = DBHelper.getInstance(getActivity().getApplicationContext()).getReadableDatabase();
                    userCursor = db.rawQuery("select IDSUB,SUBJECT,TYPE,QUANTITY,WEEKNAME, NAMESEM from SUBJECTS", null);
                    if (userCursor.getCount() > 0) {
                        Intent intent = new Intent(getActivity(), EditStoryActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "Добавьте запись", Toast.LENGTH_SHORT).show();
                    }
                }
        });
        View root = inflater.inflate(R.layout.fragment_story, container, false);
        listView=(ListView) root.findViewById(R.id.list_story);
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

            userCursor = db.rawQuery("select ID,SUBJECTS.SUBJECT,STORY.IDSUB,STORY.QUANTITY, SUBJECTS.TYPE, DATE  from STORY join SUBJECTS on STORY.IDSUB=SUBJECTS.IDSUB", null);
            stories.clear();
            if (userCursor.moveToFirst()) {
                while (!userCursor.isClosed()) {
                    stories.add(new Story(userCursor.getInt(0), userCursor.getString(1),userCursor.getInt(2),
                            userCursor.getInt(3),userCursor.getString(4),userCursor.getString(5)));
                    if (!userCursor.isLast()) {
                        userCursor.moveToNext();
                    } else {
                        userCursor.close();
                    }
                }
            }
            storyAdapter = new StoryAdapter(getActivity().getApplicationContext(), R.layout.list_item_story, stories);

            listView.setAdapter(storyAdapter);
            storyAdapter.notifyDataSetChanged();

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
                        Intent intent = new Intent(getActivity(), EditStoryActivity.class);
                        intent.putExtra("id", (int) stories.get(info.position).IDSTORY);
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
                        delete(info.position);
                    }
                }).setNegativeButton("Нет",null).setMessage("Удалить эту запись?");
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
    public void delete(int pos) {
        if (!db.isOpen()) {
            db = DBHelper.getInstance(getContext()).getWritableDatabase();
            db.execSQL("PRAGMA foreign_keys=ON");
        }
        else {
            db.close();
            db = DBHelper.getInstance(getContext()).getWritableDatabase();
            db.execSQL("PRAGMA foreign_keys=ON");
        }
        db.execSQL("DELETE FROM STORY WHERE ID="+ stories.get(pos).IDSTORY);
        db.execSQL("UPDATE SUBJECTS set QUANTITY=QUANTITY+"+String.valueOf(stories.get(pos).COUNT)+
                " where IDSUB="+ stories.get(pos).IDLAB_ID);
        db.close();
        onResume();
    }

}