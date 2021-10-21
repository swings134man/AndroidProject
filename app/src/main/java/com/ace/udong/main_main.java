package com.ace.udong;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class main_main extends AppCompatActivity {

Button btnWrite;
MainDB mainDB;

    // array
    ArrayList titleList = new ArrayList(); // 제목
    ArrayList imgList = new ArrayList(); // 이미지
    ArrayList locationList = new ArrayList(); // 지역
    ArrayList starList = new ArrayList(); // 별점

    //1번 레이아웃 배열.
    int[] ttt = new int[11];
    int[] idid = new int[11]; //id
    String[] tete = new String[11];
    String[] lolo = new String[11];
    String[] stst = new String[11];
    int count;

        //검색 레이아웃
        int[] ttt2 = new int[11];
        int[] idid2 = new int[11];
            String[] tete2 = new String[11];
            String[] lolo2 = new String[11];
            String[] stst2 = new String[11];
            int count2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_main);
        //액션바
        ActionBar ab = getSupportActionBar();
        ab.setTitle("우리 동네 합주실");
        mainDB = new MainDB(this); //DB 객
        // --------------------------------------------------------------------------------------------------------
        btnWrite = findViewById(R.id.btnWrite);// 버튼 객체
        // 앞에서 받아온 아이디 객체
        Intent intent = getIntent();
//        String mId = intent.getStringExtra("mId"); //일반
//        String cId = intent.getStringExtra("cId"); //사업자.
        // 테스트 아이디.
        String cId = "asd";
        String mId = "test1";

        if (cId != null){
            btnWrite.setVisibility(View.VISIBLE);
        }else {
            btnWrite.setVisibility(View.INVISIBLE);
        }
        // 글작성 버튼
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_main.this, insert_main.class);
                intent.putExtra("cId", cId);
                startActivity(intent);
            }
        });
        // --------------------------------------------------------------------------------------------------------
        //외부 레이아웃 파일 설정
        // 어디에 분리시켜놓은 layout 파일을 넣을지 결정!
        // 3번 레이아웃
        LinearLayout layout3 = findViewById(R.id.chat); //채팅 레이아웃.
        View chat = View.inflate(main_main.this, R.layout.test, null);
        layout3.addView(chat);
        // 4번 레이아웃
        LinearLayout layout4 = findViewById(R.id.mypage); //마이페이지 레이아웃.
        // -------------------------------------------------------------------------------------------------------
        // 1번 (main)페이지
        // 1번 레이아웃
        LinearLayout layout1 = findViewById(R.id.home); // 레이아웃 객체
        View listmain = View.inflate(main_main.this, R.layout.listmain, null); // 인플레이트
        layout1.addView(listmain); //레이아웃에 외부레이아웃 삽입
        // main 그리드뷰
        GridView gridview = findViewById(R.id.maingrid1); //그리드뷰
        // 어댑터
        ListAdapter la = new ListAdapter(this); //getView
