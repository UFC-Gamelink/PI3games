package com.gamelink.gamelinkapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gamelink.gamelinkapp.databinding.RowCommentaryListBinding
import com.gamelink.gamelinkapp.service.model.CommentaryModel
import com.gamelink.gamelinkapp.service.model.CommunityModel
import com.gamelink.gamelinkapp.view.viewholder.CommentaryViewHolder

class CommentaryAdapter: RecyclerView.Adapter<CommentaryViewHolder>() {
    private var listCommentaries: List<CommentaryModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentaryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = RowCommentaryListBinding.inflate(inflater, parent, false)

        return CommentaryViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return listCommentaries.count()
    }

    override fun onBindViewHolder(holder: CommentaryViewHolder, position: Int) {
        holder.bindData(listCommentaries[position])
    }

    fun updateCommentaries(list: List<CommentaryModel>) {
        listCommentaries = list
        notifyDataSetChanged()
    }
}