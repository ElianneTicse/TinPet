package com.example.tinpet.Adapter;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tinpet.Administrador.AdmiNuevoTipActivity;
import com.example.tinpet.Entity.Tip;
import com.example.tinpet.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TipListAdapter  extends FirestoreRecyclerAdapter<Tip,TipListAdapter.ViewHolder> {

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    int[] tipImage = {R.drawable.ic_bone,R.drawable.ic_dog_food,R.drawable.ic_star_2,R.drawable.ic_heart};

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TipListAdapter(@NonNull FirestoreRecyclerOptions<Tip> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Tip tip) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String id = documentSnapshot.getId();
        TextView tvTitulo = holder.itemView.findViewById(R.id.tvAdmiTipsTittleCard);
        TextView tvDescripcion = holder.itemView.findViewById(R.id.tvAdmiTipsDescriptionCard);
        ImageView ivItem = holder.itemView.findViewById(R.id.ivAdmiTipIconCard);
        Button btnActualizar = holder.itemView.findViewById(R.id.btnActualizarTipCard);
        Button btnDelete = holder.itemView.findViewById(R.id.btnEliminarTipCard);

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AdmiNuevoTipActivity.class);
                intent.putExtra("tip_id",id);
                v.getContext().startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTip(id,v);
            }
        });
        //seteamos info
        tvTitulo.setText(tip.getTitulo());
        tvDescripcion.setText(tip.getDescripcion());
        ivItem.setImageResource(tipImage[tip.getPosition()]);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_admi_tip,parent,false);
        return new ViewHolder(v);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        //Declaramos ids de items
        TextView tituloTip;
        TextView descripcionTip;
        ImageView imageTip;
        Button btnVerMasTip;
        Button btnDeleteTip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTip = itemView.findViewById(R.id.tvAdmiTipsTittleCard);
            descripcionTip = itemView.findViewById(R.id.tvAdmiTipsDescriptionCard);
            imageTip = itemView.findViewById(R.id.ivAdmiTipIconCard);
            btnVerMasTip = itemView.findViewById(R.id.btnActualizarTipCard);
            btnDeleteTip = itemView.findViewById(R.id.btnEliminarTipCard);
        }
    }

    private void deleteTip(String id,View view) {
        mFirestore.collection("tip").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(view.getContext(), "Se ha eliminado correctamente.", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("msg",e.getMessage());
                Toast.makeText(view.getContext(), "Ocurrió un error en el servidor", Toast.LENGTH_LONG).show();
            }
        });
    }
}
