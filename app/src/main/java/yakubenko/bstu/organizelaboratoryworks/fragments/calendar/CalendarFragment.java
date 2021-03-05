package yakubenko.bstu.organizelaboratoryworks.fragments.calendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import yakubenko.bstu.organizelaboratoryworks.CategoryActivity;
import yakubenko.bstu.organizelaboratoryworks.EditTaskActivity;
import yakubenko.bstu.organizelaboratoryworks.R;
import yakubenko.bstu.organizelaboratoryworks.xmlcalendar.Serializator;
import yakubenko.bstu.organizelaboratoryworks.xmlcalendar.Share;
import yakubenko.bstu.organizelaboratoryworks.xmlcalendar.Task;
import yakubenko.bstu.organizelaboratoryworks.xmlcalendar.WorkWithFile;

public class CalendarFragment extends Fragment {

    CalendarView calendarView;
    ListView listTask;
    Calendar dateAndTime = Calendar.getInstance();
    String date;
    ArrayAdapter<Task> adapterTask;

    Button addCategoryButton, addButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView = root.findViewById(R.id.calendarView);
        listTask = root.findViewById(R.id.listTask);

        File file = new File (getContext().getFilesDir(),"task.xml");
        if (file.exists() == true)
        {
            Share.document = WorkWithFile.Xml.parseFromFile(file);
            Serializator.parse(Share.document);
        }

        listTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EditTaskActivity.class);
                intent.putExtra("isCreate",false);
                intent.putExtra("id",position);
                startActivity(intent);
            }
        });

        addCategoryButton = root.findViewById(R.id.addButtonCategory);
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                startActivity(intent);
            }
        });

        addButton = root.findViewById(R.id.addButtonTask);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Share.Categories.getList().size() > 0)
                {
                    Intent intent = new Intent(getActivity(), EditTaskActivity.class);
                    intent.putExtra("isCreate",true);
                    intent.putExtra("date", date );

                    startActivity(intent);
                }
                else
                {
                    msg(getActivity(), "Вначале нужно добавить хотя бы одну категорию!");
                }
            }
        });

        calendarView.setOnDateChangeListener((view, year,  month,  dayOfMonth)-> {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, month);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            date = DateUtils.formatDateTime(getContext(),
                    dateAndTime.getTimeInMillis(),
                    DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR);

            search();
        });
        firstcheck();

        return root;
    }


    public void firstcheck() {
        date = DateUtils.formatDateTime(getContext(),
                calendarView.getDate(),
                DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR);
        search();
    }

    public void search()
    {
        ArrayList<Task> taskList = new ArrayList<Task>();
        for(Task task : Share.Tasks.getList())
        {
            if(task.getDate().equals(date) == true)
            {
                taskList.add(task);
            }
        }
        adapterTask = new ArrayAdapter<Task>(getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                taskList);
        listTask.setAdapter(adapterTask);
    }

    public void msg(Context context, String str)
    {
        Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();
    }
}