package com.example.bakingapp.data;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;


public class BakingApiBuilder {
  
  static final String BASE_API_URL = "http://go.udacity.com/";
  static final String RECIPES_JSON = "android-baking-app-json";
  
  public interface BakingApi {
    @GET(RECIPES_JSON)
    Call<ArrayList<Recipe>> getRecipes();
  }
  
  
  private static Retrofit getRetrofit() {
    return new Retrofit.Builder()
        .baseUrl(BASE_API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }
  
  
  public static BakingApi getBakingApi() {
    return getRetrofit().create(BakingApi.class);
  }
}
