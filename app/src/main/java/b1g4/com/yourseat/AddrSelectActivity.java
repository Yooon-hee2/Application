package b1g4.com.yourseat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AddrSelectActivity extends AppCompatActivity {

    private TextView selectedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addrlist);

        Intent intent = getIntent();
        String input = intent.getExtras().getString("input");
        Object addresses = (Object) intent.getSerializableExtra("addrList");

        ArrayList<String> addressList = new ArrayList<>();

        ListView listView = (ListView)findViewById(R.id.addrList);
        selectedTextView = (TextView)findViewById(R.id.selectedAddr);
        selectedTextView.setText(input); // 검색한 주소명으로 상단 selectedTextView 기본값 설정

        if(addresses instanceof SearchByAddressData) {
            if (((SearchByAddressData) addresses).documents.size() == 0) {
                addressList.add("검색결과가 없습니다.");
            } else {
                for (int i = 0; i < ((SearchByAddressData) addresses).documents.size(); i++) {
                    addressList.add(((SearchByAddressData) addresses).documents.get(i).address_name);
                }
            }
        } else if(addresses instanceof SearchByKeywordData) {
            if (((SearchByKeywordData) addresses).documents.size() == 0) {
                addressList.add("검색결과가 없습니다.");
            } else {
                for (int i = 0; i < ((SearchByKeywordData) addresses).documents.size(); i++) {
                    addressList.add(((SearchByKeywordData) addresses).documents.get(i).place_name);
                }
            }
        }

        // 리스트뷰와 addressList를 연결하는 어댑터
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, addressList);

        // 리스트뷰의 어댑터 지정
        listView.setAdapter(adapter);

        // 리스트뷰의 아이템을 클릭하면 해당 아이템의 문자열을 가져옴
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 클릭한 아이템 문자열 가져옴
                String selectedItem = (String) parent.getItemAtPosition(position);
                // 텍스트뷰에 출력
                selectedTextView.setText(selectedItem);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedAddr", selectedItem);
                setResult(Code.resultCode, resultIntent);
                finish();
            }
        });
    }
}
