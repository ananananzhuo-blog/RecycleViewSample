package com.ananananzhuo.recycleviewsample

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ananananzhuo.mvvm.activity.ViewBindingActivity
import com.ananananzhuo.mvvm.utils.logEE
import com.ananananzhuo.recycleviewsample.databinding.ActivityRecycleDemoBinding
import com.ananananzhuo.recycleviewsample.databinding.ItemTextBinding
import com.ananananzhuo.recycleviewsample.utils.randomColor

/**
 * 功能 这个类里面内容暂时不太靠谱
 * Created by mayong on 2022/3/27.
 */
class RecycleDifferUtilsUesActivity : ViewBindingActivity<ActivityRecycleDemoBinding>() {
    var datas = mutableListOf(
        Name("李白"),
        Name("李白1"),
        Name("李白2"),
        Name("李白3"),
        Name("李白4"),
        Name("李白5"),
        Name("李白6"),
    )
    var adapter: TAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.recycle.init()
        mBinding.recycle.adapter = TAdapter(datas).apply {
            adapter = this
        }
        mBinding.btnShuffle.setOnClickListener {
            adapter?.let {
                it.submit { old,new->
                    new.shuffle()
                    val diffResult = DiffUtil.calculateDiff(DifferCallback(old,new))
                    diffResult.dispatchUpdatesTo(it)
                }
//                it.submit { old, new ->
//                    new.set(4, Name("李白的二大爷"))
//                    val diffResult = DiffUtil.calculateDiff(DifferCallback(old, new))
//                    diffResult.dispatchUpdatesTo(it)
//                }
            }
        }
        mBinding.btnChangeData.setOnClickListener {
            adapter?.let {
                it.submit { old, new ->
                    new[4] = Name("李白的二大爷")
                    new.add(Name("安安安安卓"))
                    val diffResult = DiffUtil.calculateDiff(DifferCallback(old, new))
                    diffResult.dispatchUpdatesTo(it)
                }
            }
        }
    }

    override fun initBinding(): ActivityRecycleDemoBinding =
        ActivityRecycleDemoBinding.inflate(layoutInflater)

    /**
     * @param callback 回调数据中第一个参数是老数据，第二个是新数据
     */
    class TAdapter(var adapterDatas: MutableList<Name>) :
        RecyclerView.Adapter<RecycleDemoHolder>() {
        fun submit(callback: (MutableList<Name>, MutableList<Name>) -> Unit) {
            val newData = adapterDatas.toMutableList()
            callback(adapterDatas, newData)
            adapterDatas = newData//这里一定要更改adapter中的数据列表，否则内容是不会变的
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleDemoHolder {
            return RecycleDemoHolder(
                ItemTextBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: RecycleDemoHolder, position: Int) {
            holder.bind(position, adapterDatas[position])

        }

        override fun getItemCount(): Int {
            return adapterDatas.size
        }

    }
}

class DifferCallback(val oldDatas: List<Name>, val newDatas: List<Name>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldDatas.size

    override fun getNewListSize(): Int = newDatas.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldDatas[oldItemPosition].name == newDatas[newItemPosition].name)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areItemsTheSame(oldItemPosition, newItemPosition)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }

}


class RecycleDemoHolder(val binding: ItemTextBinding) :
    BaseViewHolder<ItemTextBinding>(binding) {

    fun bind(position: Int, data: Name) {
        binding.tv.text = "item索引:$position -- item内容:${data.name}"
        binding.root.setBackgroundColor(Color.rgb(randomColor(), randomColor(), randomColor()))
    }
}