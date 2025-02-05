

package com.example.prueba;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText etId, etNombre, etEmail, etPrecio, etCantidad, etFecha;
    private LinearLayout layoutCampos;
    private AdminSQLiteOpenHelper adminSQLiteOpenHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Conectar con los elementos de la interfaz
        etId = findViewById(R.id.etId);
        etNombre = findViewById(R.id.etNombre);
        etEmail = findViewById(R.id.etEmail);
        etPrecio = findViewById(R.id.etPrecio);
        etCantidad = findViewById(R.id.etCantidad);
        etFecha = findViewById(R.id.etFecha);
        layoutCampos = findViewById(R.id.layoutCampos);

        // Inicializar la base de datos
        adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(this);
        db = adminSQLiteOpenHelper.getWritableDatabase();

        // Asignar listeners a los botones
        findViewById(R.id.btnAgregarUsuario).setOnClickListener(v -> mostrarCamposAgregarUsuario());
        findViewById(R.id.btnAgregarProducto).setOnClickListener(v -> mostrarCamposAgregarProducto());
        findViewById(R.id.btnAgregarVenta).setOnClickListener(v -> mostrarCamposAgregarVenta());
        findViewById(R.id.btnModificarUsuario).setOnClickListener(v -> mostrarCamposModificarUsuario());
        findViewById(R.id.btnEliminarUsuario).setOnClickListener(v -> mostrarCamposEliminarUsuario());
        findViewById(R.id.btnReporte1).setOnClickListener(v -> reporte1());
        findViewById(R.id.btnReporte2).setOnClickListener(v -> reporte2());
        findViewById(R.id.btnReporte3).setOnClickListener(v -> reporte3());
        findViewById(R.id.btnReporte4).setOnClickListener(v -> reporte4());
    }

    // Métodos para mostrar campos según la acción
    private void mostrarCamposAgregarUsuario() {
        mostrarCampos(true, true, true, false, false, false);
        findViewById(R.id.btnConfirmar).setOnClickListener(v -> agregarUsuario());
    }

    private void mostrarCamposAgregarProducto() {
        mostrarCampos(false, true, false, true, false, false);
        findViewById(R.id.btnConfirmar).setOnClickListener(v -> agregarProducto());
    }

    private void mostrarCamposAgregarVenta() {
        mostrarCampos(true, false, false, false, true, true);
        findViewById(R.id.btnConfirmar).setOnClickListener(v -> agregarVenta());
    }

    private void mostrarCamposModificarUsuario() {
        mostrarCampos(true, true, true, false, false, false);
        findViewById(R.id.btnConfirmar).setOnClickListener(v -> modificarUsuario());
    }

    private void mostrarCamposEliminarUsuario() {
        mostrarCampos(true, false, false, false, false, false);
        findViewById(R.id.btnConfirmar).setOnClickListener(v -> eliminarUsuario());
    }

    // Método genérico para mostrar/ocultar campos
    private void mostrarCampos(boolean mostrarId, boolean mostrarNombre, boolean mostrarEmail,
                               boolean mostrarPrecio, boolean mostrarCantidad, boolean mostrarFecha) {
        etId.setVisibility(mostrarId ? View.VISIBLE : View.GONE);
        etNombre.setVisibility(mostrarNombre ? View.VISIBLE : View.GONE);
        etEmail.setVisibility(mostrarEmail ? View.VISIBLE : View.GONE);
        etPrecio.setVisibility(mostrarPrecio ? View.VISIBLE : View.GONE);
        etCantidad.setVisibility(mostrarCantidad ? View.VISIBLE : View.GONE);
        etFecha.setVisibility(mostrarFecha ? View.VISIBLE : View.GONE);
        layoutCampos.setVisibility(View.VISIBLE);
    }

    // Métodos CRUD
    private void agregarUsuario() {
        if (validarCampos(true, true, true, false, false, false)) {
            ContentValues valores = new ContentValues();
            valores.put("nombre", etNombre.getText().toString().trim());
            valores.put("email", etEmail.getText().toString().trim());

            long resultado = db.insert("Usuarios", null, valores);
            mostrarResultado(resultado, "Usuario agregado correctamente", "Error al agregar usuario");
        }
    }

    private void agregarProducto() {
        if (validarCampos(false, true, false, true, false, false)) {
            ContentValues valores = new ContentValues();
            valores.put("nombre", etNombre.getText().toString().trim());
            valores.put("precio", Double.parseDouble(etPrecio.getText().toString().trim()));

            long resultado = db.insert("Productos", null, valores);
            mostrarResultado(resultado, "Producto agregado correctamente", "Error al agregar producto");
        }
    }

    private void agregarVenta() {
        if (validarCampos(true, false, false, false, true, true)) {
            ContentValues valores = new ContentValues();
            valores.put("usuario_id", Integer.parseInt(etId.getText().toString().trim()));
            valores.put("producto_id", Integer.parseInt(etNombre.getText().toString().trim())); // Reutilizando etNombre para producto_id
            valores.put("cantidad", Integer.parseInt(etCantidad.getText().toString().trim()));
            valores.put("fecha", etFecha.getText().toString().trim());

            long resultado = db.insert("Ventas", null, valores);
            mostrarResultado(resultado, "Venta agregada correctamente", "Error al agregar venta");
        }
    }

    private void modificarUsuario() {
        if (validarCampos(true, true, true, false, false, false)) {
            ContentValues valores = new ContentValues();
            valores.put("nombre", etNombre.getText().toString().trim());
            valores.put("email", etEmail.getText().toString().trim());

            int filasAfectadas = db.update("Usuarios", valores, "id=?", new String[]{etId.getText().toString().trim()});
            mostrarResultado(filasAfectadas, "Usuario modificado correctamente", "No se encontró usuario con ese ID");
        }
    }

    private void eliminarUsuario() {
        if (validarCampos(true, false, false, false, false, false)) {
            int filasEliminadas = db.delete("Usuarios", "id=?", new String[]{etId.getText().toString().trim()});
            mostrarResultado(filasEliminadas, "Usuario eliminado correctamente", "No se encontró usuario con ese ID");
        }
    }

    // Métodos de reportes
    private void reporte1() {
        ejecutarConsulta("SELECT * FROM Usuarios", "Usuarios Registrados:");
    }

    private void reporte2() {
        ejecutarConsulta("SELECT * FROM Productos", "Productos Registrados:");
    }

    private void reporte3() {
        ejecutarConsulta("SELECT Ventas.id, Usuarios.nombre AS usuario, Productos.nombre AS producto, Ventas.cantidad, Ventas.fecha " +
                "FROM Ventas " +
                "JOIN Usuarios ON Ventas.usuario_id = Usuarios.id " +
                "JOIN Productos ON Ventas.producto_id = Productos.id", "Ventas Registradas:");
    }

    private void reporte4() {
        ejecutarConsulta("SELECT Usuarios.nombre, SUM(Ventas.cantidad * Productos.precio) AS total_gastado " +
                "FROM Ventas " +
                "JOIN Usuarios ON Ventas.usuario_id = Usuarios.id " +
                "JOIN Productos ON Ventas.producto_id = Productos.id " +
                "GROUP BY Usuarios.nombre", "Total gastado por usuario:");
    }

    // Métodos auxiliares
    private boolean validarCampos(boolean id, boolean nombre, boolean email, boolean precio, boolean cantidad, boolean fecha) {
        if (id && etId.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa el ID", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (nombre && etNombre.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa el nombre", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (email && etEmail.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa el email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (precio && etPrecio.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa el precio", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (cantidad && etCantidad.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa la cantidad", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (fecha && etFecha.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa la fecha", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void mostrarResultado(long resultado, String mensajeExito, String mensajeError) {
        if (resultado != -1 && resultado > 0) {
            Toast.makeText(this, mensajeExito, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, mensajeError, Toast.LENGTH_SHORT).show();
        }
    }

    private void ejecutarConsulta(String consulta, String titulo) {
        Cursor cursor = db.rawQuery(consulta, null);
        StringBuilder resultado = new StringBuilder(titulo + "\n\n");

        if (cursor.moveToFirst()) {
            do {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    resultado.append(cursor.getColumnName(i)).append(": ").append(cursor.getString(i)).append("\n");
                }
                resultado.append("\n");
            } while (cursor.moveToNext());
        } else {
            resultado.append("No se encontraron resultados.");
        }

        cursor.close();

        new AlertDialog.Builder(this)
                .setTitle("Reporte")
                .setMessage(resultado.toString())
                .setPositiveButton("Cerrar", null)
                .show();
    }
}

