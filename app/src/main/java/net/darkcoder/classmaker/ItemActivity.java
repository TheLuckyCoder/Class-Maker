package net.darkcoder.classmaker;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ItemActivity extends AppCompatActivity{

    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ClassMaker/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        File dir = new File(path);
        dir.mkdirs();
    }

    public void createMod (View view) {
        //Defines
        EditText className = (EditText) findViewById(R.id.txtClassName);
        EditText itemDescription = (EditText) findViewById(R.id.txtItemDescriptionId);
        EditText itemCategory = (EditText) findViewById(R.id.txtItemCategory);
        EditText itemTexture = (EditText) findViewById(R.id.txtItemTexture);
        EditText itemMaxStackSize = (EditText) findViewById(R.id.txtItemMaxStackSize);

        //Asserts
        assert className != null;
        assert itemDescription != null;
        assert itemCategory != null;
        assert itemTexture != null;
        assert itemMaxStackSize != null;

        //Strings
        String classNameTxt = className.getText().toString();
        String itemDescriptionTxt = itemDescription.getText().toString();
        String itemCategoryTxt = itemCategory.getText().toString();
        String itemTextureTxt = itemTexture.getText().toString();
        String itemMaxStackSizeTxt = itemMaxStackSize.getText().toString();
        if(itemCategoryTxt.matches("1"))
            itemCategoryTxt = "BLOCKS";
        else if (itemCategoryTxt.matches("2"))
            itemCategoryTxt = "DECORATIONS";
        else if (itemCategoryTxt.matches("3"))
            itemCategoryTxt = "TOOLS";
        else if (itemCategoryTxt.matches("4"))
            itemCategoryTxt = "ITEMS";

        //Code
        File itemHeaderFile = new File (path + (classNameTxt + ".h"));
        File itemSourceFile = new File (path + (classNameTxt + ".cpp"));

        String [] itemHeaderString = String.valueOf("#pragma once\n" +
                "\n" +
                "#include \"com/mojang/minecraftpe/world/item/Item.h\"\n" +
                "\n" +
                "class " + classNameTxt + " : public Item {\n" +
                "public:\n" +
                "\t" + classNameTxt + "(short itemId);\n" +
                "};")
                .split(System.getProperty("line.separator"));
        Save(itemHeaderFile, itemHeaderString);

        String [] itemSourceString = String.valueOf("#include \"" + classNameTxt + ".h\"\n" +
                "\n" +
                classNameTxt + "::" + classNameTxt + "(short itemId) : Item(\"" + itemDescriptionTxt + "\", " + "itemId - 0x100) {\n" +
                "\tItem::mItems[itemId] = this;\n" +
                "\tcreativeCategory = CreativeCategory::" + itemCategoryTxt + ";\n" +
                "\tsetIcon(\"" + itemTextureTxt + "\", 0);\n" +
                "\tsetMaxStackSize(" + itemMaxStackSizeTxt + ");\n" +
                "}")
                .split(System.getProperty("line.separator"));
        Save(itemSourceFile, itemSourceString);

        Toast.makeText(getApplicationContext(), (classNameTxt + R.string.class_generated), Toast.LENGTH_SHORT).show();
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
