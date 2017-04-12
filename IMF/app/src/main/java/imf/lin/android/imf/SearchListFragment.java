package imf.lin.android.imf;

/**
 * Created by stmac0001 on 2017/04/05.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SearchListFragment extends Fragment{

    static public SearchListFragment getInstance(String cond) {
        SearchListFragment f = new SearchListFragment();
        Bundle args = new Bundle();
        args.putString("cond", cond);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recomend_search, container, false);
    }

    @Override
    public  void onViewCreated(View view, Bundle savedInstanceState){
        //Crowring Internet
        String cond = getArguments().getString("cond");

    }
}
