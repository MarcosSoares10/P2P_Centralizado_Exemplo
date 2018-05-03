/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p.centralizado.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Arquivo;

/**
 *
 * @author Marquinhos
 */
public class Cliente {

    private String porta;
    private int portadedownload;
    private String arquivo;
    List<Arquivo> arquivos;
    Socket socket;

    public Cliente(String porta) {
        this.porta = porta;
        arquivos = new ArrayList<>();
    }

    public Cliente(DataInputStream entrada, String porta) {
        this.porta = porta;
        try {
            arquivos = new ArrayList<>();
            criarListaDeArquivo(entrada);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void criarListaDeArquivo(DataInputStream entrada) throws IOException {
        portadedownload = Integer.parseInt(entrada.readUTF());
        while (entrada.readUTF().equals("MM")) {
            String aux = entrada.readUTF();
            Arquivo arq = new Arquivo(aux);
            arquivos.add(arq);
            if (aux.equals("FF")) {
                break;
            }
            System.out.println(aux);
        }

    }

    @Override
    public boolean equals(Object objeto) {
        if (objeto instanceof Cliente) {
            Cliente alvo = (Cliente) objeto;
             return alvo.porta.equals(this.porta);
        } else {
            return false;
        }

    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.porta);
        hash = 37 * hash + Objects.hashCode(this.arquivo);
        return hash;
    }

  

  

    /**
     * @return the porta
     */
    public String getPorta() {
        return porta;
    }

    /**
     * @return the arquivo
     */
    public String getArquivo() {
        return arquivo;
    }

    /**
     * @param arquivo the arquivo to set
     */
    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    /**
     * @param porta the porta to set
     */
    public void setPorta(String porta) {
        this.porta = porta;
    }

    /**
     * @return the portadedownload
     */
    public int getPortadedownload() {
        return portadedownload;
    }

    /**
     * @param portadedownload the portadedownload to set
     */
    public void setPortadedownload(int portadedownload) {
        this.portadedownload = portadedownload;
    }

}
