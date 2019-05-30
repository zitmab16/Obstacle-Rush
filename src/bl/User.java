/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bl;

import java.awt.Color;

/**
 *
 * @author vizug
 */
public class User {
    
    private String user;
    private Color color;

    public User(String name,Color color) {
        
        this.user = name;
        this.color=color;
    }

    public String getUsername() {
        return user;
    }

    public Color getColor() {
        return color;
    }
    
    
}
