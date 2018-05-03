/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Marquinhos
 */
public class Arquivo {
    
    private final String nome;
    private  int tamanhoEmBytes;

    
    public Arquivo(String nome){
      this.nome = nome;
    }
    
    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    

    /**
     * @return the tamanhoEmBytes
     */
    public int getTamanhoEmBytes() {
        return tamanhoEmBytes;
    }

    /**
     * @param tamanhoEmBytes the tamanhoEmBytes to set
     */
    public void setTamanhoEmBytes(int tamanhoEmBytes) {
        this.tamanhoEmBytes = tamanhoEmBytes;
    }
    
}
