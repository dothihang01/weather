package com.example.thoitiet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.thoitiet.Model.DataResponse;
import com.example.thoitiet.Model.LWeather;
import com.example.thoitiet.Model.Weather;
import com.example.thoitiet.api.ApiService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    TextView tvNhietDo, tvViTri, tvCamGiac, tvThoiGian, tvChiTiet, tvDoAm, tvMay, tvGio, tvMatTroiMoc, tvMatTroiLan;
    RecyclerView lvThoiTietGio;
    List<LWeather> listGio = new ArrayList<>();
    ThoiTietGioApdater thoiTietGioAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        maps();
        getWeather();
    }

    private void maps() {
        tvNhietDo = (TextView) findViewById(R.id.tvNhietDo);
        tvViTri = (TextView) findViewById(R.id.tvViTri);
        tvCamGiac = (TextView) findViewById(R.id.tvCamGiac);
        tvThoiGian = (TextView) findViewById(R.id.tvThoiGian);
        lvThoiTietGio = (RecyclerView) findViewById(R.id.lvThoiTietGio);
        tvChiTiet = (TextView) findViewById(R.id.tvChiTiet);
        tvDoAm = (TextView) findViewById(R.id.tvDoAm);
        tvMay = (TextView) findViewById(R.id.tvMay);
        tvGio = (TextView) findViewById(R.id.tvGio);
        tvMatTroiMoc = (TextView) findViewById(R.id.tvMatTroiMoc);
        tvMatTroiLan = (TextView) findViewById(R.id.tvMatTroiLan);

    }

    private void getWeather() {
        ApiService.apiService.getWeather("hanoi", "956a223fbccb7f85cf307e66442e7f8f", "vi", "metric", "12").enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                DataResponse dataResponse = response.body();

                LWeather weather = dataResponse.getList().get(0);
                String nhietDo = weather.getMain().getTemp();
                String viTri = dataResponse.getCity().getName();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE HH:mm");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
                Date a = new Date(Long.valueOf(weather.getDt()) * 1000L);
                String thoiGian = simpleDateFormat.format(a);
                String camGiac = weather.getMain().getFeels_like();
                String doAm = weather.getMain().getHumidity();
                int may = weather.getClouds().getAll();
                String gio = weather.getWind().getSpeed();
                Long matTroiMoc = Long.valueOf(dataResponse.getCity().getSunrise());
                Long matTroiLan = Long.valueOf(dataResponse.getCity().getSunset());
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
                simpleDateFormat2.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
                Date sunrise = new Date(matTroiMoc * 1000L);
                Date sunset = new Date(matTroiLan * 1000L);


                String MatTroiMoc = simpleDateFormat2.format(sunrise);
                String MatTroiLan = simpleDateFormat2.format(sunset);

                String May;
                if(may <= 25) {
                    May = "??t";
                } else if(may <= 50) {
                    May = "R???i r??c";
                } else if (may <= 84) {
                    May = "Nhi???u";
                } else {
                    May = "U ??m";
                }

                listGio = dataResponse.getList();
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false);
                lvThoiTietGio.setLayoutManager(linearLayoutManager);
                lvThoiTietGio.setHasFixedSize(true);
                lvThoiTietGio.setAdapter(new ThoiTietGioApdater(listGio));
                tvNhietDo.setText(""+Math.round(Double.valueOf(nhietDo))+ "??C");
                tvViTri.setText(viTri);
                tvThoiGian.setText(thoiGian);
                tvCamGiac.setText("C???m gi??c nh?? " + Math.round(Double.valueOf(camGiac)) + "??C");
                tvChiTiet.setText(weather.getWeather().get(0).getDescription());
                tvDoAm.setText("????? ???m\n" + doAm + "%");
                tvMay.setText("M??y\n" + May);
                tvGio.setText("Gi??\n" + gio+" m/s");
                tvMatTroiMoc.setText("M???t tr???i m???c\n" + MatTroiMoc);
                tvMatTroiLan.setText("M???t tr???i l???n\n" + MatTroiLan);
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Log.d("test", call.toString());
            }
        });
    }
}