package kr.ac.skuniv.practive;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

/**
 * 이 클래스는 ViewPager에서 사용될 Fragment들을 관리하고 표시하는 역할을 담당합니다.
 * 주로 메인 화면에서 여러 개의 Fragment를 슬라이드 형태로 표시하는 데 사용됩니다.
 *
 * MainPagerAdapter 클래스는 FragmentPagerAdapter를 상속받아서 구현됩니다. (Fragment의 개수가 적을 때 사용)
 *
 * 이 클래스는 getItem() 메서드를 오버라이드하여 각각의 페이지에 해당하는 Fragment를 반환하도록 구현합니다.
 * 또한, getCount() 메서드를 오버라이드하여 Fragment의 총 개수를 반환하도록 구현합니다.
 */

public class MainPagerAdapter extends FragmentStateAdapter {

    private ArrayList<Fragment> fragmentList;

    /**
     * MainPagerAdapter 클래스의 생성자는 FragmentManager와 List<Fragment>를 인자로 받습니다.
     * 생성자에서는 super()를 호출하여 부모 클래스인 FragmentPagerAdapter의 생성자를 실행하고, 전달받은 List<Fragment>를 멤버 변수로 설정합니다.
     */

    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList list) {
        super(fragmentActivity);
        this.fragmentList = list;

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}


