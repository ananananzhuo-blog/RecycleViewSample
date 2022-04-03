> 关注公众号 "安安安安卓" 免费学知识


> 近期需要对公司的祖传项目进行一些重构，在重构过程中涉及到多个列表的重构。老的列表中更新数据一直使用的`notifyDataSetChanged`进行数据的更新。从我入职时期这便是我的一大心病，因此这次一并解决

## 问题描述

关于`notifyDataSetChanged`的弊端，无需赘言，他会对 Recycleview 的整个列表进行刷新。因此在大数据量场景下（目前此种更新数据方法也不早再是官方推荐的了），这可能是很大的性能障碍。

解决方案：

1. 使用 ListAdapter 进行局部刷新数据
2. 使用 DiffUtil.Callback 进行局部刷新数据
3. 使用`notifyItemChanged(int position)`、`notifyItemRangeChanged(int positionStart, int itemCount)`、`notifyItemInserted(int position)`、`notifyItemMoved(int fromPosition, int toPosition)`几个 api 进行更新

`方法3`没什么好说的，本文主要讲方法 1、2
`方法1`的实现是基于`方法2`进行实现的。

使用这两种方法的
优点如下：

1. 可以实现数据的局部刷新；例如我们添加、更改、移除数据的时候都可能涉及到 item 布局位置和内容的动态改变，使用新方案可以让这个更新过程以动画的方式进行
2. 可以提高性能；原因是只有被我们更改的数据 item 的 onBindViewHolder 才会被回调

缺点如下：

1. 操作数据后 item 回调`onBindViewHolder`中的`position`是不可靠的，建议禁止使用`position`获取数据进行操作
2. 没有`notifyDataSetChanged`这种甩手的使用方法方便（哈哈，完全可以被优点覆盖）

## 对 notifyDataSetChanged 的鞭尸

`notifyDataSetChanged`的使用很简单，本例我们放一张 gif，展示一个列表，然后将列表进行乱序然后`notifyDataSetChanged`进行刷新；

![原生刷新](https://files.mdnice.com/user/15648/bbf4b93b-99bb-4132-8000-4d3fa4578ce2.gif)

我们可以发现，刷新过程中并没有动画的实现，因为我们对没个 item 设置了随机颜色，所以每次刷新界面闪动很大

## ListAdapter 方式

### 实现代码

[RecycleViewListAdapterActivity](https://github.com/ananananzhuo-blog/RecycleViewSample/blob/main/app/src/main/java/com/ananananzhuo/recycleviewsample/RecycleViewListAdapterActivity.kt)

_关键代码_

```kt
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
```

### 效果

使用 ListAdapter 刷新更加流畅，并且有动画
![ListAdapter实现数据刷新](https://files.mdnice.com/user/15648/dae1e08f-b0d2-43e3-91fd-6a6b2a6b5204.gif)

## DiffUtil.Callback 方式

### 实现代码

[RecycleDifferUtilsUesActivity](https://github.com/ananananzhuo-blog/RecycleViewSample/blob/main/app/src/main/java/com/ananananzhuo/recycleviewsample/RecycleDifferUtilsUesActivity.kt)
_关键代码_
创建 DifferCallback 方法

```kt
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
```

调用刷新数据的代码

```kt
 val diffResult = DiffUtil.calculateDiff(DifferCallback(old, new))
                    diffResult.dispatchUpdatesTo(it)
```

### 效果

使用 ListAdapter 刷新更加流畅，并且有动画
![DiffUtils.Callback实现数据刷新](https://files.mdnice.com/user/15648/3fd7af24-affc-4714-8876-07c73f130a3c.gif)
