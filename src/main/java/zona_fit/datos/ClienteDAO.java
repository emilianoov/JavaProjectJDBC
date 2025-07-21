package zona_fit.datos;

import zona_fit.dominio.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static zona_fit.conexion.Conexion.getConexion;

public class ClienteDAO implements IClienteDAO{
    @Override
    public List<Cliente> listarCliente() {
        List<Cliente> clientes = new ArrayList<>();
        PreparedStatement ps;
        ResultSet rs;
        Connection con = getConexion();
        var sql = "SELECT * FROM CLIENTE ORDER BY ID";

        try{
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()){
                var cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setMembresia(rs.getInt("membresia"));
                clientes.add(cliente);
            }
        }catch (Exception e){
            System.out.println("Error al listar clientes " + e.getMessage());
        }finally {
            try {
                con.close();
            }catch (Exception e){
                System.out.println("Error al cerrar la conexión " + e.getMessage());
            }
        }

        return clientes;
    }

    @Override
    public boolean buscarClientePorId(Cliente cliente) {
        PreparedStatement ps;
        ResultSet rs;
        var con = getConexion();
        var sql = "SELECT * FROM CLIENTE WHERE ID = ?";

        try{
            ps = con.prepareStatement(sql);
            ps.setInt(1, cliente.getId());
            rs = ps.executeQuery();
            if(rs.next()){
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setMembresia(rs.getInt("membresia"));
                return true;
            }
        }catch (Exception e){
            System.out.println("Error al recuperar cliente por id: " + e.getMessage());
        }finally {
            try {
                con.close();
            }catch (Exception e){
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
        return false;
    }

    @Override
    public boolean agregarCliente(Cliente cliente) {
        PreparedStatement ps;
        Connection con = getConexion();
        String sql = "INSERT INTO CLIENTE(NOMBRE, APELLIDO, MEMBRESIA)" +
                " VALUES(?, ?, ?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setInt(3, cliente.getMembresia());
            ps.execute();
            return  true;
        }catch (Exception e){
            System.out.println("Error al agregar cliente: " + e.getMessage());
        }finally {
            try{
                con.close();
            }catch (Exception e){
                System.out.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
        return false;
    }

    @Override
    public boolean modificarCliente(Cliente cliente) {
        PreparedStatement ps;
        String sql = "UPDATE CLIENTE SET NOMBRE = ?, APELLIDO = ?, MEMBRESIA = ?" +
                " WHERE ID = ?";
        Connection con = getConexion();
        try{
            ps = con.prepareStatement(sql);
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setInt(3, cliente.getMembresia());
            ps.setInt(4, cliente.getId());
            ps.execute();
            return  true;
        }catch (Exception e){
            System.out.println("Error al editar el cliente: " + e.getMessage());
        }finally {
            try {
                con.close();
            }catch (Exception e){
                System.out.println("Error al cerrar conexion: " + getConexion());
            }
        }
        return false;
    }

    @Override
    public boolean eliminarCliente(Cliente cliente) {
        PreparedStatement ps;
        Connection con = getConexion();
        String sql = "DELETE FROM CLIENTE WHERE ID = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, cliente.getId());
            ps.execute();
            return true;
        }catch (Exception e){
            System.out.println("Error al eliminar el cliente: " + e.getMessage());
        }finally {
            try {
                con.close();
            }catch (Exception e){
                System.out.println("Error al cerrar la conexion: " + e.getMessage());
            }
        }
        return false;
    }

    public static void main(String[] args) {
        // Listar Clientes
//        System.out.println("*** Listar Clientes ***");
        IClienteDAO clienteDao = new ClienteDAO();
//        var clientes = clienteDao.listarCliente();
//        clientes.forEach(System.out::println);

//        Buscar clientes por ID
//        var cliente1 = new Cliente(2);
//        System.out.println("Cliente antes de la busqueda: "+ cliente1);
//        var encontrado = clienteDao.buscarClientePorId(cliente1);
//        if (encontrado)
//            System.out.println("Cliente encontrado: " + cliente1);
//        else
//            System.out.println("No se encontro cliente: " + cliente1.getId());

//        Agregar Clientes
//        var nuevoCliente = new Cliente("Manuel", "Beltran", 250);
//        var agregado = clienteDao.agregarCliente(nuevoCliente);
//        if (agregado) {
//            System.out.println("Cliente insertado correctamente: " + nuevoCliente);
//        }else{
//            System.out.println("No se agrgego el cliente: " + nuevoCliente);
//        }

//        Actualizar Clientes
//        var updateCliente = new Cliente(6,"Pedro", "Ramirez", 500);
//        var update = clienteDao.modificarCliente(updateCliente);
//
//        if (update)
//            System.out.println("Cliente actualizado: " + updateCliente);
//        else
//            System.out.println("Error al actualizar al cliente: " + updateCliente);

        // Eliminar Clientes
        var deleteCliente = new Cliente(7);
        var delete = clienteDao.eliminarCliente(deleteCliente);

        if (delete)
            System.out.println("Cliente eliminado correctamente: " + deleteCliente);
        else
            System.out.println("Error al eliminar");

        //Listar clientes
        System.out.println("*** Listar Clientes ***");
        var clientes = clienteDao.listarCliente();
        clientes.forEach(System.out::println);
    }
}
