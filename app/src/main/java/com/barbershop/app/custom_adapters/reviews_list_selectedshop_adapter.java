package com.barbershop.app.custom_adapters;
 import android.content.Context;
           import android.net.Uri;
        import android.os.Handler;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.RatingBar;
        import android.widget.TextView;

        import com.barbershop.app.Apphomescreen;
        import com.barbershop.app.custHomeActivity;
        import com.barbershop.app.custom_adapters.shop_list_adapter;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import com.barbershop.app.R;
        import com.barbershop.app.userdetails.Shop;
 import com.barbershop.app.userdetails.reviewdetail_class;
 import com.barbershop.app.userdetails.services;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.squareup.picasso.Picasso;

        import java.security.Provider;
        import java.util.ArrayList;

        import de.hdodenhof.circleimageview.CircleImageView;

public class reviews_list_selectedshop_adapter extends RecyclerView.Adapter<reviews_list_selectedshop_adapter.ViewHolder> {

    private ArrayList<reviewdetail_class> list;
    private Context context;


    public reviews_list_selectedshop_adapter(ArrayList<reviewdetail_class> listt, Context context){
        list=listt;
        Log.d("RRRRRR",listt.size()+"=size");
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.reviews_layout_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.getCust_name().setText(list.get(position).getCust_name());
        holder.getFeedbacktext().setText("\""+list.get(position).getFeedbacktext()+"\"");
        if(!list.get(position).getImg1().equals("") && !list.get(position).getImg2().equals("") && !list.get(position).getImg3().equals("") && !list.get(position).getImg4().equals("") )
        {        Picasso.get().load(list.get(position).getImg1()).into(holder.getReview_img1());
        Picasso.get().load(list.get(position).getImg2()).into(holder.getReview_img2());
        Picasso.get().load(list.get(position).getImg3()).into(holder.getReview_img3());
        Picasso.get().load(list.get(position).getImg4()).into(holder.getReview_img4());}
        if(list.get(position).getRating()!=null){
            holder.getRatings().setText(list.get(position).getRating().toString());
            holder.getR().setRating(list.get(position).getRating());
        }


        // holder.getShop_image().setImageURI(Uri.parse(list.get(position).getShop_profile_pic()));
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

        private TextView cust_name,ratings,feedbacktext;
        private RatingBar r;
        private ImageView review_img1,review_img2,review_img3,review_img4;

        public ViewHolder(View view) {
            super(view);
            cust_name=view.findViewById(R.id.cust_Name_review_card);
            ratings=view.findViewById(R.id.Shop_Ratings_review_card);
            r=view.findViewById(R.id.ratingBar_review_card);
            review_img1=view.findViewById(R.id.img1_review_card);
            review_img2=view.findViewById(R.id.img2_review_card);
            review_img3=view.findViewById(R.id.img3_review_card);
            review_img4=view.findViewById(R.id.img4_review_card);
            feedbacktext=view.findViewById(R.id.feedbacktext_review_card);
        }

        public TextView getCust_name() {
            return cust_name;
        }

        public TextView getRatings() {
            return ratings;
        }

        public TextView getFeedbacktext() {
            return feedbacktext;
        }

        public RatingBar getR() {
            return r;
        }

        public ImageView getReview_img1() {
            return review_img1;
        }

        public ImageView getReview_img2() {
            return review_img2;
        }

        public ImageView getReview_img3() {
            return review_img3;
        }

        public ImageView getReview_img4() {
            return review_img4;
        }
    }
}
