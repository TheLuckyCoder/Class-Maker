package com.smartdev.classmaker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.File;

public class ItemMaker extends AppCompatActivity {

    boolean bCustomMaxStackSize = false,
            bStackedByData = false;
    EditText etClassName, etDescriptionId, etCategory, etTexture, etMaxStackSize;
    String itemHeaderPath = "com/mojang/minecraftpe/world/item/Item.h";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        File dir = new File(Utils.path);
        dir.mkdir();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        itemHeaderPath = sharedPref.getString("itemHeaderPath", "com/mojang/minecraftpe/world/item/Item.h");

        //Defines
        etClassName = (EditText) findViewById(R.id.itemClassNameTxt);
        etDescriptionId = (EditText) findViewById(R.id.itemDescriptionIdTxt);
        etCategory = (EditText) findViewById(R.id.itemCategoryTxt);
        etTexture = (EditText) findViewById(R.id.itemTextureTxt);
        etMaxStackSize = (EditText) findViewById(R.id.itemMaxStackSizeTxt);
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
                    bCustomMaxStackSize = true;
                } else {
                    maxStackSize.setVisibility(View.GONE);
                    bCustomMaxStackSize = false;
                }
                break;
        }
    }

    public void createMod(View view) {
        //Strings and Integers
        String classNameTxt = etClassName.getText().toString();
        String descriptionIdTxt = etDescriptionId.getText().toString();
        String categoryTxt = etCategory.getText().toString();
        String textureTxt = etTexture.getText().toString();
        String maxStackSizeTxt = etMaxStackSize.getText().toString();
        int maxStackSizeInt = 64;
        String stackedByDataTxt = "";

        switch (categoryTxt) {
            case "1":
                categoryTxt = "BLOCKS";
                break;
            case "2":
                categoryTxt = "DECORATIONS";
                break;
            case "3":
                categoryTxt = "TOOLS";
                break;
            case "4":
                categoryTxt = "ITEMS";
                break;
        }

        if (bCustomMaxStackSize) {
            if (!maxStackSizeTxt.matches(""))
                maxStackSizeInt = Integer.parseInt(maxStackSizeTxt);

            if (maxStackSizeInt > 64)
                maxStackSizeInt = 64;
            else if (maxStackSizeInt == 0)
                maxStackSizeInt = 1;

            maxStackSizeTxt = "\tsetMaxStackSize(" + maxStackSizeInt + ");\n";
        } else {
            maxStackSizeTxt = "";
        }

        if (bStackedByData)
            stackedByDataTxt = "\tsetStackedByData(true);\n";


        //Code
        File headerFile = new File(Utils.path + classNameTxt + ".h");
        File sourceFile = new File(Utils.path + classNameTxt + ".cpp");

        String[] headerFileString = String.valueOf("#pragma once\n\n" +
                "#include \"" + itemHeaderPath + "\"\n\n" +
                "class " + classNameTxt + " : public Item {\n" +
                "public:\n" +
                "\t" + classNameTxt + "(short itemId);\n" +
                "};\n").split(System.getProperty("line.separator"));

        String[] sourceFileString = String.valueOf("#include \"" + classNameTxt + ".h\"\n\n" +
                classNameTxt + "::" + classNameTxt + "(short itemId) : Item(\"" + descriptionIdTxt + "\", " + "itemId - 256) {\n" +
                "\tItem::mItems[itemId] = this;\n" +
                "\tsetIcon(" + textureTxt + ");\n" +
                "\tsetCategory(CreativeItemCategory::" + categoryTxt + ");\n" +
                maxStackSizeTxt +
                stackedByDataTxt +
                "}\n").split(System.getProperty("line.separator"));

        if (!classNameTxt.matches("")) {
            Utils.Save(headerFile, headerFileString);
            Utils.Save(sourceFile, sourceFileString);
            Snackbar.make(view, R.string.class_successfully_generated, Snackbar.LENGTH_LONG).show();
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
