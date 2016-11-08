package net.theluckycoder.classmaker;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class ArmorItemMaker extends AppCompatActivity {

    private boolean bCustomMaxDamage = false;
    private EditText etClassName, etDescriptionId, etArmorMaterial, etArmorRenderType, etArmorSlot, etCategory, etTexture, etMaxDamage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_armoritem);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etClassName = (EditText) findViewById(R.id.armorItemClassNameTxt);
        etDescriptionId = (EditText) findViewById(R.id.armorItemDescriptionIdTxt);
        etArmorMaterial = (EditText) findViewById(R.id.armorMaterialTxt);
        etArmorRenderType = (EditText) findViewById(R.id.armorRenderTypeTxt);
        etArmorSlot = (EditText) findViewById(R.id.armorSlotTxt);
        etCategory = (EditText) findViewById(R.id.armorItemCategoryTxt);
        etTexture = (EditText) findViewById(R.id.armorItemTextureTxt);
        etMaxDamage = (EditText) findViewById(R.id.armorItemMaxDamageTxt);

        if (!Util.checkPermission(this)) Util.requestPermission(this);
        new File(Util.folderPath).mkdir();
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
        File headerFile = new File(Util.folderPath + classNameTxt + ".h");
        File sourceFile = new File(Util.folderPath + classNameTxt + ".cpp");

        String [] headerFileString = String.valueOf("#pragma once\n\n" +
                "#include \"minecraftpe/world/item/ArmorItem.h\"\n\n" +
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
            Util.save(headerFile, headerFileString);
            Util.save(sourceFile, sourceFileString);
            Toast.makeText(this, R.string.class_successfully_generated, Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, R.string.error_empty_mod_name, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case Util.PERMISSION_REQUEST_CODE:
                if (grantResults.length < 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), R.string.external_storage_permission_required, Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }
}
