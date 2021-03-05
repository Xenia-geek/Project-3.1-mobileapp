package yakubenko.bstu.organizelaboratoryworks.adapters;


import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.List;

import yakubenko.bstu.organizelaboratoryworks.R;
import yakubenko.bstu.organizelaboratoryworks.units.Statistic;

public class StatisticAdapter extends ArrayAdapter<Statistic> {
    private LayoutInflater inflater;
    private int layout;
    private List<Statistic> stories;

    public StatisticAdapter(Context context, int resource, List<Statistic> list) {
        super(context, resource, list);
        this.stories = list;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView idView = (TextView) view.findViewById(R.id.text3);
        TextView nameView = (TextView) view.findViewById(R.id.text2);
        TextView DeView = (TextView) view.findViewById(R.id.text1);
        TextView typess = (TextView) view.findViewById(R.id.text4);
        TextView uspivaem = (TextView) view.findViewById(R.id.text5);
        Statistic state = stories.get(position);

        nameView.setText("Предмет:"+state.SUBJECT);
        typess.setText("Тип:"+state.TYPE);
        idView.setText("Осталось:"+state.OSTALOS);
        DeView.setText("Сдано:"+state.PASSED);

        if(state.QUANTPLAN<state.PASSED) {
            uspivaem.setText("Успеваемость: Хорошо");
        }
        else{
            uspivaem.setText("Успеваемость: Плохо, поторопись!!");
        }
        return view;
    }
}
