package com.gamelink.gamelinkapp.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.gamelink.gamelinkapp.databinding.FragmentProfileBinding
import com.gamelink.gamelinkapp.utils.ImageUtils
import com.gamelink.gamelinkapp.view.adapter.ViewPagerAdapter
import com.gamelink.gamelinkapp.viewmodel.ProfileViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProfileViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        viewModel  = ViewModelProvider(this).get(ProfileViewModel::class.java)

        val tabLayout = binding.tabLayout;
        val viewPager = binding.viewPager

        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // TODO("Not yet implemented")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                //TODO("Not yet implemented")
            }

        })

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.getTabAt(position)?.select()
            }
        })

        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(context, RegisterPostActivity::class.java))
        }

        binding.imageLogout.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Fazer Logout")
                .setMessage("Deseja fazer logout?")
                .setPositiveButton("Sim") { _, _ ->
                    viewModel.logout()
                    startActivity(Intent(context, LoginActivity::class.java))
                    activity?.finish()
                }
                .setNeutralButton("Cancelar", null)
                .show()
        }

        observe()

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        viewModel.getProfile()
    }

    private fun observe() {
        viewModel.profile.observe(viewLifecycleOwner) {
            binding.textName.text = it.name
            binding.textUsername.text = it.username
            binding.textBioValue.text = it.bio
            binding.textCountPosts.text = it.numPosts.toString()

            val pathProfilePic = it.profilePicPath
            val bitmap = ImageUtils.getBitmap(pathProfilePic)
            binding.imageviewProfilePicture.setImageBitmap(bitmap)

            val pathBannerPic = it.bannerPicPath
            val bitmapBanner = ImageUtils.getBitmap(pathBannerPic)
            binding.imageviewBannerPicture.setImageBitmap(bitmapBanner)
        }
    }
}