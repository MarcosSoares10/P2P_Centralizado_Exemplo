/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p.centralizado.cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

/**
 *
 * @author Marquinhos
 */
public class Cliente {

    static Socket CServer;
    static String obtainFileName;
    static String obtainPeerID;

    public static void main(String[] args) throws IOException {
        String arqdire = "C:/Users/Marquinhos/Documents/NetBeansProjects/P2P-Centralizado/datacliente";

        while (true) {

            try {
                CServer = new Socket("localhost", 5004);

                int usuarioinpport = gerarportaserverdownload(CServer);
                System.out.println(usuarioinpport);
                Thread Tupload = new Thread(new Cliente_upload(arqdire, usuarioinpport));
                Tupload.start();

                String opcao = null;

                do {
                    System.out.println("****MENU****");
                    System.out.println("1. Registrar Arquivos");
                    System.out.println("2. Procurar Arquivo");
                    System.out.println("3. Obter um Arquivo");
                    System.out.println("4. Sair");

                    DataInputStream entradaSYS = new DataInputStream(System.in);
                    opcao = entradaSYS.readLine();

                    DataInputStream entrada = new DataInputStream(CServer.getInputStream());
                    DataOutputStream saida = new DataOutputStream(CServer.getOutputStream());

                    String pesquisaarquivo = null;
                    String searchResults = null;

                    switch (opcao) {
                        case "1":
                            atualizarlistaservidor(saida, opcao, usuarioinpport, arqdire);
                            break;
                        case "2":
                            saida.writeUTF(opcao);
                            saida.flush();
                            System.out.println("Nome do arquivo a ser procurado");
                            pesquisaarquivo = entradaSYS.readLine();
                            saida.writeUTF(pesquisaarquivo);
                            searchResults = entrada.readUTF();
                            System.out.println(searchResults);

                            obtainFileName = pesquisaarquivo;
                            obtainPeerID = searchResults;

                            break;

                        case "3":	//Obtain the file
                             
                            System.out.println(obtainFileName);
                            System.out.println(obtainPeerID);
                            boolean retorno = baixar(obtainFileName, obtainPeerID);
                            if (retorno == true) {
                                atualizarlistaservidor(saida, "1", usuarioinpport, arqdire);
                            }

                            break;

                        case "4":	//Exit

                            System.out.println("Conexão fechada .... !!!");
                            saida.writeUTF(opcao);
                            CServer.close();
                            System.exit(0);
                            break;

                        default:
                            break;
                    }
                } while (!opcao.equals("4"));
            } catch (Exception e) {
            }

        }

    }

    private static void atualizarlistaservidor(DataOutputStream saida, String opcao, int usuarioinpport, String arqdire) throws IOException {
        saida.writeUTF(opcao);
        saida.writeUTF(String.valueOf(usuarioinpport));
        File diretorio = new File(arqdire);
        File[] arq_compartilhado = diretorio.listFiles();
        for (File arq_compartilhado1 : arq_compartilhado) {
            saida.writeUTF("MM");
            saida.writeUTF(arq_compartilhado1.getName());
            System.out.println(arq_compartilhado1.getName());
         //   System.out.println("MM");
        }
        saida.writeUTF("FF");
    }

    private static boolean baixar(String nomeArq, String porta_exter) throws IOException, InterruptedException {

        String pasta_download = "C:/Users/Marquinhos/Documents/NetBeansProjects/P2P-Centralizado/datacliente2";

        Socket peerClient = new Socket("localhost", Integer.parseInt(porta_exter));
        System.out.println("Baixando Arquivo ...");

        DataInputStream in = new DataInputStream(peerClient.getInputStream());
        DataOutputStream out = new DataOutputStream(peerClient.getOutputStream());

        out.writeUTF(nomeArq);
        out.flush();
        out.writeUTF(porta_exter);
        long buffSize = in.readLong();
        int newBuffSize = (int) buffSize;

        byte[] b = new byte[newBuffSize];

        int numberofbytesread = in.read(b);

        try {
            FileOutputStream writeFileStream = new FileOutputStream(pasta_download + "/" + nomeArq);

            writeFileStream.write(b);

            writeFileStream.close();

            System.out.println("Baixado com Sucesso");

            return true;

        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException : " + ex);
            return false;
        } catch (IOException e) {
            e.printStackTrace();

        }
        return false;
    }

    private static int gerarportaserverdownload(Socket CServer) {
        Random random = new Random();
        int ret = random.nextInt(65350);
        if ((ret != 0) && (ret != CServer.getPort())) {
            return ret;
        } else {
            gerarportaserverdownload(CServer);
        }
        return 0;
    }

}
