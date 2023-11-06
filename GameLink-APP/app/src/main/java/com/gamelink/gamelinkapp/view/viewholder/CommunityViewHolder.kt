package com.gamelink.gamelinkapp.view.viewholder

import android.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gamelink.gamelinkapp.databinding.RowCommunitiesListBinding
import com.gamelink.gamelinkapp.databinding.RowPostsListBinding
import com.gamelink.gamelinkapp.service.listener.PostListener
import com.gamelink.gamelinkapp.service.model.CommunityModel
import com.gamelink.gamelinkapp.service.model.PostModel
import com.gamelink.gamelinkapp.utils.ImageUtils

class CommunityViewHolder(private val itemBinding: RowCommunitiesListBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    fun bindData(community: CommunityModel) {
        itemBinding.textCommunityTitle.text = community.name

        if(community.bannerUrl != null) {
            Glide.with(itemView).load(community.bannerUrl).into(itemBinding.imageCommunityBanner)
        }
//        itemBinding.textPost.text = post.post
//
//        if(post.postImagePath != null) {
//            Glide.with(itemBinding.root.context).load(post.postImagePath).into(itemBinding.imagePost)
//        }
    }

}