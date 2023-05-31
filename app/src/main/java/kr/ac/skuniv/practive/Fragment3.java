package kr.ac.skuniv.practive;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 오후 시간대의 정보를 제공해주는 fragment
 */

public class Fragment3 extends Fragment {

    public static Fragment3 newInstance(int number){
        Fragment3 fragment3 = new Fragment3();
        Bundle bundle = new Bundle();
        bundle.putInt("number", number);
        fragment3.setArguments(bundle);
        return fragment3;
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
        return LayoutInflater.from(inflater.getContext()).inflate(R.layout.fragment3, container, false);
    }
}
