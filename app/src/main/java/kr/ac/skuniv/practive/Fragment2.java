package kr.ac.skuniv.practive;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 아침시간의 정보를 제공해주는 fragment
 */

public class Fragment2 extends Fragment {
    public static Fragment2 newInstance(int number){
        Fragment2 fragment2 = new Fragment2();
        Bundle bundle = new Bundle();
        bundle.putInt("number", number);
        fragment2.setArguments(bundle);
        return fragment2;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            int num = getArguments().getInt("number");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(inflater.getContext()).inflate(R.layout.fragment2, container, false);
    }
}
