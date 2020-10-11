package com.example.completeapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

public class viewCarActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQ_CODE =1 ;
    public static final int ADD_CAR_RESULT_CODE =2 ;
    public static final int EDIT_CAR_RESULT_CODE =3 ;
    private Toolbar toolbar;
    private TextInputEditText et_model,et_color,et_dpl,et_description;
    private ImageView iv;
    private DatabaseAccess db;
    private int carId=-1;
//هذا المتغير للحفظ مسار الصورة اللى انا أخترتها بحيث عند الاضافة  يكون عندى مسار الصورة فبستخدمه ولان لاتوجد دالة اسمها getImageUri فلازم اخزن مسار الصورة فى متغير
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_car);

        toolbar=findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbar);

        iv=findViewById(R.id.details_iv);
        et_model=findViewById(R.id.et_details_model);
        et_color=findViewById(R.id.et_details_color);
        et_dpl=findViewById(R.id.et_details_dpl);
        et_description=findViewById(R.id.et_details_describtion);

        db=DatabaseAccess.getInstance(this);

        Intent intent=getIntent();
        carId=intent.getIntExtra(MainActivity.CAR_KEY,-1);

        if(carId==-1)
        {
            // عملية اضافة
            enableFields();
            clearFields();

        }
        else
        {
            //عملية  العرض
            disableFields();
            db.open();
            Car car=db.getCar(carId);
            db.close();
            if(car!=null)
            {
                fillCarToFields(car);
            }
        }

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(in,PICK_IMAGE_REQ_CODE);
            }
        });
    }

    private void fillCarToFields(Car car)
    {
        if(car.getImage()!=null&&!car.getImage().equals(""))
            iv.setImageURI(Uri.parse(car.getImage()));
        et_model.setText(car.getModel());
        et_color.setText(car.getColor());
        et_dpl.setText(car.getDpl()+"");
        et_description.setText(car.getDescription());
    }

    // هنعمل method من خلالها نجعل الحقول الادخال تكون فقط للعرض وغير فعالة
    private void disableFields(){
        iv.setEnabled(false);
        et_model.setEnabled(false);
        et_color.setEnabled(false);
        et_dpl.setEnabled(false);
        et_description.setEnabled(false);
    }
    //هذه ال method من خلال تفعيل حقول الادخال
    private void enableFields(){

        iv.setEnabled(true);
        et_model.setEnabled(true);
        et_color.setEnabled(true);
        et_dpl.setEnabled(true);
        et_description.setEnabled(true);
    }
    //لتفريغ الحقول بعد اضافة Car
    public void clearFields(){

        iv.setImageURI(null);
        et_model.setText(" ");
        et_color.setText(" ");
        et_dpl.setText(" ");
        et_description.setText(" ");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu,menu);
        MenuItem sava=menu.findItem(R.id.details_menu_sava);
        MenuItem edit=menu.findItem(R.id.details_menu_edit);
        MenuItem delete=menu.findItem(R.id.details_menu_delete);
        if(carId==-1)
        {
            // عملية اضافة
            sava.setVisible(true);
            edit.setVisible(false);
            delete.setVisible(false);
        }
        else
        {
            //عملية  العرض
            sava.setVisible(false);
            edit.setVisible(true);
            delete.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String model,color,image="",desc;
        double dpl;
        // طالما انت بتحتاج تفتح ال dataBase  فى كل عملية
        db.open();
      switch (item.getItemId())
      {
          case R.id.details_menu_sava:
              model=et_model.getText().toString();
              color=et_color.getText().toString();
              desc=et_description.getText().toString();
              dpl=Double.parseDouble(et_dpl.getText().toString());
              if(imageUri!=null)
                 image=imageUri.toString();

              boolean res;
              // ال cARId علشان محتاجه فى التعديل
              Car car=new Car(carId,model,color,dpl,image,desc);


              if(carId==-1)
              {
                  res=db.insertCar(car);
                  if(res)
                  {
                      Toast.makeText(this, "Car Added successfully", Toast.LENGTH_SHORT).show();
                      setResult(ADD_CAR_RESULT_CODE,null);
                      finish();
                  }
              }
              else
              {
                  res=db.updateCar(car);
                  if(res)
                  {
                      Toast.makeText(this, "Car Edited successfully", Toast.LENGTH_SHORT).show();
                      setResult(EDIT_CAR_RESULT_CODE,null);
                      finish();
                  }
              }

              return true;
          case R.id.details_menu_edit:
              enableFields();
              MenuItem sava=toolbar.getMenu().findItem(R.id.details_menu_sava);
              MenuItem edit=toolbar.getMenu().findItem(R.id.details_menu_edit);
              MenuItem delete=toolbar.getMenu().findItem(R.id.details_menu_delete);
              sava.setVisible(true);
              edit.setVisible(false);
              delete.setVisible(false);
              return true;
          case R.id.details_menu_delete:
                car=new Car(carId,null,null,0,null,null);
                res=db.deleteCar(car);
                if(res)
                {
                      Toast.makeText(this, "Car deleted successfully", Toast.LENGTH_SHORT).show();
                      setResult(EDIT_CAR_RESULT_CODE,null);
                      finish();
                }
              return true;
      }
      db.close();
      return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null) {
            imageUri = data.getData();
            iv.setImageURI(imageUri);
        }
    }
}