package yakubenko.bstu.organizelaboratoryworks.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import yakubenko.bstu.organizelaboratoryworks.R;
import yakubenko.bstu.organizelaboratoryworks.units.Plans;

public class PlansAdapter extends ArrayAdapter<Plans> {

    private LayoutInflater inflater;
    private int layout;
    private List<Plans> stories;

    public PlansAdapter(Context context, int resource, List<Plans> list){
        super(context, resource, list);
        this.stories = list;
        this.inflater = LayoutInflater.from(context);
        this.layout = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View view = inflater.inflate(this.layout, parent, false);
        TextView idView = (TextView) view.findViewById(R.id.text1);
        TextView nameView = (TextView) view.findViewById(R.id.text2);
        TextView dateView = (TextView) view.findViewById(R.id.text3);
        TextView quantityView = (TextView) view.findViewById(R.id.text5);

        Plans state = stories.get(position);

        idView.setText("Тип: "+state.TYPE);
        nameView.setText("Предмет: "+state.SUBJECT);
        dateView.setText("Дата сдачи: "+state.DATE);
        quantityView.setText("Сдать: "+state.COUNT);

        return view;

    }
}

