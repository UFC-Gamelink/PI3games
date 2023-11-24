package com.gamelink.gamelinkapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gamelink.gamelinkapp.databinding.RowCommunitiesListBinding
import com.gamelink.gamelinkapp.databinding.RowPostsListBinding
import com.gamelink.gamelinkapp.service.listener.CommunityListener
import com.gamelink.gamelinkapp.service.listener.PostListener
import com.gamelink.gamelinkapp.service.model.CommunityModel
import com.gamelink.gamelinkapp.service.model.PostModel
import com.gamelink.gamelinkapp.view.viewholder.CommunityViewHolder
import com.gamelink.gamelinkapp.view.viewholder.PostViewHolder

class CommunityAdapter : RecyclerView.Adapter<CommunityViewHolder>() {
    private var listCommunities: List<CommunityModel> = arrayListOf()
    private lateinit var listener: CommunityListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = RowCommunitiesListBinding.inflate(inflater, parent, false)

        return CommunityViewHolder(itemBinding, listener)
    }

    override fun getItemCount(): Int {
        return listCommunities.count()
    }

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        holder.bindData(listCommunities[position])
    }

    fun updateCommunities(list: List<CommunityModel>) {
        listCommunities = list
        notifyDataSetChanged()
    }

    fun attachListener(communityListener: CommunityListener) {
        listener = communityListener
    }
}