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

public class ArmorItemMaker extends AppCompatActivity {

    private boolean bCustomMaxDamage = false;
    private EditText etClassName, etDescriptionId, etArmorMaterial, etArmorRenderType, etArmorSlot, etCategory, etTexture, etMaxDamage;
    String armorItemHeaderPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_armoritem);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        armorItemHeaderPath = sharedPref.getString("armor_item_header_path", "com/mojang/minecraftpe/world/item/ArmorItem.h");

        //Defines
        etClassName = (EditText) findViewById(R.id.armorItemClassNameTxt);
        etDescriptionId = (EditText) findViewById(R.id.armorItemDescriptionIdTxt);
        etArmorMaterial = (EditText) findViewById(R.id.armorMaterialTxt);
        etArmorRenderType = (EditText) findViewById(R.id.armorRenderTypeTxt);
        etArmorSlot = (EditText) findViewById(R.id.armorSlotTxt);
        etCategory = (EditText) findViewById(R.id.armorItemCategoryTxt);
        etTexture = (EditText) findViewById(R.id.armorItemTextureTxt);
        etMaxDamage = (EditText) findViewById(R.id.armorItemMaxDamageTxt);
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.customMaxDamageCheck:
                if (checked) {
                    etMaxDamage.setVisibility(View.VISIBLE);
                    bCustomMaxDamage = true;
                } else {
                    etMaxDamage.setVisibility(View.GONE);
                    bCustomMaxDamage = false;
                }
                break;
        }
    }

    public void createClass(View view) {
        //Strings and Integers
        String classNameTxt = etClassName.getText().toString();
        String descriptionIdTxt = etDescriptionId.getText().toString();
        String armorMaterialTxt = etArmorMaterial.getText().toString();
        String armorRenderTypeTxt = etArmorRenderType.getText().toString();
        String armorSlotTxt = etArmorSlot.getText().toString();
        String categoryTxt = etCategory.getText().toString();
        String textureTxt = etTexture.getText().toString();
        String maxDamageTxt = "";
        int maxDamageInt;

        switch (armorSlotTxt) {
            case "1":
                armorSlotTxt = "HELMET";
                break;
            case "2":
                armorSlotTxt = "CHESTPLATE";
                break;
            case "3":
                armorSlotTxt = "LEGGINGS";
                break;
            case "4":
                armorSlotTxt = "BOOTS";
                break;
        }

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

        if (bCustomMaxDamage) {
            maxDamageInt = Integer.parseInt(etMaxDamage.getText().toString());
            maxDamageTxt = "\tsetMaxDamage(" + maxDamageInt + ");\n";
        }


        //Code
        File headerFile = new File(Utils.folderPath + classNameTxt + ".h");
        File sourceFile = new File(Utils.folderPath + classNameTxt + ".cpp");

        String [] headerFileString = String.valueOf("#pragma once\n\n" +
                "#include \"" + armorItemHeaderPath + "\"\n\n" +
                "class " + classNameTxt + " : public ArmorItem {\n" +
                "public:\n" +
                "\t" + classNameTxt + "(short itemId);\n" +
                "};\n").split(System.getProperty("line.separator"));

        String [] sourceFileString = String.valueOf("#include \"" + classNameTxt + ".h\"\n\n" +
            classNameTxt + "::" + classNameTxt + "(short itemId) : Item(\"" + descriptionIdTxt + "\", " + "itemId - 256, " + armorMaterialTxt + ", " + armorRenderTypeTxt + ", ArmorSlot::" + armorSlotTxt + ") {\n" +
            "\tItem::mItems[itemId] = this;\n" +
            "\tsetIcon(" + textureTxt + ");\n" +
            "\tsetCategory(CreativeItemCategory::" + categoryTxt + ");\n" +
            maxDamageTxt +
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
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}