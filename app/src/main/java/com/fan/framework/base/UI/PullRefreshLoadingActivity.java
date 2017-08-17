//package com.fan.framework.base.UI;
//
//import android.content.Context;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.widget.BaseAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.fan.framework.base.FFBaseAdapter;
//import com.fan.framework.base.UI.PullRefreshLoading.PullToRefreshBase;
//import com.fan.framework.base.UI.PullRefreshLoading.PullToRefreshListView;
//import com.fengnian.smallyellowo.foodie.R;
//import com.fengnian.smallyellowo.foodie.appbase.APP;
//import com.fengnian.smallyellowo.foodie.appbase.BaseFragmentActivity;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.jar.Attributes;
//
///**
// * Created by lanbiao on 16/9/8.
// */
//public class PullRefreshLoadingActivity extends BaseFragmentActivity{
//    boolean bDown = false;
//    private Integer index = 0;
//    private FFBaseAdapter mAdapter;
//    private ListView mListView;
//    private PullToRefreshListView mPullListView;
//
//    public static class DemoHolder{
//        TextView demo_item;
//    }
//
//    private class GetDataTask extends AsyncTask<Void,Void,String[]>{
//        @Override
//        protected String[] doInBackground(Void... params) {
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return new String[0];
//        }
//
//        @Override
//        protected void onPostExecute(String[] strings) {
//            if(bDown){
//                mPullListView.onPullDownRefreshComplete();
//            }
//            else {
//                boolean bHaveMoreData = false;
//                if(index <= 1){
//                    ArrayList<String> list = new ArrayList<String>();
//                    list.add(String.format("==more Data==%d",index));
//                    list.add(String.format("==more Data==%d",index));
//                    list.add(String.format("==more Data==%d",index));
//                    list.add(String.format("==more Data==%d",index));
//                    list.add(String.format("==more Data==%d",index));
//                    list.add(String.format("==more Data==%d",index));
//                    mAdapter.addData(list);
//                    bHaveMoreData = true;
//                }
//                else{
//                    bHaveMoreData = false;
//                }
//                mPullListView.onPullUpRefreshComplete();
//                mAdapter.notifyDataSetChanged();
//                mPullListView.setHasMoreData(bHaveMoreData);
//
//                index++;
//            }
//            super.onPostExecute(strings);
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(getmPullListView());
//
//        ArrayList<String> dataList = new ArrayList<String>();
//        dataList.addAll(Arrays.asList(mStrings));
//        mAdapter = new FFBaseAdapter<DemoHolder,String>(DemoHolder.class, R.layout.pull_refresh_demo_item,null) {
//
//            @Override
//            public void setViewData(Object viewHolder, int position, Object model) {
//                if(viewHolder instanceof DemoHolder){
//                    DemoHolder demoViewHolder = (DemoHolder)viewHolder;
//                    demoViewHolder.demo_item.setText((String)model);
//                }
//            }
//        };
//        mAdapter.addData(dataList);
//        mListView = mPullListView.getRefreshableView();
//        mListView.setAdapter(mAdapter);
//    }
//
//    public PullToRefreshListView getmPullListView() {
//        if(mPullListView == null){
//            mPullListView = new PullToRefreshListView(APP.app);
//            mPullListView.setPullRefreshEnabled(true);
//            mPullListView.setScrollLoadEnabled(true);
//
//            mPullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
//                @Override
//                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                    bDown = true;
//                    new GetDataTask().execute();
//                }
//
//                @Override
//                public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//                    bDown = false;
//                    new GetDataTask().execute();
//                }
//            });
//        }
//        return mPullListView;
//    }
//
//
//
//
//
//
//
//
//
//    public static final String[] mStrings = {
//            "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
//            "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale",
//            "Aisy Cendre", "Allgauer Emmentaler", "Alverca", "Ambert", "American Cheese",
//            "Ami du Chambertin", "Anejo Enchilado", "Anneau du Vic-Bilh", "Anthoriro", "Appenzell",
//            "Aragon", "Ardi Gasna", "Ardrahan", "Armenian String", "Aromes au Gene de Marc",
//            "Asadero", "Asiago", "Aubisque Pyrenees", "Autun", "Avaxtskyr", "Baby Swiss",
//            "Babybel", "Baguette Laonnaise", "Bakers", "Baladi", "Balaton", "Bandal", "Banon",
//            "Barry's Bay Cheddar", "Basing", "Basket Cheese", "Bath Cheese", "Bavarian Bergkase",
//            "Baylough", "Beaufort", "Beauvoorde", "Beenleigh Blue", "Beer Cheese", "Bel Paese",
//            "Bergader", "Bergere Bleue", "Berkswell", "Beyaz Peynir", "Bierkase", "Bishop Kennedy",
//            "Blarney", "Bleu d'Auvergne", "Bleu de Gex", "Bleu de Laqueuille",
//            "Bleu de Septmoncel", "Bleu Des Causses", "Blue", "Blue Castello", "Blue Rathgore",
//            "Blue Vein (Australian)", "Blue Vein Cheeses", "Bocconcini", "Bocconcini (Australian)",
//            "Boeren Leidenkaas", "Bonchester", "Bosworth", "Bougon", "Boule Du Roves",
//            "Boulette d'Avesnes", "Boursault", "Boursin", "Bouyssou", "Bra", "Braudostur",
//            "Breakfast Cheese", "Brebis du Lavort", "Brebis du Lochois", "Brebis du Puyfaucon",
//            "Bresse Bleu", "Brick", "Brie", "Brie de Meaux", "Brie de Melun", "Brillat-Savarin",
//            "Brin", "Brin d' Amour", "Brin d'Amour", "Brinza (Burduf Brinza)",
//            "Briquette de Brebis", "Briquette du Forez", "Broccio", "Broccio Demi-Affine",
//            "Brousse du Rove", "Bruder Basil", "Brusselae Kaas (Fromage de Bruxelles)", "Bryndza",
//            "Buchette d'Anjou", "Buffalo", "Burgos", "Butte", "Butterkase", "Button (Innes)",
//            "Buxton Blue", "Cabecou", "Caboc", "Cabrales", "Cachaille", "Caciocavallo", "Caciotta",
//            "Caerphilly", "Cairnsmore", "Calenzana", "Cambazola", "Camembert de Normandie",
//            "Canadian Cheddar", "Canestrato", "Cantal", "Caprice des Dieux", "Capricorn Goat",
//            "Capriole Banon", "Carre de l'Est"
//    };
//
//}
