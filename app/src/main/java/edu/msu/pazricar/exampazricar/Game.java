package edu.msu.pazricar.exampazricar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import java.lang.Math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Game {

    /**
     * Paint for filling the area the game is in
     */
    private final Paint fillPaint;

    /**
     * Paint for outlining the area the game is in
     */
    private final Paint outlinePaint;

    /**
     * The size of the game in pixels
     */
    private int gameSize;

    /**
     * Collection of puzzle pieces
     */
    public ArrayList<GamePiece> pieces = new ArrayList<>();

    /**
     * How much we scale the game pieces
     */
    private float scaleFactor;

    /**
     * First piece to match
     */
    private GamePiece piece1 = null;

    /**
     * Second piece to match
     */
    private GamePiece piece2 = null;

    /**
     * Determines if we are peeking or not
     */
    private boolean peeking = false;

    /**
     * Random number generator
     */
    private static final Random rand = new Random();

    /**
     * Game context
     */
    Context mContext;

    /**
     * Constructor
     * @param context
     */
    public Game(Context context) {
        // Create paint for filling the area the puzzle will
        // be solved in.
        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(0xff013220);

        outlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outlinePaint.setColor(0xff000000);
        outlinePaint.setStyle(Paint.Style.STROKE);

        mContext = context;

        // Load in the puzzle
        LoadPieces(context);
    }

    /**
     * Loads in the game pieces
     * @param context
     */
    private void LoadPieces(Context context) {
        pieces.add(new GamePiece(context, R.drawable.helmet, 0,0));
        pieces.add(new GamePiece(context, R.drawable.helmet, 0,0));

        pieces.add(new GamePiece(context, R.drawable.chess_bdt45, 0,0));
        pieces.add(new GamePiece(context, R.drawable.chess_bdt45, 0,0));

        pieces.add(new GamePiece(context, R.drawable.chess_kdt45, 0,0));
        pieces.add(new GamePiece(context, R.drawable.chess_kdt45, 0,0));

        pieces.add(new GamePiece(context, R.drawable.chess_ndt45, 0,0));
        pieces.add(new GamePiece(context, R.drawable.chess_ndt45, 0,0));

        pieces.add(new GamePiece(context, R.drawable.chess_pdt45, 0,0));
        pieces.add(new GamePiece(context, R.drawable.chess_pdt45, 0,0));

        pieces.add(new GamePiece(context, R.drawable.chess_qdt45, 0,0));
        pieces.add(new GamePiece(context, R.drawable.chess_qdt45, 0,0));

        pieces.add(new GamePiece(context, R.drawable.chess_rdt45, 0,0));
        pieces.add(new GamePiece(context, R.drawable.chess_rdt45, 0,0));

        pieces.add(new GamePiece(context, R.drawable.pikachu, 0,0));
        pieces.add(new GamePiece(context, R.drawable.pikachu, 0,0));

        shuffleArray(pieces, rand);
        boolean breakLoop = false;
        int x = 0;
        int y = 0;

        int counter = 0;
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                // Breaks loop if the counter exceeds the number of pieces
                if(counter >= pieces.size())
                {
                    breakLoop = true;
                    break;
                }
                pieces.get(counter).setX(x + i * .25f);
                pieces.get(counter).setY(y + j * .25f);
                counter++;
            }
            if(breakLoop)
            {
                break;
            }
        }
    }

    /**
     * Shuffles the pairs in the game
     * @param pieces
     * @param random
     */
    private void shuffleArray(ArrayList<GamePiece> pieces, Random random) {
        for (int i = pieces.size() - 1; i > 0; i--) {

            // Generate random number
            int j = (int) Math.floor(random.nextFloat() * (i + 1));

            GamePiece temp = pieces.get(i);
            pieces.set(i, pieces.get(j));
            pieces.set(j, temp);
        }
    }

    /**
     * Draws the game board and the match pair pieces
     * @param canvas
     */
    public void draw(Canvas canvas) {
        int wid = canvas.getWidth();
        int hit = canvas.getHeight();

        // Determine the minimum of the two dimensions
        int minDim = Math.min(wid, hit);
        gameSize = (minDim);
        
        
        // Draw the outline of the puzzle, first the green background
        canvas.drawRect(0, 0,
                gameSize, gameSize, fillPaint);
        canvas.drawRect(0, 0, gameSize, gameSize, outlinePaint);
        
        scaleFactor = (float)gameSize/2;
        canvas.save();
        canvas.scale(scaleFactor, scaleFactor);
        canvas.restore();
        
        for(GamePiece piece : pieces) {
            piece.draw(canvas, gameSize, scaleFactor);
        }

        // Draw the gridlines for the game
        float xStart = 0;
        float yStart = 0;
        float interval = gameSize/4f;
        
        for(int i = 0; i < 4; i++) {
            // Vetical lines
            canvas.drawLine(xStart + interval * i, yStart, xStart + interval * i, gameSize, outlinePaint);
            // Horizontal lines
            canvas.drawLine(xStart , yStart + interval * i, gameSize, yStart + interval * i, outlinePaint);
        }
    }

    /**
     * Handle a touch event from the view.
     * @param view The view that is the source of the touch
     * @param event The motion event describing the touch
     * @return true if the touch is handled.
     */
    public boolean onTouchEvent(View view, MotionEvent event) {
        //
        // Convert an x,y location to a relative location in the
        // puzzle.
        //
        float relX = (event.getX()) / gameSize;
        float relY = (event.getY()) / gameSize;

        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                return onTouched(relX, relY);
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_MOVE:

        }

        return false;
    }

    /**
     * Handle a touch message. This is when we get an initial touch
     * @param x x location for the touch, relative to the puzzle - 0 to 1 over the puzzle
     * @param y y location for the touch, relative to the puzzle - 0 to 1 over the puzzle
     * @return true if the touch is handled
     */
    private boolean onTouched(float x, float y) {

        // Check each piece to see if it has been hit
        // We do this in reverse order so we find the pieces in front
        for(int p=pieces.size()-1; p>=0;  p--) {
            if(pieces.get(p).hit(x, y)) {
                // We hit a piece!
                pieces.get(p).setFlipped(true);
                if(piece1 == null){
                    piece1 = pieces.get(p);
                } else if(pieces.get(p).getID() == piece1.getID() &&
                        pieces.get(p).getX() == piece1.getX() &&
                        pieces.get(p).getY() == piece1.getY()) {
                    //Same piece that we clicked on before
                    continue;
                }

                else{
                    piece2 = pieces.get(p);
                    possibleMatch();
                }
                return true;
            }
        }

        return false;
    }

    /**
     * Flips all the cards to peek and vice versa
     */
    public void onPeek()
    {
        if(!peeking)
        {
            peeking = true;
            for(GamePiece piece : pieces)
            {
                piece.setFlipped(true);
            }
        } else{
            peeking = false;
            for(GamePiece piece : pieces)
            {
                piece.setFlipped(false);
            }
        }
    }

    /**
     * Resets the game
     */
    public void onReset()
    {
        peeking = true;
        onPeek();
        //
        //
        // Random is not working properly
        //
        //
        LoadPieces(mContext);
    }


    /**
     * Determines if the game is complete
     * @return true if complete
     */
    public boolean isDone()
    {
        if(peeking)
        {
            return false;
        }
        for(GamePiece piece : pieces)
        {
            if(!piece.isFlipped())
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if there is a match or not
     */
    public void possibleMatch()
    {
        if(piece1.getID() == piece2.getID())
        {
            ArrayList<GamePiece> temp = new ArrayList<>();

            for(GamePiece piece : pieces){
                if(piece.getID() == piece1.getID() &&
                        piece.getX() == piece1.getX() &&
                        piece.getY() == piece1.getY())
                {
                    // Matches piece one
                } else if(piece.getID() == piece2.getID() &&
                        piece.getX() == piece2.getX() &&
                        piece.getY() == piece2.getY())
                {
                    // Mathes piece two
                } else
                {
                    // Add the pieces to a new array
                    temp.add(piece);
                }
            }
            pieces = temp;
        } else {
            // No match found
            piece1.setFlipped(false);
            piece2.setFlipped(false);
        }
        piece1 = null;
        piece2 = null;
    }

    /**
     * The name of the bundle keys to save the game
     */
    private final static String LOCATIONS = "Game.locations";
    private final static String IDS = "Game.ids";
    private final static String FLIP = "Game.flipped";
    private final static String LOCATIONPIECE1 = "Game.locationPiece1";
    private final static String IDPIECE1 = "Game.idPiece1";
    private final static String LOCATIONPIECE2 = "Game.locationPiece2";
    private final static String IDPIECE2 = "Game.idPiece2";
    private final static String PEEK = "Game.peek";


    /**
     * Save the game to a bundle
     * @param bundle The bundle we save to
     */
    public void saveInstanceState(Bundle bundle) {
        float [] locations = new float[pieces.size() * 2];
        int [] ids = new int[pieces.size()];
        boolean [] flipped = new boolean[pieces.size()];
        float [] locationPiece1 = new float[2];
        int idPiece1;
        float [] locationPiece2 = new float[2];
        int idPiece2;
        boolean peek = peeking;

        // Store the matching pairs info
        for(int i=0;  i<pieces.size(); i++) {
            GamePiece piece = pieces.get(i);
            locations[i*2] = piece.getX();
            locations[i*2+1] = piece.getY();
            ids[i] = piece.getID();
            flipped[i] = piece.isFlipped();
        }

        // Store the touched the piece1
        if(piece1 != null)
        {
            locationPiece1[0] = piece1.getX();
            locationPiece1[1] = piece1.getY();
            idPiece1 = piece1.getID();
        } else {
            locationPiece1[0] = -1;
            idPiece1 = -1;
        }

        // Store the touched the piece2
        if(piece2 != null)
        {
            locationPiece2[0] = piece2.getX();
            locationPiece2[1] = piece2.getY();
            idPiece2 = piece2.getID();
        } else {
            locationPiece2[0] = -1;
            idPiece2 = -1;
        }

        bundle.putFloatArray(LOCATIONS, locations);
        bundle.putIntArray(IDS,  ids);
        bundle.putBooleanArray(FLIP, flipped);
        bundle.putFloatArray(LOCATIONPIECE1, locationPiece1);
        bundle.putInt(IDPIECE1, idPiece1);
        bundle.putFloatArray(LOCATIONPIECE2, locationPiece2);
        bundle.putInt(IDPIECE2, idPiece2);
        bundle.putBoolean(PEEK, peek);
    }

    /**
     * Read the game from a bundle
     * @param bundle The bundle we save to
     */
    public void loadInstanceState(Bundle bundle) {
        float [] locations = bundle.getFloatArray(LOCATIONS);
        int [] ids = bundle.getIntArray(IDS);
        boolean [] flipped = bundle.getBooleanArray(FLIP);
        float [] locationPiece1 = bundle.getFloatArray(LOCATIONPIECE1);
        int idPiece1 = bundle.getInt(IDPIECE1);
        float [] locationPiece2 = bundle.getFloatArray(LOCATIONPIECE2);
        int idPiece2 = bundle.getInt(IDPIECE2);
        peeking = bundle.getBoolean(PEEK);

        // Peeking is set
        if(peeking){
            peeking = false;
            onPeek();
        }

        // The matching pieces are set
        ArrayList<GamePiece> temp = new ArrayList<>();
        for(int i=0; i<ids.length; i++) {
            GamePiece piece = new GamePiece(mContext, ids[i], locations[i*2],locations[i*2 + 1]);
            piece.setFlipped(flipped[i]);
            temp.add(piece);
        }
        pieces.clear();
        pieces = temp;

        // Touched pieces are set
        if(idPiece1 != -1) {
            piece1 = new GamePiece(mContext, idPiece1, locationPiece1[0],locationPiece1[1]);
        }
        if(idPiece2 != -1)
        {
            piece2 = new GamePiece(mContext, idPiece2, locationPiece2[0],locationPiece2[1]);
        }
    }
}
