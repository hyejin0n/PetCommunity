package com.example.myapppost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PostDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvContent;
    private int postId; // 게시물의 ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        tvTitle = findViewById(R.id.tvTitle);
        tvContent = findViewById(R.id.tvContent);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        postId = intent.getIntExtra("id", -1); // 게시물의 ID를 가져옴

        tvTitle.setText(title);
        tvContent.setText(content);
    }
    public void editPost(View view) {
        Intent intent = new Intent(PostDetailActivity.this, AddPostActivity.class);
        intent.putExtra("id", postId); // 수정할 게시물의 ID를 전달
        intent.putExtra("title", tvTitle.getText().toString());
        intent.putExtra("content", tvContent.getText().toString());
        startActivity(intent);
    }

    public void deletePost(View view) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        boolean success = databaseHelper.deletePost(postId);
        if (success) {
            Toast.makeText(this, "게시물이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            // 화면을 업데이트하거나 필요한 처리를 추가할 수 있음
            finish(); // 현재 Activity를 종료하여 이전 화면으로 돌아감
        } else {
            Toast.makeText(this, "게시물 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}