package com.example.myapppost;

import static java.util.Locale.filter;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//검색기능
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private ArrayList<Post> postList;
    private ArrayList<Post> filteredList; // 필터링된 데이터를 담을 리스트(검색기능)
    private DatabaseHelper databaseHelper;

    private EditText etSearch;  //검색기능
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //검색기능
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etSearch = findViewById(R.id.etSearch);

        // 게시글 그리드 레이아웃
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));  // 두 개의 열로 구성된 그리드 레이아웃

        postList = new ArrayList<>();
        filteredList = new ArrayList<>();
        postAdapter = new PostAdapter(this, filteredList); // 필터링된 리스트로 Adapter 초기화
        recyclerView.setAdapter(postAdapter);

        databaseHelper = new DatabaseHelper(this);

        loadPosts();

        Button btnAddPost = findViewById(R.id.btnAddPost);
        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddPostActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        // EditText에서 검색어가 변경될 때마다 검색 기능을 수행
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString().trim();
                performSearch(searchText);
            }
        });
    }

    private void performSearch(String text) {
        filteredList.clear();

        if (text.isEmpty()) {
            filteredList.addAll(postList); // 검색어가 없을 경우 전체 리스트를 보여줌
        } else {
            for (Post post : postList) {
                // 게시물 제목 또는 내용에 검색어가 포함되어 있는지 확인
                if (post.getTitle().toLowerCase().contains(text.toLowerCase()) ||
                        post.getContent().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(post);
                }
            }
        }

        postAdapter.notifyDataSetChanged(); // Adapter에 데이터 변경 알림
    }



    private void filter(String text) {
        ArrayList<Post> filteredList = new ArrayList<>();

        for (Post post : postList) {
            // 게시물 제목 또는 내용에 검색어가 포함되어 있는지 확인
            if (post.getTitle().toLowerCase().contains(text.toLowerCase()) ||
                    post.getContent().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(post);
            }
        }

        postAdapter.notifyDataSetChanged(); // Adapter에 데이터 변경 알림
    }

    private void loadPosts() {
        postList.clear();
        Cursor cursor = databaseHelper.getAllPosts();
        if (cursor.moveToFirst()) {
            do {
                Post post = new Post(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                );
                postList.add(post);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // 검색어가 있으면 해당 검색어로 필터링
        String searchText = etSearch.getText().toString().trim();
        performSearch(searchText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 검색 아이콘을 클릭하면 EditText에 포커스를 줍니다.
        if (item.getItemId() == R.id.action_search) {
            etSearch.requestFocus();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadPosts();
        }
    }


}
