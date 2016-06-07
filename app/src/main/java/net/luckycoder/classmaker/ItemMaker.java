package net.luckycoder.classmaker;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ItemMaker extends AppCompatActivity{

    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ClassMaker/";
    boolean customMaxStackSize = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        File dir = new File(path);
        dir.mkdirs();
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        EditText maxStackSize = (EditText) findViewById(R.id.txtItemMaxStackSize);
        assert maxStackSize != null;

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkCustomStackSize:
                if (checked) {
                    maxStackSize.setVisibility(View.VISIBLE);
                    customMaxStackSize = true;
                } else {
                    maxStackSize.setVisibility(View.GONE);
                    customMaxStackSize = false;
                }
                break;
        }
    }

    public void createMod (View view) {
        //Defines
        EditText className = (EditText) findViewById(R.id.txtClassName);
        EditText descriptionId = (EditText) findViewById(R.id.txtItemDescriptionId);
        EditText category = (EditText) findViewById(R.id.txtItemCategory);
        EditText texture = (EditText) findViewById(R.id.txtItemTexture);
        EditText maxStackSize = (EditText) findViewById(R.id.txtItemMaxStackSize);

        //Asserts
        assert className != null;
        assert descriptionId != null;
        assert category != null;
        assert texture != null;
        assert maxStackSize != null;

        //Strings and Integers
        String classNameTxt = className.getText().toString();
        String descriptionIdTxt = descriptionId.getText().toString();
        String categoryTxt = category.getText().toString();
        String textureTxt = texture.getText().toString();
        int maxStackSizeTxt = Integer.parseInt(maxStackSize.getText().toString());

        if (categoryTxt.matches("1"))
            categoryTxt = "BLOCKS";
        else if (categoryTxt.matches("2"))
            categoryTxt = "DECORATIONS";
        else if (categoryTxt.matches("3"))
            categoryTxt = "TOOLS";
        else if (categoryTxt.matches("4"))
            categoryTxt = "ITEMS";

        if(maxStackSizeTxt > 64)
            maxStackSizeTxt = 64;
        if(maxStackSizeTxt < 1)
            maxStackSizeTxt = 1;

        //Code
        File headerFile = new File (path + (classNameTxt + ".h"));
        File sourceFile = new File (path + (classNameTxt + ".cpp"));

        String [] headerFileString = String.valueOf("#pragma once\n\n" +
                "#include \"com/mojang/minecraftpe/world/item/Item.h\"\n\n" +
                "class " + classNameTxt + " : public Item {\n" +
                "public:\n" +
                "\t" + classNameTxt + "(short itemId);\n" +
                "};")
                .split(System.getProperty("line.separator"));
        Save(headerFile, headerFileString);

        if (customMaxStackSize) {
            String[] sourceFileString = String.valueOf("#include \"" + classNameTxt + ".h\"\n\n" +
                    classNameTxt + "::" + classNameTxt + "(short itemId) : Item(\"" + descriptionIdTxt + "\", " + "itemId - 0x100) {\n" +
                    "\tItem::mItems[itemId] = this;\n" +
                    "\tcreativeCategory = CreativeItemCategory::" + categoryTxt + ";\n" +
                    "\tsetIcon(\"" + textureTxt + "\", 0);\n" +
                    "\tsetMaxStackSize(" + maxStackSizeTxt + ");\n" +
                    "}").split(System.getProperty("line.separator"));
            Save(sourceFile, sourceFileString);
        } else {
            String[] sourceFileString = String.valueOf("#include \"" + classNameTxt + ".h\"\n\n" +
                    classNameTxt + "::" + classNameTxt + "(short itemId) : Item(\"" + descriptionIdTxt + "\", " + "itemId - 0x100) {\n" +
                    "\tItem::mItems[itemId] = this;\n" +
                    "\tcreativeCategory = CreativeItemCategory::" + categoryTxt + ";\n" +
                    "\tsetIcon(\"" + textureTxt + "\", 0);\n" +
                    "}").split(System.getProperty("line.separator"));
            Save(sourceFile, sourceFileString);
        }

        Toast.makeText(getApplicationContext(), (classNameTxt + ".cpp and " + classNameTxt + ".h" + R.string.successfully_generated), Toast.LENGTH_SHORT).show();
    }

    public static void Save(File file, String[] data) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        try {
            try {
                for (int i = 0; i<data.length; i++) {
                    if (fos != null) {
                        fos.write(data[i].getBytes());
                    }
                    if (i < data.length-1)
                    {
                        if (fos != null) {
                            fos.write("\n".getBytes());
                        }
                    }
                }
            }
            catch (IOException e) {e.printStackTrace();}
        }
        finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            }
            catch (IOException e) {e.printStackTrace();}
        }
    }
}
