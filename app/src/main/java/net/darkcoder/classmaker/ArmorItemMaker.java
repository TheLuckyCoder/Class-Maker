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


public class ArmorItemMaker extends AppCompatActivity {

    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ClassMaker/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_armoritem);

        File dir = new File(path);
        dir.mkdirs();
    }

    public void createMod(View view) {
        //Defines
        EditText className = (EditText) findViewById(R.id.txtClassName);
        EditText itemDescriptionId = (EditText) findViewById(R.id.txtItemDescriptionId);
        EditText armorMaterial = (EditText) findViewById(R.id.txtArmorItemMaterial);
        EditText armorRenderType = (EditText) findViewById(R.id.txtArmorItemRenderType);
        EditText armorSlot = (EditText) findViewById(R.id.txtArmorItemSlot);
        EditText itemCategory = (EditText) findViewById(R.id.txtItemCategory);
        EditText itemTexture = (EditText) findViewById(R.id.txtItemTexture);
        EditText itemMaxDamage = (EditText) findViewById(R.id.txtItemMaxStackSize);


        //Asserts
        assert className != null;
        assert itemDescriptionId != null;
        assert armorMaterial != null;
        assert armorRenderType != null;
        assert armorSlot != null;
        assert itemCategory != null;
        assert itemTexture != null;
        assert itemMaxDamage != null;


        //Strings
        String classNameTxt = className.getText().toString();
        String itemDescriptionIdTxt = itemDescriptionId.getText().toString();
        String armorMaterialTxt = armorMaterial.getText().toString();
        String armorRenderTypeTxt = armorRenderType.getText().toString();
        String armorSlotTxt = armorSlot.getText().toString();
        String itemCategoryTxt = itemCategory.getText().toString();
        String itemTextureTxt = itemTexture.getText().toString();
        String itemMaxDamageTxt = itemMaxDamage.getText().toString();
        if (armorSlotTxt.matches("1"))
            armorSlotTxt = "HELMET";
        else if (armorSlotTxt.matches("2"))
            armorSlotTxt = "CHESTPLATE";
        else if (armorSlotTxt.matches("3"))
            armorSlotTxt = "LEGGINGS";
        else if (armorSlotTxt.matches("4"))
            armorSlotTxt = "BOOTS";

        if (itemCategoryTxt.matches("1"))
            itemCategoryTxt = "BLOCKS";
        else if (itemCategoryTxt.matches("2"))
            itemCategoryTxt = "DECORATIONS";
        else if (itemCategoryTxt.matches("3"))
            itemCategoryTxt = "TOOLS";
        else if (itemCategoryTxt.matches("4"))
            itemCategoryTxt = "ITEMS";


        //Code
        File armorItemHeaderFile = new File(path + (classNameTxt + ".h"));
        File armorItemSourceFile = new File(path + (classNameTxt + ".cpp"));

        String[] itemHeaderString = String.valueOf("#pragma once\n\n" +
                "#include \"minecraftpe/world/item/ArmorItem.h\"\n\n" +
                "class " + classNameTxt + " : public ArmorItem {\n" +
                "public:\n" +
                "\t" + classNameTxt + "(short itemId);\n" +
                "};").split(System.getProperty("line.separator"));
        Save(armorItemHeaderFile, itemHeaderString);

        String[] itemSourceString = String.valueOf("#include \"" + classNameTxt + ".h\"\n" +
                "\n" +
                classNameTxt + "::" + classNameTxt + "(short itemId) : Item(\"" + itemDescriptionIdTxt + "\", " + "itemId - 0x100, " + armorMaterialTxt + ", " + armorRenderTypeTxt + ", ArmorSlot::" + armorSlotTxt + ") {\n" +
                "\tItem::mItems[itemId] = this;\n" +
                "\tcreativeCategory = CreativeItemCategory::" + itemCategoryTxt + ";\n" +
                "\tsetIcon(\"" + itemTextureTxt + "\", 0);\n" +
                "\tsetMaxDamage(" + itemMaxDamageTxt + ");\n" +
                "}").split(System.getProperty("line.separator"));
        Save(armorItemSourceFile, itemSourceString);

        Toast.makeText(getApplicationContext(), (classNameTxt + R.string.class_generated), Toast.LENGTH_SHORT).show();
    }

    public static void Save(File file, String[] data) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            try {
                for (int i = 0; i < data.length; i++) {
                    if (fos != null) {
                        fos.write(data[i].getBytes());
                    }
                    if (i < data.length - 1) {
                        if (fos != null) {
                            fos.write("\n".getBytes());
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}