package edu.msu.pazricar.exampazricar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
            // We have saved state
            getGameView().loadInstanceState(savedInstanceState);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        getGameView().saveInstanceState(bundle);
    }

    /**
     * Get the Game view
     * @return GameView reference
     */
    private GameView getGameView() {
        return (GameView)this.findViewById(R.id.view);
    }

    /**
     * Peeks the matching pairs
     * @param view
     */
    public void onPeek(View view)
    {
        getGameView().getGame().onPeek();
    }

    /**
     * Resets the game
     * @param view
     */
    public void onReset(View view)
    {
        getGameView().getGame().onReset();
    }

}