package indwin.c3.shareapp.activities;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;


import indwin.c3.shareapp.ProductsPage;
import indwin.c3.shareapp.R;
import indwin.c3.shareapp.Splash;
import indwin.c3.shareapp.adapters.SearchedProductListAdapter;
import indwin.c3.shareapp.models.RecentSearchItems;
import indwin.c3.shareapp.utils.AppUtils;
import io.intercom.android.sdk.Intercom;

public class FindProduct extends AppCompatActivity {

    ArrayList<RecentSearchItems> recentSearchItemsArrayList = new ArrayList<RecentSearchItems>();
    EditText search;
    ImageView taptoSearch;
    ImageView backButton;
    private String page = "";
    private String productId = "";
    SearchedProductListAdapter adp;
    TextView textView;
    RelativeLayout productbox;
    String userId = "";
    ListView recyclerView;
    SharedPreferences sharedPreferences2;
    private android.content.ClipboardManager myClipboard;
    private int checkValidUrl = 0, monthsallowed = 0;
    private Double emi = 0.0;
    public static final String MyPREFERENCES = "buddy";
    private int checkValidFromApis = 0;
    private String sellerNme = "";
    SharedPreferences sharedpreferences;
    public static boolean linkpressed = false;
    public static boolean validUrl = false;


    @Override
    protected void onStart() {
        super.onStart();
//        if(taptoSearch!=null)
//            if(taptoSearch.getVisibility() ==View.VISIBLE)
//                taptoSearch.setVisibility(View.INVISIBLE);
        Log.i("VISIBLE","called");
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        setContentView(R.layout.activity_find_product);
        search = (EditText)findViewById(R.id.link);
        search.setInputType(InputType.TYPE_NULL);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences2 = getSharedPreferences("buddyin", Context.MODE_PRIVATE);
        if (sharedpreferences.getInt("checklog", 0) == 1) {
            userId = sharedPreferences2.getString("name", null);
        }

        taptoSearch = (ImageView)findViewById(R.id.pasteAg);
        myClipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        taptoSearch.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               try {

                   search.requestFocus();
                   search.setText("");
                   ClipData abc = myClipboard.getPrimaryClip();
                   ClipData.Item item = abc.getItemAt(0);
                   String text = item.getText().toString();


                   search.setText("   " + text);

                  // taptoSearch.setVisibility(View.GONE);

               } catch (Exception e) {
                   Toast.makeText(FindProduct.this, "Please copy a URL", Toast.LENGTH_SHORT).show();
               }
           }
       });

        String price = getIntent().getStringExtra("price");



        backButton = (ImageView)findViewById(R.id.backo);
       // recentSearchItemsArrayList = (ArrayList<RecentSearchItems>) getIntent().getSerializableExtra("searchlist");
        textView = (TextView)findViewById(R.id.txtheader);
//        if(recentSearchItemsArrayList.size()==0)
//            textView.setVisibility(View.GONE);
//        else
//            textView.setVisibility(View.VISIBLE);
        productbox = (RelativeLayout) findViewById(R.id.product_box);

        recyclerView = (ListView) findViewById(R.id.products_list);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //adp = new SearchedProductListAdapter(recentSearchItemsArrayList,this);

        new FindRecentProductLinks(FindProduct.this).execute(getApplicationContext().getResources().getString(R.string.server)+"api/user/product/recent?userid="+userId +"&count=5");

//        if(ProductsPage.backpressed){
//            RecentSearchItems items = new RecentSearchItems(ProductsPage.brand1,ProductsPage.title1,Integer.toString(ProductsPage.price),productId);
//            adp.addObject(items);
//            adp.notifyDataSetChanged();
//            search.setText("");
//            if(taptoSearch.getVisibility()==View.INVISIBLE)
//                taptoSearch.setVisibility(View.VISIBLE);
//        }
        //recyclerView.setAdapter(adp);

