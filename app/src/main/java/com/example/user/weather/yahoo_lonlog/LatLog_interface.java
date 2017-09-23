package com.example.user.weather.yahoo_lonlog;

import com.example.user.weather.service_interface.DayStatus;
import com.example.user.weather.yahoo_weather.Yweather;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by user on 23/9/2017.
 */

public interface LatLog_interface {

    double Latitude=40.741895;
    double  Longitude=-73.989308;

    //String query2= "select * from weather.forecast where woeid in (SELECT woeid FROM geo.places WHERE text="(latitude,longitude)")
    String url = "select%20woeid%20from%20geo.places%20where%20text%3D%22(" + Latitude + "," + Longitude + ")%22%20limit%201&diagnostics=false";

    String city = "dhaka";
    String query = "select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+ city +"%22)&format=json";

    String BASE_URL = "https://query.yahooapis.com/v1/public/";


//    @GET("yql?q=select%20woeid%20from%20geo.places%20where%20text%3D%22({Latitude},{Longitude})%22%20limit%201&diagnostics=false")
//    Call<Example> groupList(@Path("lat") double latitude,@Path("long") double latitude);


    String url2="select%20*%20from%20weather.forecast%20where%20woeid%20in%20(SELECT%20woeid%20FROM%20geo.places%20WHERE%20text%3D%22(40.7141667%2C-74.0063889)%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    @GET("yql?q="+url2)
    Call<Example> getWeather();

    class Factory {

        private static LatLog_interface service;
        public static LatLog_interface getInstance() {
            if (service == null) {
                Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build();
                service = retrofit.create(LatLog_interface.class);
                return service;
            }
            else {
                return service;
            }
        }
    }

}
