package com.mgu.jogo.ide.editor;

import com.mgu.jogo.ide.JavaFxTurtle;
import com.mgu.jogo.ide.JogoIo;
import com.mgu.jogo.interpreter.Interpreter;
import com.mgu.jogo.interpreter.Turtle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.PlainTextChange;
import org.fxmisc.richtext.StyleSpans;
import org.reactfx.EventStream;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditorController implements Initializable {

    private final Interpreter interpreter = new Interpreter();

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @FXML
    private CodeArea codeArea;

    @FXML
    private Canvas turtleCanvas;

    public void onRunClicked(final ActionEvent actionEvent) {
        final GraphicsContext gc = this.turtleCanvas.getGraphicsContext2D();
        final Turtle turtle = new JavaFxTurtle(this.turtleCanvas.getGraphicsContext2D(),
                (int) this.turtleCanvas.getHeight(),
                (int) this.turtleCanvas.getWidth());
        final String program = this.codeArea.getText().trim();

        clearCanvas(gc);

        this.interpreter.run(turtle, program);
    }

    private void clearCanvas(final GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.clearRect(0, 0, this.turtleCanvas.getHeight(), this.turtleCanvas.getWidth());
        gc.setLineWidth(3);
    }

    public void onExitClicked(final ActionEvent actionEvent) {
        Platform.exit();
    }

    public void onLoadFileClicked(final ActionEvent actionEvent) {
        JogoIo.loadDialog().ifPresent(file -> this.codeArea.replaceText(JogoIo.load(file)));
    }

    public void onSaveFileClicked(final ActionEvent actionEvent) {
        JogoIo.saveDialog().ifPresent(file -> JogoIo.save(this.codeArea.getText(), file));
    }

    public void onAboutClicked(final ActionEvent actionEvent) {
        try {
            final Parent root = FXMLLoader.load(getClass().getResource("../about/about.fxml"));
            final Stage stage = new Stage();
            stage.setTitle("About JOGO IDE");
            stage.setScene(new Scene(root, 400, 200));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        EventStream<PlainTextChange> textChanges = codeArea.plainTextChanges();
        textChanges
                .successionEnds(Duration.ofMillis(500))
                .supplyTask(this::computeHighlightingAsync)
                .awaitLatest(textChanges)
                .subscribe(this::applyHighlighting);
        codeArea.replaceText(0, 0, StringUtils.EMPTY);
    }

    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        final String text = this.codeArea.getText();
        final Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception {
                return Highlighter.computeHighlighting(text);
            }
        };
        this.executor.execute(task);
        return task;
    }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        this.codeArea.setStyleSpans(0, highlighting);
    }

    public void stop() {
        this.executor.shutdown();
    }
}
