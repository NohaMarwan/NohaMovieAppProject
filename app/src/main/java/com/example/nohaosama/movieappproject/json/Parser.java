package com.example.nohaosama.movieappproject.json;


import com.example.nohaosama.movieappproject.models.modelMovie;
import com.example.nohaosama.movieappproject.models.reviewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aya on 06/11/2016.
 */

public class Parser {

    public static List<modelMovie> parseStringToJson(String data) {
        List<modelMovie> modelMovies;

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.optJSONArray("results");
            modelMovies = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject movieJsonObject = jsonArray.getJSONObject(i);
                String ID = movieJsonObject.optString("id");
                String poster_path = "http://image.tmdb.org/t/p/w185/" + movieJsonObject.optString("poster_path");
                String backdrop_path = "http://image.tmdb.org/t/p/w185/" + movieJsonObject.optString("backdrop_path");
                String overview = movieJsonObject.optString("overview");
                String release_date = movieJsonObject.optString("release_date");
                String vote_average = movieJsonObject.optString("vote_average");
                String original_title = movieJsonObject.optString("original_title");



                modelMovie movie = new modelMovie();
                movie.setID(ID);
                movie.setPoster(poster_path);
                movie.setOverview(overview);
                movie.setRelease_date(release_date);
                movie.setVote_average(vote_average);
                movie.setOriginal_title(original_title);
                movie.setBackdrop_path(backdrop_path);

                modelMovies.add(movie);


            }

            return modelMovies;

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    public static List<reviewModel> parseDataToReview(String data) {
        List<reviewModel> reviewMovies;

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.optJSONArray("results");
            reviewMovies = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject reviewJsonObject = jsonArray.getJSONObject(i);
                String auther = reviewJsonObject.optString("author");
                String content = reviewJsonObject.optString("content");
                String url = reviewJsonObject.optString("url");


                reviewModel review = new reviewModel();
                review.setAuther(auther);
                review.setContent(content);
                review.setUrl(url);
                reviewMovies.add(review);


            }

            return reviewMovies;

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    public static String getVideoID(String data) {

        String key = "";
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.optJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject JsonObject = jsonArray.getJSONObject(i);
                String type = JsonObject.optString("type");
                if (type.equalsIgnoreCase("Trailer"))
                    key = JsonObject.optString("key");
            }

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return key;
    }
}