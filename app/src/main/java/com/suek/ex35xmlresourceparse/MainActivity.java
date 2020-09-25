package com.suek.ex35xmlresourceparse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // 1)
    ListView listView;      // 1 현수막
    ArrayAdapter adapter;   // 2 현수막회사와 인쇄기(Textview 하나일때만 ArrayAdapter 사용)

    ArrayList<String> items= new ArrayList<>();   // 3 데이터들(기차)  //items=데이터임
    // 4 listview_item 시안/설계도


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //리스트뷰의 '테스트목적'으로 item 에 데이터를 추가
        /*items.add( new String("aaa") );
        items.add( new String("bbb") );*/

        // 2)
        listView= findViewById(R.id.listview);
        adapter= new ArrayAdapter(this, R.layout.listview_item, items);
        listView.setAdapter(adapter);
    }




    public void clickBtn(View view) {
        /*items.add( new String("ccc") ); //버튼을 누르면 Arraylist 에 'item' 을 추가
        adapter.notifyDataSetChanged();*/

        // 3) res/xml/movies.xml 을 읽어와서 분석(parse)하여 ListView 의 데이터-item(Arraylist 의)에 추가

        // 3.2) res 창고를 관리하는 창고관리자 객체소환
        Resources res= getResources();

        // 3.1) res 폴더안의 xml 문서를 읽어와서 분석해주는 분석가 객체 소환
        XmlResourceParser xrp= res.getXml(R.xml.movies);

        // 3.3) 분석가 객체(xrp)를 통해 xml 문서를 분석하기
        try {
            xrp.next();   // 문서의 다음 커서로 이동
            int eventType= xrp.getEventType();    // eventType= xrp.next(); 과 같은뜻임

            // 3.6)
            String item="";  //빈문자열객체 생성

            // 3.5) evenType 의 문서의 끝(END_DOCUMENT)이 아니면 반복해라
            while( eventType != XmlResourceParser.END_DOCUMENT){

                switch (eventType){
                    case XmlResourceParser.START_DOCUMENT:
                        Toast.makeText(this, "분석을 시작합니다", Toast.LENGTH_SHORT).show();
                        break;

                    case XmlResourceParser.START_TAG:
                        // 3.7)
                        //START_TAG 시작태그문에 써있는 글씨 얻어오기 (어떤 스타트태그인지 모르니까)
                        String name_starttag= xrp.getName();  //태그문의 이름
                        if(name_starttag.equals("item")){
                            item="";  //영화 1개의 정보 시작문자열

                        }else if(name_starttag.equals("no")){
                            item= item + "번호 : ";

                        }else if(name_starttag.equals("title")){
                            item= item + "제목 : ";

                        }else if(name_starttag.equals("genre")){
                            item= item + "장르 : ";
                        }
                        break;

                    case XmlResourceParser.TEXT:
                        //텍스트 글씨 얻어오기
                        String text= xrp.getText();
                        item= item + text;        //"번호 : " + "1"; =누적됨
                        break;

                    case XmlResourceParser.END_TAG:
                        String name_endtag= xrp.getName();
                        if( name_endtag.equals("no") ){
                            item= item + "\n";

                        }else if(name_endtag.equals("title")){
                            item += "\n";

                        }else if(name_endtag.equals("item")){
                            //하나의 영화 아이템이 끝났으므로
                            //리스트뷰가 보여주는 대량의 데이터(items)에 추가
                            items.add(item);
                            adapter.notifyDataSetChanged();
                        }
                        break;

                    case XmlResourceParser.END_DOCUMENT:
                }
                // 3.4)
                eventType= xrp.next();  //다음커서로 이동하면서 그 숫자를 줌

            }//while...

            // 3.8)
            Toast.makeText(this, "분석을 끝났습니다.",Toast.LENGTH_SHORT).show();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }
}
