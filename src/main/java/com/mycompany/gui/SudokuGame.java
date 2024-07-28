package com.mycompany.gui;

import com.mycompany.gui.model.Player;
import com.mycompany.gui.model.Grid;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.Timer;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;


public class SudokuGame {

    public static final Color BKGD_DARK_GRAY = new Color(7, 79, 87);
    public static final Color BKGD_LIGHT_GRAY = new Color(7, 113, 135);
    public static final Color APP_GREEN = new Color(158, 206, 154);

    private ArrayList<Player> listUsers;
    private Player player;
    private Grid puzzle;
    private int hintsUsed;
    private Timer timer;

    private static final String USER_API_URL = "https://script.google.com/macros/s/AKfycbynMsU9z5pdw6zDqHt9tyUbsIU5bia4GEvhMQTBmNnpu_gAQKHXTxmnBP9e32aHuzhz/exec";

    private static Player currentPlayer;


    public SudokuGame() {
        fetchUsersFromAPI();
    }


    private void fetchUsersFromAPI() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(USER_API_URL)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Parse the response JSON
            String responseData = response.body().string();
            System.out.println("Response: " + responseData);
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
                int gameTime = jsonObject.getInt("time");
                System.out.print("game time: " + gameTime);
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


    public void increaseScore(int pointsAwarded, int gameTime) {
        if (currentPlayer != null) {
            currentPlayer.setScore(currentPlayer.getScore() + pointsAwarded);
            currentPlayer.setTime(gameTime);
            currentPlayer.updateScoreInAPI(currentPlayer.getId(), currentPlayer.getScore(), currentPlayer.getTime());
        }
    }


    public Player getPlayer() {
        return player;
    }


    public void setPlayer(Player player) {
        this.player = player;
    }


    public Grid getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(Grid puzzle) {
        this.puzzle = puzzle;
    }


    public ArrayList<Player> getListUsers() {
        fetchUsersFromAPI();
        return listUsers;
    }


    public int getHintsUsed() {
        return hintsUsed;
    }


    public void setHintsUsed(int hintsUsed) {
        this.hintsUsed = hintsUsed;
    }


    public String getStringHintsUsed() {
        return this.getHintsUsed() + "/" + this.getPuzzle().getDifficulty().getMaxHints();
    }


    public Timer getTimer() {
        return timer;
    }


    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public static void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }
}
