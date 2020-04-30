package cz.cvut.fel.adaptivestructure;

import android.os.Bundle;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;
import cz.cvut.fel.adaptivestructure.adaptation.AdaptationMaker;
import cz.cvut.fel.adaptivestructure.adaptation.MoveMaker;

public class MainActivity extends AppCompatActivity {

    public SurfaceView surfaceView;
    AdaptationMaker adaptationMaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surfaceView = new SurfaceView(this);
        surfaceView.getHolder().setFixedSize(1,1);
        //DatabaseInit.getASDatabase(this).nodeDao().deleteAll();
        setContentView(R.layout.activity_main);
        adaptationMaker = AdaptationMaker.getAdaptationMaker();
        MoveMaker.makeMove(this, surfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adaptationMaker.adapt(this, surfaceView, MoveMaker.getInstance().currentView, -1, MoveMaker.getInstance().currentViewName);
    }

    @Override
    public void onBackPressed() {
        MoveMaker.getInstance().setBackClickListeners(this);
    }
}
