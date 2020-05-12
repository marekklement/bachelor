package cz.cvut.fel.adaptivestructure.creation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.RequiresApi;
import cz.cvut.fel.adaptivestructure.database.DatabaseInit;
import cz.cvut.fel.adaptivestructure.entity.AppInfo;
import cz.cvut.fel.adaptivestructure.entity.Structure;

/**
 * This method works for initialization of structure itself.
 *
 * @author Marek Klement
 */
public class StructureCreation {

    /**
     * This method is used to create application structure. As someone wants to test this app with its own structure, here is point where to initialize.
     * TODO: Follow steps bellow to create simply structure:
     * 1) Create new HashMap<String, List<String>> - first parameter will be name of the page and second its buttons.
     * 2) Create main page and its buttons - do not forget to put name of the main page to the config.properties
     * 3) Create other pages same way and them to HashMap. BEWARE! - all buttons have to have page with same name and name have to be unique
     * 4) Return HashMap
     *
     * @return
     */
    private static HashMap<String, List<String>> createStructure() {
        HashMap<String, List<String>> pairs = new HashMap<>();
        List<String> buttons = new LinkedList<>();
        buttons.add("Grafy");
        buttons.add("Zaplatit");
        buttons.add("Odměny");
        buttons.add("Menu");
        buttons.add("Pošta");
        buttons.add("Úročení");
        buttons.add("Běžný účet");
        buttons.add("Spořící účet");
        pairs.put("mainPage", buttons);
        //
        List<String> blaButtons = new LinkedList<>();
        blaButtons.add("Kategorie");
        blaButtons.add("Příjmy");
        pairs.put("Grafy", blaButtons);
        //
        List<String> bolButtons = new LinkedList<>();
        bolButtons.add("Druhy");
        bolButtons.add("Zapnuté");
        bolButtons.add("V okolí");
        bolButtons.add("E-shopy");
        bolButtons.add("Brzy končí");
        pairs.put("Odměny", bolButtons);
        //
        List<String> menuButtons = new LinkedList<>();
        menuButtons.add("Pošli mi peníze");
        menuButtons.add("Pravidelné platby");
        menuButtons.add("Inkasa a SIPO");
        menuButtons.add("Šablony");
        menuButtons.add("Účty v jiných bankách");
        menuButtons.add("Karty");
        menuButtons.add("Pojištění");
        menuButtons.add("Půjčky");
        menuButtons.add("Hypotéky");
        menuButtons.add("Kontakty");
        pairs.put("Menu", menuButtons);
        //
        pairs.put("Kategorie", new LinkedList<>());
        //
        pairs.put("Příjmy", new LinkedList<>());
        //
        pairs.put("Zaplatit", new LinkedList<>());
        //
        pairs.put("Druhy", new LinkedList<>());
        pairs.put("Zapnuté", new LinkedList<>());
        pairs.put("V okolí", new LinkedList<>());
        pairs.put("E-shopy", new LinkedList<>());
        pairs.put("Brzy končí", new LinkedList<>());
        //
        pairs.put("Pošli mi peníze", new LinkedList<>());
        //
        List<String> platbyButtons = new LinkedList<>();
        platbyButtons.add("Trvalé příkazy");
        platbyButtons.add("Spoření");
        pairs.put("Pravidelné platby", platbyButtons);
        //
        List<String> trvaleButtons = new LinkedList<>();
        trvaleButtons.add("Nový trvalý příkaz");
        pairs.put("Trvalé příkazy", trvaleButtons);
        //
        List<String> sporeniButtons = new LinkedList<>();
        sporeniButtons.add("Nové pravidelné spoření");
        pairs.put("Spoření", sporeniButtons);
        //
        pairs.put("Nový trvalý příkaz", new LinkedList<>());
        pairs.put("Nové pravidelné spoření", new LinkedList<>());
        //
        List<String> inkasaASIPOButtons = new LinkedList<>();
        inkasaASIPOButtons.add("Inkasa");
        inkasaASIPOButtons.add("SIPO");
        pairs.put("Inkasa a SIPO", inkasaASIPOButtons);
        //
        List<String> inkasaButtons = new LinkedList<>();
        inkasaButtons.add("Nové inkaso");
        pairs.put("Inkasa", inkasaButtons);
        //
        List<String> SIPOButtons = new LinkedList<>();
        SIPOButtons.add("Nové SIPO");
        pairs.put("SIPO", SIPOButtons);
        //
        pairs.put("Nové inkaso", new LinkedList<>());
        pairs.put("Nové SIPO", new LinkedList<>());
        //
        List<String> sablonyButtons = new LinkedList<>();
        sablonyButtons.add("Nová šablona");
        pairs.put("Šablony", sablonyButtons);
        //
        pairs.put("Nová šablona", new LinkedList<>());
        //
        pairs.put("Účty v jiných bankách", new LinkedList<>());
        //
        pairs.put("Karty", new LinkedList<>());
        //
        List<String> pojisteniButtons = new LinkedList<>();
        pojisteniButtons.add("Nové pojištění");
        pairs.put("Pojištění", pojisteniButtons);
        //
        pairs.put("Nové pojištění", new LinkedList<>());
        //
        pairs.put("Půjčky", new LinkedList<>());
        pairs.put("Hypotéky", new LinkedList<>());
        pairs.put("Kontakty", new LinkedList<>());
        //
        List<String> uroceniButtons = new LinkedList<>();
        uroceniButtons.add("Tento měsíc");
        uroceniButtons.add("Příští měsíc");
        pairs.put("Úročení", uroceniButtons);
        //
        pairs.put("Tento měsíc", new LinkedList<>());
        pairs.put("Příští měsíc", new LinkedList<>());
        //
        List<String> zpravyButtons = new LinkedList<>();
        zpravyButtons.add("Nová zpráva");
        pairs.put("Pošta", zpravyButtons);
        //
        pairs.put("Nová zpráva", new LinkedList<>());
        //
        pairs.put("Běžný účet", new LinkedList<>());
        pairs.put("Spořící účet", new LinkedList<>());
        return pairs;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Structure getOrMakeStructure(Context context) {
        Structure highestVersion = DatabaseInit.getASDatabase(context).structureDao().getHighestVersion();
        if (highestVersion == null) {
            getGenderAndAge(context);
            HashMap<String, List<String>> structure = createStructure();
            Structure struc = new Structure();
            struc.setVersion(1);
            struc.setPages(structure);
            DatabaseInit.getASDatabase(context).structureDao().insert(struc);
            return struc;
        }
        return highestVersion;
    }

    private static void getGenderAndAge(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Provide age and gender please!");

        // Set up the input
        final TextView ageText = new TextView(context);
        ageText.setText("Provide age please\n");
        final EditText input = new EditText(context);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        final TextView genderText = new TextView(context);
        genderText.setText("Provide gender please please\n");
        final Spinner dynamicSpinner = new Spinner(context);
        String[] items = new String[]{"Male", "Female"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, items);
        dynamicSpinner.setAdapter(adapter);

        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(ageText);
        ll.addView(input);
        ll.addView(genderText);
        ll.addView(dynamicSpinner);

        builder.setView(ll);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String age = input.getText().toString();
                String gender = (String) dynamicSpinner.getSelectedItem();
                int ageNumber = Integer.parseInt(age);
                AppInfo info = new AppInfo();
                info.setId(1);
                info.setAge(ageNumber);
                info.setGender(gender);
                DatabaseInit.getASDatabase(context).appInfoDao().insert(info);
            }
        });

        builder.show();
        System.out.println("Yes");
    }
}
