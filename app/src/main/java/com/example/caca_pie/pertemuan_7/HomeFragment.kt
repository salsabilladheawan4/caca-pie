package com.example.caca_pie.pertemuan_7

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.caca_pie.R
import com.example.caca_pie.data.api.SupabaseApiClient
import com.example.caca_pie.data.model.MenuModel
import com.example.caca_pie.databinding.FragmentHomeBinding
import com.example.caca_pie.pertemuan_11.adapter.ProductAdapter
import com.example.caca_pie.pertemuan_3.DetailActivity
import com.example.caca_pie.pertemuan_5.FifthActivity
import com.example.caca_pie.pertemuan_5.WebViewActivity
import com.example.caca_pie.pertemuan_6.AuthActivity
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var productAdapter: ProductAdapter
    private var allMenus: List<MenuModel> = emptyList()
    private var selectedCategory: String = "All"

    private val categories = listOf("Hot Coffee", "Cold Coffee", "Bakery & Pastry")
    private val imageOptions = listOf(
        "Cappuccino" to "img_cappuccino",
        "Americano" to "img_americano",
        "Spanish Latte" to "img_spanish_latte",
        "Caramel Macchiato" to "img_caramel_macchiato",
        "Apple Pie" to "apple_pie"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUserHeader()
        setupRecyclerView()
        setupListeners()
        loadMenus()
    }

    private fun setupUserHeader() {
        val sharedPref = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val nama = sharedPref.getString("nama", "") ?: ""
        val username = sharedPref.getString("username", "") ?: ""
        val displayName = when {
            nama.isNotBlank() -> nama
            username.isNotBlank() -> username
            else -> "Guest"
        }
        binding.tvUserName.text = "Hi, $displayName"
    }

    private fun setupRecyclerView() {
        // Grid 2 Kolom menggunakan GridLayoutManager (Modul 11 & CRUD Supabase Modul 12)
        binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProducts.isNestedScrollingEnabled = false

        productAdapter = ProductAdapter(
            productList = emptyList(),
            onItemClick = { menu ->
                if (menu.imageKey == "apple_pie" || menu.category.equals("Bakery & Pastry", ignoreCase = true)) {
                    startActivity(Intent(requireContext(), DetailActivity::class.java))
                } else {
                    startActivity(Intent(requireContext(), FifthActivity::class.java))
                }
            },
            onEditClick = { menu ->
                showMenuFormDialog(menu)
            },
            onDeleteClick = { menu ->
                confirmDeleteMenu(menu)
            }
        )
        binding.rvProducts.adapter = productAdapter
    }

    // READ: Ambil daftar menu dari tabel mobile.menus di Supabase
    private fun loadMenus() {
        binding.pbMenuLoading.visibility = View.VISIBLE
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = SupabaseApiClient.apiService.getMenus()
                if (_binding == null) return@launch

                binding.pbMenuLoading.visibility = View.GONE
                if (response.isSuccessful) {
                    allMenus = response.body() ?: emptyList()
                    applyCategoryFilter()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Gagal memuat menu (Kode ${response.code()})",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                if (_binding == null) return@launch
                binding.pbMenuLoading.visibility = View.GONE
                Toast.makeText(
                    requireContext(),
                    "Gagal terhubung ke Supabase: ${e.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun applyCategoryFilter() {
        val filtered = if (selectedCategory == "All") {
            allMenus
        } else {
            allMenus.filter { it.category.equals(selectedCategory, ignoreCase = true) }
        }
        productAdapter.updateData(filtered)
    }

    // CREATE & UPDATE: Form Dialog untuk tambah / edit menu ke Supabase
    private fun showMenuFormDialog(existingMenu: MenuModel? = null) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_menu_form, null)
        val tvTitle = dialogView.findViewById<TextView>(R.id.tvDialogMenuTitle)
        val etName = dialogView.findViewById<EditText>(R.id.etMenuName)
        val spinnerCategory = dialogView.findViewById<Spinner>(R.id.spinnerMenuCategory)
        val etPrice = dialogView.findViewById<EditText>(R.id.etMenuPrice)
        val etOriginalPrice = dialogView.findViewById<EditText>(R.id.etMenuOriginalPrice)
        val etTag = dialogView.findViewById<EditText>(R.id.etMenuTag)
        val spinnerImage = dialogView.findViewById<Spinner>(R.id.spinnerMenuImage)

        val categoryAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            categories
        )
        spinnerCategory.adapter = categoryAdapter

        val imageAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            imageOptions.map { it.first }
        )
        spinnerImage.adapter = imageAdapter

        if (existingMenu != null) {
            tvTitle.text = "Edit Menu"
            etName.setText(existingMenu.name)
            etPrice.setText(existingMenu.price)
            etOriginalPrice.setText(existingMenu.originalPrice)
            etTag.setText(existingMenu.tag)

            val catIndex = categories.indexOfFirst { it.equals(existingMenu.category, ignoreCase = true) }
            if (catIndex >= 0) spinnerCategory.setSelection(catIndex)

            val imgIndex = imageOptions.indexOfFirst { it.second == existingMenu.imageKey }
            if (imgIndex >= 0) spinnerImage.setSelection(imgIndex)
        } else {
            tvTitle.text = "Tambah Menu Baru"
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton(if (existingMenu == null) "Simpan" else "Perbarui", null)
            .setNegativeButton("Batal") { d, _ -> d.dismiss() }
            .create()

        dialog.setOnShowListener {
            val btnSave = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            btnSave.setTextColor(Color.parseColor("#8B4314"))
            btnSave.setOnClickListener {
                val name = etName.text.toString().trim()
                val price = etPrice.text.toString().trim()
                val originalPrice = etOriginalPrice.text.toString().trim()
                val tag = etTag.text.toString().trim()
                val category = categories[spinnerCategory.selectedItemPosition]
                val imageKey = imageOptions[spinnerImage.selectedItemPosition].second

                if (name.isEmpty() || price.isEmpty()) {
                    Toast.makeText(requireContext(), "Nama dan harga menu wajib diisi", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val menuPayload = MenuModel(
                    name = name,
                    category = category,
                    price = price,
                    originalPrice = originalPrice,
                    tag = tag,
                    imageKey = imageKey
                )

                btnSave.isEnabled = false
                if (existingMenu?.id == null) {
                    createMenuInSupabase(menuPayload, dialog, btnSave)
                } else {
                    updateMenuInSupabase(existingMenu.id, menuPayload, dialog, btnSave)
                }
            }
        }
        dialog.show()
    }

    private fun createMenuInSupabase(menu: MenuModel, dialog: AlertDialog, btnSave: View) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = SupabaseApiClient.apiService.createMenu(menu)
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Menu '${menu.name}' berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    loadMenus()
                } else {
                    btnSave.isEnabled = true
                    Toast.makeText(requireContext(), "Gagal menambah menu (Kode ${response.code()})", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                btnSave.isEnabled = true
                Toast.makeText(requireContext(), "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateMenuInSupabase(id: Long, menu: MenuModel, dialog: AlertDialog, btnSave: View) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = SupabaseApiClient.apiService.updateMenu("eq.$id", menu)
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Menu '${menu.name}' berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    loadMenus()
                } else {
                    btnSave.isEnabled = true
                    Toast.makeText(requireContext(), "Gagal memperbarui menu (Kode ${response.code()})", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                btnSave.isEnabled = true
                Toast.makeText(requireContext(), "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // DELETE: Konfirmasi & hapus menu di Supabase
    private fun confirmDeleteMenu(menu: MenuModel) {
        val menuId = menu.id ?: return
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Menu")
            .setMessage("Yakin ingin menghapus '${menu.name}' dari daftar menu?")
            .setPositiveButton("Hapus") { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        val response = SupabaseApiClient.apiService.deleteMenu("eq.$menuId")
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), "Menu '${menu.name}' dihapus", Toast.LENGTH_SHORT).show()
                            loadMenus()
                        } else {
                            Toast.makeText(requireContext(), "Gagal menghapus menu (Kode ${response.code()})", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Batal") { d, _ -> d.dismiss() }
            .show()
    }

    private fun setupListeners() {
        // Tombol + Tambah Menu (Create ke Supabase)
        binding.btnAddMenu.setOnClickListener {
            showMenuFormDialog(null)
        }

        // Banner Promo "Order Now"
        binding.btnOrderNow.setOnClickListener {
            startActivity(Intent(requireContext(), FifthActivity::class.java))
        }

        // See All -> tampilkan semua & refresh data Supabase
        binding.btnSeeAll.setOnClickListener {
            selectedCategory = "All"
            selectChip(binding.chipAll)
            loadMenus()
        }

        // Tombol Kunjungi Website
        binding.cardWebsite.setOnClickListener {
            startActivity(Intent(requireContext(), WebViewActivity::class.java))
        }

        // Filter Kategori Chips
        binding.chipAll.setOnClickListener {
            selectedCategory = "All"
            selectChip(binding.chipAll)
            applyCategoryFilter()
        }
        binding.chipHotCoffee.setOnClickListener {
            selectedCategory = "Hot Coffee"
            selectChip(binding.chipHotCoffee)
            applyCategoryFilter()
        }
        binding.chipColdCoffee.setOnClickListener {
            selectedCategory = "Cold Coffee"
            selectChip(binding.chipColdCoffee)
            applyCategoryFilter()
        }
        binding.chipPastry.setOnClickListener {
            selectedCategory = "Bakery & Pastry"
            selectChip(binding.chipPastry)
            applyCategoryFilter()
        }

        // Tombol Logout
        binding.cardLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin keluar dari akun?")
                .setPositiveButton("Ya") { _, _ ->
                    val sharedPref = requireActivity()
                        .getSharedPreferences("user_pref", Context.MODE_PRIVATE)
                    sharedPref.edit().putBoolean("isLogin", false).apply()

                    val intent = Intent(requireContext(), AuthActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }

    private fun selectChip(selectedView: View) {
        val chips = listOf(binding.chipAll, binding.chipHotCoffee, binding.chipColdCoffee, binding.chipPastry)
        for (chip in chips) {
            if (chip == selectedView) {
                chip.setBackgroundResource(R.drawable.bg_chip_selected)
                chip.setTextColor(Color.WHITE)
                chip.setTypeface(null, Typeface.BOLD)
            } else {
                chip.setBackgroundResource(R.drawable.bg_chip)
                chip.setTextColor(Color.parseColor("#3B2314"))
                chip.setTypeface(null, Typeface.NORMAL)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
