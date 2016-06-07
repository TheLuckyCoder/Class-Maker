package net.luckycoder.classmaker;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BlockMaker extends AppCompatActivity{

    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ClassMaker/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);

        File dir = new File(path);
        dir.mkdirs();
    }

    public void createMod (View view) {
        //Defines
        EditText className = (EditText) findViewById(R.id.txtClassName);
        EditText descriptionId = (EditText) findViewById(R.id.txtBlockDescriptionId);
        EditText category = (EditText) findViewById(R.id.txtBlockCategory);
        EditText material = (EditText) findViewById(R.id.txtBlockMaterial);
        EditText destroyTime = (EditText) findViewById(R.id.txtBlockDestroyTime);


        //Asserts
        assert className != null;
        assert descriptionId != null;
        assert category != null;
        assert material != null;
        assert destroyTime != null;

        //Strings and Integers
        String classNameTxt = className.getText().toString();
        String descriptionIdTxt = className.getText().toString();
        String categoryTxt = category.getText().toString();
        String materialTxt = material.getText().toString();
        String destroyTimeTxt = destroyTime.getText().toString();

        if (categoryTxt.matches("1"))
            categoryTxt = "BLOCKS";
        else if (categoryTxt.matches("2"))
            categoryTxt = "DECORATIONS";
        else if (categoryTxt.matches("3"))
            categoryTxt = "TOOLS";
        else if (categoryTxt.matches("4"))
            categoryTxt = "ITEMS";


        //Code
        File headerFile = new File (path + (classNameTxt + ".h"));
        File sourceFile = new File (path + (classNameTxt + ".cpp"));

        String [] headerFileString = String.valueOf("#pragma once\n\n" +
                "#include \"com/mojang/minecraftpe/world/level/block/Block.h\"\n\n" +
                "class " + classNameTxt + " : public Block {\n" +
                "public:\n" +
                "\t" + classNameTxt + "(int blockId);\n" +
                "};")
                .split(System.getProperty("line.separator"));
        Save(headerFile, headerFileString);

        String [] sourceFileString = String.valueOf("#include \"" + classNameTxt + ".h\"\n\n" +
                "#include \"com/mojang/minecraftpe/CreativeItemCategory.h\"\n\n" +
                classNameTxt + "::" + classNameTxt + "(int blockId) : Block(\"" + descriptionIdTxt + "\", " + "blockId, Material::getMaterial(MaterialType::" + materialTxt + ")) {\n" +
                "\tcreativeCategory = CreativeItemCategory::" + categoryTxt + ";\n" +
                "\tsetDestroyTime(" + destroyTimeTxt + "F);\n" +
                "}")
                .split(System.getProperty("line.separator"));
        Save(sourceFile, sourceFileString);

        Toast.makeText(getApplicationContext(), (classNameTxt + ".cpp and " + classNameTxt + ".h" + R.string.successfully_generated), Toast.LENGTH_SHORT).show();
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
