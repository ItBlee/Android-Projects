package com.example.lab5;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lab5.DAO.SanPhamDAO;
import com.example.lab5.adapter.SanPhamAdapter;
import com.example.lab5.model.SanPham;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private  SanPhamDAO sanPhamDAO;
    private ListView lvSanPham;
    private int idProductUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sanPhamDAO = new SanPhamDAO(this);

        lvSanPham = findViewById(R.id.lvSanPham);
        EditText edtTenSP = findViewById(R.id.edtTenSP);
        EditText edtGiaSP = findViewById(R.id.edtGiaSP);
        EditText edtHinhSP = findViewById(R.id.edtHinhSP);
        Button btnThem = findViewById(R.id.btnThem);
        Button btnCapNhat = findViewById(R.id.btnCapNhat);

//Lab5
//        ArrayList<SanPham> alSanPham = new ArrayList<SanPham>();
//
//        alSanPham.add(new SanPham(1,"Sản phẩm A", 1000, "img.png"));
//        alSanPham.add(new SanPham(2,"Sản phẩm B", 7000, "img.png"));
//        alSanPham.add(new SanPham(3,"Sản phẩm C", 6000, "img.png"));
//        alSanPham.add(new SanPham(4,"Sản phẩm D", 4000, "img.png"));
//        alSanPham.add(new SanPham(5,"Sản phẩm E", 3000, "img.png"));
//        getData();

//Lab6
        getData();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edtTenSP.getText().toString();
                int gia = Integer.parseInt(edtGiaSP.getText().toString());
                String hinh = edtHinhSP.getText().toString();

                SanPham sp = new SanPham();
                sp.setTenSP(ten);
                sp.setGiaSP(gia);
                sp.setHinhSP(hinh);
                if(sanPhamDAO.insertProduct(sp)){
                    getData();
                }else {
                    Toast.makeText(MainActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lvSanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                SanPham sanPham = (SanPham) adapterView.getAdapter().getItem(i);
                edtTenSP.setText(sanPham.getTenSP());
                edtGiaSP.setText(String.valueOf(sanPham.getGiaSP()));
                edtHinhSP.setText(sanPham.getHinhSP());
                idProductUpdate = sanPham.getId();
            }
        });

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edtTenSP.getText().toString();
                int gia = Integer.parseInt(edtGiaSP.getText().toString());
                String hinh = edtHinhSP.getText().toString();
                SanPham sp = new SanPham(idProductUpdate, ten, gia ,hinh);
                if(sanPhamDAO.updateProduct(sp)){
                    getData();
                }else {
                    Toast.makeText(MainActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lvSanPham.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long id) {
                SanPham sp = (SanPham) adapterView.getAdapter().getItem(i);
                int idDelete = sp.getId();
                String tensp = sp.getTenSP();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn có muốn xóa sản phẩm" +tensp+" không?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if(sanPhamDAO.deleteProduct(idDelete)){
                            getData();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Không xóa được", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return false;
            }
        });
    }
    private void getData(){
        ArrayList<SanPham> alSanPham = sanPhamDAO.getAll();


        SanPhamAdapter adapter = new SanPhamAdapter(alSanPham, MainActivity.this);

        lvSanPham.setAdapter(adapter);
    }
}