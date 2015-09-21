package cl.pt1.dondecomprarlo;

import cl.pt1.dondecomprarlo.InformacionProductoFragment;
import cl.pt1.dondecomprarlo.TiendasProductoFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Games fragment activity
			return new InformacionProductoFragment();
		case 1:
			// Movies fragment activity
			return new TiendasProductoFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 2;
	}

}
