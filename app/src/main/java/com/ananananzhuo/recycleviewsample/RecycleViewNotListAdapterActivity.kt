package com.ananananzhuo.recycleviewsample

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ananananzhuo.mvvm.utils.logEE
import com.ananananzhuo.recycleviewsample.databinding.ActivityRecycleViewListAdapterBinding
import com.ananananzhuo.recycleviewsample.databinding.ActivityRecycleViewNotListAdapterBinding
import com.ananananzhuo.recycleviewsample.databinding.ItemTextBinding

class RecycleViewNotListAdapterActivity : AppCompatActivity() {
    var datas = mutableListOf(
        Name("李白"),
        Name("李白1"),
        Name("李白2"),
        Name("李白3"),
        Name("李白4"),
        Name("李白5"),
        Name("李白6"),
    )
    val binding: ActivityRecycleViewNotListAdapterBinding by lazy {
        ActivityRecycleViewNotListAdapterBinding.inflate(layoutInflater)
    }
    var adapter:CustomNoListAdapter ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.recycle.layoutManager = LinearLayoutManager(this)
        binding.recycle.adapter = CustomNoListAdapter(datas).apply {
            adapter = this
        }
        binding.btnShuffle.setOnClickListener {
            datas.shuffle()
            adapter?.notifyDataSetChanged()
        }
    }
    class CustomNoListAdapter(val datas: MutableList<Name>) :
        RecyclerView.Adapter<CustomListHolder>() {
        val random = (100..200)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomListHolder {
            val binding =
                ItemTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CustomListHolder(binding)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: CustomListHolder, position: Int) {
            holder.binding.root.setBackgroundColor(Color.rgb(random.random(),random.random(),random.random()))
            logEE("执行onBindViewHolder:$position ${datas[position].name}")
            holder.binding.tv.text = "$position  ${datas[position].name}"
        }

        override fun getItemCount(): Int = datas.size
    }
}


