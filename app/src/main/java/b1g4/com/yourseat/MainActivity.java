package b1g4.com.yourseat;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.google.gson.Gson;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import net.daum.mf.map.api.*;

public class MainActivity extends AppCompatActivity {

    private List<String> list;          // 데이터를 넣은 리스트변수
    private ListView listView;          // 검색을 보여줄 리스트변수
    private EditText addrSearch;        // 검색어를 입력할 Input 창
    private AddressListViewAdapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<String> addressList;
    private EditText startEditText;
    private EditText endEditText;

    private PointFromAddressData startAddresses;
    private PointFromAddressData endAddresses;
    private String startAddress = null;
    private String endAddress = null;
    private NotificationManager notificationManager;
    private Notification.Builder builder;

    private ArrayList<ArrayList<String>> searchedRouteArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // printHashKey(); // 해시키 확인

        MapView mapView = new MapView(this);
        //ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        //mapViewContainer.addView(mapView);

        startEditText = findViewById(R.id.startLocation);
        endEditText = findViewById(R.id.endLocation);

        searchedRouteArrayList = new ArrayList<ArrayList<String>>();

        /*ArrayList<ArrayList<String>> apiRouteLists = null;
        for(int i=0; i< apiRouteLists.size(); i++) {
            ArrayList<String> tmp = new ArrayList<String>(apiRouteLists.get(i));
            searchedRouteArrayList.add(tmp);
        }*/

        //테스트용 인풋 생성
        final ArrayList<String> sample = new ArrayList<>();
        sample.add("100000384");
        sample.add("중앙대정문");
        sample.add("동작01");
        sample.add("100000165");
        sample.add("달마사");
        sample.add("111111111");
        sample.add("달마사");
        sample.add("동작21");
        sample.add("100000165");
        sample.add("중앙대중문");
        sample.add("23");
        searchedRouteArrayList.add(sample);

        // 버튼 설정
        BtnOnClickListener onClickListener = new BtnOnClickListener() ;
        Button startSearchBtn = (Button)findViewById(R.id.startSearchBtn);
        Button endSearchBtn = (Button)findViewById(R.id.endSearchBtn);
        Button searchPathBtn = (Button)findViewById(R.id.searchPathBtn);
        startSearchBtn.setOnClickListener(onClickListener);
        endSearchBtn.setOnClickListener(onClickListener);
        searchPathBtn.setOnClickListener(onClickListener);

    }


    // 주소를 입력하고 검색 버튼을 눌렀을 때 실행되는 파트
    class BtnOnClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            // 길찾기 버튼 클릭 시
            if(v.getId() == R.id.searchPathBtn) {
                // 출발지 / 도착지 주소 입력이 미완성일 경우 토스트 출력
                if(startAddress == null || endAddress == null) {
                    Toast.makeText(getApplicationContext(), "출발지와 도착지 입력을 완료해주세요.", Toast.LENGTH_SHORT);
                } else {
                    // 출발/도착지의 x,y 좌표 받아오기
                    String startX = null;
                    String startY = null;
                    String endX = null;
                    String endY = null;
                    for(int i=0; i<startAddresses.documents.size(); i++) {
                        if(startAddress.equals(startAddresses.documents.get(i).address_name)) {
                            startX = startAddresses.documents.get(i).x;
                            startY = startAddresses.documents.get(i).y;
                        }
                    }
                    for(int i=0; i<endAddresses.documents.size(); i++) {
                        if(endAddress.equals(endAddresses.documents.get(i).address_name)){
                            endX = endAddresses.documents.get(i).x;
                            endY = endAddresses.documents.get(i).y;
                        }
                    }
                    // 경로 탐색 파트로 출발/도착지 x,y 좌표 넘겨주기
                    Log.d("XYdata", "startX: " + startX + "startY: " + startY + "endX" + endX + "endY" + endY);

                    Intent intent;
                    intent = new Intent(getApplicationContext(), GetSearchedRouteActivity.class);
                    //GetSearchedRoute로 intent로 ArrayList<String>형태의 한 경로를 GetSearchedRoute로 putExtra
                    intent.putExtra("startAddress", startAddress);
                    intent.putExtra("endAddress", endAddress);
                    intent.putExtra("sRouteList", searchedRouteArrayList);
                    startActivity(intent);

                }
            }
            // 출발/도착지 주소명 검색 버튼 클릭 시
            else {
               String location = null;
                PointFromAddressData addrData = null;
                switch(v.getId()) {
                    case R.id.startSearchBtn:
                        location = startEditText.getText().toString();
                        startAddresses = getAddrData(location);
                        Intent intentS = new Intent(getApplicationContext(), AddrSelectActivity.class);
                        intentS.putExtra("addrList", startAddresses);
                        intentS.putExtra("input", location);
                        startActivityForResult(intentS, Code.requestCodeStart);//액티비티 띄우기
                        break;
                    case R.id.endSearchBtn:
                        location = endEditText.getText().toString();
                        endAddresses = getAddrData(location);
                        Intent intentE = new Intent(getApplicationContext(), AddrSelectActivity.class);
                        intentE.putExtra("addrList", endAddresses);
                        intentE.putExtra("input", location);
                        startActivityForResult(intentE, Code.requestCodeEnd);//액티비티 띄우기
                        break;
                }

            }
        }
    }

    /*
    * 다음 액티비티가 종료되었을 때 실행됨.
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        if(resultCode == Code.resultCode) {
            String selectedResult = resultIntent.getStringExtra("selectedAddr");
            if(requestCode == Code.requestCodeStart) { // 출발지 주소명 검색으로부터 넘어간 액티비티였을 경우 반환값으로 출발지 EditText 값 설정
                startAddress = selectedResult;
                startEditText.setText(selectedResult);
            } else if(requestCode == Code.requestCodeEnd) { // 도착지 주소명 검색으로부터 넘어간 액티비티였을 경우 반환값으로 도착지 EditText 값 설정
                endAddress = selectedResult;
                endEditText.setText(selectedResult);
            }
        }
    }

    // 인풋으로 받은 주소명으로 GetPointFromAddress AsyncTask를 실행해서 REST API를 호출한 후 결과 Address 데이터를 반환해준다.
    public PointFromAddressData getAddrData(String input) {
        try {
            String result = new GetPointFromAddress().execute(input).get();
            Log.d("getPointResult", result);
            Gson gsonResult = new Gson();
            PointFromAddressData pointFromAddressData = gsonResult.fromJson(result, PointFromAddressData.class);
            return pointFromAddressData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    HashKey을 가져와서 Log로 띄운다.
    다음 지도 API에 HashKey값을 저장해야 연동가능
    */
    //또 잠시 주석처리
    public void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("b1g4.com.yourseat", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}
