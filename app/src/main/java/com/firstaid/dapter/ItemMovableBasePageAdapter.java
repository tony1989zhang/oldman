package com.firstaid.dapter;

import com.firstaid.view.recyclerMovable.ItemTouchHelperAdapter;

import java.util.Collections;

/**
 * RecyclerView 分页的BaseAdapter
 * */
public abstract class ItemMovableBasePageAdapter<TYPE> extends BasePageAdapter implements ItemTouchHelperAdapter{


	@Override
	public void onItemDismiss(int position) {
		mItems.remove(position);
		notifyItemRemoved(position);
	}

	@Override
	public boolean onItemMove(int fromPosition, int toPosition) {
		Collections.swap(mItems, fromPosition, toPosition);
		notifyItemMoved(fromPosition, toPosition);
		return true;
	}
}
