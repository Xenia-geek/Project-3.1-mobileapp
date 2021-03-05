package yakubenko.bstu.organizelaboratoryworks.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import yakubenko.bstu.organizelaboratoryworks.R;
import yakubenko.bstu.organizelaboratoryworks.units.Story;

public class StoryAdapter extends ArrayAdapter<Story> {

    private LayoutInflater inflater;
    private int layout;
    private List<Story> stories;

    public StoryAdapter(Context context, int resource, List<Story> list){
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

        Story state = stories.get(position);

        idView.setText(state.SUBJECT);
        //nameView.setText("ID"+state.IDSTORY);
        dateView.setText(state.DATE + " Предмет: "+ state.TYPE);
        quantityView.setText("Сдал:"+state.COUNT);

        return view;

    }
}
