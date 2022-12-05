package com.example.tinpet.Entity;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Tip implements Serializable {
    private String titulo;
    private String descripcion;
    private int position;

    public Tip(){
    }

    public Tip(String titulo, String descripcion, int position) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
