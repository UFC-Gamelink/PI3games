package com.gamelink.gamelinkapp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gamelink.gamelinkapp.databinding.RowCommunitiesListBinding
import com.gamelink.gamelinkapp.service.listener.CommunityListener
import com.gamelink.gamelinkapp.service.model.CommunityModel

class CommunityViewHolder(
    private val itemBinding: RowCommunitiesListBinding,
    val listener: CommunityListener
) :
    RecyclerView.ViewHolder(itemBinding.root) {

    fun bindData(community: CommunityModel) {
        itemBinding.textCommunityTitle.text = community.name

        if (community.bannerUrl != null) {
            Glide.with(itemView).load(community.bannerUrl).into(itemBinding.imageCommunityBanner)
        }

        itemBinding.root.setOnClickListener { listener.onCommunityClick(community.id) }
    }

}