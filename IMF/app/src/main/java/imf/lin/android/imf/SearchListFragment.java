package imf.lin.android.imf;

/**
 * Created by stmac0001 on 2017/04/05.
 */

import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

import static com.google.android.gms.location.places.AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT;

public class SearchListFragment extends Fragment implements OnConnectionFailedListener,ConnectionCallbacks {
    private RecyclerView recyclerView;
    private RecommendListRecyclerAdapter adapter;
    private GoogleApiClient mGoogleApiClient;
    private final int REQUEST_PERMISSION = 1000;
    ArrayList<CharSequence> resultsArray = new ArrayList<>();
    View mFlagmentView;

    private View _view;

    ScheduleListActivity _scheduleListActivity;

    static public SearchListFragment getInstance(String cond) {
        SearchListFragment f = new SearchListFragment();
        Bundle args = new Bundle();
        args.putString("cond", cond);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .enableAutoManage((FragmentActivity) getActivity(), this)
                .build();
        mGoogleApiClient.connect();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFlagmentView = inflater.inflate(R.layout.fragment_recomend_search, container, false);

        return mFlagmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //Crowring Internet
        String cond = getArguments().getString("cond");
    }

    //接続成功
    @Override
    public  void onConnected (Bundle connectionHint){
        checkPermission();
    }

    //接続中断
    @Override
    public void onConnectionSuspended (int cause){

    }
    //接続失敗
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // An unresolvable error has occurred and a connection to Google APIs 
        // could not be established. Display an error message, or handle 
        // the failure silently  
        // ... 
    }
    // 位置情報許可の確認
    public void checkPermission() {
        // 既に許可している
        if(Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                execStart();
            }
            // 拒否していた場合
            else {
                requestLocationPermission();
            }
        }
    }

    // 許可を求める
    private void requestLocationPermission() {
        if(Build.VERSION.SDK_INT >= 23) {
            if (shouldShowRequestPermissionRationale(
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION);

            } else {
                Toast toast = Toast.makeText(getActivity(), "許可されないとアプリが実行できません", Toast.LENGTH_SHORT);
                toast.show();
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,}, REQUEST_PERMISSION);
            }
        }
    }

    // 結果の受け取り
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            // 使用が許可された
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                execStart();
                return;

            } else {
                // それでも拒否された時の対応
                Toast toast = Toast.makeText(getActivity(), "これ以上なにもできません", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    // メイン処理
    public void execStart() {

            if (Build.VERSION.SDK_INT >= 23) {
                if (getActivity().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || getActivity().checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //API 実行できるかテスト　Start
                    double lat = 35.170;
                    double lng = 137.00;

                    LatLngBounds bounds = new LatLngBounds.Builder().include(new LatLng(lat, lng)).build();
                    PendingResult<AutocompletePredictionBuffer> result = Places.GeoDataApi.getAutocompletePredictions(
                            mGoogleApiClient,
                            "コンビニ",
                            bounds,
                            new AutocompleteFilter.Builder().setTypeFilter(TYPE_FILTER_ESTABLISHMENT).build());

                        result.setResultCallback(new ResultCallback<AutocompletePredictionBuffer>() {
                        @Override
                        public void onResult(@NonNull AutocompletePredictionBuffer autocompletePredictions) {
                            Log.d("MyApp ACcount", Integer.toString(autocompletePredictions.getCount()) );
                            for (int i = 0; i < autocompletePredictions.getCount(); i++) {
                                AutocompletePrediction predict = autocompletePredictions.get(i);
                                String placeid = predict.getPlaceId();
                                Log.d("MyApp placeId", placeid);
                                CharSequence fulltxt = predict.getFullText(new CharacterStyle() {
                                    @Override
                                    public void updateDrawState(TextPaint textPaint) {

                                    }
                                });
                                Log.d("MyApp fulltxt", fulltxt.toString());
                                SearchListFragment.this.resultsArray.add(fulltxt);
                                Log.d("MyApp size", Integer.toString(SearchListFragment.this.resultsArray.size()));
                            }
                            autocompletePredictions.release();
                            //リサイクラービューの設定
                            setRecyclerView();
                        }
                    });
                    //API 実行できるかテスト　End
                }
            } else {
                //SDKのバージョンは２３以上なので何もしない。
            }
    }

    private void setRecyclerView(){
        recyclerView = (RecyclerView)mFlagmentView.findViewById(R.id.imf_recommend_recycler_view);
        recyclerView.setHasFixedSize(true);

        //setAdapter
        adapter = new RecommendListRecyclerAdapter(resultsArray);
        recyclerView.setAdapter(adapter);

        //implement ItemTouchHelper to swipe and Drug&Drop
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // ドラッグアンドドロップ時
                adapter.move(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction == ItemTouchHelper.RIGHT){
                    // アイテム右スワイプ時
                    adapter.removeAtPosition(viewHolder.getAdapterPosition());
                }
                if(direction == ItemTouchHelper.LEFT){
                    //************************************************************************************
                    if (mListener != null) {
                        mListener.onClickButton("result");
                    }
                    //************************************************************************************
                }
            }
        }).attachToRecyclerView(recyclerView);

    }

    //************************************************************************************
    public interface MyListener {
        public void onClickButton(String text);
    }

    // 変数を用意する
    private MyListener mListener;

    // FragmentがActivityに追加されたら呼ばれるメソッド
    @Override
    public void onAttach(Context context) {
        // APILevel23からは引数がActivity->Contextになっているので注意する
        super.onAttach(context);
        // contextクラスがMyListenerを実装しているかをチェックする
        if (context instanceof MyListener) {
            // リスナーをここでセットするようにします
            mListener = (MyListener) context;
        }
    }

    // FragmentがActivityから離れたら呼ばれるメソッド
    @Override
    public void onDetach() {
        super.onDetach();
        // 画面からFragmentが離れたあとに処理が呼ばれることを避けるためにNullで初期化しておく
        mListener = null;
    }
    //************************************************************************************
}
