package b1g4.com.yourseat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class StarlistListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> starList;

    public StarlistListViewAdapter(ArrayList<String> starList) {
        this.starList= starList;
    }

    @Override
    public int getCount() {
        return starList.size();
    }

    @Override
    public Object getItem(int position) {
        return starList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        context = parent.getContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_starlist, parent, false);
        }
        TextView tvRoute = convertView.findViewById(R.id.starListTV);
        tvRoute.setText(starList.get(position));
        return convertView;
    }
}
