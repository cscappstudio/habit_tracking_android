package com.cscmobi.habittrackingandroid.presentation

import com.cscmobi.habittrackingandroid.base.BaseBindingAdapter

interface OnItemClickPositionListener {
    fun onItemClick(position: Int)
}

interface ItemWithPostionListener<T> {
    fun onItemClicked(item: T, p: Int)

}

interface EditItemTask<T> {
    fun skip(item: T, p: Int)
    fun edit(item: T, p: Int)
    fun delete(item: T, p: Int)
}

interface ItemTaskWithEdit<T>: ItemWithPostionListener<T>, EditItemTask<T>




interface ItemBaseListener<T> :  BaseBindingAdapter.BaseBindingListener {
    fun onItemClicked(item: T)
}

interface ItemBasePosistionListener :  BaseBindingAdapter.BaseBindingListener {
    fun onItemClicked(p: Int)
}

interface ItemBaseWithPostitionListener<T>:  BaseBindingAdapter.BaseBindingListener {
    fun onItemClicked(item: T, p: Int)
}