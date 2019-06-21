package b1g4.com.yourseat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SavedRouteListViewAdapter extends BaseAdapter {
    private ArrayList<String> routeList;

    public SavedRouteListViewAdapter(ArrayList<String> routeList) {
        this.routeList = routeList;
    }

    @Override
    public int getCount() {
        return routeList.size();
    }

    @Override
    public Object getItem(int position) {
        return routeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_route, parent, false);
        }
        TextView tvRoute = convertView.findViewById(R.id.routeTV);
        tvRoute.setText(routeList.get(position));


        return convertView;
    }
}
