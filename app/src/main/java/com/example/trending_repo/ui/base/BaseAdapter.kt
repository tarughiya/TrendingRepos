package com.example.trending_repo.ui.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T : RecyclerView.ViewHolder?, D> : RecyclerView.Adapter<T>() {
    abstract fun setData(data: List<D>?)
}