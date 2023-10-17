package com.gamelink.gamelinkapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gamelink.gamelinkapp.databinding.RowPostsListBinding
import com.gamelink.gamelinkapp.service.model.PostModel
import com.gamelink.gamelinkapp.view.viewholder.PostViewHolder

class PostAdapter : RecyclerView.Adapter<PostViewHolder>() {
    private var listPosts: List<PostModel> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = RowPostsListBinding.inflate(inflater, parent, false)
        return PostViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return listPosts.count()
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bindData(listPosts[position])
    }

    fun updateTasks(list: List<PostModel>) {
        listPosts = list
        notifyDataSetChanged()
    }
}