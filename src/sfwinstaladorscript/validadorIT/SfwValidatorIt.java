/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 
 */
 
package sfwinstaladorscript.validadorIT;

import java.awt.Color;
import java.awt.GridLayout;
import java.sql.Statement;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import sfwinstaladorscript.Install;
import sfwinstaladorscript.SfwInstaladorScriptView;
import sfwinstaladorscript.interfaces.SfwWizardPage;
import sfwinstaladorscript.database.OracleConnection;

/**
 * Página de apresentação.
 */
public class SfwValidatorIt extends javax.swing.JPanel implements SfwWizardPage{
    
    /** Creates new form SfwValidator */
    public SfwValidatorIt() throws FileNotFoundException, SQLException{    

        initComponents();
        
    }
    
   
    //para teste
    public static void main(String[] args) throws FileNotFoundException, SQLException{
   
        //novo frame com nova linha
  
        javax.swing.JFrame teste = new javax.swing.JFrame("Teste");
        teste.setVisible(true);
        teste.setSize(800,1000);
        teste.setLayout(new GridLayout(2,2));
        teste.setDefaultCloseOperation(teste.EXIT_ON_CLOSE);
     
        teste.add(montaTela());
     
    }
    //para teste
    
    private static JPanel montaTela() throws FileNotFoundException, SQLException{
        
        //Cria novo JPanel
        JPanel panel = new JPanel();
        
        //Cria GridLayout com capacidade para 10 linhas de validação
        GridLayout layout = new GridLayout(10,1);
        panel.setLayout(layout);
        panel.setVisible(true);
        
        
        //Array de arquivos do diretório
        List<String> todosArquivos = listaArquivos(".//validaSql//");
        
        //Percorre todos os arquivos listados
        for(int i = 0; i < todosArquivos.size(); i++){
            
            //Cria uma nova linha para cada arquivo executado
            //Busca a descrição do arquivo
            String nomeLabel = editaNomeValidacao(todosArquivos.get(i));
                
            //Busca o retorno do script de validação e seta como label do botão
            String executionStatus  = executaArquivoDBMSRetornaStatus(todosArquivos.get(i));
            
            //Log de execução
            String executionLog = executaArquivoSqlDBMS(todosArquivos.get(i));
            
            //Adiciona a nova linha ao panel 
            panel.add(new sfwValidatorLine(nomeLabel,executionStatus,executionLog));    
        }
        
        return panel;
    }
    
    /**
     * Lista todos arquivos do diretorio
     * @param diretorioSql
     * @return all files on directory
     */
    private static List<String> listaArquivos(String diretorioSql) {
        //Declaração do array que receberá o nome dos arquivos
        List<String> results = new ArrayList<String>();

        //Declaração do array de arquivos
        File[] files = new File(diretorioSql).listFiles();
        //Se o path não se referenciar a um diretorio, então listFiles() retorna null. 

        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getName());
            }
        }
         return results;
    }
    
   /**
     * Edita o nome do script a ser exibido na tela de execução
     * @param nomeArquivo
     * @return formated name of script
     * @throws FileNotFoundException 
     */
   private static String editaNomeValidacao(String nomeArquivo) throws FileNotFoundException{
       
    File file = new File(".//validaSql/" + nomeArquivo);
    Scanner sc = new Scanner(file); 
  
    // Nome do teste deve ser inserido como comentário e encerrado com "--" 
    sc.useDelimiter("--");
    return sc.next();
}
   /**
    * Lê conteúdo do arquivo e retorna  no formato de String
    * @param nomeArquivo
    * @return
    * @throws FileNotFoundException 
    */
   
   private static String leArquivoSql(String nomeArquivo) throws FileNotFoundException{
       
    File file = new File(".//validaSql/" + nomeArquivo);
    Scanner sc = new Scanner(file); 
    //Delimita a leitura na última barra do script
    sc.useDelimiter("/");
    
    return sc.next();
}
       
      /**
      * Configura a conexão padrao
      * @return connection
      * @throws SQLException 
      */
private static Connection configConect() throws SQLException{
      //Objeto de conexão com o banco
      OracleConnection conn = new OracleConnection();
      
      //Parametros de conexao com o banco
      conn.set_username("TRIOSAP");
      conn.set_password("TRIOSAP");
      conn.set_tns("ORCL");
      
      //Seta locale default por não usar tradução nos labels
      Locale l = Locale.US;
      Install.set_us_location(l);
      Install.set_default_location(l);
      
      Connection connection = conn.Connect();
      return connection;   
}

/**
 * Executa arquivo .sql recebido
 * @param nomeArquivo
 * @throws SQLException
 * @throws FileNotFoundException 
 */
private static String executaArquivoSqlDBMS(String nomeArquivo) throws SQLException, FileNotFoundException{
    String retorno= "";
    
    //Conectando banco
    Connection conD = configConect();
   
    //Instanciando objetos
    Statement stmt = conD.createStatement();
    DbmsOutput dbmsOutput = new DbmsOutput(conD);
    
    //Seta tamanho
    dbmsOutput.enable(1000000);
    
    //Executa o comando dentro do arquivo listado
    stmt.execute(leArquivoSql(nomeArquivo));
    
    stmt.close();
    
    //Exibe retorno do DBMS_OUTPUT
    //dbmsOutput.show();
   
    retorno = dbmsOutput.returnExecution();
    conD.close();
    
    return retorno;
}
     
/**
 * Executa arquivo .sql recebido
 * @param nomeArquivo
 * @throws SQLException
 * @throws FileNotFoundException 
 */
private static String executaArquivoDBMSRetornaStatus(String nomeArquivo) throws SQLException, FileNotFoundException{
    String retorno= "";
    
    //Conectando banco
    Connection conD = configConect();
   
    //Instanciando objetos
    Statement stmt = conD.createStatement();
    DbmsOutput dbmsOutput = new DbmsOutput(conD);
    
    //Seta tamanho
    dbmsOutput.enable(1000000);
    
    //Executa o comando dentro do arquivo listado
    stmt.execute(leArquivoSql(nomeArquivo));
    
    stmt.close();
    
    //Exibe retorno do DBMS_OUTPUT
    //dbmsOutput.show();
   
    retorno = dbmsOutput.returnStatus() == true? "OK": "NOK";
    
    conD.close();
    
    return retorno;
}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sfwinstaladorscript.SfwInstaladorScriptApp.class).getContext().getResourceMap(SfwValidatorIt.class);
        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setName("Form"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 610, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 358, Short.MAX_VALUE)
        );

        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jButton1))
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton1))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        try {
            
            JOptionPane.showMessageDialog(new JFrame(),
                    montaTela(),
                    "Blah",
                    JOptionPane.ERROR_MESSAGE);
            
            //JButton a = new JButton("sd");
            //jPanel1.setLayout(new GridLayout(10,1));
            //jPanel1.add(a);
            
            //System.out.println("Sim");
            
            jPanel1.add(montaTela());       
            
           //jPanel1.setBackground(Color.green);
           // jPanel1.setBackground(Color.green);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SfwValidatorIt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SfwValidatorIt.class.getName()).log(Level.SEVERE, null, ex);
        }
 
       
    }//GEN-LAST:event_jButton1MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

    
    @Override
    public void nextClick(Hashtable wzPages, SfwInstaladorScriptView view) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void backClick(Hashtable wzPages, SfwInstaladorScriptView view) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void flowSetup(SfwInstaladorScriptView view) {
        view.getBackButton().setEnabled(true);
        view.getNextButton().setEnabled(true);
    }

  
}
