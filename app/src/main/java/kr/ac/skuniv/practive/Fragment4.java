package kr.ac.skuniv.practive;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 저녁 시간대의 정보를 제공해주는 fragment
 */

public class Fragment4 extends Fragment {

    public static Fragment4 newInstance(int number){
        Fragment4 fragment4 = new Fragment4();
        Bundle bundle = new Bundle();
        bundle.putInt("number", number);
        fragment4.setArguments(bundle);
        return fragment4;
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
        return LayoutInflater.from(inflater.getContext()).inflate(R.layout.fragment4, container, false);
    }
}
