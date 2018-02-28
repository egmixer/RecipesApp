package com.example.recipesapp.webservice;

import com.example.recipesapp.webservice.models.RecipeResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;


public class WebService {
    private static final String TAG = WebService.class.getSimpleName();
    private static WebService mInstance;
    private Services mServices;
    private Retrofit mRetrofit;
    private static final String BASE_URL = "http://go.udacity.com/";

    private WebService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mServices = mRetrofit.create(Services.class);
    }

    public static WebService getInstance() {
        if (mInstance == null) {
            mInstance = new WebService();
        }

        return mInstance;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public Services getServices() {
        return mServices;
    }

    public interface Services {
        @GET("android-baking-app-json")
        Call<List<RecipeResponse>> getRecipes();
    }
}
