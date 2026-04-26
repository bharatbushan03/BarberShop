package com.barbershop.app.custom_adapters;

        import android.content.Context;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.barbershop.app.R;
        import com.barbershop.app.userdetails.services;

        import java.util.ArrayList;

public class servicelist_adapter_inshopcard_view extends RecyclerView.Adapter<servicelist_adapter_inshopcard_view.ViewHolder> {

    private ArrayList<services> list;
    private Context context;


    public servicelist_adapter_inshopcard_view(ArrayList<services> listt, Context context){

        list=listt;
        Log.d("ASDAA", "sizeinserv_adap ="+list.size());
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

                    view = LayoutInflater.from(context)
                            .inflate(R.layout.serviceview_in_shop_cardview, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


                    holder.getServiceinfo_for_shopcardview().setText(list.get(position).getService_name()+" . "+list.get(position).getService_duration()+"mins . "+list.get(position).getService_price()+"$ ");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView serviceinfo_for_shopcardview;

        public ViewHolder(View view) {
            super(view);


                        serviceinfo_for_shopcardview=view.findViewById(R.id.service_info_inshopcard);
        }

        public TextView getServiceinfo_for_shopcardview() {
            return serviceinfo_for_shopcardview;
        }
    }
}
