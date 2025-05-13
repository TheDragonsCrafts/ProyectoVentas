/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package seguridad;

/**
 *
 * @author IanDa
 */

/** Mantiene en memoria el id del administrador que ha hecho login */
public final class Session {
    private static int idAdmin;
    private Session() {}

    public static void setIdAdmin(int id) {
        idAdmin = id;
    }

    public static int getIdAdmin() {
        return idAdmin;
    }
}

