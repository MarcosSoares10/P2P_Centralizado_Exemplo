/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p.centralizado.cliente;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 *
 * @author Marquinhos
 */
public class Cliente_upload implements Runnable {

    String pasta_download;
    int port_num;
    Socket clientSocket;
    DataInputStream entrada;
    DataOutputStream saida;

    public Cliente_upload(String filePath, int userInpPort) {
        this.port_num = userInpPort;
        this.pasta_download = filePath;
    }

    @Override
    public void run() {
        try {

            ServerSocket downloadSocket = new ServerSocket(port_num);

            while (true) {
                clientSocket = downloadSocket.accept();
                System.out.println("Cliente conectado para compartilhamento ...");
                saida = new DataOutputStream(clientSocket.getOutputStream());
                entrada = new DataInputStream(clientSocket.getInputStream());
                String fileName = entrada.readUTF();
                System.out.println("Requisição para o arquivos: " + fileName);

                File checkFile = new File(pasta_download + "/" + fileName);
                FileInputStream fin = new FileInputStream(checkFile);
                BufferedInputStream buffReader = new BufferedInputStream(fin);

                if (!checkFile.exists()) {
                    System.out.println("O arquivo não existe");
                    buffReader.close();
                    return;
                }

                int size = (int) checkFile.length();

                byte[] buffContent = new byte[size];

                saida.writeLong(size);

                int startRead = 0;
                int numOfRead = 0;

                while (startRead < buffContent.length && (numOfRead = buffReader.read(buffContent, startRead, buffContent.length - startRead)) >= 0) {
                    startRead = startRead + numOfRead;
                }

                if (startRead < buffContent.length) {
                    System.out.println("Leitura incompleta do arquivo" + checkFile.getName());
                }

                saida.write(buffContent);
                buffReader.close();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
