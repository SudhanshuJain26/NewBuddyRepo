package indwin.c3.shareapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Aniket Verma(Digo) on 4/24/2016.
 */
public class Adaptersimple extends BaseAdapter {
    private final Context context;
    int selectedPosition;
    private RadioButton checkR;
    String t="",editnow="",getli="",sedt1="",sedt2="",sedt3="",sedt4="";
    private ImageView im;
    private EditText edt1, edt2, edt3, edt4;
    View rowView, vi;
    int Check = 0;
    int del=0;
    TextView textAdd1,textAdd2,textAdd3,textAdd4,delete;
    Deladd add;
    private SharedPreferences prefs;
    //    private final String[] values;//
    EditText e1;
    ArrayList<Deladd> myList = new ArrayList<Deladd>();
    ArrayList<Deladd> myListnew = new ArrayList<Deladd>();
    LayoutInflater inflater;
    View check;
    private ImageView edit;

    public Adaptersimple(Context context, ArrayList<Deladd> myList, int Check) {
//        super(context,values);
        this.context = context;
        this.Check = Check;
        this.myList = myList;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public String getItem(int position) {
        return myList.get(position).toString();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        int t = position;
        rowView = inflater.inflate(R.layout.listelementsaddress, parent, false);
        final View rowView2 = LayoutInflater.from(context).inflate(R.layout.activity_editaddress, parent, false);
        textAdd1 = (TextView) rowView.findViewById(R.id.addRess1);
        textAdd2 = (TextView) rowView.findViewById(R.id.addRess2);
        textAdd3 = (TextView) rowView.findViewById(R.id.addRess3);
//        textAdd4 = (TextView) rowView.findViewById(R.id.addRess4);
        final RelativeLayout rradd = (RelativeLayout) rowView.findViewById(R.id.rradd);
        final TextView textView3 = (TextView) rowView.findViewById(R.id.addRess3);
        final TextView textView2 = (TextView) rowView.findViewById(R.id.addRess2);
        final ImageView imicon = (ImageView) rowView.findViewById(R.id.img);
         edit = (ImageView) rowView.findViewById(R.id.edit);
        final RadioButton rd = (RadioButton) rowView.findViewById(R.id.radioAdd);
        SharedPreferences cred = context.getSharedPreferences("cred", Context.MODE_PRIVATE);
        if(position==0){
            SharedPreferences.Editor e1=cred.edit();
            String cc=myList.get(position).getLine1().toString();
            e1.putString("address",cc);
            e1.commit();
            rd.setChecked(true);
            checkR = (RadioButton) rowView.findViewById(R.id.radioAdd);

        }
        check=rowView;
        rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(getli.contains("address"))
//                edit.setVisibility(View.GONE);

                {
                    if (checkR == null) {
                        View r=rowView.findViewById(R.id.edit);
                        im = (ImageView) check.findViewById(R.id.edit);
//r.setVisibility(View.GONE);

                        checkR = (RadioButton) v.findViewById(R.id.radioAdd);
                       // im.setVisibility(View.VISIBLE);
                        checkR.setChecked(true);
                    }

                    if (checkR == v.findViewById(R.id.radioAdd))
                        return;

// Otherwise, uncheck the currently checked RadioButton, check the newly checked
// RadioButton, and store it for future reference.
                    checkR.setChecked(false);
//                    im.setVisibility(View.INVISIBLE);
//                    ((ImageView) rowView.findViewById(R.id.edit)).setVisibility(View.INVISIBLE);
//                    ((ImageView) v.findViewById(R.id.edit)).setVisibility(View.VISIBLE);
//                    ((ImageView) check.findViewById(R.id.edit)).setVisibility(View.VISIBLE);
                    ((RadioButton) v.findViewById(R.id.radioAdd)).setChecked(true);
                    View p=parent.getRootView();
//                    String cc=myList.get(position).getLine1().toString()+myList.get(position).getLine2().toString()+myList.get(position).getcity().toString()+myList.get(position).getstate().toString();
               //     Toast.makeText(context, cc, Toast.LENGTH_SHORT).show();
                    SharedPreferences cred = context.getSharedPreferences("cred", Context.MODE_PRIVATE);
                    SharedPreferences.Editor e1=cred.edit();
                    String cc="";
                    if(position!=0)
                    cc=myList.get(position).getLine1().toString()+myList.get(position).getLine2().toString()+myList.get(position).getcity().toString()+myList.get(position).getstate().toString();
                    else
                    cc=myList.get(position).getLine1().toString();
                    e1.putString("address",cc);
                    e1.commit();



                    checkR = (RadioButton) v.findViewById(R.id.radioAdd);
                    checkR = (RadioButton) v.findViewById(R.id.radioAdd);
                    im = ((ImageView) check.findViewById(R.id.edit));

                }
            }
        });

        View par=parent.getRootView();
        delete = (TextView) par.findViewById(R.id.delete);
