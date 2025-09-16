package com.project.tukcompass.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.project.tukcompass.databinding.FragmentSuccessDialogBinding
import com.project.tukcompass.models.ClubSportModel
import com.project.tukcompass.models.EventModel
import com.project.tukcompass.models.SessionDisplayItem

class SuccessDialogFragment : DialogFragment() {

    private var _binding: FragmentSuccessDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        _binding = FragmentSuccessDialogBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.setCancelable(false)

        // Show the message
        val message = arguments?.getString("message") ?: "Success!"
        binding.successMessage.text = message
        binding.successAnimation.playAnimation()

        // Handle OK button click
        binding.okButton.setOnClickListener {
            dismiss()

            val navController = findNavController()
            val destination = arguments?.getInt("destination")

            val event = arguments?.getParcelable<EventModel>("event")
            val club = arguments?.getParcelable<ClubSportModel>("club")
            val unit = arguments?.getParcelable<SessionDisplayItem>("unit")

            if (destination != null) {
                val args = Bundle().apply {
                    event?.let { putParcelable("event", it) }
                    club?.let { putParcelable("club", it) }
                    unit?.let { putParcelable("unit", it) }
                }
                navController.navigate(destination, args)
            }
        }

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(
            message: String,
            destination: Int,
            event: EventModel? = null,
            club: ClubSportModel? = null,
            unit: SessionDisplayItem? = null
        ): SuccessDialogFragment {
            val fragment = SuccessDialogFragment()
            val bundle = Bundle().apply {
                putString("message", message)
                putInt("destination", destination)
                event?.let { putParcelable("event", it) }
                club?.let { putParcelable("club", it) }
                unit?.let { putParcelable("unit", it) }
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}


