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

public class ItemMaker extends AppCompatActivity {

    private boolean bCustomMaxStackSize = false,
            bCustomAttackDamage = false,
            bStackedByData = false;
    private EditText etClassName, etDescriptionId, etCategory, etTexture, etMaxStackSize, etAttackDamage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etClassName = (EditText) findViewById(R.id.itemClassNameTxt);
        etDescriptionId = (EditText) findViewById(R.id.itemDescriptionIdTxt);
        etCategory = (EditText) findViewById(R.id.itemCategoryTxt);
        etTexture = (EditText) findViewById(R.id.itemTextureTxt);
        etMaxStackSize = (EditText) findViewById(R.id.itemMaxStackSizeTxt);
        etAttackDamage = (EditText) findViewById(R.id.itemAttackDamageTxt);

        if (!Util.checkPermission(this)) Util.requestPermission(this);
        new File(Util.folderPath).mkdir();
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
        String classNameTxt = etClassName.getText().toString();
        String descriptionIdTxt = etDescriptionId.getText().toString();
        String categoryTxt = etCategory.getText().toString();
        String textureTxt = etTexture.getText().toString();
        String maxStackSizeTxt = etMaxStackSize.getText().toString();
        String attackDamageTxt = etAttackDamage.getText().toString();
        int maxStackSizeInt = 0;
        float attackDamageFloat = 0.0f;
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

            attackDamageTxt = "\tvirtual float getAttackDamage() { return " + attackDamageFloat + "; };\n";
        } else
            attackDamageTxt = "";

        if (bStackedByData)
            stackedByDataTxt = "\tsetStackedByData(true);\n";


        //Code
        File headerFile = new File(Util.folderPath + classNameTxt + ".h");
        File sourceFile = new File(Util.folderPath + classNameTxt + ".cpp");

        String[] headerFileContent = String.valueOf("#pragma once\n\n" +
                "#include \"minecraftpe/world/item/Item.h\"\n\n" +
                "class " + classNameTxt + " : public Item {\n" +
                "public:\n" +
                "\t" + classNameTxt + "(short itemId);\n" +
                attackDamageTxt +
                "};\n").split(System.getProperty("line.separator"));

        String[] sourceFileContent = String.valueOf("#include \"" + classNameTxt + ".h\"\n\n" +
                classNameTxt + "::" + classNameTxt + "(short itemId) : Item(\"" + descriptionIdTxt + "\", " + "itemId - 256) {\n" +
                "\tItem::mItems[itemId] = this;\n" +
                "\tsetIcon(" + textureTxt + ");\n" +
                "\tsetCategory(CreativeItemCategory::" + categoryTxt + ");\n" +
                maxStackSizeTxt +
                stackedByDataTxt +
                "}\n").split(System.getProperty("line.separator"));

        if (!classNameTxt.matches("")) {
            Util.save(headerFile, headerFileContent);
            Util.save(sourceFile, sourceFileContent);
            Toast.makeText(this, R.string.class_successfully_generated, Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, R.string.error_empty_mod_name, Toast.LENGTH_SHORT).show();
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
