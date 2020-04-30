package cz.cvut.fel.adaptivestructure.adaptation;

import android.app.Activity;
import android.content.Context;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import cz.cvut.fel.adaptivestructure.MainActivity;
import cz.cvut.fel.adaptivestructure.database.ASDatabase;
import cz.cvut.fel.adaptivestructure.database.DatabaseInit;
import cz.cvut.fel.adaptivestructure.entity.Node;
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
            currentViewName = "MainView";
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
            //setContentView(view);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        if (byId == null) {
            view.setId(id);
            id = id + 1;
        } else {
            view.setId(byId.getUid());
        }
        AdaptationMaker.getAdaptationMaker().createApplicationStructure(view, currentViewName, buttons);
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
                        @Override
                        public void onClick(View v) {
                            if (v instanceof Button) {
                                Button b = (Button) v;
                                View move = MoveMaker.getInstance().move(context, new LinkedList<>(), this.getClass().getSimpleName(), b.getText().toString());
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
            throw new IllegalArgumentException("Should be just one!");
        }
        Node node = byName.get(0);
        long parent = node.getParent();
        Node byId = db.nodeDao().getById(parent);
        View move = MoveMaker.getInstance().move(context, byId.getButtons(), this.getClass().getSimpleName(), byId.getName());
        ViewGroup vg = (ViewGroup) move;
        SurfaceView surfaceView = MoveMaker.getSurfaceViewFromView(context.getWindow().getDecorView().getRootView());
        ViewParent parentView = surfaceView.getParent();
        ViewGroup pv = (ViewGroup) parentView;
        pv.removeView(surfaceView);
        vg.addView(surfaceView);
        context.setContentView(vg);
        //context.surfaceView = MoveMaker.getSurfaceViewFromView(move);
    }

    public static void makeMove(Activity activity, SurfaceView surfaceView){
        if (MoveMaker.getInstance().nextView == null) {
            List<String> buttons = new LinkedList<>();
            buttons.add("bla");
            buttons.add("bol");
            View move = MoveMaker.getInstance().move(activity, buttons, activity.getClass().getSimpleName(), null);
            ViewGroup vg = (ViewGroup) move;
            vg.addView(surfaceView);
            activity.setContentView(vg);
            MoveMaker.getInstance().currentView = move;
            //surfaceView = MoveMaker.getSurfaceViewFromView(move);
        }
    }
}
