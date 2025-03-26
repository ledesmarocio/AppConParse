package com.example.appconparse.adapters;
import com.example.appconparse.R;
import com.example.appconparse.model.Post;
import com.example.appconparse.model.User;
import com.example.appconparse.providers.PostProvider;
import com.example.appconparse.view.PostDetailActivity;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> posts;
    private Context context; //-----------ESTO TOQUE


    public PostAdapter(List<Post> posts){

        this.posts= posts;
    }

    public void setContext(Context context) {
        this.context = context; // ESTO TOQUEE
    }


    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post,parent,false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position){
        Post post=posts.get(position);
        holder.tvTitulo.setText(post.getTitulo());
        holder.tvDescripcion.setText(post.getDescripcion());

        if(post.getImagenes()!=null){
            if(post.getImagenes().size()>0){
                Picasso.get()
                        .load(post.getImagenes().get(0))
                        .into(holder.ivImage1);
                holder.ivImage1.setVisibility(View.VISIBLE);
            }

            if(post.getImagenes().size()>1){
                Picasso.get()
                        .load(post.getImagenes().get(1))
                        .into(holder.ivImage2);
                holder.ivImage2.setVisibility(View.VISIBLE);
            }
            if (post.getImagenes().size()>2) {
                Picasso.get()
                        .load(post.getImagenes().get(2))
                        .into(holder.ivImage3);
                holder.ivImage3.setVisibility(View.VISIBLE);
            }
        }
        holder.itemView.setOnClickListener(v->{
            Context context= holder.itemView.getContext();
            PostProvider postProvider= new PostProvider();

            LiveData<Post> postDetailLiveData= postProvider.getPostDetail(post.getId());
            postDetailLiveData.observe((LifecycleOwner) context, postDetail-> {
                if(postDetail != null){
                    //Log.d("Postadapter", postDetail.getId() + postDetail.getTitulo());
                    Intent intent= new Intent(context, PostDetailActivity.class);
                    //Datos del Post
                    // Log.d("Postadapter", postDetail.getId() + postDetail.getTitulo());
                    intent.putExtra("idPost", post.getId());
                    intent.putExtra("titulo", postDetail.getTitulo());
                    intent.putExtra("descripcion",postDetail.getDescripcion());
                    intent.putExtra("categoria",postDetail.getCategoria());
                    intent.putExtra("duracion",postDetail.getDuracion());
                    intent.putExtra("presupuesto",postDetail.getPresupuesto());

                    //Datos del Usuario
                    User user= postDetail.getUser();
                    if(user !=null){
                        Log.d("Postadapter", user.getUsername());
                        intent.putExtra("username", user.getUsername());
                        intent.putExtra("email", user.getEmail());
                        intent.putExtra("redsocial", user.getRedSocial());
                        intent.putExtra("foto_perfil", user.getFotoperfil());
                        Log.d("FotoPerfil", "foto" +user.getFotoperfil());
                    }else{
                        Log.d("Postadapter", "User is null");
                    }
                    //Lista de imágenes
                    ArrayList<String> imageUrls= new ArrayList<>(postDetail.getImagenes());
                    intent.putStringArrayListExtra("imagenes",imageUrls);

                    // Lanza la actividad
                    context.startActivity(intent);
                }else{
                    Log.e("PostDetail", "No se pudo obtener el detalle del post.");
                }
            });
        });
    }

    @Override
    public int getItemCount(){
        return posts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitulo, tvDescripcion;
        ImageView ivImage1, ivImage2, ivImage3;

        public PostViewHolder(View itemView){
            super(itemView);
            tvTitulo= itemView.findViewById(R.id.tvTitulo);
            tvDescripcion=itemView.findViewById(R.id.tvDescripcion);
            ivImage1=itemView.findViewById(R.id.ivImage1);
            ivImage2=itemView.findViewById(R.id.ivImage2);
            ivImage3=itemView.findViewById(R.id.ivImage3);
        }
    }
    public void setPosts(List<Post> newPosts) {
        posts.clear();
        posts.addAll(newPosts);
        notifyDataSetChanged();
    }

}
