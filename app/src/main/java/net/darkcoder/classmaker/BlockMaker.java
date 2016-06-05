package net.darkcoder.classmaker;

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
        EditText blockDescriptionId = (EditText) findViewById(R.id.txtBlockDescriptionId);
        EditText blockCategory = (EditText) findViewById(R.id.txtBlockCategory);
        EditText blockMaterial = (EditText) findViewById(R.id.txtBlockMaterial);
        EditText blockDestroyTime = (EditText) findViewById(R.id.txtBlockDestroyTime);


        //Asserts
        assert className != null;
        assert blockDescriptionId != null;
        assert blockCategory != null;
        assert blockMaterial != null;
        assert blockDestroyTime != null;

        //Strings and Integers
        String classNameTxt = className.getText().toString();
        String blockDescriptionIdTxt = className.getText().toString();
        String blockCategoryTxt = blockCategory.getText().toString();
        String blockMaterialTxt = blockMaterial.getText().toString();
        String blockDestroyTimeTxt = blockDestroyTime.getText().toString();

        if (blockCategoryTxt.matches("1"))
            blockCategoryTxt = "BLOCKS";
        else if (blockCategoryTxt.matches("2"))
            blockCategoryTxt = "DECORATIONS";
        else if (blockCategoryTxt.matches("3"))
            blockCategoryTxt = "TOOLS";
        else if (blockCategoryTxt.matches("4"))
            blockCategoryTxt = "ITEMS";


        //Code
        File blockHeaderFile = new File (path + (classNameTxt + ".h"));
        File blockSourceFile = new File (path + (classNameTxt + ".cpp"));

        String [] itemHeaderString = String.valueOf("#pragma once\n\n" +
                "#include \"com/mojang/minecraftpe/world/level/block/Block.h\"\n\n" +
                "class " + classNameTxt + " : public Block {\n" +
                "public:\n" +
                "\t" + classNameTxt + "(int blockId);\n" +
                "};")
                .split(System.getProperty("line.separator"));
        Save(blockHeaderFile, itemHeaderString);

        String [] itemSourceString = String.valueOf("#include \"" + classNameTxt + ".h\"\n\n" +
                "#include \"com/mojang/minecraftpe/CreativeItemCategory.h\"\n\n" +
                classNameTxt + "::" + classNameTxt + "(int blockId) : Block(\"" + blockDescriptionIdTxt + "\", " + "blockId, Material::getMaterial(MaterialType::" + blockMaterialTxt + ")) {\n" +
                "\tcreativeCategory = CreativeItemCategory::" + blockCategoryTxt + ";\n" +
                "\tsetDestroyTime(" + blockDestroyTimeTxt + "F);\n" +
                "}")
                .split(System.getProperty("line.separator"));
        Save(blockSourceFile, itemSourceString);

        Toast.makeText(getApplicationContext(), (classNameTxt + R.string.class_generated), Toast.LENGTH_SHORT).show();
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
