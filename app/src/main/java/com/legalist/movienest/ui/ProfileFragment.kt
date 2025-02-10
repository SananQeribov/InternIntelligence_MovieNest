package com.legalist.movienest.ui

import android.Manifest
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.legalist.movienest.R
import com.legalist.movienest.base.BaseFragment
import com.legalist.movienest.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    private var selectedImageUri: Uri? = null
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var newPhotoPicker: ActivityResultLauncher<String>

    override fun buildUi() {
        initFirebase()
        setupPermissionLauncher()
        setupPhotoPicker()
        setupClickListeners()
        getData()
    }

    private fun initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.reference.child("Profiles")
        mAuth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
    }

    private fun setupPermissionLauncher() {
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    launchNewPhotoPicker()
                } else {
                    Toast.makeText(context, "Please grant permission", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun setupPhotoPicker() {
        newPhotoPicker =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                uri?.let {
                    binding.profile.setImageURI(it)
                    selectedImageUri = it
                }
            }
    }

    private fun setupClickListeners() {
        binding.profile.setOnClickListener {
            val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_IMAGES
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
            requestPermissionLauncher.launch(permission)
        }

        binding.button.setOnClickListener {
            uploadImageToFirebase()
        }

        binding.logout.setOnClickListener {
            mAuth.signOut()
            findNavController().navigate(R.id.action_profileFragment_to_signUpFragment)
        }
    }

    private fun launchNewPhotoPicker() {
        newPhotoPicker.launch("image/*")
    }

    private fun uploadImageToFirebase() {
        val userId = mAuth.currentUser?.uid ?: return
        val imageName = "images/$userId.jpg"
        val storageRef = storageReference.child(imageName)

        selectedImageUri?.let { uri ->
            storageRef.putFile(uri)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        saveUserDataToDatabase(userId, downloadUri.toString())
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        } ?: Toast.makeText(context, "No image selected", Toast.LENGTH_SHORT).show()
    }

    private fun saveUserDataToDatabase(userId: String, imageUrl: String) {
        val userAge = binding.AgeEditText.text.toString()
        val name = binding.NameEdittext.text.toString()
        val userEmail = mAuth.currentUser?.email ?: ""

        val userData = mapOf(
            "UserImageUrl" to imageUrl,
            "Name" to name,
            "UserEmail" to userEmail,
            "UserAge" to userAge
        )

        reference.child(userId).setValue(userData)
            .addOnSuccessListener {
                Toast.makeText(context, "Upload successful", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Database error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getData() {
        val userId = mAuth.currentUser?.uid ?: return

        reference.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.value as? Map<*, *>

                userData?.let {
                    binding.AgeEditText.setText(it["UserAge"] as? String ?: "")
                    binding.NameEdittext.setText(it["Name"] as? String ?: "")

                    val userImageUrl = it["UserImageUrl"] as? String
                    userImageUrl?.let { url ->
                        Picasso.get().load(url).into(binding.profile)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Data loading failed: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater, container, false)
    }
}


