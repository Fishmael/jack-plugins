package jackmodulatorgui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.nio.FloatBuffer;
import java.util.BitSet;
import java.util.Random;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.jaudiolibs.jnajack.util.SimpleAudioClient;


/**
 * JavaFX App
 */
public class App extends Application implements SimpleAudioClient.Processor {

    @Override
    public void start(Stage stage) {
        
        ToggleGroup type = new ToggleGroup();
        RadioButton mod = new RadioButton("Modulate");
        mod.setToggleGroup(type);
        RadioButton dis = new RadioButton("Disintegrator");
        dis.setToggleGroup(type);
        VBox rButtons = new VBox();
        rButtons.getChildren().add(mod);
        rButtons.getChildren().add(dis);
        GridPane gp = new GridPane();
        gp.getChildren().add(rButtons);
        Scene scene = new Scene(gp, 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void setup(float samplerate, int buffersize)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void process(FloatBuffer[] inputs, FloatBuffer[] outputs)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void shutdown()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}