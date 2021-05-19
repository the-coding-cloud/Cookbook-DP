package ro.oana.appetit.retrofit;

import android.annotation.SuppressLint;

import java.time.Duration;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class RetrofitClient {
    private static RetrofitClientInterface INSTANCE;

    public abstract Retrofit entityDao();

    public static RetrofitClientInterface getRetrofitClientInstance() {
        if (INSTANCE == null) {
            INSTANCE = createInstance();
        }
        return INSTANCE;
    }

    private static RetrofitClientInterface createInstance(){
        @SuppressLint({"NewApi", "LocalSuppress"}) final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        // TODO: CHANGE PORT NUMBER
        return new Retrofit.Builder()
                .baseUrl("http://localhost:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(RetrofitClientInterface.class);
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
