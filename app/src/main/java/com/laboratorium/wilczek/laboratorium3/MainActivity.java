package com.laboratorium.wilczek.laboratorium3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainActivity extends AppCompatActivity {

    private String BASE_URL = "http://dog.ceo/api/";
    ArrayList<String> dogs = new ArrayList<>();

    private OkHttpClient client;

    private Gson gson = new Gson();
    private DogListResponse dogListResponse;
    private RecyclerView.Adapter<DogsListAdapter.ViewHolder> dogsListAdapter;
    private dogImageResponse dogImageResponse;

    @BindView(R.id.dogs_rv)
    RecyclerView recycler;

    @BindView(R.id.random_dog)
    ImageView randomDogImage;

    @OnClick(R.id.show_random_dog_btn)
    void showDog() {
        getRandomDogPhoto("breeds/image/random");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setAdapter();
        createClient();
        try {
            getDogList("breeds/list");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position, String name) {
                getBreedPhoto("breed/"+name+"/images/random");
            }
        };
        dogsListAdapter = new DogListAdapterBuilder().setDogs(dogs).setListener(listener).createDogsListAdapter();
        recycler.setAdapter(dogsListAdapter);
    }

    private void createClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }

    private Request createRequest(String url) {
        return new Request.Builder()
                .url(BASE_URL + url)
                .build();
    }

    private void getBreedPhoto(String url) {
        Request request = createRequest(url);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(e.getLocalizedMessage(), e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                dogImageResponse = gson.fromJson(responseBody.string(), dogImageResponse.class);
                if (dogImageResponse.getStatus().equals("success")) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(MainActivity.this)
                                    .load(dogImageResponse.getMessage())
                                    .into(randomDogImage);
                            dogsListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });


    }

    private void getRandomDogPhoto(final String url) {
        final Request request= createRequest(url);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(e.getLocalizedMessage(), e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                dogImageResponse = gson.fromJson(responseBody.string(), dogImageResponse.class);
                if (dogImageResponse.getStatus().equals("success")) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(MainActivity.this)
                                    .load(dogImageResponse.getMessage())
                                    .into(randomDogImage);
                            dogsListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });


    }

    private void getDogList(String url) throws IOException {
        Request request = createRequest(url);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(e.getLocalizedMessage(), e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                dogListResponse = gson.fromJson(responseBody.string(), DogListResponse.class);
                if (dogListResponse.getStatus().equals("success")) {
                    dogs.addAll(dogListResponse.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            dogsListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }
}
