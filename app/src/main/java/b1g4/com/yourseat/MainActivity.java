package b1g4.com.yourseat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.google.gson.Gson;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import net.daum.mf.map.api.*;

//import b1g4.com.yourseat.app.App;

public class MainActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener {

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

    private MapView mapView;
    private CurrentLocationXY currentLocationXY = CurrentLocationXY.getInstance();

    public final int MY_PERMISSIONS=4;
    Set<String> setPermissions=new ConcurrentSkipListSet<>();

    // 검색기록 저장을 위해 추가한 것들
    private ArrayList<String> searchedRouteList;
    private ListView searchedRouteListView;
    private SavedRouteListViewAdapter searchedRouteLVAdapter;

    //즐겨찾기를 위해 추가
    private ArrayList<String> starList;
    private String starListString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //버전 확인
        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.O) return;

        //권한 확인 Context.checkSelfPermission
        if(m_checkSelfPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Log.d("권한","m_checkSelfPermission가 참 permission is granted");
        }else{
            Log.d("권한","m_checkSelfPermission가 거짓 permission is not granted");
            if(m_shouldShowRequestPermissionRationale()){
                Log.d("권한", "permission is not granted, hence showing rationle");
                //필요한 이유를 설명하는것
                //Log.d("권한", "권한" + p + "가 필요함");
                m_requestPermissions();
            }else{
                Log.d("권한","permission being requested for first time");
                m_requestPermissions();
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        printHashKey(); // 해시키 확인

        mapView = new MapView(this);
        //ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        //mapViewContainer.addView(mapView);
        mapView.setCurrentLocationEventListener(this);

        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);


        startEditText = findViewById(R.id.startLocation);
        endEditText = findViewById(R.id.endLocation);

        searchedRouteArrayList = new ArrayList<ArrayList<String>>();
        starList = new ArrayList<>();

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

        //즐겨찾기 관련 버튼 설정
        Button getStarListBtn = (Button)findViewById(R.id.getStarList);
        Button addStarListBtn = (Button)findViewById(R.id.addStarList);
        addStarListBtn.setOnClickListener(mClickListener);
        getStarListBtn.setOnClickListener(mClickListener2);

//        //즐겨찾기 관련
//        Intent starIntent = getIntent();
//        String[] savedAddress = starIntent.getStringArrayExtra("savedPoints");
//        Log.d("ADDRESS", "onCreate: 주소를 받았다." + savedAddress[0] + " , " + savedAddress[1]);
//        if(savedAddress != null) {
//            startAddress = savedAddress[0];
//            endAddress = savedAddress[1];
//            startEditText.setText(startAddress);
//            endEditText.setText(endAddress);
//        }

//        SavedSharedPreference.deleteAll(getBaseContext());
        searchedRouteListView = findViewById(R.id.searchRecordLV);


//        //busInfo에 파일들을 읽어서 정보를 저장하는 코드를 실행해야 함
//        String ext= Environment.getExternalStorageState();
//        String sdPath;
//        if(ext.equals(Environment.MEDIA_MOUNTED)){
//            sdPath=Environment.getExternalStorageDirectory().getAbsolutePath();
//        }else{
//            sdPath=Environment.MEDIA_UNMOUNTED;
//        }
//        File myFolder=new File(sdPath.concat("/YourSeat"));
//        Log.d("fileeee",myFolder.getAbsolutePath());
//
//        if(myFolder.exists() && myFolder.isDirectory()){
//
//            String routeDir=myFolder.getAbsolutePath()+"/routecsv.csv";
//            String stationDir=myFolder.getAbsolutePath()+"/stationcsv.csv";
//            String congestionDir=myFolder.getAbsolutePath()+"/congestioncsv.csv";
//            File routeFIle=new File(routeDir);
//            File stationFile=new File(stationDir);
//            File congestionFile=new File(congestionDir);
//            if(routeFIle.exists() && stationFile.exists() && congestionFile.exists()){
//                App app_readFiles=new App();
//                if(app_readFiles.saveBusInfo_for_android(routeDir,stationDir,congestionDir)){
//                    Log.d("read file","success save BusInfo");
//
//                    Toast.makeText(getApplicationContext(), "전처리 완료", Toast.LENGTH_SHORT).show();
//
//                }else{
//                    Log.d("read file","fail save BusInfo");
//                    //여기서 강제 종료를 할것인가?
//                }
//            }else{
//                Log.d("read file","파일이 없습니다");
//            }
//        }
    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

                   String addStarListString = starListString;
                   starList.add(addStarListString);
                   SavedSharedPreference.setStarListAddress(getApplicationContext(),starList);
                    Log.d("STAR", "onClick: 추가버튼 눌림");
                    Toast.makeText(getApplicationContext(), "[ "+ addStarListString +" ]\n 항목이 즐겨찾기에 추가되었습니다.", Toast.LENGTH_LONG).show();
            }
    };


    Button.OnClickListener mClickListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GetStarListActivity.class);
                starList = SavedSharedPreference.getStarListAddress(getBaseContext());
                intent.putExtra("starList", starList);
                startActivityForResult(intent,1111);
                Log.d("STAR", "onClick: 목록 눌림");
        }

    };

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

                    // 검색 기록 저장
                    String start = startEditText.getText().toString();
                    String end = endEditText.getText().toString();
                    String route = start + " → " + end;
                    //starListString = startAddress + " → " + endAddress;

                    Log.d("SavedSharedPreference", route);
                    Log.d("SavedSharedPreference", searchedRouteList.toString());
                    if (!searchedRouteList.contains(route)) {
                        searchedRouteList.add(0, route);
                        // 최근 10개까지만 저장
                        if (searchedRouteList.size() > 10) {
                            searchedRouteList.subList(10,searchedRouteList.size()).clear();
                        }
                        SavedSharedPreference.setAddressList(getBaseContext(), searchedRouteList);
                }


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

                    Intent intent;
                    intent = new Intent(getApplicationContext(), GetSearchedRouteActivity.class);
                    intent.putExtra("startAddress", startAddress);
                    intent.putExtra("endAddress", endAddress);

                    starListString = startAddress + " → " + endAddress;
                    // 출발지 - 도착지간 직선거리가 700m 이하이면 길찾기 수행X
                    CoordinatesDistance coordinatesDistance = new CoordinatesDistance();
                    if(coordinatesDistance.isTooShort(startX, startY, endX, endY)) {
                        intent.putExtra("isSearched", "false");
                    } else {
                        // 경로 탐색 파트로 출발/도착지 x,y 좌표 넘겨주기 -> 결과리스트 searchedRouteArrayList에 받도록.
                        Log.d("XYdata", "startX: " + startX + "startY: " + startY + "endX" + endX + "endY" + endY);
                        intent.putExtra("isSearched", "true");
                        intent.putExtra("sRouteList", searchedRouteArrayList);
                    }
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
        //super.onActivityResult(requestCode,resultCode,resultIntent);
        if(requestCode == 1111){
            if(resultCode == 1234){
                String[] savedAddress = resultIntent.getStringArrayExtra("savedPoints");
                Log.d("ADDRESS", "onCreate: 주소를 받았다." + savedAddress[0] + " , " + savedAddress[1]);
                if(savedAddress != null) {
                    startAddress = savedAddress[0];
                    endAddress = savedAddress[1];
                    startEditText.setText(startAddress);
                    endEditText.setText(endAddress);
                }
                Log.d("TAG", "onActivityResult: 즐겨찾기 목록 해당 주소 전송 완료");
            }
        }
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


    // 지도
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {
        MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();
        Log.i("CurrentLocationUpdate", String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));
        currentLocationXY.setX(Double.toString(mapPointGeo.longitude));
        currentLocationXY.setY(Double.toString(mapPointGeo.latitude));
        Log.i("Check", currentLocationXY.getX());
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        searchedRouteList = new ArrayList<String>();
        searchedRouteList = SavedSharedPreference.getAddressList(getBaseContext());

        searchedRouteLVAdapter = new SavedRouteListViewAdapter(searchedRouteList);
        searchedRouteListView.setAdapter(searchedRouteLVAdapter);

        searchedRouteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String route = searchedRouteList.get(position);
                String[] points = route.split(" → ");
                Log.d("SavedSharedPreference", points.toString());

                startAddress = points[0];
                endAddress = points[1];

                startEditText.setText(startAddress);
                endEditText.setText(endAddress);

                startAddresses = getAddrData(startAddress);
                endAddresses = getAddrData(endAddress);
            }
        });
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
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        mapView.setShowCurrentLocationMarker(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }



    //==============
    //
    //권한관련 함수들
    //
    //=============
    private Boolean m_checkSelfPermission(String...ps){
        for(String p:ps) setPermissions.add(p);

        for(String p:setPermissions){
            if(PackageManager.PERMISSION_GRANTED==ContextCompat.checkSelfPermission(this,p)){
                Log.d("권한","권한="+p+"가 이미 획득된 상태라서 지울거임");
                setPermissions.remove(p);
            }
        }
        if(setPermissions.size()== 0) return true;
        else return false;
    }
    private Boolean m_shouldShowRequestPermissionRationale(){
        Log.d("권한", "m_shouldShowRequestPermissionRationale() 여기로 들어옴");
        Boolean status=true;
        for(String p:setPermissions){
            if(!ActivityCompat.shouldShowRequestPermissionRationale(this,p)){
                status=false;
            }
        }
        return status;
    }
    private void m_requestPermissions(){
        String[] permissionArr;
        int size=setPermissions.size();
        permissionArr=new String[size];
        int i=0;
        for(String pp:setPermissions){
            permissionArr[i]=pp;
            i++;
        }
        ActivityCompat.requestPermissions(this,permissionArr,MY_PERMISSIONS);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        Log.d("권한","onRequestPermissionResult 실행");
        switch (requestCode) {
            case  MY_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                Log.d("권한","onRequestPermissionsResult :MY_PERMISSIONS들어옴");
                Boolean status=true;
                if(grantResults.length <= 0) status=false;
                for(int i=0;i<grantResults.length;i++){
                    if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                        status=false;
                        break;
                    }
                }

                if(status) {
                    Log.d("권한", "onRequestPermissionsResult : 승인");
                }else{
                    Log.d("권한","onRequestPermissionsResult :거부");

                    //앱종료 코드 넣어야 함
                    finishAffinity();
                    System.runFinalization();
                    System.exit(0);

                    //앱 권한 설정 페이지로 연결
                    //Context context = this;
                    //Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    //        .setData(Uri.parse("package:"+context.getPackageName()));
                   // startActivityForResult(intent,0);
                }
                return;
            }
        }
    }
}
