package com.gamelink.gamelinkapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.databinding.ActivityRegisterPostBinding
import com.gamelink.gamelinkapp.view.adapter.ItemAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog

class RegisterPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterPostBinding
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var recyclerView: RecyclerView
    private val list = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterPostBinding.inflate(layoutInflater)


        binding.buttonClose.setOnClickListener {
            finish()
        }

        for(i in 1..10) {
            list.add("item $i")
        }

        binding.textVisibilityToggle.setOnClickListener {
            showBottomSheet()
        }

        setContentView(binding.root)
    }

    private fun showBottomSheet () {
        val dialogView  = layoutInflater.inflate(R.layout.bottom_sheet, null)
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        dialog.setContentView(dialogView)
        recyclerView = dialogView.findViewById(R.id.recycler_view_item)
        itemAdapter = ItemAdapter(list)
        recyclerView.adapter = itemAdapter
        dialog.show()

    }

}