//---------------------------------------------------------------------------------
        // 1번페이지 DB part
        SQLiteDatabase sqlDB = mainDB.getWritableDatabase();
        String sql = "select * from place;" ;
        //전체검색
        Cursor cursor = sqlDB.rawQuery(sql, null);
        count = 0; //동적 계산.
            for (int i = 0; i < cursor.getCount(); i++) {
                if (cursor.moveToNext()) {
                    // 배열
                    idid[i] = cursor.getInt(0); // id
                    tete[i] = cursor.getString(1); // 제목
                    stst[i] = cursor.getString(5); // 추천수
                    lolo[i] = cursor.getString(6); //지역
                    ttt[i] = cursor.getInt(4); //이미지.
                    count++;
                } // for
            } // if
        //---------------------------------------------------------------------------------
        gridview.setAdapter(la);    // 그리드뷰에 어댑터 이너클래스 주입.
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(main_main.this, detail_main.class);
                intent.putExtra("id", idid[position]);
                intent.putExtra("writer", cId);
                startActivity(intent);
            }
        });

        // --------------------------------------------------------------------------------------------------------
        // 2번 레이아웃 (검색)
        LinearLayout layout2 = findViewById(R.id.search); // main의 2번쨰 레이아웃.
        View searchView = View.inflate(main_main.this, R.layout.searchlist, null); // 2번탭 클릭시 searchlist.xml 주입.
        // 메인 안에 그리드뷰 insert
        GridView gridView2 = searchView.findViewById(R.id.maingrid2);   // 그리드뷰2 객체
        ListAdapter2 la2 = new ListAdapter2(this);                     // 외부 어댑터 객체 생성 (검색결과 어떻게 다룰지 고민)
        // 외부 레이아웃 내에 위젯 객체.
        ImageButton btnSearch = searchView.findViewById(R.id.btnSearch); //버튼
        EditText edtSearch = searchView.findViewById(R.id.edtSearch);  //검색창
        // --------------------------------------------------------------------------------------------------------
        // 2번페이지 DB part
        // 검색 버튼
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Log.d("array", "들어있는 : "+ tete2[0]);
                    Log.d("array", "들어있는 : "+ tete2[1]);
                count2 = 0; // 동적 계산.
                //화면 리프레쉬
                // ----
                String result = edtSearch.getText().toString(); // 인풋 텍스트 박스.
                //DB
                SQLiteDatabase sqlDB = mainDB.getWritableDatabase();
                String sql = "select * from place where title like '%" + result + "%'";
                //검색
                Cursor cursor = sqlDB.rawQuery(sql, null);
                //검색 결과 삽입.
                for (int i = 0; i < cursor.getCount(); i++) {
                    if (cursor.moveToNext()) {
                        // 배열
                        idid2[i] = cursor.getInt(0); // id
                        tete2[i] = cursor.getString(1); // 제목
                        stst2[i] = cursor.getString(5); // 추천수
                        lolo2[i] = cursor.getString(6); //지역
                        ttt2[i] = cursor.getInt(4); //이미지.
                        count2++;
                    } // for
                } // if
                // 새로고침.
                gridView2.deferNotifyDataSetChanged();
            gridView2.setAdapter(la2);                                      // 그리드뷰2 객체에 어댑터 추가
            }
        }); //button end
        gridView2.setAdapter(la2);                                      // 그리드뷰2 객체에 어댑터 추가
        layout2.addView(searchView);                                    // 레이아웃2에 외부 레이아웃 추가.

        // --------------------------------------------------------------------------------------------------------
        // 탭호스트
        TabHost tabHost = findViewById(R.id.tabhost);
        tabHost.setup();

        //이미지뷰
        //1
        ImageView iv1 = new ImageView(this);
        iv1.setImageResource(R.drawable.home);
        iv1.setLayoutParams(new ViewGroup.LayoutParams(370,270));
        iv1.setPadding(50,15,80,50);

        //2
        ImageView iv2 = new ImageView(this);
        iv2.setImageResource(R.drawable.search);
        iv2.setLayoutParams(new ViewGroup.LayoutParams(370,270));
        iv2.setPadding(50,15,80,50);
        //3
        ImageView iv3 = new ImageView(this);
        iv3.setImageResource(R.drawable.chat);
        iv3.setLayoutParams(new ViewGroup.LayoutParams(370,270));
        iv3.setPadding(50,15,80,50);
        //4
        ImageView iv4 = new ImageView(this);
        iv4.setImageResource(R.drawable.mypage);
        iv4.setLayoutParams(new ViewGroup.LayoutParams(370,270));
        iv4.setPadding(50,15,80,50);
        //각탭별 설정
        TabHost.TabSpec tabSpecHome = tabHost.newTabSpec("home").setIndicator(iv1);
        tabSpecHome.setContent(R.id.home);
        tabHost.addTab(tabSpecHome);

        TabHost.TabSpec tabSpecSearch = tabHost.newTabSpec("search").setIndicator(iv2);
        tabSpecSearch.setContent(R.id.search);
        tabHost.addTab(tabSpecSearch);

        TabHost.TabSpec tabSpecChat = tabHost.newTabSpec("chat").setIndicator(iv3);
        tabSpecChat.setContent(R.id.chat);
        tabHost.addTab(tabSpecChat);

        TabHost.TabSpec tabSpecMypage = tabHost.newTabSpec("mypage").setIndicator(iv4);
        tabSpecMypage.setContent(R.id.mypage);
        tabHost.addTab(tabSpecMypage);

        tabHost.setCurrentTab(0);
        // --------------------------------------------------------------------------------------------------------

    } //oncreate
    // --------------------------------------------------------------------------------------------------------
    // -----------------------------------------------class----------------------------------------------------
    // --------------------------------------------------------------------------------------------------------
    // 어댑터뷰 이너클래스 --> 1번 레이아웃 home page
    public class ListAdapter extends BaseAdapter {
        Context context;
        ListAdapter(Context c) {
            context = c;
        }
        @Override
        public int getCount() {
            return count;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int index, View convertView, ViewGroup parent) {
            // 반복되는 View 객체 생성 해줘야함. --> 추후 DB 내용 넣어야함.
            // 디자인 뷰 inflate
            View listmain = View.inflate(context, R.layout.listmain, null);
            ImageView imageList = listmain.findViewById(R.id.imageList);      //이미지 아이디
            TextView textTitle = listmain.findViewById(R.id.textTitle);       //제목 아이디
            TextView textLocation = listmain.findViewById(R.id.textLocation); //지역 아이디
            TextView starText = listmain.findViewById(R.id.starText);         //평점 아이디
            // set 기본 전역변수 부분.
//            imageList.setImageResource(productImg[index]);
//            textTitle.setText(title[index]);
//            textLocation.setText(location[index]);
//            starText.setText(star[index]);
//---------------------------------------------------------------------------------
            // 여기 열어
            // 배열 부분
            if (tete[index] != null) {
                textTitle.setText(tete[index]); //제목
                textLocation.setText(lolo[index]); //지역
                starText.setText(stst[index]);
//            imageList.setImageResource(productImg[index]);
                //이미지 분류
                for (int i = 0; i < ttt.length; i++) {
                    int num = ttt[i];
                    if (num == 1) {
                        imgResult[i] = R.drawable.img1;
                    } else if (num == 2) {
                        imgResult[i] = R.drawable.img2;
                    } else if (num == 3) {
                        imgResult[i] = R.drawable.img3;
                    } else if (num == 4) {
                        imgResult[i] = R.drawable.img4;
                    } else if (num == 5) {
                        imgResult[i] = R.drawable.img5;
                    } else if (num == 6) {
                        imgResult[i] = R.drawable.img6;
                    } else if (num == 7) {
                        imgResult[i] = R.drawable.img7;
                    } else if (num == 8) {
                        imgResult[i] = R.drawable.img8;
                    } else if (num == 9) {
                        imgResult[i] = R.drawable.img9;
                    } else if (num == 10) {
                        imgResult[i] = R.drawable.img10;
                    }

                }//for
                imageList.setImageResource(imgResult[index]);
            } //if
            return listmain;
        } // getView
    }//listAdapter1
