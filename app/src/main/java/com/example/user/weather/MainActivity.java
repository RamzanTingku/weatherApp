package com.example.user.weather;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.weather.data.CityDml;


import com.example.user.weather.google_pid_model.RetrofitPid;
import com.example.user.weather.service_interface.DayStatus;
import com.example.user.weather.yahoo_lonlog.Example;
import com.example.user.weather.yahoo_lonlog.LatLog_interface;
import com.example.user.weather.yahoo_weather.Yweather;
import com.example.user.weather.service_interface.WeatherService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.user.weather.R.id.toolbar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public Toolbar mToolbar;
    private ActionBar mActionBar;
    public TextView locationTitle;

    WeatherService weatherService;
    DayWeatherAdapter dayWeatherAdapter;
    public ArrayList<DayWeather> dayWeathers;
    RecyclerView contactRecyclerView;

    AlertDialog alertDialog1;
    CharSequence[] values = {"Celsius","Fahrenheit"};


    public String  rising,risingNormal,risingDown,risingUp,day0Condition,day1Condition,day2Condition,day3Condition,day4Condition,currentCondition,currentTemp,crntTempUnit,lastUpdate,detailsText, minTemp,maxTemp,avgTemp,humidity,pressure,visivility,uv,wind,windDir, currentConditionText,sunrise,sunset,location,time,feelTmp,day0,day0date,day0high,day0low,
            day1,day1date,day1high,day1low,day2,day2date,day2high,day2low,day3,day3date,day3high,day3low,day4,day4date,day4high,day4low;
    TextView crntTempUnitT,crntTempRangeUnitT,day0TempUnitT,day1TempUnitT,day2TempUnitT,day3TempUnitT,day4TempUnitT, lastUpdateT,detailsTextT,  minTempt,maxTempt,avgTempt,humidityt,pressuret,visivilityt,uvt,windt,windDirt,wConditiontt,sunriset,sunsett,locationt,timet,feelTmpt,day0t,day0datet,day0hight,day0lowt,
            day1t,day1datet,day1hight,day1lowt,day2t,day2datet,day2hight,day2lowt,day3t,day3datet,day3hight,day3lowt,day4t,day4datet,day4hight,day4lowt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(false);
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(false);


        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),
                R.drawable.setting);
        mToolbar.setOverflowIcon(drawable);


        getDayStatus();


        //googleService( "school",23.777175,90.399542,1000);
        googlePid( "v" );
        yahooLatLong();



        //location spinner property

        CityDml cityDml;
        cityDml=new CityDml(this);
        Spinner spinner = (Spinner) findViewById(R.id.spinner_location);
        ArrayList<String> weatherst= cityDml.selectData();
        ArrayAdapter<String> SpinAdapter = new ArrayAdapter<>(this, R.layout.location_spinner_text, weatherst );
        SpinAdapter.setDropDownViewResource(R.layout.location_spinner_dropdown);
        spinner.setAdapter(SpinAdapter);
        spinner.setOnItemSelectedListener(this);


    }



    public void getDayStatus(){

        DayStatus.Factory.getInstance().getWeather().enqueue(new Callback<Yweather>() {
            @Override
            public void onResponse(Call<Yweather> call, Response<Yweather> response) {


                /*detailsText =response.body().getQuery().getResults().getChannel().getAtmosphere().getRising();
                detailsTextT = (TextView) findViewById(R.id.details_text);
                detailsTextT.setText(detailsText);
*/

               /* ImageView testImg = (ImageView) findViewById(R.id.details_img);
                UrlImageViewHelper.setUrlDrawable(testImg,detailsText);*/

               /*
                Picasso.with(context)
                        .load(detailsText)
                        .resize(50,50).into(testImg);*/

                avgTemp=response.body().getQuery().getResults().getChannel().getItem().getCondition().getTemp();
                crntTempUnit = response.body().getQuery().getResults().getChannel().getUnits().getTemperature();
                maxTemp=response.body().getQuery().getResults().getChannel().getItem().getForecast().get(0).getHigh();
                minTemp=response.body().getQuery().getResults().getChannel().getItem().getForecast().get(0).getLow();
                humidity=response.body().getQuery().getResults().getChannel().getAtmosphere().getHumidity();
                pressure=response.body().getQuery().getResults().getChannel().getAtmosphere().getPressure();
                rising = response.body().getQuery().getResults().getChannel().getAtmosphere().getRising();
                wind=response.body().getQuery().getResults().getChannel().getWind().getSpeed();
                windDir=response.body().getQuery().getResults().getChannel().getWind().getDirection();
                visivility=response.body().getQuery().getResults().getChannel().getAtmosphere().getVisibility();
                sunrise=response.body().getQuery().getResults().getChannel().getAstronomy().getSunrise();
                sunset=response.body().getQuery().getResults().getChannel().getAstronomy().getSunset();
                location=response.body().getQuery().getResults().getChannel().getLocation().getCity();
                currentConditionText =response.body().getQuery().getResults().getChannel().getItem().getCondition().getText();
                currentCondition =response.body().getQuery().getResults().getChannel().getItem().getCondition().getCode();
                lastUpdate = response.body().getQuery().getResults().getChannel().getLastBuildDate();

                day0=response.body().getQuery().getResults().getChannel().getItem().getForecast().get(0).getDay();
                day1=response.body().getQuery().getResults().getChannel().getItem().getForecast().get(2).getDay();
                day2=response.body().getQuery().getResults().getChannel().getItem().getForecast().get(3).getDay();
                day3=response.body().getQuery().getResults().getChannel().getItem().getForecast().get(4).getDay();
                day4=response.body().getQuery().getResults().getChannel().getItem().getForecast().get(5).getDay();

                day0date=response.body().getQuery().getResults().getChannel().getItem().getForecast().get(1).getDate().substring(0,6);
                day1date=response.body().getQuery().getResults().getChannel().getItem().getForecast().get(2).getDate().substring(0,6);
                day2date=response.body().getQuery().getResults().getChannel().getItem().getForecast().get(3).getDate().substring(0,6);
                day3date=response.body().getQuery().getResults().getChannel().getItem().getForecast().get(4).getDate().substring(0,6);
                day4date=response.body().getQuery().getResults().getChannel().getItem().getForecast().get(5).getDate().substring(0,6);

                day0high=response.body().getQuery().getResults().getChannel().getItem().getForecast().get(1).getHigh();
                day1high=response.body().getQuery().getResults().getChannel().getItem().getForecast().get(2).getHigh();
                day2high=response.body().getQuery().getResults().getChannel().getItem().getForecast().get(3).getHigh();
                day3high=response.body().getQuery().getResults().getChannel().getItem().getForecast().get(4).getHigh();
                day4high=response.body().getQuery().getResults().getChannel().getItem().getForecast().get(5).getHigh();

                day0low=response.body().getQuery().getResults().getChannel().getItem().getForecast().get(1).getLow();
                day1low=response.body().getQuery().getResults().getChannel().getItem().getForecast().get(2).getLow();
                day2low=response.body().getQuery().getResults().getChannel().getItem().getForecast().get(3).getLow();
                day3low=response.body().getQuery().getResults().getChannel().getItem().getForecast().get(4).getLow();
                day4low=response.body().getQuery().getResults().getChannel().getItem().getForecast().get(5).getLow();

                day0Condition =response.body().getQuery().getResults().getChannel().getItem().getForecast().get(2).getCode() ;
                Toast.makeText(MainActivity.this,response.body().getQuery().getResults().getChannel().getItem().getForecast().get(1).getText() , Toast.LENGTH_SHORT).show();
                day1Condition =response.body().getQuery().getResults().getChannel().getItem().getForecast().get(2).getCode() ;
                day2Condition =response.body().getQuery().getResults().getChannel().getItem().getForecast().get(3).getCode() ;
                day3Condition =response.body().getQuery().getResults().getChannel().getItem().getForecast().get(4).getCode() ;
                day4Condition =response.body().getQuery().getResults().getChannel().getItem().getForecast().get(6).getCode() ;



                avgTempt=(TextView)findViewById(R.id.current_temp);
                avgTempt.setText(FtoC(avgTemp));
                int currentImage = getResources().getIdentifier("icon_" + currentCondition, "drawable", getPackageName());
                ImageView currentImageView = (ImageView) findViewById(R.id.current_image);
                currentImageView.setImageResource(currentImage);

                crntTempUnitT = (TextView) findViewById(R.id.current_temp_unit);
                crntTempUnitT.setText(SFtoC(crntTempUnit));
                maxTempt=(TextView)findViewById(R.id.current_high_temp);
                maxTempt.setText(FtoC(maxTemp));
                minTempt=(TextView)findViewById(R.id.current_low_temp);
                minTempt.setText(FtoC(minTemp));
                crntTempRangeUnitT = (TextView) findViewById(R.id.current_range_temp_unit);
                crntTempRangeUnitT.setText(SFtoC(crntTempUnit));
                humidityt=(TextView)findViewById(R.id.current_humidity);
                humidityt.setText(humidity);
                pressuret=(TextView)findViewById(R.id.pressure);
                pressuret.setText(pressure);

                ImageView risingImageView = (ImageView) findViewById(R.id.pressure_rising);
                if (rising == "2"){
                    risingImageView.setImageResource(R.drawable.down_arrow);
                }else if(rising == "1"){
                    risingImageView.setImageResource(R.drawable.up_arrow);
                }

                visivilityt=(TextView)findViewById(R.id.visibility);
                visivilityt.setText(visivility);
                windt=(TextView)findViewById(R.id.speed);
                windt.setText(wind);
                windDirt=(TextView)findViewById(R.id.wind_direction);
                windDirt.setText(windDir);
                sunriset=(TextView)findViewById(R.id.sunrise);
                sunriset.setText(sunrise);
                sunsett=(TextView)findViewById(R.id.sunset);
                sunsett.setText(sunset);
                //locationt=(TextView)findViewById(R.id.);
                //locationt.setText(location);
                wConditiontt=(TextView)findViewById(R.id.current_condition);
                wConditiontt.setText(currentConditionText);
                lastUpdateT = (TextView) findViewById(R.id.last_update_time);
                lastUpdateT.setText(lastUpdate);

                day0t = (TextView) findViewById(R.id.day_0);
                day0t.setText(day0);
                day0datet = (TextView) findViewById(R.id.day_0_date);
                day0datet.setText(day0date);
                day0hight = (TextView) findViewById(R.id.day_0_high_temp);
                day0hight.setText(FtoC(day0high));
                day0lowt = (TextView) findViewById(R.id.day_0_low_temp);
                day0lowt.setText(FtoC(day0low));
                day0TempUnitT = (TextView) findViewById(R.id.day_0_temp_unit);
                day0TempUnitT.setText(SFtoC(crntTempUnit));
                int day0Img = getResources().getIdentifier("icon_" +day0Condition, "drawable", getPackageName());
                ImageView day0ImgView  = (ImageView) findViewById(R.id.day_0_image);
                day0ImgView.setImageResource(day0Img);

                day1t = (TextView) findViewById(R.id.day_1);
                day1t.setText(day1);
                day1datet = (TextView) findViewById(R.id.day_1_date);
                day1datet.setText(day1date);
                day1hight = (TextView) findViewById(R.id.day_1_high_temp);
                day1hight.setText(FtoC(day1high));
                day1lowt = (TextView) findViewById(R.id.day_1_low_temp);
                day1lowt.setText(FtoC(day1low));
                day1TempUnitT = (TextView) findViewById(R.id.day_1_temp_unit);
                day1TempUnitT.setText(SFtoC(crntTempUnit));
                int day1Img = getResources().getIdentifier("icon_" +day1Condition, "drawable", getPackageName());
                ImageView day1ImgView  = (ImageView) findViewById(R.id.day_1_image);
                day1ImgView.setImageResource(day1Img);

                day2t = (TextView) findViewById(R.id.day_2);
                day2t.setText(day2);
                day2datet = (TextView) findViewById(R.id.day_2_date);
                day2datet.setText(day2date);
                day2hight = (TextView) findViewById(R.id.day_2_high_temp);
                day2hight.setText(FtoC(day2high));
                day2lowt = (TextView) findViewById(R.id.day_2_low_temp);
                day2lowt.setText(FtoC(day2low));
                day2TempUnitT = (TextView) findViewById(R.id.day_2_temp_unit);
                day2TempUnitT.setText(SFtoC(crntTempUnit));
                int day2Img = getResources().getIdentifier("icon_" +day2Condition, "drawable", getPackageName());
                ImageView day2ImgView  = (ImageView) findViewById(R.id.day_2_image);
                day2ImgView.setImageResource(day2Img);

                day3t = (TextView) findViewById(R.id.day_3);
                day3t.setText(day3);
                day3datet = (TextView) findViewById(R.id.day_3_date);
                day3datet.setText(day3date);
                day3hight = (TextView) findViewById(R.id.day_3_high_temp);
                day3hight.setText(FtoC(day3high));
                day3lowt = (TextView) findViewById(R.id.day_3_low_temp);
                day3lowt.setText(FtoC(day3low));
                day3TempUnitT = (TextView) findViewById(R.id.day_3_temp_unit);
                day3TempUnitT.setText(SFtoC(crntTempUnit));
                int day3Img = getResources().getIdentifier("icon_" +day3Condition, "drawable", getPackageName());
                ImageView day3ImgView  = (ImageView) findViewById(R.id.day_3_image);
                day3ImgView.setImageResource(day3Img);

                day4t = (TextView) findViewById(R.id.day_4);
                day4t.setText(day4);
                day4datet = (TextView) findViewById(R.id.day_4_date);
                day4datet.setText(day4date);
                day4hight = (TextView) findViewById(R.id.day_4_high_temp);
                day4hight.setText(FtoC(day4high));
                day4lowt = (TextView) findViewById(R.id.day_4_low_temp);
                day4lowt.setText(FtoC(day4low));
                day4TempUnitT = (TextView) findViewById(R.id.day_4_temp_unit);
                day4TempUnitT.setText(SFtoC(crntTempUnit));
                int day4Img = getResources().getIdentifier("icon_" +day4Condition, "drawable", getPackageName());
                ImageView day4ImgView  = (ImageView) findViewById(R.id.day_4_image);
                day4ImgView.setImageResource(day4Img);



            }

            @Override
            public void onFailure(Call<Yweather> call, Throwable t) {

            }
        });


    }


    public void editLocation(View view) {

        Intent intent = new Intent(MainActivity.this,CityActivity.class);
        startActivity(intent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.temp_unit:
//                showCustomDialog();
                CreateAlertDialogWithRadioButtonGroup() ;

                break;
            case R.id.refresh:
                onRefresh();
                break;

            /*case R.id.speed_unit:
                break;*/

        }
        return super.onOptionsItemSelected(item);
    }

    public String FtoC(String Stemp) {
        int temp = 0;
        temp = Integer.parseInt(Stemp);
        return String.valueOf(Math.round((temp - 32) /1.8000));
    }

    public String SFtoC(String Stemp) {
        return "C";
    }

    public String SCtoF(String Stemp) {
        return "F";
    }


    public void googlePid( String place_id ){

        String url = "https://maps.googleapis.com/maps/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitPid service = retrofit.create(RetrofitPid.class);

        Call<com.example.user.weather.google_pid_model.Example> call = service.getLocation("ChIJ23cSxsHAVTcRWS4OcziI0cc");

       call.enqueue(new Callback<com.example.user.weather.google_pid_model.Example>() {
           @Override
           public void onResponse(Call<com.example.user.weather.google_pid_model.Example> call, Response<com.example.user.weather.google_pid_model.Example> response) {
              // Toast.makeText(getApplication(),response.body().getResult().getGeometry().getLocation().getLat().toString(),Toast.LENGTH_LONG).show();
           }

           @Override
           public void onFailure(Call<com.example.user.weather.google_pid_model.Example> call, Throwable t) {

           }
       });




    }

    public void yahooLatLong(){

        LatLog_interface.Factory.getInstance().getWeather().enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                Toast.makeText(getApplication(),response.body().getQuery().getResults().getChannel().getLocation().getCity(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
             Toast.makeText(getApplication(),"Errr",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void CreateAlertDialogWithRadioButtonGroup(){

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Select Your Choice");

        builder.setSingleChoiceItems(values,-1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    case 0:

                        avgTempt.setText(FtoC(avgTemp));
                        crntTempUnitT.setText(SFtoC(crntTempUnit));
                        maxTempt.setText(FtoC(maxTemp));
                        minTempt.setText(FtoC(minTemp));
                        crntTempRangeUnitT.setText(SFtoC(crntTempUnit));
                        day0hight.setText(FtoC(day0high));
                        day0lowt.setText(FtoC(day0low));
                        day0TempUnitT.setText(SFtoC(crntTempUnit));
                        day1hight.setText(FtoC(day1high));
                        day1lowt.setText(FtoC(day1low));
                        day1TempUnitT.setText(SFtoC(crntTempUnit));
                        day2hight.setText(FtoC(day2high));
                        day2lowt.setText(FtoC(day2low));
                        day2TempUnitT.setText(SFtoC(crntTempUnit));
                        day3hight.setText(FtoC(day3high));
                        day3lowt.setText(FtoC(day3low));
                        day3TempUnitT.setText(SFtoC(crntTempUnit));
                        day4hight.setText(FtoC(day4high));
                        day4lowt.setText(FtoC(day4low));
                        day4TempUnitT.setText(SFtoC(crntTempUnit));



                        break;

                    case 1:
                        avgTempt.setText(avgTemp);
                        crntTempUnitT.setText(crntTempUnit);
                        maxTempt.setText(maxTemp);
                        minTempt.setText(minTemp);
                        crntTempRangeUnitT.setText(crntTempUnit);
                        day0hight.setText(day0high);
                        day0lowt.setText(day0low);
                        day0TempUnitT.setText(crntTempUnit);
                        day1hight.setText(day1high);
                        day1lowt.setText(day1low);
                        day1TempUnitT.setText(crntTempUnit);
                        day2hight.setText(day2high);
                        day2lowt.setText(day2low);
                        day2TempUnitT.setText(crntTempUnit);
                        day3hight.setText(day3high);
                        day3lowt.setText(day3low);
                        day3TempUnitT.setText(crntTempUnit);
                        day4hight.setText(day4high);
                        day4lowt.setText(day4low);
                        day4TempUnitT.setText(crntTempUnit);
                        break;

                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        TextView myText = (TextView) view;
        Toast.makeText(this,"You Selected "+myText.getText(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(getApplicationContext(), "You selected Nothing", Toast.LENGTH_SHORT).show();
    }

    public void onRefresh() {
        getDayStatus();
    }
}
