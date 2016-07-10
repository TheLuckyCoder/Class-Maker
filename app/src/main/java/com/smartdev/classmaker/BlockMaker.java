package com.smartdev.classmaker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.File;

public class BlockMaker extends AppCompatActivity {

    private EditText etClassName, etDescriptionId, etCategory, etMaterial, etDestroyTime;
    String blockHeaderPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        blockHeaderPath = sharedPref.getString("block_header_path", "com/mojang/minecraftpe/world/level/block/Block.h");

        //Defines
        etClassName = (EditText) findViewById(R.id.blockClassNameTxt);
        etDescriptionId = (EditText) findViewById(R.id.blockDescriptionIdTxt);
        etCategory = (EditText) findViewById(R.id.blockCategoryTxt);
        etMaterial = (EditText) findViewById(R.id.blockMaterialTxt);
        etDestroyTime = (EditText) findViewById(R.id.blockDestroyTimeTxt);
    }

    public void createMod (View view) {
        //Strings and Integers
        String classNameTxt = etClassName.getText().toString();
        String descriptionIdTxt = etDescriptionId.getText().toString();
        String categoryTxt = etCategory.getText().toString();
        String materialTxt = etMaterial.getText().toString();
        String destroyTimeTxt = etDestroyTime.getText().toString();

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


        //Code
        File headerFile = new File (Utils.folderPath + classNameTxt + ".h");
        File sourceFile = new File (Utils.folderPath + classNameTxt + ".cpp");

        String [] headerFileString = String.valueOf("#pragma once\n\n" +
                "#include \"" + blockHeaderPath + "\"\n\n" +
                "class " + classNameTxt + " : public Block {\n" +
                "public:\n" +
                "\t" + classNameTxt + "(int blockId);\n" +
                "};\n").split(System.getProperty("line.separator"));

        String [] sourceFileString = String.valueOf("#include \"" + classNameTxt + ".h\"\n\n" +
                "#include \"com/mojang/minecraftpe/CreativeItemCategory.h\"\n\n" +
                classNameTxt + "::" + classNameTxt + "(int blockId) : Block(\"" + descriptionIdTxt + "\", " + "blockId, Material::getMaterial(MaterialType::" + materialTxt + ")) {\n" +
                "\tBlock::mBlocks[blockId] = this;\n" +
                "\tsetCategory(CreativeItemCategory::" + categoryTxt + ");\n" +
                "\tsetDestroyTime(" + destroyTimeTxt + "F);\n" +
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