//        taptoSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//
//                    search.requestFocus();
//                    ClipData abc = myClipboard.getPrimaryClip();
//                    ClipData.Item item = abc.getItemAt(0);
//                    String text = item.getText().toString();
//
//
//                    search.setText("   " + text);
//
//                    taptoSearch.setVisibility(View.GONE);
//
//                } catch (Exception e) {
//                    Toast.makeText(FindProduct.this, "Please copy a URL", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                    if (s.length() != 0) {
//                        try {
//                            URL url = new URL(s.toString());
//                        } catch (MalformedURLException e) {
//                            e.getMessage();
//                            Log.i("Invalid URL","Invalid");
//                            Toast.makeText(getApplicationContext(),"Please paste valid URL",Toast.LENGTH_SHORT).show();
//                            return;
//                        }

                       // validUrl = URLUtil.isValidUrl(s.toString());


                            Splash.checkNot = 1;

                            search.requestFocus();
                            //clickpaste();
                            parse(search.getText().toString().trim());

                            if ((checkValidUrl == 0) && (checkValidFromApis == 0)) {
                                Long time = Calendar.getInstance().getTimeInMillis() / 1000;
                                Intent in = new Intent(FindProduct.this, ProductsPage.class);
                                in.putExtra("seller", sellerNme);
                                in.putExtra("product", productId);
                                in.putExtra("query", search.getText().toString());
                                //adp.addObject(new RecentSearchItems());
                                //search.setText("");
                                in.putExtra("page", "api");
                                checkValidFromApis = 0;
                                checkValidUrl = 0;
                                startActivity(in);
                                //                            if (time + 5 < userP.getLong("expires", 0))
                                ////                                new checkAuth().execute(url);//
                                //                            {
                                //                                new linkSearch().execute();
                                //                            } else
                                //                                //   new checkAuth().execute(url);
                                //                                new AuthTokc().execute("cc");

                            } else if (checkValidUrl == 1) {
                                //monkey page
                                Intent in = new Intent(FindProduct.this, ProductsPage.class);
                                //search.setText("");
                                in.putExtra("query", search.getText().toString());
                                in.putExtra("page", "monkey");
                                startActivity(in);
                                checkValidFromApis = 0;
                                checkValidUrl = 0;
                                //                            finish();
                                page = "monkey";
                            } else if ((checkValidFromApis == 1)) {
                                //not monley page
                                //search.setText("");
                                Intent in = new Intent(FindProduct.this, ProductsPage.class);
                                in.putExtra("query", search.getText().toString());
                                in.putExtra("seller", sellerNme);
                                in.putExtra("page", "pay");
                                startActivity(in);
                                checkValidFromApis = 0;
                                checkValidUrl = 0;
                                //                            finish();
                                page = "pay";
                            }



                    }

            }
        });

//        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//
//
//
//
//                    //                        Intent in = new Intent(HomePage.this, ViewForm.class);
//                    Splash.checkNot = 1;
//
//                    search.requestFocus();
//                    clickpaste();
//                    parse(search.getText().toString().trim());
//
//                    if ((checkValidUrl == 0) && (checkValidFromApis == 0)) {
//                        Long time = Calendar.getInstance().getTimeInMillis() / 1000;
//                        Intent in = new Intent(FindProduct.this, ProductsPage.class);
//                        in.putExtra("seller", sellerNme);
//                        in.putExtra("product", productId);
//                        in.putExtra("query", search.getText().toString());
//                        //adp.addObject(new RecentSearchItems());
//                        search.setText("");
//                        in.putExtra("page", "api");
//                        checkValidFromApis = 0;
//                        checkValidUrl = 0;
//                        startActivity(in);
//                        //                            if (time + 5 < userP.getLong("expires", 0))
//                        ////                                new checkAuth().execute(url);//
//                        //                            {
//                        //                                new linkSearch().execute();
//                        //                            } else
//                        //                                //   new checkAuth().execute(url);
//                        //                                new AuthTokc().execute("cc");
//
//                    } else if (checkValidUrl == 1) {
//                        //monkey page
//                        Intent in = new Intent(FindProduct.this, ProductsPage.class);
//                        search.setText("");
//                        in.putExtra("query", search.getText().toString());
//                        in.putExtra("page", "monkey");
//                        startActivity(in);
//                        checkValidFromApis = 0;
//                        checkValidUrl = 0;
//                        //                            finish();
//                        page = "monkey";
//                    } else if ((checkValidFromApis == 1)) {
//                        //not monley page
//                        search.setText("");
//                        Intent in = new Intent(FindProduct.this, ProductsPage.class);
//                        in.putExtra("query", search.getText().toString());
//                        in.putExtra("seller", sellerNme);
//                        in.putExtra("page", "pay");
//                        startActivity(in);
//                        checkValidFromApis = 0;
//                        checkValidUrl = 0;
//                        //                            finish();
//                        page = "pay";
//                    }
//                    //                        in.putExtra("url", query.getText().toString());
//                    //                        in.putExtra("which_page", 9);
//                    //                        startActivity(in);
//                    //  Toast.makeText(HomePage.this,"oscar goes to caprio",Toast.LENGTH_LONG).show();
//                    //TODO: do something
//
//
//                return false;
//            }
//        });
        //getTotalHeightofListView();


        //adjustListHeight();


        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecentSearchItems recentSearchItems = recentSearchItemsArrayList.get(position);
                Intent intent = new Intent(FindProduct.this, ProductsPage.class);
                intent.putExtra("seller",recentSearchItems.getSeller());
                intent.putExtra("product",recentSearchItems.getProductId());
                intent.putExtra("page","");
                linkpressed =true;
                startActivity(intent);
            }
        });
