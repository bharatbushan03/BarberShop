package com.barbershop.app.custom_adapters;

        import android.content.Context;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.yourname.barbershop.R;
        import com.yourname.barbershop.userdetails.services;

        import java.util.ArrayList;

public class serviceslist_of_selected_shop_adapter extends RecyclerView.Adapter<serviceslist_of_selected_shop_adapter.ViewHolder> {

    private static ArrayList<services> list;
    public ArrayList<String> a2=new ArrayList<>();
    public static Context context;
    public static int pos;


    public serviceslist_of_selected_shop_adapter(ArrayList<services> listt, Context context){

        list=listt;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(context)
                .inflate(R.layout.service_view_of_selectedshop, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    pos=holder.getAdapterPosition();
       holder.getservicename().setText(list.get(position).getService_name());
       holder.getPrice_and_duration().setText(list.get(position).getService_price()+"$ and up to "+list.get(position).getService_duration()+"mins ");
       holder.getExpandable_description().setText(list.get(position).getService_description());

        holder.getService_checkbox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (holder.getService_checkbox().isChecked()){
                    a2.add(holder.getservicename().getText().toString());
                }
                else {
                    a2.remove(holder.getservicename().getText().toString());
                }

                Log.d("CGHE","sizeee="+a2.size()+"added="+holder.getservicename().getText().toString());
            }

        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder  {
        private TextView servicename,price_and_duration,expandable_description;
        private CheckBox service_checkbox;

        public ViewHolder(View view) {
            super(view);
            servicename=view.findViewById(R.id.servicename_of_selected_shop);
            expandable_description=view.findViewById(R.id.expand_description_selected_shop);
            price_and_duration=view.findViewById(R.id.price_duration_of_selectedshop);
            service_checkbox=view.findViewById(R.id.service_checkbox);
//            expandable_description.setOnClickListener(this);
        }

        public TextView getservicename() {
            return servicename;
        }

        public TextView getPrice_and_duration() {
            return price_and_duration;
        }

        public TextView getExpandable_description() {
            return expandable_description;
        }

        public CheckBox getService_checkbox() {
            return service_checkbox;
        }
    }
}