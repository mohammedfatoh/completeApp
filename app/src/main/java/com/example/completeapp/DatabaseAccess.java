package com.example.completeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseAccess {
     // علشان نعمل object من ال SQLiteDatabase علشان أخزن فيها المؤشر لل Database سواء مؤشر للقراءة أو الكتابة
     private SQLiteDatabase database;
     // علشان نقدر من خلاله نفتح ونقفل ال database  من خلال ال methods اللى بتجيب مؤشر لل database سواء فى حالة القراءة او الكتابة.
     private SQLiteOpenHelper openHelper;

     /*
      بيوجد طريقة لانشاء object من class DatabaseAccess  مرة واحدة فقط خارج هذا ال  class بدل ما ننشئ كذا object من هذا ال class كل ما نحتاج  object
      وهذه الطريقة تعرف ب singleton patterns وممكن نستخدم الطريقة العادية ونعمل كذا object حسب احتياجنا.
      */
     private static DatabaseAccess instance;
     //   طالما عملته private يبقى مفيش شىء خارج ال class يقدر يستخدمه وينشأ object جديد ععن طريق ال  new
     private DatabaseAccess (Context context)
     {
         this.openHelper=new MyDataBase(context);
     }

     /*
     - من خلال ال method دى أنا بنشا object من خلال ال  constructor  اللى بيكون   private ولكن قبل ما انشأ هل  object ده حصله  create ولا لا
     لو لا هنشأ واحد جديد لكن لو اه مش هنشاه تانى/ create تانى وبعد كده اعمل  return
     وهذه طريقة لانشاء object من class DatabaseAccess  مرة واحدة فقط خارج هذا ال  class بدل ما ننشئ كذا object من هذا ال class كل ما نحتاج  object
      وهذه الطريقة تعرف ب singleton patterns
      */
     public static  DatabaseAccess getInstance(Context context)
     {
         if(instance==null)
         {
             instance=new DatabaseAccess(context);
         }
         return instance;
     }

     /*
      -  طالما ان بنتعامل مع ال dataBase من خلال اى عملية لازم نفتح ال dataBase وبعد انتهاء هذه العملية نغلق ال database علشان لو حد أشتغل عايز يشتغل
     على ال dataBase يلقائها مغلقة وذى ما قلنا  فى internal storage طالما الملف مفتوح مفيش حد هيقدر يستخدمه من مكان اخر الا اذا اغلق وكذلك ملفات ال dataBase

     فى ال internal database كان الmethod اول ما تنتهى يدمر الكود اللى بداخلها وبعد كده بدمر ال collection تلقائيا
     وبيوجد طرق اخرى
      */
     // لازم الترتيب فى ال methods
     public void open()
     {
         // - بنستخدم  getWritableDatabase() لاى عملية أى أن كانت ايه
        /*
       من خلال السطر ده احنا وضعنا المؤشر/ ال connection  الخاص ب  class  ال openHelper اللى هو ال   class  ال  MyDataBase
         الخاصة ب ال dadabase اللى شغالين عليها  فى ال database اللى هو object من نوع  SQLiteDatabase

         ومش هستخدم المؤشر فى اى مكان اخر عن طريق ال method دى بنجيبه مرة واحدة فقط/ بخزنه مرة واحدة فقط فى ال متغير database و بشتغل مباشرة من
          خلال ال database وهو مخزن فيه المؤشر .
         */
         this.database=this.openHelper.getWritableDatabase();
     }

     public void close()
     {
         if(this.database!=null)
         {
             this.database.close();
         }
     }
     //  الدالة دى من خلالها بنضيف ال data على ال database وبتاخد object كاملا بحيث اقدر ابعت كل Data مجمعة مع بعض فى Object واحد
     //الدالة دى بترجع قيمة boolean وبناء على هذه القيمة هل تم اضافة ال data دى ولا لا.
     // دالة الاضافة
     public boolean insertCar(Car car)
     {

        /*
        فكرة ال  class ContentValues نفس فكرة ال Bundle بتخزن بيانات على هيئة key وقيمته ولكن بتفرق ان ال key  بيكون هو اسم العمود اللى بيتخزن فيه ال data
         */
         ContentValues values=new ContentValues();
         values.put(MyDataBase.CAR_CLN_MODEL,car.getModel());
         values.put(MyDataBase.CAR_CLN_COLOR,car.getColor());
         values.put(MyDataBase.CAR_CLN_DPL,car.getDpl());
         values.put(MyDataBase.CAR_CLN_IMAGE,car.getImage());
         values.put(MyDataBase.CAR_CLN_DESCRIPTION,car.getDescription());
        /*
        - ممكن اعمل  insert لل data فى ال database  من خلال الدالة دى  db.execSQL ونكتب الامر /العملية اللى عايز انفذها فيه او نستخدم الدالة الجاهزة وهى
        ال insert وهى اللى بتعمل ال  insert بنفسها للدالة  عم طريق ان هى بتاخد object من نوع ContentValues اللى بيكون فيه ال  data وطبعا بداخل هذا
        ال object اسم العمود اللى هو ال  key  و ال  data اللى بتوضع فيه.ودالة ال insert تاخد ال data دى من ال object وتوضعها داخل ال database
        فى الاعمدة /ال keys المقابلة ليها.
         */
        /*
         دالة ال insert بترجع  متغير من نوع long  لو قيمته ب -1 يبقى لم يتم اضافة ال data دى فى
         ال data base ولكن اذا كان رقم اخر يبقى تم اضافة ال data فى ال  database وهذا الرقم عبارة عن رقم العنصر / record الذى تم اضافة ال Data  فيه
         */
         Long result= database.insert(MyDataBase.CAR_TB_NAME,null,values);
         return result!=-1;
     }
     //دالة التعديل
     public boolean updateCar(Car car)
     {

        /*
        فكرة ال  class ContentValues نفس فكرة ال Bundle تخزين بيانات على هيئة key وقيمته ولكن بتفرق ان ال key  بيكون هو اسم العمود اللى بيتخزن فيه ال data
         */
         ContentValues values=new ContentValues();
         values.put(MyDataBase.CAR_CLN_MODEL,car.getModel());
         values.put(MyDataBase.CAR_CLN_COLOR,car.getColor());
         values.put(MyDataBase.CAR_CLN_DPL,car.getDpl());
         values.put(MyDataBase.CAR_CLN_IMAGE,car.getImage());
         values.put(MyDataBase.CAR_CLN_DESCRIPTION,car.getDescription());
        /*
        - ممكن اعمل  update لل data فى ال database  من خلال الدالة دى  db.execSQL ونكتب الامر /العملية اللى عايز انفذها فيه او نستخدم الدالة الجاهزة وهى
        ال updateوهى اللى بتعمل ال  update بنفسها للدالة  عم طريق ان هى بتاخد object من نوع ContentValues اللى بيكون فيه ال data  اللى هنعدلها
        وطبعا بداخل هذا
        ال object اسم العمود اللى هو ال  key  و ال  data  اللى هنعدلها  فيه.ودالة ال update تاخد ال data دى من ال object اللى هو ال values و
        تعدلها داخل ال database فى الاعمدة /ال keys المقابلة ليها.

         */
        /*
         دالة ال Update بترجع  متغير من نوع int وهو بيساوى عدد الصفوف اللى اتاُثرت بالعملية/كم صف تم التعديل عليه وطبعا لازم يكون أكبر من صفر غير كده يبقى لم
         يتم تعديل اى صف وفيه مشكلة
         */
         String args[]={car.getId()+""};
        /*
        - ملحوظة مهمة :
        هو هنا فصل الشرط عن القيمة بتاعته والسبب شىء اسمه SQL injection  لان ال قيمة الشرط أى ان كانت خاصة ب ايه سواء id او قيمة أى شىء اخر بمنع اى
        شخص ممكن يدخل مكان القيمة دى امر اختراق فيه drop لل Database ف لو حتى بعته هيكون على شكل نص وهيتخزن كنص وهيفحصه كنص علشان كده بخلى  القيم
             على هيئة نصوص وهذه القيم بتكون فى array لان ممكن تكون لأكثر من شىء
              فكرة الموضوع هو حماية ال DataBase من أى مشكلة أو عملية سلبية.
         */
         int result= database.update(MyDataBase.CAR_TB_NAME,values,"id=?",args);
         return result>0;
     }

     public Long getCarsCount()
     {

         // بيوجد كذا طريقة للحصول على عدد اال records منها
         // لو عايز اجيب عدد ال records ولكن بشروط معينة هستخدم طريقة دى "id=?",args  واععمل مصفوفة من ال strings واشتغل عادى
         return DatabaseUtils.queryNumEntries(database,MyDataBase.CAR_TB_NAME);
     }
     //دالة الحذف
     public boolean deleteCar(Car car)
     {

         String args[]={car.getId()+""};
         int result=database.delete(MyDataBase.CAR_TB_NAME,MyDataBase.CAR_CLN_ID+"=?",args);
         // ال result بيساوى عدد العناصر/الصفوف اللى تم حذفها
         return result>0;
     }

     //دالة الاسترجاع
     public ArrayList<Car> getAllCars()
     {
         //ال return value بتكون من نوع ArrayList<Car> لان انا بعمل استرجاع للمجموعة من ال Cars فلازم يكونوا بداخل arraylist  و  arraylist علشان أكثر مرونة
         // بنشا   arraylist من نوع cars  عشان اضع فيه ال cars اللى اعملها select فيها.
         ArrayList<Car> cars=new ArrayList<>();

        /*
        - بكتب ال Query او الامر علشان اعمل select للعناصر /records فى الدالة دى db.rawQuery هذا الدالة ال  return value الخاص بيها من نوع Cursor
          هذا ال class  بيكون مؤشر لل database اللى عندى من خلاله أنا بقدر اوصل  لل database صف صف وبقرأ  من database البيانات صف صف
          ولكن البيانات بترجع من خلال هذا ال  class فى شكل يشبه المصفوفة الثنائية
          بمعنى   ال  Cursor بيشوف ال dataBase عبارة عن مجموعة من الصفوف بداخل كل صف مجموعة من الاعمدة من خلال هذه الصفوف بنحصل على البيانات فى اى عمود احنا عايزينها
          من خلال مجموعة من ال methods انا بحرك المؤشر او اضع  فى كان معين
          بمعنى لو عايزين data موجود فى عمود معين نخلى ال cursor يشاور على العنصر/ الصف الموجودة فيها ال data وبعد كده نجيب ال  data من العمود ده
           ال cursor:
          This interface provides random read-write access to the result set returned by a database query.
         */
         Cursor cursor=database.rawQuery("SELECT * FROM "+MyDataBase.CAR_TB_NAME,null);
         //كود التعامل مع ال Cursor  وتحويله الى مصفوفة من نوع Car
         //فحص ال Cursor هل يحتوى على بيانات أم لا
         // الدالة دى cursor.moveToFirst()  ال return value الخاصة بيها  boolean وهى بتجعل ال cursor يشاور على اول صف موجود فيه.
         if(cursor!=null&&cursor.moveToFirst())
         {
             do{
                /*
                - انا من خلال ال method دى cursor.getColumnIndex(CAR_CLN_ID) بجيب ال index الخاص بالعمود اللى اسمه بتاخده هذه الدالة
                والسبب فى ان عند التعديل سواء حذف او اضافة  الاعمدة فى اى مكان فى DataBase مروحش لكل  method فيهselect واغير فى ال index
                الخاصة بكل عمود ولكن انا من خلال الدالة بعطيه اسم العمود وهى تجيب الindex الخاص بيها.
                 */
                 int id=cursor.getInt(cursor.getColumnIndex(MyDataBase.CAR_CLN_ID));
                 String model =cursor.getString(cursor.getColumnIndex(MyDataBase.CAR_CLN_MODEL));
                 String color=cursor.getString(cursor.getColumnIndex(MyDataBase.CAR_CLN_COLOR));
                 double dpi=cursor.getDouble(cursor.getColumnIndex(MyDataBase.CAR_CLN_DPL));
                 String image =cursor.getString(cursor.getColumnIndex(MyDataBase.CAR_CLN_IMAGE));
                 String description =cursor.getString(cursor.getColumnIndex(MyDataBase.CAR_CLN_DESCRIPTION));
                 // بعد الحصول على data من خلال الcursor من الdataBase بضعها فى object من نوع Car وبعد  كده بضيفها داخل ال arrayList.
                 Car car=new Car(id,model,color,dpi,image,description);
                 cars.add(car);
             }
             //  الدالة دى cursor.moveToNext()  ال return value الخاصة بيها  boolean وهى بتجعل ال  cursor/مؤشر يشاور على العنصر او  record اللى بعده.
             //من خلال الشرط ده بنشوف هل ال record اللى بعده موجود ولا لا لو لا هتكون ال  return value ب false وهيخرج من ال Loop لو اه هيكمل عادى
             while (cursor.moveToNext());
             cursor.close();
         }
         return cars;
     }
     // دالة البحث
     public ArrayList<Car> getCars(String modelSearch)
     {
         //ال return value بتكون من نوع ArrayList<Car> لان انا بعمل استرجاع للمجموعة من ال Cars فلازم يكونوا بداخل arraylist  و  arraylist علشان أكثر مرونة
         // بنشا   arraylist من نوع cars  عشان اضع فيه ال cars اللى اعملها select فيها.
         ArrayList<Car> cars=new ArrayList<>();

        /*
        - بكتب ال Query او الامر علشان اعمل select للعناصر /records فى الدالة دى db.rawQuery هذا الدالة ال  return value الخاص بيها من نوع Cursor
          هذا ال class  بيكون مؤشر لل database اللى عندى من خلاله أنا بقدر اوصل  لل database صف صف وبقرأ  من database البيانات صف صف
          ولكن البيانات بترجع من هذا ال  class فى شكل يشبه المصفوفة الثنائية
          بمعنى   ال  Cursor بيكون عبارة عن مجموعة من الصفوف بداخل كل صف مجموعة من الاعمدة من خلال هذه الصفوف بنحصل على البيانات فى اى عمود احنا عايزينها
          من خلال مجموعة من ال methods انا بحرك المؤشر او اضع  فى كان معين
          بمعنى لو عايزين data موجود فى عمود معين نخلى ال cursor يشاور على العنصر/ الصف الموجودة فيها ال data وبعد كده نجيب ال  data من العمود ده
         */
         Cursor cursor=database.rawQuery("SELECT * FROM "+MyDataBase.CAR_TB_NAME+" WHERE "+MyDataBase.CAR_CLN_MODEL+" LIKE ?"
                 , new String[]{modelSearch+"%"});
         //كود التعامل مع ال Cursor  وتحويله الى مصفوفة من نوع Car
         //فحص ال Cursor هل يحتوى على بيانات أم لا
         // الدالة دى cursor.moveToFirst()  ال return value الخاصة بيها  boolean وهى بتجعل ال cursor يشاور على اول صف موجود فيه.
         if(cursor!=null&&cursor.moveToFirst())
         {
             do{
                /*
                - انا من خلال ال method دى cursor.getColumnIndex(CAR_CLN_ID) بجيب ال index الخاص بالعمود اللى اسمه بتاخده هذه الدالة
                والسبب فى ان عند التعديل سواء حذف او اضافة  الاعمدة فى اى مكان فى DataBase مروحش لكل  method فيهselect واغير فى ال index
                الخاصة بكل عمود ولكن انا من خلال الدالة بعطيه اسم العمود وهى تجيب الindex الخاص بيها.
                 */
                 int id=cursor.getInt(cursor.getColumnIndex(MyDataBase.CAR_CLN_ID));
                 String model =cursor.getString(cursor.getColumnIndex(MyDataBase.CAR_CLN_MODEL));
                 String color=cursor.getString(cursor.getColumnIndex(MyDataBase.CAR_CLN_COLOR));
                 double dpi=cursor.getDouble(cursor.getColumnIndex(MyDataBase.CAR_CLN_DPL));
                 String image =cursor.getString(cursor.getColumnIndex(MyDataBase.CAR_CLN_IMAGE));
                 String description =cursor.getString(cursor.getColumnIndex(MyDataBase.CAR_CLN_DESCRIPTION));
                 // بعد الحصول على data من خلال الcursor من الdataBase بضعها فى object من نوع Car وبعد  كده بضيفها داخل ال arrayList.
                 Car car=new Car(id,model,color,dpi,image,description);
                 cars.add(car);
             }
             //  الدالة دى cursor.moveToNext()  ال return value الخاصة بيها  boolean وهى بتجعل ال  cursor/مؤشر يشاور على العنصر او  record اللى بعده.
             //من خلال الشرط ده بنشوف هل ال record اللى بعده موجود ولا لا لو لا هتكون ال  return value ب false وهيخرج من ال Loop لو اه هيكمل عادى
             while (cursor.moveToNext());
             cursor.close();
         }
         return cars;
     }

     //  دالة استرجاع Car
     public Car getCar(int carId)
     {

        /*
        - بكتب ال Query او الامر علشان اعمل select للعناصر /records فى الدالة دى db.rawQuery هذا الدالة ال  return value الخاص بيها من نوع Cursor
          هذا ال class  بيكون مؤشر لل database اللى عندى من خلاله أنا بقدر اوصل  لل database صف صف وبقرأ  من database البيانات صف صف
          ولكن البيانات بترجع من خلال هذا ال  class فى شكل يشبه المصفوفة الثنائية
          بمعنى   ال  Cursor بيشوف ال dataBase عبارة عن مجموعة من الصفوف بداخل كل صف مجموعة من الاعمدة من خلال هذه الصفوف بنحصل على البيانات فى اى عمود احنا عايزينها
          من خلال مجموعة من ال methods انا بحرك المؤشر او اضع  فى كان معين
          بمعنى لو عايزين data موجود فى عمود معين نخلى ال cursor يشاور على العنصر/ الصف الموجودة فيها ال data وبعد كده نجيب ال  data من العمود ده
           ال cursor:
          This interface provides random read-write access to the result set returned by a database query.
         */
         Cursor cursor=database.rawQuery("SELECT * FROM "+MyDataBase.CAR_TB_NAME+" Where "+MyDataBase.CAR_CLN_ID+"=?",
                 new String[]{String.valueOf(carId)});
         //كود التعامل مع ال Cursor  وتحويله الى مصفوفة من نوع Car
         //فحص ال Cursor هل يحتوى على بيانات أم لا
         // الدالة دى cursor.moveToFirst()  ال return value الخاصة بيها  boolean وهى بتجعل ال cursor يشاور على اول صف موجود فيه.
         if(cursor!=null&&cursor.moveToFirst())
         {

                /*
                - انا من خلال ال method دى cursor.getColumnIndex(CAR_CLN_ID) بجيب ال index الخاص بالعمود اللى اسمه بتاخده هذه الدالة
                والسبب فى ان عند التعديل سواء حذف او اضافة  الاعمدة فى اى مكان فى DataBase مروحش لكل  method فيهselect واغير فى ال index
                الخاصة بكل عمود ولكن انا من خلال الدالة بعطيه اسم العمود وهى تجيب الindex الخاص بيها.
                 */
                 int id=cursor.getInt(cursor.getColumnIndex(MyDataBase.CAR_CLN_ID));
                 String model =cursor.getString(cursor.getColumnIndex(MyDataBase.CAR_CLN_MODEL));
                 String color=cursor.getString(cursor.getColumnIndex(MyDataBase.CAR_CLN_COLOR));
                 double dpi=cursor.getDouble(cursor.getColumnIndex(MyDataBase.CAR_CLN_DPL));
                 String image =cursor.getString(cursor.getColumnIndex(MyDataBase.CAR_CLN_IMAGE));
                 String description =cursor.getString(cursor.getColumnIndex(MyDataBase.CAR_CLN_DESCRIPTION));
                 // بعد الحصول على data من خلال الcursor من الdataBase بضعها فى object من نوع Car وبعد  كده بضيفها داخل ال arrayList.
                 Car car=new Car(id,model,color,dpi,image,description);
                 cursor.close();
                 return car;
          }
         //  هيرجع null لو مش موجودة ال car فى ال database
         return null;
     }

}
