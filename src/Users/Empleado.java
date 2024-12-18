package Users;

import java.util.Objects;

/**
 * La clase Users.Empleado es abstracta y tiene como campos su nombre, apellido, dni,
 * telefono, direccion, email, sueldo y horas extra
 * tiene un constructor ...
 * metodos: getters y setters
 * otros metodos:
 *
 * @author Brenda
 * @since 2024
 * @version 1
 */
public abstract class Empleado extends Usuario {
    protected double sueldo;

    public Empleado(String nombre, String apellido, String dni, String telefono, String direccion, String email, double sueldo) {
        super(nombre, apellido, dni, telefono, direccion, email);
        this.sueldo = sueldo;
    }

    public Empleado() {
    }

    public double getSueldo() {
        return sueldo;
    }
    public void setSueldo(double sueldo) {
        this.sueldo = sueldo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Empleado empleado)) return false;
        if (!super.equals(o)) return false;
        return Double.compare(sueldo, empleado.sueldo) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sueldo);
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "sueldo=" + sueldo +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", dni='" + dni + '\'' +
                ", telefono='" + telefono + '\'' +
                ", direccion='" + direccion + '\'' +
                ", email='" + email + '\'' +
                ", estado=" + estado +
                "} " + super.toString();
    }

}
