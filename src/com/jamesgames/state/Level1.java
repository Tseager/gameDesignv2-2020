package com.jamesgames.state;

import com.jamesgames.entity.Player;
import com.jamesgames.main.LevelPanel;
import com.jamesgames.tilemap.TileMapManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class Level1 extends LevelState
{
    private Player p;
    private boolean win;
    
    //enemies
    private Enemy[] enemies;
    
    //collectables
    private Collectable[] collectables;
    
    public Level1(LevelManager lm)
    {
        super(lm);
        p = new Player("/Images/player.png");
        
        win=false;
        init();
        initEnemies();
        initCollectables();
                
    }
    
    private void init()
    {
        
    }
    
    private void initEnemies()
    {
        enemies = new Enemy[5];
        
        for (int i = 0; i < enemies.length; i++) 
        {
            enemies[i] = new Enemy("/Images/enemy.png");
        }     
    }
    
    private void initCollectables()
    {
        collectables = new Collectable[5];
        
        for (int i = 0; i < collectables.length; i++) 
        {
            collectables[i] = new Collectable("/Images/collectable.png");
        }
    }
    
    @Override
    public void keyPressed(int keyCode)
    {
        if(keyCode == KeyEvent.VK_A)
            p.moveLeft(true);
        
        if(keyCode == KeyEvent.VK_D)
            p.moveRight(true);
    }
    
    @Override
    public void KeyReleased(int keyCode)
    {
        if(keyCode == KeyEvent.VK_A)
            p.moveLeft(false);
        
        if(keyCode == KeyEvent.VK_D)
            p.moveRight(false);
    }
    
    @Override
    public void update()
    {
        p.update();
        p.checkEnemyCollision(enemies);
        p.checkCollectableCollision(collectables);
        
        //update computer moves
        
        //check collisions
        
        //other updates
        
    }
    
    @Override
    public void draw(Graphics2D g)
    {
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, LevelPanel.PANEL_WIDTH, LevelPanel.PANEL_HEIGHT);
        
        p.draw(g);
    }

    
    

}
