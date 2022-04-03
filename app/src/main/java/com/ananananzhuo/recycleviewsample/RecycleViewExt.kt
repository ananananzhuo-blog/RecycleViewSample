package com.ananananzhuo.recycleviewsample

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * author  :mayong
 * function:RecycleView的一些扩展
 * date    :2022/3/27
 **/


/**
 * 初始化，使用默认的方向
 */
fun RecyclerView.init(
    manager: RecyclerView.LayoutManager = LinearLayoutManager(
        context,
        LinearLayoutManager.VERTICAL,
        false
    )
) {
    layoutManager = manager
}