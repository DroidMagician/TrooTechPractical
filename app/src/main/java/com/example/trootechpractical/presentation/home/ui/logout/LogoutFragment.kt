package com.example.trootechpractical.presentation.home.ui.logout

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trootechpractical.R
import com.example.trootechpractical.databinding.FragmentLogoutBinding
import com.example.trootechpractical.presentation.firebaseLogin.FirebaseLoginActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LogoutFragment : Fragment() {

    private var _binding: FragmentLogoutBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(LogoutViewModel::class.java)

        _binding = FragmentLogoutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnLogout.setOnClickListener {
            displayConfirmationDialog(slideshowViewModel)
        }
        return root
    }

    private fun displayConfirmationDialog(slideshowViewModel: LogoutViewModel) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setMessage(getString(R.string.confirm_exit))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes)
            ) { dialog, id ->
                slideshowViewModel.doLogout()
                startActivity(Intent(requireContext(), FirebaseLoginActivity::class.java))
                requireActivity().finishAffinity()
            }
            .setNegativeButton(getString(R.string.no),
                DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}