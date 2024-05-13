package entities;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import utils.MyConnection;


import java.sql.Connection;

public class Sms {

    Connection cnx;

    public Sms() {
        cnx = MyConnection.getInstance().getCnx();
    }

    // twilio.com/console
    public static final String ACCOUNT_SID = "ACb6c0929f85ac8dd6f6a515ffa44b7495";
    public static final String AUTH_TOKEN = "b325feedd3c537112bae260d910930ae";

    public static void main(String[] args) {

    }

    public static void SmsSend(String clientPhoneNumber) {

        String accountSid = ACCOUNT_SID;
        String authToken = AUTH_TOKEN;

        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message message = Message.creator(
                    new PhoneNumber("+216" + clientPhoneNumber),
                    new PhoneNumber("+1 484 528 0278"),
                    "Votre article a été ajouté avec succés!"
            ).create();

            System.out.println("SID du message : " + message.getSid());
        } catch (Exception ex) {
            System.out.println("Erreur : " + ex.getMessage());
        }
    }
}