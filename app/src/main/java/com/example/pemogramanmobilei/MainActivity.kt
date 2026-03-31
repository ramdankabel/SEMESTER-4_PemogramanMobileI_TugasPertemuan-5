package com.example.pemogramanmobilei

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tilNama = findViewById<TextInputLayout>(R.id.tilNama)
        val etNama = findViewById<TextInputEditText>(R.id.etNama)
        val tilEmail = findViewById<TextInputLayout>(R.id.tilEmail)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val tilPassword = findViewById<TextInputLayout>(R.id.tilPassword)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val tilConfirm = findViewById<TextInputLayout>(R.id.tilConfirmPassword)
        val etConfirm = findViewById<TextInputEditText>(R.id.etConfirmPassword)
        val rgJenisKelamin = findViewById<RadioGroup>(R.id.rgJenisKelamin)
        val tvErrorJK = findViewById<TextView>(R.id.tvErrorJenisKelamin)
        val tvErrorHobi = findViewById<TextView>(R.id.tvErrorHobi)
        val tvErrorKota = findViewById<TextView>(R.id.tvErrorKota)
        val spinner = findViewById<Spinner>(R.id.spinnerKota)
        val btnSubmit = findViewById<MaterialButton>(R.id.btnSubmit)
        val btnReset = findViewById<MaterialButton>(R.id.btnReset)

        val hobiList = listOf(
            findViewById<CheckBox>(R.id.cbMembaca),
            findViewById<CheckBox>(R.id.cbOlahraga),
            findViewById<CheckBox>(R.id.cbMusik),
            findViewById<CheckBox>(R.id.cbMasak),
            findViewById<CheckBox>(R.id.cbTravel),
            findViewById<CheckBox>(R.id.cbFotografi)
        )

        // Spinner Kota
        val kotaList = listOf(
            "Pilih Kota...", "Jakarta", "Bandung", "Surabaya",
            "Yogyakarta", "Medan", "Makassar", "Semarang", "Palembang"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, kotaList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Real-time validasi Nama
        etNama.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().trim().isEmpty()) tilNama.error = "Nama tidak boleh kosong"
                else tilNama.error = null
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Real-time validasi Email
        etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString().trim()
                when {
                    email.isEmpty() -> tilEmail.error = "Email tidak boleh kosong"
                    !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> tilEmail.error = "Format email tidak valid"
                    else -> tilEmail.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Real-time validasi Password
        etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val pass = s.toString()
                when {
                    pass.isEmpty() -> tilPassword.error = "Password tidak boleh kosong"
                    pass.length < 6 -> tilPassword.error = "Password minimal 6 karakter"
                    else -> tilPassword.error = null
                }
                val confirm = etConfirm.text.toString()
                if (confirm.isNotEmpty() && confirm != pass) tilConfirm.error = "Password tidak cocok"
                else if (confirm == pass) tilConfirm.error = null
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Real-time validasi Konfirmasi Password
        etConfirm.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val confirm = s.toString()
                val pass = etPassword.text.toString()
                when {
                    confirm.isEmpty() -> tilConfirm.error = "Konfirmasi password tidak boleh kosong"
                    confirm != pass -> tilConfirm.error = "Password tidak cocok"
                    else -> tilConfirm.error = null
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Tombol Daftar
        btnSubmit.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()
            val confirm = etConfirm.text.toString()
            val jenisKelaminDipilih = rgJenisKelamin.checkedRadioButtonId != -1
            val jumlahHobi = hobiList.count { it.isChecked }
            val kotaDipilih = spinner.selectedItemPosition != 0
            var valid = true

            if (nama.isEmpty()) { tilNama.error = "Nama tidak boleh kosong"; valid = false }
            if (email.isEmpty()) { tilEmail.error = "Email tidak boleh kosong"; valid = false }
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { tilEmail.error = "Format email tidak valid"; valid = false }
            if (password.isEmpty()) { tilPassword.error = "Password tidak boleh kosong"; valid = false }
            else if (password.length < 6) { tilPassword.error = "Password minimal 6 karakter"; valid = false }
            if (confirm.isEmpty()) { tilConfirm.error = "Konfirmasi password tidak boleh kosong"; valid = false }
            else if (confirm != password) { tilConfirm.error = "Password tidak cocok"; valid = false }

            if (!jenisKelaminDipilih) {
                tvErrorJK.text = "Pilih jenis kelamin"
                tvErrorJK.visibility = TextView.VISIBLE
                valid = false
            } else tvErrorJK.visibility = TextView.GONE

            if (jumlahHobi < 3) {
                tvErrorHobi.text = "Pilih minimal 3 hobi"
                tvErrorHobi.visibility = TextView.VISIBLE
                valid = false
            } else tvErrorHobi.visibility = TextView.GONE

            if (!kotaDipilih) {
                tvErrorKota.text = "Pilih kota asal"
                tvErrorKota.visibility = TextView.VISIBLE
                valid = false
            } else tvErrorKota.visibility = TextView.GONE

            if (valid) {
                androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Konfirmasi Pendaftaran")
                    .setMessage("Apakah data Anda sudah benar?")
                    .setPositiveButton("Ya, Daftar") { dialog, _ ->
                        Toast.makeText(
                            this,
                            "Selamat! Anda sekarang sudah terdaftar. Terima kasih!",
                            Toast.LENGTH_LONG
                        ).show()
                        dialog.dismiss()
                    }
                    .setNegativeButton("Batal") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }

        // Long Press tombol Daftar
        btnSubmit.setOnLongClickListener {
            Toast.makeText(
                this,
                "Pastikan semua data sudah benar sebelum mendaftar!",
                Toast.LENGTH_LONG
            ).show()
            true
        }

        // Tombol Reset
        btnReset.setOnClickListener {
            etEmail.text?.clear()
            etPassword.text?.clear()
            etConfirm.text?.clear()
            tilNama.error = null
            tilEmail.error = null
            tilPassword.error = null
            tilConfirm.error = null
            rgJenisKelamin.clearCheck()
            etNama.text?.clear()
            tvErrorJK.visibility = TextView.GONE
            hobiList.forEach { it.isChecked = false }
            tvErrorHobi.visibility = TextView.GONE
            spinner.setSelection(0)
            tvErrorKota.visibility = TextView.GONE
            Toast.makeText(this, "Form berhasil direset!", Toast.LENGTH_SHORT).show()
        }

        // Long Press tombol Reset
        btnReset.setOnLongClickListener {
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Konfirmasi Reset")
                .setMessage("Yakin ingin mereset semua data form?")
                .setPositiveButton("Ya, Reset") { dialog, _ ->
                    etNama.text?.clear()
                    etEmail.text?.clear()
                    etPassword.text?.clear()
                    etConfirm.text?.clear()
                    tilNama.error = null
                    tilEmail.error = null
                    tilPassword.error = null
                    tilConfirm.error = null
                    rgJenisKelamin.clearCheck()
                    tvErrorJK.visibility = TextView.GONE
                    hobiList.forEach { it.isChecked = false }
                    tvErrorHobi.visibility = TextView.GONE
                    spinner.setSelection(0)
                    tvErrorKota.visibility = TextView.GONE
                    Toast.makeText(this, "Form berhasil direset!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
            true
        }

    }
}