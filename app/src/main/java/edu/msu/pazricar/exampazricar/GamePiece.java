package edu.msu.pazricar.exampazricar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class GamePiece {
    /**
     * The image for the actual piece.
     */
    private final Bitmap piece;

    /**
     * The game piece ID
     */
    private int id;

    /**
     * x location.
     */
    private float x;

    /**
     * y location
     */
    private float y;

    /**
     * Determines if the piece is flipped
     */
    private boolean flipped = false;

    /**
     * Paint for filling the piece when not flipped
     */
    private final Paint fillPaintFalse;

    /**
     * Paint for filling the piece when  flipped
     */
    private final Paint fillPaintTrue;

    /**
     * Game Piece Constructor
     * @param context
     * @param id
     * @param x
     * @param y
     */
    public GamePiece(Context context, int id, float x, float y) { //, float finalX, float finalY) {
        this.x = x;
        this.y = y;
        this.id = id;

        fillPaintFalse = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaintFalse.setColor(0xff00ff00);

        fillPaintTrue = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaintTrue.setColor(0xffffffff);

        piece = BitmapFactory.decodeResource(context.getResources(), id);
    }

    /**
     * Draw the puzzle piece
     * @param canvas Canvas we are drawing on
     * @param gameSize Size we draw the game in pixels
     */
    public void draw(Canvas canvas, int gameSize, float scaleFactor) {

        float width = piece.getWidth();
        float height = piece.getHeight();


        float scaleFactorW =  (scaleFactor/2) / width;
        float scaleFactorH =  (scaleFactor/2) / height;


        canvas.save();

        // Convert x,y to pixels and add the margin, then draw
        canvas.translate(x * gameSize, y * gameSize);

        // Scale it to the right size
        canvas.scale(scaleFactorW, scaleFactorH);

        // Draw the bitmap
        if(flipped) {
            canvas.drawRect(0,0, piece.getWidth(), piece.getHeight(), fillPaintTrue);
            canvas.drawBitmap(piece, 0, 0, null);
        } else {
            canvas.drawRect(0,0, piece.getWidth(), piece.getHeight(), fillPaintFalse);
        }
        canvas.restore();
    }

    /**
     * Gets the X coordinate
     * @return x
     */
    public float getX() {
        return x;
    }

    /**
     * Sets the X coordinate
     * @param x
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Gets the Y coordinate
     * @return y
     */
    public float getY() {
        return y;
    }

    /**
     * Sets the Y coordinate
     * @param y
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * The flipped status of a piece
     * @return true if flipped
     */
    public boolean isFlipped() {
        return flipped;
    }

    /**
     * Sets the flipped status of the piece
     * @param flipped
     */
    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    /**
     * Gets the piece ID
     * @return
     */
    public int getID() { return id; }

    /**
     * Sets the piece ID
     * @param id
     */
    public void setID(int id) {
        this.id = id;
    }

    /**
     * Test to see if we have touched a puzzle piece
     * @param testX X location as a normalized coordinate (0 to 1)
     * @param testY Y location as a normalized coordinate (0 to 1)
     * @return true if we hit the piece
     */
    public boolean hit(float testX, float testY) {
        if(testX >= x && testX < x + .25f){
            if(testY >= y && testY < y + .25f)
            {
                return true;
            }
        }
        return false;
    }
}
