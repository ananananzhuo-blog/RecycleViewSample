package com.ananananzhuo.recycleviewsample

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * author  :mayong
 * function:
 * date    :2022/3/27
 **/
open class BaseViewHolder<B:ViewBinding>(binding:B):RecyclerView.ViewHolder(binding.root) {
}