package cz.cvut.fel.adaptivestructure.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import cz.cvut.fel.adaptivestructure.R;
import cz.cvut.fel.adaptivestructure.xml.XMLMaker;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewBeginFragment extends Fragment {

    private String FRAGMENT_NAME;

    public NewBeginFragment() {
        // Required empty public constructor
    }

    public String getName(){
        return FRAGMENT_NAME;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        List<String> buttons = new LinkedList<>();
        buttons.add("finances");
        buttons.add("pictures");
        try {
            XMLMaker.generateXML("testLayout", this.getClass().getName(), buttons);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_begin, container, false);
    }
}
