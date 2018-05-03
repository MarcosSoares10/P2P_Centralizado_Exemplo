/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p.centralizado.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Marquinhos
 */
public class Servidor {

    /**
     * @param args the command line arguments
     */
    static List<Cliente> clientesConectados;

    public static void main(String[] args) throws IOException {
        // TODO code application logic here

        // System.out.println(conexao.getPort());
        clientesConectados = new ArrayList<>();

        int numeroporta = 5004;
        ServerSocket abrirconexao = new ServerSocket(numeroporta);
        System.out.println("Servidor Funcionando");
        while (!abrirconexao.isClosed()) {

            Socket conexao = abrirconexao.accept();
            Cliente novo = new Cliente(String.valueOf(conexao.getPort()));
            clientesConectados.add(novo);

            Thread thread = new Thread(new Testa_conexoes(novo, conexao, clientesConectados));
            thread.start();

            Thread tr = new Thread(new ConexaoClienteServidor(conexao, novo, clientesConectados));
            tr.start();
        }

    }

    
}

class Testa_conexoes implements Runnable {

    Socket conexaoteste;
    List<Cliente> clientesConectado;
    Cliente cliente;
    DataOutputStream saida;

    public Testa_conexoes(Cliente aux, Socket conexao, List<Cliente> clientesConectados) throws IOException {
        this.cliente = aux;
        this.conexaoteste = conexao;
        this.clientesConectado = clientesConectados;
        saida = new DataOutputStream(conexaoteste.getOutputStream());

    }

    @Override
    public void run() {
        while (true) {

            try {
                saida.writeUTF("TESTE");
                System.out.println("TESTE Conexão");

                Thread.sleep(10000);

            } catch (InterruptedException e) {  //Erro na execução.
               
                clientesConectado.remove(cliente);
                Thread.currentThread().stop();

            } catch (IOException ex) {
                clientesConectado.remove(cliente);
                Thread.currentThread().stop();
            }
            
        }
    }
}
