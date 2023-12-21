package com.example.todo;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.todo.Model.ToDoModel;
import com.example.todo.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class tambahTask extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";

    private EditText tambahTask;
    private Button tambahBtn;
    private DatabaseHandler db;

    public static tambahTask newInstance() {
        return new tambahTask();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.styleDialog); //STYLE_NORMAL menunjukkan bahwa dialog ini akan memiliki gaya default
        //R.style.styleDialog: Parameter kedua adalah referensi ke style yang didefinisikan di file resources (biasanya di res/values/styles.xml).
        // Ini mengatur tampilan atau atribut-atribut tertentu untuk dialog, seperti warna, ukuran font, tata letak, dll.
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_baru, container, false);
        //layout yang didefinisikan dalam file XML dengan nama task_baru diinflasi (diubah menjadi objek View) menggunakan LayoutInflater.
        // Ini mengambil layout XML dan mengubahnya menjadi tampilan yang dapat ditampilkan di dalam dialog.
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE); // Baris ini mengatur mode input pada jendela dialog.
        // setSoftInputMode() digunakan untuk mengatur perilaku keyboard saat muncul. Dalam kasus ini,
        // SOFT_INPUT_ADJUST_RESIZE mengatur agar jendela dialog dapat menyesuaikan ukurannya ketika keyboard muncul, sehingga tampilan di dalam dialog tidak tertutup oleh keyboard.
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tambahTask = getView().findViewById(R.id.taskBaruTxt);
        tambahBtn = getView().findViewById(R.id.taskbaruBtn);

        boolean isUpdate = false;

        final Bundle bundle = getArguments(); // Mendapatkan argumen yang dikirimkan ke fragment ini (jika ada).
        if (bundle != null) {
            isUpdate = true; //Jika bundle tidak null, variabel isUpdate diatur menjadi true. Ini menunjukkan bahwa fragment ini digunakan untuk melakukan pembaruan (update) terhadap data yang ada, bukan menambahkan data baru.
            String task = bundle.getString("task");
            tambahTask.setText(task);
            assert task != null; // Ini adalah pernyataan assert yang memastikan bahwa task tidak bernilai null sebelum dilakukan pemeriksaan panjang teksnya.
            if (task.length() > 0)
                tambahBtn.setTextColor(ContextCompat.getColor(getContext(), com.google.android.material.R.color.design_default_color_primary_dark));
                    // Jika teks dari tugas yang diterima memiliki panjang lebih dari 0 (tidak kosong), maka tombol tambahBtn akan diubah warnanya menjadi warna primer yang sudah ditentukan
        }

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        tambahTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    tambahBtn.setEnabled(false); //Blok kode ini memeriksa apakah teks yang dimasukkan ke dalam EditText (s) merupakan string kosong.
                    // Jika s kosong, maka:
                    tambahBtn.setTextColor(Color.GRAY);
                } else {
                    tambahBtn.setEnabled(true);
                    tambahBtn.setTextColor(ContextCompat.getColor(getContext(), com.google.android.material.R.color.design_default_color_primary_dark));
                }
                // Jadi, blok kode ini mengatur keadaan tombol tambahBtn (aktif/non-aktif dan warna teksnya) berdasarkan apakah ada teks yang dimasukkan ke dalam tambahTask. Saat tambahTask kosong,
                // tombol menjadi tidak aktif dan memiliki warna teks yang berbeda untuk memberikan petunjuk visual kepada pengguna.
                // Saat ada teks di tambahTask, tombol menjadi aktif dan kembali ke warna teks yang sebelumnya ditentukan.

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final boolean finalIsUpdate = isUpdate;
        tambahBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = tambahTask.getText().toString();
                if(finalIsUpdate){
                    db.updateTask(bundle.getInt("id"), text);
                } else {
                    ToDoModel task = new ToDoModel();
                    task.setTask(text);
                    task.setStatus(0);
                    db.insertTask(task);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog){ //sperti dialogfragment untuk menambah tugas/input baru
        Activity activity = getActivity();
        if(activity instanceof  ListenDialog){
            ((ListenDialog)activity).tutupDialog(dialog);
        }
    }


}