//        String cc=((TextView) par.findViewById(R.id.addRess2)).getText().toString();
  //      Toast.makeText(context, cc, Toast.LENGTH_SHORT).show();
        rradd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences tt=context.getSharedPreferences("cred", Context.MODE_PRIVATE);
                int w=tt.getInt("add",0);
                if(w==0)
                {
                    Intent inSend=new Intent(context,SendOtpaddress.class);
                    ((Activity)context).finish();
                    context.startActivity(inSend);

                }
                else{

Deladd d=new Deladd();
                try{

                 editnow=myList.get(position).getAdd().toString();}
                catch (Exception e)
                {}

                getli=myList.get(position).getLine1().toString();
//                Toast.makeText(context, t, Toast.LENGTH_SHORT).show();




                    RelativeLayout r = (RelativeLayout) vi.findViewById(R.id.cardeditAdd1);
                    r.setVisibility(View.VISIBLE);
                    ListView l = (ListView) vi.findViewById(R.id.list1);
                    l.setVisibility(View.GONE);
                edt1=(EditText)vi.findViewById(R.id.edtAdd11);
                edt2=(EditText)vi.findViewById(R.id.edtAdd22);
                edt3=(EditText)vi.findViewById(R.id.edtAdd33);
                edt4=(EditText)vi.findViewById(R.id.edtAdd44);

                if(getli.contains("address"))
                {

                    delete.setVisibility(View.GONE);}
                else
                    delete.setVisibility(View.VISIBLE);
                    textAdd1.setVisibility(View.GONE);
                textAdd2.setVisibility(View.GONE);
                textAdd3.setVisibility(View.GONE);
//                    Toast.makeText(context, "True that yo" + position, Toast.LENGTH_SHORT).show();
//                    ((ImageView)rowView.findViewById(R.id.edit)).setVisibility(View.INVISIBLE);

                //edit.setVisibility(View.VISIBLE);
                 }}
        });
        if (position == myList.size() - 1) {
            ((ImageView) rowView.findViewById(R.id.edit)).setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.GONE);
            textView3.setVisibility(View.GONE);

            rd.setVisibility(View.INVISIBLE);
            imicon.setVisibility(View.VISIBLE);
        }
        if (position == 0) {
//            ((ImageView) rowView.findViewById(R.id.edit)).setVisibility(View.INVISIBLE);
//            textView2.setVisibility(View.GONE);
            textView2.setText("");
//            textView3.setText("");
            textView3.setVisibility(View.GONE);

//            rd.setVisibility(View.INVISIBLE);
//            imicon.setVisibility(View.VISIBLE);
        }

        // / ImageView imageView = (ImageView) rowView.findViewById(R.id.iconr.setV);
        View v = parent.getRootView();
        prefs = context.getSharedPreferences("Cred", Context.MODE_PRIVATE);
        String text1 = myList.get(position).getLine1().toString();
        textAdd1.setText(text1);
        try{
        String text2 = myList.get(position).getLine2().toString();
        String text3 = myList.get(position).getcity().toString();
        String text4 = myList.get(position).getstate().toString();

        textAdd2.setText(text2);
        textAdd3.setText(text3+""+text4);}
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
        vi = parent.getRootView();
//if(position==myList.size()-1){

        TextView save = (TextView) vi.findViewById(R.id.save);
        e1 = ((EditText) vi.findViewById(R.id.edtAdd11));
//        TextView delete = (TextView) vi.findViewById(R.id.delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new addAddress().execute("delete");
            }
        });
//        edt1.addTextChangedListener(new TextWatcher() {
//            @Overrideadd a
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                sedt1+=s;
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    add = new Deladd();
                    sedt1 = edt1.getText().toString();
                    sedt2 = edt2.getText().toString();
                    sedt3 = edt3.getText().toString();
                    sedt4 = edt4.getText().toString();
//                Toast.makeText(context, ((EditText) vi.findViewById(R.id.edtAdd1)).getText().toString(), Toast.LENGTH_SHORT).show();
                    add.setLine1(((EditText) vi.findViewById(R.id.edtAdd11)).getText().toString());
                    add.setLine2(sedt2);
                    add.setcity(sedt3);
                    add.setstate(sedt4);
                    new addAddress().execute();


                }
            });
        // change the icon for Windows and iPhone
