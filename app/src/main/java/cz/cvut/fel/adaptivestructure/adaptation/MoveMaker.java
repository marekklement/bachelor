package cz.cvut.fel.adaptivestructure.adaptation;

import android.app.Activity;
import android.os.Build;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import androidx.annotation.RequiresApi;
import cz.cvut.fel.adaptivestructure.creation.StructureCreation;
import cz.cvut.fel.adaptivestructure.database.ASDatabase;
import cz.cvut.fel.adaptivestructure.database.DatabaseInit;
import cz.cvut.fel.adaptivestructure.entity.Node;
import cz.cvut.fel.adaptivestructure.entity.Structure;
import cz.cvut.fel.adaptivestructure.inflanter.DynamicLayoutInflator;
import cz.cvut.fel.adaptivestructure.properties.PropertyUtil;
import cz.cvut.fel.adaptivestructure.xml.XMLMaker;

/**
 * This class provides move to another page. This application needs to be dynamic build so this is its provider.
 *
 * @author Marek Klement
 */
public class MoveMaker {

    private static int id = 1;
    private static ASDatabase db;
    private static MoveMaker instance;
    public View nextView;
    public String currentViewName;
    public View currentView;

    /**
     * This is single instance class. Get instance by this method only!
     *
     * @return
     */
    public static MoveMaker getInstance() {
        if (instance == null) {
            instance = new MoveMaker();
        }
        return instance;
    }

    /**
     * Finds SurfaceView in given ViewGroup.
     *
     * @param view
     * @return
     */
    private static SurfaceView getSurfaceViewFromView(View view) {
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

    /**
     * Init point of this method from actual activity.
     *
     * @param activity
     * @param surfaceView
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void makeMove(Activity activity, SurfaceView surfaceView) {
        if (MoveMaker.getInstance().nextView == null) {
            AdaptationProvider ap = new AdaptationProvider(activity);
            ap.changeStructure();
            Structure structure = StructureCreation.getOrMakeStructure(activity);
            String mainPageName = PropertyUtil.getMainPageName(activity);
            List<String> mainPage = structure.getPages().get(mainPageName);
            if (mainPage == null) {
                throw new IllegalArgumentException("There should be at least one page with name " + mainPageName + "!");
            }
            View move = MoveMaker.getInstance().move(activity, mainPage, activity.getClass().getSimpleName(), mainPageName);
            ViewGroup vg = (ViewGroup) move;
            vg.addView(surfaceView);
            activity.setContentView(vg);
            MoveMaker.getInstance().currentView = move;
        }
    }

    /**
     * As this app needs to be dynamic build, this method is core for that functionality. It provides move to new page smoothly.
     *
     * @param context
     * @param buttons
     * @param className
     * @param viewName
     * @return
     */
    private View move(Activity context, List<String> buttons, String className, String viewName) {

        db = DatabaseInit.getASDatabase(context);
        if(currentViewName!=null){
            List<Node> parents = db.nodeDao().getByName(currentViewName);
            if(parents.size() != 1){
                throw new IllegalArgumentException("Have to be exactly one result!");
            }
            setTimeSession(parents.get(0));
        }
        int nextId = db.nodeDao().findHighestId();
        if (id == 1) {
            id = nextId + 1;
        } else if(nextId > id){
            id = nextId;
        }
        if (viewName != null) {
            currentViewName = viewName;
        } else {
            currentViewName = PropertyUtil.getMainPageName(context);
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
        View view;
        try {
            String s = XMLMaker.generateXML(currentViewName, className, buttons, context);
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

    private void setTimeSession(Node node) {
        LocalDateTime startVisit = node.getStartVisit();
        if(startVisit == null){
            throw new IllegalArgumentException("StartVisits are not set!");
        }
        Duration duration = Duration.between(startVisit, LocalDateTime.now());
        long seconds = duration.getSeconds();
        long visitationSession = node.getVisitationSession();
        long visits = node.getVisits() + 1;
        long newValue = (visitationSession + seconds) / visits;
        node.setVisitationSession(newValue);
        db.nodeDao().update(node);
    }

    /**
     * Each button needs to be initialized onCLick page change and this methods provides it for all buttons.
     *
     * @param view
     * @param context
     */
    private void setOnClickListeners(View view, Activity context) {
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
                                if (collect == null) {
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
                            }
                        }
                    });
                } else {
                    setOnClickListeners(childAt, context);
                }
            }
        }
    }

    /**
     * For dynamic app again this cannot be simple back. It needs to find previous location and switch it back.
     *
     * @param context
     */
    public void setBackClickListeners(Activity context) {
        List<Node> byName = db.nodeDao().getByName(currentViewName);
        if (byName.size() != 1) {
            throw new IllegalArgumentException("Should be just one!");
        }
        Node node = byName.get(0);
        long parent = node.getParent();
        if (parent != -1) {
            Node byId = db.nodeDao().getById(parent);
            View move = MoveMaker.getInstance().move(context, byId.getButtons(), this.getClass().getSimpleName(), byId.getName());
            ViewGroup vg = (ViewGroup) move;
            SurfaceView surfaceView = MoveMaker.getSurfaceViewFromView(context.getWindow().getDecorView().getRootView());
            ViewParent parentView = surfaceView.getParent();
            ViewGroup pv = (ViewGroup) parentView;
            pv.removeView(surfaceView);
            vg.addView(surfaceView);
            context.setContentView(vg);
        } else {
            context.finish();
            System.exit(0);
        }
    }
}
