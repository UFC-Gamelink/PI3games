package com.gamelink.gamelinkapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.gamelink.gamelinkapp.databinding.CommentsBottomSheetDialogFragmentBinding
import com.gamelink.gamelinkapp.viewmodel.CommentsBottomSheetDialogViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CommentsBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private lateinit var viewModel: CommentsBottomSheetDialogViewModel
    private var _binding: CommentsBottomSheetDialogFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CommentsBottomSheetDialogFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(CommentsBottomSheetDialogViewModel::class.java)

        viewModel.list()

        return binding.root
    }


}