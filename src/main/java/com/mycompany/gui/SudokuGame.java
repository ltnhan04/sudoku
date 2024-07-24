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

/**
 * This is the Sudoku Game (MODEL) Manager.
 *
 * @version 1.0
 */
public class SudokuGame {

    public static final Color BKGD_DARK_GRAY = new Color(7, 79, 87);
    public static final Color BKGD_LIGHT_GRAY = new Color(7, 113, 135);
    public static final Color APP_GREEN = new Color(158, 206, 154);

    // Sudoku Model Attributes
    private ArrayList<Player> listUsers;
    private Player player;
    private Grid puzzle;
    private int hintsUsed;
    private Timer timer;

    private static final String USER_API_URL = "https://script.google.com/macros/s/AKfycbynMsU9z5pdw6zDqHt9tyUbsIU5bia4GEvhMQTBmNnpu_gAQKHXTxmnBP9e32aHuzhz/exec";

    // Thêm một đối tượng lưu thông tin người dùng hiện tại
    private static Player currentPlayer;

    /**
     * Default Sudoku Model Constructor.
     */
    public SudokuGame() {
        fetchUsersFromAPI();
    }

    /**
     * Fetch user data from API and set the player.
     */
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

                // Set the first player as the current player for demo purposes
                if (i == 0) {
                    this.player = player;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Increases the player's score at completion.
     *
     * @param pointsAwarded the number of points to increase by
     */
    public void increaseScore(int pointsAwarded, int gameTime) {
        if (currentPlayer != null) {
            currentPlayer.setScore(currentPlayer.getScore() + pointsAwarded);
            currentPlayer.setTime(gameTime);
            currentPlayer.updateScoreInAPI(currentPlayer.getId(), currentPlayer.getScore(), currentPlayer.getTime());
        }
    }

    /**
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @param player the player to set
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * @return the puzzle
     */
    public Grid getPuzzle() {
        return puzzle;
    }

    /**
     * @param puzzle the puzzle to set
     */
    public void setPuzzle(Grid puzzle) {
        this.puzzle = puzzle;
    }

    /**
     * @return the latest list of scores
     */
    public ArrayList<Player> getListUsers() {
        fetchUsersFromAPI();
        return listUsers;
    }

    /**
     * Retrieves an updated version of the scores
     */
   

    /**
     * @return the number of hints used
     */
    public int getHintsUsed() {
        return hintsUsed;
    }

    /**
     * @param hintsUsed the number of hints used
     */
    public void setHintsUsed(int hintsUsed) {
        this.hintsUsed = hintsUsed;
    }

    /**
     * @return a string-formatted version of hints used
     */
    public String getStringHintsUsed() {
        return this.getHintsUsed() + "/" + this.getPuzzle().getDifficulty().getMaxHints();
    }

    /**
     * @return the timer
     */
    public Timer getTimer() {
        return timer;
    }

    /**
     * @param timer the timer to set
     */
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
