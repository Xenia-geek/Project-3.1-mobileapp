package yakubenko.bstu.organizelaboratoryworks.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import yakubenko.bstu.organizelaboratoryworks.R;
import yakubenko.bstu.organizelaboratoryworks.units.Subject;

public class SubjectAdapter extends ArrayAdapter<Subject> {
    private LayoutInflater inflater;
    private int layout;
    private List<Subject> stories;

    public SubjectAdapter(Context context, int resource, List<Subject> list) {
        super(context, resource, list);
        this.stories = list;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView idView = (TextView) view.findViewById(R.id.text3);
        TextView nameView = (TextView) view.findViewById(R.id.text2);
        TextView DeView = (TextView) view.findViewById(R.id.text1);
        Subject state = stories.get(position);

        nameView.setText("Тип:"+state.SUBJECT);
        idView.setText("Количество:"+state.QUANTITY);
        DeView.setText("Предмет:"+state.TYPE);

        return view;
    }
}
