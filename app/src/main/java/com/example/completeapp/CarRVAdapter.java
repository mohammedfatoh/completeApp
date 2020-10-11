package com.example.completeapp;

import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CarRVAdapter extends RecyclerView.Adapter<CarRVAdapter.carViewHolder> {

    private ArrayList<Car>cars;
    private OnRecycleViewItemClickListener listener;
    public CarRVAdapter(ArrayList<Car> cars,OnRecycleViewItemClickListener listener)
    {
        this.cars=cars;
        this.listener=listener;
    }

    public ArrayList<Car> getCars() {
        return cars;
    }

    public void setCars(ArrayList<Car> cars) {
        this.cars = cars;
    }

    @NonNull
    @Override
    public carViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_car_layout,null,false);
        carViewHolder viewHolder=new carViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull carViewHolder holder, int position) {
        Car car=cars.get(position);
        /*
        -  ال URI عبارة عن شىء شبيه بالرابط ولكنه أكثر توسعا منه هو عنوان لمسار صورة /لشىء اخر /معلومات عن شىء معين .
      - مسار ال imageview موجود فى ال Object اللى من نوع car على هيئة String وعلشان تتعامل معه ك مسار لل image دى لازم نحولها من  string الى
      نمط/متغير من نوع URI كده  Uri.parse(car.getImage()) وعلشان ال Imageview بتتعامل مع ال uri فقط .
      ولكن عند تخزين الصور فى ال dataBase بيخزن المسار على هيئة String ف لو هو على هيئة Uri بيتم تحويله الى string علشان أقدر أخزنه فى ال Database ولكن عند
       عرض الصور بيتم التعامل مع المسار ده على اساس أن هو متغير من نوع Uri  ف لو هو على هيئة string بيتم تحويله الى Uri.

         */
        //هذا السطر من خلاله بتاكد ان مسار الصورة موجود وبالتالى الصورة موجود و أن مسار الصورة لا يكون فارغ لتجنب حصول exception
        if(car.getImage()!=null &&!car.getImage().isEmpty() )
              holder.iv.setImageURI(Uri.parse(car.getImage()));
        else
        {
            holder.iv.setImageResource(R.drawable.city);
        }
        holder.tv_model.setText(car.getModel());
        holder.tv_color.setText(car.getColor());

        //  لو عايز تخلى لون اسم ال color هو نفسه
        //  علشان لو حصل أى exception ميحصلش  مشاكل ذى مفيش علامة # وهكذا ...
        try
        {
            holder.tv_color.setTextColor(Color.parseColor(car.getColor()));
        }
       catch (Exception e)
       {

       }
        holder.tv_dpl.setText(String.valueOf(car.getDpl()));
        holder.id=car.getId();
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    class carViewHolder extends RecyclerView.ViewHolder
    {
         ImageView iv;
         TextView tv_model,tv_color,tv_dpl;
         int id;
        public carViewHolder(@NonNull View itemView) {
            super(itemView);
            iv=itemView.findViewById(R.id.custom_car_iv);
            tv_model=itemView.findViewById(R.id.custom_car_model_tv);
            tv_color=itemView.findViewById(R.id.custom_car_tv_color);
            tv_dpl=itemView.findViewById(R.id.custom_car_tv_dpl);

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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(id);
                }
            });

        }
    }

}
