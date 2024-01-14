package com.cscmobi.habittrackingandroid.presentation

import com.cscmobi.habittrackingandroid.base.BaseBindingAdapter

interface OnItemClickPositionListener {
    fun onItemClick(position: Int)
}

interface ItemWithPostionListener<T> {
    fun onItemClicked(item: T, p: Int)

}


interface ItemBaseListener<T> :  BaseBindingAdapter.BaseBindingListener {
    fun onItemClicked(item: T)
}

interface ItemBasePosistionListener :  BaseBindingAdapter.BaseBindingListener {
    fun onItemClicked(p: Int)
}

interface ItemBaseWithPostitionListener<T>:  BaseBindingAdapter.BaseBindingListener {
    fun onItemClicked(item: T, p: Int)
}