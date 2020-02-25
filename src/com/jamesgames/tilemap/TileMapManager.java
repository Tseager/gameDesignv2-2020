
package com.jamesgames.tilemap;

import com.jamesgames.main.LevelPanel;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import javax.imageio.ImageIO;

/**
 *
 * @author 1629489
 */
public class TileMapManager 
{
    //this calue represents the size of the tile (height and width)
    // This represents a tile size of 64x64
    private final int TILE_SIZE = 64;
    
    //this array stores the tiles in use in the game
    private Tile[] tiles;
    
    //this array represents the array map in use for the level
    //eventually the data in this array can be stored in a text file and
    //loaded in to the game
    //the number in the array is mapped to the array of tiles
    private final int[][] map =
    {
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,},
        {0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0,},
        {0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 1, 1, 0, 0,},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,},
        {2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 2,},
        {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 2,},
        {2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 2,},     
    };
    
    //these variables are used as the centre point for the camera
    //we use doubles so that we can calculate positions to sub-pixel position
    private double cameraX; //camera x - camera coordinates differ from screen coordinates
    private double cameraY; // camera Y
    
    //these variables control which rows and columns of the tilemap are going to be displayed on screen
    
    //these variables set the bounds of the camera
    
    private int xMax; //the mavimum X size of the level
    private int yMax; // the maximum Y size of the level
    private int xMin;
    private int yMin;
    
    private int numberOfColumns; // Number of rows in the tilemap
    private int numberOfRows; // Number of columns in the tilemap
    
    private int rowOffSet;
    private int colOffSet;
    
    private int numColumnsToDraw; //this value indicates the number of columns to draw
    private int numRowsToDraw; // this value indicates the number of rows to draw
    
    public TileMapManager()
    {
        int width;
        int height;
        
        numberOfRows = map.length; //quick hack to find number of rows dynamically - this can be set in a file
        numberOfColumns = map[0].length; // quick hack to find the number of columns dynamically - this can be set in a file
        
        //work out the number of columns to draw on screen - dont bother with parts of the tile map
        // that are currently off screen
        numColumnsToDraw = LevelPanel.PANEL_WIDTH / TILE_SIZE + 2; // +2 ensures that there is no gap at the edge of the screen where no tiles is drawn
        numRowsToDraw = LevelPanel.PANEL_HEIGHT / TILE_SIZE + 2;
        
        width = numberOfColumns * TILE_SIZE;
        height = numberOfRows * TILE_SIZE;
        
        xMin = LevelPanel.PANEL_WIDTH - width;
        xMax = 0;
        yMin = LevelPanel.PANEL_HEIGHT - height;
        yMax = 0;
        cameraX = 0;
        cameraY = 0;
        loadTiles();
        
    }
    
    /**
     * Loads the tiles from resources
     * 
     * improvements - load from a single image map files
     * only 3 types of tile are provided in this example
     */
    private void loadTiles()
    {
        tiles = new Tile[3];
        try
        {
            tiles[0] = new Tile(ImageIO.read(getClass().getResourceAsStream("/images/col1.png")), Tile.TYPE_NORMAL);
            tiles[1] = new Tile(ImageIO.read(getClass().getResourceAsStream("/images/col2.png")), Tile.TYPE_NORMAL);
            tiles[2] = new Tile(ImageIO.read(getClass().getResourceAsStream("/images/col3.png")), Tile.TYPE_NORMAL);
        }catch(java.io.IOException ex)
        {
            System.err.println("Error loading tiles");
        }
    }
    
    /**
     * gets the tiles at the specified x and y coordinate
     * Parameters are double - this integrates with the level class
     * @param x
     * @param y 
     * @return
     */
    public Tile getTileAt(double x, double y)
    {
        //incoming x and y coordinates are screen coordinates - must convert to camera coordinates or we will return the wrong tile
        x = x - cameraX;
        y = y - cameraY;
        
        int row = (int) y / TILE_SIZE;
        int col = (int) x / TILE_SIZE;
        
        int tileID = map[row][col];
        
        tileID = matchTile(tileID);
        System.out.println("X: " + x + "Y: " + y);
        System.out.println("In tile: " + row + "," + col);
        return tiles[tileID];
    }
    
    /**
     * sets the position of the camera following the player
     * note to self - the x and y coordinates are half the width - player x and y
     * @param x
     * @param y
     */
    
    public void setCameraPosition(double x, double y)
    {
        cameraX += (x - cameraX);
        cameraY += (y - cameraY);
        
        fixCameraBounds();
        
        colOffSet = (int) -cameraX / TILE_SIZE;
        rowOffSet = (int) -cameraY / TILE_SIZE;
    }
    
    /**
     * this method ensures that the camera cannot go past the edges of the level
     */
    private void fixCameraBounds()
    {
        if(cameraX < xMin)
            cameraX = xMin;
        
        if(cameraX > xMax)
            cameraX = xMax;
        
        if(cameraY < yMin)
            cameraY = yMin;
        
        if(cameraY > yMax)
            cameraY = yMax;
    }
        
    /**
     * this method takes a value from the tile map and returns the correct tile image
     * array index
     * @param tileMapID
     * @return
     */
    private int matchTile(int tileMapID)
    {
        int mappedTile = 0;
        
        switch(tileMapID)
        {
            case 1:
                mappedTile = 0;
                break;
            case 2:
                mappedTile = 1;
                break;
            case 3:
                mappedTile = 2;
                break;
        }
        return mappedTile;
    }
    
    /**
     * this method draws the tilemap - this should happen first in the level drawing process
     * @param g
     */
    
    public void draw(Graphics2D g)
    {
        int tileImage;
        double tempX;
        double tempY; // these two variables are offsets from the camera start position
        
        g.setFont(new Font("Ariel", Font.PLAIN, 14));
        g.setColor(Color.WHITE);
        g.drawString("Camera X: " + cameraX + " Camera Y: " + cameraY, 20, 20);
        
        g.setColor(Color.BLACK);
        for(int row = rowOffSet; row < rowOffSet + numRowsToDraw; row++)
        {
            if(row >= numberofRows) break; // break causes the current iteration of the loop to be skipped
            
            tempY = cameraY + row * TILE_SIZE;
            for(int col = colOffSet; col < colOffSet + numColumnsToDraw; col++)
            {
                if(col >= numberOfColumns) break; //break causes the current iteration of the loop to be skipped
                
                tempX = cameraX + col * TILE_SIZE;
                //0 indicates no tile - so skip and dont draw
                if(map[row][col] != 0)
                {
                    tileImage = matchTile
                }
            }
        }
    }


    
}
