package jeremy.com.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import jeremy.com.R;
import jeremy.com.activity.SelectFromAmapActivity;

/**
 * Created by Xin on 2017/3/15 0015.
 */

public class SelectLocationFragment extends Fragment {

    private TextView tv_select_location;
    private Button bn_select_from_map;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selecte_location, null);
        tv_select_location = (TextView) view.findViewById(R.id.tv_select_location);
        bn_select_from_map = (Button) view.findViewById(R.id.bn_select_from_map);
        bn_select_from_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectFromAmapActivity.class);
                startActivityForResult(intent,1);
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String address = data.getStringExtra("address");
        tv_select_location.setText(address);
    }
}
