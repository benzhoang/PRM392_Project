package com.example.prm392_project.di;

import android.app.Application;

import com.example.prm392_project.data.remote.CosmeticsApi;
import com.example.prm392_project.data.repository.CosmeticsRepository;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public static CosmeticsApi provideCosmeticsApi() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        return new Retrofit.Builder()
                .baseUrl("https://bellavita-be.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(CosmeticsApi.class);
    }

    @Provides
    @Singleton
    public static CosmeticsRepository provideCosmeticsRepository(CosmeticsApi cosmeticsApi) {
        return new CosmeticsRepository(cosmeticsApi);
    }
}
