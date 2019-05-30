/*

 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bl;

/**
 *
 * @author vizug
 */
public class Highscore {
    private int score;
    private String username;

    public Highscore(int score, String username) {
        this.score = score;
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public String getUsername() {
        return username;
    }
    
    
}
