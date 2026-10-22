# Laporan 10 Error & Perbaikan (Soal 3)

Berikut adalah daftar 10 Error yang ditemui selama pembuatan fitur Registrasi dan Validasi (Soal 1 & 2), beserta penyebab dan kode sebelum/sesudah diperbaiki. Laporan ini siap untuk dipindahkan ke kanvas tldraw Anda.

---

### Error 1: Unresolved reference: ValidationActivity
*   **Nama Error & Deskripsi:** `Unresolved reference: ValidationActivity` pada file `RegisterActivity.kt`.
*   **Penyebabnya:** Class `ValidationActivity` belum dibuat sama sekali di dalam *project*, namun sudah dipanggil di dalam parameter `Intent`.
*   **Capture Before Fix Code:**
    ```kotlin
    val intent = Intent(this, ValidationActivity::class.java)
    startActivity(intent)
    ```
*   **Capture After Fix:**
    (Membuat file `ValidationActivity.kt` baru di dalam *package* `com.example.caca_pie`)

---

### Error 2: Unresolved reference: tvSignUp
*   **Nama Error & Deskripsi:** `Unresolved reference: tvSignUp` saat mencoba *binding* tombol pendaftaran.
*   **Penyebabnya:** Di file layout `activity_auth.xml`, komponen `TextView` untuk tulisan "Sign Up" belum diberikan ID, sehingga tidak dikenali di `AuthActivity.kt`.
*   **Capture Before Fix Code (activity_auth.xml):**
    ```xml
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Sign Up"
        android:textColor="#8B4314" />
    ```
*   **Capture After Fix:**
    ```xml
    <TextView
        android:id="@+id/tvSignUp"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Sign Up"
        android:textColor="#8B4314" />
    ```

---

### Error 3: Unresolved reference: activity_validation
*   **Nama Error & Deskripsi:** `Unresolved reference: activity_validation` pada `ValidationActivity.kt`.
*   **Penyebabnya:** File layout XML `activity_validation.xml` belum dibuat, tetapi kode `setContentView` sudah mencoba memanggilnya.
*   **Capture Before Fix Code (ValidationActivity.kt):**
    ```kotlin
    setContentView(R.layout.activity_validation) // Merah/Error
    ```
*   **Capture After Fix:**
    (Membuat file resource layout `activity_validation.xml` di dalam folder `res/layout`)

---

### Error 4: Unresolved reference: tvData
*   **Nama Error & Deskripsi:** `Unresolved reference: tvData` pada `ValidationActivity.kt`.
*   **Penyebabnya:** Mencoba menginisialisasi `TextView` dengan ID `tvData` melalui `findViewById`, tetapi komponen dengan ID tersebut belum ada di `activity_validation.xml`.
*   **Capture Before Fix Code (ValidationActivity.kt):**
    ```kotlin
    val tvData = findViewById<TextView>(R.id.tvData) // tvData error merah
    ```
*   **Capture After Fix (activity_validation.xml):**
    ```xml
    <TextView
        android:id="@+id/tvData"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textColor="#3B2314" />
    ```

---

### Error 5: Unresolved reference: btnSubmit
*   **Nama Error & Deskripsi:** `Unresolved reference: btnSubmit` pada `ValidationActivity.kt`.
*   **Penyebabnya:** ID `btnSubmit` pada layout XML belum didefinisikan atau ada *typo* penulisan nama ID saat pemanggilan.
*   **Capture Before Fix Code (activity_validation.xml):**
    ```xml
    <Button
        android:layout_width="match_parent"
        android:layout_height="52dp"
        android:text="Submit" />
    ```
*   **Capture After Fix (activity_validation.xml):**
    ```xml
    <Button
        android:id="@+id/btnSubmit"
        android:layout_width="match_parent"
        android:layout_height="52dp"
        android:text="Submit" />
    ```

---

### Error 6: Unresolved reference: btnKembali
*   **Nama Error & Deskripsi:** `Unresolved reference: btnKembali` pada `ValidationActivity.kt`.
*   **Penyebabnya:** Sama seperti Error 4 & 5, ID `btnKembali` belum disematkan pada tag `<Button>` di file XML untuk fitur kembali ke registrasi.
*   **Capture Before Fix Code (activity_validation.xml):**
    ```xml
    <Button
        android:layout_width="match_parent"
        android:layout_height="52dp"
        android:text="Kembali" />
    ```
*   **Capture After Fix (activity_validation.xml):**
    ```xml
    <Button
        android:id="@+id/btnKembali"
        android:layout_width="match_parent"
        android:layout_height="52dp"
        android:text="Kembali" />
    ```

---