//        String s = values[position];
//        if (s.startsWith("iPhone")) {
//            imageView.setImageResource(R.drawable.no);
//        } else {
//            imageView.setImageResource(R.drawable.ok);
//        }

        return rowView;
    }

    private class addAddress extends
            AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... data) {

             String del1="";
            try{
                     del1 = data[0];}
            catch (Exception e){del1="";}
            //   HashMap<String, String> details = data[0];
            JSONObject payload = new JSONObject();
            try {
                // userid=12&productid=23&action=add
                // TYPE: POST
                SharedPreferences red = context.getSharedPreferences("Referral", Context.MODE_PRIVATE);
//                String referralinst=red.getString("referrer","");
                SharedPreferences cred = context.getSharedPreferences("cred", Context.MODE_PRIVATE);
                String creduserid = cred.getString("phone_number", "");
                payload.put("userid", creduserid);
                JSONObject pay = new JSONObject();

                pay.put("line1", sedt1);
                pay.put("line2",  sedt2);
                pay.put("city", sedt3);
                pay.put("state",  sedt4);

                if(del1.equals("delete"))
                    pay.put("isActive",false);
                else
                pay.put("isActive",true);
                pay.put("pin", "");
                JSONObject pay2 = new JSONObject();
                if(editnow.trim().length()!=0)
                pay2.put(editnow, pay);
                else
                pay2.put("add"+String.valueOf(Check),pay);
                payload.put("deliveryAddress", pay2);

//                payload.put("code", crcode);
//                // payload.put("college", mCollege);
//                if(mRef.trim().length()>0)
//                    payload.put("refCode",mRef);
//                if((mRef.trim().length()==0)&&(referralinst.trim().length()!=0)) {
                //
                //
                //    Toast.makeText(Inviteform.this,referralinst,Toast.LENGTH_LONG).show();
                //    Toast.makeText(Inviteform.this,referralinst,Toast.LENGTH_LONG).show();
//                    payload.put("refCode", referralinst.trim());
//                }
//                payload.put("phone", mPhone);
//                payload.put("offlineForm",false);
                // payload.put("action", details.get("action"));

                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);
                //api/login/sendotp
                String url2 = context.getString(R.string.server) + "api/user/account/address";
                HttpPost httppost = new HttpPost(url2);
//                HttpDelete
                SharedPreferences toks = context.getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp = toks.getString("token_value", "");
                httppost.setHeader("x-access-token", tok_sp);
                httppost.setHeader("Content-Type", "application/json");
                StringEntity entity = new StringEntity(payload.toString());
                httppost.setEntity(entity);
                HttpResponse response = client.execute(httppost);
                HttpEntity ent = response.getEntity();
                String responseString = EntityUtils.toString(ent, "UTF-8");
                if (response.getStatusLine().getStatusCode() != 200) {

                    Log.e("MeshCommunication", "Server returned code "
                            + response.getStatusLine().getStatusCode());
                    return "fail";
                } else {
                    JSONObject resp = new JSONObject(responseString);

                    if (resp.getString("status").contains("error")) {
                        String truth = resp.getString("msg");
                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return resp.getString("msg");

                    } else {
//                        myListnew=new ArrayList<Deladd>();
                        for(int i=0;i<myList.size()-1;i++)
                        {
if((!del1.equals("delete"))&&!(getli.contains("address")))
{
    if(myList.get(i).getAdd().equals(editnow))
    {
        Deladd p=new Deladd();
        p.setLine1(sedt1);
        p.setLine2(sedt2);
        p.setcity(sedt3);
        p.setstate(sedt4);
        myListnew.add(p);
    }
    else
    { myListnew.add(myList.get(i));}
}
                            if (editnow.equals(myList.get(i).getAdd())&&(del1.equals("delete")))
                                continue;
                            else

                            { myListnew.add(myList.get(i));}
                        }
                        myList.clear();
                     //   JSONObject data1 = new JSONObject(resp.getString("data"));
                        if(getli.contains("address"))
                        {Check++;
                        Deladd p=new Deladd();
                            p.setLine1(sedt1);
                            p.setLine2(sedt2);
                            p.setcity(sedt3);
                            p.setstate(sedt4);
                        myListnew.add(p);}
                        myList=myListnew;
                        return "win";

                    }

                }

            } catch (Exception e) {
                Log.e("mesherror", e.getMessage());
                return "fail";

            }
        }

        protected void onPostExecute(String result) {
            if (result.equals("win")) {

//                context.list.setParams(mParam);
                Deladd c=new Deladd();

                c.setLine1("Add an address");
                myList.add(c);
                ListView l = (ListView) vi.findViewById(R.id.list1);
//                Toast.makeText(context, myList.size()+1+"yo", Toast.LENGTH_SHORT).show();
                LinearLayout.LayoutParams mParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                Adaptersimple adapter = new Adaptersimple(context, null, Check);

                adapter.notifyDataSetChanged();
                adapter=new Adaptersimple(context,myList,Check);
                l.setLayoutParams(mParam);
                l.setAdapter(adapter);
                adapter.notifyDataSetChanged();
//                ((BaseAdapter) myList.getAdapter()).notifyDataSetChanged();

//                textView.setText(prefs.getString(String.valueOf(position), ""));
//                Toast.makeText(context, myList.size()+"", Toast.LENGTH_SHORT).show();
                RelativeLayout r = (RelativeLayout) vi.findViewById(R.id.cardeditAdd1);
                r.setVisibility(View.GONE);

                l.setVisibility(View.VISIBLE);
                textAdd1.setVisibility(View.VISIBLE);
                textAdd2.setVisibility(View.VISIBLE);
                textAdd3.setVisibility(View.VISIBLE);
            }
        }
    }

}
