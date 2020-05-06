package cz.cvut.fel.adaptivestructure;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceView;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import cz.cvut.fel.adaptivestructure.adaptation.AdaptationPrepare;
import cz.cvut.fel.adaptivestructure.adaptation.MoveMaker;
import cz.cvut.fel.adaptivestructure.database.DatabaseInit;

/**
 * Demo app activity.
 *
 * @author Marek Klement
 */
public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 101;

    public SurfaceView surfaceView;
    AdaptationPrepare adaptationPrepare;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // some permissions needed
        checkPermission(
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                PERMISSION_CODE);
        waitAnswered();
        // wait for permissions
        //
        surfaceView = new SurfaceView(this);
        surfaceView.getHolder().setFixedSize(1, 1);
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

    public void checkPermission(String[] permissions, int requestCode)
    {
        List<String> permissionsToGet = new LinkedList<>();
        for (String permission : permissions){
            if (ContextCompat.checkSelfPermission(
                    MainActivity.this,
                    permission)
                    == PackageManager.PERMISSION_DENIED){
                permissionsToGet.add(permission);
            }
        }
        if(permissionsToGet.size()!=0){
            ActivityCompat
                    .requestPermissions(
                            MainActivity.this,
                            permissionsToGet.toArray(new String[permissionsToGet.size()]),
                            requestCode);
        }
    }

    public void waitAnswered(){
        boolean isAnswered = false;

        while(!isAnswered){
            if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                isAnswered = true;
            }
        }
    }
}