### Error 7: ActivityNotFoundException untuk RegisterActivity
*   **Nama Error & Deskripsi:** `ActivityNotFoundException: Unable to find explicit activity class ... RegisterActivity` (Muncul saat Runtime/Aplikasi *Crash*).
*   **Penyebabnya:** Saat tombol Sign Up ditekan, aplikasi *crash* karena `RegisterActivity` belum didaftarkan (dideklarasikan) di dalam `AndroidManifest.xml`.
*   **Capture Before Fix Code (AndroidManifest.xml):**
    ```xml
    <!-- Hanya ada MainActivity dan AuthActivity, tidak ada RegisterActivity -->
    ```
*   **Capture After Fix (AndroidManifest.xml):**
    ```xml
    <activity
        android:name=".RegisterActivity"
        android:exported="false"
        android:windowSoftInputMode="adjustResize" />
    ```

---

### Error 8: ActivityNotFoundException untuk ValidationActivity
*   **Nama Error & Deskripsi:** `ActivityNotFoundException: Unable to find explicit activity class ... ValidationActivity` (Muncul saat Runtime/Aplikasi *Crash*).
*   **Penyebabnya:** Aplikasi *crash* saat menekan tombol "Selanjutnya" di halaman pendaftaran karena `ValidationActivity` juga belum didaftarkan di dalam `AndroidManifest.xml`.
*   **Capture Before Fix Code (AndroidManifest.xml):**
    ```xml
    <!-- Belum ada tag activity untuk ValidationActivity -->
    ```
*   **Capture After Fix (AndroidManifest.xml):**
    ```xml
    <activity
        android:name=".ValidationActivity"
        android:exported="false"
        android:windowSoftInputMode="adjustResize" />
    ```

---

### Error 9: Data SharedPreferences Kosong (Logika/Runtime)
*   **Nama Error & Deskripsi:** Data Registrasi tidak muncul (kosong) di halaman `ValidationActivity`.
*   **Penyebabnya:** Di `RegisterActivity.kt`, data sudah di-*put* ke `SharedPreferences`, tetapi lupa memanggil `editor.apply()` atau `editor.commit()`, sehingga data tidak tersimpan ke penyimpanan lokal perangkat.
*   **Capture Before Fix Code (RegisterActivity.kt):**
    ```kotlin
    val editor = sharedPref.edit()
    editor.putString("username_reg", username)
    editor.putString("password_reg", password)
    // Lupa dipanggil apply()
    val intent = Intent(this, ValidationActivity::class.java)
    ```
*   **Capture After Fix:**
    ```kotlin
    val editor = sharedPref.edit()
    editor.putString("username_reg", username)
    editor.putString("password_reg", password)
    editor.apply() // Menambahkan ini agar data tersimpan
    val intent = Intent(this, ValidationActivity::class.java)
    ```

---

### Error 10: Unresolved reference: btnBackToLogin
*   **Nama Error & Deskripsi:** `Unresolved reference: btnBackToLogin` pada `RegisterActivity.kt`.
*   **Penyebabnya:** Menambahkan fitur tombol "Kembali" (panah *back*) di bagian atas `RegisterActivity`, namun di file `activity_register.xml` komponen `ImageView` panahnya belum memiliki ID `btnBackToLogin`.
*   **Capture Before Fix Code (activity_register.xml):**
    ```xml
    <ImageView
        android:layout_width="24dp"
        android:layout_height="24dp"
        android:src="@drawable/ic_arrow_back" />
    ```
*   **Capture After Fix (activity_register.xml):**
    ```xml
    <ImageView
        android:id="@+id/btnBackToLogin"
        android:layout_width="24dp"
        android:layout_height="24dp"
        android:src="@drawable/ic_arrow_back" />
    ```

---

## Alur UI Aplikasi (Untuk Keterangan Flow di Bawah tldraw)

1.  **Halaman Sign In:** Login gagal karena belum buat akun atau data tidak sesuai -> Pengguna menekan tombol teks **Sign Up**.
2.  **Halaman Registration:** Pengguna memasukkan data registrasi lengkap (Nama, Email, Tanggal Lahir, Jenis Kelamin, dll). -> Pengguna menekan **Selanjutnya**.
3.  **Halaman Data Registrasi (Verifikasi):** Ditampilkan rekapan data inputan (Verifikasi OTP digantikan dengan Verifikasi Data).
    *   *Jika data belum sesuai/kosong/password beda:* Muncul pesan peringatan/gagal -> Tekan **Kembali** untuk mengedit data.
    *   *Jika sukses:* Muncul pesan sukses -> Tekan **Submit** dan diarahkan kembali ke halaman Sign In.
4.  **Halaman Sign In (Berhasil):** Login dengan data (Username & Password) yang sudah diregistrasikan di langkah sebelumnya (tersimpan di *SharedPreferences*) -> Berhasil masuk ke halaman Utama/Dashboard.