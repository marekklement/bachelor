package cz.cvut.fel.adaptivestructure;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import cz.cvut.fel.adaptivestructure.inflanter.DynamicLayoutInflator;
import cz.cvut.fel.adaptivestructure.xml.XMLMaker;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        List<String> buttons = new LinkedList<>();
        buttons.add("bla");
        buttons.add("bol");
        try {
            String s = XMLMaker.generateXML("testLayout", "NewBeginFragment", buttons);
            View view = DynamicLayoutInflator.inflateName(this, "testLayout");
            setContentView(view);
            String str = "";
        } catch (IOException e) {
            //throw new IllegalArgumentException(e);
            //e.printStackTrace();
        }
    }


}
