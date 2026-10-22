### Error: ActivityNotFoundException untuk ValidationActivity
*   **Nama Error & Deskripsi:** `ActivityNotFoundException: Unable to find explicit activity class ... ValidationActivity` (Muncul saat Runtime/Aplikasi *Crash*).
*   **Penyebabnya:** Aplikasi *crash* (menutup paksa) saat menekan tombol "Selanjutnya" di halaman pendaftaran. Hal ini terjadi karena komponen `ValidationActivity` sudah dipanggil melalui `Intent`, namun kelas tersebut belum didaftarkan di dalam `AndroidManifest.xml`.
*   **Capture Before Fix Code (AndroidManifest.xml):**
    ```xml
    <!-- Belum ada deklarasi tag activity untuk ValidationActivity di dalam tag <application> -->
    <activity
        android:name=".RegisterActivity"
        android:exported="false"
        android:windowSoftInputMode="adjustResize" />
    ```
*   **Capture After Fix (AndroidManifest.xml):**
    ```xml
    <activity
        android:name=".RegisterActivity"
        android:exported="false"
        android:windowSoftInputMode="adjustResize" />
        
    <!-- Menambahkan deklarasi ValidationActivity agar dikenali sistem -->
    <activity
        android:name=".ValidationActivity"
        android:exported="false"
        android:windowSoftInputMode="adjustResize" />
    ```