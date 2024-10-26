package Prototype;

import Observer.Maquina;

public interface CloneableMaquina {
    public Maquina clone() throws CloneNotSupportedException;
}
