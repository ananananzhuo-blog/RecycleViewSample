package com.ananananzhuo.recycleviewsample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.ananananzhuo.mvvm.activity.CustomAdapterActivity
import com.ananananzhuo.mvvm.bean.bean.ItemData
import com.ananananzhuo.mvvm.callback.CallData
import com.ananananzhuo.recycleviewsample.databinding.ActivityMainBinding

class MainActivity : CustomAdapterActivity() {
    override fun getAdapterDatas(): MutableList<ItemData> = mutableListOf(
        ItemData(title = "使用ListAdapter实现乱序动画重排",activity = RecycleViewListAdapterActivity::class.java){
            startActivity(Intent(this,it.itemData.activity))
        },
        ItemData(title = "使用原生Adapter实现乱序重排",activity = RecycleViewNotListAdapterActivity::class.java){
            startActivity(Intent(this,it.itemData.activity))
        },
        ItemData(title = "使用原生Adapter实现乱序重排",activity = RecycleDifferUtilsUesActivity::class.java){
            startActivity(Intent(this,it.itemData.activity))
        }
    )


    override fun showFirstItem(): Boolean = false
}
