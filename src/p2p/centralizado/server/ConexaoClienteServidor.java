/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p.centralizado.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import modelo.Arquivo;

/**
 *
 * @author Marquinhos
 */
public class ConexaoClienteServidor implements Runnable {

    Socket conexaocliente;
    List<Cliente> listadeclientesconectados;
    Cliente cliente;

    public ConexaoClienteServidor(Socket aux, Cliente novo, List<Cliente> clientesConectados) {
        this.listadeclientesconectados = clientesConectados;
        this.conexaocliente = aux;
        this.cliente = novo;
    }

    @Override
    public void run() {

        System.out.println("Cliente conectado");

        boolean loop = true;

        while (loop) {
            try {

                String nomeArqpesq;

                DataInputStream entrada = new DataInputStream(conexaocliente.getInputStream());
                DataOutputStream saida = new DataOutputStream(conexaocliente.getOutputStream());
                String idPeer = String.valueOf(conexaocliente.getPort());

                String retorno = entrada.readUTF();

                switch (retorno) {

                    case "1":
                        System.out.println("Registrando arquivos ...");
                        registraarquivos(idPeer, entrada);

                        break;

                    case "2":
                        nomeArqpesq = entrada.readUTF();
                        System.out.println(nomeArqpesq);
                        try {
                            System.out.println("Pegando os detalhes do arquivo");

                          
                            Cliente clienteQueTemArquivo = procurarClienteQueTenhaArquivo(nomeArqpesq);
                            System.out.println("Cliente que tem o Arquivo: " + clienteQueTemArquivo);
                            if (clienteQueTemArquivo != null) {
                                saida.writeUTF(String.valueOf(clienteQueTemArquivo.getPortadedownload()));
                               
                            } else {
                                System.out.println("Arquivo nao existe");
                                saida.writeUTF("Arquivo nao existe");
                                saida.flush();
                            }


                        } catch (Exception e) {

                        }
                        break;

                    case "4":
                        conexaocliente.close();
                        listadeclientesconectados.remove(cliente);
                        System.out.println("Cliente Desconectado");
                        System.out.println("Apagar da lista");
                        loop = false;

                        break;

                    default:
                        break;
                }

            } catch (Exception e) {
            }
        }
    }

    private Cliente procurarClienteQueTenhaArquivo(String nomeArqpesq) {

        for (Cliente clienteprocurado : listadeclientesconectados) {
            if (existeArquivoQueNaoSejaDoMesmoCliente(clienteprocurado, nomeArqpesq)) {
                return clienteprocurado;
            }

        }
        return null;
    }

    private boolean existeArquivoQueNaoSejaDoMesmoCliente(Cliente c, String nomeArqpesq) {

        if ((!tanamesmaporta(c)) && (temarquivo(c, nomeArqpesq))) {

            return true;
        }
        return false;
    }

    private boolean tanamesmaporta(Cliente c) {
        return c.equals(cliente);

    }

    private boolean temarquivo(Cliente v, String nomeArqpesq) {

        for (Arquivo arquivo : v.arquivos) {

            if (arquivo.getNome().equals(nomeArqpesq)) {

                return true;

            }
        }
        return false;
    }

    private void registraarquivos(String idString, DataInputStream entrada) throws IOException {
        for (Cliente clienteprocurado : listadeclientesconectados) {
            if (clienteprocurado.getPorta().equalsIgnoreCase(idString)) {
                listadeclientesconectados.remove(cliente);
                cliente = new Cliente(entrada, idString);
                listadeclientesconectados.add(cliente);
            }
        }
    }

}
