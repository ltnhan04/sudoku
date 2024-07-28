package com.mycompany.gui.model;

import com.mycompany.gui.SudokuGame;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Player {

    private int id;
    private String username;
    private String email;
    private String password;
    private int score;
    private int gameTime;
    private static final String USER_API_URL = "https://script.google.com/macros/s/AKfycbynMsU9z5pdw6zDqHt9tyUbsIU5bia4GEvhMQTBmNnpu_gAQKHXTxmnBP9e32aHuzhz/exec";
    private ArrayList<Player> listUsers;
    private Player player;

    public Player(int id, String username, String email, String password, int score, int gameTime) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.score = score;
        this.gameTime = gameTime;
    }

    public Player(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.score = 0;
        this.gameTime = 0;
    }

    public Player(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void fetchUsersFromAPI() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(USER_API_URL)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String responseData = response.body().string();
            JSONArray jsonArray = new JSONArray(responseData);
            this.listUsers = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int id = jsonObject.getInt("id");
                String username = jsonObject.getString("username");
                String email = jsonObject.getString("email");
                String password = jsonObject.getString("password");
                int score = jsonObject.getInt("score");
                int gameTime = jsonObject.getInt("time");
                Player player = new Player(id, username, email, password, score, gameTime);
                this.listUsers.add(player);

                if (i == 0) {
                    this.player = player;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkLogin() {
        fetchUsersFromAPI();
        for (Player player : this.listUsers) {
            if (player.getEmail().equals(this.email) && player.getPassword().equals(this.password)) {
                this.id = player.getId();
                this.username = player.getUsername();
                this.score = player.getScore();
                this.gameTime = player.getTime();
                SudokuGame.setCurrentPlayer(player);
                return true;
            }
        }
        return false;
    }

    public boolean registerUser() {
        fetchUsersFromAPI();
        for (Player player : this.listUsers) {
            if (player.getEmail().equals(this.email)) {
                return false;
            }
        }
        return postUserToAPI();
    }

    private boolean postUserToAPI() {
        OkHttpClient client = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", this.username);
        jsonObject.put("email", this.email);
        jsonObject.put("password", this.password);
        jsonObject.put("score", this.score);
        jsonObject.put("time", 0);
        RequestBody body = RequestBody.create(
                jsonObject.toString(), MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(USER_API_URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateScoreInAPI(int id, int score, int gameTime) {
        OkHttpClient client = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", "updateScore");
        jsonObject.put("id", id);
        jsonObject.put("score", score);
        jsonObject.put("time", gameTime);
        RequestBody body = RequestBody.create(
                jsonObject.toString(), MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(USER_API_URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    public void setTime(int gameTime){
        this.gameTime = gameTime;
    }
    public int getTime(){
        return gameTime;
    }

}
