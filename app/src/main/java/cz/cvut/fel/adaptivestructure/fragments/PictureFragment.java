package cz.cvut.fel.adaptivestructure.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cz.cvut.fel.adaptivestructure.R;
import cz.cvut.fel.adaptivestructure.adaptation.AdaptationMaker;

public class PictureFragment extends Fragment {


    SurfaceView surfaceView;
    AdaptationMaker adaptationMaker;

    public PictureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adaptationMaker = AdaptationMaker.getAdaptationMaker();
        surfaceView = (SurfaceView) view.findViewById(R.id.surfaceViewPictures);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_picture, container, false);
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
