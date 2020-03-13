package cz.cvut.fel.adaptivestructure.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import cz.cvut.fel.adaptivestructure.R;
import cz.cvut.fel.adaptivestructure.adaptation.AdaptationMaker;

public class MainFragment extends Fragment implements View.OnClickListener {

    SurfaceView surfaceView;
    AdaptationMaker adaptationMaker;
    private NavController navController = null;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        ((Button) view.findViewById(R.id.finances)).setOnClickListener(this);
        ((Button) view.findViewById(R.id.pictures)).setOnClickListener(this);
        surfaceView = (SurfaceView) view.findViewById(R.id.surfaceViewMain);
        adaptationMaker = AdaptationMaker.getAdaptationMaker();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onClick(View v) {
        switch (Objects.requireNonNull(v).getId()) {
            case R.id.finances:
                Objects.requireNonNull(navController).navigate(R.id.action_mainFragment_to_financesFragment);
                break;
            case R.id.pictures:
                Objects.requireNonNull(navController).navigate(R.id.action_mainFragment_to_pictureFragment);
                break;
            default:
                throw new IllegalArgumentException("None ID recognized!");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adaptationMaker.start(getContext(), surfaceView);
        adaptationMaker.createApplicationStructure(getView(), 0);
    }

    @Override
    public void onStop() {
        super.onStop();
        adaptationMaker.stop();
    }
}
