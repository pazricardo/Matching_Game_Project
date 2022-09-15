package edu.msu.pazricar.exampazricar;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class GameView extends View {
    /**
     * The Game
     */
    private Game game;

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        game = new Game(getContext());

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        game.draw(canvas);
        if(game.isDone())
        {
            post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), R.string.winner, Toast.LENGTH_SHORT).show();
                }
            });
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return game.onTouchEvent(this, event);
    }

    /**
     * Game the view is calling
     * @return
     */
    public Game getGame() {
        return game;
    }

    /**
     * Save the game to a bundle
     * @param bundle The bundle we save to
     */
    public void saveInstanceState(Bundle bundle) {
        game.saveInstanceState(bundle);
    }

    /**
     * Load the game from a bundle
     * @param bundle The bundle we save to
     */
    public void loadInstanceState(Bundle bundle) {
        game.loadInstanceState(bundle);
    }
}
