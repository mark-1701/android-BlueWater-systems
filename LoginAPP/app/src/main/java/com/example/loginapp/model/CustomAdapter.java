package com.example.loginapp.model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.loginapp.R;
import com.example.loginapp.activitys.InformationViewActivity;
import com.example.loginapp.database.DatabaseUsers;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHOlder> {
    private List<User> cardItemList;
    private Context context;

    public CustomAdapter(List<User> cardItemList, Context context) {
        this.cardItemList = cardItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_layout, parent, false);
        return new ViewHOlder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHOlder holder, int position) {
        // cargar la informacion
        User cardItem = cardItemList.get(position);

        // Aquí puedes obtener las vistas dentro del CardView y establecer los datos
        TextView textViewName = holder.cardView.findViewById(R.id.textViewNameCV);
        TextView textViewEmail = holder.cardView.findViewById(R.id.textViewEmailCV);
        TextView textViewPassword = holder.cardView.findViewById(R.id.textViewPasswordCV);

        textViewName.setText(cardItem.getName());
        textViewEmail.setText(cardItem.getEmail());
        textViewPassword.setText(cardItem.getPassword());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), InformationViewActivity.class);
                holder.itemView.getContext().startActivity(intent);
                DatabaseUsers.globalUser = cardItem;
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardItemList.size();
    }

    public class ViewHOlder extends RecyclerView.ViewHolder {
        public CardView cardView;

        public ViewHOlder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }
    }
}

