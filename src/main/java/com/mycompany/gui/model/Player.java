package com.mycompany.gui.model;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Information Relating to a Sudoku Player
 *
 * @version 1.0
 */
public class Player {

    // Player Attributes
    private int id;
    private String username;
    private String email;
    private String password;
    private int score;
    private static final String USER_API_URL = "https://script.google.com/macros/s/AKfycbyadGWIfSBvsE3lZpafKmK46E85dAHkKNjuaP6qu52BJZzq0H6BFgdSgOtXeNF0xcERkA/exec";
    private ArrayList<Player> listUsers;
    private Player player;

    /**
     * Constructs a Sudoku Player.
     *
     * @param id the player's ID
     * @param username the player's username
     * @param email the player's email address
     * @param password the player's password
     * @param score the player's score
     */
    public Player(int id, String username, String email, String password, int score) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.score = score;
    }

    public Player(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
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

            // Parse the response JSON
            String responseData = response.body().string();
            JSONArray jsonArray = new JSONArray(responseData);

            this.listUsers = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Create a new player from the response
                int id = jsonObject.getInt("id");
                String username = jsonObject.getString("username");
                String email = jsonObject.getString("email");
                String password = jsonObject.getString("password");
                int score = jsonObject.getInt("score");

                Player player = new Player(id, username, email, password, score);
                this.listUsers.add(player);

                // Set the first player as the current player for demo purposes
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
                return true;
            }
        }
        return false;
    }

    public boolean registerUser() {
        fetchUsersFromAPI();
        for (Player player : this.listUsers) {
            if (player.getEmail().equals(this.email)) {
                return false; // User already exists
            }
        }
        // Code to register the user (e.g., send POST request to your API)
        return true;
    }

    // Getter and Setter methods

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
}
