package com.cscmobi.habittrackingandroid.presentation

import com.cscmobi.habittrackingandroid.base.BaseBindingAdapter

interface OnItemClickPositionListener {
    fun onItemClick(position: Int)
}

interface ItemListener<T> :  BaseBindingAdapter.BaseBindingListener {
    fun onItemClicked(item: T)
}

interface ItemPosistionListener :  BaseBindingAdapter.BaseBindingListener {
    fun onItemClicked(p: Int)
}

interface ItemWithPostitionListenr<T>:  BaseBindingAdapter.BaseBindingListener {
    fun onItemClicked(item: T, p: Int)
}