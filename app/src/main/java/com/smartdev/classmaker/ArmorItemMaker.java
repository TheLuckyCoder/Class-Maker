package com.smartdev.classmaker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.smartdev.classmaker.utils.Utils;

import java.io.File;

public class ArmorItemMaker extends AppCompatActivity {

    boolean customMaxDamage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_armoritem);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        File dir = new File(Utils.path);
        dir.mkdirs();
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        EditText maxDamage = (EditText) findViewById(R.id.txtItemMaxDamage);
        assert maxDamage != null;

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkCustomMaxDamage:
                if (checked) {
                    maxDamage.setVisibility(View.VISIBLE);
                    customMaxDamage = true;
                } else {
                    maxDamage.setVisibility(View.GONE);
                    customMaxDamage = false;
                }
                break;
        }
    }

    public void createMod(View view) {
        //Defines
        EditText className = (EditText) findViewById(R.id.txtClassName);
        EditText descriptionId = (EditText) findViewById(R.id.txtItemDescriptionId);
        EditText armorMaterial = (EditText) findViewById(R.id.txtArmorItemMaterial);
        EditText armorRenderType = (EditText) findViewById(R.id.txtArmorItemRenderType);
        EditText armorSlot = (EditText) findViewById(R.id.txtArmorItemSlot);
        EditText category = (EditText) findViewById(R.id.txtItemCategory);
        EditText texture = (EditText) findViewById(R.id.txtItemTexture);
        EditText maxDamage = (EditText) findViewById(R.id.txtItemMaxStackSize);


        //Asserts
        assert className != null;
        assert descriptionId != null;
        assert armorMaterial != null;
        assert armorRenderType != null;
        assert armorSlot != null;
        assert category != null;
        assert texture != null;
        assert maxDamage != null;


        //Strings and Integers
        String classNameTxt = className.getText().toString();
        String descriptionIdTxt = descriptionId.getText().toString();
        String armorMaterialTxt = armorMaterial.getText().toString();
        String armorRenderTypeTxt = armorRenderType.getText().toString();
        String armorSlotTxt = armorSlot.getText().toString();
        String categoryTxt = category.getText().toString();
        String textureTxt = texture.getText().toString();
        String maxDamageTxt = "";
        int maxDamageInt;

        if (armorSlotTxt.matches("1"))
            armorSlotTxt = "HELMET";
        else if (armorSlotTxt.matches("2"))
            armorSlotTxt = "CHESTPLATE";
        else if (armorSlotTxt.matches("3"))
            armorSlotTxt = "LEGGINGS";
        else if (armorSlotTxt.matches("4"))
            armorSlotTxt = "BOOTS";

        if (categoryTxt.matches("1"))
            categoryTxt = "BLOCKS";
        else if (categoryTxt.matches("2"))
            categoryTxt = "DECORATIONS";
        else if (categoryTxt.matches("3"))
            categoryTxt = "TOOLS";
        else if (categoryTxt.matches("4"))
            categoryTxt = "ITEMS";

        if (customMaxDamage) {
            maxDamageInt = Integer.parseInt(maxDamage.getText().toString());
            maxDamageTxt = "\tsetMaxDamage(" + maxDamageInt + ");\n";
        }


        //Code
        File headerFile = new File(Utils.path + (classNameTxt + ".h"));
        File sourceFile = new File(Utils.path + (classNameTxt + ".cpp"));

        String [] headerFileString = String.valueOf("#pragma once\n\n" +
                "#include \"com/mojang/minecraftpe/world/item/ArmorItem.h\"\n\n" +
                "class " + classNameTxt + " : public ArmorItem {\n" +
                "public:\n" +
                "\t" + classNameTxt + "(short itemId);\n" +
                "};").split(System.getProperty("line.separator"));

        String [] sourceFileString = String.valueOf("#include \"" + classNameTxt + ".h\"\n\n" +
            classNameTxt + "::" + classNameTxt + "(short itemId) : Item(\"" + descriptionIdTxt + "\", " + "itemId - 0x100, " + armorMaterialTxt + ", " + armorRenderTypeTxt + ", ArmorSlot::" + armorSlotTxt + ") {\n" +
            "\tItem::mItems[itemId] = this;\n" +
            "\tcreativeCategory = CreativeItemCategory::" + categoryTxt + ";\n" +
            "\tsetIcon(\"" + textureTxt + "\", 0);\n" +
            maxDamageTxt +
            "}").split(System.getProperty("line.separator"));

        if (!classNameTxt.matches("")) {
            Utils.Save(headerFile, headerFileString);
            Utils.Save(sourceFile, sourceFileString);
            Toast.makeText(getApplicationContext(), (classNameTxt + ".cpp" + R.string.and + classNameTxt + ".h" + R.string.successfully_generated), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), R.string.error_empty_mod_name, Toast.LENGTH_SHORT).show();
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