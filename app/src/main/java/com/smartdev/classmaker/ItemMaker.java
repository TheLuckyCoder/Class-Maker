package com.smartdev.classmaker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import java.io.File;

public class ItemMaker extends AppCompatActivity {

    private boolean bCustomMaxStackSize = false,
            bCustomAttackDamage = false,
            bStackedByData = false;
    private android.support.design.widget.TextInputEditText etClassName, etDescriptionId, etCategory, etTexture, etMaxStackSize, etAttackDamage;
    String itemHeaderPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        itemHeaderPath = sharedPref.getString("item_header_path", "minecraftpe/world/item/Item.h");

        //Defines
        etClassName = (android.support.design.widget.TextInputEditText) findViewById(R.id.itemClassNameTxt);
        etDescriptionId = (android.support.design.widget.TextInputEditText) findViewById(R.id.itemDescriptionIdTxt);
        etCategory = (android.support.design.widget.TextInputEditText) findViewById(R.id.itemCategoryTxt);
        etTexture = (android.support.design.widget.TextInputEditText) findViewById(R.id.itemTextureTxt);
        etMaxStackSize = (android.support.design.widget.TextInputEditText) findViewById(R.id.itemMaxStackSizeTxt);
        etAttackDamage = (android.support.design.widget.TextInputEditText) findViewById(R.id.itemAttackDamageTxt);
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.customStackSizeCheck:
                if (checked) {
                    etMaxStackSize.setVisibility(View.VISIBLE);
                    bCustomMaxStackSize = true;
                } else {
                    etMaxStackSize.setVisibility(View.GONE);
                    bCustomMaxStackSize = false;
                }
                break;
            case R.id.stackedByDataCheck:
                bStackedByData = checked;
                break;
            case R.id.customAttackDamageCheck:
                if (checked) {
                    etAttackDamage.setVisibility(View.VISIBLE);
                    bCustomAttackDamage = true;
                } else {
                    etAttackDamage.setVisibility(View.GONE);
                    bCustomAttackDamage = false;
                }
                break;
        }
    }

    public void createClass(View view) {
        //Strings and Integers
        String classNameTxt = etClassName.getText().toString();
        String descriptionIdTxt = etDescriptionId.getText().toString();
        String categoryTxt = etCategory.getText().toString();
        String textureTxt = etTexture.getText().toString();
        String maxStackSizeTxt = etMaxStackSize.getText().toString();
        String attackDamageTxt = etAttackDamage.getText().toString();
        String headerAttackDamageTxt = "";
        int maxStackSizeInt = 0;
        float attackDamageFloat = 0f;
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
        } else
            maxStackSizeTxt = "";

        if (bCustomAttackDamage) {
            if (!attackDamageTxt.matches(""))
                attackDamageFloat = Float.parseFloat(attackDamageTxt);

            attackDamageTxt = "\n" + classNameTxt + "::getAttackDamage() {\n" +
                    "\treturn " + attackDamageFloat + ";\n" +
                    "}\n";
            headerAttackDamageTxt = "\n\tvirtual float getAttackDamage();\n";
        } else
            attackDamageTxt = "";

        if (bStackedByData)
            stackedByDataTxt = "\tsetStackedByData(true);\n";


        //Code
        File headerFile = new File(Utils.folderPath + classNameTxt + ".h");
        File sourceFile = new File(Utils.folderPath + classNameTxt + ".cpp");

        String[] headerFileContent = String.valueOf("#pragma once\n\n" +
                "#include \"" + itemHeaderPath + "\"\n\n" +
                "class " + classNameTxt + " : public Item {\n" +
                "public:\n" +
                "\t" + classNameTxt + "(short itemId);\n" +
                headerAttackDamageTxt +
                "};\n").split(System.getProperty("line.separator"));

        String[] sourceFileContent = String.valueOf("#include \"" + classNameTxt + ".h\"\n\n" +
                classNameTxt + "::" + classNameTxt + "(short itemId) : Item(\"" + descriptionIdTxt + "\", " + "itemId - 256) {\n" +
                "\tItem::mItems[itemId] = this;\n" +
                "\tsetIcon(" + textureTxt + ");\n" +
                "\tsetCategory(CreativeItemCategory::" + categoryTxt + ");\n" +
                maxStackSizeTxt +
                stackedByDataTxt +
                "}\n" +
                attackDamageTxt).split(System.getProperty("line.separator"));

        if (!classNameTxt.matches("")) {
            Utils.Save(headerFile, headerFileContent);
            Utils.Save(sourceFile, sourceFileContent);
            Snackbar.make(view, R.string.class_successfully_generated, Snackbar.LENGTH_LONG).show();
        } else
            Snackbar.make(view, R.string.error_empty_mod_name, Snackbar.LENGTH_LONG).show();
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
