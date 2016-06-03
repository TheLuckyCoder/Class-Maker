package net.darkcoder.classmaker;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.razvanmcrafter.modmaker.pro.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class ItemActivity extends AppCompatActivity{

    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ModMaker/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        File dir = new File(path);
        dir.mkdirs();
    }

    public void createModClick (View view) {
        //Defines
        EditText itemName = (EditText) findViewById(R.id.txtItemName);
        EditText itemDescription = (EditText) findViewById(R.id.txtItemDescriptionId);
        EditText itemCategory = (EditText) findViewById(R.id.txtItemCategory);
        EditText itemTexture = (EditText) findViewById(R.id.txtItemTexture);
        EditText itemMaxStackSize = (EditText) findViewById(R.id.txtItemStackSize);

        //Asserts
        assert itemName != null;
        assert itemDescription != null;
        assert itemCategory != null;
        assert itemTexture != null;
        assert itemMaxStackSize != null;

        //Strings
        String itemNameTxt = itemName.getText().toString();

        //Code
        File itemHeader = new File (path + (itemName + ".h"));
        File itemSource = new File (path + (itemName + ".cpp"));

        String [] itemHeaderFile = String.valueOf("#pragma once\n" +
                "#include \"minecraftpe/world/item/Item.h\"\n" + //I can't write #include "minecraftpe/world/item/Item.h"
                "class " + itemNameTxt + " : public Item {\n" +
                "public:\n" +
                "   " + itemNameTxt + "(short);\n" +
                "};")
                .split(System.getProperty("line.separator"));
        Save(itemHeader, itemHeaderFile);

        String [] itemSourceFile = String.valueOf("#include " + itemNameTxt + "\n" +
                itemNameTxt + "::" + itemNameTxt + "(short itemId) : Item('" + itemDescription.getText() + "', " + "itemId - 0x100) {\n" +
                "   Item::mItems[itemId] = this;\n" +
                "   creativeCategory = CreativeCategory::" + itemCategory.getText() + ";\n" +
                "   setMaxStackSize(" + itemMaxStackSize.getText() + ");\n" +
                "}")
                .split(System.getProperty("line.separator"));
        Save(itemSource, itemSourceFile);
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
