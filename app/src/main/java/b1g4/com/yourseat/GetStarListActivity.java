package b1g4.com.yourseat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class GetStarListActivity extends AppCompatActivity {

    private StarlistListViewAdapter starlistListViewAdapter;
    private ListView listView;
    private ArrayList<String> stringArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_star_list);

        Intent intent = getIntent();
        stringArrayList = intent.getStringArrayListExtra("starList");
        SavedSharedPreference.setStarListAddress(getApplicationContext(), stringArrayList);



//        starlistListViewAdapter = new StarlistListViewAdapter(stringArrayList);
//
//        listView = (ListView)findViewById(R.id.getStarListLV);
//        listView.setAdapter(starlistListViewAdapter);

        }

    @Override
    protected void onResume() {
        super.onResume();
        starlistListViewAdapter = new StarlistListViewAdapter(stringArrayList);
        starlistListViewAdapter.notifyDataSetChanged();
        SavedSharedPreference.setStarListAddress(getApplicationContext(), stringArrayList);

        listView = (ListView)findViewById(R.id.getStarListLV);
        listView.setAdapter(starlistListViewAdapter);

        starlistListViewAdapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String savedAddress = stringArrayList.get(position);
                String[] points = savedAddress.split(" → ");

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("savedPoints",points);
                setResult(1234,intent);
                finish();

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                stringArrayList.remove(position);
                starlistListViewAdapter.notifyDataSetChanged();
                SavedSharedPreference.setStarListAddress(getApplicationContext(), stringArrayList);
                return true;
            }
        });

    }
}
