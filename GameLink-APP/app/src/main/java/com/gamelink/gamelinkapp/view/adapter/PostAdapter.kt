package com.gamelink.gamelinkapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gamelink.gamelinkapp.databinding.RowPostsListBinding
import com.gamelink.gamelinkapp.service.listener.PostListener
import com.gamelink.gamelinkapp.service.model.PostModel
import com.gamelink.gamelinkapp.view.viewholder.PostViewHolder

class PostAdapter : RecyclerView.Adapter<PostViewHolder>() {
    private var listPosts: List<PostModel> = arrayListOf()
    private lateinit var listener: PostListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = RowPostsListBinding.inflate(inflater, parent, false)

        return PostViewHolder(itemBinding, listener, inflater)
    }

    override fun getItemCount(): Int {
        return listPosts.count()
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bindData(listPosts[position])
    }

    fun updatePosts(list: List<PostModel>) {
        listPosts = list
        notifyDataSetChanged()
    }

    fun attachListener(postListener: PostListener) {
        listener = postListener
    }
}