//        recyclerView.addOnItemTouchListener(
//                new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
//                    @Override
//                    public void onClick(View view, int position) {
//                        RecentSearchItems recentSearchItems = recentSearchItemsArrayList.get(position);
//                        Intent intent = new Intent(FindProduct.this, ProductsPage.class);
//                        intent.putExtra("seller",recentSearchItems.getSeller());
//                        intent.putExtra("product",recentSearchItems.getProductId());
//                        intent.putExtra("page","");
//                        startActivity(intent);
//                    }
//
//                    @Override
//                    public void onLongClick(View view, int position) {
//
//                    }
//                })
//        );
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        ImageView inter = (ImageView) findViewById(R.id.interCom);
        inter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intercom.client().displayMessageComposer();
                } catch (Exception e) {

                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        search.setText("");
        taptoSearch.setVisibility(View.VISIBLE);
        if(recentSearchItemsArrayList.size()==0)
            textView.setVisibility(View.INVISIBLE);
        if(ProductsPage.backpressed){
            //RecentSearchItems items = new RecentSearchItems(ProductsPage.brand1,ProductsPage.title1,Integer.toString(ProductsPage.price),productId);
            if(adp!=null){
            //adp.addObject(items);
           // textView.setVisibility(View.VISIBLE);
            adp.notifyDataSetChanged();



            ProductsPage.backpressed = false;
        }}
    }

    public void clickpaste() {
        taptoSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    search.requestFocus();
                    ClipData abc = myClipboard.getPrimaryClip();
                    ClipData.Item item = abc.getItemAt(0);
                    String text = item.getText().toString();


                    search.setText("   " + text);

                    taptoSearch.setVisibility(View.GONE);

                } catch (Exception e) {
                    Toast.makeText(FindProduct.this, "Please copy a URL", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void parse(String parseString) {
        SharedPreferences cred = getSharedPreferences("cred", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = cred.edit();
        et.putString("urlprod",parseString);
        et.commit();
        productId = "";
        int pos = -1;
        if (parseString.contains("flipkart")) {
            sellerNme = "flipkart";
            pos = parseString.indexOf("pid");
            if (pos != -1) {
                for (int j = pos + 4; ; j++) {
                    if (parseString.charAt(j) == '&')
                        break;
                    else {
                        productId += parseString.charAt(j);
                    }

                }
            } else {
                checkValidUrl = 1;
            }

        }
        //snapdeal


        else if (parseString.contains("snapdeal")) {
            sellerNme = "snapdeal";
            pos = parseString.lastIndexOf("/");
            if (pos != -1) {

                for (int j = pos + 1; j < parseString.length(); j++) {
                    if (((parseString.charAt(j)) >= '0') && (parseString.charAt(j) <= '9'))

                        productId += parseString.charAt(j);
                    else break;


                }
            } else {
                checkValidUrl = 1;
            }
        } else if (parseString.contains("myntra")) {
            sellerNme = "myntra";
            checkValidFromApis = 1;
        } else if (parseString.contains("shopclues")) {
            sellerNme = "shopclues";
            checkValidFromApis = 1;
        } else if (parseString.contains("jabong")) {
            sellerNme = "jabong";
            checkValidFromApis = 1;

        } else if (parseString.contains("paytm")) {
            sellerNme = "paytm";
            checkValidFromApis = 1;
        }
        //amazon
        else if (parseString.contains("amazon")) {
            sellerNme = "amazon";
            int w = 0;
            pos = parseString.indexOf("/dp/");
            if (pos != -1) {
                pos = parseString.indexOf("dp");
            }

            if (pos == -1) {
                pos = parseString.indexOf("/product/");
                if (pos != -1)
                    w = 1;
            }
            if (pos == -1) {

                pos = parseString.indexOf("/d/");
                if (pos != -1)
                    w = 2;
            }


            if (pos != -1) {
                int r = 0;
                if (w == 0)
                    r = pos + 3;
                if (w == 1)
                    r = pos + 9;
                if (w == 2)
                    r = pos + 3;
                for (int j = r; ; j++) {
                    if ((parseString.charAt(j) == '/') || ((parseString.charAt(j) == '?')))
                        break;
                    else {
                        productId += parseString.charAt(j);
                    }

                }
            } else {
                checkValidUrl = 1;
            }

        } else if (parseString.contains("shopclues"))
            checkValidFromApis = 1;
        else
            checkValidUrl = 1;

        if ((checkValidFromApis == 0) && (checkValidUrl == 0)) {
            //make api call
        }
        if ((checkValidFromApis == 1)) {
            //not monley page
        }
        if (checkValidUrl == 1) {
            //monkey page
        }
        //
        //       Toast.makeText(HomePage.this, productId, Toast.LENGTH_SHORT).show();

    }

//    private void adjustListHeight(){
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        params.height = getTotalHeightofListView() + textView.getHeight();
//        productbox.setLayoutParams(params);
//        productbox.requestLayout();
//      //  commentBox.setLayoutParams(params);
//
//    }

//    private void getTotalHeightofListView() {
//        if(adp.getCount() > 2){
//            View item = adp.getView(0, null, recyclerView);
//            item.measure(0, 0);
////            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) (2.5 * item.getMeasuredHeight()));
////            recyclerView.setLayoutParams(params);
//
//        }
//    }

//    public interface ClickListener {
//        void onClick(View view, int position);
//
//        void onLongClick(View view, int position);
//    }
//
//    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
//
//        private GestureDetector gestureDetector;
//        private FindProduct.ClickListener clickListener;
//
//        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final FindProduct.ClickListener clickListener) {
//            this.clickListener = clickListener;
//            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
//                @Override
//                public boolean onSingleTapUp(MotionEvent e) {
//                    return true;
//                }
//
//                @Override
//                public void onLongPress(MotionEvent e) {
//                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
//                    if (child != null && clickListener != null) {
//                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
//                    }
//                }
//            });
//        }

//        @Override
//        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//
//            View child = rv.findChildViewUnder(e.getX(), e.getY());
//            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
//                clickListener.onClick(child, rv.getChildPosition(child));
//            }
//            return false;
//        }
//
//        @Override
//        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//        }
//
//        @Override
//        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//        }
//    }
private class FindRecentProductLinks extends AsyncTask<String,Void,String> {

    Context ctx;
    public FindRecentProductLinks(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        JSONObject payload = new JSONObject();
        try {

            try {

            } catch (Exception e) {
                System.out.println("dio " + e.toString());
            }
            //SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);

            SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
            String tok_sp = toks.getString("token_value", "");
            //String tok_sp = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfaWQiOiI1NjY1M2M2YTUwZTQzNzgyNjc0M2YyNjYiLCJuYW1lIjoiYnVkZHkgYXBpIGFkbWluIiwidXNlcm5hbWUiOiJidWRkeWFwaWFkbWluIiwicGFzc3dvcmQiOiJtZW1vbmdvc2gxIiwiZW1haWwiOiJjYXJlQGhlbGxvYnVkZHkuaW4iLCJpYXQiOjE0NjU1NDQwMDgsImV4cCI6MTQ2NTU4MDAwOH0.ZpAwCEB0lYSqiYdfaBYjnBJOXfGrqE9qN8USoRzWR8g";
            HttpResponse response = AppUtils.connectToServerGet(params[0], tok_sp, null);
            if (response != null) {
                HttpEntity ent = response.getEntity();
                String responseString = EntityUtils.toString(ent, "UTF-8");

                if (response.getStatusLine().getStatusCode() != 200) {

                    Log.e("MeshCommunication", "Server returned code in Recent "
                            + response.getStatusLine().getStatusCode());
                    return "fail";
                }else {
                    try {
                        Log.i("Running","Recents");
                        JSONObject jsonObject = new JSONObject(responseString);
                        JSONArray data = jsonObject.getJSONArray("data");
                        recentSearchItemsArrayList = new ArrayList<>();
                        for(int i=0;i<data.length();i++){
                            try {
                                JSONObject jsonObject1 = data.getJSONObject(i);
                                String seller = jsonObject1.getString("seller");
                                String title = jsonObject1.getString("title");
                                String price = jsonObject1.getString("sellingPrice");
                                String productId = jsonObject1.getString("fkProductId");
                                RecentSearchItems recentSearchItems = new RecentSearchItems(seller, price, title, productId);
                                recentSearchItemsArrayList.add(recentSearchItems);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        return "win";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        adp = new SearchedProductListAdapter(recentSearchItemsArrayList,ctx);
        recyclerView.setAdapter(adp);
        if(recentSearchItemsArrayList.size()==0)
            textView.setVisibility(View.INVISIBLE);
        else
            textView.setVisibility(View.VISIBLE);



    }
}

}
