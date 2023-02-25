package com.example.trootechpractical.presentation.home.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.trootechpractical.R
import com.example.trootechpractical.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setUserData(galleryViewModel)
        return root
    }

    private fun setUserData(galleryViewModel: ProfileViewModel) {
        galleryViewModel.loginUser.observe(viewLifecycleOwner) {
            binding.edtEmail.setText(it.email ?: "")
            binding.edtName.setText(it.name ?: "")
            Glide.with(requireActivity())
                .load(it.profileImage)
                .error(R.drawable.ic_item_placeholder)
                .placeholder(R.drawable.ic_item_placeholder)
                .into(binding.imgView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}