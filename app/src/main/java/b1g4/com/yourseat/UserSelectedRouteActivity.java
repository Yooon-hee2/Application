package b1g4.com.yourseat;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class UserSelectedRouteActivity extends AppCompatActivity {

    private TextView startPoint;
    private TextView endPoint;
    private TextView finalRoute;
    private String startAddress;
    private String endAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selected_view);

        //FragmentManager을 통해 fragment를 관리
        FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentTransaction을 통해 fragment 관련 작업을 수행
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contents,new NewRecomRoute());
        //commit을 꼭 해야 반영이 된다.
        fragmentTransaction.commit();

        //fragment 갱신하고자 할 때 아래 메소드 쓰면 된다!!!
        /*private void refresh(){
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.datach(this).attach(this).commit();
        }*/


        //설희 화면에서 얻어온 출발지 주소와 도착지 주소 startPoint와 endPoint에 넣기!!!!!!!
        startPoint = (TextView)findViewById(R.id.startPoint);
        endPoint = (TextView)findViewById(R.id.endPoint);
        finalRoute = (TextView)findViewById(R.id.finalRoute);

        Intent intent = getIntent();
        String selectedRoute = (String) intent.getStringExtra("selectedRoute");
        startAddress = intent.getStringExtra("startAddress");
        endAddress = intent.getStringExtra("endAddress");

        //사용자가 선택한 경로찾기를 받아와서 finalRoute에 넣기.
        startPoint.setText(startAddress);
        endPoint.setText(endAddress);
        finalRoute.setText(selectedRoute.toString());



    }


}
