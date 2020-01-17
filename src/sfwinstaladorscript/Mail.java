package sfwinstaladorscript;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Mail {

    private static final String SMTP_HOST_NAME = Utils.decrypt("*\"U1b!Zx]pasXtQtSuTq[/^nW/`c_/Rn\\dYgV2"); // smtp da softway
    private static final String SMTP_AUTH_USER = Utils.decrypt("%%[pVn]$Ws_$XtYe\\sZy^$"); // usuário para autenticação externa
    private static final String SMTP_AUTH_PWD = Utils.decrypt("&\"]!^!W2Z3Y1V1[!U4\\!X4"); // senha para autenticação externa

    public void post(StringBuilder msg, String para, File arquivo)
            throws AddressException, MessagingException {
        boolean debug = false;

        //
        // Seta o endereço do HOST
        //
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");

        //
        // Cria propriedades e seta o valor padrão da sessão
        //
        Authenticator auth = new SMTPAuthenticator();
        Session mailSession = Session.getDefaultInstance(props, auth);
        mailSession.setDebug(debug);

        Transport transport = mailSession.getTransport();

        //
        // Cria a mensagem
        //
        Message mensagem = new MimeMessage(mailSession);
        mensagem.setSubject("[Instalador de Scripts] Envio automático de e-mail");

        //
        // Seta o endereço para quem irá enviar
        //
        InternetAddress addressFrom;
        try {
            addressFrom = new InternetAddress(Utils.decrypt("*#QedcS0[BhoZqTrcue0`y]qXw_v\\uYvVqfeWfgqUt^hi\"acb{Ro"),
                    "Gerência de Configuração Softway");
            mensagem.setFrom(addressFrom);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
        }

        InternetAddress[] addressTo = getAdresses(para);
        mensagem.setRecipients(Message.RecipientType.TO, addressTo);

        //
        // Cria o objeto que recebe o texto do corpo do email
        //
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent(getHeader() + msg.toString() + getFooter(), "text/html");
        Multipart mps = new MimeMultipart();

        //
        // Cria um novo objeto para cada arquivo, e anexa o arquivo
        //
        if (arquivo != null) {
            MimeBodyPart attachFilePart = new MimeBodyPart();
            FileDataSource fds = new FileDataSource(arquivo);
            attachFilePart.setDataHandler(new DataHandler(fds));
            attachFilePart.setFileName(fds.getName());

            //
            // Adiciona os anexos da mensagem
            //
            mps.addBodyPart(attachFilePart, 0);
        }

        //
        //adiciona o corpo texto da mensagem
        //
        mps.addBodyPart(textPart);

        //
        //adiciona a mensagem o conteúdo texto e anexo
        //
        mensagem.setContent(mps);
        mensagem.addHeader("MyHeaderName", "Instalador de Scripts");
        mensagem.setSentDate(new Date());

        transport.connect();
        transport.sendMessage(mensagem,
                mensagem.getRecipients(Message.RecipientType.TO));
        transport.close();
    }

    /**
     *
     * @param para
     * @return
     * @throws AddressException
     */
    private static InternetAddress[] getAdresses(String para)
            throws AddressException {
        String[] adds = para.split(";");
        InternetAddress[] mails = new InternetAddress[adds.length];
        int i = 0;
        for (String a : adds) {
            mails[i] = new InternetAddress(a);
            i++;
        }
        return mails;
    }

    /**
     *  Função para autenticação
     */
    private class SMTPAuthenticator extends javax.mail.Authenticator {

        public PasswordAuthentication getPasswordAuthentication() {
            String username = SMTP_AUTH_USER;
            String password = SMTP_AUTH_PWD;
            return new PasswordAuthentication(username, password);
        }
    }

    /**
     *
     * @return
     */
    private static String getHeader() {
        //
        StringBuilder header = new StringBuilder();
        //
        header.append("<html>");
        header.append("<style>");
        header.append("table, th, td {");
        header.append("	border: 1px solid #D4E0EE;");
        header.append("	border-collapse: collapse;");
        header.append("	font-family: \"Trebuchet MS\", Arial, sans-serif;");
        header.append("	color: #555;");
        header.append("}");
        header.append("");
        header.append("caption {");
        header.append("	font-size: 150%;");
        header.append("	font-weight: bold;");
        header.append("	margin: 5px;");
        header.append("}");
        header.append("");
        header.append("td, th {");
        header.append("	padding: 4px;");
        header.append("}");
        header.append("");
        header.append("thead th {");
        header.append("	text-align: center;");
        header.append("	background: #E6EDF5;");
        header.append("	color: #4F76A3;");
        header.append("	font-size: 100% !important;");
        header.append("}");
        header.append("");
        header.append("tbody th {");
        header.append("	font-weight: bold;");
        header.append("}");
        header.append("");
        header.append("tbody tr { background: #FCFDFE; }");
        header.append("");
        header.append("tbody tr.odd { background: #F7F9FC; }");
        header.append("");
        header.append("table a:link {");
        header.append("	color: #718ABE;");
        header.append("	text-decoration: none;");
        header.append("}");
        header.append("");
        header.append("table a:visited {");
        header.append("	color: #718ABE;");
        header.append("	text-decoration: none;");
        header.append("}");
        header.append("");
        header.append("table a:hover {");
        header.append("	color: #718ABE;");
        header.append("	text-decoration: underline !important;");
        header.append("}");
        header.append("");
        header.append("tfoot th, tfoot td {");
        header.append("	font-size: 85%;");
        header.append("}");
        header.append("</style>");
        //
        return header.toString();
    }

    /**
     *
     * @return
     */
    private static String getFooter() {
        return "</html>";
    }
}
