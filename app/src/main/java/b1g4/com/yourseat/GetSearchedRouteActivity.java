package b1g4.com.yourseat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class GetSearchedRouteActivity extends AppCompatActivity {

    private TextView selectedItemView;
    private TextView startAddressView;
    private TextView endAddressView;
    private String startAddress;
    private String endAddress;
    private String isSearched;
    private ArrayList<ArrayList<String>> srouteList;
    private ArrayList<ArrayList<String>> srouteStationList; //heesu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_searched_route);

        Intent intent = getIntent();
        startAddress = intent.getStringExtra("startAddress");
        endAddress = intent.getStringExtra("endAddress");
        isSearched = intent.getStringExtra("isSearched");

        startAddressView = (TextView) findViewById(R.id.startAddress);
        endAddressView = (TextView) findViewById(R.id.endAddress);
        startAddressView.setText(startAddress);
        endAddressView.setText(endAddress);

        ListView listView = (ListView) findViewById(R.id.srouteList);
        selectedItemView = (TextView) findViewById(R.id.selectableRoute);

        // 출발-도착지 간 거리가 700m 이하여서 길찾기를 수행하지 않았을 시
        if(isSearched.equals("false")) {
            selectedItemView.setText("출발지와 도착지 간의 직선거리가 700m 미만인 경우, 결과를 제공하지 않습니다.");
        } else {
            selectedItemView.setText("이동 초기의 혼잡도 순으로 정렬된 아래 길찾기 중 선택하세요.");
            srouteList = (ArrayList<ArrayList<String>>) intent.getSerializableExtra("sRouteList");
            //heesu
            srouteStationList = (ArrayList<ArrayList<String>>) intent.getSerializableExtra("sRouteStationList");

            //리스트뷰와 sRouteList를 연결해준다.
            ArrayList<String> srouteStringList = new ArrayList<String>();
            for (int i = 0; i < srouteList.size(); i++) {
                SearchedRoute searchedRoute = new SearchedRoute(srouteList.get(i));
                String srouteListString;
                String a = "* 출발 정류장: " + srouteList.get(i).get(1) + "\n    - 노선명: " + srouteList.get(i).get(2);
                String b=null;
                String c = null;
                String e= null;
                if (srouteList.get(i).size() == 6) {    //환승이 없을 경우
                    b = "\n* 도착 정류장: " + srouteList.get(i).get(4) + "\n총 소요시간: " + srouteList.get(i).get(5);
                    srouteListString = a+b;
                    srouteStringList.add(srouteListString);
                }
                else {                               //환승이 있을 경우
                    for (int j = 0; j < searchedRoute.getTransferNum(); j++) {
                        c = "\n* 환승 정류장: " + searchedRoute.getTransferStationList().get(j) + "\n    - 노선명: " + searchedRoute.getTransferRouteList().get(j);
                        a += c;
                    }
                    e = "\n* 도착 정류장: " + searchedRoute.getEndStation()+"\n총 소요시간 - "+ searchedRoute.getTime();
                    srouteListString = a+e;
                    srouteStringList.add(srouteListString);
                }

                String stationListString = "\n\n[정류장 목록]\n";
                int countStations = srouteStationList.get(i).size();
                for(int j=0;j<countStations;j++){
                    stationListString += " - " + srouteStationList.get(i).get(j);
                }
                srouteStringList.set(i,srouteListString.concat(stationListString));
            }

            //final ArrayAdapter<ArrayList<String>> adapter = new ArrayAdapter<ArrayList<String>>(this, android.R.layout.simple_list_item_1,srouteList);
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,srouteStringList);
            //리스트뷰의 어댑터를 지정
            listView.setAdapter(adapter);

            //리스트뷰의 아이템 클릭 시 문자열 가져옴
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = (String)parent.getItemAtPosition(position);
                    selectedItemView.setText((position+1) + "번째 경로를 선택하셨습니다.");

                    Intent resultIntent = new Intent(getApplicationContext(), UserSelectedRouteActivity.class);
                    //selectedItem을 putExtra
                    resultIntent.putExtra("selectedRoute",selectedItem);
                    resultIntent.putExtra("startAddress", startAddress);
                    resultIntent.putExtra("endAddress", endAddress);
                    startActivity(resultIntent);
                    ;
                }
            });
        }

    }
}
