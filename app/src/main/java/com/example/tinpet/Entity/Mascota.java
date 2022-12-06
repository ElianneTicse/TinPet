package com.example.tinpet.Entity;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Mascota implements Serializable {

    @Exclude
    private String uid;
    private String nickname;
    private String tipo;
    private String sexo;
    private String nombreMascota;
    private int edad;
    private String sobreMascota;
    private String urlFotoPrincipal;
    private List<String> urlFotos;
    private String nombreDuenio;
    private int numeroDuenio;
    private String correoDuenio;
    private String ubicacion;
    private String rol;
    private List<String> amigos;

    public Mascota(){
    }

    public List<String> getAmigos() {
        return amigos;
    }

    public void setAmigos(List<String> amigos) {
        this.amigos = amigos;
    }

    public Mascota(String nickname, String tipo, String sexo, String nombreMascota, int edad, String sobreMascota, String urlFotoPrincipal, List<String> urlFotos) {
        this.nickname = nickname;
        this.tipo = tipo;
        this.sexo = sexo;
        this.nombreMascota = nombreMascota;
        this.edad = edad;
        this.sobreMascota = sobreMascota;
        this.urlFotoPrincipal = urlFotoPrincipal;
        this.urlFotos = urlFotos;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getUid() {
        return uid;
    }

    public Mascota(String nickname, String tipo, String sexo, String nombreMascota, int edad, String sobreMascota) {
        this.nickname = nickname;
        this.tipo = tipo;
        this.sexo = sexo;
        this.nombreMascota = nombreMascota;
        this.edad = edad;
        this.sobreMascota = sobreMascota;
    }


    public Mascota(String nickname, String tipo, String sexo, String nombreMascota, int edad, String sobreMascota, String urlFotoPrincipal, ArrayList<String> urlFotos, String nombreDuenio, int numeroDuenio, String correoDuenio, String ubicacion, String rol) {
        this.nickname = nickname;
        this.tipo = tipo;
        this.sexo = sexo;
        this.nombreMascota = nombreMascota;
        this.edad = edad;
        this.sobreMascota = sobreMascota;
        this.urlFotoPrincipal = urlFotoPrincipal;
        this.urlFotos = urlFotos;
        this.nombreDuenio = nombreDuenio;
        this.numeroDuenio = numeroDuenio;
        this.correoDuenio = correoDuenio;
        this.ubicacion = ubicacion;
        this.rol = rol;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNombreMascota() {
        return nombreMascota;
    }

    public void setNombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getSobreMascota() {
        return sobreMascota;
    }

    public void setSobreMascota(String sobreMascota) {
        this.sobreMascota = sobreMascota;
    }

    public String getUrlFotoPrincipal() {
        return urlFotoPrincipal;
    }

    public void setUrlFotoPrincipal(String urlFotoPrincipal) {
        this.urlFotoPrincipal = urlFotoPrincipal;
    }

    public List<String> getUrlFotos() {
        return urlFotos;
    }

    public void setUrlFotos(List<String> urlFotos) {
        this.urlFotos = urlFotos;
    }

    public String getNombreDuenio() {
        return nombreDuenio;
    }

    public void setNombreDuenio(String nombreDuenio) {
        this.nombreDuenio = nombreDuenio;
    }

    public int getNumeroDuenio() {
        return numeroDuenio;
    }

    public void setNumeroDuenio(int numeroDuenio) {
        this.numeroDuenio = numeroDuenio;
    }

    public String getCorreoDuenio() {
        return correoDuenio;
    }

    public void setCorreoDuenio(String correoDuenio) {
        this.correoDuenio = correoDuenio;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}
