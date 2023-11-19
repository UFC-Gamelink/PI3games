package com.gamelink.gamelinkapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gamelink.gamelinkapp.databinding.CommentsBottomSheetDialogFragmentBinding
import com.gamelink.gamelinkapp.view.adapter.CommentaryAdapter
import com.gamelink.gamelinkapp.viewmodel.CommentsBottomSheetDialogViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CommentsBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private lateinit var viewModel: CommentsBottomSheetDialogViewModel
    private var _binding: CommentsBottomSheetDialogFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var bundle: Bundle
    private val adapter = CommentaryAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CommentsBottomSheetDialogFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(CommentsBottomSheetDialogViewModel::class.java)

        binding.recyclerCommentaries.layoutManager = LinearLayoutManager(context)
        binding.recyclerCommentaries.adapter = adapter


        observe()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        if(arguments != null) {
            bundle = arguments as Bundle
        }

        val postId = bundle.getInt("post_id")

        viewModel.listByPost(postId)
    }

    private fun observe() {
        viewModel.comments.observe(viewLifecycleOwner) {
            adapter.updateCommentaries(it)
        }
    }


}