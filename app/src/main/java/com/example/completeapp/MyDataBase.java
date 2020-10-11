package com.example.completeapp;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class MyDataBase extends SQLiteAssetHelper {
    /*
    - هذا ال class مصمم للتعامل مع external DataBase فلا نستدعى دالة create لانشاء ال  dataBase لانه بطبعت انشات من قبل باستخدام البرنامج خارجيا.
    وهو  بيورث من  SQLiteOpenHelper يعنى هو هو ولكن مع اكواد اضافية وترتيب.ووظيفته هو انشاء ال  database جدبدة داخليا عن طريق ال method  oncreate
    وبينسخ الملف ال  database اللى هو external database في الملف ال database الذى انشائه
     */
    // لازم الاسم ال database يكون هو هو اسم الملف الموجود فى floder ال asset عشان يقدر يتعرف عليه ويتعامل معاه
    public static final String DB_NAME="cars.db";
    /*
      كل database بتنشاها لازم يكون ليه version والسبب فى انك لما تحدث الdatabase تحدث ال  version  الخاص بيها والسبب فى ال نظام ال android داخليا
      بيحدد من خلال ال version  هل يحدث ال database أو اضيف  dataBase جديدة ولا ذى ما هى بمعنى لو شغلت التطبيق   وتغير ال  version ونزلت التطبيق
      مرة أخرى على الجهاز تلقائيا هيتم استدعاء دالة اعادة انشاء ال dataBase اللى هى داخل onUpgrade .
      ملحوظة أى تغيير فى ال database لازم أغير ال   رقم ال version الى أعلى   والا مش هيحصل تغيير فى ال  dataBase
     */
    public static final int DB_VERSIONS=1;
    // لازم يكون اسماء الاعمدة هو هو نفس اسماء الاعمدة داخل ملف ال Database
    public static final String CAR_TB_NAME="car";
    public static final String CAR_CLN_ID="id";
    public static final String CAR_CLN_MODEL="model";
    public static final String CAR_CLN_COLOR="color";
    public static final String CAR_CLN_DESCRIPTION="describtion";
    public static final String CAR_CLN_IMAGE="image";
    public static final String CAR_CLN_DPL="distanceparLetter";

    public MyDataBase( Context context)
    {
        super(context,DB_NAME,null,DB_VERSIONS);
    }

}
