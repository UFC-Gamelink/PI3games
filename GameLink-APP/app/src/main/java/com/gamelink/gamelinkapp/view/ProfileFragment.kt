package com.gamelink.gamelinkapp.view

import android.app.AlertDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.databinding.FragmentProfileBinding
import com.gamelink.gamelinkapp.service.constants.GameLinkConstants
import com.gamelink.gamelinkapp.view.adapter.ViewPagerAdapter
import com.gamelink.gamelinkapp.viewmodel.ProfileViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProfileViewModel
    private val bundle = Bundle()
    private var optionsActive = false



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

        binding.fabSelectPostType.setOnClickListener {
            handleSelectTypePostButton()
        }

        binding.fabNewPost.setOnClickListener {
            startActivity(Intent(context, RegisterPostActivity::class.java))
            handleSelectTypePostButton()
        }

        binding.fabNewEvent.setOnClickListener {
            startActivity(Intent(context, MapsActivity::class.java))
            handleSelectTypePostButton()
        }


        binding.imageProfileOptions.setOnClickListener {
            showPopupMenu(it)
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
            binding.textCountPosts.text = it.qntPosts.toString()
            binding.textCountFollowers.text = it.qntFollowers.toString()
            binding.textCountFollowing.text = it.qntFollowing.toString()



            if ( it.showBirthday ) {
                binding.textAgeTotal.visibility = View.VISIBLE
                binding.textAge.visibility = View.VISIBLE
                binding.textAgeTotal.text = it.birthday
                binding.textAgeTotal.text = getAge()
            } else {
                binding.textAgeTotal.visibility = View.INVISIBLE
                binding.textAge.visibility = View.INVISIBLE
            }



            Glide.with(this).load(it.icon.url).into(binding.imageviewProfilePicture)
            Glide.with(this).load(it.banner.url).into(binding.imageviewBannerPicture)


            bundle.putString(GameLinkConstants.SHARED.USER_ID, it.id)
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(context, view).apply {
            menuInflater.inflate(R.menu.options_profile_menu, this.menu)
        }

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_update_profile -> {
                    val intent = Intent(context, ProfileFormActivity::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                    true
                }
                R.id.item_logout -> {
                    showAlertDialog()
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

    private fun handleSelectTypePostButton() {
        optionsActive = if(!optionsActive) {
            binding.fabNewPost.show()
            binding.fabNewEvent.show()
            binding.fabSelectPostType.setImageResource(R.drawable.ic_close)
            true
        } else {
            binding.fabNewPost.hide()
            binding.fabNewEvent.hide()
            binding.fabSelectPostType.setImageResource(R.drawable.ic_add)
            false
        }
    }

    private fun showAlertDialog() {
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

    private fun getAge() : String {
        var yearClient: Int = binding.textAgeTotal.text.toString().substring(0,4).toInt()
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val idade  =  currentYear - yearClient
        return idade.toString()
    }
}