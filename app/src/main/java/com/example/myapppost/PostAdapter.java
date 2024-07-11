package com.example.myapppost;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private ArrayList<Post> postList;
    private Context context;

    private ArrayList<Post> filteredList; // 필터링된 데이터를 담을 리스트(검색기능)

    private DatabaseHelper databaseHelper;

    public PostAdapter(Context context, ArrayList<Post> postList) {
        this.context = context;
        this.postList = postList;
        this.filteredList = new ArrayList<>(postList); // 초기화 시 postList를 filteredList로 복사
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.tvTitle.setText(post.getTitle());
        holder.tvContent.setText(post.getContent());
        // 썸네일 이미지를 설정하는 부분 (현재는 기본 배경색으로 설정되어 있음)
        holder.ivThumbnail.setImageResource(R.drawable.ic_placeholder); // 기본 이미지 설정


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra("title", post.getTitle());
                intent.putExtra("content", post.getContent());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent;
        ImageView ivThumbnail;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
        }
    }

    // 외부에서 호출하여 필터링된 데이터로 업데이트하는 메소드
    public void filterList(ArrayList<Post> filteredList) {
        this.postList = filteredList; // 필터링된 리스트로 업데이트
        notifyDataSetChanged(); // RecyclerView 갱신
    }
}