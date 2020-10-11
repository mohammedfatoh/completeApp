package com.example.completeapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int ADD_CAR_REQ_CODE=1;
    private static final int EDIT_CAR_REQ_CODE=1;
    public static final String CAR_KEY="car_key";
    private RecyclerView rv;
    private FloatingActionButton fab;
    private Toolbar  toolbar;
    private CarRVAdapter adapter;
    private DatabaseAccess db;
    private static final int PERMIISSIN_REQ_CODE=5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  طبعا من Api 6 الى اعلى بيتم عمل ال permission يدويا

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
        PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMIISSIN_REQ_CODE);
        }
        toolbar=findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        rv=findViewById(R.id.main_rv);
        fab=findViewById(R.id.main_fab);

       db=DatabaseAccess.getInstance(this);

        db.open();
        ArrayList<Car>cars=db.getAllCars();
        db.close();

        /*
        - أحنا عايزين من خلال الضغط على اى item اللى هو Card View داخل ال Recycle view يحولنى /يبنقلنى الى شاشة التفاصيل الخاص ب ال item ده
        ولكن ال Recycler view لا يوجد فيه  listener علشان اقدر انفذ فيه  شىء/كود الانتقال الى شاشة التفاصيل .
        ولكن يوجد حل وهو ان استدعى   listener الخاص بالتصميم كله الخاص ب ال  item فى ال viewHolder اللى بيكون فى ال adapter  ولكن الكود
        اللى بيكون داخل ال listener وهو ازاى اعرف ال context الخاص ب ال mainActivity اللى بنتقل منها الى شاشة التفاصيل عن طريق ال intent او استخدم
        الدالة دى ازاى  startActivityForResult  الخاصة بالرجوع من شاشة التفاصيل وهكذا ... ثم من الاصح ان هذا الكود خاص ب ال MainActivity
        فمينفعش من ناحية ال OOP أن أكتب هذا الكود داخل  Class ال  adapter ف انا محتاج كود يتكتب فى ال mainActivity ولكن يتنفذ داخل ال adapter
        فالحل أن أنشأ interface وانشا فيه دالة واعمله implemention هنا واكتب الكود اللى انا عايزه داخل الدالة وبالتالى هذا الكود يكون معلق داخل ال adapter
       حتى يتم الضغط على اى item بداخله وبالتالى يتنفذ فبيحصل callback لل interface داخل ال listener الخاص ب ال item  كله اللى
       موجود فى ReceyclerView هذا ال interface اللى هو بيعمل نفس عمل ال Listener ببعته ك  parameter لل object  adapter علشان يتعامل معه .

         */

        adapter=new CarRVAdapter(cars, new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(int carId) {
                Intent i=new Intent(getBaseContext(),viewCarActivity.class);
                i.putExtra(CAR_KEY,carId);
                startActivityForResult(i,EDIT_CAR_REQ_CODE);
            }
        });

        rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm=new GridLayoutManager(this,2);
        rv.setLayoutManager(lm);
        rv.setHasFixedSize(true);

   //بنعمل Listener لل button بداخله بعمل intent بحيث أقدر أنتقل لل Activity الاخرى عشان أضيف cars جديدة او اعرض ال details الخاصة ب car ضغطنا عليها
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(),viewCarActivity.class);
                startActivityForResult(intent,ADD_CAR_REQ_CODE);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        /*
        - ال search view او شريط البحث اللى موجود فى ال menu اللى موجود فى الtoolbar اللى بيكون عبارة عن الايقونة بيتم الضغط عليها وبعد كده بيتم
         توسعة هذا الشريط علشان يكون EditText للكتابة فيه.بنستخدمه من خلال ال custom_menu  عن طريق الخاصية دى
          app:actionViewClass="androidx.appcompat.widget.SearchView"
          ولازم تكون فى ال  support library

        - علشان اقدر ابحث من خلال شريط البحث/ search view اللى موجود فى ال menu اللى موجود فى ال  toolbar لازم اعمله  inflate علشان اقدر اتعامل معاه.
         بحيث  لما يكتب فيها  حاجة يحصل كذا وهكذا...

         ملحوظة:
          تتم عملية البحث بطريقتين اما اما تكون شغالة أثناء الكتابة من خلال ال listener او البحث بعد الكتابة والضغط على ال Button اللى هو submit button.
         */
        /*
        - بنعمل مؤشر/inflate لل search View اللى داخل ال menu
        ال search View لازم يكون موجود من ال support library ولازم نعمل casting لل view اللى جاى من الدالة دى getActionView() الى SearchView

         */
        SearchView searchView=(SearchView) menu.findItem(R.id.main_search).getActionView();
        //  من خلال السطر ده  أنا بفعل زرار ال submit اللى عند الضغط عليه بيتم البحث عن الQuery/الاستعلام اللى بتبحث عنه.وهذه طريقة من طرق البحث
        searchView.setSubmitButtonEnabled(true);
        // الطريقة الاخرى من خلال ال Listener هذا ال listener بيستمع لل Query/الاستعلام اللى ببحث عنه وطبعا وضعناه فى  oncreate علشان يبقى جاهز فى اى وقت
        // بيوجد عدة listeners من اهمها
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                 // هذه الدالة تستدعى عند الضغط على ال Button submit
                // هنا تتم كتابة كود البحث وبيتم عرض فقط ال cars اللى ببحث عنها .
                db.open();
                ArrayList<Car>cars=db.getCars(query);
                db.close();
                adapter.setCars(cars);
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // هذه الدالة تستدعى عند حدوث اى تغير فى  ال query /الاستعلام اللى ببحث عنه سواء بزيادة أو النقصان الأحرف
                // هنا تتم كتابة كود البحث وبيتم عرض فقط ال cars اللى ببحث عنها .
                db.open();
                ArrayList<Car>cars=db.getCars(newText);
                db.close();
                adapter.setCars(cars);
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        //هذا ال  listener بيتم استدعاءه عند اغلاق ال  search View
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //هذه الدالة   بيتم استدعاءها عند اغلاق ال  search View يعنى مثلا عايز ارجع للوضع اللى قبل البحث وهكذا
                //  هنا بتتم كتابة كود عرض السيارات  اللى هما موجودين
                db.open();
                ArrayList<Car>cars=db.getAllCars();
                db.close();
                adapter.setCars(cars);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ADD_CAR_REQ_CODE)
        {
            db.open();
            ArrayList<Car> cars=db.getAllCars();
            db.close();
            adapter.setCars(cars);
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case PERMIISSIN_REQ_CODE:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    // تم الحصول على الصلاحية
                }
        }
    }
}