//---------------------------------------------------------------------------------
        // 어댑터뷰 이너클래스 --> 2번 레이아웃 검색 부분
        public class ListAdapter2 extends BaseAdapter {

            Context context;

            ListAdapter2(Context c) {
                context = c;
            }

            @Override
            public int getCount() {
                return count2;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int index, View convertView, ViewGroup parent) {
                // 반복되는 View 객체 생성 해줘야함. --> 추후 DB 내용 넣어야함.
                // 디자인 뷰 inflate
                View listmain2 = View.inflate(context, R.layout.listmain2, null);

                ImageView imageList2 = listmain2.findViewById(R.id.imageList2);      //이미지 아이디
                TextView textTitle2 = listmain2.findViewById(R.id.textTitle2);       //제목 아이디
                TextView textLocation2 = listmain2.findViewById(R.id.textLocation2); //지역 아이디
                TextView starText2 = listmain2.findViewById(R.id.starText2);         //평점 아이디

                // set 기본 전역변수 부분.
//            imageList.setImageResource(productImg[index]);
//            textTitle.setText(title[index]);
//            textLocation.setText(location[index]);
//            starText.setText(star[index]);
//---------------------------------------------------------------------------------
                // 배열 부분
                if (tete2[index] != null) {
                    textTitle2.setText(tete2[index]); //제목
                    textLocation2.setText(lolo2[index]); //지역
                    starText2.setText(stst2[index]);
                    //이미지 분류
                    for (int i = 0; i < ttt2.length; i++) {
                        int num = ttt2[i];
                        if (num == 1) {
                            imgResult2[i] = R.drawable.img1;
                        } else if (num == 2) {
                            imgResult2[i] = R.drawable.img2;
                        } else if (num == 3) {
                            imgResult2[i] = R.drawable.img3;
                        } else if (num == 4) {
                            imgResult2[i] = R.drawable.img4;
                        } else if (num == 5) {
                            imgResult2[i] = R.drawable.img5;
                        } else if (num == 6) {
                            imgResult2[i] = R.drawable.img6;
                        } else if (num == 7) {
                            imgResult2[i] = R.drawable.img7;
                        } else if (num == 8) {
                            imgResult2[i] = R.drawable.img8;
                        } else if (num == 9) {
                            imgResult2[i] = R.drawable.img9;
                        } else if (num == 10) {
                            imgResult2[i] = R.drawable.img10;
                        }
                    }//for
                    imageList2.setImageResource(imgResult2[index]);
                }
                return listmain2;
            } // getview
        }// listadapter2
//---------------------------------------------------------------------------------
        // DB 에서 데이터 가져오는것으로 할것.
        // 이미지 배열
        int[] imgResult = new int[11];
        int[] imgResult2 = new int[11];
        int[] productImg = {
                R.drawable.img1,
                R.drawable.img2,
                R.drawable.img3,
                R.drawable.img4,
                R.drawable.img5,
                R.drawable.img6,
                R.drawable.img7,
                R.drawable.img8,
                R.drawable.img9,
                R.drawable.img10,
        };
    } //class