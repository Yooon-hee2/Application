package b1g4.com.yourseat;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/*
 * AsyncTask<doInBackground()의 변수 종류, onProgressUpdate()에서 사용할 변수 종류, onPostExecute()에서 사용할 변수종류>
 * doInBackground()의 변수 종류: 해당 AsyncTask execute할 때 전달할 값 종류
 * onProgressUpdate()에서 사용할 변수 종류: 진행상황을 알릴 때 전달할 값 종류
 * onPostExecute()에서 사용할 변수종류: AsyncTask가 끝난 뒤 결과값의 종류
 * */
public class GetPointFromAddress extends AsyncTask<String, String, String> {

    String auth = "7d6284ad9ad1da5fbe0177af6945e99e";
    String result = null;

    // 백그라운드 작업 시작전 호출
    // 준비작업 구현, ex) 네트워크 준비, 객체의 new..
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    // 본격적인 작업
    // 도중에 publishProgress()호출하면 onProgressUpdate() 실행됨 -> 사용자에게 진행을 알릴 때 사용
    @Override
    protected String doInBackground(String... String) {
        String address = String[0];

        try {
            String addr = URLEncoder.encode(address, "UTF-8");
            Log.d("addrEncode", addr);
            String apiURL = "https://dapi.kakao.com/v2/local/search/address.json?query=" + addr;
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("charset", "utf-8");
            con.setRequestProperty("X-Requested-With", "curl");
            con.setRequestProperty("Authorization", "KakaoAK " + auth);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            } else {  // 에러 발생
                Log.d("ResponseCodeError", Integer.toString(responseCode));
                br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            result = response.toString();

        } catch (Exception e) {
        }

        return result;
    }

    @Override
    protected void onProgressUpdate(String... params) {

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

}
