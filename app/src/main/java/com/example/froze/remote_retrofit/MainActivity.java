package com.example.froze.remote_retrofit;

import android.inputmethodservice.Keyboard;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class MainActivity extends AppCompatActivity {

    /* Open Api key : 685554457566726f3736674a746c45
                    : sample
       사용 Api : http://openapi.seoul.go.kr:8088/685554457566726f3736674a746c45/json/PublicParkingAvaliable/1/5/
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String key = "685554457566726f3736674a746c45";
        String serviceName = "PublicParkingAvaliable";
        int begin = 1;
        int end = 5;

        String url = "http://openapi.seoul.go.kr:8088/"+key+"/json/"+serviceName+"/"+begin+"/"+end+"/";

        Log.e("Retrofit URL",url);
        //1. Retrofit client 생성
        Retrofit client = new Retrofit.Builder().baseUrl("http://openapi.seoul.go.kr:8088") // 베이스 도메인 지정
                .addConverterFactory(GsonConverterFactory.create()) // json 컨버팅 라이브러리 지정
                .build();
        // 2. Retrofit client 에서 사용할 interface 지정
        ISeoulOpenData service = client.create(ISeoulOpenData.class);
        // 3. Interface(서비스)를 통해서 데이터를 호출한다
        Call<RemoteData> remoteData = service.getData(key,serviceName,begin,end);
        // 4. 비동기 데이터를 받기위한 리스너 세팅
        remoteData.enqueue(new Callback<RemoteData>() {
            @Override
            public void onResponse(Call<RemoteData> call, Response<RemoteData> response) {
                if(response.isSuccessful()){
                    RemoteData data = response.body();
                    for(Row row : data.getPublicParkingAvaliable().getRow()){
                        Log.i("Remote Data Result", "Park Name = "+row.getPARK_NAME());
                    }
                }else{
                    Log.e("RemoteData", response.message());
                }
            }

            @Override
            public void onFailure(Call<RemoteData> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}

interface ISeoulOpenData {
    @GET("/{key}/json/{serviceName}/{begin}/{end}/")
    Call<RemoteData> getData(@Path("key")String key, @Path("serviceName")String serviceName, @Path("begin")int begin, @Path("end")int end);
}

class RemoteData{
    public PublicParkingAvaliable getPublicParkingAvaliable() {
        return PublicParkingAvaliable;
    }
    PublicParkingAvaliable PublicParkingAvaliable;
}

class PublicParkingAvaliable{

    String list_total_count;
    Result RESULT;
    List<Row> row;

    public String getList_total_count() {
        return list_total_count;
    }

    public Result getRESULT() {
        return RESULT;
    }

    public List<Row> getRow() {
        return row;
    }
}
class Result{
    String CODE;
    String MESSAGE;

    public String getCODE() {
        return CODE;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }
}
class Row {
    String PARKMASTER_CD;
    String PARK_NAME;
    String MAX_PARKING_CNT;
    String PARKING_CNT;
    String PARK_ADDRESS;
    String TEL_NO;
    String OWNER_NAME;
    String COMPANY_NM;

    public String getPARKMASTER_CD() {
        return PARKMASTER_CD;
    }

    public String getPARK_NAME() {
        return PARK_NAME;
    }

    public String getMAX_PARKING_CNT() {
        return MAX_PARKING_CNT;
    }

    public String getPARKING_CNT() {
        return PARKING_CNT;
    }

    public String getPARK_ADDRESS() {
        return PARK_ADDRESS;
    }

    public String getTEL_NO() {
        return TEL_NO;
    }

    public String getOWNER_NAME() {
        return OWNER_NAME;
    }

    public String getCOMPANY_NM() {
        return COMPANY_NM;
    }

}