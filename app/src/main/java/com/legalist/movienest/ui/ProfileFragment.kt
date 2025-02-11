package com.legalist.movienest.ui
import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
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
                    showPermissionDialog()
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

    private fun showPermissionDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("İzin Gerekli")
            .setMessage("Profil fotoğrafı seçebilmek için izin vermelisiniz.")
            .setPositiveButton("Ayarlar") { _, _ ->
                val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", requireContext().packageName, null)
                startActivity(intent)
            }
            .setNegativeButton("İptal", null)
            .show()
    }

    private fun launchNewPhotoPicker() {
        newPhotoPicker.launch("image/*")
    }

    private fun uploadImageToFirebase() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Toast.makeText(context, "Lütfen önce giriş yapın", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = user.uid
        if (selectedImageUri == null) {
            Toast.makeText(context, "Lütfen bir resim seçin", Toast.LENGTH_SHORT).show()
            return
        }

        val imageName = "images/${userId}.jpg"
        val storageRef = storageReference.child(imageName)

        val progressDialog = ProgressDialog(requireContext()).apply {
            setMessage("Yükleniyor...")
            setCancelable(false)
            show()
        }

        storageRef.putFile(selectedImageUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    saveUserDataToDatabase(userId, downloadUri.toString())
                    progressDialog.dismiss()
                    Toast.makeText(context, "Yükleme başarılı!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(context, "Yükleme başarısız: ${it.message}", Toast.LENGTH_SHORT).show()
            }
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
                Snackbar.make(binding.root, "Profil güncellendi!", Snackbar.LENGTH_LONG).show()
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
                        Glide.with(requireContext()).load(url).placeholder(R.drawable.spiderman).into(binding.profile)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Data loading failed: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater, container, false)
    }
}
