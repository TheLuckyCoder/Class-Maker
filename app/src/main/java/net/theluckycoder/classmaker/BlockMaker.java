package net.theluckycoder.classmaker;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class BlockMaker extends AppCompatActivity {

    private EditText etClassName, etDescriptionId, etCategory, etMaterial, etDestroyTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etClassName = (EditText) findViewById(R.id.blockClassNameTxt);
        etDescriptionId = (EditText) findViewById(R.id.blockDescriptionIdTxt);
        etCategory = (EditText) findViewById(R.id.blockCategoryTxt);
        etMaterial = (EditText) findViewById(R.id.blockMaterialTxt);
        etDestroyTime = (EditText) findViewById(R.id.blockDestroyTimeTxt);

        if (!Util.checkPermission(this)) Util.requestPermission(this);
        new File(Util.folderPath).mkdir();
    }

    public void createClass(View view) {
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
        File headerFile = new File (Util.folderPath + classNameTxt + ".h");
        File sourceFile = new File (Util.folderPath + classNameTxt + ".cpp");

        String [] headerFileString = String.valueOf("#pragma once\n\n" +
                "#include \"minecraftpe/world/level/block/Block.h\"\n\n" +
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
