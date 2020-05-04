package cz.cvut.fel.adaptivestructure.adaptation;

import android.app.Activity;
import android.os.Build;
import android.util.Pair;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.RequiresApi;
import cz.cvut.fel.adaptivestructure.creation.StructureCreation;
import cz.cvut.fel.adaptivestructure.database.ASDatabase;
import cz.cvut.fel.adaptivestructure.database.DatabaseInit;
import cz.cvut.fel.adaptivestructure.entity.Node;
import cz.cvut.fel.adaptivestructure.entity.Structure;
import cz.cvut.fel.adaptivestructure.inflanter.DynamicLayoutInflator;
import cz.cvut.fel.adaptivestructure.xml.XMLMaker;

public class MoveMaker {

    private static int id = 1;
    private static ASDatabase db;
    private static MoveMaker instance;
    public View nextView;
    public String currentViewName;
    public View currentView;

    public static MoveMaker getInstance() {
        if (instance == null) {
            instance = new MoveMaker();
        }
        return instance;
    }

    public static SurfaceView getSurfaceViewFromView(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View childAt = vg.getChildAt(i);
                if (childAt instanceof SurfaceView) {
                    return (SurfaceView) childAt;
                } else {
                    SurfaceView surfaceViewFromView = getSurfaceViewFromView(childAt);
                    if (surfaceViewFromView != null) {
                        return surfaceViewFromView;
                    }
                }
            }
        }
        return null;
    }

    public View move(Activity context, List<String> buttons, String className, String viewName) {
        db = DatabaseInit.getASDatabase(context);
        if (viewName != null) {
            currentViewName = viewName;
        } else {
            currentViewName = "mainPage";
        }
        List<Node> byIds = db.nodeDao().getByName(currentViewName);
        Node byId;
        if (byIds.size() > 1) {
            throw new IllegalArgumentException("Must be just one Node by name!");
        } else if (byIds.size() == 1) {
            byId = byIds.get(0);
        } else {
            byId = null;
        }
        View view = null;
        try {
            String s = XMLMaker.generateXML(currentViewName, className, buttons);
            view = DynamicLayoutInflator.inflateName(context, currentViewName);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        if (byId == null) {
            view.setId(id);
            id = id + 1;
        } else {
            view.setId(byId.getUid());
        }
        AdaptationPrepare.getAdaptationMaker().createApplicationStructure(view, currentViewName, buttons);
        nextView = view;
        MoveMaker.getInstance().setOnClickListeners(view, context);
        return view;
    }

    public void setOnClickListeners(View view, Activity context) {
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View childAt = vg.getChildAt(i);
                if (childAt instanceof Button) {
                    Button button = (Button) childAt;
                    button.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onClick(View v) {
                            if (v instanceof Button) {
                                Button b = (Button) v;
                                List<String> collect = StructureCreation.getOrMakeStructure(context).getPages().get(b.getText().toString());
                                //List<Pair<String, List<String>>> collect = StructureCreation.getOrMakeStructure(context).getPages().stream().filter(l -> l.first.equals(b.getText().toString())).collect(Collectors.toList());
                                if(collect == null){
                                    throw new IllegalArgumentException("Page with name '" + b.getText().toString() + "' does not exist.");
                                }
                                View move = MoveMaker.getInstance().move(context, collect, this.getClass().getSimpleName(), b.getText().toString());
                                ViewGroup vg = (ViewGroup) move;
                                SurfaceView surfaceView = MoveMaker.getSurfaceViewFromView(context.getWindow().getDecorView().getRootView());
                                ViewParent parent = surfaceView.getParent();
                                ViewGroup pv = (ViewGroup) parent;
                                pv.removeView(surfaceView);
                                vg.addView(surfaceView);
                                context.setContentView(vg);
                                //context.surfaceView = MoveMaker.getSurfaceViewFromView(move);
                            }
                        }
                    });
                } else {
                    setOnClickListeners(childAt, context);
                }
            }
        }
    }

    public void setBackClickListeners(Activity context) {
        List<Node> byName = db.nodeDao().getByName(currentViewName);
        if (byName.size() != 1) {
            // todo kdyz nejsou na dane strance emoce pak se nevytvori uzel
            throw new IllegalArgumentException("Should be just one!");
        }
        Node node = byName.get(0);
        long parent = node.getParent();
        if(parent != -1) {
            Node byId = db.nodeDao().getById(parent);
            View move = MoveMaker.getInstance().move(context, byId.getButtons(), this.getClass().getSimpleName(), byId.getName());
            ViewGroup vg = (ViewGroup) move;
            SurfaceView surfaceView = MoveMaker.getSurfaceViewFromView(context.getWindow().getDecorView().getRootView());
            ViewParent parentView = surfaceView.getParent();
            ViewGroup pv = (ViewGroup) parentView;
            pv.removeView(surfaceView);
            vg.addView(surfaceView);
            context.setContentView(vg);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void makeMove(Activity activity, SurfaceView surfaceView){
        if (MoveMaker.getInstance().nextView == null) {
            AdaptationProvider ap = new AdaptationProvider(activity);
            ap.changeStructure();
            Structure structure = StructureCreation.getOrMakeStructure(activity);
            List<String> mainPage = structure.getPages().get("mainPage");
            //List<Pair<String, List<String>>> mainPage = structure.getPages().stream().filter(l -> l.first.equals("mainPage")).collect(Collectors.toList());
            if(mainPage == null){
                throw new IllegalArgumentException("There should be at least one page with name mainPage!");
            }
            View move = MoveMaker.getInstance().move(activity, mainPage, activity.getClass().getSimpleName(),  "mainPage");
            ViewGroup vg = (ViewGroup) move;
            vg.addView(surfaceView);
            activity.setContentView(vg);
            MoveMaker.getInstance().currentView = move;
        }
    }
}
