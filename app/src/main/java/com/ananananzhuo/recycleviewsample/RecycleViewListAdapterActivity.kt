package com.ananananzhuo.recycleviewsample

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ananananzhuo.mvvm.utils.logEE
import com.ananananzhuo.mvvm.view.recycle.CustomHolder
import com.ananananzhuo.recycleviewsample.databinding.ActivityRecycleDemoBinding
import com.ananananzhuo.recycleviewsample.databinding.ActivityRecycleViewListAdapterBinding
import com.ananananzhuo.recycleviewsample.databinding.ItemTextBinding
import com.ananananzhuo.recycleviewsample.utils.randomColor

/**
 * 功能：使用ListAdapter实现RecycleView中打乱顺序后更新动画
 * Created by mayong on 2022/4/3.
 */
class RecycleViewListAdapterActivity : AppCompatActivity() {
    var datas = mutableListOf(
        Name("李白"),
        Name("李白1"),
        Name("李白2"),
        Name("李白3"),
        Name("李白4"),
        Name("李白5"),
        Name("李白6"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRecycleViewListAdapterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = CustomListAdapter()
        binding.btnShuffle.setOnClickListener {
            val newData = datas.toMutableList().apply {
                shuffle()
            }
            datas = newData
            adapter.submitList(newData)
        }
        binding.recycle.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
        }
        adapter.submitList(datas)
    }

    private class CustomListAdapter() : ListAdapter<Name, CustomListHolder>(CustomDiffCallback()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomListHolder {
            val binding =
                ItemTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CustomListHolder(binding)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: CustomListHolder, position: Int) {
            logEE("执行onBindViewHolder:$position ${getItem(position).name}")
            holder.binding.tv.text = "$position  ${getItem(position).name}"
            holder.binding.root.setBackgroundColor(Color.rgb(randomColor(), randomColor(), randomColor()))
        }

    }
}
class CustomListHolder(val binding: ItemTextBinding) : RecyclerView.ViewHolder(binding.root) {

}
data class Name(val name: String)
class CustomDiffCallback : DiffUtil.ItemCallback<Name>() {

    override fun areItemsTheSame(oldItem: Name, newItem: Name): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Name, newItem: Name): Boolean {
        return oldItem == newItem
    }
}