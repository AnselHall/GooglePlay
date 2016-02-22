package com.exe.googleplay.ui.fragment;

import android.support.v4.app.Fragment;

import java.util.HashMap;

/**
 * Fragment创建工厂
 */
public class FragmentFactory {
    private static HashMap<Integer, Fragment> fragmentCache = new HashMap<Integer, Fragment>();
//    private static SparseArray<Fragment> sparseArray = new SparseArray<>();
    public static Fragment createFragment(int position) {
//        Fragment fragment = sparseArray.get(position);
        Fragment fragment = fragmentCache.get(position);
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new HomeFragment();
                    break;
                case 1:
                    fragment = new AppFragment();
                    break;
                case 2:
                    fragment = new CategoryFragment();
                    break;
                case 3:
                    fragment = new GameFragment();
                    break;
                case 4:
                    fragment = new HotFragment();
                    break;
                case 5:
                    fragment = new RecommendFragment();
                    break;
                case 6:
                    fragment = new SubjectFragment();
                    break;
            }
//            sparseArray.put(position, fragment);
            fragmentCache.put(position, fragment);
        }
        return fragment;
    }
}
