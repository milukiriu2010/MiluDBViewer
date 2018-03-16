package fx.overlay;

import java.util.List;


import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import java.net.Authenticator;
import java.net.PasswordAuthentication;

// https://stackoverflow.com/questions/28152323/javafx-webview-set-proxy
// https://stackoverflow.com/questions/1626549/authenticated-http-proxy-with-java
public class DetectProxy extends Application {

    private Pane root;

    @Override
    public void start(final Stage stage) throws URISyntaxException {
        root = new VBox();

        List<Proxy> proxies = ProxySelector.getDefault().select(new URI("http://www.livedoor.com/"));
        System.out.println( "proxies.size:" + proxies.size() );
        System.out.println( "proxy:" + proxies.get(0).toString() );
        final Proxy proxy = proxies.get(0); // ignoring multiple proxies to simplify code snippet
        if (proxy.type() != Proxy.Type.DIRECT) {
            // you can change that to dialog using separate Stage
            final TextField login = new TextField("login");
            final PasswordField pwd = new PasswordField();
            Button btn = new Button("Submit");
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    System.setProperty("http.proxyUser", login.getText());
                    System.setProperty("http.proxyPassword", pwd.getText());
                    System.setProperty("https.proxyUser", login.getText());
                    System.setProperty("https.proxyPassword", pwd.getText());
                    
                    Authenticator.setDefault(new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            System.out.println("\n--------------\nProxy auth: " + login.getText());
                            return new PasswordAuthentication (login.getText(), pwd.getText().toCharArray() );
                        }
                    });
                    
                    showWebView();
                }
            });
            root.getChildren().addAll(login, pwd, btn);
        } else {
            showWebView();
        }

        stage.setScene(new Scene(root, 600, 600));
        stage.show();
    }

    private void showWebView() {
        root.getChildren().clear();
        WebView webView = new WebView();

        final WebEngine webEngine = webView.getEngine();
        root.getChildren().addAll(webView);
        webEngine.load("http://www.livedoor.com/");

    }

    public static void main(String[] args) {
		if ( args.length >= 2 )
		{
			System.setProperty( "http.proxyHost", args[0] );
			System.setProperty( "http.proxyPort", args[1] );
			System.setProperty( "https.proxyHost", args[0] );
			System.setProperty( "https.proxyPort", args[1] );
		}    	
        launch();
    }

}
