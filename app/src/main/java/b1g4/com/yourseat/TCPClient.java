package b1g4.com.yourseat;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;


//public class TCPClient extends AsyncTask<String, String, ArrayList<ArrayList<String>>> {
  public class TCPClient extends AsyncTask<String, String, ResultOfServer> {


    public boolean status=false;
    public ArrayList<ArrayList<String>> returnMsg = new ArrayList<ArrayList<String>>();
    public ArrayList<ArrayList<String>> returnMsgWithStop = new ArrayList<ArrayList<String>>();
    public ResultOfServer resultOfServer = new ResultOfServer(); //yoonhee
    //public String testString="";
    private Socket socket;
    ObjectOutputStream outputStream;
    ObjectInputStream inputStream;
    private String host = "xxx.xxx.xxx.xxx"; // IP
    private int port = 8000; // PORT번호
    int timeout = 3000;


    // 백그라운드 작업 시작전 호출
    // 준비작업 구현, ex) 네트워크 준비, 객체의 new..
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    // 본격적인 작업
    // 도중에 publishProgress()호출하면 onProgressUpdate() 실행됨 -> 사용자에게 진행을 알릴 때 사용
    @Override
    //protected ArrayList<ArrayList<String>> doInBackground(String... String) {
    protected ResultOfServer doInBackground(String... String) {
        String startX = String[0];
        String startY=String[1];
        String endX=String[2];
        String endY=String[3];

        try{
            socket=new Socket();
            socket.setSoTimeout(timeout);//read 메서드가 connection_timeout 시간동안 응답을 기다린다.
            socket.setSoLinger(true, timeout);//서버와의 정상 종료를 위해서 connection_timeout 시간동안 close 호출 후 기다린다.
            socket.connect(new InetSocketAddress(host,port), timeout);

            //서버로 보내는 부분
            PrintWriter writer=new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.println(startX+" "+startY+" "+endX+" "+endY);
            writer.flush();
            Log.d("tcp","서버로 보냄!");

            //클라이언트에서 받는 부분
            BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
            String clientMsg = reader.readLine();
            Log.d("tcp","받은 데이터 !"+clientMsg);
            //convertMsg("1 11 119000097 동작구청.노량진초등학교앞 641 119000067 상도시장 119900108 상도역 동작21 119900255 중앙대중문 19 8 119000097 119000099 119000059 119000061 119900108 119900013 119900105 119900255");
            convertMsg(clientMsg);

            status=true;

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socket=null;
            }
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //return  this.returnMsg;
        return this.resultOfServer;
    }

    private void convertMsg(String clientMsg){

        String[] arrayOfClientMsg = clientMsg.split("\\s");
        int routeNum = Integer.parseInt(arrayOfClientMsg[0]);

        if(routeNum != 0) {
            for (int i = 1; i < arrayOfClientMsg.length;) {

                int detailedRouteNum = Integer.parseInt(arrayOfClientMsg[i]);
                int stationNum = Integer.parseInt(arrayOfClientMsg[i + detailedRouteNum + 1]);

                ArrayList<String> tempReturnMsg = new ArrayList<String>();

                for (int j = i + 1; j <= i + detailedRouteNum; j++) {
                    tempReturnMsg.add(arrayOfClientMsg[j]);
                }
                returnMsg.add(tempReturnMsg);
                resultOfServer.searchedRouteArrayList = returnMsg;

                ArrayList<String> tempReturnMsgWithStop = new ArrayList<String>();

                for (int j = i + detailedRouteNum + 2; j <= i + detailedRouteNum + stationNum; j++) {
                    tempReturnMsgWithStop.add(arrayOfClientMsg[j]);
                }
                returnMsgWithStop.add(tempReturnMsgWithStop);
                resultOfServer.searchedRouteArrayListByStop = returnMsgWithStop;

                i = i + detailedRouteNum + stationNum + 2;
            }
        }
        else{
            Log.d("data","경로가 존재하지 않습니다!");
        }

    }

    @Override
    protected void onProgressUpdate(String... params) {

    }

    @Override
    protected void onPostExecute(ResultOfServer rs) {
        super.onPostExecute(rs);
    }
}
