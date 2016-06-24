package com.smartdev.classmaker;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.smartdev.classmaker.utils.Utils;

import java.io.File;

public class ItemMaker extends AppCompatActivity {

    boolean customMaxStackSize = false;
    boolean stackedByData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        File dir = new File(Utils.path);
        dir.mkdirs();
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        EditText maxStackSize = (EditText) findViewById(R.id.itemMaxStackSizeTxt);
        assert maxStackSize != null;

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.customStackSizeCheck:
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

    public void createMod(View view) {
        //Defines
        EditText className = (EditText) findViewById(R.id.itemClassNameTxt);
        EditText descriptionId = (EditText) findViewById(R.id.itemDescriptionIdTxt);
        EditText category = (EditText) findViewById(R.id.itemCategoryTxt);
        EditText texture = (EditText) findViewById(R.id.itemTextureTxt);
        EditText maxStackSize = (EditText) findViewById(R.id.itemMaxStackSizeTxt);

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
        String maxStackSizeTxt = maxStackSize.getText().toString();
        int maxStackSizeInt = 64;
        String stackedByDataTxt = "";

        if (categoryTxt.matches("1"))
            categoryTxt = "BLOCKS";
        else if (categoryTxt.matches("2"))
            categoryTxt = "DECORATIONS";
        else if (categoryTxt.matches("3"))
            categoryTxt = "TOOLS";
        else if (categoryTxt.matches("4"))
            categoryTxt = "ITEMS";

        if (!maxStackSizeTxt.matches(""))
            maxStackSizeInt = Integer.parseInt(maxStackSizeTxt);

        if (customMaxStackSize) {
            if (maxStackSizeInt > 64)
                maxStackSizeInt = 64;
            if (maxStackSizeInt == 0)
                maxStackSizeInt = 1;
        }

        if (stackedByData)
            stackedByDataTxt = "\tsetStackedByData(true);\n";


        //Code
        File headerFile = new File(Utils.path + (classNameTxt + ".h"));
        File sourceFile = new File(Utils.path + (classNameTxt + ".cpp"));

        String[] headerFileString = String.valueOf("#pragma once\n\n" +
                "#include \"com/mojang/minecraftpe/world/item/Item.h\"\n\n" +
                "class " + classNameTxt + " : public Item {\n" +
                "public:\n" +
                "\t" + classNameTxt + "(short itemId);\n" +
                "};").split(System.getProperty("line.separator"));

        String[] sourceFileString = String.valueOf("#include \"" + classNameTxt + ".h\"\n\n" +
                classNameTxt + "::" + classNameTxt + "(short itemId) : Item(\"" + descriptionIdTxt + "\", " + "itemId - 0x100) {\n" +
                "\tItem::mItems[itemId] = this;\n" +
                "\tcreativeCategory = CreativeItemCategory::" + categoryTxt + ";\n" +
                "\tsetIcon(" + textureTxt + ");\n" +
                "\tsetMaxStackSize(" + maxStackSizeInt + ");\n" +
                stackedByDataTxt +
                "}").split(System.getProperty("line.separator"));

        if (!classNameTxt.matches("")) {
            Utils.Save(headerFile, headerFileString);
            Utils.Save(sourceFile, sourceFileString);
            Snackbar.make(view, (classNameTxt + R.string.class_successfully_generated), Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(view, R.string.error_empty_mod_name, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
