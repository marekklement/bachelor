package cz.cvut.fel.adaptivestructure;

import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import cz.cvut.fel.adaptivestructure.adaptation.AdaptationPrepare;
import cz.cvut.fel.adaptivestructure.adaptation.MoveMaker;
import cz.cvut.fel.adaptivestructure.database.DatabaseInit;

public class MainActivity extends AppCompatActivity {

    public SurfaceView surfaceView;
    AdaptationPrepare adaptationPrepare;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surfaceView = new SurfaceView(this);
        surfaceView.getHolder().setFixedSize(1,1);
        // delete all to test
//        DatabaseInit.getASDatabase(this).cleanDatabase();
        setContentView(R.layout.activity_main);
        adaptationPrepare = AdaptationPrepare.getAdaptationMaker();
        MoveMaker.makeMove(this, surfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adaptationPrepare.adapt(this, surfaceView, MoveMaker.getInstance().currentView, -1, MoveMaker.getInstance().currentViewName);
    }

    @Override
    public void onBackPressed() {
        MoveMaker.getInstance().setBackClickListeners(this);
    }
}
